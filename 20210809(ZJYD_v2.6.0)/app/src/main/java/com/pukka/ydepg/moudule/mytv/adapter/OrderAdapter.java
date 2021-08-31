package com.pukka.ydepg.moudule.mytv.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.BookmarkItem;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscription;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.moudule.mytv.presenter.OrderedPresenter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by liudo on 2018/4/19.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final Context mContext;
    private List<Subscription> mSubscriptionList;
    private OrderedPresenter mPresenter;
    private DecimalFormat decimalFormat;

    public OrderAdapter(List<Subscription> contentList, Context context, OrderedPresenter mPresenter) {
        mSubscriptionList = contentList;
        mContext = context;
        this.mPresenter = mPresenter;
        decimalFormat = new DecimalFormat("0.00");
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mytv_order_item, null, false);
        return new OrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {
        Subscription mSubscription = mSubscriptionList.get(position);
        String productName = mSubscription.getProductName();
        StringBuffer namesb = new StringBuffer();
        if (TextUtils.equals(mSubscription.getProductType(), Subscription.ProductType.BY_TIMES)) {
            namesb.append(TextUtils.isEmpty(productName.trim()) ? mContext.getResources().getString(R.string.mytv_order_unknown) : productName.trim());
            if (null != mSubscription.getPriceObjectDetail() && null != mSubscription.getPriceObjectDetail().getVOD()) {
                namesb.append(" " + mSubscription.getPriceObjectDetail().getVOD().getName());
            }
        } else if("0".equals(mSubscription.getIsMainPackage())){
            namesb.append(TextUtils.isEmpty(productName.trim()) ? mContext.getResources().getString(R.string.mytv_order_unknown) : productName.trim());
            if(!TextUtils.isEmpty(mSubscription.getPrice())) {
                double price = Double.parseDouble(mSubscription.getPrice()) / 100;
                String priceStr=decimalFormat.format(price);
                if(priceStr.contains(".00")){
                    priceStr=priceStr.substring(0,priceStr.indexOf("."));
                }
                namesb.append(priceStr +"元/" + StringUtils.analyticValiditySubscription(mSubscription));
            }
        }else{
            namesb.append(TextUtils.isEmpty(productName.trim()) ? mContext.getResources().getString(R.string.mytv_order_unknown) : productName.trim());
        }
        holder.name.setText(namesb.toString());
        String startTime = mSubscription.getStartTime();
        String endTime = mSubscription.getEndTime();
        StringBuffer sb = new StringBuffer();
//        if (!TextUtils.equals(mSubscription.getProductType(), Subscription.ProductType.BY_TIMES)&&"0".equals(mSubscription.getIsMainPackage())) {
//            sb.append(TextUtils.isEmpty(mSubscription.getPeriodLength())?"":mSubscription.getPeriodLength()+mPresenter.getValidtime(mSubscription)+",");
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
    }

    public void addData(List<Subscription> contentList) {
        mSubscriptionList.addAll(contentList);
        notifyItemRangeInserted(mSubscriptionList.size() - contentList.size(), contentList.size());
    }

    @Override
    public int getItemCount() {
        return null == mSubscriptionList ? 0 : mSubscriptionList.size() > 10 ? 10 : mSubscriptionList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.mytv_list_name);
            time = (TextView) itemView.findViewById(R.id.mytv_list_time);
        }
    }
}
