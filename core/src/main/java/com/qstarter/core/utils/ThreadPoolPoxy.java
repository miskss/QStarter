package com.qstarter.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * create at 2021/11/23 14:23
 *
 * @author peter
 */
@Slf4j
public final class ThreadPoolPoxy implements Executor {

    private final static AtomicInteger TOTAL = new AtomicInteger(0);
    public final int capacity;
    private final ConcurrentLinkedQueue<Runnable> waitQueue = new ConcurrentLinkedQueue<>();
    private final LinkedBlockingQueue<Runnable> workQueue;
    private final AtomicInteger threadNumber;

    private final ExecutorService pool;
    private final int poolIdx;
    private final String poolName;
    private Thread daemon;


    public ThreadPoolPoxy(String poolName, int capacity) {
        this.capacity = capacity;
        this.workQueue = new LinkedBlockingQueue<>(this.capacity);
        this.poolName = poolName;
        this.threadNumber = new AtomicInteger();
        this.poolIdx = TOTAL.incrementAndGet();
        int processors = Runtime.getRuntime().availableProcessors();
        pool = new ThreadPoolExecutor(Math.max(processors / 6, 2),
                processors * 2,
                10L, TimeUnit.SECONDS,
                workQueue,
                getThreadFactory(poolName),
                new ThreadPoolExecutor.CallerRunsPolicy() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                        waitQueue.add(r);
                        initDaemon();
                    }
                });


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            String name = this.poolName + "-" + this.poolIdx;
            log.info("准备关闭线程池【{}】。。。。", name);
            try {
                this.pool.shutdown();
                if (this.daemon != null && this.daemon.isAlive() && !this.daemon.isInterrupted()) {
                    this.daemon.interrupt();
                }
                boolean b = this.pool.awaitTermination(10, TimeUnit.SECONDS);
                if (!b) this.pool.shutdownNow();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                this.pool.shutdownNow();
            }
            log.info("关闭线程池【{}】", name);

        }));
    }

    public void initDaemon() {
        if (this.daemon == null || !this.daemon.isAlive()) {
            synchronized (this) {
                if (this.daemon == null || !this.daemon.isAlive()) {
                    this.daemon = new Thread(daemon());
                    this.daemon.setDaemon(true);
                    this.daemon.setName(poolName + "-" + this.poolIdx + "-daemon");
                    this.daemon.start();
                }
            }
        }
    }

    public Runnable daemon() {
        return () -> {
            for (; ; ) {
                if (waitQueue.size() == 0) {
                    return;
                }
                if (workQueue.size() == capacity) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                Runnable poll = waitQueue.poll();
                if (poll != null) {
                    pool.execute(poll);
                    try {
                        TimeUnit.MILLISECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        };
    }


    public Future<?> submit(Runnable r) {
        return pool.submit(r);
    }

    public void execute(Runnable r) {
        pool.execute(r);
    }

    private ThreadFactory getThreadFactory(String poolName) {
        return r -> {
            Thread t = new Thread(r,
                    poolName + "-" + this.poolIdx + "-" + threadNumber.getAndIncrement()
            );
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        };
    }

    public static void main(String[] args) throws InterruptedException {
        TimeUnit.SECONDS.sleep(20);
        System.out.println("start");
        ThreadPoolPoxy test = new ThreadPoolPoxy("test", 10);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (int i = 0; i < 2000; i++) {
            test.execute(() -> {
                log.info(Thread.currentThread().getName());
                atomicInteger.incrementAndGet();
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        TimeUnit.SECONDS.sleep(10);

        for (int i = 0; i < 2000; i++) {
            test.execute(() -> {
                log.info(Thread.currentThread().getName());
                atomicInteger.incrementAndGet();
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        TimeUnit.SECONDS.sleep(5);

        for (int i = 0; i < 20000; i++) {
            test.execute(() -> {
                log.info(Thread.currentThread().getName());
                atomicInteger.incrementAndGet();
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        TimeUnit.SECONDS.sleep(100);
        System.out.println("end");
        System.out.println(atomicInteger.get());
        System.exit(0);
    }
}
