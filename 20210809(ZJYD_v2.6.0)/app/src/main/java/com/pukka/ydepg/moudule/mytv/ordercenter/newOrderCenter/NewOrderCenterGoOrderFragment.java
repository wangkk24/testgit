package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.leanback.widget.VerticalGridView;

import com.google.gson.reflect.TypeToken;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.bean.node.Marketing;
import com.pukka.ydepg.common.http.bean.node.OfferInfo;
import com.pukka.ydepg.common.http.bean.node.User;
import com.pukka.ydepg.common.http.bean.request.QueryMultiqryRequest;
import com.pukka.ydepg.common.http.bean.response.QueryMultiqryResponse;
import com.pukka.ydepg.common.http.bean.response.QueryProductInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.CustomGroup;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUniPayInfoResponse;
import com.pukka.ydepg.common.http.vss.response.QueryMultiUserInfoResponse;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Purchase;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.event.FinishPlayUrlEvent;
import com.pukka.ydepg.event.OrderCenterFocusEvent;
import com.pukka.ydepg.event.PlayUrlEvent;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.fragment.BaseMvpFragment;
import com.pukka.ydepg.moudule.mytv.ZjYdUniAndPhonePayActivty;
import com.pukka.ydepg.moudule.mytv.adapter.GoOrderListAdapter;
import com.pukka.ydepg.moudule.mytv.bean.OrderItemBean;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.adapter.NewGoOrderListAdapter;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.AvoidRepeatPaymentUtils;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.JumpToH5OrderUtils;
import com.pukka.ydepg.moudule.mytv.presenter.OrderCenterPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.OrderUserGroupPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.ProductOrderPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.contract.ProductOrderContract;
import com.pukka.ydepg.moudule.mytv.presenter.view.OrderCenterView;
import com.pukka.ydepg.service.NtpTimeService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * ????????????-????????????
 *
 * @FileName: com.pukka.ydepg.module.mytv.NewOrderCenterGoOrderFragment.java
 * @author: weicy
 * @data: 2019-11-114 16:38
 * @Version V2.1 <????????????????????????>
 */
public class NewOrderCenterGoOrderFragment extends BaseMvpFragment<OrderCenterPresenter> implements OrderCenterView , ProductOrderContract.View {
    private static final String TAG = com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewOrderCenterGoOrderFragment.class.getSimpleName();
    private final int PAGE_SIZE = 50;

    @BindView(R.id.order_center_go_order_list)
    VerticalGridView contentListView;

    //????????????????????????
    List<Product> productList = new ArrayList<>();

    //???????????????????????????offerid ??? tag id
    private List<OfferInfo> offerInfos;

    NewGoOrderListAdapter mAdapter;

    //????????????????????????
    private Map<String, String> orderItemMaps = new HashMap<>();
    //????????????????????????
    private Map<String, String> orderItemMapsForColor = new HashMap<>();

    private OrderUserGroupPresenter getUserGroupPresenter = new OrderUserGroupPresenter();

    private ProductOrderPresenter mPresenter = new ProductOrderPresenter();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    //product?????????????????????????????????
    private final static String zjProductOfSales = "zjProductOfSales";

    private int totalNumber;
    private List<String> productIds = new ArrayList<>();

    private Product selectedProduct;

    //????????????????????????
    private boolean isFirstLoad = true;

    //???????????????????????????  key:????????????????????????productId, value:?????????????????????????????????????????????
    Map<String, Product> productMap = new HashMap<String, Product>();

//    //?????????????????????????????????????????????????????? key:??????????????????offerId ???value???????????????????????????productId
//    Map<String, String> productIdMap = new HashMap<String, String>();

    //key:???????????????productId ???value??????????????????????????????offerId
    Map<String, String> MarkerProductIdMap = new HashMap<String, String>();

    //key:???????????????productId ???value??????????????????????????????????????????marketing
    Map<String, Marketing> ProductMarketing = new HashMap<String, Marketing>();

