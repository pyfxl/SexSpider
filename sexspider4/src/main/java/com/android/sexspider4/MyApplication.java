package com.android.sexspider4;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by feng on 2017/5/5.
 */

public class MyApplication extends Application {

    //上下文
    public static Context context;

    //站点外部文件
    public static final String SITE_URL = "http://www.fxlweb.com/SexSpider/GetListData4.aspx";

    //浏览器标识
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3141.8 Safari/537.36";

    //连接超时设置
    public static final int DEFAULT_TIMEOUT = 5 * 1000;

    //一次获取的页数
    public static final int LIST_PAGE = 10;

    //权重总数太小不加载
    public static final int SEARCH_COUNT_MAX = 20;

    //词典长度，中文2英文1，小于长度不保存
    public static final int SEG_LENGTH = 4;

    //词典匹配数量
    public static final int SEG_NUMBER = 2;

    //默认目录
    public static final String FILE_PATH = Environment.getExternalStorageDirectory().toString() + File.separator + "sexspider4" + File.separator;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

}
