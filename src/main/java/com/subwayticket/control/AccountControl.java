package com.subwayticket.control;

import com.subwayticket.database.control.SubwayTicketDBHelperBean;
import com.subwayticket.database.model.Account;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.RegisterRequest;
import com.subwayticket.model.Result;
import com.subwayticket.util.BundleUtil;
import com.subwayticket.util.SecurityUtil;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletRequest;

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
}
