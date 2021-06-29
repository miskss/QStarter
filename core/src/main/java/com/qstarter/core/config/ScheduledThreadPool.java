package com.qstarter.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author peter
 * date: 2019-09-25 16:48
 **/
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ScheduledThreadPool {

    public static final long MAX_KEEP_ALIVE_TIME = 20L;
    public static final int MAX_QUEUE_CAPACITY = 1500;
    //可执行普通的Runnable 和Callable
    private final static ExecutorService executor;
    //可执行异步周期性任务
    private final static ScheduledExecutorService scheduler;

    static {
        try {
            int availableProcessors = Runtime.getRuntime().availableProcessors();
            int coreThreads = availableProcessors * 2;
            log.info("核心线程数：【{}】", coreThreads);
            executor = new ThreadPoolExecutor(coreThreads, coreThreads + 5, MAX_KEEP_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<>(MAX_QUEUE_CAPACITY), new ScheduledThreadFactory());
            scheduler = new ScheduledThreadPoolExecutor((availableProcessors - 4) <= 0 ? 1 : (coreThreads - 4), new ScheduledThreadFactory("-sche-"));

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    log.info("ThreadPool shutting down.");
                    log.info("executor线程池 使用的最大线程数量为【{}】", ((ThreadPoolExecutor) executor).getLargestPoolSize());
                    log.info("scheduler线程池 使用的最大线程数量为【{}】", ((ScheduledThreadPoolExecutor) scheduler).getLargestPoolSize());

                    executor.shutdown();
                    scheduler.shutdown();
                    try {
                        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                            log.error("线程池 executor  shutdown immediately due to wait timeout.");
                            executor.shutdownNow();
                        }
                        if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
                            log.error("线程池  scheduler shutdown immediately due to wait timeout.");
                            scheduler.shutdownNow();
                        }
                    } catch (InterruptedException e) {
                        log.error("线程池 executor scheduler shutdown interrupted.");
                        executor.shutdownNow();
                        scheduler.shutdownNow();
                    }
                    log.info("线程池 shutdown complete.");
                }
            }));
        } catch (Exception e) {
            log.error("ThreadPool init error.", e);
            throw new ExceptionInInitializerError(e);
        }
    }


    public static ExecutorService executor() {
        return executor;
    }

    public static ScheduledExecutorService scheduler() {
        return scheduler;
    }

    static class ScheduledThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        ScheduledThreadFactory() {
            this("-exec-");
        }


        ScheduledThreadFactory(String name) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "sys-" +
                    poolNumber.getAndIncrement() +
                    name;
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
