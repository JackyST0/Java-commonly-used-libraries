package com.example.testspringboot.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/1/9 17:02
 */
public class DateUtil {

    public final static  String PATTERN_YEAR_MONTH = "yyyy-MM";
    public final static  String PATTERN_ALL = "yyyy-MM-dd HH:mm:ss";

    private static final String timeUrl = "http://47.112.12.109:8443/user-service/custom/time";

    public static void main(String[] args) throws Exception {
        String format = format(now(), PATTERN_YEAR_MONTH);
        System.out.println(format);
    }

    /**
     * 当前时间
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 将时间转换为时间戳
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    /**
     * 计算给定日期的下一个月的日期
     */
    public static String getNextMonth(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.MONTH, 1);
        // 从现在算，之前一个月,如果是2个月，那么-1-----》改为-2
        Date dateFrom = cl.getTime();
        return sdf.format(dateFrom);
    }

    /**
     * 获取前一天
     */
    public static String getLastDay(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.DAY_OF_MONTH, -1);
        return sdf.format(cl.getTime());
    }

    /**
     * 计算给定日期的前两天的日期
     */
    public static String getTwoDaysAgo(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.DAY_OF_MONTH, -2);
        return sdf.format(cl.getTime());
    }

    /**
     * 将时间转换为时间戳
     */
    public static Date parseDatetime(String dateStr) {
        return parse(dateStr, PATTERN_ALL);
    }
    public static Date parse(String dateStr, String pattern) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 计算给定日期的前一个月的日期
     */
    public static String lastMonth(String data, String pattern) {
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date date = sdf.parse(data);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -1);
            result = sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 计算给定日期的前一个月的日期
     */
    public static String lastMonth(Date date, String pattern) {
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -1);
            result = sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将时间转换为时间戳
     */
    public static String thisEnglishMonth(String data, String pattern) {
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date date = sdf.parse(data);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMM", Locale.ENGLISH);
            result = sdf2.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取上月的第n天
     * num: 1 表第一天 ，其他为最后一天
     * @return
     */
    public static String getLastMonthDay(String dataFormat, Date date, int num) {
        //获取当前日期
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if(num == 1) {
            c.add(Calendar.MONTH, -1);
            c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        } else {
            c.set(Calendar.DAY_OF_MONTH, 0);
        }
        return new SimpleDateFormat(dataFormat).format(c.getTime());
    }

    /**
     * 获取上周的第n天
     * num: 1 表周一 ，其他为周日
     * @return
     */
    public static String getLastWeekDay(String dataFormat, Date date,int num ) {
        //获取当前日期
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if(num == 1) {
            c.add(Calendar.DATE, -7);
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //上周周一
        } else {
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); //上周周日
        }
        return new SimpleDateFormat(dataFormat).format(c.getTime());
    }

    /**
     * 将毫秒（Java下时间戳）转换为日期格式，指定格式
     */
    public static String msToDate(long _ms, String format) {
        Date date = new Date(_ms);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    /**
     * 从指定url获取时间
     */
    public static String getNetTime() throws Exception {
        URL url = new URL(timeUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder result = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            result.append(inputLine);
        }

        in.close();
        conn.disconnect();
        return result.toString();
    }

    /**
     * 获取每个星期四的日期
     */
    public static String getThisWeekTHURSDAY(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.THURSDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        return df.format(cal.getTime());
    }

    /**
     * 格式化某时间
     */
    public static String formatDate(String format, Date date) {
        // 设置日期格式
        return format(date, format);
    }
}
