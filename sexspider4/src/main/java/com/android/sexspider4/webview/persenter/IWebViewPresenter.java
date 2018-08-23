package com.android.sexspider4.webview.persenter;

import com.android.sexspider4.list.bean.ListBean;

public interface IWebViewPresenter {
    void createVideoUrl(ListBean list, String html);
    ListBean getListByListId(int listId);
}
