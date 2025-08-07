package com.example.testspringboot.entity.bo;

import lombok.Getter;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/9/3 13:56
 */
@Getter
public class Counter2 {
    // ReentrantLock
    private final ReentrantLock lock = new ReentrantLock();
    private int count = 0;

    public void increment() {
        lock.lock();  // 获取锁
        try {
            count++;
        } finally {
            lock.unlock();  // 释放锁
        }
    }
}
