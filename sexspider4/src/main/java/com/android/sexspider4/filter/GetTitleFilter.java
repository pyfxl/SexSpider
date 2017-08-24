package com.android.sexspider4.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fengxiangling on 2017/5/6.
 */

public class GetTitleFilter implements IFilter {
    @Override
    public String doFilter(String str) {
        Map<String, String> map = new HashMap<>();
        String role = "([^\\s=]+)=(['\"\"\\s]?)([^'\"\"]+)\\2(?=\\s|$|>)";
        Pattern pattern = Pattern.compile(role);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            map.put(matcher.group(1), matcher.group(3));
        }

        return map.containsKey("title") ? map.get("title") : map.get("alt");
    }
}
