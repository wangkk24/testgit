package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.utils.ClickUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.moudule.mytv.adapter.NewHistoryFragmentListAdapter;
import com.pukka.ydepg.moudule.mytv.adapter.NewHistoryListAdapter;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.event.ProductOrderSwitchEvent;
import com.pukka.ydepg.moudule.mytv.presenter.ProductOrderPresenter;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class NewProductOrderAdapter extends RecyclerView.Adapter <NewProductOrderAdapter.ViewHolder>{

    private static final String TAG = "NewProductOrderAdapter";

    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_XSMALL);

    private Context mContext;

    private List<Product> productList;

    private ProductOrderPresenter mPresenter;

    //key:????????????????????????productId, value:?????????????????????????????????????????????
    private Map<String,Product> productMap;

    //????????????
    private OnitemClick onitemClick;

    //???????????? ?????????????????????
    private static final String orgPriceKey = "ORIGINAL_PRICE";

    //???????????? ????????????
    private static final String cornreMarketInfo = "CORNRE_MARKER_INFO";

    //???????????????????????????item????????????
    boolean isFirstLoad = true;

    private DecimalFormat decimalFormat;

    private static final long MIN_CLICK_INTERVAL = 350L;

    //????????????????????????????????????????????????
    private boolean canSwitch = true;

    /**
     * VOD??????
     */
    private String mVODName;

    public NewProductOrderAdapter(Context mContext, List<Product> productList ,ProductOrderPresenter presenter ,Map<String,Product> productMap) {
        this.mContext = mContext;
        this.productList = productList;
        this.mPresenter = presenter;
        this.productMap = productMap;
        decimalFormat = new DecimalFormat("0.00");

        ClickUtil.setInterval(TAG,MIN_CLICK_INTERVAL);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_orderlist_new, null, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemView.setTag(position);

        //???????????? ?????????????????????
        if (position == 0){

            if (isFirstLoad){
                holder.bgUnseleted.setVisibility(View.GONE);
                holder.bgseleted.setVisibility(View.VISIBLE);
                holder.title.setTextColor(mContext.getResources().getColor(R.color.order_list_title));
                holder.subTitle.setTextColor(mContext.getResources().getColor(R.color.order_list_title));
                holder.titleWithout4K.setTextColor(mContext.getResources().getColor(R.color.order_list_title));
                holder.price.setTextColor(mContext.getResources().getColor(R.color.order_list_title));
                holder.mode.setTextColor(mContext.getResources().getColor(R.color.order_list_title));

                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.itemView.requestFocus();
                        isFirstLoad = false;
                    }
                },100);

            }
        }

        Product OrgProduct = productList.get(position);
        Product product = OrgProduct;

        if (null != productMap && null != productMap.get(OrgProduct.getID())){
            product = productMap.get(OrgProduct.getID());
        }

        if (null == product){
            return;
        }

        List<NamedParameter> listNp = product.getCustomFields();

        //????????????
        String name = OrgProduct.getName();
        //???????????????????????????4k??????4k?????????
