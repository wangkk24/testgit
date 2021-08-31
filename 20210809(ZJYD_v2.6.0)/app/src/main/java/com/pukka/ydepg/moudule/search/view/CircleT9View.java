package com.pukka.ydepg.moudule.search.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.uiutil.DensityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CircleT9View extends View {

    //============ 固定属性 Start ============
    //[映射]数字按键->T9键盘字符
    private static final HashMap<Integer, List<String>> mapNumber2Keyboard;
    //[映射]按键位置->文字坐标
    private static List<Float[]> listPosition;
    static{
        mapNumber2Keyboard = new HashMap<>();
        //5个字符位置分别对应0左,1上,2右,3下,4中(这样为了初始化方便,先初始化外圈,最后中心,没有按键则以空代替)
        mapNumber2Keyboard.put(2,new ArrayList<>(Arrays.asList("A", "2", "C","" ,"B")));
        mapNumber2Keyboard.put(3,new ArrayList<>(Arrays.asList("D", "3", "F","" ,"E")));
        mapNumber2Keyboard.put(4,new ArrayList<>(Arrays.asList("G", "4", "I","" ,"H")));
        mapNumber2Keyboard.put(5,new ArrayList<>(Arrays.asList("J", "5", "L","" ,"K")));
        mapNumber2Keyboard.put(6,new ArrayList<>(Arrays.asList("M", "6", "O","" ,"N")));
        mapNumber2Keyboard.put(7,new ArrayList<>(Arrays.asList("P", "7", "R","S","Q")));
        mapNumber2Keyboard.put(8,new ArrayList<>(Arrays.asList("T", "8", "V","" ,"U")));
        mapNumber2Keyboard.put(9,new ArrayList<>(Arrays.asList("W", "9", "Y","Z","X")));
    }

    //初始画弧所在的角度
    private static final int START_DEGREE     = 135;
    //============ 固定属性 End ============



    //============ 可调整属性(根据高保真调整) Start ============
    //外圆边框的宽度(宽度为此值*2)
    private static final float STROKE_CIRCLE_WIDTH = 0.5f;
    //边框的颜色
    private static final int   STROKE_COLOR      = Color.WHITE;
    //选中区域的颜色
    private static final int   PIE_COLOR_SELECT  = Color.WHITE;
    //未选中区域的颜色
    private static       int   PIE_COLOR_NORMAL  = Color.GRAY;
    //选中文字的颜色
    private static       int   TEXT_COLOR_SELECT = Color.GRAY;
    //未选中文字的颜色
    private static final int   TEXT_COLOR_NORMAL = Color.WHITE;
    //文字大小
    private static       float mTextSize;
    //饼状图外圆半径
    private static       float mRadius;
    //饼状图内圆半径, 需满足mRadiusInner < mRadius
    private static       float mRadiusInner;

    //============ 可调整属性(根据高保真调整) End ============



    private final Context context;
    //饼图白色轮廓画笔
    private Paint mStrokePaint;
    //饼状图画笔
    private Paint mPiePaint;
    //文字画笔
    private Paint mTextPaint;
    //背景圆画笔,用于产生按键间分割线的效果
    private Paint mBackgroundPaint;
    //背景区域
    private final RectF mRectBackground = new RectF();
    //4个外圆区域List,根据pos依次是0左,1上,2右,3下,4中
    List<RectF> listRectPie = new ArrayList<>();
    //当前选中的T9键位置 0左,1上,2右,3下,4中
    private int selectPosition;
    //构成饼状图的按键数据集合,根据pos依次是0左,1上,2右,3下,4中
    private List<String> mListKey = new ArrayList<>();



    public CircleT9View(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CircleT9View(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CircleT9View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    //onMeasure 作用
    //（1）一般情况重写onMeasure()方法作用是为了自定义View尺寸的规则，如果你的自定义View的尺寸是根据父控件行为一致，就不需要重写onMeasure()方法
    //（2）如果不重写onMeasure方法，那么自定义view的尺寸默认就和父控件一样大小，当然也可以在布局文件里面写死宽高，而重写该方法可以根据自己的需求设置自定义view大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //mRadius是外圆半径
        int length = 2*(int)mRadius+1;
        setMeasuredDimension(length, length);
    }

    //初始化画笔和效果动画
    private void init() {
        mRadius           = getResources().getDimensionPixelSize(R.dimen.margin_46);    //高保真标注外圆直径138px  则半径为138/2/1.5=46dp
        mRadiusInner      = getResources().getDimensionPixelSize(R.dimen.margin_18p7);  //高保真标注内圆直径56px   则半径为 56/2/1.5=18.67dp
        mTextSize         = getResources().getDimensionPixelSize(R.dimen.order_text_22sp);//字号22sp为试验得到

        PIE_COLOR_NORMAL  = getResources().getColor(R.color.search_btn_background_color);
        TEXT_COLOR_SELECT = PIE_COLOR_NORMAL;

        //初始化时选中中间位置
        selectPosition = SelectPosition.CENTER;
        initPaint();
        initRect();
        initTextPosition();
    }

    //初始化画笔
    private void initPaint() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(STROKE_COLOR);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mStrokePaint = new Paint();
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        //Pass 0 to stroke in hairline mode.Hairlines always draws a single pixel independent of the canva's matrix.
        mStrokePaint.setStrokeWidth(0);
        mStrokePaint.setColor(STROKE_COLOR);

        mPiePaint = new Paint();
        mPiePaint.setAntiAlias(true);
        mPiePaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setColor(TEXT_COLOR_SELECT);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);  //使文字锚点x坐标居中
    }

    //初始化绘制内外弧形所在矩形的四点坐标
    private void initRect() {
        //0左,1上,2右,3下,4中
        listRectPie.add(new RectF(0,STROKE_CIRCLE_WIDTH,2*mRadius-2*STROKE_CIRCLE_WIDTH,2*mRadius-STROKE_CIRCLE_WIDTH));
        listRectPie.add(new RectF(STROKE_CIRCLE_WIDTH,0,2*mRadius-STROKE_CIRCLE_WIDTH,2*mRadius-2*STROKE_CIRCLE_WIDTH));
        listRectPie.add(new RectF(2*STROKE_CIRCLE_WIDTH,STROKE_CIRCLE_WIDTH,2*mRadius,2*mRadius-STROKE_CIRCLE_WIDTH));
        listRectPie.add(new RectF(STROKE_CIRCLE_WIDTH,2*STROKE_CIRCLE_WIDTH,2*mRadius-STROKE_CIRCLE_WIDTH,2*mRadius));
        listRectPie.add(new RectF(mRadius - mRadiusInner,mRadius - mRadiusInner,mRadius + mRadiusInner,mRadius + mRadiusInner));

        mRectBackground.left   = 2 * STROKE_CIRCLE_WIDTH;
        mRectBackground.top    = 2 * STROKE_CIRCLE_WIDTH;
        mRectBackground.right  = 2 * mRadius - 2 * STROKE_CIRCLE_WIDTH;
        mRectBackground.bottom = 2 * mRadius - 2 * STROKE_CIRCLE_WIDTH;
    }

    //初始化文字位置
    private void initTextPosition(){
        Paint.FontMetrics fontMetrics=mTextPaint.getFontMetrics();
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        //左、右、中心按键字符位置坐标Y值
        float centerTextPosY = mRadius+distance;
        listPosition = new ArrayList<>();
        listPosition.add(new Float[]{(mRadius-mRadiusInner)/2,centerTextPosY});//左
        listPosition.add(new Float[]{mRadius,getResources().getDimension(R.dimen.margin_22)});//上 Y坐标22dp为试验得到
        listPosition.add(new Float[]{(3*mRadius+mRadiusInner)/2,centerTextPosY});//右
        listPosition.add(new Float[]{mRadius,getResources().getDimension(R.dimen.margin_86)});//下 Y坐标86dp为试验得到
        listPosition.add(new Float[]{mRadius,centerTextPosY});//中间
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mRectBackground,0, 360, true,mBackgroundPaint);
        if (mListKey != null && !mListKey.isEmpty()) {
            float pieStart = START_DEGREE;//起始位置
            for (int i = 0; i <= 4; i++) {
                //设置弧形颜色和文字颜色
                if( selectPosition == i ){
                    mPiePaint.setColor(PIE_COLOR_SELECT);    //选中时背景为白色
                    mTextPaint.setColor(TEXT_COLOR_SELECT);  //选中时文字为灰色
                } else {
                    mPiePaint.setColor(PIE_COLOR_NORMAL);    //未选中时背景为灰色
                    mTextPaint.setColor(TEXT_COLOR_NORMAL);  //未选中时文字为白色
                }

                if( i != SelectPosition.CENTER){
                    //绘制外圈扇形区域
                    canvas.drawArc(listRectPie.get(i), pieStart, 90, true, mPiePaint);
                } else {
                    //绘制中心圆形区域
                    canvas.drawArc(listRectPie.get(i), 0, 360, true, mPiePaint);
                    canvas.drawArc(listRectPie.get(i), 0, 360, true, mStrokePaint);//绘制中心区域白色边框
                }
                //绘制文字
                canvas.drawText(mListKey.get(i), listPosition.get(i)[0] ,listPosition.get(i)[1], mTextPaint );
                //获取下一个弧形的起点
                pieStart += 90;
            }
        }
    }

    //设置需要绘制的数据集合,使用前必须首先调用此方法
    //number为键盘中心数字
    public void init(int number) {
        mListKey = mapNumber2Keyboard.get(number);
        selectPosition = SelectPosition.CENTER;
        invalidate();
    }

    //direction传递SelectPosition对象的值
    public void setSelectPosition(int direction){
        //键盘某方向无对应字母无法选中该位置
        if(!TextUtils.isEmpty(mListKey.get(direction))){
            selectPosition = direction;
            invalidate();
        }
    }

    //0左,1上,2右,3下,4中
    public interface SelectPosition{
        int LEFT   = 0;
        int UP     = 1;
        int RIGHT  = 2;
        int DOWN   = 3;
        int CENTER = 4;
    }

    public String getSelectText(int pos){
        if (pos<0 || pos>4 ){
            pos = selectPosition;
        }
        return mListKey.get(pos);
    }
}