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
import android.widget.TextView;
import butterknife.BindView;
import com.pukka.ydepg.R;
import com.pukka.ydepg.customui.focusView.ChannelLinearLayoutFocusView;
import com.pukka.ydepg.customui.focusView.TVGuideManagerView;
import com.pukka.ydepg.moudule.player.node.Program;
import java.util.List;

/**
 * 频道页->节目单Adapter
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: ProgramAdapter
 * @Package com.pukka.ydepg.moudule.livetv.adapter
 * @date 2017/12/29 17:09
 */
public class ProgramAdapter extends BaseAdapter<Program,ProgramAdapter.ProgramHolder> {

  public ProgramAdapter(Context context, List<Program> list) {
    super(context, list);
  }

  @Override protected ProgramHolder createViewHolder(View view) {
    return new ProgramHolder(view);
  }

  @Override protected int getAdapterLayoutId() {
    return R.layout.item_epgprogram;
  }

  public class ProgramHolder extends BaseHolder<Program>{
    @BindView(R.id.ll_focusview) ChannelLinearLayoutFocusView foucsView;
    @BindView(R.id.tv_program_name) TextView mProgramName;

    public ProgramHolder(View itemView) {
      super(itemView);
    }

    @Override public void bindView(Program program, int position) {
      //设置当前焦点的tag类型
      foucsView.setTag(TVGuideManagerView.ViewType.VIEW_PROGRAM);
      String programName=program.getName();
      if(TextUtils.isEmpty(programName)){//没有节目单
        programName=mProgramName.getResources().getString(R.string.channel_playbill_name_empty);
      }
      mProgramName.setText(programName);
    }
  }
}
