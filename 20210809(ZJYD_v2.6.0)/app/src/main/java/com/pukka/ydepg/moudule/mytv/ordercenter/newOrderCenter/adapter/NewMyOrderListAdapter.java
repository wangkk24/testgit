package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.adapter;

import android.app.Activity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscription;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.event.OrderCenterMyOrderFocusEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class NewMyOrderListAdapter extends RecyclerView.Adapter <NewMyOrderListAdapter.ViewHolder>{

    private static final String TAG = "OrderedListAdapter";
    //重复点击时间
    private static final long VALID_TIME = 500;
    private final Activity mContext;
    private final RecyclerView mRecyclerView;
    private final String cancel_subscriber = "cancel_subscriber";
    private final String play = "play";
    private List<Subscription> mSubscriptionList;
    private OnItemSelectedListener mOnItemSelectedListener;
    private onUnSubscribedListener unSubscribedListener;
    private boolean isSelectUnSubscribed = false;
    private DecimalFormat decimalFormat;
    //重复点击最后时间
    private long mLastOnClickTime = 0;
    //防止重复点击
    private boolean canClick = true;
    private int selectPostion = 0;
    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            setFocusEffect(hasFocus, (TextView) v.findViewById(R.id.tv_item_my_order_name));
            if (hasFocus) {
                v.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg_foucs_select));
                if (null != v.findViewById(R.id.rl_action_layout).getTag()) {
                    v.setBackground(ContextCompat.getDrawable(mContext, R.drawable.transparent_drawable));
                    v.findViewById(R.id.item_my_order_list_content).setBackground(ContextCompat
                            .getDrawable(mContext, R
                                    .drawable.mytv_item_bg_foucs_select));
                    v.findViewById(R.id.rl_action_layout).setVisibility(View.VISIBLE);
                }
                final int position = (int) v.getTag();
                v.findViewById(R.id.rl_action_layout).setBackground
                        (ContextCompat.getDrawable(mContext, R
                                .drawable.mytv_item_bg));

                isSelectUnSubscribed = false;
                selectPostion = position;
            } else {
                v.findViewById(R.id.item_my_order_list_content).setSelected(false);
                v.setBackground(ContextCompat.getDrawable(mContext, R.drawable.transparent_drawable));
                v.findViewById(R.id.rl_action_layout).setBackground
                        (ContextCompat.getDrawable(mContext, R
                                .drawable.transparent_drawable));
                v.findViewById(R.id.item_my_order_list_content).setBackground(ContextCompat
                        .getDrawable(mContext, R
                                .drawable.transparent_drawable));
                v.findViewById(R.id.rl_action_layout).setVisibility(View.GONE);
            }
        }
    };
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            switch (v.getId()) {
                case R.id.ll_item_my_order_list_root:

                    if (!canClick){
                        return;
                    }
                    canClick = false;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            canClick = true;
                        }
                    },500);
