package com.example.testspringboot.thread;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/8/27 16:41
 */

public class ThreadTests {

    @Test
    public void fixedThreadPool() {
        // 创建一个固定数目的、可重用的线程。
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> System.out.println(Thread.currentThread().getName() + " is running."));
        }
        executorService.shutdown();
    }

    @Test
    public void cachedThreadPool() {
        //  创建一个可根据需要创建新线程的线程池。
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> System.out.println(Thread.currentThread().getName() + " is running."));
        }
        executorService.shutdown();
    }

    @Test
    public void scheduledThreadPool() throws InterruptedException {
        // 创建一个可以延迟启动，定时启动的线程池。
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        executorService.schedule(() -> System.out.println(Thread.currentThread().getName() + " is running."), 3, TimeUnit.SECONDS);
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    @Test
    public void singleThreadExecutor() {
        // 创建一个只有一个线程的线程池。
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> System.out.println(Thread.currentThread().getName() + " is running."));
        executorService.shutdown();
    }

    @Test
    public void CustomThreadPool() {
        // 创建自定义线程池。
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5, // 核心线程数
                10, // 最大线程数
                60, // 空闲线程存活时间
                TimeUnit.SECONDS, // 时间单位
                new ArrayBlockingQueue<>(100), // 任务队列
                new ThreadPoolExecutor.CallerRunsPolicy() // 饱和策略
        );
        // 提交任务
        for (int i = 0; i < 10; i++) {
            final int task = i;
            executor.execute(() -> System.out.println("Executing task " + task + " by " + Thread.currentThread().getName()));
        }
        // 关闭线程池
        executor.shutdown();
    }

    @Test
    public void NoCountDownLatch() {
        int taskCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        System.out.println(Thread.currentThread().getName() + " is running.");

        for (int i = 0; i < taskCount; i++) {
            executorService.submit(() -> {
                // 执行子任务
                System.out.println(Thread.currentThread().getName() + " is running.");
            });
        }

        // 执行另一个任务
        System.out.println(Thread.currentThread().getName() + " is running.");
        executorService.shutdown();
    }

    @Test
    public void CountDownLatch() throws InterruptedException {
        int taskCount = 10;
        // 使用锁存器阻塞线程
        CountDownLatch latch = new CountDownLatch(taskCount);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        System.out.println(Thread.currentThread().getName() + " is running.");

        for (int i = 0; i < taskCount; i++) {
            executorService.submit(() -> {
                // 执行子任务
                System.out.println(Thread.currentThread().getName() + " is running.");
                latch.countDown();
            });
        }

        latch.await();  // 等待所有子任务完成

        // 执行另一个任务
        System.out.println(Thread.currentThread().getName() + " is running.");
        executorService.shutdown();
    }

    @Test
    public void Semaphore() {
        // 创建一个拥有10个线程的线程池
        ExecutorService executor = Executors.newFixedThreadPool(10);
        // 创建一个Semaphore实例
        Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    // 获取许可
                    semaphore.acquire();

                    // 模拟耗时操作
                    System.out.println(Thread.currentThread().getName() + " is working");
                    Thread.sleep(2000);

                    // 释放许可
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // 关闭线程池
        executor.shutdown();
    }
}
