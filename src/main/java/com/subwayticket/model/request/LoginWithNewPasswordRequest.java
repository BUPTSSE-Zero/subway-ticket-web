package com.subwayticket.model.request;

/**
 * Created by shengyun-zhou on 6/6/16.
 */
public class LoginWithNewPasswordRequest {
    private String phoneNumber;
    private String newPassword;
    private String captcha;

    public LoginWithNewPasswordRequest() {
    }

    public LoginWithNewPasswordRequest(String phoneNumber, String newPassword, String captcha) {
        this.phoneNumber = phoneNumber;
        this.newPassword = newPassword;
        this.captcha = captcha;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
