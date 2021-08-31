package com.pukka.ydepg.moudule.search.utils;

import android.content.Context;

/**
 * 屏幕像素转换工具类
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.moudule.search.utils.ScreenUtil.java
 * @date: 2017-12-15 14:55
 * @version: V1.0 描述当前版本功能
 */


public class ScreenUtil {
    /**
     * 根据dimens id获取px浮点型
     * @param context
     * @param dimenId
     * @return
     */
    public static float getDimensionF(Context context,int dimenId){
        return context.getResources().getDimension(dimenId);
    }

    /**
     * 根据dimens id获取px整型，小数四舍五入
     * @param context
     * @param dimenId
     * @return
     */
    public static int getDimension(Context context,int dimenId){
        return context.getResources().getDimensionPixelSize(dimenId);
    }
}
