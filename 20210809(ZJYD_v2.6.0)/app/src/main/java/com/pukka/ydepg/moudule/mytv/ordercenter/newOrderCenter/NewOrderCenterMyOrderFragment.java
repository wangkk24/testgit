package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.leanback.widget.BrowseFrameLayout;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;
import com.pukka.ydepg.common.http.bean.node.OfferInfo;
import com.pukka.ydepg.common.http.bean.request.CancelSubscribeRequest;
import com.pukka.ydepg.common.http.bean.request.DSVQuerySubscription;
import com.pukka.ydepg.common.http.bean.request.QueryMultiqryRequest;
import com.pukka.ydepg.common.http.bean.request.QueryRecommendRequest;
import com.pukka.ydepg.common.http.bean.response.QueryMultiqryResponse;
import com.pukka.ydepg.common.http.bean.response.QueryProductInfoResponse;
import com.pukka.ydepg.common.http.bean.response.SubscribeDeleteResponse;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODListCallBack;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.Genre;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.ProduceZone;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.SubjectVODList;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscription;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUniPayInfoResponse;
import com.pukka.ydepg.common.http.vss.response.QueryMultiUserInfoResponse;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.scene.UBDAdvert;
import com.pukka.ydepg.common.utils.DateUtil;
import com.pukka.ydepg.common.utils.HeartBeatUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.uiutil.CpRoute;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.customui.MiguQRViewPopWindow;
import com.pukka.ydepg.customui.dialog.MyOrderFilterDialog;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.event.OrderCenterFocusEvent;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.fragment.BaseMvpFragment;
import com.pukka.ydepg.launcher.ui.fragment.FocusInterceptor;
import com.pukka.ydepg.launcher.ui.fragment.WebActivity;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.mytv.adapter.UnSubscribeRecommendAdapter;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.adapter.NewMyOrderListAdapter;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.bean.UnsubscribeTip;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.event.OrderCenterMyOrderFocusEvent;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.event.OrderCenterTopFocusEvent;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.UnsubscribeButtonConfigUtils;
import com.pukka.ydepg.moudule.mytv.presenter.OrderCenterPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.ProductOrderPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.contract.ProductOrderContract;
import com.pukka.ydepg.moudule.mytv.presenter.view.OrderCenterView;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.view.DetailDataView;
import com.pukka.ydepg.moudule.vod.view.FocusVerticalGridView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 订购中心-我的订购
 *
 * @FileName: com.pukka.ydepg.module.mytv.NewOrderCenterMyOrderFragment.java
 * @author: weicy
 * @data: 2019-11-114 16.39
 * @Version V2.1 <描述当前版本功能>
 */
public class NewOrderCenterMyOrderFragment extends BaseMvpFragment<OrderCenterPresenter> implements OrderCenterView, NewMyOrderListAdapter.onUnSubscribedListener, DetailDataView, ProductOrderContract.View {
    private static final String TAG = com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewOrderCenterMyOrderFragment.class.getSimpleName();
    private final String PAGE_SIZE  = "30";
    private final String UNIX_TIME_29990101 = "32472115200000";

    @BindView(R.id.order_center_my_order_list)
    VerticalGridView gridViewOrderList;

    @BindView(R.id.tv_my_order_filter)
    TextView tvFilter;

    @BindView(R.id.my_order_root_layout)
    BrowseFrameLayout rootLayout;

    private PopupWindow unSubscribeDialog = null;//退订弹窗

    //退订挽留弹框
    private PopupWindow  PersuadeDialog=null;

    private MyOrderFilterDialog mDialog;

    private NewMyOrderListAdapter myOrderListAdapter;

    //挽留营销活动url
    private String  iconUrl;

    //挽留营销活动跳转url
    private String clickUrl;

    private String productId;

    private String prodcutName;

    private String cancelType;

    private List<NamedParameter> extensionFields;

    DSVQuerySubscription request;
    private String mTotal;
    //已经查询到的offset
    private int offset = 0;
    private boolean mIsLoadMore;
    private List<Subscription> mSubscriptionList = new ArrayList<>();
    private DetailPresenter mDetailPresenter;
    private VODDetail vodDetail;

    /*定制的有营销活动的产品包的退订提示语
     */
    private Map<String,List<UnsubscribeTip>> unSubscribeTips = new HashMap<>();

    //产品包扩展参数的跳转url
    private static final String ZJ_URL_OF_RETENTION = "zjUrlOfRetention";

