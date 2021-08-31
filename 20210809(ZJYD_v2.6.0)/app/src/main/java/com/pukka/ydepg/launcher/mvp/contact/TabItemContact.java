package com.pukka.ydepg.launcher.mvp.contact;

import android.content.Context;

import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6response.GetVODDetailResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryBookmarkResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryFavoriteResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryLauncherResponse;
import com.pukka.ydepg.launcher.bean.GroupElement;
import com.pukka.ydepg.launcher.bean.node.SubjectVodsList;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.launcher.util.RxCallBack;

import java.util.List;
import java.util.Map;

/**
 * 首页tab页动态生成fragment的contact接口
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.mvp.contact.TabItemContact.java
 * @date: 2017-12-19 10:10
 * @version: V1.0 首页tab页动态生成fragment的contact接口
 */


public interface TabItemContact extends IBaseContact {

    interface ITabItemView extends IBaseContact.IBaseView {
        /**
         * 加载groupType为3，6，7的数据
         * @param subjectVODLists
         */
        void loadVODData(List<SubjectVodsList> subjectVODLists);
    }

    interface ITabItemPresenter extends IBaseContact.IBasePresenter {
        /**
         * 查询queryHomeEpg接口信息，用于GroupType为3，6，7
         * @param navId
         * @param groupElements
         */
        void queryHomeEpg(String navId, List<GroupElement> groupElements, Context context);

        /**
         * 查询视频详情
         * @param vodId
         * @param callBack
         */
        void getVODDetail(String vodId, RxCallBack<GetVODDetailResponse> callBack);

        /**
         * 查询播放地址
         * @param vodDetail
         * @param callBack
         */
        void getPlayUrl(VODDetail vodDetail, RxCallBack<PlayVODResponse> callBack);

        void queryBookMark(RxCallBack<QueryBookmarkResponse> callBack);

        void queryFavorite(RxCallBack<QueryFavoriteResponse> callBack);
    }
}
