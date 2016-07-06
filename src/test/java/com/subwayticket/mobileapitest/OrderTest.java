package com.subwayticket.mobileapitest;

import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.request.*;
import com.subwayticket.model.result.MobileLoginResult;
import com.subwayticket.model.result.RefundOrderResult;
import com.subwayticket.model.result.SubmitOrderResult;

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
        if(submitOrderResult.getTicketOrder() != null){
            System.out.println("Order ID:" + submitOrderResult.getTicketOrder().getTicketOrderId());
            System.out.println("Start Station:" + submitOrderResult.getTicketOrder().getStartStation().getSubwayStationName());
            System.out.println("End Station:" + submitOrderResult.getTicketOrder().getEndStation().getSubwayStationName());
        }

        String orderID = reader.next();
        response = RESTServiceTestUtil.delete(RESTServiceTestUtil.API_BASE_URL_V1 + "/ticket_order/cancel/" + orderID,
                result.getToken());
        RESTServiceTestUtil.showResponse(response);

        response = RESTServiceTestUtil.post(RESTServiceTestUtil.API_BASE_URL_V1 + "/ticket_order/submit", submitOrderRequest,
                result.getToken());
        submitOrderResult = (SubmitOrderResult) RESTServiceTestUtil.showResponse(response, SubmitOrderResult.class);
        if(submitOrderResult.getTicketOrder() != null){
            System.out.println("Order ID:" + submitOrderResult.getTicketOrder().getTicketOrderId());
            System.out.println("Start Station:" + submitOrderResult.getTicketOrder().getStartStation().getSubwayStationName());
            System.out.println("End Station:" + submitOrderResult.getTicketOrder().getEndStation().getSubwayStationName());
        }

        orderID = reader.next();
        PayOrderRequest payOrderRequest = new PayOrderRequest(orderID);
        response = RESTServiceTestUtil.put(RESTServiceTestUtil.API_BASE_URL_V1 + "/ticket_order/pay", payOrderRequest,
                result.getToken());
        RESTServiceTestUtil.showResponse(response);

        RefundOrderRequest refundOrderRequest = new RefundOrderRequest(orderID);
        response = RESTServiceTestUtil.put(RESTServiceTestUtil.API_BASE_URL_V1 + "/ticket_order/refund", refundOrderRequest,
                result.getToken());
        RefundOrderResult refundOrderResult = (RefundOrderResult) RESTServiceTestUtil.showResponse(response, RefundOrderResult.class);
        if(refundOrderResult.getResultCode() == PublicResultCode.SUCCESS)
            System.out.println("Refund Amount:" + refundOrderResult.getRefundAmount());

        response = RESTServiceTestUtil.put(RESTServiceTestUtil.API_BASE_URL_V1 + "/account/logout", null,
                result.getToken());
        RESTServiceTestUtil.showResponse(response);
    }
}
