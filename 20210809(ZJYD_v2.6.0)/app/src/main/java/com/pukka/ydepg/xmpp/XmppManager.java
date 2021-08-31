package com.pukka.ydepg.xmpp;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Xml;
import android.webkit.MimeTypeMap;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.report.ubd.scene.UBDPushMessage;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.event.MessageDialogEvent;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.mytv.bean.BodyBean;
import com.pukka.ydepg.moudule.mytv.utils.MessageDataHolder;
import com.pukka.ydepg.xmpp.utils.XmppUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.xmpp.XmppManager.java
 * @author:xj
 * @date: 2018-01-11 16:24
 */
public class XmppManager {
    private static final String TAG              = XmppManager.class.getSimpleName();

    public  static final String XML_MESSAGE      = "xmpp_message";

    //展示最近的N条消息”及“展示最近N天的消息”
    public  static final String MESSAGE_COUNT    = "count";
    public  static final String MESSAGE_DAY      = "day";

    private static Date showLastDate;

    //使用系统下载器下载
    private final static String downloadPath = "/Android/data/";
    private static String path = Environment.getExternalStorageDirectory() + "/Android/data/";
    private static String mDownloadName = "message.xml";
    private static String mBody = "";
    private static long mTaskId;
    private static DownloadManager downloadManager;


