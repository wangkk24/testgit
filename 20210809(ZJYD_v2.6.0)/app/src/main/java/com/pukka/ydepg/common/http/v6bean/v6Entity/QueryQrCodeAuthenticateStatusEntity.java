package com.pukka.ydepg.common.http.v6bean.v6Entity;

import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpService;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.entity.JsonBaseEntity;
import com.pukka.ydepg.common.http.listener.HttpOnNextListener;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryQrCodeStatusRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryQrCodeStatusResponse;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;

/**
 * 查询二维码认证状态
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6Entity.QueryQrCodeAuthenticateStatusEntity.java
 * @author:xj
 * @date: 2018-01-09 16:48
 */
public class QueryQrCodeAuthenticateStatusEntity extends JsonBaseEntity {

    private QueryQrCodeStatusRequest mRequest;

    public QueryQrCodeAuthenticateStatusEntity(HttpOnNextListener<QueryQrCodeStatusResponse> listener, RxAppCompatActivity rxAppCompatActivity) {
        super(listener, rxAppCompatActivity);
        setInterfaceName(HttpConstant.QUERYQRCODEAUTHENTICATESTATUS);
    }

    public void setRequest(QueryQrCodeStatusRequest request){
        mRequest = request;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        String url = HttpUtil.getVspUrl(HttpConstant.QUERYQRCODEAUTHENTICATESTATUS);
        return methods.queryQrCodeAuthenticateStatus(url,mRequest);
    }
}
