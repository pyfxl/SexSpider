package com.android.sexspider4.list.listener;

import com.android.sexspider4.list.bean.ListBean;

/**
 * Created by feng on 2017/5/5.
 */

public interface OnItemClickListener {
    void onItemClick(ListBean list, int position);
    void onItemLongClick(ListBean list, int position);
}
