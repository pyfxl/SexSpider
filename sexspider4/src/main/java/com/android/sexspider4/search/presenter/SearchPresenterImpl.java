package com.android.sexspider4.search.presenter;

import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.search.bean.SearchBean;
import com.android.sexspider4.search.model.ISearchModel;
import com.android.sexspider4.search.model.SearchModelImpl;
import com.android.sexspider4.search.view.ISearchView;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class SearchPresenterImpl implements ISearchPresenter {
    private ISearchModel searchModel;

    public SearchPresenterImpl() {
        searchModel = new SearchModelImpl();
    }

    public SearchPresenterImpl(ISearchView searchView) {
        searchModel = new SearchModelImpl();
    }

    @Override
    public List<SearchBean> query(int type) {
        return searchModel.query(type);
    }

    @Override
    public List<SearchBean> queryByKey(String key) {
        return searchModel.queryByKey(key);
    }

    @Override
    public void insert(SearchBean search) {
        searchModel.insert(search);
    }

    @Override
    public void delete(String searchName) {
        searchModel.delete(searchName);
    }

    @Override
    public void updateList(ListBean list) {
        searchModel.updateList(list);
    }

    @Override
    public void delete(int type) {
        searchModel.delete(type);
    }

    @Override
    public List<String> queryDicsByType(int type) {
        return searchModel.queryDicsByType(type);
    }

}
