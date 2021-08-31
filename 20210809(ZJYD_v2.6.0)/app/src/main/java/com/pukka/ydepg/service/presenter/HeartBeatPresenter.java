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
package com.pukka.ydepg.service.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.IPlayURLCallback;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDynamicProperties;
import com.pukka.ydepg.common.http.v6bean.v6node.ContentRight;
import com.pukka.ydepg.common.http.v6bean.v6node.PersonalDataVersion;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryAllChannelDynamicPropertiesRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryAllChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryChannelStcPropsBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryChannelSubjectListRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.OnLineHeartbeatResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PBSRemixRecommendResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryAllChannelDynamicPropertiesResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryAllChannelResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryChannelStcPropsBySubjectResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryChannelSubjectListResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySuperScriptResponse;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.HeartBeatUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.LogSecurityUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.RxApiManager;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.mvp.presenter.BasePresenter;
import com.pukka.ydepg.launcher.session.Session;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.catchup.presenter.TVODPresenter;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.livetv.rate.RateUtil;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.livetv.utils.LiveUtils;
import com.pukka.ydepg.moudule.mytv.utils.EPGMainDataHolder;
import com.pukka.ydepg.moudule.player.node.Schedule;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;
import com.pukka.ydepg.moudule.player.util.PlayerUtils;
import com.pukka.ydepg.service.HeartBeatService;
import com.pukka.ydepg.service.presenter.contract.HeartBeatContract;
import com.pukka.ydepg.xmpp.XmppService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 心跳presenter
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: HeartBeatPresenter
 * @Package com.pukka.ydepg.service.presenter
 * @date 2018/01/16 11:25
 */
public class HeartBeatPresenter extends BasePresenter<HeartBeatContract.HeartBeatView> implements HeartBeatContract.HeartBeatPresenter {
    private static String TAG = "HeartBeatPresenter";
    private Context mContext;

    /**
     * 心跳
     *
     * @param isFirst 是不是第一次启动心跳,即service如果是第一次启动的话就是true；
     */
    @Override
    public void startHeartBeatService(boolean isFirst, Context context) {
        SuperLog.info2SD(TAG, "Send OnLineHeartbeat request, isFirst=" + isFirst);
        HttpApi.getInstance().getService().onLineHeartbeat(HttpUtil.getVspUrl(HttpConstant.ONLINEHEARTBEAT))
                .compose(onCompose(null == mView ? null : mView.bindToLife()))
                .subscribe(new RxCallBack<OnLineHeartbeatResponse>(HttpConstant.ONLINEHEARTBEAT, context) {
                    @Override
                    public void onSuccess(OnLineHeartbeatResponse response) {
                        if(null!=response.getResult()&&Result.RETCODE_OK.equals(response.getResult().getRetCode()))
                        {
                            String log = LogSecurityUtil.getSecurityLog(response.toString(), LogSecurityUtil.REGEX_TOKEN, LogSecurityUtil.REPLACEMENT_TOKEN);
                            SuperLog.info2SD(TAG, LogSecurityUtil.getSecurityLog(log, LogSecurityUtil.REGEX_SUBSCRIBER, LogSecurityUtil.REPLACEMENT_SUBSCRIBER));
                            SessionService service = SessionService.getInstance();
                            Session session = service.getSession();
                            session.setUserFilter(response.getUserFilter());
                            session.setUserVODListFilter(response.getUserVODListFilter());
                            SuperLog.debug("UserToken===","response.getUserToken()="+TextUtils.isEmpty(response.getUserToken()));
                            if(!TextUtils.isEmpty(response.getUserToken())){
                                SuperLog.debug("UserToken===","response.getUserToken()="+response.getUserToken());
                            }
                            service.commitSession();
                            //心跳接口中如果更新了userToken则XMPP token也需要更新
                            if (!TextUtils.isEmpty(response.getUserToken())) {
                                session.setUserToken(response.getUserToken());
                                SuperLog.info2SD(TAG, "Begin to update XMPP token after heart beat.");
                                XmppService.getInstance().updateToken(response.getUserToken());
                            }

                            if (mView != null) {
                                //用户数据版本
                                PersonalDataVersion version = response.getPersonalDataVersions();
                                if(version!=null) {
                                    if (null != version.getBookmark() && HeartBeatUtil.getInstance().isVersionChange(HeartBeatService.VersionType.VERSION_BOOKMARK, version.getBookmark())) {
                                        //书签版本号变化回调
                                        mView.onBookmarkVersionChange(version.getBookmark());
                                    }

                                    if (null != version.getFavorite() && HeartBeatUtil.getInstance().isVersionChange(HeartBeatService.VersionType.VERSION_FAVORITE, version.getFavorite())) {
                                        // 收藏版本号变化回调
                                        mView.onFavoriteVersionChange(version.getFavorite());
                                    }
                                    if (null != version.getChannelVersion() && null != version.getSubscribeVersion()) {
                                        mView.onChannelVersionChange(       //频道版本号是否变化回调
                                                HeartBeatUtil.getInstance().isVersionChange(HeartBeatService.VersionType.VERSION_CHANNEL, version.getChannelVersion()),
                                                version.getChannelVersion(),
                                                version.getSubscribeVersion());
                                    }
                                }

                                if (!TextUtils.isEmpty(response.getNextCallInterval())) {
                                    mView.onStartHeartBeatSucc(response.getNextCallInterval());
                                }
                            }
                        }else
                        {
                            if (mView != null) {
                                SuperLog.error(TAG, "OnLineHeartbeat response error!!!");
                                mView.onStartHeartBeatError(isFirst);
                            }
                        }


                    }

                    @Override
                    public void onFail(@NonNull Throwable e) {
                        SuperLog.error(TAG, e);
                        if (mView != null) {
                            mView.onStartHeartBeatError(isFirst);
                        }
                    }
                });
    }

