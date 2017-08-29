package com.android.sexspider4.filter;

/**
 * Created by feng on 2017/8/29.
 */

public class GetJpgFilter implements IFilter {
    @Override
    public String doFilter(String str) {
        return str.replaceAll("\\?.*?([^\"]+)", "");
    }
}
