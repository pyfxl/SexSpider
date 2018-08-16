package com.android.sexspider4.filter;

public class GetMp4Filter implements IFilter {
    @Override
    public String doFilter(String str) {
        try {
            str = str.replace("'", "\"");

            int pos = str.indexOf(".mp4");
            String fStr = str.substring(0, pos);
            String eStr = str.substring(pos);
            String fResult = fStr.substring(fStr.lastIndexOf("\"") + 1);
            String eResult = eStr.substring(0, eStr.indexOf("\""));
            return fResult.replace("\\", "") + eResult;
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
}