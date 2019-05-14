package com.kevin.quatrz;

import org.apache.commons.lang3.time.FastDateFormat;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * Created by NoisyFish on 2018-09-10.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ColorJob implements Job{
    private static final Logger logger = LoggerFactory.getLogger(ColorJob.class.getName());

    // 静态变量
    public static final String FAVORITE_COLOR = "favorite color";
    public static final String EXECUTION_COUNT = "count";
    // Quartz 将每次将会重新实例化对象 ，非静态的成员变量不能用来保持状态
    private int _counter = 1;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobName = context.getJobDetail().getKey().getName();
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy 年 MM 月 dd 日  HH 时 mm 分 ss 秒");
        String runTime = dateFormat.format(Calendar.getInstance().getTime());

        JobDataMap data = context.getJobDetail().getJobDataMap();
        String favoriteColor  = data.getString(FAVORITE_COLOR);
        int count = data.getInt(EXECUTION_COUNT);

        logger.info("ColorJob: " + jobName + " 在 " + runTime + " 执行了 ...  \n"
                        + "      喜欢的颜色是：  " + favoriteColor + "\n"
                        + "      执行次数统计(from job jobDataMap)： " + count + "\n"
                        + "      执行次数统计( from job 类的成员变 量 ): "
                        + _counter+ " \n ");
        // 每次+1 并放回Map 中
        count++;
        data.put(EXECUTION_COUNT, count);
        // 成员变量的增加没有意义，每次实例化对象的时候会 同时初始化该变量
        _counter++;
    }
}
