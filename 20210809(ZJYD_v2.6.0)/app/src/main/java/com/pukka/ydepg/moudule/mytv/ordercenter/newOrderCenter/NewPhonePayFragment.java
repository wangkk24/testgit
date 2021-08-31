package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.BroadCastConstant;
import com.pukka.ydepg.common.extview.VerticalScrollTextView;
import com.pukka.ydepg.common.http.bean.node.Marketing;
import com.pukka.ydepg.common.http.bean.node.PayChannel;
import com.pukka.ydepg.common.http.bean.node.PayInfo;
import com.pukka.ydepg.common.http.bean.node.SubProdInfo;
import com.pukka.ydepg.common.http.bean.node.UnsubProdInfo;
import com.pukka.ydepg.common.http.bean.node.User;
import com.pukka.ydepg.common.http.bean.node.XmppMessage;
import com.pukka.ydepg.common.http.bean.request.OrderProductRequest;
import com.pukka.ydepg.common.http.bean.response.OrderProductResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.PaymentInfo;
import com.pukka.ydepg.common.http.v6bean.v6node.PriceObject;
import com.pukka.ydepg.common.http.v6bean.v6node.PriceObjectForVSS;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Profile;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscribe;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6request.SubscribeProductRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.SubscribeProductResponse;
import com.pukka.ydepg.common.http.vss.node.RespParam;
import com.pukka.ydepg.common.report.ubd.extension.PurchaseData;
import com.pukka.ydepg.common.report.ubd.scene.UBDPurchase;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.TextUtil;
import com.pukka.ydepg.common.utils.base64Utils.AepAuthUtil;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.event.FinishPlayUrlEvent;
import com.pukka.ydepg.event.PlayUrlEvent;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.moudule.mytv.ZjYdUniAndPhonePayActivty;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.bean.LogDescExtInfo;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.AvoidRepeatPaymentUtils;
import com.pukka.ydepg.moudule.mytv.presenter.PayPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.UniPayPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.contract.PayContract;
import com.pukka.ydepg.moudule.mytv.presenter.contract.UniPayContract;
import com.pukka.ydepg.moudule.mytv.utils.DoubleArithmeticUtil;
import com.pukka.ydepg.moudule.player.node.CurrentChannelPlaybillInfo;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.view.DetailDataView;
import com.pukka.ydepg.service.NtpTimeService;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewMyPayModeActivity.PAYMENT_PHONE_PAY;

public class NewPhonePayFragment extends NewPaymentFragment implements View.OnClickListener, PayContract.View, DetailDataView, UniPayContract.View
{
    private final String TAG = this.getClass().getName();
    /**
     * 手机号码
     */
    EditText mInputNumber;

    /**
     * 获取验证码
     */
    Button get_verification_btn;

    /**
     * 验证码输入
     */
    EditText et_input_verification;
    /**
     * 确认支付
     */
    Button  comfirm_btn;

    private boolean isClick = true;

    /**
     * 总布局
     */
    @BindView(R.id.root_view)
    LinearLayout root_view;

    //产品包名称
    TextView product_title_textview;

    //产品包价格
    TextView product_price_textview;

    //产品包折扣信息
    TextView product_dicount_tip;

    //产品包价格单位
    TextView product_prcie_mode_textview;

    //产品包原价
    TextView product_orgin_price_textview;

    //产品包描述
    VerticalScrollTextView product_describe_textview;

    //活动详情tip
    TextView product_describe_tip;

    //拓展参数 折扣信息
    private static final String cornreMarketInfo = "CORNRE_MARKER_INFO";

    //拓展参数 产品包原价信息
    private static final String orgPriceKey = "ORIGINAL_PRICE";


    /**
     * 产品信息
     */
    private Product mProductInfo;

    private List<UnsubProdInfo> unsubProdInfos = new ArrayList<>();

    private Product marketProduct;

    /**
     * VOD详情
     */
    private VODDetail mVODDetail;

    /**
     * 是不是试看订购
     */
    private boolean isTrySeeSubscribe = false;

    /**
     * 是不是VOD订购
     */
    private boolean isVODSubscribe = false;

    /**
     * 是从订购中心
     */
    private boolean isOrderCenter=false;

