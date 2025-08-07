package com.example.testspringboot.date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/12/24 10:50
 */
public class ApacheCommonsLang {

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println("当前时间是：" + date);

        Date tomorrow = DateUtils.addDays(date, 1);
        System.out.println("明天的这个时间是：" + tomorrow);

        String strDate = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
        System.out.println("格式化后的时间是：" + strDate);
    }

}
