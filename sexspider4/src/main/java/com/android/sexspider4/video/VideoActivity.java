package com.android.sexspider4.video;

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

import com.android.sexspider4.R;
import com.android.sexspider4.image.bean.ImageBean;
import com.android.sexspider4.image.presenter.IImagePresenter;
import com.android.sexspider4.image.presenter.ImagePresenterImpl;
import com.android.sexspider4.image.view.IImageView;

import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class VideoActivity extends AppCompatActivity implements IImageView {
    private IImagePresenter imagePresenter;
    private List<ImageBean> images;
    private TextView textView;
    private ImageView favoriteView;
    private boolean favoriteFlag = false;
    private int listId;
    private int isFavorite;
    private int position = 0;
    private ImageView floatView;

    private String url1 = "http://img.ksbbs.com/asset/Mon_1703/d30e02a5626c066.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        //取传入的值
        Intent intent = super.getIntent();
        listId = intent.getIntExtra("listid", 0);
        String title = intent.getStringExtra("title");
        position = intent.getIntExtra("position", 0);
        isFavorite = intent.getIntExtra("isfavorite", 0);
        favoriteFlag = (isFavorite == 1);
        setFavorite();

        //初始化
        findView();
        floatButton();
        //setActionBarTitle(title);

        TextView textView = (TextView) super.findViewById(R.id.textView);
        textView.setText(title);

        imagePresenter = new ImagePresenterImpl(this);
        images = imagePresenter.getImagesByListId(listId);

        if(images.size() <= 0) {
            doBackError();
        } else {
            //视频播放器
            String videoUrl = images.get(0).imageLink;
            if (!videoUrl.equals("")) {
                //initPlayer(videoUrl);
                initJZVideo(videoUrl);
            }
        }
    }

    private void doBackError() {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        setResult(Activity.RESULT_CANCELED, intent);

        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void doBack() {
        Intent intent = new Intent();
        intent.putExtra("isfavorite", isFavorite);
        intent.putExtra("position", position);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    public void findView() {
        textView = (TextView) super.findViewById(R.id.textView);
        floatView = (ImageView) super.findViewById(R.id.imageView);
    }

    public void floatButton() {
        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFavorite();
            }
        });
    }

    @Override
    public void setLists(List<ImageBean> images) {
        this.images = images;
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

    //初始喜爱按钮
    private void setFavorite() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (favoriteFlag) {
                    favoriteOn();
                } else {
                    favoriteOff();
                }
            }
        }, 200);
    }

    //喜爱按钮
    private void doFavorite() {
        if (favoriteFlag) {
            favoriteOff();
            isFavorite = 0;
            imagePresenter.updateFavorite(listId, 0);
            Toast.makeText(VideoActivity.this, R.string.txt_image_favorite_cancel, Toast.LENGTH_SHORT).show();
        } else {
            favoriteOn();
            isFavorite = 1;
            imagePresenter.updateFavorite(listId, 1);
            Toast.makeText(VideoActivity.this, R.string.txt_image_favorite_success, Toast.LENGTH_SHORT).show();
        }
    }

    //查看按钮开
    private void favoriteOn() {
        favoriteFlag = true;
        if (favoriteView != null) favoriteView.setImageResource(R.drawable.ic_action_favorite_on);
        if (floatView != null) floatView.setImageResource(R.drawable.ic_action_favorite_on);
    }

    //查看按钮关
    private void favoriteOff() {
        favoriteFlag = false;
        if (favoriteView != null) favoriteView.setImageResource(R.drawable.ic_action_favorite_off);
        if (floatView != null) floatView.setImageResource(R.drawable.ic_action_favorite_off);
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
