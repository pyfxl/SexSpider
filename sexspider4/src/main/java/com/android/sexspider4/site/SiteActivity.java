package com.android.sexspider4.site;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.sexspider4.BaseActivity;
import com.android.sexspider4.DividerItemDecoration;
import com.android.sexspider4.R;
import com.android.sexspider4.helper.ImageHelper;
import com.android.sexspider4.list.ListActivity;
import com.android.sexspider4.list.ListAllDownActivity;
import com.android.sexspider4.list.ListFavoriteActivity;
import com.android.sexspider4.list.ListNotReadActivity;
import com.android.sexspider4.list.ListRecommendActivity;
import com.android.sexspider4.list.ListSearchActivity;
import com.android.sexspider4.list.presenter.IListPresenter;
import com.android.sexspider4.list.presenter.ListPresenterImpl;
import com.android.sexspider4.search.presenter.ISearchPresenter;
import com.android.sexspider4.search.presenter.SearchPresenterImpl;
import com.android.sexspider4.site.adapter.SiteAdapter;
import com.android.sexspider4.site.bean.SiteBean;
import com.android.sexspider4.site.listener.OnItemClickListener;
import com.android.sexspider4.site.presenter.ISitePresenter;
import com.android.sexspider4.site.presenter.SitePresenterImpl;
import com.android.sexspider4.site.view.ISiteView;
import com.android.sexspider4.utils.FileUtils;

import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.dic.Dictionary;

