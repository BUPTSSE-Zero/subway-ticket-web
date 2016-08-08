package com.subwayticket.control.listener;

import com.subwayticket.control.jobs.AutoRefundCancelOrdersJob;
import com.subwayticket.control.jobs.ClearOutdateOrdersJob;
import com.subwayticket.database.control.EntityManagerFactory;
import com.subwayticket.util.JedisUtil;
import com.subwayticket.util.LoggerUtil;
import com.subwayticket.util.ScheduleUtil;
import org.quartz.SchedulerException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Date;

/**
 * 监听器，Web App部署与卸载时触发
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
@WebListener
public class WebappListener implements ServletContextListener {

    // Public constructor is required by servlet spec
    public WebappListener() {}

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
        JedisUtil.initJedisPool();
        LoggerUtil.setLogBaseDir(sce.getServletContext().getRealPath("/"));
        try {
            ScheduleUtil.initScheduler();
            ScheduleUtil.startScheduler();
            ScheduleUtil.addImmediateScheduleJob(ClearOutdateOrdersJob.class, "ClearOutdateNotpayOrders", null);
            ScheduleUtil.addDailyFixedTimeScheduleJob(AutoRefundCancelOrdersJob.class, "AutoRefundCancelOrders", null, 0, 0);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        EntityManagerFactory.closeSubwayTicketDBPU();
        System.out.println("Persistence units have been closed.");
        try {
            ScheduleUtil.stopScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
