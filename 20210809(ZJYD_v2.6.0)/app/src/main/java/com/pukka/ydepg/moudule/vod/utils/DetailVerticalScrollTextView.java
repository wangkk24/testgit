package com.pukka.ydepg.moudule.vod.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.VerticalScrollTextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by weicy on 2021/6/1.
 *
 * 使用这个控件，必须设置确定的长宽，长要自己调试，正好可以容纳每一行最后一个字符
 * 这个控件做成了可滚动时paddingTop不生效，从而避免可滚动和不可滚动时paddingTop不一致的问题
 * 需要设置paddingTop为margin_5来实现可滚动和不可滚动时paddingTop一致
 */

public class DetailVerticalScrollTextView extends AppCompatTextView {

    private float step = 0f;

    private String text;

    private boolean focused;

    private float height;

    private int textHeight;

    private boolean selected;

    private List<String> textList = new ArrayList<String>();    //分行保存textview的显示信息。

    private List<String> tempTextList = new ArrayList<>();

    /**
     * 自动滚动
     *
     */
    private boolean isAuto;

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
        invalidate();
    }

    private int lineCount;

    private DetailVerticalScrollTextView.RefreshHandler mhandler;


    private static class RefreshHandler extends Handler {

        private WeakReference<DetailVerticalScrollTextView> weakView;

        public RefreshHandler(DetailVerticalScrollTextView view){
            this.weakView=new WeakReference<DetailVerticalScrollTextView>(view);

        }

        @Override
        public void handleMessage(Message msg) {
            if(null!=weakView.get()){
                weakView.get().invalidate();
            }
        }
    }


    public DetailVerticalScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(null!=attrs){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerticalScrollTextView, 0, 0);
            isAuto = a.getBoolean(R.styleable.VerticalScrollTextView_auto, false);
            lineCount=a.getInt(R.styleable.VerticalScrollTextView_tv_line,4);
            a.recycle();
        }
        mhandler=new DetailVerticalScrollTextView.RefreshHandler(this);
    }

    public DetailVerticalScrollTextView(Context context) {

        super(context);
        mhandler=new DetailVerticalScrollTextView.RefreshHandler(this);

    }

    @Override

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        textList.clear();
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        Paint.FontMetrics fm = paint.getFontMetrics();
        text = getText().toString();
        if (text == null || text.length() == 0) {
            return;
        }
        Layout layout = getLayout();
        textHeight = (int) (Math.ceil(fm.descent - fm.ascent));
        textHeight = (int) (textHeight * layout.getSpacingMultiplier() + layout
                .getSpacingAdd());
        for (int i = 0; i < layout.getLineCount(); i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            String line = text.substring(lineStart, lineEnd);
            textList.add(line);
        }
        height = textHeight * layout.getLineCount();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        this.focused = focused;
        invalidate();
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    //下面代码是利用上面计算的显示行数，将文字画在画布上，实时更新。


    @Override
    public void setSelected(boolean selected)
    {
        this.selected=selected;
        invalidate();
        super.setSelected(selected);
    }

    @Override

    public void onDraw(Canvas canvas) {
        if ((textList.size() == 0 || height <= this.getHeight())) {
            super.onDraw(canvas);
        } else {
            if (selected||focused||isAuto) {
                for (int i = 0; i < textList.size(); i++) {
                    canvas.drawText(textList.get(i),getPaddingLeft(), (i + 1) * textHeight - step, getPaint());
                }

                step = step + 0.4f;
                if (step >= height - this.getHeight() + getResources().getDimensionPixelOffset(R.dimen.details_data_source_margin_top)) {
                    step = 0;
                }
                mhandler.sendEmptyMessageDelayed(1,20);
            } else {
                step = 0;
                if (textList.size() > lineCount-1) {
                    tempTextList.clear();
                    for (int i = 0; i < lineCount; i++) {
                        if (i == lineCount-1) {
                            String content = textList.get(i);
                            String temp = content.substring(0, content.length() - 1);
                            tempTextList.add(temp + "...");
                        } else {
                            tempTextList.add(textList.get(i));
                        }

                    }
                    for (int i = 0; i < tempTextList.size(); i++) {
                        canvas.drawText(tempTextList.get(i),getPaddingLeft(), (i + 1) * textHeight  - step, getPaint());
                    }

                } else {
                    super.onDraw(canvas);
                }

            }
        }
    }

    @Override
    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

}
