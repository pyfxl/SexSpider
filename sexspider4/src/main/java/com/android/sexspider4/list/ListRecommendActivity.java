package com.android.sexspider4.list;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.sexspider4.MyApplication;
import com.android.sexspider4.R;
import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.list.view.IListView;
import com.android.sexspider4.search.bean.SearchBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class ListRecommendActivity extends ListActivity implements IListView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //禁用上拉刷新
        disablePullEvent();

        //隐藏浮动按钮
        //floatView.setVisibility(View.VISIBLE);
        //floatView2.setVisibility(View.INVISIBLE);
    }

    @Override
    public List<ListBean> getLists(int siteId) {
        progressBar.setVisibility(View.INVISIBLE);
        return listPresenter.getListsByRecommend();
    }

    @Override
    public void setActionBar() {
        setActionBarTitle(R.string.txt_title_recommend);
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

        //recommendViewOn();

        return true;
    }

    @Override
    public void viewOnMethod() {
        List<ListBean> lists = listPresenter.getListsByRecommendDown();
        setLists(lists);
    }

    @Override
    public void viewOffMethod() {
        //禁用上拉刷新
        disablePullEvent();

        List<ListBean> lists = listPresenter.getListsByRecommend();
        setLists(lists);
    }

    @Override
    public void doRefreshFirst() {
        //列表刷新中不能操作
        if (checkRefreshing()) return;

        //图片下载中不能操作
        if (checkDowning()) return;

        //更新推荐权重
        updateRank();
    }

    //更新推荐权重
    private void updateRank() {
        showToast(this, R.string.txt_list_recommend_start);
        loadStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ListBean> listAll = listPresenter.getListsByAll();
                List<SearchBean> searchs = searchPresenter.query(3);
                List<ListBean> updateLists = new ArrayList();
                for (ListBean list : listAll) {
                    int num = 0;
                    for (SearchBean search : searchs) {
                        if (list.listTitle.contains(search.searchName)) {
                            list.listRank += search.searchCount;
                            num++;
                        }
                    }
                    if (num < MyApplication.SEG_NUMBER) continue;
                    updateLists.add(list);
                }
                listPresenter.updateList(updateLists);
                updateRankEnd();
            }
        }).start();
    }

    //推荐权重更新完成
    private void updateRankEnd() {
        ListRecommendActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(ListRecommendActivity.this, R.string.txt_list_recommend_end);
                refreshStop();
                List<ListBean> recommends = listPresenter.getListsByRecommend();
                setLists(recommends);

                //刷新完viewOn
                //recommendViewOn();
            }
        });
    }

    @Override
    public void updateIsNew() { }

    private void recommendViewOn() {
        //开启查看按钮
        viewOn();

        //开启下拉刷新
        swipeRefreshLayout.setEnabled(true);
    }

    private void disablePullEvent() {
        //禁止下拉上拉事件
        disableRefreshEvent();

        //开启下拉刷新
        swipeRefreshLayout.setEnabled(true);
    }
}
