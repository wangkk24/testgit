/*
 *Copyright (C) 2017 广州易杰科技, Inc.
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
package com.pukka.ydepg.moudule.livetv.presenter;

import android.content.Context;
import android.util.ArrayMap;

import com.pukka.ydepg.common.http.v6bean.v6CallBack.TVGuideCallback;
import com.pukka.ydepg.common.http.v6bean.v6Controller.TVGuideController;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelPlaybillContext;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.customui.focusView.TVGuideRecycleView;
import com.pukka.ydepg.moudule.livetv.adapter.BaseAdapter;
import com.pukka.ydepg.moudule.livetv.presenter.contract.TVGuideContract;

import java.util.List;

/**
 * 直播频道页presenter
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: TVGuidePresenter
 * @Package com.pukka.ydepg.moudule.livetv.presenter
 * @date 2017/12/26 09:51
 */
public class TVGuidePresenter extends TVGuideContract.Presenter implements TVGuideCallback {

    private static final String TAG = "TVGuidePresenter";

    /**
     * 频道页业务逻辑控制器
     */
    private TVGuideController mController = new TVGuideController(this);

    /**
     * 初始化RecycleView和adapter
     */
    @Override
    public void initialize(ArrayMap<TVGuideRecycleView, Integer> spaceMaps,
                           ArrayMap<TVGuideRecycleView, BaseAdapter> adapterMaps) {
        SuperLog.debug(TAG, "[initialize]");
        int size = spaceMaps.size();
        for (int i = 0; i < size; i++) {
            TVGuideRecycleView recycleView = spaceMaps.keyAt(i);
            recycleView.addItemDecoration(
                    new TVGuideRecycleView.CustomItemDecoration(spaceMaps.valueAt(i)));
        }
        int adapterSize = adapterMaps.size();
        for (int j = 0; j < adapterSize; j++) {
            TVGuideRecycleView recycleView = adapterMaps.keyAt(j);
            recycleView.setAdapter(adapterMaps.valueAt(j));
        }
    }

    /**
     * 查询当前频道当前节目单
     *
     * @param offset 查询数据的起始位置
     * @param count  每次查询数据的总量
     */
    @Override
    public void queryCurrentChannelPlaybill(int offset, int count, List<ChannelDetail> mChannelDetailList, Context context) {
        if (isViewAttached()) {
            mController.queryChannelPlaybill(offset, count, compose(getBaseView().bindToLife()), mChannelDetailList, context);
        }
    }

    /**
     * 当前频道当前节目单返回成功
     *
     * @param list 当前频道当前节目单列表
     */
    @Override
    public void onCurrentChannelPlayBillSucc(List<ChannelPlaybillContext> list) {
        SuperLog.debug(TAG, "[onCurrentChannelPlayBillSucc]");
        if (isViewAttached()) {
            getBaseView().onLoadChannelPlaybillSucc(list);
        }
    }

    /**
     * 当前频道当前节目单返回数据出错
     */
    @Override
    public void onCurrentChannelPlayBillError(Context context, String errorCode, String errorMessage) {
        SuperLog.error(TAG, "[onCurrentChannelPlayBillError]");

        if (!isViewAttached())
            return;

        getBaseView().onLoadChannelPlaybillError();
    }
}