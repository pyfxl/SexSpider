package com.android.sexspider4.filter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by fengxiangling on 2017/5/6.
 */

public class Site55_Filter implements IFilter {
    @Override
    public String doFilter(String str) {
        Document doc = Jsoup.parse(str);
        Elements content = doc.select("img");
        str = content.get(0).attr("src");

        return str.replace("-lp", "");
    }
}
