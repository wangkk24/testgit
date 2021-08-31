package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.bean.response.QueryProductInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODListCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Controller.VODListController;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.Genre;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.ProduceZone;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.SubjectVODList;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6request.QuerySubjectDetailRequest;
import com.pukka.ydepg.common.utils.DeviceInfo;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.fragment.BaseMvpFragment;
import com.pukka.ydepg.launcher.ui.fragment.WebActivity;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.moudule.mytv.presenter.OrderCenterPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.view.OrderCenterView;
import com.pukka.ydepg.moudule.mytv.utils.QRCodeGenerator;
import com.pukka.ydepg.moudule.search.presenter.BasePresenter;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 订购中心-基本业务
 *
 * @FileName: com.pukka.ydepg.moudule.mytv.NewOrderCenterBusinessFragment.java
 * @author: weicy
 * @data: 2019-11-14 16.36
 * @Version V2.1 <描述当前版本功能>
 */
public class NewOrderCenterBusinessFragment extends BaseMvpFragment<OrderCenterPresenter> implements OrderCenterView, VODListCallBack {

    private static final String DEVICE_STBID = "ro.product.stb.stbid";

    private static final String Broadband_renew_url_key = "BroadbandRenewUrl";

    private static final String Broadband_TV_renew_url_key = "BroadbandTVRenewUrl";

    //宽带续费图片跳转的url
    private String BroadbandRenewUrl;

