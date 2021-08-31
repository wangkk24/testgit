package com.pukka.ydepg.moudule.home.util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * ----Created By----
 * User: Fang Liang.
 * Date: 2019/1/2.
 * ------------------
 */

public class ScreenShotView {
    public static Drawable getScreenShot(View view, int width, int height) {
        if (view.getWidth() <= 0 || view.getHeight() <= 0) {
            view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap map = view.getDrawingCache();
        return new BitmapDrawable(map);
    }
}