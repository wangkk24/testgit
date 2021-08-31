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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseAdapter
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: BaseAdapter
 * @Package com.pukka.ydepg.moudule.livetv.adapter
 * @date 2017/12/25 15:12
 */
public abstract class BaseAdapter<T, VH extends BaseHolder> extends RecyclerView.Adapter<VH> {

    private static final String TAG = "BaseAdapter";

    private Context mContext;

    private List<T> mDataList;

    private OnItemListener mItemListener;

    /**
     * 记录第一位的holder
     */
    private VH mFirstHolder;

    /**
     * 记录当前保存焦点的位置
     */
    private int mRecordFocusPosition;

    /**
     * 点击DPAD_UP是否可以有响应
     */
    private boolean isCanUp;
    /**
     * 点击DPAD_DOWN是否可以有响应
     */
    private boolean isCanDown;

    private OnItemkeyListener mOnItemkeyListener;

    public BaseAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.mDataList = list;
        this.mRecordFocusPosition = 0;
        setHasStableIds(true);
    }

    /**
     * 是否能够继续下上滚动
     */
    public boolean isCanUp() {
        updateCanUpDownStatus(mRecordFocusPosition);
        return isCanUp;
    }

    /**
     * 是否能够继续往下翻页
     */
    public boolean isCanDown() {
        updateCanUpDownStatus(mRecordFocusPosition);
        return isCanDown;
    }

    /**
     * 是否可以加载下一页数据
     */
    public boolean hasPreLoad() {
        int itemCount = getItemCount();
        return itemCount >= 14 && mRecordFocusPosition + 5 >= itemCount;
    }

    /**
     * 返回记录的焦点View的位置
     */
    public int getRecordFocusPosition() {
        return mRecordFocusPosition;
    }

    /**
     * 按需自己设置焦点view的位置
     * @param position
     */
    public void setRecordFocusPosition(int position){
        this.mRecordFocusPosition=position;
    }

    /**
     * 监听item选中以及点击的回调监听,目的是为了知道当前的position
     */
    public void setOnItemListener(OnItemListener itemListener) {
        this.mItemListener = itemListener;
    }

    /**
     * 监听item选中以及点击的回调监听,目的是为了知道当前的position
     */
    public void setOnItemKeyListener(OnItemkeyListener itemListener) {
        this.mOnItemkeyListener = itemListener;
    }
    @Override
    public int getItemCount() {
        return (null != mDataList) ? mDataList.size() : 0;
    }

    /**
     * 绑定数据
     *
     * @param list 将要添加的数据集合
     */
    public void bindData(List<T> list) {
        if (null == list) return;
        int itemSize = mDataList.size();
        int size = list.size();
        mDataList.addAll(list);
        if (itemSize == 0) {
            notifyDataSetChanged();
        } else {
            //防止焦点丢失
            notifyItemRangeInserted(mDataList.size() - size, size);
        }
    }

    @Override
    public long getItemId(int position) {
        return position + 800L;
    }

    /**
     * 清除全部数据,刷新adapter
     */
    public void clearAll() {
        if (null == mDataList) {
            mDataList = new ArrayList<>();
        }
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 通过item的position获取item位置的数据对象
     *
     * @param position position
     */
    public T getItemPosition(int position) {
        if (null != mDataList && position <= getItemCount() - 1) {
            return mDataList.get(position);
        }
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(getAdapterLayoutId(), parent, false);
        return createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bindView(mDataList.size() > 0 ? mDataList.get(position % mDataList.size()) : "", position);
        if (position == 0 && null == mFirstHolder) {
            mFirstHolder = holder;
        }
        //item焦点变化回调
        holder.itemView.setOnFocusChangeListener((v, hasFocus) -> {
            onFocusChange(v,hasFocus);
            if (hasFocus) {
                //记录焦点的View的位置
                mRecordFocusPosition = position;
                int totalCount = getItemCount();
                //fixed size==1
                if (totalCount == 1) {
                    isCanDown = false;
                    isCanUp = false;
                } else {
                    updateCanUpDownStatus(position);
                }
                ////是否可以预加载数据
                //hasPreLoad=(position+5>=totalCount);
                //item选中回调
                if (null != mItemListener && totalCount > 0) {
                    mItemListener.onItemSelect(String.valueOf(holder.itemView.getTag()), position);
                }
            }
        });
        //DPAD_CENTER,点击事件
        holder.itemView.setOnClickListener(v -> {
            if (null != mItemListener) {
                mItemListener.onItemClickListener(String.valueOf(holder.itemView.getTag()), position);
            }
        });
        holder.itemView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (null != mOnItemkeyListener) {
                    return mOnItemkeyListener.onItemkeyListener(keyCode, event, position,String.valueOf(holder.itemView.getTag()));
                }
                return false;
            }
        });
    }

    public void onFocusChange(View v,boolean focus){

    }

    /**
     * 记录position=0的holder
     *
     * @return
     */
    public VH getFirstHolder() {
        return mFirstHolder;
    }

    /**
     * 判断DPAD上下按钮能否继续点击
     *
     * @param position
     */
    private void updateCanUpDownStatus(int position) {
        if (getItemCount() == 1) {
            isCanDown = false;
            isCanUp = false;
        } else {
            if (position == 0) {
                isCanUp = false;
                isCanDown = true;
            } else {
                isCanUp = true;
                if (position == getItemCount() - 1) {
                    isCanDown = false;
                } else {
                    isCanDown = true;
                }
            }
        }
    }

    protected abstract VH createViewHolder(View view);

    protected abstract int getAdapterLayoutId();

    public interface OnItemListener {
        /**
         * Item选中回调
         *
         * @param viewType
         * @param position
         */
        void onItemSelect(String viewType, int position);

        /**
         * Item点击回调
         *
         * @param viewType
         * @param position
         */
        void onItemClickListener(String viewType, int position);
    }

    public interface OnItemkeyListener {
        /**
         * Item点击回调
         *

         * @param position
         */
        boolean onItemkeyListener(int keyCode, KeyEvent event, int position,String viewType);
    }
}
