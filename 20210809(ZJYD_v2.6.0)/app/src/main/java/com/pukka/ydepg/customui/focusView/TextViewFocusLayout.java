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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: TextViewFocusLayout
 * @Package com.pukka.ydepg.customui.focusView
 * @date 2018/09/22 15:01
 */
@SuppressLint("AppCompatCustomView")
public class TextViewFocusLayout extends TextView {
    public TextViewFocusLayout(Context context) {
        super(context);
    }

    public TextViewFocusLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewFocusLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if(null!=mCallback){
            mCallback.onFocusChange(this,focused);
        }
    }

    private OnTextViewFocusCallback mCallback;

    public void setFocusCallback(OnTextViewFocusCallback callback){
        this.mCallback=callback;
    }

    public interface OnTextViewFocusCallback{
        void onFocusChange(View view,boolean hasFocus);
    }
}
