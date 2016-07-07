package com.subwayticket.model.request;

/**
 * Created by zhou-shengyun on 7/7/16.
 */
public class ExtractTicketRequest {
    private String extractCode;
    private int extractAmount;

    public ExtractTicketRequest(String extractCode, int extractAmount) {
        this.extractCode = extractCode;
        this.extractAmount = extractAmount;
    }

    public String getExtractCode() {
        return extractCode;
    }

    public void setExtractCode(String extractCode) {
        this.extractCode = extractCode;
    }

    public int getExtractAmount() {
        return extractAmount;
    }

    public void setExtractAmount(int extractAmount) {
        this.extractAmount = extractAmount;
    }
}
