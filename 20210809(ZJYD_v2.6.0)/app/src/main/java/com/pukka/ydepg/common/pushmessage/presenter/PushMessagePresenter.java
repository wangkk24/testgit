package com.pukka.ydepg.common.pushmessage.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.CustomGroup;
import com.pukka.ydepg.common.pushmessage.view.MessageDialog;
import com.pukka.ydepg.common.refresh.RefreshManager;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Tvms;
import com.pukka.ydepg.common.report.ubd.scene.UBDPushMessage;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.fragment.WebActivity;
import com.pukka.ydepg.launcher.ui.reminder.ReminderDialog;
import com.pukka.ydepg.launcher.ui.reminder.beans.ReminderMessage;
import com.pukka.ydepg.moudule.mytv.bean.BodyContentBean;
import com.pukka.ydepg.moudule.mytv.presenter.OrderUserGroupPresenter;
import com.pukka.ydepg.moudule.mytv.utils.MessageDataHolder;
import com.pukka.ydepg.xmpp.XmppManager;
import com.pukka.ydepg.xmpp.utils.XmppUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.pukka.ydepg.common.utils.JsonParse.json2Object;

/**
 * 推送消息
 * */
public class PushMessagePresenter {

    private static final String TAG = PushMessagePresenter.class.getSimpleName();

    private static OrderUserGroupPresenter getUserGroupPresenter = new OrderUserGroupPresenter();

    //展示最近的N条消息”及“展示最近N天的消息”
    public  static final String MESSAGE_COUNT    = "count";
    public  static final String MESSAGE_DAY      = "day";

    private static final String REMINDER_TYPE = "REMINDER_TYPE";

    //实现对纯文本消息的控制
    private static final String ZJ_TXT_TV_MS_PAGE_CONTROL = "ZJtxttvmsPageControl";

    //实现对非纯文本消息的控制
    private static final String ZJ_RTF_TV_MS_PAGE_CONTROL = "ZJrtftvmsPageControl";

    private static Date showLastDate;

    //用户所属的用户群
    private static List<CustomGroup> mCustomGroup;

    //H5消息仅开机展示一次
    private static boolean isHasShowH5Message = false;

    //如果是眺望其他界面，从其他界面返回后再去检测有无未展示的消息
    private static boolean mIsJumpToOtherPage = false;
    private static MessageDialog mMessageDialog;

    private static Activity mActivity;

    public PushMessagePresenter() {

    }

    //H5消息仅开机展示一次
    public static void setHasShowH5Message(boolean hasShowH5Message){
        isHasShowH5Message = hasShowH5Message;
    }
    //是否是由dialog跳转到其他界面，if is，则先不去处理待展示的message
    public static void setIsJumpToOtherPage(boolean isJumpToOtherPage){
        mIsJumpToOtherPage = isJumpToOtherPage;
    }

