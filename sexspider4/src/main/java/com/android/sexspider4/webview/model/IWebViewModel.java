package com.android.sexspider4.webview.model;

import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.webview.listener.OnCreateDataListener;

public interface IWebViewModel {
    void createVideoUrl(ListBean list, String html, OnCreateDataListener listener);
    ListBean getListByListId(int listId);
}
