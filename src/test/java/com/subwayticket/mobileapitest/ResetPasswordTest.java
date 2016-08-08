package com.subwayticket.mobileapitest;

import com.subwayticket.model.request.PhoneCaptchaRequest;
import com.subwayticket.model.request.ResetPasswordRequest;

import javax.ws.rs.core.Response;
import java.util.Scanner;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class ResetPasswordTest {
    public static void main(String[] argv) {
        System.out.println("****Reset Password Test****");
        Scanner reader = new Scanner(System.in);
        String phoneNumber = reader.next();
        Response response = RESTServiceTestUtil.put(RESTServiceTestUtil.API_BASE_URL_V1 + "/account/phone_captcha",
                new PhoneCaptchaRequest(phoneNumber), null);
        RESTServiceTestUtil.showResponse(response);
        String newPassword = reader.next();
        String captcha = reader.next();
        response = RESTServiceTestUtil.put(RESTServiceTestUtil.API_BASE_URL_V1 + "/account/reset_password",
                new ResetPasswordRequest(phoneNumber, newPassword, captcha), null);
        RESTServiceTestUtil.showResponse(response);
    }
}
