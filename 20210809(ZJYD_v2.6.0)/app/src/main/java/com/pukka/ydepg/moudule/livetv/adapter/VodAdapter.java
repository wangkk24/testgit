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
package com.pukka.ydepg.moudule.livetv.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.customui.focusView.ChannelLinearLayoutFocusView;
import com.pukka.ydepg.customui.focusView.TVGuideManagerView;
import java.util.List;

/**
 * 频道页->VOD数据列表的Adapter
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: VodAdapter
 * @Package com.pukka.ydepg.moudule.livetv.adapter
 * @date 2018/01/09 14:10
 */
public class VodAdapter extends BaseAdapter<VOD,VodAdapter.VodHolder> {

  public VodAdapter(Context context, List<VOD> list) {
    super(context, list);
  }

  @Override protected VodHolder createViewHolder(View view) {
    return new VodHolder(view);
  }

  @Override protected int getAdapterLayoutId() {
    return R.layout.item_epgchannel;
  }

  public class VodHolder extends BaseHolder<VOD>{
    @BindView(R.id.ll_focusview) ChannelLinearLayoutFocusView foucsView;
    @BindView(R.id.tv_program_name) TextView mProgramName;
    @BindView(R.id.tv_channel_name) TextView mChannelName;
    @BindView(R.id.iv_right) ImageView mRightIcon;

    public VodHolder(View itemView) {
      super(itemView);
    }

    @Override public void bindView(VOD vod, int position) {
      mChannelName.setVisibility(View.GONE);
      foucsView.setTag(TVGuideManagerView.ViewType.VIEW_TVOD);
      mProgramName.setText(vod.getName());
      /*
        0: 非电视剧
        1: 普通连续剧
        2: 季播剧父集
        3: 季播剧
       */
      boolean isTVSeries=false;
      if(!vod.getVODType().equals(VOD.VODType.UN_TV_SERIES)){
        //当前是电视剧
        isTVSeries=true;
      }
      boolean finalIsTVSeries = isTVSeries;
      foucsView.setFocusListener(new ChannelLinearLayoutFocusView.ChangeListener() {
        @Override public void onFocusChange(boolean hasFocus) {
          if(finalIsTVSeries){
            mRightIcon.setVisibility(hasFocus?View.VISIBLE:View.GONE);
          }else{
            mRightIcon.setVisibility(View.GONE);
          }
        }
      });
    }
  }
}
