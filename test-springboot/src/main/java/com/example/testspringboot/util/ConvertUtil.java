package com.example.testspringboot.util;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/12/24 10:50
 */
public class ConvertUtil {
    public static void main(String[] args) {
        String s = hexStr2Str("48656C6C6F");
        System.out.println(s);

        String s1 = strToUnicode("你好");
        System.out.println(s1);
    }

    /**
     * byte[]转换成16进制字符串
     */
    public static String convert2Hexstring(byte[] bs) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder();
        int bit;
        for (byte b : bs) {
            bit = (b & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = b & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }

    /**
     * 字符串转成16进制字符串
     */
    public static byte[] hexStr2Bytes(String src) {
        int m, n;
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = Byte.decode("0x" + src.substring(i * 2, m) + src.substring(m, n));
        }
        return ret;
    }

    /**
     * 16进制字符串转成字符串
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * String的字符串转换成unicode的String
     */
    public static String strToUnicode(String strText) {
        char c;
        StringBuilder str = new StringBuilder();
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++) {
            c = strText.charAt(i);
            intAsc = c;
            strHex = Integer.toHexString(intAsc);
            if (intAsc > 128) {
                str.append("\\u" + strHex);
            }
            else{

            }
        }
        return str.toString();
    }
}
