package com.pukka.ydepg.moudule.multviewforstb.multiview.module.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.R;

import java.text.DecimalFormat;

/**
 * 鸟瞰图
 */
public class AerialView extends View {

    private static final String TAG = "AerialView";

    /**
     * 当前View的宽度
     */
    private int viewWidth;
    /**
     * 当前View的高度
     */
    private int viewHeight;

    /**
     * 当前activity的宽度
     */
    private int phoneWidth;
    /**
     * 当前activity的高度
     */
    private int phoneHeight;

    /**
     * 当前缩放画面的宽度
     */
    private int currentFrameWidth;
    /**
     * 当前缩放画面的高度
     */
    private int currentFrameHeight;

    /**
     * 绘制线宽度
     */
    private final float STROKE_WIDTH = 4f;

    /**
     * 上边距
     */
    private int marginTop;

    /**
     * 左边距
     */
    private int marginLeft;

    /**
     * 矩形4个角位置
     */
    private float leftX;
    private float rightX;
    private float topY;
    private float bottomY;

    /**
     * 是否绘制背景
     */
    private boolean drawBg = true;

    /**
     * 鸟瞰框颜色
     *
     * @ColorInt
     */
    private int aerialColorInt = Color.BLUE;

    /**
     * 鸟瞰框背景颜色
     *
     * @ColorInt
     */
    private int aerialBGColorInt = Color.WHITE;

    /**
     * 鸟瞰框字体颜色
     *
     * @ColorInt
     */
    private int aerialTextColorInt = Color.WHITE;

    /**
     * 背景颜色
     *
     * @ColorInt
     */
    private int bgColorInt = Color.GRAY;

    /**
     * 创建一个画笔
     */
    private Paint mPaint;

    /**
     * 鸟瞰图框区域
     */
    private RectF aerialStrokeRect;

    /**
     * 要显示的图片区域
     */
    private Rect bitmapSrcRect;

    /**
     * 显示图片的区域
     */
    private Rect bitmapDstRect;

    /**
     * 绘制文字
     */
    private String drawText;

    /**
     * 格式化小数
     */
    DecimalFormat fnum = new DecimalFormat("##0.00");

    public AerialView(Context context) {
        super(context);
        initPaint();
    }

    public AerialView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initAttr(context, attrs);
    }

    public AerialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initAttr(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        DebugLog.debug(TAG, "[onMeasure] w h " + viewWidth + " " + viewHeight);
        if (viewWidth > 0 && viewHeight > 0) {
            doUpdate();
        }
    }

    /**
     * 加载配置的颜色
     *
     * @param context
     * @param attrs
     */
    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AerialView);
        bgColorInt = ta.getColor(R.styleable.AerialView_bgColor, bgColorInt);
        aerialColorInt = ta.getColor(R.styleable.AerialView_aerialColor, aerialColorInt);
        aerialBGColorInt = ta.getColor(R.styleable.AerialView_aerialBgColor, aerialBGColorInt);
        aerialTextColorInt = ta.getColor(R.styleable.AerialView_aerialTextColor, aerialTextColorInt);
        drawBg = ta.getBoolean(R.styleable.AerialView_drawBg, true);
        ta.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        aerialStrokeRect = new RectF(leftX, topY, rightX, bottomY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (drawBg) {
//            canvas.drawColor(bgColorInt);
        }

        mPaint.setColor(aerialBGColorInt);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(STROKE_WIDTH);
        //绘制矩形背景
        canvas.drawRect(leftX, topY, rightX, bottomY, mPaint);

        mPaint.setColor(aerialColorInt);
        mPaint.setStyle(Paint.Style.STROKE);
        //绘制矩形边框
        aerialStrokeRect.set(leftX, topY, rightX, bottomY);
        aerialStrokeRect.inset(STROKE_WIDTH / 2, STROKE_WIDTH / 2);
        canvas.drawRect(aerialStrokeRect, mPaint);

        if (!TextUtils.isEmpty(drawText)) {
            mPaint.setColor(aerialTextColorInt);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setTextSize(viewHeight / 5);
            //绘制文字
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            int verticalBaseline = (int) (aerialStrokeRect.centerY() - fontMetrics.top / 2 - fontMetrics.bottom / 2);
            canvas.drawText(drawText, aerialStrokeRect.centerX(), verticalBaseline, mPaint);
        }
    }

    /**
     * 传入外部的缩放比例和左上角位移数据
     *
     * @param scaling
     * @param marginLeft
     * @param marginTop
     * @param phoneW
     * @param phoneH
     */
    public void updateData(float scaling, int marginLeft, int marginTop,
                           int phoneW, int phoneH,
                           int currentFrameWidth, int currentFrameHeight) {
        if (getVisibility() != VISIBLE) {
            return;
        }
        if (scaling <= 0) {
            DebugLog.error(TAG, "[updateData] scaling <= 0");
            return;
        }
        if (phoneW <= 0 || phoneH <= 0) {
            DebugLog.error(TAG, "[updateData] phoneW <= 0 || phoneH <= 0");
            return;
        }


        DebugLog.debug(TAG, "");
        this.drawText = "x" + fnum.format(scaling);
        this.currentFrameWidth = currentFrameWidth;
        this.currentFrameHeight = currentFrameHeight;
        this.marginLeft = marginLeft;
        this.marginTop = marginTop;
        this.phoneWidth = phoneW;
        this.phoneHeight = phoneH;

        DebugLog.debug(TAG, "[updateData] scaling=" + scaling + " marginLeft=" + this.marginLeft + " marginTop=" + this.marginTop + " phoneW=" + phoneW + " phoneH=" + phoneH);

        if (viewWidth > 0 && viewHeight > 0) {
            doUpdate();
        }
    }

    private void doUpdate() {

        if (phoneWidth == 0 || phoneHeight == 0) {
            return;
        }

        leftX = this.marginLeft * viewWidth / (float) currentFrameWidth;
        rightX = leftX + viewWidth * phoneWidth / (float) currentFrameWidth;
        topY = this.marginTop * viewHeight / (float) currentFrameHeight;
        bottomY = topY + viewHeight * phoneHeight / (float) currentFrameHeight;
        DebugLog.debug(TAG, "[updateData] leftX=" + leftX + " rightX=" + rightX + " topY=" + topY + " bottomY=" + bottomY);
        invalidate();
    }
}
