package com.pukka.ydepg.common.utils.uiutil;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public final class DensityUtil {

    private DensityUtil(){}

    /**
     * from dp to px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int value = Math.round(dpValue * scale);
        if (value == 0) {
            value = 1;
        }
        return value;
    }

    /**
     * from px to dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int value = Math.round(pxValue / scale);
        if (value == 0) {
            value = 1;
        }
        return value;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int getScreenWidth(Context context) {
        return Math.min(context.getResources().getDisplayMetrics().widthPixels,context.getResources().getDisplayMetrics().heightPixels);
    }

    /**
     * 获取屏幕高度
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕实际宽度
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int getRealScreenWidth(Activity activity) {
        DisplayMetrics metrics =new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        return width;
    }

    /**
     * 获取屏幕实际高度
     *
     * @return
     * @see [类、类#方法、类#成员]
     *
     * getRealMetrics - 屏幕的原始尺寸，即包含状态栏。
     * version >= 4.2.2
     *
     */
    public static int getRealScreenHeight(Activity activity) {

        DisplayMetrics metrics =new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return height;
    }
    /**
     * 获取状态栏高度
     * http://blog.csdn.net/a_running_wolf/article/details/50477965
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int getScreenWithStatusBar(Context context){
        int statusBarHeight = -1;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 获取屏幕密度
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取屏幕密度DPI
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int getScreenDensityDPI(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * 获取字体大小密度DPI
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static float getScreenScaledDensity(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }
}