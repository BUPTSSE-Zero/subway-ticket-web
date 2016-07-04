package com.subwayticket.control;

import com.subwayticket.database.control.SubwayInfoDBHelperBean;
import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.model.Account;
import com.subwayticket.database.model.SubwayStation;
import com.subwayticket.database.model.TicketOrder;
import com.subwayticket.database.model.TicketPrice;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.request.SubmitOrderRequest;
import com.subwayticket.model.result.Result;
import com.subwayticket.util.BundleUtil;

import javax.ejb.EJBException;
import javax.servlet.ServletRequest;
import java.util.Date;

/**
 * Created by zhou-shengyun on 7/4/16.
 */
public class TicketOrderControl {
    private static Object orderGlobalLock = new Object();
    private static long timestamp = 0;
    private static int serialNumber = 0;

    public static Result submitOrder(ServletRequest req, SystemDBHelperBean dbHelperBean, SubwayInfoDBHelperBean subwayInfoDBHelperBean, Account user, SubmitOrderRequest submitOrderRequest){
        TicketPrice ticketPrice = subwayInfoDBHelperBean.getTicketPrice(submitOrderRequest.getStartStationID(), submitOrderRequest.getEndStationID());
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
            TicketOrder newOrder = new TicketOrder(orderID, date, user, new SubwayStation(submitOrderRequest.getStartStationID()),
                                                   new SubwayStation(submitOrderRequest.getEndStationID()), ticketPrice.getPrice(), submitOrderRequest.getAmount());
            dbHelperBean.create(newOrder);
            return new Result(PublicResultCode.SUCCESS, BundleUtil.getString(req, "TipOrderSubmitSuccess"));
        }catch (EJBException e){
            e.printStackTrace();
        }
        return new Result(PublicResultCode.ORDER_SUBMIT_ERROR, BundleUtil.getString(req, "TipOrderSubmitError"));
    }

}
