package com.subwayticket.model.result;

/**
 * Created by shengyun-zhou on 6/6/16.
 */
public class MobileLoginResult extends Result {
    private String token;

    public MobileLoginResult(Result result){
        setResultCode(result.getResultCode());
        setResultDescription(result.getResultDescription());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
