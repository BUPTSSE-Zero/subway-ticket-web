package com.subwayticket.model;

/**
 * Created by shengyun-zhou on 6/6/16.
 */
public class MobileToken {
    private String phoneNumber;
    private long timestamp;
    private String token;

    public MobileToken() {}

    public MobileToken(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.timestamp = System.currentTimeMillis();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
