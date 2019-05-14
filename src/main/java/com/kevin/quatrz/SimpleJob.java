package com.kevin.quatrz;

import org.apache.commons.lang3.time.FastDateFormat;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * Created by NoisyFish on 2018-09-10.
 */
public class SimpleJob implements Job{

    private static final Logger logger = LoggerFactory.getLogger(SimpleJob.class.getName());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobName = context.getJobDetail().getKey().getName();
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy 年 MM 月 dd 日  HH 时 mm 分 ss 秒");
        String runtime = dateFormat.format(Calendar.getInstance().getTime());

        logger.info("------- 任务 : " + jobName + " 在  " + runtime + " 执行了 ");
    }
}