    //宽带电视续费图片跳转的url
    private String BroadbandTVRenewUrl;

    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_SMALL);

    BusinessPresenter businessPresenter;

    RxAppCompatActivity rxAppCompatActivity;

    Unbinder unbinder;

    @BindView(R.id.line)
    View line;
    @BindView(R.id.scan_title)
    TextView scanTitle;
    @BindView(R.id.tv_my_function_busness_account_title)
    TextView tvAccount;
    @BindView(R.id.tv_userName)
    TextView tvUserId;
    @BindView(R.id.tv_deviceVersion)
    TextView tvDevice;
    @BindView(R.id.tv_versionName)
    TextView tvVersion;
    @BindView(R.id.tv_expiretime)
    TextView tvExpireTime;
    @BindView(R.id.iv_order_center_userinfo_QRcode)
    ImageView ivUserinfoQRCode;
    @BindView(R.id.iv_order_center_broadband_tv_QR)
    ImageView ivTVQRCode;
    @BindView(R.id.iv_order_center_broadband_QR)
    ImageView ivBroadbandQRCode;
    @BindView(R.id.scan_right_container)
    LinearLayout scanRightContainer;
    @BindView(R.id.down_text)
    TextView downText;
    @BindView(R.id.scan_left_container)
    LinearLayout scanLeftContainer;
    @BindView(R.id.sacn_renew)     //宽带续费图片
    ImageView sacnRenew;
    @BindView(R.id.sacn_tv_renew) //宽带电视续费图片
    ImageView sacnTvRenew;
    @BindView(R.id.sacn_renew_layout)
    RelativeLayout sacnRenewLayout;
    @BindView(R.id.sacn_renew_tv_layout)
    RelativeLayout sacnRenewTvLayout;


    @Override
    protected void initView(View view) {
        showAccount();
        UserInfo userInfo = AuthenticateManager.getInstance().getUserInfo();
        String id = "";
        if (userInfo != null) {
            id = userInfo.getUserId();
        }
        tvUserId.setText(getBoldString(appendLabel(getString(R.string.my_user_name), id)));
        tvVersion.setText(getBoldString(appendLabel(getString(R.string.my_version_name), CommonUtil.getVersionName())));
        tvDevice.setText(getBoldString(appendLabel(getString(R.string.my_device_no),CommonUtil.getDeviceType())));
        presenter.querySubscribe();

        //改版，不再显示二维码
        scanRightContainer.setVisibility(View.GONE);
        scanLeftContainer.setVisibility(View.GONE);
        downText.setVisibility(View.GONE);
        if (mFocusHighlight != null) {
            mFocusHighlight.onInitializeView(sacnRenewLayout);
            mFocusHighlight.onInitializeView(sacnRenewTvLayout);
        }
        sacnRenew.setFocusable(false);
        sacnTvRenew.setFocusable(false);

        sacnRenewLayout.setVisibility(View.GONE);
        sacnRenewTvLayout.setVisibility(View.GONE);

        sacnRenew.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (null != sacnRenewLayout) {
                    sacnRenewLayout.setSelected(hasFocus);
                }
            }
        });

        sacnTvRenew.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (null != sacnRenewTvLayout) {
                    sacnRenewTvLayout.setSelected(hasFocus);
                }
            }
        });

        sacnRenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BroadbandRenewUrl.length() > 0) {
                    Intent intent = new Intent(getContext(), WebActivity.class);
                    intent.putExtra("url", BroadbandRenewUrl);
                    startActivity(intent);
                }
            }
        });

        sacnTvRenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BroadbandTVRenewUrl.length() > 0) {
                    Intent intent = new Intent(getContext(), WebActivity.class);
                    intent.putExtra("url", BroadbandTVRenewUrl);
                    startActivity(intent);
                }

            }
        });

        sacnRenew.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    return true;
                }
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                    if (sacnTvRenew.getVisibility() == View.VISIBLE && sacnTvRenew.isFocusable()){
                        return false;
                    }else{
                        return true;
                    }
                }
                return false;
            }
        });

        sacnTvRenew.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    return true;
                }
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                    if (sacnRenew.getVisibility() == View.VISIBLE && sacnRenew.isFocusable()){
                        return false;
                    }else{
                        return true;
                    }
                }
                return false;
            }
        });

        showQRCode();

        rxAppCompatActivity = (RxAppCompatActivity) OTTApplication.getContext().getCurrentActivity();

        businessPresenter = new BusinessPresenter(rxAppCompatActivity);

        //获取终端配置参数中的subjectID 用于查询图片url和跳转url的SubjectID
        String subjectID = CommonUtil.getConfigValue(Configuration.Key.BASE_BUSINESS_SUBJCET_ID);
        if (TextUtils.isEmpty(subjectID)) {
            line.setVisibility(View.INVISIBLE);
            scanTitle.setVisibility(View.INVISIBLE);
        } else {
            businessPresenter.querySubjectDetail(subjectID, this);
        }
    }

    private void showQRCode() {
        String billId = SessionService.getInstance().getSession().getAccountName();
        String userId = AuthenticateManager.getInstance().getUserInfo() == null ? "" : AuthenticateManager.getInstance().getUserInfo().getUserId();
        String stdId = DeviceInfo.getSystemInfo(DEVICE_STBID);
        StringBuilder sb = new StringBuilder();
        sb.append("billid:").append(billId).append(",userid:").append(userId).append(",stbid:").append(stdId).append(getString(R.string.broadband_account)).append(":");
        Bitmap userInfoQRCode = QRCodeGenerator.genQrCode(sb.toString(), 140, 140);
        ivUserinfoQRCode.setImageBitmap(userInfoQRCode);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.order_center_business_layout_new;
    }

    @Override
    protected void initPresenter() {
        presenter = new OrderCenterPresenter();
    }


    @Override
    public void querySubscriberSucess(String userId) {
        String maccount = "";
        if (!TextUtils.isEmpty(userId)) {
            if (userId.contains("null")) {
                maccount = "";
            } else {
                maccount = userId;
            }
        } else {
            maccount = "";
        }
        tvAccount.setText(getBoldString(appendLabel(getString(R.string.my_account_name), maccount)));
    }

    @Override
    public void loadSubscription(QueryProductInfoResponse response) { }

    @Override
    public void queryProductInfoFail() { }

    @Override
    public void loadProducts(List<Product> products) { }

    /**
     * 展示业务账号
     */
    public void showAccount() {
        String maccount = SessionService.getInstance().getSession().getAccountName();
        if (!TextUtils.isEmpty(maccount)) {
            if (maccount.contains("null")) {
                maccount = "";
            }
        } else {
            maccount = "";
        }
        tvAccount.setText(getBoldString(appendLabel(getString(R.string.my_account_name), maccount)));
    }


    private String appendLabel(String tag, String value) {
        return String.format("%s%s", tag, value);
    }

    private SpannableString getBoldString(String string) {
        SpannableString spannableString = new SpannableString(string);
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(styleSpan, 0, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableString;
    }

    @Override
    public void querySubjectDetailSuccess(int total, List<Subject> subjects) {

        if (null == sacnRenew || null == sacnTvRenew){
            return;
        }

        Subject subject = subjects.get(0);
        Picture picture = subject.getPicture();
        List<String> renewUrls = picture.getDrafts();
        List<String> renewTVUrls = picture.getBackgrounds();
        if (null != renewUrls && renewUrls.size() > 0) {
            sacnRenewLayout.setVisibility(View.VISIBLE);
            String renewUrl = renewUrls.get(0);
            RequestOptions options = new RequestOptions()
                    .placeholder(null)
                    .error(null);
            if (renewUrl.length() > 0 && null != sacnRenew) {
                Glide.with(this).load(renewUrl).apply(options).into(sacnRenew);
            }

        }
        if (null != renewTVUrls && renewTVUrls.size() > 0) {
            sacnRenewTvLayout.setVisibility(View.VISIBLE);
            String renewTVUrl = renewTVUrls.get(0);
            RequestOptions options = new RequestOptions()
                    .placeholder(null)
                    .error(null);
            if (renewTVUrl.length() > 0 && null != sacnTvRenew) {
                Glide.with(this).load(renewTVUrl).apply(options).into(sacnTvRenew);
            }

        }

        //将跳转链接保存
        List<NamedParameter> listNp = subject.getCustomFields();
        List<String> BroadbandRenewUrls = CommonUtil.getCustomNamedParameterByKey(listNp, Broadband_renew_url_key);
        List<String> BroadbandTVRenewUrls = CommonUtil.getCustomNamedParameterByKey(listNp, Broadband_TV_renew_url_key);
        if (null != BroadbandRenewUrls && BroadbandRenewUrls.size() > 0) {
            String url = BroadbandRenewUrls.get(0);
            BroadbandRenewUrl = url;
        } else {
            BroadbandRenewUrl = "";
        }
        if (null != BroadbandTVRenewUrls && BroadbandTVRenewUrls.size() > 0) {
            String url = BroadbandTVRenewUrls.get(0);
            BroadbandTVRenewUrl = url;
        } else {
            BroadbandTVRenewUrl = "";
        }

        if (!BroadbandRenewUrl.equals("") && sacnRenewLayout.getVisibility() == View.VISIBLE){
            sacnRenew.setFocusable(true);
        }

        if (!BroadbandTVRenewUrl.equals("") && sacnRenewTvLayout.getVisibility() == View.VISIBLE){
            sacnTvRenew.setFocusable(true);
        }
    }

    @Override public void queryVODListBySubjectSuccess(int total, List<VOD> vodList, String subjectId) { }

    @Override public void queryVODListBySubjectFailed() { }

    @Override public void queryVODSubjectListSuccess(int total, List<Subject> subjects) { }

    @Override public void queryVODSubjectListFailed() { }

    @Override public void querySubjectVODBySubjectIDSuccess(int total, List<SubjectVODList> subjectVODLists) { }

    @Override public void querySubjectVODBySubjectIDFailed() { }

    @Override public void getContentConfigSuccess(List<ProduceZone> produceZoneList, List<Genre> genreList) { }

    @Override public void getContentConfigFailed() { }

    @Override public void queryPSBRecommendSuccess(int total, List<VOD> vodDetails) { }

    @Override public void queryPSBRecommendFail() { }

    @Override public void onError(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class BusinessPresenter extends BasePresenter {
        VODListController controller;
        private RxAppCompatActivity rxAppCompatActivity;

        BusinessPresenter(RxAppCompatActivity rxAppCompatActivity) {
            this.rxAppCompatActivity = rxAppCompatActivity;
            this.controller = new VODListController(rxAppCompatActivity);

        }

        void querySubjectDetail(String subjectId, VODListCallBack mVODListCallBack) {
            QuerySubjectDetailRequest mQuerySubjectDetailRequest = new QuerySubjectDetailRequest();
            List<String> categoryList = new ArrayList<>();
            categoryList.add(subjectId);
            mQuerySubjectDetailRequest.setSubjectIds(categoryList);
            controller.querySubjectDetail(mQuerySubjectDetailRequest, compose(bindToLifecycle(rxAppCompatActivity)), mVODListCallBack);
        }
    }
}