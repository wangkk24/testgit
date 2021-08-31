package com.pukka.ydepg.moudule.mytv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.leanback.widget.BrowseFrameLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.BroadCastConstant;
import com.pukka.ydepg.common.extview.RetainCursorEditText;
import com.pukka.ydepg.common.http.bean.response.OrderProductResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.PaymentInfo;
import com.pukka.ydepg.common.http.v6bean.v6node.PriceObject;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Profile;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscribe;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SendSmsRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SubscribeProductRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.SubscribeProductResponse;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.TextUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.event.FinishPlayUrlEvent;
import com.pukka.ydepg.event.PlayUrlEvent;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.launcher.util.ThreadTool;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.livetv.adapter.BaseAdapter.OnItemListener;
import com.pukka.ydepg.moudule.mytv.adapter.PayKeyBoardAdapter;
import com.pukka.ydepg.moudule.mytv.presenter.PayPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.PayQrCodeLinkPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.UniPayPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.contract.PayContract;
import com.pukka.ydepg.moudule.mytv.presenter.contract.PayQrCodeLinkContract;
import com.pukka.ydepg.moudule.mytv.presenter.contract.UniPayContract;
import com.pukka.ydepg.moudule.player.node.CurrentChannelPlaybillInfo;
import com.pukka.ydepg.moudule.player.util.RemoteKeyEvent;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liudo on 2018/4/2.
 */

