# Java Date

## java.util.Date 和 java.util.Calendar
这是Java早期版本中用于处理日期和时间的主要类。Date 可以表示特定的时间（精确到毫秒），而 Calendar 则提供了处理日期和时间的一些复杂操作，例如获取特定日期的年份、月份、日等。
- ### 简单使用
    ```
    import java.util.Calendar;
    import java.util.Date;

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
    ```

## java.text.SimpleDateFormat
这是一个可以对 Date 进行格式化和解析的类，非常适合用于日期和时间的显示和用户输入。
- ### 简单使用
    ```
    import java.text.SimpleDateFormat;
    import java.util.Date;

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
    ```

## java.time
这是Java 8引入的新的日期和时间API，设计目标是要解决旧的 Date 和 Calendar API的一些问题和局限性。其中包括 LocalDate、LocalTime、LocalDateTime、ZonedDateTime、Period、Duration 等类，可以很好地处理日期和时间，以及他们之间的差值。
- ### 简单使用
    ```
    import java.time.LocalDate;
    import java.time.LocalTime;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;

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
    ```

## Joda-Time
这是一个第三方的日期和时间库，功能强大，易于使用，是Java 8的 java.time 包的灵感来源。但是，由于Java 8及以上版本已经有了自己的日期和时间API，因此，对于使用这些版本的Java的项目，一般建议使用 java.time 包，而不是 Joda-Time。
- ### 简单使用
    ```
    import org.joda.time.DateTime;
    import org.joda.time.format.DateTimeFormat;
    import org.joda.time.format.DateTimeFormatter;

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
    ```

## Apache Commons Lang
这个库中的DateUtils和DateFormatUtils类提供了一些方便的日期时间处理工具。
- ### 简单使用
    ```
    import org.apache.commons.lang3.time.DateUtils;
    import org.apache.commons.lang3.time.DateFormatUtils;

    import java.util.Date;

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
    ```

## Hutool
这是一个Java工具类库，也提供了一些处理日期和时间的工具类，例如 DateTime，和一些相关的工具方法。
- ### 简单使用
    ```
    import cn.hutool.core.date.DateUtil;

    import java.util.Date;

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
    ```