    /**
     * 查询全部频道
     *
     * @param channelVersion
     */
    @Override
    public void queryAllChannel(String channelVersion, String subscribeVersion, Context context) {
        this.mContext = context;
        QueryAllChannelRequest request = new QueryAllChannelRequest();
        request.setUserFilter(SessionService.getInstance().getSession().getUserFilter());
        HttpApi.getInstance().getService().queryAllChannel(HttpUtil.getVspUrl(HttpConstant.QUERYALLCHANNEL), request)
                .compose(onCompose(mView.bindToLife()))
                .subscribe(new RxCallBack<QueryAllChannelResponse>(HttpConstant.QUERYALLCHANNEL, context) {
                    @Override
                    public void onSuccess(QueryAllChannelResponse response) {
                        if (null != response && null != response.getResult()) {
                            if (response.getResult().getRetCode().equals(Result.RETCODE_OK)) {
                                SuperLog.info2SD(TAG, "[queryAllChannel] onSuccess()");
                                String rspChannelVersion = response.getChannelVersion();
                                if (TextUtils.isEmpty(rspChannelVersion)) {
                                    rspChannelVersion = channelVersion;
                                }
                                if (!TextUtils.isEmpty(rspChannelVersion)) {
                                    //更新本地频道版本
                                    HeartBeatUtil.getInstance().setVersion(HeartBeatService.VersionType.VERSION_CHANNEL, rspChannelVersion);
                                }
                                //解析频道详情
                                List<ChannelDetail> channelDetails = response.getChannelDetails();
                                Map<String, ChannelDetail> channelDetailMap = new HashMap<>();
                                List<Schedule> schedules = new ArrayList<>();
                                if (null != channelDetails && !channelDetails.isEmpty()) {
                                    for (ChannelDetail channelDetail : channelDetails) {
                                        RateUtil.setRateInfo(channelDetail);  //Added by liuxia at20201014设置频道多码率信息
                                        channelDetailMap.put(channelDetail.getID(), channelDetail);
                                        Schedule schedule = LiveUtils.parseSingleSchedule(channelDetail);
                                        schedules.add(schedule);
                                    }
                                }
                                LiveDataHolder.get().setAllSchedules(schedules);
                                LiveDataHolder.get().setMapID2ChannelDetail(channelDetailMap);
                                SuperLog.info2SD(TAG, "<><><><><><><><><> save all channel info to file in on success of queryAllChannel");
                                LiveDataHolder.get().setAllChannels(channelDetails);
                                //获得可回看的频道列表，语音回看进行限制
                                TVODPresenter presenter = new TVODPresenter(context);
                                presenter.queryChannelSubjectList();


                                //在心跳的频道的版本号或频道订购版本号变化时调用此接口进行缓存刷新
                                //频道增加了,需要及时刷新频道信息
                                //查询channel动态属性,用于更新频道号channelNO
                                queryAllChannelDynamicProperties(subscribeVersion, context);
                            }
                        }
                    }

                    @Override
                    public void onFail(@NonNull Throwable e) {
                        SuperLog.error(TAG, e);
                    }
                });
    }

