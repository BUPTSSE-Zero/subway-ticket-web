package com.subwayticket.model.result;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
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
