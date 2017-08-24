package com.fxlweb.mydemo.sites.presenter;

import com.fxlweb.mydemo.sites.bean.SitesBean;

import java.util.List;

/**
 * Created by feng on 2016/6/5.
 */

public interface ISitesPresenter {
    void getLists();
    void doLoad();
    void setProgressBarVisibility(int visibility);
    void showToast(int resId);
    void showEmpty(int visibility);
    List<SitesBean> doLoadData();
}
