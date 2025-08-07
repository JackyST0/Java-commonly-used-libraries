package com.example.testspringboot.date;

import java.util.Calendar;
import java.util.Date;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/12/24 10:47
 */
public class DataAndCalendar {

    public static void main(String[] args) {
        // 创建一个Date对象，表示当前时间
        Date date = new Date();
        System.out.println(date);

        // 使用Calendar获取当前时间的年、月、日
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;  // 注意：月份是从0开始的
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, 1); // 加一天
        System.out.println(year + "-" + month + "-" + day);
        System.out.println("明天的这个时间是：" + calendar.getTime());
    }
}
