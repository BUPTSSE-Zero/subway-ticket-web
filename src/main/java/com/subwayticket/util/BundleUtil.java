package com.subwayticket.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.servlet.ServletRequest;

/**
 *
 * @author buptsse-zero
 */
public class BundleUtil {
    public static ResourceBundle getResourceBundle(ServletRequest request){
        return ResourceBundle.getBundle("/bundle/string", request.getLocale());
    }
    
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
