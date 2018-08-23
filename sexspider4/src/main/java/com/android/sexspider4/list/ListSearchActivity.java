package com.android.sexspider4.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.android.sexspider4.R;
import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.list.view.IListView;
import com.android.sexspider4.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class ListSearchActivity extends ListActivity implements IListView {
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //禁止下拉上拉事件
        disableRefreshEvent();
    }

    @Override
    public List<ListBean> getLists(int siteId) {
        progressBar.setVisibility(View.INVISIBLE);
        return listPresenter.getListsByQueryKey(queryKey);
    }

    @Override
    public void setActionBar() {
        setActionBarTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.collapseActionView();
        searchItem.expandActionView();

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconified(false);
        searchView.setSubmitButtonEnabled(true);

        int goImgId = searchView.getContext().getResources().getIdentifier("android:id/search_go_btn", null, null);
        ImageView searchButton = (ImageView) searchView.findViewById(goImgId);
        searchButton.setImageResource(R.drawable.ic_action_search);

        int closeImgId = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        final ImageView closeButton = (ImageView) searchView.findViewById(closeImgId);
        closeButton.setVisibility(View.INVISIBLE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                closeButton.setVisibility(View.INVISIBLE);
                if (!StringUtils.isEmpty(newText)) {
                    //图片下载中不能操作
                    if (checkDowning()) return true;

                    queryKey = newText;
                    List<ListBean> lists = listPresenter.getListsByQueryKey(queryKey);

                    if (viewFlag) {
                        lists = listPresenter.getListsDownByQueryKey(queryKey);
                    }

                    setLists(lists);
                }

                return true;
            }
        });

        final Menu m = menu;

        final MenuItem downloadItem = menu.findItem(R.id.action_download);
        downloadView = (ImageView)MenuItemCompat.getActionView(downloadItem);
        downloadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.performIdentifierAction(downloadItem.getItemId(), 0);
                doDownload();
            }
        });

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FIRST_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                searchView.clearFocus();
            }
        }
    }

    @Override
    public void viewOnMethod() {
        List<ListBean> lists = listPresenter.getListsDownByQueryKey(queryKey);
        setLists(lists);
    }

    @Override
    public void viewOffMethod() {
        List<ListBean> lists = listPresenter.getListsByQueryKey(queryKey);
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
