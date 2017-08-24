package com.android.sexspider4.site.listener;

/**
 * Created by feng on 2017/5/5.
 */

public interface OnSiteDataLoadListener {
    void onSiteLoadStart();
    void onSiteLoadProgress();
    void onSiteLoadSuccess();
    void onSiteLoadError();
    void onSiteLoadEnd();
}
