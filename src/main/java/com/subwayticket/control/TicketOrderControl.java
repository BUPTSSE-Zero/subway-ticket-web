package com.subwayticket.control;

import com.subwayticket.control.jobs.AutoCancelOrderJob;
import com.subwayticket.database.control.SubwayInfoDBHelperBean;
import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.control.TicketOrderDBHelperBean;
import com.subwayticket.database.model.*;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.request.ExtractTicketRequest;
import com.subwayticket.model.request.PayOrderRequest;
import com.subwayticket.model.request.RefundOrderRequest;
import com.subwayticket.model.request.SubmitOrderRequest;
import com.subwayticket.model.result.PayOrderResult;
import com.subwayticket.model.result.RefundOrderResult;
import com.subwayticket.model.result.Result;
import com.subwayticket.model.result.SubmitOrderResult;
import com.subwayticket.util.BundleUtil;
import com.subwayticket.util.ScheduleUtil;
import com.subwayticket.util.SecurityUtil;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;

import javax.ejb.EJBException;
import javax.servlet.ServletRequest;
import java.util.Date;

/**
 * 订单操作的底层业务逻辑
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class TicketOrderControl {
    private static Object orderGlobalLock = new Object();
    private static long timestamp = 0;
    private static int serialNumber = 0;
    public static final int MAX_TICKET_AMOUNT = 10;
    public static final int MAX_NOTPAY_MINUTES = 60;
    public static final String AUTO_CANCEL_ORDER_JOB_PREFIX = "CancelOrderJob-";

    /**
     * 提交订单
     * @param req Request对象
     * @param subwayInfoDBHelperBean 相关数据库操作EJB
     * @param user 要提交订单的用户对象
     * @param submitOrderRequest 提交订单请求
     * @return
     */
    public static Result submitOrder(ServletRequest req, SubwayInfoDBHelperBean subwayInfoDBHelperBean, Account user, SubmitOrderRequest submitOrderRequest){
        if(submitOrderRequest.getAmount() <= 0)
            return new Result(PublicResultCode.ORDER_SUBMIT_AMOUNT_ILLEGAL, BundleUtil.getString(req, "TipOrderAmountIllegal"));
        else if(submitOrderRequest.getAmount() > MAX_TICKET_AMOUNT)
            return new Result(PublicResultCode.ORDER_SUBMIT_AMOUNT_EXCESS, BundleUtil.getString(req, "TipOrderAmountExcess"));
        TicketPrice ticketPrice = subwayInfoDBHelperBean.getTicketPrice(submitOrderRequest.getStartStationId(), submitOrderRequest.getEndStationId());
        if(ticketPrice == null)
            return new Result(PublicResultCode.ORDER_SUBMIT_ROUTE_NOT_EXIST, BundleUtil.getString(req, "TipSubwayRouteNotExist"));
        Result result = null;
        try{
            String orderID;
            Date date;
            //采用同步机制产生订单号，防止两个订单产生同样的订单号
            synchronized (orderGlobalLock){
                date = new Date();
                if(date.getTime() > timestamp) {
                    timestamp = date.getTime();
                    serialNumber = 0;
                }
                orderID = "ST" + date.getTime() + String.format("%05d", serialNumber);
                serialNumber++;
            }
            TicketOrder newOrder = new TicketOrder(orderID, date, user, new SubwayStation(submitOrderRequest.getStartStationId()),
                                                   new SubwayStation(submitOrderRequest.getEndStationId()), ticketPrice.getPrice(), submitOrderRequest.getAmount());
            subwayInfoDBHelperBean.create(newOrder);
            result = new SubmitOrderResult(PublicResultCode.SUCCESS, BundleUtil.getString(req, "TipOrderSubmitSuccess"), newOrder);

            //定时60分钟后取消该订单
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(AutoCancelOrderJob.JOB_KEY_ORDER_ID, orderID);
            ScheduleUtil.addIntervalScheduleJob(AutoCancelOrderJob.class, AUTO_CANCEL_ORDER_JOB_PREFIX + orderID, dataMap, MAX_NOTPAY_MINUTES * 60, 1, false);

            //将该订单中的路线写入到历史路线记录中，每个用户最多保存3条记录
            user = (Account)subwayInfoDBHelperBean.find(Account.class, user.getPhoneNumber());
            for(HistoryRoute hr : user.getHistoryRouteList()){
                if(hr.getStartStationId() == submitOrderRequest.getStartStationId() &&
                        hr.getEndStartionId() == submitOrderRequest.getEndStationId()) {
                    hr.setAddTime(new Date());
                    subwayInfoDBHelperBean.merge(hr);
                    return result;
                }
            }
            HistoryRoute newRoute = new HistoryRoute(user.getPhoneNumber(), submitOrderRequest.getStartStationId(),
                    submitOrderRequest.getEndStationId());
            if(user.getHistoryRouteList().size() < 3){
                subwayInfoDBHelperBean.create(newRoute);
                return result;
            }
            subwayInfoDBHelperBean.remove(user.getHistoryRouteList().get(user.getHistoryRouteList().size() - 1));
            subwayInfoDBHelperBean.create(newRoute);
            subwayInfoDBHelperBean.refresh(user);
        }catch (EJBException e){
            e.printStackTrace();
            result = new Result(PublicResultCode.ORDER_SUBMIT_ERROR, BundleUtil.getString(req, "TipOrderSubmitError"));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 取消订单
     * @param req Request对象
     * @param dbHelperBean 相关数据库操作的EJB
     * @param user 要取消订单的用户对象
     * @param orderId 要取消的订单号
     * @return 取消订单结果
     */
    public static Result cancelOrder(ServletRequest req, SystemDBHelperBean dbHelperBean, Account user, String orderId){
        TicketOrder ticketOrder = (TicketOrder) dbHelperBean.find(TicketOrder.class, orderId);
        if(ticketOrder == null)
            return new Result(PublicResultCode.ORDER_NOT_EXIST, BundleUtil.getString(req, "TipOrderNotExist"));
        if(!ticketOrder.getUser().getPhoneNumber().equals(user.getPhoneNumber()))
            return new Result(PublicResultCode.ORDER_NOT_EXIST, BundleUtil.getString(req, "TipOrderNotExist"));
        if(ticketOrder.getStatus() != TicketOrder.ORDER_STATUS_NOT_PAY)
            return new Result(PublicResultCode.ORDER_CANCEL_NOT_CANCELABLE, BundleUtil.getString(req, "TipOrderNotCancelable"));
        cancelOrder(dbHelperBean, ticketOrder);
        try {
            //取消自动取消该订单的工作
            ScheduleUtil.cancelScheduleJob(AUTO_CANCEL_ORDER_JOB_PREFIX + orderId);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return new Result(PublicResultCode.SUCCESS, BundleUtil.getString(req, "TipOrderCancelSuccess"));
    }

    /**
     * 从数据库中取消订单
     * @param dbHelperBean 相关数据库操作的EJB
     * @param to 订单对象
     */
    public static void cancelOrder(SystemDBHelperBean dbHelperBean, TicketOrder to){
        dbHelperBean.remove(to);
    }

    /**
     * 支付订单
     * @param req Request对象
     * @param dbHelperBean 相关数据库操作的EJB
     * @param user 要支付订单的用户对象
     * @param payOrderRequest 支付订单的请求
     * @return 支付结果
     */
    public static Result payOrder(ServletRequest req, SystemDBHelperBean dbHelperBean, Account user, PayOrderRequest payOrderRequest){
        //TODO:调用真正的支付API
        TicketOrder ticketOrder = (TicketOrder) dbHelperBean.find(TicketOrder.class, payOrderRequest.getOrderId());
        if(ticketOrder == null)
            return new Result(PublicResultCode.ORDER_NOT_EXIST, BundleUtil.getString(req, "TipOrderNotExist"));
        if(!ticketOrder.getUser().getPhoneNumber().equals(user.getPhoneNumber()))
            return new Result(PublicResultCode.ORDER_NOT_EXIST, BundleUtil.getString(req, "TipOrderNotExist"));
        if(ticketOrder.getStatus() != TicketOrder.ORDER_STATUS_NOT_PAY)
            return new Result(PublicResultCode.ORDER_PAY_NOT_PAYABLE, BundleUtil.getString(req, "TipOrderNotPayable"));
        ticketOrder.setExtractAmount(0);
        ticketOrder.setStatus(TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET);
        ticketOrder.setExtractCode(SecurityUtil.getExtractCode(user.getPhoneNumber()));
        dbHelperBean.merge(ticketOrder);
        try {
            //取消自动取消该订单的工作
            ScheduleUtil.cancelScheduleJob(AUTO_CANCEL_ORDER_JOB_PREFIX + payOrderRequest.getOrderId());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return new PayOrderResult(PublicResultCode.SUCCESS, BundleUtil.getString(req, "TipOrderPaySuccess"), ticketOrder.getExtractCode());
    }

    /**
     * 退票
     * @param req Request对象
     * @param dbHelperBean 相关数据库操作的EJB
     * @param user 要退票的用户
     * @param refundOrderRequest 退票请求
     * @return 退票结果
     */
    public static Result refundOrder(ServletRequest req, SystemDBHelperBean dbHelperBean, Account user, RefundOrderRequest refundOrderRequest){
        //TODO:调用真正的退款API
        TicketOrder ticketOrder = (TicketOrder) dbHelperBean.find(TicketOrder.class, refundOrderRequest.getOrderId());
        if(ticketOrder == null)
            return new Result(PublicResultCode.ORDER_NOT_EXIST, BundleUtil.getString(req, "TipOrderNotExist"));
        if(!ticketOrder.getUser().getPhoneNumber().equals(user.getPhoneNumber()))
            return new Result(PublicResultCode.ORDER_NOT_EXIST, BundleUtil.getString(req, "TipOrderNotExist"));
        if(ticketOrder.getStatus() != TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET || ticketOrder.getExtractAmount() >= ticketOrder.getAmount())
            return new Result(PublicResultCode.ORDER_REFUND_NOT_REFUNDABLE, BundleUtil.getString(req, "TipOrderNotRefundable"));
        refundOrder(dbHelperBean, ticketOrder);
        return new RefundOrderResult(PublicResultCode.SUCCESS, BundleUtil.getString(req, "TipOrderRefundSuccess"),
                                     ticketOrder.getTicketPrice() * (ticketOrder.getAmount() - ticketOrder.getExtractAmount()));
    }

    /**
     * 在数据库中将订单状态修改为已退票
     * @param dbHelperBean 相关数据库操作的EJB
     * @param ticketOrder 订单对象
     */
    public static void refundOrder(SystemDBHelperBean dbHelperBean, TicketOrder ticketOrder){
        ticketOrder.setStatus(TicketOrder.ORDER_STATUS_REFUNDED);
        ticketOrder.setExtractCode(null);
        dbHelperBean.merge(ticketOrder);
    }

    /**
     * 取票
     * @param req Request对象
     * @param dbHelperBean 相关数据库操作的EJB
     * @param extractTicketRequest 取票请求
     * @return 取票结果
     */
    public static Result extractTicket(ServletRequest req, TicketOrderDBHelperBean dbHelperBean, ExtractTicketRequest extractTicketRequest){
        if(extractTicketRequest.getExtractAmount() <= 0)
            return new Result(PublicResultCode.TICKET_EXTRACT_AMOUNT_ILLEGAL, BundleUtil.getString(req, "TipExtractTicketAmountIllegal"));
        TicketOrder ticketOrder = dbHelperBean.getOrderByExtractCode(extractTicketRequest.getExtractCode());
        if(ticketOrder == null)
            return new Result(PublicResultCode.ORDER_NOT_EXIST, BundleUtil.getString(req, "TipOrderNotExist"));
        if(ticketOrder.getStatus() != TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET || ticketOrder.getAmount() == ticketOrder.getExtractAmount())
            return new Result(PublicResultCode.TICKET_EXTRACT_NOT_EXTRACTABLE, BundleUtil.getString(req, "TipExtractTicketNotExtractable"));
        if(extractTicketRequest.getExtractAmount() > ticketOrder.getAmount() - ticketOrder.getExtractAmount())
            return new Result(PublicResultCode.TICKET_EXTRACT_AMOUNT_EXCESS, BundleUtil.getString(req, "TipExtractTicketAmountExcess"));

        ticketOrder.setExtractAmount(ticketOrder.getExtractAmount() + extractTicketRequest.getExtractAmount());
        if(ticketOrder.getAmount() == ticketOrder.getExtractAmount()) {
            ticketOrder.setStatus(TicketOrder.ORDER_STATUS_FINISHED);
            ticketOrder.setExtractCode(null);
        }
        dbHelperBean.merge(ticketOrder);
        return new Result(PublicResultCode.SUCCESS, BundleUtil.getString(req, "TipExtractTicketSuccess"));
    }
}
