package com.fxlweb.mydemo.sites.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.fxlweb.mydemo.common.SQLiteCommon;
import com.fxlweb.mydemo.sites.bean.SitesBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2016/6/8.
 */

public class SitesAccess extends SQLiteCommon {
    private static final String TABLE_SITE = "SiteTable";

    //构造方法
    public SitesAccess(Context context) {
        super(context);
    }

    //插入方法
    public void insert(SitesBean entity) {
        ContentValues values = new ContentValues();
        values.put("SiteID", entity.siteId);
        values.put("SiteRank", entity.siteRank);
        values.put("IsHided", entity.isHided);
        values.put("SiteName", entity.siteName);
        values.put("ListPage", entity.listPage);
        values.put("PageEncode", entity.pageEncode);
        values.put("Domain", entity.domain);
        values.put("SiteLink", entity.siteLink);
        values.put("ListStart", entity.listStart);
        values.put("ListEnd", entity.listEnd);
        values.put("ImageStart", entity.imageStart);
        values.put("ImageEnd", entity.imageEnd);
        values.put("OnInclude", entity.onInclude);
        values.put("NotInclude", entity.notInclude);
        values.put("PageStart", entity.pageStart);
        values.put("PageEnd", entity.pageEnd);
        values.put("ListNum", entity.listNum);
        values.put("IsUpdated", entity.isUpdated);
        values.put("IsDowning", entity.isDowning);

        try {
            db.insert(TABLE_SITE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询方法
    public List<SitesBean> query() {
        List<SitesBean> lists = new ArrayList<SitesBean>();

        try {
            Cursor cursor = db.query(TABLE_SITE, null, "IsHided = ?", new String[] { "0" }, null, null, "SiteRank ASC");
            while (cursor.moveToNext()) {
                SitesBean entity = new SitesBean();
                entity.siteId = cursor.getInt(cursor.getColumnIndex("SiteID"));
                entity.siteRank = cursor.getString(cursor.getColumnIndex("SiteRank"));
                entity.isHided = cursor.getInt(cursor.getColumnIndex("IsHided"));
                entity.siteName = cursor.getString(cursor.getColumnIndex("SiteName"));
                entity.listPage = cursor.getString(cursor.getColumnIndex("ListPage"));
                entity.pageEncode = cursor.getString(cursor.getColumnIndex("PageEncode"));
                entity.domain = cursor.getString(cursor.getColumnIndex("Domain"));
                entity.siteLink = cursor.getString(cursor.getColumnIndex("SiteLink"));
                entity.listStart = cursor.getString(cursor.getColumnIndex("ListStart"));
                entity.listEnd = cursor.getString(cursor.getColumnIndex("ListEnd"));
                entity.imageStart = cursor.getString(cursor.getColumnIndex("ImageStart"));
                entity.imageEnd = cursor.getString(cursor.getColumnIndex("ImageEnd"));
                entity.onInclude = cursor.getString(cursor.getColumnIndex("OnInclude"));
                entity.notInclude = cursor.getString(cursor.getColumnIndex("NotInclude"));
                entity.pageStart = cursor.getString(cursor.getColumnIndex("PageStart"));
                entity.pageEnd = cursor.getString(cursor.getColumnIndex("PageEnd"));
                entity.listNum = cursor.getInt(cursor.getColumnIndex("ListNum"));
                entity.isUpdated = cursor.getInt(cursor.getColumnIndex("IsUpdated"));
                entity.isDowning = cursor.getInt(cursor.getColumnIndex("IsDowning"));
                lists.add(entity);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lists;
    }

    //根据ID查询
    public SitesBean queryById(int siteId) {
        SitesBean entity = new SitesBean();

        try {
            Cursor cursor = db.query(TABLE_SITE, null, "SiteID = ?", new String[] { String.valueOf(siteId) } , null, null, null);
            if(cursor.moveToNext()) {
                entity.siteId = cursor.getInt(cursor.getColumnIndex("SiteID"));
                entity.siteRank = cursor.getString(cursor.getColumnIndex("SiteRank"));
                entity.isHided = cursor.getInt(cursor.getColumnIndex("IsHided"));
                entity.siteName = cursor.getString(cursor.getColumnIndex("SiteName"));
                entity.listPage = cursor.getString(cursor.getColumnIndex("ListPage"));
                entity.pageEncode = cursor.getString(cursor.getColumnIndex("PageEncode"));
                entity.domain = cursor.getString(cursor.getColumnIndex("Domain"));
                entity.siteLink = cursor.getString(cursor.getColumnIndex("SiteLink"));
                entity.listStart = cursor.getString(cursor.getColumnIndex("ListStart"));
                entity.listEnd = cursor.getString(cursor.getColumnIndex("ListEnd"));
                entity.imageStart = cursor.getString(cursor.getColumnIndex("ImageStart"));
                entity.imageEnd = cursor.getString(cursor.getColumnIndex("ImageEnd"));
                entity.onInclude = cursor.getString(cursor.getColumnIndex("OnInclude"));
                entity.notInclude = cursor.getString(cursor.getColumnIndex("NotInclude"));
                entity.pageStart = cursor.getString(cursor.getColumnIndex("PageStart"));
                entity.pageEnd = cursor.getString(cursor.getColumnIndex("PageEnd"));
                entity.listNum = cursor.getInt(cursor.getColumnIndex("ListNum"));
                entity.isUpdated = cursor.getInt(cursor.getColumnIndex("IsUpdated"));
                entity.isDowning = cursor.getInt(cursor.getColumnIndex("IsDowning"));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entity;
    }

    //更新方法
    public void update(SitesBean entity) {
        ContentValues values = new ContentValues();
        values.put("SiteRank", entity.siteRank);
        values.put("IsHided", entity.isHided);
        values.put("SiteName", entity.siteName);
        values.put("ListPage", entity.listPage);
        values.put("PageEncode", entity.pageEncode);
        values.put("Domain", entity.domain);
        values.put("SiteLink", entity.siteLink);
        values.put("ListStart", entity.listStart);
        values.put("ListEnd", entity.listEnd);
        values.put("ImageStart", entity.imageStart);
        values.put("ImageEnd", entity.imageEnd);
        values.put("OnInclude", entity.onInclude);
        values.put("NotInclude", entity.notInclude);
        values.put("PageStart", entity.pageStart);
        values.put("PageEnd", entity.pageEnd);
        values.put("ListNum", entity.listNum);
        values.put("IsUpdated", entity.isUpdated);
        values.put("IsDowning", entity.isDowning);

        try {
            db.update(TABLE_SITE, values, "SiteID = ?", new String[] { String.valueOf(entity.siteId) });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
