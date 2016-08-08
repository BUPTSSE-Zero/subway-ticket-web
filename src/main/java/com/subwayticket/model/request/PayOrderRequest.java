package com.subwayticket.model.request;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
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
