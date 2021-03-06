package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.extview.VerticalScrollTextView;
import com.pukka.ydepg.common.http.bean.node.OfferInfo;
import com.pukka.ydepg.common.http.bean.request.QueryMultiqryRequest;
import com.pukka.ydepg.common.http.bean.response.QueryMultiqryResponse;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODListCallBack;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.SubMutex;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.UniPayInfo;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.GetProductMutExRelaResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PBSRemixRecommendResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUniPayInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.Recommend;
import com.pukka.ydepg.common.http.vss.request.QueryMultiUserInfoRequest;
import com.pukka.ydepg.common.http.vss.response.QueryMultiUserInfoResponse;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.scene.UBDRecommendImpression;
import com.pukka.ydepg.common.toptool.EpgTopFunctionMenu;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.productUtil.ProductUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.control.selfregister.SelfAppInfoController;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.browse.VodSubjectCallBack;
import com.pukka.ydepg.moudule.mytv.ZjYdUniAndPhonePayActivty;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.adapter.OrderTransitionListAdapter;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.event.ProductTransitionListFocusEvent;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.AvoidRepeatPaymentUtils;
import com.pukka.ydepg.moudule.mytv.presenter.ProductOrderPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.contract.ProductOrderContract;
import com.pukka.ydepg.moudule.mytv.utils.PaymentUtils;
import com.pukka.ydepg.moudule.vod.activity.ChildModeVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;
import com.pukka.ydepg.moudule.vod.presenter.MoviesListPresenter;
import com.pukka.ydepg.moudule.vod.utils.RemixRecommendUtil;

