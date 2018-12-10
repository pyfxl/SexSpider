package com.android.sexspider4.list.view;

import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.site.bean.SiteBean;

import java.util.List;

/**
 * Created by feng on 2016/6/8.
 */

public interface IListView {
    void setSiteBean(SiteBean site);
    void setLists(List<ListBean> lists);
    void loadStart();
    void loadProgress();
    void loadSuccess();
    void loadSuccess(List<ListBean> lists);
    void loadNoUpdate();
    void loadRepeat();
    void loadError();
    void loadEnd();
    void downStart(int position);
    void downProgress(int position);
    void downSuccess(int position);
    void downComplete(int position);
    void downError(int position);
    void downEnd(ListBean list, int position);
    void downCancel();

    String getHtmlByUrl(String loadUrl);
}
