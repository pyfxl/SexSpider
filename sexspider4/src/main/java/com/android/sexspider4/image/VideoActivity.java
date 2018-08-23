package com.android.sexspider4.image;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sexspider4.BaseActivity;
import com.android.sexspider4.R;
import com.android.sexspider4.image.ImageActivity;
import com.android.sexspider4.image.bean.ImageBean;
import com.android.sexspider4.image.presenter.IImagePresenter;
import com.android.sexspider4.image.presenter.ImagePresenterImpl;
import com.android.sexspider4.image.view.IImageView;

import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class VideoActivity extends ImageActivity implements IImageView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected  void showView() {
        setContentView(R.layout.activity_video);
    }

    protected  void showImage(final List<ImageBean> images) {
        //标题
        textView.setText(title);
        //视频播放器
        String videoUrl = images.get(0).imageLink;
        if (!videoUrl.equals("")) {
            //initPlayer(videoUrl);
            initJZVideo(videoUrl);
        }
    }

    //JZVideo
    private void initJZVideo(String videoUrl) {
        JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
        jzVideoPlayerStandard.setUp(videoUrl,
                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                "");
        //jzVideoPlayerStandard.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

}
