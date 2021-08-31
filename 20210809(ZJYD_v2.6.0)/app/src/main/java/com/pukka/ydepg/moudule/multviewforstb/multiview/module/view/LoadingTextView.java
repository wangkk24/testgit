package com.pukka.ydepg.moudule.multviewforstb.multiview.module.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.pukka.ydepg.R;


public class LoadingTextView extends View {

    private static final String TAG = "LoadingTextView";


    /**
     * 原始View的宽高
     */
    private int oldWidth;
    private int oldHeight;

    private Paint mPaint;

    private volatile String percentMsg = "100%";

    /**
     * 是否停止状态
     */
    private volatile boolean stopState = true;

    public LoadingTextView(Context context) {
        super(context);
        init(null);
    }

    public LoadingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public LoadingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);

    }

    private void init(AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(2);
        // 设置是否抗锯齿
        mPaint.setAntiAlias(true);
        // 帮助消除锯齿
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //如果是停止状态，画透明色
        if (stopState) {
            return;
        }
        canvas.drawColor(0x65000000);
        mPaint.setTextSize(oldWidth * 1f / 6);
        //绘制文字
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        int verticalBaseline = (int) (oldHeight * 1f / 2 - fontMetrics.top / 2 - fontMetrics.bottom / 2);
        canvas.drawText(percentMsg, oldWidth * 1f / 2, verticalBaseline, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        oldWidth = w;
        oldHeight = h;
    }

    /**
     * 显示动画
     */
    public void onBufferingUpdate(int percent) {
        if(percent <= 0){
            percent = 1;
        }
        stopState = false;
        percentMsg = getContext().getString(R.string.loading_tip) + (percent < 10 ? ("0" + percent) : percent) + "%";
        postInvalidate();
    }

    /**
     * 停止动画
     */
    public void stopBufferingUpdate() {
        stopState = true;
        postInvalidate();
    }
}