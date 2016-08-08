package com.subwayticket.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;

/**
 * I18N字符串工具类
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class BundleUtil {
    public static final Locale DEFAULT_BUNDLE_LOCALE = Locale.SIMPLIFIED_CHINESE;

    /**
     * 获取字符串资源
     * @param request Request对象
     * @return 字符串资源
     */
    public static ResourceBundle getResourceBundle(ServletRequest request){
        try {
            return ResourceBundle.getBundle("/bundle/string", request.getLocale());
        }catch (MissingResourceException mre){
            return ResourceBundle.getBundle("/bundle/string", DEFAULT_BUNDLE_LOCALE);
        }
    }

    /**
     * 获取字符串资源中指定的字符串
     * @param request Request对象
     * @param key 要获取的字符串的key
     * @return 返回指定key对应的字符串，若找不到则返回一个空字符串
     */
    public static String getString(ServletRequest request, String key){
        ResourceBundle bundle = getResourceBundle(request);
        try{
            return bundle.getString(key);
        }catch(MissingResourceException mre){
            mre.printStackTrace();
            return "";
        }
    }
}
