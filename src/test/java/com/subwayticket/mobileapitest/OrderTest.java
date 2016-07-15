package com.subwayticket.mobileapitest;

import com.subwayticket.database.model.TicketOrder;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.request.*;
import com.subwayticket.model.result.*;

import javax.ws.rs.core.Response;
import java.util.Scanner;

/**
 * Created by zhou-shengyun on 7/4/16.
 */
public class OrderTest {
    public static void main(String[] argv) {
        System.out.println("****Order Test****");
        Scanner reader = new Scanner(System.in);
        String phoneNumber = reader.next();
        String password = reader.next();
        LoginRequest loginRequest = new LoginRequest(phoneNumber, password);
        Response response = RESTServiceTestUtil.put(RESTServiceTestUtil.API_BASE_URL_V1 + "/account/login", loginRequest, null);
        MobileLoginResult result = (MobileLoginResult) RESTServiceTestUtil.showResponse(response, MobileLoginResult.class);
        if(result.getToken() != null)
            System.out.println("Token:" + result.getToken());
        else
            return;

        int startStationID = reader.nextInt();
        int endStationID = reader.nextInt();
        int amount = reader.nextInt();
        SubmitOrderRequest submitOrderRequest = new SubmitOrderRequest(startStationID, endStationID, amount);
        response = RESTServiceTestUtil.post(RESTServiceTestUtil.API_BASE_URL_V1 + "/ticket_order/submit", submitOrderRequest,
                result.getToken());
        SubmitOrderResult submitOrderResult = (SubmitOrderResult) RESTServiceTestUtil.showResponse(response, SubmitOrderResult.class);
        showTicketOrderInfo(submitOrderResult.getTicketOrder());

        response = RESTServiceTestUtil.delete(RESTServiceTestUtil.API_BASE_URL_V1 + "/ticket_order/cancel/" + submitOrderResult.getTicketOrder().getTicketOrderId(),
                result.getToken());
        RESTServiceTestUtil.showResponse(response);

        response = RESTServiceTestUtil.post(RESTServiceTestUtil.API_BASE_URL_V1 + "/ticket_order/submit", submitOrderRequest,
                result.getToken());
        submitOrderResult = (SubmitOrderResult) RESTServiceTestUtil.showResponse(response, SubmitOrderResult.class);
        showTicketOrderInfo(submitOrderResult.getTicketOrder());

        PayOrderRequest payOrderRequest = new PayOrderRequest(submitOrderResult.getTicketOrder().getTicketOrderId());
        response = RESTServiceTestUtil.put(RESTServiceTestUtil.API_BASE_URL_V1 + "/ticket_order/pay", payOrderRequest,
                result.getToken());
        PayOrderResult payOrderResult = (PayOrderResult) RESTServiceTestUtil.showResponse(response, PayOrderResult.class);
        if(payOrderResult.getResultCode() == PublicResultCode.SUCCESS){
            System.out.println("Extract Code:" + payOrderResult.getExtractCode());
        }

        ExtractTicketRequest extractTicketRequest = new ExtractTicketRequest(payOrderResult.getExtractCode(), amount);
        response = RESTServiceTestUtil.put(RESTServiceTestUtil.API_BASE_URL_V1 + "/ticket_order/extract_ticket", extractTicketRequest, null);
        RESTServiceTestUtil.showResponse(response);

        /*RefundOrderRequest refundOrderRequest = new RefundOrderRequest(orderID);
        response = RESTServiceTestUtil.put(RESTServiceTestUtil.API_BASE_URL_V1 + "/ticket_order/refund", refundOrderRequest,
                result.getToken());
        RefundOrderResult refundOrderResult = (RefundOrderResult) RESTServiceTestUtil.showResponse(response, RefundOrderResult.class);
        if(refundOrderResult.getResultCode() == PublicResultCode.SUCCESS)
            System.out.println("Refund Amount:" + refundOrderResult.getRefundAmount());*/

        response = RESTServiceTestUtil.get(RESTServiceTestUtil.API_BASE_URL_V1 + "/ticket_order/order_info/" + submitOrderResult.getTicketOrder().getTicketOrderId(), result.getToken());
        OrderInfoResult orderInfoResult = (OrderInfoResult) RESTServiceTestUtil.showResponse(response, OrderInfoResult.class);
        showTicketOrderInfo(orderInfoResult.getTicketOrder());

        String todayStr = Long.toString(System.currentTimeMillis());
        response = RESTServiceTestUtil.get(RESTServiceTestUtil.API_BASE_URL_V1 + "/ticket_order/order_list/" + TicketOrder.ORDER_STATUS_FINISHED + '/' + todayStr + '/' + todayStr, result.getToken());
        OrderListResult orderListResult = (OrderListResult)RESTServiceTestUtil.showResponse(response, OrderListResult.class);
        System.out.println("\nFinished Order List:");
        for(TicketOrder t : orderListResult.getTicketOrderList()){
            showTicketOrderInfo(t);
        }

        response = RESTServiceTestUtil.get(RESTServiceTestUtil.API_BASE_URL_V1 + "/ticket_order/order_list/" + todayStr + '/' + todayStr, result.getToken());
        orderListResult = (OrderListResult)RESTServiceTestUtil.showResponse(response, OrderListResult.class);
        System.out.println("\nAll Order:");
        for(TicketOrder t : orderListResult.getTicketOrderList()){
            showTicketOrderInfo(t);
        }

        response = RESTServiceTestUtil.put(RESTServiceTestUtil.API_BASE_URL_V1 + "/account/logout", null,
                result.getToken());
        RESTServiceTestUtil.showResponse(response);
    }

    private static void showTicketOrderInfo(TicketOrder ticketOrder){
        if(ticketOrder == null)
            return;
        System.out.println("--------Ticket Order Info--------");
        System.out.println("Order ID:" + ticketOrder.getTicketOrderId());
        System.out.println("Start Station:" + ticketOrder.getStartStation().getSubwayLine().getCity().getCityName() + ' ' +
                ticketOrder.getStartStation().getSubwayLine().getSubwayLineName() + '-' +
                ticketOrder.getStartStation().getSubwayStationName());
        System.out.println("End Station:" + ticketOrder.getEndStation().getSubwayLine().getCity().getCityName() + ' ' +
                ticketOrder.getEndStation().getSubwayLine().getSubwayLineName() + '-' +
                ticketOrder.getEndStation().getSubwayStationName());
        System.out.println("Order Time:" + ticketOrder.getTicketOrderTime().toString());
        System.out.println("Amount:" + ticketOrder.getAmount());
        System.out.println("Draw Amount:" + ticketOrder.getExtractAmount());
        System.out.print("Order Status:");
        switch (ticketOrder.getStatus()){
            case TicketOrder.ORDER_STATUS_NOT_PAY:
                System.out.println("not pay");
                break;
            case TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET:
                System.out.println("not draw tickets");
                break;
            case TicketOrder.ORDER_STATUS_FINISHED:
                System.out.println("finished");
                break;
            case TicketOrder.ORDER_STATUS_REFUNDED:
                System.out.println("refunded");
        }
        if(ticketOrder.getExtractCode() != null){
            System.out.println("Extract Code:" + ticketOrder.getExtractCode());
        }
    }
}
