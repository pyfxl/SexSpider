package com.android.sexspider4.search.model;

import com.android.sexspider4.MyApplication;
import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.list.db.ListAccess;
import com.android.sexspider4.search.bean.SearchBean;
import com.android.sexspider4.search.db.SearchAccess;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class SearchModelImpl implements ISearchModel {
    private SearchAccess searchAccess;
    private ListAccess listAccess;

    public SearchModelImpl() {
        searchAccess = new SearchAccess(MyApplication.getAppContext());
        listAccess = new ListAccess(MyApplication.getAppContext());
    }

    @Override
    public List<SearchBean> query(int type) {
        return searchAccess.query(type);
    }

    @Override
    public List<SearchBean> queryByKey(String key) {
        return null;
    }

    @Override
    public void insert(SearchBean search) {
        searchAccess.insert(search);
    }

    @Override
    public void delete(String searchName) {
        searchAccess.delete(searchName);
    }

    @Override
    public void updateList(ListBean list) {
        listAccess.update(list);
    }

    @Override
    public void delete(int type) {
        searchAccess.delete(type);
    }

    @Override
    public List<String> queryDicsByType(int type) {
        return searchAccess.queryDicsByType(type);
    }
}
