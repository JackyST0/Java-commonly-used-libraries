package com.example.testspringboot.date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/12/24 10:49
 */
public class JodaTime {

    public static void main(String[] args) {
        // 获取当前的日期和时间
        DateTime dateTime = DateTime.now();
        System.out.println(dateTime);

        // 获取明天的日期和时间
        DateTime tomorrow = dateTime.plusDays(1);
        System.out.println("明天的这个时间是：" + tomorrow);

        // 格式化日期和时间
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String dateTimeString = dateTime.toString(formatter);
        System.out.println(dateTimeString);

        // 解析日期和时间字符串
        DateTime parsedDateTime = formatter.parseDateTime(dateTimeString);
        System.out.println(parsedDateTime);
    }
}
