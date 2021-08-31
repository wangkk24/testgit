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
package com.pukka.ydepg.moudule.livetv.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.BroadCastConstant;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.IPlayURLCallback;
import com.pukka.ydepg.common.http.v6bean.v6Controller.PlayUrlControl;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.PlaybillLite;
import com.pukka.ydepg.common.http.v6bean.v6node.QueryChannel;
import com.pukka.ydepg.common.http.v6bean.v6node.QueryPlaybill;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryPlaybillListRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ReportChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryPlaybillListResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.event.PlayUrlEvent;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.livetv.presenter.contract.PlayUrlContract;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.livetv.utils.LiveTVCacheUtil;
import com.pukka.ydepg.moudule.livetv.utils.LiveUtils;
import com.pukka.ydepg.moudule.player.node.CurrentChannelPlaybillInfo;
import com.pukka.ydepg.moudule.player.node.Schedule;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;

import com.pukka.ydepg.service.NtpTimeService;
import com.pukka.ydepg.service.presenter.HeartBeatPresenter;

import org.greenrobot.eventbus.EventBus;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * PlayUrlPresenter
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: PlayUrlPresenter
 * @Package com.pukka.ydepg.moudule.livetv.presenter
 * @date 2018/01/20 17:56
 */
public class PlayUrlPresenter extends PlayUrlContract.Presenter implements IPlayURLCallback {

    private static final String TAG = "PlayUrlPresenter";

    /**
     * 节目单信息infobar callback
     */
    private RxCallBack<QueryPlaybillListResponse> mPlaybillCallback;

    /**
     * 播放地址鉴权控制器
     */
    private PlayUrlControl mPlayUrlController = new PlayUrlControl(this);

