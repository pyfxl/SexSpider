package com.android.sexspider4.webview.persenter;

import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.webview.listener.OnCreateDataListener;
import com.android.sexspider4.webview.model.IWebViewModel;
import com.android.sexspider4.webview.model.WebViewModelImpl;
import com.android.sexspider4.webview.view.IWebView;

public class WebViewPresenterImpl implements IWebViewPresenter, OnCreateDataListener {
    private IWebViewModel webViewModel;
    private IWebView webView;

    public WebViewPresenterImpl(IWebView webView){
        this.webViewModel = new WebViewModelImpl();
        this.webView = webView;
    }

    @Override
    public void createVideoUrl(ListBean list, String html) {
        webViewModel.createVideoUrl(list, html, this);
    }

    @Override
    public ListBean getListByListId(int listId) {
        return webViewModel.getListByListId(listId);
    }

    @Override
    public void onCreateError() {
        webView.createError();
    }

    @Override
    public void onCreateSuccess() {
        webView.createSuccess();
    }
}
