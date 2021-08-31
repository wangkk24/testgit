package com.pukka.ydepg.moudule.multviewforstb.multiview.module.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.R;


/**
 * 鸟瞰图
 */
public class AngleView extends View {

    private static final String TAG = "AngleView";

    /**
     * 创建一个画笔
     */
    private Paint mPaint;

    /**
     * 圆形直径
     */
    private int diameter;

    /**
     * 圆形的padding
     */
    private final int CIRCLE_PADDING = 100;
    /**
     * 扇形相对圆形的padding
     */
    private final int ARC_PADDING = 20;

    /**
     * 圆心X
     */
    private int circleX;
    /**
     * 圆心Y
     */
    private int circleY;

    /**
     * 圆半径
     */
    private int circleRadius;

    /**
     * 扇形区域
     */
    private RectF oval;

    /**
     * 底边圆弧区域
     */
    private RectF ovalBottom;

    /**
     * 文字椭圆区域
     */
    private RectF ovalText;

    /**
     * 指针图片
     */
    private Bitmap anglePoint;
    /**
     * 眼睛图片
     */
    private Bitmap angleEye;
    /**
     * 指针图片位置
     */
    private int anglePointLeft;
    /**
     * 指针图片位置
     */
    private int anglePointTop;
    /**
     * 眼睛图片位置
     */
    private int angleEyeLeft;
    /**
     * 眼睛图片位置
     */
    private int angleEyeTop;
    /**
     * 文字背景宽度
     */
    private final int TEXT_BG_W = 80;
    /**
     * 文字背景高度
     */
    private final int TEXT_BG_H = 30;

    /**
     * 度
     */
    private final String DEGREE = "°";
    /**
     * 总共角度
     */
    private int totalDegree = 360;
    /**
     * 当前机位号
     * <p>
     * 默认使用：1
     */
    private volatile int currentCamera = 1;

    /**
     * 所有机位数量
     */
    private int totalCamera;

    /**
     * 平均机位角度
     */
    private float averageDegree;

    /**
     * 是否刷新操作
     */
    private volatile boolean doUpdate = true;
    /**
     * 当前初始角度
     * currentStartDegree[90,450]
     */
    private volatile float currentStartDegree = 90;

    public AngleView(Context context) {
        super(context);
        initSth();
    }

    public AngleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSth();
    }

    public AngleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSth();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        DebugLog.debug(TAG, "[onMeasure] w h " + viewWidth + " " + viewHeight);
        if (viewWidth > 0 && viewHeight > 0) {
            //圆形区域
            diameter = Math.min(viewHeight, viewWidth);
            circleX = diameter / 2;
            circleY = diameter / 2 - CIRCLE_PADDING;
            circleRadius = diameter / 2 - CIRCLE_PADDING;
            //扇形区域
            oval.left = CIRCLE_PADDING + ARC_PADDING;
            oval.top = ARC_PADDING;
            oval.right = diameter - CIRCLE_PADDING - ARC_PADDING;
            oval.bottom = diameter - CIRCLE_PADDING * 2 - ARC_PADDING;
            //底边圆弧区域
            ovalBottom.left = CIRCLE_PADDING;
            ovalBottom.top = 0;
            ovalBottom.right = diameter - CIRCLE_PADDING + 1;
            ovalBottom.bottom = diameter - CIRCLE_PADDING * 2 + 1;
            //指针位置
            int anglePointHeight = anglePoint.getHeight();
            int anglePointWidth = anglePoint.getWidth();
            anglePointLeft = (diameter - anglePointWidth) / 2;
            anglePointTop = diameter - CIRCLE_PADDING * 2 - anglePointHeight;
            //眼睛位置
            int angleEyeHeight = angleEye.getHeight();
            int angleEyeWidth = angleEye.getWidth();
            angleEyeLeft = (diameter - angleEyeWidth) / 2;
            angleEyeTop = diameter - CIRCLE_PADDING * 2 + 5;
            //文字区域
            ovalText.left = (diameter - TEXT_BG_W) / 2;
            ovalText.top = angleEyeTop + angleEyeHeight + 6;
            ovalText.right = (diameter + TEXT_BG_W) / 2;
            ovalText.bottom = ovalText.top + TEXT_BG_H;
            invalidate();
        }
    }

    /**
     * 初始化画笔
     */
    private void initSth() {
        oval = new RectF(0, 0, getRight(), getBottom());
        ovalBottom = new RectF(0, 0, getRight(), getBottom());
        ovalText = new RectF(0, 0, getRight(), getBottom());
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(2);
        // 设置是否抗锯齿
        mPaint.setAntiAlias(true);
        // 帮助消除锯齿
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        anglePoint = BitmapFactory.decodeResource(getResources(), R.mipmap.angle_point);
        angleEye = BitmapFactory.decodeResource(getResources(), R.mipmap.angle_eye);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLUE);
        mPaint.setAlpha(20);
        //测试使用
