/*
 *Copyright (C) 2018 广州易杰科技, Inc.
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package com.pukka.ydepg.customui.focusView;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: FrameLayoutFocusLayout
 * @Package com.pukka.ydepg.customui.focusView
 * @date 2018/09/21 12:02
 */
public class FrameLayoutFocusLayout extends FrameLayout{
    public FrameLayoutFocusLayout(@NonNull Context context) {
        super(context);
    }

    public FrameLayoutFocusLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameLayoutFocusLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private FocusCallback mCallback;

    public void setCallback(FocusCallback callback){
        this.mCallback=callback;
    }

    @Override protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if(null!=mCallback){
            mCallback.onFocusCallback(gainFocus,this);
        }
    }

    public interface FocusCallback{

        void onFocusCallback(boolean hasFocus,FrameLayoutFocusLayout view);
    }
}