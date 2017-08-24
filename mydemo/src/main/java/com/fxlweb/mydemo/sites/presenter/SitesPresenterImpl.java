package com.fxlweb.mydemo.sites.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.fxlweb.mydemo.sites.bean.SitesBean;
import com.fxlweb.mydemo.sites.model.ISites;
import com.fxlweb.mydemo.sites.model.SitesModel;
import com.fxlweb.mydemo.sites.view.ISitesView;

import java.util.List;

/**
 * Created by feng on 2016/6/5.
 */

public class SitesPresenterImpl implements ISitesPresenter {
    private ISitesView sitesView;
    private ISites sitesModel;

    public SitesPresenterImpl(ISitesView sitesView) {
        this.sitesView = sitesView;
        this.sitesModel = new SitesModel();
    }

    @Override
    public void getLists() {
        List<SitesBean> sitesBean = sitesModel.query();
        sitesView.showLists(sitesBean);
    }

    @Override
    public void doLoad() {
        new Thread() {
            @Override
            public void run() {
                sitesModel.loadSites();

                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public void setProgressBarVisibility(int visibility) {
        sitesView.onSetProgressBarVisibility(visibility);
    }

    @Override
    public void showToast(int resId) {
        sitesView.onShowToast(resId);
    }

    @Override
    public void showEmpty(int visibility) {
        sitesView.onShowEmpty(visibility);
    }

    @Override
    public List<SitesBean> doLoadData() {
        return null;
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    sitesView.onLoadResult(true, 0);
                    break;
            }
        }
    };
}
