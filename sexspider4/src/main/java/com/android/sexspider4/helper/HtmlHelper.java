package com.android.sexspider4.helper;

import com.android.sexspider4.filter.FilterChain;
import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.site.bean.SiteBean;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by feng on 2017/5/5.
 */

public class HtmlHelper {

    private HtmlHelper() {
    }

    //取实际内容
    private static String getContextFromHtml(String str, String start, String end) {
        if(str.equals("404") || str.equals("")) return "";

        //不要改大小写，否则图片显示错误
        //str = str.toLowerCase();

        str = str.replaceAll("\\s|\r\n|\n|\t|&nbsp;", "");
        str = str.replaceAll("<DIV", "<div");
        str = str.replaceAll("<IMG", "<img");
        str = str.replaceAll("<A", "<a");
        str = str.replaceAll("</A>", "</a>");
        str = str.replaceAll("<BODY", "<body");
        str = str.replaceAll("'", "\"");
        str = str.replaceAll("&amp;", "&");
        str = str.replaceAll("&nbsp;", "");
        str = str.replaceAll("<em.*?>|</em>|<font.*?>|</font>|<span.*?>|</span>|<script.*?>.*?</script>|<br/>", "");
        str = str.replaceAll("<EM.*?>|</EM>|<FONT.*?>|</FONT>|<SPAN.*?>|</SPAN>|<SCRIPT.*?>.*?</SCRIPT>|<BR/>", "");

        try {
            str = str.substring(str.indexOf(start), str.length());
            str = str.substring(0, str.indexOf(end));
        } catch (Exception e) {
            str = "";
            e.printStackTrace();
        }

        return str;
    }

    //取列表内容数组
    public static List<String[]> getListArrayFromHtml(SiteBean site, List<String> delDics) {
        //取网页内容
        String str = HttpHelper.getStringFromLink(site.siteLink, site.pageEncode, site.domain);

        List<String[]> result = new ArrayList<>();
        if(str.equals("")) return result;

        //公共处理
        Iterator<String> it = delDics.iterator();
        while (it.hasNext()) {
            str = str.replaceAll(it.next().toString(), "");
        }

        //责任链
        FilterChain filter = new FilterChain(site.listFilter);

        try {
            if (site.docType.equals("json")) {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(site.listDiv);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String strs = filter.doFilter(obj.toString());
                    String[] arr = strs.split(",");
                    arr[1] = setLink(arr[1], site.domain);

                    result.add(arr);
                }
            } else {
                //取实际内容
                Document doc = Jsoup.parse(str);
                Elements content = doc.select(site.listDiv);
                for (Element ele : content) {
                    String[] arr = new String[4];
                    arr[0] = filter.doFilter(ele.html());
                    arr[1] = setLink(ele.attr("href"), site.domain);
                    arr[2] = site.domain;
                    arr[3] = "";

                    if (isEmptyLink(arr[0])) continue;

                    result.add(arr);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //取图片内容
    public static List<String> getImageArrayFromHtml(ListBean list) {
        //取网页内容
        String str = HttpHelper.getStringFromLink(list.getListLink(), list.siteInfo.pageEncode, list.siteInfo.domain);

        List<String> result = new ArrayList<>();
        if (str.equals("")) return result;

        //责任链
        FilterChain filter = new FilterChain(list.siteInfo.imageFilter);

        try {
            //取实际内容
            Document doc = Jsoup.parse(str);
            Elements content = doc.select(list.siteInfo.imageDiv);
            for (Element ele : content) {
                String _link = "";
                if (filter.Count() > 0) {
                    _link = filter.doFilter(ele.outerHtml());
                } else {
                    _link = ele.attr("src");
                }

                if (isEmptyLink(_link)) continue;

                String _image = setLink(_link, list.siteInfo.domain);

                result.add(_image);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //取分页内容
    public static List<String> getPageArrayFromHtml(ListBean list) {
        //取网页内容
        String str = HttpHelper.getStringFromLink(list.getListLink(), list.siteInfo.pageEncode, list.siteInfo.domain);

        List<String> result = new ArrayList<>();
        if (str.equals("")) return result;

        //责任链
        FilterChain filter = new FilterChain(list.siteInfo.pageFilter);
        str = filter.doFilter(str);

        String url = list.getListLink();
        String domain = url.substring(0, url.lastIndexOf("/") + 1);

        try {
            //取实际内容
            Document doc = Jsoup.parse(str);
            Elements content = doc.select(list.siteInfo.pageDiv);
            for (Element ele : content) {
                if(!ele.text().equals("")) {
                    if (!ele.text().matches("^\\d*$")) continue;
                }

                String _link = ele.attr("href");
                if (isEmptyLink(_link)) continue;

                result.add(setLink(_link, domain, list.siteInfo.domain));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //设置url全部
    private static String setLink(String url, String domain) {
        if (url.startsWith("http")) {
            return url;
        }

        url = url.replace("./", "").replace("../", "");

        return domain + url;
    }

    //设置url全部
    private static String setLink(String url, String _domain, String domain) {
        if (url.startsWith("http")) {
            return url;
        } else if (url.startsWith("/")) {
            return domain + url;
        }

        url = url.replace("./", "").replace("../", "");

        return _domain + url;
    }

    public static List<String> getPageMany(String url, List<String> pages) {
        List<String> result = new ArrayList<>();

        int lastPos = url.lastIndexOf("/");
        String urlFirst = url.substring(0, lastPos);
        String urlEnd = url.substring(lastPos);

        int dianPos = urlEnd.lastIndexOf(".");
        String dianFirst = "";
        if (dianPos != -1) {
            dianFirst = urlEnd.substring(0, dianPos);
        } else {
            dianFirst = urlEnd;
        }

        String newUrl = urlFirst + dianFirst;
        String lastPage = pages.get(pages.size() - 1);
        String pageStr = lastPage.replace(newUrl, "");

        int pageNum = Integer.parseInt(pageStr.replaceAll("[^\\d]", ""));
        for(int i = 2; i <= pageNum; i++) {
            String newStr = pageStr.replaceAll("\\d+", String.valueOf(i));
            result.add(newUrl + newStr);
        }

        return result;
    }

    private static boolean isEmptyLink(String _link) {
        if (_link == null) return true;
        if (_link.equals("")) return true;
        if (_link.equals("#")) return true;

        return false;
    }
}
