package com.android.sexspider4.list.presenter;

import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.search.bean.SearchBean;
import com.android.sexspider4.site.bean.SiteBean;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public interface IListPresenter {
    void getSiteById(int siteId);
    List<ListBean> getListsBySiteId(int siteId);
    List<ListBean> getListsAllDownBySiteId(int siteId);
    List<ListBean> getListsByAllDown();
    List<ListBean> getListsByAll();
    List<ListBean> getListsByFavorite();
    List<ListBean> getListsByRecommend();
    List<ListBean> getListsByRecommendDown();
    List<ListBean> getListsByNotRead();
    List<ListBean> getListsByQueryKey(String queryKey);
    List<ListBean> getListsDownByQueryKey(String queryKey);
    void setListsBySiteId(int siteId);
    void loadListDataBySite(SiteBean site);
    void downImageByList(ListBean list, int position);
    void updateList(ListBean list);
    void updateList(List<ListBean> lists);
    void cancelDown(List<ListBean> lists);
    void deleteList(ListBean list);
    void insertSearch(SearchBean search);
    void updateIsNew(int siteId);
}
