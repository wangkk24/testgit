package com.pukka.ydepg.launcher.http.hecaiyun;

import android.text.TextUtils;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.http.hecaiyun.data.BoothCarouselCont;
import com.pukka.ydepg.launcher.http.hecaiyun.data.LoginData;
import com.pukka.ydepg.launcher.util.AuthenticateManager;

import org.simpleframework.xml.core.Persister;

import java.util.List;

public class HecaiyunServiceImpl{

    final static String TAG = HecaiyunServiceImpl.class.getSimpleName();

    private HecaiyunHttpService httpService;

    static LoginData getLoginData() {
        return loginData;
    }

    public static void setLoginData(LoginData loginData) {
        HecaiyunServiceImpl.loginData = loginData;
    }

    private static LoginData loginData;

    private OnResponse callback;
    public interface OnResponse{
        void onQueryListener(List<BoothCarouselCont> listBoothCarouselCont);
    }

    public HecaiyunServiceImpl() {
        this.httpService = new HecaiyunHttpService(this);

    }

    //获取登录和彩云平台时的入参的本地token
    String getToken(){
        return AuthenticateManager.getInstance().getLocalToken();
    }

    //查询和彩云平台图片
    public void queryPic(OnResponse callback) {
        this.callback    = callback;
        httpService.login();
    }

    void onLoginBack(boolean isLoginSucceed){
        if(isLoginSucceed){
            //登录成功,进行查询和彩云图片
            httpService.queryPic(loginData.getAccount());
        } else {
            //登录失败
            SuperLog.error(TAG,"Login Hecaiyun platform failed.");
            callback.onQueryListener(null);
        }
    }

    //解密登录接口返回的加密XML格式数据
    void getLoginResponseData(String response){
        if(TextUtils.isEmpty(response)){
            return;
        }

        try{
            if(response.startsWith("<root>") || response.startsWith("<?xml version=")){
                //明文说明接口返回错误
                SuperLog.info2SD(HecaiyunServiceImpl.TAG,"Hecaiyun response is as followed:>>>\n\t" + response);
            } else {
                //密文说明接口返回正常数据
                String sKey = DecodeUtil.getLoginClientKet(getToken());
                String decryptResponse = DecodeUtil.decryptAES(response,sKey);
                parseXml(decryptResponse);
                SuperLog.info2SD(HecaiyunServiceImpl.TAG,"Hecaiyun response is as followed:>>>\n\t" + decryptResponse);
            }
        } catch (Exception e){
            SuperLog.error(HecaiyunServiceImpl.TAG,e);
            SuperLog.error(HecaiyunServiceImpl.TAG,e);
        }
    }

    //反序列化login接口返回的XML格式登录数据
    void parseXml(String xmlString){
        //Persister序列化、持久化---simple-xml-2.7.1.jar里提供的[是其他人已经写好分装好的]
        Persister persister = new Persister();//实例化一个Persister
        try {
            loginData = persister.read(LoginData.class,xmlString);   //读
        } catch (Exception e){
            SuperLog.error(TAG,"Parse xml string exception.");
            SuperLog.error(TAG,e);
        }
    }

    void onQueryBack(boolean isQuerySucceed, List<BoothCarouselCont> listBoothCarouselCont){
        callback.onQueryListener(listBoothCarouselCont);
    }

    //请求头： "Authorization" : "Basic " + authorizationStrOld
    //account 登录接口返回的account
    //token   登录接口返回的token
    public static String getRequestHeaderAuthorization(){
        String account = loginData.getAccount();
        String token   = loginData.getToken();
        String authorization = "ph5:" + account + ":" + token;
        String authorizationStrOld  = new String(Base64Util.encodeBase64(authorization.getBytes()));
        return "Basic " + authorizationStrOld;
    }
}