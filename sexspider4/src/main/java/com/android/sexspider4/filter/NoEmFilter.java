package com.android.sexspider4.filter;

/**
 * Created by fengxiangling on 2017/5/6.
 */

public class NoEmFilter implements IFilter {
    @Override
    public String doFilter(String str) {
        return str.replaceAll("<em[^>]*>.*</em>", "");
    }
}