    //key:????????????id???value????????????id
    Map<String, String> mapForPromotion = new HashMap<>();

    //??????????????????????????????productIDs
    List<String> MarketProductIds = new ArrayList<>();

    final static String superscript = "superscript";

    final static String color = "color";

    /**
     * ??????????????????
     */
    private Product marketingProduct;

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);
        if (null != mAdapter) {
            return;
        }

        initData();
        contentListView.setColumnWidth(getResources().getDimensionPixelOffset(R.dimen.order_center_go_order_item_width_total));
        contentListView.setNumColumns(4);
        mAdapter = new NewGoOrderListAdapter(contentListView, productList, getActivity(), orderItemMaps,orderItemMapsForColor,productMap);
        mAdapter.setHasStableIds(true);
        contentListView.setAdapter(mAdapter);
        mAdapter.setListener(new GoOrderListAdapter.OnItemClickListener() {
            @Override
            public void onClik(Product product, int position) {
                //???????????????????????????
                if (TextUtils.equals(product.getIsSubscribed(), "1")) {
                    selectedProduct=null;
                    EpgToast.showToast(getActivity(),getString(R.string.subscribed_hint));
                    return;
                }

                if (null != productMap.get(product.getID())){
                    marketingProduct = productMap.get(product.getID());
                }else{
                    marketingProduct = null;
                }

                AuthorizeResult result = new AuthorizeResult();
                List<Product> products = new ArrayList<>();
                selectedProduct=product;

                //????????????????????????
                boolean canpay = AvoidRepeatPaymentUtils.getInstance().canPay(selectedProduct.getID());
                if (!canpay){
                    EpgToast.showToast(getActivity(), "??????????????????");
                    return;
                }

                products.add(product);
                result.setPricedProducts(products);

                String needJumpToH5Order = SessionService.getInstance().getSession().getTerminalConfigurationNeedJumpToH5Order();
                if (null != needJumpToH5Order && needJumpToH5Order.equals("1")){
                    JumpToH5OrderUtils.getInstance().jumpToH5OrderFromOrderCenter(result.getPricedProducts(),getActivity());
                    //pbs????????????
                    String promotionId = "";
                    String groupId = "";
                    if (null != marketingProduct && null != marketingProduct.getMarketing()){
                        groupId = mapForPromotion.get(marketingProduct.getMarketing().getId());
                        promotionId = marketingProduct.getMarketing().getId();
                    }
                    PbsUaService.report(Purchase.getOrderCenterData(product.getID(),promotionId,groupId,position+""));
                }else{
                    Intent intent = new Intent(getActivity(), ProductTransitionActivity.class);
                    intent.putExtra(NewProductOrderActivity.AUTHORIZE_RESULT, JsonParse.object2String(result));
                    intent.putExtra(ZjYdUniAndPhonePayActivty.ISORDERCENTER, true);

                    if (null != marketingProduct)
                    {
                        intent.putExtra(ZjYdUniAndPhonePayActivty.MARKETING_PRODUCT,
                                JsonParse.object2String(marketingProduct));
                    }

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (null != getActivity()){
                        getActivity().startActivity(intent);
                    }
                }
            }
        });
    }

    private void initList(){
        String billId = SessionService.getInstance().getSession().getAccountName();
        //??????????????????????????????
        if (!TextUtils.isEmpty(billId)) {
            QueryMultiqryRequest request = new QueryMultiqryRequest();
            //TODO
            request.setMessageID("0000");
            request.setBillID(billId);
            request.setValidType("1");
            mPresenter.queryMultiqry(request);
        } else {
            queryData(PAGE_SIZE, 0);
        }
    }

    private void initData() {
        String content = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.ORDER_CENTER_PRODUCT_INFOS);
        List<OrderItemBean> orderItemBeans = JsonParse.json2Object(content, new TypeToken<List<OrderItemBean>>(){}.getType());
