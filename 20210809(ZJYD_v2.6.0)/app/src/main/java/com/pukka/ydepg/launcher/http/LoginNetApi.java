package com.pukka.ydepg.launcher.http;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.http.interceptor.LogInterceptor;
import com.pukka.ydepg.common.utils.ConfigUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 登录相关的api
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.http.LoginNetApi.java
 * @date: 2018-01-08 15:52
 * @version: V1.0 描述当前版本功能
 */
public class LoginNetApi {

    private static final String BASE_URL = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL();

    private static volatile LoginNetApi sInstance;

    private LoginNetService service;

    public LoginNetService getService() {
        return service;
    }

    public void setService(LoginNetService service) {
        this.service = service;
    }

    public static LoginNetApi getInstance() {
        if (sInstance == null) {
            synchronized (LoginNetApi.class) {
                if (sInstance == null)
                    sInstance = new LoginNetApi();
            }
        }
        return sInstance;
    }

    private LoginNetApi() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(Constant.TIMEOUT_MILLIS, TimeUnit.SECONDS);
        builder.writeTimeout(Constant.READ_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(Constant.READ_TIMEOUT, TimeUnit.SECONDS);

        //设置消息体的日志
        LogInterceptor mLogInterceptor = new LogInterceptor();
        builder.addInterceptor(mLogInterceptor);

        //安全整改，认证方式不安全，当前改为不支持HTTPS认证
//        //新Https认证
//        HttpsUtil.SSLParams sslParams = HttpsUtil.getSslSocketFactory(OTTApplication.getContext(), null, 0, null);
//        builder.sslSocketFactory(sslParams.getSslSocketFactory(), sslParams.getTrustManager());
//        builder.hostnameVerifier(HttpsUtil.getHostnameVerifier());

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        service = retrofit.create(LoginNetService.class);
    }
}