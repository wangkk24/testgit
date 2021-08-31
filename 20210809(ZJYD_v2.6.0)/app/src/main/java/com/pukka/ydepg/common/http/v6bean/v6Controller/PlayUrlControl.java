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
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.errorcode.ErrorCode;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.IPlayURLCallback;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelPlaybill;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.SubjectChannelList;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.BatchQueryChannelListBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryChannelListBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryChannelSubjectListRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ReportChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.BatchQueryChannelListBySubjectResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayChannelResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryChannelSubjectListResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.RxApiManager;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.launcher.util.ThreadTool;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.livetv.utils.LiveTVCacheUtil;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;


import io.reactivex.functions.Action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import com.pukka.ydepg.event.PlaySubListNo1ChannelEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 获取播放URL鉴权控制器
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: PlayUrlControl
 * @Package com.pukka.ydepg.common.http.v6bean.v6Controller
 * @date 2018/01/20 16:47
 */
public class PlayUrlControl {

    private static final String TAG = "PlayUrlControl";

    private IPlayURLCallback mCallback;

    private RxCallBack<PlayChannelResponse> mPlayChannelCallback;

    private RxCallBack<PlayVODResponse> mPlayVODCallback;

    private int mBatchQueryChannelListRequestCount = 0;

    public PlayUrlControl(IPlayURLCallback callback) {
        this.mCallback = callback;
    }

