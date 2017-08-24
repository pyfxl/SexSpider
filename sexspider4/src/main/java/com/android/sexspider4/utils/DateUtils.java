package com.android.sexspider4.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by feng on 2017/5/5.
 */

public class DateUtils {

    private DateUtils() { }

    //取当前日期时间
    public static String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar c = Calendar.getInstance();
        return sdf.format(c.getTime());
    }

}
