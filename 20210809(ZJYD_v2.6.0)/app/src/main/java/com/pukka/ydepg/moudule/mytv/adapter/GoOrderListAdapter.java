package com.pukka.ydepg.moudule.mytv.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 在此写用途
 *
 * @FileName: com.pukka.ydepg.moudule.mytv.adapter.GoOrderListAdapter.java
 * @author: luwm
 * @data: 2018-08-16 17:19
 * @Version V1.0 <描述当前版本功能>
 */
public class GoOrderListAdapter extends RecyclerView.Adapter<GoOrderListAdapter.MyHolder> {
    private static final String TAG = "GoOrderListAdapter";
    private Context mContext;
    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_MEDIUM);
    private List<Product> mProducts;
    private RecyclerView mRecyclerView;
    private OnItemClickListener mListener;
    private Map<String, String> mOrderItemMaps;
    private DecimalFormat format;

    public GoOrderListAdapter(RecyclerView recyclerView, List<Product> products, Context context, Map<String, String> orderItemMaps) {
        this.mContext = context;
        this.mProducts = products;
        this.mRecyclerView = recyclerView;
        format = new DecimalFormat("0.00");
        this.mOrderItemMaps = orderItemMaps;
    }

    public List<Product> getProducts()
    {
        return mProducts;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_go_order_list, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Product product = mProducts.get(position);
        holder.itemView.setTag(product);
        if (null != product.getName()) {
            holder.tvName.setText(product.getName());
        }
        initPoster(holder, product.getPicture());
        initPrice(holder, product.getPrice());
        initSuperscript(holder, product);
        holder.itemView.setOnClickListener(v -> mListener.onClik((Product) v.getTag(),position));
    }

    private void initPoster(MyHolder holder, Picture picture) {
        if (null != picture) {
            String imgUrl = "";
            List<String> adList = picture.getAds();
            if (adList != null && adList.size() > 0) {
                imgUrl = adList.get(0).toString();
            }
            RequestOptions options  = new RequestOptions()
                    .placeholder(R.drawable.default_poster_bg)
                    .error(R.drawable.default_poster_bg);

            Glide.with(mContext).load(imgUrl).apply(options).into(holder.ivPoster);
        }
    }

    private void initSuperscript(MyHolder holder, Product product) {
        //已经订购展示已订购角标，未订购展示终端参数中配置的角标
        if (TextUtils.equals(product.getIsSubscribed(), "1")) {
            holder.ivSuperscript.setVisibility(View.VISIBLE);
            holder.ivSuperscript.setImageResource(R.drawable.superscript_ordered);
            holder.tvSuperscript.setVisibility(View.GONE);
        } else if (null != mOrderItemMaps && null != mOrderItemMaps.get(product.getID())) {
            holder.ivSuperscript.setVisibility(View.VISIBLE);
            holder.ivSuperscript.setImageResource(R.drawable.superscript_bg);
            holder.tvSuperscript.setVisibility(View.VISIBLE);
            holder.tvSuperscript.setText(mOrderItemMaps.get(product.getID()));
        } else {
            holder.ivSuperscript.setVisibility(View.GONE);
            holder.tvSuperscript.setVisibility(View.GONE);
        }
    }

    private void initPrice(MyHolder holder, String price) {
        String formatPrice = null;
        if (!TextUtils.isEmpty(price)) {
            formatPrice = format.format(Double.parseDouble(price) / 100);
        }
        if (formatPrice != null) {
            price = String.format(mContext.getString(R.string.go_order_price_format), formatPrice);
            SpannableString spannableString = new SpannableString(price);
            RelativeSizeSpan sizeSpan = new RelativeSizeSpan(1.2f);
            spannableString.setSpan(sizeSpan, 0, formatPrice.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.tvPrice.setText(spannableString);
        }
    }

    @Override
    public int getItemCount() {
        return null == mProducts ? 0 : mProducts.size();
    }

    public void addData(List<Product> list, boolean needClear) {
        int lastIndex = 0;
        if (needClear && null != mProducts) {
            mProducts.clear();
            notifyDataSetChanged();
        }
        if (mProducts != null) {
            lastIndex = mProducts.size();
            mProducts.addAll(list);
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    private boolean isLastLine(View v) {
        int position = mRecyclerView.getChildAdapterPosition(v);
        if ((position >= (getItemCount() / 4 * 4) && getItemCount() > 4) || getItemCount() < 4) {
            return true;
        } else {
            return false;
        }
    }

    public interface OnItemClickListener {
        void onClik(Product product,int position);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView ivPoster;
        private ImageView ivSuperscript;
        private TextView tvSuperscript;
        private TextView tvName;
        private TextView tvPrice;

        public MyHolder(View itemView) {
            super(itemView);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_go_order_item_poster);
            ivSuperscript = (ImageView) itemView.findViewById(R.id.iv_go_order_item_superscript);
            tvSuperscript = (TextView) itemView.findViewById(R.id.tv_go_order_item_superscript);
            tvName = (TextView) itemView.findViewById(R.id.tv_go_order_item_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_go_order_item_price);
            if (mFocusHighlight != null) {
                mFocusHighlight.onInitializeView(itemView);
            }
            itemView.setOnFocusChangeListener((v, hasFocus) -> {
                if (mFocusHighlight != null) {
                    mFocusHighlight.onItemFocused(v, hasFocus);
                }
                tvName.setSelected(hasFocus);
                if (hasFocus) {
                    v.setSelected(true);
                } else {
                    v.setSelected(false);
                }
            });
            itemView.setOnKeyListener((v, keyCode, event) -> {
                //解决从倒数第二行向下按，如果下方不存在内容，无响应的问题
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (null == FocusFinder.getInstance().findNextFocus(mRecyclerView, v, View.FOCUS_DOWN) && !isLastLine(v)) {
                        View nextFocusView = mRecyclerView.getChildAt(mRecyclerView.getLayoutManager().getChildCount() - 1);
                        nextFocusView.requestFocus();
                        return true;
                    }
                }
                return false;
            });
        }

    }

}
