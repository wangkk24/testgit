package com.pukka.ydepg.launcher.mvp.contact;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * mvp架构接口
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.mvp.contact.IBaseContact.java
 * @date: 2017-12-15 11:37
 * @version: V1.0 mvp架构接口
 */
public interface IBaseContact {
    interface IBasePresenter<T extends IBaseView> {

        /**
         * 添加view
         * @param view
         */
        void attachView(T view);

        /**
         * 移除view
         */
        void detachView();
    }

    interface IBaseView {

        /**
         * 配合RxLifecycle使用
         * @param <T>
         * @return
         */
        <T> LifecycleTransformer<T> bindToLife();
    }
}