public class ZjYdUniAndPhonePayActivty extends BaseActivity implements UniPayContract.View, PayQrCodeLinkContract.View, View.OnClickListener, PayContract.View
        , OnItemListener, TextWatcher {


    private final String TAG = this.getClass().getName();

    public static final String BILL_ID = "BILL_ID";

    /**
     * 产品信息
     */
    public static final String PRODUCT_INFO = "product_info";

    /**
     * 支付账号
     */
    public static final String UNIPAY_BILLID = "billID";

    /**
     * 有效期
     */
    public static final String UNIPAY_EXPTIME = "exptime";

    /**
     * VOD详情
     */
    public static final String VOD_DETAIL = "VOD_DETAIL";

    /**
     * 是不是VOD订购?
     */
    public static final String ISVOD_SUBSCRIBE = "is_vod_subscribe";

    /**
     * 是不是试看订购?
     */
    public static final String IS_TRY_SEE_SUBSCRIBE = "is_try_see_subscribe";

    /**
     * 是不是TVOD订购?
     */
    public static final String ISTVOD_SUBSCRIBE = "is_tvod_subscribe";

    /**
     * 是不是甩屏?
     */
    public static final String ISOFFSCREEN_SUBSCRIBE = "is_offscreen_subscribe";
    /**
     * 是否从订购中心订购
     */
    public static final String ISORDERCENTER="is_order_center";

    /**
     * 甩屏信息
     */
    public static final String  XMPP_MESSAGE= "xmpp_message";


    /**
     * 标识符
     */
    public static final String PAY_TYPE = "product_pay_type";

    /**
     * 统一支付标识
     */
    public static final String UNIPAY_FLAG = "unipay_flag";


    /**
     * 统一支付手机验证标识
     */
    public static final String UNIPAY_PHONE_CODE_FLAG = "unipay_phone_code_flag";

    /**
     * 二维码标识
     */
    public static final String QRCODE_FLAG = "qrcode_flag";

    /**
     * 开通统一支付提示标识
     */
    public static final String UNONPENED_ACCOUNT = "unonpened_account";

    /**
     * 手机支付标识
     */
    public static final String PHONE_PAY_FLAG = "phone_pay_flag";

    /**
     * CHANNELID_MEDIAID
     */
    public static final String CHANNELID_MEDIAID = "channelid_mediaid";


    /**
     * phone_number_edit
     */
    public static final String PHONE_NUMBER_EDIT = "phone_number_edit";

    /**
     * verification_code_edit
     */
    public static final String VERIFICATION_CODE_EDIT = "verification_code_edit";

    /**
     * 统一支付标识
     */

    public static final String UNI_PAY_TYPE="uni_pay_type";
    /**
     * 统一支付二维码
     */
    public static final String UNI_CODE_MODE="uni_code_mode";
    /**
     * 开通统一支付提示语
     */
    public static final String UNONPENED_UNI_MODE="unonpened_uni_mode";
    /**
     * 统一支付普通模式
     */
    public static final String UNI_PHONE_PAY_MODE="uni_phone_pay_mode";
    /**
     * 统一支付儿童锁模式
     */
    public static final String UNI_CHILDREN_PAY_MODE="uni_children_pay_mode";


    public static final String HOTEL_MODE="hotel_mode";


    public static final String MONTH_PRODUCT_MODE="month_product_mode";

    public static  final String SHOW_ITEM_MODE="show_item_mode";

    public static  final String MARKETING_PRODUCT="marketingproduct";

    public static final String  UNSUBPRODINFOIDS="unsubProdInfoIds";

    /**
     * 是不是按次订购
     */
    public static final String IS_ORDER_BY_ORDER = "is_order_by_order";
    /**
     * 主布局
     */
    @BindView(R.id.root_view)
    BrowseFrameLayout root_view;


    /**
     * 包月poster总布局
     */
    @BindView(R.id.monthly_poster_ly)
    RelativeLayout monthly_poster_ly;


    /**
     * 包月poster
     */
    @BindView(R.id.monthly_img)
    ImageView monthly_img;


    /**
     * 包月价格
     */
    @BindView(R.id.monthly_tv_price)
    TextView monthly_tv_price;

    /**
     * 包月图片名字
     */
    @BindView(R.id.monthly_poster_name)
    TextView monthly_poster_name;

    /**
     * 按次poster总布局
     */
    @BindView(R.id.sequence_ly)
    FrameLayout sequence_ly;

    /**
     * 按次poster
     */
    @BindView(R.id.iv_poster)
    ImageView sequence_Img;


    /**
     * 按次价格
     */
    @BindView(R.id.sequence_price)
    TextView sequence_price;

    /**
     * 按次poster描述
     */
    @BindView(R.id.seuqence_desc)
    TextView seuquece_poster_desc;

    /**
     * 按次内容名称
     */
    @BindView(R.id.tv_valid_name)
    TextView nametv;

    /**
     * 有效期
     */
    @BindView(R.id.tv_valid_label)
    TextView validtv;

    /**
     * 描述信息
     */
    @BindView(R.id.tv_label_desc)
    TextView desctv;


    /**
     * 统一支付内容
     */
    @BindView(R.id.unicontent_ly)
    LinearLayout unicontent_ly;

    /**
     * 统一支付账户
     */
    @BindView(R.id.uni_subscriber)
    TextView uni_subscriber;


    /**
     * 确认支付
     */
    @BindView(R.id.btn_confirm_pay)
    Button confirmbtn;

    /**
     * 取消
     */
    @BindView(R.id.btn_close)
    Button colsebtn;


    /**
     * 二维码布局
     */
    @BindView(R.id.pay_qrcode)
    LinearLayout pay_qrcode_ly;


    /**
     * 二维码图片
     */
    @BindView(R.id.iv_unipay_qrcode)
    ImageView mQRCodeImageView;

    /**
     * 手机支付布局
     */
    @BindView(R.id.phonely)
    LinearLayout phonely;

    /**
     * 键盘
     */
    @BindView(R.id.rv_pay_keyboard)
    RecyclerView mPayKeyboardRecycleView;

    /**
     * 清空输入
     */
    @BindView(R.id.btn_clear)
    Button btn_clear;

    /**
     * 删除输入
     */
    @BindView(R.id.rl_delete)
    RelativeLayout rl_delete;


    /**
     * 手机号输入
     */
    RetainCursorEditText mInputNumber;

    /**
     * 获取验证码
     */
    @BindView(R.id.btn_verificationcode)
    Button mGetVerificationCode;


    /**
     * 重新获取验证码
     */
    @BindView(R.id.btn_resend_code)
    Button mResendCode;

    /**
     * 验证码输入
     */

    RetainCursorEditText input_verification;


    /**
     * 确认
     */
    @BindView(R.id.btn_phone_comfirm_pay)
    Button mConfirmPay;

    /**
     * 取消
     */
    @BindView(R.id.btn_phone_close)
    Button colsephonebtn;
    /**
     * poster中有效期
     */
    @BindView(R.id.seuqence_indate)
    TextView poster_validtv;

    /**
     * poster中有效期
     */
    @BindView(R.id.tv_product_price_hint)
    TextView price_hint;

    /**
     * UniPayPresenter
     */
    private UniPayPresenter uniPayPresenter;


    /**
     * 产品信息
     */
    private Product mProductInfo;

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
     * 是不是TVOD订购
     */
    private boolean isTVODSubscribe = false;

    /**
     * 频道ID和媒体资源ID
     */
    private String[] mCHANNELMEDIA_ID = null;


    /**
     * 付款成功弹窗
     */
    private PopupWindow mPaymentPopwindow;

    private Handler mHandler = new Handler();

    private boolean isClick = true;

    /**
     * 户主ID
     */
    private String mBillId = "";


    /**
     * 二维码有效期时间
     */
    private long mValidTime = 900 * 1000L;


    /**
     * 统一支付默认连接地址
     */
    private static final String DEFAULT_LINK_URL =
            "https://app.m.zj.chinamobile.com/zjweb/sjyytv4/kuandaiTVThirdPay/index.html";


    /**
     * PayQrCodeLinkPresenter
     */
    private PayQrCodeLinkPresenter PayQrCodemPresenter;


    /**
     * 订户ID
     */
    private String mUserID;

    /**
     * 键盘adapter
     */
    private PayKeyBoardAdapter mKeyBoardAdapter;


    /**
     * 支付presenter
     */
    private PayPresenter mPayPresenter = new PayPresenter();


    /**
     * profile信息
     */
    private Profile mProfile;

    /**
     * 倒计时开始时间
     */
    private static final int COUNT_DOWN_TIME = 59;

    /**
     * 输入的手机号码
     */
    private String mPhoneNumber;


    /**
     * 是否正在请求支付
     */
    private boolean isRequestSubscribe = false;

    private String payType;

    private String currentEditState = PHONE_NUMBER_EDIT;


    private boolean isInputNumberHasContent;

    private boolean isVericateHasContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uni_phone_pay);
        ButterKnife.bind(this);
        uniPayPresenter = new UniPayPresenter();
        uniPayPresenter.attachView(this);
        initDate();
    }

    private void initDate() {
        isVODSubscribe = getIntent().getBooleanExtra(ISVOD_SUBSCRIBE, false);
        isTVODSubscribe = getIntent().getBooleanExtra(ISTVOD_SUBSCRIBE, false);
        isTrySeeSubscribe = getIntent().getBooleanExtra(IS_TRY_SEE_SUBSCRIBE, false);
        mCHANNELMEDIA_ID = getIntent().getStringArrayExtra(CHANNELID_MEDIAID);
        mProductInfo = JsonParse.json2Object(getIntent().getStringExtra(PRODUCT_INFO), Product.class);
        mBillId = getIntent().getStringExtra(UNIPAY_BILLID);
        String vodDetailJson = getIntent().getStringExtra(VOD_DETAIL);
        payType = getIntent().getStringExtra(PAY_TYPE);
        if (!TextUtils.isEmpty(vodDetailJson)) {
            mVODDetail = JsonParse.json2Object(vodDetailJson, VODDetail.class);
        }
        if (null != mProductInfo) {
            setLeftLayoutShow(mProductInfo);
        }
        if (QRCODE_FLAG.equals(payType)) {
            qrcodeShow();
        } else if (UNIPAY_FLAG.equals(payType)) {
            uniPayShow();
        } else {
            phonePayShow();
        }

    }


    public void setLeftLayoutShow(Product mProductInfo) {
        String posterUrl = uniPayPresenter.resolveProductPoster(mVODDetail, mProductInfo);
        monthly_poster_ly.setVisibility(View.GONE);
        desctv.setVisibility(View.GONE);
        if (QRCODE_FLAG.equals(payType)) {
            validtv.setVisibility(View.VISIBLE);
            validtv.setText(getString(R.string.uni_qrcode_hint));
        } else {

            validtv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mProductInfo.getPrice())) {
            sequence_price.setText(String.valueOf(Double.valueOf(mProductInfo.getPrice()) / 100));
        }
        //按次
        if ("1".equals(mProductInfo.getProductType())) {
            if (!TextUtils.isEmpty(posterUrl)) {
                RequestOptions options  = new RequestOptions()
                        .placeholder(R.drawable.sequence_product_bg);
                Glide.with(this).load(posterUrl).apply(options).into(sequence_Img);
            }else{
                sequence_Img.setImageDrawable(getResources().getDrawable(R.drawable.sequence_product_bg));
            }
            if (null != mVODDetail) {
                seuquece_poster_desc.setText(mProductInfo.getName());
                poster_validtv.setText(getString(R.string.valide_hint) + getIntent().getStringExtra(UNIPAY_EXPTIME));
                nametv.setText(getString(R.string.name_hint) + mVODDetail.getName());

            }
            setTextViewColor(getResources().getColor(R.color.c39_color));
        } else {
            sequence_Img.setImageDrawable(getResources().getDrawable(R.drawable.month_product_bg));
            if (!TextUtils.isEmpty(posterUrl)) {
                RequestOptions options  = new RequestOptions()
                        .placeholder(R.drawable.month_product_bg);
                Glide.with(this).load(posterUrl).apply(options).into(sequence_Img);
            }
            seuquece_poster_desc.setText(mProductInfo.getName());
            poster_validtv.setText(getString(R.string.mouth_valide_hint));
            nametv.setText(getString(R.string.mouth_content_hint));
            setTextViewColor(getResources().getColor(R.color.c40_color));
        }

    }

    public void setTextViewColor(int color) {

        seuquece_poster_desc.setTextColor(color);
        poster_validtv.setTextColor(color);
        sequence_price.setTextColor(color);
        price_hint.setTextColor(color);

    }

    public void uniPayShow() {
        unicontent_ly.setVisibility(View.VISIBLE);
        pay_qrcode_ly.setVisibility(View.GONE);
        phonely.setVisibility(View.GONE);
        uni_subscriber.setText("【" + mBillId + "】");
        confirmbtn.requestFocus();

    }


    public void qrcodeShow() {
        PayQrCodemPresenter = new PayQrCodeLinkPresenter();
        PayQrCodemPresenter.attachView(this);
        unicontent_ly.setVisibility(View.GONE);
        pay_qrcode_ly.setVisibility(View.VISIBLE);
        phonely.setVisibility(View.GONE);
        //二维码有效期
        String validTime = SessionService.getInstance().getSession().getTerminalConfigurationValue("epg_bill_qrcode_validTime");
        if (!TextUtils.isEmpty(validTime)) {
            mValidTime = Long.parseLong(validTime) * 1000;
        }
        PayQrCodemPresenter.querySubscriberInfo(new QueryUniInfoRequest(), this);
    }

    public void phonePayShow() {
        unicontent_ly.setVisibility(View.GONE);
        pay_qrcode_ly.setVisibility(View.GONE);
        phonely.setVisibility(View.VISIBLE);
        mInputNumber= (RetainCursorEditText) findViewById(R.id.et_input_number);
        input_verification= (RetainCursorEditText) findViewById(R.id.et_input_verification);
        TextUtil.setEditTextFilter(mInputNumber, 11);
        mInputNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        input_verification.setInputType(InputType.TYPE_CLASS_NUMBER);
        TextUtil.setEditTextFilter(input_verification, 6);
        //添加文本监听
        mInputNumber.addTextChangedListener(this);
        setFocusChangeListener(mInputNumber,input_verification,mResendCode,mGetVerificationCode,mConfirmPay);
        mPayPresenter.attachView(this);
        mPayKeyboardRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mKeyBoardAdapter = new PayKeyBoardAdapter(this, new ArrayList<>(),rl_delete);
        mPayKeyboardRecycleView.setAdapter(mKeyBoardAdapter);
        mKeyBoardAdapter.setOnItemListener(this);
        mPayPresenter.generateProductInfo(getIntent().getStringExtra(PRODUCT_INFO));
        mPayPresenter.generateKeyboard();
        mInputNumber.setFocusable(true);
        mInputNumber.requestFocus();
        mGetVerificationCode.setNextFocusUpId(R.id.btn_verificationcode);
        mGetVerificationCode.setNextFocusRightId(R.id.btn_verificationcode);
        mResendCode.setNextFocusUpId(R.id.btn_resend_code);
        mResendCode.setNextFocusRightId(R.id.btn_resend_code);
        colsephonebtn.setNextFocusRightId(R.id.btn_phone_close);
        colsephonebtn.setNextFocusLeftId(R.id.btn_phone_close);
        input_verification.setNextFocusRightId(R.id.et_input_verification);
        mInputNumber.setNextFocusRightId(R.id.et_input_number);
        int payBtnTextColor = mConfirmPay.getCurrentTextColor();
        mConfirmPay.setFocusable(false);
        switchButtonTextColor(payBtnTextColor, R.color.c24_color, mConfirmPay);
        mInputNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (KeyEvent.ACTION_DOWN == keyEvent.getAction()) {
                    int codeValue = RemoteKeyEvent.getInstance().getVODKeyCodeValue(keyCode);
                    if(codeValue == RemoteKeyEvent.MEDIA_PAUSE_PLAY||keyCode==KeyEvent.KEYCODE_DPAD_UP){
                        mPayKeyboardRecycleView.requestFocus();
                        mInputNumber.startRetainCursor();
                        currentEditState = PHONE_NUMBER_EDIT;
                        return true;
                    }

                }
                return false;
            }
        });
        input_verification.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (KeyEvent.ACTION_DOWN == keyEvent.getAction()) {
                    int codeValue = RemoteKeyEvent.getInstance().getVODKeyCodeValue(keyCode);
                    if(codeValue == RemoteKeyEvent.MEDIA_PAUSE_PLAY){
                        mPayKeyboardRecycleView.requestFocus();
                        input_verification.startRetainCursor();
                        currentEditState = VERIFICATION_CODE_EDIT;
                        return true;
                    }

                }
                return false;
            }
        });
        colsephonebtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isHasFocus) {
                if(isHasFocus){
                    cancelRetainCursor();
                    if(mConfirmPay.isFocusable()){
                        colsephonebtn.setNextFocusLeftId(R.id.btn_phone_comfirm_pay);
                    }else {
                        colsephonebtn.setNextFocusLeftId(R.id.btn_phone_close);
                    }
                }

            }
        });

        input_verification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                input_verification.invalidate();
                 int inputVerificationlength=  input_verification.getText().length();
                int inputMaxLength = TextUtil.getEditTextMaxLength(input_verification);
                int payBtnTextColor = mConfirmPay.getCurrentTextColor();
              if(inputVerificationlength<inputMaxLength){
                  mConfirmPay.setFocusable(false);
                  switchButtonTextColor(payBtnTextColor, R.color.c24_color, mConfirmPay);
                  isVericateHasContent=false;
              }else{
                  isVericateHasContent=true;
                  if(isVericateHasContent&&isInputNumberHasContent){
                      mConfirmPay.setFocusable(true);
                      switchButtonTextColor(payBtnTextColor, R.color.c21_color, mConfirmPay);
                  }
              }

            }
        });
        rl_delete.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
                if (KeyEvent.ACTION_DOWN == keyEvent.getAction()) {
                    int codeValue = RemoteKeyEvent.getInstance().getVODKeyCodeValue(keyCode);
                    if(codeValue == RemoteKeyEvent.VOD_FAST_FORWARD){
                        mPayKeyboardRecycleView.requestFocus();
                        return true;
                    }

                }
                return false;
            }
        });
    }

    public void cancelRetainCursor(){
        mInputNumber.stopRetainCursor();
        input_verification.stopRetainCursor();
    }

    public void setFocusChangeListener(View... views){
        for (View v : views) {
            v.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        cancelRetainCursor();
                    }
                }
            });
        }

    }

    @Override
    @OnClick({R.id.btn_confirm_pay, R.id.btn_phone_close, R.id.btn_close, R.id.btn_clear, R.id.rl_delete, R.id.btn_resend_code, R.id.btn_verificationcode,R.id.btn_phone_comfirm_pay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_pay:
                if (!isClick) {
                    return;
                }
                isClick = false;
                //支付
                SubscribeProductRequest request = new SubscribeProductRequest();
                Subscribe subscribe = new Subscribe();
                //产品ID
                subscribe.setProductID(mProductInfo.getID());
                //包月要求支持续订
                if (uniPayPresenter.isAutoExtend(mProductInfo)) {
                    subscribe.setIsAutoExtend(Subscribe.IsAutoExtend.RENEW);
                } else {
                    subscribe.setIsAutoExtend(Subscribe.IsAutoExtend.NO_RENEW);
                }
      /*
       * 产品类型,0 ：包周期;1 ：按次
       */
                if (!TextUtils.isEmpty(mProductInfo.getProductType())) {
                    if (mProductInfo.getProductType().equals("1")) {
                        //按次产品,参考C40接口文档
                        List<PriceObject> priceObjList = new ArrayList<>();
                        PriceObject pb = mProductInfo.getPriceObject();
                        if(null!=mVODDetail) {
                            if(null!=pb) {
                                pb.setID(mVODDetail.getID());
                            }else{
                                pb=new PriceObject();
                                pb.setID(mVODDetail.getID());
                                pb.setContentType("VOD");
                            }
                        }
                        priceObjList.add(pb);
                        subscribe.setPriceObjects(priceObjList);
                    }
                }
                //支付信息
                subscribe.setPayment(new PaymentInfo());
                request.setSubscribe(subscribe);
                request.setType("0");
                uniPayPresenter.payment(request, this);
                break;
            case R.id.btn_close:
            case R.id.btn_phone_close:
                finish();
                break;
            case R.id.btn_clear:
                //清空
                if (currentEditState.equals(PHONE_NUMBER_EDIT) ) {
                    mInputNumber.setText("");
                } else {
                    input_verification.setText("");
                }
                break;
            case R.id.rl_delete:
                //删除,逐字删除
                if (currentEditState.equals(PHONE_NUMBER_EDIT)) {
                    int index = mInputNumber.getSelectionStart();
                    Editable editable = mInputNumber.getText();
                    if(index-1>=0) {
                        editable.delete(index - 1, index);
                    }

                } else {
                    int index = input_verification.getSelectionStart();
                    Editable editable = input_verification.getText();
                    if(index-1>=0) {
                        editable.delete(index - 1, index);
                    }
                }

                break;
            case R.id.btn_resend_code:
                //重新发送验证码
                String number = mInputNumber.getText().toString();
                if (!TextUtil.isIllegalPhoneNumber(number)) {
                    //请输入正确的移动手机号码
                    EpgToast.showLongToast(this, getString(R.string.input_phonenumber_error));
                    return;
                }
                mPhoneNumber = number;
                if (mResendCode.getText().toString().equals(getString(R.string.order_pay_resend_code))) {
                    //倒计时
                    mPayPresenter.countDown(COUNT_DOWN_TIME - 1);
                }
                input_verification.setFocusable(true);
                input_verification.requestFocus();
//                int payBtnColor = mConfirmPay.getCurrentTextColor();
//                mConfirmPay.setFocusable(true);
//                switchButtonTextColor(payBtnColor, R.color.c21_color, mConfirmPay);
//
                break;
            case R.id.btn_verificationcode:
                //获取验证码
                String phoneNumber = mInputNumber.getText().toString();
                if (!TextUtil.isIllegalPhoneNumber(phoneNumber)) {
                    //请输入正确的移动手机号码
                    EpgToast.showLongToast(this, getString(R.string.input_phonenumber_error));
                    return;
                }
                mPhoneNumber = phoneNumber;
                //发送验证码按钮不可见
                mGetVerificationCode.setVisibility(View.INVISIBLE);
                //展示支付按钮和重新发送验证码按钮
                mResendCode.setVisibility(View.VISIBLE);
                //倒计时
                mPayPresenter.countDown(COUNT_DOWN_TIME - 1);
                input_verification.setFocusable(true);
                input_verification.requestFocus();
//                int payBtnTextColor = mConfirmPay.getCurrentTextColor();
//                mConfirmPay.setFocusable(true);
//                switchButtonTextColor(payBtnTextColor, R.color.c21_color, mConfirmPay);

                break;
            case R.id.btn_phone_comfirm_pay:
                if (isRequestSubscribe) return;
                //确认支付
                if (TextUtils.isEmpty(mInputNumber.getText().toString())) {
                    //请输入验证码
                    EpgToast.showToast(ZjYdUniAndPhonePayActivty.this,
                            getString(R.string.input_verificationcode_hint));
                    return;
                }
                //请求失败或者请求成功再重新reset标识符
                isRequestSubscribe = true;
                //支付
                payment();
                break;
        }
    }

    /**
     * 支付
     */
    private void payment() {
        SubscribeProductRequest request = new SubscribeProductRequest();
        //输入的短信验证码
        request.setCorrelator(input_verification.getText().toString());
        //订购验证类型,短信验证码方式订购
        request.setType(SubscribeProductRequest.TYPE.SMS_CODE);
        Subscribe subscribe = new Subscribe();
        //是否自动续订,不自动续订
        subscribe.setIsAutoExtend(Subscribe.IsAutoExtend.NO_RENEW);
        //付款账号
        subscribe.setPaidUserID(mPhoneNumber);
        subscribe.setProductID(mProductInfo.getID());
        if (!TextUtils.isEmpty(mProductInfo.getProductType())) {
            /**
             * 产品类型,0 ：包周期;1 ：按次
             */
            if (mProductInfo.getProductType().equals("1")) {
                List<PriceObject> priceObjList = new ArrayList<>();
                priceObjList.add(mProductInfo.getPriceObject());
                subscribe.setPriceObjects(priceObjList);
            }
        }
        //支付信息
        subscribe.setPayment(new PaymentInfo());
        request.setSubscribe(subscribe);
        //支付
        mPayPresenter.payment(request, this);
    }

    @Override
    public void onPaymentSucc(SubscribeProductResponse response) {
        //立刻发起心跳
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadCastConstant.Heartbit.COM_HUAWEI_OTT_START_HEARTBIT));
        //支付成功弹框提示
        showPaymentSuccPopWindow();
        //发送鉴权消息
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SuperLog.info2SD(TAG, "send PlayUrlEvent >>> CHANNELMEDIA_ID:" + (null != mCHANNELMEDIA_ID) + "|isVODSubscribe:" + isVODSubscribe + "|isTVODSubscribe:" + isTVODSubscribe + "|isTrySeeSubscribe:" + isTrySeeSubscribe);
                if (null != mCHANNELMEDIA_ID) {
                    CurrentChannelPlaybillInfo playbillInfo = new CurrentChannelPlaybillInfo();
                    playbillInfo.setChannelId(mCHANNELMEDIA_ID[0]);
                    playbillInfo.setChannelMediaId(mCHANNELMEDIA_ID[1]);
                    //直播鉴权
                    EventBus.getDefault().post(new PlayUrlEvent(playbillInfo));
                } else if (isVODSubscribe) {
                    //VOD鉴权
                    EventBus.getDefault().post(new PlayUrlEvent(true, false,mProductInfo==null?"":mProductInfo.getID()));
                } else if (isTVODSubscribe) {
                    //回看鉴权
                    EventBus.getDefault().post(new PlayUrlEvent(false, true,mProductInfo==null?"":mProductInfo.getID()));
                } else if (isTrySeeSubscribe) {
                    EventBus.getDefault().post(new PlayUrlEvent(false, false, true,mProductInfo==null?"":mProductInfo.getID()));
                }
            }
        }, 2000);
    }

    @Override
    public void onPaymentSucc(OrderProductResponse response)
    {
        //立刻发起心跳
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadCastConstant.Heartbit.COM_HUAWEI_OTT_START_HEARTBIT));
        //支付成功弹框提示
        showPaymentSuccPopWindow();
        //发送鉴权消息
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SuperLog.info2SD(TAG, "send PlayUrlEvent >>> CHANNELMEDIA_ID:" + (null != mCHANNELMEDIA_ID) + "|isVODSubscribe:" + isVODSubscribe + "|isTVODSubscribe:" + isTVODSubscribe + "|isTrySeeSubscribe:" + isTrySeeSubscribe);
                if (null != mCHANNELMEDIA_ID) {
                    CurrentChannelPlaybillInfo playbillInfo = new CurrentChannelPlaybillInfo();
                    playbillInfo.setChannelId(mCHANNELMEDIA_ID[0]);
                    playbillInfo.setChannelMediaId(mCHANNELMEDIA_ID[1]);
                    //直播鉴权
                    EventBus.getDefault().post(new PlayUrlEvent(playbillInfo));
                } else if (isVODSubscribe) {
                    //VOD鉴权
                    EventBus.getDefault().post(new PlayUrlEvent(true, false,mProductInfo==null?"":mProductInfo.getID()));
                } else if (isTVODSubscribe) {
                    //回看鉴权
                    EventBus.getDefault().post(new PlayUrlEvent(false, true,mProductInfo==null?"":mProductInfo.getID()));
                } else if (isTrySeeSubscribe) {
                    EventBus.getDefault().post(new PlayUrlEvent(false, false, true,mProductInfo==null?"":mProductInfo.getID()));
                }
            }
        }, 2000);
    }

    @Override
    public void onPaymentFailed() {
        isClick = true;
        isRequestSubscribe=false;

    }

    @Override
    public void updateUserRegInfoSucess()
    {

    }

    @Override
    public void updateUserRegInfoFail()
    {

    }

    @Override
    public void onResultQRCode(Bitmap resultBitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mQRCodeImageView.setImageBitmap(resultBitmap);
            }
        });
        SuperLog.debug(TAG, "[onResultQRCode] >>>>> start Timer");
        //启动定时器
        Observable.timer(mValidTime, TimeUnit.SECONDS)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .unsubscribeOn(Schedulers.io()).subscribe(new RxCallBack<Long>(this) {
            @Override
            public void onSuccess(@NonNull Long aLong) {
                if (null != PayQrCodemPresenter) {
                    SuperLog.debug(TAG, "start generateQrCode");
                    PayQrCodemPresenter.generateQrCode(DEFAULT_LINK_URL, mUserID, mProductInfo != null ? mProductInfo.getName() : "");
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
                if (BILL_ID.equals(namedParameter.getKey()) && !CollectionUtil.isEmpty(
                        namedParameter.getValues())) {
                    mUserID = namedParameter.getValues().get(0);
                    SuperLog.debug(TAG, "userId=" + mUserID);
                    //生成二维码
                    if (null != PayQrCodemPresenter) {
                        PayQrCodemPresenter.generateQrCode(DEFAULT_LINK_URL, mUserID, mProductInfo != null ? mProductInfo.getName() : "");
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onQuerySubscriberInfoError() {

    }

    @Override
    protected void onDestroy() {
        Drawable codeDrawable = mQRCodeImageView.getDrawable();
        if (QRCODE_FLAG.equals(payType) && null != codeDrawable && codeDrawable instanceof BitmapDrawable) {
            SuperLog.debug(TAG, "QrCode Bitmap recycle");
            ((BitmapDrawable) codeDrawable).getBitmap().recycle();
        }
        super.onDestroy();
        if (null != PayQrCodemPresenter) {
            PayQrCodemPresenter.detachView();
        }
        if (null != uniPayPresenter) {
            uniPayPresenter.detachView();
        }
        if (null != mPaymentPopwindow) {
            mPaymentPopwindow.dismiss();
        }
    }

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


    /**
     * 展示付款成功提示窗
     */
    private void showPaymentSuccPopWindow() {
        View successView = LayoutInflater.from(this).inflate(R.layout.window_pip_payment_success, null);
        TextView tvTips = (TextView) successView.findViewById(R.id.tv_payment_tips);
        //显示户主信息
        if(UNIPAY_FLAG.equals(payType)||QRCODE_FLAG.equals(payType)) {
            tvTips.setText(String.format(getString(R.string.payment_success_tips), mBillId));
        }else{
            tvTips.setText("支付成功");
        }
        mPaymentPopwindow = new PopupWindow(successView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPaymentPopwindow.showAtLocation(root_view, Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
        mPaymentPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public void onItemSelect(String viewType, int position) {

    }

    @Override
    public void onItemClickListener(String viewType, int position) {
        if (currentEditState.equals(PHONE_NUMBER_EDIT)) {
            int index = mInputNumber.getSelectionStart();
            Editable editable = mInputNumber.getText();
            editable.insert(index, mKeyBoardAdapter.getItemPosition(position));
        } else {
            int index = input_verification.getSelectionStart();
            Editable editable = input_verification.getText();
            editable.insert(index, mKeyBoardAdapter.getItemPosition(position));
        }
    }


    @Override
    public void onResultProductInfo(Product product) {
        List<Profile> profileList = SessionService.getInstance().getSession().getProfileList();
        if (profileList != null && profileList.size() != 0) {
            mProfile = profileList.get(0);
        }

    }

    @Override
    public void onResultKeyBoardValue(List<String> keyboardList) {
        mKeyBoardAdapter.bindData(keyboardList);
    }

    @Override
    public void onCountDownStart() {
        //切换到主线程
        ThreadTool.switchMainThread(new Action() {
            @Override
            public void run() throws Exception {
                //正在倒计时重新发送,先不让按钮获取到焦点,倒计时结束,再重新设置可以获取到焦点;
                mResendCode.setFocusable(false);
                mInputNumber.setNextFocusRightId(R.id.et_input_number);
                mResendCode.setTextColor(getResources().getColor(R.color.c24_color));
                mResendCode.setText(
                        String.format(getResources().getString(R.string.order_pay_sendverification_code),
                                String.valueOf(COUNT_DOWN_TIME)));
                if (null != mProfile) {
                    //发送短信验证码.....
                    SendSmsRequest request = new SendSmsRequest();
                    request.setLoginName(mProfile.getLoginName());
                    request.setDestMobilePhone(mInputNumber.getText().toString());
                    request.setMsgType(SendSmsRequest.MsgType.SMS_SUBSCRIBE_CODE);
                    mPayPresenter.sendSMS(request, ZjYdUniAndPhonePayActivty.this);
                }
            }
        });
    }

    @Override
    public void onCountDownFinish() {
        //倒计时完成
        mResendCode.setText(getString(R.string.order_pay_resend_code));
        mResendCode.setFocusable(true);//焦点可以获取
        mInputNumber.setNextFocusRightId(R.id.btn_resend_code);
        mResendCode.setTextColor(getResources().getColor(R.color.c21_color));
    }

    @Override
    public void onCountDownProgressUpdate(Long progress) {
        //倒计时数字更新......
        mResendCode.setText(
                String.format(getResources().getString(R.string.order_pay_sendverification_code),
                        String.valueOf(progress)));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        mInputNumber.invalidate();
        //输入框文字当前长度
        int inputLength = mInputNumber.getText().length();
        //输入框允许输入的最大长度
        int inputMaxLength = TextUtil.getEditTextMaxLength(mInputNumber);
        //获取验证码按钮,可见状态
        int verificationCodeVisibility = mGetVerificationCode.getVisibility();
        //按钮当前文本颜色
        int payBtnTextColor = mConfirmPay.getCurrentTextColor();
        int verificationBtnTextColor = mGetVerificationCode.getCurrentTextColor();
        if (inputLength < inputMaxLength) {//判断当前输入的文本长度
            //获取验证码按钮当前可见,当前文本框内容长度不够
            //设置不允许获取焦点
            if (verificationCodeVisibility == View.VISIBLE) {
                mGetVerificationCode.setFocusable(false);
                switchButtonTextColor(verificationBtnTextColor, R.color.c24_color, mGetVerificationCode);
            }
            mConfirmPay.setFocusable(false);
            switchButtonTextColor(payBtnTextColor, R.color.c24_color, mConfirmPay);
            mInputNumber.setNextFocusRightId(R.id.et_input_number);
            isInputNumberHasContent=false;
        } else if (inputLength == inputMaxLength) {//达到长度标准
            //按钮状态可见的,设置该按钮当前焦点可以获取,同时修改按钮颜色
            if (verificationCodeVisibility == View.VISIBLE) {
                mGetVerificationCode.setFocusable(true);
                mInputNumber.setNextFocusRightId(R.id.btn_verificationcode);
                switchButtonTextColor(verificationBtnTextColor, R.color.c21_color, mGetVerificationCode);
            }
            isInputNumberHasContent=true;
            if(isInputNumberHasContent&&isVericateHasContent){
                mConfirmPay.setFocusable(true);
                switchButtonTextColor(payBtnTextColor, R.color.c21_color, mConfirmPay);
            }
        }
    }

    /**
     * 调整按钮颜色
     *
     * @param currentTextColor
     * @param destResId
     * @param button
     */
    private void switchButtonTextColor(int currentTextColor, int destResId, Button button) {
        int destTextColor = getResources().getColor(destResId);
        if (currentTextColor != destTextColor) {//不一致再设置
            button.setTextColor(destTextColor);
        }
    }

}