//        canvas.drawRect(new Rect(0, 0, diameter, diameter), mPaint);
        //圆形
        mPaint.setColor(Color.BLACK);
        mPaint.setAlpha(51);
        canvas.drawCircle(circleX, circleY, circleRadius, mPaint);
        //扇形
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(76);
        //360 显示圆形并且画一根直线
        if (totalDegree != 360) {
            float startD = 180 - currentStartDegree;
            if (startD < 0) {
                startD += 360;
            }
            canvas.drawArc(oval, startD, totalDegree, true, mPaint);
        } else {
            canvas.drawCircle(circleX, circleY, circleRadius - ARC_PADDING, mPaint);
            mPaint.setAlpha(204);
            mPaint.setStrokeWidth(4);
            //根据旋转角度画线
            float startD = 180 - currentStartDegree;
            if (startD < 0) {
                startD += 360;
            }
            //角度变成弧度
            double angleHude = startD * Math.PI / 180;
            int x = (int) ((circleRadius - ARC_PADDING) * Math.cos(angleHude)) + circleX;
            int y = (int) ((circleRadius - ARC_PADDING) * Math.sin(angleHude)) + circleY;
            canvas.drawLine(circleX, circleY, x, y, mPaint);
        }
        //圆弧
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        canvas.drawArc(ovalBottom, 45, 90, false, mPaint);
        //指针
        canvas.save();
        canvas.drawBitmap(anglePoint, anglePointLeft, anglePointTop, null);
        //眼睛
        canvas.drawBitmap(angleEye, angleEyeLeft, angleEyeTop, null);
        canvas.restore();
        //文字背景
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAlpha(40);
        mPaint.setStrokeWidth(2);
        canvas.drawRoundRect(ovalText, 15, 15, mPaint);
        //文字
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(TEXT_BG_H);
        //绘制文字
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        int verticalBaseline = (int) (ovalText.centerY() - fontMetrics.top / 2 - fontMetrics.bottom / 2);
        String textAngle = Math.round(currentStartDegree - 90) + DEGREE;
        //360 显示圆形，并且从-180 180
        if (totalDegree == 360) {
            if ((currentStartDegree - 90) > 180) {
                textAngle = Math.round(currentStartDegree - 90 - 360) + DEGREE;
            }
        }
        canvas.drawText(textAngle, ovalText.centerX(), verticalBaseline, mPaint);
    }

    /**
     * 初始化参数
     *
     * @param totalCamera
     * @param degree
     * @param defaultCamera
     */
    public void initAngle(int totalCamera, int degree, int defaultCamera) {
        DebugLog.info(TAG, "[initAngle] totalCamera=" + totalCamera + " degree=" + degree + " defaultCamera=" + defaultCamera);
        if (doUpdate) {
            doUpdate = false;
        }
        this.totalCamera = totalCamera;
        this.totalDegree = degree;
        if (this.totalCamera <= 0) {
            DebugLog.error(TAG, "[initAngle] totalCamera = " + this.totalCamera);
            return;
        }

        if (this.totalDegree <= 0) {
            DebugLog.error(TAG, "[initAngle] totalDegree = " + this.totalDegree);
            return;
        }

        if (defaultCamera < 0) {
            DebugLog.error(TAG, "[initAngle] defaultCamera = " + defaultCamera);
            return;
        }
        currentCamera = defaultCamera;
        averageDegree = this.totalDegree * 1.0f / this.totalCamera;
        /*float startDegree = averageDegree * (defaultCamera - 1);
        if (startDegree < 0) {
            startDegree = 0;
        }
        currentStartDegree = startDegree + 90;

        doUpdate = true;
        //启动刷新动画线程
        new UpdateThread().start();

        if (diameter > 0) {
            postInvalidate();
        }*/
        postRefresh();
    }

    /**
     * 传入外部的缩放比例和左上角位移数据
     *
     * @param currentCamera
     */
    public void updateData(int currentCamera) {
       /* this.startDegree = averageDegree * (currentCamera - 1);
        if(this.startDegree < 0){
            this.startDegree = 0;
        }*/
        this.currentCamera = currentCamera;
        postRefresh();
        DebugLog.debug(TAG, "[updateData] currentCamera=" + currentCamera);
    }

    /**
     * 刷新当前机位位置
     */
    private void postRefresh(){
        float startDegree = averageDegree * (currentCamera - 1);
        if (startDegree < 0) {
            startDegree = 0;
        }
        currentStartDegree = startDegree + 90;
        if (diameter > 0) {
            postInvalidate();
        }
    }

    /**
     * 退出
     */
    public void stopAni() {
        doUpdate = false;
    }

    private class UpdateThread extends Thread {

        @Override
        public void run() {
            while (doUpdate) {
                float startDegree = averageDegree * (currentCamera - 1);
                if (startDegree < 0) {
                    startDegree = 0;
                }
                float targetDegree = startDegree + 90;

                if(currentCamera == totalCamera && totalDegree != 360){
                    targetDegree = totalDegree + 90;
                }

                float offsetDegree = currentStartDegree - targetDegree;
                //currentStartDegree 区间[90,450]
                if (offsetDegree == 0f) {
                    continue;
                } else if (offsetDegree > 180) {
                    //顺时针，路过机位1
                    currentStartDegree++;
                    if (currentStartDegree >= 450) {
                        currentStartDegree = 90;
                    }
                    if (currentStartDegree > targetDegree) {
                        currentStartDegree = targetDegree;
                    }
                    DebugLog.debug(TAG, "[UpdateThread] 1 currentStartDegree=" + currentStartDegree + " targetDegree=" + targetDegree);
                } else if (offsetDegree > 0) {
                    //正常逆时针
                    currentStartDegree--;
                    if (currentStartDegree < targetDegree) {
                        currentStartDegree = targetDegree;
                    }
                    DebugLog.debug(TAG, "[UpdateThread] 2 currentStartDegree=" + currentStartDegree + " targetDegree=" + targetDegree);
                } else if (offsetDegree < -180) {
                    //逆时针，路过机位1
                    currentStartDegree--;
                    if (currentStartDegree <= 90) {
                        currentStartDegree = 450;
                    }
                    if (currentStartDegree < targetDegree) {
                        currentStartDegree = targetDegree;
                    }
                    DebugLog.debug(TAG, "[UpdateThread] 3 currentStartDegree=" + currentStartDegree + " targetDegree=" + targetDegree);
                } else {
                    //正常顺时针
                    currentStartDegree++;
                    if (currentStartDegree > targetDegree) {
                        currentStartDegree = targetDegree;
                    }
                    DebugLog.debug(TAG, "[UpdateThread] 4 currentStartDegree=" + currentStartDegree + " targetDegree=" + targetDegree);
                }


                postInvalidate();
              /*  try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        }
    }
}
