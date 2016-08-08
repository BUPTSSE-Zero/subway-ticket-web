package com.subwayticket.control;

import com.subwayticket.database.control.SystemDBHelperBean;
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
 * 有关账号操作的底层业务逻辑
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class AccountControl {
    /**
     * 注册账号
     * @param req Request对象
     * @param regReq 注册请求，含有注册所需信息
     * @param dbBean 操作数据库的EJB
     * @param jedis Jedis对象，用于校验验证码
     * @return 注册结果
     */
    public static Result register(ServletRequest req, RegisterRequest regReq, SystemDBHelperBean dbBean, Jedis jedis){
        Result result;
        if((result = checkPhoneNumber(regReq.getPhoneNumber(), req)).getResultCode() != PublicResultCode.SUCCESS)
            return result;
        if((result = checkPhoneNumberRegistered(regReq.getPhoneNumber(), dbBean, req)).getResultCode() != PublicResultCode.SUCCESS)
            return result;
        if((result = checkPassword(regReq.getPassword(), req)).getResultCode() != PublicResultCode.SUCCESS)
            return result;
        if((result = checkPhoneCaptcha(regReq.getPhoneNumber(), regReq.getCaptcha(), jedis, req)).getResultCode() != PublicResultCode.SUCCESS)
            return result;

        Account newUser = new Account(regReq.getPhoneNumber(), regReq.getPassword());
        dbBean.create(newUser);
        SecurityUtil.clearPhoneCaptcha(jedis, regReq.getPhoneNumber());
        result.setResultDescription(BundleUtil.getString(req, "TipRegisterSuccess"));
        return result;
    }

    /**
     * 检查手机号是否有效
     * @param phoneNumber 手机号
     * @param req Request对象
     * @return 检查结果
     */
    public static Result checkPhoneNumber(String phoneNumber, ServletRequest req){
        if(phoneNumber == null || phoneNumber.isEmpty())
            return new Result(PublicResultCode.PHONE_NUM_INVALID, BundleUtil.getString(req, "TipPhoneNumInvalid"));
        for(int i = 0; i < phoneNumber.length(); i++){
            if(phoneNumber.charAt(i) >= '0' && phoneNumber.charAt(i) <= '9')
                continue;
            return new Result(PublicResultCode.PHONE_NUM_INVALID, BundleUtil.getString(req, "TipPhoneNumInvalid"));
        }
        return new Result(PublicResultCode.SUCCESS, "");
    }

    /**
     * 检查手机号是否已被注册
     * @param phoneNumber 手机号
     * @param dbBean 操作数据库的EJB
     * @param req Request对象
     * @return 检查结果
     */
    public static Result checkPhoneNumberRegistered(String phoneNumber, SystemDBHelperBean dbBean, ServletRequest req){
        if(dbBean.find(Account.class, phoneNumber) != null)
            return new Result(PublicResultCode.PHONE_NUM_REGISTERED, BundleUtil.getString(req, "TipPhoneNumRegistered"));
        return new Result(PublicResultCode.SUCCESS, "");
    }

    /**
     * 检查用户输入的新密码是否有效
     * @param password 密码
     * @param req Request对象
     * @return 检查结果
     */
    public static Result checkPassword(String password, ServletRequest req){
        if(password == null || password.length() < 6 || password.length() > 20)
            return new Result(PublicResultCode.PASSWORD_LENGTH_ILLEGAL, BundleUtil.getString(req, "TipPasswordLengthIllegal"));
        return new Result(PublicResultCode.SUCCESS, "");
    }

    /**
     * 校验用户输入的手机验证码
     * @param phoneNumber 手机号
     * @param captcha 用户输入的手机验证码
     * @param jedis Jedis对象
     * @param req Request对象
     * @return 校验结果
     */
    public static Result checkPhoneCaptcha(String phoneNumber, String captcha, Jedis jedis, ServletRequest req){
        if(!SecurityUtil.checkPhoneCaptcha(phoneNumber, captcha, jedis)) {
            if(SecurityUtil.increasePhoneCaptchaErrorTimes(jedis, phoneNumber)){
                SecurityUtil.clearPhoneCaptcha(jedis, phoneNumber);
                return new Result(PublicResultCode.PHONE_CAPTCHA_INVALIDATE, BundleUtil.getString(req, "TipCaptchaErrorTimesExceed"));
            }
            return new Result(PublicResultCode.PHONE_CAPTCHA_INCORRECT, BundleUtil.getString(req, "TipCaptchaIncorrect"));
        }
        return new Result(PublicResultCode.SUCCESS, "");
    }

    public static final String SESSION_ATTR_USER = "user";
    public static final String APPLICATION_ATTR_WEBUSER = "appWebUser";

    /**
     * 登录账号
     * @param req Request对象
     * @param loginRequest 登录请求，含有登录所需信息
     * @param dbBean 操作数据库的EJB
     * @param sessionFlag 是否要将登录的账号信息写入Session对象（网页端登录则为true）
     * @return 登录结果
     */
    private static Result login(HttpServletRequest req, LoginRequest loginRequest, SystemDBHelperBean dbBean, boolean sessionFlag){
        Account account = (Account) dbBean.find(Account.class, loginRequest.getUserId());
        if(account == null)
            return new Result(PublicResultCode.USER_NOT_EXIST, BundleUtil.getString(req, "TipUserNotExist"));
        if(!account.getPassword().equals(loginRequest.getPassword()))
            return new Result(PublicResultCode.PASSWORD_INCORRECT, BundleUtil.getString(req, "TipPasswordIncorrect"));
        if(sessionFlag)
            req.getSession(true).setAttribute(SESSION_ATTR_USER, account);
        return new Result(PublicResultCode.SUCCESS, BundleUtil.getString(req, "TipLoginSuccess"));
    }

    /**
     * 网页端登录账号
     * @param req Request对象
     * @param loginRequest 登录请求，含有登录所需信息
     * @param dbBean 操作数据库的EJB
     * @return 登录结果
     */
    public static Result webLogin(HttpServletRequest req, LoginRequest loginRequest, SystemDBHelperBean dbBean){
        Result result = login(req, loginRequest, dbBean, true);
        if(result.getResultCode() == PublicResultCode.SUCCESS){
            ServletContext context = req.getServletContext();
            String appAttr = APPLICATION_ATTR_WEBUSER + ((Account)req.getSession().getAttribute(SESSION_ATTR_USER)).getPhoneNumber();
            HttpSession preSession = (HttpSession) context.getAttribute(appAttr);
            if(preSession != null && !preSession.getId().equals(req.getSession().getId())){
                try {
                    webLogout(preSession);
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

    /**
     * 移动端登录账号
     * @param req Request对象
     * @param loginRequest 登录请求，含有登录所需信息
     * @param dbBean 操作数据库的EJB
     * @param jedis Jedis对象，用于存储token
     * @return 登录结果
     */
    public static Result mobileLogin(HttpServletRequest req, LoginRequest loginRequest, SystemDBHelperBean dbBean, Jedis jedis){
        Result result = login(req, loginRequest, dbBean, false);
        if(result.getResultCode() != PublicResultCode.SUCCESS)
            return result;
        MobileLoginResult mobileLoginResult = new MobileLoginResult(result);
        if(jedis.get(SecurityUtil.REDIS_KEY_MOBILETOKEN_PREFIX + loginRequest.getUserId()) != null){
            mobileLoginResult.setResultCode(PublicResultCode.LOGIN_SUCCESS_WITH_PRE_OFFLINE);
            mobileLoginResult.setResultDescription(BundleUtil.getString(req, "TipLoginWithPreOffline"));
        }
        mobileLoginResult.setToken(SecurityUtil.getMobileToken(loginRequest.getUserId()));
        jedis.setex(SecurityUtil.REDIS_KEY_MOBILETOKEN_PREFIX + loginRequest.getUserId(), SecurityUtil.MOBILETOKEN_VALID_HOURS * 3600,
                    mobileLoginResult.getToken());
        return mobileLoginResult;
    }

    /**
     * 重置账号密码
     * @param req Request对象
     * @param resetRequest 重置密码的请求
     * @param dbBean 操作数据库的EJB
     * @param jedis Jedis对象，用于校验验证码
     * @return 重置密码的结果
     */
    public static Result resetPassword(HttpServletRequest req, ResetPasswordRequest resetRequest, SystemDBHelperBean dbBean, Jedis jedis) {
        Account account = (Account) dbBean.find(Account.class, resetRequest.getPhoneNumber());
        if (account == null)
            return new Result(PublicResultCode.USER_NOT_EXIST, BundleUtil.getString(req, "TipUserNotExist"));

        Result result;
        if((result = checkPhoneCaptcha(resetRequest.getPhoneNumber(), resetRequest.getCaptcha(), jedis, req)).getResultCode() != PublicResultCode.SUCCESS)
            return result;

        result = setNewPassword(req, dbBean, account, resetRequest.getNewPassword());
        if (result.getResultCode() == PublicResultCode.SUCCESS){
            SecurityUtil.clearPhoneCaptcha(jedis, account.getPhoneNumber());
            result.setResultDescription(BundleUtil.getString(req, "TipResetPasswordSuccess"));
        }
        return result;
    }

    /**
     * 修改账号密码
     * @param req Request对象
     * @param modifyRequest 修改密码请求
     * @param account 要修改密码的账号对象
     * @param dbBean 操作数据库的EJB
     * @return 修改密码结果
     */
    public static Result modifyPassword(HttpServletRequest req, ModifyPasswordRequest modifyRequest, Account account, SystemDBHelperBean dbBean){
        account = (Account) dbBean.find(Account.class, account.getPhoneNumber());
        if(!account.getPassword().equals(modifyRequest.getOldPassword()))
            return new Result(PublicResultCode.PASSWORD_INCORRECT, BundleUtil.getString(req, "TipOldPasswordIncorrect"));
        Result result = setNewPassword(req, dbBean, account, modifyRequest.getNewPassword());
        if(result.getResultCode() == PublicResultCode.SUCCESS)
            result.setResultDescription(BundleUtil.getString(req, "TipModifyPasswordSuccess"));
        return result;
    }

    /**
     * 将新的密码设置到数据库中
     * @param req Request对象
     * @param dbBean 操作数据库的EJB
     * @param account 要设置新密码的账号对象，注意该对象要处于持久化状态
     * @param newPassword 新密码
     * @return 设置新密码的结果
     */
    private static Result setNewPassword(HttpServletRequest req, SystemDBHelperBean dbBean, Account account, String newPassword){
        Result result;
        if((result = checkPassword(newPassword, req)).getResultCode() != PublicResultCode.SUCCESS)
            return result;
        account.setPassword(newPassword);
        dbBean.merge(account);
        return result;
    }

    /**
     * 网页端注销账号，从Session中删除账号信息
     * @param session Session对象
     */
    private static void webLogout(HttpSession session){
        session.removeAttribute(SESSION_ATTR_USER);
        session.invalidate();
    }

    /**
     * 网页端注销账号
     * @param req Request对象
     */
    public static void webLogout(HttpServletRequest req) {
        Account account = (Account) req.getSession(false).getAttribute(SESSION_ATTR_USER);
        if(account == null)
            return;
        ServletContext context = req.getServletContext();
        context.removeAttribute(APPLICATION_ATTR_WEBUSER + account.getPhoneNumber());
        webLogout(req.getSession(false));
        req.getSession(true);
    }

    /**
     * 移动端注销账号
     * @param account 要注销的账号对象
     * @param jedis Jedis对象
     */
    public static void mobileLogout(Account account, Jedis jedis){
        jedis.del(SecurityUtil.REDIS_KEY_MOBILETOKEN_PREFIX + account.getPhoneNumber());
    }
}