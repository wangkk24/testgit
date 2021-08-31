package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.BroadCastConstant;
import com.pukka.ydepg.common.http.bean.node.XmppMessage;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.vss.node.OrderInfo;
import com.pukka.ydepg.common.http.vss.request.QueryOrderInfoRequest;
import com.pukka.ydepg.common.http.vss.response.CancelOrderResponse;
import com.pukka.ydepg.common.http.vss.response.QueryOrderInfoResponse;
import com.pukka.ydepg.common.report.ubd.extension.PurchaseData;
import com.pukka.ydepg.common.report.ubd.scene.UBDPurchase;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.RxTimerUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.common.utils.uiutil.Strings;
import com.pukka.ydepg.event.FinishPlayUrlEvent;
import com.pukka.ydepg.event.PlayUrlEvent;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.base.BaseFragment;
import com.pukka.ydepg.moudule.mytv.ZjYdUniAndPhonePayActivty;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.AvoidRepeatPaymentUtils;
import com.pukka.ydepg.moudule.mytv.presenter.PayModePresenter;
import com.pukka.ydepg.moudule.mytv.presenter.contract.PayModeContract;
import com.pukka.ydepg.moudule.mytv.utils.PaymentEvent;
import com.pukka.ydepg.moudule.player.node.CurrentChannelPlaybillInfo;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.view.DetailDataView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class NewMyPayModeActivity extends BaseActivity implements  PayModeContract.View,
        View.OnFocusChangeListener, View.OnKeyListener, DetailDataView
{

    static final String myPayModeStr_unipay = "关联手机号支付";

    static final String myPaymodeStr_phonePay = "其他手机号支付";

    static final String myPaymodeStr_benefit = "观影券支付";

    static final String myPaymodeStr_ThirdPartPay = "微信/支付宝";

    static final String myPaymodeStr_ScorePay = "积分支付";

    //第一个标签
    TextView firstTitle;

    //第二个标签
    TextView secondTitle;

    //第三个标签
    TextView thirdTitle;

    //第四个标签
    TextView forthTitle;

    //第五个标签
    TextView fifthTitle;

    TextView nowTitle;


    TextView title;
//    /**
//     * 订购列表
//     */
//    TextView orderListtv;
//    /**
//     * 订购短信验证开关开关
//     */
//    TextView orderSettv;
//
//    /**
//     * 第三方支付导航菜单
//     */
//    TextView menuThirdPartPayment;
//
//    TextView pay_benefit;

    NewPaymentFragment mFragment;
    View mMenu;
    View mLine;
    NewPhonePayFragment mPhonePayFragement;
    NewUniPayFragment mUniPayFragment;

    NewPayBenefitFragment payBenefitFragment;


    private Product mProduct;

    private PayModePresenter mPayModePresenter;

    static OrderInfo cachaOrderInfo;

    /**
     * 第三方支付UI
     */
    private NewThirdPartPaymentFragment mThirdPartPaymentFragment;

    /**
     * 积分支付UI
     */
    private NewPointPayFragment mPointPaymentFragment;

    //是否支持第三方支付
    private boolean needShowThirdPartPayment = false;

    //是否支持观影卷支付
    private boolean needShowBenefitPayment = false;

    //是否支持统一支付
    private boolean needshowUniPayment = true;

    //是否支持其他手机号支付
    private boolean needShowPhonePayment = true;

    //是否支持积分支付
    private boolean needShowPointPayment = false;

    private static final String TAG = NewMyPayModeActivity.class.getSimpleName();


    public static final String NEED_SHOW_THIRD_PART_PAYMENT = "needShowThirdPartPayment";

    public static final String NEED_SHOW_USER_STATE_VALIDATION_FAILED_NOTIFICATION = "needShowUserStateValidationFailedNotification";

    /**
     * 支付方式标识：手机支付
     */
    public static final int PAYMENT_PHONE_PAY = 0;

    /**
     * 支付方式标识：统一支付
     */
    public static final int PAYMENT_UNI_PAY = 1;

    /**
     * 支付方式标识：第三方支付
     */
    public static final int PAYMENT_THIRD_PART_PAY = 2;

    /**
     * 支付方式标识：观影券支付
     */
    public static final int PAYMENT_BENEFIT = 3;

    /**
     * 支付方式标识：积分支付
     */
    public static final int PAYMENT_SOCRE = 4;

    //原始支付方式列表
    private List<String> orgPaymentList = new ArrayList<>();

    //筛选之后展示用的支付方式列表
    private List<String> nowPaymentList = new ArrayList<>();


    public static final String DATA_BUNDLE_KEY_ORDER_INFO = "dataBundleKeyOrderInfo";

    private static int checkedPayment;

    //产品包扩展参数key 支付方式灵活可配
    private static final String ZJ_PRODUCT_OF_PAYMENT = "zjProductofPayment";

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pay_mode_new);
        initPresenter();
        // 2018/10/10 第三方支付需求 ADD END
        initView();
        initData();
    }

    /**
     * 1 历史选中 其余收藏选中
     */
    private void initData() {
        title.setText(getString(R.string.pay_mode_uni_phone));
        firstTitle.requestFocus();
        nowTitle = firstTitle;
    }

    public void hideAllFragment(Fragment... fragments){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for(int i=0;i<fragments.length;i++){
            if(null!=fragments[i])
            {
                transaction.hide(fragments[i]);
            }
        }
        transaction.commitAllowingStateLoss();
    }


    private void initView() {
        mProduct = JsonParse.json2Object(getIntent().getStringExtra(ZjYdUniAndPhonePayActivty.PRODUCT_INFO), Product.class);
        mLine = findViewById(R.id.line);
        title = (TextView) findViewById(R.id.activity_mytv_movie_title);

        firstTitle = (TextView) findViewById(R.id.activity_mytv_movie_history);
        secondTitle = (TextView) findViewById(R.id.activity_mytv_movie_collection);
        thirdTitle = (TextView) findViewById(R.id.pay_benefit);
        forthTitle = (TextView) findViewById(R.id.menu_item_third_part_payment);
        fifthTitle = (TextView) findViewById(R.id.menu_item_fifth);

        mMenu = findViewById(R.id.activity_my_movie_menu);

        //获取展示统一支付和其他手机号支付的标识
        String showMode = getIntent().getStringExtra(ZjYdUniAndPhonePayActivty.SHOW_ITEM_MODE);
        if (ZjYdUniAndPhonePayActivty.HOTEL_MODE.equals(showMode)) {
            needshowUniPayment = false;
            needShowPhonePayment = true;
        } else if (ZjYdUniAndPhonePayActivty.MONTH_PRODUCT_MODE.equals(showMode)) {
            needshowUniPayment = true;
            needShowPhonePayment = false;
            orgPaymentList.add(myPayModeStr_unipay);
        }else{
            needshowUniPayment = true;
            needShowPhonePayment = true;
            orgPaymentList.add(myPayModeStr_unipay);
            orgPaymentList.add(myPaymodeStr_phonePay);
        }

        // 获取是否需要展示第三方支付的标识
        needShowThirdPartPayment = getIntent().getBooleanExtra(NEED_SHOW_THIRD_PART_PAYMENT, false);
        if (needShowThirdPartPayment){
            orgPaymentList.add(myPaymodeStr_ThirdPartPay);
        }

        //获取是否需要展示观影卷支付的标识
        needShowBenefitPayment = getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.IS_ORDER_BY_ORDER,false);
        if (needShowBenefitPayment){
            orgPaymentList.add(myPaymodeStr_benefit);
        }

        //获取是否需要展示积分支付的标识(产品不支持自动续订或者是单点产品)
        if (mProduct.getIsAutoExtend().equals("0") || mProduct.getProductType().equals("1")){
            needShowPointPayment = true;
            orgPaymentList.add(myPaymodeStr_ScorePay);
        }else{
            needShowPointPayment = false;
        }


        //2020.4.13 添加支付方式灵活可配需求
        initPaymode();


        mPointPaymentFragment = new NewPointPayFragment();
        bindDataToFragment(mPointPaymentFragment);
        mPhonePayFragement = new NewPhonePayFragment();
        bindDataToFragment(mPhonePayFragement);
        mUniPayFragment = new NewUniPayFragment();
        bindDataToFragment(mUniPayFragment);

        firstTitle.setOnFocusChangeListener(this);
        firstTitle.setOnKeyListener(this);

        secondTitle.setOnFocusChangeListener(this);
        secondTitle.setOnKeyListener(this);

        thirdTitle.setOnFocusChangeListener(this);
        thirdTitle.setOnKeyListener(this);

        forthTitle.setOnFocusChangeListener(this);
        forthTitle.setOnKeyListener(this);

        fifthTitle.setOnFocusChangeListener(this);
        fifthTitle.setOnKeyListener(this);

        isSupportBenefitWatch();

        // 2018/10/10 第三方支付需求 ADD START
        initThirdPartPaymentSupport();
        // 2018/10/10 第三方支付需求 ADD END
    }

    //2020.4.13 添加支付方式灵活可配需求
    private void initPaymode(){
        List<NamedParameter> listNp = mProduct.getCustomFields();
        List<String> paymodeStrList = CommonUtil.getCustomNamedParameterByKey(listNp, ZJ_PRODUCT_OF_PAYMENT);
        if (null != paymodeStrList && paymodeStrList.size() > 0){
            String paymodeStr = paymodeStrList.get(0);

            //获取配置的排序的支付方式列表
            List<String> paymodeList = Arrays.asList(paymodeStr.split("|"));

            //不存在交集时展示原本默认的支付方式
            if (!isHavePaymode(paymodeStr)){
                //按照原本的支付方式顺序装进数组
                showOrgPayment();
                showPayment();
                return;
            }

            StringBuilder sb = new StringBuilder("");
            if (paymodeStr.length()>0){
                //是否展示统一支付
                if (needshowUniPayment && paymodeStr.contains("0")){
                    sb.append("0");
                }
                //是否展示其他手机号支付
                if (needShowPhonePayment && paymodeStr.contains("1")){
                    sb.append("1");
                }
                //是否展示观影卷支付
                if (needShowBenefitPayment && paymodeStr.contains("2")){
                    sb.append("2");
                }
                //是否展示第三方支付
                if (needShowThirdPartPayment && paymodeStr.contains("3")){
                    sb.append("3");
                }
                //是否展示积分支付
                if (needShowPointPayment && paymodeStr.contains("4")){
                    sb.append("4");
                }
            }
            //遍历配置的排序的支付方式列表，支持展示则加入展示的支付方式列表
            nowPaymentList.clear();
            Log.i(TAG, "initPaymode:  "+sb);
            for (int i = 0; i < paymodeList.size() ; i++) {
                String str = paymodeList.get(i);
                Log.i(TAG, "initPaymode: "+str);
                if (sb.toString().contains(str)){
                    if (!"".equals(getTitleStr(str))){
                        nowPaymentList.add(getTitleStr(str));
                    }
                }
            }
            //展示
            showPayment();
        }else{
            showOrgPayment();
            showPayment();
        }
    }

    private String getTitleStr(String num){
        switch (num){
            case "0":{
                return myPayModeStr_unipay;
            }
            case "1":{
                return myPaymodeStr_phonePay;
            }
            case "2":{
                return myPaymodeStr_benefit;
            }
            case "3":{
                return myPaymodeStr_ThirdPartPay;
            }
            case "4":{
                return myPaymodeStr_ScorePay;
            }
        }
        return "";
    }

    private void showOrgPayment(){
        //按照原本的支付方式顺序装进数组
        nowPaymentList.clear();
        if (needshowUniPayment){
            nowPaymentList.add(myPayModeStr_unipay);
        }
        if (needShowPhonePayment){
            nowPaymentList.add(myPaymodeStr_phonePay);
        }
        if (needShowBenefitPayment){
            nowPaymentList.add(myPaymodeStr_benefit);
        }
        if (needShowThirdPartPayment){
            nowPaymentList.add(myPaymodeStr_ThirdPartPay);
        }
        if (needShowPointPayment){
            nowPaymentList.add(myPaymodeStr_ScorePay);
        }
    }

    /*原本默认支付方式和配置的支付方式是否有交集
     *true   存在交集时展示交集的支付方式
     *false  不存在交集时展示原本默认的支付方式
     */
    private boolean isHavePaymode(String paymodeStr){
        //是否展示统一支付
        if (needshowUniPayment && paymodeStr.contains("0")){
            return true;
        }
        //是否展示其他手机号支付
        if (needShowPhonePayment && paymodeStr.contains("1")){
            return true;
        }
        //是否展示观影卷支付
        if (needShowBenefitPayment && paymodeStr.contains("2")){
            return true;
        }
        //是否展示第三方支付
        if (needShowThirdPartPayment && paymodeStr.contains("3")){
            return true;
        }
        if (needShowPointPayment && paymodeStr.contains("4")){
            return true;
        }

        return false;
    }

    //根据nowPaymentList展示支付方式
    private void showPayment(){

        if (nowPaymentList.size() >= 1){
            firstTitle.setVisibility(View.VISIBLE);
            firstTitle.setText(nowPaymentList.get(0));
        }

        if (nowPaymentList.size() >= 2){
            secondTitle.setVisibility(View.VISIBLE);
            secondTitle.setText(nowPaymentList.get(1));
        }

        if (nowPaymentList.size() >= 3){
            thirdTitle.setVisibility(View.VISIBLE);
            thirdTitle.setText(nowPaymentList.get(2));
        }

        if (nowPaymentList.size() >= 4){
            forthTitle.setVisibility(View.VISIBLE);
            forthTitle.setText(nowPaymentList.get(3));
        }

        if (nowPaymentList.size() >= 5){
            fifthTitle.setVisibility(View.VISIBLE);
            fifthTitle.setText(nowPaymentList.get(4));
        }
    }

    //修改崩溃问题，不在使用super方法
    @Override
    @SuppressLint("MissingSuperCall")
    protected void onSaveInstanceState(Bundle outState) {

    }

    /**
     * 优惠券支付
     */
    private void isSupportBenefitWatch(){
        payBenefitFragment = new NewPayBenefitFragment();
        //这里做观影卷支付的处理
        if(needShowBenefitPayment){
            bindDataToFragment(payBenefitFragment);
        }
    }

    private void initThirdPartPaymentSupport() {
        //这里做第三方支付的处理
        if (needShowThirdPartPayment) {
            bindDataToThirdPartPaymentFragment();
        } else {
            // 读取是否展示提示标识，当用户校验失败导致第三方支付不展示时，需展示提示。
            boolean needShowUserStateValidationFailedNotification
                    = getIntent().getBooleanExtra(NEED_SHOW_USER_STATE_VALIDATION_FAILED_NOTIFICATION, false);
            if (needShowUserStateValidationFailedNotification) {
                EpgToast.showLongToast(this, Strings.getInstance().getString(R.string.user_state_validation_failed));
            }
        }
    }

    private void bindDataToFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(ZjYdUniAndPhonePayActivty.UNI_PAY_TYPE, getIntent().getStringExtra(ZjYdUniAndPhonePayActivty.UNI_PAY_TYPE));
        bundle.putString(ZjYdUniAndPhonePayActivty.PRODUCT_INFO, getIntent().getStringExtra(ZjYdUniAndPhonePayActivty.PRODUCT_INFO));
        bundle.putString(ZjYdUniAndPhonePayActivty.UNIPAY_BILLID, getIntent().getStringExtra(ZjYdUniAndPhonePayActivty.UNIPAY_BILLID));
        bundle.putString(ZjYdUniAndPhonePayActivty.VOD_DETAIL, getIntent().getStringExtra(ZjYdUniAndPhonePayActivty.VOD_DETAIL));
        bundle.putBoolean(ZjYdUniAndPhonePayActivty.ISVOD_SUBSCRIBE, getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISVOD_SUBSCRIBE, false));
        bundle.putBoolean(ZjYdUniAndPhonePayActivty.ISTVOD_SUBSCRIBE, getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISTVOD_SUBSCRIBE, false));
        bundle.putBoolean(ZjYdUniAndPhonePayActivty.ISORDERCENTER, getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISORDERCENTER, false));
        bundle.putStringArray(ZjYdUniAndPhonePayActivty.CHANNELID_MEDIAID, getIntent().getStringArrayExtra(ZjYdUniAndPhonePayActivty.CHANNELID_MEDIAID));
        bundle.putBoolean(ZjYdUniAndPhonePayActivty.IS_TRY_SEE_SUBSCRIBE, getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.IS_TRY_SEE_SUBSCRIBE, false));
        bundle.putBoolean(ZjYdUniAndPhonePayActivty.ISOFFSCREEN_SUBSCRIBE, getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISOFFSCREEN_SUBSCRIBE, false));
        bundle.putSerializable(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE, getIntent().getSerializableExtra(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE));
        bundle.putString(ZjYdUniAndPhonePayActivty.MARKETING_PRODUCT, getIntent().getStringExtra(ZjYdUniAndPhonePayActivty.MARKETING_PRODUCT));
        bundle.putString(ZjYdUniAndPhonePayActivty.UNSUBPRODINFOIDS, getIntent().getStringExtra(ZjYdUniAndPhonePayActivty.UNSUBPRODINFOIDS));

        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_my_movie_content, fragment).commitAllowingStateLoss();
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        NewPaymentFragment to = null;

        TextView view = (TextView)v;
        String title = view.getText().toString();
        if (hasFocus){
            firstTitle.setSelected(false);
            secondTitle.setSelected(false);
            thirdTitle.setSelected(false);
            forthTitle.setSelected(false);
            fifthTitle.setSelected(false);
            view.setSelected(true);
            nowTitle = view;
            firstTitle.setFocusable(true);
            secondTitle.setFocusable(true);
            thirdTitle.setFocusable(true);
            forthTitle.setFocusable(true);
            fifthTitle.setFocusable(true);
            switch (title) {
                case myPayModeStr_unipay:
                    to = mUniPayFragment;
                    // 2018/10/10 第三方支付需求 ADD START
                    checkedPayment = PAYMENT_UNI_PAY;
                    RxTimerUtil.cancel();
                    switchContent(to);
                    // 2018/10/10 第三方支付需求 ADD END
                    break;
                case myPaymodeStr_phonePay:
                    to = mPhonePayFragement;
                    // 2018/10/10 第三方支付需求 ADD START
                    RxTimerUtil.cancel();
                    checkedPayment = PAYMENT_PHONE_PAY;
                    switchContent(to);
                    // 2018/10/10 第三方支付需求 ADD END
                    break;
                // 2018/10/10 第三方支付需求 ADD START
                case myPaymodeStr_benefit:
                    to = payBenefitFragment;
                    checkedPayment = PAYMENT_BENEFIT;
                    RxTimerUtil.cancel();
                    switchContent(to);
                    break;
                case myPaymodeStr_ThirdPartPay:
                    SuperLog.debug("ThirdPartPaymentFragment","click third pay");
                    to = mThirdPartPaymentFragment;
                    checkedPayment = PAYMENT_THIRD_PART_PAY;
                    switchContent(to);
                    break;
                case myPaymodeStr_ScorePay:
                    to = mPointPaymentFragment;
                    checkedPayment = PAYMENT_SOCRE;
                    switchContent(to);
                    break;
                default:
                    break;
                // 2018/10/10 第三方支付需求 ADD END
            }
        }

    }

    /**
     *      * Modify the contents of the display will not reload
     */
    protected void switchContent(NewPaymentFragment to) {

        if (mFragment != to && !this.isFinishing()) {
            hideAllFragment(mUniPayFragment,mPhonePayFragement,payBenefitFragment,mThirdPartPaymentFragment,mPointPaymentFragment);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.show(to).commitAllowingStateLoss();
            mFragment = to;
            mPhonePayFragement.reload();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK&&null!=mFragment&&!mFragment.isCanPressBack()){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_DOWN:
                {
                    TextView view = (TextView) v;
                    if (view.getText().toString().equals(myPaymodeStr_benefit)){
                        return true;
                    }else{
                        jumpActivity(view.getText().toString());
                        return true;
                    }
                }
                case KeyEvent.KEYCODE_DPAD_UP:
                {
                    TextView view = (TextView) v;
                    if (view.getText().toString().equals(myPaymodeStr_ThirdPartPay)){
                        return true;
                    }
                    return false;
                }
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                {
                    if (null != getLastVisibleTitle()){
                        if (getLastVisibleTitle() == firstTitle){
                            return true;
                        }else if (getLastVisibleTitle() == secondTitle){
                            if (v.getId() == R.id.activity_mytv_movie_collection){
                                return true;
                            }
                            return false;
                        }else if (getLastVisibleTitle() == thirdTitle){
                            if (v.getId() == R.id.pay_benefit){
                                return true;
                            }
                            return false;
                        }else if (getLastVisibleTitle() == forthTitle){
                            if (v.getId() == R.id.menu_item_third_part_payment){
                                return true;
                            }
                            return false;
                        }else if (getLastVisibleTitle() == fifthTitle){
                            if (v.getId() == R.id.menu_item_fifth){
                                return true;
                            }
                            return false;
                        }
                    }
                }
                default:
                    break;
            }
        return false;
    }

    private TextView getLastVisibleTitle(){
        switch (nowPaymentList.size()){
            case 1:
            {
                return firstTitle;
            }
            case 2:
            {
                return secondTitle;
            }
            case 3:
            {
                return thirdTitle;
            }
            case 4:
            {
                return forthTitle;
            }
            case 5:
            {
                return fifthTitle;
            }
        }
        return null;
    }

    public TextView getNowTitle(){
        return nowTitle;
    }

    //除了当前正在的标签的标题外的标题全部置为不能落焦，防止误跳
    public void clearAllTitle(){
        firstTitle.setFocusable(false);
        secondTitle.setFocusable(false);
        thirdTitle.setFocusable(false);
        forthTitle.setFocusable(false);
        fifthTitle.setFocusable(false);

        nowTitle.setFocusable(true);
    }


    private void jumpActivity(String str) {
        switch (str) {
            //关联手机号支付(统一支付)
            case myPayModeStr_unipay:
                if (null != mUniPayFragment) {
                    if (null != mUniPayFragment.findViewById(R.id.children_pay_lay) && mUniPayFragment.findViewById(R.id.children_pay_lay).getVisibility() == View.VISIBLE) {
                        if (null != mUniPayFragment.findViewById(R.id.get_verification) && mUniPayFragment.findViewById(R.id.get_verification).isFocusable()) {
                            mUniPayFragment.findViewById(R.id.get_verification).requestFocus();
                        } else {
                            if (null != mUniPayFragment.findViewById(R.id.et_input_verification)) {
                                mUniPayFragment.findViewById(R.id.et_input_verification).setFocusable(true);
                                mUniPayFragment.findViewById(R.id.et_input_verification).requestFocus();
                            }
                        }
                    } else if (null != mUniPayFragment.findViewById(R.id.uni_pay_lay) && mUniPayFragment.findViewById(R.id.uni_pay_lay).getVisibility() == View.VISIBLE) {
                        if( CommonUtil.showVerifyCode()) {
                            //这里
                            if (null != mUniPayFragment.findViewById(R.id.et_code_number)) {
                                mUniPayFragment.findViewById(R.id.et_code_number).setFocusable(true);
                                mUniPayFragment.findViewById(R.id.et_code_number).requestFocus();
                            }
                        }else {
                            if (null != mUniPayFragment.findViewById(R.id.comfirm_btn_normal)) {
                                mUniPayFragment.findViewById(R.id.comfirm_btn_normal).setFocusable(true);
                                mUniPayFragment.findViewById(R.id.comfirm_btn_normal).requestFocus();
                            }
                        }
                    }else if (null != mUniPayFragment.findViewById(R.id.qrcode_layout) && mUniPayFragment.findViewById(R.id.qrcode_layout).getVisibility() == View.VISIBLE && mUniPayFragment.findViewById(R.id.order_paymode_event_text).getVisibility() == View.VISIBLE ){
                        mUniPayFragment.findViewById(R.id.order_paymode_event_text).setFocusable(true);
                        mUniPayFragment.findViewById(R.id.order_paymode_event_text).requestFocus();
                    }
                } else {
                    firstTitle.requestFocus();
                }

                break;
            //其他手机号支付
            case myPaymodeStr_phonePay:
                if (null != mPhonePayFragement && null != mPhonePayFragement.findViewById(R.id.et_phone_number)) {
                    mPhonePayFragement.findViewById(R.id.et_phone_number).setFocusable(true);
                    mPhonePayFragement.findViewById(R.id.et_phone_number).requestFocus();
                }
                break;
            case myPaymodeStr_ThirdPartPay:
                // 第三方支付
                if (null != mThirdPartPaymentFragment) {
                    mThirdPartPaymentFragment.findViewById(R.id.third_part_pay).setFocusable(true);
                    mThirdPartPaymentFragment.findViewById(R.id.third_part_pay).requestFocus();
                }
                break;
            case myPaymodeStr_ScorePay:
                //积分支付
                if (null != mPointPaymentFragment){
                    mPointPaymentFragment.findViewById(R.id.et_phone_number).setFocusable(true);
                    mPointPaymentFragment.findViewById(R.id.et_phone_number).requestFocus();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        SuperLog.debug(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        SuperLog.debug(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        SuperLog.debug(TAG, "onDestroy");
        cachaOrderInfo=null;
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    // 2018/10/10 第三方支付需求 ADD START
    public void playOffScreen() {
        DetailPresenter mDetailPresenter = new DetailPresenter(this);
        mDetailPresenter.setDetailDataView(this);
        VODDetail mVODDetail = JsonParse.json2Object(getIntent().getStringExtra(ZjYdUniAndPhonePayActivty.VOD_DETAIL), VODDetail.class);
        XmppMessage mXmppMessage = (XmppMessage) getIntent().getSerializableExtra(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE);
        mDetailPresenter.setVODDetail(mVODDetail);
        if (null != mXmppMessage && null != mVODDetail) {
            String mediaType = mXmppMessage.getMediaType();
            String vodId = null;
            String childVodId = null;

            if (!TextUtils.isEmpty(mediaType) && ("2".equals(mediaType))) { // VOD

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
                    if (vodMediaFiles != null && !vodMediaFiles.isEmpty()) {
                        VODMediaFile vodMediaFile = vodMediaFiles.get(0);
                        mDetailPresenter.playClipfile(vodMediaFile, vodId, vodMediaFile.getElapseTime());
                    }
                } else {//剧集
                    List<Episode> episodes = mVODDetail.getEpisodes();
                    for (Episode episode : episodes) {
                        VOD vod = episode.getVOD();
                        if (vod.getID().equals(childVodId)) {
                            List<VODMediaFile> vodMediaFiles = vod.getMediaFiles();
                            if (vodMediaFiles != null && !vodMediaFiles.isEmpty()) {
                                VODMediaFile vodMediaFile = vodMediaFiles.get(0);
                                mDetailPresenter.playClipfile(vodMediaFile, vod.getID(), vodMediaFile.getElapseTime());
                            }
                        }
                    }
                }
            }
        }
    }

    public void onPaymentSuccess() {
        //立刻发起心跳
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadCastConstant.Heartbit
                .COM_HUAWEI_OTT_START_HEARTBIT));
        //记录下产品的订购时间
        AvoidRepeatPaymentUtils.getInstance().recordPaymentTime(mProduct.getID());
        //支付成功弹框提示
        showPaymentSuccessPopWindow();
        orderFinish();
    }

    public void orderFinish(){

        //订购成功上报
        VODDetail mVODDetail = JsonParse.json2Object(getIntent().getStringExtra(ZjYdUniAndPhonePayActivty.VOD_DETAIL), VODDetail.class);
        UBDPurchase.record(mProduct,mVODDetail==null?"":mVODDetail.getID(), PurchaseData.SUCCESS);
        //发送鉴权消息
        new Handler().postDelayed(() -> {
            String[] channelMediaIds = getIntent().getStringArrayExtra(ZjYdUniAndPhonePayActivty.CHANNELID_MEDIAID);
            boolean isVODSubscribe = getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISVOD_SUBSCRIBE, false);
            boolean isTVODSubscribe = getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISTVOD_SUBSCRIBE, false);
            boolean isTrySeeSubscribe = getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.IS_TRY_SEE_SUBSCRIBE, false);
            boolean isOrderCenter = getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISORDERCENTER, false);
            PlayUrlEvent event = null;
            if (null != channelMediaIds && channelMediaIds.length == 2) {
                CurrentChannelPlaybillInfo playbillInfo = new CurrentChannelPlaybillInfo();
                playbillInfo.setChannelId(channelMediaIds[0]);
                playbillInfo.setChannelMediaId(channelMediaIds[1]);
                //直播鉴权
                event = new PlayUrlEvent(playbillInfo);
            } else if (isVODSubscribe) {
                boolean isOffScreen = getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISOFFSCREEN_SUBSCRIBE, false);
                //VOD鉴权
                if (isOffScreen) {
                    playOffScreen();
                } else {
                    event = new PlayUrlEvent(true, false,mProduct==null?"":mProduct.getID());
                }
            } else if (isTVODSubscribe) {
                //回看鉴权
                event = new PlayUrlEvent(false, true,mProduct==null?"":mProduct.getID());
            } else if (isTrySeeSubscribe) {
                event = new PlayUrlEvent(false, false, true,mProduct==null?"":mProduct.getID());
            } else if (isOrderCenter) {
                event = new PlayUrlEvent(false, false, false, true,mProduct==null?"":mProduct.getID());
            } else {
                event = new PlayUrlEvent(false, false, true,mProduct==null?"":mProduct.getID());
            }
            EventBus.getDefault().post(event);
        }, 2000);
    }

    RxTimerUtil.IRxNext task = new RxTimerUtil.IRxNext() {
        Context context = getBaseContext();

        @Override
        public void doNext(long number) {
            SuperLog.debug(TAG, "doNext number:" + number);
            if(cachaOrderInfo!=null) {
                QueryOrderInfoRequest request = new QueryOrderInfoRequest();
                request.setOrderID(cachaOrderInfo.getOrderID());
                request.setUserID(AuthenticateManager.getInstance().getCurrentUserInfo().getUserId());
                Observable<QueryOrderInfoResponse> observable = mPayModePresenter.vssQueryOrderInfo(request, context);
                Disposable disposable = observable.subscribe(queryOrderInfoResponse -> {
                    if (null != queryOrderInfoResponse
                            && (TextUtils.equals(queryOrderInfoResponse.getCode(), Result.RETCODE_OK)
                            || TextUtils.equals(queryOrderInfoResponse.getCode(), Result.RETCODE_OK_TWO))) {
                        List<OrderInfo> orders = queryOrderInfoResponse.getOrderList();
                        if (orders != null && !orders.isEmpty()) {
                            OrderInfo orderInfo = orders.get(0);
                            if (orderInfo != null) {
                                if ((OrderInfo.Status.FINISHED + "").equals(orderInfo.getOrderStatus())) {
                                    RxTimerUtil.cancel();
                                    SuperLog.error(TAG, "The order has finished.");
                                    onPaymentSuccess();
                                } else if((OrderInfo.Status.CANCELED + "").equals(orderInfo.getOrderStatus())) {
                                    SuperLog.error(TAG, "The order has been canceled.");
                                    RxTimerUtil.cancel();
                                    NewMyPayModeActivity.cachaOrderInfo=null;
                                    EpgToast.showLongToast(OTTApplication.getContext(), getString(R.string.third_part_payment_order_has_been_canceled));
                                }
                            }
                        }
                    }
                }, throwable -> {
                    SuperLog.debug(TAG, throwable.getMessage());
                });
            }else{
                RxTimerUtil.cancel();
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void handleEvent(PaymentEvent event) {
        if (event.getCode() == PaymentEvent.EventCode.CHECK_ORDER_STATUS_BEFORE_CALLBACK) {
            mPayModePresenter.checkOrderStatusBeforeCallback(task);
        } else if (event.getCode() == PaymentEvent.EventCode.CHECK_ORDER_STATUS_AFTER_CALLBACK) {
            // 拦截到第三方支付回调后轮询订单状态
            mPayModePresenter.checkOrderStatusAfterCallback(task);
        }
    }

    private void bindDataToThirdPartPaymentFragment() {
        if (mThirdPartPaymentFragment == null) {
            mThirdPartPaymentFragment = new NewThirdPartPaymentFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString(ZjYdUniAndPhonePayActivty.PRODUCT_INFO, getIntent().getStringExtra(ZjYdUniAndPhonePayActivty.PRODUCT_INFO));
        bundle.putString(ZjYdUniAndPhonePayActivty.VOD_DETAIL, getIntent().getStringExtra(ZjYdUniAndPhonePayActivty.VOD_DETAIL));
        bundle.putString(ZjYdUniAndPhonePayActivty.UNIPAY_BILLID, getIntent().getStringExtra(ZjYdUniAndPhonePayActivty.UNIPAY_BILLID));
        bundle.putBoolean(ZjYdUniAndPhonePayActivty.ISVOD_SUBSCRIBE, getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISVOD_SUBSCRIBE, false));
        bundle.putBoolean(ZjYdUniAndPhonePayActivty.ISTVOD_SUBSCRIBE, getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISTVOD_SUBSCRIBE, false));
        bundle.putStringArray(ZjYdUniAndPhonePayActivty.CHANNELID_MEDIAID, getIntent().getStringArrayExtra(ZjYdUniAndPhonePayActivty.CHANNELID_MEDIAID));
        bundle.putBoolean(ZjYdUniAndPhonePayActivty.IS_TRY_SEE_SUBSCRIBE, getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.IS_TRY_SEE_SUBSCRIBE, false));
        bundle.putBoolean(ZjYdUniAndPhonePayActivty.ISORDERCENTER, getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISORDERCENTER, false));
        bundle.putBoolean(ZjYdUniAndPhonePayActivty.ISOFFSCREEN_SUBSCRIBE, getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISOFFSCREEN_SUBSCRIBE, false));
        bundle.putString(ZjYdUniAndPhonePayActivty.MARKETING_PRODUCT, getIntent().getStringExtra(ZjYdUniAndPhonePayActivty.MARKETING_PRODUCT));
        mThirdPartPaymentFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_my_movie_content, mThirdPartPaymentFragment).commitAllowingStateLoss();
    }

    @Override
    public void cancelOrderSuccess(CancelOrderResponse response, Bundle extraData)
    {

    }

    @Override
    public void cancelOrderFailed()
    {

    }

    @Override
    public void initPresenter() {
        mPayModePresenter = new PayModePresenter();
        mPayModePresenter.attachView(this);
    }

    @Override
    public void orderProductSuccess(OrderInfo response)
    {

    }

    @Override
    public void orderProductFailed()
    {

    }

    @Override
    public void unfinishedOrderExist(OrderInfo orderInfo)
    {

    }

    @Override
    public void checkUnfinishedOrderFailed()
    {

    }


    protected PopupWindow mNotificationPopup = null;

    protected void showPaymentSuccessPopWindow() {
        View successView = LayoutInflater.from(this).inflate(R.layout
                .window_pip_payment_success, null);
        TextView tvTips = (TextView) successView.findViewById(R.id.tv_payment_tips);
        tvTips.setText(getString(R.string.third_part_payment_success));
        mNotificationPopup = new PopupWindow(successView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        mNotificationPopup.showAtLocation(getPopupParentView(), Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = 1.0f;
        this.getWindow().setAttributes(lp);
        mNotificationPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Subscribe
    public void onEvent(FinishPlayUrlEvent event) {
        if (null !=  mNotificationPopup && mNotificationPopup.isShowing()) {
            mNotificationPopup.dismiss();
        }
    }


    private View getPopupParentView() {
        BaseFragment currentCheckedFragment = null;
        switch (checkedPayment) {
            case PAYMENT_PHONE_PAY:
                currentCheckedFragment = mPhonePayFragement;
                break;
            case PAYMENT_UNI_PAY:
                currentCheckedFragment =  mUniPayFragment;
                break;
            case PAYMENT_THIRD_PART_PAY:
                currentCheckedFragment =  mThirdPartPaymentFragment;
                break;
            case PAYMENT_BENEFIT:
                currentCheckedFragment = payBenefitFragment;
                break;
            case PAYMENT_SOCRE:
                currentCheckedFragment = mPointPaymentFragment;
                break;
            default:
        }
        View popupParentView = null;
        if (currentCheckedFragment != null) {
            popupParentView = currentCheckedFragment.findViewById(R.id.root_view);
        }
        return popupParentView;
    }

    private static final int GIFT_FINISH_CODE = 11100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GIFT_FINISH_CODE) {
            orderFinish();
        }
    }
}