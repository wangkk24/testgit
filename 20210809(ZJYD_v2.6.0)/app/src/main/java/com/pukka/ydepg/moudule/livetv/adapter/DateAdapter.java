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
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.pukka.ydepg.R;
import com.pukka.ydepg.customui.focusView.LinearLayoutFoucsView;
import com.pukka.ydepg.customui.focusView.TVGuideManagerView;
import java.util.List;

/**
 * 频道页->日期Adapter
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: DateAdapter
 * @Package com.pukka.ydepg.moudule.livetv.adapter
 * @date 2017/12/29 14:23
 */
public class DateAdapter extends BaseAdapter<String,DateAdapter.DateHolder> {

  public DateAdapter(Context context, List<String> list) {
    super(context, list);
  }

  @Override protected DateHolder createViewHolder(View view) {
    return new DateHolder(view);
  }

  @Override protected int getAdapterLayoutId() {
    return R.layout.item_epgdate;
  }

  public class DateHolder extends BaseHolder<String>{
    @BindView(R.id.ll_focusview) LinearLayoutFoucsView foucsView;
    @BindView(R.id.tv_content) TextView mContent;
    public DateHolder(View itemView) {
      super(itemView);
    }
    @Override public void bindView(String s, int position) {
      //设置当前焦点的tag类型
      foucsView.setTag(TVGuideManagerView.ViewType.VIEW_DATE);
      if(position==0){
        //今天
        mContent.setText(mContent.getContext().getString(R.string.epglist_date_today));
      }else{
        //后7天数据
        mContent.setText(parseDateValue(s));
      }
    }
  }

  /**
   * 解析成epg展示给用户的日期格式
   * @param dateValue
   * @return
   */
  private String parseDateValue(String dateValue){
    String subDateValue = dateValue.substring(dateValue.length() - 5, dateValue.length());
    String dateMonthValue = subDateValue.replace("-", "月");
    return  dateMonthValue + "日";
  }
}
