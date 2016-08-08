package com.subwayticket.control.mobileapi;

import com.subwayticket.model.result.Result;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class MobileAPIBaseException extends RuntimeException {
    private int statusCode;
    private Result errorResult;

    public MobileAPIBaseException(int statusCode, Result errorResult){
        super(errorResult.getResultDescription());
        this.statusCode = statusCode;
        this.errorResult = errorResult;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Result getErrorResult() {
        return errorResult;
    }

    public void setErrorResult(Result errorResult) {
        this.errorResult = errorResult;
    }
}
