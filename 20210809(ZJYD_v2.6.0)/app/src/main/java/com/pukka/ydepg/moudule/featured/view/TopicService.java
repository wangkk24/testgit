package com.pukka.ydepg.moudule.featured.view;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.OTTFormat;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.node.Topic;
import com.pukka.ydepg.launcher.bean.request.TopicRequest;
import com.pukka.ydepg.launcher.bean.response.TopicResponse;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.RxCallBack;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.moudule.featured.view.TopicService.java
 * @date: 2018-03-11 10:04
 * @version: V1.0 描述当前版本功能
 */

public class TopicService {
    /**
     * 检查更新定时器
     */
    private RxCallBack<JsonObject> mCheckObserver;
    private static final int DEFAULT_PERIOD = 30; //默认更新时间30分钟
    private int mNextIntervalPeriod = 0;   //更新时间
    private static volatile TopicService sInstance;
    private static final String TAG = TopicService.class.getSimpleName();
    private Map<String, Topic> mTopicMap = new HashMap<>();

    public static TopicService getInstance() {
        if (sInstance == null) {
            synchronized (TopicService.class) {
                if (sInstance == null)
                    sInstance = new TopicService();
            }
        }
        return sInstance;
    }

    private TopicService() {}

    public synchronized void checkAndUpdate(Context context) {
        SuperLog.debug(TAG,"checkAndUpdate Topic, interval(next check time interval) = " + mNextIntervalPeriod +"m");
        setObserver(false, context);
        Observable.timer(mNextIntervalPeriod, TimeUnit.MINUTES).flatMap(new Function<Long, ObservableSource<JsonObject>>() {
            @Override
            public ObservableSource<JsonObject> apply(@NonNull Long aLong) throws Exception {
                return queryTopic();
            }
        }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(mCheckObserver);
    }

    public synchronized void checkAndUpdateAfterSwitch(Context  context) {
        SuperLog.debug(TAG,"Enter checkAndUpdateAfterSwitch");
        setObserver(true, context);
        queryTopic().subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(mCheckObserver);
    }

    private void setObserver(boolean isSwitch, Context context){
        mCheckObserver = new RxCallBack<JsonObject>(context) {
            @Override
            public void onSuccess(@NonNull JsonObject jsonObject) {
                String content = jsonObject.toString();
                //SuperLog.debug(TAG,"queryTopic onSuccess and content= "+ content);
                TopicResponse topicResponse = JsonParse.json2Object(content, TopicResponse.class);
                if (topicResponse == null) {
                    mNextIntervalPeriod = DEFAULT_PERIOD;
                    if( !isSwitch ){
                        checkAndUpdate(context);
                    }
                    return;
                }
                if (TextUtils.equals(topicResponse.getResult().getRetCode(), Result.RETCODE_OK)) {
                    SharedPreferenceUtil.getInstance().saveTopicData(content);
                    parseTopicData(topicResponse.getTopics());
                    mNextIntervalPeriod = OTTFormat.convertInt(topicResponse.getNextIntervalPeriod());
                } else {
                    SuperLog.error(TAG, "QueryTopic return incorrect result!" );
                    mNextIntervalPeriod = DEFAULT_PERIOD;
                }
                if( !isSwitch ){
                    checkAndUpdate(context);
                }
            }

            @Override
            public void onFail(@NonNull Throwable e) {
                SuperLog.error(TAG, e);
                parseTopicData(null);
                mNextIntervalPeriod = DEFAULT_PERIOD;
                if( !isSwitch ){
                    checkAndUpdate(context);
                }
            }
        };
    }

    public Map<String, Topic> getTopicMap() {
        return mTopicMap;
    }

    public boolean isEmpty() {
        return mTopicMap.size() == 0;
    }

    /**
     * 解析topic数据，如果topicList 为null则加载缓存中的数据
     *
     * @param topicList
     */
    private void parseTopicData(List<Topic> topicList) {
        if (topicList == null) {
            String content = SharedPreferenceUtil.getInstance().getTopicData();
            if (TextUtils.isEmpty(content)) {
                SuperLog.debug(TAG, "content为null");
                return;
            }
            SuperLog.debug(TAG, content);
            TopicResponse response = JsonParse.json2Object(content, TopicResponse.class);
            parseDataToMap(response.getTopics());
        } else {
            parseDataToMap(topicList);
        }
    }

    private void parseDataToMap(List<Topic> topicList) {
        SuperLog.debug(TAG, "parseDataToMap");
        if (topicList == null || topicList.size() == 0) {
            SuperLog.error(TAG, "topicList为空");
            return;
        }
        HashMap<String, Topic> topicMap = new HashMap<>();
        for (Topic topic : topicList) {
            topicMap.put(topic.getRelationSubjectId().trim(), topic);
        }
        mTopicMap = topicMap;
    }

    public Observable<JsonObject> queryTopic() {
        TopicRequest request = new TopicRequest();
        request.setDesktopType("2"); //类型为平板
        String url = "";
        String queryPhmLauncher = LauncherService.getInstance().getQueryPhmLauncher();
        if (!TextUtils.isEmpty(LauncherService.getInstance().getPhsUrl()) && !TextUtils.isEmpty(queryPhmLauncher) && queryPhmLauncher.equalsIgnoreCase("1")){
            url = LauncherService.getInstance().getPhsUrl()
                    + "/video/phs/"
                    + HttpConstant.QUERY_GET_TOPICS;
        }else if (TextUtils.isEmpty(queryPhmLauncher) || queryPhmLauncher.equalsIgnoreCase("0")){
            url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                    + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3
                    + HttpConstant.QUERY_GET_TOPICS;
        }
        SuperLog.debug(TAG,"queryTopic Url = "+ url);
        //Modified by liuxia 不用传版本号，获取专题全量配置(传版本号则获取当前版本之后的增量变化)
        //request.setVersion(SharedPreferenceUtil.getInstance().getTopicVersion());
        return HttpApi.getInstance().getService().queryTopic(url, request);
    }

    /**
     * 关闭launcher更新定时器
     */
    public synchronized void stopCheckUpdate() {
        if(mCheckObserver!=null) {
            mCheckObserver.dispose();
        }
    }
}