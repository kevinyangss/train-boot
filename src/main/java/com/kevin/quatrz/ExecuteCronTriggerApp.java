package com.kevin.quatrz;

import org.apache.commons.lang3.time.FastDateFormat;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by NoisyFish on 2018-09-10.
 */
public class ExecuteCronTriggerApp {

    private static final Logger logger = LoggerFactory.getLogger(CronJob.class.getName());

    public static void main(String[] args) throws SchedulerException {
        new ExecuteCronTriggerApp().run();
    }

    public void run() throws SchedulerException {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy 年 MM 月 dd 日  HH 时 mm 分 ss 秒");

        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();

        // job1 每20s运行一次
        JobDetail job = JobBuilder.newJob(CronJob.class).withIdentity("job1", "group1").build();
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?"))
                .build();
        Date ft = scheduler.scheduleJob(job, trigger);
        logger.info(job.getKey().getName() + " 将在: "
                + dateFormat.format(ft) + " 运行 . 并且基于Cron表达式 : "
                + trigger.getCronExpression() + "  (含义:每20s运行一次)");

        // job2 偶数分钟的第15秒运行
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job2", "group1").build();
        trigger =  TriggerBuilder.newTrigger()
                .withIdentity("trigger2", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("15 0/2 * * * ?"))
                .build();
        ft = scheduler.scheduleJob(job, trigger);
        logger.info(job.getKey().getName() + " 将在: "
                + dateFormat.format(ft) + " 运行 . 并且基于Cron表达式 : "
                + trigger.getCronExpression() + "  (含义:偶数分钟的第15秒运行)");

        // job3 从8时到17时 ,每个 偶数分钟执行一次
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job3", "group1").build();
        trigger =  TriggerBuilder.newTrigger()
                .withIdentity("trigger3", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/2 8-17 * * ?"))
                .build();
        ft = scheduler.scheduleJob(job, trigger);
        logger.info(job.getKey().getName() + " 将在: "
                + dateFormat.format(ft) + " 运行 . 并且基于Cron表达式 : "
                + trigger.getCronExpression() + "  (含义:从8时到17时 ,每个 偶数分钟执行一次)");

        // job4 从17时到23时,每3分钟运行一次
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job4", "group1").build();
        trigger =  TriggerBuilder.newTrigger().withIdentity("trigger4", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/3 17-23 * * ?")).build();
        ft = scheduler.scheduleJob(job, trigger);
        logger.info(job.getKey().getName() + " 将在: "
                + dateFormat.format(ft) + " 运行 . 并且基于Cron表达式 : "
                + trigger.getCronExpression() + "  (含义: 从17时到23时,每3分钟运行一次)");

        // job5 每个月的1号和15号的上午10点 运行
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job5", "group1").build();
        trigger = TriggerBuilder.newTrigger().withIdentity("trigger5", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 10am 1,15 * ?")).build();
        ft = scheduler.scheduleJob(job, trigger);
        logger.info(job.getKey().getName() + " 将在: "
                + dateFormat.format(ft) + " 运行 . 并且基于Cron表达式 : "
                + trigger.getCronExpression() + "  (含义:每个月的1号和15号的上午10点 运行)");

        // job6 周一至周五,每30秒运行一次
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job6", "group1").build();
        trigger = TriggerBuilder.newTrigger().withIdentity("trigger6", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0,30 * * ? * MON-FRI")).build();
        ft = scheduler.scheduleJob(job, trigger);
        logger.info(job.getKey().getName() + " 将在: "
                + dateFormat.format(ft) + " 运行 . 并且基于Cron表达式 : "
                + trigger.getCronExpression() + "  (含义:周一至周五,每30秒运行一次 )");

        // job7 周六,周日 每30秒运行
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job7", "group1").build();
        trigger = TriggerBuilder.newTrigger().withIdentity("trigger7", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0,30 * * ? * SAT,SUN")).build();
        ft = scheduler.scheduleJob(job, trigger);
        logger.info(job.getKey().getName() + " 将在: "
                + dateFormat.format(ft) + " 运行 . 并且基于Cron表达式 : "
                + trigger.getCronExpression() + "  (含义:周六,周日  每30秒运行 )");


        scheduler.start();

        logger.info("------- 开始调度 (调用.start()方法) ----------------");
        logger.info("------- 等待5分钟,给任务的调度留出时间  ... ------------");
        try {
            Thread.sleep(300L * 1000L);
        } catch (Exception e) {
        }

        scheduler.shutdown(true);
        logger.info("------- 调度已关闭 ---------------------");

        // 显示一下 已经执行的任务信息
        SchedulerMetaData metaData = scheduler.getMetaData();
        logger.info("~~~~~~~~~~  执行了 "
                + metaData.getNumberOfJobsExecuted() + " 个 jobs.");
    }
}
