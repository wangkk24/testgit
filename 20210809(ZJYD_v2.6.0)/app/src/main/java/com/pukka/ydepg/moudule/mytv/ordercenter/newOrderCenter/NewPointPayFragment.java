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

import androidx.annotation.NonNull;
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
import com.pukka.ydepg.common.http.v6bean.v6node.PriceObjectForVSS;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscribe;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;

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
import com.pukka.ydepg.moudule.mytv.presenter.PointPayPresenter;
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

import static android.app.Activity.RESULT_OK;

public class NewPointPayFragment extends NewPaymentFragment implements DetailDataView ,View.OnClickListener{

    /**
     * 手机号码输入
     */
    EditText mInputNumber;

    /**
     * 获取验证码按钮
     */
    Button get_verification_btn;

    /**
     * 验证码输入
     */
    EditText et_input_verification;
    /**
     * 确认支付按钮
     */
    Button  comfirm_btn;

    //根布局
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

    /*产品信息*/
    private Product mProductInfo;
    /*产品营销活动信息*/
    private Product marketProduct;
    /*VOD详情信息*/
    private VODDetail mVODDetail;
    /*是否是VOD片源订购*/
    private boolean isVODSubscribe;
    /*是否回看节目单订购*/
    private boolean isTVODSubscribe;

    private XmppMessage mXmppMessage;

    PointPayPresenter presenter;
    private PayPresenter mPayPresenter = new PayPresenter();

    private List<UnsubProdInfo> unsubProdInfos = new ArrayList<>();

    //防止重复点击
    private boolean isClick = true;

    /**
     * 倒计时开始时间
     */
    private int sms_count_time = 60;

    private int comfirm_count_time=6;

    private  String mPhoneNumber;

    private final int  SMS_LOOP_FLAG=10000;

    private final int COMFIRM_LOOP_FLAG=11111;

    private String totalScoreStr = "";

    private String needScoreStr = "";

    /**
     * 频道ID和媒体资源ID
     */
    private String[] mCHANNELMEDIA_ID = null;

    private boolean isOffScreen = false;

    /**
     * 是不是试看订购
     */
    private boolean isTrySeeSubscribe = false;

    /**
     * 是从订购中心
     */
    private boolean isOrderCenter=false;

    /**
     * 付款成功弹窗
     */
    private PopupWindow mPaymentPopwindow;

    /**
     * 积分不足弹窗
     */
    private PopupWindow mNotEnoughWindow;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    //拓展参数 折扣信息
    private static final String cornreMarketInfo = "CORNRE_MARKER_INFO";

    //拓展参数 产品包原价信息
    private static final String orgPriceKey = "ORIGINAL_PRICE";

