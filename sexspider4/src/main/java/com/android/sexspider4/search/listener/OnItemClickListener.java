package com.android.sexspider4.search.listener;

import com.android.sexspider4.search.bean.SearchBean;

/**
 * Created by feng on 2017/5/5.
 */

public interface OnItemClickListener {
    void onItemClick(SearchBean search, int position);
    void onDeleteClick(String searchName, int position);
}
