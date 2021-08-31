package com.pukka.ydepg.customui.tv.bridge;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.customui.tv.widget.MainUpView;


public class BaseEffectBridgeWrapper extends BaseEffectBridge {
    private static final String TAG = BaseEffectBridgeWrapper.class.getSimpleName();
    //缩放
    private static final int DEFAULT_TRAN_DUR_ANIM = 100;
    private MainUpView mMainUpView;
    //需要绘制的阴影图片
    private Drawable mDrawableShadow;
    //需要绘制的图片，来源于美工
    private Drawable mDrawableUpRect;
    private Context mContext;
    private RectF mUpPaddingRect = new RectF(0, 0, 0, 0);
    private RectF mShadowPaddingRect = new RectF(0, 0, 0, 0);

    /**
     * 继承这个类重写的话.<br>
     * 记得要加 super.onInitBridge(view);
     */
    @Override
    public void onInitBridge(MainUpView view) {
        mContext = view.getContext();
    }

    /**
     * ==========设置阴影图片===========
     */

    @Override
    public void setShadowResource(int resId) {
        try {
            this.mDrawableShadow = mContext.getResources().getDrawable(resId); // 移动的边框.
        } catch (Exception e) {
            this.mDrawableShadow = null;
            SuperLog.error(TAG,e);
        }
    }

    /**
     * 当图片边框不自带阴影的话，可以自行设置阴影图片. 设置阴影.
     */
    @Override
    public void setShadowDrawable(Drawable shadowDrawable) {
        this.mDrawableShadow = shadowDrawable;
    }

    @Override
    public Drawable getShadowDrawable() {
        return this.mDrawableShadow;
    }

    /**
     * 根据阴影图片边框 自行 填写 相差的边距. <br>
     * 比如 res/drawble/white_shadow.9.png的图片，边距就差很多.
     */
    @Override
    public void setDrawShadowRectPadding(Rect rect) {
        mShadowPaddingRect.set(rect);
    }

    @Override
    public void setDrawShadowRectPadding(RectF rectf) {
        mShadowPaddingRect.set(rectf);
    }

    @Override
    public RectF getDrawShadowRect() {
        return this.mShadowPaddingRect;
    }

    /**
     * 设置边框图片
     */
    @Override
    public void setUpRectResource(int resId) {
        try {
            this.mDrawableUpRect = mContext.getResources().getDrawable(resId); // 移动的边框.
        } catch (Exception e) {
            SuperLog.error(TAG,e);
        }
    }

    /**
     * 设置边框图片
     */
    @Override
    public void setUpRectDrawable(Drawable upRectDrawable) {
        this.mDrawableUpRect = upRectDrawable;
    }

    @Override
    public Drawable getUpRectDrawable() {
        return this.mDrawableUpRect;
    }

    /**
     * 根据图片边框 自行 填写 相差的边距. <br>
     * 比如 res/drawble/white_light_10.9.png的图片，边距就差很多.
     */
    @Override
    public void setDrawUpRectPadding(Rect rect) {
        mUpPaddingRect.set(rect);
    }

    /**
     * 设置外边距
     * 根据图片边框 自行 填写 相差的边距. <br>
     * **/
    @Override
    public void setDrawUpRectPadding(RectF rectf) {
        mUpPaddingRect.set(rectf);
    }

    @Override
    public RectF getDrawUpRect() {
        return this.mUpPaddingRect;
    }

    /**
     * 老的焦点View.
     */
    @Override
    public void onOldFocusView(View oldFocusView, float scaleX, float scaleY) {
        if (oldFocusView != null) {
            /**对失去焦点的view，进行缩放处理*/
            oldFocusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(DEFAULT_TRAN_DUR_ANIM).start();
        }
    }

    /**
     * 新的焦点View.
     */
    @Override
    public void onFocusView(View focusView, View oldView, float scaleX, float scaleY) {
        if (focusView != null) {
            focusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(DEFAULT_TRAN_DUR_ANIM).start(); // 放大焦点VIEW的动画.
            runTranslateAnimation(focusView, oldView, scaleX, scaleY); // 移动边框的动画。
        }
    }

    /**
     * ==========绘制处理===========
     */

    /**
     * 需要绘制的东西.
     */
    @Override
    public boolean onDrawMainUpView(Canvas canvas) {
        canvas.save();
        // 绘制阴影.
        onDrawShadow(canvas);
        // 绘制图片
        onDrawUpRect(canvas);
        canvas.restore();
        return true;
    }

