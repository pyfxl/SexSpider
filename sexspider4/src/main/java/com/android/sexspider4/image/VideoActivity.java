package com.android.sexspider4.image;

import android.os.Bundle;
import android.util.Log;

import com.android.sexspider4.R;
import com.android.sexspider4.image.bean.ImageBean;
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

    //视频播放器
    protected  void showImage(final List<ImageBean> images) {
        String videoUrl = images.get(0).imageLink;
        textView.setText(videoUrl);

        Log.d("TAG", videoUrl);

        if (!videoUrl.equals("")) {
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
