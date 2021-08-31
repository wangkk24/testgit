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
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.pukka.ydepg.R;

/**
 *
 * 容器监听当前是获取到了焦点还是失去了焦点
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: LinearLayoutFoucsView
 * @Package com.pukka.ydepg.customui.focusView
 * @date 2017/12/25 15:46
 */
public class LinearLayoutFoucsView extends BaseLinearFocusView{

  private TextView mChildTextView;

  public LinearLayoutFoucsView(Context context) {
    this(context,null);
  }
  public LinearLayoutFoucsView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    mChildTextView = (TextView) findViewById(R.id.tv_content);
  }

  @Override protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
    super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    setFocusStatus(gainFocus,gainFocus?R.drawable.button_epg_column_focus:Color.TRANSPARENT);
  }

  /**
   * 保存当前位置状态,用背景色做标记
   */
  @Override
  public void updateRecordViewStatus(boolean isClearBgColor){
    setFocusStatus(true,!isClearBgColor?R.color.white_transparent_50:Color.TRANSPARENT);
  }

  /**
   * 设置获取焦点/失去焦点时候的状态
   * @param flag
   * @param resId
   */
  private void setFocusStatus(boolean flag,int resId){
    setSelectStatus(flag,resId,mChildTextView);
  }

}