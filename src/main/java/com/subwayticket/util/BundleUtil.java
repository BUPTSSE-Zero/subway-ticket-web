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
 *
 * @author buptsse-zero
 */
public class BundleUtil {
    public static final Locale DEFAULT_BUNDLE_LOCALE = Locale.SIMPLIFIED_CHINESE;

    public static ResourceBundle getResourceBundle(ServletRequest request){
        try {
            return ResourceBundle.getBundle("/bundle/string", request.getLocale());
        }catch (MissingResourceException mre){
            return ResourceBundle.getBundle("/bundle/string", DEFAULT_BUNDLE_LOCALE);
        }
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
