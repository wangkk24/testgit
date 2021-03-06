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
    * ??????????????????
    * */
    @BindView(R.id.content_text_tv)
    VerticalScrollTextView mContentTextTv;

    /*
    * ????????????????????????
    * */
    @BindView(R.id.ok_text_tv)
    TextView mOkTextTv;

    /*
    * ?????????????????????
    * */
    @BindView(R.id.text_rela)
    RelativeLayout mTextRela;

    /*
    * ??????????????????
    * */
    @BindView(R.id.image_view)
    ImageView mImageView;

    /*
    * ??????????????????
    * */
    @BindView(R.id.title_tv)
    TextView mTitleTv;

    /*
    * ????????????????????????
    * */
    @BindView(R.id.content_play_tv)
    VerticalScrollTextView mContentPlayTv;

    /*
    * ????????????????????????
    * */
    @BindView(R.id.cancel_play_tv)
    TextView mCancelPlayTv;

    /*
    * ????????????????????????
    * */
    @BindView(R.id.play_tv)
    TextView mPlayTv;

    /*
    * ?????????????????????
    * */
    @BindView(R.id.play_rela)
    LinearLayout mPlayRela;

    private Activity mRxAppCompatActivity;

    private MessageDialog mMessageDialog;

    private CountDownTimer countDownTimer;

    /*
    * ?????????
    * */
    private String mBody = "";

    /*
    * ??????????????????mode
    * */
    public static final String MODE = "mode";

    /*
    * ????????????
    * */
    public static final String CONTENT = "content";

    /*
    * ???????????????
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

    //????????????
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    //????????????????????????
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //initData();
        initOnClick();
    }


    //????????????????????????
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
            //?????????????????????
            this.getBackground().mutate().setAlpha(100);
            JSONObject object = new JSONObject(body);
            if (!TextUtils.isEmpty(object.getString(MODE)) && object.getString(MODE).equals("6")) {
                //??????????????????
                mTextRela.setVisibility(View.VISIBLE);
                mPlayRela.setVisibility(View.GONE);
                mOkTextTv.requestFocus();
                setTextInfo(object.getString(CONTENT));
            } else if (!TextUtils.isEmpty(object.getString(MODE)) && object.getString(MODE).equals("5")) {
                //??????????????????
                mTextRela.setVisibility(View.GONE);
                //---
                //mPlayRela.setVisibility(View.VISIBLE);
                //mPlayTv.requestFocus();
                //setTextPlayInfo(mBodyContentBean);
                //---
                //2.4??????xmpp
                mBodyContentBean = JsonParse.json2Object(body, BodyContentBean.class);
                if (null != mBodyContentBean){
                    extensionInfo = json2Object(mBodyContentBean.getContent().getExtensionInfo(), BodyContentBean.ExtensionInfo.class);
                }
                //???????????????
                setAlpha();

                if (isPicMessage()){
                    //????????????
                    addViewForPic();
                }else{
                    //???????????????????????????
                    addViewForTxt();
                }

            } else if (!TextUtils.isEmpty(object.getString(MODE)) && object.getString(MODE).equals("0")) {
                //??????????????????
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

    //???????????????????????????????????????????????????????????????
    private boolean isPicMessage(){
        if (null != extensionInfo && !TextUtils.isEmpty(extensionInfo.getPostURL())) {
            return true;
        }
        SuperLog.info2SDDebug(TAG,"???????????????????????????????????????????????????????????????");
        return false;
    }

    //??????????????????View
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

    //????????????????????????????????????
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

    //??????dialog???????????????
    private void setParams(RelativeLayout.LayoutParams params,String location){
        //????????????1:?????????2:?????????3:?????????4:?????????5:??????
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

    //2.4xmpp?????????  ???????????????
    private void startCountDownTimer(TextViewExt tv_countdown,String theretime){
        //????????????1:15S???2:30S???3:45S???4:60S???5:??????
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
    * ?????????????????? ??????
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
            case R.id.play_tv://???????????????
                //?????????????????????????????????top???????????????????????????
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

                //????????????
                if (!TextUtils.isEmpty(content.getActionType()) && content.getActionType().equals("0")) {
                    if (!TextUtils.isEmpty(content.getContentType())) {
                        if (content.getContentType().equals("VOD")) {
                            //?????????????????????????????????,????????????
                            Intent intent = new Intent(mRxAppCompatActivity, NewVodDetailActivity.class);
                            intent.putExtra(NewVodDetailActivity.VOD_ID, content.getContentId());
                            mRxAppCompatActivity.startActivity(intent);

                            //message??????????????????
                            UBDPushMessage.recordConversion(messageId,NewVodDetailActivity.class.getSimpleName());
                        } else if (content.getContentType().equals("CHANNEL")) {
                            //??????????????????????????????id??????????????????????????????????????????
                            String mediaID = "";
                            if (!TextUtils.isEmpty(content.getContentId())) {
                                ChannelDetail channelDetail = LiveUtils.findChannelById(content.getContentId());
                                if (null != channelDetail) {
                                    //??????ID
                                    List<Integer> maxList = new ArrayList<>();
                                    if (channelDetail.getPhysicalChannels() != null
                                            && channelDetail.getPhysicalChannels().size() > 0) {
                                        if (channelDetail.getPhysicalChannels().size() == 1) {
                                            mediaID = channelDetail.getPhysicalChannels().get(0).getID();
                                        } else {
                                            for (int i = 0; i < channelDetail.getPhysicalChannels().size(); i++) {
                                                maxList.add(Integer.parseInt(channelDetail.getPhysicalChannels().get(i).getDefinition()));
                                            }
                                            //??????????????????,?????????????????????
                                            mediaID = channelDetail.getPhysicalChannels()
                                                    .get(maxList.indexOf(Collections.max(maxList)))
                                                    .getID();
                                        }
                                    }
                                } else {
                                    EpgToast.showLongToast(mRxAppCompatActivity, "????????????????????????????????????");
                                    return;
                                }
                                //???????????????????????????ID???mediaID
                                if (!TextUtils.isEmpty(content.getContentId()) && !TextUtils.isEmpty(mediaID)) {
                                    LiveTVCacheUtil.getInstance().recordPlayChannelInfo(content.getContentId(), mediaID);
                                }
                                //????????????????????????????????????????????????finish??????????????????
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

                                //message??????????????????
                                UBDPushMessage.recordConversion(messageId, LiveTVActivity.class.getSimpleName());
                            }
                        }
                    }else{
                        SuperLog.info2SD(TAG,"onClick_actionType=0,????????????????????????,??????contentType=null????????????????????????????????????????????????return");
                    }
                } else if (!TextUtils.isEmpty(content.getActionType()) && content.getActionType().equals("1")) {
                    if (!TextUtils.isEmpty(content.getLinkURL())) {
                        try {
                            //actionType = 1?????????linkURL????????????webview
                            //Intent intent = new Intent(mRxAppCompatActivity, MessageWebViewActivity.class);
                            Intent intent = new Intent(mRxAppCompatActivity, WebActivity.class);
                            String urlDe = java.net.URLDecoder.decode(content.getLinkURL(), "utf-8");
                            urlDe = Tvms.addPurchaseInfo(urlDe,messageId);
                            SuperLog.info2SD(TAG,"Xmpp Message H5url="+urlDe);
                            intent.putExtra("url", urlDe);
                            mRxAppCompatActivity.startActivity(intent);

                            //message??????????????????
                            UBDPushMessage.recordConversion(messageId,WebActivity.class.getSimpleName());
                        } catch (UnsupportedEncodingException e) {
                            SuperLog.error(TAG,e);
                        }
                    }else{
                        SuperLog.info2SD(TAG,"onClick_actionType=1,??????H5??????,linkURL=null???????????????????????????return");
                    }

                } else if (!TextUtils.isEmpty(content.getActionType()) && content.getActionType().equals("2")) {
                    //???????????????????????????categoryId???????????????????????????
                    if (!TextUtils.isEmpty(content.getCategoryId())) {

                        Intent intent = getVodCategoryIntent(mRxAppCompatActivity, content.getCategoryId());
                        mRxAppCompatActivity.startActivity(intent);

                        //message??????????????????
                        UBDPushMessage.recordConversion(messageId,intent.getAction());
                    }else{
                        SuperLog.info2SD(TAG,"onClick_actionType=2,??????????????????,categoryId=null???????????????????????????return");
                    }
                } else if (!TextUtils.isEmpty(content.getActionType()) && content.getActionType().equals("3")) {
                    SuperLog.info2SD(TAG,"ActionType = 3,??????PHM????????????");
                    //??????EPG????????????
                    if (null != extensionInfo) {
                        String actionUrl = null;
                        String actionType = ZJVRoute.LauncherElementActionType.TYPE_0;

                        if (!TextUtils.isEmpty(extensionInfo.getActionType())){
                            actionType = extensionInfo.getActionType();
                        }

                        //??????actionUrl????????????????????????
                        if (!TextUtils.isEmpty(extensionInfo.getActionUrl())){
                            actionUrl = extensionInfo.getActionUrl();
                        }else{
                            //????????????key???actionUrl??? ?????????????????????actionUrl
                            actionUrl = getActionUrl(extensionInfo);
                        }

                        if (TextUtils.isEmpty(actionUrl)){
                            SuperLog.info2SD(TAG,"onClick_actionType=3?????????PHM???????????????xmpp message actionUrl is null,don't jump,return");
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

                        //message??????????????????
                        UBDPushMessage.recordConversion(messageId,ZJVRoute.getClassName());
                    }else{
                        SuperLog.info2SD(TAG,"onClick_actionType=3,??????PHM????????????,extensionInfo=null???????????????????????????return");
                    }
                }
        }
    }

    private static Intent getVodCategoryIntent(Context context, String categoryId) {
        Intent intent = new Intent(context, VodMainActivity.class);
        Topic topic = TopicService.getInstance().getTopicMap().get(categoryId);
        SuperLog.info2SD("getVodCategoryIntent", "Topic is null :" + (null == topic) + "???????????????" + categoryId + "     ,??????????????????" + TopicService.getInstance().getTopicMap().keySet().toString() + ",");
        if (null != topic) {
            SuperLog.info2SD("getVodCategoryIntent", "????????????categoryId:" + categoryId + ",????????????styleID:" + topic.getTopicStyleId());
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
        //??????????????????Dialog
        EventBus.getDefault().post(new MessageEvent());

        if (!isDetachedFromWindow){
            //??????BaseActivity?????????Dialog
            PushMessagePresenter.setIsJumpToOtherPage(isToOtherPage);
            mMessageDialog.dismiss();
            /*MessageDialogEvent messageDialogEvent = new MessageDialogEvent();
            messageDialogEvent.setShow(false);
            messageDialogEvent.setToOtherPage(isToOtherPage);
            EventBus.getDefault().post(messageDialogEvent);*/
        }
    }

    //2.4xmpp??????
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
                    SuperLog.info2SD(TAG, "??????Url==" + urlDe);
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

            if (null != confirmTv && (TextUtils.isEmpty(extensionInfo.getConfirmBtnText()) || extensionInfo.getConfirmBtnText().equals("??????")
                    || extensionInfo.getConfirmBtnText().equals("1"))) {
                confirmTv.setText("????????????");
            } else if (null != confirmTv){
                confirmTv.setText(extensionInfo.getConfirmBtnText());
            }

            if (null != cancelTv && (TextUtils.isEmpty(extensionInfo.getCancelBtnText()) || extensionInfo.getCancelBtnText().equals("??????")
                    || extensionInfo.getCancelBtnText().equals("1"))) {
                cancelTv.setText("??????");
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
                    //???????????????????????????contentId????????????????????????id????????????????????????id???version?????????????????????????????????json,(100363_103051@1614744750189)
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