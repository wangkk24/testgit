package com.pukka.ydepg.moudule.children.addpter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.TextViewExt;
import com.pukka.ydepg.customui.focusView.FrameLayoutFocusLayout;
import com.pukka.ydepg.moudule.livetv.adapter.BaseAdapter;
import com.pukka.ydepg.moudule.livetv.adapter.BaseHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Eason on 28-Mar-19.
 * 家长中心选择单次Or总观看时间Adapter
 */

public class ParentSetCenterDurationAdapter extends BaseAdapter<String, ParentSetCenterDurationAdapter.DurationItemHold> {

    private Context mContext;
    //默认焦点
    private int mDefaulFocus = -1;
    //类型
    private int mViewType;

    private List<String> mList;

    public ParentSetCenterDurationAdapter(Context context, List<String> list, int viewType,int currentIndex) {
        super(context, list);
        this.mContext = context;
        this.mViewType = viewType;
        this.mList = list;
        this.mDefaulFocus = currentIndex;
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    //设置默认焦点
    public void setDefaulFocus(int defaulFocus) {
        this.mDefaulFocus = defaulFocus;
    }

    @Override
    protected DurationItemHold createViewHolder(View view) {
        return new DurationItemHold(view);
    }

    @Override
    protected int getAdapterLayoutId() {
        return R.layout.dialog_parent_duration_item_adapter;
    }

    public class DurationItemHold extends BaseHolder<String> {
        @BindView(R.id.fm_container)
        FrameLayoutFocusLayout mFrameContainerLayout;
        @BindView(R.id.tv_duration)
        TextViewExt tvDuration;
        View itemView;

        public DurationItemHold(View itemView) {
            super(itemView);
            itemView.setTag(mViewType);
            this.itemView = itemView;
        }

        @Override
        public void bindView(String value, int position) {
            if (position == mDefaulFocus) {
                mDefaulFocus = -1;
                itemView.postDelayed(() -> {
                    itemView.requestFocus();
                    tvDuration.setTextColor(mContext.getResources().getColor(R.color.caff_color));
                }, 200);
            }
            tvDuration.setText(value);
            mFrameContainerLayout.setCallback(mCallback);
        }
    }

    private FrameLayoutFocusLayout.FocusCallback mCallback = new FrameLayoutFocusLayout.FocusCallback() {
        @Override
        public void onFocusCallback(boolean hasFocus, FrameLayoutFocusLayout view) {
            TextView tvDuration = (TextView) view.findViewById(R.id.tv_duration);
            if (hasFocus) {
                tvDuration.setTextColor(mContext.getResources().getColor(R.color.caff_color));
            } else {
                tvDuration.setTextColor(mContext.getResources().getColor(R.color.white_0));
            }
        }
    };

}
