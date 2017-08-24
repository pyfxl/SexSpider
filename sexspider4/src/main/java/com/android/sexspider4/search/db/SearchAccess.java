package com.android.sexspider4.search.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.android.sexspider4.BaseAccess;
import com.android.sexspider4.MyApplication;
import com.android.sexspider4.search.bean.SearchBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class SearchAccess extends BaseAccess {
    //构造方法
    public SearchAccess(Context context) {
        super(context);
    }

    public void insert(SearchBean search) {
        ContentValues values = new ContentValues();
        values.put("SearchName", search.searchName);
        values.put("SearchType", search.searchType);

        try {
            db.insert(TABLE_SEARCH, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SearchBean> query(int type) {
        List<SearchBean> lists = new ArrayList<>();
        String sql = "SELECT MAX(SearchID) as SearchID, SearchName, COUNT(0) AS SearchCount FROM " + TABLE_SEARCH + " WHERE SearchType = ? GROUP BY SearchName ORDER BY COUNT(0) DESC";

        try {
            Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(type) });
            while (cursor.moveToNext()) {
                SearchBean search = new SearchBean();
                search.searchId = cursor.getInt(cursor.getColumnIndex("SearchID"));
                search.searchName = cursor.getString(cursor.getColumnIndex("SearchName"));
                search.searchType = type;
                search.searchCount = cursor.getInt(cursor.getColumnIndex("SearchCount"));

                if (search.searchCount < MyApplication.SEARCH_COUNT_MAX) continue;

                lists.add(search);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lists;
    }

    public List<String> queryDicsByType(int type) {
        List<String> lists = new ArrayList<>();

        try {
            Cursor cursor = db.query(TABLE_SEARCH, null, "SearchType = ?", new String[] { String.valueOf(type) } , null, null, null);
            while (cursor.moveToNext()) {
                lists.add(cursor.getString(cursor.getColumnIndex("SearchName")));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lists;
    }

    public void delete(String searchName) {
        try {
            db.delete(TABLE_SEARCH, "SearchName = ?", new String[] { searchName });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int type) {
        try {
            db.delete(TABLE_SEARCH, "SearchType = ?", new String[] { String.valueOf(type) });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
