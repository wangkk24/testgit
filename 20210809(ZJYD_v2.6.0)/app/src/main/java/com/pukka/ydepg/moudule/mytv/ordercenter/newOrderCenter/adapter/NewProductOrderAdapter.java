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

    //key:关联的一级产品包productId, value:该一级产品包下生效的营销产品包
    private Map<String,Product> productMap;

    //点击回调
    private OnitemClick onitemClick;

    //拓展参数 产品包原价信息
    private static final String orgPriceKey = "ORIGINAL_PRICE";

    //拓展参数 折扣信息
    private static final String cornreMarketInfo = "CORNRE_MARKER_INFO";

    //第一次加载时第一个item获得焦点
    boolean isFirstLoad = true;

    private DecimalFormat decimalFormat;

    private static final long MIN_CLICK_INTERVAL = 350L;

    //防止点击后快速切换产品包产生错误
    private boolean canSwitch = true;

    /**
     * VOD名称
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

        //首次加载 第一项获得焦点
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

        //产品包名
        String name = OrgProduct.getName();
        //如果产品包名中包含4k，在4k处换行
//        if(name.indexOf("含4k") != -1)
//        {
//            String temStr = name.substring(0, name.indexOf("含4k") - 1);
//            String temStr1 = String.format(temStr + "\n"+"(含4k)");
//            holder.title.setText(temStr1);
//        }else{
//            holder.title.setText(name);
//        }
        if(name.contains("含4k") || name.contains("含4K")){
            holder.titleLayout.setVisibility(View.VISIBLE);
            holder.titleWithout4K.setVisibility(View.INVISIBLE);
            String temStr = name.substring(0, name.length() - 5);
            holder.title.setText(temStr);
        }else{
            holder.titleLayout.setVisibility(View.INVISIBLE);
            holder.titleWithout4K.setVisibility(View.VISIBLE);
            holder.titleWithout4K.setText(name);
        }

        //产品包价格
        double price = Double.parseDouble(product.getPrice()) / 100;
        holder.price.setText(decimalFormat.format(price));

        if (null != productMap && null != productMap.get(OrgProduct.getID())){
            //获取折扣信息
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

            //获取原价信息
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
         * 产品类型,0 ：包周期;1 ：按次
         */
        if((!TextUtils.isEmpty(OrgProduct.getProductType()) && OrgProduct.getProductType().equals("1"))){
            holder.mode.setText("元/次");
            //按次订购显示有效期
            String indateValue = mPresenter.analyticValidity(OrgProduct);
            //产品有效期
            if (!TextUtils.isEmpty(indateValue)) {
                holder.validity.setVisibility(View.VISIBLE);
                String valideHint= !TextUtils.isEmpty(OrgProduct.getGstCode())?"("+OrgProduct.getGstCode()+")":"";
                holder.validity.setText("有效期："+indateValue+valideHint);
            } else {
                holder.validity.setVisibility(View.INVISIBLE);
            }
        }else{
            if("0".equals(OrgProduct.getChargeMode())||"13".equals(OrgProduct.getChargeMode())){
                holder.validity.setVisibility(View.VISIBLE);
                holder.validity.setText("连续包月");
            }else{
                holder.validity.setVisibility(View.INVISIBLE);
            }
            holder.mode.setText("元/"+  StringUtils.analyticValidity(OrgProduct));
        }

        //绑定点击事件
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

        //非选中时的背景
        ImageView bgUnseleted;

        //选中时的背景
        ImageView bgseleted;

        //产品包名（含4k）
        TextView title;
        LinearLayout titleLayout;
        //产品副标题
        TextView subTitle;

        //产品包名（不含4k）
        TextView titleWithout4K;

        //产品折扣信息
        TextView discount;

        //产品价格
        TextView price;

        //产品订购方式，按次还是按月
        TextView mode;

        //产品原价
        TextView orgPrice;

        //产品有效期
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

                        //发送事件 刷新页面
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

                        //防止过快切换，焦点消失
                        if (ClickUtil.isFastDoubleClick(TAG)){
                            return true;
                        }

                        //防止点击产品包之后快速切换产生错误
                        if (!canSwitch){
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    //点击事件的接口
    public interface OnitemClick {
        void onItemClick(int position);
    }

    public void setOnitemClickLintener (OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }

}
