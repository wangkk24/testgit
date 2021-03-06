package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.BroadCastConstant;
import com.pukka.ydepg.common.extview.VerticalScrollTextView;
import com.pukka.ydepg.common.http.bean.node.Marketing;
import com.pukka.ydepg.common.http.bean.node.OfferInfo;
import com.pukka.ydepg.common.http.bean.node.PayChannel;
import com.pukka.ydepg.common.http.bean.node.PayInfo;
import com.pukka.ydepg.common.http.bean.node.SubProdInfo;
import com.pukka.ydepg.common.http.bean.node.UnsubProdInfo;
import com.pukka.ydepg.common.http.bean.node.User;
import com.pukka.ydepg.common.http.bean.node.XmppMessage;
import com.pukka.ydepg.common.http.bean.request.OrderProductRequest;
import com.pukka.ydepg.common.http.bean.request.QueryMultiqryRequest;
import com.pukka.ydepg.common.http.bean.response.OrderProductResponse;
import com.pukka.ydepg.common.http.bean.response.QueryMultiqryResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.PriceObjectForVSS;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Profile;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscribe;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUniPayInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.SubscribeProductResponse;
import com.pukka.ydepg.common.http.vss.node.RespParam;
import com.pukka.ydepg.common.http.vss.response.QueryMultiUserInfoResponse;
import com.pukka.ydepg.common.report.ubd.extension.PurchaseData;
import com.pukka.ydepg.common.report.ubd.scene.UBDPurchase;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.TextUtil;
import com.pukka.ydepg.common.utils.base64Utils.AepAuthUtil;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.common.utils.uiutil.IdentifyingCode;
import com.pukka.ydepg.event.FinishPlayUrlEvent;
import com.pukka.ydepg.event.PlayUrlEvent;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.fragment.WebActivity;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.mytv.ZjYdUniAndPhonePayActivty;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.bean.LogDescExtInfo;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.bean.UnsubscribeTip;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.AvoidRepeatPaymentUtils;
import com.pukka.ydepg.moudule.mytv.presenter.PayPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.PayQrCodeLinkPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.ProductOrderPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.UniPayPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.contract.PayContract;
import com.pukka.ydepg.moudule.mytv.presenter.contract.PayQrCodeLinkContract;
import com.pukka.ydepg.moudule.mytv.presenter.contract.ProductOrderContract;
import com.pukka.ydepg.moudule.mytv.presenter.contract.UniPayContract;
import com.pukka.ydepg.moudule.mytv.utils.DoubleArithmeticUtil;
import com.pukka.ydepg.moudule.player.node.CurrentChannelPlaybillInfo;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.view.DetailDataView;
import com.pukka.ydepg.service.NtpTimeService;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewMyPayModeActivity.PAYMENT_UNI_PAY;

