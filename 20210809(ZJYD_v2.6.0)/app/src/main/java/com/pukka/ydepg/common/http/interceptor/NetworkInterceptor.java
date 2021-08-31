package com.pukka.ydepg.common.http.interceptor;

import com.pukka.ydepg.common.upgrade.UpgradeManager;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

//根据服务器要求,为升级请求修改头域Content-Type为application/x-www-form-urlencoded,否则升级接口不通
//由于HTTP的BridgeInterceptor会修改Content-Type,因此需要在其后修改Content-Type
public class NetworkInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url();
        String requestUrl = "";
        if (url != null) {
            requestUrl = url.toString();
        }

        if(UpgradeManager.isUpgradeConfigUrl(requestUrl)){
            //华为升级服务器头域设置
            request = request.newBuilder()
                    .removeHeader("User-Agent")      //系统默认BridgeInterceptor添加
                    .removeHeader("Accept-Encoding") //系统默认BridgeInterceptor添加
                    .removeHeader("Connection")      //系统默认BridgeInterceptor添加
                    .removeHeader("Cookie")          //自定义日志拦截器添加
                    .removeHeader("Set-Cookie")      //自定义日志拦截器添加
                    .removeHeader("authorization")   //自定义日志拦截器添加
                    .header("Content-Type", UpgradeManager.CONTENT_TYPE)  //华为升级服务器要求必传字段
                    .build();
        }

        Response response = chain.proceed(request);
        return response;
    }
}
