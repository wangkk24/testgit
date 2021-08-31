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
package com.pukka.ydepg.moudule.livetv.presenter.contract;

import android.content.Context;

import com.pukka.ydepg.common.http.v6bean.v6CallBack.IPlayURLCallback;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayChannelRequest;
import com.pukka.ydepg.moudule.livetv.presenter.base.BasePresenter;
import com.pukka.ydepg.moudule.livetv.presenter.base.BaseView;
import com.pukka.ydepg.moudule.player.node.Schedule;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: PlayUrlContract
 * @Package com.pukka.ydepg.moudule.livetv.presenter.contract
 * @date 2018/01/20 17:57
 */
public interface PlayUrlContract {

  interface View extends BaseView,IPlayURLCallback {

    /**
     * 返回当前播放的节目单和频道信息
     * @param channelName 频道名称
     * @param programName 节目单名称
     * @param syTime 剩余时间
     */
    void onCurrentChannelPlayBillSucc(String programName,String syTime,String channelName);

    /**
     * 返回频道节目单出错
     */
    void onCurrentChannelPlayBillError();

    /**
     * 返回切换的频道号
     * @param switchIndex 切换的索引位置++
     * @param channelNO channelNO
     */
    void onSwitchChannelNO(int switchIndex,String channelNO);

    /**
     * 返回切换的频道号对应的索引位置
     * @param switchIndex 位置
     */
    void onSwitchChannelIndex(int switchIndex);
  }

  abstract class Presenter extends BasePresenter<View> {

    /**
     * 默认播放鉴权操作
     */
    public abstract void playDefaultChannel(Context context, String subjectID, String isQuerySubject);

    /**
     * 直播播放鉴权
     * @param isPlayback 是不是回看
     * @param schedule
     * @param request
     */
    public abstract void playChannel(boolean isPlayback,Schedule schedule,PlayChannelRequest request, Context context, String subjectID, String isQuerySubject, String currentChildSubjectID);

    /**
     * 回看鉴权
     * @param sitcomNO 如果是连续剧的话,返回连续剧子集编号
     * @param seriesID 电视剧父集ID
     * @param vodDetail
     */
    public abstract void playVOD(String sitcomNO,String seriesID,VODDetail vodDetail, Context context);

    /**
     * 切换频道
     * @param isNext 是不是切换到下一个频道
     * @param index 当前切换的索引位置
     */
    public abstract void switchChannelNO(boolean isNext,int index);

    /**
     * 切换频道
     * @param isNext 是不是切换到下一个频道
     * @param index 当前切换的索引位置
     * @param subjectID 栏目id
     */
    public abstract void switchChannelNO(boolean isNext,int index, String subjectID, String isQuerySubject);

    /**
     * 强制取消频道鉴权:
     * 针对快速切台的时候,切换到下一个频道的时候,上一个频道数据此时回来了
     */
    public abstract void forceDisposablePlayChannel();


  }
}
