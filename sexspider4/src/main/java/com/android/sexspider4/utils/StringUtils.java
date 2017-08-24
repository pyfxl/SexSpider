package com.android.sexspider4.utils;

/**
 * Created by feng on 2017/5/5.
 */

public class StringUtils {

    private StringUtils() { }

    //判断字符串是否为空
    public static boolean isEmpty(String str) {
        return str == null || str.trim().equals("");
    }

    //从左边开始截取字符串
    public static String substringLeft(String str, String s) {
        if (isEmpty(str)) return "";

        int i = str.indexOf(s);
        if (i == -1) return str;

        return str.substring(0, i);
    }

    //从右边开始截取len个字符串
    public static String removeStringRight(String str, int len) {
        if (isEmpty(str)) return "";

        return str.substring(0, str.length()-len);
    }

    //取字符长度中文2英文1
    public static int strLength(String str) {
        int len = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i=0; i<str.length(); i++) {
            String s = str.substring(i, i + 1);
            len = s.matches(chinese) ? len + 2 : len + 1;
        }

        return len;
    }

}