    private int count = 0;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_point, container, false);
        EventBus.getDefault().register(this);
        //产品包名称
        product_title_textview = (TextView) view.findViewById(R.id.order_paymode_item_title);
        //产品包价格
        product_price_textview = (TextView) view.findViewById(R.id.order_paymode_price);
        //产品包折扣信息
        product_dicount_tip = (TextView) view.findViewById(R.id.order_paymode_item_discount);
        //产品包价格单位
        product_prcie_mode_textview = (TextView) view.findViewById(R.id.order_paymode_price_mode);
        //产品包原价
        product_orgin_price_textview = (TextView) view.findViewById(R.id.order_paymode_item_discountprice);
        //产品包描述
        product_describe_textview = (VerticalScrollTextView) view.findViewById(R.id.order_paymode_event_text);
        //活动详情tip
        product_describe_tip = (TextView) view.findViewById(R.id.order_paymode_event_tip);

        mInputNumber= (EditText) view.findViewById(R.id.et_phone_number);
        get_verification_btn= (Button) view.findViewById(R.id.get_verification);
        et_input_verification= (EditText) view.findViewById(R.id.et_input_verification);
        comfirm_btn= (Button) view.findViewById(R.id.comfirm_btn);
        root_view= (LinearLayout) view.findViewById(R.id.root_view);

        initView();
        initData();
        return view;
    }

    private void initData(){
        mProductInfo = JsonParse.json2Object(getArguments().getString(ZjYdUniAndPhonePayActivty
                .PRODUCT_INFO), Product.class);
        String marketProductStr = getArguments().getString(ZjYdUniAndPhonePayActivty.MARKETING_PRODUCT);
        if (!TextUtils.isEmpty(marketProductStr)) {
            marketProduct = JsonParse.json2Object(marketProductStr, Product.class);
        }
        mVODDetail = JsonParse.json2Object(getArguments().getString(ZjYdUniAndPhonePayActivty
                .VOD_DETAIL), VODDetail.class);

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

        mCHANNELMEDIA_ID = getArguments().getStringArray(ZjYdUniAndPhonePayActivty.CHANNELID_MEDIAID);

        isOffScreen= getArguments().getBoolean(ZjYdUniAndPhonePayActivty.ISOFFSCREEN_SUBSCRIBE, false);

        isTrySeeSubscribe = getArguments().getBoolean(ZjYdUniAndPhonePayActivty.IS_TRY_SEE_SUBSCRIBE, false);

        isOrderCenter=getArguments().getBoolean(ZjYdUniAndPhonePayActivty.ISORDERCENTER,false);

        isVODSubscribe = getArguments().getBoolean(ZjYdUniAndPhonePayActivty.ISVOD_SUBSCRIBE,
                false);
        isTVODSubscribe = getArguments().getBoolean(ZjYdUniAndPhonePayActivty
                .ISTVOD_SUBSCRIBE, false);
        if (getArguments().getSerializable(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE) != null) {
            mXmppMessage = (XmppMessage) getArguments().getSerializable(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE);
        }
        presenter = new PointPayPresenter();
        presenter.attachView(this);

        initProductInfo();
    }

    private void initView() {
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

                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT ){
                        if (product_describe_textview.getVisibility() == View.VISIBLE){
                            product_describe_textview.requestFocus();
                        }
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

                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
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
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT ){
                        if (product_describe_textview.getVisibility() == View.VISIBLE){
                            product_describe_textview.requestFocus();
                        }
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
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
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

    //对比现有积分和需要积分，判断积分是否足够
    private void compareScore(){
        double totalSocre = Double.parseDouble(totalScoreStr);
        double needSocre  = Double.parseDouble(needScoreStr);

        if (totalSocre >= needSocre){
            //弹出二次确认弹框
            showComfirmPopwindow();
        }else{
            //弹出积分不足弹框
            showNotEnoughWindow();
        }
    }

    //按钮的点击事件
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
                boolean canpay = AvoidRepeatPaymentUtils.getInstance().canPay(mProductInfo.getID());
                if (!canpay){
                    EpgToast.showToast(getActivity(), "正在订购……");
                    return;
                }


                if (!isClick)
                {
                    return;
                }
                isClick = false;
                presenter.queryScore(mPhoneNumber,getActivity(),queryPointsCallBack);
                count = count + 1;
                break;
        }

    }

    public void countDown(int time) {
        sms_count_time = time;
        et_input_verification.setFocusable(true);
        et_input_verification.requestFocus();
        get_verification_btn.setFocusable(false);
        get_verification_btn.setText(String.format("重新发送(%1$s)", String.valueOf(time)));
        mPhoneNumber=mInputNumber.getText().toString();
        //发送短信验证码.....
        presenter.sendVerifiedCode(mPhoneNumber,getActivity(),sendVerifiedCodeCallBack);
        mHandler.sendEmptyMessageDelayed(SMS_LOOP_FLAG, 1000);
    }

    /**
     * 展示二次确认提示窗
     */
    private PopupWindow mComfirmPopwindow;
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
            sb.append("您的手机账号");
            sb.append("<strong><font color=\"#EC821B\">");
            sb.append(mInputNumber.getText().toString());
            sb.append("</font></strong>");
            sb.append("共有");
            sb.append("<strong><font color=\"#EC821B\">");
            sb.append(totalScoreStr);
            sb.append("</font></strong>");
            sb.append("积分，本次订购将消耗");
            sb.append("<strong><font color=\"#EC821B\">");
            sb.append(needScoreStr);
            sb.append("</font></strong>");
            sb.append("积分。");
            tv.setText(Html.fromHtml(sb.toString()));
        }
        mComfirmPopwindow=new PopupWindow(comfirmView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        mComfirmPopwindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mComfirmPopwindow.setBackgroundDrawable(dw);
        cancelbtn.setOnClickListener(view->{
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

        comfirmbtn.setOnClickListener(view->{
            //初始化确认键倒计时
            comfirm_count_time=6;
            mHandler.removeMessages(COMFIRM_LOOP_FLAG);
            mHandler.sendEmptyMessage(COMFIRM_LOOP_FLAG);
            isCanPressBack=false;
            //验证验证码
            presenter.checkVerifiedCode(et_input_verification.getText().toString(), mInputNumber.getText().toString(),getActivity(),checkVerifiedCodeCallBack);

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
            sb.append("积分支付。");
            tvTips.setText(Html.fromHtml(sb.toString()));
        }


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

    //展示积分不足提示框
    private void showNotEnoughWindow(){
        View comfirmView = LayoutInflater.from(getActivity()).inflate(R.layout
                .window_pip_payment_not_enough, null);
        Button comfirmbtn = (Button) comfirmView.findViewById(R.id.confirm_btn);
        TextView textView = comfirmView.findViewById(R.id.tv_payment_tips);
        mNotEnoughWindow = new PopupWindow(comfirmView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mNotEnoughWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mNotEnoughWindow.setBackgroundDrawable(dw);
        textView.setText("积分不足，请选择其他支付方式进行订购！");
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

    /**
     * 来自甩屏内容进行甩屏播放
     */
    public void playOffscreen() {
        DetailPresenter mDetailPresenter = new DetailPresenter((RxAppCompatActivity) getActivity());
        mDetailPresenter.setDetailDataView(this);
        mDetailPresenter.setVODDetail(mVODDetail);
        if (null != mXmppMessage && null != mVODDetail) {
            String type = mXmppMessage.getMediaType();
            String vodId = null;
            String childVodId = null;
            if (!TextUtils.isEmpty(type) && ("2".equals(type))) {
                String mediaCode = mXmppMessage.getMediaCode();
                String[] str = mediaCode.split(",");
                vodId = str[0];
                if (str.length > 1) {
                    childVodId = mediaCode.split(",")[1];
                }
            }
            if (childVodId != null && childVodId.contains("index") && vodId != null) {
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

    /**
     * 通知URL鉴权完成
     * @param event event
     */
    @org.greenrobot.eventbus.Subscribe
    public void onEvent(FinishPlayUrlEvent event) {
        if (null != mPaymentPopwindow && mPaymentPopwindow.isShowing()) {
            mPaymentPopwindow.dismiss();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void executePay() {
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
//                PriceObject pb = mProductInfo.getPriceObject();
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

        List<PayChannel> payChannels = new ArrayList<PayChannel>();
        PayChannel payChannel = new PayChannel();
        payChannel.setChannelType("917");
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

        presenter.ScorePay(request,getActivity(),queryPointsCallBack);


    }
    /*
    *发送验证码回调
    */
    PointPayPresenter.SendVerifiedCodeCallBack sendVerifiedCodeCallBack = new PointPayPresenter.SendVerifiedCodeCallBack() {
        @Override
        public void sendVerifiedCodeSuccsee() {

        }

        @Override
        public void sendVerifiedCodeFail(String description) {
            EpgToast.showLongToast(OTTApplication.getContext(),
                    "发送验证码失败");
        }
    };

    /*
     *验证验证码回调
     */
    PointPayPresenter.CheckVerifiedCodeCallBack checkVerifiedCodeCallBack = new PointPayPresenter.CheckVerifiedCodeCallBack() {
        @Override
        public void checkVerifiedCodeSuccsee() {
            //验证码正确，进入订购流程
            checkOrderBeforeSubcribe();
        }

        @Override
        public void checkVerifiedCodeFail(String description) {
            EpgToast.showLongToast(OTTApplication.getContext(),
                    description);
            isClick = true;
        }
    };

    /*
     *换算积分回调
     */
    PointPayPresenter.ConvertFeeToScoreCallBack convertFeeToScoreCallBack = new PointPayPresenter.ConvertFeeToScoreCallBack() {
        @Override
        public void convertFeeToScoreSuccsee(String score) {
            if (!TextUtils.isEmpty(score)){
                needScoreStr = score;
                compareScore();
            }else{
                EpgToast.showLongToast(OTTApplication.getContext(),
                        "查询订购所需积分失败");
                isClick = true;
            }
        }

        @Override
        public void convertFeeToScoreFail(String description) {
            EpgToast.showLongToast(OTTApplication.getContext(),
                    "查询订购所需积分失败");
            isClick = true;
        }
    };

    /*
     *积分查询，积分支付回调
     */
    PointPayPresenter.QueryPointsCallBack queryPointsCallBack = new PointPayPresenter.QueryPointsCallBack() {
        @Override
        public void queryPointSuccess(RespParam param) {
            if (null != param && null != param.getBusinessInfo() && !TextUtils.isEmpty(param.getBusinessInfo().getTotalScore())){
                totalScoreStr = param.getBusinessInfo().getTotalScore();
                String price = mProductInfo.getPrice();
                if (null != marketProduct){
                    price = marketProduct.getPrice();
                }
                //查询积分订购所需要的积分
                presenter.convertFeeToScore(price,getActivity(),convertFeeToScoreCallBack);
            }else{
                EpgToast.showLongToast(OTTApplication.getContext(),
                        "查询积分失败");
                isClick = true;
            }
        }

        @Override
        public void queryPointFail() {
            EpgToast.showLongToast(OTTApplication.getContext(),
                    "查询积分失败");
            isClick = true;
        }

        @Override
        public void pointPaySuccess(OrderProductResponse response) {
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
        public void pointPayFail() {

            isClick = true;
            //记录下产品的订购时间
            UBDPurchase.record(mProductInfo,mVODDetail==null?"":mVODDetail.getID(), PurchaseData.FAIL);

        }
    };


    @Override
    public VODDetail initVodDetail() {
        return mVODDetail;
    }

    @Override
    public Product initProduct() {
        return mProductInfo;
    }

    @Override
    public int initCheckedPayment() {
        return NewMyPayModeActivity.PAYMENT_SOCRE;
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
    public void showDetail(VODDetail vodDetail, String actionId, List<RecmContents> recmContentsList) { }

    @Override
    public void showCollection(boolean isCollection) { }

    @Override
    public void setNewScore(List<Float> newScore) { }

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
}