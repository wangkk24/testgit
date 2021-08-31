package com.pukka.ydepg.common.report.ubd;

import android.text.TextUtils;

import com.huawei.ott.sdk.log.DebugLog;
import com.huawei.ott.sdk.ubd.Common;
import com.huawei.ott.sdk.ubd.MsaUBD;
import com.huawei.ott.sdk.ubd.SwitchPage;
import com.huawei.ott.sdk.ubd.UBDConfig;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.session.SessionService;

/**
 * Created by Liu on 2018/5/10.
 */
public class UBDService {

    public  static final String TAG = UBDService.class.getSimpleName();

    public  static final int UBD_INIT_SUCCESS = 0;

    private static final String UBD_DEFAULT_PASSWORD = "000000";

    private static final String UBD_DEFAULT_TYPE = "HW_STB_EPGUI";

    private static final String UBD_SERVER_CERT = "HuaweiCA.pem";

    private static int configUBD(
            String appID,
            String appPassword,
            String serverURL,
            String type,
            String deviceID,
            String profileSN){
        StringBuffer sb = new StringBuffer("UBD init param:")
                .append("\n\tappID      =").append(appID)
                .append("\n\tappPassword=").append(appPassword)
                .append("\n\tserverURL  =").append(serverURL)
                .append("\n\ttype       =").append(type)
                .append("\n\tdeviceID   =").append(deviceID)
                .append("\n\tuserID     =").append(profileSN);
        SuperLog.info2SDSecurity(TAG,sb.toString());
        UBDConfig ubdConfig = new UBDConfig();
        ubdConfig.setAppId(appID);
        ubdConfig.setAppPassword(appPassword);
        ubdConfig.setServerURL(serverURL);
        ubdConfig.setDeviceType(type);
        ubdConfig.setDeviceId(deviceID);
        ubdConfig.setUserId(profileSN);
        return MsaUBD.getInstance().init(ubdConfig);
    }

    static void init(){
        //初始化日志函数，用于日志跟踪
        DebugLog.initLogcatLevel(DebugLog.DEBUG);

        int result = MsaUBD.loadLibraries(OTTApplication.getContext());
        if ( UBD_INIT_SUCCESS != result ){
            SuperLog.error(TAG,"MSA UBD loadLibraries failed.");
            return;
        }

        //获取UBD终端参数
        String userId = SessionService.getInstance().getSession().getProfileSN();
        if (TextUtils.isEmpty(userId)) {
            SuperLog.error(TAG,"Get MSA UBD init param(session) [userId] failed.");
            return;
        }

        String deviceId = SessionService.getInstance().getSession().getDeviceId();
//        if (TextUtils.isEmpty(deviceId)) {
//            SuperLog.error(TAG,"Get MSA UBD init param(session) [deviceID] failed.");
//            return UBD_INIT_FAILED;
//        }

        String appPassword = SessionService.getInstance().getSession().getTerminalConfigurationValue("UBD_appPassword");
        if (TextUtils.isEmpty(appPassword)) {
            SuperLog.errorSecurity(TAG,"Get MSA UBD init param(config) [appPassword] failed. Use default password : " + UBD_DEFAULT_PASSWORD);
            appPassword = UBD_DEFAULT_PASSWORD;
        }

        String type = SessionService.getInstance().getSession().getTerminalConfigurationValue("UBD_type");
        if (TextUtils.isEmpty(type)) {
            SuperLog.error(TAG,"Get MSA UBD init param(config) [type] failed. Use default type : " + UBD_DEFAULT_TYPE);
            type = UBD_DEFAULT_TYPE;
        }

        String serverURL = SessionService.getInstance().getSession().getTerminalConfigurationValue("UBD_serverURL");
        if (TextUtils.isEmpty(serverURL)) {
            SuperLog.error(TAG,"Get MSA UBD init param(config) [serverURL] failed. Use default server URL.");
            serverURL = "https://117.148.130.114:37151/EPGLogServer/LogTracker";
        }

        if(serverURL != null && serverURL.contains("https://")){
            // 如果UBD SDK与大数据平台使用https交互，需要设置证书文件给SDK，
            // 证书文件由服务端生成，并将pem格式证书给客户端，开发者将证书内置到应用assets的根目录下。
            MsaUBD.getInstance().setCertPath(OTTApplication.getContext(),UBD_SERVER_CERT);
        }

        String appId = SessionService.getInstance().getSession().getTerminalConfigurationValue("UBD_appId");
        if (TextUtils.isEmpty(appId)) {
            SuperLog.error(TAG,"Get MSA UBD init param(config) [appID] failed.");
            return;
        }

        result = configUBD(appId,appPassword,serverURL,type,deviceId,userId);
        if(UBD_INIT_SUCCESS != result){
            SuperLog.error(TAG,"Initialize MSA UBD function failed.");
            return;
        }


        boolean switchResult = MsaUBD.getInstance().setUBDSwitch(true);
        SuperLog.info2SD(TAG,"Call [MsaUBD]setUBDSwitch(true) result="+switchResult);

        setUBDListener();
        SuperLog.info2SD(TAG,"Initialize MSA UBD function succeeded.");
    }

