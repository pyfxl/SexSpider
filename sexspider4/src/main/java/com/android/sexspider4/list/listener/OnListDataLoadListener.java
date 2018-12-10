package com.android.sexspider4.list.listener;

import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.site.bean.SiteBean;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public interface OnListDataLoadListener {
    void onListLoadStart();
    void onListLoadProgress();
    void onListLoadSuccess();
    void onListLoadSuccess(List<ListBean> lists);
    void onListLoadNoUpdate();
    void onListLoadRepeat();
    void onListLoadError();
    void onListLoadEnd();
    void onListLoadStart(SiteBean site);
    void onListLoadEnd(SiteBean site);

    String getHtmlByUrl(String loadUrl);
}