    private List<OfferInfo> offerInfos;
    private ProductOrderPresenter mPresenter = new ProductOrderPresenter();

    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_LARGE);

    @Override
    protected int attachLayoutRes() {
        return R.layout.order_center_my_order_layout_new;
    }

    @Override
    protected void initPresenter() {
        presenter = new OrderCenterPresenter();
    }

    @Override
    protected void initView(View view) {
        
        if (null != myOrderListAdapter) {
            return;
        }
        mPresenter.attachView(this);

        mDetailPresenter = new DetailPresenter((RxAppCompatActivity) getActivity());
        mDetailPresenter.setDetailDataView(this);
        request = new DSVQuerySubscription();
        request.setSortType("ENDTIME:DESC");//按订购关系失效时间降序排列

        myOrderListAdapter = new NewMyOrderListAdapter(gridViewOrderList, mSubscriptionList, getActivity());
        myOrderListAdapter.setUnSubscribedListener(this);
        myOrderListAdapter.setHasStableIds(true);
        myOrderListAdapter.setOnItemSelectedListener(new NewMyOrderListAdapter.OnItemSelectedListener() {
            @Override
            public void onLoseFocus(boolean isClickFilter) {
                tvFilter.setFocusable(true);
                tvFilter.requestFocus();
                if(isClickFilter){
                    //焦点在右侧订购记录列表Item项上时按菜单键打开过滤Dialog
                    showFilterWindow();
                }
            }
        });

        gridViewOrderList.setAdapter(myOrderListAdapter);
        gridViewOrderList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    return;
                }

                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                int lastChildBottom = lastChildView.getBottom();
                int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
                if ((lastChildBottom - recyclerBottom) < lastChildView.getHeight() && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                    SuperLog.debug(TAG, "scrolled to last item!");
                    if (offset < Integer.parseInt(mTotal)) {
                        mIsLoadMore = true;
                        requestOrderData();
                    }
                }
            }
        });

        tvFilter.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_MENU //焦点在右上角过滤标签上时点击菜单键和确定键都可以打开过滤对话框
                        || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    showFilterWindow();
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && !hasData()){
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP){
                    EventBus.getDefault().post(new OrderCenterTopFocusEvent());
                    return true;
                }
            }
            return false;
        });

        unSubscribeTips = SessionService.getInstance().getSession().getTerminalConfigurationUnsubscribeTips();
        
        requestOrderData();
    }

    private void showFilterWindow() {
        mDialog = new MyOrderFilterDialog(getActivity());
        mDialog.setListener((year, month) -> {
            //根据选中的条件修改筛选标题
            if (TextUtils.equals(year, "全部")) {
                tvFilter.setText(getActivity().getString(R.string.my_order_filter_all_date));
            } else {
                String text = String.format(getActivity().getString(R.string.my_order_filter_format), year + "/" + month);
                tvFilter.setText(text);
            }
            mIsLoadMore = false;

            if (TextUtils.equals(year, "全部")) {
                //年份选择全部,              筛选区间起点【1970/01/01 08:00:00】
                request.setFromDate("0");
            } else if (TextUtils.equals(month, "全部")) {
                //年份选择特定年,月份选择全部,  筛选区间起点【XXXX/01/01 00:00:00】
                request.setFromDate(DateUtil.getFirstDayOfMonth(Integer.valueOf(year),1));
            } else {
                //年份选择指定年,月份选择指定月,筛选区间起点【XXXX/XX/01 00:00:00】
                request.setFromDate(DateUtil.getFirstDayOfMonth(Integer.valueOf(year), Integer.valueOf(month)));
            }
            //筛选区间重点【2999/1/1 00:00:00】防止搜索不到产品
            request.setToDate(UNIX_TIME_29990101);
            requestOrderData();
        });
        mDialog.show();
    }

    /**
     * 数据加载 加载更多从列表下一个开始 否则从0开始
     */
    private void requestOrderData() {
        String billId = SessionService.getInstance().getSession().getAccountName();
        //生成可订购的产品列表

        if (!TextUtils.isEmpty(billId)) {
            QueryMultiqryRequest request = new QueryMultiqryRequest();
            //TODO
            request.setMessageID("0000");
            request.setBillID(billId);
            request.setValidType("1");
            if (null == mPresenter || null == mPresenter.getBaseView()){
                return;
            }
            mPresenter.queryMultiqry(request);
        } else {
            requestData();
        }
    }

    private void requestData(){
        int offset;
        if (mIsLoadMore) {
            offset = this.offset;
        } else {
            offset = 0;
        }
        request.setOffset(offset + "");
        request.setCount(PAGE_SIZE);
        presenter.queryProductInfo(request, getActivity());
    }

    @Override
    public void loadSubscription(QueryProductInfoResponse response) {
        if (null != response) {
            mTotal = response.getTotal();
            if (!mIsLoadMore) {
                myOrderListAdapter.clearData();
                offset = 0;
            }
            List<Subscription> subscriptionList = response.getProducts();
            if (null != subscriptionList && !subscriptionList.isEmpty()) {
                //记录已经查询到的offset
                offset = offset + subscriptionList.size();
                String unshowListStr = SessionService.getInstance().getSession().getTerminalConfigurationMyOrderUnshowList();
                if (!TextUtils.isEmpty(unshowListStr)){
                    List<Subscription> tempList = new ArrayList<>();
                    for (int i = 0; i < subscriptionList.size(); i++) {
                        Subscription subscription = subscriptionList.get(i);
                        String productid = subscription.getProductID();
                        if (!TextUtils.isEmpty(productid) && !unshowListStr.contains(productid)){
                            tempList.add(subscription);
                        }
                    }
                    if (tempList.size()>0){
                        myOrderListAdapter.addData(tempList);
                    }
                }else{
                    myOrderListAdapter.addData(subscriptionList);
                }
            }
        }
    }



    @Override
    public void loadProducts(List<Product> products) { }

    @Override
    public void onUnSubscribed(String productId, String productName, String cancelType, List<NamedParameter> extensionFields, Picture mPicture) {
        String isUseCVI= SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.USE_CVI_RECOMMEND);
        if(!TextUtils.isEmpty(isUseCVI)&&isUseCVI.equals("1")){
            if(null!=mPicture&&null!=mPicture.getIcons()&&mPicture.getIcons().size()>0){
                iconUrl=mPicture.getIcons().get(0);
            }else{
                iconUrl=null;
            }

            this.extensionFields=extensionFields;
            this.productId=productId;
            this.prodcutName = productName;
            this.cancelType=cancelType;
            //从产品包中取跳转url
            List<String> clickurlList = CommonUtil.getCustomNamedParameterByKey(extensionFields, ZJ_URL_OF_RETENTION);
            if (null != clickurlList && clickurlList.size() > 0) {
                clickUrl = clickurlList.get(0);
            }else{
                clickUrl = null;
            }

            mDetailPresenter.queryPBSRecommend(QueryRecommendRequest.SecenarizedType.PRODUCT_UNSUBSCRIBE,productId,mVODListCallBack,"4","0");
        }else{
            showUnSubscribeDialog(0, productId, productName,cancelType);

        }
    }

    @Override
    public void onPlayVod(VOD vod) {
        if (null != vod) {
            if (!TextUtils.isEmpty(vod.getCpId()) && CpRoute.isCp(vod.getCpId(),vod.getCustomFields())) {
                CpRoute.goCp(vod.getCustomFields());
            } else {
                mDetailPresenter.getVODDetail(vod.getID());
            }
        }
    }

    VODListCallBack mVODListCallBack=new VODListCallBack() {
        @Override
        public void queryVODListBySubjectSuccess(int total, List<VOD> vodList, String subjectId) {
            if(null!=vodList&&vodList.size()>3){
                showPersuadeDialog(vodList);
            }else{
                showUnSubscribeDialog(0, productId, prodcutName, cancelType);
            }
        }

        @Override
        public void queryVODListBySubjectFailed() { }

        @Override
        public void queryVODSubjectListSuccess(int total, List<Subject> subjects) { }

        @Override
        public void queryVODSubjectListFailed() { }

        @Override
        public void querySubjectVODBySubjectIDSuccess(int total, List<SubjectVODList> subjectVODLists) { }

        @Override
        public void querySubjectVODBySubjectIDFailed() { }

        @Override
        public void getContentConfigSuccess(List<ProduceZone> produceZoneList, List<Genre> genreList) { }

        @Override
        public void getContentConfigFailed() { }

        @Override
        public void querySubjectDetailSuccess(int total, List<Subject> subjects) { }

        @Override
        public void queryPSBRecommendSuccess(int total, List<VOD> vodDetails) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(null!=vodDetails&&vodDetails.size()>3){
                        showPersuadeDialog(vodDetails);
                    }else{
                        boolean isshowUnSubscribeDialog=true;
                        if(null!=extensionFields&&extensionFields.size()>0) {
                            for (int i = 0; i < extensionFields.size(); i++) {
                                if ("subjectID".equals(extensionFields.get(i).getKey())) {
                                    String firstValue = extensionFields.get(i).getFistItemFromValue();
                                    if(!TextUtils.isEmpty(firstValue)){
                                        mDetailPresenter.loadMoviesContent(firstValue,"0","4",mVODListCallBack);
                                        isshowUnSubscribeDialog=false;
                                        break;
                                    }

                                }
                            }
                        }
                        if(isshowUnSubscribeDialog){
                            showUnSubscribeDialog(0, productId,prodcutName, cancelType);
                        }
                    }
                }
            });
        }

        @Override
        public void queryPSBRecommendFail() {

        }

        @Override
        public void onError() {

        }
    };



    private  void showPersuadeDialog(List<VOD> vodList){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_persuade, null);
        PersuadeDialog = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
        TextView  confirm = (TextView) view.findViewById(R.id.unSubscriber_confirm);
        TextView  cancel  = (TextView) view.findViewById(R.id.back);
        ImageView iconImg = (ImageView)view.findViewById(R.id.icon_img);
        RelativeLayoutExt iconImgbg = (RelativeLayoutExt)view.findViewById(R.id.icon_img_bg);
        FocusVerticalGridView mRecommendList = (FocusVerticalGridView) view.findViewById(R.id.recommend);
        UnSubscribeRecommendAdapter adapter=new UnSubscribeRecommendAdapter(vodList,getActivity(),PersuadeDialog);
        mRecommendList.setAdapter(adapter);
        mRecommendList.setNumColumns(4);
        mRecommendList.setInterceptor(new FocusInterceptor() {
            @Override
            public boolean interceptFocus(KeyEvent event, View view) {
                int keycode = event.getKeyCode();
                if (keycode == KeyEvent.KEYCODE_DPAD_DOWN&& mRecommendList.findFocus() != null && isBorder((ViewGroup) view, mRecommendList.findFocus(), View.FOCUS_DOWN)) {
                    cancel.setFocusable(true);
                    cancel.requestFocus();
                    return true;
                }

                if (iconImgbg.isFocusable()){
                    if (keycode == KeyEvent.KEYCODE_DPAD_UP && mRecommendList.findFocus() != null && isBorder((ViewGroup) view, mRecommendList.findFocus(), View.FOCUS_DOWN)){
                        iconImgbg.requestFocus();
                        return true;
                    }
                }
                return false;
            }
        });

        //RequestOptions options  = new RequestOptions().placeholder(R.drawable.default_poster_bg).error(R.drawable.default_poster_bg);
        RequestOptions options  = new RequestOptions();
