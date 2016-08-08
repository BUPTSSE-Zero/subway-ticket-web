package com.subwayticket.model.result;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
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
