package com.fxlweb.mydemo.sites;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fxlweb.mydemo.R;
import com.fxlweb.mydemo.sites.adapter.SitesAdapter;
import com.fxlweb.mydemo.sites.bean.SitesBean;
import com.fxlweb.mydemo.sites.presenter.ISitesPresenter;
import com.fxlweb.mydemo.sites.presenter.SitesPresenterImpl;
import com.fxlweb.mydemo.sites.view.ISitesView;

import java.util.ArrayList;
import java.util.List;

public class SitesActivity extends Activity implements ISitesView {
    private ListView listView;
    private List<SitesBean> lists;
    private SitesAdapter adapter;
    private ISitesPresenter sitesPresenter;
    private ProgressBar progressBar;
    private TextView textEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites);

        progressBar = (ProgressBar) super.findViewById(R.id.progressBar);
        listView = (ListView) super.findViewById(R.id.sites_listview);
        textEmpty = (TextView) super.findViewById(R.id.textEmpty);

        sitesPresenter = new SitesPresenterImpl(this);
        sitesPresenter.showEmpty(View.INVISIBLE);
        sitesPresenter.getLists();

        adapter = new SitesAdapter(this, lists);
        listView.setAdapter(adapter);
    }

    @Override
    public void showLists(List<SitesBean> lists) {
        this.lists = lists;
    }

    @Override
    public void onLoadResult(Boolean result, int code) {
        sitesPresenter.setProgressBarVisibility(View.INVISIBLE);
        sitesPresenter.getLists();
    }

    @Override
    public void onSetProgressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void onShowToast(int resId) {
        Toast.makeText(SitesActivity.this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowEmpty(int visibility) {
        textEmpty.setVisibility(visibility);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            sitesPresenter.setProgressBarVisibility(View.VISIBLE);
            sitesPresenter.doLoad();
        }

        return super.onOptionsItemSelected(item);
    }

}
