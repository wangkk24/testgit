package com.pukka.ydepg.customui.focusView;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.pukka.ydepg.common.extview.RelativeLayoutExt;

/**
 * Created by Eason on 9/27/2018.
 */

public class RelativeLayoutFocusView extends RelativeLayoutExt
{
    private OnFocusChangeListener mListener;

    public RelativeLayoutFocusView(Context context)
    {
        super(context);
    }

    public RelativeLayoutFocusView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect
            previouslyFocusedRect)
    {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if(null!=mListener){
            mListener.onFocusChange(this,gainFocus);
        }
    }

    public void setOnFocusListener(OnFocusChangeListener changeListener){
        this.mListener=changeListener;
    }

}
