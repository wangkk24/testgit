package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
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
import com.google.gson.Gson;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.moudule.mytv.adapter.GoOrderListAdapter;
import com.pukka.ydepg.moudule.mytv.bean.OrderItemBean;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.event.OrderCenterTopFocusEvent;
import com.pukka.ydepg.moudule.mytv.presenter.EventKeyLeftSwitchFocus;
import com.pukka.ydepg.moudule.mytv.presenter.ProductOrderPresenter;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewGoOrderListAdapter extends RecyclerView.Adapter <NewGoOrderListAdapter.MyHolder>{
    private static final String TAG = "GoOrderListAdapter";
    private Context mContext;
    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_SMALL);
    private List<Product> mProducts;
    private RecyclerView mRecyclerView;
    private GoOrderListAdapter.OnItemClickListener mListener;
    private Map<String, String> mOrderItemMaps;
    private Map<String, String> mOrderItemMapsForColor;
    private DecimalFormat format;
    //???????????? ?????????????????????
    private static final String orgPriceKey = "ORIGINAL_PRICE";
    //???????????????????????????  key:????????????????????????productId, value:?????????????????????????????????????????????
    Map<String, Product> productMap = new HashMap<String, Product>();

    private ProductOrderPresenter mPresenter = new ProductOrderPresenter();

    public NewGoOrderListAdapter(RecyclerView recyclerView, List<Product> products, Context context, Map<String, String> orderItemMaps,Map<String, String> orderItemMapsForColor,Map<String, Product> productMap) {
        this.mContext = context;
        this.mProducts = products;
        this.mRecyclerView = recyclerView;
        format = new DecimalFormat("0.00");
        this.mOrderItemMaps = orderItemMaps;
        this.mOrderItemMapsForColor = orderItemMapsForColor;
        this.productMap = productMap;
    }

    public List<Product> getProducts()
    {
        return mProducts;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_go_order_list_new, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {



        holder.position = position;

        Product OrgProduct = mProducts.get(position);

        Product product = OrgProduct;

        if (null != productMap && null != productMap.get(OrgProduct.getID()) && OrgProduct.getIsSubscribed().equals("0")){
            product = productMap.get(OrgProduct.getID());
        }


        holder.itemView.setTag(OrgProduct);
        if (null != OrgProduct.getName()) {
            holder.tvName.setText(OrgProduct.getName());
        }
        initPoster(holder, product.getPicture());
        initPrice(holder, product.getPrice(), product);
        initSuperscript(holder, product);
        holder.itemView.setOnClickListener(v -> mListener.onClik((Product) v.getTag(),position));

        List<NamedParameter> listNp = product.getCustomFields();
        if (null != productMap && null != productMap.get(OrgProduct.getID())){
            //??????????????????
            List<String> orgPrice = CommonUtil.getCustomNamedParameterByKey(listNp,orgPriceKey);
            if (null != orgPrice && orgPrice.size()>0){
                String str = orgPrice.get(0);

                SpannableString spannableString = new SpannableString(str);
                StrikethroughSpan colorSpan = new StrikethroughSpan();
                spannableString.setSpan(colorSpan, 0, spannableString.length() -1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                if (!TextUtils.isEmpty(spannableString)){
                    holder.tvPriceOrg.setVisibility(View.VISIBLE);
                    holder.tvPriceOrg.setText(spannableString);
                }else{
                    holder.tvPriceOrg.setVisibility(View.GONE);
                    holder.tvPriceOrg.setText("");
                }
            }else{
                holder.tvPriceOrg.setVisibility(View.GONE);
                holder.tvPriceOrg.setText("");
            }
        }else{
            holder.tvPriceOrg.setVisibility(View.GONE);
        }

        /*
         * ????????????,0 ????????????;1 ?????????
         */
        if ((!TextUtils.isEmpty(OrgProduct.getProductType()) && OrgProduct.getProductType().equals("1"))) {
            //???????????????????????????
            String indateValue = mPresenter.analyticValidity(OrgProduct);
            //???????????????
            if (!TextUtils.isEmpty(indateValue)) {
                holder.tvPaymode.setVisibility(View.VISIBLE);
                String valideHint = !TextUtils.isEmpty(OrgProduct.getGstCode()) ? "(" + OrgProduct.getGstCode() + ")" : "";
                holder.tvPaymode.setText("????????????" + indateValue + valideHint);
            } else {
                holder.tvPaymode.setVisibility(View.INVISIBLE);
            }
        } else {
            if ("0".equals(OrgProduct.getChargeMode()) || "13".equals(OrgProduct.getChargeMode())) {
                holder.tvPaymode.setVisibility(View.VISIBLE);
                holder.tvPaymode.setText("????????????");
            } else {
                holder.tvPaymode.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void initPoster(MyHolder holder, Picture picture) {
        if (null != picture) {
            String imgUrl = "";
            List<String> adList = picture.getTitles();
            if (adList != null && adList.size() > 0) {
                imgUrl = adList.get(0).toString();
            }
            RequestOptions options  = new RequestOptions()
                    .placeholder(null)
                    .error(null);

            Glide.with(mContext).load(imgUrl).apply(options).into(holder.ivPoster);
        }
    }

    private void initSuperscript(MyHolder holder, Product product) {
        Log.i(TAG, "initSuperscript:  "+ product.getID());
        //?????????????????????????????????????????????????????????????????????????????????
        if (TextUtils.equals(product.getIsSubscribed(), "1")) {
            holder.ivSuperscript.setVisibility(View.VISIBLE);
            holder.ivSuperscript.setImageResource(R.drawable.superscript_ordered);
            holder.tvSuperscript.setVisibility(View.GONE);
        } else if (null != mOrderItemMaps && null != mOrderItemMaps.get(product.getID())) {
            holder.tvSuperscript.setVisibility(View.VISIBLE);
            holder.tvSuperscript.setText(mOrderItemMaps.get(product.getID()));
            String color = mOrderItemMapsForColor.get(product.getID());
            if (null != color){
                switch (color){
                    //?????????????????????????????????
                    case OrderItemBean.COLOR_RED:{
                        holder.ivSuperscript.setVisibility(View.VISIBLE);
                        holder.ivSuperscript.setImageResource(R.drawable.superscript_bg);
                        holder.tvSuperscript.setTextColor(mContext.getResources().getColor(R.color.go_order_redsuperscript_color));
                        break;
                    }
                    //?????????????????????????????????
                    case OrderItemBean.COLOR_YELLOW:{
                        holder.ivSuperscript.setVisibility(View.VISIBLE);
                        holder.ivSuperscript.setImageResource(R.drawable.superscript_bg_yellow);
                        holder.tvSuperscript.setTextColor(mContext.getResources().getColor(R.color.go_order_yellowsuperscript_color));
                        break;
                    }
                    //?????????????????????????????????????????????
                    default:{
                        holder.ivSuperscript.setVisibility(View.VISIBLE);
                        holder.ivSuperscript.setImageResource(R.drawable.superscript_bg);
                        holder.tvSuperscript.setTextColor(mContext.getResources().getColor(R.color.go_order_redsuperscript_color));
                        break;
                    }
                }
            }else{
                //?????????????????????????????????
                holder.ivSuperscript.setVisibility(View.VISIBLE);
                holder.ivSuperscript.setImageResource(R.drawable.superscript_bg);
                holder.tvSuperscript.setTextColor(mContext.getResources().getColor(R.color.go_order_redsuperscript_color));
            }
        } else {
            holder.ivSuperscript.setVisibility(View.GONE);
            holder.tvSuperscript.setVisibility(View.GONE);
        }
    }

    private void initPrice(MyHolder holder, String price ,Product product) {
        String formatPrice = null;
        if (!TextUtils.isEmpty(price)) {
            formatPrice = format.format(Double.parseDouble(price) / 100);
        }
        if (formatPrice != null) {

            String typeStr = "";
            /*
             * ????????????,0 ????????????;1 ?????????
             */
            if((!TextUtils.isEmpty(product.getProductType()) && product.getProductType().equals("1"))){
                typeStr = "???/???";
            }else{
                typeStr = String.format("???/"+  StringUtils.analyticValidity(product));
            }
//            price = String.format(mContext.getString(R.string.go_order_price_format_new), formatPrice);
            price = String.format( formatPrice + typeStr);
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

    public void setListener(GoOrderListAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    private boolean isLastLine(View v) {
        int position = mRecyclerView.getChildAdapterPosition(v);
        if ((getItemCount() / 4 * 4) == getItemCount()){
            //??????????????????
            if ((position >= (((getItemCount() / 4)-1) * 4) && getItemCount() > 4) || getItemCount() < 4) {
                return true;
            } else {
                return false;
            }
        }else{
            if ((position >= (getItemCount() / 4 * 4) && getItemCount() > 4) || getItemCount() < 4) {
                return true;
            } else {
                return false;
            }
        }
    }

    public interface OnItemClickListener {
        void onClik(Product product);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView ivPoster;
        private ImageView ivSuperscript;
        private TextView tvSuperscript;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvPriceOrg;
        private TextView tvPaymode;
        private int position;

        public MyHolder(View itemView) {
            super(itemView);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_go_order_item_poster);
            ivSuperscript = (ImageView) itemView.findViewById(R.id.iv_go_order_item_superscript);
            tvSuperscript = (TextView) itemView.findViewById(R.id.tv_go_order_item_superscript);
            tvName = (TextView) itemView.findViewById(R.id.tv_go_order_item_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_go_order_item_price);
            tvPriceOrg = itemView.findViewById(R.id.tv_go_order_item_price_org);
            tvPaymode = itemView.findViewById(R.id.tv_go_order_item_mode);
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
                    tvName.setTextColor(mContext.getResources().getColor(R.color.scroll_static_resource_item_bg));
                } else {
                    v.setSelected(false);
                    tvName.setTextColor(mContext.getResources().getColor(R.color.white_0));
                }
            });
            itemView.setOnKeyListener((v, keyCode, event) -> {
                //????????????????????????????????????????????????????????????????????????????????????
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (null == FocusFinder.getInstance().findNextFocus(mRecyclerView, v, View.FOCUS_DOWN) && !isLastLine(v)) {
                        View nextFocusView = mRecyclerView.getChildAt(mRecyclerView.getLayoutManager().getChildCount() - 1);
                        nextFocusView.requestFocus();
                        return true;
                    }
                }

                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN && position == mProducts.size() - 1){
                    //??????????????????item??????????????????????????????????????????????????????
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN && position <= 3){
                    //?????????item??????????????????????????????
                    EventBus.getDefault().post(new OrderCenterTopFocusEvent());
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN && position %4 == 0){
                    //?????????????????????item????????????????????????????????????????????????
                    return true;
                }
                return false;
            });
        }

    }
}
