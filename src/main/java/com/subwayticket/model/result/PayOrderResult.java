package com.subwayticket.model.result;

/**
 * Created by zhou-shengyun on 7/7/16.
 */
public class PayOrderResult extends Result {
    private String extractCode;

    public PayOrderResult(int resultCode, String resultDescription, String extractCode) {
        super(resultCode, resultDescription);
        this.extractCode = extractCode;
    }

    public String getExtractCode() {
        return extractCode;
    }

    public void setExtractCode(String extractCode) {
        this.extractCode = extractCode;
    }
}
