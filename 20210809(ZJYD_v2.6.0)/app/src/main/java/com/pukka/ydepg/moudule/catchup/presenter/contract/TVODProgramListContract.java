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
package com.pukka.ydepg.moudule.catchup.presenter.contract;

import android.content.Context;
import android.util.ArrayMap;

import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.PlaybillLite;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayChannelResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryPlaybillListResponse;
import com.pukka.ydepg.launcher.mvp.contact.IBaseContact;
import com.pukka.ydepg.moudule.player.node.Program;

import java.util.List;

import io.reactivex.ObservableTransformer;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: TVODProgramListContract
 * @Package com.pukka.ydepg.moudule.catchup.presenter.contract
 * @date 2018/09/21 11:18
 */
public interface TVODProgramListContract {

    interface View extends IBaseContact.IBaseView {
        /**
         * 回看日期列表
         */
        void onTVODDateList(List<String> originalList,List<String> dateList,List<String> voiceList);

        /**
         * 节目单列表数据为空
         */
        void onPlayBillListEmpty(String dateValueKey);

        /**
         * 查询节目单列表数据成功
         */
        void onQueryPlayBillListSuccess(String dateValueKey, int programSize,List<ArrayMap<Integer,List<Program>>> allProgramList);

        /**
         * 节目单列表数据查询失败
         */
        void onQueryPlaybillListFailed();

        /**
         * 回看播放鉴权成功回调
         */
        void onPlayChannelUrlSuccess(String channelID,String url,String bookMark);

        /**
         * 回看播放鉴权地址鉴权失败,需要订购
         */
        void onPlayChannelUrlFailed(AuthorizeResult authorizeResult);

        /**
         * 播放失败(网络异常)
         */
        void onPlayChannelUrlError();
    }

    interface Presenter<T extends IBaseContact.IBaseView>extends IBaseContact.IBasePresenter<T>{
        /**
         * 创建回看日期列表
         */
        void createTVODDateList();

        /**
         * 查询节目单列表
         */
        void queryPlaybillList(Context context,String dateValue, List<String> channelIds);

        /**
         * 回看播放鉴权
         */
        void playChannel(Context context,PlayChannelRequest request);
    }
}
