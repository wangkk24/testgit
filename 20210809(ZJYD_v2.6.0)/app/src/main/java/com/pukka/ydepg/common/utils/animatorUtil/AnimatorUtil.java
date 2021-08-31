package com.pukka.ydepg.common.utils.animatorUtil;

import android.animation.ValueAnimator;

import androidx.annotation.NonNull;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import java.lang.reflect.Field;

public class AnimatorUtil {

    /**
     * 重置动画缩放时长
     */
    public static void resetDurationScaleIfDisable() {
        if (getDurationScale() == 0)
            resetDurationScale();
    }

    /**
     * 重置动画缩放时长
     */
    private static void resetDurationScale() {
        try {
            getField().setFloat(null, 1);
        } catch (Exception e) {
            SuperLog.error("AnimatorUtil",e);
        }
    }


    /**
     * 获取动画缩放时长
     */
    private static float getDurationScale() {
        try {
            return getField().getFloat(null);
        } catch (Exception e) {
            SuperLog.error("AnimatorUtil",e);
            return -1;
        }
    }

    /**
     * 获取动画缩放时长
     */
    @NonNull
    private static Field getField() throws NoSuchFieldException {
        Field field = ValueAnimator.class.getDeclaredField("sDurationScale");
        field.setAccessible(true);
        return field;
    }
}
