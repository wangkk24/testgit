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
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.customui.focusView.FrameLayoutFocusLayout;
import com.pukka.ydepg.moudule.livetv.adapter.BaseAdapter;
import com.pukka.ydepg.moudule.livetv.adapter.BaseHolder;
import java.util.List;
import butterknife.BindView;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: TVODProgramMenuItemListAdapter
 * @Package com.pukka.ydepg.moudule.catchup.adapter
 * @date 2018/09/21 10:55
 */
public class TVODProgramMenuItemListAdapter extends BaseAdapter<String,TVODProgramMenuItemListAdapter.MenuItemHolder> {

    private String mViewType;

    //true:item字体高亮，false:跟随焦点
    private boolean mIsSelect = true;


    public TVODProgramMenuItemListAdapter(Context context, String viewType,List<String> list) {
        super(context, list);
        this.mViewType=viewType;
    }

    @Override
    protected MenuItemHolder createViewHolder(View view) {
        return new MenuItemHolder(view);
    }

    @Override
    protected int getAdapterLayoutId() {
        return R.layout.item_tvod_column_item;
    }

    public void setIsSelect(boolean isSelect){
        this.mIsSelect = isSelect;
        notifyDataSetChanged();
    }

    public class MenuItemHolder extends BaseHolder<String> {
        @BindView(R.id.fm_container)
        FrameLayoutFocusLayout mFrameContainerLayout;
        @BindView(R.id.tv_content_name)
        TextView mContentName;

        public MenuItemHolder(View itemView) {
            super(itemView);
            itemView.setTag(mViewType);
        }

        @Override
        public void bindView(String value, int position) {
            mContentName.setSelected(false);
            mContentName.setText(value);
            mFrameContainerLayout.setCallback(mCallback);
        }
    }

    private FrameLayoutFocusLayout.FocusCallback mCallback = new FrameLayoutFocusLayout.FocusCallback(){
        @Override
        public void onFocusCallback(boolean hasFocus, FrameLayoutFocusLayout view) {
            ViewGroup.LayoutParams layoutParams=view.getLayoutParams();
            TextView contentName = (TextView) view.findViewById(R.id.tv_content_name);
            layoutParams.height= OTTApplication.getContext().getResources().getDimensionPixelSize(R.dimen.tVodProgramList_MenuItem_ItemHeight_NoSelect);
            if(hasFocus){
                view.setBackgroundResource(R.drawable.shape_white_frame);
                contentName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            }else{
                view.setBackground(null);
                contentName.setEllipsize(TextUtils.TruncateAt.END);
            }
            view.setLayoutParams(layoutParams);
            if (mIsSelect){
                contentName.setSelected(true);
                mIsSelect = false;
            }else{
                contentName.setSelected(hasFocus);
            }
        }
    };
}