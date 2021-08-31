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
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.pukka.ydepg.R;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;

/**
 * 产品列表itemview,内部进行从小到大,从大变小的动画切换
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: ProductListRelativeLayout
 * @Package com.pukka.ydepg.customui.focusView
 * @date 2018/01/23 13:51
 */
public class ProductListRelativeLayout extends RelativeLayout
{

    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.
        ItemFocusScale(FocusHighlight.ZOOM_FACTOR_SMALL);

    private boolean isCanUpdate = true;

    private boolean keyDownFocusKeep;

    public ProductListRelativeLayout(Context context)
    {
        this(context, null);

    }

    public ProductListRelativeLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);


    }

    public ProductListRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        if (null != attrs)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable
                .ProductListRelativeLayout, 0, 0);
            keyDownFocusKeep = a.getBoolean(R.styleable
                .ProductListRelativeLayout_key_down_focus_keep, false);
            a.recycle();
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect
        previouslyFocusedRect)
    {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus)
        {
            isCanUpdate = true;
        }
        if (null != mFocusHighlight && isCanUpdate)
        {
            mFocusHighlight.onItemFocused(this, gainFocus);
        }
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mFocusHighlight.onInitializeView(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && keyDownFocusKeep)
        {
            isCanUpdate = false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
