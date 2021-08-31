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

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.pukka.ydepg.R;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * TVGUIDE页
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: TVGuidePopwindow
 * @Package com.pukka.ydepg.customui
 * @date 2017/12/25 10:59
 */
public final class TVGuidePopwindow extends PopupWindow implements PopupWindow.OnDismissListener {
  private WindowDismissListener mListener;

  public TVGuidePopwindow(RxAppCompatActivity rxAppCompatActivity, WindowDismissListener listener, String subjectID, String mIsQuerySubject, String mIsShowChannelNo, String currentChildSubjectID) {
    mListener = listener;
    View view = LayoutInflater.from(rxAppCompatActivity).inflate(R.layout.window_pip_tvguide, null);
    TVGuideManagerView managerView = (TVGuideManagerView) view.findViewById(R.id.epg_managerview);
    managerView.setSubjectParam(subjectID, mIsQuerySubject, mIsShowChannelNo, currentChildSubjectID);
    managerView.setRxAppCompatActivity(rxAppCompatActivity);
    setContentView(view);
      setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
      setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    //dialog销毁的回调监听
    setOnDismissListener(this);
  }

  /**
   * 展示popwindow
   */
  public final void show(View parentView) {
      this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
      this.setFocusable(true);
      this.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
  }

    @Override
    public void onDismiss() {
    if (null != mListener) {
      mListener.onDismiss();
    }
  }

  public interface WindowDismissListener {
    void onDismiss();
  }

}