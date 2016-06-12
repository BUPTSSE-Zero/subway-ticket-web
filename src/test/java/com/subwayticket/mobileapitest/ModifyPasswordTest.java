package com.subwayticket.mobileapitest;

import com.subwayticket.model.request.LoginRequest;
import com.subwayticket.model.request.ModifyPasswordRequest;
import com.subwayticket.model.result.MobileLoginResult;

import javax.ws.rs.core.Response;
import java.util.Scanner;

/**
 * Created by shengyun-zhou on 6/12/16.
 */
public class ModifyPasswordTest {
    public static void main(String[] argv) {
        System.out.println("****Modify Password Test****");
        Scanner reader = new Scanner(System.in);
        String phoneNumber = reader.next();
        String password = reader.next();
        LoginRequest loginRequest = new LoginRequest(phoneNumber, password);
        Response response = RESTServiceTestUtil.put(RESTServiceTestUtil.API_BASE_URL_V1 + "/account/login", loginRequest, null);
        MobileLoginResult result = (MobileLoginResult) RESTServiceTestUtil.showResponse(response, MobileLoginResult.class);
        if(result.getToken() != null)
            System.out.println("Token:" + result.getToken());
        else
            return;
        String newPassword = reader.next();
        ModifyPasswordRequest modifyPasswordRequest = new ModifyPasswordRequest(password, newPassword);
        response = RESTServiceTestUtil.put(RESTServiceTestUtil.API_BASE_URL_V1 + "/account/modify_password", modifyPasswordRequest,
                                           result.getToken());
        RESTServiceTestUtil.showResponse(response);
    }
}
