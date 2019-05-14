package com.kevin.quatrz;

import org.apache.commons.lang3.time.FastDateFormat;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * SimpleTrigger
 * Created by NoisyFish on 2018-09-10.
 */
public class ExecuteSimpleTriggerApp {

    private static final Logger logger = LoggerFactory.getLogger(SimpleJob.class.getName());

    public static void main(String[] args) throws SchedulerException {
        new ExecuteSimpleTriggerApp().run();
    }

    public void  run() throws SchedulerException {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy 年 MM 月 dd 日  HH 时 mm 分 ss 秒");

        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();

        Date startTime = DateBuilder.nextGivenSecondDate(null, 15);

        // job1 将只会执行一次
        JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("job1", "group1").build();
        SimpleTrigger trigger = (SimpleTrigger )TriggerBuilder
                .newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(startTime)
                .build();
        // 把job1 和 trigger加入计划   .  ft:此任务要执行的时间
        Date ft = scheduler.scheduleJob(job, trigger);
        logger.info(job.getKey().getName() + " 将在 : " + dateFormat.format(ft) + " 时运行.并且重复: "
                + trigger.getRepeatCount() + " 次, 每次间隔 "
                + trigger.getRepeatInterval() / 1000 + " 秒");

        // job2 将只会和执行一次(和job1一样一样的,吼~~)
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job2", "group1").build();
        trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity("trigger2", "group1").startAt(startTime).build();
        ft = scheduler.scheduleJob(job, trigger);
        logger.info(job.getKey().getName() + " 将在 : " + dateFormat.format(ft) + " 时运行.并且重复: "
                + trigger.getRepeatCount() + " 次, 每次间隔 "
                + trigger.getRepeatInterval() / 1000 + " 秒");

        // job3 将执行11次(执行1次,重复10次) ,每10秒重复一次
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job3", "group1").build();
        trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("trigger3", "group1")
                .startAt(startTime)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(10).withMisfireHandlingInstructionFireNow()
                ).build();
        ft = scheduler.scheduleJob(job, trigger);
        logger.info(job.getKey().getName()+ " 将在 : " + dateFormat.format(ft) + " 时运行.并且重复: "
                + trigger.getRepeatCount() + " 次, 每次间隔 "
                + trigger.getRepeatInterval() / 1000 + " 秒");

        // trigger3 改变了.  每隔10s重复.共重复2次
        // 此处说明 , 上面job3已经 设定了 trigger3 重复10次,每次10s
        //        在这里又改变了 trigger3的设置,不会对以前构成影响,而是当做一个新的来处理
        trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger3", "group2")
                .startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .withRepeatCount(2))
                .forJob(job).build();
        ft = scheduler.scheduleJob(trigger);
        logger.info(job.getKey().getName() + " 改变过trigger3属性的job3 : " + dateFormat.format(ft) + " 时运行.并且重复: "
                + trigger.getRepeatCount() + " 次, 每次间隔 "
                + trigger.getRepeatInterval() / 1000 + " 秒");

        // job5 将在5分钟后运行一次
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job5", "group1").build();
        trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity("trigger5", "group1")
                .startAt(DateBuilder.futureDate(5, DateBuilder.IntervalUnit.MINUTE)) // 设定5分钟后运行
                .build();
        ft = scheduler.scheduleJob(job, trigger);
        logger.info(job.getKey().getName()+ " 将在 : " + dateFormat.format(ft) + " 时运行.并且重复: "
                + trigger.getRepeatCount() + " 次, 每次间隔 "
                + trigger.getRepeatInterval() / 1000 + " 秒");

        // job6  每40s运行一次,没有指定重复次数,则无下限的重复
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job6", "group1").build();
        trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger6", "group1")
                .startAt(startTime)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(40)
                                .repeatForever()).build();
        ft = scheduler.scheduleJob(job, trigger);
        logger.info(job.getKey().getName() + " 将在 : " + dateFormat.format(ft) + " 时运行.并且重复: "
                + trigger.getRepeatCount() + " 次, 每次间隔 "
                + trigger.getRepeatInterval() / 1000 + " 秒");

        // 所有的任务都被加入到了 scheduler中 ,但只有 schedulers.start(); 时才开始执行
        scheduler.start();
        logger.info("------- 开始调度 (调用.start()方法) ----------------");
        logger.info("-------系统 启动 的 时间 :" + dateFormat.format(new Date()));

        // 在 scheduled.start(); 之后,还可以将 jobs 添加到执行计划中
        // job7 将重复20次 ,每5分钟重复一次
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job7", "group1").build();
        trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger7", "group1")
                .startAt(startTime)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInMinutes(5) // 5分钟
                                .withRepeatCount(20))     // 重复20次
                .build();
        ft = scheduler.scheduleJob(job, trigger);
        logger.info(job.getKey().getName() + " 将在 : " + dateFormat.format(ft) + " 时运行.并且重复: "
                + trigger.getRepeatCount() + " 次, 每次间隔 "
                + trigger.getRepeatInterval() / 1000 + " 秒");

        // job8  可以立即执行. 无trigger注册
        job = JobBuilder.newJob(SimpleJob.class).withIdentity("job8", "group1")
                .storeDurably().build();
        scheduler.addJob(job, true);// 无trigger，任务立即执行
        logger.info("手动触发  job8...(立即执行)");
        scheduler.triggerJob(JobKey.jobKey("job8", "group1"));

        logger.info("------- 等待30 秒... --------------");

        try {
            Thread.sleep(30L * 1000L);
        } catch (Exception e) { }

        // job7 将马上执行,重复10次,每秒一次
        logger.info("-------  重新安排 ... --------------------");
        trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger7", "group1")
                .startAt(startTime)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(5)
                                .withRepeatCount(20)).build();

        ft = scheduler.rescheduleJob(trigger.getKey(), trigger);
        logger.info("job7 被重新安排 在 : " + dateFormat.format(ft) +"  执行. \r   当前时间 :" + dateFormat.format(new Date())+"预定执行时间已过,任务立即执行");

        try {
            logger.info("------- 等待5分钟  ... ------------");
            Thread.sleep(300L * 1000L);
        } catch (Exception e) { }

        scheduler.shutdown(true);
        logger.info("------- 调度已关闭 ---------------------");

        // 显示一下  已经执行的任务信息
        SchedulerMetaData metaData = scheduler.getMetaData();
        logger.info("~~~~~~~~~~  执行了 " + metaData.getNumberOfJobsExecuted() + " 个 jobs.");

    }
}
