package com.pukka.ydepg.launcher.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.customui.tv.cache.BitmapMemoryCache;
import com.pukka.ydepg.customui.tv.utils.DrawUtils;
import com.pukka.ydepg.customui.tv.utils.TVUtils;
import com.pukka.ydepg.moudule.search.utils.ScreenUtil;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.view.RoundShadowLayout.java
 * @date: 2018-01-23 16:11
 * @version: V1.0 描述当前版本功能
 */


public class RoundShadowLayout extends RelativeLayout {

    private static final String TAG = "ReflectItemView";

    private static final int DEFUALT_REFHEIGHT = 80;
    private static final int DEFUALT_RADIUS = 12;

    private Paint mClearPaint = null;
    private Paint mShapePaint = null;
    private Paint mRefPaint = null;
    private int mRefHeight = DEFUALT_REFHEIGHT;

    private boolean mIsDrawShape = true;
    private boolean mIsReflection = false;

    private float mRadius = DEFUALT_RADIUS;
    private int mRefleSpacing = 0;
    private BitmapMemoryCache mBitmapMemoryCache = BitmapMemoryCache.getInstance();
    private static int sViewIDNum = 0;
    private int viewIDNum = 0;

    public RoundShadowLayout(Context context) {
        this(context, null, 0);
    }

