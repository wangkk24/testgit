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
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.customui.focusView.LinearLayoutFoucsView;
import com.pukka.ydepg.customui.focusView.TVGuideManagerView;
import java.util.List;

/**
 * 频道页->栏目的Adapter
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: ColumnAdapter
 * @Package com.pukka.ydepg.moudule.livetv.adapter
 * @date 2017/12/25 15:36
 */
public class ColumnAdapter extends BaseAdapter<Subject,ColumnAdapter.ColumnHolder> {

  public ColumnAdapter(Context context, List<Subject> list) {
    super(context, list);
  }

  @Override protected ColumnHolder createViewHolder(View view) {
    return new ColumnHolder(view);
  }

  @Override protected int getAdapterLayoutId() {
    return R.layout.item_epgcolumn;
  }

  public class ColumnHolder extends BaseHolder<Subject>{
      @BindView(R.id.ll_focusview) LinearLayoutFoucsView foucsView;
      @BindView(R.id.tv_content)
      TextView mContent;
      public ColumnHolder(View itemView) {
        super(itemView);
      }

    @Override public void bindView(Subject subject, int position) {
      //设置当前焦点的tag类型
      foucsView.setTag(TVGuideManagerView.ViewType.VIEW_COLUMN);
      if(!TextUtils.isEmpty(subject.getName())){
        mContent.setText(subject.getName());
      }
    }
  }
}
