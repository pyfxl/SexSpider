package com.fxlweb.mydemo.sites.view;

import com.fxlweb.mydemo.sites.bean.SitesBean;

import java.util.List;

/**
 * Created by feng on 2016/6/5.
 */

public interface ISitesView {
    void showLists(List<SitesBean> lists);
    void onLoadResult(Boolean result, int code);
    void onSetProgressBarVisibility(int visibility);
    void onShowToast(int resId);
    void onShowEmpty(int visibility);
}
