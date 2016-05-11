package com.subwayticket.model;

import com.google.gson.JsonSyntaxException;
import com.subwayticket.util.GsonUtil;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */
public class PhoneCaptcha implements Serializable{
    private final String code;
    private final Date sendTime;

    public PhoneCaptcha(String code) {
        this(code, new Date());
    }

    public PhoneCaptcha(String code, Date sendTime){
        this.code = code;
        this.sendTime = sendTime;
    }
    
    public String getCode() {
        return code;
    }

    public Date getSendTime() {
        return sendTime;
    }
    
    public static PhoneCaptcha fromString(String str){
        if(str == null || str.isEmpty())
            return null;
        try{
            PhoneCaptcha c = GsonUtil.getGson().fromJson(str, PhoneCaptcha.class);
            return c;
        }catch(JsonSyntaxException jse){
            jse.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String toString(){
        return GsonUtil.getGson().toJson(this);
    }
}
