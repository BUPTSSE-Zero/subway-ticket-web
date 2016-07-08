package com.subwayticket.model.request;

/**
 * Created by zhou-shengyun on 7/7/16.
 */
public class ExtractTicketRequest {
    private String extractCode;
    private Integer extractAmount;

    public ExtractTicketRequest(String extractCode, Integer extractAmount) {
        this.extractCode = extractCode;
        this.extractAmount = extractAmount;
    }

    public String getExtractCode() {
        return extractCode;
    }

    public void setExtractCode(String extractCode) {
        this.extractCode = extractCode;
    }

    public Integer getExtractAmount() {
        return extractAmount;
    }

    public void setExtractAmount(Integer extractAmount) {
        this.extractAmount = extractAmount;
    }
}
