package com.android.sexspider4.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.android.sexspider4.BaseActivity;
import com.android.sexspider4.DividerItemDecoration;
import com.android.sexspider4.R;
import com.android.sexspider4.list.ListSearchActivity;
import com.android.sexspider4.search.adapter.SearchAdapter;
import com.android.sexspider4.search.bean.SearchBean;
import com.android.sexspider4.search.listener.OnItemClickListener;
import com.android.sexspider4.search.presenter.ISearchPresenter;
import com.android.sexspider4.search.presenter.SearchPresenterImpl;
import com.android.sexspider4.search.view.ISearchView;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class SearchActivity extends BaseActivity implements ISearchView {
    private ISearchPresenter searchPresenter;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<SearchBean> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findView();

        searchPresenter = new SearchPresenterImpl(this);

        lists = getLists();
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchAdapter(lists, new MyOnItemClickListener());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        setActionBarTitle("");
    }

    @Override
    public void doBack() {
        finish();
    }

    @Override
    public void findView() {
        recyclerView = (RecyclerView) super.findViewById(R.id.search_listview);
    }

    @Override
    public void floatButton() { }

    private List<SearchBean> getLists() {
        return searchPresenter.query(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FIRST_REQUEST_CODE) {
            onCreate(null);
        }
    }

    //跳转到ListActivity
    private class MyOnItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(SearchBean search, int position) {
            Intent intent = new Intent(SearchActivity.this, ListSearchActivity.class);
            intent.putExtra("query", search.searchName);
            startActivityForResult(intent, FIRST_REQUEST_CODE);
        }

        @Override
        public void onDeleteClick(String searchName, int position) {
            searchPresenter.delete(searchName);
            lists.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }

}
