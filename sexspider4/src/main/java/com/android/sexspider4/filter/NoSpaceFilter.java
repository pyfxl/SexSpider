package com.android.sexspider4.filter;

/**
 * Created by fengxiangling on 2017/5/6.
 */

public class NoSpaceFilter implements IFilter {
    @Override
    public String doFilter(String str) {
        str = str.replaceAll("(?<=^|>)[^<>]+(?=<|$)", "");
        str = str.replace("\n", "");

        return str;
    }
}
