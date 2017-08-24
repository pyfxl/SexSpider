package com.android.sexspider4.image.model;

import com.android.sexspider4.MyApplication;
import com.android.sexspider4.image.bean.ImageBean;
import com.android.sexspider4.image.db.ImageAccess;
import com.android.sexspider4.list.db.ListAccess;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class ImageModelImpl implements IImageModel {
    private ImageAccess imageAccess;
    private ListAccess listAccess;

    public ImageModelImpl() {
        imageAccess = new ImageAccess(MyApplication.getAppContext());
        listAccess = new ListAccess(MyApplication.getAppContext());
    }

    @Override
    public List<ImageBean> getImagesByListId(int listId) {
        return imageAccess.queryIsDownById(listId);
    }

    @Override
    public void updateFavorite(int listId, int isFavorite) {
        listAccess.updateFavorite(listId, isFavorite);
    }
}
