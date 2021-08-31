/*
 *Copyright (C) 2018 广州易杰科技, Inc.
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package com.pukka.ydepg.common.http;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.interceptor.LogInterceptor;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.netutil.HttpsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * HttpApi
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: HttpApi
 * @Package com.pukka.ydepg.common.http
 * @date 2018/01/11 17:59
 */
public class HttpApi {

    /**
     * 超时时间
     */
    private static final int DEFAULT_TIMEOUT = Constant.TIMEOUT_MILLIS;
    /**
     * BaseURL
     */
    private static final String BASE_URL =
            ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                    + "/" + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3;

    private static HttpApi mHttpApi;

    public HttpService getService() {
        return service;
    }

    public void setService(HttpService service) {
        this.service = service;
    }

    private HttpService service;

    private HttpApi() {
        createHttpApiService(createOkHttp());
    }

    public static synchronized HttpApi getInstance() {
        if (null == mHttpApi) {
            mHttpApi = new HttpApi();
        }
        return mHttpApi;
    }

    private OkHttpClient createOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //builder.followRedirects(false);  //禁制OkHttp的重定向操作,自定义重定向处理,升级流程需要
        builder.addInterceptor(new LogInterceptor());
        //https
        List<Integer> certificateIds=new ArrayList<>();
        certificateIds.add(R.raw.huaweissp);
        HttpsUtil.SSLParams sslParams = HttpsUtil.getSslSocketFactory(OTTApplication.getContext(), certificateIds, 0, null);
        builder.sslSocketFactory(sslParams.getSslSocketFactory(), sslParams.getTrustManager());
        builder.hostnameVerifier(HttpsUtil.getHostnameVerifier());
        return builder.build();
    }

    private void createHttpApiService(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        service = retrofit.create(HttpService.class);
    }
}