    /**
     * 播放默认频道
     */
    @Override
    public void playDefaultChannel(Context context, String subjectID, String isQuerySubject) {
        Observable.create(new ObservableOnSubscribe<Schedule>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Schedule> e) throws Exception {

                Schedule schedule = null;
                //获取缓存的全量频道列表信息

                //======>
                HashMap<String, List<ChannelDetail>> saveHashMap = LiveDataHolder.get().getSaveHashMap();
                Set set = saveHashMap.keySet();
                List<ChannelDetail> channelDetailCanPlay = new ArrayList<>();
                for (Iterator iterator = set.iterator(); iterator.hasNext(); ) {
                    String key = (String) iterator.next();
                    channelDetailCanPlay.addAll(saveHashMap.get(key));
                }
                LiveDataHolder.get().setChannelPlay(channelDetailCanPlay);
                List<ChannelDetail> channelDetailListCanPlay = new ArrayList<>();

                if(TextUtils.isEmpty(subjectID)) {
                    channelDetailListCanPlay.addAll(channelDetailCanPlay);
                }
                else{
                    List<ChannelDetail> channelDetails = LiveDataHolder.get().getTargetChannelPlay(subjectID, TextUtils.equals(isQuerySubject, "1"));
                    if (channelDetails != null) {
                        channelDetailListCanPlay.addAll(channelDetails);
                    }
                }



                //======end
                //List<ChannelDetail> channelDetailListCanPlay = LiveDataHolder.get().getChannelPlay();
                List<Schedule> scheduleList = LiveDataHolder.get().getChannelPlayToSchedule(channelDetailListCanPlay);
                //先取上次播放过的频道
                String[] infos = LiveTVCacheUtil.getInstance().getRecordPlayChannelInfo();

                //先判断直播播放固定频道的channelID、MediaId是否为空，为空走正常逻辑，不为空则播放的是固定的频道
                String channelAndMediaId = LiveDataHolder.get().getChannelAndMediaId();
                LiveDataHolder.get().setChannelAndMediaId("","");
                if (!TextUtils.isEmpty(channelAndMediaId)){
                    String[] split = channelAndMediaId.split(",");
                    schedule = new Schedule();
                    schedule.setId(split[0]);//channelID
                    schedule.setMediaID(split[1]);//mediaID
                    if (null != scheduleList && scheduleList.size() > 0) {
                        for (Schedule sh : scheduleList) {
                            //通过频道ID获取当前频道号
                            if (sh.getId().equals(split[0])) {
                                schedule.setChannelNo(sh.getChannelNo());
                                break;
                            }
                        }
                    }
                    //没有获取到,从全量列表中获取第一位的频道信息
                    if (!TextUtils.isEmpty(subjectID) && TextUtils.isEmpty(schedule.getChannelNo())) {
                        schedule = scheduleList.get(0);
                    }
                }
                else if (null != infos && infos.length == 2) {
                    schedule = new Schedule();
                    schedule.setId(infos[0]);//channelID
                    schedule.setMediaID(infos[1]);//mediaID
                    if (null != scheduleList && scheduleList.size() > 0) {
                        for (Schedule sh : scheduleList) {
                            //通过频道ID获取当前频道号
                            if (sh.getId().equals(infos[0])) {
                                schedule.setChannelNo(sh.getChannelNo());
                                break;
                            }
                        }
                    }

                    //没有获取到,从全量列表中获取第一位的频道信息
                    if (!TextUtils.isEmpty(subjectID) && TextUtils.isEmpty(schedule.getChannelNo())) {
                        schedule = scheduleList.get(0);
                    }
                } else {
                    /**
                     * 没有上次播放过的频道,说明可能是第一次安装进入直播,先从终端配置参数中获取默认频道号,
                     * 从终端配置参数中取默认频道号，然后和本地频道列表做对比;
                     * 如果默认频道号在本地频道列表中查询到了,那么做播放鉴权操作;
                     * 如果默认频道号在本地频道列表中查询不到,那么在从本地频道列表中取第一个频道进行播放鉴权;
                     * 如果本地频道列表没有缓存下来,那么什么播放操作都不做;
                     */
                    String defauleChannelNO = SessionService.getInstance().getSession()
                            .getTerminalConfigurationValue(Configuration.Key.PLAY_DEFAULT_CHANNEL);
                    if (null != scheduleList && scheduleList.size() > 0) {
                        if (!TextUtils.isEmpty(defauleChannelNO)) {
                            //根据频道号获取对应的频道信息
                            for (Schedule sh : scheduleList) {
                                String channelNo = sh.getChannelNo();
                                if (!TextUtils.isEmpty(channelNo) && channelNo.equals(defauleChannelNO)) {
                                    schedule = sh;
                                    break;
                                }
                            }
                        }
                        //没有获取到,从全量列表中获取第一位的频道信息
                        if (null == schedule) {
                            schedule = scheduleList.get(0);
                        }
                    }
                }
                //统一处理,在进直播时,没有获取到缓存的频道信息时,需要发送一个刷新频道信息的广播
                if (null == scheduleList || scheduleList.size() == 0) {
                    //发送刷新频道信息接口
                    LocalBroadcastManager.getInstance(OTTApplication.getContext()).sendBroadcast(new Intent(BroadCastConstant.Heartbit.COM_HUAWEI_OTT_HEARTBIT_CHANNEL_UPDATE));
                }
                if (null != schedule) {
                    //解析出上次播放的频道对应的索引位置
                    int channelIndex = LiveUtils.parseChannelDetailIndex(schedule.getId(), channelDetailListCanPlay);
                    if (channelIndex != -1) {
                        if (isViewAttached()) {
                            //返回当前频道号对应的位置,同时回调当前默认播放的频道索引位置
                            getBaseView().onSwitchChannelIndex(channelIndex);
                        }
                    } else {
                        if (null != scheduleList && scheduleList.size() > 0) {
                            //没有频道号，首次进行播放默认频道的时候，尝试刷新频道动态参数属性接口
                            LocalBroadcastManager.getInstance(OTTApplication.getContext()).sendBroadcast(new Intent(BroadCastConstant.Heartbit.COM_HUAWEI_OTT_HEARTBIT_CHANNELNO_UPDATE));
                        }
                    }
                }
                e.onNext(schedule);
                e.onComplete();
            }
        }).compose(compose(getBaseView().bindToLife()))
                .subscribe(new RxCallBack<Schedule>("playDefaultChannel",context) {
                    @Override
                    public void onSuccess(Schedule schedule) {
                        playChannel(false, schedule, null, context, subjectID, isQuerySubject, null);
                    }

                    @Override
                    public void onFail(@NonNull Throwable e) {
                        SuperLog.error(TAG, e);
                    }
                });
    }

    /**
     * 解析频道ID对应的索引位置
     *
     * @param channelID 频道ID
     */
    public void parseChannelNOIndex(String channelID) {
        /*Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(LiveUtils.parseChannelIndex(channelID, LiveDataHolder.get().getAllSchedules()));
                e.onComplete();
            }
        }).compose(compose(getBaseView().bindToLife()))
                .subscribe(new RxCallBack<Integer>() {
                    @Override
                    public void onSuccess(@NonNull Integer index) {
                        if (index != -1 && isViewAttached()) {
                            //返回当前频道ID对应的位置,同时回调当前默认播放的频道号
                            getBaseView().onSwitchChannelIndex(index);
                        }
                    }

                    @Override
                    public void onFail(@NonNull Throwable e) {
                    }
                });*/
    }

    /**
     * 频道鉴权
     *
     * @param isPlayback 是不是回看
     * @param schedule   频道信息
     * @param request    直播url鉴权request
     */
    @Override
    public synchronized void playChannel(boolean isPlayback, Schedule schedule, PlayChannelRequest request, Context context, String subjectID, String isQuerySubject, String currentSubjectID) {
        List<ChannelDetail> canPlayLsit = LiveDataHolder.get().getChannelPlay();
        if(!TextUtils.isEmpty(subjectID)){
            if(!TextUtils.isEmpty(currentSubjectID)){
                canPlayLsit = LiveDataHolder.get().getTargetChannelPlay(currentSubjectID, false);
            }
            else {
                canPlayLsit = LiveDataHolder.get().getTargetChannelPlay(subjectID, TextUtils.equals(isQuerySubject, "1"));
            }
        }
        if (null != request) {
			int index = LiveUtils.parseChannelDetailIndex(request.getChannelID(), canPlayLsit);
            if(!LiveDataHolder.get().isMiniEpg()){
            	//非点击菜单进行切台(上下键/数字键/语音)
				if (index != -1 && !LiveDataHolder.get().getIsShowingSkip()) {
					//存在request.getChannelID()这个频道   存储落焦位置
					getPosition(canPlayLsit.get(index), index, subjectID, isQuerySubject, currentSubjectID);
				}
			} else {
            	//点击菜单进行切台 在Item点击事件中已经记录了落焦位置，因此这里不用再记录
            	LiveDataHolder.get().setMiniEpg(false);
				//当前频道index信息，用于上下键切台时的当前频道位置信息
				getBaseView().onSwitchChannelIndex(index);
			}

            mPlayUrlController.playChannel(request, compose(getBaseView().bindToLife()), context);
            SuperLog.debug(TAG,"4playChannel->null != request:"+request.getChannelID());
        } else if (null != schedule) {
            CurrentChannelPlaybillInfo playbillInfo = new CurrentChannelPlaybillInfo();
            playbillInfo.setChannelId(schedule.getId());
            playbillInfo.setChannelMediaId(schedule.getMediaID());
            playbillInfo.setChannelNo(schedule.getChannelNo());

//            /*
//             * 定位弹框落焦
//             * */
//            int index = LiveUtils.parseChannelDetailIndex(schedule.getId(), canPlayLsit);
//            if (index != -1) {
//                getPosition(canPlayLsit.get(index), index);
//            }
            SuperLog.debug(TAG,"5playChannel->PlayUrlEvent:"+playbillInfo.getChannelNo());
            //直播鉴权
            EventBus.getDefault().post(new PlayUrlEvent(playbillInfo));
        }
    }

    /**
     * 强制取消频道鉴权:
     * 针对快速切台的时候,切换到下一个频道的时候,上一个频道数据此时回来了
     */
    @Override
    public void forceDisposablePlayChannel() {
        if(null!=mPlayUrlController){
            mPlayUrlController.forceDisposablePlayChannel();
        }
    }

    /**
     * 回看鉴权
     *
     * @param sitcomNO  剧集编号
     * @param seriesID  剧集父集ID
     * @param vodDetail VOD详情
     */
    @Override
    public void playVOD(String sitcomNO, String seriesID, VODDetail vodDetail, Context context) {
        mPlayUrlController.playVOD(sitcomNO, seriesID, vodDetail, context);
    }

    /**
     * 返回切换的频道号和索引位置
     *
     * @param isNext 是不是下一个频道
     * @param index  当前切换的索引位置
     */
    @Override
    public void switchChannelNO(boolean isNext, int index) {
        //强制取消上一个频道的播放鉴权
        forceDisposablePlayChannel();

        List<ChannelDetail> channelPlay = LiveDataHolder.get().getChannelPlay();

        /*
         * 只有一个频道时不让上下切台
         * */
        if (channelPlay == null || channelPlay.size() == 1
                || channelPlay.size() == 0) {
            return;
        }

        int size = channelPlay.size();
        if (isViewAttached()) {
            if (isNext) {
                index++;
            } else {
                index--;
            }
            //resize channel list index
            if (index >= size) {
                index = 0;
            } else if (index < 0) {
                index = size - 1;
            }
            ChannelDetail channelDetail = channelPlay.get(index);
            String channelNO = channelDetail.getChannelNO();
            /*String mColumnId = "";
            List<Subject> saveSubjectList = LiveDataHolder.get().getSaveSubjectList();

            if (saveSubjectList != null && (saveSubjectList.size() == 1 || saveSubjectList.size() == 0)) {
                //int channelIndex = LiveUtils.parseChannelIdIndex(channelNO, channelPlay);
                LiveDataHolder.get().setChannelSelectPosition(index);
            } else {
                for (Subject subject : saveSubjectList) {
                    if (TextUtils.isEmpty(mColumnId)) {
                        mColumnId = subject.getID();
                    } else {
                        mColumnId = mColumnId + "," + subject.getID();
                    }
                }
                List<String> subjectIDs = channelDetail.getSubjectIDs();
                String[] split = mColumnId.split(",");
                boolean isGoOn = true;
                int indexPo = 0;
                for (int i = 0; i < split.length; i++) {
                    if (!isGoOn) break;
                    for (String subject : subjectIDs) {
                        if (split[i].equals(subject)) {
                            LiveDataHolder.get().setCulomnSelectPosition(i);
                            indexPo = i;
                            isGoOn = false;
                            break;
                        }
                    }
                }
                List<ChannelDetail> channelDetailList = LiveDataHolder.get().getSaveHashMap().get(split[indexPo]);
                if (channelDetailList != null && channelDetailList.size() > 0) {
                    for (int i = 0; i < channelDetailList.size(); i++) {
                        if (channelNO.equals(channelDetailList.get(i).getChannelNO())) {
                            LiveDataHolder.get().setChannelSelectPosition(i);
                            break;
                        }
                    }
                }
            }*/
            getBaseView().onSwitchChannelNO(index, channelNO);
        }
    }

    /**
     * 返回切换的频道号和索引位置
     *
     * @param isNext 是不是下一个频道
     * @param index  当前切换的索引位置
     * @param subjectID  栏目id
     */
    @Override
    public void switchChannelNO(boolean isNext, int index, String subjectID, String isQuerySubject) {
        //强制取消上一个频道的播放鉴权
        forceDisposablePlayChannel();
        List<ChannelDetail> channelPlay = null;
//        Map<String, List<ChannelDetail>> channelHashMap =  LiveDataHolder.get().getSaveHashMap();
//        if(channelHashMap != null){
//            channelPlay = channelHashMap.get(subjectID);
//        }

        channelPlay = LiveDataHolder.get().getTargetChannelPlay(subjectID, TextUtils.equals(isQuerySubject, "1"));

        /*
         * 只有一个频道时不让上下切台
         * */
        if (channelPlay == null || channelPlay.size() == 1
                || channelPlay.size() == 0) {
            return;
        }

        int size = channelPlay.size();
        if (isViewAttached()) {
            if (isNext) {
                index++;
            } else {
                index--;
            }
            //resize channel list index
            if (index >= size) {
                index = 0;
            } else if (index < 0) {
                index = size - 1;
            }
            ChannelDetail channelDetail = channelPlay.get(index);
            String channelNO = channelDetail.getChannelNO();
            /*String mColumnId = "";
            List<Subject> saveSubjectList = LiveDataHolder.get().getSaveSubjectList();

            if (saveSubjectList != null && (saveSubjectList.size() == 1 || saveSubjectList.size() == 0)) {
                //int channelIndex = LiveUtils.parseChannelIdIndex(channelNO, channelPlay);
                LiveDataHolder.get().setChannelSelectPosition(index);
            } else {
                for (Subject subject : saveSubjectList) {
                    if (TextUtils.isEmpty(mColumnId)) {
                        mColumnId = subject.getID();
                    } else {
                        mColumnId = mColumnId + "," + subject.getID();
                    }
                }
                List<String> subjectIDs = channelDetail.getSubjectIDs();
                String[] split = mColumnId.split(",");
                boolean isGoOn = true;
                int indexPo = 0;
                for (int i = 0; i < split.length; i++) {
                    if (!isGoOn) break;
                    for (String subject : subjectIDs) {
                        if (split[i].equals(subject)) {
                            LiveDataHolder.get().setCulomnSelectPosition(i);
                            indexPo = i;
                            isGoOn = false;
                            break;
                        }
                    }
                }
                List<ChannelDetail> channelDetailList = LiveDataHolder.get().getSaveHashMap().get(split[indexPo]);
                if (channelDetailList != null && channelDetailList.size() > 0) {
                    for (int i = 0; i < channelDetailList.size(); i++) {
                        if (channelNO.equals(channelDetailList.get(i).getChannelNO())) {
                            LiveDataHolder.get().setChannelSelectPosition(i);
                            break;
                        }
                    }
                }
            }*/
            getBaseView().onSwitchChannelNO(index, channelNO);
        }
    }

    /**
     * 频道播放鉴权成功,拿到播放地址
     *
     * @param channelId 频道ID
     * @param url       播放地址
     * @param bookmark  书签
     */
    @Override
    public void getChannelPlayUrlSuccess(String channelId, String url, String bookmark,String attchUrl) {
        if (isViewAttached()) {
            SuperLog.debug(TAG, "[getChannelPlayUrlSuccess]");
            getBaseView().getChannelPlayUrlSuccess(channelId, url, bookmark,attchUrl);
        }
    }

    /**
     * VOD播放鉴权成功,拿到播放地址
     *
     * @param url       播放地址 （鉴权成功返回）
     * @param bookmark  书签
     * @param productId 产品ID
     */
    @Override
    public void getVODPlayUrlSuccess(String url, String bookmark, String productId) {
        if (isViewAttached()) {
            SuperLog.debug(TAG, "[getVODPlayUrlSuccess]");
            getBaseView().getVODPlayUrlSuccess(url, bookmark, productId);
        }
    }

    /**
     * 播放鉴权失败,authorizeResultValue:内部存放的是可订购列表
     *
     * @param isVOD                是不是回看VOD
     * @param authorizeResult AuthorizeResult转成的jsonString
     */
    @Override
    public void getPlayUrlFailed(String channelId, boolean isVOD, AuthorizeResult authorizeResult, String contentId, String url, String attchUrl) {
        if (isViewAttached()) {
            SuperLog.debug(TAG, "[getPlayUrlFailed]");
            getBaseView().getPlayUrlFailed(channelId, isVOD, authorizeResult,contentId, url, attchUrl);
        }
    }

    /**
     * 播放地址鉴权,异常情况处理
     */
    @Override
    public void playUrlError() {
        if (isViewAttached()) {
            SuperLog.debug(TAG, "[playUrlError]");
            getBaseView().playUrlError();
        }
    }

    /*
     * 请求用户栏目-频道数据成功
     * */
    @Override
    public void onChannelColumn() {
        if (isViewAttached()) {
            SuperLog.debug(TAG, "[onChannelColumn]");
            getBaseView().onChannelColumn();
        }
    }

    @Override
    public void onChannelIndex(int index) {

    }

    @Override
    public void getChannelPlayKey(String playKey) {
//        if (isViewAttached()) {
//            getBaseView().getChannelPlayKey(playKey);
//        }
    }

    @Override
    public void getProductID(String productID) {
        if (isViewAttached()) {
            getBaseView().getProductID(productID);
        }
    }

    /**
     * 查询在infobar上显示的当前频道正在播放的节目单
     *
     * @param channelIds 频道ID集合
     * @param startTime  开始时间
     */
    public synchronized void queryInfoBarPlaybillLite(List<String> channelIds, String startTime, Context context) {
        if (null != mPlaybillCallback) {
            mPlaybillCallback.dispose();
            mPlaybillCallback = null;
        }
        mPlaybillCallback = new RxCallBack<QueryPlaybillListResponse>(HttpConstant.QUERYPLAYBILLLIST, context) {
            @Override
            public void onSuccess(QueryPlaybillListResponse response) {
                if (null == response) return;
                Result result = response.getResult();
                if (null != result) {
                    String retCode = result.getRetCode();
                    if (!TextUtils.isEmpty(retCode) && retCode.equals(Result.RETCODE_OK)) {
                        if (response.getChannelPlaybills() != null && response.getChannelPlaybills().size() > 0
                                && response.getChannelPlaybills().get(0).getPlaybillLites() != null
                                && response.getChannelPlaybills().get(0).getPlaybillLites().size() > 0) {
                            PlaybillLite playbillLite = response.getChannelPlaybills()
                                    .get(0).getPlaybillLites().get(0);
                            if (null != playbillLite) {
                                String programName = playbillLite.getName();
                                if (TextUtils.isEmpty(programName)) {
                                    //没有节目单
                                    programName = OTTApplication.getContext().getString(R.string.channel_playbill_name_empty);
                                }
                                //剩余时间
                                String syTime = "";
                                String channelName = "";
                                String startTime = playbillLite.getStartTime();
                                String endTime = playbillLite.getEndTime();
                                if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
                                    try {
                                        //计算出剩余时间
                                        syTime = String.format(OTTApplication.getContext().getString(
                                                R.string.channel_playbill_totaltime), String.valueOf((Long.
                                                parseLong(endTime) - Long.parseLong(startTime)) / 1000 / 60));
                                    } catch (NumberFormatException e) {
                                        SuperLog.error(TAG, e);
                                    }
                                }
                                if (response.getChannelPlaybills().get(0).getChannelDetail() != null && !TextUtils.isEmpty(response.getChannelPlaybills().get(0).getChannelDetail().getName())) {
                                    channelName = response.getChannelPlaybills().get(0).getChannelDetail().getName();
                                }
                                if (isViewAttached()) {
                                    getBaseView().onCurrentChannelPlayBillSucc(programName, syTime, channelName);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFail(@NonNull Throwable e) {
                SuperLog.error(TAG, e);
            }
        };

        //查询节目单列表
        QueryPlaybillListRequest request = new QueryPlaybillListRequest();
        QueryChannel queryChannel = new QueryChannel();
        queryChannel.setChannelIDs(channelIds);
        queryChannel.setContentType("CHANNEL");
        queryChannel.setIsReturnAllMedia("1");

        QueryPlaybill queryPlaybill = new QueryPlaybill();
        queryPlaybill.setType("0");
        queryPlaybill.setStartTime(String.valueOf(startTime));
        queryPlaybill.setCount("1");
        queryPlaybill.setOffset("0");
        queryPlaybill.setIsFillProgram("1");
        queryPlaybill.setEndTime(DateCalendarUtils.getEndTimeOfDay((DateCalendarUtils.
                formatDateTime(NtpTimeService.queryNtpTime(), "yyyyMMdd"))) + "");

        request.setNeedChannel("1");
        request.setQueryChannel(queryChannel);
        request.setQueryPlaybill(queryPlaybill);

        HttpApi.getInstance().getService().queryPlaybillList(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERYPLAYBILLLIST, request)
                .compose(compose(getBaseView().bindToLife()))
                .subscribe(mPlaybillCallback);
    }

//    /*
//     * 查询栏目--频道数据
//     * */
//    public void queryChannelSubjectList(Context context) {
//        mPlayUrlController.queryChannelSubjectList(compose(getBaseView().bindToLife()), context);
//    }

    /**
     * 查询当前频道栏目数据
     */
    public void queryColumnList(Context context, String subjectId, String isQuerySubject) {
        HeartBeatPresenter mPresenter = new HeartBeatPresenter();
        /*
         * 获取缓存的栏目、频道、Hasp Map
         * 如果为null  则重新请求
         * */
        //mController.queryChannelSubjectList(compose(getBaseView().bindToLife()));
        try {
            /*
             * 获取本地缓存的时间戳
             * */
            String liveDataTime = LiveDataHolder.get().getLiveDataTime();

            /*
             * 获取本地缓存的用户可用栏目Id
             * */
            String userCulomnId = LiveDataHolder.get().getChannelSubjectId();

            /*
             * 获取launcher.json返回的用户栏目Id
             * */
            String channelSubjectsID = "";
            if (null != LauncherService.getInstance().getLauncher().getExtraData()) {
                channelSubjectsID = LauncherService.getInstance().getLauncher().getExtraData().get(LiveTVActivity.KEY_CHANNEL_SUBJECTS);
            }
            SuperLog.debug(TAG, "launcher.json中配置的用户可用栏目id : " + channelSubjectsID);
            /*
             * 如果launcher.json中沒有返回用戶可用欄目id
             * 显示所有频道
             * */
            if (TextUtils.isEmpty(channelSubjectsID)) {
                HashMap<String, List<ChannelDetail>> mHashMap = new HashMap<String, List<ChannelDetail>>();
                SuperLog.info2SD(TAG,"<><><><><><><><><> get all channel info in queryColumnList");
                List<ChannelDetail> channelDetailList = LiveDataHolder.get().getAllChannels();
                List<Subject> mNewSubjectList = new ArrayList<>();
                Subject subject = new Subject();
                subject.setID("message");
                subject.setName("message");
                mNewSubjectList.add(subject);
                mHashMap.put(subject.getID(), channelDetailList);

                //缓存：栏目-频道 对应的Hash Map到本地
                LiveDataHolder.get().setSaveHashMap(mHashMap);

                //缓存欄目到本地
                LiveDataHolder.get().setSaveSubjectList(mNewSubjectList);
                getBaseView().onChannelColumn();
                return;
            }
            //EpgToast.showLongToast(OTTApplication.getContext(),"channelSubjectsID====="+channelSubjectsID);

            /*
             * 缓存的时间戳为空
             * 缓存的用户栏目id为空
             * 缓存的用户栏目id与launcher.json中获取的栏目id不一致
             * --》重新请求数据
             * */

            if(!TextUtils.isEmpty(subjectId) && TextUtils.equals(isQuerySubject, "1")){
                mPlayUrlController.queryChannelSubjectList(compose(getBaseView().bindToLife()), compose(getBaseView().bindToLife()), context, subjectId);
            }
            else if(!TextUtils.isEmpty(subjectId)){
                List<String> subjectIDs = new ArrayList<>();
                subjectIDs.add(subjectId);
                mPlayUrlController.queryChannelsBySubjectIDs(compose(getBaseView().bindToLife()), context, subjectIDs);
            }
            else if (LiveDataHolder.get().getChannelPlay() == null || LiveDataHolder.get().getChannelPlay().size() == 0 || LiveDataHolder.get().isChangeMulticastSwitch()) {
                //mPlayUrlController.queryChannelSubjectList(compose(getBaseView().bindToLife()), context);
                if (LiveDataHolder.get().isChangeMulticastSwitch()){
                    //清空播放记录，解决切换组播开关后播放上一次的频道
                    LiveTVCacheUtil.getInstance().recordPlayChannelInfo("", "");
                }
                mPresenter.queryChannelSubjectList(context, SharedPreferenceUtil.getInstance().getMulticastSwitch(),this);
            } else {

                if (!TextUtils.isEmpty(liveDataTime) && ((TextUtils.isEmpty(channelSubjectsID) && TextUtils.isEmpty(userCulomnId))
                        || (!TextUtils.isEmpty(channelSubjectsID) && !TextUtils.isEmpty(userCulomnId) && channelSubjectsID.equals(userCulomnId))
                )) {

                    long mLocalDate = Long.parseLong(LiveDataHolder.get().getLiveDataTime());
                    SuperLog.debug(TAG, "获取存储时间戳：" + mLocalDate);
                    //本地最新时间
                    Date mNowDate = new Date();
                    SuperLog.debug(TAG, "本地最新时间戳：" + mNowDate.getTime() / 1000);

                    /*
                     * 緩存时间一个小时
                     * */
                    if ((mNowDate.getTime() / 1000 - mLocalDate) > 3600) {
                        //获取频道栏目列表
                        //mPlayUrlController.queryChannelSubjectList(compose(getBaseView().bindToLife()), context);
                        mPresenter.queryChannelSubjectList(context, SharedPreferenceUtil.getInstance().getMulticastSwitch(), this);
                    } else {
                        List<Subject> cacheSubjectList = LiveDataHolder.get().getSaveSubjectList();
                        HashMap<String, List<ChannelDetail>> mHashMap = LiveDataHolder.get().getSaveHashMap();

                        if (null != cacheSubjectList && cacheSubjectList.size() > 0
                                && mHashMap != null && mHashMap.size() > 0) {
                            SuperLog.debug(TAG, "[queryColumnList] >>>> Load CacheSubjectList");

                            if (isViewAttached()) {
                                getBaseView().onChannelColumn();
                                //getBaseView().onLoadColumnSucc(cacheSubjectList);
                            }
                        } else {
                            //获取频道栏目列表
                            //mPlayUrlController.queryChannelSubjectList(compose(getBaseView().bindToLife()), context);
                            mPresenter.queryChannelSubjectList(context, SharedPreferenceUtil.getInstance().getMulticastSwitch(), this);

                        }
                    }
                } else {
                    //mPlayUrlController.queryChannelSubjectList(compose(getBaseView().bindToLife()), context);
                    mPresenter.queryChannelSubjectList(context, SharedPreferenceUtil.getInstance().getMulticastSwitch(), this);
                }
            }
        } catch (Exception e) {
            SuperLog.error(TAG,e);
        }
    /*else{
      SuperLog.debug(TAG,"[queryColumnList] >>>> queryColumnList");
      if(isViewAttached()){*/
        //mController.queryColumn(subjectId,compose(getBaseView().bindToLife()));
      /*}
    }*/
    }

    private void getPosition(ChannelDetail channelDetail, int index, String subjectID, String isQuerySubject, String currentChildSubjectID) {

        if (isViewAttached()) {
            getBaseView().onChannelIndex(index);
        }

        String mColumnId = "";
        List<Subject> saveSubjectList = new ArrayList<>();
        if(TextUtils.isEmpty(subjectID)){
            saveSubjectList = LiveDataHolder.get().getSaveSubjectList();
        }
        else{
            if(TextUtils.equals(isQuerySubject, "1")) {
                saveSubjectList = LiveDataHolder.get().getTargetChildSubject(subjectID);
            }
            else{
                saveSubjectList.add(LiveDataHolder.get().getTargetSubject(subjectID));
            }
        }

        if (saveSubjectList != null && (saveSubjectList.size() == 1 || saveSubjectList.size() == 0)) {
            //int channelIndex = LiveUtils.parseChannelIdIndex(channelNO, channelPlay);
            LiveDataHolder.get().setChannelSelectPosition(index);
        } else if (saveSubjectList != null && saveSubjectList.size() > 1) {
            for (Subject subject : saveSubjectList) {
                if (TextUtils.isEmpty(mColumnId)) {
                    mColumnId = subject.getID();
                } else {
                    mColumnId = mColumnId + "," + subject.getID();
                }
            }
            List<String> subjectIDs = channelDetail.getSubjectIDs();
            String[] split = mColumnId.split(",");
            boolean isGoOn = true;
            int indexPo = 0;
            for (int i = 0; i < split.length; i++) {
                if (!isGoOn) break;
                if(TextUtils.isEmpty(currentChildSubjectID) || (!TextUtils.isEmpty(currentChildSubjectID) && !subjectIDs.contains(currentChildSubjectID))) {
                    for (String subject : subjectIDs) {
                        if (split[i].equals(subject)) {
                            LiveDataHolder.get().setCulomnSelectPosition(i);
                            indexPo = i;
                            isGoOn = false;
                            break;
                        }
                    }
                }
                else{
                    if (split[i].equals(currentChildSubjectID)) {
                        LiveDataHolder.get().setCulomnSelectPosition(i);
                        indexPo = i;
                        isGoOn = false;
                        break;
                    }
                }
            }
            List<ChannelDetail> channelDetailList = LiveDataHolder.get().getSaveHashMap().get(split[indexPo]);
            if (channelDetailList != null && channelDetailList.size() > 0) {
                for (int i = 0; i < channelDetailList.size(); i++) {
                    if (channelDetail.getID().equals(channelDetailList.get(i).getID())) {
                        LiveDataHolder.get().setChannelSelectPosition(i);
                        break;
                    }
                }
            }
        }
    }

    //直播行为上传
    public void reportChannel(int action, String channelID, String nextChannelID,
                              String mediaID, String nextMediaID, String businessType, int isDownload, String productID, String subjectID) {
        ReportChannelRequest reportChannelRequest = new ReportChannelRequest();
        reportChannelRequest.setAction(action);

        reportChannelRequest.setChannelID(channelID);
        if (action == 2) {
            reportChannelRequest.setNextChannelID(nextChannelID);
            reportChannelRequest.setNextMediaID(nextMediaID);
        }
        reportChannelRequest.setMediaID(mediaID);
        reportChannelRequest.setBusinessType(businessType);
        reportChannelRequest.setProductID(productID);
        reportChannelRequest.setSubjectID(subjectID);
        mPlayUrlController.reportChannel(reportChannelRequest);

    }
}
