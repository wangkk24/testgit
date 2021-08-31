/*
 *Copyright (C) 2017 广州易杰科技, Inc.
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
package com.pukka.ydepg.moudule.livetv.presenter.base;


import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: BasePresenter
 * @Package com.pukka.ydepg.moudule.livetv.presenter
 * @date 2017/12/26 10:00
 */
public class BasePresenter<T extends BaseView> {

  private T mView;

  public boolean isViewAttached() {
    return this.mView != null;
  }

  public void attachView(T view) {
    this.mView = view;
  }

  public void detachView() {
    this.mView = null;
  }

  public T getBaseView() {
    return this.mView;
  }

  public static <Z> ObservableTransformer<Z,Z> compose(final LifecycleTransformer<Z> lifecycle) {
    if(null!=lifecycle){
      return tObservable -> tObservable.subscribeOn(Schedulers.io())
          .unsubscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .compose(lifecycle);
    }
    return tObservable -> tObservable.subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }
}