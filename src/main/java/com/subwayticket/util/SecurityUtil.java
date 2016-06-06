package com.subwayticket.util;

import com.google.gson.Gson;
import com.subwayticket.model.MobileToken;
import com.subwayticket.model.PhoneCaptcha;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.result.Result;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletRequest;
import java.security.Key;
import java.util.Date;

/**
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */
public class SecurityUtil {
    public static final String REDIS_KEY_PHONE_CAPTCHA_PREFIX = "PhoneCaptcha";
    public static final int AUTH_CODE_VALID_MINUTES = 30;
    public static final int AUTH_CODE_INTERVAL_SECONDS = 60;

    public static final int PHONE_CAPTCHA_SEND_FAILED = 100;
    public static final int PHONE_CAPTCHA_INTERVAL_ILLEGAL = 101;

    public static Result sendPhoneCaptcha(ServletRequest request, String phoneNumber, Jedis jedis){
        if(phoneNumber == null || phoneNumber.isEmpty())
            return new Result(PHONE_CAPTCHA_SEND_FAILED, BundleUtil.getString(request, "TipCaptchaSendFailed"));
        PhoneCaptcha pc = PhoneCaptcha.fromString(jedis.get(REDIS_KEY_PHONE_CAPTCHA_PREFIX + phoneNumber));
        if(pc != null){
            if(new Date().getTime() - pc.getSendTime().getTime() < AUTH_CODE_INTERVAL_SECONDS * 1000)
                return new Result(PHONE_CAPTCHA_INTERVAL_ILLEGAL, BundleUtil.getString(request, "TipCaptchaIntervalIllegal"));
        }
        jedis.setex(REDIS_KEY_PHONE_CAPTCHA_PREFIX + phoneNumber, AUTH_CODE_VALID_MINUTES * 60, new PhoneCaptcha("123456").toString());
        return new Result(PublicResultCode.SUCCESS_CODE, BundleUtil.getString(request, "TipCaptchaSendSuccess"));
    }

    public static boolean checkPhoneCaptcha(String phoneNumber, String captcha, Jedis jedis){
        if(phoneNumber == null || captcha == null)
            return false;
        PhoneCaptcha pc = PhoneCaptcha.fromString(jedis.get(REDIS_KEY_PHONE_CAPTCHA_PREFIX + phoneNumber));
        if(pc == null || !captcha.equals(pc.getCode()))
            return false;
        return true;
    }

    public static void clearPhoneCaptcha(Jedis jedis, String phoneNumber){
        jedis.del(REDIS_KEY_PHONE_CAPTCHA_PREFIX + phoneNumber);
    }

    private static Key tokenkey = MacProvider.generateKey();

    public static String getMobileToken(String phoneNumber){
        MobileToken token = new MobileToken(phoneNumber);
        Gson gson = GsonUtil.getGson();
        String tokenString = Jwts.builder().setSubject(gson.toJson(token)).signWith(SignatureAlgorithm.HS512, tokenkey).compact();
        return tokenString.substring(tokenString.lastIndexOf(".") + 1);
    }
}
