package com.pukka.ydepg.customui.tv.autoscroll;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.ViewPager;

import com.pukka.ydepg.R;

import static android.view.Gravity.CENTER;

public class AutoScrollViewPager extends RelativeLayout {
    private AutoViewPager mViewPager;

    protected Context mContext;
    private Drawable selected;
    private Drawable unSelected;
    private float marginLeft;
    private float marginRight;
    private float marginBottom;
    protected LinearLayout layout;

    private AutoScrollViewPagerAdapter mAdapter;

    public AutoScrollViewPager(Context context) {
        super(context);
        init(context);
    }

    public AutoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoScrollViewPager);
        selected = typedArray.getDrawable(R.styleable.AutoScrollViewPager_dot_selected);
        unSelected = typedArray.getDrawable(R.styleable.AutoScrollViewPager_dot_unselect);
        marginLeft = typedArray.getDimension(R.styleable.AutoScrollViewPager_dot_marginleft, 12);
        marginRight = typedArray.getDimension(R.styleable.AutoScrollViewPager_dots_marginRight, 12);
        marginBottom = typedArray.getDimension(R.styleable.AutoScrollViewPager_dots_marginBottom, 12);
        typedArray.recycle();
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void init(Context context) {
        mContext = context;
        // 避免多次初始化
        if (null != mViewPager) {
            return;
        }
        mViewPager = new AutoViewPager(context);
        mViewPager.setFocusable(false);
        addView(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setTime(Long time) {
        getViewPager().setInternalTime(time);
    }

    public void setAdapter(AutoScrollViewPagerAdapter adapter) {
        if (mViewPager != null) {
            mAdapter = adapter;
            mViewPager.init(mViewPager, adapter);
        }
    }

    public AutoViewPager getViewPager() {
        return mViewPager;
    }

    public void initPointView(int size) {
        if (layout != null) {
            removeView(layout);
        }
        layout = new LinearLayout(mContext);
        //只有一个资源时隐藏指示器
        if (size == 1) {
            layout.setVisibility(GONE);
        }
        layout.setFocusable(false);
        for (int i = 0; i < size; i++) {
            //ShimmerImageView shimmerImageView = new ShimmerImageView(mContext, mAttrs);
            ImageView imageView = new ImageView(mContext);
            //ImageView imageView = shimmerImageView.getImageViewExt();
            imageView.setFocusable(false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 12;
            params.gravity = CENTER;
            imageView.setLayoutParams(params);
            if (i == 0) {
                imageView.setImageDrawable(selected);
            } else {
                imageView.setImageDrawable(unSelected);
            }
            layout.addView(imageView);
        }

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(ALIGN_PARENT_RIGHT);
        layoutParams.setMargins(12, 20, 0, (int) marginBottom);
        layout.setLayoutParams(layoutParams);
        layout.setPadding(0, 0, (int) marginRight, 0);
        layout.setClipChildren(false);
        layout.setClipToPadding(false);
        addView(layout);
    }

    public void updatePointView(int position) {
        int size = layout.getChildCount();
        for (int i = 0; i < size; i++) {
            ImageView imageView = (ImageView) layout.getChildAt(i);
            imageView.setImageDrawable(unSelected);
        }
        ImageView focus = (ImageView) layout.getChildAt(position);
        if (null != focus)
        focus.setImageDrawable(selected);
    }


    public void onDestroy() {
        if (mViewPager != null) {
            mViewPager.onStop();
        }
    }

    public void onResume() {
        if (null != mViewPager) {
            mViewPager.onResume();
        }
    }
}
