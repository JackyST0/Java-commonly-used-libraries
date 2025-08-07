package com.example.testspringboot;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.example.testspringboot.util.JedisPoolUtil;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/9/25 13:59
 */
public class Tests {

    @Test
    public void test1() {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Semaphore semaphore = new Semaphore(3); // 每个线程最多只能处理三个任务

        for (int i = 0; i < 7; i++) {
            executor.submit(() -> {
                try {
                    semaphore.acquire(); // 请求许可
                    System.out.println("Processing task by " + Thread.currentThread().getName());
                    Thread.sleep(10000); // 模拟任务处理时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release(); // 释放许可
                }
            });
        }

        executor.shutdown();
        while (true) {

        }
    }

    @Test
    public void test2() {
        JedisPoolUtil.get("name");
        JedisPoolUtil.put("name", 60L, "tjx");
    }

    @Test
    public void test3() throws SQLException {
        Db.use().insert(
                Entity.create("test")
                        .set("name", "tjx")
        );
    }
}
