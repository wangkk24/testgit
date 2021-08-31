package com.pukka.ydepg.launcher.mvp.contact;

import com.pukka.ydepg.common.http.v6bean.v6node.Navigate;
import com.pukka.ydepg.launcher.bean.GroupElement;

import java.util.ArrayList;
import java.util.List;

/**
 * 再次写用途
 *
 * @FileName: com.pukka.ydepg.launcher.mvp.contact.ChildLauncherContact.java
 * @author: luwm
 * @data: 2018-05-16 16:39
 * @Version V1.0 <描述当前版本功能>
 */
public interface ChildLauncherContact {
    interface IChildLauncherView extends AuthenticateContact.IAuthenticateView {
        /**
         * 加载launcher，返回nav和fragment数据
         */
        void loadLauncherData(List<Navigate> navigates, ArrayList<List<GroupElement>> groupElementsList);
        void loadLauncherSuccess(boolean success);

        void updateLauncher();

        void downLoadLauncher();
    }

    interface IChildLauncherPresenter<T extends AuthenticateContact.IAuthenticateView> extends AuthenticateContact.IAuthenticatePresenter<T> {
        void loadLauncher();
    }
}
