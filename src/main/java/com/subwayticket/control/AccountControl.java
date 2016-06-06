package com.subwayticket.control;

import com.subwayticket.database.control.SubwayTicketDBHelperBean;
import com.subwayticket.database.model.Account;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.request.LoginRequest;
import com.subwayticket.model.request.LoginWithNewPasswordRequest;
import com.subwayticket.model.request.RegisterRequest;
import com.subwayticket.model.result.Result;
import com.subwayticket.util.BundleUtil;
import com.subwayticket.util.SecurityUtil;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */
public class AccountControl {
    public static final int REGISTER_PHONE_NUM_EMPTY = 100;
    public static final int REGISTER_PHONE_NUM_REGISTERED = 101;
    public static final int REGISTER_PASSWORD_LENGTH_ILLEGAL = 200;
    public static final int REGISTER_PASSWORD_FORMAT_ILLEGAL = 201;
    public static final int REGISTER_CAPTCHA_INCORRECT = 300;

    public static Result register(ServletRequest req, RegisterRequest regReq, SubwayTicketDBHelperBean dbBean, Jedis jedis){
        if(regReq.getPhoneNumber() == null || regReq.getPhoneNumber().isEmpty())
            return new Result(REGISTER_PHONE_NUM_EMPTY, BundleUtil.getString(req, "TipPhoneNumEmpty"));
        if(dbBean.find(Account.class, regReq.getPhoneNumber()) != null)
            return new Result(REGISTER_PHONE_NUM_REGISTERED, BundleUtil.getString(req, "TipPhoneNumRegistered"));
        int passwordResult = checkPassword(regReq.getPassword());
        switch (passwordResult){
            case PASSWORD_LENGTH_ILLEGAL:
                return new Result(REGISTER_PASSWORD_LENGTH_ILLEGAL, BundleUtil.getString(req, "TipPasswordLengthIllegal"));
            case PASSWORD_FORMAT_ILLEGAL:
                return new Result(REGISTER_PASSWORD_FORMAT_ILLEGAL, BundleUtil.getString(req, "TipPasswordFormatIllegal"));
        }
        if(!SecurityUtil.checkPhoneCaptcha(regReq.getPhoneNumber(), regReq.getCaptcha(), jedis))
            return new Result(REGISTER_CAPTCHA_INCORRECT, BundleUtil.getString(req, "TipCaptchaIncorrect"));

        Account newUser = new Account(regReq.getPhoneNumber(), regReq.getPassword());
        dbBean.create(newUser);
        SecurityUtil.clearPhoneCaptcha(jedis, regReq.getPhoneNumber());
        return new Result(PublicResultCode.SUCCESS_CODE, BundleUtil.getString(req, "TipRegisterSuccess"));
    }

    public static final int PASSWORD_LENGTH_ILLEGAL = 1;
    public static final int PASSWORD_FORMAT_ILLEGAL = 2;

    public static int checkPassword(String password){
        if(password == null || password.length() < 6 || password.length() > 20)
            return PASSWORD_LENGTH_ILLEGAL;
        return PublicResultCode.SUCCESS_CODE;
    }

    public static final String SESSION_ATTR_USER = "user";
    public static final String APPLICATION_ATTR_WEBUSER = "appWebUser";

    public static final int LOGIN_SUCCESS_WITH_PRE_OFFLINE = 100;
    public static final int LOGIN_USER_NOT_EXIST = 200;
    public static final int LOGIN_PASSWORD_INCORRECT = 300;
    public static final int LOGIN_NEW_PASSWORD_LENGTH_ILLEGAL = 301;
    public static final int LOGIN_NEW_PASSWORD_FORMAT_ILLEGAL = 302;
    public static final int LOGIN_CAPTCHA_INCORRECT = 400;

    private static Result login(HttpServletRequest req, LoginRequest loginRequest, SubwayTicketDBHelperBean dbBean){
        Account account = (Account) dbBean.find(Account.class, loginRequest.getPhoneNumber());
        if(account == null)
            return new Result(LOGIN_USER_NOT_EXIST, BundleUtil.getString(req, "TipUserNotExist"));
        if(!account.getPassword().equals(loginRequest.getPassword()))
            return new Result(LOGIN_PASSWORD_INCORRECT, BundleUtil.getString(req, "TipPasswordIncorrect"));
        req.getSession(true).setAttribute(SESSION_ATTR_USER, account);
        return new Result(PublicResultCode.SUCCESS_CODE, BundleUtil.getString(req, "TipLoginSuccess"));
    }

    public static Result webLogin(HttpServletRequest req, LoginRequest loginRequest, SubwayTicketDBHelperBean dbBean){
        Result result = login(req, loginRequest, dbBean);
        if(result.getResultCode() == PublicResultCode.SUCCESS_CODE){
            ServletContext context = req.getServletContext();
            String appAttr = APPLICATION_ATTR_WEBUSER + ((Account)req.getSession().getAttribute(SESSION_ATTR_USER)).getPhoneNumber();
            HttpSession preSession = (HttpSession) context.getAttribute(appAttr);
            if(preSession != null && !preSession.getId().equals(req.getSession().getId())){
                result.setResultCode(LOGIN_SUCCESS_WITH_PRE_OFFLINE);
                result.setResultDescription(BundleUtil.getString(req, "TipLoginWithPreOffline"));
                logout(preSession);
            }
            context.setAttribute(appAttr, req.getSession());
        }
        return result;
    }

    public static Result resetPassword(HttpServletRequest req, LoginWithNewPasswordRequest loginRequest, SubwayTicketDBHelperBean dbBean, Jedis jedis){
        Account account = (Account) dbBean.find(Account.class, loginRequest.getPhoneNumber());
        if(account == null)
            return new Result(LOGIN_USER_NOT_EXIST, BundleUtil.getString(req, "TipUserNotExist"));
        int passwordResult = checkPassword(loginRequest.getNewPassword());
        switch (passwordResult){
            case PASSWORD_LENGTH_ILLEGAL:
                return new Result(LOGIN_NEW_PASSWORD_LENGTH_ILLEGAL, BundleUtil.getString(req, "TipPasswordLengthIllegal"));
            case PASSWORD_FORMAT_ILLEGAL:
                return new Result(LOGIN_NEW_PASSWORD_FORMAT_ILLEGAL, BundleUtil.getString(req, "TipPasswordFormatIllegal"));
        }
        if(!SecurityUtil.checkPhoneCaptcha(loginRequest.getPhoneNumber(), loginRequest.getCaptcha(), jedis))
            return new Result(LOGIN_CAPTCHA_INCORRECT, BundleUtil.getString(req, "TipCaptchaIncorrect"));
        account.setPassword(loginRequest.getNewPassword());
        dbBean.merge(account);
        SecurityUtil.clearPhoneCaptcha(jedis, loginRequest.getPhoneNumber());
        return new Result(PublicResultCode.SUCCESS_CODE, "");
    }

    private static void logout(HttpSession session){
        session.removeAttribute(SESSION_ATTR_USER);
        session.invalidate();
    }

    public static void webLogout(HttpServletRequest req) {
        Account account = (Account) req.getSession(false).getAttribute(SESSION_ATTR_USER);
        if(account == null)
            return;
        ServletContext context = req.getServletContext();
        context.removeAttribute(APPLICATION_ATTR_WEBUSER + account.getPhoneNumber());
        logout(req.getSession(false));
        req.getSession(true);
    }
}