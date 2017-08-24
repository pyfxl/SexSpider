package com.android.sexspider4.image.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.android.sexspider4.BaseAccess;
import com.android.sexspider4.image.bean.ImageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class ImageAccess extends BaseAccess {
    private static final String TABLE_IMAGE = "ImageTable";

    //构造函数
    public ImageAccess(Context context) {
        super(context);
    }

    //插入数据
    public void insert(ImageBean image) {
        ContentValues values = new ContentValues();
        values.put("ImageID", image.imageId);
        values.put("ImageName", image.imageName);
        values.put("ImageLink", image.imageLink);
        values.put("ListID", image.listId);
        values.put("IsDown", image.isDown);
        values.put("SiteId", image.siteId);

        try {
            //先删除
            db.delete(TABLE_IMAGE, "ImageID = ? AND ListID = ?", new String[] { String.valueOf(image.imageId), String.valueOf(image.listId) });

            db.insert(TABLE_IMAGE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //根据ID查未下载
    public List<ImageBean> queryNotDownById(int listId) {
        return queryByString(new String[] { String.valueOf(listId), "0"});
    }

    //根据ID查已下载
    public List<ImageBean> queryIsDownById(int listId) {
        return queryByString(new String[] { String.valueOf(listId), "1"});
    }

    //查询基本方法
    private List<ImageBean> queryByString(String[] query) {
        List<ImageBean> lists = new ArrayList<>();

        try {
            Cursor cursor = db.query(TABLE_IMAGE, null, "ListID = ? AND IsDown = ?", query, null, null, null);
            while (cursor.moveToNext()) {
                ImageBean image = new ImageBean();
                image.imageId = cursor.getInt(cursor.getColumnIndex("ImageID"));
                image.imageName = cursor.getString(cursor.getColumnIndex("ImageName"));
                image.imageLink = cursor.getString(cursor.getColumnIndex("ImageLink"));
                image.listId = cursor.getInt(cursor.getColumnIndex("ListID"));
                image.isDown = cursor.getInt(cursor.getColumnIndex("IsDown"));
                image.siteId = cursor.getInt(cursor.getColumnIndex("SiteID"));

                lists.add(image);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lists;
    }

    //更新数据
    public void update(ImageBean image) {
        ContentValues values = new ContentValues();
        values.put("isDown", image.isDown);

        try {
            db.update(TABLE_IMAGE, values, "ImageID = ? AND ListID = ?", new String[]{String.valueOf(image.imageId), String.valueOf(image.listId)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
