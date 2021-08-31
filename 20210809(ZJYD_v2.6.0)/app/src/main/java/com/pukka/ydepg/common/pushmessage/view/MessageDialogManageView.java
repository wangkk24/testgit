package com.pukka.ydepg.common.pushmessage.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.ImageViewExt;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;
import com.pukka.ydepg.common.extview.TextViewExt;
import com.pukka.ydepg.common.extview.VerticalScrollTextView;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.pushmessage.PushMessageUtils;
import com.pukka.ydepg.common.pushmessage.presenter.PushMessagePresenter;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Tvms;
import com.pukka.ydepg.common.report.ubd.scene.UBDPushMessage;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.event.MessageEvent;
import com.pukka.ydepg.launcher.bean.node.Topic;
import com.pukka.ydepg.launcher.ui.activity.TopicActivity;
import com.pukka.ydepg.launcher.ui.fragment.WebActivity;
import com.pukka.ydepg.launcher.view.topic.TopicViewManager;
import com.pukka.ydepg.moudule.featured.view.TopicService;
import com.pukka.ydepg.moudule.home.view.MarqueeText;
import com.pukka.ydepg.moudule.livetv.utils.LiveTVCacheUtil;
import com.pukka.ydepg.moudule.livetv.utils.LiveUtils;
import com.pukka.ydepg.moudule.mytv.bean.BodyContentBean;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.VodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.VodMainActivity;
import com.pukka.ydepg.xmpp.bean.XmppSuccessEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pukka.ydepg.common.utils.JsonParse.json2Object;

/**
 * Created by Eason on 2018/5/14.
 * MessageDialog ManageView
 */
public class MessageDialogManageView extends RelativeLayout implements View.OnClickListener {

    /*
    * 文本信息展示
    * */
    @BindView(R.id.content_text_tv)
    VerticalScrollTextView mContentTextTv;

    /*
    * 文本显示关闭按钮
    * */
    @BindView(R.id.ok_text_tv)
    TextView mOkTextTv;

    /*
    * 文本展示根布局
    * */
    @BindView(R.id.text_rela)
    RelativeLayout mTextRela;

    /*
    * 跳转播放海报
    * */
    @BindView(R.id.image_view)
    ImageView mImageView;

    /*
    * 跳转播放标题
    * */
    @BindView(R.id.title_tv)
    TextView mTitleTv;

    /*
    * 跳转播放详情介绍
    * */
    @BindView(R.id.content_play_tv)
    VerticalScrollTextView mContentPlayTv;

    /*
    * 跳转播放取消按钮
    * */
    @BindView(R.id.cancel_play_tv)
    TextView mCancelPlayTv;

    /*
    * 跳转播放跳转按钮
    * */
    @BindView(R.id.play_tv)
    TextView mPlayTv;

    /*
    * 跳转播放根布局
    * */
    @BindView(R.id.play_rela)
    LinearLayout mPlayRela;

    private Activity mRxAppCompatActivity;

    private MessageDialog mMessageDialog;

    private CountDownTimer countDownTimer;

    /*
    * 消息体
    * */
    private String mBody = "";

    /*
    * 消息展示类型mode
    * */
    public static final String MODE = "mode";

    /*
    * 消息内容
    * */
    public static final String CONTENT = "content";

    /*
    * 命令消息体
    * */
    private BodyContentBean mBodyContentBean = null;
    private BodyContentBean.ExtensionInfo extensionInfo = null;

    public static final String TAG = "MessageDialogManageView";

    private boolean isClickCloseDialog = false;

    public MessageDialogManageView(Context context) {
        super(context);
    }