    /**
     * 绘制阴影.图片
     */
    public void onDrawShadow(Canvas canvas) {
        Drawable drawableShadow = getShadowDrawable();
        if (drawableShadow != null) {
            RectF shadowPaddingRect = getDrawShadowRect();
            int width = getMainUpView().getWidth();
            int height = getMainUpView().getHeight();
            Rect padding = new Rect();
            drawableShadow.getPadding(padding);
            //
            int left = (int)Math.rint(shadowPaddingRect.left);
            int right = (int)Math.rint(shadowPaddingRect.right);
            int bottom = (int)Math.rint(shadowPaddingRect.bottom);
            int top = (int)Math.rint(shadowPaddingRect.top);
            //
            drawableShadow.setBounds(-padding.left - (left), -padding.top - (top),
                    width + padding.right + (right),
                    height + padding.bottom + (bottom));
            drawableShadow.draw(canvas);
        }
    }

    /**
     * 绘制图片
     */
    public void onDrawUpRect(Canvas canvas) {
        Drawable drawableUp = getUpRectDrawable();
        if (drawableUp != null) {
            RectF paddingRect = getDrawUpRect();
            int width = getMainUpView().getWidth();
            int height = getMainUpView().getHeight();
            Rect padding = new Rect();
            // 边框的绘制.
            drawableUp.getPadding(padding);
            //
            int left = (int)Math.rint(paddingRect.left);
            int right = (int)Math.rint(paddingRect.right);
            int bottom = (int)Math.rint(paddingRect.bottom);
            int top = (int)Math.rint(paddingRect.top);
            //drawable将在被绘制在canvas的哪个矩形区域内
            drawableUp.setBounds(-padding.left - (left), -padding.top - (top),
                    width + padding.right + (right), height + padding.bottom + (bottom));
            drawableUp.draw(canvas);
        }
    }
    /**
     * 边框移动
     * */
    public void runTranslateAnimation(View toView, View oldView, float scaleX, float scaleY) {
        try {
            //将mainUpView控件移动到toView控件上面  移动外面的白框
            flyWhiteBorder(toView, oldView, getMainUpView(), scaleX, scaleY);
        } catch (Exception e) {
            SuperLog.error(TAG,e);
        }
    }

    public Rect findLocationWithView(View view) {
        ViewGroup root = (ViewGroup) getMainUpView().getParent();
        Rect rect = new Rect();
        root.offsetDescendantRectToMyCoords(view, rect);
        return rect;
    }

    public void flyWhiteBorder(final View focusView, View oldView, View moveView, float scaleX, float scaleY) {
        int newWidth = 0;
        int newHeight = 0;
        int oldWidth = 0;
        int oldHeight = 0;

        int newX = 0;
        int newY = 0;

        if (focusView != null) {
            // 有一点偏差,需要进行四舍五入.
            newWidth = (int) (Math.rint(focusView.getMeasuredWidth() * scaleX));
            newHeight = (int) (Math.rint(focusView.getMeasuredHeight() * scaleY));
            oldWidth = moveView.getMeasuredWidth();
            oldHeight = moveView.getMeasuredHeight();
            Rect fromRect = findLocationWithView(moveView);
            Rect toRect = findLocationWithView(focusView);
            int x = toRect.left - fromRect.left;
            int y = toRect.top - fromRect.top;
            newX = x - Math.abs(focusView.getMeasuredWidth() - newWidth) / 2;
            newY = y - Math.abs(focusView.getMeasuredHeight() - newHeight) / 2;
            Log.d("mCurrentFocus","toRect" + "left:" + toRect.left + "right:" + toRect.right);
            Log.d("mCurrentFocus","fromRect" + "left:" + fromRect.left + "right:" + fromRect.right);
            Log.d("mCurrentFocus","xy" + "x:" + newX + "y:" + newY);
        }
        ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(moveView, "translationX", newX);
        ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(moveView, "translationY", newY);
        // BUG，因为缩放会造成图片失真(拉伸).
        // hailong.qiu 2016.02.26 修复 :)
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofInt(new ScaleView(moveView), "width", oldWidth,
                (int) newWidth);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofInt(new ScaleView(moveView), "height", oldHeight,
                (int) newHeight);
        //
        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator);
        mAnimatorSet.setInterpolator(new DecelerateInterpolator(1));
        mAnimatorSet.setDuration(DEFAULT_TRAN_DUR_ANIM);
        mAnimatorSet.start();
    }

    /**
     * 用於放大的view
     */
    public class ScaleView {
        private View view;

        public ScaleView(View view) {
            this.view = view;
        }

        public int getWidth() {
            return view.getLayoutParams().width;
        }

        public void setWidth(int width) {
            view.getLayoutParams().width = width;
            view.requestLayout();
        }

        public int getHeight() {
            return view.getLayoutParams().height;
        }

        public void setHeight(int height) {
            view.getLayoutParams().height = height;
            view.requestLayout();
        }
    }

    /**
     * 最上层的移动的view.
     */
    @Override
    public void setMainUpView(MainUpView view) {
        this.mMainUpView = view;
    }

    /**
     * 最上层移动的view.
     */
    @Override
    public MainUpView getMainUpView() {
        return this.mMainUpView;
    }

}
