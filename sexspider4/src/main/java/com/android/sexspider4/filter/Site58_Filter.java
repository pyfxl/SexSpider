package com.android.sexspider4.filter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fengxiangling on 2017/5/6.
 */

public class Site58_Filter implements IFilter {
    @Override
    public String doFilter(String str) {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(str);
            result = jsonObject.get("title") + "," + jsonObject.get("htmlname") + ",," + jsonObject.get("startcol");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
