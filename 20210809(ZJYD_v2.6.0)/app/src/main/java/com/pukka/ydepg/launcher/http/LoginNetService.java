package com.pukka.ydepg.launcher.http;


import com.pukka.ydepg.annotation.RetrofitBuilder;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryLauncherRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryLauncherResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryPHMLauncherListRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryPHMLauncherListResponse;
import com.pukka.ydepg.launcher.bean.request.LoginRequest;
import com.pukka.ydepg.launcher.bean.request.QueryCustomizeConfigRequest;
import com.pukka.ydepg.launcher.bean.request.ZJLoginRequest;
import com.pukka.ydepg.launcher.bean.response.LoginResponse;
import com.pukka.ydepg.launcher.bean.response.QueryCustomizeConfigResponse;
import com.pukka.ydepg.launcher.bean.response.ZJLoginResponse;


import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;


/**
 * 登录相关的接口
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.http.LoginNetService.java
 * @date: 2018-01-08 15:34
 * @version: V1.0 描述当前版本功能
 */

@RetrofitBuilder(httpAPiName = LoginNetApi.class)
public interface LoginNetService {
    /**
     * 登录调度（Java）
     *
     * @param url
     * @param loginRequest
     * @return
     */
    @POST()
    Observable<LoginResponse> login(@Url String url, @Body LoginRequest loginRequest);


    /**
     * 登录认证
     * @param url
     * @param zjLoginRequest
     * @return
     */
    @POST()
    Observable<ZJLoginResponse> zjLogin(@Url String url, @Body ZJLoginRequest zjLoginRequest);

    /**
     * 用户配置信息
     * @param url
     * @param queryCustomizeConfigRequest
     * @return
     */
    @POST()
    Observable<QueryCustomizeConfigResponse> queryCustomizeConfig(@Url String url, @Body QueryCustomizeConfigRequest queryCustomizeConfigRequest);
    @POST()
    Observable<QueryLauncherResponse> queryLuancher(@Url String url, @Body QueryLauncherRequest request);

    @POST()
    Observable<QueryPHMLauncherListResponse> queryPHMLauncherList(@Url String url,@Body QueryPHMLauncherListRequest request);

}
