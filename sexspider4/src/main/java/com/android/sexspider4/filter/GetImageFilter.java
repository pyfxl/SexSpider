package com.android.sexspider4.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fengxiangling on 2017/5/6.
 */

public class GetImageFilter implements IFilter {
    @Override
    public String doFilter(String str) {
        Map<String, String> map = new HashMap<String, String>();
        String role = "([^\\s=]+)=(['\"\"\\s]?)([^'\"\"]+)\\2(?=\\s|$|>)";
        Pattern pattern = Pattern.compile(role);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            map.put(matcher.group(1), matcher.group(3));
        }

        List<String> lists = new ArrayList<String>();
        lists.add("file");
        lists.add("data-original");
        lists.add("data-src");
        lists.add("src");

        for(String s : lists) {
            if(map.containsKey(s)) return map.get(s);
        }

        return "";
    }
}
