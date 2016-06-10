package com.subwayticket.control.mobileapi.v1;

import com.subwayticket.control.AccountControl;
import com.subwayticket.control.mobileapi.CheckException;
import com.subwayticket.database.control.SubwayTicketDBHelperBean;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.request.PhoneCaptchaRequest;
import com.subwayticket.model.request.RegisterRequest;
import com.subwayticket.model.result.Result;
import com.subwayticket.util.JedisUtil;
import com.subwayticket.util.SecurityUtil;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by shengyun-zhou on 6/9/16.
 */

@Path("/v1/account")
public class Account {
    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;
    @EJB
    private SubwayTicketDBHelperBean dbBean;


    @POST
    @Path("/register")
    @Consumes("application/json")
    public Response register(RegisterRequest regReq){
        if(regReq.getPassword() == null || regReq.getPhoneNumber() == null || regReq.getCaptcha() == null)
            throw new BadRequestException();
        Result result = AccountControl.register(request, regReq, dbBean, JedisUtil.getJedis());
        if(result.getResultCode() != PublicResultCode.SUCCESS)
            throw new CheckException(result);
        return Response.status(Response.Status.CREATED).entity(result).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @PUT
    @Path("/phone_captcha")
    @Consumes("application/json")
    public Response phoneCaptcha(PhoneCaptchaRequest pcReq){
        if(pcReq.getPhoneNumber() == null)
            throw new BadRequestException();
        Result result = SecurityUtil.sendPhoneCaptcha(request, pcReq.getPhoneNumber(), JedisUtil.getJedis());
        if(result.getResultCode() != PublicResultCode.SUCCESS)
            throw new CheckException(result);
        return Response.status(Response.Status.CREATED).entity(result).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
