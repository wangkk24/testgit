package com.pukka.ydepg.common.viewpage2;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.viewpager2.widget.ViewPager2;
/**
 * 控制ViewPage2切换动画的速度
 * */
public class ViewPage2Help {
    /**
     * 保存前一个animatedValue
     */
    private static int previousValue;

    /**
     * 设置当前Item
     * @param pager    viewpager2
     * @param item     下一个跳转的item
     * @param duration scroll时长
     */
    public static void setCurrentItem(final ViewPager2 pager, int item, long duration,String direction) {
        previousValue = 0;
        //int currentItem = pager.getCurrentItem();
        int pxToDrag;
        //滑动方向：0 水平，1 垂直,默认为0
        if (direction.equalsIgnoreCase("1")){
            pxToDrag = pager.getHeight();
        }else{
            pxToDrag = pager.getWidth();
        }
        //int pxToDrag = pagePxWidth * (item - currentItem);
        final ValueAnimator animator = ValueAnimator.ofInt(0, pxToDrag);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                float currentPxToDrag = (float) (currentValue - previousValue);
                pager.fakeDragBy(-currentPxToDrag);
                previousValue = currentValue;
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                pager.beginFakeDrag();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                pager.endFakeDrag();
            }

            @Override
            public void onAnimationCancel(Animator animation) { }

            @Override
            public void onAnimationRepeat(Animator animation) { }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(duration);
        animator.start();
    }

}
