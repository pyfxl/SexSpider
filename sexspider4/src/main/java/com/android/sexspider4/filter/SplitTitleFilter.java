package com.android.sexspider4.filter;

/**
 * Created by fengxiangling on 2017/5/6.
 */

public class SplitTitleFilter implements IFilter {
    @Override
    public String doFilter(String str) {
        try {
            return str.substring(0, str.indexOf("】")) + "】";
        } catch (Exception e) {
            return str;
        }
    }
}
