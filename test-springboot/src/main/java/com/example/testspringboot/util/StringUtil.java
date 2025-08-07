package com.example.testspringboot.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/1/9 17:02
 */
public class StringUtil {
    public static void main(String[] args) {
        System.out.println(doubleToSting(0));
    }

    /**
     * 将字符串解析为Double类型
     */
    public static Double parseDouble(String str, Double defaultValue) {
        if (isBlank(str)) return defaultValue;
        try {
            return Double.valueOf(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    /**
     * 判断字符串是否为空或者长度为0
     */
    public static boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 将double类型的数值四舍五入到小数点后两位
     */
    public static double roundingTwo(double value) {
        BigDecimal b = BigDecimal.valueOf(value);
        return b.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 将double类型的数值四舍五入到指定的小数位数
     */
    public static double rounding(double value, int scole) {
        BigDecimal b = BigDecimal.valueOf(value);
        double f1 = b.setScale(scole, RoundingMode.HALF_UP).doubleValue();
        return f1;
    }

    /**
     * 将double类型的数值四舍五入到小数点后六位，然后再四舍五入到小数点后两位，并以字符串的形式返回。
     */
    public static String doubleToSting(double value) {
        double decTwo = StringUtil.roundingTwo(StringUtil.rounding(value, 6));
        return String.format("%.2f", decTwo);
    }

    /**
     * 判断字符串是否不为空
     */
    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    /**
     * 移除字符串中的所有换行符
     */
    public static String removeAllLineBreaks(String string) {
        return string.replaceAll("[\r\n]", "");
    }
}
