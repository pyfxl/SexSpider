package com.android.sexspider4.filter;

public class GetFlashFilter implements IFilter {
    @Override
    public String doFilter(String str) {
        try {
            //str = str.replace("'", "\"");
            //str = str.replaceAll("<.+?>", "");

            int ePos = str.indexOf(".m3u8");
            int sPos = str.indexOf("video=");
            return str.substring(sPos+6, ePos+5);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
}