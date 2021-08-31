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
package com.pukka.ydepg.moudule.livetv.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.pukka.ydepg.R;
import com.pukka.ydepg.customui.focusView.ChannelLinearLayoutFocusView;
import com.pukka.ydepg.customui.focusView.TVGuideManagerView;
import com.pukka.ydepg.moudule.livetv.utils.LiveUtils;
import com.pukka.ydepg.moudule.player.node.CurrentChannelPlaybillInfo;
import java.util.List;

/**
 * 频道列表->频道Adapter
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: ChannelAdapter
 * @Package com.pukka.ydepg.moudule.livetv.adapter
 * @date 2017/12/26 14:13
 */
public class ChannelAdapter extends BaseAdapter<CurrentChannelPlaybillInfo,ChannelAdapter.ChannelHolder> {

  /**
   * true:显示频道名称;false:不显示频道名称
   */
  private boolean isShowChannelName;

  /**
   * true:显示右边方向键;false:不显示右边方向键
   */
  private boolean isShowRightIcon;

  /**
   * 请求是否有返回
   */
  private boolean hasDataResponse;

  /**
   * 频道页
   */
  private TVGuideManagerView mManagerView;

  private boolean isSpecialSubject = false;

  /**
   * 1代表展示频道号，非1代表不展示
   *  频道号非channelNo,而是频道集合的position，从1开始，同时切台的频道号按照此postion，并非之前的channelNo,(勿影响之前原有逻辑)。
   * */
  private String mIsShowChannelNo;

  public ChannelAdapter(Context context, List<CurrentChannelPlaybillInfo> list) {
    super(context, list);
    isShowChannelName= LiveUtils.isShowChannelName();
  }

  @Override protected ChannelHolder createViewHolder(View view) {
    return new ChannelHolder(view);
  }

  @Override protected int getAdapterLayoutId() {
    return R.layout.item_epgchannel;
  }

  public class ChannelHolder extends BaseHolder<CurrentChannelPlaybillInfo>{
    @BindView(R.id.ll_focusview) ChannelLinearLayoutFocusView foucsView;
    @BindView(R.id.tv_program_name) TextView mProgramName;
    @BindView(R.id.tv_channel_name) TextView mChannelName;
    @BindView(R.id.tv_channel_no) TextView mChannelNo;
    @BindView(R.id.iv_right) ImageView mRightIcon;

    public ChannelHolder(View itemView) {
      super(itemView);
    }
    @Override public void bindView(CurrentChannelPlaybillInfo info, int position) {
      //设置当前焦点的tag类型
      foucsView.setTag(TVGuideManagerView.ViewType.VIEW_CHANNEL);
      String programName=info.getChannelProgramName();
      if(TextUtils.isEmpty(programName)){
        //为空则为没有节目单
        programName=mProgramName.getContext().getString(R.string.channel_playbill_name_empty);
      }
      mProgramName.setText(programName);
      if(isShowChannelName){
        mChannelName.setVisibility(View.VISIBLE);
      }else{
        mChannelName.setVisibility(View.GONE);
      }

      if(isSpecialSubject) {
        if (TextUtils.equals(mIsShowChannelNo, "1")) {
          mChannelNo.setVisibility(View.VISIBLE);
          mChannelNo.setText(position + 1 + "");
        } else {
          mChannelNo.setVisibility(View.GONE);
          mChannelNo.setText("");
        }
      }
      else{
        mChannelNo.setVisibility(View.VISIBLE);
        mChannelNo.setText(info.getChannelNo());
      }

//      if (TextUtils.isEmpty(info.getChannelNo())){
//        mChannelNo.setVisibility(View.GONE);
//      }else{
//        if(isSpecialSubject){
//          if(TextUtils.equals(mIsShowChannelNo, "1")){
//            mChannelNo.setText(position + 1 + "");
//          }
//          else{
//            mChannelNo.setText("");
//          }
//        }
//        else {
//          mChannelNo.setText(info.getChannelNo());
//        }
//      }
      mChannelName.setText(info.getChannelName());
      //检查数据是否过期并且数据是否正在请求&数据是否返回了
      //参考帕克实现
      if(hasDataResponse && info.getRefreshDataTime()+ 1000 * 15 * 60 < System.currentTimeMillis()){
        if(null!=mManagerView){
          hasDataResponse=false;
          mManagerView.requestPlaybillContextByTime(position);
        }
      }

      //Modified by l00190891 for new requirement of 电视看点只保留直播
//      foucsView.setFocusListener(new ChannelLinearLayoutFocusView.ChangeListener() {
//        @Override public void onFocusChange(boolean hasFocus) {
//          if(isShowRightIcon){
//            mRightIcon.setVisibility(hasFocus?View.VISIBLE:View.GONE);
//          }else{
//            mRightIcon.setVisibility(View.GONE);
//          }
//        }
//      });
    }
  }

  /**
   * 更新右边方向按键状态
   * @param isShow true:显示;false:不显示
   */
  public void updateRightIconStatus(boolean isShow){
    isShowRightIcon=isShow;
  }

  /**
   * 频道页
   * @param managerView managerView
   */
  public void setManagerView(TVGuideManagerView managerView){
    this.mManagerView=managerView;
  }

  /**
   * 设置请求数据状态
   * @param hasResponse true:请求结果返回;false:正在请求
   */
  public void setRequestDataStatus(boolean hasResponse){
    hasDataResponse=hasResponse;
  }

  public void setSpecialSubject(boolean specialSubject) {
    isSpecialSubject = specialSubject;
  }

  public void setmIsShowChannelNo(String isShowChannelNo){
    mIsShowChannelNo = isShowChannelNo;
  }
}
