package com.android.sexspider4.image;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sexspider4.BaseActivity;
import com.android.sexspider4.R;
import com.android.sexspider4.helper.ImageHelper;
import com.android.sexspider4.image.bean.ImageBean;
import com.android.sexspider4.image.presenter.IImagePresenter;
import com.android.sexspider4.image.presenter.ImagePresenterImpl;
import com.android.sexspider4.image.view.IImageView;

import java.util.ArrayList;
import java.util.List;

import ru.truba.touchgallery.GalleryWidget.BasePagerAdapter;
import ru.truba.touchgallery.GalleryWidget.FilePagerAdapter;
import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;

/**
 * Created by feng on 2017/5/5.
 */

public class ImageActivity extends BaseActivity implements IImageView {
    private IImagePresenter imagePresenter;
    private List<ImageBean> images;
    protected TextView textView;
    private ImageView favoriteView;
    private boolean favoriteFlag = false;
    private int listId;
    private int isFavorite;
    private int position = 0;
    private ImageView floatView;
    protected String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showView();

        //取传入的值
        Intent intent = super.getIntent();
        listId = intent.getIntExtra("listid", 0);
        title = intent.getStringExtra("title");
        position = intent.getIntExtra("position", 0);
        isFavorite = intent.getIntExtra("isfavorite", 0);
        favoriteFlag = (isFavorite == 1);
        setFavorite();

        //初始化
        findView();
        floatButton();
        setActionBarTitle(title);

        imagePresenter = new ImagePresenterImpl(this);
        images = imagePresenter.getImagesByListId(listId);

        if(images.size() <= 0) {
            doBackError();
        } else {
            showImage(images);
        }
    }

    protected  void showView() {
        setContentView(R.layout.activity_image);
    }

    protected  void showImage(final List<ImageBean> images) {
        //图片列表
        List<String> items = new ArrayList<>();
        for (ImageBean image : images) {
            String fileName = ImageHelper.getFile(image).getPath();
            items.add(fileName);
        }

        //第三方图片控件
        FilePagerAdapter pagerAdapter = new FilePagerAdapter(this, items);
        pagerAdapter.setOnItemChangeListener(new BasePagerAdapter.OnItemChangeListener() {
            @Override
            public void onItemChange(int currentPosition) {
                textView.setText(images.get(currentPosition).imageId + "/" + images.size());
            }
        });
        GalleryViewPager gallery = (GalleryViewPager) super.findViewById(R.id.gallery);
        gallery.setOffscreenPageLimit(3);
        gallery.setAdapter(pagerAdapter);
    }

    private void doBackError() {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        setResult(Activity.RESULT_CANCELED, intent);

        finish();
    }

    @Override
    public void doBack() {
        Intent intent = new Intent();
        intent.putExtra("isfavorite", isFavorite);
        intent.putExtra("position", position);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    @Override
    public void findView() {
        textView = (TextView) super.findViewById(R.id.textView);
        floatView = (ImageView) super.findViewById(R.id.imageView);
    }

    @Override
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
            showToast(ImageActivity.this, R.string.txt_image_favorite_cancel);
        } else {
            favoriteOn();
            isFavorite = 1;
            imagePresenter.updateFavorite(listId, 1);
            showToast(ImageActivity.this, R.string.txt_image_favorite_success);
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

}
