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

    //取列表数据
    public static List<Map<String, String>> getListArrayFromHtml(SiteBean site, List<String> delDics) {
        List<Map<String, String>> result = new ArrayList<>();

        //取网页内容
        String str = HttpHelper.getStringFromLink(site.siteLink, site.pageEncode, site.domain);
        if(str.equals("")) return result;

        //站点过滤
        str = ReplaceChain(str, site.siteReplace);
        str = FilterChain(str, site.siteFilter);

        //公共处理
        Iterator<String> it = delDics.iterator();
        while (it.hasNext()) {
            str = str.replaceAll(it.next().toString(), "");
        }

        //责任链
        FilterChain filter = new FilterChain(site.listFilter);

        try {
            //json
            if (site.docType.equals("json")) {
                String[] root = site.listDiv.split("\\|\\|");
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(root[0]);

                for (int i = 0; i < jsonArray.length(); i++) {
                    String[] child = root[1].split("&");
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Map<String, String> map = new HashMap<>();
                    map.put("title", obj.getString(child[0]));
                    map.put("thumb", obj.getString(child[2]));
                    map.put("link", setLink(obj.getString(child[1]), site.domain));
                    if (child.length == 3)
                        map.put("last", obj.getString(child[2]));

                    result.add(map);
                }
            } else {
                if (!site.mainDiv.equals("null") && !site.mainDiv.equals("")) {
                    //取实际内容
                    Document doc = Jsoup.parse(str);
                    Elements content = doc.select(site.mainDiv);
                    for (Element ele : content) {
                        Document _doc = Jsoup.parse(ele.html());
                        Elements _ele = _doc.select(site.listDiv);
                        Element _item = _ele.first();

                        if(_item == null) continue;

                        String _title = filter.doFilter(_item.html());

                        //标题为空跳过
                        if (isEmptyLink(_title)) continue;

                        Element _img = _doc.select(site.thumbDiv).first();
                        String _imgtxt = _img == null ? "" : _img.toString();

                        Map<String, String> map = new HashMap<>();
                        map.put("title", _title);
                        map.put("thumb", getThumb(_imgtxt, site.domain));
                        map.put("link", setLink(_item.attr("href"), site.domain));
                        map.put("last", "");

                        result.add(map);
                    }
                } else {
                    //取实际内容
                    Document doc = Jsoup.parse(str);
                    Elements content = doc.select(site.listDiv);
                    for (Element ele : content) {
                        String _title = filter.doFilter(ele.html());

                        //标题为空跳过
                        if (isEmptyLink(_title)) continue;

                        Map<String, String> map = new HashMap<>();
                        map.put("title", _title);
                        map.put("thumb", getThumb(ele.html(), site.domain));
                        map.put("link", setLink(ele.attr("href"), site.domain));
                        map.put("last", "");

                        result.add(map);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //取图片内容
    public static List<String> getImageArrayFromHtml(ListBean list) {
        List<String> result = new ArrayList<>();

        //取网页内容
        String str = HttpHelper.getStringFromLink(list.getListLink(), list.siteInfo.pageEncode, list.siteInfo.domain);
        if (str.equals("")) return result;

        //站点过滤
        str = ReplaceChain(str, list.siteInfo.siteReplace);
        str = FilterChain(str, list.siteInfo.siteFilter);

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
        List<String> result = new ArrayList<>();

        //取网页内容
        String str = HttpHelper.getStringFromLink(list.getListLink(), list.siteInfo.pageEncode, list.siteInfo.domain);
        if (str.equals("")) return result;

        //站点过滤
        str = ReplaceChain(str, list.siteInfo.siteReplace);
        str = FilterChain(str, list.siteInfo.siteFilter);

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

            //取总页数
            content = doc.select(list.siteInfo.pageFilter);//取总页数filter
            for (Element ele : content) {
                list.listTotal = ele.html().replaceAll("[^\\d]", "");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //设置url全部
    private static String setLink(String url, String domain) {
        if(url == null || url.equals("")) return "";

        if (url.startsWith("http")) {
            return url;
        }
        if (url.startsWith("//")) {
            return "http:" + url;
        }

        url = url.replace("./", "").replace("../", "");

        return domain + url;
    }

    //设置url全部
    private static String setLink(String url, String _domain, String domain) {
        if(url == null || url.equals("")) return "";

        if (url.startsWith("http")) {
            return url;
        } else if (url.startsWith("/")) {
            return domain + url;
        }

        url = url.replace("./", "").replace("../", "");

        return _domain + url;
    }

    //缩略图
    private static String getThumb(String str, String domain){
        if(str.equals("")) return "";

        Document doc = Jsoup.parse(str);
        Elements content = doc.select("img");
        if(content != null && content.size() > 0){
            Element item = content.first();
            List<String> lists = new ArrayList<>();
            lists.add("file");
            lists.add("data-original");
            lists.add("data-src");
            lists.add("zoomfile");
            lists.add("src");

            for(String s : lists) {
                if(item.hasAttr(s)) return setLink(item.attr(s), domain);
            }
        }

        return "";
    }

    //取分页页数，比如妹子图[1][2][3]...[70]
    public static List<String> getPageMany(String url, List<String> pages, String total) {
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

        int pageNum = 0;
        if (!total.equals("")) {
            pageNum = Integer.parseInt(total);
        } else {
            pageNum = Integer.parseInt(pageStr.replaceAll("[^\\d]", ""));
        }

        for(int i = 2; i <= pageNum; i++) {
            String newStr = pageStr.replaceAll("\\d+", String.valueOf(i));
            result.add(newUrl + newStr);
        }

        return result;
    }

    private static boolean isEmptyLink(String _link) {
        if (_link == null) return true;
        if (_link.equals("")) return true;
        if (_link.contains("javascript")) return true;
        return _link.equals("#");
    }

    public static String getPageTotal(ListBean list) {
        String result = "";

        //取网页内容
        String str = HttpHelper.getStringFromLink(list.getListLink(), list.siteInfo.pageEncode, list.siteInfo.domain);
        if (str.equals("")) return "";

        try {
            //取实际内容
            Document doc = Jsoup.parse(str);
            Elements content = doc.select(list.siteInfo.pageFilter);//取总页数filter
            for (Element ele : content) {
                result = ele.html().replaceAll("[^\\d]", "");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //替换站点html
    private static String ReplaceChain(String str, String fi){
        if(fi == null || fi.equals("null") || fi.equals("")) return str;

        try {
            JSONArray jsonArray = new JSONArray(fi);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String oldStr = obj.getString("old");
                String newStr = obj.getString("new");
                str = str.replace(oldStr, newStr);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }

    //过滤站点html
    private static String FilterChain(String str, String fi){
        if(fi == null || fi.equals("null") || fi.equals("")) return str;

        FilterChain filter = new FilterChain(fi);
        if(filter.Count() > 0) {
            str = filter.doFilter(str);
        }

        return str;
    }
}
