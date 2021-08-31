package com.pukka.ydepg.moudule.multviewforstb.multiview.module.view.focus;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Region;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IntDef;
import androidx.core.view.ViewCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class ColorFocusBorder extends AbsFocusBorder {
    private Paint mShadowPaint;
    private Paint mBorderPaint;
    private int mShadowColor;
    private float mShadowWidth;
    private int mBorderColor;
    private float mBorderWidth;
    private float mRoundRadius = 0;

    private ObjectAnimator mRoundRadiusAnimator;
    private View mBorderView;

    private ColorFocusBorder(Context context, Builder builder) {
        super(context, builder);

        this.mShadowColor = builder.mShadowColor;
        this.mShadowWidth = builder.mShadowWidth;
        this.mBorderColor = builder.mBorderColor;
        this.mBorderWidth = builder.mBorderWidth;

        final float padding = mShadowWidth + mBorderWidth;
        mPaddingRectF.set(padding, padding, padding, padding);
        initPaint();

        mBorderView = new BorderView(context);
        //关闭硬件加速
        mBorderView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mBorderView, params);
    }

    private void initPaint() {
        mShadowPaint = new Paint();
        mShadowPaint.setColor(mShadowColor);
//        mShadowPaint.setAntiAlias(true); //抗锯齿功能，会消耗较大资源，绘制图形速度会变慢
//        mShadowPaint.setDither(true); //抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        mShadowPaint.setMaskFilter(new BlurMaskFilter(mShadowWidth, BlurMaskFilter.Blur.OUTER));

        mBorderPaint = new Paint();
//        mBorderPaint.setAntiAlias(true);
//        mBorderPaint.setDither(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setMaskFilter(new BlurMaskFilter(0.5f, BlurMaskFilter.Blur.NORMAL));
    }

    @Override
    public View getBorderView() {
        return mBorderView;
    }

    protected void setRoundRadius(float roundRadius) {
        if (mRoundRadius != roundRadius) {
            mRoundRadius = roundRadius;
            ViewCompat.postInvalidateOnAnimation(mBorderView);
        }
    }

    @Override
    public float getRoundRadius() {
        return mRoundRadius;
    }

    @Override
    List<Animator> getTogetherAnimators(float newX, float newY, int newWidth, int newHeight, AbsFocusBorder.Options options) {
        if (options instanceof Options) {
            final Options rawOptions = (Options) options;
            List<Animator> animators = new ArrayList<>();
            animators.add(getRoundRadiusAnimator(rawOptions.roundRadius));
            return animators;
        } else if (mRoundRadius != 0) {
            List<Animator> animators = new ArrayList<>();
            animators.add(getRoundRadiusAnimator(0));
            return animators;
        }
        return null;
    }

    @Override
    List<Animator> getSequentiallyAnimators(float newX, float newY, int newWidth, int newHeight, AbsFocusBorder.Options options) {
        return null;
    }

    private ObjectAnimator getRoundRadiusAnimator(float roundRadius) {
        if (null == mRoundRadiusAnimator) {
            mRoundRadiusAnimator = ObjectAnimator.ofFloat(this, "roundRadius", getRoundRadius(), roundRadius);
        } else {
            mRoundRadiusAnimator.setFloatValues(getRoundRadius(), roundRadius);
        }
        return mRoundRadiusAnimator;
    }

    /**
     * 绘制外发光阴影
     *
     * @param canvas
     */
    private void onDrawShadow(Canvas canvas) {
        if (mShadowWidth > 0) {
            canvas.save();
            //裁剪处理(使阴影矩形框内变为透明)
            //android api 19 如条件不成立时中间会绘制成不透明，所以暂时注释掉
//            if (mRoundRadius > 0) {
            canvas.clipRect(0, 0, getWidth(), getHeight());
            mTempRectF.set(mFrameRectF);
            mTempRectF.inset(mRoundRadius / 2f, mRoundRadius / 2f);
            canvas.clipRect(mTempRectF, Region.Op.DIFFERENCE);
//            }
            //绘制外发光阴影效果
            canvas.drawRoundRect(mFrameRectF, mRoundRadius, mRoundRadius, mShadowPaint);
            canvas.restore();
        }
    }

    /**
     * 绘制边框
     *
     * @param canvas
     */
    private void onDrawBorder(Canvas canvas) {
        if (mBorderWidth > 0) {
            canvas.save();
            mTempRectF.set(mFrameRectF);
            canvas.drawRoundRect(mTempRectF, mRoundRadius, mRoundRadius, mBorderPaint);
            canvas.restore();
        }
    }

    class BorderView extends View {

        public BorderView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            onDrawShadow(canvas);
            onDrawBorder(canvas);
        }
    }

    public static class Options extends AbsFocusBorder.Options {
        private float roundRadius = 0f;

        Options() {
            super();
        }

        private static class OptionsHolder {
            private static final Options INSTANCE = new Options();
        }

        public static Options get(float scaleX, float scaleY, float roundRadius) {
            return get(scaleX, scaleY, roundRadius, null);
        }

        public static Options get(float scaleX, float scaleY, float roundRadius, String title) {
            OptionsHolder.INSTANCE.scaleX = scaleX;
            OptionsHolder.INSTANCE.scaleY = scaleY;
            OptionsHolder.INSTANCE.roundRadius = roundRadius;
            OptionsHolder.INSTANCE.title = title;
            return OptionsHolder.INSTANCE;
        }
    }

    @IntDef({TypedValue.COMPLEX_UNIT_PX, TypedValue.COMPLEX_UNIT_DIP, TypedValue.COMPLEX_UNIT_SP,
            TypedValue.COMPLEX_UNIT_PT, TypedValue.COMPLEX_UNIT_IN, TypedValue.COMPLEX_UNIT_MM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Unit {
    }

    public final static class Builder extends AbsFocusBorder.Builder {
        private int mShadowColor = 0;
        private int mShadowColorRes = 0;
        private float mShadowWidth = 0f;
        private int mShadowWidthUnit = -1;
        private float mShadowWidthSrc = 0f;
        private int mBorderColor = 0;
        private int mBorderColorRes = 0;
        private float mBorderWidth = 0f;
        private int mBorderWidthUnit = -1;
        private float mBorderWidthSrc = 0f;

        public Builder shadowColor(@ColorInt int color) {
            mShadowColor = color;
            return this;
        }

        public Builder shadowColorRes(@ColorRes int colorRes) {
            mShadowColorRes = colorRes;
            return this;
        }

        public Builder shadowWidth(float pxWidth) {
            mShadowWidth = pxWidth;
            return this;
        }

        public Builder shadowWidth(@Unit int unit, float width) {
            mShadowWidthUnit = unit;
            mShadowWidthSrc = width;
            return this;
        }

        public Builder borderColor(@ColorInt int color) {
            mBorderColor = color;
            return this;
        }

        public Builder borderColorRes(@ColorRes int colorRes) {
            mBorderColorRes = colorRes;
            return this;
        }

        public Builder borderWidth(float pxWidth) {
            mBorderWidth = pxWidth;
            return this;
        }

        public Builder borderWidth(@Unit int unit, float width) {
            mBorderWidthUnit = unit;
            mBorderWidthSrc = width;
            return this;
        }

        @Override
        public FocusBorder build(Activity activity) {
            if (null == activity) {
                throw new NullPointerException("The activity cannot be null");
            }
            ViewGroup parent = activity.findViewById(android.R.id.content);
            return build(parent);
        }

        @Override
        public FocusBorder build(ViewGroup parent) {
            if (null == parent) {
                throw new NullPointerException("The FocusBorder parent cannot be null");
            }

            if (mBorderWidthUnit >= 0 && mBorderWidthSrc > 0) {
                mBorderWidth = TypedValue.applyDimension(
                        mBorderWidthUnit, mBorderWidthSrc, parent.getResources().getDisplayMetrics());
            }
            if (mShadowWidthUnit >= 0 && mShadowWidthSrc > 0) {
                mShadowWidth = TypedValue.applyDimension(
                        mShadowWidthUnit, mShadowWidthSrc, parent.getResources().getDisplayMetrics());
            }
            if (mBorderColorRes > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mBorderColor = parent.getResources().getColor(mBorderColorRes, parent.getContext().getTheme());
                } else {
                    mBorderColor = parent.getResources().getColor(mBorderColorRes);
                }
            }
            if (mShadowColorRes > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mShadowColor = parent.getResources().getColor(mShadowColorRes, parent.getContext().getTheme());
                } else {
                    mShadowColor = parent.getResources().getColor(mShadowColorRes);
                }
            }

            final ColorFocusBorder borderView = new ColorFocusBorder(parent.getContext().getApplicationContext(), this);
            final ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(1, 1);
            parent.addView(borderView, lp);
            return borderView;
        }
    }

}
