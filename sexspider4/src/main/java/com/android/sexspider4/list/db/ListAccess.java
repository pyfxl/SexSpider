package com.android.sexspider4.list.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.android.sexspider4.BaseAccess;
import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.site.bean.SiteBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class ListAccess extends BaseAccess {
    //构造方法
    public ListAccess(Context context) {
        super(context);
    }

    //查询所有已下载列表
    public List<ListBean> queryAllDown() {
        String sql = "SELECT A.ListID, A.ListTitle, A.ListLink, A.SiteID, A.IsDown, A.IsDowning, A.IsShow, A.IsRead, A.IsNew, B.ImageDiv, B.ImageFilter, B.PageEncode, B.Domain, B.PageDiv, B.PageFilter, B.PageLevel, A.ListPicture, A.IsFavorite "
                + "FROM " + TABLE_LIST + " A INNER JOIN " + TABLE_SITE + " B ON A.SiteID = B.SiteID WHERE A.IsShow = 1 AND A.IsDown <> 0 ORDER BY A.ListTitle ASC, A.IsRead ASC";

        return queryByString(sql, null);
    }

    //查询所有列表
    public List<ListBean> queryAll() {
        String sql = "SELECT A.ListID, A.ListTitle, A.ListLink, A.SiteID, A.IsDown, A.IsDowning, A.IsShow, A.IsRead, A.IsNew, B.ImageDiv, B.ImageFilter, B.PageEncode, B.Domain, B.PageDiv, B.PageFilter, B.PageLevel, A.ListPicture, A.IsFavorite "
                + "FROM " + TABLE_LIST + " A INNER JOIN " + TABLE_SITE + " B ON A.SiteID = B.SiteID WHERE B.IsUpdated <> 0 AND A.IsShow = 1 ORDER BY A.ListTitle ASC, A.IsRead ASC";

        return queryByString(sql, null);
    }

    //查询所有喜爱列表
    public List<ListBean> queryAllFavorite() {
        String sql = "SELECT A.ListID, A.ListTitle, A.ListLink, A.SiteID, A.IsDown, A.IsDowning, A.IsShow, A.IsRead, A.IsNew, B.ImageDiv, B.ImageFilter, B.PageEncode, B.Domain, B.PageDiv, B.PageFilter, B.PageLevel, A.ListPicture, A.IsFavorite "
                + "FROM " + TABLE_LIST + " A INNER JOIN " + TABLE_SITE + " B ON A.SiteID = B.SiteID WHERE A.IsFavorite = 1 AND A.IsShow = 1 ORDER BY A.ListTitle ASC, A.IsRead ASC";

        return queryByString(sql, null);
    }

    //查询未下载推荐列表
    public List<ListBean> queryAllRecommend() {
        String sql = "SELECT A.ListID, A.ListTitle, A.ListLink, A.SiteID, A.IsDown, A.IsDowning, A.IsShow, A.IsRead, A.IsNew, B.ImageDiv, B.ImageFilter, B.PageEncode, B.Domain, B.PageDiv, B.PageFilter, B.PageLevel, A.ListPicture, A.IsFavorite "
                + "FROM " + TABLE_LIST + " A INNER JOIN " + TABLE_SITE + " B ON A.SiteID = B.SiteID WHERE B.IsUpdated <> 0 AND A.IsShow = 1 AND (A.IsDown = 0 OR A.IsRead = 0) AND A.ListRank > 0 ORDER BY A.ListRank DESC, A.ListTitle ASC, A.IsRead ASC LIMIT 500 OFFSET 0";

        return queryByString(sql, null);
    }

    //查询所有推荐列表
    public List<ListBean> queryAllRecommendDown() {
        String sql = "SELECT A.ListID, A.ListTitle, A.ListLink, A.SiteID, A.IsDown, A.IsDowning, A.IsShow, A.IsRead, A.IsNew, B.ImageDiv, B.ImageFilter, B.PageEncode, B.Domain, B.PageDiv, B.PageFilter, B.PageLevel, A.ListPicture, A.IsFavorite "
                + "FROM " + TABLE_LIST + " A INNER JOIN " + TABLE_SITE + " B ON A.SiteID = B.SiteID WHERE B.IsUpdated <> 0 AND A.IsShow = 1 AND A.ListRank > 0 ORDER BY A.ListRank DESC, A.ListTitle ASC, A.IsRead ASC LIMIT 500 OFFSET 0";

        return queryByString(sql, null);
    }

    //查询所有未读列表
    public List<ListBean> queryAllNotRead() {
        String sql = "SELECT A.ListID, A.ListTitle, A.ListLink, A.SiteID, A.IsDown, A.IsDowning, A.IsShow, A.IsRead, A.IsNew, B.ImageDiv, B.ImageFilter, B.PageEncode, B.Domain, B.PageDiv, B.PageFilter, B.PageLevel, A.ListPicture, A.IsFavorite "
                + "FROM " + TABLE_LIST + " A INNER JOIN " + TABLE_SITE + " B ON A.SiteID = B.SiteID WHERE A.IsRead = 0 AND A.IsShow = 1 AND A.IsDown <> 0 ORDER BY A.ListTitle ASC, A.IsRead ASC";

        return queryByString(sql, null);
    }

    //根据siteId查询所有列表
    public List<ListBean> queryAllById(int siteId) {
        String sql = "SELECT A.ListID, A.ListTitle, A.ListLink, A.SiteID, A.IsDown, A.IsDowning, A.IsShow, A.IsRead, A.IsNew, B.ImageDiv, B.ImageFilter, B.PageEncode, B.Domain, B.PageDiv, B.PageFilter, B.PageLevel, A.ListPicture, A.IsFavorite "
                + "FROM " + TABLE_LIST + " A INNER JOIN " + TABLE_SITE + " B ON A.SiteID = B.SiteID WHERE A.SiteID = ? ORDER BY A.ListNum ASC, A.ListID DESC";

        return queryByString(sql, new String[] { String.valueOf(siteId) });
    }

    //根据siteId查询所有下载列表
    public List<ListBean> queryAllDownById(int siteId) {
        String sql = "SELECT A.ListID, A.ListTitle, A.ListLink, A.SiteID, A.IsDown, A.IsDowning, A.IsShow, A.IsRead, A.IsNew, B.ImageDiv, B.ImageFilter, B.PageEncode, B.Domain, B.PageDiv, B.PageFilter, B.PageLevel, A.ListPicture, A.IsFavorite "
                + "FROM " + TABLE_LIST + " A INNER JOIN " + TABLE_SITE + " B ON A.SiteID = B.SiteID WHERE A.SiteID = ? AND A.IsDown <> 0 ORDER BY A.ListNum ASC, A.ListID DESC";

        return queryByString(sql, new String[] { String.valueOf(siteId) });
    }

    //根据query查询所有列表
    public List<ListBean> queryIsShowByKey(String searchKey) {
        String key = "%" + searchKey + "%";
        String sql = "SELECT A.ListID, A.ListTitle, A.ListLink, A.SiteID, A.IsDown, A.IsDowning, A.IsShow, A.IsRead, A.IsNew, B.ImageDiv, B.ImageFilter, B.PageEncode, B.Domain, B.PageDiv, B.PageFilter, B.PageLevel, A.ListPicture, A.IsFavorite "
                + "FROM " + TABLE_LIST + " A INNER JOIN " + TABLE_SITE + " B ON A.SiteID = B.SiteID WHERE B.IsUpdated <> 0 AND A.ListTitle LIKE ? AND A.IsShow = 1 ORDER BY A.ListTitle ASC, A.IsRead ASC";

        return queryByString(sql, new String[] { String.valueOf(key) });
    }

    //根据query查询所有下载列表
    public List<ListBean> queryIsDownByKey(String searchKey) {
        String key = "%" + searchKey + "%";
        String sql = "SELECT A.ListID, A.ListTitle, A.ListLink, A.SiteID, A.IsDown, A.IsDowning, A.IsShow, A.IsRead, A.IsNew, B.ImageDiv, B.ImageFilter, B.PageEncode, B.Domain, B.PageDiv, B.PageFilter, B.PageLevel, A.ListPicture, A.IsFavorite "
                + "FROM " + TABLE_LIST + " A INNER JOIN " + TABLE_SITE + " B ON A.SiteID = B.SiteID WHERE A.ListTitle LIKE ? AND A.IsShow = 1 AND A.IsDown <> 0 ORDER BY A.ListTitle ASC, A.IsRead ASC";

        return queryByString(sql, new String[] { String.valueOf(key) });
    }

    //查询基本方法
    private List<ListBean> queryByString(String sql, String[] query) {
        List<ListBean> list = new ArrayList<>();

        try {
            Cursor cursor = db.rawQuery(sql, query);
            while (cursor.moveToNext()) {
                SiteBean site = new SiteBean();
                ListBean entity = new ListBean();
                entity.listId = cursor.getInt(0);
                entity.listTitle = cursor.getString(1);
                entity.listLink = cursor.getString(2);
                site.siteId = cursor.getInt(3);
                entity.isDown = cursor.getInt(4);
                entity.isDowning = cursor.getInt(5);
                entity.isShow = cursor.getInt(6);
                entity.isRead = cursor.getInt(7);
                entity.isNew = cursor.getInt(8);
                site.imageDiv = cursor.getString(9);
                site.imageFilter = cursor.getString(10);
                site.pageEncode = cursor.getString(11);
                site.domain = cursor.getString(12);
                site.pageDiv = cursor.getString(13);
                site.pageFilter = cursor.getString(14);
                site.pageLevel = cursor.getString(15);
                entity.listPicture = cursor.getString(16);
                entity.isFavorite = cursor.getInt(17);
                entity.siteInfo = site;

                list.add(entity);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //插入数据
    public void insert(ListBean list) {
        ContentValues values = new ContentValues();
        values.put("ListTitle", list.listTitle);
        values.put("ListLink", list.listLink);
        values.put("SiteID", list.siteInfo.siteId);
        values.put("ListNum", list.listNum);
        values.put("IsNew", list.isNew);

        try {
            db.insert(TABLE_LIST, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //更新数据
    public void update(ListBean list) {
        ContentValues values = new ContentValues();
        values.put("IsDown", list.isDown);
        values.put("IsDowning", list.isDowning);
        values.put("IsRead", list.isRead);
        values.put("IsShow", list.isShow);
        values.put("ListPicture", list.listPicture);
        values.put("IsFavorite", list.isFavorite);
        values.put("ListRank", list.listRank);
        values.put("IsNew", list.isNew);

        try {
            db.update(TABLE_LIST, values, "ListID = ?", new String[] { String.valueOf(list.listId) });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //批量更新
    public void update(List<ListBean> lists) {
        try {
            Iterator<ListBean> it = lists.iterator();
            while (it.hasNext()) {
                ListBean list = it.next();
                ContentValues values = new ContentValues();
                values.put("ListRank", list.listRank);
                db.update(TABLE_LIST, values, "ListID = ?", new String[] { String.valueOf(list.listId) });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //正在下载的项取消下载
    public void cancelDown() {
        ContentValues values = new ContentValues();
        values.put("IsDowning", 0);

        try {
            db.update(TABLE_LIST, values, "IsDowning = ?", new String[] { "1" });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //更新最新下载的项
    public void updateIsNew(int siteId) {
        ContentValues values = new ContentValues();
        values.put("IsNew", 0);

        try {
            db.update(TABLE_LIST, values, "SiteID = ? AND ListNum = 1 AND STRFTIME('%Y-%m-%d', ListDate) <> STRFTIME('%Y-%m-%d', datetime('now', '+8 hour'))", new String[] { String.valueOf(siteId) });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除数据
    public void delete(ListBean list) {
        try {
            db.delete(TABLE_LIST, "ListID = ?", new String[] { String.valueOf(list.listId) });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //更新喜爱
    public void updateFavorite(int listId, int isFavorite) {
        ContentValues values = new ContentValues();
        values.put("IsFavorite", isFavorite);

        try {
            db.update(TABLE_LIST, values, "ListID = ?", new String[] { String.valueOf(listId) });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
