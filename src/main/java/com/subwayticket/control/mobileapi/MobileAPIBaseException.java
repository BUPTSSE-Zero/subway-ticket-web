package com.subwayticket.control.mobileapi;

import com.subwayticket.model.result.Result;

/**
 * Created by shengyun-zhou on 6/10/16.
 */
public class MobileAPIBaseException extends RuntimeException {
    private int statusCode;
    private Result errorResult;

    public MobileAPIBaseException(int statusCode, Result errorResult){
        super("Mobile API Exception");
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
