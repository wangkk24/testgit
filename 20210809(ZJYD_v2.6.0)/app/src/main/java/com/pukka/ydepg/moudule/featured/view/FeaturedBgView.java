package com.pukka.ydepg.moudule.featured.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by hasee on 2016/11/25.
 */

public class FeaturedBgView extends LinearLayout {
    int mStartColor = 0x00FFFFFF;
    int mMiddleColor ;
    int mEndColor = 0x99FFFFFF;
    public FeaturedBgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FeaturedBgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public FeaturedBgView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
    }

    public void setEndColor(int endColor){
//        mEndColor = mStartColor + 2*(endColor -mStartColor)/3;
//        int colors[] = { mStartColor ,alphaEvaluator(0.99f,endColor), endColor };//分别为开始颜色，中间夜色，结束颜色
//        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        setBackgroundColor(endColor);
    }
    public void setEndColor2(int endColor){
        mEndColor = mStartColor + 2*(endColor -mStartColor)/3;
        int colors[] = { mStartColor, endColor };//分别为开始颜色，中间夜色，结束颜色
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        setBackground(gd);
    }
    public void setColor(int endColor){
        mMiddleColor = mStartColor + (endColor - mStartColor)/2;
        int colors[] = { mMiddleColor , endColor };//分别为开始颜色，中间夜色，结束颜色
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        setBackground(gd);
    }
    /**
     * 颜色透明度修改
     *
     * @param fraction
     * @param color
     * @return
     */
    public int alphaEvaluator(float fraction, int color) {
        int r = (color >> 16) & 0xff;
        int g = (color >> 8) & 0xff;
        int b = color & 0xff;
        return ((int) (256 * fraction) << 24) | r << 16 | g << 8 | b;
    }


}
