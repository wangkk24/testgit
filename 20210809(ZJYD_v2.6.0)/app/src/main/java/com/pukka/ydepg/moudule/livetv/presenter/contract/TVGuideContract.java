package com.pukka.ydepg.moudule.livetv.presenter.contract;

import android.content.Context;
import android.util.ArrayMap;

import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelPlaybillContext;
import com.pukka.ydepg.customui.focusView.TVGuideRecycleView;
import com.pukka.ydepg.moudule.livetv.adapter.BaseAdapter;
import com.pukka.ydepg.moudule.livetv.presenter.base.BasePresenter;
import com.pukka.ydepg.moudule.livetv.presenter.base.BaseView;

import java.util.List;

/**
 * 直播频道页Contract
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: TVGuideContract
 * @Package com.pukka.ydepg.moudule.livetv.presenter.contract
 * @date 2017/12/26 09:51
 */
public interface TVGuideContract {

    interface View extends BaseView {

        /**
         * 加载当前频道当前节目单列表数据成功
         *
         * @param list 当前频道当前节目单列表
         */
        void onLoadChannelPlaybillSucc(List<ChannelPlaybillContext> list);

        /**
         * 加载当前频道当前节目单数据失败
         */
        void onLoadChannelPlaybillError();

        /*
        * 栏目和频道数据组织成功
        * */
        void onChannelColumn();
    }

    abstract class Presenter extends BasePresenter<View> {

        /**
         * 初始化
         *
         * @param spaceMaps
         * @param adapterMaps
         */
        public abstract void initialize(
                ArrayMap<TVGuideRecycleView, Integer> spaceMaps,
                ArrayMap<TVGuideRecycleView, BaseAdapter> adapterMaps);

        /**
         * 查询当前频道当前节目单
         *
         * @param offset 查询数据的起始位置
         * @param count  每次查询数据的总量
         */
        public abstract void queryCurrentChannelPlaybill(int offset, int count,List<ChannelDetail> mChannelDetailList, Context context);
    }
}