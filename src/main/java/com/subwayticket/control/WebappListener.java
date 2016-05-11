package com.subwayticket.control; /**
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */

import com.subwayticket.util.JedisUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener()
public class WebappListener implements ServletContextListener {

    // Public constructor is required by servlet spec
    public WebappListener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
        JedisUtil.initJedisPool();
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }

}
