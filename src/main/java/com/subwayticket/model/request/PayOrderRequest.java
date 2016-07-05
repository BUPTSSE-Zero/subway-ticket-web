package com.subwayticket.model.request;

/**
 * Created by zhou-shengyun on 7/5/16.
 */
public class PayOrderRequest {
    private String orderId;

    public PayOrderRequest(String orderID) {
        this.orderId = orderID;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
