package com.android.sexspider4.site.presenter;

import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.list.listener.OnListDataLoadListener;
import com.android.sexspider4.list.model.IListModel;
import com.android.sexspider4.list.model.ListModelImpl;
import com.android.sexspider4.site.bean.SiteBean;
import com.android.sexspider4.site.listener.OnSiteDataLoadListener;
import com.android.sexspider4.site.model.ISite;
import com.android.sexspider4.site.model.SiteModel;
import com.android.sexspider4.site.view.ISiteView;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class SitePresenterImpl implements ISitePresenter, OnSiteDataLoadListener, OnListDataLoadListener {
    private ISite siteModel;
    private IListModel listModel;
    private ISiteView siteView;

    public SitePresenterImpl(ISiteView siteView) {
        this.siteModel = new SiteModel();
        this.listModel = new ListModelImpl();
        this.siteView = siteView;
    }

    @Override
    public void setLists() {
        List<SiteBean> lists = siteModel.getLists();
        siteView.setLists(lists);
    }

    @Override
    public List<SiteBean> getLists() {
        return siteModel.getLists();
    }

    @Override
    public void loadWebData() {
        siteModel.loadWebData(this);
    }

    @Override
    public void loadListDataBySite(SiteBean site) {
        listModel.loadListDataBySite(site, this);
    }

    @Override
    public void update(SiteBean site) {
        siteModel.update(site);
    }

    @Override
    public void onSiteLoadStart() {
        siteView.loadStart();
    }

    @Override
    public void onSiteLoadProgress() {
        siteView.loadProgress();
    }

    @Override
    public void onSiteLoadSuccess() {
        siteView.loadSuccess();
    }

    @Override
    public void onSiteLoadError() {
        siteView.loadError();
    }

    @Override
    public void onSiteLoadEnd() {
        siteView.loadEnd();
    }

    @Override
    public void onListLoadStart() {

    }

    @Override
    public void onListLoadProgress() {

    }

    @Override
    public void onListLoadSuccess() {

    }

    @Override
    public void onListLoadSuccess(List<ListBean> lists) {

    }

    @Override
    public void onListLoadNoUpdate() {

    }

    @Override
    public void onListLoadRepeat() {

    }

    @Override
    public void onListLoadError() {

    }

    @Override
    public void onListLoadEnd() {

    }

    @Override
    public void onListLoadStart(SiteBean site) {
        siteView.listLoadStart(site);
    }

    @Override
    public void onListLoadEnd(SiteBean site) {
        siteView.listLoadEnd(site);
    }

    @Override
    public String getHtmlByUrl(String loadUrl) {
        return "";
    }
}
