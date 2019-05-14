package com.kevin.quatrz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by NoisyFish on 2018-09-10.
 */
public class HelloJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(HelloJob.class.getName());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("------- Hello World! - " + new Date());
    }

}
