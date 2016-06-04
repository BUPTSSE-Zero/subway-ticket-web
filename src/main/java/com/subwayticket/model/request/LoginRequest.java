package com.subwayticket.model.request;

/**
 * Created by shengyun-zhou on 6/2/16.
 */
public class LoginRequest {
    private String phoneNumber;
    private String password;

    public LoginRequest(){}

    public LoginRequest(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