import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductTransitionActivity extends BaseActivity implements
        ProductOrderContract.View,
        View.OnClickListener,
        OrderTransitionListAdapter.OnitemClick {

    private final String TAG = ProductTransitionActivity.class.getSimpleName();

    //?????????
    @BindView(R.id.root_view)
    ScrollView rootView;

    //????????????
    @BindView(R.id.order_transition_event_slogan_textview)
    TextView eventSloganTextview;

    //??????????????????
    @BindView(R.id.order_transition_title_image)
    ImageView titleImage;

    //?????????item??????????????????4K???
    @BindView(R.id.order_transition_item_title_without4k)
    TextView itemTitleWithout4k;

    //?????????item????????????4K???
    @BindView(R.id.order_transition_item_title_4k)
    TextView itemTitle4k;

    //?????????item??????????????????4K???
    @BindView(R.id.order_transition_item_title_4k_layout)
    LinearLayout orderTransitionItemTitle4kLayout;

    //?????????item????????????
    @BindView(R.id.order_paymode_item_discount)
    TextView itemDiscount;

    //?????????item??????
    @BindView(R.id.order_paymode_price)
    TextView orderPaymodePrice;

    //?????????item????????????
    @BindView(R.id.order_paymode_price_mode)
    TextView orderPaymodePriceMode;

    //?????????item????????????
    @BindView(R.id.order_paymode_price_layout)
    LinearLayout orderPaymodePriceLayout;

    //?????????item??????
    @BindView(R.id.order_paymode_item_discountprice)
    TextView itemDiscountprice;

    //?????????item ????????????/??????
    @BindView(R.id.order_paymode_item_mode)
    TextView itemMode;

    //?????????????????????
    @BindView(R.id.order_transition_sellingpoint)
    ImageView sellingpointImage;

    //???????????????
    @BindView(R.id.order_transition_describe)
    VerticalScrollTextView describe;

    //?????????????????????
    @BindView(R.id.order_transition_event_describe_title)
    TextView eventDescribeTitle;

    //???????????????????????????
    @BindView(R.id.order_transition_event_describe)
    VerticalScrollTextView eventDescribe;

    //??????????????????
    @BindView(R.id.order_transition_confirm_btn)
    Button confirmBtn;

    //????????????
    @BindView(R.id.order_transition_recommend_list)
    RecyclerView recommendList;

    //?????????????????????
    @BindView(R.id.order_transition_item_layout)
    RelativeLayout itemLayout;

    //???????????????????????????
    @BindView(R.id.order_transition_event_describe_layout)
    RelativeLayout eventDescribeLayout;

    //????????????
    @BindView(R.id.order_transition_bg)
    ImageView orderTransitionBg;

    //????????????text
    @BindView(R.id.order_transition_recommend_textview)
    TextView orderTransitionRecommendTextview;

    //????????????????????????
    TabItemPresenter presenter = new TabItemPresenter();

    private VODListCallBack mVodListCallBack = new OrderVodSubjectCallBack();

    //????????????Adapter
    private OrderTransitionListAdapter mAdapter;


    private ProductOrderPresenter mPresenter = new ProductOrderPresenter();

    private boolean isVODSubscribe = false;

    private boolean isTVODSubscribe = false;

    private boolean isTrySee = false;

    private boolean isOffScreen = false;

    private static final int REQUEST_CODE = 110;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private VODDetail mVODDetail;

    //????????????????????????
    private Product mProductInfo;

    private UniPayInfo mainUniPayInfo;

    //??????????????????
    private List<VOD> recommendVodList = new ArrayList<VOD>();


    //product??????????????????????????????????????????????????????????????????6???/??????
    private final static String eventSloganKey = "EVENT_SLOGAN";

    //???????????? ????????????
    private static final String cornreMarketInfo = "CORNRE_MARKER_INFO";

    //???????????? ?????????????????????
    private static final String orgPriceKey = "ORIGINAL_PRICE";

    /**
     * ????????????????????????,intent????????????????????????key
     */
    public static final String AUTHORIZE_RESULT = "authorize_result";

//    //????????????????????????subjectId,??????????????????
//    public static final String subjectId = "catauto2000011082";

    /**
     * ???????????????????????????subjectID
     */
    String ORDER_TRANSITION_VODLIST_SUBJECTID = "order_transition_vodlist_subjectid";


    /**
     * ??????????????????
     */
    public Product marketingProduct;



    //[UBD]??????ID  ??????????????????????????????sceneId      ????????????????????????????????????subjectId
    private String ubd_sceneId;

    //[UBD]???????????? ??????????????????????????????identifyType ???????????????-1
    private String ubd_recommendType;


    /**
     * ???????????????????????????
     */
    private boolean isSupportPayBenefit = true;

    private String[] mChannelMediaIds = null;

    private List<String> unsubProdInfoIds;

    private List<OfferInfo> offerInfos;

    /******************************************????????????***********************************/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_transition);
        ButterKnife.bind(this);

        mPresenter.attachView(this);

        isVODSubscribe = getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISVOD_SUBSCRIBE, false);
        isTVODSubscribe = getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISTVOD_SUBSCRIBE, false);
        isTrySee = getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.IS_TRY_SEE_SUBSCRIBE, false);
        isOffScreen = getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISOFFSCREEN_SUBSCRIBE, false);
        mChannelMediaIds = getIntent().getStringArrayExtra(ZjYdUniAndPhonePayActivty.CHANNELID_MEDIAID);

        String vodDetailJson = getIntent().getStringExtra(ZjYdUniAndPhonePayActivty.VOD_DETAIL);
        if (!TextUtils.isEmpty(vodDetailJson)) {
            mVODDetail = JsonParse.json2Object(vodDetailJson, VODDetail.class);
        }
        String marketingProductStr = getIntent().getStringExtra(ZjYdUniAndPhonePayActivty.MARKETING_PRODUCT);
        if (!TextUtils.isEmpty(marketingProductStr)) {
            marketingProduct = JsonParse.json2Object(marketingProductStr, Product.class);
        }

        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * ????????????
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //??????????????????????????????
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            finish();
        }
    }

    /******************************************???????????????***********************************/

    /**
     * ???????????????
     */
    private void initData() {
        String billId = SessionService.getInstance().getSession().getAccountName();
        //??????????????????????????????
        if (!TextUtils.isEmpty(billId)) {
            QueryMultiqryRequest request = new QueryMultiqryRequest();
            request.setMessageID("0000");
            request.setBillID(billId);
            request.setValidType("1");
            mPresenter.queryMultiqry(request);
        } else {
            mPresenter.generateProductList(getIntent().getStringExtra(AUTHORIZE_RESULT));
        }

    }

    /**
     * ????????????
     */
    private void initView() {

        eventDescribe.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    return true;
                }
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    return true;
                }
                return false;
            }
        });

        describe.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    return true;
                }
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    return true;
                }
                return false;
            }
        });

        confirmBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v instanceof Button) {
                    Button btn = (Button) v;
                    if (hasFocus) {
                        btn.setTextColor(getResources().getColor(R.color.white_0));
                        rootView.smoothScrollTo(0, 0);
                    } else {
                        btn.setTextColor(getResources().getColor(R.color.order_paymode_buttom_textcolor));
                    }
                }
            }
        });
        confirmBtn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    return true;
                }
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    return true;
                }
                return false;
            }
        });
        confirmBtn.setOnClickListener(this);
    }

    private void freshUIOnMain(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                freshUI();
            }
        });
    }

    //???????????????????????????????????????
    private void freshUI() {

        if (null == mProductInfo) {
            return;
        }

        confirmBtn.setVisibility(View.VISIBLE);

        itemLayout.setVisibility(View.VISIBLE);

        itemTitleWithout4k.setSelected(true);
        itemTitle4k.setSelected(true);

        Product product = mProductInfo;

        if (null != marketingProduct) {
            product = marketingProduct;
        }

        //????????????
        String name = mProductInfo.getName();
        if (name.contains("???4k") || name.contains("???4K")) {
            orderTransitionItemTitle4kLayout.setVisibility(View.VISIBLE);
            itemTitle4k.setVisibility(View.VISIBLE);
            itemTitleWithout4k.setVisibility(View.INVISIBLE);
            String temStr = name.substring(0, name.length() - 5);
            itemTitle4k.setText(temStr);
        } else {
            orderTransitionItemTitle4kLayout.setVisibility(View.INVISIBLE);
            itemTitleWithout4k.setVisibility(View.VISIBLE);
            itemTitleWithout4k.setText(name);
        }

        //???????????????
        double price = Double.parseDouble(product.getPrice()) / 100;
        orderPaymodePrice.setText(decimalFormat.format(price));

        List<NamedParameter> listNp = product.getCustomFields();
        if (null != marketingProduct) {
            //??????????????????
            List<String> marketInfo = CommonUtil.getCustomNamedParameterByKey(listNp, cornreMarketInfo);
            if (null != marketInfo && marketInfo.size() > 0) {
                String str = marketInfo.get(0);
                if (!TextUtils.isEmpty(str)) {
                    itemDiscount.setVisibility(View.VISIBLE);
                    itemDiscount.setText(str);
                } else {
                    itemDiscount.setText("");
                    itemDiscount.setVisibility(View.INVISIBLE);
                }
            } else {
                itemDiscount.setText("");
                itemDiscount.setVisibility(View.INVISIBLE);
            }

            //??????????????????
            List<String> orgPrice = CommonUtil.getCustomNamedParameterByKey(listNp, orgPriceKey);
            if (null != orgPrice && orgPrice.size() > 0) {
                String str = orgPrice.get(0);
                if (!TextUtils.isEmpty(str)) {
                    SpannableString spannableString = new SpannableString(str);
                    StrikethroughSpan colorSpan = new StrikethroughSpan();
                    spannableString.setSpan(colorSpan, 0, spannableString.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    itemDiscountprice.setVisibility(View.VISIBLE);
                    itemDiscountprice.setText(spannableString);
                } else {
                    itemDiscountprice.setVisibility(View.GONE);
                    itemDiscountprice.setText("");
                }
            } else {
                itemDiscountprice.setVisibility(View.GONE);
                itemDiscountprice.setText("");
            }
        } else {
            itemDiscount.setText("");
            itemDiscount.setVisibility(View.INVISIBLE);
            itemDiscountprice.setVisibility(View.GONE);
            itemDiscountprice.setText("");
        }
        /*
         * ????????????,0 ????????????;1 ?????????
         */
        if ((!TextUtils.isEmpty(mProductInfo.getProductType()) && mProductInfo.getProductType().equals("1"))) {
            orderPaymodePriceMode.setText("???/???");
            //???????????????????????????
            String indateValue = mPresenter.analyticValidity(mProductInfo);
            //???????????????
            if (!TextUtils.isEmpty(indateValue)) {
                itemMode.setVisibility(View.VISIBLE);
                String valideHint = !TextUtils.isEmpty(mProductInfo.getGstCode()) ? "(" + mProductInfo.getGstCode() + ")" : "";
                itemMode.setText("????????????" + indateValue + valideHint);
            } else {
                itemMode.setVisibility(View.INVISIBLE);
            }
        } else {
            if ("0".equals(mProductInfo.getChargeMode()) || "13".equals(mProductInfo.getChargeMode())) {
                itemMode.setVisibility(View.VISIBLE);
                itemMode.setText("????????????");
            } else {
                itemMode.setVisibility(View.INVISIBLE);
            }
            orderPaymodePriceMode.setText("???/" + StringUtils.analyticValidity(mProductInfo));
        }

        View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                VerticalScrollTextView view = (VerticalScrollTextView)v;
                view.setSelected(hasFocus);
                view.setAuto(hasFocus);

            }
        };

        describe.setOnFocusChangeListener(listener);
        eventDescribe.setOnFocusChangeListener(listener);

        //???????????????
        describe.setVisibility(View.VISIBLE);
        describe.setText(mProductInfo.getIntroduce());
