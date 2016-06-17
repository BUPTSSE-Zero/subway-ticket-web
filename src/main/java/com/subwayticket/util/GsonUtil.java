package com.subwayticket.util;

import com.google.gson.*;

/**
 *
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */
public class GsonUtil {
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
    
    public static Gson getGson(){
        return getDefaultGsonBuilder().create();
    }
}
