package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.VerticalScrollTextView;
import com.pukka.ydepg.common.http.bean.node.XmppMessage;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.utils.CornersTransform;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.security.SHA256Util;
import com.pukka.ydepg.event.FinishPlayUrlEvent;
import com.pukka.ydepg.event.PlayUrlEvent;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.moudule.mytv.ZjYdUniAndPhonePayActivty;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.AvoidRepeatPaymentUtils;
import com.pukka.ydepg.moudule.mytv.presenter.PayBenefitPresenter;
import com.pukka.ydepg.moudule.mytv.utils.QRCodeGenerator;
import com.pukka.ydepg.moudule.player.node.CurrentChannelPlaybillInfo;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.view.DetailDataView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class NewPayBenefitFragment extends NewPaymentFragment implements DetailDataView
{

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

    private static final String TAG = NewPayBenefitFragment.class.getName();
    /*轮询任务 what*/
    private static final int PERIOD_TASK = 110;
    /*退出任务 what*/
    private static final int STOP_TASK = 111;
    /*轮询鉴权任务 what*/
    private static final int AUTH_TASK = 112;
    /*退出当前页面时间*/
    private int stop_time = 15 * 60 * 1000;
    /*轮询刷新二维码时间*/
    private int refresh_time = 3 * 60 * 1000;
    /*轮询鉴权时间*/
    private int auth_Time = 2 * 1000;
    /*界面 ImageView*/
    private ImageView pay_benefit;
    /*优惠券支付URL*/
    final String DEFAULT_URL = "http://aikanvod.miguvideo.com/video/p/share.jsp";
    /*产品信息*/
    private Product mProductInfo;
    /*VOD详情信息*/
    private VODDetail mVODDetail;
    /*宽带电视机顶盒登录账号*/
    private String userId;
    /*内容编号*/
    private String contentCode;
    /*内容名称*/
    private String contentName;
    /*产品ID*/
    private String productId;
    /*订购产品名称*/
    private String productName;
    /*订购产品价格*/
    private String productPrice;
    /*取终端参数配置轮询进行鉴权时间*/
    private static final String PAY_BENEFIT_AUTH_TIME = "pay_benefit_auth_time";
    /*取终端参数配置刷新二维码时间*/
    private static final String PAY_BENEFIT_REFRESH_TIME = "pay_benefit_refresh_time";
    /*取终端参数配置退出优惠券页面时间*/
    private static final String PAY_BENEFIT_STOP_TIME = "pay_benefit_stop_time";
    /*是否是VOD片源订购*/
    private boolean isVODSubscribe;
    /*是否回看节目单订购*/
    private boolean isTVODSubscribe;
    /*是否是预览订购*/
    private boolean isPreviewSubscribe;
    /*是否是甩屏片源*/
    private boolean isPushScreen;
    private XmppMessage mXmppMessage;
    /*是否是直播订购，参数类型为数组*/
    private String[] liveChannelSubscribe;

    private PayBenefitPresenter payBenefitPresenter;

    private BenefitHandler handler = new BenefitHandler(this);

    private static class BenefitHandler extends Handler {

        private WeakReference<NewPayBenefitFragment> mReference;

        BenefitHandler(NewPayBenefitFragment fragment) {
            this.mReference = new WeakReference<>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            if (null == mReference || null == mReference.get()){
                return;
            }
            switch (msg.what) {
                case PERIOD_TASK:
                    // TODO 刷新二维码
                    Log.d(TAG, "执行刷新二维码");
                    String time = (new Date().getTime() + mReference.get().refresh_time) + "";
                    String keyStr = mReference.get().getUserId() + mReference.get().getContentCode() + mReference.get().getContentName() + mReference.get().getProductId() + mReference.get().getProductName() + mReference.get().getProductPrice() + time + "ZJMobileAndTv";
                    String key = SHA256Util.Encrypt(keyStr, "SHA-256");
                    String qrcodeContent = mReference.get().getDEFAULT_URL()
                            + "?userId=" + mReference.get().getUserId()
                            + "&contentId=" + mReference.get().getContentCode()
                            + "&contentname=" + mReference.get().getContentName()
                            + "&productId=" + mReference.get().getProductId()
                            + "&productname=" + mReference.get().getProductName()
                            + "&productprice=" + mReference.get().getProductPrice()
                            + "&Time=" + Long.valueOf(time)
                            + "&Key=" + key;
                    int width = OTTApplication.getContext().getResources()
                            .getDimensionPixelSize(R.dimen.pay_benefit_qrcode_imageview_width);
                    Bitmap bitmap = QRCodeGenerator.genQrCode(qrcodeContent, width, width);
                    mReference.get().pay_benefit.setImageBitmap(bitmap);
                    mReference.get().handler.sendEmptyMessageDelayed(PERIOD_TASK,mReference.get().refresh_time);
                    break;
                case STOP_TASK:
                    Log.d(TAG, "执行退出程序");
                    if(null!=mReference.get().getActivity())
                    {
                        mReference.get().getActivity().finish();
                    }
                    break;
                case AUTH_TASK:
                    Log.d(TAG, "执行轮询鉴权任务");
                    if (mReference.get().isAccessAuth()) {
                        mReference.get().handler.removeMessages(AUTH_TASK);
                        mReference.get().authToPlay();
                    }else{
                        mReference.get().handler.sendEmptyMessageDelayed(AUTH_TASK,mReference.get().auth_Time);
                    }
                    break;
            }
        }
    };







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_benefit_new, container, false);
        EventBus.getDefault().register(this);
        pay_benefit = (ImageView) view.findViewById(R.id.pay_benefit);
        TextView pay_benefit_tip_install = (TextView) view.findViewById(R.id.pay_benefit_tip_install);
        TextView pay_benefit_tip_uninstall = (TextView) view.findViewById(R.id.pay_benefit_tip_uninstall);
        ImageView pay_benefit_migu_logo = (ImageView) view.findViewById(R.id.pay_benefit_migu_logo);
        setStyleText(pay_benefit_tip_install, getResources().getString(R.string.pay_benefit_install), 3, 7);
        setStyleText(pay_benefit_tip_uninstall, getResources().getString(R.string.pay_benefit_uninstall), 4, 8);
        RequestOptions options  = new RequestOptions()
                .transform(new CornersTransform(8));

        Glide.with(getContext()).load(R.drawable.migu_logo)
                .apply(options)
                .into(pay_benefit_migu_logo);

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


        initData();
        return view;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        auth_Time = SessionService.getInstance().getSession().getTerminalConfigurationValue(PAY_BENEFIT_AUTH_TIME) != null ?
                Integer.valueOf(SessionService.getInstance().getSession().getTerminalConfigurationValue(PAY_BENEFIT_AUTH_TIME)) : auth_Time;
        refresh_time = SessionService.getInstance().getSession().getTerminalConfigurationValue(PAY_BENEFIT_REFRESH_TIME) != null ?
                Integer.valueOf(SessionService.getInstance().getSession().getTerminalConfigurationValue(PAY_BENEFIT_REFRESH_TIME)) : refresh_time;
        stop_time = SessionService.getInstance().getSession().getTerminalConfigurationValue(PAY_BENEFIT_STOP_TIME) != null ?
                Integer.valueOf(SessionService.getInstance().getSession().getTerminalConfigurationValue(PAY_BENEFIT_STOP_TIME)) : stop_time;
        Log.d(TAG, "Time : auth_Time" + auth_Time + "refresh_time" + refresh_time + "stop_time" + stop_time);
        mProductInfo = JsonParse.json2Object(getArguments().getString(ZjYdUniAndPhonePayActivty
                .PRODUCT_INFO), Product.class);
        mVODDetail = JsonParse.json2Object(getArguments().getString(ZjYdUniAndPhonePayActivty
                .VOD_DETAIL), VODDetail.class);
        if(null!= AuthenticateManager.getInstance().getUserInfo()) {
            userId = AuthenticateManager.getInstance().getUserInfo().getUserId();
        }
        if (mProductInfo != null && mVODDetail != null) {
            contentCode = mVODDetail.getCode();
            contentName = mVODDetail.getName();
            productId = mProductInfo.getID();
            productName = mProductInfo.getName();
            productPrice = mProductInfo.getPrice();
        }
        isVODSubscribe = getArguments().getBoolean(ZjYdUniAndPhonePayActivty.ISVOD_SUBSCRIBE,
                false);
        isTVODSubscribe = getArguments().getBoolean(ZjYdUniAndPhonePayActivty
                .ISTVOD_SUBSCRIBE, false);
        isPreviewSubscribe = getArguments().getBoolean(ZjYdUniAndPhonePayActivty
                .IS_TRY_SEE_SUBSCRIBE, false);
        isPushScreen = getArguments().getBoolean(ZjYdUniAndPhonePayActivty
                .ISOFFSCREEN_SUBSCRIBE, false);
        liveChannelSubscribe = getArguments().getStringArray(ZjYdUniAndPhonePayActivty
                .CHANNELID_MEDIAID);
        if (getArguments().getSerializable(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE) != null) {
            mXmppMessage = (XmppMessage) getArguments().getSerializable(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE);
        }
        payBenefitPresenter = new PayBenefitPresenter((RxAppCompatActivity) getActivity());

        initProductInfo();
    }

    private void initProductInfo(){

        if (null == mProductInfo){
            return;
        }

        //产品包名
        String name = mProductInfo.getName();
        product_title_textview.setText(name);
        product_title_textview.setSelected(true);


        //产品包价格
        double price = Double.parseDouble(mProductInfo.getPrice()) / 100;
        product_price_textview.setText(decimalFormat.format(price));

        //非营销产品不展示折扣和原价和活动详情
        product_orgin_price_textview.setVisibility(View.GONE);
        product_orgin_price_textview.setText("");
        product_dicount_tip.setText("");
        product_dicount_tip.setVisibility(View.INVISIBLE);
        product_describe_textview.setVisibility(View.INVISIBLE);
        product_describe_textview.setText("");
        product_describe_tip.setVisibility(View.INVISIBLE);

//        //获取折扣信息
//        List<NamedParameter> listNp = mProductInfo.getCustomFields();
//        List<String> marketInfo = CommonUtil.getCustomNamedParameterByKey(listNp,cornreMarketInfo);
//        if (null != marketInfo && marketInfo.size()>0){
//            String str = marketInfo.get(0);
//            if (!TextUtils.isEmpty(str)){
//                product_dicount_tip.setVisibility(View.VISIBLE);
//                product_dicount_tip.setText(str);
//            }else{
//                product_dicount_tip.setText("");
//                product_dicount_tip.setVisibility(View.INVISIBLE);
//            }
//        }else{
//            product_dicount_tip.setText("");
//            product_dicount_tip.setVisibility(View.INVISIBLE);
//        }

//        //获取原价信息
//        List<String> orgPrice = CommonUtil.getCustomNamedParameterByKey(listNp,orgPriceKey);
//        if (null != orgPrice && orgPrice.size()>0){
//            String str = orgPrice.get(0);
//            if (!TextUtils.isEmpty(str)){
//                SpannableString spannableString = new SpannableString(str);
//                StrikethroughSpan colorSpan = new StrikethroughSpan();
//                spannableString.setSpan(colorSpan, 0, spannableString.length() -1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                product_orgin_price_textview.setVisibility(View.VISIBLE);
//                product_orgin_price_textview.setText(spannableString);
//            }else{
//                product_orgin_price_textview.setVisibility(View.GONE);
//                product_orgin_price_textview.setText("");
//            }
//        }else{
//            product_orgin_price_textview.setVisibility(View.GONE);
//            product_orgin_price_textview.setText("");
//        }

        /*
         * 产品类型,0 ：包周期;1 ：按次
         */
        if((!TextUtils.isEmpty(mProductInfo.getProductType()) && mProductInfo.getProductType().equals("1"))){
            product_prcie_mode_textview.setText("元/次");
        }else{
            product_prcie_mode_textview.setText("元/"+  StringUtils.analyticValidity(mProductInfo));
        }

//        //产品包描述
//        product_describe_textview.setText(mProductInfo.getIntroduce());
//        product_describe_textview.setAuto(true);
    }

    /**
     * 通知URL鉴权完成
     *
     * @param event event
     */
    @Subscribe
    public void onEvent(FinishPlayUrlEvent event) {
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }


    /**
     * 是否通过鉴权
     */
    private boolean isAccessAuth() {
        if (null != liveChannelSubscribe) {
            return payBenefitPresenter.passAuthPlayChannel(liveChannelSubscribe);
        } else {
            return payBenefitPresenter.passAuthPlayVOD(mVODDetail);
        }
    }

    /**
     * 修改指定位置的字符串样式
     *
     * @param tv    TextView
     * @param text  String
     * @param start int 起始位置
     * @param end   int 结束位置
     */
    private void setStyleText(TextView tv, String text, int start, int end) {
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.pay_benefit)), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(style);
    }


    /**
     * 鉴权播放
     */
    private void authToPlay() {
        Log.i(TAG, "authToPlay: 鉴权");
        //记录下产品的订购时间
        AvoidRepeatPaymentUtils.getInstance().recordPaymentTime(mProductInfo.getID());
        if (null != liveChannelSubscribe) {
            /*直播鉴权*/
            CurrentChannelPlaybillInfo playbillInfo = new CurrentChannelPlaybillInfo();
            playbillInfo.setChannelId(liveChannelSubscribe[0]);
            playbillInfo.setChannelMediaId(liveChannelSubscribe[1]);
            EventBus.getDefault().post(new PlayUrlEvent(playbillInfo));
        } else if (isVODSubscribe) {
            /*点播鉴权*/
            if (isPushScreen) {
                /*来自甩屏片源*/
                playPushScreen();
            } else {
                EventBus.getDefault().post(new PlayUrlEvent(true, false,mProductInfo==null?"":mProductInfo.getID()));
            }
        } else if (isTVODSubscribe) {
            /*回看鉴权*/
            EventBus.getDefault().post(new PlayUrlEvent(false, true,mProductInfo==null?"":mProductInfo.getID()));
        } else if (isPreviewSubscribe) {
            /*预览鉴权*/
            EventBus.getDefault().post(new PlayUrlEvent(false, false, true,mProductInfo==null?"":mProductInfo.getID()));
        }
    }

    /**
     * 来自甩屏内容进行甩屏播放
     */
    public void playPushScreen() {
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

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        SuperLog.debug(TAG,"onHiddenChanged->hidden:"+hidden+"|UserVisibleHint:"+getUserVisibleHint());
        if(getUserVisibleHint()){
            if(hidden){
                handler.removeMessages(AUTH_TASK);
                handler.removeMessages(STOP_TASK);
                handler.removeMessages(PERIOD_TASK);
            }else{
                Log.d(TAG, "Timer Start");
                handler.sendEmptyMessageDelayed(AUTH_TASK,auth_Time);
                /*轮询刷新二维码任务*/
                handler.sendEmptyMessage(PERIOD_TASK);
                /*停止并退出二维码页面任务*/
                handler.sendEmptyMessageDelayed(STOP_TASK,stop_time);
            }
        }
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
    public void executePay() { }

    @Override
    public VODDetail initVodDetail() {
        return null;
    }

    @Override
    public Product initProduct() {
        return null;
    }

    @Override
    public int initCheckedPayment() {
        return 0;
    }

    @Override
    public boolean istrySee() {
        return false;
    }

    @Override
    public boolean isOrdercenter() {
        return false;
    }

    @Override
    public Product getMarketProduct() {
        return null;
    }

    @Override
    public void showNoContent() { }

    @Override
    public void showError(String message) { }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public String getDEFAULT_URL() {
        return DEFAULT_URL;
    }

    public String getUserId() {
        return userId;
    }

    public String getContentCode() {
        return contentCode;
    }

    public String getContentName() {
        return contentName;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }
}
