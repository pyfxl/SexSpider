package com.android.sexspider4.filter;

public class GetM3u8Filter implements IFilter {
    @Override
    public String doFilter(String str) {
        try {
            str = str.replace("'", "\"");
            str = str.replaceAll("<.+?>", "");

            int pos = str.indexOf(".m3u8");
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