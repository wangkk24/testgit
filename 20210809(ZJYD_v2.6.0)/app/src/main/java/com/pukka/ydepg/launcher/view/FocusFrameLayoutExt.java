package com.pukka.ydepg.launcher.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pukka.ydepg.common.extview.FrameLayoutExt;
import com.pukka.ydepg.launcher.ui.fragment.FocusInterceptor;
import com.pukka.ydepg.launcher.ui.fragment.FocusObserver;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.moudule.home.view.FrameLayoutFocusLayoutExt.java
 * @date: 2017-12-28 14:26
 * @version: V1.0 描述当前版本功能
 */
public class FocusFrameLayoutExt extends FrameLayoutExt implements FocusObserver {

    private FocusInterceptor mIntercept;

    public FocusFrameLayoutExt(@NonNull Context context) {
        super(context);
    }

    public FocusFrameLayoutExt(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusFrameLayoutExt(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setInterceptor(FocusInterceptor focusInterceptor) {
        if(mIntercept == null) {
            this.mIntercept = focusInterceptor;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //dispatchKeyEvent通常会调用2次，一次down一次up，调用up的时候焦点已经变成了切换后的焦点，所以要过滤掉up事件
        return (mIntercept!=null&&mIntercept.interceptFocus(event,this))||super.dispatchKeyEvent(event);
    }
}