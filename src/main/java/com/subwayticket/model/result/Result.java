package com.subwayticket.model.result;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class Result {
    private int resultCode;
    private String resultDescription;

    public Result(){}

    public Result(int resultCode, String resultDescription) {
        this.resultCode = resultCode;
        this.resultDescription = resultDescription;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }
}
