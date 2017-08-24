package com.android.sexspider4.filter;

/**
 * Created by fengxiangling on 2017/5/6.
 */

public class Site55_Filter implements IFilter {
    @Override
    public String doFilter(String str) {
        return str.replace("-lp", "");
    }
}