    /**
     * 是不是TVOD订购
     */
    private boolean isTVODSubscribe = false;

    private XmppMessage mXmppMessage;

    private boolean isOffScreen = false;

    /**
     * 频道ID和媒体资源ID
     */
    private String[] mCHANNELMEDIA_ID = null;


    /**
     * 付款成功弹窗
     */
    private PopupWindow mPaymentPopwindow;


    /**
     * 支付presenter
     */
    private PayPresenter mPayPresenter = new PayPresenter();

    private UniPayPresenter uniPayPresenter = new UniPayPresenter();




    /**
     * profile信息
     */
    private Profile mProfile;

    private PopupWindow mNotEnoughWindow;



    /**
     * 倒计时开始时间
     */
    private int sms_count_time = 60;

    private int comfirm_count_time=6;

    private  String mPhoneNumber;

    private final int  SMS_LOOP_FLAG=10000;

    private final int COMFIRM_LOOP_FLAG=11111;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case SMS_LOOP_FLAG:
                    sms_count_time--;
                    if (sms_count_time != 0)
                    {
                        get_verification_btn.setText(String.format("重新发送(%1$s)", String.valueOf(sms_count_time)));
                        sendEmptyMessageDelayed(SMS_LOOP_FLAG, 1000);
                    }
                    else
                    {
                        get_verification_btn.setText("重新发送");
                        get_verification_btn.setFocusable(true);//焦点可以获取
                        removeMessages(SMS_LOOP_FLAG);
                    }
                    break;
                case COMFIRM_LOOP_FLAG:
                    comfirm_count_time--;
                    if(comfirm_count_time!=0&&comfirm_count_time>0){
                        comfirm_btn.setClickable(false);
                        comfirm_btn.setText(String.format("支付中(%1$s)", String.valueOf(comfirm_count_time)));
                        sendEmptyMessageDelayed(COMFIRM_LOOP_FLAG, 1000);
                    }else{
                        isCanPressBack=true;
                        comfirm_btn.setClickable(true);
                        comfirm_btn.setText(R.string.comfirm_pay);
                    }
                    break;


            }
        }
    };

    private UniPayPresenter.QueryResultBalCallback callback = new UniPayPresenter.QueryResultBalCallback() {
        @Override
        public void QueryResultBalSuccess(RespParam param, String comeFrom) {
            //获取当前的话费
            String nowBillStr = "";
            double nowBill = 0;
            if (null != param.getBusinessInfo() && null != param.getBusinessInfo().getBalance()){
                nowBillStr = param.getBusinessInfo().getBalance();
            }else{
                EpgToast.showToast(OTTApplication.getContext(),"查询话费失败");
                return;
            }
            if (nowBillStr.length()>0){
                nowBill = Double.parseDouble(nowBillStr) / 100;
            }else{
                EpgToast.showToast(OTTApplication.getContext(),"查询话费失败");
                return;
            }

            //获取支付之后的最少话费
            String minimumAmountStr = SessionService.getInstance().getSession().getTerminalConfigurationOrderPhoneBillPayMinimumAmount();
            double minimumAmount = 0;
            if (null != minimumAmountStr && TextUtils.isEmpty(minimumAmountStr)){
                if (isNumeric(minimumAmountStr)){
                    minimumAmount = Double.parseDouble(minimumAmountStr)/100;
                }
            }

            //产品包价格
            double price = Double.parseDouble(mProductInfo.getPrice()) / 100;
            if (nowBill < price){
                //手机话费小于产品包价格，展示账户余额不足，请充值之后再订购
                showNotEnoughWindow();
            }else if (DoubleArithmeticUtil.compare(DoubleArithmeticUtil.sub(nowBill,price),minimumAmount) == -1){
                //订购完成之后剩余的手机话费小于配置的最小值，展示账户余额不足，请充值之后再订购
                showNotEnoughWindow();
            }else{
                if (comeFrom.equals(UniPayPresenter.phonepay)){
                    //支付
                    showComfirmPopwindow();
                }
            }

        }

        //判断是否全是数字
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
            EpgToast.showToast(OTTApplication.getContext(),"查询话费失败");
        }
    };

    private UniPayPresenter.SendVerifiedCodeCallBack sendVerifiedCodeCallBack = new UniPayPresenter.SendVerifiedCodeCallBack() {
        @Override
        public void sendVerifiedCodeSuccsee() {

        }

        @Override
        public void sendVerifiedCodeFail(String description) {
            EpgToast.showLongToast(OTTApplication.getContext(),
                    "发送验证码失败");
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view=inflater.inflate(R.layout.fragment_phone_pay_new,container,false);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }



    private void initDate() {

        String marketProductStr = getArguments().getString(ZjYdUniAndPhonePayActivty.MARKETING_PRODUCT);
        if (!TextUtils.isEmpty(marketProductStr)) {
            marketProduct = JsonParse.json2Object(marketProductStr, Product.class);
        }

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

        uniPayPresenter.attachView(this);
        isVODSubscribe = getArguments().getBoolean(ZjYdUniAndPhonePayActivty.ISVOD_SUBSCRIBE, false);
        isTVODSubscribe = getArguments().getBoolean(ZjYdUniAndPhonePayActivty.ISTVOD_SUBSCRIBE, false);
        isOrderCenter=getArguments().getBoolean(ZjYdUniAndPhonePayActivty.ISORDERCENTER,false);
        isTrySeeSubscribe = getArguments().getBoolean(ZjYdUniAndPhonePayActivty.IS_TRY_SEE_SUBSCRIBE, false);
        mCHANNELMEDIA_ID = getArguments().getStringArray(ZjYdUniAndPhonePayActivty.CHANNELID_MEDIAID);
        isOffScreen= getArguments().getBoolean(ZjYdUniAndPhonePayActivty.ISOFFSCREEN_SUBSCRIBE, false);
        if (getArguments().getSerializable(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE) != null) {
            mXmppMessage = (XmppMessage) getArguments().getSerializable(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE);
        }
        mProductInfo = JsonParse.json2Object(getArguments().getString(ZjYdUniAndPhonePayActivty.PRODUCT_INFO), Product.class);
        String vodDetailJson = getArguments().getString(ZjYdUniAndPhonePayActivty.VOD_DETAIL);
        if (!TextUtils.isEmpty(vodDetailJson)) {
            mVODDetail = JsonParse.json2Object(vodDetailJson, VODDetail.class);
        }
        mPayPresenter.attachView(this);
        mPayPresenter.generateProductInfo(getArguments().getString(ZjYdUniAndPhonePayActivty.PRODUCT_INFO));

        initProductInfo();
    }

    private void initView() {
        mInputNumber= (EditText) findViewById(R.id.et_phone_number);
        get_verification_btn= (Button) findViewById(R.id.get_verification);
        et_input_verification= (EditText) findViewById(R.id.et_input_verification);
        comfirm_btn= (Button) findViewById(R.id.comfirm_btn);
        root_view= (LinearLayout) findViewById(R.id.root_view);
        get_verification_btn.setOnClickListener(this);
        comfirm_btn.setOnClickListener(this);
        TextUtil.setEditTextFilter(mInputNumber, 11);
        mInputNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_input_verification.setInputType(InputType.TYPE_CLASS_NUMBER);
        TextUtil.setEditTextFilter(et_input_verification, 6);
        get_verification_btn.setNextFocusDownId(R.id.et_input_verification);

        et_input_verification.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction()) {

                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT ||  keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                        return true;
                    }

                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP){
                        if (get_verification_btn.isFocusable()){
                            get_verification_btn.requestFocus();
                        }else{
                            mInputNumber.requestFocus();
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        comfirm_btn.setNextFocusUpId(R.id.et_input_verification);
        comfirm_btn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction()) {

                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                        return true;
                    }
                }
                return false;
            }
        });
        mInputNumber.setOnKeyListener(new View.OnKeyListener() {
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
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                        if (get_verification_btn.isFocusable()){
                            get_verification_btn.requestFocus();
                        }else{
                            et_input_verification.requestFocus();
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
                if (KeyEvent.ACTION_DOWN == event.getAction()) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
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

        get_verification_btn.setOnFocusChangeListener(listener);
        comfirm_btn.setOnFocusChangeListener(listener);

    }

    private void initProductInfo(){
        //产品包名称
        product_title_textview = (TextView) findViewById(R.id.order_paymode_item_title);
        //产品包价格
        product_price_textview = (TextView) findViewById(R.id.order_paymode_price);
        //产品包折扣信息
        product_dicount_tip = (TextView) findViewById(R.id.order_paymode_item_discount);
        //产品包价格单位
        product_prcie_mode_textview = (TextView) findViewById(R.id.order_paymode_price_mode);
        //产品包原价
        product_orgin_price_textview = (TextView) findViewById(R.id.order_paymode_item_discountprice);
        //产品包描述
        product_describe_textview = (VerticalScrollTextView) findViewById(R.id.order_paymode_event_text);
        //活动详情tip
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

        //产品包名
        String name = mProductInfo.getName();
        product_title_textview.setText(name);
        product_title_textview.setSelected(true);

        //产品包价格
        double price = Double.parseDouble(product.getPrice()) / 100;
        product_price_textview.setText(decimalFormat.format(price));

        //获取折扣信息
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

            //获取原价信息
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
         * 产品类型,0 ：包周期;1 ：按次
         */
        if((!TextUtils.isEmpty(mProductInfo.getProductType()) && mProductInfo.getProductType().equals("1"))){
            product_prcie_mode_textview.setText("元/次");
        }else{
            product_prcie_mode_textview.setText("元/"+  StringUtils.analyticValidity(mProductInfo));
        }

        if (null != marketProduct){
            product_describe_tip.setVisibility(View.VISIBLE);
            //产品包描述
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

    public void  reload(){
        if (null != mHandler && null != et_input_verification) {
            //避免页签切换时计时归零，如果之后出现问题需要还原打开以下代码
//            mInputNumber.setText("");
//            et_input_verification.setText("");
//            mHandler.removeMessages(SMS_LOOP_FLAG);
//            mHandler.removeMessages(COMFIRM_LOOP_FLAG);
//            isCanPressBack=true;
//            comfirm_btn.setText(R.string.comfirm_pay);
//            comfirm_btn.setClickable(true);
//            get_verification_btn.setText("获取验证码");
//            get_verification_btn.setFocusable(true);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_verification:
                if(11!=mInputNumber.getText().toString().length()){
                    EpgToast.showToast(getActivity(),"请输入正确的手机号码");
                    return;
                }
                countDown(60);
                break;
            case R.id.comfirm_btn:
                String verification = et_input_verification.getText().toString();
                String mPhoneNumber= mInputNumber.getText().toString();
                if(TextUtils.isEmpty(mPhoneNumber)||11!=mPhoneNumber.length()){
                    EpgToast.showToast(getActivity(),"请输入正确的手机号码");
                    return;
                }
                if (TextUtils.isEmpty(verification) || verification.length() != 6) {
                    EpgToast.showToast(getActivity(), "请输入正确的验证码");
                    return;
                }

                //判断是否重复订购
                boolean canpay1 = AvoidRepeatPaymentUtils.getInstance().canPay(mProductInfo.getID());
                if (!canpay1){
                    EpgToast.showToast(getActivity(), "正在订购……");
                    return;
                }

                if (!isClick)
                {
                    return;
                }
                isClick = false;
                uniPayPresenter.queryResultBal(mPhoneNumber,getContext(),callback,UniPayPresenter.phonepay);
                break;
        }
    }

    private PopupWindow mComfirmPopwindow;

    private DecimalFormat decimalFormat= new DecimalFormat("0.00");

    private void showComfirmPopwindow(){
        View comfirmView=LayoutInflater.from(getActivity()).inflate(R.layout.window_pip_payment_comfirm,null);
        TextView tv= (TextView) comfirmView.findViewById(R.id.tv_payment_tips);
        Button cancelbtn=(Button) comfirmView.findViewById(R.id.cancel_btn);
        Button comfirmbtn= (Button) comfirmView.findViewById(R.id.comfirm_btn);
        if(null!=mProductInfo)
        {
            StringBuffer sb=new StringBuffer();
            sb.append(getString(R.string.is_comfirm_subscribe));
            double price = Double.parseDouble(mProductInfo.getPrice()) / 100;
            if("1".equals(mProductInfo.getProductType())){
                sb.append("<strong><font color=\"#FFFFFF\">");
                sb.append(" “");
                sb.append(mVODDetail.getName());
                sb.append("” ");
                sb.append("</font></strong>");
                sb.append("，");
                sb.append("资费");
                sb.append("<strong><font color=\"#FFFFFF\">");
                sb.append(decimalFormat.format(price));
                sb.append("</font></strong>");
                sb.append("元/部。");
            }else{
                sb.append("<strong><font color=\"#FFFFFF\">");
                sb.append("“");
                sb.append(mProductInfo.getName());
                sb.append("”");
                sb.append("</font></strong>");
                sb.append("产品，资费");
                sb.append("<strong><font color=\"#FFFFFF\">");
                sb.append(decimalFormat.format(price));
                sb.append("</font></strong>");
                sb.append("元/");
                sb.append(StringUtils.analyticValidity(mProductInfo));
                sb.append("。");

            }
            sb.append("费用将由手机号");
            sb.append("<strong><font color=\"#EC821B\">");
            sb.append(mInputNumber.getText().toString());
            sb.append("</font></strong>");
            sb.append("支付。");
            tv.setText(Html.fromHtml(sb.toString()));
        }
        mComfirmPopwindow=new PopupWindow(comfirmView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        mComfirmPopwindow.setFocusable(true);
//        mComfirmPopwindow.setTouchable(false);
//        mComfirmPopwindow.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mComfirmPopwindow.setBackgroundDrawable(dw);
        cancelbtn.setOnClickListener(view->{
            isClick = true;
            mComfirmPopwindow.dismiss();
//            showPaymentSuccPopWindow();
        });
        mComfirmPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                isClick = true;
            }
        });

        comfirmbtn.setOnClickListener(view->{
            //初始化确认键倒计时
            comfirm_count_time=6;
            mHandler.removeMessages(COMFIRM_LOOP_FLAG);
            mHandler.sendEmptyMessage(COMFIRM_LOOP_FLAG);
            isCanPressBack=false;
            checkOrderBeforeSubcribe();
            mComfirmPopwindow.dismiss();
        });
        if(!mComfirmPopwindow.isShowing()){
            mComfirmPopwindow.showAtLocation(root_view, Gravity.CENTER,0,0);
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


    /**
     * 支付
     */
    private void payment() {
        SubscribeProductRequest request = new SubscribeProductRequest();
        //输入的短信验证码
        request.setCorrelator(et_input_verification.getText().toString());
        //订购验证类型,短信验证码方式订购
        request.setType(SubscribeProductRequest.TYPE.SMS_CODE);
        Subscribe subscribe = new Subscribe();
        //付款账号
//        subscribe.setPaidUserID(mPhoneNumber);
        subscribe.setPaidUserID(mInputNumber.getText().toString());
        subscribe.setProductID(mProductInfo.getID());
        if (!TextUtils.isEmpty(mProductInfo.getProductType())) {
            /**
             * 产品类型,0 ：包周期;1 ：按次
             */
            if (mProductInfo.getProductType().equals("1")) {
                List<PriceObject> priceObjList = new ArrayList<>();
                PriceObject priceObject = mProductInfo.getPriceObject();
                if(null != priceObject&&null!=mVODDetail) {
                    priceObject.setID(mVODDetail.getID());
                    priceObjList.add(priceObject);
                }else {
                    EpgToast.showToast(getActivity(),"参数不合法");
                    return ;
                }
                subscribe.setPriceObjects(priceObjList);
                //按次不支持续订
                subscribe.setIsAutoExtend(Subscribe.IsAutoExtend.NO_RENEW);
            }else{
                //包周期看产品状态
                subscribe.setIsAutoExtend(mPayPresenter.isAutoExtend(mProductInfo));
            }
        }
        //支付信息
        subscribe.setPayment(new PaymentInfo());
        request.setSubscribe(subscribe);
        //支付
        mPayPresenter.payment(request, getActivity());
    }

    //新的订购
    private void paymentByVSS(){
        //orderproduct订购
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
                //按次产品,参考C40接口文档
                PriceObjectForVSS pb = new PriceObjectForVSS();
                if (null != mVODDetail)
                {
                    pb.setID(mVODDetail.getCode());
                    pb.setType("200");
                }
                subProdInfo.setObject(pb);
                //按次不支持续订
                subProdInfo.setRenewFlag(Subscribe.IsAutoExtend.NO_RENEW);
                subProdInfo.setType(SubProdInfo.SubProdInfoType.ONCE_PRODUCT);
            }
            else
            {
                //包周期看产品状态
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
        payInfo.setPaymentUserId(mInputNumber.getText().toString());
        payInfo.setMobileNum(mInputNumber.getText().toString());
        payInfo.setVerifiedCode(et_input_verification.getText().toString());

        List<PayChannel> payChannels = new ArrayList<PayChannel>();
        PayChannel payChannel = new PayChannel();
        //903话单支付
        payChannel.setChannelType("903");
        payChannel.setCurrency("CNY");
        payChannels.add(payChannel);
        payInfo.setPayChannels(payChannels);

        request.setPayInfo(payInfo);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        request.setEffectTime(sdf.format(new Date(NtpTimeService.queryNtpTime())));

        //内容code统计需求
        LogDescExtInfo IdInfo = null;
        if (null != mVODDetail){
            IdInfo = new LogDescExtInfo();
            IdInfo.setKey("contentID");
            IdInfo.setValue(mVODDetail.getCode());
        }

        //来源统计需求
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

        //营销活动
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

    public void countDown(int time) {
        sms_count_time = time;
        et_input_verification.setFocusable(true);
        et_input_verification.requestFocus();
        get_verification_btn.setFocusable(false);
        get_verification_btn.setText(String.format("重新发送(%1$s)", String.valueOf(time)));
        mPhoneNumber=mInputNumber.getText().toString();
        uniPayPresenter.sendVerifiedCode(mInputNumber.getText().toString(),getActivity(),UniPayPresenter.SCENE_TYPE_PHONE,sendVerifiedCodeCallBack);
        mHandler.sendEmptyMessageDelayed(SMS_LOOP_FLAG, 1000);
    }


    @Override
    public Context context() {
        return null;
    }

    @Override
    public void showNoContent() { }

    @Override
    public void showError(String message) { }

    @Override
    public void showDetail(VODDetail vodDetail, String actionId, List<RecmContents> recmContentsList) { }

    @Override
    public void showCollection(boolean isCollection) { }

    @Override
    public void setNewScore(List<Float> newScore) { }

    @Override
    public void showContentNotExit() { }

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
        return PAYMENT_PHONE_PAY;
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

    @Override
    public void onResultProductInfo(Product product) {
        List<Profile> profileList = SessionService.getInstance().getSession().getProfileList();
        if (profileList != null && profileList.size() != 0) {
            mProfile = profileList.get(0);
        }
    }

    @Override
    public void onResultKeyBoardValue(List<String> keyboardList) { }

    @Override
    public void onCountDownStart() { }

    @Override
    public void onCountDownFinish() { }

    @Override
    public void onCountDownProgressUpdate(Long progress) { }

    @Override
    public void onPaymentSucc(SubscribeProductResponse response) {
        isClick = true;
        if(null==getActivity()||getActivity().isFinishing()){
            return;
        }
        //立刻发起心跳
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(BroadCastConstant.Heartbit.COM_HUAWEI_OTT_START_HEARTBIT));
        //支付成功弹框提示
        showPaymentSuccPopWindow();
        //发送鉴权消息
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Product mProduct=new Product();
                mProduct.setID(mProductInfo==null?"":mProductInfo.getID());
                //记录下产品的订购时间
                AvoidRepeatPaymentUtils.getInstance().recordPaymentTime(mProductInfo.getID());
                //订购完成后，上报
                UBDPurchase.record(mProduct,mVODDetail==null?"":mVODDetail.getID(), PurchaseData.SUCCESS);

                SuperLog.info2SD(TAG, "send PlayUrlEvent >>> CHANNELMEDIA_ID:" + (null != mCHANNELMEDIA_ID) + "|isVODSubscribe:" + isVODSubscribe + "|isTVODSubscribe:" + isTVODSubscribe + "|isTrySeeSubscribe:" + isTrySeeSubscribe);
                if (null != mCHANNELMEDIA_ID) {
                    CurrentChannelPlaybillInfo playbillInfo = new CurrentChannelPlaybillInfo();
                    playbillInfo.setChannelId(mCHANNELMEDIA_ID[0]);
                    playbillInfo.setChannelMediaId(mCHANNELMEDIA_ID[1]);
                    //直播鉴权
                    EventBus.getDefault().post(new PlayUrlEvent(playbillInfo));
                } else if (isVODSubscribe) {
                    //VOD鉴权
                    if (isOffScreen) {
                        playOffscreen();
                    } else {
                        EventBus.getDefault().post(new PlayUrlEvent(true, false,mProductInfo==null?"":mProductInfo.getID()));
                    }
                } else if (isTVODSubscribe) {
                    //回看鉴权
                    EventBus.getDefault().post(new PlayUrlEvent(false, true,mProductInfo==null?"":mProductInfo.getID()));
                } else if (isTrySeeSubscribe) {
//                    if (isOffScreen) {
//                        playOffscreen();
//                    } else {
                    EventBus.getDefault().post(new PlayUrlEvent(false, false, true,mProductInfo==null?"":mProductInfo.getID()));
//                    }
                }else if(isOrderCenter){
                    EventBus.getDefault().post(new PlayUrlEvent(false,false,false,true,mProductInfo==null?"":mProductInfo.getID()));
                }else{
                    EventBus.getDefault().post(new PlayUrlEvent(false, false, true,mProductInfo==null?"":mProductInfo.getID()));
                }
            }
        }, 2000);
    }

    @Override
    public void onPaymentSucc(OrderProductResponse response)
    {
        isClick = true;
        if(null==getActivity()||getActivity().isFinishing()){
            return;
        }
        //立刻发起心跳
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(BroadCastConstant.Heartbit.COM_HUAWEI_OTT_START_HEARTBIT));
        //支付成功弹框提示
        showPaymentSuccPopWindow();
        //发送鉴权消息
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Product mProduct=new Product();
                mProduct.setID(mProductInfo==null?"":mProductInfo.getID());
                //记录下产品的订购时间
                AvoidRepeatPaymentUtils.getInstance().recordPaymentTime(mProductInfo.getID());
                //订购完成后，上报
                UBDPurchase.record(mProduct,mVODDetail==null?"":mVODDetail.getID(), PurchaseData.SUCCESS);

                SuperLog.info2SD(TAG, "send PlayUrlEvent >>> CHANNELMEDIA_ID:" + (null != mCHANNELMEDIA_ID) + "|isVODSubscribe:" + isVODSubscribe + "|isTVODSubscribe:" + isTVODSubscribe + "|isTrySeeSubscribe:" + isTrySeeSubscribe);
                if (null != mCHANNELMEDIA_ID) {
                    CurrentChannelPlaybillInfo playbillInfo = new CurrentChannelPlaybillInfo();
                    playbillInfo.setChannelId(mCHANNELMEDIA_ID[0]);
                    playbillInfo.setChannelMediaId(mCHANNELMEDIA_ID[1]);
                    //直播鉴权
                    EventBus.getDefault().post(new PlayUrlEvent(playbillInfo));
                } else if (isVODSubscribe) {
                    //VOD鉴权
                    if (isOffScreen) {
                        playOffscreen();
                    } else {
                        EventBus.getDefault().post(new PlayUrlEvent(true, false,mProductInfo==null?"":mProductInfo.getID()));
                    }
                } else if (isTVODSubscribe) {
                    //回看鉴权
                    EventBus.getDefault().post(new PlayUrlEvent(false, true,mProductInfo==null?"":mProductInfo.getID()));
                } else if (isTrySeeSubscribe) {
                    //                    if (isOffScreen) {
                    //                        playOffscreen();
                    //                    } else {
                    EventBus.getDefault().post(new PlayUrlEvent(false, false, true,mProductInfo==null?"":mProductInfo.getID()));
                    //                    }
                }else if(isOrderCenter){
                    EventBus.getDefault().post(new PlayUrlEvent(false,false,false,true,mProductInfo==null?"":mProductInfo.getID()));
                }else{
                    EventBus.getDefault().post(new PlayUrlEvent(false, false, true,mProductInfo==null?"":mProductInfo.getID()));
                }
            }
        }, 2000);
    }

    /**
     * 展示付款成功提示窗
     */
    private void showPaymentSuccPopWindow() {
        View successView = LayoutInflater.from(getActivity()).inflate(R.layout.window_pip_payment_success, null);
        TextView tvTips = (TextView) successView.findViewById(R.id.tv_payment_tips);
        //显示户主信息
        if(null!=mProductInfo)
        {
            StringBuffer sb=new StringBuffer();
            sb.append("您已成功订购");
            double price = Double.parseDouble(mProductInfo.getPrice()) / 100;
            if("1".equals(mProductInfo.getProductType())){
                sb.append("<strong><font color=\"#FFFFFF\">");
                sb.append(" “");
                sb.append(mVODDetail.getName());
                sb.append("”，");
                sb.append("</font></strong>");
            }else{
                sb.append("<strong><font color=\"#FFFFFF\">");
                sb.append("“");
                sb.append(mProductInfo.getName());
                sb.append("”产品，");
                sb.append("</font></strong>");
            }
            sb.append("费用将由手机号");
            sb.append("<strong><font color=\"#EC821B\">");
            sb.append(mInputNumber.getText().toString());
            sb.append("</font></strong>");
            sb.append("支付。");
            tvTips.setText(Html.fromHtml(sb.toString()));
//            tv.setText(Html.fromHtml(sb.toString()));
        }


//        tvTips.setText(R.string.pay_success);
        mPaymentPopwindow = new PopupWindow(successView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPaymentPopwindow.showAtLocation(root_view, Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams lp =  getActivity().getWindow().getAttributes();
        lp.alpha = 1.0f;
        getActivity().getWindow().setAttributes(lp);
        mPaymentPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                getActivity().setResult(RESULT_OK);
                getActivity().finish();
            }
        });
    }
    @Override
    public void onPaymentFailed() {
        isClick = true;
        //记录下产品的订购时间
        UBDPurchase.record(mProductInfo,mVODDetail==null?"":mVODDetail.getID(), PurchaseData.FAIL);
    }

    @Override
    public void updateUserRegInfoSucess(){ }

    @Override
    public void updateUserRegInfoFail() { }

    /**
     * 通知URL鉴权完成
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


    public void playOffscreen() {
        DetailPresenter mDetailPresenter = new DetailPresenter((RxAppCompatActivity) getActivity());
        mDetailPresenter.setDetailDataView(this);
        mDetailPresenter.setVODDetail(mVODDetail);
        if (null != mXmppMessage && null != mVODDetail) {
            String type = mXmppMessage.getMediaType();
            String vodId = null;
            String childVodId = null;
            if (!TextUtils.isEmpty(type) && ("2".equals(type))) { // VOD
                String mediaCode = mXmppMessage.getMediaCode();
                String[] str = mediaCode.split(",");
                if (str != null) {
                    vodId = str[0];
                    if (str.length > 1) {
                        childVodId = mediaCode.split(",")[1];
                    }
                }

            }
            if (childVodId != null && childVodId.indexOf("index") > -1 && vodId != null) {
                String index = childVodId.substring(5);
                List<VODMediaFile> clipfiles = mVODDetail.getClipfiles();
                VODMediaFile vodMediaFile = clipfiles.get(Integer.parseInt(index));
                mDetailPresenter.playClipfile(vodMediaFile, vodId, vodMediaFile.getElapseTime());
            } else {
                if (mVODDetail.getVODType().equals("0")) {
                    List<VODMediaFile> vodMediaFiles = mVODDetail.getMediaFiles();
                    if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
                        VODMediaFile vodMediaFile = vodMediaFiles.get(0);
                        mDetailPresenter.playClipfile(vodMediaFile, vodId, vodMediaFile.getElapseTime());
                    }
                } else {//剧集
                    List<Episode> episodes = mVODDetail.getEpisodes();
                    for (Episode episode : episodes) {
                        VOD vod = episode.getVOD();
                        if (vod.getID().equals(childVodId)) {
                            List<VODMediaFile> vodMediaFiles = vod.getMediaFiles();
                            if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
                                VODMediaFile vodMediaFile = vodMediaFiles.get(0);
                                mDetailPresenter.playClipfile(vodMediaFile, vod.getID(), vodMediaFile.getElapseTime());
                            }
                        }
                    }
                }
            }


        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uniPayPresenter.detachView();
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void executePay() {
        paymentByVSS();
    }
}