    /**
     * 查询频道动态参数
     */
    @Override
    public void queryAllChannelDynamicProperties(String subscribeVersion, Context context) {
        QueryAllChannelDynamicPropertiesRequest request = new QueryAllChannelDynamicPropertiesRequest();
        HttpApi.getInstance().getService().queryAllChannelDynamicProperties(HttpUtil.getVspUrl(HttpConstant.QUERYALLCHANNELDYNAMICPROPERTIES), request)
                .compose(onCompose(mView.bindToLife()))
                .subscribe(new RxCallBack<QueryAllChannelDynamicPropertiesResponse>(HttpConstant.QUERYALLCHANNELDYNAMICPROPERTIES, context) {
                    @Override
                    public void onSuccess(QueryAllChannelDynamicPropertiesResponse response) {
                        if (null != response && null != response.getResult()) {
                            if (Result.RETCODE_OK.equals(response.getResult().getRetCode())) {
                                SuperLog.info2SD(TAG, "[queryAllChannelDynamicProperties] onSuccess");
                                if (!TextUtils.isEmpty(subscribeVersion)) {
                                    HeartBeatUtil.getInstance().setVersion(HeartBeatService.VersionType.VERSION_SUBSCRIBE, subscribeVersion);
                                }
                                parseDynamicProperties(response, context);
                            }
                        }
                    }

                    @Override
                    public void onFail(@NonNull Throwable e) {
                        SuperLog.error(TAG, e);
                    }
                });
    }

