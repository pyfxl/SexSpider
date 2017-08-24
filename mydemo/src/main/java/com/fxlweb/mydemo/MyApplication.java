package com.fxlweb.mydemo;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.apache.http.protocol.RequestUserAgent;

/**
 * Created by feng on 2016/6/5.
 */

public class MyApplication extends Application {
    public static RequestQueue queue;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.queue = Volley.newRequestQueue(getApplicationContext());
        MyApplication.context = getApplicationContext();
    }

    public static RequestQueue getHttpQueue() {
        return queue;
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
