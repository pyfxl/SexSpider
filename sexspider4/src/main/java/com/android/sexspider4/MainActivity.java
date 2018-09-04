package com.android.sexspider4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.android.sexspider4.helper.ImageHelper;
import com.android.sexspider4.site.SiteActivity;

/**
 * Created by feng on 2017/5/5.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //创建.nomedia文件，禁止系统图库搜索图片。
        ImageHelper.saveNoMedia();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, SiteActivity.class);
                startActivity(intent);

                MainActivity.this.finish();
            }
        }, 300);
    }
}
