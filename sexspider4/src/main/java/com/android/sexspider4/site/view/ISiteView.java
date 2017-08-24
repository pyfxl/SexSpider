package com.android.sexspider4.site.view;

import com.android.sexspider4.site.bean.SiteBean;

import java.util.List;

/**
 * Created by feng on 2016/6/8.
 */

public interface ISiteView {
    void setLists(List<SiteBean> lists);
    void loadStart();
    void loadProgress();
    void loadSuccess();
    void loadEnd();
    void loadError();
    void listLoadStart(SiteBean site);
    void listLoadEnd(SiteBean site);
}
