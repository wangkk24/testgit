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
package com.pukka.ydepg.customui.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.pukka.ydepg.R;

/**
 * 系统提醒对话框
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: ReminderDialog
 * @Package com.pukka.ydepg.customui.dialog
 * @date 2017/12/22 17:10
 */
public class ReminderDialog extends PopupWindow {
  private Context mContext;
  private TextView mTitleView;
  private TextView mDetailView;
  private TextView mOkView;
  private TextView mCancelView;
  private OnReminderConfirmListener mListener;

  public ReminderDialog(Context context, OnReminderConfirmListener listener) {
    this.mContext = context;
    this.mListener = listener;
    initView();
  }

  private void initView() {
    View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_reminder_layout, null);
    setContentView(view);
    setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
    setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    mTitleView = (TextView) view.findViewById(R.id.tv_dialog_title);
    mDetailView = (TextView) view.findViewById(R.id.tv_dialog_detail);
    mOkView = (TextView) view.findViewById(R.id.tv_dialog_ok);
    mCancelView = (TextView) view.findViewById(R.id.tv_dialog_cancel);
    mCancelView.setOnClickListener(v -> dismiss());
    mOkView.setOnClickListener(v -> {
      dismiss();
      //回调点击了确认按钮,交给调用方处理
      if (null != mListener) {
        mListener.onConfirm();
      }
    });
  }

  /**
   * 显示提醒框
   */
  public void showRemiderDialog(String msg, View parentView) {
    this.setOutsideTouchable(true);
    this.setBackgroundDrawable(new ColorDrawable());
    this.mOkView.setFocusable(true);
    //预先设置ok按钮先请求获取到焦点
    this.mOkView.requestFocus();
    //设置内容
    this.mDetailView.setText(msg);
    this.mDetailView.setTextColor(mContext.getResources().getColor(R.color.c21_color));
    //很关键,设置这个窗口可以获取焦点
    this.setFocusable(true);
    //展示
    this.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
  }

  public interface OnReminderConfirmListener {
    void onConfirm();
  }
}