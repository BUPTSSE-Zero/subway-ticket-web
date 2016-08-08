package com.subwayticket.model;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class MobileToken {
    private String userId;
    private long timestamp;
    private String token;

    public MobileToken() {}

    public MobileToken(String userId) {
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
