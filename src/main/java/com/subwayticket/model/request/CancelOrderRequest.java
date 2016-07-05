package com.subwayticket.model.request;

/**
 * Created by zhou-shengyun on 7/5/16.
 */
public class CancelOrderRequest {
    private String orderID;

    public CancelOrderRequest(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