    public RoundShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundShadowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setClipChildren(false);
        setClipToPadding(false);
        setWillNotDraw(false);
        //设置圆角
        setRadius(ScreenUtil.getDimensionF(context,R.dimen.my_moreItem_radius));
        // 初始化圆角矩形.
        initShapePaint();
        // 初始化倒影参数.
        initRefPaint();
    }

    private void initShapePaint() {
        mShapePaint = new Paint();
        mShapePaint.setAntiAlias(true);
        // 取两层绘制交集。显示下层。
        mShapePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    private void initRefPaint() {
        if (mRefPaint == null) {
            mRefPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // 倒影渐变.
            mRefPaint.setShader(
                    new LinearGradient(0, 0, 0, mRefHeight, new int[]{0x77000000, 0x66AAAAAA, 0x0500000, 0x00000000},
                            new float[]{0.0f, 0.1f, 0.9f, 1.0f}, Shader.TileMode.CLAMP));
            mRefPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        }
        if (mClearPaint == null) {
            mClearPaint = new Paint();
            mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
    }

    /**
     * 设置倒影的shader属性.
     */
    public void setReflectionShader(Shader shader) {
        if (mRefPaint != null) {
            mRefPaint.setShader(shader);
            invalidate();
        }
    }
    /**
     * 倒影与原图的隔离距离
     *
     */
    public void setReflectionSpacing(int spacing) {
        mRefleSpacing = spacing;
        invalidate();
    }

    /**
     * 设置是否倒影.
     */
    public void setReflection(boolean ref) {
        mIsReflection = ref;
        invalidate();
    }

    public boolean isReflection() {
        return this.mIsReflection;
    }

    /**
     * 设置绘制奇形怪状的形状.
     */
    public void setDrawShape(boolean isDrawShape) {
        mIsDrawShape = isDrawShape;
        invalidate();
    }

    public boolean isDrawShape() {
        return this.mIsDrawShape;
    }

    /**
     * 倒影高度.
     */
    public void setRefHeight(int height) {
        this.mRefHeight = height;
        invalidate();
    }

    public int getRefHeight() {
        return this.mRefHeight;
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    /**
     * 获取缓存ID.
     */
    private int getViewCacheID() {
        if (viewIDNum == 0) {
            sViewIDNum++;
            viewIDNum = sViewIDNum;
        }
        return viewIDNum;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (viewIDNum != 0) {
            mBitmapMemoryCache.removeImageCache(viewIDNum + "");
        }
    }

    public Path getShapePath(int width, int height, float radius) {
        return DrawUtils.addRoundPath3(getWidth(), getHeight(), radius);
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            if (canvas != null) {
                if (mIsDrawShape && (mRadius > 0)) {
                    //绘制圆角
                    drawShapePathCanvas(canvas);
                } else {
                    super.draw(canvas);
                }
                /**
                 * 绘制倒影. 4.3 SDK-18,有问题，<br>
                 * 在使用Canvas.translate(dx, dy)会出现BUG. <br>
                 */
                if (TVUtils.getSDKVersion() == 18) {
                    drawRefleCanvas4_3_18(canvas);
                } else if (TVUtils.getSDKVersion() == 17) {
                    // 4.2 不需要倒影，绘制有问题，暂时屏蔽.
                    drawRefleCanvas(canvas);
                } else { // 性能高速-倒影(4.3有问题).
                    drawRefleCanvas(canvas);
                }
            }
        } catch (Exception e) {
            SuperLog.error(TAG,e);
        }
    }

    /**
     * 绘制圆角控件. 修复使用clipPath有锯齿问题.
     */
    private void drawShapePathCanvas(Canvas shapeCanvas) {
        if (shapeCanvas != null) {
            int width = getWidth();
            int height = getHeight();
            if (width == 0 || height == 0)
                return;
            int count = shapeCanvas.save();
            int count2 = shapeCanvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
            //轨迹
            Path path = getShapePath(width, height, mRadius);
            super.draw(shapeCanvas);
            //绘制
            shapeCanvas.drawPath(path, mShapePaint);
            //
            if (count2 > 0) {
                shapeCanvas.restoreToCount(count2);
            }
            shapeCanvas.restoreToCount(count);
        }
    }

    /**
     * 绘制倒影. 修复原先使用bitmap卡顿的问题.
     */
    private void drawRefleCanvas(Canvas refleCanvas) {
        if (mIsReflection) {
            refleCanvas.save();
            int dy = getHeight();
            int dx = 0;
            refleCanvas.translate(dx, dy + mRefleSpacing);
            drawReflection(refleCanvas);
            refleCanvas.restore();
        }
    }

    private void drawRefleCanvas4_3_18(Canvas canvas) {
        if (mIsReflection) {
            // 创建一个画布.
            String cacheID = getViewCacheID() + "";
            //
            Bitmap reflectBitmap = mBitmapMemoryCache.getBitmapFromMemCache(cacheID);
            if (reflectBitmap == null) {
                reflectBitmap = Bitmap.createBitmap(getWidth(), mRefHeight, Bitmap.Config.ARGB_8888);
                mBitmapMemoryCache.addBitmapToMemoryCache(cacheID, reflectBitmap);
            }
            Canvas reflectCanvas = new Canvas(reflectBitmap);
            reflectCanvas.drawPaint(mClearPaint); // 清空画布.
            /**
             * 如果设置了圆角，倒影也需要圆角.
             */
            int width = reflectCanvas.getWidth();
            int height = reflectCanvas.getHeight();
            RectF outerRect = new RectF(0, 0, width, height);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            if (mIsDrawShape) {
                reflectCanvas.drawPath(getShapePath(width, height + 50, mRadius), paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            }
            reflectCanvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
            drawReflection4_3_18(reflectCanvas);
            reflectCanvas.restore();
            canvas.save();
            int dy = getHeight();
            int dx = 0;
            canvas.translate(dx, dy + mRefleSpacing);
            canvas.drawBitmap(reflectBitmap, 0, 0, null);
            canvas.restore();
            reflectBitmap.recycle();
        }
    }

    public void drawReflection4_3_18(Canvas canvas) {
        canvas.save();
        canvas.clipRect(0, 0, getWidth(), mRefHeight);
        canvas.save();
        canvas.scale(1, -1);
        canvas.translate(0, -getHeight());
        super.draw(canvas);
        canvas.restore();
        canvas.drawRect(0, 0, getWidth(), mRefHeight, mRefPaint);
        canvas.restore();
    }

    /**
     * 绘制倒影.
     */
    public void drawReflection(Canvas reflectionCanvas) {
        int width = getWidth();
        int height = getHeight();
        int count = reflectionCanvas.save();
        int count2 = reflectionCanvas.saveLayer(0, 0, width, mRefHeight, null, Canvas.ALL_SAVE_FLAG);
        //
        reflectionCanvas.save();
        reflectionCanvas.clipRect(0, 0, getWidth(), mRefHeight);
        reflectionCanvas.save();
        reflectionCanvas.scale(1, -1);
        reflectionCanvas.translate(0, -getHeight());
        super.draw(reflectionCanvas);
        if (mIsDrawShape) {
            Path path = getShapePath(width, height, mRadius);
            reflectionCanvas.drawPath(path, mShapePaint);
        }
        reflectionCanvas.restore();
        reflectionCanvas.drawRect(0, 0, getWidth(), mRefHeight, mRefPaint);
        reflectionCanvas.restore();
        //
        if (count2 > 0) {
            reflectionCanvas.restoreToCount(count2);
        }
        reflectionCanvas.restoreToCount(count);
    }

	/*
     * 设置/获取-圆角的角度.
	 */

    public void setRadius(float radius) {
        this.mRadius = radius;
        invalidate();
    }

}
