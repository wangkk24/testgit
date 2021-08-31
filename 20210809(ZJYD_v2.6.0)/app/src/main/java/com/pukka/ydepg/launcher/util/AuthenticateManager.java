package com.pukka.ydepg.launcher.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.chinamobile.middleware.auth.CMCCAuthResult;
import com.chinamobile.middleware.auth.TokenManager;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.AppConfig;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.bean.UserInfo;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 认证工具类，管理状态
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.util.AuthenticateManager.java
 * @date: 2018-01-09 17:00
 * @version: V1.0 描述当前版本功能
 */
public class AuthenticateManager {
    private static final    String              TAG         = AuthenticateManager.class.getSimpleName();
    private static final    Uri                 CONTENT_URI = Uri.parse("content://com.chinamobile.middleware.auth.contentprovider/data");
    private        final    int      REQUEST_TOKEN_INTERVAL = 15000; //重新请求Token的时间间隔,单位ms
    private                 Timer               timer;
    private static volatile AuthenticateManager sInstance;
    private        volatile String              mToken;
    private                 boolean             isLogin;
    private                 UserInfo            mUserInfo;
    private                 TokenManager        mTokenManager;

    private AuthenticateManager() {}

    public static AuthenticateManager getInstance() {
        if (sInstance == null) {
            synchronized (AuthenticateManager.class) {
                if (sInstance == null)
                    sInstance = new AuthenticateManager();
            }
        }
        return sInstance;
    }

    public void createTokenManagerObject(){
        mTokenManager = new TokenManager(OTTApplication.getContext());
        SuperLog.info2SD(TAG,"New TokenManager object created.");
    }

    public String getLocalToken() {return mToken;}

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public void saveTokenAndUserInfo(String token){
        SuperLog.info2SDSecurity(TAG,"CurrentToken="+mToken+" NewToken="+token);
        mToken = token;

        //保存url地址
        UserInfo userInfo = getCurrentUserInfo();
        if(TextUtils.isEmpty(userInfo.getIP())||TextUtils.isEmpty(userInfo.getPort())){
            return;
        }
        saveVspUrl(userInfo.getIP(),userInfo.getPort());
    }

    //验证token有效性
    public void reportTokenStatus() {
        //setTokenState(TokenState.TIMEOUT);
        // 0 TOKEN有效; 01 TOKEN完整性错误; 02 TOKEN一致性错误; 03 TOKEN过期; 04 查询不到TOKEN信息
        byte[] status = new byte[]{0, 4};
        int result = mTokenManager.reportTokenStatus(status);
        SuperLog.info2SD(TAG," ========== Send reportTokenStatus request(result="+result+"), wait token update broadcast ========== ");
    }

    public void saveVspUrl(String ip, String port) {
        if(CommonUtil.isSelfAuthenticate()){
            mUserInfo = new UserInfo();
            mUserInfo.setIP(ip);
            mUserInfo.setPort(port);
        }
        String url = "http://" + ip + ":" + port;
        AppConfig appConfig = ConfigUtil.getConfig(OTTApplication.getContext());
        SuperLog.info2SDSecurity(TAG, "VSP URL : " + url);
        appConfig.setEdsURL(url);
        ConfigUtil.saveToFile(OTTApplication.getContext(), appConfig);
    }


    //从中间件获取当前的userInfo
    public UserInfo getCurrentUserInfo() {
        if(!CommonUtil.isSelfAuthenticate()){
            mUserInfo = getUserInfo(OTTApplication.getContext());
        }
        return mUserInfo;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    //获取用户信息
    private UserInfo getUserInfo(Context context) {
        UserInfo userInfo = new UserInfo();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                userInfo.setUserId(cursor.getString(1));//用户id
                userInfo.setIP(cursor.getString(2));    //ip地址
                userInfo.setPort(cursor.getString(3));  //端口号
            } else {
                SuperLog.error(TAG, "cursor为空");
            }
        } catch (Exception ex) {
            SuperLog.error(TAG,ex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        SuperLog.info2SDSecurity(TAG,"UserID="+userInfo.getUserId()+" IP="+userInfo.getIP()+" Port="+userInfo.getPort());
        return userInfo;
    }

    //获取token,如果获取失败则启动定时器循环去尝试
    public String getTokenTotal() {
        String token = getToken();
        if(TextUtils.isEmpty(token)){
            getTokenPeriodic();
        }
        return token;
    }

    //向认证APK请求TOKEN
    public String getToken(){
        CMCCAuthResult tokenResult = mTokenManager.getTokenResult();
        String token = tokenResult.getToken();
        SuperLog.info2SD(TAG, "Begin to get token. Token=" + token );
        SuperLog.info2SD(TAG, "CMCCAuth tokenResult=" + tokenResult.getStatusCode()+" (0:OK 1:ServiceUnavailable 2:TokenUnavailable 3:RemoteException 4:ParamError)");
        if (tokenResult.getStatusCode() == CMCCAuthResult.SC_OK && token != null) {
            return token;
        } else {
            SuperLog.error(TAG,"No token or tokenState incorrect.");
            return "";//这里不能返回null,调用处RxJava的onNext方法不予许传空对象
        }
    }

    //每隔[REQUEST_TOKEN_INTERVAL]ms请求一次Token
    private void getTokenPeriodic(){
        SuperLog.info2SD(TAG,REQUEST_TOKEN_INTERVAL/1000 + " seconds later will getToken(getToken) again.");
        if(timer == null){
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            public void run() {
                //尝试获取token
                if(TextUtils.isEmpty(getToken())){
                    //获取token失败
                    SuperLog.info2SD(TAG,"Retry getToken(getToken) failed, reportTokenStatus and wait for the auth broadcast.");
                    reportTokenStatus();      //发送reportTokenStatus请求到认证APK,等待广播
                    getTokenPeriodic();       //启动定时器,一段时间后再次尝试获取token
                } else {
                    //获取token成功,开始登陆
                    SuperLog.info2SD(TAG,"Retry getToken(getToken) successfully, and call MainActivity::onStateChange manually.");
                    Intent intent = new Intent();
                    intent.putExtra("status","online");
                    OTTApplication.getContext().getMainActivity().onStateChange(null,intent);
                }
            }
        }, REQUEST_TOKEN_INTERVAL);
    }

    //取消循环请求Token
    public void cancelReportToken(){
        if(timer != null){
            SuperLog.info2SD(TAG,"getTokenPeriodic() operation has been canceled.");
            timer.cancel();
            timer = null;
        }
    }
}