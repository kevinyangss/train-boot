package com.kevin.quatrz;

import org.apache.commons.lang3.time.FastDateFormat;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * Created by yss on 2018-09-10.
 */
public class StatefulJob implements Job{

    private static final Logger logger = LoggerFactory.getLogger(CronJob.class.getName());
    // 静态常量，作为任务在调用间，保持数据的键(key)
    // NUM_EXECUTIONS，保存的计数每次递增1
    // EXECUTION_DELAY，任务在执行时，中间睡眠的时间。本例中睡眠时间过长导致了错失触发
    public static final String NUM_EXECUTIONS = "NumExecutions";
    public static final String EXECUTION_DELAY = "ExecutionDelay";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobName = context.getJobDetail().getKey().getName();
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy 年 MM 月 dd 日  HH 时 mm 分 ss 秒");
        String runTime = dateFormat.format(Calendar.getInstance().getTime());
        logger.info("---" + jobName + " 在  : [" + runTime + "] 执行了!!");

        // 任务执行计数 累加
        JobDataMap map = context.getJobDetail().getJobDataMap();
        int executeCount = 0;
        if (map.containsKey(NUM_EXECUTIONS)) {
            executeCount = map.getInt(NUM_EXECUTIONS);
        }
        executeCount++;
        map.put(NUM_EXECUTIONS,executeCount);

        // 睡眠时间: 由调度类重新设置值 ,本例为 睡眠10s
        long delay = 5000L;
        if (map.containsKey(EXECUTION_DELAY)) {
            delay = map.getLong(EXECUTION_DELAY);
        }
        try{
            Thread.sleep(delay);
        }catch (Exception e){

        }
        // 睡眠醒来后，打印任务执行结束的信息
        logger.info("  -" + context.getJobDetail().getKey().getName()
                + " 完成次数  : " + executeCount );
    }
}