//                    if (mLastOnClickTime != 0 && System.currentTimeMillis() - mLastOnClickTime < VALID_TIME) {
//                        Log.i(TAG, "ll_item_my_order_list_root onClick: 1");
//                        return;
//                    }
//                    mLastOnClickTime = System.currentTimeMillis();
                    if (position < mSubscriptionList.size() && isSelectUnSubscribed) {
                        Log.i(TAG, "ll_item_my_order_list_root onClick: 2");
                        //selectedView = v;
                        selectPostion = position;
                        Subscription subscription = mSubscriptionList.get(position);
                        View childView = v.findViewById(R.id.rl_action_layout);

                        if (null != childView && null != childView.getTag()) {
                            if (null != unSubscribedListener) {
                                if (cancel_subscriber.equals(childView.getTag().toString()))
                                    unSubscribedListener.onUnSubscribed(subscription.getProductID(),subscription.getProductName(), "2",subscription.getExtensionFields(),subscription.getPicture());
                                if (play.equals(childView.getTag().toString())) {
                                    unSubscribedListener.onPlayVod(subscription.getPriceObjectDetail() == null ? null : subscription.getPriceObjectDetail().getVOD());
                                }
                            }
                        }
                    }else{
                        Log.i(TAG, "ll_item_my_order_list_root onClick: 3");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void setFocusEffect(boolean hasFocus, TextView tv) {
        if (!TextUtils.isEmpty(tv.getText())) {
            tv.setSelected(hasFocus);
            if (hasFocus) {
                tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            } else {
                tv.setEllipsize(TextUtils.TruncateAt.END);
            }
        }
    }

    public NewMyOrderListAdapter(RecyclerView recyclerView, List<Subscription> subscriptionList, Activity context) {
        mSubscriptionList = subscriptionList;
        mContext = context;
        mRecyclerView = recyclerView;
        decimalFormat = new DecimalFormat("0.00");
    }

    public void setSelectPostion(int selectPostion) {
        this.selectPostion = selectPostion;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_order_list, parent, false);
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
        } else {
            namesb.append(TextUtils.isEmpty(productName.trim()) ? mContext.getResources().getString(R.string.mytv_order_unknown) : productName.trim());
        }
        holder.name.setText(namesb.toString());
        initType(holder, subscription);
        initPrice(holder, subscription);
        initTime(holder, subscription);
        boolean isCancelSubscribed = isCancelSubscribed(subscription.getIsAutoExtend());
        SuperLog.debug(TAG, "isCancelSubscribed=" + isCancelSubscribed);
        if (subscription.getIsMainPackage().equals("0")) {//非基础包
            if (isCancelSubscribed && subscription.getProductType().equals("0")) {//自动续订产品 && 包周期产品
                if (subscription.getOrderState().equals("0")){//订单状态  0：订购状态 1：退订状态
                    holder.unSubscirberLayout.setTag(cancel_subscriber);
                    holder.imageUnsubscriber.setImageDrawable(mContext.getResources().getDrawable(R.drawable.order_unsubscribe));
                }else{
                    holder.unSubscirberLayout.setTag(null);
                }
            } else if (subscription.getProductType().equals("1")) { //按次产品
                holder.unSubscirberLayout.setTag(play);
                holder.imageUnsubscriber.setImageDrawable(mContext.getResources().getDrawable(R.drawable.subscriber_play));
            } else {
                holder.unSubscirberLayout.setTag(null);
            }
        } else {//非自动续订产品 && 包周期产品
            holder.unSubscirberLayout.setTag(null);
        }
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.itemView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_MENU:
                        mOnItemSelectedListener.onLoseFocus(true);
                        break;
                    case KeyEvent.KEYCODE_DPAD_LEFT://向左
                        if (holder.unSubscirberLayout.getVisibility() == View.VISIBLE) {
                            if (isSelectUnSubscribed) {
                                isSelectUnSubscribed = false;
                                holder.unSubscirberLayout.setBackground(ContextCompat.getDrawable(mContext, R
                                        .drawable.mytv_item_bg));
                                holder.listItemLayout.setBackground(ContextCompat.getDrawable(mContext, R
                                        .drawable.mytv_item_bg_foucs_select));
                                return true;
                            }
                        }
                        return true;
                    case KeyEvent.KEYCODE_DPAD_RIGHT://向右
                        //选中删除
                        if (holder.unSubscirberLayout.getVisibility() == View.VISIBLE) {
                            isSelectUnSubscribed = true;
                            holder.listItemLayout.setBackground(ContextCompat.getDrawable(mContext, R
                                    .drawable.mytv_item_bg));
                            holder.unSubscirberLayout.setBackground(ContextCompat.getDrawable(mContext, R
                                    .drawable.mytv_item_bg_foucs_select));
                        }
                        return true;
                    case KeyEvent.KEYCODE_DPAD_UP:
                        if (mRecyclerView.getChildAdapterPosition(v) == 0) {
                            mOnItemSelectedListener.onLoseFocus(false);
                            return true;
                        }

                        if (position == 0){
                            EventBus.getDefault().post(new OrderCenterMyOrderFocusEvent());
                            return true;
                        }
                        break;
                    default:
                        break;
                }
            }
            return false;
        });
    }

    private void initPrice(ViewHolder holder, Subscription subscription) {
        //非基础包展示价格
        if (!TextUtils.isEmpty(subscription.getPrice()) && TextUtils.equals("0", subscription.getIsMainPackage())) {
            double price = Double.parseDouble(subscription.getPrice()) / 100;
            String priceStr = decimalFormat.format(price);

            Product product = new Product();
            product.setID(subscription.getProductID());
            product.setChargeMode(subscription.getChargeMode());
            product.setPeriodLength(subscription.getPeriodLength());
            product.setProductType(subscription.getProductType());
            product.setRentPeriod(subscription.getContentRentPeriod());

            SpannableString spannable = new SpannableString(priceStr + "元/" + (TextUtils.equals(Subscription.ProductType.BY_TIMES, subscription.getProductType()) ? "部"
                    : StringUtils.analyticValidity(product)));
            RelativeSizeSpan span = new RelativeSizeSpan(1.2f);
            spannable.setSpan(span, 0, priceStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.price.setText(spannable);
        } else {
            holder.price.setText("");
        }
    }

    private void initTime(ViewHolder holder, Subscription subscription) {
        String orderTime = subscription.getOrderTime();
        String endTime = subscription.getEndTime();
        if (!TextUtils.isEmpty(orderTime)) {
            orderTime = DateCalendarUtils.formatDate(Long.parseLong(orderTime), "yyyy.MM.dd HH:mm");
        } else {
            orderTime = mContext.getResources().getString(R.string.mytv_order_unknown);
        }
        if (!TextUtils.isEmpty(endTime)) {
            endTime = DateCalendarUtils.formatDate(Long.parseLong(endTime), "yyyy.MM.dd HH:mm");
        } else {
            endTime = mContext.getResources().getString(R.string.mytv_order_unknown);
        }
        holder.orderTime.setText(orderTime);
        holder.expireTime.setText(endTime);
    }

    private void initType(ViewHolder holder, Subscription subscription) {
        if (TextUtils.equals(subscription.getProductType(), Subscription.ProductType.BY_TIMES)) {
            holder.type.setText(mContext.getString(R.string.my_order_type_single));
        } else {
            String type = StringUtils.getValidTime(subscription.getChargeMode());
            if (!TextUtils.isEmpty(type) && !TextUtils.equals(" ", type)) {
                holder.type.setText(String.format(mContext.getString(R.string.my_order_type_period), type));

            } else {
                holder.type.setText("");
            }
        }
    }

    @Override
    public int getItemCount() {
        return null == mSubscriptionList ? 0 : mSubscriptionList.size();
    }

    public void addData(List<Subscription> subscriptionList) {
        mSubscriptionList.addAll(subscriptionList);
        notifyItemRangeInserted(mSubscriptionList.size() - subscriptionList.size(), subscriptionList.size());
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

    public void setUnSubscribedListener(onUnSubscribedListener unSubscribedListener) {
        this.unSubscribedListener = unSubscribedListener;
    }

    private boolean isCancelSubscribed(String isAuto) {
        return "1".equals(isAuto);
    }

    public interface OnItemSelectedListener {
        void onLoseFocus(boolean clickFilter);
    }

    public interface onUnSubscribedListener {
        void onUnSubscribed(String productId, String productName, String cancelType, List<NamedParameter> extensionFields, Picture mPicture);

        void onPlayVod(VOD vod);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView type;
        TextView name;
        TextView price;
        TextView orderTime;
        TextView expireTime;
        RelativeLayout unSubscirberLayout;
        LinearLayout listItemLayout;
        ImageView imageUnsubscriber;

        public ViewHolder(View itemView) {

            super(itemView);
            type = (TextView) itemView.findViewById(R.id.tv_item_my_order_type);
            name = (TextView) itemView.findViewById(R.id.tv_item_my_order_name);
            price = (TextView) itemView.findViewById(R.id.tv_item_my_order_price);
            orderTime = (TextView) itemView.findViewById(R.id.tv_item_my_order_ordertime);
            expireTime = (TextView) itemView.findViewById(R.id.tv_item_my_order_expiretime);
            unSubscirberLayout = (RelativeLayout) itemView.findViewById(R.id.rl_action_layout);
            listItemLayout = (LinearLayout) itemView.findViewById(R.id.item_my_order_list_content);
            imageUnsubscriber = (ImageView) itemView.findViewById(R.id.iv_my_order_item_action);
        }
    }
}
