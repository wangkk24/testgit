package com.pukka.ydepg.moudule.mytv.adapter;

import android.app.Activity;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscription;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;

import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by hasee on 2017/8/24.
 */

public class OrderedListAdapter extends RecyclerView.Adapter<OrderedListAdapter.ViewHolder> {

    private static final String TAG = "OrderedListAdapter";
    private final Activity mContext;
    private final RecyclerView mRecyclerView;
    private List<Subscription> mSubscriptionList;
    private OnItemSelectedListener mOnItemSelectedListener;
    private onUnSubscribedListener unSubscribedListener;
    private boolean isSelectUnSubscribed = false;

    private DecimalFormat decimalFormat;

    private final String cancel_subscriber="cancel_subscriber";

    private final String play="play";

    //重复点击最后时间
    private long mLastOnClickTime = 0;
    //重复点击时间
    private static final long VALID_TIME = 500;

    //private View selectedView;
    private int selectPostion = 0;

    public int getSelectPostion() {
        return selectPostion;
    }

    public void setSelectPostion(int selectPostion) {
        this.selectPostion = selectPostion;
    }

    public OrderedListAdapter(RecyclerView recyclerView, List<Subscription> subscriptionList, Activity context) {
        mSubscriptionList = subscriptionList;
        mContext = context;
        mRecyclerView = recyclerView;
        decimalFormat = new DecimalFormat("0.00");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ordered_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        holder.itemView.setOnFocusChangeListener(mOnFocusChangeListener);
        Subscription subscription = mSubscriptionList.get(position);
        String productName = subscription.getProductName();
        StringBuffer namesb = new StringBuffer();
        if (TextUtils.equals(subscription.getProductType(), Subscription.ProductType.BY_TIMES)) {
            namesb.append(TextUtils.isEmpty(productName.trim()) ? mContext.getResources().getString(R.string.mytv_order_unknown) : productName.trim());
            if (null != subscription.getPriceObjectDetail() && null != subscription.getPriceObjectDetail().getVOD()) {
                namesb.append(" " + subscription.getPriceObjectDetail().getVOD().getName());
            }
        } else if("0".equals(subscription.getIsMainPackage())){
            namesb.append(TextUtils.isEmpty(productName.trim()) ? mContext.getResources().getString(R.string.mytv_order_unknown) : productName.trim());
            if(!TextUtils.isEmpty(subscription.getPrice())) {
                double price = Double.parseDouble(subscription.getPrice()) / 100;
                String priceStr=decimalFormat.format(price);
                if(priceStr.contains(".00")){
                    priceStr=priceStr.substring(0,priceStr.indexOf("."));
                }
                namesb.append(priceStr+ "元/" + StringUtils.analyticValiditySubscription(subscription));
            }
        }else{
            namesb.append(TextUtils.isEmpty(productName.trim()) ? mContext.getResources().getString(R.string.mytv_order_unknown) : productName.trim());
        }
        holder.name.setText(namesb.toString());
        String price = subscription.getPrice();
        if (!TextUtils.isEmpty(price)) {
            holder.price.setText(String.format(mContext.getResources().getString(R.string.order_list_item_price), price));
        } else {
            holder.price.setText("");
        }
        String startTime = subscription.getStartTime();
        String endTime = subscription.getEndTime();
        StringBuffer sb = new StringBuffer();
//        if (!TextUtils.equals(subscription.getProductType(), Subscription.ProductType.BY_TIMES)&&"0".equals(subscription.getIsMainPackage())) {
//            sb.append(TextUtils.isEmpty(subscription.getPeriodLength())?"":subscription.getPeriodLength()+"个"+StringUtils.getValidtime(subscription.getChargeMode())+"，");
//            if (!TextUtils.isEmpty(endTime)) {
//                sb.append( DateCalendarUtils.formatDate(Long.parseLong(endTime), "dd.MM.yyyy HH:mm"));
//            } else {
//                sb.append(mContext.getResources().getString(R.string.mytv_order_unknown));
//            }
//        } else {
            if (!TextUtils.isEmpty(startTime)) {
                sb.append(DateCalendarUtils.formatDate(Long.parseLong(startTime), "yyyy.MM.dd HH:mm"));
            } else {
                sb.append(mContext.getResources().getString(R.string.mytv_order_unknown));
            }
            if (!TextUtils.isEmpty(endTime)) {
                sb.append(" - " + DateCalendarUtils.formatDate(Long.parseLong(endTime), "yyyy.MM.dd HH:mm"));
            } else {
                sb.append(" - " + mContext.getResources().getString(R.string.mytv_order_unknown));
            }
//        }
        holder.time.setText(sb.toString());
        boolean isCancelSubscribed = isCancelSubscribed(subscription.getIsAutoExtend());
        SuperLog.debug(TAG, "isCancelSubscribed=" + isCancelSubscribed);
        if(subscription.getIsMainPackage().equals("0")) {
            if (isCancelSubscribed&&subscription.getProductType().equals("0")) {
                holder.unSubscirberLayout.setTag(cancel_subscriber);
                holder.imageUnsubscriber.setImageDrawable(mContext.getResources().getDrawable(R.drawable.order_unsubscribe));
            } else if (subscription.getProductType().equals("1"))
            {
                holder.unSubscirberLayout.setTag(play);
                holder.imageUnsubscriber.setImageDrawable(mContext.getResources().getDrawable(R.drawable.subscriber_play));
            }else{
                holder.unSubscirberLayout.setTag(null);
            }
        }else{
            holder.unSubscirberLayout.setTag(null);
        }
        holder.unSubscirberLayout.setBackground(ContextCompat.getDrawable(mContext, R
                    .drawable.transparent_drawable));
        holder.listItemLayout.setBackground(ContextCompat.getDrawable(mContext, R
                .drawable.transparent_drawable));
        if (position == selectPostion) {
            if (holder.unSubscirberLayout.getVisibility() == View.VISIBLE) {
                holder.unSubscirberLayout.setBackground(ContextCompat.getDrawable(mContext, R
                        .drawable.mytv_item_bg));
            }
            holder.listItemLayout.setBackground(ContextCompat.getDrawable(mContext, R
                    .drawable.mytv_item_bg_foucs_select));
        }
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.itemLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_LEFT://向左

                            if (holder.unSubscirberLayout.getVisibility() == View.VISIBLE) {
                                if(isSelectUnSubscribed){
                                    isSelectUnSubscribed = false;
                                    holder.unSubscirberLayout.setBackground(ContextCompat.getDrawable(mContext, R
                                            .drawable.mytv_item_bg));
                                    holder.listItemLayout.setBackground(ContextCompat.getDrawable(mContext, R
                                            .drawable.mytv_item_bg_foucs_select));
                                }else{
                                    mContext.finish();
                                }
                            }else{
                                mContext.finish();
                            }
                            break;
                        case KeyEvent.KEYCODE_DPAD_RIGHT://向右
                            //选中删除
                            if (holder.unSubscirberLayout.getVisibility() == View.VISIBLE) {
                                isSelectUnSubscribed = true;
                                holder.listItemLayout.setBackground(ContextCompat.getDrawable(mContext, R
                                        .drawable.mytv_item_bg));
                                holder.unSubscirberLayout.setBackground(ContextCompat.getDrawable(mContext, R
                                        .drawable.mytv_item_bg_foucs_select));
                            }
                            break;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == mSubscriptionList ? 0 : mSubscriptionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        TextView time;
        RelativeLayout itemLayout;
        RelativeLayout unSubscirberLayout;
        RelativeLayout listItemLayout;
        ImageView imageUnsubscriber;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.ordered_list_item_name);
            price = (TextView) itemView.findViewById(R.id.ordered_list_item_price);
            time = (TextView) itemView.findViewById(R.id.order_list_item_time);
            itemLayout = (RelativeLayout) itemView.findViewById(R.id.order_item_root_layout);
            unSubscirberLayout = (RelativeLayout) itemView.findViewById(R.id.unsubscriber_layout);
            listItemLayout = (RelativeLayout) itemView.findViewById(R.id.order_list_item_layout);
            imageUnsubscriber= (ImageView) itemView.findViewById(R.id.imageUnsubscriber);
        }
    }

    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                if(null!=v.findViewById(R.id.unsubscriber_layout).getTag()){
                    v.findViewById(R.id.unsubscriber_layout).setVisibility(View.VISIBLE);
                }
                v.findViewById(R.id.ordered_list_item_name).setSelected(true);
                final int position = (int) v.getTag();
                v.findViewById(R.id.order_list_item_layout).setSelected(true);
                v.findViewById(R.id.unsubscriber_layout).setBackground
                        (ContextCompat.getDrawable(mContext, R
                                .drawable.mytv_item_bg));
                v.findViewById(R.id.order_list_item_layout).setBackground(ContextCompat
                        .getDrawable(mContext, R
                                .drawable.mytv_item_bg_foucs_select));
                isSelectUnSubscribed = false;
                selectPostion = position;
                Subscription subscription = null;
                if (position < mSubscriptionList.size()) {
                    subscription = mSubscriptionList.get(position);
                }
                if (null != mOnItemSelectedListener) {
                    mOnItemSelectedListener.onItemSelected(subscription, position);
                }
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemChanged(position);
                    }
                });
            } else {
                v.findViewById(R.id.ordered_list_item_name).setSelected(false);
                v.findViewById(R.id.order_list_item_layout).setSelected(false);
                v.findViewById(R.id.unsubscriber_layout).setBackground
                        (ContextCompat.getDrawable(mContext, R
                                .drawable.transparent_drawable));
                v.findViewById(R.id.order_list_item_layout).setBackground(ContextCompat
                        .getDrawable(mContext, R
                                .drawable.transparent_drawable));
                v.findViewById(R.id.unsubscriber_layout).setVisibility(View.GONE);
            }
        }
    };

    public void addData(List<Subscription> subscriptionList) {
        mSubscriptionList.addAll(subscriptionList);
        notifyItemRangeInserted(mSubscriptionList.size() - subscriptionList.size(), subscriptionList.size());
    }

    public interface OnItemSelectedListener {
        void onItemSelected(Subscription subscription, int position);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        mOnItemSelectedListener = onItemSelectedListener;
    }

    public void clearData() {
        mSubscriptionList.clear();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface onUnSubscribedListener {
        void onUnSubscribed(String productId, String cancelType);
        void onPlayVod(VOD vod);
    }

    public void setUnSubscribedListener(onUnSubscribedListener unSubscribedListener) {
        this.unSubscribedListener = unSubscribedListener;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            switch (v.getId()) {
                case R.id.order_item_root_layout:
                    if (mLastOnClickTime != 0 && System.currentTimeMillis() - mLastOnClickTime < VALID_TIME) {
                        return;
                    }
                    mLastOnClickTime = System.currentTimeMillis();
                    if (position < mSubscriptionList.size() && isSelectUnSubscribed) {
                        //selectedView = v;
                        selectPostion = position;
                        Subscription subscription = mSubscriptionList.get(position);
                        View childView= v.findViewById(R.id.unsubscriber_layout);

                        if(null!=childView&&null!=childView.getTag()) {
                            if(null!=unSubscribedListener) {
                                if(cancel_subscriber.equals(childView.getTag().toString()))
                                unSubscribedListener.onUnSubscribed(subscription.getProductID(), "2");
                                if(play.equals(childView.getTag().toString())) {
                                    unSubscribedListener.onPlayVod(subscription.getPriceObjectDetail()==null?null:subscription.getPriceObjectDetail().getVOD());
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };
//    private boolean isCancelSubscribed(long validateTime) {
//        SuperLog.debug(TAG, "validateTime=" + validateTime);
//        boolean isSupport = false;
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(validateTime);
//        int year = calendar.get(Calendar.YEAR);
//        SuperLog.debug(TAG, "year=" + year);
//        if (2099 <= year) {
//            isSupport = true;
//        }
//
//        return isSupport;
//    }

    private boolean isCancelSubscribed(String isAuto){
        return  "1".equals(isAuto);
    }


    public void notifyChanged() {
        notifyDataSetChanged();
//        if (selectedView == null) {
//            DebugLog.debug(TAG, "selectedView is null.");
//            return;
//        }
//        selectedView.findViewById(R.id.unsubscriber_layout).setVisibility(View.GONE);
//        selectedView.findViewById(R.id.order_list_item_layout).setBackground(ContextCompat
//                .getDrawable(mContext, R
//                        .drawable.mytv_item_bg_foucs_select));

    }
}