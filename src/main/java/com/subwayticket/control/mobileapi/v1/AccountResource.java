package com.subwayticket.control.mobileapi.v1;

import com.subwayticket.control.AccountControl;
import com.subwayticket.control.mobileapi.CheckException;
import com.subwayticket.database.control.SubwayTicketDBHelperBean;
import com.subwayticket.database.model.Account;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.request.*;
import com.subwayticket.model.result.Result;
import com.subwayticket.util.BundleUtil;
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
public class AccountResource {
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

    @PUT
    @Path("/login")
    @Consumes("application/json")
    public Response login(LoginRequest loginRequest){
        if(loginRequest.getPhoneNumber() == null || loginRequest.getPassword() == null)
            throw new BadRequestException();
        Result result = AccountControl.mobileLogin(request, loginRequest, dbBean, JedisUtil.getJedis());
        if(result.getResultCode() != PublicResultCode.SUCCESS && result.getResultCode() != PublicResultCode.LOGIN_SUCCESS_WITH_PRE_OFFLINE)
            throw new CheckException(Response.Status.UNAUTHORIZED.getStatusCode(), result);
        return Response.status(Response.Status.CREATED).entity(result).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public static Account authCheck(HttpServletRequest request){
        Account account = SecurityUtil.checkMobileToken(request.getHeader(SecurityUtil.HEADER_TOKEN_KEY), JedisUtil.getJedis());
        if(account == null)
            throw new CheckException(Response.Status.UNAUTHORIZED.getStatusCode(), new Result(Response.Status.UNAUTHORIZED.getStatusCode(),
                                        BundleUtil.getString(request, "TipTokenInvalid")));
        return account;
    }

    @PUT
    @Path("/modify_password")
    @Consumes("application/json")
    public Response modifyPassword(ModifyPasswordRequest modifyReq){
        if(modifyReq.getOldPassword() == null || modifyReq.getNewPassword() == null)
            throw new BadRequestException();
        Account account = authCheck(request);
        account = (Account) dbBean.find(Account.class, account.getPhoneNumber());
        Result result = AccountControl.mobileModifyPassword(request, modifyReq, account, dbBean);
        if(result.getResultCode() == PublicResultCode.PASSWORD_INCORRECT)
            throw new CheckException(Response.Status.UNAUTHORIZED.getStatusCode(), result);
        else if(result.getResultCode() != PublicResultCode.SUCCESS)
            throw new CheckException(result);
        return Response.status(Response.Status.CREATED).entity(result).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @PUT
    @Path("/reset_password")
    @Consumes("application/json")
    public Response resetPassword(ResetPasswordRequest resetRequest){
        if(resetRequest.getPhoneNumber() == null || resetRequest.getNewPassword() == null || resetRequest.getCaptcha() == null)
            throw new BadRequestException();
        Result result = AccountControl.resetPassword(request, resetRequest, dbBean, JedisUtil.getJedis());
        if(result.getResultCode() == PublicResultCode.USER_NOT_EXIST)
            throw new CheckException(Response.Status.UNAUTHORIZED.getStatusCode(), result);
        else if(result.getResultCode() != PublicResultCode.SUCCESS)
            throw new CheckException(result);
        return Response.status(Response.Status.CREATED).entity(result).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @PUT
    @Path("/logout")
    public Response logout(){
        Account account = authCheck(request);
        AccountControl.mobileLogout(account, JedisUtil.getJedis());
        return Response.noContent().build();
    }
}
