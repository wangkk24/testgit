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
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.pukka.ydepg.R;

/**
 * 频道条目管理内容焦点的容器View
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: ChannelLinearLayoutFocusView
 * @Package com.pukka.ydepg.customui.focusView
 * @date 2017/12/26 14:45
 */
public class ChannelLinearLayoutFocusView extends BaseLinearFocusView {
  private TextView mProgramName;
  private TextView mChannelName;
  private TextView mChannelNo;
  private ImageView mImagePlayIcon;
  private RelativeLayout mContent;
  private ChangeListener mChangeListener;

  public ChannelLinearLayoutFocusView(Context context) {
    this(context,null);
  }

  public ChannelLinearLayoutFocusView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    mProgramName= (TextView) findViewById(R.id.tv_program_name);
    mChannelName= (TextView) findViewById(R.id.tv_channel_name);
    mChannelNo= (TextView) findViewById(R.id.tv_channel_no);
    mContent= (RelativeLayout) findViewById(R.id.rl_content);
    mImagePlayIcon= (ImageView) findViewById(R.id.iv_program_play);
  }

  @Override protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
    super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    setFocusStatus(gainFocus,gainFocus?R.drawable.button_epg_column_focus: Color.TRANSPARENT);
    if(null!=mImagePlayIcon){
      mImagePlayIcon.setVisibility(gainFocus? View.VISIBLE:View.GONE);
    }
    if(null!=mChangeListener){
      mChangeListener.onFocusChange(gainFocus);
    }
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
   * @param flag flag
   * @param resId resId
   */
  private void setFocusStatus(boolean flag,int resId){
    setSelectView(flag,resId,mProgramName,mChannelName,mChannelNo);
    if(!flag){
      if(getTag().equals(TVGuideManagerView.ViewType.VIEW_PROGRAM)){
        mProgramName.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.c24_color)));
      }else{
        mProgramName.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.c21_color)));
      }
      if(!getTag().equals(TVGuideManagerView.ViewType.VIEW_CHANNEL)){
        if(null!=mChannelName){
          mChannelName.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.c21_color)));
        }
        if(null!=mChannelNo){
          mChannelNo.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.c21_color)));
        }
      }else{
        if(null!=mChannelName){
          mChannelName.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.c24_color)));
        }
        if(null!=mChannelNo){
          mChannelNo.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.c24_color)));
        }
      }
      mProgramName.setSelected(false);
    }else{
      mProgramName.setSelected(true);
      if(getTag().equals(TVGuideManagerView.ViewType.VIEW_PROGRAM)){
        mProgramName.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.c21_color)));
      }
    }
  }

  private void setSelectView(boolean flag,int resId,TextView ... viewList){
    if(flag){
      //当前获取到了焦点
      mContent.setBackgroundResource(resId);
    }else{
      //当前失去了焦点
      mContent.setBackground(null);
      mContent.setBackgroundColor(resId);
    }
    if(null!=viewList){
      for (TextView textView:viewList){
        //1.10版本修改节目单上加频道号，同时选中状态字体高亮---2019.06.13
        //if(null!=textView && textView.getId()!=R.id.tv_channel_name){
        if(null!=textView){
          textView.setTextColor(flag? ColorStateList.valueOf(Color.WHITE):ColorStateList.
                  valueOf(getContext().getResources().getColor(R.color.c24_color)));
        }
      }
    }
  }

  public void setFocusListener(ChangeListener listener){
    this.mChangeListener=listener;
  }

  public interface ChangeListener{
    void onFocusChange(boolean hasFocus);
  }
}
