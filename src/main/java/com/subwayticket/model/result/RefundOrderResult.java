package com.subwayticket.model.result;

/**
 * Created by zhou-shengyun on 7/6/16.
 */
public class RefundOrderResult extends Result {
    private float refundAmount;

    public RefundOrderResult(int resultCode, String resultDescription, float refundAmount) {
        super(resultCode, resultDescription);
        this.refundAmount = refundAmount;
    }

    public float getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(float refundAmount) {
        this.refundAmount = refundAmount;
    }
}