    public static void downloadMessage(String body) {
        mBody = body;
        String downloadUrl;
        String downloadUrlNew = "";

        try {
            JSONObject object = new JSONObject(body);
            downloadUrl = object.getString("content");
            String edsUrl = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL();
            downloadUrlNew = downloadUrl.replace("http://nginxip:nginxport", edsUrl);
            SuperLog.debug(TAG,"downloadUrl===="+downloadUrlNew);
        } catch (JSONException e) {
            SuperLog.error(TAG,e);
        }

        File file = new File(path + mDownloadName);
        if (!file.exists()) {
            SuperLog.debug(TAG,"删除文件失败:" + path + mDownloadName + "不存在！");
        } else {
            if (file.isFile()){
                if (file.delete()){
                    SuperLog.debug(TAG,"删除文件成功:" + path + mDownloadName + ";");
                }else{
                    SuperLog.debug(TAG,"删除文件失败:" + path + mDownloadName + ";");
                }
            }
        }

        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrlNew));
        request.setAllowedOverRoaming(false);//漫游网络是否可以下载

        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downloadUrlNew));
        request.setMimeType(mimeString);

        //sdcard的目录下的download文件夹，必须设置
        request.setDestinationInExternalPublicDir(downloadPath, mDownloadName);
        //request.setDestinationInExternalFilesDir(),也可以自己制定下载路径

        //将下载请求加入下载队列
        downloadManager = (DownloadManager) OTTApplication.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        //加入下载队列后会给该任务返回一个long型的id，
        //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
        mTaskId = downloadManager.enqueue(request);

        //注册广播接收者，监听下载状态
        OTTApplication.getContext().registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    //广播接受者，接收下载状态
    private static BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkDownloadStatus();//检查下载状态
        }
    };

    //检查下载状态
    private static void checkDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mTaskId);//筛选下载任务，传入任务ID，可变参数
        Cursor c = downloadManager.query(query);
        if (null != c && c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    SuperLog.debug(TAG, "====STATUS_PAUSED====下载暂停");
                    break;
                case DownloadManager.STATUS_PENDING:
                    SuperLog.debug(TAG, "====STATUS_PENDING====下载延迟");
                    break;
                case DownloadManager.STATUS_RUNNING:
                    SuperLog.debug(TAG, "====STATUS_RUNNING====正在下载");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    SuperLog.debug(TAG, "====STATUS_SUCCESSFUL====下载完成");
                    readSDFile(mBody);
                    break;
                case DownloadManager.STATUS_FAILED:
                    SuperLog.debug(TAG, "====STATUS_FAILED====下载失败");
                    break;
                default:
                    SuperLog.error(TAG,"====Undefined download status====");
                    break;
            }
        }
    }

    /*
    * 读取下载的文件
    * 滚动内容显示
    * */
    private static void readSDFile(String body){
        try {
            File file = new File(path + mDownloadName);
            if (!file.exists()) {
                SuperLog.debug(TAG, "读取的文件不存在");
                return;
            }

            FileInputStream fin = new FileInputStream(file);

            // 获得pull解析器对象
            XmlPullParser parser = Xml.newPullParser();
            // 指定解析的文件和编码格式
            parser.setInput(fin, "utf-8");

            int eventType = parser.getEventType(); // 获得事件类型

            String title = "";
            String content = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName(); // 获得当前节点的名称
                switch (eventType) {
                    case XmlPullParser.START_TAG: // 当前等于开始节点 <person>
                        if ("GetMsgTaskResp".equals(tagName)) { // <persons>
                        } else if ("RollMessage".equals(tagName)) { // <person id="1">
                            //id = parser.getAttributeValue(null, "id");
                        } else if ("title".equals(tagName)) { // <name>
                            title = parser.nextText();
                        }else if ("content".equals(tagName)) { // <age>
                            content = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG: // </persons>
                        if ("GetMsgTaskResp".equals(tagName)) {
                            SuperLog.debug(TAG,"title=="+title+";content=="+content);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next(); // 获得下一个事件类型
            }

            SuperLog.debug(TAG,"title=="+title+";content=="+content);

            fin.close();

            /*
            * 滚动消息网址转txt完成并存入
            * */
            BodyBean bodyBean = JsonParse.json2Object(body,BodyBean.class);
            bodyBean.setContent(content);
            //2.保存在本地供消息通知列表查询
            SharedPreferenceUtil.getInstance().saveNotification(JsonParse.object2String(bodyBean));
            //1.TVMS弹窗提示
            TvmsView.getInstance(OTTApplication.getContext()).addNewMessage(JsonParse.object2String(bodyBean)+"");
        } catch (Exception e) {
            SuperLog.error(TAG,e);
        }
    }

    // 处理推送消息
    @SuppressLint("SimpleDateFormat")
    public static void handlePushMessage(String body) {
        try {
            //重新构建消息,增加receivingTime字段
            String receivedTime = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(new Date());
            String bodyResult = body.substring(0, body.length() - 1) + ",\"receivingTime\":\"" + receivedTime + "\"}";
            SuperLog.debug(TAG, "BodyResult = " + bodyResult);
            JSONObject msgBody = new JSONObject(bodyResult);
            //判断消息类型是否正确 消息是否过期
            if (isPushMessageFormatValid(msgBody) && isPushMessageTimeValid(msgBody)) {
                if (!msgBody.getString("mode").equals("0")) {
                    //mode!=0时直接保存信息
                    getShowMessage(bodyResult, msgBody);
                    long delayTime = getDelayTime();
                    if (MessageDataHolder.get().getMessageAllList().size() > 0 &&
                            (null == showLastDate || (new Date().getTime() - showLastDate.getTime()) > delayTime)) {
                        showLastDate = new Date();
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            //推送消息显示弹框
                            SuperLog.info2SD(TAG,"EventBus post [MessageDialogEvent] event");
                            MessageDialogEvent messageDialogEvent = new MessageDialogEvent();
                            messageDialogEvent.setShow(true);
                            EventBus.getDefault().post(messageDialogEvent);
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

    private static boolean isPushMessageFormatValid(JSONObject object){
        boolean isValid = false;
        //只处理type = 0  mode = 0、5、6 的消息
        if ("0".equals(XmppUtil.getString(object,"type"))) {
            if ("0".equals(XmppUtil.getString(object, "mode")) || "6".equals(XmppUtil.getString(object, "mode"))) {
                isValid = true;
            } else if ("5".equals(XmppUtil.getString(object, "mode"))) {
                try {
                    //String postUrl = object.getJSONObject("content").getJSONObject("extensionInfo").getString("postURL");
                    String strExtensionInfo = object.getJSONObject("content").getString("extensionInfo");
                    JSONObject objExtensionInfo = new JSONObject(strExtensionInfo);
                    String postUrl = objExtensionInfo.getString("postURL");
                    if (!TextUtils.isEmpty(postUrl)) {
                        isValid = true;
                    }
                } catch (Exception e) {
                    SuperLog.error(TAG, e);
                }
            }
        }
        return isValid;
    }

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
        return false;
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

    //获取需要弹框展示的消息
    private static void getShowMessage(String bodyResult, JSONObject msgBody) {
        try {
            SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date nowDate = new Date();
            Date sendDate = null;
            if ( "5".equals(XmppUtil.getString(msgBody,"mode")) ){
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
                        return;
                    }
                }

                //终端参数配置可显示的条数
                String messageCount = messageFrequencyControlMap.get(MESSAGE_COUNT);

                //如果配置消息显示频次为0，则不显示推送消息
                if (!TextUtils.isEmpty(messageCount) && messageCount.equalsIgnoreCase("0")){
                    //return false;
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

    //{"isVR":0,"mediaType":"98","playUrl":"playType%3DEPGChange%26EPGMode%3DnomalMode"}
    //playUrl解码后格式 playType=EPGChange&EPGMode=nomalMode
    //EPGMode取值nomalMode/childrenMode/agedMode
    static void switchDesktop(String playUrl){
        if(TextUtils.isEmpty(playUrl)){
            SuperLog.error(TAG,"Switch desktop message is null.");
        }

        try{
            String switchMsg = URLDecoder.decode( playUrl, "UTF-8" );
            int startPos = switchMsg.indexOf("EPGMode=");
            String mode = switchMsg.substring(startPos+"EPGMode=".length());
            switch (mode){
                case "nomalMode":
                case "normalMode":
                    SuperLog.info2SD(TAG,"Switch desktop to normal");
                    XmppUtil.loadNewDesktop(com.pukka.ydepg.common.constant.Constant.DesktopType.NORMAL);
                    break;
                case "childrenMode":
                    SuperLog.info2SD(TAG,"Switch desktop to child");
                    XmppUtil.loadNewDesktop(com.pukka.ydepg.common.constant.Constant.DesktopType.CHILD);
                    break;
                case "agedMode":
                    SuperLog.info2SD(TAG,"Switch desktop to simple");
                    XmppUtil.loadNewDesktop(com.pukka.ydepg.common.constant.Constant.DesktopType.SIMPLE);
                    break;
                default:
                    SuperLog.error(TAG,"Unknown switch desktop [EPGMode].");
                    break;

            }
        } catch (Exception e){
            SuperLog.error(TAG,e);
            SuperLog.error(TAG,"Switch desktop failed.");
        }
    }
}