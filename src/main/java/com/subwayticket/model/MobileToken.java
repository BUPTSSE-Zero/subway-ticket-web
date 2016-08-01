package com.subwayticket.model;

/**
 * Created by shengyun-zhou on 6/6/16.
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
