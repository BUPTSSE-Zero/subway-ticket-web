package com.subwayticket.control.mobileapi.v1;

import com.subwayticket.control.AccountControl;
import com.subwayticket.control.mobileapi.CheckException;
import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.model.Account;
import com.subwayticket.database.model.SystemAccount;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.request.LoginRequest;
import com.subwayticket.model.result.MobileLoginResult;
import com.subwayticket.model.result.Result;
import com.subwayticket.util.BundleUtil;
import com.subwayticket.util.JedisUtil;
import com.subwayticket.util.SecurityUtil;
import redis.clients.jedis.Jedis;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * RESTful API-系统管理员账户相关
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */

@Path("/v1/system_account")
public class SystemAccountResource {
    @Context
    private HttpServletRequest req;
    @Context
    private HttpServletResponse response;
    @EJB
    private SystemDBHelperBean dbBean;


    public static SystemAccount authCheck(HttpServletRequest request){
        String userId = SecurityUtil.checkMobileToken(request.getHeader(SecurityUtil.HEADER_TOKEN_KEY), JedisUtil.getJedis());
        if(userId == null)
            throw new CheckException(Response.Status.UNAUTHORIZED.getStatusCode(), new Result(Response.Status.UNAUTHORIZED.getStatusCode(),
                    BundleUtil.getString(request, "TipTokenInvalid")));
        return new SystemAccount(userId);
    }

    @PUT
    @Path("/login")
    @Consumes("application/json")
    public Response login(LoginRequest loginRequest){
        SystemAccount account = (SystemAccount) dbBean.find(SystemAccount.class, loginRequest.getUserId());
        if(account == null)
            throw new CheckException(new Result(PublicResultCode.USER_NOT_EXIST, BundleUtil.getString(req, "TipUserNotExist")));
        if(!account.getPassword().equals(loginRequest.getPassword()))
            throw new CheckException(new Result(PublicResultCode.PASSWORD_INCORRECT, BundleUtil.getString(req, "TipPasswordIncorrect")));
        MobileLoginResult mobileLoginResult = new MobileLoginResult(new Result(PublicResultCode.SUCCESS, BundleUtil.getString(req, "TipLoginSuccess")));
        Jedis jedis = JedisUtil.getJedis();
        mobileLoginResult.setToken(SecurityUtil.getMobileToken(loginRequest.getUserId()));
        jedis.setex(SecurityUtil.REDIS_KEY_MOBILETOKEN_PREFIX + loginRequest.getUserId(), SecurityUtil.MOBILETOKEN_VALID_HOURS * 3600,
                mobileLoginResult.getToken());
        return Response.status(Response.Status.CREATED).entity(mobileLoginResult).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
