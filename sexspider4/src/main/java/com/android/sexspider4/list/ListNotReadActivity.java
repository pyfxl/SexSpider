package com.android.sexspider4.list;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.sexspider4.R;
import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.list.view.IListView;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class ListNotReadActivity extends ListActivity implements IListView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //禁止下拉上拉事件
        disableRefreshEvent();
    }

    @Override
    public List<ListBean> getLists(int siteId) {
        progressBar.setVisibility(View.INVISIBLE);
        return listPresenter.getListsByNotRead();
    }

    @Override
    public void setActionBar() {
        setActionBarTitle(R.string.txt_title_notread);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);

        final Menu m = menu;

        final MenuItem downloadItem = menu.findItem(R.id.action_download);
        downloadView = (ImageView) MenuItemCompat.getActionView(downloadItem);
        downloadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.performIdentifierAction(downloadItem.getItemId(), 0);
                doDownload();
            }
        });

        //开启查看按钮
        viewOn();

        return true;
    }

    @Override
    public void viewOnMethod() {
        List<ListBean> lists = listPresenter.getListsByNotRead();
        setLists(lists);
    }

    @Override
    public void viewOffMethod() {
        List<ListBean> lists = listPresenter.getListsByAllDown();
        setLists(lists);
    }

    @Override
    public void doRefreshFirst() { }

    @Override
    public void enableRefreshEvent() { }

    @Override
    public void updateIsNew() { }

    @Override
    public void checkListsEmpty() {
        emptyText.setText(R.string.txt_list_empty_normal);
        super.checkListsEmpty();
    }

}
