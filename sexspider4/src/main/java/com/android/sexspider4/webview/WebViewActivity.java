package com.android.sexspider4.webview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.sexspider4.BaseActivity;
import com.android.sexspider4.MyApplication;
import com.android.sexspider4.R;
import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.list.presenter.IListPresenter;
import com.android.sexspider4.webview.persenter.IWebViewPresenter;
import com.android.sexspider4.webview.persenter.WebViewPresenterImpl;
import com.android.sexspider4.webview.view.IWebView;

public class WebViewActivity extends BaseActivity implements IWebView {
    private IWebViewPresenter webViewPresenter;
    private int listId;
    private int position = 0;
    private String url;
    private ProgressBar progressBar;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webViewPresenter = new WebViewPresenterImpl(this);

        //设置视图
        findView();
        setActionBarTitle(R.string.activity_webview);

        //取传入的值
        Intent intent = super.getIntent();
        listId = intent.getIntExtra("listid", 0);
        position = intent.getIntExtra("position", 0);
        url = intent.getStringExtra("url");

        //webview
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        webView.getSettings().setUserAgentString(MyApplication.USER_AGENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.loadUrl(url);
        webView.setWebViewClient(new MyWebViewClient());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(webView!=null) webView.destroy();
    }

    private void doBackError() {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.putExtra("isdown", 3);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    private void doBackSuccess() {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.putExtra("isdown", 1);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    @Override
    public void doBack() {
        //Intent intent = new Intent();
        //intent.putExtra("position", position);
        //setResult(Activity.RESULT_OK, intent);

        finish();
    }

    @Override
    public void findView() {
        progressBar = (ProgressBar) super.findViewById(R.id.progressBar);
    }

    @Override
    public void floatButton() {

    }

    @Override
    public void createError() {
        WebViewActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(WebViewActivity.this, R.string.txt_image_downerror);
                doBackError();
            }
        });
    }

    @Override
    public void createSuccess() {
        WebViewActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(WebViewActivity.this, R.string.txt_image_downcomplete);
                doBackSuccess();
            }
        });
    }

    //网页
    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            view.loadUrl("javascript:window.local_obj.showSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            //System.out.println("====>html="+html);
            ListBean list = webViewPresenter.getListByListId(listId);
            webViewPresenter.createVideoUrl(list, html);
        }
    }
}
