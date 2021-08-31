package com.pukka.ydepg.moudule.search.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import java.lang.reflect.Field;

/**
 * Created by hasee on 2017/1/23.
 */

public class SearchEditText extends AppCompatAutoCompleteTextView {
    public SearchEditText(Context context) {
        super(context);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        modifyCursorDrawable(context,attrs);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        modifyCursorDrawable(context,attrs);
    }

    private void modifyCursorDrawable(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchEditText);
        int drawable = a.getResourceId(R.styleable.SearchEditText_textCursorDrawable,0);
        if(drawable != 0) {
            try {

                Field setCursor = TextView.class.getDeclaredField("mCursorDrawableRes");
                setCursor.setAccessible(true);
                setCursor.set(this, drawable);

            } catch (Exception e) {
                SuperLog.error("modifyCursorDrawable",e);
            }
        }
        a.recycle();
    }
}
