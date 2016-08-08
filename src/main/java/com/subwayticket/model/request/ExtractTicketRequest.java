package com.subwayticket.model.request;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
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
