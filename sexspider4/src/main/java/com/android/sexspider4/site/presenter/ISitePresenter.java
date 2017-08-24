package com.android.sexspider4.site.presenter;

import com.android.sexspider4.site.bean.SiteBean;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public interface ISitePresenter {
    void setLists();
    List<SiteBean> getLists();
    void loadWebData();
    void update(SiteBean site);
    void loadListDataBySite(SiteBean site);
}
