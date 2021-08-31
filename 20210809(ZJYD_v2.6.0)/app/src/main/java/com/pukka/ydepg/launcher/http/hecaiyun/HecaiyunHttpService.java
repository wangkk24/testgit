package com.pukka.ydepg.launcher.http.hecaiyun;

import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.http.hecaiyun.data.QueryRequest;
import com.pukka.ydepg.launcher.util.AuthenticateManager;

import java.io.IOException;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HecaiyunHttpService {

    //使用HTTP/HTTPS
    private final static String HTTP_TYPE = "http";

    //外部要用 需要为静态
    public final static String HCY_QUERY_API = "queryBoothCarouselCont";

    public final String LOGIN_URL = HTTP_TYPE + "://aas.caiyun.feixin.10086.cn/tellin/thirdlogin.do";

    public final String QUERY_URL = HTTP_TYPE + "://photo.caiyun.feixin.10086.cn/andAlbum/openApi/" + HCY_QUERY_API;

    private HecaiyunServiceImpl service;

    public HecaiyunHttpService(HecaiyunServiceImpl service) {
        this.service = service;
    }

    @SuppressWarnings("checkResult")
    void login(){
        String ip    = AuthenticateManager.getInstance().getUserInfo().getIP();
        String port  = AuthenticateManager.getInstance().getUserInfo().getPort();

        //<![CDATA[<ip>117.148.130.129</ip><port>7214</port><ifOpenAccount>1</ifOpenAccount>]]>
        StringBuilder extInfo = new StringBuilder("<![CDATA[")
                .append("<ip>").append(ip).append("</ip>")
                .append("<port>").append(port).append("</port>")
                .append("<ifOpenAccount>1</ifOpenAccount>")
                .append("]]>");

        StringBuilder requestXmlString = new StringBuilder()
            .append("<root>")
                .append("<pintype>4</pintype>")
                .append("<cpid>354</cpid>")
                .append("<clienttype>732</clienttype>")
                .append("<dycpwd>").append(service.getToken()).append("</dycpwd>")
                .append("<version>").append(CommonUtil.getVersionName()).append("</version>")
                .append("<extInfo>").append(extInfo).append("</extInfo>")
            .append("</root>");

        //客户端对象
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/xml;charset=utf-8"), requestXmlString.toString());
        //请求对象 url 请求头 post方式
        Request req = new Request.Builder().url(LOGIN_URL).header("Content-Type", "application/xml").post(body).build();
        //异步发送请求并接受收响应
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SuperLog.error(HecaiyunServiceImpl.TAG,"Get Hecaiyun server response failed!");
                SuperLog.error(HecaiyunServiceImpl.TAG,e);
                service.onLoginBack(false);
            }

            @Override
            public void onResponse(Call call, final Response response) {
                String xmlResult = null;
                try{
                    if (response.isSuccessful()) {
                        //和彩云平台接口错误返回明文错误信息，成功返回加密数据
                        xmlResult = response.body().string();
                        service.getLoginResponseData(xmlResult);
                        service.onLoginBack(true);
                    } else {
                        SuperLog.error(HecaiyunServiceImpl.TAG,"Hecaiyun server response failed : " + response.code());
                        service.onLoginBack(false);
                    }
                } catch (Exception e){
                    SuperLog.error(HecaiyunServiceImpl.TAG,"Parse Hecaiyun server response exception!");
                    SuperLog.error(HecaiyunServiceImpl.TAG,e);
                    service.onLoginBack(false);
                }
            }
        });
    }


    void queryPic(String account){
        QueryRequest request = new QueryRequest(account);
        HttpApi.getInstance().getService().queryHcyPic(QUERY_URL,request)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                response -> {
                    if("0".equals(response.getResult().getResultCode())){
                        SuperLog.debug(HecaiyunServiceImpl.TAG, "Query hecaiyun picture successfully.");
                        service.onQueryBack(true,response.getListBoothCarouselCont());
                    } else {
                        SuperLog.debug(HecaiyunServiceImpl.TAG, "Query hecaiyun picture failed. Reason : " + response.getResult().getResultDesc());
                        service.onQueryBack(false,null);
                    }
                },
                throwable -> {
                    SuperLog.error(HecaiyunServiceImpl.TAG,"Query hecaiyun picture exception.");
                    SuperLog.error(HecaiyunServiceImpl.TAG,throwable);
                    service.onQueryBack(false,null);
                });
    }
}