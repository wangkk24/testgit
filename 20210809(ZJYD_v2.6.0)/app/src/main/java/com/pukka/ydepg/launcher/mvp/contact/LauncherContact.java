package com.pukka.ydepg.launcher.mvp.contact;

import android.content.Context;

import com.pukka.ydepg.common.http.v6bean.v6node.Navigate;
import com.pukka.ydepg.launcher.bean.GroupElement;

import java.util.ArrayList;
import java.util.List;

/**
 * 桌面的Contact接口
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.mvp.contact.LauncherContact.java
 * @date: 2017-12-15 11:50
 * @version: V1.0 定义了LauncherView的方法和Presenter方法
 */
public interface LauncherContact {
    interface ILauncherView extends AuthenticateContact.IAuthenticateView {
        /**
         * 加载launcher，返回nav和fragment数据
         */
        void loadLauncherData(List<Navigate> navigates, boolean isFirstLoad);
        void loadLauncherSuccess(boolean success);
    }

    interface ILauncherPresenter<T extends AuthenticateContact.IAuthenticateView> extends AuthenticateContact.IAuthenticatePresenter<T> {
        void loginAndLoadLauncher(Context context);
        void loadLauncher(Context context);
    }
}
