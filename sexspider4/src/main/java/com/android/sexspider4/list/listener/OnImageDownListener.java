package com.android.sexspider4.list.listener;

import com.android.sexspider4.list.bean.ListBean;

/**
 * Created by feng on 2017/5/5.
 */

public interface OnImageDownListener {
    void onDownStart(int position);
    void onDownProgress(int position);
    void onDownSuccess(int position);
    void onDownComplete(int position);
    void onDownError(int position);
    void onDownEnd(ListBean list, int position);
    void onDownCancel();
}
