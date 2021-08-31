package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.extview.VerticalScrollTextView;
import com.pukka.ydepg.common.http.bean.node.PayChnRstInfo;
import com.pukka.ydepg.common.http.bean.response.OrderProductResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6response.SubscribeProductResponse;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.PayUtil;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.mytv.ZjYdUniAndPhonePayActivty;
import com.pukka.ydepg.moudule.mytv.presenter.contract.PayContract;
import com.pukka.ydepg.moudule.mytv.utils.PaymentEvent;
import com.sinovatech.payment.client.sign.KeyCore;

import org.apache.commons.io.FileUtils;
import org.greenrobot.eventbus.EventBus;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewMyPayModeActivity.PAYMENT_THIRD_PART_PAY;

public class NewThirdPartPaymentFragment extends NewPaymentFragment implements PayContract.View, View.OnFocusChangeListener
{
    private static final String TAG = NewThirdPartPaymentFragment.class.getSimpleName();

    private static final String REQUEST_CHARSET = "GBK";

    private Product marketProduct;

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

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private WebView thirdPartPayment = null;

    /**
     * 是不是试看订购
     */
    private boolean isTrySeeSubscribe = false;

    /**
     * 是从订购中心
     */
    private boolean isOrderCenter=false;


    private Product mProduct;

    /**
     * VOD详情
     */
    private VODDetail mVODDetail;

    private Bundle arguments = null;

    private View rootView;


    private boolean isPageLoadSuccess;

    private  long expiredTimeDuration=4*60+30L;


    public static final String THIRD_PART_PAYMENT_CALLBACK_URL="http://127.0.0.1/chinamobile/zj/thirdpartpayment/callback";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        arguments = getArguments();
        mProduct = JsonParse.json2Object(arguments.getString(ZjYdUniAndPhonePayActivty.PRODUCT_INFO), Product.class);
        String vodDetailJson = getArguments().getString(ZjYdUniAndPhonePayActivty.VOD_DETAIL);
        if (!TextUtils.isEmpty(vodDetailJson)) {
            mVODDetail = JsonParse.json2Object(vodDetailJson, VODDetail.class);
        }

        isTrySeeSubscribe = getArguments().getBoolean(ZjYdUniAndPhonePayActivty.IS_TRY_SEE_SUBSCRIBE, false);

        isOrderCenter=getArguments().getBoolean(ZjYdUniAndPhonePayActivty.ISORDERCENTER,false);

        String marketProductStr = getArguments().getString(ZjYdUniAndPhonePayActivty.MARKETING_PRODUCT);
        if (!TextUtils.isEmpty(marketProductStr)) {
            marketProduct = JsonParse.json2Object(marketProductStr, Product.class);
        }

