package com.subwayticket.control.mobileapi.v1;

import com.subwayticket.control.AccountControl;
import com.subwayticket.control.mobileapi.CheckException;
import com.subwayticket.database.control.SystemDBHelperBean;
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
    private SystemDBHelperBean dbBean;


    @POST
    @Path("/register")
    @Consumes("application/json")
    public Response register(RegisterRequest regReq){
        Result result = AccountControl.register(request, regReq, dbBean, JedisUtil.getJedis());
        if(result.getResultCode() != PublicResultCode.SUCCESS)
            throw new CheckException(result);
        return Response.status(Response.Status.CREATED).entity(result).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @PUT
    @Path("/phone_captcha")
    @Consumes("application/json")
    public Response phoneCaptcha(PhoneCaptchaRequest pcReq){
        Result result = SecurityUtil.sendPhoneCaptcha(request, pcReq.getPhoneNumber(), JedisUtil.getJedis());
        if(result.getResultCode() != PublicResultCode.SUCCESS)
            throw new CheckException(result);
        return Response.status(Response.Status.CREATED).entity(result).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @PUT
    @Path("/login")
    @Consumes("application/json")
    public Response login(LoginRequest loginRequest){
        Result result = AccountControl.mobileLogin(request, loginRequest, dbBean, JedisUtil.getJedis());
        if(result.getResultCode() != PublicResultCode.SUCCESS && result.getResultCode() != PublicResultCode.LOGIN_SUCCESS_WITH_PRE_OFFLINE)
            throw new CheckException(Response.Status.UNAUTHORIZED.getStatusCode(), result);
        return Response.status(Response.Status.CREATED).entity(result).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public static Account authCheck(HttpServletRequest request){
        String userId = SecurityUtil.checkMobileToken(request.getHeader(SecurityUtil.HEADER_TOKEN_KEY), JedisUtil.getJedis());
        if(userId == null)
            throw new CheckException(Response.Status.UNAUTHORIZED.getStatusCode(), new Result(Response.Status.UNAUTHORIZED.getStatusCode(),
                                        BundleUtil.getString(request, "TipTokenInvalid")));
        return new Account(userId);
    }

    @GET
    @Path("/check_login")
    public Response checkLogin(){
        authCheck(request);
        return Response.noContent().build();
    }

    @PUT
    @Path("/modify_password")
    @Consumes("application/json")
    @Produces("application/json")
    public Result modifyPassword(ModifyPasswordRequest modifyReq){
        Account account = authCheck(request);
        account = (Account) dbBean.find(Account.class, account.getPhoneNumber());
        Result result = AccountControl.mobileModifyPassword(request, modifyReq, account, dbBean);
        if(result.getResultCode() == PublicResultCode.PASSWORD_INCORRECT)
            throw new CheckException(Response.Status.UNAUTHORIZED.getStatusCode(), result);
        else if(result.getResultCode() != PublicResultCode.SUCCESS)
            throw new CheckException(result);
        return result;
    }

    @PUT
    @Path("/reset_password")
    @Consumes("application/json")
    @Produces("application/json")
    public Result resetPassword(ResetPasswordRequest resetRequest){
        Result result = AccountControl.resetPassword(request, resetRequest, dbBean, JedisUtil.getJedis());
        if(result.getResultCode() == PublicResultCode.USER_NOT_EXIST)
            throw new CheckException(Response.Status.UNAUTHORIZED.getStatusCode(), result);
        else if(result.getResultCode() != PublicResultCode.SUCCESS)
            throw new CheckException(result);
        return result;
    }

    @PUT
    @Path("/logout")
    public Response logout(){
        Account account = authCheck(request);
        AccountControl.mobileLogout(account, JedisUtil.getJedis());
        return Response.noContent().build();
    }
}
