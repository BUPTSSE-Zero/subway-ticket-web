package com.subwayticket.util;

import java.io.IOException;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 *
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */
public class LoggerUtil {
    private static Level LOWEST_LEVEL = Level.ALL;
    private static String logBaseDir = null;

    static public void setLogBaseDir(String baseDir){
        logBaseDir = baseDir;
    }

    static public Logger getLogger(String name, String fileName){
        Logger logger = Logger.getLogger(name);
        logger.setLevel(LOWEST_LEVEL);
        Layout layout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss,SSS} - " + name + " - %p: %m %n");
        try{
            Appender fileAppender = new FileAppender(layout, logBaseDir + "/logs/" + fileName, true);
            logger.addAppender(fileAppender);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        return logger;
    }
}