        if(rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_third_part_payment_new, container, false);
            initThirdPartPayment(rootView);
        }

        //产品包名称
        product_title_textview = (TextView) rootView.findViewById(R.id.order_paymode_item_title);
        //产品包价格
        product_price_textview = (TextView) rootView.findViewById(R.id.order_paymode_price);
        //产品包折扣信息
        product_dicount_tip = (TextView) rootView.findViewById(R.id.order_paymode_item_discount);
        //产品包价格单位
        product_prcie_mode_textview = (TextView) rootView.findViewById(R.id.order_paymode_price_mode);
        //产品包原价
        product_orgin_price_textview = (TextView) rootView.findViewById(R.id.order_paymode_item_discountprice);
        //产品包描述
        product_describe_textview = (VerticalScrollTextView) rootView.findViewById(R.id.order_paymode_event_text);
        //活动详情tip
        product_describe_tip = (TextView) rootView.findViewById(R.id.order_paymode_event_tip);

        initProductInfo();

        return rootView;
    }

    private void initProductInfo(){
        if (null == mProduct){
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

        Product product = mProduct;
        if (null != marketProduct){
            product = marketProduct;
        }

        //产品包名
        String name = mProduct.getName();
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
        if((!TextUtils.isEmpty(mProduct.getProductType()) && mProduct.getProductType().equals("1"))){
            product_prcie_mode_textview.setText("元/次");
        }else{
            product_prcie_mode_textview.setText("元/"+  StringUtils.analyticValidity(mProduct));
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

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        SuperLog.debug(TAG,"onHiddenChanged->hidden:"+hidden+"|UserVisibleHint:"+getUserVisibleHint());
        if(getUserVisibleHint()&&!hidden){
            isPageLoadSuccess = true;
            checkOrderBeforeSubcribe();
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResultProductInfo(Product product) { }

    @Override
    public void onResultKeyBoardValue(List<String> keyboardList) { }

    @Override
    public void onCountDownStart() { }

    @Override
    public void onCountDownFinish() { }

    @Override
    public void onCountDownProgressUpdate(Long progress) { }

    @Override
    public void onPaymentSucc(SubscribeProductResponse response) { }

    @Override
    public void onPaymentSucc(OrderProductResponse response) { }

    @Override
    public void onPaymentFailed() { }

    @Override
    public void updateUserRegInfoSucess() { }

    @Override
    public void updateUserRegInfoFail() { }

    @Override
    public void executePay() {
        SuperLog.info2SD(TAG,"Thirdpay->executePay");
        if(mOrderInfo == null) {
            SuperLog.error(TAG, "Order info is null");
            EpgToast.showLongToast(OTTApplication.getContext(), getString(R.string.subscirbe_fail));
            return ;
        }
        String url = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.THIRD_PART_PAYMENT_URL);
        if(url == null) {
            SuperLog.error(TAG, "Could not find the terminal parameter [third_part_payment_url]");
            EpgToast.showLongToast(OTTApplication.getContext(), getString(R.string.subscirbe_fail));
            return ;
        }
        Map<String, String> parameters = initThirdPartPaymentRequestParameter();
        byte[] postData = getPostData(parameters, REQUEST_CHARSET);
        SuperLog.info2SD(TAG, " Thirdpay->webview Load third part payment URL: [" + url + "]");
        thirdPartPayment.postUrl(url, postData);
    }

    @Override
    public VODDetail initVodDetail()
    {
        return mVODDetail;
    }

    @Override
    public Product initProduct()
    {
        return mProduct;
    }

    @Override
    public int initCheckedPayment()
    {
        return PAYMENT_THIRD_PART_PAY;
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

    /**
     * 加载第三方支付页面
     * @param container 加载第三方页面的父容器
     */
    private void initThirdPartPayment(View container) {
        thirdPartPayment = (WebView) container.findViewById(R.id.third_part_pay);
        thirdPartPayment.setVisibility(View.GONE);
        thirdPartPayment.setWebChromeClient(new WebChromeClient());
        thirdPartPayment.setOnFocusChangeListener(this);
        thirdPartPayment.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
        rootView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        thirdPartPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuperLog.debug(TAG,"Onclicked.");
            }
        });

        thirdPartPayment.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SuperLog.debug(TAG,"Onclicked.");
                return false;
            }
        });

        WebSettings settings = thirdPartPayment.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAllowFileAccess(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        thirdPartPayment.setWebViewClient(initWebViewClient());
    }

    private WebViewClient initWebViewClient() {
        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                super.shouldOverrideUrlLoading(view, request);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url) && url.contains("favicon.ico")) {
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @SuppressLint("NewApi")
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, final WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                isPageLoadSuccess = false;
                successOrderId=null;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                isPageLoadSuccess = false;
                successOrderId=null;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                SuperLog.info2SD(TAG,"Thirdpay-> webview Load Finished");
                // 页面加载成功时，展示页面，否则给出 页面加载失败提示
                if (isPageLoadSuccess) {
                    view.setVisibility(View.VISIBLE);
                    if(null!=mOrderInfo)
                    {
                        successOrderId =mOrderInfo.getOrderID();
                    }
                    // 页面加载成功后启动定时器，轮训订单状态
                    PaymentEvent event = new PaymentEvent(PaymentEvent.EventCode.CHECK_ORDER_STATUS_BEFORE_CALLBACK, NewMyPayModeActivity.PAYMENT_THIRD_PART_PAY);
                    EventBus.getDefault().post(event);

                } else {
                    EpgToast.showLongToast(getActivity(), getString(R.string.third_part_payment_load_failed_notification_message));
                }
            }
        };
    }

    private Map<String, String> initThirdPartPaymentRequestParameter() {
        Context context = OTTApplication.getContext();
        String dir = String.format("%s/key/", context.getFilesDir().getPath());
        File aesKey = new File(dir+ "aes.key");
        copySignKeyToTargetDir(aesKey, "key/aes.key");

        File publicKey = new File(dir+ "public.key");
        copySignKeyToTargetDir(publicKey, "key/public.key");

        File privateKey = new File(dir+ "private.key");
        copySignKeyToTargetDir(privateKey, "key/private.key");

        Map<String, String> map = new HashMap<String, String>();
        String appID = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.THIRD_PART_PAYMENT_APP_ID);
        if(appID == null) {
            SuperLog.error(TAG, "Could not find the terminal parameter [third_part_payment_app_id]");
        }
        PayChnRstInfo payChnRstInfo = mOrderInfo.getPayChannel().get(0);
        String orderNO = null;
        if (TextUtils.isEmpty(payChnRstInfo.getPaymentID())) {
            orderNO = payChnRstInfo.getPayID();
        } else {
            orderNO = payChnRstInfo.getPaymentID();
        }
        String amount = mOrderInfo.getFee();
        String orderDesc = mProduct.getName();
        String prifiled = generatePrivateFiledParam(orderNO);
        String version = "2018V5";
        String pageNotifyURL = THIRD_PART_PAYMENT_CALLBACK_URL;
        String bgNotifyURL = mOrderInfo.getCallbackURL();
        String methodMeris = "ZJYD-KDDS";
        String payWay = "PAYMETHOD017";
        String payChannel = Constant.BLANK_STARING;
        String accessType = "none";
        String payMethodGroupType = Constant.BLANK_STARING;


        map.put("appid", appID);
        map.put("orderno", orderNO);
        map.put("amount", amount);
        map.put("orderdesc", orderDesc);
        map.put("prifiled", prifiled);
        map.put("version", version);
        map.put("pageNotifyURL", pageNotifyURL);
        map.put("bgNotifyURL", bgNotifyURL);
        map.put("methodMeri", methodMeris);
        map.put("payWay", payWay);
        map.put("payChannel", payChannel);
        map.put("accessType", accessType);
        map.put("encoding", REQUEST_CHARSET);
        map.put("paymethodGroupType", payMethodGroupType);

        map.put("organCode", Constant.BLANK_STARING);
        map.put("organName", Constant.BLANK_STARING);
        map.put("orderExpireTime", Constant.BLANK_STARING);
        map.put("orderType", Constant.BLANK_STARING);
        map.put("orderTime", Constant.BLANK_STARING);

        String sign = KeyCore.getSign(KeyCore.getSignStr(map), dir);
        map.put("sign", sign);
        return map;
    }

    private String generatePrivateFiledParam(String orderId) {
        Map<String, String> params = new HashMap<String, String>();
        String billID = SessionService.getInstance().getSession().getAccountName();

        params.put("accountCode", TextUtils.isEmpty(billID) ? "" : billID);
        params.put("accountType", "1");
        params.put("accountName", Constant.BLANK_STARING);
        params.put("bankId", Constant.BLANK_STARING);
        params.put("bankIdExt", Constant.BLANK_STARING);
        params.put("isInstallment", "1");
        params.put("originId", "KDDS");
        params.put("payItemType", "60");
        params.put("payMethod", Constant.BLANK_STARING);
        SuperLog.error(TAG,"generatePrivateFiledParam->getExpireTime:"+expiredTimeDuration);
        if(!TextUtils.isEmpty(SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.THIRD_PAY_EXPIRED_TIME_DURATION))){
            expiredTimeDuration=Long.parseLong(SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.THIRD_PAY_EXPIRED_TIME_DURATION));
        }
        params.put("payPeriod", expiredTimeDuration+"s");
        params.put("interfaceType", "h5");

        String areaCode = SessionService.getInstance().getSession().getUserAreaCode();
        PayUtil.AreaInfo areaInfo = PayUtil.getAreaInfo(areaCode);
        String userAreaId = null;

        if (areaInfo != null) {
            userAreaId = areaInfo.getAreacode();
            if (userAreaId != null && areaInfo.getForeignsn().length() > 3) {
                params.put("RegionId", userAreaId.substring(0, 3));
            } else {
                params.put("RegionId", userAreaId);
            }
        }

        params.put("RegionId", areaCode);
        params.put("CountyId", userAreaId);

        // 产品ID
        params.put("GoodsMode", mProduct.getID());
        params.put("GoodsName", mProduct.getName());
        params.put("OrderId", orderId);
        params.put("PayAmount", mOrderInfo.getFee());
        double price = (Double.parseDouble(mOrderInfo.getFee()) / 100);
        params.put("Price", price + "");
        params.put("TotalFee", price + "");

        return JsonParse.object2String(params);
    }

    private byte[] getPostData(Map<String, String> parameters, String charset) {
        List<String> keys = new ArrayList<String>(parameters.keySet());
        Collections.sort(keys);
        StringBuilder builder = new StringBuilder(Constant.BLANK_STARING);
        String value = null;
        try {
            for (String key : keys) {
                value = parameters.get(key);
                if(value == null) {
                    value = Constant.BLANK_STARING;
                }
                builder.append(String.format(Locale.CHINA
                        , "%s=%s&"
                        , key
                        , URLEncoder.encode(value, charset)));

            }
            builder.deleteCharAt(builder.lastIndexOf("&"));
            return builder.toString().getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            SuperLog.error(TAG, e);
        }
        return new byte[0];
    }

    private void copySignKeyToTargetDir(File key, String srcKey){
        if (key.exists()) {
            SuperLog.info2SD(TAG, "Thirdpay->The [" + key.getPath() + "] is exists, skip to write a new one.");
            return;
        }
        InputStream inputReader = null;
        FileOutputStream out = null;
        try {
            inputReader = getResources().getAssets().open(srcKey);
            FileUtils.forceMkdirParent(key);
            out = new FileOutputStream(key);
            byte[] buffer = new byte[1024];
            while (inputReader.read(buffer) != -1) {
                out.write(buffer);
            }
        } catch (IOException e) {
            SuperLog.error(TAG, e);
        } finally {
            close(out);
            close(inputReader);
        }
    }

    private  void close(Closeable closeableObject) {
        if (closeableObject != null) {//关闭前要判断对象是否存在
            if( closeableObject instanceof Flushable){
                try {
                    ((Flushable)closeableObject).flush();
                } catch (Exception e) {
                    SuperLog.error(TAG, e);
                }
            }

            try {
                closeableObject.close();
                closeableObject = null; //将对象置空，这样下次Java的GC机制就会回收堆内存中的空间，接着下次会回收占内存中的空间。
            } catch (Exception e) {
                SuperLog.error(TAG, e);
            }
        }
    }

    @Override
    public void onDestroy() {
        thirdPartPayment.setVisibility(View.GONE);
        super.onDestroy();

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) {
            NewMyPayModeActivity activity = (NewMyPayModeActivity) getActivity();
            if (null != activity){
                activity.clearAllTitle();
            }
//            getActivity().findViewById(R.id.activity_mytv_movie_history).setFocusable(false);
//            getActivity().findViewById(R.id.activity_mytv_movie_collection).setFocusable(false);
//            getActivity().findViewById(R.id.pay_benefit).setFocusable(false);
        }
        if (!hasFocus && null != getActivity()) {
            NewMyPayModeActivity activity = (NewMyPayModeActivity) getActivity();
            if (null != activity.getNowTitle()){
                activity.getNowTitle().setFocusable(true);
                activity.getNowTitle().requestFocus();
            }
//            getActivity().findViewById(R.id.menu_item_third_part_payment).setFocusable(true);
//            getActivity().findViewById(R.id.menu_item_third_part_payment).requestFocus();
        }
    }
}
