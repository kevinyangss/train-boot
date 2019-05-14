package com.kevin.quatrz;

import org.apache.commons.lang3.time.FastDateFormat;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Quartz 的 Misfire处理规则:

 调度(scheduleJob)或恢复调度(resumeTrigger,resumeJob)后不同的misfire对应的处理规则
 CronTrigg
 CronTrigger

 withMisfireHandlingInstructionDoNothing
 ——不触发立即执行
 ——等待下次Cron触发频率到达时刻开始按照Cron频率依次执行

 withMisfireHandlingInstructionIgnoreMisfires
 ——以错过的第一个频率时间立刻开始执行
 ——重做错过的所有频率周期后
 ——当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行

 withMisfireHandlingInstructionFireAndProceed
 ——以当前时间为触发频率立刻触发一次执行
 ——然后按照Cron频率依次执行


 SimpleTrigger

 withMisfireHandlingInstructionFireNow
 ——以当前时间为触发频率立即触发执行
 ——执行至FinalTIme的剩余周期次数
 ——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
 ——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值

 withMisfireHandlingInstructionIgnoreMisfires
 ——以错过的第一个频率时间立刻开始执行
 ——重做错过的所有频率周期
 ——当下一次触发频率发生时间大于当前时间以后，按照Interval的依次执行剩下的频率
 ——共执行RepeatCount+1次

 withMisfireHandlingInstructionNextWithExistingCount
 ——不触发立即执行
 ——等待下次触发频率周期时刻，执行至FinalTime的剩余周期次数
 ——以startTime为基准计算周期频率，并得到FinalTime
 ——即使中间出现pause，resume以后保持FinalTime时间不变


 withMisfireHandlingInstructionNowWithExistingCount
 ——以当前时间为触发频率立即触发执行
 ——执行至FinalTIme的剩余周期次数
 ——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
 ——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值

 withMisfireHandlingInstructionNextWithRemainingCount
 ——不触发立即执行
 ——等待下次触发频率周期时刻，执行至FinalTime的剩余周期次数
 ——以startTime为基准计算周期频率，并得到FinalTime
 ——即使中间出现pause，resume以后保持FinalTime时间不变

 withMisfireHandlingInstructionNowWithRemainingCount
 ——以当前时间为触发频率立即触发执行
 ——执行至FinalTIme的剩余周期次数
 ——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到

 ——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值

 MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT
 ——此指令导致trigger忘记原始设置的starttime和repeat-count
 ——触发器的repeat-count将被设置为剩余的次数
 ——这样会导致后面无法获得原始设定的starttime和repeat-count值
 * Created by yss on 2018-09-10.
 */
public class ExecuteMisfireJobApp {

    private static final Logger logger = LoggerFactory.getLogger(CronJob.class.getName());

    public static void main(String[] args) throws SchedulerException {
        new ExecuteMisfireJobApp().run();
    }

    public void run() throws SchedulerException {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy 年 MM 月 dd 日  HH 时 mm 分 ss 秒");
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();

        // 下一个第15秒
        Date startTime = nextGivenSecondDate(null, 15);

        // statefulJob1 每3s运行一次,但它会延迟10s
        JobDetail job = newJob(StatefulJob.class)
                .withIdentity("statefulJob1", "group1")
                .usingJobData(StatefulJob.EXECUTION_DELAY, 10000L) // 设置参数:睡眠时间 10s
                .build();
        SimpleTrigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(startTime)
                .withSchedule(
                        simpleSchedule().withIntervalInSeconds(3)
                                .repeatForever()).build();
        Date ft = scheduler.scheduleJob(job, trigger);
        logger.info(job.getKey().getName() + " 将在: " + dateFormat.format(ft)
                + "  时运行.并且重复: " + trigger.getRepeatCount() + " 次, 每次间隔 "
                + trigger.getRepeatInterval() / 1000 + " 秒");

        // statefulJob2 将每3s运行一次 , 但它将延迟10s , 然后不断的迭代
        job = newJob(StatefulJob.class)
                .withIdentity("statefulJob2", "group1")
                .usingJobData(StatefulJob.EXECUTION_DELAY, 10000L)// 设置参数:睡眠时间 10s
                .build();
        trigger = newTrigger()
                .withIdentity("trigger2", "group1")
                .startAt(startTime)
                .withSchedule(
                        simpleSchedule()
                                .withIntervalInSeconds(3)
                                .repeatForever()
                                // 设置错失触发后的调度策略
                                .withMisfireHandlingInstructionNowWithRemainingCount()
                )
                .build();

        ft = scheduler.scheduleJob(job, trigger);
        System.out.println(job.getKey().getName() + " 将在: " + dateFormat.format(ft)
                + "  时运行.并且重复: " + trigger.getRepeatCount() + " 次, 每次间隔 "
                + trigger.getRepeatInterval() / 1000 + " 秒");

        System.out.println("------- 开始调度 (调用.start()方法) ----------------");
        scheduler.start();

        // 给任务留时间运行
        try {
            Thread.sleep(600L * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        scheduler.shutdown(true);
        System.out.println("------- 调度已关闭 ---------------------");

        // 显示一下 已经执行的任务信息
        SchedulerMetaData metaData = scheduler.getMetaData();
        System.out.println("~~~~~~~~~~  执行了 "
                + metaData.getNumberOfJobsExecuted() + " 个 jobs.");
    }
}
