package com.android.sexspider4.filter;

/**
 * Created by fengxiangling on 2017/5/6.
 */

public class NoSpanFilter implements IFilter {
    @Override
    public String doFilter(String str) {
        return str.replaceAll("<span[^>]*>.*</span>", "");
    }
}
