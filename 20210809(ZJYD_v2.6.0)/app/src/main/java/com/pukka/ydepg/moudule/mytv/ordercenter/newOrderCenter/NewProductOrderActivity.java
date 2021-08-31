package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.HorizontalGridView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.reflect.TypeToken;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.extview.VerticalScrollTextView;
import com.pukka.ydepg.common.http.bean.node.Marketing;
import com.pukka.ydepg.common.http.bean.node.OfferInfo;
import com.pukka.ydepg.common.http.bean.request.QueryMultiqryRequest;
import com.pukka.ydepg.common.http.bean.response.QueryMultiqryResponse;
import com.pukka.ydepg.common.http.bean.response.QueryProductInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.CustomGroup;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.SubMutex;
import com.pukka.ydepg.common.http.v6bean.v6node.UniPayInfo;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.GetProductMutExRelaResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUniPayInfoResponse;
import com.pukka.ydepg.common.http.vss.request.QueryMultiUserInfoRequest;
import com.pukka.ydepg.common.http.vss.response.QueryMultiUserInfoResponse;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.productUtil.ProductUtilCallback;
import com.pukka.ydepg.common.utils.productUtil.ProductUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.control.selfregister.SelfAppInfoController;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.mytv.ZjYdUniAndPhonePayActivty;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.adapter.NewProductOrderAdapter;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.adapter.NewProductOrderMutiAdapter;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.event.ProductOrderMutiSwitchEvent;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.event.ProductOrderSwitchEvent;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.AvoidRepeatPaymentUtils;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.MarketingUtils;
import com.pukka.ydepg.moudule.mytv.presenter.OrderCenterPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.OrderUserGroupPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.ProductOrderPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.contract.ProductOrderContract;
import com.pukka.ydepg.moudule.mytv.presenter.view.OrderCenterView;
import com.pukka.ydepg.moudule.mytv.utils.PaymentUtils;
import com.pukka.ydepg.service.NtpTimeService;

