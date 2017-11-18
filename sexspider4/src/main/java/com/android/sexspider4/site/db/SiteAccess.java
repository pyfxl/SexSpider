package com.android.sexspider4.site.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.android.sexspider4.BaseAccess;
import com.android.sexspider4.site.bean.SiteBean;
import com.android.sexspider4.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class SiteAccess extends BaseAccess {
    //构造方法
    public SiteAccess(Context context) {
        super(context);
    }

    //插入方法
    public void insert(SiteBean site) {
        ContentValues values = new ContentValues();
        updateValue(values, site);

        try {
            db.insert(TABLE_SITE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询方法
    public List<SiteBean> query() {
        List<SiteBean> lists = new ArrayList<>();

        try {
            Cursor cursor = db.query(TABLE_SITE, null, "IsHided = ?", new String[] { "0" }, null, null, "SiteRank ASC");
            while (cursor.moveToNext()) {
                SiteBean site = SetSiteBean(cursor);
                lists.add(site);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lists;
    }

    //根据ID查询
    public SiteBean queryById(int siteId) {
        SiteBean site = new SiteBean();

        try {
            Cursor cursor = db.query(TABLE_SITE, null, "SiteID = ?", new String[] { String.valueOf(siteId) } , null, null, null);
            if(cursor.moveToNext()) {
                site = SetSiteBean(cursor);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return site;
    }

    //更新方法
    public void update(SiteBean site) {
        ContentValues values = new ContentValues();
        updateValue(values, site);

        try {
            db.update(TABLE_SITE, values, "SiteID = ?", new String[] { String.valueOf(site.siteId) });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SiteBean SetSiteBean(Cursor cursor) {
        SiteBean site = new SiteBean();
        site.siteId = cursor.getInt(cursor.getColumnIndex("SiteID"));
        site.siteRank = cursor.getString(cursor.getColumnIndex("SiteRank"));
        site.vipLevel = cursor.getInt(cursor.getColumnIndex("VipLevel"));
        site.isHided = cursor.getInt(cursor.getColumnIndex("IsHided"));
        site.siteName = cursor.getString(cursor.getColumnIndex("SiteName"));
        site.listPage = cursor.getString(cursor.getColumnIndex("ListPage"));
        site.pageEncode = cursor.getString(cursor.getColumnIndex("PageEncode"));
        site.domain = cursor.getString(cursor.getColumnIndex("Domain"));
        site.siteLink = cursor.getString(cursor.getColumnIndex("SiteLink"));
        site.listDiv = cursor.getString(cursor.getColumnIndex("ListDiv"));
        site.listFilter = cursor.getString(cursor.getColumnIndex("ListFilter"));
        site.imageDiv = cursor.getString(cursor.getColumnIndex("ImageDiv"));
        site.imageFilter = cursor.getString(cursor.getColumnIndex("ImageFilter"));
        site.pageLevel = cursor.getString(cursor.getColumnIndex("PageLevel"));
        site.pageDiv = cursor.getString(cursor.getColumnIndex("PageDiv"));
        site.pageFilter = cursor.getString(cursor.getColumnIndex("PageFilter"));
        site.listNum = cursor.getInt(cursor.getColumnIndex("ListNum"));
        site.isUpdated = cursor.getInt(cursor.getColumnIndex("IsUpdated"));
        site.isDowning = cursor.getInt(cursor.getColumnIndex("IsDowning"));
        site.docType = cursor.getString(cursor.getColumnIndex("DocType"));
        site.siteNotes = cursor.getString(cursor.getColumnIndex("SiteNotes"));
        site.lastStart = cursor.getString(cursor.getColumnIndex("LastStart"));

        return site;
    }

    private void updateValue(ContentValues values, SiteBean site) {
        values.put("SiteID", site.siteId);
        values.put("SiteRank", site.siteRank);
        values.put("VipLevel", site.vipLevel);
        values.put("IsHided", site.isHided);
        values.put("SiteName", site.siteName);
        values.put("ListPage", site.listPage);
        values.put("PageEncode", site.pageEncode);
        values.put("Domain", site.domain);
        values.put("SiteLink", site.siteLink);
        values.put("ListDiv", site.listDiv);
        values.put("ListFilter", site.listFilter);
        values.put("ImageDiv", site.imageDiv);
        values.put("ImageFilter", site.imageFilter);
        values.put("PageLevel", site.pageLevel);
        values.put("PageDiv", site.pageDiv);
        values.put("PageFilter", site.pageFilter);
        values.put("ListNum", site.listNum);
        values.put("IsUpdated", site.isUpdated);
        values.put("IsDowning", site.isDowning);
        values.put("SiteDate", DateUtils.getDateTime());
        values.put("DocType", site.docType);
        values.put("SiteNotes", site.siteNotes);
        values.put("LastStart", site.lastStart);
    }

}
