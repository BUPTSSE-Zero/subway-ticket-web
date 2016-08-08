package com.subwayticket.model.request;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class RegisterRequest {
    private String phoneNumber;
    private String password;
    private String captcha;

    public RegisterRequest(){}

    public RegisterRequest(String phoneNumber, String password, String captcha) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.captcha = captcha;
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

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
