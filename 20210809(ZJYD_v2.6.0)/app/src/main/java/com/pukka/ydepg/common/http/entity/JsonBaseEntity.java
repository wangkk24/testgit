package com.pukka.ydepg.common.http.entity;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.http.listener.HttpOnNextListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


/**
 * Created by xxt on 2016/11/7.
 */
public abstract class JsonBaseEntity<T> extends  BaseEntity implements Function<T, T> {

    private static final String TAG = "JsonBaseEntity";

    public JsonBaseEntity(HttpOnNextListener listener, RxAppCompatActivity rxAppCompatActivity) {
        super(listener, rxAppCompatActivity);
    }

    @Override
    public T apply(@NonNull T httpResult) throws Exception {
        if(httpResult != null)
            SuperLog.debug(TAG,"httpResult class :"+httpResult.getClass().getSimpleName());

        return httpResult;
    }


}