//        List<OrderItemBean> orderItemBeans = new ArrayList<>();
//        OrderItemBean tempBean = new OrderItemBean();
//        tempBean.setId("600000535594");
//        orderItemBeans.add(tempBean);

        productIds.clear();
        if (!CollectionUtil.isEmpty(orderItemBeans)) {
            totalNumber = orderItemBeans.size();
            for (OrderItemBean bean : orderItemBeans) {
                productIds.add(bean.getId());
                orderItemMaps.put(bean.getId(), bean.getSuprScript());
                orderItemMapsForColor.put(bean.getId(), bean.getColor());
            }

        }
    }

    private void queryData(int count, int offset) {
        presenter.queryProduct(productIds, count, offset, getActivity());
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.order_center_go_order_layout_new;
    }

    @Override
    protected void initPresenter() {
        presenter = new OrderCenterPresenter();
    }

    @Override
    public void generateProductListSucc(List<Product> productList) {

    }

    @Override
    public void generateProductListError() {

    }

    @Override
    public void queryUniPayInfoSucc(QueryUniPayInfoResponse response) {

    }

    @Override
    public void queryUniPayInfoError() {

    }

    @Override
    public void querySubscriberSucess(String userId) {

    }

    @Override
    public void querySubscriberfail() {

    }

    @Override
    public void queryMultiqrySuccess(QueryMultiqryResponse response) {
        offerInfos = response.getOfferList();
        queryData(PAGE_SIZE, 0);

    }

    @Override
    public void queryMultiqryFail() {
        queryData(PAGE_SIZE, 0);

    }

    @Override
    public void queryMultiUserInfoSuccess(QueryMultiUserInfoResponse response, Intent intent) {

    }

    @Override
    public void queryMultiUserInfoFail(Intent intent) {

    }

    @Override
    public void loadSubscription(QueryProductInfoResponse response) { }

    @Override
    public void queryProductInfoFail() { }

    public boolean hasData() {
        return null != contentListView && contentListView.getChildCount() > 0;
    }

    @Override
    public void loadProducts(List<Product> products) {
        if (isFirstLoad){
            isFirstLoad = false;
            this.productList.clear();
            this.productList.addAll(products);
            //??????????????????
            getUserGroupPresenter.getLabelsByUserId(SessionService.getInstance().getSession().getUserId(),callback);

        }else{
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

                    List<NamedParameter> listNp = MarketProduct.getCustomFields();
                    String superscriptStr = "";
                    String colorStr = "";
                    //??????????????????
                    List<String> superscriptStrList = CommonUtil.getCustomNamedParameterByKey(listNp,superscript);
                    if (null != superscriptStrList && superscriptStrList.size()>0){
                        superscriptStr = superscriptStrList.get(0);
                    }
                    //????????????????????????
                    List<String> colorStrList = CommonUtil.getCustomNamedParameterByKey(listNp,color);
                    if (null !=colorStrList && colorStrList.size()>0 ){
                        colorStr = colorStrList.get(0);
                    }

                    if (!"".equals(superscriptStr) && !"".equals(colorStr)){
                        orderItemMaps.put(MarketProduct.getID(),superscriptStr);
                        orderItemMapsForColor.put(MarketProduct.getID(),colorStr);
                    }

                }
            }
            initProductList();
        }

    }

    private void initProductList(){
        if (null != getActivity()){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    mAdapter.addData(productList,true);
                    mAdapter.notifyDataSetChanged();
                    Log.i(TAG, "run: ");
                }
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.attachView(this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mPresenter.detachView();
    }

    @Override
    public void onResume() {
        super.onResume();
        isFirstLoad = true;
        initList();
    }

    //??????????????????????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PlayUrlEvent event) {
        SuperLog.debug(TAG,"get PlayUrlEvent->isOrderCenter:"+event.isOrderCenter());
        if (event.isOrderCenter())
            {
                //????????????????????????
//                UBDTool.getInstance().recordUBDPurchase(selectedProduct,"",
//                    PurchaseData.SUCCESS);
            /**
             *?????????????????????????????????
             */
//            EventBus.getDefault().post(new OrderCenterFocusEvent(OrderCenterFocusEvent.GO_ORDER));
            EventBus.getDefault().post(new FinishPlayUrlEvent());
        }
    }

    private OrderUserGroupPresenter.GetUserGroupCallback callback = new OrderUserGroupPresenter.GetUserGroupCallback() {
        @Override
        public void getLabelsByUserIdSuccess(List<CustomGroup> customGroups) {
            if (null != customGroups && customGroups.size()>0){
                MarketProductIds.clear();
                for (int i = 0; i < productList.size(); i++) {
                    Product product = productList.get(i);
                    //?????????????????????????????????????????????
                    Marketing marketing = getmarketingProduct(customGroups,product);
                    if (null != marketing){
                        MarketProductIds.add(marketing.getId());
//                        productIdMap.put(marketing.getId(),product.getID());
                        MarkerProductIdMap.put(product.getID(),marketing.getId());
                        ProductMarketing.put(product.getID(),marketing);
                    }
                }
                //???productIds??????QueryProduct??????
                if (MarketProductIds.size()>0){
                    presenter.queryProduct(MarketProductIds,50,0,getActivity());
                }else{
                    //????????????????????????????????????????????????????????????
                    if (null != getActivity()){
                        initProductList();
                    }
                }
            }else{
                //????????????????????????????????????????????????
                initProductList();
            }
        }

        //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????null
        private Marketing getmarketingProduct(List<CustomGroup> customGroups,Product product){
            //???????????????????????????????????????????????????
            List<NamedParameter> listNp = product.getCustomFields();
            List<String> marketInfo = CommonUtil.getCustomNamedParameterByKey(listNp,zjProductOfSales);
            Map<String, Marketing> marketingMap = new HashMap<>();
            if (null != marketInfo && marketInfo.size()>0){
                String str = marketInfo.get(0);
                if (!TextUtils.isEmpty(str)){
                    //???????????????
                    marketingMap = JsonParse.json2Object(str,new TypeToken<HashMap<String, Marketing>>() {}.getType());
                }
            }
            if (null == marketingMap){
                return null;
            }
            //???????????????
            for (int j = 0; j < customGroups.size(); j++) {
                CustomGroup group = customGroups.get(j);
                //????????????????????????????????? 1?????? 0?????????
                if (group.getLabelValue().equals("1")){
                    //?????????????????????????????????????????????????????????????????????
                    //??????????????????????????????????????????
                    if (null != marketingMap.get(group.getGroupId())){
                        Marketing marketing = marketingMap.get(group.getGroupId());
                        if (null != marketing){
                            try {
                                //??????????????????
                                long endTime = sdf.parse(marketing.getEndTime()).getTime();
                                long startTime = sdf.parse(marketing.getStartTime()).getTime();
                                long currentTime = NtpTimeService.queryNtpTime();
                                if (endTime > currentTime && startTime <= currentTime){
                                    //??????????????????????????????????????????????????????????????????
                                    if (!isMarketingOrdered(marketing)){
                                        mapForPromotion.put(marketing.getId(),group.getGroupId());
                                        return marketing;
                                    }
                                }
                            }catch (ParseException e){
                                SuperLog.error(TAG, e);
                            }
                        }
                    }
                }
            }
            return null;
        }

        //??????Marking?????????????????????
        private Boolean isMarketingOrdered(Marketing marketing){
            if (null != offerInfos && offerInfos.size() > 0) {
                for (int i = 0; i < offerInfos.size(); i++) {
                    //????????????Marking???OfferID???tagID???offerInfos????????????true
                    if ((offerInfos.get(i).getOfferID().equals(marketing.getId()) || offerInfos.get(i).getOfferID().equals(marketing.getTag()))) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void getLabelsByUserIdFail() {
            //?????????????????????
            initProductList();
        }
    };
}