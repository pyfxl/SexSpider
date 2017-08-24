package com.android.sexspider4.image.bean;

/**
 * Created by feng on 2017/5/5.
 */

public class ImageBean {
    public int imageId;
    public String imageName;
    public String imageLink;
    public int listId;
    public int siteId;
    public int isDown;

    public ImageBean() {
    }

    public ImageBean(int siteId, int listId, String imageName) {
        this.siteId = siteId;
        this.listId = listId;
        this.imageName = imageName;
    }
}
