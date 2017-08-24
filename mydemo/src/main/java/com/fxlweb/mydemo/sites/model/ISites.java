package com.fxlweb.mydemo.sites.model;

import com.fxlweb.mydemo.sites.bean.SitesBean;

import java.util.List;

/**
 * Created by feng on 2016/6/5.
 */

public interface ISites {
    void loadSites();
    List<SitesBean> query();
}
