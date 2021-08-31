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
package com.pukka.ydepg.common.http.v6bean.v6CallBack;

import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.moudule.player.node.Schedule;

/**
 * 播放URL鉴权回调接口
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: IPlayURLCallback
 * @Package com.pukka.ydepg.common.http.v6bean.v6CallBack
 * @date 2018/01/20 17:40
 */
public interface IPlayURLCallback {

    /**
     * 频道相关鉴权回调
     *
     * @param channelId 频道ID
     * @param url       播放地址
     * @param bookmark  书签
     */
    void getChannelPlayUrlSuccess(String channelId, String url, String bookmark,String attchUrl);

    /**
     * VOD鉴权回调
     *
     * @param url       播放地址 （鉴权成功返回）
     * @param bookmark  书签
     * @param productId 产品ID
     */
    void getVODPlayUrlSuccess(String url, String bookmark, String productId);

    /**
     * 播放鉴权失败
     *
     * @param isVOD           是不是回看VOD
     * @param authorizeResult AuthorizeResult
     */
    void getPlayUrlFailed(String channelId, boolean isVOD, AuthorizeResult authorizeResult,String contentID, String playUrl, String attchUrl);

    /**
     * 其他情况,网络框架走到onError中
     */
    void playUrlError();


    /*
     * 请求用户栏目-频道数据成功
     * */
    void onChannelColumn();

    /*
     * 频道位置
     * index
     * */
    void onChannelIndex(int index);

    void getChannelPlayKey(String playKey);

    void getProductID(String productID);
}