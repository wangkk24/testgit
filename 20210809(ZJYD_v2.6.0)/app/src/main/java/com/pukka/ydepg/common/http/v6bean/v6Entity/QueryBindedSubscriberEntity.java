package com.pukka.ydepg.common.http.v6bean.v6Entity;

import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpService;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.entity.JsonBaseEntity;
import com.pukka.ydepg.common.http.listener.HttpOnNextListener;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryBindedSubscriberRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryBindedSubscriberResponse;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6Entity.QueryBindedSubscriberEntity.java
 * @author:xj
 * @date: 2018-01-09 17:43
 */
public class QueryBindedSubscriberEntity extends JsonBaseEntity {
    private QueryBindedSubscriberRequest mRequest;

    public QueryBindedSubscriberEntity(HttpOnNextListener<QueryBindedSubscriberResponse> listener, RxAppCompatActivity rxAppCompatActivity) {
        super(listener, rxAppCompatActivity);
        setInterfaceName(HttpConstant.QUERYBINDEDSUBSCRIBER);
    }
    
    public void setRequest(QueryBindedSubscriberRequest request){
        mRequest = request;
    }

    /**
     * 设置请求的方法
     *
     * @param methods
     * @return
     */
    @Override
    public Observable getObservable(HttpService methods) {
        String url = HttpUtil.getVspUrl(HttpConstant.QUERYQRCODEAUTHENTICATESTATUS);
        return methods.qeryBindedSubscriber(url,mRequest);
    }
}