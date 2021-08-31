package com.pukka.ydepg.launcher.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.pukka.ydepg.common.extview.RelativeLayoutExt;
import com.pukka.ydepg.launcher.ui.fragment.FocusInterceptor;
import com.pukka.ydepg.launcher.ui.fragment.FocusObserver;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.moudule.home.view.MyFragmentFocusInterceptLayout.java
 * @date: 2017-12-25 17:21
 * @version: V1.0 描述当前版本功能
 */


public class FocusRelativeLayoutExt extends RelativeLayoutExt implements FocusObserver {
    private FocusInterceptor mIntercept;
    public FocusRelativeLayoutExt(Context context) {
        super(context);
    }


    @Override
    public void setInterceptor(FocusInterceptor focusInterceptor) {
        if(mIntercept == null) {
            this.mIntercept = focusInterceptor;
        }
    }

    public FocusRelativeLayoutExt(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //dispatchKeyEvent通常会调用2次，一次down一次up，调用up的时候焦点已经变成了切换后的焦点，所以要过滤掉up事件
        return (event.getAction() ==KeyEvent.ACTION_DOWN&&mIntercept!=null&&mIntercept.interceptFocus(event,this))||super.dispatchKeyEvent(event);
    }
}
