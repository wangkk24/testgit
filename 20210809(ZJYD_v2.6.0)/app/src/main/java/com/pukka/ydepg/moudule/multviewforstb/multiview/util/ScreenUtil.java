package com.pukka.ydepg.moudule.multviewforstb.multiview.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.annotation.NonNull;

public class ScreenUtil {

    private static TypedValue mTmpValue = new TypedValue();

    public static int getScreenHeigt(@NonNull Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        return outMetrics.heightPixels;
    }

    public static int getScreenWidth(@NonNull Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static String getScreenInfo(@NonNull Activity activity){
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        int heightPixels = outMetrics.heightPixels;
        float density  = outMetrics.density;		// 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        int densityDPI = outMetrics.densityDpi;		// 屏幕密度（每寸像素：120/160/240/320）
        return "("+widthPixels+"x"+heightPixels+")"+" density="+density+" densityDpi="+densityDPI;
    }

    /**
     * 获取dimens的dp对应的dp值
     *
     * @param context
     * @param id
     * @return
     */
    public static int getXmlDP(Context context, int id) {
        synchronized (mTmpValue) {
            TypedValue value = mTmpValue;
            context.getResources().getValue(id, value, true);
            return (int) TypedValue.complexToFloat(value.data);
        }
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param dipValue
     * @return
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
