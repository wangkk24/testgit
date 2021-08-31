package com.pukka.ydepg.moudule.search.presenter;


import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.moudule.search.presenter.BasePresenter.java
 * @author:xj
 * @date: 2018-01-24 15:47
 */

public class BasePresenter {

    public static final <Z> LifecycleTransformer<Z> bindToLifecycle(RxAppCompatActivity mRxAppCompatActivity ) {
        if (null != mRxAppCompatActivity){
            return mRxAppCompatActivity.bindToLifecycle();
        }
        return null;
    }

    public static <Z> ObservableTransformer<Z,Z> compose(final LifecycleTransformer<Z> lifecycle) {
        return new ObservableTransformer<Z, Z>() {
            @Override
            public ObservableSource<Z> apply(@NonNull Observable<Z> upstream) {
                return upstream.subscribeOn(Schedulers.io()).doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {

                    }
                }).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).compose(lifecycle);
            }
        };
    }
}