    @Override
    public void querySuperScript() {
        String jsessionid = SharedPreferenceUtil.getInstance().getSeesionId();
        if (!jsessionid.contains("JSESSIONID=")) {
            jsessionid = "JSESSIONID=" + jsessionid;
        }
        Request request = new Request.Builder()
                .header("Cookie", jsessionid)
                .header("Set-Cookie", jsessionid + "; Path=/VSP/")
                .header("User-Agent", "OTT-Android")
                .header("authorization", SessionService.getInstance().getSession().getUserToken())
                .header("EpgSession", jsessionid)
                .header("Location", ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL())
                .url(HttpConstant.PBS_GetSuperScriptInfo)
                .build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SuperLog.error(TAG, "querySuperScript fail");
                SuperLog.error(TAG, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                SuperLog.info2SD(TAG, "querySuperScript success responseData = " + body);
                QuerySuperScriptResponse querySuperScriptResponse = JsonParse.json2Object(body, QuerySuperScriptResponse.class);
                if (null != querySuperScriptResponse && !CollectionUtil.isEmpty(querySuperScriptResponse.getSuperScripts())) {
                    EPGMainDataHolder.setSuperScript(querySuperScriptResponse.getSuperScripts());
                }
            }
        });
    }

    /**
     * 解析频道动态属性
     *
     * @param response
     */
    private void parseDynamicProperties(QueryAllChannelDynamicPropertiesResponse response, Context context) {
        if (null == response) return;
        List<Schedule> allSchedules = LiveDataHolder.get().getAllSchedules();
        SuperLog.info2SD(TAG, "<><><><><><><><><> get all channel info in parseDynamicProperties");
        List<ChannelDetail> channelDetails = LiveDataHolder.get().getAllChannels();
        List<ChannelDynamicProperties> channelDynamaicProp = response.getChannelDynamaicProp();
        if (null != channelDynamaicProp && !channelDynamaicProp.isEmpty()) {
            for (ChannelDynamicProperties dynamicProperties : channelDynamaicProp) {
                if (!TextUtils.isEmpty(dynamicProperties.getID())) {
                    updateAllChannel(allSchedules, dynamicProperties);
                    updateAllChannelDetail(channelDetails, dynamicProperties, context);
                }
            }
            Map<String, ChannelDetail> channelDetailMap = new HashMap<>();
            if (null != channelDetails && !channelDetails.isEmpty()) {
                for (ChannelDetail channelDetail : channelDetails) {
                    channelDetailMap.put(channelDetail.getID(), channelDetail);
                }
            }
            LiveDataHolder.get().setMapID2ChannelDetail(channelDetailMap);
            LiveDataHolder.get().setAllSchedules(allSchedules);
            SuperLog.info2SD(TAG, "<><><><><><><><><> save all channel info to file in parseDynamicProperties");
            LiveDataHolder.get().setAllChannels(channelDetails);
            //获得可回看的频道列表，语音回看进行限制
            TVODPresenter presenter = new TVODPresenter(context);
            presenter.queryChannelSubjectList();
        }
        //查询栏目-组织栏目频道数据
        queryChannelSubjectList(context, SharedPreferenceUtil.getInstance().getMulticastSwitch(), null);
    }

    /**
     * 更新全部频道信息
     *
     * @param allSchedules
     * @param dynamicProperties
     */
    private void updateAllChannel(List<Schedule> allSchedules, ChannelDynamicProperties dynamicProperties) {
        if (null != allSchedules && !allSchedules.isEmpty()) {
            for (Schedule schedule : allSchedules) {
                if (TextUtils.equals(schedule.getId(), dynamicProperties.getID()) && !TextUtils.isEmpty(String.valueOf(dynamicProperties.getChannelNO()))) {
                    //更新频道号
                    schedule.setChannelNo(String.valueOf(dynamicProperties.getChannelNO()));
                    if (null != dynamicProperties.getPhysicalChannelsDynamicProperties() && dynamicProperties.getPhysicalChannelsDynamicProperties().size() > 0) {
                        ContentRight pltvCR = dynamicProperties.getPhysicalChannelsDynamicProperties().get(0).getPltvCR();
                        //更新时移信息
                        schedule.setPltvLength(PlayerUtils.getPLTVLength(pltvCR));
                    }
                }
            }
        }
    }

    /**
     * 更新全部频道信息
     *
     * @param allSchedules
     * @param dynamicProperties
     */
    private void updateAllChannelDetail(List<ChannelDetail> allSchedules, ChannelDynamicProperties dynamicProperties, Context context) {
        if (null != allSchedules && !allSchedules.isEmpty()) {
            for (ChannelDetail schedule : allSchedules) {
                if (TextUtils.equals(schedule.getID(), dynamicProperties.getID()) && !TextUtils.isEmpty(String.valueOf(dynamicProperties.getChannelNO()))) {
                    //更新频道号
                    schedule.setChannelNO(String.valueOf(dynamicProperties.getChannelNO()));
                    if (null != dynamicProperties.getPhysicalChannelsDynamicProperties() && dynamicProperties.getPhysicalChannelsDynamicProperties().size() > 0) {
                        ContentRight pltvCR = dynamicProperties.getPhysicalChannelsDynamicProperties().get(0).getPltvCR();
                        //更新时移信息
                        schedule.setPltvLength(PlayerUtils.getPLTVLength(pltvCR));
                    }
                }
            }
        }
    }

    /*
     * 获取栏目下频道列表QueryChannelListBySubject
     */
    public void queryChannelSubjectList(Context context, boolean multicastSwitch, IPlayURLCallback mCallback) {
        SuperLog.debug(TAG, "queryChannelSubjectList====获取栏目下频道列表===start");

        // 获取终端参数Id、用于请求queryChannelListBySubject的id
        String liveSubjectID;
        if (multicastSwitch) {
            //组播逻辑
            liveSubjectID = CommonUtil.getConfigValue(Constant.LIVE_SUBJECT_ID_MULTICAST);
        } else {
            //单播逻辑
            liveSubjectID = CommonUtil.getConfigValue(Constant.LIVE_SUBJECT_ID);
        }

        // 获取本地存储的频道，并进行筛选
        SuperLog.info2SD(TAG, "MultiCastSwitch= " + multicastSwitch + "\tSubjectID=" + liveSubjectID);
        List<ChannelDetail> channelDetailList = LiveDataHolder.get().getAllChannels();

        // 新建HashMap栏目+频道
        HashMap<String, List<ChannelDetail>> mHashMap = new HashMap<String, List<ChannelDetail>>();
        if (TextUtils.isEmpty(liveSubjectID)) {
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
            LiveDataHolder.get().setChannelPlay(channelDetailList);
        }

        QueryChannelSubjectListRequest queryChannelSubjectListRequest = new QueryChannelSubjectListRequest();
        queryChannelSubjectListRequest.setSubjectID(liveSubjectID);
        queryChannelSubjectListRequest.setOffset("0");
        queryChannelSubjectListRequest.setCount("49");
        RxCallBack<QueryChannelSubjectListResponse> rxCallBack = new RxCallBack<QueryChannelSubjectListResponse>(HttpConstant.QUERYCHANNELSUBJECTLIST, context) {
            @Override
            public void onSuccess(QueryChannelSubjectListResponse queryChannelListBySubjectResponse) {
                SuperLog.info2SD(TAG, "queryChannelListBySubjectResponse onSuccess");
                Result result = queryChannelListBySubjectResponse.getResult();
                // 用户可播的频道
                List<ChannelDetail> mUserChannelDetaiCanPlay = new ArrayList<>();

                if (result != null
                        && !TextUtils.isEmpty(result.getRetCode())
                        && !result.getRetCode().equals(Result.RETCODE_OK)) {
                    SuperLog.error(TAG, "[onNext] QueryChannelListBySubject code = " + result.getRetCode() + " message = " + result.getRetMsg());
                }

                if (result != null && !TextUtils.isEmpty(result.getRetCode())
                        && result.getRetCode().equals(Result.RETCODE_OK)
                        && queryChannelListBySubjectResponse.getSubjects() != null &&
                        queryChannelListBySubjectResponse.getSubjects().size() > 0) {

                    //对返回的数据进行Hash Map重组  Key:subjectIDs;Value:频道信息
                    List<Subject> subjectList = queryChannelListBySubjectResponse.getSubjects();

                    // 新建一个栏目List防止数据重复
                    List<Subject> mNewSubjectList = new ArrayList<>();
                    SuperLog.info2SD(TAG, "queryPBSRemixRecommend up");
                    queryPBSRemixRecommend(subjectList, mNewSubjectList, channelDetailList, mHashMap, mUserChannelDetaiCanPlay, mCallback, multicastSwitch);
                } else {
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
                    LiveDataHolder.get().setChannelPlay(channelDetailList);
                }
            }

            @Override
            public void onFail(Throwable e) {
                SuperLog.error(TAG, "queryChannelSubjectList====获取栏目下频道列表failed");
            }
        };
        RxApiManager.get().add(HttpConstant.QUERYCHANNELSUBJECTLIST, rxCallBack);
        HttpApi.getInstance().getService().queryChannelSubjectList(HttpUtil.getVspUrl(HttpConstant.QUERYCHANNELSUBJECTLIST), queryChannelSubjectListRequest)
                .compose(onCompose(null == mView ? null : mView.bindToLife()))
                .subscribe(rxCallBack);
    }

    public void queryPBSRemixRecommend(List<Subject> subjectList, List<Subject> mNewSubjectList, List<ChannelDetail> channelDetailList, HashMap<String, List<ChannelDetail>> mHashMap, List<ChannelDetail> mUserChannelDetaiCanPlay, IPlayURLCallback mCallback, boolean multicastSwitch) {
        final String[] subjectId = {""};
        OkHttpClient client = new OkHttpClient();
        StringBuffer sb = new StringBuffer(HttpConstant.PBS_RemixRecommend_URL).append("?");
        sb.append("offset=").append("0");
        sb.append("&count=").append("100");
        sb.append("&appointedIds=").append("live25");
        sb.append("&vt=").append("9");
        String jsessionid = SharedPreferenceUtil.getInstance().getSeesionId();
        if (!jsessionid.contains("JSESSIONID=")) {
            jsessionid = "JSESSIONID=" + jsessionid;
        }
        SuperLog.info2SD(TAG, "stb_remixRecommend Url=" + sb.toString());
        Request request = new Request.Builder()
                .header("Cookie", jsessionid)
                .header("Set-Cookie", jsessionid + "; Path=/VSP/")
                .header("User-Agent", "OTT-Android")
                .header("authorization", SessionService.getInstance().getSession().getUserToken())
                .header("EpgSession", jsessionid)
                .header("Location", ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL())
                .url(sb.toString())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getSubjectId(subjectId[0], channelDetailList, subjectList, channelDetailList, mUserChannelDetaiCanPlay, mHashMap, mNewSubjectList, mCallback);
                SuperLog.info2SD(TAG, "stb_remixRecommend fail,queryPBSRemixRecommend");
                SuperLog.info2SD(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                SuperLog.info2SD(TAG, "stb_remixRecommend success,queryPBSRemixRecommend");
                String body = response.body().string();
                SuperLog.info2SD(TAG, "stb_remixRecommend responseData = " + body);
                PBSRemixRecommendResponse pbsRemixRecommendResponse = JsonParse.json2Object(body, PBSRemixRecommendResponse.class);

                if (null != pbsRemixRecommendResponse) {
                    if (null != pbsRemixRecommendResponse.getRecommends() && pbsRemixRecommendResponse.getRecommends().size() > 0) {
                        for (int i = 0; i < pbsRemixRecommendResponse.getRecommends().size(); i++) {
                            if (pbsRemixRecommendResponse.getRecommends().get(i).getOther() != null && pbsRemixRecommendResponse.getRecommends().get(i).getOther().size() > 0) {
                                for (int j = 0; j < pbsRemixRecommendResponse.getRecommends().get(i).getOther().size(); j++) {
                                    if (null != pbsRemixRecommendResponse.getRecommends().get(i).getOther().get(j).getStringURL() && !TextUtils.isEmpty(pbsRemixRecommendResponse.getRecommends().get(i).getOther().get(j).getStringURL())) {
//                                        for (int k = 0; k < pbsRemixRecommendResponse.getRecommends().get(i).getOther().get(j).getStringURL().size(); k++) {
                                        if (multicastSwitch && pbsRemixRecommendResponse.getRecommends().get(i).getSceneType().equals("7")) {
                                            //组播
                                            subjectId[0] = pbsRemixRecommendResponse.getRecommends().get(i).getOther().get(j).getStringURL();
                                        } else if (!multicastSwitch && pbsRemixRecommendResponse.getRecommends().get(i).getSceneType().equals("8")) {
                                            //单播
                                            subjectId[0] = pbsRemixRecommendResponse.getRecommends().get(i).getOther().get(j).getStringURL();
                                        }
//                                        }
                                    }

                                }
                            }
                        }
                    }
                }
                getSubjectId(subjectId[0], channelDetailList, subjectList, channelDetailList, mUserChannelDetaiCanPlay, mHashMap, mNewSubjectList, mCallback);
            }
        });
    }

    private void getSubjectId(String pbsSubjectId, List<ChannelDetail> channelDetailList, List<Subject> subjectList, List<ChannelDetail> detailList, List<ChannelDetail> mUserChannelDetaiCanPlay, HashMap<String, List<ChannelDetail>> mHashMap, List<Subject> mNewSubjectList, IPlayURLCallback mCallback) {
        // 读取launcher.json中返回的订户可用栏目ID，多个是用英文逗号隔开的
        String channelSubjectsID = "";
        String mchannelSubjectsID = "";

        SuperLog.info2SD(TAG, "all subjectId=" + LauncherService.getInstance().getLauncher().getExtraData().get(LiveTVActivity.KEY_CHANNEL_SUBJECTS));
        if (!TextUtils.isEmpty(pbsSubjectId) && pbsSubjectId.length() > 0) {
            mchannelSubjectsID = pbsSubjectId;
        } else {
            //按照长顺的说法pbs为空用phm的
            mchannelSubjectsID = LauncherService.getInstance().getLauncher().getExtraData().get(LiveTVActivity.KEY_CHANNEL_SUBJECTS);
        }

        //订户可用栏目ID过滤掉重复数据
        if (!TextUtils.isEmpty(mchannelSubjectsID) && mchannelSubjectsID.split(",").length > 0) {
            String[] split = mchannelSubjectsID.split(",");
            for (String value : split) {
                if (!channelSubjectsID.contains(value)) {
                    if (!TextUtils.isEmpty(channelSubjectsID)) {
                        channelSubjectsID = channelSubjectsID + "," + value;
                    } else {
                        channelSubjectsID = value;
                    }
                }
            }
        }

        SuperLog.info2SD(TAG, "订户可用栏目ID=====" + channelSubjectsID + ";原始数据==" + mchannelSubjectsID);

        // HashMap key
        List<String> mKeyString = new ArrayList<>();
        List<List<ChannelDetail>> mAllChannelDetailList = new ArrayList<>();

        if (channelDetailList == null) {
            return;
        }

        if (!TextUtils.isEmpty(channelSubjectsID)) {
            LiveDataHolder.get().setChannelSubjectId(channelSubjectsID);
            String[] channelSubjectsIdStr = channelSubjectsID.split(",");

            /*
             * 通过订户可用栏目id筛选所有栏目id
             * 只将用户可用栏目id，放进hashmap
             * */
            for (Subject subject : subjectList) {
                for (int i = 0; i < channelSubjectsIdStr.length; i++) {
                    if (subject.getID().equals(channelSubjectsIdStr[i])) {
                        mKeyString.add(subject.getID());
                        //mNewSubjectList.add(subject);
                    }
                }
            }
            //如果不为空走pbs逻辑
//            if (!TextUtils.isEmpty(pbsSubjectId) && mKeyString != null && mKeyString.size() > 0) {
//                for (int i = 0; i < mKeyString.size(); i++) {
                queryChannelStcPropsBySubject(0, mHashMap, channelDetailList, mKeyString, mUserChannelDetaiCanPlay, subjectList, channelSubjectsID, mNewSubjectList, mCallback);
//                }
//            } else {
//                for (String key : mKeyString) {
//                    List<ChannelDetail> mLocal = new ArrayList<>();
//                    for (ChannelDetail channelDetail : channelDetailList) {
//                        if (channelDetail.getSubjectIDs() != null && channelDetail.getSubjectIDs().size() > 0) {
//                            for (String subjectId : channelDetail.getSubjectIDs()) {
//                                if (key.equals(subjectId)) {
//                                    mUserChannelDetaiCanPlay.add(channelDetail);
//                                    mLocal.add(channelDetail);
//                                }
//                            }
//                        }
//                    }
//                    mAllChannelDetailList.add(mLocal);
//                }
//            }

        }

//        if (TextUtils.isEmpty(pbsSubjectId)) {
//            setCanPlayChannel(false, mKeyString, mAllChannelDetailList, mHashMap, subjectList, channelSubjectsID, mUserChannelDetaiCanPlay, channelDetailList, mNewSubjectList, mCallback);
//        }
    }


    public void queryChannelStcPropsBySubject(int position, HashMap<String, List<ChannelDetail>> mHashMap, List<ChannelDetail> channelDetailList, List<String> keyString, List<ChannelDetail> mUserChannelDetaiCanPlay, List<Subject> subjectList, String channelSubjectsID, List<Subject> mNewSubjectList, IPlayURLCallback mCallback) {
        QueryChannelStcPropsBySubjectRequest request = new QueryChannelStcPropsBySubjectRequest();
        request.setSubjectID(keyString.get(position));
        request.setOffset("0");
        request.setCount("1000");
        request.setSortType("ORDERINDEX:DESC");
        List<ChannelDetail> channelDetailListForSub = new ArrayList<>();
        RxCallBack<QueryChannelStcPropsBySubjectResponse> rxCallBack = new RxCallBack<QueryChannelStcPropsBySubjectResponse>(HttpConstant.QUERYCHANNELSTCPROPSBYSUBJECT, mContext) {
            @Override
            public void onSuccess(QueryChannelStcPropsBySubjectResponse response) {
                if (null == response.getChannelDetails() || response.getChannelDetails().size() == 0) {
                    SuperLog.info2SD(TAG, " response.getChannelDetails() is null or size==0" + response.toString());
//                        mView.onQueryChannelStcPropsBySubjectFailed();
                } else {
                    List<ChannelDetail> channelDetails = response.getChannelDetails();
                    //channelDetailList所有频道列表
                    for (int i = 0; i < channelDetails.size(); i++) {
                        for (int j = 0; j < channelDetailList.size(); j++) {
                            if (channelDetails.get(i).getID().equals(channelDetailList.get(j).getID())) {
                                channelDetailListForSub.add(channelDetailList.get(j));
                                mUserChannelDetaiCanPlay.add(channelDetailList.get(j));
                            }
                        }
                    }
                    mHashMap.put(keyString.get(position), channelDetailListForSub);
                    if (position + 1 < keyString.size()) {
                        queryChannelStcPropsBySubject(position + 1, mHashMap, channelDetailList, keyString, mUserChannelDetaiCanPlay, subjectList, channelSubjectsID, mNewSubjectList, mCallback);
                    }
                    if (mHashMap.size() == keyString.size()) {
                        setCanPlayChannel(true, keyString, null, mHashMap, subjectList, channelSubjectsID, mUserChannelDetaiCanPlay, channelDetailList, mNewSubjectList, mCallback);
                    }

                }
            }

            @Override
            public void onFail(Throwable e) {
                SuperLog.info2SD(TAG, "queryChannelStcPropsBySubject onFail e=" + e.getMessage());
                List<List<ChannelDetail>> mAllChannelDetailList = new ArrayList<>();

                for (String key : keyString) {
                    List<ChannelDetail> mLocal = new ArrayList<>();
                    for (ChannelDetail channelDetail : channelDetailList) {
                        if (channelDetail.getSubjectIDs() != null && channelDetail.getSubjectIDs().size() > 0) {
                            for (String subjectId : channelDetail.getSubjectIDs()) {
                                if (key.equals(subjectId)) {
                                    mUserChannelDetaiCanPlay.add(channelDetail);
                                    mLocal.add(channelDetail);
                                }
                            }
                        }
                    }

                    mAllChannelDetailList.add(mLocal);
                }

                setCanPlayChannel(false, keyString, mAllChannelDetailList, mHashMap, subjectList, channelSubjectsID, mUserChannelDetaiCanPlay, channelDetailList, mNewSubjectList, mCallback);
                // mView.onQueryChannelStcPropsBySubjectFailed();
            }
        };

        RxApiManager.get().add(HttpConstant.QUERYCHANNELSUBJECTLIST, rxCallBack);
        HttpApi.getInstance().getService().queryChannelStcPropsBySubject(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERYCHANNELSTCPROPSBYSUBJECT, request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxCallBack);
    }

    private void setCanPlayChannel(boolean isPbs, List<String> mKeyString, List<List<ChannelDetail>> mAllChannelDetailList, HashMap<String, List<ChannelDetail>> mHashMap, List<Subject> subjectList, String channelSubjectsID, List<ChannelDetail> mUserChannelDetaiCanPlay, List<ChannelDetail> channelDetailList, List<Subject> mNewSubjectList, IPlayURLCallback mCallback) {
        for (int i = 0; i < mKeyString.size(); i++) {
            if (isPbs) {
                for (Subject subject : subjectList) {
                    if (mKeyString.get(i).equals(subject.getID())) {
                        mNewSubjectList.add(subject);
                    }
                }
            } else {
                if (mAllChannelDetailList.get(i) != null && mAllChannelDetailList.get(i).size() > 0) {
                    mHashMap.put(mKeyString.get(i), mAllChannelDetailList.get(i));
                    for (Subject subject : subjectList) {
                        if (mKeyString.get(i).equals(subject.getID())) {
                            mNewSubjectList.add(subject);
                        }
                    }
                }
            }
        }

        // 缓存到本地用户可播放的栏目
        if (!TextUtils.isEmpty(channelSubjectsID)) {
            LiveDataHolder.get().setChannelPlay(mUserChannelDetaiCanPlay);
        } else {
            LiveDataHolder.get().setChannelPlay(channelDetailList);
            Subject subject = new Subject();
            subject.setID("message");
            subject.setName("message");
            mNewSubjectList.add(subject);
            mHashMap.put(subject.getID(), channelDetailList);
        }

        // 缓存欄目到本地
        LiveDataHolder.get().setSaveSubjectList(mNewSubjectList);

        // 缓存：栏目-频道 对应的Hash Map到本地
        LiveDataHolder.get().setSaveHashMap(mHashMap);

        // 缓存时间戳到本地，一个半小时刷新数据
        SuperLog.debug(TAG, "存储时间戳：" + new Date().getTime() / 1000);
        LiveDataHolder.get().setLiveDataTime(String.valueOf(new Date().getTime() / 1000));
        SuperLog.debug(TAG, "queryChannelSubjectList====获取栏目下频道列表===end");
        if (null != mCallback) {
            mCallback.onChannelColumn();
        }
    }
}