package com.subwayticket.mobileapitest;

import com.subwayticket.model.request.PhoneCaptchaRequest;
import com.subwayticket.model.request.RegisterRequest;
import javax.ws.rs.core.Response;
import java.util.Scanner;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class RegisterTest {
    public static void main(String[] argv) {
        System.out.println("****Register Test****");
        Scanner reader = new Scanner(System.in);
        String phoneNumber = reader.next();
        Response response = RESTServiceTestUtil.put(RESTServiceTestUtil.API_BASE_URL_V1 + "/account/phone_captcha",
                            new PhoneCaptchaRequest(phoneNumber), null);
        RESTServiceTestUtil.showResponse(response);
        String password = reader.next();
        String captcha = reader.next();
        RegisterRequest regReq = new RegisterRequest(phoneNumber, password, captcha);
        response = RESTServiceTestUtil.post(RESTServiceTestUtil.API_BASE_URL_V1 + "/account/register", regReq, null);
        RESTServiceTestUtil.showResponse(response);
    }
}
