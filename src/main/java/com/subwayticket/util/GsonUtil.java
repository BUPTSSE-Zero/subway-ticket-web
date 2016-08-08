package com.subwayticket.util;

import com.google.gson.*;

/**
 * Gson相关工具类
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class GsonUtil {

    /**
     * 获取默认的Gson配置
     * @return GsonBuilder对象，可用于构造Gson实例
     */
    private static GsonBuilder getDefaultGsonBuilder(){
        return new GsonBuilder()
                   .setDateFormat("yyyy-MM-dd HH:mm:ss")
                   .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                   .setExclusionStrategies(new ExclusionStrategy() {
                       @Override
                       public boolean shouldSkipField(FieldAttributes f) {
                           if(f.getName().startsWith("_"))
                               return true;
                           return false;
                       }

                       @Override
                       public boolean shouldSkipClass(Class<?> clazz) {
                           return false;
                       }
                   });
    }

    /**
     * 获取由默认配置生成的Gson实例
     * @return Gson实例对象
     */
    public static Gson getDefaultGson(){
        return getDefaultGsonBuilder().create();
    }
}
