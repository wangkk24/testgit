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
package com.pukka.ydepg.event;

import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.moudule.player.node.CurrentChannelPlaybillInfo;

/**
 * URL鉴权事件
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: PlayUrlEvent
 * @Package com.pukka.ydepg.event
 * @date 2018/01/22 17:01
 */
public class PlayUrlEvent
{

    /**
     * 剧集子集索引位置index
     */
    private String mSitcomNO;

    /**
     * 连续剧父集ID
     */
    private String mSeriesID;

    /**
     * 影片名称
     */
    private String mVideoName;

    /**
     * VOD详情
     */
    private VODDetail mVODDetail;

    /**
     * 当前频道当前节目单信息
     */
    private CurrentChannelPlaybillInfo mPlayBillInfo;

    /**
     * 是VOD订购
     */
    private boolean isVODSubscribe;

    /**
     * 是回看订购
     */
    private boolean isTVODSubscribe;

    /**
     * 是试看订购
     */
    private boolean isTrySeeSubscribe;
    /**
     * 是否是订购中心
     */
    private boolean isOrderCenter;


    private String productId;

    private String subjectID;

    private boolean isTVGuide = false;

    public String getProductId()
    {
        return productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public boolean isOrderCenter()
    {
        return isOrderCenter;
    }

    public PlayUrlEvent(boolean isVODSuscribe, boolean isTVODSubscribe, boolean isTrySeeSubscribe,boolean isOrderCenter,String productId)
    {
        this.isVODSubscribe = isVODSuscribe;
        this.isTVODSubscribe = isTVODSubscribe;
        this.isTrySeeSubscribe = isTrySeeSubscribe;
        this.isOrderCenter=isOrderCenter;
        this.productId=productId;
    }



    public void setOrderCenter(boolean orderCenter)
    {
        isOrderCenter = orderCenter;
    }

    public boolean isTrySeeSubscribe()
    {
        return isTrySeeSubscribe;
    }


    public boolean isTVODSubscribe()
    {
        return isTVODSubscribe;
    }

    public PlayUrlEvent(boolean isVODSuscribe, boolean isTVODSubscribe,String productId)
    {
        this.isVODSubscribe = isVODSuscribe;
        this.isTVODSubscribe = isTVODSubscribe;
        this.productId=productId;
    }

    public PlayUrlEvent(boolean isVODSuscribe, boolean isTVODSubscribe, boolean isTrySeeSubscribe,String productId)
    {
        this.isVODSubscribe = isVODSuscribe;
        this.isTVODSubscribe = isTVODSubscribe;
        this.isTrySeeSubscribe = isTrySeeSubscribe;
        this.productId=productId;
    }

    public PlayUrlEvent(String sitcomNO, String seriesParentVODID, String videoName, VODDetail
        vodDetail,String productId)
    {
        this.mSitcomNO = sitcomNO;
        this.mSeriesID = seriesParentVODID;
        this.mVideoName = videoName;
        this.mVODDetail = vodDetail;
        this.productId=productId;
    }

    public PlayUrlEvent(CurrentChannelPlaybillInfo playbillInfo)
    {
        this.mPlayBillInfo = playbillInfo;
    }

    public PlayUrlEvent(CurrentChannelPlaybillInfo playbillInfo, String subjectID, boolean isTVGuide)
    {
        this.mPlayBillInfo = playbillInfo;
        this.subjectID = subjectID;
        this.isTVGuide = isTVGuide;
    }

    public String getSitcomNO()
    {
        return mSitcomNO;
    }

    public String getSeriesID()
    {
        return mSeriesID;
    }

    public String getVideoName()
    {
        return mVideoName;
    }

    public VODDetail getVODDetail()
    {
        return mVODDetail;
    }

    public boolean isVODSubscribe()
    {
        return isVODSubscribe;
    }

    public CurrentChannelPlaybillInfo getPlaybillInfo()
    {
        return this.mPlayBillInfo;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public boolean isTVGuide() {
        return isTVGuide;
    }
}
