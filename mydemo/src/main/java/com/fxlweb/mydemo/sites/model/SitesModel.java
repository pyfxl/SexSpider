package com.fxlweb.mydemo.sites.model;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fxlweb.mydemo.MyApplication;
import com.fxlweb.mydemo.sites.bean.SitesBean;
import com.fxlweb.mydemo.sites.db.SitesAccess;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by feng on 2016/6/5.
 */

public class SitesModel implements ISites {
    private SitesAccess sitesAccess;

    //构造函数
    public SitesModel() {
        sitesAccess = new SitesAccess(MyApplication.getAppContext());
    }

    @Override
    public void loadSites() {
        String url = "http://www.fxlweb.com/SexSpider/GetListData2.aspx";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("TAG", response.toString());
                    getWebData(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                }
        });
        MyApplication.getHttpQueue().add(jsonObjectRequest);
    }

    public void getWebData(JSONObject jsonAll) {
        try {
            JSONArray jsonArray = jsonAll.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int siteId = jsonObject.getInt("siteid");

                SitesBean entity = sitesAccess.queryById(siteId);
                boolean update = entity.siteId > 0;

                entity.siteId = jsonObject.getInt("siteid");
                entity.siteRank = jsonObject.getString("siterank");
                entity.vipLevel = jsonObject.getInt("viplevel");
                entity.isHided = jsonObject.getInt("ishided");
                entity.siteName = jsonObject.getString("sitename");
                entity.listPage = jsonObject.getString("listpage");
                entity.pageEncode = jsonObject.getString("pageencode");
                entity.domain = jsonObject.getString("domain");
                entity.siteLink = jsonObject.getString("sitelink");
                entity.listStart = jsonObject.getString("liststart");
                entity.listEnd = jsonObject.getString("listend");
                entity.imageStart = jsonObject.getString("imagestart");
                entity.imageEnd = jsonObject.getString("imageend");
                entity.onInclude = jsonObject.getString("oninclude");
                entity.notInclude = jsonObject.getString("notinclude");
                entity.pageStart = jsonObject.getString("pagestart");
                entity.pageEnd = jsonObject.getString("pageend");

                if(update) {
                    sitesAccess.update(entity);
                } else {
                    sitesAccess.insert(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<SitesBean> query() {
        return sitesAccess.query();
    }

}