    // 处理推送消息\
    public static void pushMessage(String body) {
        try {
            //重新构建消息,增加receivingTime字段
            String receivedTime = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(new Date());
            String bodyResult = body.substring(0, body.length() - 1) + ",\"receivingTime\":\"" + receivedTime + "\"}";
            SuperLog.info2SD(TAG, "BodyResult = " + bodyResult);
            JSONObject msgBody = new JSONObject(bodyResult);

            //
            mActivity = OTTApplication.getContext().getCurrentActivity();

            //判断消息类型是否正确 消息是否过期
            if (isPushMessageFormatValid(msgBody) && isPushMessageTimeValid(msgBody)) {
                if (!msgBody.getString("mode").equals("0")) {

                    long delayTime = getDelayTime();

                    //如果是H5消息  不弹窗显示，直接打开H5页面
                    if (isH5Message(bodyResult)){
                        List<String> messageH5AllList = MessageDataHolder.get().getMessageH5AllList();
                        setMessageH5(body,msgBody,messageH5AllList);
                        if (null != messageH5AllList && messageH5AllList.size() > 0 &&
                                (null == showLastDate || (new Date().getTime() - showLastDate.getTime()) > delayTime)) {
                            showLastDate = new Date();
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                showXmppMessage();
                            }, delayTime);
                        }
                        UBDPushMessage.recordArrived(msgBody);
                        return;
                    }

                    //mode!=0时直接保存信息
                    getShowMessage(bodyResult, msgBody);

                    if (MessageDataHolder.get().getMessageAllList().size() > 0 &&
                            (null == showLastDate || (new Date().getTime() - showLastDate.getTime()) > delayTime)) {
                        showLastDate = new Date();
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            showXmppMessage();
                        }, delayTime);
                    }
                    //2.保存在本地供消息通知列表查询
                    SharedPreferenceUtil.getInstance().saveNotification(bodyResult);
                } else {
                    //mode=0时为滚动消息，需要下载再进行保存  下载url转txt、缓存、显示Dialog
                    XmppManager.downloadMessage(bodyResult);
                }
                //上报UBD,统计上报linkURL type=5 命令消息
                UBDPushMessage.recordArrived(msgBody);
            }
        } catch (Exception e) {
            SuperLog.error(TAG, e);
        }
    }

    //验证格式是否有效
    private static boolean isPushMessageFormatValid(JSONObject object){
        boolean isValid = false;
        //只处理type = 0  mode = 0、5、6 的消息
        if ("0".equals(XmppUtil.getString(object,"type"))) {
            if ("0".equals(XmppUtil.getString(object, "mode")) || "6".equals(XmppUtil.getString(object, "mode"))) {
                isValid = true;
            } else if ("5".equals(XmppUtil.getString(object, "mode"))) {
                //xmpp2.4 xmpp优化，运营消息中不含有海报的消息为文本消息
                isValid = true;
            }
        }
        if (!isValid){
            SuperLog.info2SD(TAG,"消息格式有误，不展示消息");
        }
        return isValid;
    }

    //验证消息有效期
    private static boolean isPushMessageTimeValid(JSONObject msgBody) {
        try{
            //判断时间是否过期
            String expiredTime = XmppUtil.getString(msgBody, "validTime");
            SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date expiredDate = mFormat.parse(expiredTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(expiredDate);
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
            expiredDate = calendar.getTime();

            Date nowDate = new Date();
            if (nowDate.before(expiredDate)) {
                return true;
            }
        }catch (Exception e){
            SuperLog.error(TAG,e);
        }
        SuperLog.info2SD(TAG,"消息已过有效期，不展示消息");
        return false;
    }

    private static boolean isH5Message(String body){
        String mode = "";
        String messageType = "1";
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.has("mode")){
                mode = jsonObject.getString("mode");
            }

            JSONObject objectContent   = jsonObject.getJSONObject("content");
            JSONObject extensionObject = new JSONObject(objectContent.getString("extensionInfo"));
            if (extensionObject.has("messagetype") && !TextUtils.isEmpty(extensionObject.getString("messagetype"))) {
                messageType = extensionObject.getString("messagetype");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mode.equals("5") && messageType.equalsIgnoreCase("2")){
            BodyContentBean mBodyContentBean = JsonParse.json2Object(body, BodyContentBean.class);
            BodyContentBean.Content content = mBodyContentBean.getContent();
            try {
                if (content.getActionType().equalsIgnoreCase("1")){
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //获取需要弹框展示的消息,mode=6文本消息，=5运营消息
    private static void getShowMessage(String bodyResult, JSONObject msgBody) {
        try {

            //2.6版本第一批需求【(20210519)浙江移动视频项目FRS【R339】老年版儿童版弹窗优化-final】
            if (!isNeedShowForEPGDeskTop(msgBody)){
                return;
            }

            SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date nowDate = new Date();
            Date sendDate = null;
            if ("5".equals(XmppUtil.getString(msgBody,"mode")) ){
                JSONObject objectContent   = msgBody.getJSONObject("content");
                JSONObject extensionObject = new JSONObject(objectContent.getString("extensionInfo"));
                if (null != extensionObject && extensionObject.has("sendTime") && !TextUtils.isEmpty(extensionObject.getString("sendTime"))) {
                    //解析发送时间
                    sendDate = mFormat.parse(extensionObject.getString("sendTime"));
                }
            }

            // 获取推送消息弹窗展示策略
            // (1)同时配置“展示最近的N条消息”及“展示最近N天的消息”
            // (2)只配置展示最近的N条消息
            // (3)只配置展示最近N天的消息
            Map<String, String> messageFrequencyControlMap = SessionService.getInstance().getSession().getMessageFrequencyControl();
            if (null != messageFrequencyControlMap) {

                if (null == sendDate){
                    String sendTime = mFormat.format(nowDate);
                    sendDate = mFormat.parse(sendTime);
                }
                SuperLog.debug(TAG,"终端参数配置可显示的条数="+messageFrequencyControlMap.get(MESSAGE_COUNT)+";终端参数配置最近N天="+messageFrequencyControlMap.get(MESSAGE_DAY));
                //终端参数配置最近N天的条数
                if (!TextUtils.isEmpty(messageFrequencyControlMap.get(MESSAGE_DAY)) && null != sendDate) {
                    Calendar calendarControl = Calendar.getInstance();
                    calendarControl.setTime(sendDate);
                    //发送时间+配置的N天和现在时间比较，是否在N天之内
                    calendarControl.set(Calendar.DATE, calendarControl.get(Calendar.DATE) + Integer.parseInt(messageFrequencyControlMap.get(MESSAGE_DAY)));
                    Date isValidDate = calendarControl.getTime();
                    //mFormat.format(isValidDate);
                    //判断是否是在N天之内
                    if (isValidDate.getTime() - nowDate.getTime() < 0) {
                        //消息不在所要展示的N天之内
                        SuperLog.info2SD(TAG,"消息不在所要展示的"+messageFrequencyControlMap.get(MESSAGE_DAY)+"天之内");
                        return;
                    }
                }

                //终端参数配置可显示的条数
               String messageCount = messageFrequencyControlMap.get(MESSAGE_COUNT);

                //如果配置消息显示频次为0，则不显示推送消息
                if (!TextUtils.isEmpty(messageCount) && messageCount.equalsIgnoreCase("0")){
                    //return false;
                    SuperLog.info2SD(TAG,"配置消息显示频次为0，不显示推送消息");
                    return;
                }
                //待展示的message条数
                List<String> messageAllList = MessageDataHolder.get().getMessageAllList();
                //if (!TextUtils.isEmpty(messageCount) && messageAllList.size() >= Integer.parseInt(messageCount)) {
                if (messageAllList.size() > 0) {
                    /**
                     * 已经满足显示N条需求
                     * 判断新进的一条消息的发送时间比现在待展示的列表要新，则替换之
                     * */
                    if (null != sendDate) {
                        int timeInt = -2;
                        for (int i = 0; i < messageAllList.size(); i++) {
                            JSONObject objectTemp = new JSONObject(messageAllList.get(i));
                            String sendTime = "";
                            if (objectTemp.getString("mode").equalsIgnoreCase("6")){
                                sendTime = getReceivingTime(objectTemp);
                            }else if (objectTemp.getString("mode").equalsIgnoreCase("5")){
                                JSONObject extensionInfoObjectTemp = new JSONObject(new JSONObject(objectTemp.getString("content")).getString("extensionInfo"));
                                if (null != extensionInfoObjectTemp && extensionInfoObjectTemp.has("sendTime")){
                                    sendTime = extensionInfoObjectTemp.getString("sendTime");
                                }else{
                                    sendTime = getReceivingTime(objectTemp);
                                }
                            }
                            if (!TextUtils.isEmpty(sendTime) && !sendDate.before(mFormat.parse(sendTime))) {
                                timeInt = i;
                                break;
                            }
                        }
                        setXmppMessageData(messageAllList,timeInt,bodyResult,messageCount);
                    }else{
                        //发送时间为空 则改用接收时间
                        setXmppMessageData(messageAllList,-1,bodyResult,messageCount);
                    }
                    //return false;
                }else{
                    MessageDataHolder.get().setMessageList(bodyResult);
                }

            }else{
                MessageDataHolder.get().setMessageList(bodyResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return true;
    }

    private static void setMessageH5(String bodyResult, JSONObject msgBody,List<String> messageAllList) {
        try {
            SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date sendDate = null;
            JSONObject objectContent = msgBody.getJSONObject("content");
            JSONObject extensionObject = new JSONObject(objectContent.getString("extensionInfo"));
            if (extensionObject.has("sendTime") && !TextUtils.isEmpty(extensionObject.getString("sendTime"))) {
                //解析发送时间
                sendDate = mFormat.parse(extensionObject.getString("sendTime"));
            }
            //待展示的message条数
            //if (!TextUtils.isEmpty(messageCount) && messageAllList.size() >= Integer.parseInt(messageCount)) {
            if (null != messageAllList && messageAllList.size() > 0) {
                /**
                 * 判断新进的一条消息的发送时间比现在待展示的列表要新，则替换之
                 * */
                int timeInt = -2;
                for (int i = 0; i < messageAllList.size(); i++) {
                    JSONObject objectTemp = new JSONObject(messageAllList.get(i));
                    String sendTime = "";
                    JSONObject extensionInfoObjectTemp = new JSONObject(new JSONObject(objectTemp.getString("content")).getString("extensionInfo"));
                    if (null != extensionInfoObjectTemp && extensionInfoObjectTemp.has("sendTime")) {
                        sendTime = extensionInfoObjectTemp.getString("sendTime");
                    } else {
                        sendTime = getReceivingTime(objectTemp);
                    }

                    if (!TextUtils.isEmpty(sendTime) && !sendDate.before(mFormat.parse(sendTime))) {
                        timeInt = i;
                        break;
                    }
                }
                setXmppMessageH5Data(messageAllList, timeInt, bodyResult);
                //return false;
            } else {
                MessageDataHolder.get().setMessageH5List(bodyResult);
            }
            saveH5MessageToLocal(MessageDataHolder.get().getMessageH5AllList(),true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //推送消息延时显示时间
    private static long getDelayTime(){
        try {
            String delayTime = CommonUtil.getConfigValue(Constant.XMPP_RECEIVE_SHOW_TIME);
            return Long.parseLong(delayTime);
        } catch (Exception e){
            //终端参数如果没配置或者配置错误默认15s
            return  15000L;
        }
    }

    //处理推送消息的到达事件
    private static String getReceivingTime(JSONObject objectTemp){
        try {
            return objectTemp.getString("receivingTime")
                    .replace(".","")
                    .replace(":","")
                    .replace(" ","")+"00";
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    //处理待展示的message
    private static void setXmppMessageData(List<String> messageAllList, int i, String bodyResult,String messageCount){
        if (!TextUtils.isEmpty(messageCount) && messageAllList.size() >= Integer.parseInt(messageCount)){
            if (i != -2){
                messageAllList.remove(messageAllList.size()-1);
            }
            if (i == -1){
                messageAllList.add(0,bodyResult);
            }else if (i >= 0){
                messageAllList.add(i,bodyResult);
            }
        }else{
            if (i == -1){
                messageAllList.add(0,bodyResult);
            }else if (i == -2){
                messageAllList.add(bodyResult);
            }else{
                messageAllList.add(i,bodyResult);
            }
        }
        MessageDataHolder.get().setMessageData(messageAllList);
    }

    private static void setXmppMessageH5Data(List<String> messageAllList, int i, String bodyResult) {
        if (i == -2) {
            messageAllList.add(bodyResult);
        } else {
            messageAllList.add(i, bodyResult);
        }
        MessageDataHolder.get().setMessageH5Data(messageAllList);
    }

    public static void showXmppMessage(){
        SuperLog.info2SD(TAG, "Start to show pushmessage");

        //处于加载默认图片  不弹框
        /*是否在刚开机加载界面
        如果在则不显示，跳到MainActivity再弹框显示*/
        if (!MessageDataHolder.get().getIsComplete()) {
            SuperLog.info2SD(TAG, "Ad is showing, push message will be delay.");
            return;
        }
        if (null != mActivity && mActivity != OTTApplication.getContext().getCurrentActivity()) {
            SuperLog.info2SD(TAG, "Activity is changed,no show message dialog");
            return;
        }

        if (CollectionUtil.isEmpty(mCustomGroup)){
            getUserGroupPresenter.getLabelsByUserId(SessionService.getInstance().getSession().getUserId(), callback);
        }else if (null == mMessageDialog || !mMessageDialog.isShowing()) {
            hideMessageDialogAndToShow();
        }

        /*
         * 防止多个推送弹框弹出
         * */
        /*if (!mIsShowing) {
            hideMessageDialogAndToShow();
        }*/
    }

    //处理开机移动端书签提示数据
    public static void setReminderMessage(String body){
        SuperLog.info2SD(TAG, "开机展示手机移动端书签.");
        if (null != mMessageDialog && mMessageDialog.isShowing()){
            if (!TextUtils.isEmpty(body)) {
                MessageDataHolder.get().setMessageMobileAndEpg(body);
            }
        }else{
            showMessageDialogBase(body);
        }
    }

    /*
     * 推送消息浏览Dialog显示
     * */
    public static void showMessageDialogBase(String body) {
        SuperLog.info2SD(TAG, "mIsShowing = true");
        RefreshManager.getInstance().getScreenPresenter().exit();//如果有屏保需要关闭屏保
        //hideMessageDialog();
        mActivity = OTTApplication.getContext().getCurrentActivity();
        mMessageDialog = new MessageDialog(mActivity);
        if (isReminderType(body)) {
            ReminderMessage reminder = json2Object(body, ReminderMessage.class);
            if (reminder != null) {
                showReminderDialog(reminder);
            } else {
                SuperLog.error(TAG, "Reminder Body is null");
            }
        } else {
            recordImpression(body);
            mMessageDialog.setBody(body);
            mMessageDialog.show();
        }
    }

    private static void recordImpression(String body){
        try {
            JSONObject msgBody = new JSONObject(body);
            UBDPushMessage.recordImpression(msgBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //先隐藏dialog，再检测是否有未展示的message
    public static void hideMessageDialogAndToShow() {
        //先隐藏
        hideMessageDialog();

        //在判断是否还有待展示的消息
        if (!mIsJumpToOtherPage){//如果是跳转到其他界面，不走查询待展示的消息
            String messageString = MessageDataHolder.get().getMessageList();
            if (!TextUtils.isEmpty(messageString) && isBelongUserGroup(messageString)) {
                showMessageDialogBase(messageString);
            }else if (!isHasShowH5Message){
                mActivity = null;
                //处理H5消息  直接打开H5页面
                toH5WebActivity();
            }else{
                mActivity = null;
            }
        }
        mIsJumpToOtherPage = false;
    }

    public static void hideMessageDialog() {
        if (mMessageDialog != null) {
            mMessageDialog.dismiss();
            mMessageDialog = null;
        }
    }

    /**
     * 是否是提醒类型
     *
     * @param body 消息体
     * @return boolean
     */
    private static boolean isReminderType(String body) {
        return body.contains(REMINDER_TYPE);
    }

    /**
     * 展示开机提醒弹窗
     */
    private static void showReminderDialog(ReminderMessage reminder) {
        ReminderDialog reminderDialog = new ReminderDialog(OTTApplication.getContext().getCurrentActivity(), reminder);
        reminderDialog.show();
    }

    private static void toH5WebActivity(){
        isHasShowH5Message = true;

        String messageH5String = MessageDataHolder.get().getMessageH5List();
        if (!TextUtils.isEmpty(messageH5String) && isBelongUserGroup(messageH5String)) {

            String messageId = null;
            BodyContentBean.Content content = JsonParse.json2Object(messageH5String, BodyContentBean.class).getContent();
            try {
                if (!TextUtils.isEmpty(new JSONObject(content.getExtensionInfo()).getString("messageId"))){
                    messageId = new JSONObject(content.getExtensionInfo()).getString("messageId");
                }else{
                    messageId = content.getLinkURL();
                }
                Intent intent = new Intent(OTTApplication.getContext().getCurrentActivity(), WebActivity.class);
                String urlDe = java.net.URLDecoder.decode(content.getLinkURL(), "utf-8");
                urlDe = Tvms.addPurchaseInfo(urlDe,messageId);
                SuperLog.info2SD(TAG,"Xmpp Message H5url="+urlDe);
                intent.putExtra("url", urlDe);
                OTTApplication.getContext().getCurrentActivity().startActivity(intent);

                recordImpression(messageH5String);
                UBDPushMessage.recordConversion(messageId,WebActivity.class.getSimpleName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        saveH5MessageToLocal(MessageDataHolder.get().getMessageH5AllList(),false);
    }

    //将H5消息存储到本地
    private static void saveH5MessageToLocal(List<String> mMessageH5List,boolean isSaveAll) {
        if (!CollectionUtil.isEmpty(mMessageH5List)){
            if (!isSaveAll){
                BodyContentBean mBodyContentBean = JsonParse.json2Object(mMessageH5List.get(0), BodyContentBean.class);
                BodyContentBean.ExtensionInfo extensionInfo = null;
                if (null != mBodyContentBean && null != mBodyContentBean.getContent() && !TextUtils.isEmpty(mBodyContentBean.getContent().getExtensionInfo())){
                    extensionInfo = json2Object(mBodyContentBean.getContent().getExtensionInfo(), BodyContentBean.ExtensionInfo.class);
                }
                if (null != extensionInfo){
                    if (!TextUtils.isEmpty(extensionInfo.getMessagenum()) && Integer.parseInt(extensionInfo.getMessagenum()) > 1){
                        extensionInfo.setMessagenum(String.valueOf(Integer.parseInt(extensionInfo.getMessagenum()) - 1));
                        mBodyContentBean.getContent().setExtensionInfo(JsonParse.object2String(extensionInfo));
                        mMessageH5List.remove(mMessageH5List.get(0));
                        mMessageH5List.add(0,JsonParse.object2String(mBodyContentBean));
                    }else{
                        mMessageH5List.remove(mMessageH5List.get(0));
                    }
                }
            }
            SharedPreferenceUtil.getInstance().saveXmppMessageH5(mMessageH5List);
        }
    }

    private static OrderUserGroupPresenter.GetUserGroupCallback callback = new OrderUserGroupPresenter.GetUserGroupCallback() {
        @Override
        public void getLabelsByUserIdSuccess(List<CustomGroup> customGroups) {
            mCustomGroup = customGroups;
            if (CollectionUtil.isEmpty(customGroups)) {
                SuperLog.info2SD(TAG, "用户群（标签库客户画像）= null,消息不展示");
            } else {
                SuperLog.info2SD(TAG, "用户群（标签库客户画像）=" + JsonParse.listToJsonString(customGroups));
            }
            if (null == mMessageDialog || !mMessageDialog.isShowing()) {
                OTTApplication.getContext().getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideMessageDialogAndToShow();
                    }
                });
            }
        }

        @Override
        public void getLabelsByUserIdFail() {
            SuperLog.info2SD(TAG, "用户群（标签库客户画像）getLabelsByUserIdFail,消息不展示");
            if (null == mMessageDialog || !mMessageDialog.isShowing()) {
                OTTApplication.getContext().getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideMessageDialogAndToShow();
                    }
                });
            }
        }
    };

    //判断此消息是否在用户所在的用户群
    private static boolean isBelongUserGroup(String body){
        String mode = "";
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.has("mode")){
                mode = jsonObject.getString("mode");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mode.equalsIgnoreCase("5")){
            BodyContentBean mBodyContentBean = JsonParse.json2Object(body, BodyContentBean.class);
            BodyContentBean.ExtensionInfo extensionInfo = null;
            if (null != mBodyContentBean.getContent() && !TextUtils.isEmpty(mBodyContentBean.getContent().getExtensionInfo())){
                extensionInfo = json2Object(mBodyContentBean.getContent().getExtensionInfo(), BodyContentBean.ExtensionInfo.class);
                String messagelabel = extensionInfo.getMessagelabel();
                if (!TextUtils.isEmpty(messagelabel) && !CollectionUtil.isEmpty(mCustomGroup)){
                    String[] split = messagelabel.split("\\|");
                    for (CustomGroup customGroup : mCustomGroup){
                        for (int i = 0; i < split.length; i++){
                            //LabelName相等的同时，LabelValue==1才显示消息
                            if (customGroup.getLabelName().equalsIgnoreCase(split[i]) && customGroup.getLabelValue().equalsIgnoreCase("1")){
                                return true;
                            }
                        }
                    }
                    SuperLog.info2SD(TAG,"XmppMessage is no belong User group");
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isEpgDesktopTypeForShow(String zJRtfTvMsPageControl){
        String type;
        if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
            type = "3";
        }else if (SharedPreferenceUtil.getInstance().getIsSimpleEpg()){
            type = "2";
        }else{
            type = "1";
        }
        SuperLog.debug(TAG,"当前桌面zJTvMsPageControl="+type);
        if (zJRtfTvMsPageControl.equalsIgnoreCase("1") && zJRtfTvMsPageControl.equalsIgnoreCase(type)){
            return true;
        }else if (zJRtfTvMsPageControl.equalsIgnoreCase("2") && !type.equalsIgnoreCase("3")){
            return true;
        }else if (zJRtfTvMsPageControl.equalsIgnoreCase("3") && !type.equalsIgnoreCase("2")){
            return true;
        }
        return false;
    }

    /**
     * 0  //消息针对所有页面都展示
     * 1  //消息只对普通页面展示
     * 2  //消息只针对普通+老年页面展示
     * 3  //消息只针对普通+儿童页面展示
     * 4~9 //预留场景
     *
     * 获取需要弹框展示的消息,mode=6文本消息，=5运营消息
     * */
    private static boolean isNeedShowForEPGDeskTop(JSONObject msgBody){
        String zJTvMsPageControl = "0";
        if ("5".equalsIgnoreCase(XmppUtil.getString(msgBody,"mode"))){
            JSONObject objectContent   = null;
            try {
                objectContent = msgBody.getJSONObject("content");
                JSONObject extensionObject = new JSONObject(objectContent.getString("extensionInfo"));
                if (extensionObject.has(ZJ_RTF_TV_MS_PAGE_CONTROL) && !TextUtils.isEmpty(extensionObject.getString(ZJ_RTF_TV_MS_PAGE_CONTROL))) {
                    //实现对非纯文本消息的控制，默认为0
                    zJTvMsPageControl = extensionObject.getString(ZJ_RTF_TV_MS_PAGE_CONTROL);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if ("6".equalsIgnoreCase(XmppUtil.getString(msgBody,"mode"))){
            if (!TextUtils.isEmpty(SessionService.getInstance().getSession().getTerminalConfigurationValue(ZJ_TXT_TV_MS_PAGE_CONTROL))){
                zJTvMsPageControl = SessionService.getInstance().getSession().getTerminalConfigurationValue(ZJ_TXT_TV_MS_PAGE_CONTROL);
            }
        }
        SuperLog.debug(TAG,"xmpp message zJTvMsPageControl="+zJTvMsPageControl+ ";\n" +
                "=0  //消息针对所有页面都展示\n" +
                "=1  //消息只对普通页面展示\n" +
                "=2  //消息只针对普通+老年页面展示\n" +
                "=3  //消息只针对普通+儿童页面展示");
        if (zJTvMsPageControl.equalsIgnoreCase("0") || isEpgDesktopTypeForShow(zJTvMsPageControl)){
            return true;
        }else{
            return false;
        }
    }

}
