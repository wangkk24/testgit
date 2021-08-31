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
package com.pukka.ydepg.customui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pukka.ydepg.R;
import com.pukka.ydepg.moudule.player.util.ViewkeyCode;

/**
 * 遥控器操作监听到下面的KeyCode时候,显示当前popupwindow;
 *
 *  keyCode == KeyEvent.KEYCODE_DPAD_CENTER
 *
 *  keyCode == KeyEvent.KEYCODE_ENTER
 *
 *  keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
 *
 *  keyCode == KeyEvent.KEYCODE_BACK
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: PlayBackViewPopwindow
 * @Package com.pukka.ydepg.customui
 * @date 2017/12/20 16:57
 */
public class PlayBackViewPopwindow extends PopupWindow implements
    View.OnClickListener,View.OnFocusChangeListener,View.OnKeyListener{
  private Context mContext;
  private Handler mHandler;

  private RelativeLayout mPlayContinue;
  private RelativeLayout mRePlay;
  private RelativeLayout mBackTVPoint;
  private RelativeLayout mPlayDetail;
  private ImageView mImgRePlay;
  private TextView mTVRePlay;

  private int mNonePosition = -1;

  private boolean isBackKeyCode;

  private OnKeyBackDismissListener mListener;

  /**
   * 用来标记是不是用户手动触发:
   * true是用户取消的,false不是用户手动操作取消的;
   */
  private boolean fromUserDismiss;

  /**
   * description: 构造方法
   * @author fuqiang
   * @date 2017/12/20 下午5:34
   * @version 1.0
   * @param context
   * @param handler
   * @param nonePosition
   */
  public PlayBackViewPopwindow(Context context, Handler handler, int nonePosition) {
    this.mContext = context;
    this.mHandler = handler;
    this.mNonePosition = nonePosition;
    initView();
    initOnKeyListener();
    initClickFocusListener();
  }

  private void initView() {
    View view = LayoutInflater.from(mContext).inflate(R.layout.window_pip_playback, null);
    mPlayContinue = (RelativeLayout) view.findViewById(R.id.rl_continue);
    mRePlay = (RelativeLayout) view.findViewById(R.id.rl_replay);
    mBackTVPoint = (RelativeLayout) view.findViewById(R.id.rl_backtv_point);
    mPlayDetail = (RelativeLayout) view.findViewById(R.id.rl_detail);
    mImgRePlay = (ImageView) view.findViewById(R.id.iv_replay);
    mTVRePlay= (TextView) view.findViewById(R.id.tv_replay);
    mBackTVPoint.setVisibility(View.GONE);
    setContentView(view);
    setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
    setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    if (mNonePosition == 0) {
      mPlayContinue.setVisibility(View.GONE);
    }
  }

  private void initOnKeyListener() {
    mPlayContinue.setOnKeyListener(this);
    mRePlay.setOnKeyListener(this);
    mBackTVPoint.setOnKeyListener(this);
    mPlayDetail.setOnKeyListener(this);
  }

  private void initClickFocusListener() {
    mPlayContinue.setOnClickListener(this);
    mRePlay.setOnClickListener(this);
    mBackTVPoint.setOnClickListener(this);
    mPlayDetail.setOnClickListener(this);

    mPlayContinue.setOnFocusChangeListener(this);
    mRePlay.setOnFocusChangeListener(this);
    mBackTVPoint.setOnFocusChangeListener(this);
    mPlayDetail.setOnFocusChangeListener(this);
  }
  /**
   * description: Pip界面子view响应的click事件
   * @author fuqiang
   * @date 2017/12/20 下午5:34
   * @version 1.0
   * @param v
   */
  @Override public void onClick(View v) {
    fromUserDismiss=true;
    dismiss();
    switch (v.getId()) {
      case R.id.rl_continue:
        mHandler.sendEmptyMessage(ViewkeyCode.VIEW_KEY_CONTINUE_PLAY);
        break;
      case R.id.rl_replay:
        if(isBackKeyCode){
          mHandler.sendEmptyMessage(ViewkeyCode.VIEW_KEY_EXIT);
        }else{
          fromUserDismiss=true;
          mHandler.sendEmptyMessage(ViewkeyCode.VIEW_KEY_REPLAY);
        }
        break;
      case R.id.rl_backtv_point:
        mHandler.sendEmptyMessage(ViewkeyCode.VIEW_KEY_BACK_TV_PLAY);
        break;
      case R.id.rl_detail:
        mHandler.sendEmptyMessage(ViewkeyCode.VIEW_KEY_DETAIL_PLAY);
        break;
    }
  }
  /**
   * description: 焦点改变,切换背景
   * @author fuqiang
   * @date 2017/12/20 下午5:32
   * @version 1.0
   * @param v
   * @param hasFocus
   */
  @Override public void onFocusChange(View v, boolean hasFocus) {
    switch (v.getId()) {
      case R.id.rl_continue:
        if (hasFocus) {
          mPlayContinue.setBackgroundResource(R.color.white_transparent_50);
        } else {
          mPlayContinue.setBackgroundResource(R.color.transparent);
        }
        break;
      case R.id.rl_replay:
        if (hasFocus) {
          mRePlay.setBackgroundResource(R.color.white_transparent_50);
        } else {
          mRePlay.setBackgroundResource(R.color.transparent);
        }
        break;
      case R.id.rl_backtv_point:
        if (hasFocus) {
          mBackTVPoint.setBackgroundResource(R.color.white_transparent_50);
        } else {
          mBackTVPoint.setBackgroundResource(R.color.transparent);
        }
        break;
      case R.id.rl_detail:
        if (hasFocus) {
          mPlayDetail.setBackgroundResource(R.color.white_transparent_50);
        } else {
          mPlayDetail.setBackgroundResource(R.color.transparent);
        }
        break;
    }
  }
  /**
   * description: 窗口销毁
   * @author fuqiang
   * @date 2017/12/20 下午5:34
   * @version 1.0
   */
  @Override
  public void dismiss() {
    super.dismiss();
    //不是用户手动dismiss的话,回调到当前fragment/activity中
    //防止重复操作,如:popwindow销毁恢复播放这个操作.
    if(null!=mListener && !fromUserDismiss){
      mListener.onDismiss();
    }
  }

  //播放结束的方法
  public void showPlayBack(View parentView,boolean isBackKey,OnKeyBackDismissListener listener,boolean isCompleted){
    showPlayBack(parentView,isBackKey,listener);
    if(isCompleted) {
      mPlayContinue.setVisibility(View.GONE);
      mBackTVPoint.setVisibility(View.VISIBLE);
      initFocus(mRePlay);
    }
  }

  /**
   * description: 显示Pip PlayBack 界面
   * @author fuqiang
   * @date 2017/12/20 下午5:32
   * @version 1.0
   * @param parentView 依附的父容器
   * @param isBackKey 是不是KeyCode.KEY_BACK
   */
  public void showPlayBack(View parentView,boolean isBackKey,OnKeyBackDismissListener listener) {
    this.isBackKeyCode=isBackKey;
    this.mListener=listener;
    fromUserDismiss=false;
    if(mNonePosition==0){
      mPlayContinue.setVisibility(View.GONE);
    }else{
      mPlayContinue.setVisibility(View.VISIBLE);
    }
    if(isBackKeyCode){
      mImgRePlay.setBackground(null);
      mImgRePlay.setImageResource(R.drawable.exit_96);
      mTVRePlay.setText(mContext.getResources().getString(R.string.pip_playback_txt_exit));
      mBackTVPoint.setVisibility(View.GONE);
    }else{
      mImgRePlay.setBackground(null);
      mImgRePlay.setImageResource(R.drawable.restart_96);
      mTVRePlay.setText(mContext.getResources().getString(R.string.pip_playback_txt_replay));
      mBackTVPoint.setVisibility(View.GONE);
    }
    this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
    this.setFocusable(true);
    this.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    initOnKeyListener();
    initClickFocusListener();
    if (mNonePosition == 0) {
      initFocus(mRePlay);
    } else {
        if(isBackKeyCode){
            initFocus(mRePlay);
        }else{
            initFocus(mPlayContinue);
        }
    }
  }
  /**
   * description: 初始化获取焦点设置
   * @author fuqiang
   * @date 2017/12/20 下午5:33
   * @version 1.0
   * @param focusView
   */
  private void initFocus(View focusView) {
    focusView.setFocusable(true);
    focusView.requestFocus();
  }
  /**
   * description: onKey事件,View设置监听遥控器按键
   * @author fuqiang
   * @date 2017/12/20 下午5:33
   * @version 1.0
   * @param v
   * @param keyCode
   * @param event
   */
  @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
    if (event.getAction() == KeyEvent.ACTION_DOWN) {
      int id=v.getId();
      if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
         if(id==R.id.rl_continue){
           initFocus(mRePlay);
         }else if(id==R.id.rl_replay){
           initFocus(mBackTVPoint);
         }else if(id==R.id.rl_backtv_point){
           initFocus(mPlayDetail);
         }
        return true;
      }else if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
          if(id==R.id.rl_replay){
            initFocus(mPlayContinue);
          }else if(id==R.id.rl_backtv_point){
            initFocus(mRePlay);
          }else if(id==R.id.rl_detail){
            initFocus(mBackTVPoint);
          }
          return true;
      }
      if(id!=R.id.rl_detail){
        if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
          mHandler.sendEmptyMessage(ViewkeyCode.VIEW_KEY_CONTINUE_PLAY);
          fromUserDismiss=true;
          dismiss();
        }
      }
    }
    return false;
  }

  public interface OnKeyBackDismissListener{
    void onDismiss();
  }

  public void setOnKeyBackDismissListener(OnKeyBackDismissListener listener){
    this.mListener = listener;
  }
}