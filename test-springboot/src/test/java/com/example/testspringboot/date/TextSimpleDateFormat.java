package com.example.testspringboot.date;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/12/24 10:48
 */
public class TextSimpleDateFormat {

    public static void main(String[] args) throws Exception {
        // 创建一个Date对象，表示当前时间
        Date date = new Date();

        // 格式化Date对象
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(date);
        System.out.println(dateString);

        // 解析日期字符串
        Date parsedDate = format.parse(dateString);
        System.out.println(parsedDate);
    }
}
