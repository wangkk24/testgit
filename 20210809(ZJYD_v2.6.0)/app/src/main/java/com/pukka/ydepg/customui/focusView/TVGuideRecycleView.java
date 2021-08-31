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
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.customui.CommonTVRecycleView;
import com.pukka.ydepg.event.ResponseKeyEvent;
import com.pukka.ydepg.moudule.livetv.adapter.BaseAdapter;
import com.pukka.ydepg.moudule.livetv.adapter.BaseHolder;

import org.greenrobot.eventbus.EventBus;

/**
 *
 * 控制了adapter中记录的焦点获取以及记录的焦点触发方法等
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: CustomTVRecycleView
 * @Package com.pukka.ydepg.customui
 * @date 2017/12/27 09:18
 */
public class TVGuideRecycleView extends CommonTVRecycleView {
  //TAG常量
  private static final String TAG = "CustomTVRecycleView";
  //禁止快速点击时间间隔
  private static final long SPACETIME=400;
  //刷新焦点延时时间
  private static final long DELAY_INVILIDATE=40;
  //记录最新的点击时间
  private long mLastClickTime;
  ////布局样式
  //private int layoutStyle;
  //spancount
  //private int spanCount;
  //是否自动测量
  private boolean autoMeasureEnable;
  //keyevent拦截器
  private InteceptorListener mInteceptor;

  public void setInteceptor(InteceptorListener inteceptor) {
    this.mInteceptor = inteceptor;
  }

  public interface InteceptorListener {
    boolean onInterceptorFocus(int keycode, String viewType);
  }

  public TVGuideRecycleView(Context context) {
    this(context, null);
  }

