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
package com.pukka.ydepg.moudule.catchup.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.TextUtil;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.customui.focusView.TextViewFocusLayout;
import com.pukka.ydepg.moudule.livetv.adapter.BaseAdapter;
import com.pukka.ydepg.moudule.livetv.adapter.BaseHolder;
import com.pukka.ydepg.moudule.player.node.Program;

import java.util.List;

import butterknife.BindView;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: TVODProgramListAdapter
 * @Package com.pukka.ydepg.moudule.catchup.adapter
 * @date 2018/09/22 14:04
 */
public class TVODProgramListAdapter extends BaseAdapter<Program,TVODProgramListAdapter.TVODProgramListHolder>{

    private String mViewType;

    public TVODProgramListAdapter(Context context,String viewType, List<Program> list) {
        super(context, list);
        this.mViewType=viewType;
    }

    @Override
    protected TVODProgramListHolder createViewHolder(View view) {
        return new TVODProgramListHolder(view);
    }

    @Override
    protected int getAdapterLayoutId() {
        return R.layout.item_tvod_program_list;
    }

    public class TVODProgramListHolder extends BaseHolder<Program> {
        @BindView(R.id.tv_playbill)
        TextViewFocusLayout mPlaybillInfo;

        public TVODProgramListHolder(View itemView) {
            super(itemView);
            itemView.setTag(mViewType);
        }

        @Override
        public void bindView(Program program, int position) {
            mPlaybillInfo.setText(DateCalendarUtils.formatDate(Long.parseLong(program.getStartTime()),"HH:mm")
            +"\u3000"+program.getName());
            mPlaybillInfo.setFocusCallback(mCallback);
        }
    }

    private TextViewFocusLayout.OnTextViewFocusCallback mCallback=(view, hasFocus)-> TextUtil.setMarqueen(hasFocus, (TextView) view);
}