    //日志关键字 recordBehavior(),Search
    //记录用户搜索关键字
    public static void recordCommon(Common common){
        if(MsaUBD.getInstance().getUBDStatus()){
            if(0==MsaUBD.getInstance().recordBehavior(common)){
                StringBuilder msg = new StringBuilder("recordBehavior(Common) success. ")
                        .append("\t\nActionType   = ").append(common.getActionType())
                        .append("\t\nExtraData    = ").append(common.getlabel());
                SuperLog.info2SDDebug(TAG,msg.toString());
            } else {
                SuperLog.error(TAG,"recordBehavior(Common) failed");
            }
        }
    }

    //日志关键字 recordBehavior(),recordBehavior
    //记录页面(Activity)跳转
    public static void recordSwitchPage(String pageFrom,String pageTo,String extensionFields){
        //UBD开关
        if(MsaUBD.getInstance().getUBDStatus()){
            //判断上报数据有效性
            if(TextUtils.isEmpty(pageFrom) || TextUtils.isEmpty(pageTo) || TextUtils.isEmpty(extensionFields)){
                SuperLog.error(TAG,"Some parameter is null, no need to report UBD switch page.");
                return;
            }

            SwitchPage switchPage = new SwitchPage();
            switchPage.setPageFrom(pageFrom);
            switchPage.setPageTo(pageTo);
            switchPage.setExtensionFields(extensionFields);
            if(0==MsaUBD.getInstance().recordBehavior(switchPage)){
                StringBuilder msg = new StringBuilder("recordBehavior(switchPage) success. ")
                        .append("\t\nFromActivity = ").append(pageFrom)
                        .append("\t\nToActivity   = ").append(pageTo)
                        .append("\t\nExtraData    = ").append(extensionFields);
                SuperLog.info2SDDebug(TAG,msg.toString());
            } else {
                SuperLog.error(TAG,"recordBehavior(switchPage) failed");
            }
        }
    }

    //日志关键字 recordBehavior(),Search
    //记录用户搜索关键字
    public static void recordSearchKey(String keyword){
        //TODO 除浏览页面的UBD其他UBD屏蔽
//        if(MsaUBD.getInstance().getUBDStatus()){
//            SuperLog.debug(TAG,"Begin to report UBD info [Search]");
//            Search search = new Search();
//            search.setKeyword(keyword);
//            if(0!=MsaUBD.getInstance().recordBehavior(search)){
//                SuperLog.error(TAG,"recordBehavior(search) failed");
//            }
//        }
    }

    //设置UBD listener
    public static void setUBDListener(){
        DebugLog.debug(TAG,"Set UBD listener.");
        MsaUBD.getInstance().setUBDListener((serverURL,requestBody,httpStatus)-> {
            StringBuffer sb = new StringBuffer("Received UBD behavior callback. Details is as followed:>>>")
                    .append("\n\tHttp status = ").append(httpStatus)
                    .append("\n\tServerURL   = ").append(serverURL)
                    .append("\n\tRequestBody = ").append(requestBody);
            if( httpStatus.equals("200") ){
                //SuperLog.debug(TAG,sb.toString());
                SuperLog.debug(TAG,"Received UBD behavior callback,HTTP CODE="+httpStatus);
            }else{
                SuperLog.error(TAG,sb.toString());
            }
        });
    }
}

//           ubdsdk已经把发送方法放到子线程了 因此不需要，暂时注释
//           /*Disposable disposable = */Observable.create(new ObservableOnSubscribe<Integer>() {
//                @Override
//                public void subscribe(ObservableEmitter<Integer> emitter){
//                    emitter.onNext(MsaUBD.getInstance().recordBehavior(switchPage));
//                }
//            }).subscribeOn(Schedulers.from(ThreadManager.getInstance().getUBDSingleThread()))
//                    .unsubscribeOn(Schedulers.from(ThreadManager.getInstance().getUBDSingleThread())).subscribe(new Consumer<Integer>() {
//                @Override
//                public void accept(Integer result) {
//                    if(0 != result){
//                        SuperLog.error(TAG,"recordBehavior(switchPage) failed");
//                    } else {
//                        StringBuilder msg = new StringBuilder("recordBehavior(switchPage) success. FromActivity=[")
//                                .append(pageFrom)
//                                .append("] ToActivity=[")
//                                .append(pageTo)
//                                .append("] ExtraData=[")
//                                .append(extensionFields)
//                                .append("]");
//                        SuperLog.debug(TAG,msg.toString());
//                    }
//                }
//            });
