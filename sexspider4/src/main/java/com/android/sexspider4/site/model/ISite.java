package com.android.sexspider4.site.model;

import com.android.sexspider4.site.bean.SiteBean;
import com.android.sexspider4.site.listener.OnSiteDataLoadListener;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public interface ISite {
    List<SiteBean> getLists();
    void loadWebData(OnSiteDataLoadListener listener);
    void update(SiteBean site);
}