public class NewUniPayFragment extends NewPaymentFragment implements PayQrCodeLinkContract.View, View
        .OnClickListener, UniPayContract.View, PayContract.View, DetailDataView, ProductOrderContract.View
{
    private final String TAG = this.getClass().getName();

    /**
     * ?????????
     */
    FrameLayout root_view;

    /**
     * ??????????????????
     */
    LinearLayout qrcode_layout;

    /**
     * ?????????
     */
    ImageView mqrcode_Img;

    /**
     * ??????????????????
     */
    RelativeLayout uni_pay_lay;

    /**
     * ????????????
     */
    TextView accounttv;

    /**
     * ????????????
     */
    Button comfirm_btn;
    /**
     * ??????????????????????????????????????????
     */
    Button comfirm_btn_normal;

    /**
     * ??????????????????????????????
     */
    TextView child_hint;

    /**
     * ?????????????????????????????????????????????
     */
    TextView hintNormaltv;
    /**
     * ?????????????????????
     */
    private RelativeLayout code_rly;

    /**
     * ??????????????????
     */
    private RelativeLayout account_hint_layout;

    /**
     * ?????????????????????
     */
    ImageView  identifyingCodeImg;
    /**
     * ????????????????????????
     */
    EditText et_code_number;

    /**
     * ??????????????????????????????
     */
    RelativeLayout verification_ly;

    /**
     * ?????????????????????????????????
     */
    EditText et_input_verification;

    /**
     * ?????????????????????????????????????????????
     */
    Button get_verification_btn;

    /**
     * ????????????????????????
     */
    TextView children_child_hint;



    /**
     * ???????????????
     */
    RelativeLayout children_pay_lay;
    /**
     * ????????????
     */
    TextView children_account;

    Button children_comfirm_btn;

    //???????????????
    TextView product_title_textview;

    //???????????????
    TextView product_price_textview;

    //?????????????????????
    TextView product_dicount_tip;

    //?????????????????????
    TextView product_prcie_mode_textview;

    //???????????????
    TextView product_orgin_price_textview;

    //???????????????
    VerticalScrollTextView product_describe_textview;

    //????????????tip
    TextView product_describe_tip;

    private String uni_pay_type;

    /**
     * PayQrCodeLinkPresenter
     */
    private PayQrCodeLinkPresenter PayQrCodemPresenter;
    /**
     * UniPayPresenter
     */
    private UniPayPresenter uniPayPresenter;

    /**
     * ??????????????????
     */
    private boolean isOrderCenter = false;
    /**
     * ??????presenter
     */
    private PayPresenter mPayPresenter = new PayPresenter();
    /**
     * ????????????????????????
     */
    private long mValidTime = 900 * 1000L;
    /**
     * ??????ID
     */
    private String mUserID;
    /**
     * ????????????
     */
    private Product mProductInfo;
    /**
     * ??????????????????????????????
     */
    private static final String DEFAULT_LINK_URL = "https://app.m.zj.chinamobile" + "" +
            ".com/zjweb/sjyytv4/kuandaiTVThirdPay/index.html";

    /**
     * ??????ID
     */
    private String mBillId = "";

    private boolean isClick = true;

    /**
     * VOD??????
     */
    private VODDetail mVODDetail;

    /**
     * ??????????????????
     */
    private PopupWindow mPaymentPopwindow;

    private int sms_count_time = 60;

    private int comfirm_count_time=6;

    /**
     * ?????????????????????
     */
    private boolean isTrySeeSubscribe = false;

    /**
     * ?????????VOD??????
     */
    private boolean isVODSubscribe = false;


    /**
     * ?????????TVOD??????
     */
    private boolean isTVODSubscribe = false;


    private boolean isOffScreen = false;

    /**
     * ??????ID???????????????ID
     */
    private String[] mCHANNELMEDIA_ID = null;

    private XmppMessage mXmppMessage;

    /**
     * profile??????
     */
    private Profile mProfile;

    private String qrcodeUrl;

    private List<UnsubProdInfo> unsubProdInfos = new ArrayList<>();

    private Product marketProduct;


    private final int  SMS_LOOP_FLAG=10000;

    private final int COMFIRM_LOOP_FLAG=11111;

    //???????????? ????????????
    private static final String cornreMarketInfo = "CORNRE_MARKER_INFO";

    //???????????? ?????????????????????
    private static final String orgPriceKey = "ORIGINAL_PRICE";

    /*??????????????????????????????????????????????????????
     */
    private Map<String,List<UnsubscribeTip>> unSubscribeTips = new HashMap<>();

    //?????????????????????????????????productid
    private String giftProductIds = "[\"600000591415\"???\"600000591411\"???\"600000485623\"???\"600000485613\"???\"600000495388\"???\"600000501108\"???\"600000526411\"???\"600000512741\"???\"600000512682\"???\"600000501100\"???\"600000522848\"???\"600000535723\"???\"600000535725\"???\"600000526409\"???\"600000633267\"]";

    //?????????????????????????????????
    private String giftedOffid = "600000631792";
    //??????????????????
    private String prepaymentOfferid = "600000643695";

    private ProductOrderPresenter mPresenter = new ProductOrderPresenter();

    private List<OfferInfo> offerInfos;

    //???????????????url
    public static final String giftUrl = "http://aikanvod.miguvideo.com:8858/pvideo/p/AC_RemoteCollection.jsp";

    //????????????
    private int payType;

    //?????????
    private String code;

    private UniPayPresenter.QueryResultBalCallback callback = new UniPayPresenter.QueryResultBalCallback() {
        @Override
        public void QueryResultBalSuccess(RespParam param, String comeFrom) {
            //?????????????????????
            String nowBillStr = "";
            double nowBill = 0;
            if (null != param.getBusinessInfo() && null != param.getBusinessInfo().getBalance()){
                nowBillStr = param.getBusinessInfo().getBalance();
            }else{
                EpgToast.showToast(OTTApplication.getContext(),"??????????????????");
            }
            if (nowBillStr.length()>0){
                nowBill = Double.parseDouble(nowBillStr) / 100;
            }else{
                EpgToast.showToast(OTTApplication.getContext(),"??????????????????");
            }

            //?????????????????????????????????
            String minimumAmountStr = SessionService.getInstance().getSession().getTerminalConfigurationOrderPhoneBillPayMinimumAmount();
            double minimumAmount = 0;
            if (null != minimumAmountStr && TextUtils.isEmpty(minimumAmountStr)){
                if (isNumeric(minimumAmountStr)){
                    minimumAmount = Double.parseDouble(minimumAmountStr)/100;
                }
            }

            Product product = mProductInfo;
            if (null != marketProduct){
                product = marketProduct;
            }

            //???????????????
            double price = Double.parseDouble(product.getPrice()) / 100;
            if (nowBill < price){
                //???????????????????????????????????????????????????????????????????????????????????????
                showNotEnoughWindow();
            }else if (DoubleArithmeticUtil.compare(DoubleArithmeticUtil.sub(nowBill,price),minimumAmount) == -1){
                //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                showNotEnoughWindow();
            }else{
                switch (comeFrom){
                    case UniPayPresenter.unipayment:{
                        uni_pay_comfirm_click();
                        break;
                    }
                    case UniPayPresenter.unipayment_child:{
                        child_pay_comfirm_click();
                        break;
                    }
                    default:
                }
            }

        }

        //????????????????????????
        public boolean isNumeric(String str){
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            if( !isNum.matches() ){
                return false;
            }
            return true;
        }

        private void showNotEnoughWindow(){
            View comfirmView = LayoutInflater.from(getActivity()).inflate(R.layout
                    .window_pip_payment_not_enough, null);
            Button comfirmbtn = (Button) comfirmView.findViewById(R.id.confirm_btn);
            mNotEnoughWindow = new PopupWindow(comfirmView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mNotEnoughWindow.setFocusable(true);
            ColorDrawable dw = new ColorDrawable(0x00000000);
            mNotEnoughWindow.setBackgroundDrawable(dw);
            mNotEnoughWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
            {
                @Override
                public void onDismiss()
                {
                    isClick = true;
                }
            });
            comfirmbtn.setOnClickListener(view ->
            {
                mNotEnoughWindow.dismiss();
            });
            if (!mNotEnoughWindow.isShowing())
            {
                mNotEnoughWindow.showAtLocation(root_view, Gravity.CENTER, 0, 0);
                comfirmbtn.setFocusable(true);
                comfirmbtn.requestFocus();
            }
        }

        @Override
        public void QueryResultBalFail(String comeFrom) {
            isClick = true;
            EpgToast.showToast(OTTApplication.getContext(),"??????????????????");
        }
    };

    private PopupWindow mNotEnoughWindow;

    //??????????????????
    private static class UniPayHandler extends Handler {

        private final int  SMS_LOOP_FLAG=10000;

        private final int COMFIRM_LOOP_FLAG=11111;

        private WeakReference<NewUniPayFragment> mReference;

        UniPayHandler(NewUniPayFragment fragment) {
            this.mReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (null == mReference || null == mReference.get()){
                return;
            }
            switch (msg.what)
            {
                case SMS_LOOP_FLAG:
                    mReference.get().sms_count_time--;
                    if (mReference.get().sms_count_time != 0)
                    {
                        mReference.get().get_verification_btn.setText(String.format("????????????(%1$s)", String.valueOf(mReference.get().sms_count_time)));
                        sendEmptyMessageDelayed(mReference.get().SMS_LOOP_FLAG, 1000);
                    }
                    else
                    {
                        mReference.get().get_verification_btn.setText("????????????");
                        mReference.get().get_verification_btn.setFocusable(true);//??????????????????
                        removeMessages(mReference.get().SMS_LOOP_FLAG);
                    }
                    break;
                case COMFIRM_LOOP_FLAG:
                    mReference.get().comfirm_count_time--;
                    if(mReference.get().payType==COMMON_PAY)
                    {
                        mReference.get().comfirmLoop(CommonUtil.showVerifyCode()?mReference.get().comfirm_btn:mReference.get().comfirm_btn_normal);

                    }else{
                        mReference.get().comfirmLoop(mReference.get().children_comfirm_btn);
                    }
                    break;

            }
        }
    }

    private UniPayPresenter.SendVerifiedCodeCallBack sendVerifiedCodeCallBack = new UniPayPresenter.SendVerifiedCodeCallBack() {
        @Override
        public void sendVerifiedCodeSuccsee() {

        }

        @Override
        public void sendVerifiedCodeFail(String description) {
            EpgToast.showLongToast(OTTApplication.getContext(),
                    "?????????????????????");
        }
    } ;

    private UniPayHandler mHandler = new UniPayHandler(this);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_uni_pay_new, container, false);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this);

        String billId = SessionService.getInstance().getSession().getAccountName();
        QueryMultiqryRequest request = new QueryMultiqryRequest();
        //TODO
        request.setMessageID("0000");
        request.setBillID(billId);
        request.setValidType("1");
        mPresenter.queryMultiqry(request);
    }

    @Override
    public void queryMultiqrySuccess(QueryMultiqryResponse response) {
        offerInfos = response.getOfferList();
        initView();
        initDate();
    }

    @Override
    public void queryMultiqryFail() {
        initView();
        initDate();
    }

    private void initView()
    {
        account_hint_layout = (RelativeLayout) findViewById(R.id.account_hint_layout);

        qrcode_layout = (LinearLayout) findViewById(R.id.qrcode_layout);
        mqrcode_Img = (ImageView) findViewById(R.id.iv_unipay_qrcode);
        uni_pay_lay = (RelativeLayout) findViewById(R.id.uni_pay_lay);
        accounttv = (TextView) findViewById(R.id.account);
        comfirm_btn = (Button) findViewById(R.id.comfirm_btn);
        comfirm_btn_normal=(Button) findViewById(R.id.comfirm_btn_normal);
        code_rly= (RelativeLayout) findViewById(R.id.code_rly);
        verification_ly = (RelativeLayout) findViewById(R.id.verification_ly);
        et_input_verification = (EditText) findViewById(R.id.et_input_verification);
        child_hint = (TextView) findViewById(R.id.child_hint);
        hintNormaltv= (TextView) findViewById(R.id.child_hint_normal);
        get_verification_btn = (Button) findViewById(R.id.get_verification);
        et_code_number= (EditText) findViewById(R.id.et_code_number);
        root_view = (FrameLayout) findViewById(R.id.root_view);
        identifyingCodeImg= (ImageView) findViewById(R.id.identifyingCodeImg);
        children_child_hint = (TextView) findViewById(R.id.children_child_hint);

        children_pay_lay = (RelativeLayout) findViewById(R.id.children_pay_lay);
        children_account = (TextView) findViewById(R.id.children_account);
        children_comfirm_btn = (Button) findViewById(R.id.children_comfirm_btn);
        comfirm_btn.setOnClickListener(this);
        comfirm_btn_normal.setOnClickListener(this);
        get_verification_btn.setOnClickListener(this);
        children_comfirm_btn.setOnClickListener(this);

        children_comfirm_btn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction())
                {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        et_input_verification.requestFocus();
                        return true;
                    }
                    if ( keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT ){
                        if (product_describe_textview.getVisibility() == View.VISIBLE){
                            product_describe_textview.requestFocus();
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        get_verification_btn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction())
                {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        if(null!=getActivity()){
                            NewMyPayModeActivity activity = (NewMyPayModeActivity) getActivity();
                            if (null != activity.getNowTitle()){
                                activity.getNowTitle().setFocusable(true);
                                activity.getNowTitle().requestFocus();
                            }
                        }
                        return true;
                    }
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                    if (product_describe_textview.getVisibility() == View.VISIBLE){
                        product_describe_textview.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });
        et_input_verification.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (KeyEvent.ACTION_DOWN == event.getAction())
                {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
                    {

                        if (get_verification_btn.isFocusable()){
                            get_verification_btn.requestFocus();
                            return true;
                        }else{
                            if(null!=getActivity()){
                                NewMyPayModeActivity activity = (NewMyPayModeActivity) getActivity();
                                if (null != activity.getNowTitle()){
                                    activity.getNowTitle().setFocusable(true);
                                    activity.getNowTitle().requestFocus();
                                }
                            }
                            return true;
                        }

                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                        if (product_describe_textview.getVisibility() == View.VISIBLE){
                            product_describe_textview.requestFocus();
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v instanceof Button){
                    Button btn = (Button)v;
                    if (hasFocus){
                        btn.setTextColor(getContext().getResources().getColor(R.color.white_0));
                    }else {
                        btn.setTextColor(getContext().getResources().getColor(R.color.order_paymode_buttom_textcolor));
                    }
                }
            }
        };

        comfirm_btn.setOnFocusChangeListener(listener);
        comfirm_btn_normal.setOnFocusChangeListener(listener);
        get_verification_btn.setOnFocusChangeListener(listener);
        children_comfirm_btn.setOnFocusChangeListener(listener);

        unSubscribeTips = SessionService.getInstance().getSession().getTerminalConfigurationUnsubscribeTips();
    }

    private void initDate() {
        qrcodeUrl = SessionService.getInstance().getSession().getTerminalConfigurationValue("epg_bill_qrcode_url");

        String productIds = SessionService.getInstance().getSession().getTerminalConfigurationGiftProductId();

        if (null != productIds && !TextUtils.isEmpty(productIds)){
            giftProductIds = productIds;
        }

        if (TextUtils.isEmpty(qrcodeUrl)) {
            qrcodeUrl = DEFAULT_LINK_URL;
        }
        uni_pay_type = getArguments().getString(ZjYdUniAndPhonePayActivty.UNI_PAY_TYPE);
        mProductInfo = JsonParse.json2Object(getArguments().getString(ZjYdUniAndPhonePayActivty.PRODUCT_INFO), Product.class);
        String unsubProdInfoIdStr = getArguments().getString(ZjYdUniAndPhonePayActivty.UNSUBPRODINFOIDS);
        if (!TextUtils.isEmpty(unsubProdInfoIdStr)) {
            List<String> unsubProdInfoIds = JsonParse.jsonToStringList(unsubProdInfoIdStr);
            if (null != unsubProdInfoIds && unsubProdInfoIds.size() > 0) {
                for (int i = 0; i < unsubProdInfoIds.size(); i++) {
                    UnsubProdInfo info = new UnsubProdInfo();
                    info.setProductId(unsubProdInfoIds.get(i));
                    info.setApplyMode("0");
                    info.setType(UnsubProdInfo.UnSubProdInfoType.BIG_SMAIL_PRODUCT);
                    unsubProdInfos.add(info);
                }
            }
        }
        mBillId = getArguments().getString(ZjYdUniAndPhonePayActivty.UNIPAY_BILLID);
        String marketProductStr = getArguments().getString(ZjYdUniAndPhonePayActivty.MARKETING_PRODUCT);
        if (!TextUtils.isEmpty(marketProductStr)) {
            marketProduct = JsonParse.json2Object(marketProductStr, Product.class);
        }
        Log.i(TAG, "initDate:  "+ JsonParse.object2String(marketProduct));
        if (ZjYdUniAndPhonePayActivty.UNI_CODE_MODE.equals(uni_pay_type) ||
                ZjYdUniAndPhonePayActivty.UNONPENED_UNI_MODE.equals(uni_pay_type) || TextUtils
                .isEmpty(uni_pay_type)) {
            //???????????????
            uniCodeMode();
        } else {
            String vodDetailJson = getArguments().getString(ZjYdUniAndPhonePayActivty.VOD_DETAIL);
            if (!TextUtils.isEmpty(vodDetailJson)) {
                mVODDetail = JsonParse.json2Object(vodDetailJson, VODDetail.class);
            }
            if (!TextUtils.isEmpty(mBillId)) {
                accounttv.setText(mBillId);
                children_account.setText(mBillId);
            }

            isVODSubscribe = getArguments().getBoolean(ZjYdUniAndPhonePayActivty.ISVOD_SUBSCRIBE, false);
            isTVODSubscribe = getArguments().getBoolean(ZjYdUniAndPhonePayActivty.ISTVOD_SUBSCRIBE, false);
            isOrderCenter = getArguments().getBoolean(ZjYdUniAndPhonePayActivty.ISORDERCENTER, false);
            isTrySeeSubscribe = getArguments().getBoolean(ZjYdUniAndPhonePayActivty.IS_TRY_SEE_SUBSCRIBE, false);
            isOffScreen = getArguments().getBoolean(ZjYdUniAndPhonePayActivty.ISOFFSCREEN_SUBSCRIBE, false);
            mCHANNELMEDIA_ID = getArguments().getStringArray(ZjYdUniAndPhonePayActivty.CHANNELID_MEDIAID);
            if (getArguments().getSerializable(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE) != null) {
                mXmppMessage = (XmppMessage) getArguments().getSerializable(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE);
            }
            List<Profile> profileList = SessionService.getInstance().getSession().getProfileList();
            if (profileList != null && profileList.size() != 0) {
                mProfile = profileList.get(0);
            }
            uniPayPresenter = new UniPayPresenter();
            uniPayPresenter.attachView(this);
            mPayPresenter.attachView(this);
            //??????
            if (ZjYdUniAndPhonePayActivty.UNI_CHILDREN_PAY_MODE.equals(uni_pay_type)) {
                //?????????????????????
                childMode();
            } else {
                List<String> secondComfirmProductIds = SessionService.getInstance().getSession()
                        .getTerminalConfigurationSecondConfirmProductIDs();
                if (null != secondComfirmProductIds && secondComfirmProductIds.size() > 0 && null
                        != mProductInfo) {
                    if (secondComfirmProductIds.contains(mProductInfo.getID())) {
                        //?????????????????????
                        childMode();
                    } else {
                        //??????????????????
                        uniPayMode();
                    }
                } else {
                    //??????????????????
                    uniPayMode();
                }
            }
        }

        //2.2???????????? ??????????????????????????????
        initProductInfo();
    }

    private void initProductInfo(){
        //???????????????
        product_title_textview = (TextView) findViewById(R.id.order_paymode_item_title);
        //???????????????
        product_price_textview = (TextView) findViewById(R.id.order_paymode_price);
        //?????????????????????
        product_dicount_tip = (TextView) findViewById(R.id.order_paymode_item_discount);
        //?????????????????????
        product_prcie_mode_textview = (TextView) findViewById(R.id.order_paymode_price_mode);
        //???????????????
        product_orgin_price_textview = (TextView) findViewById(R.id.order_paymode_item_discountprice);
        //???????????????
        product_describe_textview = (VerticalScrollTextView) findViewById(R.id.order_paymode_event_text);
        //????????????tip
        product_describe_tip = (TextView) findViewById(R.id.order_paymode_event_tip);
        if (null == mProductInfo){
            return;
        }

        product_describe_textview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction()) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        if(null!=getActivity()){
                            NewMyPayModeActivity activity = (NewMyPayModeActivity) getActivity();
                            if (null != activity.getNowTitle()){
                                activity.getNowTitle().setFocusable(true);
                                activity.getNowTitle().requestFocus();
                            }
                        }
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                        return true;
                    }
                }
                return false;
            }
        });

        Product product = mProductInfo;
        if (null != marketProduct){
            product = marketProduct;
        }

        //????????????
        String name = mProductInfo.getName();
        product_title_textview.setText(name);
        product_title_textview.setSelected(true);

        //???????????????
        double price = Double.parseDouble(product.getPrice()) / 100;
        product_price_textview.setText(decimalFormat.format(price));

        //??????????????????
        List<NamedParameter> listNp = product.getCustomFields();
        if (null != marketProduct){
            List<String> marketInfo = CommonUtil.getCustomNamedParameterByKey(listNp,cornreMarketInfo);
            if (null != marketInfo && marketInfo.size()>0){
                String str = marketInfo.get(0);
                if (!TextUtils.isEmpty(str)){
                    product_dicount_tip.setVisibility(View.VISIBLE);
                    product_dicount_tip.setText(str);
                }else{
                    product_dicount_tip.setText("");
                    product_dicount_tip.setVisibility(View.INVISIBLE);
                }
            }else{
                product_dicount_tip.setText("");
                product_dicount_tip.setVisibility(View.INVISIBLE);
            }

            //??????????????????
            List<String> orgPrice = CommonUtil.getCustomNamedParameterByKey(listNp,orgPriceKey);
            if (null != orgPrice && orgPrice.size()>0){
                String str = orgPrice.get(0);
                if (!TextUtils.isEmpty(str)){
                    SpannableString spannableString = new SpannableString(str);
                    StrikethroughSpan colorSpan = new StrikethroughSpan();
                    spannableString.setSpan(colorSpan, 0, spannableString.length() -1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    product_orgin_price_textview.setVisibility(View.VISIBLE);
                    product_orgin_price_textview.setText(spannableString);
                }else{
                    product_orgin_price_textview.setVisibility(View.GONE);
                    product_orgin_price_textview.setText("");
                }
            }else{
                product_orgin_price_textview.setVisibility(View.GONE);
                product_orgin_price_textview.setText("");
            }
        }else{
            product_dicount_tip.setText("");
            product_dicount_tip.setVisibility(View.INVISIBLE);
            product_orgin_price_textview.setVisibility(View.GONE);
            product_orgin_price_textview.setText("");
        }

        /*
         * ????????????,0 ????????????;1 ?????????
         */
        if((!TextUtils.isEmpty(mProductInfo.getProductType()) && mProductInfo.getProductType().equals("1"))){
            product_prcie_mode_textview.setText("???/???");
        }else{
            product_prcie_mode_textview.setText("???/"+  StringUtils.analyticValidity(mProductInfo));
        }

        if (null != marketProduct){
            product_describe_tip.setVisibility(View.VISIBLE);
            //???????????????
            product_describe_textview.setText(product.getIntroduce());
//            product_describe_textview.setAuto(true);
            product_describe_textview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    VerticalScrollTextView view = (VerticalScrollTextView)v;
                    view.setSelected(hasFocus);
                    view.setAuto(hasFocus);
                }
            });
        }else{
            product_describe_textview.setText("");
            product_describe_textview.setVisibility(View.INVISIBLE);
            product_describe_tip.setVisibility(View.INVISIBLE);
        }


    }

    /**
     * ???????????????
     */
    private void uniCodeMode() {
        uni_pay_lay.setVisibility(View.GONE);
        children_pay_lay.setVisibility(View.GONE);
        qrcode_layout.setVisibility(View.VISIBLE);
        PayQrCodemPresenter = new PayQrCodeLinkPresenter();
        PayQrCodemPresenter.attachView(this);
        //??????????????????
        String validTime = SessionService.getInstance().getSession().getTerminalConfigurationValue("epg_bill_qrcode_validTime");
        if (!TextUtils.isEmpty(validTime)) {
            mValidTime = Long.parseLong(validTime) * 1000;
        }
        PayQrCodemPresenter.querySubscriberInfo(new QueryUniInfoRequest(), getActivity());
    }

    /**
     * ???????????????
     */
    private void childMode() {
        uni_pay_lay.setVisibility(View.GONE);
        qrcode_layout.setVisibility(View.GONE);
        children_pay_lay.setVisibility(View.VISIBLE);
        String str = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.CHILDREN_LOCK_ONPEN_HINT);
        if (!TextUtils.isEmpty(str)) {
            children_child_hint.setText(str);
            children_child_hint.setVisibility(View.VISIBLE);
        } else {
            children_child_hint.setVisibility(View.GONE);
        }
        TextUtil.setEditTextFilter(et_input_verification, 6);
    }


    View.OnKeyListener  myKeyListener=new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (KeyEvent.ACTION_DOWN == event.getAction()) {
                if(null!=getActivity()){
                    NewMyPayModeActivity activity = (NewMyPayModeActivity) getActivity();
                    if (null != activity.getNowTitle()){
                        activity.getNowTitle().setFocusable(true);
                        activity.getNowTitle().requestFocus();
                    }
                }
            }
            return false;
        }
    };

    /**
     * ??????????????????
     */
    private void uniPayMode() {
        uni_pay_lay.setVisibility(View.VISIBLE);
        qrcode_layout.setVisibility(View.GONE);
        children_pay_lay.setVisibility(View.GONE);
        if(CommonUtil.showVerifyCode()) {

            //????????????
//            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) uni_pay_lay.getLayoutParams();
//            layoutParams.topMargin = 0;
//            uni_pay_lay.setLayoutParams(layoutParams);

            code_rly.setVisibility(View.VISIBLE);
            comfirm_btn_normal.setVisibility(View.GONE);
            hintNormaltv.setVisibility(View.GONE);
        }else{
            //????????????
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) account_hint_layout.getLayoutParams();
//            layoutParams.leftMargin = 0;
//            account_hint_layout.setLayoutParams(layoutParams);

            code_rly.setVisibility(View.GONE);
            comfirm_btn_normal.setVisibility(View.VISIBLE);
            hintNormaltv.setVisibility(View.VISIBLE);
        }
        identifyingCodeImg.setImageBitmap(IdentifyingCode.getInstance().createBitmap(getContext().getResources().getDimensionPixelOffset(R.dimen.margin_102),getContext().getResources().getDimensionPixelOffset(R.dimen.style_one_scroll_marginLeft)));
        code=IdentifyingCode.getInstance().getCode();
        identifyingCodeImg.setFocusable(true);
        identifyingCodeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                identifyingCodeImg.setImageBitmap(IdentifyingCode.getInstance().createBitmap(getContext().getResources().getDimensionPixelOffset(R.dimen.margin_102),getContext().getResources().getDimensionPixelOffset(R.dimen.style_one_scroll_marginLeft)));
                code=IdentifyingCode.getInstance().getCode();
            }
        });
        identifyingCodeImg.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction()) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        if(null!=getActivity()){
                            NewMyPayModeActivity activity = (NewMyPayModeActivity) getActivity();
                            if (null != activity.getNowTitle()){
                                activity.getNowTitle().setFocusable(true);
                                activity.getNowTitle().requestFocus();
                            }
                        }
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                        return true;
                    }
                }
                return false;
            }
        });
        et_code_number.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction()) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        if(null!=getActivity()){
                            NewMyPayModeActivity activity = (NewMyPayModeActivity) getActivity();
                            if (null != activity.getNowTitle()){
                                activity.getNowTitle().setFocusable(true);
                                activity.getNowTitle().requestFocus();
                            }
                        }
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                        if (product_describe_textview.getVisibility() == View.VISIBLE){
                            product_describe_textview.requestFocus();
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        comfirm_btn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction()) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {

                        if (et_code_number.isFocusable()){
                            et_code_number.requestFocus();
                            return true;
                        }else{
                            if(null!=getActivity()){
                                NewMyPayModeActivity activity = (NewMyPayModeActivity) getActivity();
                                if (null != activity.getNowTitle()){
                                    activity.getNowTitle().setFocusable(true);
                                    activity.getNowTitle().requestFocus();
                                }
                            }
                            return true;
                        }
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                        if (product_describe_textview.getVisibility() == View.VISIBLE){
                            product_describe_textview.requestFocus();
                        }
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                        return true;
                    }
                }
                return false;
            }
        });
        comfirm_btn_normal.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction()) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        if(null!=getActivity()){
                            NewMyPayModeActivity activity = (NewMyPayModeActivity) getActivity();
                            if (null != activity.getNowTitle()){
                                activity.getNowTitle().setFocusable(true);
                                activity.getNowTitle().requestFocus();
                            }
                        }
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                        if (product_describe_textview.getVisibility() == View.VISIBLE){
                            product_describe_textview.requestFocus();
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        String str = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.CHILDREN_LOCK_HINT);
        if (!TextUtils.isEmpty(str)) {
            if(CommonUtil.showVerifyCode()) {
                child_hint.setText(str);
                child_hint.setVisibility(View.VISIBLE);
                hintNormaltv.setVisibility(View.GONE);
            }else{
                hintNormaltv.setText(str);
                hintNormaltv.setVisibility(View.VISIBLE);
                child_hint.setVisibility(View.GONE);
            }

        } else {
            child_hint.setVisibility(View.GONE);
            hintNormaltv.setVisibility(View.GONE);
        }
    }

    public void comfirmLoop(Button comfirmbtn) {
        if (comfirm_count_time != 0&&comfirm_count_time>0)
        {
            comfirmbtn.setClickable(false);
            comfirmbtn.setText(String.format("?????????(%1$s)", String.valueOf(comfirm_count_time)));

            mHandler.sendEmptyMessageDelayed(COMFIRM_LOOP_FLAG, 1000);
        }
        else
        {
            isCanPressBack=true;
            comfirmbtn.setClickable(true);
            comfirmbtn.setText(R.string.comfirm_pay);
        }
    }


    @Override
    public Context context()
    {
        return null;
    }

    @Override
    public void showNoContent() { }

    @Override
    public void showError(String message) { }

    @Override
    public <Z> LifecycleTransformer<Z> bindToLife()
    {
        return null;
    }

    @Override
    public void onResultQRCode(Bitmap resultBitmap) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                mqrcode_Img.setImageBitmap(resultBitmap);
            }
        });
        SuperLog.debug(TAG, "[onResultQRCode] >>>>> start Timer");
        //???????????????
        Observable.timer(mValidTime, TimeUnit.SECONDS).compose(bindToLifecycle()).subscribeOn
                (Schedulers.io()).compose(bindUntilEvent(FragmentEvent.DESTROY.DESTROY))
                .unsubscribeOn(Schedulers.io()).subscribe(new RxCallBack<Long>(getActivity()) {
            @Override
            public void onSuccess(@NonNull Long aLong) {
                if (null != PayQrCodemPresenter) {
                    SuperLog.debug(TAG, "start generateQrCode");
                    PayQrCodemPresenter.generateQrCode(qrcodeUrl, mUserID, mProductInfo != null ? mProductInfo.getName() : "");
                }
            }

            @Override
            public void onFail(@NonNull Throwable e) {
                SuperLog.debug(TAG, "start Timer >>>>> onFail:" + e.getMessage());
            }
        });
    }

    @Override
    public void onQuerySubscriberInfoSucc(QuerySubscriberResponse response) {
        List<NamedParameter> customFields = response.getSubscriber().getCustomFields();
        if (!CollectionUtil.isEmpty(customFields)) {
            for (NamedParameter namedParameter : customFields) {
                if (ZjYdUniAndPhonePayActivty.BILL_ID.equals(namedParameter.getKey()) &&
                        !CollectionUtil.isEmpty(namedParameter.getValues())) {
                    mUserID = namedParameter.getValues().get(0);
                    SuperLog.debug(TAG, "userId=" + mUserID);
                    break;
                }
            }
        }
        //???????????????
        if (null != PayQrCodemPresenter) {
            PayQrCodemPresenter.generateQrCode(qrcodeUrl, mUserID, mProductInfo != null ?
                    mProductInfo.getName() : "");
        }
    }

    @Override
    public void onQuerySubscriberInfoError() { }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comfirm_btn:
                //?????????????????????????????????
                if(TextUtils.isEmpty(et_code_number.getText().toString())||!et_code_number.getText().toString().equalsIgnoreCase(code)){
                    EpgToast.showToast(OTTApplication.getContext(),"?????????????????????");
                    return;
                }

                //????????????????????????
                boolean canpay1 = AvoidRepeatPaymentUtils.getInstance().canPay(mProductInfo.getID());
                if (!canpay1){
                    EpgToast.showToast(getActivity(), "??????????????????");
                    return;
                }

                if (!isClick)
                {
                    return;
                }
                isClick = false;

                uniPayPresenter.queryResultBal(mBillId,getContext(),callback,UniPayPresenter.unipayment);
