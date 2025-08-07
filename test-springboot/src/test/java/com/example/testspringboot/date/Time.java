package com.example.testspringboot.date;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/12/24 10:48
 */
public class Time {

    public static void main(String[] args) {
        // 获取当前的日期、时间、日期和时间
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        LocalDateTime dateTime = LocalDateTime.now();
        System.out.println(date);
        System.out.println(time);
        System.out.println(dateTime);

        // 格式化日期和时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTimeString = dateTime.format(formatter);
        System.out.println(dateTimeString);

        // 解析日期和时间字符串
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTimeString, formatter);
        System.out.println(parsedDateTime);
    }
}