  public TVGuideRecycleView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TVGuideRecycleView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTVRecyclerView);

    //layoutStyle = typedArray.getInt(R.styleable.CustomTVRecyclerView_layout_style, 0);
    //spanCount = typedArray.getInteger(R.styleable.CustomTVRecyclerView_spanCount, 2);
    autoMeasureEnable =
        typedArray.getBoolean(R.styleable.CustomTVRecyclerView_autoMeasureEnable, false);
    typedArray.recycle();
    setLayoutStyle();
    setAutoMeasureEnable(autoMeasureEnable);

    //去除动画
    setItemAnimator(null);
  }

  public void setLayoutStyle() {
    LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    setLayoutManager(layoutManager);
  }

  public void setAutoMeasureEnable(boolean autoMeasureEnable) {
    getLayoutManager().setAutoMeasureEnabled(autoMeasureEnable);
  }

  /**
   * 根据位置获取recycleview,itemview
   * @param position 位置
   */
  public final View findItemView(int position){
    LinearLayoutManager layoutManager= (LinearLayoutManager) getLayoutManager();
    layoutManager.scrollToPositionWithOffset(position , 20);
    return layoutManager.findViewByPosition(position);
  }

  /**
   * 设置recycleview adapter内部对应位置的item view获取焦点
   */
  public final View itemViewRequestFocus(int position) {
    LinearLayoutManager layoutManager= (LinearLayoutManager) getLayoutManager();
    if(layoutManager.findViewByPosition(position) == null) {
      layoutManager.scrollToPositionWithOffset(position, 20);
    }
    postDelayed(() -> {
      //设置焦点
      setFocusView(layoutManager.findViewByPosition(position));
    }, DELAY_INVILIDATE);
    return layoutManager.findViewByPosition(position);
  }

  @Override public boolean dispatchKeyEvent(KeyEvent event) {
    int action=event.getAction();
    int keycode=event.getKeyCode();
    //DOWN和UP都会走,拦截UP事件
    if (action == KeyEvent.ACTION_UP
        && keycode!=KeyEvent.KEYCODE_DPAD_CENTER
        && keycode!=KeyEvent.KEYCODE_ENTER) {
      return false;
    }
    //做延时处理,防止焦点丢失乱窜
    if (action == KeyEvent.ACTION_DOWN) {
      long currenTime = System.currentTimeMillis();
      if (currenTime - mLastClickTime <= SPACETIME) {
        return true;
      }
      mLastClickTime = currenTime;
    }
    EventBus.getDefault().post(new ResponseKeyEvent());
    if (keycode == KeyEvent.KEYCODE_DPAD_DOWN && !isCanDown()) {
      //到底部了,要回到顶部
      setFocusStart();
      return true;
    } else if (keycode == KeyEvent.KEYCODE_DPAD_UP && !isCanUp()) {
      //到顶部了,要回到底部
      setFocusEnd();
      return true;
    }
    else if(keycode == KeyEvent.KEYCODE_DPAD_DOWN){
      scrollToPosition(((BaseAdapter)getAdapter()).getRecordFocusPosition() + 1);
    }
    else if(keycode == KeyEvent.KEYCODE_DPAD_UP){
      scrollToPosition(((BaseAdapter)getAdapter()).getRecordFocusPosition() - 1);
    }
    int childCount = getChildCount();
    if (childCount > 0
        && null != mInteceptor
        && keycode != KeyEvent.KEYCODE_DPAD_CENTER
        && keycode != KeyEvent.KEYCODE_ENTER) {
      BaseLinearFocusView focusView = (BaseLinearFocusView) getChildAt(0);
      //获取当前是哪个类型的条目同时需要传入code
      return mInteceptor.onInterceptorFocus(event.getKeyCode(), String.valueOf(focusView.getTag()));
    }
    return super.dispatchKeyEvent(event);
  }

  /**
   * 设置滚动到顶部获取焦点
   */
  private void setFocusStart() {
    scrollToPosition(0);
    postDelayed(() -> {
      //设置焦点
      setFocusView(getLayoutManager().findViewByPosition(0));
    }, DELAY_INVILIDATE);
  }

  /**
   * 设置滚动到底部获取焦点
   */
  private void setFocusEnd(){
    LinearLayoutManager layoutManager= (LinearLayoutManager) getLayoutManager();
    final int totalCount = layoutManager.getItemCount();
    layoutManager.scrollToPositionWithOffset(totalCount - 1 , 20);
    postDelayed(() -> {
      //设置焦点
      setFocusView(layoutManager.findViewByPosition(totalCount - 1));
    }, DELAY_INVILIDATE);
  }

  /**
   * 设置view获取焦点
   * @param view 焦点的view
   */
  private void setFocusView(View view) {
    if(null==view)return;
    view.setFocusable(true);
    view.setFocusableInTouchMode(true);
    view.requestFocus();
  }

  /**
   * 返回RecycleView中记录的焦点view
   */
  public BaseLinearFocusView getRecordFocusView() {
    BaseAdapter baseAdapter = (BaseAdapter) getAdapter();
    int position=baseAdapter.getRecordFocusPosition();
    ViewHolder holder=findViewHolderForLayoutPosition(position);
    if(null!=holder && null!=holder.itemView){
      return (BaseLinearFocusView) holder.itemView.findViewById(R.id.ll_focusview);
    }else{
      BaseHolder baseHolder=((BaseAdapter)getAdapter()).getFirstHolder();
      if(null!=baseHolder && null!=baseHolder.itemView){
        return (BaseLinearFocusView) baseHolder.itemView.findViewById(R.id.ll_focusview);
      }
    }
    return null;
  }

  /**
   * 让记录的焦点view获取焦点
   */
  public boolean recordViewRequestFocus() {
    BaseLinearFocusView baseLinearFocusView=getRecordFocusView();
    if (null != baseLinearFocusView) {
      postDelayed(() -> {
        //设置焦点
        setFocusView(baseLinearFocusView);
      }, DELAY_INVILIDATE);
      return true;
    } else {
      SuperLog.debug(TAG, "CustomTVRecycleView -> getRecordFocusView() == null");
    }
    return false;
  }

  /**
   * 更新当前条目记录的焦点view的状态含颜色背景
   * =======
   * 触发操作:
   * 从当前条目通过DPAD_RIGHT移动到别的条目列表中,
   * 需要更新上一个条目焦点位置的状态,虽然已经失去焦点,还是要标记住是从哪过来的以及恢复时使用。
   */
  public void switchRecordViewBackground() {
    BaseLinearFocusView focusView=getRecordFocusView();
    if (null != focusView) {
      if(focusView instanceof ChannelLinearLayoutFocusView){
        ((ChannelLinearLayoutFocusView) focusView).updateRecordViewStatus(false);
      }else{
        ((LinearLayoutFoucsView) focusView).updateRecordViewStatus(false);
      }
    } else {
      SuperLog.debug(TAG, "CustomTVRecycleView -> getRecordFocusView() == null");
    }
  }

  /**
   * 是否到达顶部
   */
  private boolean isCanUp() {
    BaseAdapter baseAdapter = (BaseAdapter) getAdapter();
    return baseAdapter.isCanUp();
  }

  /**
   * 是否到达底部
   */
  private boolean isCanDown() {
    BaseAdapter baseAdapter = (BaseAdapter) getAdapter();
    return baseAdapter.isCanDown();
  }
  /**
   * 是否滑动到可以预加载数据了
   */
  public boolean hasPreLoad(){
    BaseAdapter baseAdapter = (BaseAdapter) getAdapter();
    return baseAdapter.hasPreLoad();
  }

  /**
   * 装饰器
   */
  public static final class CustomItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace = -1;

    public CustomItemDecoration(int space) {
      this.mSpace = space;
    }

    @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
        RecyclerView.State state) {
      if (mSpace != -1) {
        outRect.bottom = mSpace;
      }
    }
  }
}
