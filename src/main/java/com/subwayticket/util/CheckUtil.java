package com.subwayticket.util;

import com.subwayticket.database.control.SubwayTicketDBHelperBean;
import com.subwayticket.database.model.Account;
import com.subwayticket.model.PhoneCaptcha;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.RegisterRequest;
import com.subwayticket.model.Result;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletRequest;

/**
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */
public class CheckUtil {
    public static final int REGISTER_PHONE_NUM_EMPTY = 100;
    public static final int REGISTER_PHONE_NUM_REGISTERED = 101;
    public static final int REGISTER_PASSWORD_LENGTH_ILLEGAL = 200;
    public static final int REGISTER_PASSWORD_FORMAT_ILLEGAL = 201;
    public static final int REGISTER_CAPTCHA_INCORRECT = 300;

    public static Result checkRegisterInfo(ServletRequest req, RegisterRequest regReq, SubwayTicketDBHelperBean dbBean, Jedis jedis){
        if(regReq.getPhoneNumber() == null || regReq.getPhoneNumber().isEmpty())
            return new Result(REGISTER_PHONE_NUM_EMPTY, BundleUtil.getString(req, "TipPhoneNumEmpty"));
        if(dbBean.find(Account.class, regReq.getPhoneNumber()) != null)
            return new Result(REGISTER_PHONE_NUM_REGISTERED, BundleUtil.getString(req, "TipPhoneNumRegistered"));
        if(regReq.getPassword() == null || regReq.getPassword().length() < 6 || regReq.getPassword().length() > 20)
            return new Result(REGISTER_PASSWORD_LENGTH_ILLEGAL, BundleUtil.getString(req, "TipPasswordLengthIllegal"));
        if(!checkPhoneCaptcha(regReq.getPhoneNumber(), regReq.getCaptcha(), jedis))
            return new Result(REGISTER_CAPTCHA_INCORRECT, BundleUtil.getString(req, "TipCaptchaIncorrect"));
        return new Result(PublicResultCode.SUCCESS_CODE, BundleUtil.getString(req, "TipRegisterSuccess"));
    }

    public static boolean checkPhoneCaptcha(String phoneNumber, String captcha, Jedis jedis){
        if(phoneNumber == null || captcha == null)
            return false;
        PhoneCaptcha pc = PhoneCaptcha.fromString(jedis.get(SecurityUtil.REDIS_KEY_PHONE_CAPTCHA_PREFIX + phoneNumber));
        if(pc == null || !captcha.equals(pc.getCode()))
            return false;
        jedis.del(SecurityUtil.REDIS_KEY_PHONE_CAPTCHA_PREFIX + phoneNumber);
        return true;
    }
}
