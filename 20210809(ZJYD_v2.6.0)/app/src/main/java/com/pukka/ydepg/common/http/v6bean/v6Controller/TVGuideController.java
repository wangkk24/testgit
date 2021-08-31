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
package com.pukka.ydepg.common.http.v6bean.v6Controller;

import android.content.Context;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.errorcode.ErrorCode;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.exception.ExceptionEngine;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.TVGuideCallback;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.QueryChannel;
import com.pukka.ydepg.common.http.v6bean.v6node.QueryPlaybillContext;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryPlaybillContextRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryPlaybillContextResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.launcher.util.RxCallBack;

import com.pukka.ydepg.service.NtpTimeService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;


/**
 * 直播频道页controller
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: TVGuideController
 * @Package com.pukka.ydepg.common.http.v6bean.v6Controller
 * @date 2018/01/09 16:47
 */
public class TVGuideController {

    private TVGuideCallback mCallback;

    public TVGuideController(TVGuideCallback callback) {
        mCallback = callback;
    }

    /**
     * 查询当前频道当前节目单
     *
     * @param offset      获取数据的位置
     * @param count       每次获取数据的数量
     * @param transformer transformer
     */
    public void queryChannelPlaybill(int offset, int count,
			ObservableTransformer<QueryPlaybillContextResponse, QueryPlaybillContextResponse> transformer,
			List<ChannelDetail> mChannelDetailList, Context context) {

        // 栏目--下的频道--获取节目单
        List<String> channelIds = new ArrayList<>();
        int index = 0;

        if (null == mChannelDetailList) {
            if (null != mCallback) {
                mCallback.onCurrentChannelPlayBillError(context, null, null);
            }
            return;
        }
        for (ChannelDetail channelDetail : mChannelDetailList) {
            //起始位置和结束位置获取这个区间的channelId
            if (index >= offset && index < (offset + count)) {
                channelIds.add(channelDetail.getID());
            }
            index++;
        }
        queryPlaybillContext(channelIds, String.valueOf(NtpTimeService.queryNtpTime()), transformer, context);
    }


    /**
     * 查询当前频道当前节目单上下文
     *
     * @param channelIds  频道ID集合
     * @param startTime   开始时间
     * @param transformer transformer
     */
    private void queryPlaybillContext(List<String> channelIds, String startTime,
                                      ObservableTransformer<QueryPlaybillContextResponse, QueryPlaybillContextResponse> transformer, Context context) {
        QueryPlaybillContextRequest queryPlaybillContextRequest = new QueryPlaybillContextRequest();
        queryPlaybillContextRequest.setNeedChannel("0");

        QueryPlaybillContext queryPlaybillContext = new QueryPlaybillContext();
        queryPlaybillContext.setDate(startTime);
        queryPlaybillContext.setType("1");
        queryPlaybillContext.setNextNumber("0");
        queryPlaybillContext.setPreNumber("0");
        queryPlaybillContext.setIsFillProgram("1");
        queryPlaybillContextRequest.setQueryPlaybillContext(queryPlaybillContext);

        QueryChannel queryChannel = new QueryChannel();
        queryChannel.setChannelIDs(channelIds);
        queryPlaybillContextRequest.setQueryChannel(queryChannel);

        HttpApi.getInstance().getService().queryPlaybillContext(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERYPLAYBILLCONTEXT, queryPlaybillContextRequest)
                .compose(transformer).subscribe(new RxCallBack<QueryPlaybillContextResponse>(HttpConstant.QUERYPLAYBILLCONTEXT, context) {
            @Override
            public void onSuccess(QueryPlaybillContextResponse response) {
                if (null == mCallback || null == response) return;
                if (null != response.getChannelPlaybillContexts()
                        && response.getChannelPlaybillContexts().size() > 0) {
                    mCallback.onCurrentChannelPlayBillSucc(response.getChannelPlaybillContexts());
                } else {
                    if(response.getResult() != null && response.getResult().getRetCode() != null) {
                        mCallback.onCurrentChannelPlayBillError(context, response.getResult().getRetCode()
                                , ErrorCode.findError(HttpConstant.QUERYPLAYBILLCONTEXT, response.getResult().getRetCode()).getMessage());
                    }
                    else{
                        mCallback.onCurrentChannelPlayBillError(context, null, null);
                    }
                }
            }

            @Override
            public void onFail(@NonNull Throwable e) {
                if (null != mCallback) {
                    mCallback.onCurrentChannelPlayBillError(context, ExceptionEngine.handleException(e).getErrorCode() + "", ExceptionEngine.handleException(e).getMessage());
                }
            }
        });
    }
}