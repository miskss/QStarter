package com.qstarter.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

/**
 * 配置@Scheduled定时任务的执行的线程池
 *
 * @author peter
 * date: 2019-11-22 08:50
 **/
@Configuration
public class ScheduledTaskConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(ScheduledThreadPool.scheduler());
    }
}
