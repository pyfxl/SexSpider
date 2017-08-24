package com.android.sexspider4.search.presenter;

import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.search.bean.SearchBean;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public interface ISearchPresenter {
    List<SearchBean> query(int type);
    List<SearchBean> queryByKey(String key);
    void insert(SearchBean search);
    void delete(String searchName);
    void updateList(ListBean list);
    void delete(int type);
    List<String> queryDicsByType(int type);
}
