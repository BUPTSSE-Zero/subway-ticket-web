package com.subwayticket.util;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by zhou-shengyun on 7/23/16.
 */
public class ScheduleUtil {
    private static Scheduler scheduler;
    public static String JOB_GROUP = "SubwayTicketJobGroup";
    public static String TRIGGER_GROUP = "SubwayTicketJobTriggerGroup";

    public static void initScheduler() throws SchedulerException{
        scheduler = StdSchedulerFactory.getDefaultScheduler();
    }

    public static void startScheduler() throws SchedulerException {
        scheduler.start();
    }

    private static JobDetail buildJob(Class<? extends Job> jobClass, String jobName, JobDataMap dataMap){
        JobBuilder builder = JobBuilder.newJob(jobClass).withIdentity(jobName, JOB_GROUP);
        if(dataMap != null)
            builder.setJobData(dataMap);
        return builder.build();
    }

    public static void addImmediateScheduleJob(Class<? extends Job> jobClass, String jobName, JobDataMap dataMap) throws SchedulerException {
        JobDetail job = buildJob(jobClass, jobName, dataMap);
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName + "Trigger", TRIGGER_GROUP).startNow().build();
        scheduler.scheduleJob(job, trigger);
    }

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

    public static void addDailyFixedTimeScheduleJob(Class<? extends Job> jobClass, String jobName, JobDataMap dataMap, int hour, int minute) throws SchedulerException {
        JobDetail job = buildJob(jobClass, jobName, dataMap);
        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger().withIdentity(jobName + "Trigger", TRIGGER_GROUP);
        triggerBuilder.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(hour, minute));
        scheduler.scheduleJob(job, triggerBuilder.build());
    }

    public static boolean cancelScheduleJob(String jobName) throws SchedulerException {
        JobKey key = new JobKey(jobName, JOB_GROUP);
        return scheduler.deleteJob(key);
    }

    public static void stopScheduler() throws SchedulerException {
        scheduler.shutdown(true);
    }
}
