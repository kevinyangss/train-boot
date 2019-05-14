package com.kevin.quatrz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by NoisyFish on 2018-08-31.
 */
@Component
public class ScheduledTest {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTest.class.getName());

    @Scheduled(cron="0 0/1 8-20 * * ?")
    public void executeFileDownloadTask(){
        // 间隔2分钟,执行工单上传任务
        Thread thread = Thread.currentThread();
        System.out.println("定时任务1：" + thread.getName());
        logger.info("ScheduledTest.executeFileDownLoadTask 定时任务1:"+thread.getId()+ ",name:"+thread.getName());
    }

    @Scheduled(cron="0 0/1 8-20 * * ?")
    public void executeUploadTask() {
        // 间隔1分钟,执行工单上传任务
        Thread current = Thread.currentThread();
        System.out.println("定时任务2:"+current.getId());
        logger.info("ScheduledTest.executeUploadTask 定时任务2:"+current.getId() + ",name:"+current.getName());
    }

    @Scheduled(cron="0 0/1 5-23 * * ?")
    public void executeUploadBackTask() {
        // 间隔3分钟,执行工单上传任务
        Thread current = Thread.currentThread();
        System.out.println("定时任务3:"+current.getId());
        logger.info("ScheduledTest.executeUploadBackTask 定时任务3:"+current.getId()+ ",name:"+current.getName());
    }
}
