package com.subwayticket.util;

import com.google.gson.Gson;
import com.subwayticket.model.MobileToken;
import com.subwayticket.model.PhoneCaptcha;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.result.Result;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64UrlCodec;
import io.jsonwebtoken.impl.crypto.MacProvider;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.Random;

/**
 * 安全相关工具
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class SecurityUtil {
    public static final String REDIS_KEY_PHONE_CAPTCHA_PREFIX = "PhoneCaptcha";
    public static final String REDIS_KEY_PHONE_CAPTCHA_ERROR_TIMES_PREFIX = "PhoneCaptchaErrorTimes";
    public static final int PHONE_CAPTCHA_VALID_MINUTES = 30;
    public static final int PHONE_CAPTCHA_INTERVAL_SECONDS = 60;
    public static final int PHONE_CAPTCHA_ERROR_TIMES = 3;

    public static final String REDIS_KEY_MOBILETOKEN_PREFIX = "MobileToken-";
    public static int MOBILETOKEN_VALID_HOURS = 120;

    /**
     * 发送验证码
     * @param request Request对象
     * @param phoneNumber 手机号
     * @param jedis Jedis对象
     * @return 发送结果
     */
    public static Result sendPhoneCaptcha(ServletRequest request, String phoneNumber, Jedis jedis){
        if(phoneNumber == null || phoneNumber.isEmpty())
            return new Result(PublicResultCode.PHONE_NUM_INVALID, BundleUtil.getString(request, "TipPhoneNumInvalid"));
        for(int i = 0; i < phoneNumber.length(); i++){
            if(phoneNumber.charAt(i) >= '0' && phoneNumber.charAt(i) <= '9')
                continue;
            return new Result(PublicResultCode.PHONE_NUM_INVALID, BundleUtil.getString(request, "TipPhoneNumInvalid"));
        }
        PhoneCaptcha pc = PhoneCaptcha.fromString(jedis.get(REDIS_KEY_PHONE_CAPTCHA_PREFIX + phoneNumber));
        if(pc != null){
            if(new Date().getTime() - pc.getSendTime().getTime() < PHONE_CAPTCHA_INTERVAL_SECONDS * 1000)
                return new Result(PublicResultCode.PHONE_CAPTCHA_INTERVAL_ILLEGAL, BundleUtil.getString(request, "TipCaptchaIntervalIllegal"));
        }

        //TODO:将含有验证码的短信发送到指定手机上

        jedis.setex(REDIS_KEY_PHONE_CAPTCHA_PREFIX + phoneNumber, PHONE_CAPTCHA_VALID_MINUTES * 60, new PhoneCaptcha("123456").toString());
        jedis.setex(REDIS_KEY_PHONE_CAPTCHA_ERROR_TIMES_PREFIX + phoneNumber, PHONE_CAPTCHA_VALID_MINUTES * 60, Integer.toString(0));
        return new Result(PublicResultCode.SUCCESS, BundleUtil.getString(request, "TipCaptchaSendSuccess"));
    }

    /**
     * 校验手机验证码
     * @param phoneNumber 手机号
     * @param captcha 用户输入的验证码
     * @param jedis Jedis对象
     * @return true表示用户输入的验证码正确
     */
    public static boolean checkPhoneCaptcha(String phoneNumber, String captcha, Jedis jedis){
        if(phoneNumber == null || captcha == null)
            return false;
        PhoneCaptcha pc = PhoneCaptcha.fromString(jedis.get(REDIS_KEY_PHONE_CAPTCHA_PREFIX + phoneNumber));
        if(pc == null || !captcha.equals(pc.getCode()))
            return false;
        return true;
    }

    /**
     * 在用户输错验证码时增加错误次数
     * @param jedis Jedis对象
     * @param phoneNumber 手机号
     * @return true表示错误次数已达到最大
     */
    public static boolean increasePhoneCaptchaErrorTimes(Jedis jedis, String phoneNumber){
        String errorTimesStr = jedis.get(REDIS_KEY_PHONE_CAPTCHA_ERROR_TIMES_PREFIX + phoneNumber);
        if(errorTimesStr == null)
            return false;
        int errorTimes = Integer.valueOf(errorTimesStr);
        errorTimes++;
        jedis.set(REDIS_KEY_PHONE_CAPTCHA_ERROR_TIMES_PREFIX + phoneNumber, Integer.toString(errorTimes));
        if(errorTimes >= PHONE_CAPTCHA_ERROR_TIMES)
            return true;
        return false;
    }

    /**
     * 从Redis数据库中删除临时手机验证码
     * @param jedis Jedis对象
     * @param phoneNumber 手机号
     */
    public static void clearPhoneCaptcha(Jedis jedis, String phoneNumber){
        jedis.del(REDIS_KEY_PHONE_CAPTCHA_PREFIX + phoneNumber);
        jedis.del(REDIS_KEY_PHONE_CAPTCHA_ERROR_TIMES_PREFIX + phoneNumber);
    }

    private static Key tokenkey = MacProvider.generateKey();
    public static final String HEADER_TOKEN_KEY = "AuthToken";

    /**
     * 生成移动端token
     * @param userId 用户ID（即手机号）
     * @return 生成的token
     */
    public static String getMobileToken(String userId){
        MobileToken token = new MobileToken(userId);
        Gson gson = GsonUtil.getDefaultGson();
        String tokenString = Jwts.builder().setIssuer(userId).setSubject(gson.toJson(token)).signWith(SignatureAlgorithm.HS512, tokenkey).compact();
        return tokenString;
    }

    /**
     * 校验token的合法性
     * @param tokenStr token
     * @param jedis Jedis对象
     * @return 若token合法，返回token对应的用户ID（手机号），否则返回null
     */
    public static String checkMobileToken(String tokenStr, Jedis jedis){
        if(tokenStr == null)
            return null;
        try {
            String subject = Jwts.parser().setSigningKey(tokenkey).parseClaimsJws(tokenStr).getBody().getSubject();
            MobileToken token = GsonUtil.getDefaultGson().fromJson(subject, MobileToken.class);
            String redisToken = jedis.get(REDIS_KEY_MOBILETOKEN_PREFIX + token.getUserId());
            if(!tokenStr.equals(redisToken))
                return null;
            jedis.setex(REDIS_KEY_MOBILETOKEN_PREFIX + token.getUserId(), MOBILETOKEN_VALID_HOURS * 3600, redisToken);
            return token.getUserId();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static final int BYTE_ORDER[] = {1, 4, 3, 7, 0, 2, 5, 6};
    private static final Random RANDOM = new Random();

    /**
     * 使用Base64算法生成取票码
     * @param phoneNumber 用户的手机号
     * @return 取票码
     */
    public static String getExtractCode(String phoneNumber){
        long num = Long.valueOf(phoneNumber);

        //计算存储手机号（看做整数）所需的字节数
        int validByteCount = (int)(Math.log(Long.highestOneBit(num))/Math.log(2)) + 1;
        validByteCount = (validByteCount % 8 == 0) ? validByteCount / 8 : validByteCount / 8 + 1;

        byte codeByte[] = new byte[BYTE_ORDER.length];
        int i;
        for(i = 0; i < validByteCount && i < codeByte.length; i++){
            //每次都取出手机号（看做整数）的最高位所对应的那个字节存入目标字节数组
            codeByte[BYTE_ORDER[i]] = new Long(num & 0xFF).byteValue();
            num = num >> 8;
        }
        byte ramdonBytes[] = new byte[codeByte.length - i];

        //对目标字节数组剩余的字节数填充随机数
        RANDOM.nextBytes(ramdonBytes);
        for(int c = 0; i < codeByte.length; i++, c++){
            codeByte[BYTE_ORDER[i]] = ramdonBytes[c];
        }
        return new Base64UrlCodec().encode(codeByte).toUpperCase();
    }
}
