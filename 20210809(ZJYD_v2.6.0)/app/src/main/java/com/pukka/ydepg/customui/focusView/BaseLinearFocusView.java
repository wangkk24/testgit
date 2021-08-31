/*
 *Copyright (C) 2017 广州易杰科技, Inc.
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
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.LinearLayoutExt;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: BaseLinearFocusView
 * @Package com.pukka.ydepg.customui.focusView
 * @date 2017/12/26 14:54
 */
public abstract class BaseLinearFocusView extends LinearLayoutExt {

  public BaseLinearFocusView(Context context) {
    this(context, null);
  }

  public BaseLinearFocusView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * 设置内部textview字体加粗/正常,同时修改背景色
   */
  protected final void setSelectStatus(boolean flag, int resId, TextView... viewList) {
    if (flag) {
      //当前获取到了焦点
      setBackgroundResource(resId);
    } else {
      //当前失去了焦点
      setBackground(null);
      setBackgroundColor(resId);
    }
    if (null != viewList) {
      for (TextView textView : viewList) {
        if (null != textView) {
          textView.setTextColor((resId != Color.TRANSPARENT) ? ColorStateList.valueOf(Color.WHITE)
              : ColorStateList.valueOf(getContext().getResources().getColor(R.color.c24_color)));
        }
      }
    }
  }

  /**
   * 更新记录的焦点View状态(背景字体等)
   *
   * @param isClearBgColor 是否清除背景色?
   */
  abstract void updateRecordViewStatus(boolean isClearBgColor);
}