//                uni_pay_comfirm_click();
                break;
            case R.id.comfirm_btn_normal:

                //????????????????????????
                boolean canpay2 = AvoidRepeatPaymentUtils.getInstance().canPay(mProductInfo.getID());
                if (!canpay2){
                    EpgToast.showToast(getActivity(), "??????????????????");
                    return;
                }
                //??????????????????????????????
                if (!isClick)
                {
                    return;
                }
                isClick = false;
                uniPayPresenter.queryResultBal(mBillId,getContext(),callback,UniPayPresenter.unipayment);
//                showPaymentSuccPopWindow();
                break;
            case R.id.get_verification:
                countDown(60);
                break;
            case R.id.children_comfirm_btn:
                //?????????????????????

                String verification = et_input_verification.getText().toString();
                if (TextUtils.isEmpty(verification) || verification.length() != 6)
                {
                    EpgToast.showToast(getActivity(), "???????????????????????????");
                    return;
                }

                //????????????????????????
                boolean canpay3 = AvoidRepeatPaymentUtils.getInstance().canPay(mProductInfo.getID());
                if (!canpay3){
                    EpgToast.showToast(getActivity(), "??????????????????");
                    return;
                }

                if (!isClick)
                {
                    return;
                }
                isClick = false;
                uniPayPresenter.queryResultBal(mBillId,getContext(),callback,UniPayPresenter.unipayment_child);

                break;
        }
    }

    public void countDown(int time) {
        sms_count_time = time;
        et_input_verification.setFocusable(true);
        et_input_verification.requestFocus();
        get_verification_btn.setFocusable(false);
        get_verification_btn.setText(String.format("????????????(%1$s)", String.valueOf(time)));
        uniPayPresenter.sendVerifiedCode(mBillId,getActivity(),UniPayPresenter.SCENE_TYPE_UNI,sendVerifiedCodeCallBack);
        mHandler.sendEmptyMessageDelayed(SMS_LOOP_FLAG, 1000);
    }

    public void uni_pay_comfirm_click()
    {
        showComfirmPopwindow(COMMON_PAY);
    }

    private void paymentWithverifiedCode(){
        //orderproduct??????
        OrderProductRequest request = new OrderProductRequest();
        request.setTransactionId(AepAuthUtil.getNonce());
        UserInfo mUserInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        if (null != mUserInfo)
        {
            User mUser = new User();
            mUser.setId(mUserInfo.getUserId());
            request.setUserID(mUser);
        }
        SubProdInfo subProdInfo = new SubProdInfo();
        subProdInfo.setProductId(mProductInfo.getID());
        if (!TextUtils.isEmpty(mProductInfo.getProductType()))
        {
            if (mProductInfo.getProductType().equals("1"))
            {
                //????????????,??????C40????????????
                PriceObjectForVSS pb = new PriceObjectForVSS();
                if (null != mVODDetail)
                {
                    pb.setID(mVODDetail.getCode());
                    pb.setType("200");

                    Map<String,String> temMap = new HashMap<>();
                    temMap.put("trafficeAttractionContentId",mVODDetail.getCode());
                    subProdInfo.setExtensionInfo(temMap);
                }
                subProdInfo.setObject(pb);
                //?????????????????????
                subProdInfo.setRenewFlag(Subscribe.IsAutoExtend.NO_RENEW);
                subProdInfo.setType(SubProdInfo.SubProdInfoType.ONCE_PRODUCT);
            }
            else
            {
                //????????????????????????
                subProdInfo.setRenewFlag(mPayPresenter.isAutoExtend(mProductInfo));
                subProdInfo.setType(SubProdInfo.SubProdInfoType.PERIOD_PRODUCT);
            }
            if (null != unsubProdInfos && unsubProdInfos.size() > 0)
            {
                request.setUnSubInfo(unsubProdInfos);
            }
        }
        request.setSubInofo(subProdInfo);

        PayInfo payInfo = new PayInfo();
        payInfo.setPayType("1");
        payInfo.setCurreny("CNY");
        payInfo.setMobileNum(mBillId);
        payInfo.setVerifiedCode(et_input_verification.getText().toString());

        List<PayChannel> payChannels = new ArrayList<PayChannel>();
        PayChannel payChannel = new PayChannel();
        //903????????????
        payChannel.setChannelType("903");
        payChannel.setCurrency("CNY");
        payChannels.add(payChannel);
        payInfo.setPayChannels(payChannels);

        request.setPayInfo(payInfo);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        request.setEffectTime(sdf.format(new Date(NtpTimeService.queryNtpTime())));

        //??????code????????????
        LogDescExtInfo IdInfo = null;
        if (null != mVODDetail){
            IdInfo = new LogDescExtInfo();
            IdInfo.setKey("contentID");
            IdInfo.setValue(mVODDetail.getCode());
        }

        //??????????????????
        LogDescExtInfo fromInfo = new LogDescExtInfo();
        fromInfo.setKey("fromPage");
        if (!TextUtils.isEmpty(UBDPurchase.getSrcch())){
            fromInfo.setValue("4");
        }else if (isOrderCenter){
            fromInfo.setValue("3");
        }else if (isTrySeeSubscribe){
            fromInfo.setValue("2");
        }else{
            fromInfo.setValue("1");
        }

        List<LogDescExtInfo> list = new ArrayList<>();

        list.add(fromInfo);
        if (null != IdInfo){
            list.add(IdInfo);
        }

        String listStr = JsonParse.object2String(list);

        Map<String,String> map = new HashMap<>();
        //????????????
        if (null != marketProduct && null != marketProduct.getMarketing())
        {
            List<Marketing> marketlist = new ArrayList<>();
            Marketing marketing = marketProduct.getMarketing();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            marketing.setStartTime(str);
            marketing.setEndTime(null);
            marketlist.add(marketing);
            String marketingStr = JsonParse.object2String(marketlist);
            if (null != marketingStr){
                map.put("promListBoss",marketingStr);
            }
        }

        if (null != listStr){
            map.put("logDescExtInfo",listStr);
        }

        request.setExtentionInfo(map);

        mPayPresenter.paymentByVSS(request, getActivity());
    }

    private void paymentWithOutverifiedCode(){
        //orderproduct??????
        OrderProductRequest request = new OrderProductRequest();
        request.setTransactionId(AepAuthUtil.getNonce());
        UserInfo mUserInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        if (null != mUserInfo)
        {
            User mUser = new User();
            mUser.setId(mUserInfo.getUserId());
            request.setUserID(mUser);
        }
        SubProdInfo subProdInfo = new SubProdInfo();
        subProdInfo.setProductId(mProductInfo.getID());
        if (!TextUtils.isEmpty(mProductInfo.getProductType()))
        {
            if (mProductInfo.getProductType().equals("1"))
            {
                //????????????,??????C40????????????
                PriceObjectForVSS pb = new PriceObjectForVSS();
                if (null != mVODDetail)
                {
                    pb.setID(mVODDetail.getCode());
                    pb.setType("200");
                }
                subProdInfo.setObject(pb);
                //?????????????????????
                subProdInfo.setRenewFlag(Subscribe.IsAutoExtend.NO_RENEW);
                subProdInfo.setType(SubProdInfo.SubProdInfoType.ONCE_PRODUCT);
            }
            else
            {
                //????????????????????????
                subProdInfo.setRenewFlag(mPayPresenter.isAutoExtend(mProductInfo));
                subProdInfo.setType(SubProdInfo.SubProdInfoType.PERIOD_PRODUCT);
            }
            if (null != unsubProdInfos && unsubProdInfos.size() > 0)
            {
                request.setUnSubInfo(unsubProdInfos);
            }
        }

        if (null != mVODDetail){
            Map<String,String> temMap = new HashMap<>();
            temMap.put("trafficeAttractionContentId",mVODDetail.getCode());
            subProdInfo.setExtensionInfo(temMap);
        }

        request.setSubInofo(subProdInfo);

        PayInfo payInfo = new PayInfo();
        payInfo.setPayType("1");
        payInfo.setCurreny("CNY");

        List<PayChannel> payChannels = new ArrayList<PayChannel>();
        PayChannel payChannel = new PayChannel();

        payChannel.setCurrency("CNY");
        payChannel.setChannelType("903");
        payChannels.add(payChannel);
        payInfo.setPayChannels(payChannels);

        request.setPayInfo(payInfo);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        request.setEffectTime(sdf.format(new Date(NtpTimeService.queryNtpTime())));

        //??????code????????????
        LogDescExtInfo IdInfo = null;
        if (null != mVODDetail){
            IdInfo = new LogDescExtInfo();
            IdInfo.setKey("contentID");
            IdInfo.setValue(mVODDetail.getCode());
        }

        //??????????????????
        LogDescExtInfo fromInfo = new LogDescExtInfo();
        fromInfo.setKey("fromPage");
        if (!TextUtils.isEmpty(UBDPurchase.getSrcch())){
            fromInfo.setValue("4");
        }else if (isOrderCenter){
            fromInfo.setValue("3");
        }else if (isTrySeeSubscribe){
            fromInfo.setValue("2");
        }else{
            fromInfo.setValue("1");
        }

        List<LogDescExtInfo> list = new ArrayList<>();

        list.add(fromInfo);
        if (null != IdInfo){
            list.add(IdInfo);
        }

        String listStr = JsonParse.object2String(list);

        Map<String,String> map = new HashMap<>();
        if (null != listStr){
            map.put("logDescExtInfo",listStr);
        }


        //????????????
        if (null != marketProduct && null != marketProduct.getMarketing())
        {
            List<Marketing> marketlist = new ArrayList<>();
            Marketing marketing = marketProduct.getMarketing();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            marketing.setStartTime(str);
            marketing.setEndTime(null);
            marketlist.add(marketing);
            String marketingStr = JsonParse.object2String(marketlist);
            if (null != marketingStr){
                map.put("promListBoss",marketingStr);
            }
        }

        request.setExtentionInfo(map);
        mPayPresenter.paymentByVSS(request, getActivity());
    }


    public void child_pay_comfirm_click()
    {

        showComfirmPopwindow(SECOND_COMFIRM_PAY);

    }


    @Override
    public void onResultProductInfo(Product product)
    {

    }

    @Override
    public void onResultKeyBoardValue(List<String> keyboardList)
    {

    }

    @Override
    public void onCountDownStart()
    {

    }

    @Override
    public void onCountDownFinish()
    {

    }

    @Override
    public void onCountDownProgressUpdate(Long progress)
    {

    }

    @Override
    public void onPaymentSucc(SubscribeProductResponse response)
    {
        isClick = true;
        if(null!=getActivity()&&!getActivity().isFinishing()) {
            paymentSuccess();
        }
    }

    @Override
    public void onPaymentSucc(OrderProductResponse response) {
        isClick = true;
        if(null!=getActivity()&&!getActivity().isFinishing()) {
            paymentSuccess();
        }
    }

    @Override
    public void onPaymentFailed() {
        isClick = true;
        //??????????????????????????????
        UBDPurchase.record(mProductInfo,mVODDetail==null?"":mVODDetail.getID(), PurchaseData.FAIL);
    }

    @Override
    public void updateUserRegInfoSucess()
    {

    }

    @Override
    public void updateUserRegInfoFail() {
        EpgToast.showToast(getActivity(), "???????????????");
        isClick = true;
    }


    public void paymentSuccess() {
        //??????????????????
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(BroadCastConstant.Heartbit.COM_HUAWEI_OTT_START_HEARTBIT));
        //??????????????????????????????
        AvoidRepeatPaymentUtils.getInstance().recordPaymentTime(mProductInfo.getID());
        //????????????????????????
        showPaymentSuccPopWindow();

        //????????????????????????
        String giftSwitch = SessionService.getInstance().getSession().getTerminalConfigurationGiftProductIdSwitch();
        boolean showH5 = false;
        if (null != giftSwitch && giftSwitch.equals("1") && giftProductIds.contains(mProductInfo.getID())){
            showH5 = true;
            if (null != offerInfos && offerInfos.size()>0){
                for (int i = 0; i < offerInfos.size(); i++) {
                    OfferInfo info = offerInfos.get(i);
                    if (info.getOfferID().equals(prepaymentOfferid) || info.getOfferID().equals(giftedOffid)){
                        showH5 = false;
                        break;
                    }
                }
            }
        }

        if (showH5 && null != getActivity()){
            Intent intent = new Intent();
            intent.setClass(getActivity(), WebActivity.class);
            String url = giftUrl + "?productID="+mProductInfo.getID();
            intent.putExtra("url", url);
            getActivity().startActivityForResult(intent,11100);
            return;
        }

        //??????????????????
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Product mProduct=new Product();
                mProduct.setID(mProductInfo==null?"":mProductInfo.getID());
                //????????????????????????
                UBDPurchase.record(mProduct,mVODDetail==null?"":mVODDetail.getID(), PurchaseData.SUCCESS);

                SuperLog.info2SD(TAG, "send PlayUrlEvent >>> CHANNELMEDIA_ID:" + (null !=
                        mCHANNELMEDIA_ID) + "|isVODSubscribe:" + isVODSubscribe + "|isTVODSubscribe:"
                        + isTVODSubscribe + "|isTrySeeSubscribe:" + isTrySeeSubscribe);
                if (null != mCHANNELMEDIA_ID) {
                    CurrentChannelPlaybillInfo playbillInfo = new CurrentChannelPlaybillInfo();
                    playbillInfo.setChannelId(mCHANNELMEDIA_ID[0]);
                    playbillInfo.setChannelMediaId(mCHANNELMEDIA_ID[1]);
                    //????????????
                    Log.i(TAG, "run: PlayUrlEvent sendEvent");
                    EventBus.getDefault().post(new PlayUrlEvent(playbillInfo));
                } else if (isVODSubscribe) {
                    //VOD??????
                    if (isOffScreen) {
                        playOffscreen();
                    } else {
                        EventBus.getDefault().post(new PlayUrlEvent(true, false,mProductInfo==null?"":mProductInfo.getID()));
                    }
                } else if (isTVODSubscribe) {
                    //????????????
                    EventBus.getDefault().post(new PlayUrlEvent(false, true,mProductInfo==null?"":mProductInfo.getID()));
                } else if (isTrySeeSubscribe) {
                    EventBus.getDefault().post(new PlayUrlEvent(false, false, true,mProductInfo==null?"":mProductInfo.getID()));
                } else if (isOrderCenter) {
                    EventBus.getDefault().post(new PlayUrlEvent(false, false, false, true,mProductInfo==null?"":mProductInfo.getID()));
                } else {
                    EventBus.getDefault().post(new PlayUrlEvent(false, false, true,mProductInfo==null?"":mProductInfo.getID()));
                }
            }
        }, 2000);
    }

    /**
     * ??????URL????????????
     *
     * @param event event
     */
    @org.greenrobot.eventbus.Subscribe
    public void onEvent(FinishPlayUrlEvent event) {
        if (null != mPaymentPopwindow && mPaymentPopwindow.isShowing()) {
            mPaymentPopwindow.dismiss();
        }
        //setResult(RESULT_OK);
        //finish();
    }

    /**
     * ???????????????????????????
     */
    private void showPaymentSuccPopWindow()
    {
        View successView = LayoutInflater.from(getActivity()).inflate(R.layout
                .window_pip_payment_success, null);
        TextView tvTips = (TextView) successView.findViewById(R.id.tv_payment_tips);
        //??????????????????

        if(null!=mProductInfo)
        {
            StringBuffer sb=new StringBuffer();
            double price = Double.parseDouble(mProductInfo.getPrice()) / 100;
            if("1".equals(mProductInfo.getProductType())){
                sb.append("??????????????????");
                sb.append("<strong><font color=\"#FFFFFF\">");
                sb.append(" ???");
                sb.append(mVODDetail.getName());
                sb.append("??????");
                sb.append("</font></strong>");
                sb.append("?????????????????????");
                sb.append("<strong><font color=\"#EC821B\">");
                sb.append(mBillId);
                sb.append("</font></strong>");
                sb.append("?????????");
                tvTips.setText(Html.fromHtml(sb.toString()));
            }else{
                boolean showUnscribeTip = false;
                if (null != marketProduct && null != unSubscribeTips){
                    List<UnsubscribeTip> list = unSubscribeTips.get(mProductInfo.getID());
                    if (null != list && list.size() > 0){
                        for (int i = 0; i < list.size(); i++) {
                            UnsubscribeTip tip = list.get(i);
                            Marketing marketing = marketProduct.getMarketing();
                            if (null != tip && null != marketing && null != tip.getOfferid() && null != marketing.getId()){
                                if (tip.getOfferid().equals(marketing.getId())){
                                    showUnscribeTip = true;
                                }
                            }
                        }
                    }
                }
                if (showUnscribeTip){
                    sb.append("??????????????????");
                    sb.append("<strong><font color=\"#FFFFFF\">");
                    sb.append(" ???");
                    sb.append(mProductInfo.getName());
                    sb.append("????????????");
                    sb.append("</font></strong>");
                    sb.append("?????????????????????");
                    sb.append("<strong><font color=\"#EC821B\">");
                    sb.append(mBillId);
                    sb.append("</font></strong>");
                    sb.append("?????????");
                    tvTips.setText(Html.fromHtml(sb.toString()));
                }else{
                    sb.append("??????????????????");
                    sb.append("<strong><font color=\"#FFFFFF\">");
                    sb.append(" ???");
                    sb.append(mProductInfo.getName());
                    sb.append("????????????");
                    sb.append("</font></strong>");
                    sb.append("??????????????????????????????????????????????????????-??????????????????????????????");
                    tvTips.setText(Html.fromHtml(sb.toString()));
                }


            }
        }

//        tvTips.setText(String.format(getString(R.string.payment_success_tips), mBillId));
        mPaymentPopwindow = new PopupWindow(successView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPaymentPopwindow.showAtLocation(root_view, Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 1.0f;
        getActivity().getWindow().setAttributes(lp);
        mPaymentPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss()
            {
                getActivity().setResult(RESULT_OK);
                getActivity().finish();
            }
        });
    }

    private PopupWindow mComfirmPopwindow;

    private static final int COMMON_PAY = 1101;

    private static final int SECOND_COMFIRM_PAY = 1102;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private void showComfirmPopwindow(int paytype) {
        this.payType=paytype;
        View comfirmView = LayoutInflater.from(getActivity()).inflate(R.layout
                .window_pip_payment_comfirm, null);
        TextView tv = (TextView) comfirmView.findViewById(R.id.tv_payment_tips);
        Button cancelbtn = (Button) comfirmView.findViewById(R.id.cancel_btn);
        Button comfirmbtn = (Button) comfirmView.findViewById(R.id.comfirm_btn);
        if (null != mProductInfo)
        {
            StringBuffer sb = new StringBuffer();
            sb.append(getString(R.string.is_comfirm_subscribe));
            double price = Double.parseDouble(mProductInfo.getPrice()) / 100;
            if (null != marketProduct){
                price = Double.parseDouble(marketProduct.getPrice()) / 100;
            }
            if ("1".equals(mProductInfo.getProductType()))
            {
                sb.append("<strong><font color=\"#FFFFFF\">");
                sb.append("???");
                sb.append(mVODDetail.getName());
                sb.append("???");
                sb.append("</font></strong>");
                sb.append("???");
                sb.append("??????");
                sb.append("<strong><font color=\"#FFFFFF\">");
                sb.append(decimalFormat.format(price));
                sb.append("</font></strong>");
                sb.append("???/???");

                if (null != marketProduct)
                {
                    sb.append(",??????????????????????????????????????????????????????");
                }
                else
                {
                    sb.append("???");
                }
                sb.append("?????????????????????");
                sb.append("<strong><font color=\"#EC821B\">");
                sb.append(mBillId);
                sb.append("</font></strong>");
                sb.append("?????????");
            }
            else
            {
                sb.append("<strong><font color=\"#FFFFFF\">");
                sb.append("???");
                sb.append(mProductInfo.getName());
                sb.append("???");
                sb.append("</font></strong>");
                if (null != marketProduct)
                {
                    sb.append("???????????????");
                }
                else
                {
                    sb.append("???????????????");
                }
                sb.append("<strong><font color=\"#FFFFFF\">");
                sb.append(decimalFormat.format(price));
                sb.append("</font></strong>");
                sb.append("???/");
                sb.append(StringUtils.analyticValidity(mProductInfo));
                if (null != marketProduct)
                {
//                    sb.append(",");
//                    sb.append("??????????????????<br>");
//                    sb.append("<strong><font color=\"#FFFFFF\">");
//                    sb.append("???");
//                    sb.append(marketProduct.getName());
//                    sb.append("???");
//                    sb.append("</font></strong>");
//                    sb.append("???");
//                    sb.append(",??????????????????<br>????????????????????????????????????");
                    sb.append(",??????????????????????????????????????????????????????");
                }
                else
                {
                    sb.append("???");
                }
                sb.append("?????????????????????");
                sb.append("<strong><font color=\"#EC821B\">");
                sb.append(mBillId);
                sb.append("</font></strong>");
                sb.append("?????????");
            }
            tv.setText(Html.fromHtml(sb.toString()));
        }
        mComfirmPopwindow = new PopupWindow(comfirmView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mComfirmPopwindow.setFocusable(true);
        //        mComfirmPopwindow.setTouchable(false);
        //        mComfirmPopwindow.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mComfirmPopwindow.setBackgroundDrawable(dw);
        cancelbtn.setOnClickListener(view ->
        {
            isClick = true;
            mComfirmPopwindow.dismiss();
        });
        mComfirmPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                isClick = true;
            }
        });
        comfirmbtn.setOnClickListener(view ->
        {
            comfirm_count_time=6;
            mHandler.removeMessages(COMFIRM_LOOP_FLAG);
            mHandler.sendEmptyMessage(COMFIRM_LOOP_FLAG);
            isCanPressBack=false;
            checkOrderBeforeSubcribe();
            mComfirmPopwindow.dismiss();

        });
        if (!mComfirmPopwindow.isShowing())
        {
            mComfirmPopwindow.showAtLocation(root_view, Gravity.CENTER, 0, 0);
            String secondConfirmFocus = SessionService.getInstance().getSession().getTerminalConfigurationOrderSecondConfirmFocus();
            comfirmbtn.setFocusable(true);
            cancelbtn.setFocusable(true);
            if (null != secondConfirmFocus && !TextUtils.isEmpty(secondConfirmFocus)){
                if ("1".equals(secondConfirmFocus)){
                    comfirmbtn.requestFocus();
                }else{
                    cancelbtn.requestFocus();
                }
            }else{
                cancelbtn.requestFocus();
            }

        }
    }


    public void playOffscreen()
    {
        DetailPresenter mDetailPresenter = new DetailPresenter((RxAppCompatActivity) getActivity());
        mDetailPresenter.setDetailDataView(this);
        mDetailPresenter.setVODDetail(mVODDetail);
        if (null != mXmppMessage && null != mVODDetail)
        {
            String type = mXmppMessage.getMediaType();
            String vodId = null;
            String childVodId = null;
            if (!TextUtils.isEmpty(type) && ("2".equals(type)))
            { // VOD

                String mediaCode = mXmppMessage.getMediaCode();
                String[] str = mediaCode.split(",");
                if (str != null)
                {
                    vodId = str[0];
                    if (str.length > 1)
                    {
                        childVodId = mediaCode.split(",")[1];
                    }
                }

            }
            if (childVodId != null && childVodId.indexOf("index") > -1 && vodId != null)
            {
                String index = childVodId.substring(5);
                List<VODMediaFile> clipfiles = mVODDetail.getClipfiles();
                VODMediaFile vodMediaFile = clipfiles.get(Integer.parseInt(index));
                mDetailPresenter.playClipfile(vodMediaFile, vodId, vodMediaFile.getElapseTime());
            }
            else
            {
                if (mVODDetail.getVODType().equals("0"))
                {
                    List<VODMediaFile> vodMediaFiles = mVODDetail.getMediaFiles();
                    if (vodMediaFiles != null && vodMediaFiles.size() != 0)
                    {
                        VODMediaFile vodMediaFile = vodMediaFiles.get(0);
                        mDetailPresenter.playClipfile(vodMediaFile, vodId, vodMediaFile
                                .getElapseTime());
                    }
                }
                else
                {//??????
                    List<Episode> episodes = mVODDetail.getEpisodes();
                    for (Episode episode : episodes)
                    {
                        VOD vod = episode.getVOD();
                        if (vod.getID().equals(childVodId))
                        {
                            List<VODMediaFile> vodMediaFiles = vod.getMediaFiles();
                            if (vodMediaFiles != null && vodMediaFiles.size() != 0)
                            {
                                VODMediaFile vodMediaFile = vodMediaFiles.get(0);
                                mDetailPresenter.playClipfile(vodMediaFile, vod.getID(),
                                        vodMediaFile.getElapseTime());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void showDetail(VODDetail vodDetail, String actionId, List<RecmContents> recmContentsList) {
        //No need to implement this interface
    }

    @Override
    public void showCollection(boolean isCollection) {
        //No need to implement this interface
    }

    @Override
    public void setNewScore(List<Float> newScore) {
        //No need to implement this interface
    }

    @Override
    public void showContentNotExit() {
        //No need to implement this interface
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != uniPayPresenter && null != uniPayPresenter.getBaseView()){
            uniPayPresenter.detachView();
        }
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacksAndMessages(null);
        mPresenter.detachView();
    }

    @Override
    public void executePay() {
        if (children_pay_lay.getVisibility() == View.VISIBLE) {
            paymentWithverifiedCode();
        } else {
            paymentWithOutverifiedCode();
        }
    }

    @Override
    public VODDetail initVodDetail()
    {
        return mVODDetail;
    }

    @Override
    public Product initProduct()
    {
        return mProductInfo;
    }

    @Override
    public int initCheckedPayment()
    {
        return PAYMENT_UNI_PAY;
    }

    @Override
    public boolean istrySee() {
        return isTrySeeSubscribe;
    }

    @Override
    public boolean isOrdercenter() {
        return isOrderCenter;
    }

    @Override
    public Product getMarketProduct() {
        return marketProduct;
    }

    public int getSMS_LOOP_FLAG() {
        return SMS_LOOP_FLAG;
    }

    public int getCOMFIRM_LOOP_FLAG() {
        return COMFIRM_LOOP_FLAG;
    }


    /************************************ProductOrderContract.View******************************************/
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
    public void querySubscriberSucess(String orderingSwitch) {

    }

    @Override
    public void querySubscriberfail() {

    }

    @Override
    public void queryMultiUserInfoSuccess(QueryMultiUserInfoResponse response, Intent intent) {

    }

    @Override
    public void queryMultiUserInfoFail(Intent intent) {

    }
}
