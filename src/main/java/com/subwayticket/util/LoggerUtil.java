package com.subwayticket.util;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * 日志记录工具类
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class LoggerUtil {
    public static Level DEFAULT_LOG_LEVEL = Level.ALL;
    private static String logBaseDir = "";

    /**
     * 设置日志文件存放的基路径
     * @param baseDir 日志文件存放的基路径
     */
    static public void setLogBaseDir(String baseDir){
        logBaseDir = baseDir;
    }

    /**
     * 获取默认配置的logger
     * @param name 日志记录的主体名
     * @param fileName 日志文件名，文件将存放在${BASE_DIR}/logs路径下
     * @return logger对象
     */
    static public Logger getLogger(String name, String fileName){
        Logger logger = Logger.getLogger(name);
        logger.setLevel(DEFAULT_LOG_LEVEL);
        Layout layout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss,SSS} - " + name + " - %p: %m %n");
        try{
            String path;
            if(logBaseDir == null)
                path = "logs/" + fileName;
            else if(logBaseDir.endsWith(File.separator) || logBaseDir.isEmpty())
                path = logBaseDir + "logs/" + fileName;
            else
                path = logBaseDir + "/logs/" + fileName;
            Appender fileAppender = new FileAppender(layout, path, true);
            logger.addAppender(fileAppender);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        return logger;
    }
}