//        if(name.indexOf("???4k") != -1)
//        {
//            String temStr = name.substring(0, name.indexOf("???4k") - 1);
//            String temStr1 = String.format(temStr + "\n"+"(???4k)");
//            holder.title.setText(temStr1);
//        }else{
//            holder.title.setText(name);
//        }
        if(name.contains("???4k") || name.contains("???4K")){
            holder.titleLayout.setVisibility(View.VISIBLE);
            holder.titleWithout4K.setVisibility(View.INVISIBLE);
            String temStr = name.substring(0, name.length() - 5);
            holder.title.setText(temStr);
        }else{
            holder.titleLayout.setVisibility(View.INVISIBLE);
            holder.titleWithout4K.setVisibility(View.VISIBLE);
            holder.titleWithout4K.setText(name);
        }

        //???????????????
        double price = Double.parseDouble(product.getPrice()) / 100;
        holder.price.setText(decimalFormat.format(price));

        if (null != productMap && null != productMap.get(OrgProduct.getID())){
            //??????????????????
            List<String> marketInfo = CommonUtil.getCustomNamedParameterByKey(listNp,cornreMarketInfo);
            if (null != marketInfo && marketInfo.size()>0){
                String str = marketInfo.get(0);
                if (!TextUtils.isEmpty(str)){
                    holder.discount.setVisibility(View.VISIBLE);
                    holder.discount.setText(str);
                }else{
                    holder.discount.setText("");
                    holder.discount.setVisibility(View.INVISIBLE);
                }
            }else{
                holder.discount.setText("");
                holder.discount.setVisibility(View.INVISIBLE);
            }

            //??????????????????
            List<String> orgPrice = CommonUtil.getCustomNamedParameterByKey(listNp,orgPriceKey);
            if (null != orgPrice && orgPrice.size()>0){
                String str = orgPrice.get(0);
                if (!TextUtils.isEmpty(str)){
                    SpannableString spannableString = new SpannableString(str);
                    StrikethroughSpan colorSpan = new StrikethroughSpan();
                    spannableString.setSpan(colorSpan, 0, spannableString.length() -1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    holder.orgPrice.setText(spannableString);
                }else{
                    holder.orgPrice.setText("");
                }
            }else{
                holder.orgPrice.setText("");
            }
        }else{
            holder.discount.setText("");
            holder.discount.setVisibility(View.INVISIBLE);
            holder.orgPrice.setText("");
        }

        /*
         * ????????????,0 ????????????;1 ?????????
         */
        if((!TextUtils.isEmpty(OrgProduct.getProductType()) && OrgProduct.getProductType().equals("1"))){
            holder.mode.setText("???/???");
            //???????????????????????????
            String indateValue = mPresenter.analyticValidity(OrgProduct);
            //???????????????
            if (!TextUtils.isEmpty(indateValue)) {
                holder.validity.setVisibility(View.VISIBLE);
                String valideHint= !TextUtils.isEmpty(OrgProduct.getGstCode())?"("+OrgProduct.getGstCode()+")":"";
                holder.validity.setText("????????????"+indateValue+valideHint);
            } else {
                holder.validity.setVisibility(View.INVISIBLE);
            }
        }else{
            if("0".equals(OrgProduct.getChargeMode())||"13".equals(OrgProduct.getChargeMode())){
                holder.validity.setVisibility(View.VISIBLE);
                holder.validity.setText("????????????");
            }else{
                holder.validity.setVisibility(View.INVISIBLE);
            }
            holder.mode.setText("???/"+  StringUtils.analyticValidity(OrgProduct));
        }

        //??????????????????
        if (onitemClick != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    canSwitch = false;
                    onitemClick.onItemClick(position);
                    Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            canSwitch = true;
                        }
                    },2000);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setmVODName(String mVODName) {
        this.mVODName = mVODName;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        //?????????????????????
        ImageView bgUnseleted;

        //??????????????????
        ImageView bgseleted;

        //??????????????????4k???
        TextView title;
        LinearLayout titleLayout;
        //???????????????
        TextView subTitle;

        //?????????????????????4k???
        TextView titleWithout4K;

        //??????????????????
        TextView discount;

        //????????????
        TextView price;

        //???????????????????????????????????????
        TextView mode;

        //????????????
        TextView orgPrice;

        //???????????????
        TextView validity;

        public ViewHolder(View itemView) {
            super(itemView);
            bgUnseleted = itemView.findViewById(R.id.order_list_bg_unselected);
            bgseleted = itemView.findViewById(R.id.order_list_bg_selected);
            title = itemView.findViewById(R.id.order_list_title);
            subTitle = itemView.findViewById(R.id.order_list_subtitle);
            titleLayout = itemView.findViewById(R.id.order_list_title_layout);
            titleWithout4K = itemView.findViewById(R.id.order_list_title_without4k);
            discount = itemView.findViewById(R.id.order_list_discount);
            price = itemView.findViewById(R.id.order_list_price);
            mode = itemView.findViewById(R.id.order_list_mode);
            orgPrice = itemView.findViewById(R.id.order_list_org_price);
            validity = itemView.findViewById(R.id.order_list_time);

            if (mFocusHighlight != null) {
                mFocusHighlight.onInitializeView(itemView);
            }

            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (mFocusHighlight != null) {
                        mFocusHighlight.onItemFocused(v, hasFocus);
                    }
                    title.setSelected(hasFocus);
                    subTitle.setSelected(hasFocus);
                    titleWithout4K.setSelected(hasFocus);

                    if (hasFocus){
                        bgUnseleted.setVisibility(View.GONE);
                        bgseleted.setVisibility(View.VISIBLE);
                        title.setTextColor(mContext.getResources().getColor(R.color.order_list_title));
                        subTitle.setTextColor(mContext.getResources().getColor(R.color.order_list_title));
                        titleWithout4K.setTextColor(mContext.getResources().getColor(R.color.order_list_title));
                        price.setTextColor(mContext.getResources().getColor(R.color.order_list_title));
                        mode.setTextColor(mContext.getResources().getColor(R.color.order_list_title));

                        //???????????? ????????????
                        int position = (int) itemView.getTag();
                        EventBus.getDefault().post(new ProductOrderSwitchEvent(position));

                    }else{
                        bgUnseleted.setVisibility(View.VISIBLE);
                        bgseleted.setVisibility(View.GONE);
                        title.setTextColor(mContext.getResources().getColor(R.color.white_0));
                        subTitle.setTextColor(mContext.getResources().getColor(R.color.white_0));
                        titleWithout4K.setTextColor(mContext.getResources().getColor(R.color.white_0));
                        price.setTextColor(mContext.getResources().getColor(R.color.white_0));
                        mode.setTextColor(mContext.getResources().getColor(R.color.white_0));
                    }
                }
            });

            itemView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {

                        if (isFirstLoad){
                            return true;
                        }

                        //?????????????????????????????????
                        if (ClickUtil.isFastDoubleClick(TAG)){
                            return true;
                        }

                        //???????????????????????????????????????????????????
                        if (!canSwitch){
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    //?????????????????????
    public interface OnitemClick {
        void onItemClick(int position);
    }

    public void setOnitemClickLintener (OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }

}
