package com.subwayticket.control;

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
import com.subwayticket.util.SecurityUtil;

import javax.ejb.EJBException;
import javax.servlet.ServletRequest;
import java.util.Collections;
import java.util.Date;

/**
 * Created by zhou-shengyun on 7/4/16.
 */
public class TicketOrderControl {
    private static Object orderGlobalLock = new Object();
    private static long timestamp = 0;
    private static int serialNumber = 0;
    public static final int MAX_TICKET_AMOUNT = 10;

    public static Result submitOrder(ServletRequest req, SubwayInfoDBHelperBean subwayInfoDBHelperBean, Account user, SubmitOrderRequest submitOrderRequest){
        if(submitOrderRequest.getAmount() <= 0)
            return new Result(PublicResultCode.ORDER_SUBMIT_AMOUNT_ILLEGAL, BundleUtil.getString(req, "TipOrderAmountIllegal"));
        else if(submitOrderRequest.getAmount() > MAX_TICKET_AMOUNT)
            return new Result(PublicResultCode.ORDER_SUBMIT_AMOUNT_EXCESS, BundleUtil.getString(req, "TipOrderAmountExcess"));
        TicketPrice ticketPrice = subwayInfoDBHelperBean.getTicketPrice(submitOrderRequest.getStartStationId(), submitOrderRequest.getEndStationId());
        if(ticketPrice == null)
            return new Result(PublicResultCode.ORDER_SUBMIT_ROUTE_NOT_EXIST, BundleUtil.getString(req, "TipSubwayRouteNotExist"));
        try{
            String orderID;
            Date date;
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
            subwayInfoDBHelperBean.refresh(newOrder);
            SubmitOrderResult result = new SubmitOrderResult(PublicResultCode.SUCCESS, BundleUtil.getString(req, "TipOrderSubmitSuccess"), newOrder);
            user = (Account)subwayInfoDBHelperBean.find(Account.class, user.getPhoneNumber());
            for(HistoryRoute hr : user.getHistoryRouteList()){
                if(hr.getStartStationId() == submitOrderRequest.getStartStationId() &&
                        hr.getEndStartionId() == submitOrderRequest.getEndStationId()) {
                    hr.setAddTime(new Date());
                    subwayInfoDBHelperBean.merge(hr);
                    return result;
                }
            }
            if(user.getHistoryRouteList().size() < 3){
                subwayInfoDBHelperBean.create(new HistoryRoute(user.getPhoneNumber(), submitOrderRequest.getStartStationId(),
                        submitOrderRequest.getEndStationId()));
                return result;
            }
            subwayInfoDBHelperBean.remove(user.getHistoryRouteList().get(user.getHistoryRouteList().size() - 1));
            subwayInfoDBHelperBean.create(new HistoryRoute(user.getPhoneNumber(), submitOrderRequest.getStartStationId(),
                    submitOrderRequest.getEndStationId()));
            subwayInfoDBHelperBean.refresh(user);
            return result;
        }catch (EJBException e){
            e.printStackTrace();
        }
        return new Result(PublicResultCode.ORDER_SUBMIT_ERROR, BundleUtil.getString(req, "TipOrderSubmitError"));
    }


    public static Result cancelOrder(ServletRequest req, SystemDBHelperBean dbHelperBean, Account user, String orderId){
        TicketOrder ticketOrder = (TicketOrder) dbHelperBean.find(TicketOrder.class, orderId);
        if(ticketOrder == null)
            return new Result(PublicResultCode.ORDER_NOT_EXIST, BundleUtil.getString(req, "TipOrderNotExist"));
        if(!ticketOrder.getUser().getPhoneNumber().equals(user.getPhoneNumber()))
            return new Result(PublicResultCode.ORDER_NOT_EXIST, BundleUtil.getString(req, "TipOrderNotExist"));
        if(ticketOrder.getStatus() != TicketOrder.ORDER_STATUS_NOT_PAY)
            return new Result(PublicResultCode.ORDER_CANCEL_NOT_CANCELABLE, BundleUtil.getString(req, "TipOrderNotCancelable"));
        dbHelperBean.remove(ticketOrder);
        return new Result(PublicResultCode.SUCCESS, BundleUtil.getString(req, "TipOrderCancelSuccess"));
    }

    public static Result payOrder(ServletRequest req, SystemDBHelperBean dbHelperBean, Account user, PayOrderRequest payOrderRequest){
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
        return new PayOrderResult(PublicResultCode.SUCCESS, BundleUtil.getString(req, "TipOrderPaySuccess"), ticketOrder.getExtractCode());
    }

    public static Result refundOrder(ServletRequest req, SystemDBHelperBean dbHelperBean, Account user, RefundOrderRequest refundOrderRequest){
        TicketOrder ticketOrder = (TicketOrder) dbHelperBean.find(TicketOrder.class, refundOrderRequest.getOrderId());
        if(ticketOrder == null)
            return new Result(PublicResultCode.ORDER_NOT_EXIST, BundleUtil.getString(req, "TipOrderNotExist"));
        if(!ticketOrder.getUser().getPhoneNumber().equals(user.getPhoneNumber()))
            return new Result(PublicResultCode.ORDER_NOT_EXIST, BundleUtil.getString(req, "TipOrderNotExist"));
        if(ticketOrder.getStatus() != TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET || ticketOrder.getExtractAmount() >= ticketOrder.getAmount())
            return new Result(PublicResultCode.ORDER_REFUND_NOT_REFUNDABLE, BundleUtil.getString(req, "TipOrderNotRefundable"));
        ticketOrder.setStatus(TicketOrder.ORDER_STATUS_REFUNDED);
        ticketOrder.setExtractCode(null);
        dbHelperBean.merge(ticketOrder);
        return new RefundOrderResult(PublicResultCode.SUCCESS, BundleUtil.getString(req, "TipOrderRefundSuccess"),
                                     ticketOrder.getTicketPrice() * (ticketOrder.getAmount() - ticketOrder.getExtractAmount()));
    }

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
