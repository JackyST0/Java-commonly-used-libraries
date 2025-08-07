package com.example.testspringboot.date;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/12/24 10:49
 */
public class Hutool {

    public static void main(String[] args) {
        // 获取当前的日期和时间
        String now = DateUtil.now();
        System.out.println(now);

        // 格式化日期和时间
        String dateStr = DateUtil.formatDateTime(DateUtil.date());
        System.out.println(dateStr);

        // 解析日期和时间字符串
        Date date = DateUtil.parse(dateStr);
        System.out.println(date);
    }
}
