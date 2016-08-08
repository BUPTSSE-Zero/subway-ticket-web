package com.subwayticket.model.result;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
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
