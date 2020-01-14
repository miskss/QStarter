package com.qstarter.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author peter
 * date: 2019-09-25 16:48
 **/
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ScheduledThreadPool {

    private static ThreadPoolExecutor scheduler;

    @PostConstruct
    public void init() {
        int coreThreads = Runtime.getRuntime().availableProcessors() * 2;
        log.info("核心线程数：【{}】" , coreThreads);
        scheduler = new ThreadPoolExecutor(coreThreads, 64, 20, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000), new ScheduledThreadFactory());
    }

    public static ThreadPoolExecutor scheduler() {
        return scheduler;
    }


    static class ScheduledThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        ScheduledThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "sys-" +
                    poolNumber.getAndIncrement() +
                    "-exec-";
        }


        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

}
