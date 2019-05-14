package com.kevin.quatrz;

import org.apache.commons.lang3.time.FastDateFormat;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by yss on 2018-09-10.
 */
public class ExecuteStateJobApp {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteStateJobApp.class.getName());

    public static void main(String[] args) throws SchedulerException {
        new ExecuteStateJobApp().run();
    }

    public void run() throws SchedulerException {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy 年 MM 月 dd 日  HH 时 mm 分 ss 秒");
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        // 下一个10秒，不懂的去查API
        Date startTime = DateBuilder.nextGivenSecondDate(null, 10);
        // job1 第10秒执行一次，共执行5次
        JobDetail job1 = JobBuilder.newJob(ColorJob.class).withIdentity("job1", "group1")
                .build();

        SimpleTrigger trigger1 = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10)
                        .withRepeatCount(4)).build();
        job1.getJobDataMap().put(ColorJob.FAVORITE_COLOR, "######  绿   #####");
        job1.getJobDataMap().put(ColorJob.EXECUTION_COUNT, 1);
        Date scheduleTime1 = sched.scheduleJob(job1, trigger1);
        logger.info(job1.getKey().getName() + " 将在 : "
                + dateFormat.format(scheduleTime1) + " 执行, 并重复 : "
                + trigger1.getRepeatCount() + " 次, 每次间隔   "
                + trigger1.getRepeatInterval() / 1000 + " 秒");


        // job2 每10秒执行一次，共执行5次
        JobDetail job2 = JobBuilder.newJob(ColorJob.class).withIdentity("job2", "group1")
                .build();
        SimpleTrigger trigger2 = TriggerBuilder.newTrigger()
                .withIdentity("trigger2", "group1")
                .startAt(startTime)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10)
                                .withRepeatCount(4)).build();
        // 初始化传入的参数
        job2.getJobDataMap().put(ColorJob.FAVORITE_COLOR, "######  红   #####");
        job2.getJobDataMap().put(ColorJob.EXECUTION_COUNT, 1);
        Date scheduleTime2 = sched.scheduleJob(job2, trigger2);
        logger.info(job2.getKey().getName() + " 将在 : "
                + dateFormat.format(scheduleTime2) + " 执行, 并重复 : "
                + trigger2.getRepeatCount() + " 次, 每次间隔   "
                + trigger2.getRepeatInterval() / 1000 + " 秒");

        sched.start();
        logger.info("------- 等待60秒 ... -------------");
        try {
            Thread.sleep(60L * 1000L);
        } catch (Exception e) {
        }

        sched.shutdown(true);
        logger.info("------- 调度已关闭 ---------------------");

        // 显示一下 已经执行的任务信息
        SchedulerMetaData metaData = sched.getMetaData();
        logger.info("~~~~~~~~~~  执行了 "
                + metaData.getNumberOfJobsExecuted() + " 个 jobs.");
    }
}
