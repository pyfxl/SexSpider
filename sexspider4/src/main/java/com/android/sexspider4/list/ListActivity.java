package com.android.sexspider4.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.sexspider4.BaseActivity;
import com.android.sexspider4.DividerItemDecoration;
import com.android.sexspider4.MyApplication;
import com.android.sexspider4.R;
import com.android.sexspider4.helper.ImageHelper;
import com.android.sexspider4.helper.SegHelper;
import com.android.sexspider4.image.ImageActivity;
import com.android.sexspider4.list.adapter.ListAdapter;
import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.list.listener.OnItemClickListener;
import com.android.sexspider4.list.presenter.IListPresenter;
import com.android.sexspider4.list.presenter.ListPresenterImpl;
import com.android.sexspider4.list.view.IListView;
import com.android.sexspider4.search.bean.SearchBean;
import com.android.sexspider4.search.presenter.ISearchPresenter;
import com.android.sexspider4.search.presenter.SearchPresenterImpl;
import com.android.sexspider4.site.bean.SiteBean;
import com.android.sexspider4.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class ListActivity extends BaseActivity implements IListView {
    protected IListPresenter listPresenter;
    protected SiteBean site;
    protected ProgressBar progressBar;
    protected RecyclerView recyclerView;
    protected ListAdapter adapter;
    protected List<ListBean> lists;
    protected TextView emptyText;
    protected List<ListBean> downLists = new ArrayList<>();
    protected String siteLink = "";
    protected ImageView floatView;
    protected ImageView floatView2;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ISearchPresenter searchPresenter;
    protected RecyclerView.OnScrollListener myOnScrollListener;

    protected RelativeLayout tipsLayout;
    protected TextView tipsText;
    protected ImageView tipsImage;
    protected List<ListBean> tipsList = new ArrayList<>();

    protected int position = 0;
    protected String queryKey = "1=2";

    protected boolean canLoadNext = false;
    protected boolean isLoadNext = false;
    protected boolean isListEnd = false;
    protected boolean isLoadError = false;
    protected int isUpdated = 0;

    //菜单
    protected ImageView refreshView;
    protected ImageView downloadView;
    protected ImageView imageView;
    protected boolean refreshFlag = false;
    protected boolean downloadFlag = false;
    protected boolean cancelFlag = false;
    protected boolean viewFlag = false;
    protected boolean bottomFlag = false;
    protected boolean scrollFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //设置视图
        findView();

        //传入的参数
        Intent intent = super.getIntent();
        int siteId = intent.getIntExtra("siteid", 0);
        position = intent.getIntExtra("position", 0);
        queryKey = (queryKey.equals("") ? intent.getStringExtra("query") : queryKey);
        queryKey = (queryKey == null ? "" : queryKey);

        //MVP中间层
        listPresenter = new ListPresenterImpl(this);
        listPresenter.getSiteById(siteId);
        searchPresenter = new SearchPresenterImpl();

        //更新最新下载项
        updateIsNew();

        //列表recycler
        lists = getLists(siteId);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ListAdapter(lists, this, new MyOnItemClickListener());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setItemAnimator(null);
        recyclerView.addOnScrollListener(myOnScrollListener = new MyOnScrollListener());
        recyclerView.addOnScrollListener(new MyBottomOnScrollListener());

        //下拉刷新
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent_1, R.color.colorAccent_2, R.color.colorAccent_3);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefreshFirst();
            }
        });

        //顶部提示
        tipsLayout.setVisibility(View.INVISIBLE);
        tipsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = tipsList.size() - 1;

                ListBean tips = tipsList.get(position);
                ListBean list = getTipsList(tips);
                position = lists.indexOf(list);

                //跳转图片页
                startImageActivity(list, position);
            }
        });

        //隐藏浮动按钮
        floatView.setVisibility(View.INVISIBLE);

        //检查列表是否为空
        checkListsEmpty();

        //设置标题栏
        setActionBar();

        //浮动按钮事件
        floatButton();

        //下拉刷新
        //doRefreshFirst();
    }

    @Override
    public void doBack() {
        //列表刷新中不能退出
        if (checkRefreshing()) return;

        downloadFlag = false;
        listPresenter.cancelDown(downLists);

        Intent intent = new Intent();
        intent.putExtra("isupdated", isUpdated);
        intent.putExtra("position", position);
        intent.putExtra("query", queryKey);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    @Override
    public void findView() {
        progressBar = (ProgressBar) super.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) super.findViewById(R.id.list_recyclerview);
        emptyText = (TextView) super.findViewById(R.id.emptyText);
        floatView = (ImageView) super.findViewById(R.id.imageView);
        floatView2 = (ImageView) super.findViewById(R.id.imageView2);
        swipeRefreshLayout = (SwipeRefreshLayout) super.findViewById(R.id.swipe_refresh_layout);
        tipsLayout = (RelativeLayout) super.findViewById(R.id.tips_layout);
        tipsText = (TextView) super.findViewById(R.id.tipsText);
        tipsImage = (ImageView) super.findViewById(R.id.tipsImage);
    }

    @Override
    public void floatButton() {
        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRefreshFirst();
            }
        });
        floatView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doView();
            }
        });
    }

    //设置列表数据
    protected List<ListBean> getLists(int siteId) {
        progressBar.setVisibility(View.INVISIBLE);
        return listPresenter.getListsBySiteId(siteId);
    }

    //设置标题
    protected void setActionBar() {
        setActionBarTitle(site.getSiteTitle());
    }

    @Override
    public void setSiteBean(SiteBean site) {
        this.site = site;
        this.site.isUpdated = getIntent().getIntExtra("isupdated", 0);
        siteLink = site.siteLink;
    }

    @Override
    public void setLists(List<ListBean> lists) {
        this.lists.clear();
        this.lists.addAll(lists);
        adapter.notifyDataSetChanged();

        checkListsEmpty();
    }

    @Override
    public void loadStart() {
        ListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshStart();
                emptyText.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void loadProgress() {
        ListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(ListActivity.this, R.string.txt_list_refresh);
            }
        });
    }

    @Override
    public void loadSuccess() {
        isListEnd = false;
        canLoadNext = false;
        isLoadError = false;
        isUpdated = 1;
        ListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(ListActivity.this, R.string.txt_list_success);
                listPresenter.setListsBySiteId(site.siteId);
            }
        });
    }

    @Override
    public void loadSuccess(List<ListBean> lists) {

    }

    @Override
    public void loadNoUpdate() {
        isUpdated = 1;
        ListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(ListActivity.this, R.string.txt_list_noupdate);
            }
        });
    }

    @Override
    public void loadError() {
        isLoadError = true;
        isUpdated = 2;
        ListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(ListActivity.this, R.string.txt_list_error);
            }
        });
    }

    @Override
    public void loadEnd() {
        ListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                checkListsEmpty();
                refreshStop();
                setActionBar();
                if (!isLoadError && site.isFirst==0) continueLoad();
            }
        });
    }

    @Override
    public void downStart(final int position) {
        ListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (downLists.size() > 0) {
                    downloadFlag = true;
                    downloadStart();
                }
                showToast(ListActivity.this, R.string.txt_image_downstart);
                adapter.notifyItemChanged(position);
            }
        });
    }

    @Override
    public void downProgress(final int position) {
        ListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemChanged(position);
            }
        });
    }

    @Override
    public void downSuccess(final int position) {
        ListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemChanged(position);
            }
        });
    }

    @Override
    public void downComplete(final int position) {
        ListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(ListActivity.this, R.string.txt_image_downcomplete);
                adapter.notifyItemChanged(position);
            }
        });
    }

    @Override
    public void downError(final int position) {
        ListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(ListActivity.this, R.string.txt_image_downerror);
                adapter.notifyItemChanged(position);
            }
        });
    }

    @Override
    public void downEnd(final ListBean list, final int position) {
        ListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemChanged(position);
                downLists.remove(list);
                if (downLists.size() <= 0) {
                    downloadStop();
                    downloadFlag = false;
                }

                //添加到tips
                if (list.isRead == 0 && (list.isDown == 1 || list.isDown == 2)) {
                    if (!tipsList.contains(list)) {
                        tipsList.add(list);
                        updateTipsView();
                    }
                }
            }
        });
    }

    @Override
    public void downCancel() {
        ListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cancelFlag = false;
                showToast(ListActivity.this, R.string.txt_image_downcancel);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);

        final Menu m = menu;

        final MenuItem downloadItem = menu.findItem(R.id.action_download);
        downloadView = (ImageView)downloadItem.getActionView();
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
            case R.id.action_viewdown:
                doView();
                break;
            case R.id.action_jumptobottom:
                toBottom();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FIRST_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                if (data != null) {
                    int position = data.getIntExtra("position", 0);
                    ListBean list = lists.get(position);
                    list.isDown = 3;
                    list.isRead = 0;
                    listPresenter.updateList(list);
                    adapter.notifyItemChanged(position);
                }
                showToast(ListActivity.this, R.string.txt_image_downerror);
            }
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    int position = data.getIntExtra("position", 0);
                    ListBean list = lists.get(position);
                    list.isFavorite = data.getIntExtra("isfavorite", 0);
                    adapter.notifyItemChanged(position);
                }
            }
        }
    }

    //列表点击跳转
    protected class MyOnItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(final ListBean list, final int position) {
            //列表刷新中不能点击
            if (checkRefreshing()) return;

            //图片正在下载中
            if (checkDowning(list)) return;

            //分词
            if (list.isDown == 0) {
                String words = SegHelper.getSegString(list.listTitle, getStopDics());
                addSegString(words);
            }

            //如果未下载就开始下载
            if (list.isDown == 0) {
                downLists.add(list);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        listPresenter.downImageByList(list, position);
                    }
                }).start();
                return;
            }

            //跳转图片页
            startImageActivity(list, position);
        }

        @Override
        public void onItemLongClick(final ListBean list, final int position) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(ListActivity.this);
            dialog.setMessage(R.string.txt_dialog_title);
            //取消
            dialog.setNegativeButton(R.string.txt_dialog_delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (checkDowning()) return;
                    listPresenter.deleteList(list);
                    ImageHelper.deleteImage(list);

                    lists.remove(list);
                    downLists.remove(list);
                    removeTipsView(list);
                    adapter.notifyItemRemoved(position);
                }
            });
            //确定
            dialog.setPositiveButton(R.string.txt_dialog_redown, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (checkDowning(list)) return;
                    list.isRedown = 1;
                    downLists.add(list);
                    if (tipsList.contains(list)) {
                        tipsList.remove(list);
                        updateTipsView();
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            listPresenter.downImageByList(list, position);
                        }
                    }).start();
                }
            });
            dialog.show();

            setVibrator();
        }
    }

    //列表滚动
    protected class MyOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            //如果滚动停止，又到了底部，又可以向上拉，就加载下一页
            if (newState == RecyclerView.SCROLL_STATE_IDLE && isListEnd && canLoadNext) {
                //设置下一页url
                setNextPageToSite();
                site.isFirst = 0;

                //开始刷新
                doRefresh();

                isLoadNext = true;
            } else {
                isLoadNext = false;
            }

            //如果滚动停止，又到了底部，标记可以向上拉并提示
            if (newState == RecyclerView.SCROLL_STATE_IDLE && isListEnd && !isLoadNext) {
                canLoadNext = true;
                showToast(ListActivity.this, String.format(getString(R.string.txt_list_loadnext), String.valueOf(site.listNum)));
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();

            //判断是否到了底部
            isListEnd = (totalItemCount <= visibleItemCount || (dy > 0 && recyclerView.canScrollVertically(-1) && !recyclerView.canScrollVertically(1)));

            if (!isListEnd) {
                canLoadNext = false;
            }
        }
    }

    //列表滚动底部
    protected class MyBottomOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            scrollFlag = newState == RecyclerView.SCROLL_STATE_SETTLING;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (!scrollFlag) {
                //dy<0上滑，dy>0下滑
                if (dy < 0) {
                    bottomFlag = true;
                } else if (dy > 0) {
                    bottomFlag = false;
                }
            }
        }
    }

    //跳转图片页
    protected void startImageActivity(ListBean list, int position) {
        //删除提示
        removeTipsView(list);

        //设置当前项为已读
        if (list.isRead == 0) {
            list.isRead = 1;
            listPresenter.updateList(list);
            adapter.notifyItemChanged(position);
        }

        //跳转图片页面
        Intent intent = new Intent(ListActivity.this, ImageActivity.class);
        intent.putExtra("listid", list.listId);
        intent.putExtra("title", list.listTitle);
        intent.putExtra("isfavorite", list.isFavorite);
        intent.putExtra("position", position);
        startActivityForResult(intent, FIRST_REQUEST_CODE);
    }

    //下载图片
    protected void downloadImage() {
        //检查是否有下载
        if (checkNoDown()) return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (ListBean list : lists) {
                    if (!downloadFlag) break;
                    if (list.isDown == 1 || list.isDowning == 1) continue;
                    if (list.isDown > 0) list.isRedown = 1;
                    int position = lists.indexOf(list);
                    downLists.add(list);
                    listPresenter.downImageByList(list, position);
                }
            }
        }).start();
    }

    //刷新动画开始
    protected void refreshStart() {
        refreshFlag = true;
        if (refreshView != null && refreshView.getVisibility() != View.INVISIBLE) refreshView.startAnimation(getRefreshAnim());
        if (floatView != null && floatView.getVisibility() != View.INVISIBLE) floatView.startAnimation(getRefreshAnim());
        swipeRefreshLayout.setRefreshing(true);
    }

    //刷新动画停止
    protected void refreshStop() {
        refreshFlag = false;
        if (refreshView != null && refreshView.getVisibility() != View.INVISIBLE) refreshView.clearAnimation();
        if (floatView != null && floatView.getVisibility() != View.INVISIBLE) floatView.clearAnimation();
        swipeRefreshLayout.setRefreshing(false);
    }

    protected Animation getRefreshAnim() {
        Animation animRefresh = AnimationUtils.loadAnimation(ListActivity.this, R.anim.anim_refresh);
        animRefresh.setRepeatCount(Animation.INFINITE);
        return animRefresh;
    }

    //下载动画开始
    protected void downloadStart() {
        if (downloadView != null) {
            downloadView.setImageResource(R.drawable.anim_download);
            ((AnimationDrawable) downloadView.getDrawable()).start();
        }
    }

    //下载动画停止
    protected void downloadStop() {
        if (downloadView != null) downloadView.setImageResource(R.drawable.ic_action_download);
    }

    //查看按钮开
    protected void viewOn() {
        viewFlag = true;
        if (imageView != null) imageView.setImageResource(R.drawable.ic_action_view_on);
        if (floatView2 != null) floatView2.setImageResource(R.drawable.ic_action_view_on);
        disableRefreshEvent();
    }

    //查看按钮关
    protected void viewOff() {
        viewFlag = false;
        if (imageView != null) imageView.setImageResource(R.drawable.ic_action_view_off);
        if (floatView2 != null) floatView2.setImageResource(R.drawable.ic_action_view_off);
        enableRefreshEvent();
    }

    //线程加载列表
    protected void loadListDataBySite() {
        //列表刷新中不能操作
        if (checkRefreshing()) return;

        //图片下载中不能操作
        if (checkDowning()) return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                listPresenter.loadListDataBySite(site);
            }
        }).start();
    }

    //设置振动
    protected void setVibrator() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
        vibrator.cancel();
    }

    //取下一页列表
    protected void setNextPageToSite() {
        String link = siteLink.substring(0, siteLink.lastIndexOf("/") + 1);
        if (site.siteId == 58) {
            site.siteLink = link + site.listPage.replace("(*)", String.valueOf(site.listNum-1)).replace("(%)", site.siteNotes);
        } else {
            site.siteLink = link + site.listPage.replace("(*)", String.valueOf(site.listNum));
        }
    }

    //刷新按钮
    protected void doRefresh() {
        if (viewFlag) {
            viewOff();
            viewOffMethod();
        }
        loadListDataBySite();
    }

    //下载按钮
    protected void doDownload() {
        if (lists.size() <= 0) {
            showToast(ListActivity.this, R.string.txt_image_nodown);
            return;
        }

        if (cancelFlag) {
            showToast(ListActivity.this, R.string.txt_image_canceling);
            return;
        }
        if (downloadFlag) {
            downloadFlag = false;
            cancelFlag = true;
            showToast(ListActivity.this, R.string.txt_image_canceling);
            listPresenter.cancelDown(downLists);
        } else {
            downloadFlag = true;
            downloadImage();
        }
    }

    //查看按钮
    protected void doView() {
        //图片下载中不能操作
        if (checkDowning()) return;

        if (viewFlag) {
            viewOff();
            viewOffMethod();
        } else {
            viewOn();
            viewOnMethod();
        }
    }

    //只查看下载项
    protected void viewOnMethod() {
        List<ListBean> lists = listPresenter.getListsAllDownBySiteId(site.siteId);
        setLists(lists);
    }

    //查看未下载项
    protected void viewOffMethod() {
        List<ListBean> lists = listPresenter.getListsBySiteId(site.siteId);
        setLists(lists);
    }

    //滚动到底部
    protected void toBottom() {
        if (adapter.getItemCount() > 0) {
            if (bottomFlag) {
                bottomFlag = false;
                recyclerView.smoothScrollToPosition(0);
            } else {
                bottomFlag = true;
                recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
            }
        }
    }

    //检查是否下载中
    protected boolean checkDowning() {
        if (downLists.size() > 0) {
            swipeRefreshLayout.setRefreshing(false);
            showToast(ListActivity.this, R.string.txt_image_hasdown);
            return true;
        }
        return false;
    }

    //检查是否有下载
    protected boolean checkNoDown() {
        int count = 0;
        for (ListBean list : lists) {
            if (list.isDown == 1 || list.isDowning == 1) continue;
            count++;
        }
        if (count == 0) {
            downloadFlag = false;
            showToast(ListActivity.this, R.string.txt_image_nodown);
            return true;
        }

        return false;
    }

    //检查是否下载中
    protected boolean checkDowning(ListBean list) {
        if (list.isDowning == 1) {
            showToast(ListActivity.this, R.string.txt_image_hasdown);
            return true;
        }
        return false;
    }

    //检查是否刷新中
    protected boolean checkRefreshing() {
        if (refreshFlag) {
            showToast(ListActivity.this, R.string.txt_list_isloading);
            return true;
        }
        return false;
    }

    //检查列表是否为空
    protected void checkListsEmpty() {
        if (lists.size() > 0) {
            emptyText.setVisibility(View.INVISIBLE);
        } else {
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    //刷新第一页
    protected void doRefreshFirst() {
        site.siteLink = siteLink;
        site.isFirst = 1;
        doRefresh();
    }

    //禁止刷新事件
    protected void disableRefreshEvent() {
        //移除列表滚动事件
        recyclerView.removeOnScrollListener(myOnScrollListener);

        //禁止下拉刷新
        swipeRefreshLayout.setEnabled(false);
    }

    //开启刷新事件
    protected void enableRefreshEvent() {
        //添加列表滚动事件
        recyclerView.addOnScrollListener(myOnScrollListener);

        //开启下拉刷新
        swipeRefreshLayout.setEnabled(true);
    }

    //连续加载
    protected void continueLoad() {
        if((site.listNum-1) % MyApplication.LIST_PAGE != 0) {
            setNextPageToSite();
            site.isFirst = 0;
            doRefresh();
        }
    }

    //更新提示布局
    protected void updateTipsView() {
        int position = tipsList.size() - 1;
        if (position < 0) {
            tipsLayout.setVisibility(View.INVISIBLE);
            return;
        }

        ListBean list = tipsList.get(position);

        tipsImage.setVisibility(View.GONE);
        if (list.listPicture != null && !list.listPicture.equals("")) {
            tipsImage.setImageBitmap(ImageHelper.getThumbImage(list));
            tipsImage.setVisibility(View.VISIBLE);
        }
        tipsText.setText(list.listTitle);
        tipsLayout.setVisibility(View.VISIBLE);

        if (list.isDown == 2) {
            tipsText.setTextColor(this.getResources().getColor(R.color.list_hasdown));
        } else {
            tipsText.setTextColor(this.getResources().getColor(R.color.list_isdown));
        }
    }

    //删除顶部tips
    protected void removeTipsView(ListBean list) {
        ListBean tips = getListTips(list);
        if (tipsList.contains(tips)) {
            tipsList.remove(tips);
            updateTipsView();
        }
    }

    //取tipsList
    protected ListBean getTipsList(ListBean tips) {
        for (ListBean list : lists) {
            if (list.listId == tips.listId) return list;
        }

        return null;
    }

    //取listTips
    protected ListBean getListTips(ListBean list) {
        for (ListBean tips : tipsList) {
            if (list.listId == tips.listId) return tips;
        }

        return null;
    }

    //处理分词
    protected void addSegString(String s) {
        if (s.equals("")) return;

        String[] arr = s.split("\\|");
        for (String str : arr) {
            if (StringUtils.strLength(str) < MyApplication.SEG_LENGTH) continue;
            searchPresenter.insert(new SearchBean(str, 3));
        }
    }

    //取停止词
    protected List<String> getStopDics() {
        return searchPresenter.queryDicsByType(2);
    }

    //更新最新下载项
    protected void updateIsNew() {
        listPresenter.updateIsNew(site.siteId);
    }

}
