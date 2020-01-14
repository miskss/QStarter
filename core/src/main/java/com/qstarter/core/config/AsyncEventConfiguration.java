package com.qstarter.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

/**
 * @author peter
 * date: 2019-09-25 16:38
 **/
@EnableAsync
@Configuration
public class AsyncEventConfiguration implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        return ScheduledThreadPool.scheduler();
    }
}