//        describe.setAuto(true);
//        describe.setSelected(true);

        //?????????????????????
        if (null != marketingProduct) {
            eventDescribeLayout.setVisibility(View.VISIBLE);
            eventDescribe.setVisibility(View.VISIBLE);
            eventDescribe.setText(marketingProduct.getIntroduce());
//            eventDescribe.setSelected(true);
//            eventDescribe.setAuto(true);
        }

        //????????????????????????
        List<String> eventSlogan = CommonUtil.getCustomNamedParameterByKey(listNp, eventSloganKey);
        if (null != eventSlogan && eventSlogan.size() > 0) {
            String str = eventSlogan.get(0);
            if (!TextUtils.isEmpty(str)) {
                eventSloganTextview.setVisibility(View.VISIBLE);
                eventSloganTextview.setText(str);
            } else {
                eventSloganTextview.setText("");
                eventSloganTextview.setVisibility(View.GONE);
            }
        } else {
            eventSloganTextview.setText("");
            eventSloganTextview.setVisibility(View.GONE);
        }

        String url = "";
        String sellPointUrl = "";
        String bgUrl = "";

        Picture picture = product.getPicture();
        Picture orgPicture = mProductInfo.getPicture();

        List<String> urlList = orgPicture.getDrafts();
        if (null != urlList && urlList.size() > 0) {
            url = urlList.get(0).toString();
        }

        List<String> pointUrlList = picture.getStills();
        if (null != pointUrlList && pointUrlList.size() > 0) {
            sellPointUrl = pointUrlList.get(0).toString();
        }

        //?????????????????????????????????url,?????????????????????
        if ("".equals(sellPointUrl)){
            List<String> pointUrlListOrg = orgPicture.getStills();
            if (null != pointUrlListOrg && pointUrlListOrg.size()>0){
                sellPointUrl = pointUrlListOrg.get(0).toString();
            }
        }

        List<String> posterList = picture.getPosters();
        if (null != posterList && posterList.size() > 0) {
            bgUrl = posterList.get(0).toString();
        }

        //?????????????????????????????????url,?????????????????????
        if ("".equals(bgUrl)){
            List<String> posterListOrg = orgPicture.getPosters();
            if (null != posterListOrg && posterListOrg.size()>0){
                bgUrl = posterListOrg.get(0).toString();
            }
        }


        RequestOptions options = new RequestOptions()
                .placeholder(null)
                .error(null);
        titleImage.setVisibility(View.VISIBLE);
        sellingpointImage.setVisibility(View.VISIBLE);
        Glide.with(this).load(url).apply(options).into(titleImage);
        Glide.with(this).load(sellPointUrl).apply(options).into(sellingpointImage);
        Glide.with(this).load(bgUrl).apply(options).into(orderTransitionBg);
        Log.i(TAG, "freshUI:  " + bgUrl);

        confirmBtn.setTextColor(getResources().getColor(R.color.white_0));
        confirmBtn.requestFocus();


        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //????????????
                rootView.scrollTo(0, 0);

                //????????????????????????
                if (recommendVodList.size() > 0) {

                    LinearLayoutManager manager = new LinearLayoutManager(ProductTransitionActivity.this);
                    manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recommendList.setFocusableInTouchMode(false);
                    recommendList.setLayoutManager(manager);
                    recommendList.addItemDecoration(new SpaceItemDecoration(ProductTransitionActivity.this, getResources().getDimensionPixelSize(R.dimen.margin_14)));
                    mAdapter = new OrderTransitionListAdapter(recommendVodList, ProductTransitionActivity.this);
                    recommendList.setAdapter(mAdapter);
                    mAdapter.setOnitemClickLintener(ProductTransitionActivity.this);

                    recommendList.setVisibility(View.VISIBLE);
                    orderTransitionRecommendTextview.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                } else {
                    orderTransitionRecommendTextview.setVisibility(View.GONE);
                    recommendList.setVisibility(View.GONE);
                }
            }
        }, 50);
    }

    /**
     * ?????????????????????
     */
    public void switchPhonePayActivity(UniPayInfo mainUniPayInfo, String payType) {
        Intent intent = new Intent();
        intent.setClassName(getPackageName(), NewMyPayModeActivity.class.getName());
        if (SessionService.getInstance().getSession().isHotelUser()) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.SHOW_ITEM_MODE,
                    ZjYdUniAndPhonePayActivty.HOTEL_MODE);
        }
        if (isMouthProduct(mProductInfo)) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.SHOW_ITEM_MODE,
                    ZjYdUniAndPhonePayActivty.MONTH_PRODUCT_MODE);
        }
        if (ZjYdUniAndPhonePayActivty.QRCODE_FLAG.equals(payType)) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.UNI_PAY_TYPE,
                    ZjYdUniAndPhonePayActivty.UNI_CODE_MODE);
        } else if (ZjYdUniAndPhonePayActivty.UNIPAY_FLAG.equals(payType)) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.UNI_PAY_TYPE,
                    ZjYdUniAndPhonePayActivty.UNI_PHONE_PAY_MODE);
        } else if (ZjYdUniAndPhonePayActivty.UNIPAY_PHONE_CODE_FLAG.equals(payType)) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.UNI_PAY_TYPE,
                    ZjYdUniAndPhonePayActivty.UNI_CHILDREN_PAY_MODE);
        } else if (ZjYdUniAndPhonePayActivty.UNONPENED_ACCOUNT.equals(payType)) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.UNI_PAY_TYPE,
                    ZjYdUniAndPhonePayActivty.UNONPENED_UNI_MODE);
        }
        intent.putExtra(ZjYdUniAndPhonePayActivty.PRODUCT_INFO,
                JsonParse.object2String(mProductInfo));
        if (null != mVODDetail) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.VOD_DETAIL,
                    JsonParse.object2String(mVODDetail));
        }
        if (null != marketingProduct) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.MARKETING_PRODUCT,
                    JsonParse.object2String(marketingProduct));
        }
        if (null != unsubProdInfoIds && unsubProdInfoIds.size() > 0) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.UNSUBPRODINFOIDS,
                    JsonParse.listToJsonString(unsubProdInfoIds));
        }
        intent.putExtra(ZjYdUniAndPhonePayActivty.UNIPAY_EXPTIME,
                mPresenter.analyticValidity(mProductInfo));
        if (null != mainUniPayInfo) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.UNIPAY_BILLID, mainUniPayInfo.getBillID());
        }
        intent.putExtra(ZjYdUniAndPhonePayActivty.ISVOD_SUBSCRIBE, isVODSubscribe);
        intent.putExtra(ZjYdUniAndPhonePayActivty.ISTVOD_SUBSCRIBE, isTVODSubscribe);
        intent.putExtra(ZjYdUniAndPhonePayActivty.CHANNELID_MEDIAID, mChannelMediaIds);
        intent.putExtra(ZjYdUniAndPhonePayActivty.IS_TRY_SEE_SUBSCRIBE, isTrySee);
        intent.putExtra(ZjYdUniAndPhonePayActivty.ISOFFSCREEN_SUBSCRIBE, isOffScreen);
        intent.putExtra(ZjYdUniAndPhonePayActivty.ISORDERCENTER,
                getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISORDERCENTER, false));

        if (getIntent().getSerializableExtra(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE) != null) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE,
                    getIntent().getSerializableExtra(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE));
        }
        /*?????????????????????????????????????????????????????????*/
        if (isOrderByOrder(mProductInfo) && isSupportPayBenefit) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.IS_ORDER_BY_ORDER, true);
        }
        // 2018/10/10 ????????????????????? ADD START
        // ?????????????????????????????????????????????
        if (PaymentUtils.isProductSupportThirdPartPayment(mProductInfo)) {
            QueryMultiUserInfoRequest request = new QueryMultiUserInfoRequest();
            String billID = SessionService.getInstance().getSession().getAccountName();
            if (!TextUtils.isEmpty(billID)) {
                request.setBillID(billID);
                request.setMessageID(PaymentUtils.generateMessageID());
                mPresenter.vssQueryMultiUserInfo(request, this, intent);
                return;
            } else {
                SuperLog.error(TAG, String.format("Get QueryMultiUserInfoRequest parameter " +
                        "'billId', the 'billId' is: %s", billID));
                // ?????????????????????????????????????????????????????????
                intent.putExtra(NewMyPayModeActivity.NEED_SHOW_THIRD_PART_PAYMENT, false);
                intent.putExtra(NewMyPayModeActivity.NEED_SHOW_USER_STATE_VALIDATION_FAILED_NOTIFICATION, true);
            }
        }
        // 2018/10/10 ????????????????????? ADD END
        startActivityForResult(intent, REQUEST_CODE);

    }

    public boolean isMouthProduct(Product mProductInfo) {
        return null != mProductInfo && "0".equals(mProductInfo.getProductType()) && "0".equals(mProductInfo.getChargeMode()) && "1".equals(mProductInfo.getPeriodLength()) && "1".equals(mProductInfo.getIsAutoExtend());
    }

    /**
     * ??????????????????
     */
    public boolean isOrderByOrder(Product mProductInfo) {
        return null != mProductInfo && "1".equals(mProductInfo.getProductType());
    }

    /******************************************ProductOrderContract***********************************/

    //??????????????????????????????????????????
    @Override
    public void generateProductListSucc(List<Product> productList) {
        if (productList.size() > 0) {
            mProductInfo = productList.get(0);
            //????????????????????????
            boolean needRemixRex = RemixRecommendUtil.getRemixRecommendSwitch(RemixRecommendUtil.RemixRec_Order);
            if (needRemixRex){
                presenter.queryPBSRemixRecommend(null,"",mProductInfo.getID(), new EpgTopFunctionMenu.OnPBSRemixRecommendListener() {
                    @Override
                    public void getRemixRecommendData(PBSRemixRecommendResponse pbsRemixRecommendResponse) {
                        if ( CollectionUtil.isEmpty(pbsRemixRecommendResponse.getRecommends()) || CollectionUtil.isEmpty(pbsRemixRecommendResponse.getRecommends().get(0).getVODs())){
                            //?????????????????????????????????????????????????????????
                            getArtificialRecommend();
                        } else {
                            Recommend recommend = pbsRemixRecommendResponse.getRecommends().get(0);
                            List<VOD> listVod   = recommend.getVODs();
                            if (listVod.size() == 0){
                                //?????????????????????????????????????????????
                                getArtificialRecommend();
                            }else{//vods.size() > 0
                                //???????????????????????????5?????????5???
                                recommendVodList = listVod.size() > 5 ? listVod.subList(0,5):listVod;
                                //??????UBD????????????
                                String recommendContentIDs = UBDRecommendImpression.getRecommendContentIDs(recommendVodList);
                                ubd_sceneId = recommend.getSceneId();
                                ubd_recommendType = String.valueOf(recommend.getIdentifyType());
                                UBDRecommendImpression.record(RemixRecommendUtil.APPPINEDID_ORDER,ubd_sceneId,recommendContentIDs,ubd_recommendType,mProductInfo.getID(),null);
                                freshUIOnMain();
                            }
                        }
                    }

                    @Override
                    public void getRemixRecommendDataFail() {
                        SuperLog.error(TAG, "?????????????????????????????????????????????");
                        getArtificialRecommend();
                    }
                }, RemixRecommendUtil.APPPINEDID_ORDER,"6");
            } else {
                getArtificialRecommend();
            }
        }
    }

    //?????????????????????????????????
    private void getArtificialRecommend(){
        //??????????????????????????????????????????SubjectID
        List<NamedParameter> listNp = mProductInfo.getCustomFields();
        String subjectId = "";
        List<String> recommendList = CommonUtil.getCustomNamedParameterByKey(listNp,ORDER_TRANSITION_VODLIST_SUBJECTID);
        if (null != recommendList && recommendList.size()>0){
            subjectId = recommendList.get(0);
        }
        SuperLog.info2SD(TAG,"Manual recommend subjectID = " + subjectId);
        if (null != subjectId && subjectId.length() > 0) {
            new MoviesListPresenter(this).loadMoviesContent(subjectId, "0", "5", mVodListCallBack);
        }else{
            //????????????????????????????????????,?????????????????????
            freshUIOnMain();
        }
    }

    //?????????????????????????????????
    @Override
    public void generateProductListError() {}

    /**
     * ????????????????????????
     */
    @Override
    public void queryUniPayInfoSucc(QueryUniPayInfoResponse response) {
        mainUniPayInfo = mPresenter.resolveMainUniPayInfo(response);
        if (null != mainUniPayInfo) {
            if (TextUtils.isEmpty(mainUniPayInfo.getBillID())) {
                //?????????????????????
                switchPhonePayActivity(null, ZjYdUniAndPhonePayActivty.UNONPENED_ACCOUNT);
            } else {
                mPresenter.queryUserOrderingSwitch();
            }
        } else {
            //?????????????????????????????????????????????
            switchPhonePayActivity(null, ZjYdUniAndPhonePayActivty.QRCODE_FLAG);
        }
    }

    @Override
    public void queryUniPayInfoError() {
        //?????????????????????
        switchPhonePayActivity(null, ZjYdUniAndPhonePayActivty.UNONPENED_ACCOUNT);
    }

    @Override
    public void querySubscriberSucess(String orderingSwitch) {
        //?????????????????????
        if (!TextUtils.isEmpty(orderingSwitch) && "1".equals(orderingSwitch)) {
            switchPhonePayActivity(mainUniPayInfo, ZjYdUniAndPhonePayActivty.UNIPAY_PHONE_CODE_FLAG);
        } else {
            switchPhonePayActivity(mainUniPayInfo, ZjYdUniAndPhonePayActivty.UNIPAY_FLAG);
        }

    }

    @Override
    public void querySubscriberfail() {
        switchPhonePayActivity(mainUniPayInfo, ZjYdUniAndPhonePayActivty.UNIPAY_PHONE_CODE_FLAG);
    }

    @Override
    public void queryMultiqrySuccess(QueryMultiqryResponse response) {
        offerInfos = response.getOfferList();
        mPresenter.generateProductList(getIntent().getStringExtra(AUTHORIZE_RESULT));
    }

    @Override
    public void queryMultiqryFail() {
        mPresenter.generateProductList(getIntent().getStringExtra(AUTHORIZE_RESULT));
    }

    @Override
    public void queryMultiUserInfoSuccess(QueryMultiUserInfoResponse response, Intent intent) {
        if (PaymentUtils.isUserSupportThirdPartPayment(response.getUserState(), response.getRealNameFlag())) {
            intent.putExtra(NewMyPayModeActivity.NEED_SHOW_THIRD_PART_PAYMENT, true);
        } else {
            intent.putExtra(NewMyPayModeActivity.NEED_SHOW_THIRD_PART_PAYMENT, false);
            intent.putExtra(NewMyPayModeActivity.NEED_SHOW_USER_STATE_VALIDATION_FAILED_NOTIFICATION, true);
        }
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void queryMultiUserInfoFail(Intent intent) {
        intent.putExtra(NewMyPayModeActivity.NEED_SHOW_THIRD_PART_PAYMENT, false);
        intent.putExtra(NewMyPayModeActivity.NEED_SHOW_USER_STATE_VALIDATION_FAILED_NOTIFICATION, true);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /******************************************??????????????????***********************************/
    private boolean canClick = true;
    @Override
    public void onClick(View v) {

        //???????????????????????????
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
        },2000);

        //????????????????????????
        boolean canpay = AvoidRepeatPaymentUtils.getInstance().canPay(mProductInfo.getID());
        if (!canpay){
            EpgToast.showToast(this, "??????????????????");
            return;
        }

        if (v.getId() == R.id.order_transition_confirm_btn) {
            //?????????????????????????????????
            if (null != offerInfos && offerInfos.size() > 0) {
                for (int i = 0; i < offerInfos.size(); i++) {
                    if (null != marketingProduct && null != marketingProduct.getMarketing() && (offerInfos.get(i).getOfferID().equals(marketingProduct.getMarketing().getId()) || offerInfos.get(i).getOfferID().equals(marketingProduct.getMarketing().getTag()))) {
                        marketingProduct = null;
                    }
                }
            }

            if (null != unsubProdInfoIds){
                unsubProdInfoIds.clear();
            }else{
                unsubProdInfoIds = new ArrayList<>();
            }
            if (mProductInfo.getProductType().equals("0")){
                //?????????????????????????????????
                ProductUtils.getProductMutExRela(this, new SelfAppInfoController.GetProductMutExRelaCallBack() {
                    @Override
                    public void GetProductMutExRelaCallBackSuccess(GetProductMutExRelaResponse response) {
                        List<SubMutex> list = response.getSubMutexs();
                        if (null != list && list.size()>0){
                            for (int i = 0; i < list.size(); i++) {
                                SubMutex subMutex = list.get(i);
                                if (subMutex.getRuleType().equals(SubMutex.RULE_TYPE_MUTEX)){
                                    String mutexStr = SessionService.getInstance().getSession().getTerminalConfigurationOrderProductMutexInfo();
                                    if (null != mutexStr && mutexStr.length()>0){
                                        EpgToast.showToast(OTTApplication.getContext(),
                                                mutexStr);
                                    }else{
                                        EpgToast.showToast(OTTApplication.getContext(),
                                                getResources().getString(R.string.big_small_product_mutex));
                                    }
                                    return;
                                }else if (subMutex.getRuleType().equals(SubMutex.RULE_TYPE_REPLACE)){
                                    unsubProdInfoIds.add(subMutex.getProductID());
                                }
                            }
                        }

                        mPresenter.queryUniPayInfo(new QueryUniInfoRequest(), ProductTransitionActivity.this);
                    }

                    @Override
                    public void GetProductMutExRelaCallBackFailed() {

                    }
                },mProductInfo,mVODDetail);
            }else{
                mPresenter.queryUniPayInfo(new QueryUniInfoRequest(), ProductTransitionActivity.this);
            }

        }
    }

    //item????????????
    @Override
    public void onItemClick(int position) {
        if (position>= 0 && position <= recommendVodList.size() - 1 ){
            VOD vod = recommendVodList.get(position);
            if (null != vod){
                //????????????????????????,?????????????????????
                Intent intent;
                if(SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
                    intent = new Intent(this, ChildModeVodDetailActivity.class);
                } else {
                    intent = new Intent(this, NewVodDetailActivity.class);
                }
                intent.putExtra(NewVodDetailActivity.VOD_ID, vod.getID());
                intent.putExtra(NewVodDetailActivity.ORGIN_VOD,vod);
                intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_APP_POINTED_ID,RemixRecommendUtil.APPPINEDID_ORDER);
                intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_RECOMMEND_TYEP,ubd_recommendType);
                intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_SCENE_ID,      ubd_sceneId);
                startActivity(intent);
            }
        }
    }

    /******************************************??????????????????***********************************/

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private static final String TAG = "CustomItemDecoration";
        private int mSpace;
        private Context mContext;

        /**
         * @param space ??????????????????????????????dp
         */
        SpaceItemDecoration(Context context, float space) {
            SuperLog.debug(TAG, "space is " + space);
            this.mContext = context;
            this.mSpace = (int) space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int itemCount = parent.getAdapter().getItemCount();
            int pos = parent.getChildAdapterPosition(view);
            SuperLog.debug(TAG, "itemCount>>" + itemCount + ";Position>>" + pos);
            outRect.left = mSpace;
            outRect.top = mContext.getResources().getDimensionPixelSize(R.dimen.history_list_space_top);
            outRect.bottom = 0;
            outRect.right = mSpace;

            if (pos == 0) {
                outRect.left = mContext.getResources().getDimensionPixelSize(R.dimen.margin_10);
            }
        }
    }

    //??????????????????????????????(??????????????????VOD??????????????????????????????)
    private class OrderVodSubjectCallBack extends VodSubjectCallBack {
        @Override
        public void queryVODSubjectListSuccess(int total, List<Subject> subjects) { }

        @Override
        public void queryVODSubjectListFailed() { }

        @Override
        public void querySubjectDetailSuccess(int total, List<Subject> subjects) { }

        @Override
        public void queryPSBRecommendSuccess(int total, List<VOD> vodDetails) {
            //No need to implement this interface
        }

        @Override
        public void queryPSBRecommendFail() {
            //No need to implement this interface
        }

        @Override
        public void onError() {}

        //??????????????????????????????
        @Override
        public void queryVODListBySubjectSuccess(int total, List<VOD> vodList, String subjectId) {
            if(!CollectionUtil.isEmpty(vodList)){
                recommendVodList = vodList.size() > 5? vodList.subList(0,5): vodList;
                //??????UBD????????????
                String recommendContentIDs = UBDRecommendImpression.getRecommendContentIDs(recommendVodList);
                ubd_sceneId       = subjectId;
                ubd_recommendType = UBDConstant.MANUAL_RECOMMEND_TYPE;
                UBDRecommendImpression.record(RemixRecommendUtil.APPPINEDID_ORDER, ubd_sceneId,recommendContentIDs,ubd_recommendType,mProductInfo.getID(),null);
            }
            freshUIOnMain();
        }

        //??????????????????????????????
        @Override
        public void queryVODListBySubjectFailed() {
            orderTransitionRecommendTextview.setVisibility(View.GONE);
            recommendList.setVisibility(View.GONE);
        }
    }

    /******************************************EventBus??????***********************************/

    @Subscribe
    public void onEvent(ProductTransitionListFocusEvent event) {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //???????????????????????????????????????
                rootView.smoothScrollBy(0, getResources().getDimensionPixelSize(R.dimen.history_list_item_height));
            }
        }, 50);
    }
}