//        iconUrl = "http://adimage.aikan.miguvideo.com/img/group8601/M00/00/06/wKgBn17I2zuAEcQbAAHL_VDeUKc741.jpg";
        //退订挽留广告添加UBD上报
        if (!TextUtils.isEmpty(iconUrl)){
            UBDAdvert.reportConfigBannerImpression(productId,prodcutName,iconUrl, UBDConstant.ActionType.PURCHASE_BANNER_IMPRESSION);
        }
        Glide.with(this).load(iconUrl).apply(options).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                iconImgbg.setVisibility(View.GONE);
                iconImg.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                iconImgbg.setVisibility(View.VISIBLE);
                iconImg.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(iconImg);

        if (iconImg.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(clickUrl) && clickUrl.startsWith("http")){
            iconImgbg.setFocusable(true);
            iconImgbg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        iconImgbg.setSelected(true);
                    } else {
                        iconImgbg.setSelected(false);
                    }
                }
            });
            iconImgbg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", clickUrl);
                    intent.putExtra("isAdvert", true);
                    startActivity(intent);

                    //延时关闭弹框和我的订购页面，避免视觉上感觉先关闭再跳转
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (null != PersuadeDialog){
                                PersuadeDialog.dismiss();
                            }
                            if (null != getActivity()){
                                getActivity().finish();
                            }
                        }
                    },1500);
                }
            });
        }else{
            iconImgbg.setFocusable(false);
        }



        confirm.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
                        showUnSubscribeDialog(0, productId, prodcutName, cancelType);
                        PersuadeDialog.dismiss();
                        return true;
                    }
                    if(keyCode == KeyEvent.KEYCODE_BACK ){
                        PersuadeDialog.dismiss();
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                        return true;
                    }
                }
                return false;
            }
        });
        cancel.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_BACK){
                        PersuadeDialog.dismiss();
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                        return true;
                    }
                }
                return false;
            }
        });

        String cancelText = UnsubscribeButtonConfigUtils.getButtonText(UnsubscribeButtonConfigUtils.UNSUBSCRIBE_BUTTON,UnsubscribeButtonConfigUtils.BUTTON_TEXT_CONFIG_CANCEL);
        if (!"".equals(cancelText)){
            cancel.setText(cancelText);
        }else{
            cancel.setText(UnsubscribeButtonConfigUtils.UNSUBSCRIBE_BUTTON_TEXT_CANCEL);
        }

        String confirmText = UnsubscribeButtonConfigUtils.getButtonText(UnsubscribeButtonConfigUtils.UNSUBSCRIBE_BUTTON,UnsubscribeButtonConfigUtils.BUTTON_TEXT_CONFIG_CONFIRM);
        if (!"".equals(cancelText)){
            confirm.setText(confirmText);
        }else{
            confirm.setText(UnsubscribeButtonConfigUtils.UNSUBSCRIBE_BUTTON_TEXT_CONFIRM);
        }


        String focusStr = UnsubscribeButtonConfigUtils.getFocusConfig(UnsubscribeButtonConfigUtils.UNSUBSCRIBE_BUTTON);
        if (focusStr.equals(UnsubscribeButtonConfigUtils.CANCEL)){
            cancel.requestFocus();
        } else if (focusStr.equals(UnsubscribeButtonConfigUtils.CONFIRM)){
            confirm.requestFocus();
        }else{
            cancel.requestFocus();
        }

        BrowseFrameLayout rootLayout = (BrowseFrameLayout) findViewById(R.id.my_order_root_layout);
        PersuadeDialog.showAtLocation(rootLayout, Gravity.CENTER, 0, 0);
    }

    private void showUnSubscribeDialog(final int showType, final String productId, final  String productname, final String cancelType) {
        SuperLog.debug(TAG, "showType=" + showType + "|productId=" + productId + "|cancelType=" + cancelType);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_unsubscribe, null);
        TextView successTip = (TextView) view.findViewById(R.id.dialog_unsubscribe_success);
        TextView tip = (TextView)view.findViewById(R.id.dialog_unsubscribe_tip);
        ImageView title = (ImageView)view.findViewById(R.id.dialog_unsubscribe_title);
        TextView ok = (TextView) view.findViewById(R.id.dialog_unsubscribe_button_confirm);
        TextView cancel = (TextView) view.findViewById(R.id.dialog_unsubscribe_button_cancel);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.dialog_unsubscribe_button_layout);
        unSubscribeDialog = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        unSubscribeDialog.setOutsideTouchable(true);
        unSubscribeDialog.setBackgroundDrawable(new ColorDrawable());

        if (mFocusHighlight != null) {
            mFocusHighlight.onInitializeView(ok);
        }
        if (mFocusHighlight != null) {
            mFocusHighlight.onInitializeView(cancel);
        }

        View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mFocusHighlight != null) {
                    mFocusHighlight.onItemFocused(v, hasFocus);
                }

                if (hasFocus){
                    if (v instanceof TextView){
                        ((TextView) v).setTextColor(getResources().getColor(R.color.white_0));
                    }
                }else{
                    if (v instanceof TextView){
                        ((TextView) v).setTextColor(getResources().getColor(R.color.my_order_unsubscribe_button_Text_color));
                    }
                }
            }
        };
        ok.setOnFocusChangeListener(listener);
        cancel.setOnFocusChangeListener(listener);
        if (showType == 1) {
            successTip.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
            tip.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
        } else if (showType == 2) {
            successTip.setVisibility(View.VISIBLE);
            successTip.setText("退订处理中");
            layout.setVisibility(View.GONE);
            tip.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
        } else {
            successTip.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
            tip.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            tip.setText(getUnsubScribeTip(prodcutName, productId));

            String cancelText = UnsubscribeButtonConfigUtils.getButtonText(UnsubscribeButtonConfigUtils.UNSUBSCRIBE_CONFIRM_BUTTON,UnsubscribeButtonConfigUtils.BUTTON_TEXT_CONFIG_CANCEL);
            if (!"".equals(cancelText)){
                cancel.setText(cancelText);
            }else{
                cancel.setText(UnsubscribeButtonConfigUtils.UNSUBSCRIBE_BUTTON_TEXT_CANCEL);
            }

            String confirmText = UnsubscribeButtonConfigUtils.getButtonText(UnsubscribeButtonConfigUtils.UNSUBSCRIBE_CONFIRM_BUTTON,UnsubscribeButtonConfigUtils.BUTTON_TEXT_CONFIG_CONFIRM);
            if (!"".equals(cancelText)){
                ok.setText(confirmText);
            }else{
                ok.setText(UnsubscribeButtonConfigUtils.UNSUBSCRIBE_BUTTON_TEXT_CONFIRM);
            }

            String focusStr = UnsubscribeButtonConfigUtils.getFocusConfig(UnsubscribeButtonConfigUtils.UNSUBSCRIBE_CONFIRM_BUTTON);

            if (focusStr.equals(UnsubscribeButtonConfigUtils.CANCEL)){
                cancel.requestFocus();
            } else if (focusStr.equals(UnsubscribeButtonConfigUtils.CONFIRM)){
                ok.requestFocus();
            }else{
                cancel.requestFocus();
            }
            cancel.setOnClickListener(v -> unSubscribeDialog.dismiss());
        }
        ok.setOnClickListener(v -> {
            unSubscribeDialog.dismiss();
            if (showType == 0) {
                cancelSubscribed(productId, cancelType);
            }
        });
        BrowseFrameLayout rootLayout = (BrowseFrameLayout) findViewById(R.id.my_order_root_layout);
        unSubscribeDialog.showAtLocation(rootLayout, Gravity.CENTER, 0, 0);

    }

    private String getUnsubScribeTip(String prodcutName, String productId){
        if (null != offerInfos && offerInfos.size()>0 && null != unSubscribeTips){
            List<UnsubscribeTip> unsubscribeTipList = unSubscribeTips.get(productId);
            if (null != unsubscribeTipList  && unsubscribeTipList.size()>0){
                for (int i = 0; i < unsubscribeTipList.size(); i++) {
                    UnsubscribeTip tip = unsubscribeTipList.get(i);
                    String offerInfosStr = JsonParse.object2String(offerInfos);
                    if (null != offerInfosStr && !TextUtils.isEmpty(offerInfosStr) ){
                        if (offerInfosStr.contains(tip.getOfferid())){
                            return tip.getTip();
                        }
                    }
                }
            }
        }
        return "您是否要退订\"" + prodcutName + "\"产品";
    }

    public void cancelSubscribed(String productId, String cancelType) {
        if (null != presenter) {
            SuperLog.debug(TAG, "cancelType=" + cancelType + "|productId=" + productId);
            CancelSubscribeRequest.CancelSubscibe cancelSubscribe = new CancelSubscribeRequest.CancelSubscibe(productId, Integer.parseInt(cancelType));
            CancelSubscribeRequest cancelSubscribeRequest = new CancelSubscribeRequest(cancelSubscribe);
            presenter.unsuscribe(cancelSubscribeRequest, new RxCallBack<SubscribeDeleteResponse>(getContext()) {
                @Override
                public void onSuccess(SubscribeDeleteResponse subscribeDeleteResponse) {
                    if (TextUtils.equals(Result.RETCODE_OK, subscribeDeleteResponse.getResult().getRetCode())) {
                        if (null != unSubscribeDialog && unSubscribeDialog.isShowing()) {
                            unSubscribeDialog.dismiss();
                        }
                        EventBus.getDefault().post(new OrderCenterFocusEvent(OrderCenterFocusEvent.MY_ORDER));
                        //退订成功弹出提示，重新请求数据
                        //按夏哥要求添加500毫秒延迟之后刷新数据，防止平台刷新数据不及时
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                requestOrderData();
                            }
                        },500);

                        showUnSubscribeDialog(1, null, null,null);
                    } else {
                        unSubscribeFailed();
                    }
                }

                @Override
                public void onFail(Throwable e) {
                    SuperLog.error(TAG, "unsubscribe fail , " + e);
                }
            });
        }
    }

    private void unSubscribeFailed() {
        EpgToast.showToast(getContext(), getActivity().getString(R.string.my_order_unsubscribe_fail));
    }

    @Override
    public void showDetail(VODDetail vodDetail, String actionId, List<RecmContents> recmContentsList) {
        if (null != mDetailPresenter) {
            this.vodDetail = vodDetail;
            mDetailPresenter.setVODDetail(vodDetail);
            playProduct(vodDetail);
        }
    }

    @Override
    public void showCollection(boolean isCollection) { }

    @Override
    public void setNewScore(List<Float> newScore) { }

    public boolean hasData() {
        return null != gridViewOrderList && gridViewOrderList.getChildCount() > 0;
    }

    public View getFirstItem() {
        return gridViewOrderList.getChildAt(0);
    }

    public void setFilterFocused() {
        tvFilter.requestFocus();
    }

    @Override
    public void showContentNotExit() { }

    @Override
    public Context context() {
        return null;
    }

    @Override
    public void showNoContent() { }

    @Override
    public void showError(String message) { }

    public void playProduct(VODDetail detail) {
        mDetailPresenter.setButtonOrderOrSee(true);
        if (VodUtil.isMiguVod(detail)) {
            RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.my_order_root_layout);
            MiguQRViewPopWindow popWindow = new MiguQRViewPopWindow(getContext(), detail.getCode(), MiguQRViewPopWindow.mSearchResultType);
            popWindow.showPopupWindow(rootLayout);
            return;
        }
        String vodType = detail.getVODType();
        if (vodType.equals("0")) {
            List<VODMediaFile> vodMediaFiles = detail.getMediaFiles();
            if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
                mDetailPresenter.playVOD(detail);
            } else {
                if (!HeartBeatUtil.getInstance().isSubscribedByPrice(detail, null == vodDetail ? "" : vodDetail.getPrice())) {
                    EpgToast.showToast(getContext(), "没有找到资源文件！");
                } else {
                    EpgToast.showToast(getContext(), "播放失败！");
                }
                return;
            }
        } else {
            List<Episode> episodes = detail.getEpisodes();
            Bookmark bookmark = detail.getBookmark();
            if (bookmark != null) {
                SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + bookmark.getItemID() + ",sitcomNO:" + bookmark.getSitcomNO() + ",breakpoint:" + bookmark.getRangeTime());
            }
            if (episodes != null && episodes.size() != 0) {
                Episode playEpisode = null;
                if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
                    for (Episode episode : episodes) {
                        if (bookmark.getSitcomNO().equals(episode.getSitcomNO())) {
                            playEpisode = episode;
                        }
                    }
                } else {
                    playEpisode = episodes.get(0);
                }
                if (null != playEpisode){
                    mDetailPresenter.playVOD(playEpisode);
                }
            } else {
                EpgToast.showToast(getContext(), "没有可播放的子集！");
                return;
            }
        }
    }

    //焦点在订购中心左侧选择TAB上时按下菜单键打开过滤Dialog
    public void onMenuKeyDown(){
        if ( mDialog !=null && mDialog.isShowing() ){
            mDialog.dismiss();
        } else {
            showFilterWindow();
        }
    }

    /**
     * 判断是否是边界
     *
     * @param root
     * @param focused
     * @param direction
     * @return
     */
    private boolean isBorder(ViewGroup root, View focused, int direction) {
        return FocusFinder.getInstance().findNextFocus(root, focused, direction) == null;
    }


    //落焦到日期过滤
    @Subscribe
    public void onEvent(OrderCenterMyOrderFocusEvent event){
        tvFilter.requestFocus();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        mPresenter.detachView();
    }

    /*******************************************ProductOrderContract*****************************/
    @Override
    public void generateProductListSucc(List<Product> productList) { }

    @Override
    public void generateProductListError() { }

    @Override
    public void queryUniPayInfoSucc(QueryUniPayInfoResponse response) { }

    @Override
    public void queryUniPayInfoError() { }

    @Override
    public void querySubscriberSucess(String userId) { }

    @Override
    public void querySubscriberfail() { }

    @Override
    public void queryProductInfoFail() { }

    @Override
    public void queryMultiqrySuccess(QueryMultiqryResponse response) {
        offerInfos = response.getOfferList();
        Log.i(TAG, "queryMultiqrySuccess:  "+ JsonParse.object2String(offerInfos));
        requestData();
    }

    @Override
    public void queryMultiqryFail() {
        requestData();
    }

    @Override
    public void queryMultiUserInfoSuccess(QueryMultiUserInfoResponse response, Intent intent) { }

    @Override
    public void queryMultiUserInfoFail(Intent intent) { }
}