package com.pukka.ydepg.customui.adpter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ld on 2018/1/22.
 */
public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerHolder> {

    protected List<T> datas;
    private final int mItemLayoutId;
    protected Context mContext;
    private OnItemClickListener onItemClickListener;
    protected OnKeyDownListener onKeyDownListener;
    private OnItemSelectListener mSelectListener;
    private onTouchListener onTouchListener;
    private boolean isBaseItemClick = true;//是否使用父类(当前类)的点击事件，默认使用
    private boolean isBaseKey = false;//是否使用父类(当前类)的按键事件，默认不使用
    private RecyclerView mRrecyclerVew;
    protected String currentSitNum;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                if(mRrecyclerVew != null){
                    if(mRrecyclerVew.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && !mRrecyclerVew.isComputingLayout()){
                        notifyDataSetChanged();
                    }else{
                        sendEmptyMessageDelayed(1,50);
                    }
                }
            }
        }
    };

    public CommonRecyclerAdapter(RecyclerView v, List<T> datas, int itemLayoutId) {
        if (datas == null) {
            this.datas = new ArrayList<T>();
        } else {
            this.datas = datas;
        }
        mItemLayoutId = itemLayoutId;
        mContext = v.getContext();
        mRrecyclerVew = v;
    }

    /**
     * Recycler适配器填充方法
     *
     * @param helper viewholder
     * @param item   javabean
     *               param isScrolling RecyclerView是否正在滚动
     */
    public abstract void convert(RecyclerHolder helper, T item, int position/*, boolean isScrolling*/);

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View root = inflater.inflate(mItemLayoutId, parent, false);
        return new RecyclerHolder(root);
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, final int position) {
        convert(holder, datas.get(position), position/*, isScrolling*/);
        if (isBaseItemClick) {
            holder.itemView.setOnClickListener(getOnClickListener(position));
            holder.itemView.setOnTouchListener(getOnTouchListener(position));
            holder.itemView.setOnFocusChangeListener(getItemSeletedListen(position));
        }
        if (isBaseKey) {
            holder.itemView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (onKeyDownListener != null) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            return onKeyDownListener.onKeyDown(v, keyCode, event, position);
                        }
                        if (event.getAction() == KeyEvent.ACTION_UP) {
                            return onKeyDownListener.onKeyUp(v, keyCode, event, position);
                        }
                        return false;
                    } else {
                        return false;
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (datas != null) {
            count = datas.size();
        }
        return count;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        onItemClickListener = l;
    }

    public void setOnKeyDownListener(OnKeyDownListener l){
        onKeyDownListener = l;
    }

    public View.OnClickListener getOnClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(@Nullable View v) {
                if (onItemClickListener != null && v != null) {
                    onItemClickListener.onItemClick(v, datas.get(position), position);
                }
            }
        };
    }

    public View.OnTouchListener getOnTouchListener(final int position) {
        return new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (null != onTouchListener && view != null) {
                    onTouchListener.onTouchListener(view, datas.get(position), position, motionEvent);
                }
                return false;
            }
        };
    }

    public View.OnFocusChangeListener getItemSeletedListen(final int position) {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (null != mSelectListener && v != null) {
                    if (hasFocus) {
                        if (datas.size() > 0) {
                            mSelectListener.onItemSelect(v, datas.get(position), position);
                        }
                    } else {
                        mSelectListener.onUnFocusSelect(v);
                    }
                }
            }
        };
    }

    /**
     * 设置数据 objects为null则不设置
     * 若想清空数据请调用clearAll();
     *
     * @param objects
     */
    public void setDatas(List<T> objects) {
        this.datas = new ArrayList<>();
        if (objects != null) {
            this.datas.addAll(objects);
        }
        mHandler.sendEmptyMessage(1);
    }

    /**
     * 添加数据 若addData为null 则不添加
     *
     * @param addData
     */
    public void addData(T addData) {
        if (addData != null) {
            if (datas == null) {
                datas = new ArrayList<T>();
            }
            this.datas.add(addData);
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Object data, int position);
    }

    public interface onTouchListener {
        void onTouchListener(View view, Object data, int position, MotionEvent event);
    }

    public interface OnItemSelectListener {
        void onItemSelect(View view, Object data, int position);

        void onUnFocusSelect(View view);
    }

    public interface OnKeyDownListener {
        boolean onKeyDown(View v, int keyCode, KeyEvent event, int position);

        boolean onKeyUp(View v, int keyCode, KeyEvent event, int position);
    }

    public void setCurrentSitNum(String currentSitNum) {
        this.currentSitNum = currentSitNum;
    }
}