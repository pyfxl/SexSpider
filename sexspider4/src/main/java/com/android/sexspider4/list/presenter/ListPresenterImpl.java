package com.android.sexspider4.list.presenter;

import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.list.listener.OnImageDownListener;
import com.android.sexspider4.list.listener.OnListDataLoadListener;
import com.android.sexspider4.list.model.IListModel;
import com.android.sexspider4.list.model.ListModelImpl;
import com.android.sexspider4.list.view.IListView;
import com.android.sexspider4.search.bean.SearchBean;
import com.android.sexspider4.site.bean.SiteBean;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class ListPresenterImpl implements IListPresenter, OnListDataLoadListener, OnImageDownListener {
    private IListModel listModel;
    private IListView listView;

    public ListPresenterImpl(IListView listView) {
        this.listModel = new ListModelImpl();
        this.listView = listView;
    }

    public ListPresenterImpl() {
        this.listModel = new ListModelImpl();
    }

    @Override
    public void getSiteById(int siteId) {
        SiteBean site = listModel.getSiteById(siteId);
        listView.setSiteBean(site);
    }

    @Override
    public List<ListBean> getListsBySiteId(int siteId) {
        return listModel.getListsBySiteId(siteId);
    }

    @Override
    public List<ListBean> getListsAllDownBySiteId(int siteId) {
        return listModel.getListsAllDownBySiteId(siteId);
    }

    @Override
    public List<ListBean> getListsByAllDown() {
        return listModel.getListsByAllDown();
    }

    @Override
    public List<ListBean> getListsByAll() {
        return listModel.getListsByAll();
    }

    @Override
    public List<ListBean> getListsByFavorite() {
        return listModel.getListsByFavorite();
    }

    @Override
    public List<ListBean> getListsByRecommend() {
        return listModel.getListsByRecommend();
    }

    @Override
    public List<ListBean> getListsByRecommendDown() {
        return listModel.getListsByRecommendDown();
    }

    @Override
    public List<ListBean> getListsByNotRead() {
        return listModel.getListsByNotRead();
    }

    @Override
    public List<ListBean> getListsByQueryKey(String queryKey) {
        return listModel.getListsByQueryKey(queryKey);
    }

    @Override
    public List<ListBean> getListsDownByQueryKey(String queryKey) {
        return listModel.getListsDownByQueryKey(queryKey);
    }

    @Override
    public ListBean getListByListId(int listId) {
        return listModel.getListByListId(listId);
    }

    @Override
    public void setListsBySiteId(int siteId) {
        List<ListBean> lists = listModel.getListsBySiteId(siteId);
        listView.setLists(lists);
    }

    @Override
    public void loadListDataBySite(SiteBean site) {
        listModel.loadListDataBySite(site, this);
    }

    @Override
    public void downImageByList(ListBean list, int position) {
        listModel.downImageByList(list, position, this);
    }

    @Override
    public void updateList(ListBean list) {
        listModel.updateList(list);
    }

    @Override
    public void updateList(List<ListBean> lists) {
        listModel.updateList(lists);
    }

    @Override
    public void onListLoadStart() {
        listView.loadStart();
    }

    @Override
    public void onListLoadProgress() {
        listView.loadProgress();
    }

    @Override
    public void onListLoadSuccess() {
        listView.loadSuccess();
    }

    @Override
    public void onListLoadSuccess(List<ListBean> lists) {
        listView.loadSuccess(lists);
    }

    @Override
    public void onListLoadNoUpdate() {
        listView.loadNoUpdate();
    }

    @Override
    public void onListLoadError() {
        listView.loadError();
    }

    @Override
    public void onListLoadEnd() {
        listView.loadEnd();
    }

    @Override
    public void onListLoadStart(SiteBean site) {

    }

    @Override
    public void onListLoadEnd(SiteBean site) {

    }

    @Override
    public void onDownStart(int position) {
        listView.downStart(position);
    }

    @Override
    public void onDownSuccess(int position) {
        listView.downSuccess(position);
    }

    @Override
    public void onDownProgress(int position) {
        listView.downProgress(position);
    }

    @Override
    public void onDownComplete(int position) {
        listView.downComplete(position);
    }

    @Override
    public void onDownError(int position) {
        listView.downError(position);
    }

    @Override
    public void onDownEnd(ListBean list, int position) {
        listView.downEnd(list, position);
    }

    @Override
    public void onDownCancel() {
        listView.downCancel();
    }

    @Override
    public String getListHtml(ListBean list, int position) {
        return listView.getListHtml(list, position);
    }

    @Override
    public void cancelDown(List<ListBean> lists) {
        listModel.cancelDown();
        for (ListBean list : lists) {
            list.isDowning = 0;
        }
    }

    @Override
    public void deleteList(ListBean list) {
        listModel.deleteList(list);
    }

    @Override
    public void insertSearch(SearchBean search) {
        listModel.insertSearch(search);
    }

    @Override
    public void updateIsNew(int siteId) {
        listModel.updateIsNew(siteId);
    }

}
