package com.subwayticket.util;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 定时工作调度工具类
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class ScheduleUtil {
    private static Scheduler scheduler;
    public static String JOB_GROUP = "SubwayTicketJobGroup";
    public static String TRIGGER_GROUP = "SubwayTicketJobTriggerGroup";

    /**
     * 初始化调度器
     * @throws SchedulerException
     */
    public static void initScheduler() throws SchedulerException{
        scheduler = StdSchedulerFactory.getDefaultScheduler();
    }

    /**
     * 启动调度器
     * @throws SchedulerException
     */
    public static void startScheduler() throws SchedulerException {
        scheduler.start();
    }

    /**
     * 构造具体的工作对象
     * @param jobClass 要执行的工作的类
     * @param jobName 工作名，用于唯一标识工作实例
     * @param dataMap 工作执行时需要的额外外部数据
     * @return 具体的工作对象
     */
    private static JobDetail buildJob(Class<? extends Job> jobClass, String jobName, JobDataMap dataMap){
        JobBuilder builder = JobBuilder.newJob(jobClass).withIdentity(jobName, JOB_GROUP);
        if(dataMap != null)
            builder.setJobData(dataMap);
        return builder.build();
    }

    /**
     * 添加要立即执行的工作
     * @param jobClass 要执行的工作的类
     * @param jobName 工作名，用于唯一标识工作实例
     * @param dataMap 工作执行时需要的额外外部数据
     * @throws SchedulerException
     */
    public static void addImmediateScheduleJob(Class<? extends Job> jobClass, String jobName, JobDataMap dataMap) throws SchedulerException {
        JobDetail job = buildJob(jobClass, jobName, dataMap);
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName + "Trigger", TRIGGER_GROUP).startNow().build();
        scheduler.scheduleJob(job, trigger);
    }

    /**
     * 添加每隔一定时间就执行的工作
     * @param jobClass 要执行的工作的类
     * @param jobName 工作名，用于唯一标识工作实例
     * @param dataMap 工作执行时需要的额外外部数据
     * @param intervalSeconds 间隔时间，以秒为单位
     * @param execCount 工作的总执行次数，-1表示无限次执行
     * @param startNow 是否立即开始执行工作
     * @throws SchedulerException
     */
    public static void addIntervalScheduleJob(Class<? extends Job> jobClass, String jobName, JobDataMap dataMap, int intervalSeconds, int execCount, boolean startNow) throws SchedulerException {
        if(execCount == 0)
            return;
        JobDetail job = buildJob(jobClass, jobName, dataMap);
        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger().withIdentity(jobName + "Trigger", TRIGGER_GROUP);
        if(startNow)
            triggerBuilder.startNow();
        else
            triggerBuilder.startAt(DateBuilder.futureDate(intervalSeconds, DateBuilder.IntervalUnit.SECOND));
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(intervalSeconds);
        if(execCount < 0)
            scheduleBuilder.repeatForever();
        else
            scheduleBuilder.withRepeatCount(execCount - 1);
        triggerBuilder.withSchedule(scheduleBuilder);
        scheduler.scheduleJob(job, triggerBuilder.build());
    }

    /**
     * 添加每天在固定时间点执行的工作
     * @param jobClass 要执行的工作的类
     * @param jobName 工作名，用于唯一标识工作实例
     * @param dataMap 工作执行时需要的额外外部数据
     * @param hour 时间点的小时部分，24小时制
     * @param minute 时间点的分钟部分
     * @throws SchedulerException
     */
    public static void addDailyFixedTimeScheduleJob(Class<? extends Job> jobClass, String jobName, JobDataMap dataMap, int hour, int minute) throws SchedulerException {
        JobDetail job = buildJob(jobClass, jobName, dataMap);
        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger().withIdentity(jobName + "Trigger", TRIGGER_GROUP);
        triggerBuilder.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(hour, minute));
        scheduler.scheduleJob(job, triggerBuilder.build());
    }

    /**
     * 取消某个工作，取消后的工作将不会再执行
     * @param jobName 要取消的工作名
     * @return true表示取消成功
     * @throws SchedulerException
     */
    public static boolean cancelScheduleJob(String jobName) throws SchedulerException {
        JobKey key = new JobKey(jobName, JOB_GROUP);
        return scheduler.deleteJob(key);
    }

    /**
     * 停止调度器
     * @throws SchedulerException
     */
    public static void stopScheduler() throws SchedulerException {
        scheduler.shutdown(true);
    }
}
