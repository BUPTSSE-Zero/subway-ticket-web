package com.subwayticket.control;

import com.subwayticket.database.control.SubwayTicketDBHelperBean;
import com.subwayticket.database.model.Account;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.request.LoginRequest;
import com.subwayticket.model.request.ModifyPasswordRequest;
import com.subwayticket.model.request.ResetPasswordRequest;
import com.subwayticket.model.request.RegisterRequest;
import com.subwayticket.model.result.MobileLoginResult;
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
    public static Result register(ServletRequest req, RegisterRequest regReq, SubwayTicketDBHelperBean dbBean, Jedis jedis){
        if(regReq.getPhoneNumber() == null || regReq.getPhoneNumber().isEmpty())
            return new Result(PublicResultCode.PHONE_NUM_EMPTY, BundleUtil.getString(req, "TipPhoneNumEmpty"));
        if(dbBean.find(Account.class, regReq.getPhoneNumber()) != null)
            return new Result(PublicResultCode.PHONE_NUM_REGISTERED, BundleUtil.getString(req, "TipPhoneNumRegistered"));
        int passwordResult = checkPassword(regReq.getPassword());
        switch (passwordResult){
            case PublicResultCode.PASSWORD_LENGTH_ILLEGAL:
                return new Result(passwordResult, BundleUtil.getString(req, "TipPasswordLengthIllegal"));
            case PublicResultCode.PASSWORD_FORMAT_ILLEGAL:
                return new Result(passwordResult, BundleUtil.getString(req, "TipPasswordFormatIllegal"));
        }
        if(!SecurityUtil.checkPhoneCaptcha(regReq.getPhoneNumber(), regReq.getCaptcha(), jedis))
            return new Result(PublicResultCode.PHONE_CAPTCHA_INCORRECT, BundleUtil.getString(req, "TipCaptchaIncorrect"));

        Account newUser = new Account(regReq.getPhoneNumber(), regReq.getPassword());
        dbBean.create(newUser);
        SecurityUtil.clearPhoneCaptcha(jedis, regReq.getPhoneNumber());
        return new Result(PublicResultCode.SUCCESS, BundleUtil.getString(req, "TipRegisterSuccess"));
    }

    public static int checkPassword(String password){
        if(password == null || password.length() < 6 || password.length() > 20)
            return PublicResultCode.PASSWORD_LENGTH_ILLEGAL;
        return PublicResultCode.SUCCESS;
    }

    public static final String SESSION_ATTR_USER = "user";
    public static final String APPLICATION_ATTR_WEBUSER = "appWebUser";


    private static Result login(HttpServletRequest req, LoginRequest loginRequest, SubwayTicketDBHelperBean dbBean, boolean sessionFlag){
        Account account = (Account) dbBean.find(Account.class, loginRequest.getPhoneNumber());
        if(account == null)
            return new Result(PublicResultCode.USER_NOT_EXIST, BundleUtil.getString(req, "TipUserNotExist"));
        if(!account.getPassword().equals(loginRequest.getPassword()))
            return new Result(PublicResultCode.PASSWORD_INCORRECT, BundleUtil.getString(req, "TipPasswordIncorrect"));
        if(sessionFlag)
            req.getSession(true).setAttribute(SESSION_ATTR_USER, account);
        return new Result(PublicResultCode.SUCCESS, BundleUtil.getString(req, "TipLoginSuccess"));
    }

    public static Result webLogin(HttpServletRequest req, LoginRequest loginRequest, SubwayTicketDBHelperBean dbBean){
        Result result = login(req, loginRequest, dbBean, true);
        if(result.getResultCode() == PublicResultCode.SUCCESS){
            ServletContext context = req.getServletContext();
            String appAttr = APPLICATION_ATTR_WEBUSER + ((Account)req.getSession().getAttribute(SESSION_ATTR_USER)).getPhoneNumber();
            HttpSession preSession = (HttpSession) context.getAttribute(appAttr);
            if(preSession != null && !preSession.getId().equals(req.getSession().getId())){
                try {
                    logout(preSession);
                    result.setResultCode(PublicResultCode.LOGIN_SUCCESS_WITH_PRE_OFFLINE);
                    result.setResultDescription(BundleUtil.getString(req, "TipLoginWithPreOffline"));
                }catch (IllegalStateException ise){
                    ise.printStackTrace();
                }
            }
            context.setAttribute(appAttr, req.getSession());
        }
        return result;
    }

    public static Result mobileLogin(HttpServletRequest req, LoginRequest loginRequest, SubwayTicketDBHelperBean dbBean, Jedis jedis){
        Result result = login(req, loginRequest, dbBean, false);
        if(result.getResultCode() != PublicResultCode.SUCCESS)
            return result;
        MobileLoginResult mobileLoginResult = new MobileLoginResult(result);
        if(jedis.get(SecurityUtil.REDIS_KEY_MOBILETOKEN + loginRequest.getPhoneNumber()) != null){
            mobileLoginResult.setResultCode(PublicResultCode.LOGIN_SUCCESS_WITH_PRE_OFFLINE);
            mobileLoginResult.setResultDescription(BundleUtil.getString(req, "TipLoginWithPreOffline"));
        }
        mobileLoginResult.setToken(SecurityUtil.getMobileToken(loginRequest.getPhoneNumber()));
        jedis.setex(SecurityUtil.REDIS_KEY_MOBILETOKEN + loginRequest.getPhoneNumber(), SecurityUtil.MOBILETOKEN_VALID_HOURS * 3600,
                    mobileLoginResult.getToken());
        return mobileLoginResult;
    }

    public static Result resetPassword(HttpServletRequest req, ResetPasswordRequest resetRequest, SubwayTicketDBHelperBean dbBean, Jedis jedis) {
        Account account = (Account) dbBean.find(Account.class, resetRequest.getPhoneNumber());
        if (account == null)
            return new Result(PublicResultCode.USER_NOT_EXIST, BundleUtil.getString(req, "TipUserNotExist"));
        if (!SecurityUtil.checkPhoneCaptcha(resetRequest.getPhoneNumber(), resetRequest.getCaptcha(), jedis))
            return new Result(PublicResultCode.PHONE_CAPTCHA_INCORRECT, BundleUtil.getString(req, "TipCaptchaIncorrect"));

        Result result = setNewPassword(req, dbBean, account, resetRequest.getNewPassword());
        if (result.getResultCode() == PublicResultCode.SUCCESS){
            SecurityUtil.clearPhoneCaptcha(jedis, account.getPhoneNumber());
            result.setResultDescription(BundleUtil.getString(req, "TipResetPasswordSuccess"));
        }
        return result;
    }

    public static Result webModifyPassword(HttpServletRequest req, ModifyPasswordRequest modifyRequest, SubwayTicketDBHelperBean dbBean){
        Account account = (Account) dbBean.find(Account.class, ((Account)req.getSession().getAttribute(SESSION_ATTR_USER)).getPhoneNumber());
        return modifyPassword(req, modifyRequest, account, dbBean);
    }

    public static Result mobileModifyPassword(HttpServletRequest req, ModifyPasswordRequest modifyRequest, Account account, SubwayTicketDBHelperBean dbBean){
        return modifyPassword(req, modifyRequest, account, dbBean);
    }

    private static Result modifyPassword(HttpServletRequest req, ModifyPasswordRequest modifyRequest, Account account, SubwayTicketDBHelperBean dbBean){
        if(!account.getPassword().equals(modifyRequest.getOldPassword()))
            return new Result(PublicResultCode.PASSWORD_INCORRECT, BundleUtil.getString(req, "TipOldPasswordIncorrect"));
        Result result = setNewPassword(req, dbBean, account, modifyRequest.getNewPassword());
        if(result.getResultCode() == PublicResultCode.SUCCESS)
            result.setResultDescription(BundleUtil.getString(req, "TipModifyPasswordSuccess"));
        return result;
    }

    private static Result setNewPassword(HttpServletRequest req, SubwayTicketDBHelperBean dbBean, Account account, String newPassword){
        int passwordResult = checkPassword(newPassword);
        switch (passwordResult){
            case PublicResultCode.PASSWORD_LENGTH_ILLEGAL:
                return new Result(passwordResult, BundleUtil.getString(req, "TipPasswordLengthIllegal"));
            case PublicResultCode.PASSWORD_FORMAT_ILLEGAL:
                return new Result(passwordResult, BundleUtil.getString(req, "TipPasswordFormatIllegal"));
        }
        account.setPassword(newPassword);
        dbBean.merge(account);
        return new Result(PublicResultCode.SUCCESS, null);
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

    public static void mobileLogout(Account account, Jedis jedis){
        jedis.del(SecurityUtil.REDIS_KEY_MOBILETOKEN + account.getPhoneNumber());
    }
}