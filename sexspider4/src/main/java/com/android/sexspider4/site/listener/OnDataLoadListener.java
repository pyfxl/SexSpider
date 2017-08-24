package com.android.sexspider4.site.listener;

/**
 * Created by feng on 2017/5/5.
 */

public interface OnDataLoadListener {
    void onLoadStart();
    void onLoadProgress();
    void onLoadSuccess();
    void onLoadError();
    void onLoadEnd();
}
