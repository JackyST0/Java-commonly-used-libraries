package com.example.testspringboot.entity.bo;

import lombok.Getter;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/9/3 13:41
 */
@Getter
public class Counter1 {
    int count = 0;

    // synchronized
    public synchronized void increment() {
        count = count + 1;
    }

}
