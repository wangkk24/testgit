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

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;

/**
 * BaseHodler
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: BaseHolder
 * @Package com.pukka.ydepg.moudule.livetv.adapter
 * @date 2017/12/25 15:15
 */
public abstract class BaseHolder<T> extends RecyclerView.ViewHolder {
  public BaseHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this,itemView);
    itemView.setFocusable(true);
    itemView.setFocusableInTouchMode(true);
  }
  public abstract void bindView(T t,int position);
}