    public MessageDialogManageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageDialogManageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //映射完成
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    //将视图依附到容器
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //initData();
        initOnClick();
    }


    //视图从容器中销毁
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != countDownTimer){
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    public void setRxAppCompatActivity(Activity rxAppCompatActivity,MessageDialog messageDialog) {
        this.mRxAppCompatActivity = rxAppCompatActivity;
        this.mMessageDialog = messageDialog;
    }

    public void setBody(String body) {
        this.mBody = body;
        initData(body);
    }

    private void initData(String body) {
        try {
            //设置默认透明度
            this.getBackground().mutate().setAlpha(100);
            JSONObject object = new JSONObject(body);
            if (!TextUtils.isEmpty(object.getString(MODE)) && object.getString(MODE).equals("6")) {
                //普通文本消息
                mTextRela.setVisibility(View.VISIBLE);
                mPlayRela.setVisibility(View.GONE);
                mOkTextTv.requestFocus();
                setTextInfo(object.getString(CONTENT));
            } else if (!TextUtils.isEmpty(object.getString(MODE)) && object.getString(MODE).equals("5")) {
                //命令通知消息
                mTextRela.setVisibility(View.GONE);
                //---
                //mPlayRela.setVisibility(View.VISIBLE);
                //mPlayTv.requestFocus();
                //setTextPlayInfo(mBodyContentBean);
                //---
                //2.4优化xmpp
                mBodyContentBean = JsonParse.json2Object(body, BodyContentBean.class);
                if (null != mBodyContentBean){
                    extensionInfo = json2Object(mBodyContentBean.getContent().getExtensionInfo(), BodyContentBean.ExtensionInfo.class);
                }
                //设置透明度
                setAlpha();

                if (isPicMessage()){
                    //运营消息
                    addViewForPic();
                }else{
                    //运营消息的文本消息
                    addViewForTxt();
                }

            } else if (!TextUtils.isEmpty(object.getString(MODE)) && object.getString(MODE).equals("0")) {
                //滚动消息模式
                mTextRela.setVisibility(View.VISIBLE);
                mPlayRela.setVisibility(View.GONE);
                mOkTextTv.requestFocus();
                setTextInfo(object.getString(CONTENT));
            }
        } catch (Exception e) {
            SuperLog.error(TAG,e);
        }
    }

    private void setAlpha() {
        if (null != extensionInfo){
            this.getBackground().mutate().setAlpha(255*Integer.parseInt(extensionInfo.getOpaque())/100);
        }
    }

    //运营消息中不含有海报的消息当作文本消息处理
    private boolean isPicMessage(){
        if (null != extensionInfo && !TextUtils.isEmpty(extensionInfo.getPostURL())) {
            return true;
        }
        SuperLog.info2SDDebug(TAG,"运营消息中不含有海报的消息当作文本消息处理");
        return false;
    }

    //添加运营消息View
    private void addViewForPic(){
        RelativeLayoutExt view = (RelativeLayoutExt) LayoutInflater.from(mRxAppCompatActivity).inflate(R.layout.dialog_message_pic, this,false);

        RelativeLayoutExt play_rela = view.findViewById(R.id.play_rela);
        VerticalScrollTextView content_play_tv = view.findViewById(R.id.content_play_tv);
        ImageViewExt image_view = view.findViewById(R.id.image_view);
        MarqueeText title_tv = view.findViewById(R.id.title_tv);
        TextViewExt tv_countdown = view.findViewById(R.id.tv_countdown);
        TextViewExt cancel_play_tv = view.findViewById(R.id.cancel_play_tv);
        TextViewExt play_tv = view.findViewById(R.id.play_tv);
        play_tv.requestFocus();
        cancel_play_tv.setOnClickListener(this);
        play_tv.setOnClickListener(this);

        play_tv.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    play_tv.setTextColor(getResources().getColor(R.color.white_0));
                }else{
                    play_tv.setTextColor(getResources().getColor(R.color.color_dbdad6));
                }
            }
        });
        cancel_play_tv.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    cancel_play_tv.setTextColor(getResources().getColor(R.color.white_0));
                }else{
                    cancel_play_tv.setTextColor(getResources().getColor(R.color.color_dbdad6));
                }
            }
        });

        setViewData(mBodyContentBean,title_tv,content_play_tv,play_tv,cancel_play_tv,image_view,tv_countdown,(LayoutParams) play_rela.getLayoutParams());

        addView(view);
    }

    //添加运营消息中的文本消息
    private void addViewForTxt(){
        RelativeLayoutExt view = (RelativeLayoutExt) LayoutInflater.from(mRxAppCompatActivity).inflate(R.layout.dialog_message_txt, this,false);

        RelativeLayoutExt rela_content = view.findViewById(R.id.rela_content);
        MarqueeText title_tip = view.findViewById(R.id.title_tip);
        VerticalScrollTextView content_tip = view.findViewById(R.id.content_tip);
        TextViewExt tv_countdown = view.findViewById(R.id.tv_countdown);
        ImageViewExt iv_skip = view.findViewById(R.id.iv_skip);
        iv_skip.requestFocus();
        iv_skip.setOnClickListener(this);

        /*RelativeLayout.LayoutParams params = (LayoutParams) rela_content.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_IN_PARENT);*/

        setViewData(mBodyContentBean,title_tip,content_tip,null,null,null,tv_countdown,(LayoutParams) rela_content.getLayoutParams());

        addView(view);
    }

    //设置dialog显示的位置
    private void setParams(RelativeLayout.LayoutParams params,String location){
        //枚举值：1:左上，2:右上，3:左下，4:右下，5:中间
        if (location.equalsIgnoreCase("1")){
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }else if (location.equalsIgnoreCase("2")){
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }else if (location.equalsIgnoreCase("3")){
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }else if (location.equalsIgnoreCase("4")){
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }else if (location.equalsIgnoreCase("5")){
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
        }
    }

    //2.4xmpp优化后  开始倒计时
    private void startCountDownTimer(TextViewExt tv_countdown,String theretime){
        //枚举值：1:15S、2:30S、3:45S、4:60S、5:始终
        if (theretime.equalsIgnoreCase("5")){
            tv_countdown.setVisibility(GONE);
        }else {
            countDownTimer = new CountDownTimer(Long.parseLong(theretime)*15*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tv_countdown.setText(millisUntilFinished / 1000 + "s");
                }

                @Override
                public void onFinish() {
                    mMessageDialog.dismiss();
                }
            }.start();
        }
    }

    /*
    * 设置普通文本 消息
    * CONTENT
    * */
    private void setTextInfo(String content) {
        if (!TextUtils.isEmpty(content)) {
            mContentTextTv.setText(content);
        } else {
            mContentTextTv.setText("");
        }

    }

    private void initOnClick() {
        mOkTextTv.setOnClickListener(this);
        mCancelPlayTv.setOnClickListener(this);
        mPlayTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_text_tv:
            case R.id.cancel_play_tv:
            case R.id.iv_skip:
                isClickCloseDialog = true;
                closeDialog(false,false);
                break;
            case R.id.play_tv://跳转到播放
                //防止播放器在后台，影响top播放器弹出错误提示
                EventBus.getDefault().post(new XmppSuccessEvent());
                isClickCloseDialog = true;
                closeDialog(false,true);

                if (mBodyContentBean == null || mBodyContentBean.getContent() == null) {
                    return;
                }
                BodyContentBean.Content content = mBodyContentBean.getContent();

                String messageId = null;
                if (null != extensionInfo && !TextUtils.isEmpty(extensionInfo.getMessageId())){
                    messageId = extensionInfo.getMessageId();
                }else{
                    messageId = content.getLinkURL();
                }

                //播放视频
                if (!TextUtils.isEmpty(content.getActionType()) && content.getActionType().equals("0")) {
                    if (!TextUtils.isEmpty(content.getContentType())) {
                        if (content.getContentType().equals("VOD")) {
                            //点播获取详情，鉴权播放,跳转详情
                            Intent intent = new Intent(mRxAppCompatActivity, NewVodDetailActivity.class);
                            intent.putExtra(NewVodDetailActivity.VOD_ID, content.getContentId());
                            mRxAppCompatActivity.startActivity(intent);

                            //message转化统计上报
                            UBDPushMessage.recordConversion(messageId,NewVodDetailActivity.class.getSimpleName());
                        } else if (content.getContentType().equals("CHANNEL")) {
                            //如果是直播，获取根据id找到对应的频道，任何鉴权播放
                            String mediaID = "";
                            if (!TextUtils.isEmpty(content.getContentId())) {
                                ChannelDetail channelDetail = LiveUtils.findChannelById(content.getContentId());
                                if (null != channelDetail) {
                                    //媒体ID
                                    List<Integer> maxList = new ArrayList<>();
                                    if (channelDetail.getPhysicalChannels() != null
                                            && channelDetail.getPhysicalChannels().size() > 0) {
                                        if (channelDetail.getPhysicalChannels().size() == 1) {
                                            mediaID = channelDetail.getPhysicalChannels().get(0).getID();
                                        } else {
                                            for (int i = 0; i < channelDetail.getPhysicalChannels().size(); i++) {
                                                maxList.add(Integer.parseInt(channelDetail.getPhysicalChannels().get(i).getDefinition()));
                                            }
                                            //高清标清标识,取高清度最大的
                                            mediaID = channelDetail.getPhysicalChannels()
                                                    .get(maxList.indexOf(Collections.max(maxList)))
                                                    .getID();
                                        }
                                    }
                                } else {
                                    EpgToast.showLongToast(mRxAppCompatActivity, "当前用户无权限观看该频道");
                                    return;
                                }
                                //记录准备播放的频道ID和mediaID
                                if (!TextUtils.isEmpty(content.getContentId()) && !TextUtils.isEmpty(mediaID)) {
                                    LiveTVCacheUtil.getInstance().recordPlayChannelInfo(content.getContentId(), mediaID);
                                }
                                //如果是从点播详情页跳转到直播，现finish点播详情页面
                                //if (OTTApplication.getContext().getCurrentActivity().getClass().getSimpleName().equalsIgnoreCase(NewVodDetailActivity.class.getSimpleName())) {
                                if (OTTApplication.getContext().getCurrentActivity() instanceof NewVodDetailActivity) {
                                    OTTApplication.getContext().getCurrentActivity().finish();
                                }

                                if (OTTApplication.getContext().getCurrentActivity() instanceof LiveTVActivity){
                                    ((LiveTVActivity)OTTApplication.getContext().getCurrentActivity()).getLiveTvPlayFragment().onChannelColumn();
                                }else{
                                    Intent intent = new Intent(mRxAppCompatActivity, LiveTVActivity.class);
                                    intent.putExtra(LiveTVActivity.VIDEO_TYPE, LiveTVActivity.VIDEO_TYPE_LIVETV);
                                    mRxAppCompatActivity.startActivity(intent);
                                }

                                //message转化统计上报
                                UBDPushMessage.recordConversion(messageId, LiveTVActivity.class.getSimpleName());
                            }
                        }
                    }else{
                        SuperLog.info2SD(TAG,"onClick_actionType=0,跳往视频播放界面,但是contentType=null，无法判断是跳往点播或者是直播，return");
                    }
                } else if (!TextUtils.isEmpty(content.getActionType()) && content.getActionType().equals("1")) {
                    if (!TextUtils.isEmpty(content.getLinkURL())) {
                        try {
                            //actionType = 1：获取linkURL，跳转至webview
                            //Intent intent = new Intent(mRxAppCompatActivity, MessageWebViewActivity.class);
                            Intent intent = new Intent(mRxAppCompatActivity, WebActivity.class);
                            String urlDe = java.net.URLDecoder.decode(content.getLinkURL(), "utf-8");
                            urlDe = Tvms.addPurchaseInfo(urlDe,messageId);
                            SuperLog.info2SD(TAG,"Xmpp Message H5url="+urlDe);
                            intent.putExtra("url", urlDe);
                            mRxAppCompatActivity.startActivity(intent);

                            //message转化统计上报
                            UBDPushMessage.recordConversion(messageId,WebActivity.class.getSimpleName());
                        } catch (UnsupportedEncodingException e) {
                            SuperLog.error(TAG,e);
                        }
                    }else{
                        SuperLog.info2SD(TAG,"onClick_actionType=1,跳往H5页面,linkURL=null，所以不进行跳转，return");
                    }

                } else if (!TextUtils.isEmpty(content.getActionType()) && content.getActionType().equals("2")) {
                    //打开本地网页：获取categoryId，跳转至二级栏目页
                    if (!TextUtils.isEmpty(content.getCategoryId())) {

                        Intent intent = getVodCategoryIntent(mRxAppCompatActivity, content.getCategoryId());
                        mRxAppCompatActivity.startActivity(intent);

                        //message转化统计上报
                        UBDPushMessage.recordConversion(messageId,intent.getAction());
                    }else{
                        SuperLog.info2SD(TAG,"onClick_actionType=2,跳往专题界面,categoryId=null，所以不进行跳转，return");
                    }
                } else if (!TextUtils.isEmpty(content.getActionType()) && content.getActionType().equals("3")) {
                    SuperLog.info2SD(TAG,"ActionType = 3,跳转PHM内部界面");
                    //跳转EPG内部界面
                    if (null != extensionInfo) {
                        String actionUrl = null;
                        String actionType = ZJVRoute.LauncherElementActionType.TYPE_0;

                        if (!TextUtils.isEmpty(extensionInfo.getActionType())){
                            actionType = extensionInfo.getActionType();
                        }

                        //配置actionUrl即不需要拼接方式
                        if (!TextUtils.isEmpty(extensionInfo.getActionUrl())){
                            actionUrl = extensionInfo.getActionUrl();
                        }else{
                            //没有配置key为actionUrl时 用拼接方式拼接actionUrl
                            actionUrl = getActionUrl(extensionInfo);
                        }

                        if (TextUtils.isEmpty(actionUrl)){
                            SuperLog.info2SD(TAG,"onClick_actionType=3，打开PHM原生界面，xmpp message actionUrl is null,don't jump,return");
                            return;
                        }else{
                            if (actionUrl.contains("ContentType=PAGE") && TextUtils.isEmpty(extensionInfo.getActionType())){
                                actionType = ZJVRoute.LauncherElementActionType.TYPE_2;
                            }
                            SuperLog.info2SD(TAG,"xmpp message actionUrl="+actionUrl+";actionType="+actionType);
                        }
                        Map<String, String> extraData = new HashMap<>();
                        try {
                            actionUrl = java.net.URLDecoder.decode(actionUrl, "utf-8");
                            if (!TextUtils.isEmpty(extensionInfo.getExtraData())){
                                PushMessageUtils.setExtraData(java.net.URLDecoder.decode(extensionInfo.getExtraData(), "utf-8"),extraData);
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        ZJVRoute.route(mRxAppCompatActivity, ZJVRoute.LauncherElementDataType.STATIC_ITEM, actionUrl, actionType, null, null, extraData);

                        //message转化统计上报
                        UBDPushMessage.recordConversion(messageId,ZJVRoute.getClassName());
                    }else{
                        SuperLog.info2SD(TAG,"onClick_actionType=3,打开PHM原生界面,extensionInfo=null，所以不进行跳转，return");
                    }
                }
        }
    }

    private static Intent getVodCategoryIntent(Context context, String categoryId) {
        Intent intent = new Intent(context, VodMainActivity.class);
        Topic topic = TopicService.getInstance().getTopicMap().get(categoryId);
        SuperLog.info2SD("getVodCategoryIntent", "Topic is null :" + (null == topic) + "选中的专题" + categoryId + "     ,缓存的专题：" + TopicService.getInstance().getTopicMap().keySet().toString() + ",");
        if (null != topic) {
            SuperLog.info2SD("getVodCategoryIntent", "当前专题categoryId:" + categoryId + ",当前专题styleID:" + topic.getTopicStyleId());
        }
        if (topic != null && TopicViewManager.isStyleIdValid(topic.getTopicStyleId())) {
            intent = new Intent(context, TopicActivity.class);
            intent.putExtra(TopicActivity.TOPIC_OBJECT, (Serializable) topic);
            return intent;
        }
        intent.putExtra(VodMainActivity.CATEGORY_ID, categoryId);
        return intent;
    }

    private void closeDialog(boolean isDetachedFromWindow,boolean isToOtherPage){
        //关闭消息列表Dialog
        EventBus.getDefault().post(new MessageEvent());

        if (!isDetachedFromWindow){
            //关闭BaseActivity———Dialog
            PushMessagePresenter.setIsJumpToOtherPage(isToOtherPage);
            mMessageDialog.dismiss();
            /*MessageDialogEvent messageDialogEvent = new MessageDialogEvent();
            messageDialogEvent.setShow(false);
            messageDialogEvent.setToOtherPage(isToOtherPage);
            EventBus.getDefault().post(messageDialogEvent);*/
        }
    }

    //2.4xmpp优化
    private void setViewData(BodyContentBean bodyContentBean,MarqueeText tvTitle,VerticalScrollTextView tvContent
            ,TextViewExt confirmTv,TextViewExt cancelTv,ImageViewExt ivPoster,TextViewExt tvCountdown,RelativeLayout.LayoutParams params) {

        BodyContentBean.Content content = bodyContentBean.getContent();
        if( content == null ){
            tvTitle.setText("");
            tvContent.setText("");
            return;
        }

        setParams(params,extensionInfo.getLocation());

        startCountDownTimer(tvCountdown,extensionInfo.getTheretime());

        if (null != extensionInfo) {
            if (null != ivPoster && !TextUtils.isEmpty(extensionInfo.getPostURL())) {
                //String url = object.getString("postURL").replace(";","/");
                String url = extensionInfo.getPostURL();
                String edsUrl = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL();
                url = edsUrl + url;
                try {
                    String urlDe = java.net.URLDecoder.decode(url, "utf-8");
                    SuperLog.info2SD(TAG, "海报Url==" + urlDe);
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.default_poster_bb)
                            .error(R.drawable.default_poster_bb);

                    GlideApp.with(OTTApplication.getContext())
                            .load(urlDe)
                            .apply(options)
                            .into(ivPoster);
                } catch (UnsupportedEncodingException e) {
                    SuperLog.error(TAG, e);
                }
            }

            if (null != confirmTv && (TextUtils.isEmpty(extensionInfo.getConfirmBtnText()) || extensionInfo.getConfirmBtnText().equals("默认")
                    || extensionInfo.getConfirmBtnText().equals("1"))) {
                confirmTv.setText("立即前往");
            } else if (null != confirmTv){
                confirmTv.setText(extensionInfo.getConfirmBtnText());
            }

            if (null != cancelTv && (TextUtils.isEmpty(extensionInfo.getCancelBtnText()) || extensionInfo.getCancelBtnText().equals("默认")
                    || extensionInfo.getCancelBtnText().equals("1"))) {
                cancelTv.setText("返回");
            } else if (null != cancelTv){
                cancelTv.setText(extensionInfo.getCancelBtnText());
            }
        }
        tvTitle.setText(TextUtils.isEmpty(content.getMessageTitle()) ? "" : content.getMessageTitle());
        tvContent.setText(TextUtils.isEmpty(content.getMessageIntroduce()) ? "" : content.getMessageIntroduce());
    }

    private String getActionUrl(BodyContentBean.ExtensionInfo extensionInfo) {
        StringBuilder sb = new StringBuilder();
        String contentType = null;
        if (!TextUtils.isEmpty(extensionInfo.getContentType())){
            contentType = extensionInfo.getContentType();
            sb.append("&").append(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE).append("=").append(extensionInfo.getContentType());
        }
        if (!TextUtils.isEmpty(extensionInfo.getSubContentType())){
            sb.append("&").append(ZJVRoute.ActionUrlKeyType.SUB_CONTENT_TYPE).append("=").append(extensionInfo.getSubContentType());
        }
        if (!TextUtils.isEmpty(extensionInfo.getContentID())){
            if (ZJVRoute.LauncherElementContentType.PAGE.equalsIgnoreCase(contentType)
                && !extensionInfo.getContentID().contains("@")){
                    //处理跳往二级桌面，contentId为二级桌面的桌面id，需拼接首页桌面id和version，才能正确下载二级桌面json,(100363_103051@1614744750189)
                    sb.append("&")
                            .append(ZJVRoute.ActionUrlKeyType.CONTENT_ID)
                            .append("=")
                            .append(SharedPreferenceUtil.getInstance().getLauncherDeskTopIdForChild())
                            .append("_")
                            .append(extensionInfo.getContentID())
                            .append("@")
                            .append(SharedPreferenceUtil.getInstance().getLauncherVersionForChild());
            }else{
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.CONTENT_ID).append("=").append(extensionInfo.getContentID());
            }
        }
        if (!TextUtils.isEmpty(extensionInfo.getSubjectID())){
            sb.append("&").append(ZJVRoute.ActionUrlKeyType.SUBJECT_ID).append("=").append(extensionInfo.getSubjectID());
        }
        if (!TextUtils.isEmpty(extensionInfo.getFocusContentID())){
            sb.append("&").append(ZJVRoute.ActionUrlKeyType.FOCUS_COTENTID).append("=").append(extensionInfo.getFocusContentID());
        }
        if (!TextUtils.isEmpty(extensionInfo.getAppPkg())){
            sb.append("&").append(ZJVRoute.ActionUrlKeyType.APP_PKG).append("=").append(extensionInfo.getAppPkg());
        }
        if (!TextUtils.isEmpty(extensionInfo.getAppClass())){
            sb.append("&").append(ZJVRoute.ActionUrlKeyType.APP_CLASS).append("=").append(extensionInfo.getAppClass());
        }
        if (!TextUtils.isEmpty(extensionInfo.getVersion())){
            sb.append("&").append(ZJVRoute.ActionUrlKeyType.VERSION).append("=").append(extensionInfo.getVersion());
        }
        if (!TextUtils.isEmpty(extensionInfo.getApkUrl())){
            sb.append("&").append(ZJVRoute.ActionUrlKeyType.APK_URL).append("=").append(extensionInfo.getApkUrl());
        }
        if (!TextUtils.isEmpty(extensionInfo.getAction())){
            sb.append("&").append(ZJVRoute.ActionUrlKeyType.ACTION).append("=").append(extensionInfo.getAction());
        }
        if (!TextUtils.isEmpty(extensionInfo.getContentCode())){
            sb.append("&").append(ZJVRoute.ActionUrlKeyType.CONTENT_CODE).append("=").append(extensionInfo.getContentCode());
        }
        if (!TextUtils.isEmpty(extensionInfo.getClassName())){
            sb.append("&").append(ZJVRoute.ActionUrlKeyType.CLASS_NAME).append("=").append(extensionInfo.getClassName());
        }
        if (!TextUtils.isEmpty(extensionInfo.getChannelMiniType())){
            sb.append("&").append(ZJVRoute.ActionUrlKeyType.CHANNEL_MINI_TYPE).append("=").append(extensionInfo.getChannelMiniType());
        }
        if (!TextUtils.isEmpty(extensionInfo.getFourKContentID())){
            sb.append("&").append(ZJVRoute.ActionUrlKeyType.FOURK_CONTENT_ID).append("=").append(extensionInfo.getFourKContentID());
        }
        if (!TextUtils.isEmpty(extensionInfo.getVODId())){
            sb.append("&").append(ZJVRoute.ActionUrlKeyType.VODID).append("=").append(extensionInfo.getVODId());
        }
        if (!TextUtils.isEmpty(extensionInfo.getType())){
            sb.append("&").append(ZJVRoute.ActionUrlKeyType.TYPE).append("=").append(extensionInfo.getType());
        }
        if (!TextUtils.isEmpty(extensionInfo.getKey())){
            sb.append("&").append(ZJVRoute.ActionUrlKeyType.KEY).append("=").append(extensionInfo.getKey());
        }
        return sb.toString();
    }

}