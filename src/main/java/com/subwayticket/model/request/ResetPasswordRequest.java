package com.subwayticket.model.request;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class ResetPasswordRequest {
    private String phoneNumber;
    private String newPassword;
    private String captcha;

    public ResetPasswordRequest() {
    }

    public ResetPasswordRequest(String phoneNumber, String newPassword, String captcha) {
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
