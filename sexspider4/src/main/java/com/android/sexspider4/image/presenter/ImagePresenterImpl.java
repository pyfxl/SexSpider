package com.android.sexspider4.image.presenter;

import com.android.sexspider4.image.bean.ImageBean;
import com.android.sexspider4.image.model.IImageModel;
import com.android.sexspider4.image.model.ImageModelImpl;
import com.android.sexspider4.image.view.IImageView;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class ImagePresenterImpl implements IImagePresenter {
    private IImageModel imageModel;

    public ImagePresenterImpl(IImageView imageView) {
        this.imageModel = new ImageModelImpl();
    }

    @Override
    public List<ImageBean> getImagesByListId(int listId) {
        return imageModel.getImagesByListId(listId);
    }

    @Override
    public void updateFavorite(int listId, int isFavorite) {
        imageModel.updateFavorite(listId, isFavorite);
    }

}
