/*
 *Copyright (C) 2018 广州易杰科技, Inc.
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
package com.pukka.ydepg.service.presenter.contract;

import android.content.Context;

import com.pukka.ydepg.common.http.v6bean.v6node.PersonalDataVersion;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryAllChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryFavoriteRequest;
import com.pukka.ydepg.launcher.mvp.contact.IBaseContact;
import com.pukka.ydepg.moudule.livetv.presenter.base.BasePresenter;
import com.pukka.ydepg.moudule.livetv.presenter.base.BaseView;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: HeartBeatContract
 * @Package com.pukka.ydepg.service.presenter.contract
 * @date 2018/01/16 11:29
 */
public interface HeartBeatContract {

    interface HeartBeatView extends IBaseContact.IBaseView {
        /**
         * 心跳调用成功
         *
         * @param nextCallInterval 心跳周期
         */
        void onStartHeartBeatSucc(String nextCallInterval);

        /**
         * 心跳调用失败
         *
         * @param isFirst
         */
        void onStartHeartBeatError(boolean isFirst);

        /**
         * 书签版本号变化
         *
         * @param bookmarkDataVersion bookmark 心跳返回的version
         *                            第一次心跳，全量缓存各个接口的版本;第二次心跳乃至n次心跳,通过请求返回的response对应的版本号更新到本地；
         *                            ！！！注意：如果response返回的version为null则从心跳返回的PersonalDataVersion中对应的version设置；
         * @see PersonalDataVersion bookmark心跳返回的version
         */
        void onBookmarkVersionChange(String bookmarkDataVersion);

        /**
         * 收藏版本号变化了
         *
         * @param favoriteDataVersion favorite 心跳返回的version
         */
        void onFavoriteVersionChange(String favoriteDataVersion);

        /**
         * 频道版本号变化了
         *
         * @param isChange           true表示版本号有变化
         * @param channelDataVersion channel 心跳返回的version
         */
        void onChannelVersionChange(boolean isChange, String channelDataVersion, String subscribeVersion);

    }

    interface HeartBeatPresenter extends IBaseContact.IBasePresenter {
        /**
         * 执行心跳
         *
         * @param isFirst
         */
        void startHeartBeatService(boolean isFirst, Context context);

        /**
         * 查询全部频道
         *
         * @param channelVersion   频道 personalDataVersion.channelversion
         * @param subscribeVersion 订购关系变化version
         * @param request
         */
        void queryAllChannel(String channelVersion, String subscribeVersion, Context context);

        /**
         * 查询频道动态属性
         *
         * @param subscribeVersion 频道动态属性 personalDataVersion.subscribeversion
         *                         订购信息变化需要，调用该接口刷新频道动态属性
         */
        void queryAllChannelDynamicProperties(String subscribeVersion, Context context);


        //开机查询角标
        void querySuperScript();
    }
}