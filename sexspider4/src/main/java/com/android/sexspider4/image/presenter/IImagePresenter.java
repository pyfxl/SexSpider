package com.android.sexspider4.image.presenter;

import com.android.sexspider4.image.bean.ImageBean;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public interface IImagePresenter {
    List<ImageBean> getImagesByListId(int listId);
    void updateFavorite(int listId, int isFavorite);
}
