package com.android.sexspider4.site.model;

import com.android.sexspider4.MyApplication;
import com.android.sexspider4.helper.HttpHelper;
import com.android.sexspider4.search.bean.SearchBean;
import com.android.sexspider4.search.db.SearchAccess;
import com.android.sexspider4.site.bean.SiteBean;
import com.android.sexspider4.site.db.SiteAccess;
import com.android.sexspider4.site.listener.OnSiteDataLoadListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class SiteModel implements ISite {
    private SiteAccess siteAccess;
    private SearchAccess searchAccess;

    public SiteModel() {
        siteAccess = new SiteAccess(MyApplication.getAppContext());
        searchAccess = new SearchAccess(MyApplication.getAppContext());
    }

    @Override
    public List<SiteBean> getLists() {
        return siteAccess.query();
    }

    @Override
    public void loadWebData(final OnSiteDataLoadListener listener) {
        listener.onSiteLoadStart();
        listener.onSiteLoadProgress();

        String html = HttpHelper.getStringFromLink(MyApplication.SITE_URL);
        if (html.equals("")) {
            listener.onSiteLoadError();
        }

        if (parseWebData(html)) {
            listener.onSiteLoadSuccess();
        } else {
            listener.onSiteLoadError();
        }

        listener.onSiteLoadEnd();
    }

    @Override
    public void update(SiteBean site) {
        siteAccess.update(site);
    }

    //处理网络数据
    private boolean parseWebData(String jsonString) {
        try {
            JSONObject jsonAll = new JSONObject(jsonString);
            JSONArray jsonArray = jsonAll.getJSONArray("site_list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int siteId = jsonObject.getInt("siteid");

                SiteBean site = siteAccess.queryById(siteId);
                boolean isUpdate = site.siteId > 0;

                site.siteId = jsonObject.getInt("siteid");
                site.siteRank = jsonObject.getString("siterank");
                site.vipLevel = jsonObject.getInt("viplevel");
                site.isHided = jsonObject.getInt("ishided");
                site.siteName = jsonObject.getString("sitename");
                site.listPage = jsonObject.getString("listpage");
                site.pageEncode = jsonObject.getString("pageencode");
                site.domain = jsonObject.getString("domain");
                site.siteLink = jsonObject.getString("sitelink");
                site.siteFilter = jsonObject.getString("sitefilter");
                site.siteReplace = jsonObject.getString("sitereplace");
                site.mainDiv = jsonObject.getString("maindiv");
                site.thumbDiv = jsonObject.getString("thumbdiv");
                site.listDiv = jsonObject.getString("listdiv");
                site.listFilter = jsonObject.getString("listfilter");
                site.imageDiv = jsonObject.getString("imagediv");
                site.imageFilter = jsonObject.getString("imagefilter");
                site.pageLevel = jsonObject.getString("pagelevel");
                site.pageDiv = jsonObject.getString("pagediv");
                site.pageFilter = jsonObject.getString("pagefilter");
                site.docType = jsonObject.getString("doctype");

                if (isUpdate) {
                    siteAccess.update(site);
                } else {
                    siteAccess.insert(site);
                }
            }

            //词典删除
            searchAccess.delete(1);
            searchAccess.delete(2);
            searchAccess.delete(4);

            //词典插入
            jsonArray = jsonAll.getJSONArray("ext_dic");
            for (int i = 0; i < jsonArray.length(); i++) {
                searchAccess.insert(new SearchBean(jsonArray.getString(i), 1));
            }

            //停止词插入
            jsonArray = jsonAll.getJSONArray("stop_dic");
            for (int i = 0; i < jsonArray.length(); i++) {
                searchAccess.insert(new SearchBean(jsonArray.getString(i), 2));
            }

            //删除词插入
            jsonArray = jsonAll.getJSONArray("del_dic");
            for (int i = 0; i < jsonArray.length(); i++) {
                searchAccess.insert(new SearchBean(jsonArray.getString(i), 4));
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
