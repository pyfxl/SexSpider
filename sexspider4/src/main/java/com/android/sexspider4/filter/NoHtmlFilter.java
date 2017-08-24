package com.android.sexspider4.filter;

/**
 * Created by fengxiangling on 2017/5/6.
 */

public class NoHtmlFilter implements IFilter {
    @Override
    public String doFilter(String str) {
        return str.replaceAll("\\n", "").replaceAll("<.+?>", "").trim();
    }
}
