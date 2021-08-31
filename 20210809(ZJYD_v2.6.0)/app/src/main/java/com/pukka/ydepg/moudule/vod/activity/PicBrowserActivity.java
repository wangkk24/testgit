package com.pukka.ydepg.moudule.vod.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.pukka.ydepg.R;
import com.pukka.ydepg.moudule.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 *
 *片花
 * @author: ld
 * @date: 2017-12-19
 */

public class PicBrowserActivity extends BaseActivity {
    @BindView(R.id.img_pic)
    ImageView mPicImg;
    @BindView(R.id.has_next)
    RelativeLayout mHasNextLayout;
    private List<String> mPics;
    private int currentItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPics = getIntent().getStringArrayListExtra("pics");
        currentItem = getIntent().getIntExtra("current_item", 0);
        setContentView(R.layout.activity_pic_browser);
        setmUnBinder(ButterKnife.bind(this));
        if (mPics.size() <= 1) {
            mHasNextLayout.setVisibility(View.GONE);
        }
        Glide.with(this).load(mPics.get(currentItem)).into(mPicImg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean onKeyDown = false;

        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (mPics != null) {
                if (currentItem > 0) {
                    currentItem--;
                } else if (currentItem == 0) {
                    currentItem = mPics.size() - 1;
                }
                Glide.with(this).load(mPics.get(currentItem)).into(mPicImg);

            }
            onKeyDown = true;
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (mPics != null) {
                if (currentItem < mPics.size() - 1) {
                    currentItem++;
                } else if (currentItem == mPics.size() - 1) {
                    currentItem = 0;
                }
                Glide.with(this).load(mPics.get(currentItem)).into(mPicImg);

            }
            onKeyDown = true;
        }

        if (!onKeyDown) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }
}
