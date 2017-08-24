package com.android.sexspider4.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengxiangling on 2017/5/6.
 */

public class FilterChain {
    private List<IFilter> list;

    public FilterChain() {
        this.list = new ArrayList<IFilter>();
    }

    public FilterChain(String str) {
        this.list = new ArrayList<IFilter>();
        String[] cls = str.split(",");
        for(String s : cls) {
            try {
                if(s.equals("")) continue;

                Class<?> c = Class.forName("com.android.sexspider4.filter." + s);
                IFilter f = (IFilter)c.newInstance();

                this.list.add(f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addFilter(IFilter filter) {
        this.list.add(filter);
    }

    public String doFilter(String str) {
        for(IFilter filter : list) {
            str = filter.doFilter(str);
        }
        return str;
    }

    public int Count() {
        return this.list.size();
    }
}
