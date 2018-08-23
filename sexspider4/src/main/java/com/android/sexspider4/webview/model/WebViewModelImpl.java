package com.android.sexspider4.webview.model;

import com.android.sexspider4.MyApplication;
import com.android.sexspider4.helper.HtmlHelper;
import com.android.sexspider4.helper.ImageHelper;
import com.android.sexspider4.image.bean.ImageBean;
import com.android.sexspider4.image.db.ImageAccess;
import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.list.db.ListAccess;
import com.android.sexspider4.webview.listener.OnCreateDataListener;

import java.util.ArrayList;
import java.util.List;

public class WebViewModelImpl implements IWebViewModel {
    private ListAccess listAccess;
    private ImageAccess imageAccess;

    public WebViewModelImpl() {
        listAccess = new ListAccess(MyApplication.getAppContext());
        imageAccess = new ImageAccess(MyApplication.getAppContext());
    }

    @Override
    public void createVideoUrl(ListBean list, String html, OnCreateDataListener listener) {
        //取得所有图片
        List<String> imgs = HtmlHelper.getImageArrayFromHtml(list, html);

        //插入图片到数据库
        int i = 0;
        for(String link : imgs) {
            int imageId = i + 1;
            ImageBean image = new ImageBean();
            image.imageId = imageId;
            image.imageLink = link;
            image.listId = list.listId;
            image.siteId = list.siteInfo.siteId;
            image.isDown = 1;
            image.imageName = ImageHelper.getNewFileName(link, imageId);

            imageAccess.insert(image);
            i++;
        }

        if(imgs.size() > 0) {
            listener.onCreateSuccess();
            //list.isDown = 1;
            //list.isRead = 0;
            //listAccess.update(list);
        } else {
            listener.onCreateError();
        }
    }

    @Override
    public ListBean getListByListId(int listId) {
        return listAccess.queryByListId(listId);
    }
}