    /**
     * 强制取消频道鉴权:
     * 针对快速切台的时候,切换到下一个频道的时候,上一个频道数据此时回来了
     */
    public void forceDisposablePlayChannel(){
        if (null != mPlayChannelCallback) {
            mPlayChannelCallback.dispose();
            mPlayChannelCallback = null;
        }
    }
    /**
     * 直播鉴权
     *
     * @param request     直播鉴权request
     * @param transformer transformer
     */
    public synchronized void playChannel(PlayChannelRequest request, ObservableTransformer<PlayChannelResponse, PlayChannelResponse> transformer, Context context) {
        forceDisposablePlayChannel();
        mPlayChannelCallback = new RxCallBack<PlayChannelResponse>(HttpConstant.PLAYCHANNEL, context) {
            @Override
            public void onSuccess(PlayChannelResponse response) {
                SuperLog.info2SD(TAG,"Time[ReceivePlayChannel]="+System.currentTimeMillis());
                if (null != response && null != response.getResult()) {
                    Result result = response.getResult();
                    String returnCode = result.getRetCode();
                    if (!TextUtils.isEmpty(returnCode)) {
                        //鉴权结果
                        AuthorizeResult authorizeResult = response.getAuthorizeResult();
                        if (returnCode.equals(Result.RETCODE_OK)) {
                            String productId = null;
                            if (null != authorizeResult) {
                                productId = authorizeResult.getProductID();
                                mCallback.getProductID(productId);
                            }
                            String url = response.getPlayURL();
                            //鉴权成功获取到的产品ID
                            if (!TextUtils.isEmpty(productId) || !TextUtils.isEmpty(url)) {
                                //播放地址
                                SuperLog.debug(TAG, "[playChannel] get playUrl success.");
                                if (null != mCallback) {
                                    mCallback.getChannelPlayUrlSuccess(request.getChannelID(), StringUtils.splicingPlayUrl(url), response.getBookmark(),StringUtils.splicingPlayUrl(response.getAttachedPlayURL()));
                                }
                            }
                        } else if (!TextUtils.isEmpty(returnCode) && returnCode.equals("146020012")) {
                            /*
                             * 播放失败，清空本地记录的频道id和meidaId
                             * */
//                            EpgToast.showLongToast(OTTApplication.getContext(), "该频道不存在");
                            //直播频道被删除后的处理逻辑优化
                            //2.5.0
                            //现状：
                            //用户播放直播频道A后退出直播页；
                            //l A频道正常：直接进入A频道播放
                            //l A频道被删除：页面提示”该频道不存在”，然后黑屏
                            //         需要优化成：
                            //用户播放直播频道A后退出直播页；
                            //l A频道正常：直接进入A频道播放
                            //l A频道被删除：选择当前栏目中第一个直播频道进行播放
                            SharedPreferenceUtil.remove(LiveTVCacheUtil.RECORD_CHANNELINFO);
                            SharedPreferenceUtil.getInstance().putString(LiveTVCacheUtil.RECORD_CHANNELINFO, "");
                            EventBus.getDefault().post(new PlaySubListNo1ChannelEvent());
                        } else {
                            SuperLog.error(TAG,
                                    "[playChannel] error code = "
                                            + returnCode + " message = " + result.getRetMsg());
                            if (null != authorizeResult) {
                                //订购的产品列表为空
                                if (returnCode.equals("146021000")
                                        || (null == authorizeResult.getPricedProducts()
                                        || authorizeResult.getPricedProducts().size() == 0)) {
                                    EpgToast.showLongToast(OTTApplication.getContext(), "无可订购产品！");
                                    mCallback.playUrlError();
                                    return;
                                }
                                String url = response.getPlayURL();
                                //跳转到订购页时,需要携带的参数
                                if (null != mCallback) {
                                    mCallback.getPlayUrlFailed(request.getChannelID(), false, response.getAuthorizeResult(),request.getChannelID(), url, StringUtils.splicingPlayUrl(response.getAttachedPlayURL()));
                                }
                            } else {
                                if (null != mCallback) {
                                    mCallback.playUrlError();
                                }
                            }
                            //统一的错误提示
//              EpgToast.showLongToast(OTTApplication.getContext(),
//                  ErrorCode.findError(HttpConstant.PLAYCHANNEL, returnCode).getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFail(@NonNull Throwable e) {
                if (null != mCallback) {
                    mCallback.playUrlError();
                }
            }
        };
        SuperLog.info2SD(TAG,"Time[SendPlayChannel]="+System.currentTimeMillis());
        HttpApi.getInstance().getService().playChannel(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.PLAYCHANNEL, request)
                .compose(transformer)
                .subscribe(mPlayChannelCallback);
    }

    public void reportChannel(ReportChannelRequest request) {
        HttpApi.getInstance().getService().reportChannel(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.REPORTCHANNEL, request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reportChannelResponse -> {
                    if (null != reportChannelResponse && null != reportChannelResponse.getResult() && Result.RETCODE_OK.equals(reportChannelResponse.getResult().getRetCode())) {
                        SuperLog.debug(TAG, "reportChannel" + request.getChannelID() + " reportChannel success");
                        //平台侧确认 不需要 关注playSessionKey
//                        if (request.getAction() == 0 || request.getAction() == 2) {
//                            String playKey = reportChannelResponse.getPlaySessionKey();
//                            if(null!=playKey)
//                            {
//                                mCallback.getChannelPlayKey(playKey);
//                            }else
//                            {
//                                SuperLog.error(TAG,"playKey is null");
//                            }
//                        }
                    }
                }, throwable -> {
                    SuperLog.debug(TAG, "reportChannel" + request.getChannelID() + " reportChannel fail");
                });
    }


    /**
     * 回看鉴权
     *
     * @param sitcomNO  剧集编号
     * @param seriesID  连续剧父集ID
     * @param vodDetail vod详情
     */
    public void playVOD(String sitcomNO, String seriesID, VODDetail vodDetail, Context context) {
        if (null != mPlayVODCallback) {
            mPlayVODCallback.dispose();
            mPlayVODCallback = null;
        }
        mPlayVODCallback = new RxCallBack<PlayVODResponse>(HttpConstant.PLAYVOD, context) {
            @Override
            public void onSuccess(PlayVODResponse response) {
                //返回的code码
                String returnCode = response.getResult().getRetCode();
                //鉴权结果集
                AuthorizeResult authorizeResult = response.getAuthorizeResult();
                if (returnCode.equals(Result.RETCODE_OK)) {
                    //产品ID
                    String productId = "";
                    if (null != authorizeResult) {
                        productId = authorizeResult.getProductID();
                    }
                    //if(!TextUtils.isEmpty(productId)){
                    //鉴权成功返回的播放地址
                    String playUrl = response.getPlayURL();
                    if (null != mCallback) {
                        mCallback.getVODPlayUrlSuccess(StringUtils.splicingPlayUrl(playUrl), response.getBookmark(), productId);
                    }
                    //}
                } else {

                    //鉴权成功返回的播放地址
                    String playUrl = response.getPlayURL();
                    if (null != mCallback) {
                        mCallback.getPlayUrlFailed(null, true, authorizeResult,vodDetail.getID(), playUrl, null);
                    }
                }
            }

            @Override
            public void onFail(@NonNull Throwable e) {
                SuperLog.error(TAG,e);
                //统一做失败回调处理
                if (null != mCallback) {
                    mCallback.playUrlError();
                }
            }
        };
        PlayVODRequest playVODRequest = new PlayVODRequest();
        playVODRequest.setURLFormat("1");//HLS
        if (!TextUtils.isEmpty(sitcomNO)) {
            Episode episode = vodDetail.getEpisodes().get(Integer.parseInt(sitcomNO));
            playVODRequest.setVODID(episode.getVOD().getID());//子集VODID
        } else {
            playVODRequest.setVODID(vodDetail.getID());//VODID
        }
        Observable.just(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.PLAYVOD)
                .flatMap(new Function<String, ObservableSource<PlayVODResponse>>() {
                    @Override
                    public ObservableSource<PlayVODResponse> apply(@NonNull String url) throws Exception {
                        if (null != vodDetail.getMediaFiles() && vodDetail.getMediaFiles().size() > 0) {
                            //设置播放的VOD媒资ID
                            playVODRequest.setMediaID(vodDetail.getMediaFiles().get(0).getID());
                        }
                        if (!TextUtils.isEmpty(sitcomNO) && !TextUtils.isEmpty(seriesID)) {
                            List<Episode> episodeList = vodDetail.getEpisodes();
                            //连续剧,子集的父集ID
                            playVODRequest.setSeriesID(seriesID);
                            //剧集号对应的某一集的对象信息
                            VOD eposideVOD = episodeList.get(Integer.parseInt(sitcomNO)).getVOD();
                            if (null != eposideVOD
                                    && null != eposideVOD.getMediaFiles()
                                    && eposideVOD.getMediaFiles().size() > 0) {
                                //设置播放的VOD媒资ID
                                playVODRequest.setMediaID(eposideVOD.getMediaFiles().get(0).getID());
                            }
                        }
                        return HttpApi.getInstance().getService().playVOD(url, playVODRequest);
                    }
                }).filter(new Predicate<PlayVODResponse>() {
            @Override
            public boolean test(@NonNull PlayVODResponse response) throws Exception {
                if (null != response && null != response.getResult()) {
                    String returnCode = response.getResult().getRetCode();
                    String returnMsg = response.getResult().getRetMsg();
                    if (!TextUtils.isEmpty(returnCode)) {
                        if (returnCode.equals(Result.RETCODE_OK)) {
                            String productId = "";
                            if (null != response.getAuthorizeResult()) {
                                productId = response.getAuthorizeResult().getProductID();
                            }
                            String playUrl = response.getPlayURL();
                            //判断产品ID是否存在或者Url是否存在
                            if (!TextUtils.isEmpty(productId) || !TextUtils.isEmpty(playUrl)) {
                                return true;
                            }
                        } else {
                            SuperLog.error(TAG, "[PlayVOD] error code = "
                                    + returnCode
                                    + " message = " + returnMsg);
                            //鉴权结果集
                            AuthorizeResult authorizeResult = response.getAuthorizeResult();
                            boolean isFlag = false;
                            boolean isShowToast = false;
                            if (null != authorizeResult) {
                                //判断产品列表是否为空
                                if ((returnCode.equals("144020000"))
                                        || (null == authorizeResult.getPricedProducts()
                                        || authorizeResult.getPricedProducts().size() == 0)) {
                                    ThreadTool.switchMainThread(new Action() {
                                        @Override
                                        public void run() throws Exception {
                                            EpgToast.showLongToast(OTTApplication.getContext(), "无可订购产品！");
                                        }
                                    });
                                    isShowToast = true;
                                } else {
                                    isFlag = true;
                                }
                            }
                            if (!isShowToast) {
                                //统一的错误提示
                                ThreadTool.switchMainThread(new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        EpgToast.showLongToast(OTTApplication.getContext(),
                                                ErrorCode.findError(HttpConstant.PLAYVOD, returnCode).getMessage());
                                    }
                                });
                            }
                            return isFlag;
                        }
                    }
                }
                return false;
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mPlayVODCallback);
    }

    /*
     * 新建HashMap栏目+频道
     * */
    HashMap<String, List<ChannelDetail>> mHashMap = new HashMap<String, List<ChannelDetail>>();

    /*
     * 获取栏目下频道列表
     * QueryChannelListBySubject
     * */
    public void queryChannelSubjectList(ObservableTransformer<QueryChannelSubjectListResponse,
            QueryChannelSubjectListResponse> transformer, ObservableTransformer<BatchQueryChannelListBySubjectResponse,
            BatchQueryChannelListBySubjectResponse> transformer2, Context context, String subjectId) {

        QueryChannelSubjectListRequest mQueryChannelSubjectListRequest = new QueryChannelSubjectListRequest();
        mQueryChannelSubjectListRequest.setSubjectID(subjectId);
        /*
         * 获取的条数
         * */
        mQueryChannelSubjectListRequest.setOffset("0");
        mQueryChannelSubjectListRequest.setCount("49");

        SuperLog.debug(TAG, "[根据此ID查询直播子栏目列表(观看栏目列表)] 直播父栏目ID=" + subjectId);

        RxCallBack<QueryChannelSubjectListResponse> rxCallBack = new RxCallBack<QueryChannelSubjectListResponse>(HttpConstant.QUERYCHANNELSUBJECTLIST, context) {
            @Override
            public void onSuccess(QueryChannelSubjectListResponse queryChannelSubjectListResponse) {

                Result result = queryChannelSubjectListResponse.getResult();

                if (result != null
                        && !TextUtils.isEmpty(result.getRetCode())
                        && !result.getRetCode().equals(Result.RETCODE_OK)) {
                    SuperLog.error(TAG, "[onNext] QueryChannelListBySubject code = " + result.getRetCode()
                            + " message = " + result.getRetMsg());
                }

                if (result != null && !TextUtils.isEmpty(result.getRetCode())
                        && result.getRetCode().equals(Result.RETCODE_OK)
                        && queryChannelSubjectListResponse.getSubjects() != null &&
                        queryChannelSubjectListResponse.getSubjects().size() > 0) {

                    /** 对返回的数据进行Hash Map重组  Key:subjectIDs;Value:频道信息
                     * */
                    List<Subject> subjectList = queryChannelSubjectListResponse.getSubjects();
                    LiveDataHolder.get().setChildSubject(subjectList);
                    List<List<Subject>> splitSubjectList = splitList(subjectList, 5);
                    mBatchQueryChannelListRequestCount = splitSubjectList.size();
                    for(List<Subject> list:splitSubjectList){
                        queryChannelsBySubjects(transformer2, context, list);
                    }
                } else {
                    List<String> subjectIDs = new ArrayList<>();
                    subjectIDs.add(subjectId);
                    queryChannelsBySubjectIDs(transformer2, context, subjectIDs);
                }
            }

            @Override
            public void onFail(Throwable e) {
                SuperLog.error(TAG, "[onError] QueryChannelListBySubject = " + e.toString());
                LiveDataHolder.get().setChildSubject(null);
                mCallback.onChannelColumn();
            }
        };
        RxApiManager.get().add(HttpConstant.QUERYCHANNELSUBJECTLIST, rxCallBack);
        HttpApi.getInstance().getService().queryChannelSubjectList(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERYCHANNELSUBJECTLIST, mQueryChannelSubjectListRequest)
                .compose(transformer)
                .subscribe(rxCallBack);

    }

    public void queryChannelsBySubjects(ObservableTransformer<BatchQueryChannelListBySubjectResponse,
            BatchQueryChannelListBySubjectResponse> transformer, Context context, List<Subject> subjects){
        List<QueryChannelListBySubjectRequest> requests = new ArrayList<>();
        if(subjects != null && subjects.size() > 0){
            for(Subject subject:subjects){
                QueryChannelListBySubjectRequest request = new QueryChannelListBySubjectRequest();
                request.setSubjectID(subject.getID());
                request.setCount("50");
                request.setOffset("0");
                requests.add(request);
            }
        }
        BatchQueryChannelListBySubjectRequest batchQueryChannelListBySubjectRequest = new BatchQueryChannelListBySubjectRequest();
        batchQueryChannelListBySubjectRequest.setConditions(requests);
        RxCallBack<BatchQueryChannelListBySubjectResponse> rxCallBack = new RxCallBack<BatchQueryChannelListBySubjectResponse>(HttpConstant.QUERYCHANNELLISTBYSUBJECT, context) {
            @Override
            public void onSuccess(BatchQueryChannelListBySubjectResponse queryChannelSubjectListResponse) {

                Result result = queryChannelSubjectListResponse.getResult();

                if (result != null
                        && !TextUtils.isEmpty(result.getRetCode())
                        && !result.getRetCode().equals(Result.RETCODE_OK)) {
                    SuperLog.error(TAG, "[onNext] QueryChannelListBySubject code = " + result.getRetCode()
                            + " message = " + result.getRetMsg());
                }

                if (result != null && !TextUtils.isEmpty(result.getRetCode())
                        && result.getRetCode().equals(Result.RETCODE_OK)
                        && queryChannelSubjectListResponse.getSubjectChannelList() != null &&
                        queryChannelSubjectListResponse.getSubjectChannelList().size() > 0) {

                    /** 对返回的数据进行Hash Map重组  Key:subjectIDs;Value:频道信息
                     * */
                    initSubjectChannelInfo(queryChannelSubjectListResponse.getSubjectChannelList());
                    mBatchQueryChannelListRequestCount--;
                    if(mBatchQueryChannelListRequestCount == 0) {
                        HashMap<String, List<ChannelDetail>> hashMap = LiveDataHolder.get().getSaveHashMap();
                        for(Map.Entry<String, List<ChannelDetail>> entry : channelDetailMap.entrySet()){
                            String mapKey = entry.getKey();
                            if(hashMap.get(mapKey) == null){
                                hashMap.put(mapKey, channelDetailMap.get(mapKey));
                            }
                        }
                        channelDetailMap.clear();
                        LiveDataHolder.get().changeSaveHashMap(hashMap);
                        mCallback.onChannelColumn();
                    }
                } else {
                    mBatchQueryChannelListRequestCount--;
                    if(mBatchQueryChannelListRequestCount == 0) {
                        HashMap<String, List<ChannelDetail>> hashMap = LiveDataHolder.get().getSaveHashMap();
                        for(Map.Entry<String, List<ChannelDetail>> entry : channelDetailMap.entrySet()){
                            String mapKey = entry.getKey();
                            if(hashMap.get(mapKey) == null){
                                hashMap.put(mapKey, channelDetailMap.get(mapKey));
                            }
                        }
                        channelDetailMap.clear();
                        LiveDataHolder.get().changeSaveHashMap(hashMap);
                        mCallback.onChannelColumn();
                    }
                }
            }

            @Override
            public void onFail(Throwable e) {
                SuperLog.error(TAG, "[onError] QueryChannelListBySubject = " + e.toString());
                mBatchQueryChannelListRequestCount--;
                if(mBatchQueryChannelListRequestCount == 0) {
                    HashMap<String, List<ChannelDetail>> hashMap = LiveDataHolder.get().getSaveHashMap();
                    for(Map.Entry<String, List<ChannelDetail>> entry : channelDetailMap.entrySet()){
                        String mapKey = entry.getKey();
                        if(hashMap.get(mapKey) == null){
                            hashMap.put(mapKey, channelDetailMap.get(mapKey));
                        }
                    }
                    channelDetailMap.clear();
                    LiveDataHolder.get().changeSaveHashMap(hashMap);
                    mCallback.onChannelColumn();
                }
            }
        };
        RxApiManager.get().add(HttpConstant.QUERYCHANNELLISTBYSUBJECT, rxCallBack);
        HttpApi.getInstance().getService().queryChannelListBySubjectIDs(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERYCHANNELLISTBYSUBJECT, batchQueryChannelListBySubjectRequest)
                .compose(transformer)
                .subscribe(rxCallBack);
    }

    public void queryChannelsBySubjectIDs(ObservableTransformer<BatchQueryChannelListBySubjectResponse,
            BatchQueryChannelListBySubjectResponse> transformer, Context context, List<String> subjects){
        List<QueryChannelListBySubjectRequest> requests = new ArrayList<>();
        if(subjects != null && subjects.size() > 0){
            for(String subjectID:subjects){
                QueryChannelListBySubjectRequest request = new QueryChannelListBySubjectRequest();
                request.setSubjectID(subjectID);
                request.setCount("50");
                request.setOffset("0");

                requests.add(request);
            }
        }
        BatchQueryChannelListBySubjectRequest batchQueryChannelListBySubjectRequest = new BatchQueryChannelListBySubjectRequest();
        batchQueryChannelListBySubjectRequest.setConditions(requests);
        RxCallBack<BatchQueryChannelListBySubjectResponse> rxCallBack = new RxCallBack<BatchQueryChannelListBySubjectResponse>(HttpConstant.QUERYCHANNELLISTBYSUBJECT, context) {
            @Override
            public void onSuccess(BatchQueryChannelListBySubjectResponse queryChannelSubjectListResponse) {

                Result result = queryChannelSubjectListResponse.getResult();

                if (result != null
                        && !TextUtils.isEmpty(result.getRetCode())
                        && !result.getRetCode().equals(Result.RETCODE_OK)) {
                    SuperLog.error(TAG, "[onNext] QueryChannelListBySubject code = " + result.getRetCode()
                            + " message = " + result.getRetMsg());
                }

                if (result != null && !TextUtils.isEmpty(result.getRetCode())
                        && result.getRetCode().equals(Result.RETCODE_OK)
                        && queryChannelSubjectListResponse.getSubjectChannelList() != null &&
                        queryChannelSubjectListResponse.getSubjectChannelList().size() > 0) {

                    /** 对返回的数据进行Hash Map重组  Key:subjectIDs;Value:频道信息
                     * */
                    LiveDataHolder.get().setChildSubject(getSubjectList(queryChannelSubjectListResponse.getSubjectChannelList()));
                    initSubjectChannelInfo(queryChannelSubjectListResponse.getSubjectChannelList());
                    HashMap<String, List<ChannelDetail>> hashMap = LiveDataHolder.get().getSaveHashMap();
                    for(Map.Entry<String, List<ChannelDetail>> entry : channelDetailMap.entrySet()){
                        String mapKey = entry.getKey();
                        if(hashMap.get(mapKey) == null){
                            hashMap.put(mapKey, channelDetailMap.get(mapKey));
                        }
                    }
                    channelDetailMap.clear();
                    LiveDataHolder.get().changeSaveHashMap(hashMap);
                    mCallback.onChannelColumn();
                } else {
                    LiveDataHolder.get().setChildSubject(null);
                    HashMap<String, List<ChannelDetail>> hashMap = LiveDataHolder.get().getSaveHashMap();
                    for(Map.Entry<String, List<ChannelDetail>> entry : channelDetailMap.entrySet()){
                        String mapKey = entry.getKey();
                        if(hashMap.get(mapKey) == null){
                            hashMap.put(mapKey, channelDetailMap.get(mapKey));
                        }
                    }
                    channelDetailMap.clear();
                    LiveDataHolder.get().changeSaveHashMap(hashMap);
                    mCallback.onChannelColumn();
                }
            }

            @Override
            public void onFail(Throwable e) {
                SuperLog.error(TAG, "[onError] QueryChannelListBySubject = " + e.toString());
                LiveDataHolder.get().setChildSubject(null);
                HashMap<String, List<ChannelDetail>> hashMap = LiveDataHolder.get().getSaveHashMap();
                for(Map.Entry<String, List<ChannelDetail>> entry : channelDetailMap.entrySet()){
                    String mapKey = entry.getKey();
                    if(hashMap.get(mapKey) == null){
                        hashMap.put(mapKey, channelDetailMap.get(mapKey));
                    }
                }
                channelDetailMap.clear();
                LiveDataHolder.get().changeSaveHashMap(hashMap);
                mCallback.onChannelColumn();
            }
        };
        RxApiManager.get().add(HttpConstant.QUERYCHANNELLISTBYSUBJECT, rxCallBack);
        HttpApi.getInstance().getService().queryChannelListBySubjectIDs(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERYCHANNELLISTBYSUBJECT, batchQueryChannelListBySubjectRequest)
                .compose(transformer)
                .subscribe(rxCallBack);
    }

    private List<Subject> getSubjectList(List<SubjectChannelList> channelLists){
        List<Subject> result = new ArrayList<>();
        if(channelLists != null && channelLists.size() > 0){
            for(SubjectChannelList subjectChannelList:channelLists){
                result.add(subjectChannelList.getSubject());
            }
        }
        return result;
    }

    private Map<String, List<ChannelDetail>> channelDetailMap = new HashMap<>();

    private void initSubjectChannelInfo(List<SubjectChannelList> subjectChannelLists){
        if(subjectChannelLists != null && subjectChannelLists.size() > 0){
                for(SubjectChannelList subjectChannelList:subjectChannelLists){
                    List<ChannelDetail> channelDetails = new ArrayList<>();
                    if(subjectChannelList.getChannelPlaybills() != null
                            && subjectChannelList.getChannelPlaybills().size() > 0){
                        for(ChannelPlaybill channelPlaybill:subjectChannelList.getChannelPlaybills()){
                            channelDetails.add(LiveDataHolder.get().getChannelDetailByChannelID(channelPlaybill.getChannelDetail().getID()));
                        }
                    }
                    channelDetailMap.put(subjectChannelList.getSubject().getID(), channelDetails);
                }
        }
    }

    private List<List<Subject>> splitList(List<Subject> list , int groupSize){
        int length = list.size();
        // 计算可以分成多少组
        int num = ( length + groupSize - 1 )/groupSize ; // TODO
        List<List<Subject>> newList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            // 开始位置
            int fromIndex = i * groupSize;
            // 结束位置
            int toIndex = (i+1) * groupSize < length ? ( i+1 ) * groupSize : length ;
            newList.add(list.subList(fromIndex,toIndex)) ;
        }
        return  newList ;
    }

}