import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewProductOrderActivity extends BaseActivity implements ProductOrderContract.View, NewProductOrderAdapter.OnitemClick, OrderCenterView {

    private static final String TAG = "NewProductOrderActivity";

    /**
     * 启动产品订购界面,intent传递的鉴权结果的key
     */
    public static final String AUTHORIZE_RESULT = "authorize_result";

    private static final int REQUEST_CODE = 110;

    //是否是一排展示
    boolean showSingleList = true;

    //如果是两排展示，存放单次订购产品包的数组
    List<Product> onceProductList = new ArrayList<Product>();

    //如果是两排展示，存放包周期订购产品包的数组
    List<Product> cycleProductList = new ArrayList<Product>();


    //顶部宣传标语
    @BindView(R.id.order_list_slogan)
    TextView orderListSlogan;

    //单行产品包布局
    @BindView(R.id.recyclerview_layout_single)
    RelativeLayout recyclerviewLayoutSingle;

    //产品名称
    @BindView(R.id.order_list_title_image)
    ImageView orderListTitleImage;

    //产品卖点，一行产品包时展示
    @BindView(R.id.order_list_title_selling_point)
    ImageView orderListTitleSellingPoint;

    //可订购产品列表，产品大于5个时展示这个可滚动的
    @BindView(R.id.order_list_list)
    HorizontalGridView orderListList;

    //可订购产品列表，产品小于5个时展示这个可适应大小的
    @BindView(R.id.order_list_list_auto)
    RecyclerView orderListListAuto;

    //产品描述(产品包1-3个时显示这个)
    @BindView(R.id.order_list_des)
    VerticalScrollTextView orderListDes;

    //产品描述(产品包4-5个时显示这个)
    @BindView(R.id.order_list_des_align)
    VerticalScrollTextView orderListDesAlign;

    //5个以上展示这个
    @BindView(R.id.order_list_des_over)
    VerticalScrollTextView orderListDesOver;

    //两行行产品包布局
    @BindView(R.id.recyclerview_layout_multi)
    RelativeLayout recyclerviewLayoutMulti;

    @BindView(R.id.order_list_title_image_multi)
    ImageView orderListTitleImageMulti;

    //上方的可续订产品，大于4个时展示这个可滚动的
    @BindView(R.id.order_list_list_up)
    HorizontalGridView orderListListUp;

    //上方的可续订产品，小于4个时展示这个可自适应大小的
    @BindView(R.id.order_list_list_up_auto)
    RecyclerView orderListListUpAuto;

    //下方的不可续订产品，大于4个时展示这个可滚动的
    @BindView(R.id.order_list_list_down)
    HorizontalGridView orderListListDown;

    //下方的不可续订产品，小于4个时展示这个可自适应大小的
    @BindView(R.id.order_list_list_down_auto)
    RecyclerView orderListListDownAuto;

    //两行时的产品描述(产品包1-3个时显示这个)
    @BindView(R.id.order_list_des_align_multi)
    VerticalScrollTextView orderListDesAlignMulti;

    //两行时的产品描述(产品包4-5个时显示这个)
    @BindView(R.id.order_list_des_multi)
    VerticalScrollTextView orderListDesMulti;



    private ProductOrderPresenter mPresenter = new ProductOrderPresenter();

    private OrderCenterPresenter orderCenterPresenter = new OrderCenterPresenter();

    private OrderUserGroupPresenter getUserGroupPresenter = new OrderUserGroupPresenter();

    private boolean isVODSubscribe = false;

    private boolean isTVODSubscribe = false;

    private boolean isTrySee = false;

    private boolean isOffScreen = false;

    //product拓展参数，活动标语（新人送好礼，新用户首月仅6元/月）
    private final static String eventSloganKey = "EVENT_SLOGAN";

    //product扩展参数，营销活动信息
    private final static String zjProductOfSales = "zjProductOfSales";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");


    /**
     * 是否支持观影券功能
     */
    private boolean isSupportPayBenefit = true;

    private String[] mChannelMediaIds = null;

    private UniPayInfo mainUniPayInfo;

    //当前选中的产品包
    private Product mProductInfo;

    /**
     * 没有billid,请求用户信息报错true
     */
    private boolean isInVisiableMarket;

    private VODDetail mVODDetail;

    //adapter
    NewProductOrderAdapter mAdapter;

    NewProductOrderMutiAdapter mutiAdapterForOnce;
    NewProductOrderMutiAdapter mutiAdapterForCycle;

    //可订购产品包列表
    List<Product> productList = new ArrayList<>();

    private List<OfferInfo> offerInfos;

    private List<String> unsubProdInfoIds;

    //储存营销活动产品包  key:关联的一级产品包productId, value:该一级产品包下生效的营销产品包
    Map<String, Product> productMap = new HashMap<String, Product>();

    //key:一级产品包productId ，value：关联的营销产品包的offerId
    Map<String, String> MarkerProductIdMap = new HashMap<String, String>();

    //key:一级产品包productId ，value：关联的营销产品包的营销信息marketing
    Map<String, Marketing> ProductMarketing = new HashMap<String, Marketing>();

    //用于查询营销产品包的productIDs
    List<String> productIds = new ArrayList<>();

    /**
     * 营销活动产品
     */
    private Product marketingProduct;

    /******************************************生命周期***********************************/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_orderlist_new);
        ButterKnife.bind(this);

        mPresenter.attachView(this);
        orderCenterPresenter.attachView(this);

        isVODSubscribe = getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISVOD_SUBSCRIBE, false);
        isTVODSubscribe = getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISTVOD_SUBSCRIBE, false);
        isTrySee = getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.IS_TRY_SEE_SUBSCRIBE, false);
        isOffScreen = getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISOFFSCREEN_SUBSCRIBE, false);
        mChannelMediaIds = getIntent().getStringArrayExtra(ZjYdUniAndPhonePayActivty.CHANNELID_MEDIAID);

        String vodDetailJson = getIntent().getStringExtra(ZjYdUniAndPhonePayActivty.VOD_DETAIL);
        if (!TextUtils.isEmpty(vodDetailJson)) {
            mVODDetail = JsonParse.json2Object(vodDetailJson, VODDetail.class);
        }

        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 界面销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        orderCenterPresenter.detachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //关闭产品订购列表界面
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            finish();
        }
    }


    /******************************************使用到的自定义方法***********************************/

    /**
     * 初始化数据
     */
    private void initData() {
        mAdapter = new NewProductOrderAdapter(this, productList, mPresenter, productMap);

        orderListList.setNumRows(1);
        orderListList.addItemDecoration(new SpaceItemDecoration(this, getResources().getDimensionPixelSize(R.dimen.margin_26)));

        //绑定点击事件
        mAdapter.setOnitemClickLintener(this);

        orderListList.setAdapter(mAdapter);

        mutiAdapterForOnce = new NewProductOrderMutiAdapter(this, onceProductList, mPresenter, productMap);
        //绑定点击事件
        mutiAdapterForOnce.setOnitemClickLintener(this);
        orderListListDown.setNumRows(1);
        orderListListDown.addItemDecoration(new SpaceItemDecoration(this, getResources().getDimensionPixelSize(R.dimen.margin_26)));
        orderListListDown.setAdapter(mutiAdapterForOnce);

        mutiAdapterForCycle = new NewProductOrderMutiAdapter(this, cycleProductList, mPresenter, productMap);
        //绑定点击事件
        mutiAdapterForCycle.setOnitemClickLintener(this);
        orderListListUp.setNumRows(1);
        orderListListUp.addItemDecoration(new SpaceItemDecoration(this, getResources().getDimensionPixelSize(R.dimen.margin_26)));
        orderListListUp.setAdapter(mutiAdapterForCycle);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        orderListListAuto.setLayoutManager(manager);
        orderListListAuto.addItemDecoration(new SpaceItemDecoration(this, getResources().getDimensionPixelSize(R.dimen.margin_26)));
        orderListListAuto.setAdapter(mAdapter);

        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        orderListListUpAuto.setLayoutManager(manager1);
        orderListListUpAuto.addItemDecoration(new SpaceItemDecoration(this, getResources().getDimensionPixelSize(R.dimen.margin_26)));
        orderListListUpAuto.setAdapter(mutiAdapterForCycle);

        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        manager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        orderListListDownAuto.setLayoutManager(manager2);
        orderListListDownAuto.addItemDecoration(new SpaceItemDecoration(this, getResources().getDimensionPixelSize(R.dimen.margin_26)));
        orderListListDownAuto.setAdapter(mutiAdapterForOnce);


        String billId = SessionService.getInstance().getSession().getAccountName();
        //生成可订购的产品列表

        if (!TextUtils.isEmpty(billId)) {
            isInVisiableMarket = false;
            QueryMultiqryRequest request = new QueryMultiqryRequest();
            //TODO
            request.setMessageID("0000");
            request.setBillID(billId);
            request.setValidType("1");
            mPresenter.queryMultiqry(request);
        } else {
            isInVisiableMarket = true;
            mPresenter.generateProductList(getIntent().getStringExtra(AUTHORIZE_RESULT));
        }

        orderListListDown.setWindowAlignment(BaseGridView.WINDOW_ALIGN_LOW_EDGE);
        orderListListUp.setWindowAlignment(BaseGridView.WINDOW_ALIGN_LOW_EDGE);
        orderListList.setWindowAlignment(BaseGridView.WINDOW_ALIGN_LOW_EDGE);

        orderListListDown.setWindowAlignmentOffsetPercent(81.5f);
        orderListListUp.setWindowAlignmentOffsetPercent(81.5f);
        orderListList.setWindowAlignmentOffsetPercent(80f);

    }

    /**
     * 加载视图
     */
    private void initView() {

    }

    /**
     * 当前选中产品包切换，刷新页面
     */
    private void freshUI() {

        //防止页面已经回收，仍加载页面
        if (isFinishing()){
            return;
        }

        try {
            Product product = mProductInfo;
            if (null != productMap && null != productMap.get(mProductInfo.getID())){
                product = productMap.get(mProductInfo.getID());
            }
            if (null == product){
                return;
            }

            List<NamedParameter> listNp = product.getCustomFields();
            //获取当前活动标语
            List<String> marketInfo = CommonUtil.getCustomNamedParameterByKey(listNp, eventSloganKey);
            if (null != marketInfo && marketInfo.size() > 0) {
                String str = marketInfo.get(0);
                if (!TextUtils.isEmpty(str)) {
                    orderListSlogan.setVisibility(View.VISIBLE);
                    orderListSlogan.setText(str);
                } else {
                    orderListSlogan.setText("");
                    orderListSlogan.setVisibility(View.INVISIBLE);
                }
            } else {
                orderListSlogan.setText("");
                orderListSlogan.setVisibility(View.INVISIBLE);
            }

            String url = "";
            String sellPointUrl = "";

            Picture picture = product.getPicture();
            Picture orgPicture = mProductInfo.getPicture();

            List<String> urlList = orgPicture.getDrafts();
            if (null != urlList && urlList.size() > 0) {
                url = urlList.get(0).toString();
            }

            List<String> pointUrlList = picture.getBackgrounds();
            if (null != pointUrlList && pointUrlList.size() > 0) {
                sellPointUrl = pointUrlList.get(0).toString();
            }
            //二级产品包取不到的时候回去取一级的
            if ("".equals(sellPointUrl)){
                List<String> pointUrlListOrg = orgPicture.getBackgrounds();
                if (null != pointUrlListOrg && pointUrlListOrg.size()>0){
                    sellPointUrl = pointUrlListOrg.get(0).toString();
                }
            }

            RequestOptions options = new RequestOptions()
                    .placeholder(null)
                    .error(null);

            if (recyclerviewLayoutSingle.getVisibility() == View.VISIBLE) {
                Glide.with(this).load(url).apply(options).into(orderListTitleImage);
                Glide.with(this).load(sellPointUrl).apply(options).into(orderListTitleSellingPoint);
            } else {
                Glide.with(this).load(url).apply(options).into(orderListTitleImageMulti);
            }
            //产品包描述
            //描述始终取原有产品包的
            String introduce = mProductInfo.getIntroduce();

            if (showSingleList) {
                if (productList.size() > 3 && productList.size() < 6) {
                    orderListDesOver.setVisibility(View.GONE);
                    orderListDes.setVisibility(View.GONE);
                    orderListDesAlign.setVisibility(View.VISIBLE);
                    orderListDesAlign.setText(introduce);
                    orderListDesAlign.setAuto(true);
                    orderListDesAlign.setSelected(true);

                } else if (productList.size() < 4) {
                    orderListDesOver.setVisibility(View.GONE);
                    orderListDes.setVisibility(View.VISIBLE);
                    orderListDesAlign.setVisibility(View.GONE);
                    orderListDes.setText(introduce);
                    orderListDes.setAuto(true);
                    orderListDes.setSelected(true);
                } else {
                    orderListDesOver.setVisibility(View.VISIBLE);
                    orderListDes.setVisibility(View.GONE);
                    orderListDesAlign.setVisibility(View.GONE);
                    orderListDesOver.setText(introduce);
                    orderListDesOver.setAuto(true);
                    orderListDesOver.setSelected(true);
                }
            } else {
                if (onceProductList.size() < 4) {
                    orderListDesMulti.setVisibility(View.GONE);
                    orderListDesAlignMulti.setVisibility(View.VISIBLE);
                    orderListDesAlignMulti.setText(introduce);
                    orderListDesAlignMulti.setAuto(true);
                    orderListDesAlignMulti.setSelected(true);
                } else {
                    orderListDesMulti.setVisibility(View.VISIBLE);
                    orderListDesAlignMulti.setVisibility(View.GONE);
                    orderListDesMulti.setText(introduce);
                    orderListDesMulti.setAuto(true);
                    orderListDesMulti.setSelected(true);
                }
            }
        }catch (Exception e){
            SuperLog.error(TAG, e);
        }
    }

    /**
     * 跳转到支付页面
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
        if (null != marketingProduct)
        {
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
        /*观影券需求，产品支付类型只支持按次产品*/
        if (isOrderByOrder(mProductInfo) && isSupportPayBenefit) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.IS_ORDER_BY_ORDER, true);
        }
        // 2018/10/10 第三方支付需求 ADD START
        // 判断当前产品是否支持第三方支付
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
                // 当用户状态校验失败时，不展示第三方支付
                intent.putExtra(NewMyPayModeActivity.NEED_SHOW_THIRD_PART_PAYMENT, false);
                intent.putExtra(NewMyPayModeActivity.NEED_SHOW_USER_STATE_VALIDATION_FAILED_NOTIFICATION, true);
            }
        }
        // 2018/10/10 第三方支付需求 ADD END
        startActivityForResult(intent, REQUEST_CODE);

    }

    public boolean isMouthProduct(Product mProductInfo) {
        return null != mProductInfo && "0".equals(mProductInfo.getProductType()) && "0".equals(mProductInfo.getChargeMode()) && "1".equals(mProductInfo.getPeriodLength()) && "1".equals(mProductInfo.getIsAutoExtend());
    }

    /**
     * 是否按次订购
     */
    public boolean isOrderByOrder(Product mProductInfo) {
        return null != mProductInfo && "1".equals(mProductInfo.getProductType());
    }

    private void initProductListOnUIThread(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initProductList();
            }
        });
    }

    //处理展示获取到产品包列表
    private void initProductList(){
        /*判断产品列表是否大于4个，是否同时包含可自动续订和不可自动续订产品
          产品包大于4个且同同时包含可自动续订和不可自动续订产品时
          两排展示，否则一排展示*/
        boolean isContain = false;
        boolean containOnce = false;
        boolean containCycle = false;
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            if ((!TextUtils.isEmpty(product.getIsAutoExtend()) && product.getIsAutoExtend().equals("0"))) {
                //是按次订购产品
                containOnce = true;
            }
            if ((!TextUtils.isEmpty(product.getIsAutoExtend()) && product.getIsAutoExtend().equals("1"))) {
                //是包周期产品
                containCycle = true;
            }
        }

        if (containOnce && containCycle) {
            //同时包含包周期和按次订购产品
            isContain = true;
        }

        if (productList.size() > 4 && isContain) {
            showSingleList = false;
        }

        if (showSingleList) {
            //展示一排
            recyclerviewLayoutSingle.setVisibility(View.VISIBLE);
            if (productList.size() > 0) {
                mProductInfo = this.productList.get(0);
                freshUI();
                if (this.productList.size() > 5) {
                    orderListListAuto.setVisibility(View.GONE);
                } else {
                    orderListList.setVisibility(View.GONE);
                }
                mAdapter.notifyDataSetChanged();
            }
        } else {
            //展示两排,对数据进行分类
            onceProductList.clear();
            cycleProductList.clear();
            for (int i = 0; i < productList.size(); i++) {
                Product product = productList.get(i);
                if ((!TextUtils.isEmpty(product.getProductType()) && product.getIsAutoExtend().equals("0"))) {
                    //是单次产品
                    onceProductList.add(product);
                } else if ((!TextUtils.isEmpty(product.getProductType()) && product.getIsAutoExtend().equals("1"))) {
                    //是包周期产品
                    cycleProductList.add(product);
                }
            }

            recyclerviewLayoutMulti.setVisibility(View.VISIBLE);
            mProductInfo = cycleProductList.get(0);
            freshUI();
            if (cycleProductList.size() > 4) {
                orderListListUpAuto.setVisibility(View.GONE);
            } else {
                orderListListUp.setVisibility(View.GONE);
            }
            if (onceProductList.size() > 4) {
                orderListListDownAuto.setVisibility(View.GONE);
            } else {
                orderListListDown.setVisibility(View.GONE);
            }
            mutiAdapterForCycle.notifyDataSetChanged();
            mutiAdapterForOnce.notifyDataSetChanged();
        }
    }

    /******************************************列表点击事件***********************************/

    private boolean canClick = true;
    @Override
    public void onItemClick(int position) {

        //防止短时间快速点击
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

        //判断是否重复订购
        boolean canpay = AvoidRepeatPaymentUtils.getInstance().canPay(mProductInfo.getID());
        if (!canpay){
            EpgToast.showToast(this, "正在订购……");
            return;
        }

        marketingProduct = null;

        if (null != productMap.get(mProductInfo.getID())){
            Product marketProduct = productMap.get(mProductInfo.getID());
            marketingProduct = marketProduct;

            if (null != offerInfos && offerInfos.size() > 0) {
                for (int i = 0; i < offerInfos.size(); i++) {
                    if (null !=marketProduct &&  null != marketProduct.getMarketing() && (offerInfos.get(i).getOfferID().equals(marketProduct.getMarketing().getId()) || offerInfos.get(i).getOfferID().equals(marketProduct.getMarketing().getTag()))) {
                        marketingProduct = null;
                    }
                }
            }
        }


//        ProductUtils.checkSubscribeProductsType(this, new ProductUtilCallback() {
//            @Override
//            public void onCheckPackageRelationshipSuccess(Map<String, List<String>> params) {
//                //大包已订购，不能订购
//                if (null == params) {
//
//                    String mutexStr = SessionService.getInstance().getSession().getTerminalConfigurationOrderProductMutexInfo();
//                    if (null != mutexStr && mutexStr.length()>0){
//                        EpgToast.showToast(OTTApplication.getContext(),
//                                mutexStr);
//                    }else{
//                        EpgToast.showToast(OTTApplication.getContext(),
//                                getResources().getString(R.string.big_small_product_mutex));
//                    }
////                    EpgToast.showToast(OTTApplication.getContext(),
////                            getResources().getString(R.string.big_small_product_mutex));
//                    return;
//                } else {
//                    unsubProdInfoIds = params.get(ProductUtils.SUBSCRIBED_PRODUCT_ID);
//                }
//                mPresenter.queryUniPayInfo(new QueryUniInfoRequest(), NewProductOrderActivity.this);
//
//            }
//
//            @Override
//            public void onQueryProductInfoFailed() {
//            }
//
//            @Override
//            public void onQueryProductEmpty() {
//                mPresenter.queryUniPayInfo(new QueryUniInfoRequest(), NewProductOrderActivity.this);
//            }
//        }, mProductInfo.getID());

        if (null != unsubProdInfoIds){
            unsubProdInfoIds.clear();
        }else{
            unsubProdInfoIds = new ArrayList<>();
        }
        if (mProductInfo.getProductType().equals("0")){
            //包周期产品判断互斥关系
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

                    mPresenter.queryUniPayInfo(new QueryUniInfoRequest(), NewProductOrderActivity.this);
                }

                @Override
                public void GetProductMutExRelaCallBackFailed() {}
            },mProductInfo,mVODDetail);
        }else{
            mPresenter.queryUniPayInfo(new QueryUniInfoRequest(), NewProductOrderActivity.this);
        }
    }

    /******************************************ProductOrderContract回调***********************************/

    //请求成功，获得可订购产品列表
    @Override
    public void generateProductListSucc(List<Product> productList) {
        this.productList.clear();
        this.productList.addAll(productList);
        //查询用户分组
        getUserGroupPresenter.getLabelsByUserId(SessionService.getInstance().getSession().getUserId(),callback);
    }

    //获得可订购产品列表失败
    @Override
    public void generateProductListError() {

    }

    /**
     * 查询开通情况成功
     */
    @Override
    public void queryUniPayInfoSucc(QueryUniPayInfoResponse response) {
        mainUniPayInfo = mPresenter.resolveMainUniPayInfo(response);
        if (null != mainUniPayInfo) {
            if (TextUtils.isEmpty(mainUniPayInfo.getBillID())) {
                //统一账号不存在
                switchPhonePayActivity(null, ZjYdUniAndPhonePayActivty.UNONPENED_ACCOUNT);
            } else {
                mPresenter.queryUserOrderingSwitch();
            }
        } else {
            //跳转到开通统一支付扫码连接界面
            switchPhonePayActivity(null, ZjYdUniAndPhonePayActivty.QRCODE_FLAG);
        }
    }

    @Override
    public void queryUniPayInfoError() {
        //统一账号不存在
        switchPhonePayActivity(null, ZjYdUniAndPhonePayActivty.UNONPENED_ACCOUNT);
    }

    @Override
    public void querySubscriberSucess(String orderingSwitch) {
        //跳转到支付界面
        if (!TextUtils.isEmpty(orderingSwitch) && "1".equals(orderingSwitch)) {
            switchPhonePayActivity(mainUniPayInfo,
                    ZjYdUniAndPhonePayActivty.UNIPAY_PHONE_CODE_FLAG);
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
        isInVisiableMarket = false;
        offerInfos = response.getOfferList();
        mPresenter.generateProductList(getIntent().getStringExtra(AUTHORIZE_RESULT));
    }

    @Override
    public void queryMultiqryFail() {
        isInVisiableMarket = true;
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

    /******************************************ProductOrderContract回调***********************************/

    @Override
    public void loadSubscription(QueryProductInfoResponse response) {

    }

    @Override
    public void queryProductInfoFail() {

    }

    @Override
    public void loadProducts(List<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            Product MarketProduct = products.get(i);

            for (int j = 0; j <productList.size() ; j++) {
                if (MarketProduct.getID().equals(MarkerProductIdMap.get(productList.get(j).getID()))){
                    Marketing marketing = ProductMarketing.get(productList.get(j).getID());
                    if (null != marketing){
                        MarketProduct.setMarketing(marketing);
                    }
                    productMap.put(productList.get(j).getID(),MarketProduct);
                };

            }
        }

        initProductListOnUIThread();
    }



    /******************************************EventBus事件***********************************/

    //切换包刷新页面
    @Subscribe
    public void onEvent(ProductOrderSwitchEvent event) {
        int position = event.getPostion();
        if (position >= 0 && position < productList.size()) {
            Product product = productList.get(position);
            mProductInfo = product;
            freshUI();
        }
    }

    @Subscribe
    public void onEvent(ProductOrderMutiSwitchEvent event) {
        int section = event.getSection();
        int position = event.getPostion();

        if (section == 0) {
            if (position >= 0 && position < cycleProductList.size()) {
                Product product = cycleProductList.get(position);
                mProductInfo = product;
                freshUI();
            }
        } else if (section == 1) {
            if (position >= 0 && position < onceProductList.size()) {
                Product product = onceProductList.get(position);
                mProductInfo = product;
                freshUI();
            }
        }

    }

    /******************************************自定义类***********************************/

    //recycleview的间距
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private static final String TAG = "CustomItemDecoration";

        private int mSpace;

        private Context mContext;

        /**
         * @param space 传入的值，其单位视为dp
         */
        public SpaceItemDecoration(Context context, float space) {
            SuperLog.debug(TAG, "space is " + space);
            this.mContext = context;
            this.mSpace = (int) space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int itemCount = parent.getAdapter().getItemCount();
            int pos = parent.getChildAdapterPosition(view);
            SuperLog.debug(TAG, "itemCount>>" + itemCount + ";Position>>" + pos);

            outRect.left = 0;
            outRect.top = mContext.getResources().getDimensionPixelSize(R.dimen.margin_10);
            outRect.bottom = mContext.getResources().getDimensionPixelSize(R.dimen.margin_10);

            outRect.right = mSpace / 2;
            outRect.left = mSpace / 2;
        }
    }

    private OrderUserGroupPresenter.GetUserGroupCallback callback = new OrderUserGroupPresenter.GetUserGroupCallback() {
        @Override
        public void getLabelsByUserIdSuccess(List<CustomGroup> customGroups) {
            if (null != customGroups && customGroups.size()>0){
                productIds.clear();
                for (int i = 0; i < productList.size(); i++) {
                    Product product = productList.get(i);
                    //这个产品包是否有匹配的营销活动
                    Marketing marketing = MarketingUtils.getmarketingProduct(customGroups,product,offerInfos);
                    if (null != marketing){
                        productIds.add(marketing.getId());
//                        productIdMap.put(marketing.getId(),product.getID());
                        MarkerProductIdMap.put(product.getID(),marketing.getId());
                        ProductMarketing.put(product.getID(),marketing);
                    }
                }
                //用productIds查询QueryProduct接口
                if (productIds.size()>0){
                    orderCenterPresenter.queryProduct(productIds,50,0,NewProductOrderActivity.this);
                }else{
                    //没有匹配的营销产品包，直接展示产品包列表
                    initProductListOnUIThread();
                }
            }else{
                //用户没有分组，直接展示产品包列表
                initProductListOnUIThread();
            }
        }

        @Override
        public void getLabelsByUserIdFail() {
            Log.i(TAG, "getLabelsByUserIdFail: ");
            //展示产品包列表
            initProductListOnUIThread();
        }
    };
}
