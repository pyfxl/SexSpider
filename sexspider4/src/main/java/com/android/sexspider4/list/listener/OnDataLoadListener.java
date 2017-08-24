package com.android.sexspider4.list.listener;

import com.android.sexspider4.list.bean.ListBean;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public interface OnDataLoadListener {
    void onLoadStart();
    void onLoadProgress();
    void onLoadSuccess();
    void onLoadSuccess(List<ListBean> lists);
    void onLoadNoUpdate();
    void onLoadError();
    void onLoadEnd();
}