import java.io.IOException;
import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class SiteActivity extends BaseActivity implements ISiteView {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<SiteBean> lists;
    private ISitePresenter sitePresenter;
    private ProgressBar progressBar;
    private TextView emptyText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyOnRefreshListener myOnRefreshListener;
    protected ISearchPresenter searchPresenter;
    protected int isUpdated = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        //加载视图
        findView();

        //MVP中间层
        sitePresenter = new SitePresenterImpl(this);
        searchPresenter = new SearchPresenterImpl();

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent_1, R.color.colorAccent_2, R.color.colorAccent_3);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        myOnRefreshListener = new MyOnRefreshListener();
        swipeRefreshLayout.setOnRefreshListener(myOnRefreshListener);

        lists = getLists();
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SiteAdapter(lists, this, new MyOnItemClickListener());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        //设置标题栏
        setActionBarTitle(R.string.app_name);

        floatButton();

        //初始词典
        initSegDics();
    }

    @Override
    public void doBack() {
        //writeLog();
        finish();
    }

    @Override
    public void findView() {
        progressBar = (ProgressBar) super.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) super.findViewById(R.id.site_recyclerview);
        emptyText = (TextView) super.findViewById(R.id.emptyText);
        swipeRefreshLayout = (SwipeRefreshLayout) super.findViewById(R.id.swipe_refresh_layout);
    }

    //取列表数据
    private List<SiteBean> getLists() {
        progressBar.setVisibility(View.INVISIBLE);
        List<SiteBean> lists = sitePresenter.getLists();
        if (lists.size() > 0){
            emptyText.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    myOnRefreshListener.onRefresh();
                }
            });
        }

        return lists;
    }

    @Override
    public void floatButton() {
        ImageView floatView = (ImageView) super.findViewById(R.id.imageView);
        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFavorite();
            }
        });
    }

    @Override
    public void setLists(List<SiteBean> lists) {
        if (lists.size() > 0) {
            emptyText.setVisibility(View.INVISIBLE);
            this.lists.clear();
            this.lists.addAll(lists);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadStart() {
        SiteActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                emptyText.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void loadProgress() {
        SiteActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(SiteActivity.this, R.string.txt_list_refresh);
            }
        });
    }

    @Override
    public void loadSuccess() {
        //初始词典
        initSegDics();

        SiteActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(SiteActivity.this, R.string.txt_list_success);
                sitePresenter.setLists();

                //批量加载列表数据
                loadListData();
            }
        });

    }

    @Override
    public void loadEnd() {
        SiteActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                if (lists.size() <= 0) {
                    emptyText.setVisibility(View.VISIBLE);
                } else {
                    emptyText.setVisibility(View.INVISIBLE);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void loadError() {
        SiteActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(SiteActivity.this, R.string.txt_list_error);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_site, menu);

        final Menu m = menu;

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        ImageView searchView = (ImageView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.performIdentifierAction(searchItem.getItemId(), 0);
                doSearch();
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                doBack();
                break;
            case R.id.action_favorite:
                doFavorite();
                break;
            case R.id.action_recommend:
                doRecommend();
                break;
            case R.id.action_viewall:
                doViewAll();
                break;
            case R.id.action_viewnotread:
                doViewNotRead();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == FIRST_REQUEST_CODE) {
            if (data != null) {
                int position = data.getIntExtra("position", 0);
                int isUpdated = data.getIntExtra("isupdated", 0);
                SiteBean site = lists.get(position);
                if (isUpdated != 0) site.isUpdated = isUpdated;
                adapter.notifyItemChanged(position);
            }
        }
    }

    //跳转到ListActivity
    private class MyOnItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(SiteBean site, int position) {
            site.isUpdated = 1;
            adapter.notifyItemChanged(position);

            Intent intent = new Intent(SiteActivity.this, ListActivity.class);
            intent.putExtra("siteid", site.siteId);
            intent.putExtra("isupdated", site.isUpdated);
            intent.putExtra("position", position);
            startActivityForResult(intent, FIRST_REQUEST_CODE);
        }
    }

    //swipe刷新
    private class MyOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            doRefresh();
        }
    }

    //批量加载列表数据
    private void loadListData() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SiteActivity.this);
        dialog.setMessage(R.string.txt_dialog_auto_title);
        //不加载
        dialog.setNegativeButton(R.string.txt_dialog_auto_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        //加载
        dialog.setPositiveButton(R.string.txt_dialog_auto_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0, n=lists.size(); i<n; i++) {
                            SiteBean site = lists.get(i);
                            if(site == null) continue;
                            site.isFirst = 1;//指定第一页
                            sitePresenter.loadListDataBySite(site);
                        }
                    }
                }).start();
            }
        });
        dialog.show();
    }

    @Override
    public void listLoadStart(final SiteBean site) {
        SiteActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(true);

                site.isUpdated = 5;
                int position = lists.indexOf(site);
                adapter.notifyItemChanged(position);
            }
        });
    }

    @Override
    public void listLoadEnd(final SiteBean site) {
        SiteActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int position = lists.indexOf(site);
                adapter.notifyItemChanged(position);

                if(position == lists.size()-1) swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    //刷新按钮
    private void doRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sitePresenter.loadWebData();
            }
        }).start();
    }

    //查找按钮
    private void doSearch() {
        Intent intent = new Intent(SiteActivity.this, ListSearchActivity.class);
        startActivity(intent);
    }

    //查看按钮
    private void doViewAll() {
        Intent intent = new Intent(SiteActivity.this, ListAllDownActivity.class);
        startActivity(intent);
    }

    //喜爱按钮
    private void doFavorite() {
        Intent intent = new Intent(SiteActivity.this, ListFavoriteActivity.class);
        startActivity(intent);
    }

    //推荐按钮
    private void doRecommend() {
        Intent intent = new Intent(SiteActivity.this, ListRecommendActivity.class);
        startActivity(intent);
    }

    //未读按钮
    private void doViewNotRead() {
        Intent intent = new Intent(SiteActivity.this, ListNotReadActivity.class);
        startActivity(intent);
    }

    //写入日志
    private void writeLog() {
        try {
            Process process = Runtime.getRuntime().exec("logcat -d -v time -s SEG");
            FileUtils.writeFile("log.txt", process.getInputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //初始词典
    private void initSegDics() {
        List<String> extDics = searchPresenter.queryDicsByType(1);
        Dictionary.initial(DefaultConfig.getInstance());
        Dictionary.getSingleton().addWords(extDics);
    }

}
