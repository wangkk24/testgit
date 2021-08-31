package com.pukka.ydepg.moudule.vod.presenter;

import android.text.TextUtils;
import android.util.Log;

import androidx.exifinterface.media.ExifInterface;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.bean.request.QueryRecommendRequest;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODListCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Controller.VODListController;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PBSRemixRecommendResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryVODListBySubjectResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.Recommend;
import com.pukka.ydepg.common.report.ubd.extension.RecommendData;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;
import com.pukka.ydepg.common.report.ubd.scene.UBDRecommendImpression;
import com.pukka.ydepg.common.toptool.EpgTopFunctionMenu;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.launcher.session.Session;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.search.presenter.BasePresenter;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class VodDetailRecPresenter extends BasePresenter {
    private static final String TAG = "VodDetailRecPresenter";
    private String vodid = "";

    //是否开启最近推荐的智能推荐
    private boolean relatedSwitch = true;
    //最近推荐的人工推荐的栏目id
    private String relatedSubjectId = "";

    //是否开启猜你喜欢的智能推荐
    private boolean guessSwitch = true;
    //猜你喜欢的人工推荐的栏目id
    private String guessSubjectId = "";

    private static final String SHOW = "show";
    private static final String SWITCH = "switch";
    public static final String SUBJECTID = "subjectId";
    public static final String RELATED = "related";
    public static final String GUESS = "guess";

    public static final String RELATEDID = "relatedReco25";
    public static final String GUESSID = "guessReco25";

    //推荐方式 0智能推荐 1人工推荐 -1未请求到
    public String relatedRecType = recType_no_exist;
    public String guessRecType = recType_no_exist;

    private static final String recType_mix = "0";
    private static final String recType_hand = "1";
    private static final String recType_no_exist = "-1";

    //最近推荐列表
    private List<VOD> relatedVods = new ArrayList<>();
    //猜你喜欢列表
    private List<VOD> guessVods = new ArrayList<>();

    //最近推荐IdentifyType
    private String relatedIdentifyType = "";
    //猜你喜欢IdentifyType
    private String guessIdentifyType = "";

    //最近推荐是否查询完毕
    private boolean relatedComplete = false;
    //猜你喜欢是否推荐完毕
    private boolean guessComplete = false;

    //最近推荐是否展示
    private boolean relatedShow = true;
    //猜你喜欢是否展示
    private boolean guessShow = true;

    private GetRecCallback callback;

    /**********相关推荐的人工推荐栏目配置**********/
    //综艺的人工栏目key
    public final static String RELATED_SUBJECTID_SITCOM_KEY = "sitcom";
    //电视剧的人工栏目key
    public final static String RELATED_SUBJECTID_VARIETR_KEY = "variety";
    //其他的人工栏目key
    public final static String RELATED_SUBJECTID_OTHER_KEY = "other";

    //Vod的CmsType电视剧
    public final static String RELATED_CMS_SITCOM = "电视剧";
    //Vod的CmsType综艺
    public final static String RELATED_CMS_VARIETR = "综艺";

    //猜你喜欢的tarckerUrl，若存在，为九天的推荐资源
    private String guessTracker = "";

    //相关推荐的tarckerUrl，若存在，为九天的推荐资源
    private String relatedTracker = "";

    //用于查询智能推荐
    private TabItemPresenter presenter = new TabItemPresenter();

    public VodDetailRecPresenter(VOD vod) {
        relatedIdentifyType = "";
        guessIdentifyType = "";
        guessTracker = "";
        relatedTracker = "";
        String configStr = SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.DETAIL_RECOM_CONFIG);


//        configStr = "{\"show\":{\"related\":\"1\",\"guess\":\"1\"},\"switch\":{\"related\":\"1\",\"guess\":\"1\"},\"subjectId\":{\"guess\":\"catauto2000011094\",\"related\":{\"sitcom\":\"catauto2000011091\",\"variety\":\"catauto2000011092\",\"other\":\"catauto2000011093\"}}}";

        if (!TextUtils.isEmpty(configStr)){
            try {
                Map map = JsonParse.json2Object(configStr,Map.class);
                //获取展示开关
                if (null != map && null != map.get(SHOW)){
                    Map<String,String> switchMap = (Map<String, String>) map.get(SHOW);
                    if (null != switchMap && !TextUtils.isEmpty(switchMap.get(RELATED))){
                        if ("0".equals(switchMap.get(RELATED))){
                            relatedShow = false;
                        }else{
                            relatedShow = true;
                        }
                    }
                    if (null != switchMap && !TextUtils.isEmpty(switchMap.get(GUESS))){
                        if ("0".equals(switchMap.get(GUESS))){
                            guessShow = false;
                        }else{
                            guessShow = true;
                        }
                    }
                }
            }catch (Exception e){
                SuperLog.error(TAG,e);
            }

            try {
                //获取开关状态
                Map map = JsonParse.json2Object(configStr,Map.class);
                if (null != map && null != map.get(SWITCH)){
                    Map<String,String> switchMap = (Map<String, String>) map.get(SWITCH);
                    if (null != switchMap && !TextUtils.isEmpty(switchMap.get(RELATED))){
                        if ("0".equals(switchMap.get(RELATED))){
                            relatedSwitch = false;
                        }else{
                            relatedSwitch = true;
                        }
                    }
                    if (null != switchMap && !TextUtils.isEmpty(switchMap.get(GUESS))){
                        if ("0".equals(switchMap.get(GUESS))){
                            guessSwitch = false;
                        }else{
                            guessSwitch = true;
                        }
                    }
                }
            }catch (Exception e){
                SuperLog.error(TAG,e);
            }

            try {
                Map map = JsonParse.json2Object(configStr,Map.class);
                //获取展示的人工推荐栏目
                if (null != map && null != map.get(SUBJECTID)){
                    Map subjectIdMap = (Map) map.get(SUBJECTID);
                    if (null != subjectIdMap && null != subjectIdMap.get(GUESS)){
                        //猜你爱看的人工栏目id可以根据影片的cmsType配置
                        Map<String,String> guessMap = (Map<String,String>)subjectIdMap.get(GUESS);
                        if (null != guessMap && null != vod){
                            this.vodid = vod.getID();
                            SuperLog.debug(TAG, "VodDetailRecPresenter: cmsType " +vod.getCmsType());
                            if (vod.getCmsType().contains(RELATED_CMS_SITCOM)){
                                if (!TextUtils.isEmpty(guessMap.get(RELATED_SUBJECTID_SITCOM_KEY))){
                                    guessSubjectId = guessMap.get(RELATED_SUBJECTID_SITCOM_KEY);
                                }
                            }else if (vod.getCmsType().contains(RELATED_CMS_VARIETR)){
                                if (!TextUtils.isEmpty(guessMap.get(RELATED_SUBJECTID_VARIETR_KEY))){
                                    guessSubjectId = guessMap.get(RELATED_SUBJECTID_VARIETR_KEY);
                                }
                            }else{
                                if (!TextUtils.isEmpty(guessMap.get(RELATED_SUBJECTID_OTHER_KEY))){
                                    guessSubjectId = guessMap.get(RELATED_SUBJECTID_OTHER_KEY);
                                }
                            }
                        }

                    }
                    if (null != subjectIdMap && null != subjectIdMap.get(RELATED)){
                        //相关推荐的人工栏目id直接取
                        relatedSubjectId = (String) subjectIdMap.get(RELATED);
                    }
                }
            }catch (Exception e){
                SuperLog.error(TAG,e);
            }
            Log.i(TAG, "VodDetailRecPresenter: show:"+relatedShow + " " + guessShow + " switch:" +relatedSwitch + " "+ guessSwitch + " id:"+ relatedSubjectId + " "+ guessSubjectId);
        }
    }

    public void getVoddetailRecList(GetRecCallback callback){
        this.callback = callback;
        guessComplete = false;
        relatedComplete = false;
        relatedVods.clear();
        guessVods.clear();
        //请求最近推荐
        if (relatedShow){
            if (relatedSwitch){
                //开关打开请求智能推荐
                getMixRecommend(true);
            }else if (!TextUtils.isEmpty(relatedSubjectId)){
                //开关关闭请求人工推荐
                loadMoviesContent(true,relatedSubjectId);
            }else{
                //开关关闭且没有配置栏目，请求结束
                requestOver(true,new ArrayList<>());
            }
        }else{
            //不展示，结束
            requestOver(true,new ArrayList<>());
        }

        //请求猜你喜欢
        if (guessShow){
            if (guessSwitch){
                //开关打开请求智能推荐
                getMixRecommend(false);
            }else if (!TextUtils.isEmpty(guessSubjectId)){
                //开关关闭请求人工推荐
                loadMoviesContent(false,guessSubjectId);
            }else{
                //开关关闭且没有配置栏目，请求结束
                requestOver(false,new ArrayList<>());
            }
        }else{
            //不展示，结束
            requestOver(false,new ArrayList<>());
        }

    }

    //true为相关推荐，false为猜你喜欢
    public void getMixRecommend(boolean isRelated){
        Log.i(TAG, "getMixRecommend: 请求智能推荐 "+ isRelated);
        String appointedId = "";
        String subjectId = "";
        if (isRelated){
            appointedId = RELATEDID;
        }else{
            appointedId = GUESSID;
        }
        if (isRelated){
            subjectId = relatedSubjectId;
        }else{
            subjectId = guessSubjectId;
        }
        String finalSubjectId = subjectId;
        presenter.queryPBSRemixRecommend(null, vodid, "", new EpgTopFunctionMenu.OnPBSRemixRecommendListener() {
            @Override
            public void getRemixRecommendData(PBSRemixRecommendResponse pbsRemixRecommendResponse) {

                if (null != pbsRemixRecommendResponse && null != pbsRemixRecommendResponse.getRecommends() && pbsRemixRecommendResponse.getRecommends().size() > 0){
                    Recommend recommend = pbsRemixRecommendResponse.getRecommends().get(0);
                    if (null != recommend.getVODs() && recommend.getVODs().size()>0){
                        //最多展示12个内容
                        if (recommend.getVODs().size() >= 12){
                            Log.i(TAG, "getMixRecommend: 请求智能推荐 成功");
                            List<VOD> vods = recommend.getVODs().subList(0,12);
                            //请求完毕，返回
                            if (isRelated){
                                relatedRecType = recType_mix;
                                PbsUaConstant.sceneId_rec = recommend.getSceneId();
                                relatedIdentifyType = recommend.getIdentifyType() + "";
                                if (!TextUtils.isEmpty(recommend.getDisplay_tracker())){
                                    relatedTracker = recommend.getDisplay_tracker();
                                }else{
                                    relatedTracker = "";
                                }
                            }else{
                                guessRecType = recType_mix;
                                PbsUaConstant.sceneId_guess = recommend.getSceneId();
                                guessIdentifyType = recommend.getIdentifyType() + "";
                                if (!TextUtils.isEmpty(recommend.getDisplay_tracker())){
                                    guessTracker = recommend.getDisplay_tracker();
                                }else{
                                    guessTracker = "";
                                }
                            }
                            requestOver(isRelated,vods);
                            return;
                        }
                    }
                }

                //请求失败或者请求到的不足12个，获取人工推荐
                Log.i(TAG, "getMixRecommend: 请求智能推荐 转人工推荐");
                if (TextUtils.isEmpty(finalSubjectId)){
                    //没有配置人工推荐，请求结束
                    requestOver(isRelated,new ArrayList<>());
                }else{
                    //请求人工推荐
                    loadMoviesContent(isRelated, finalSubjectId);
                }

            }

            @Override
            public void getRemixRecommendDataFail() {
                //请求失败，获取人工推荐
                if (TextUtils.isEmpty(finalSubjectId)){
                    //没有配置人工推荐，请求结束
                    requestOver(isRelated,new ArrayList<>());
                }else{
                    //请求人工推荐
                    loadMoviesContent(isRelated, finalSubjectId);
                }
            }
        }, appointedId, "12");
    }

    private void loadMoviesContent(boolean isRelated,String subjectId) {
        Log.i(TAG, "loadMoviesContent: 请求人工推荐 "+subjectId);

        QueryVODListBySubjectRequest request = new QueryVODListBySubjectRequest();

        request.setSubjectID(subjectId);
        request.setCount("12");
        request.setOffset("0");
        request.setSortType("CNTARRANGE");

        String url = HttpUtil.getVspUrl(HttpConstant.QUERYVODLISTSTCPROPSBYSUBJECT) + HttpUtil.addUserVODListFilter();

        RxCallBack<QueryVODListBySubjectResponse> rxCallBack = new RxCallBack<QueryVODListBySubjectResponse>(url, OTTApplication.getContext()) {
            @Override
            public void onSuccess(QueryVODListBySubjectResponse response) {
                if (response != null && response.getResult() != null && response.getResult().getRetCode()!=null) {
                    Result result = response.getResult();
                    if(Result.RETCODE_OK.equals(result.getRetCode())){
                        //请求成功
                        //取前12条
                        if (null != response.getVODs() && response.getVODs().size() > 0){
                            List<VOD>vods;
                            if (response.getVODs().size() < 12){
                                Log.i(TAG, "loadMoviesContent: 请求人工推荐 不足12个");
                                //不足12个，按需求不展示
                                vods = new ArrayList<>();
                            }else{
                                Log.i(TAG, "loadMoviesContent: 请求人工推荐 成功");
                                vods = response.getVODs().subList(0,12);
                            }
                            //请求结束
                            if (isRelated){
                                relatedRecType = recType_hand;
                                PbsUaConstant.sceneId_rec = relatedSubjectId;
                                relatedIdentifyType = "";
                            }else{
                                guessRecType = recType_hand;
                                PbsUaConstant.sceneId_guess = guessSubjectId;
                                guessIdentifyType = "";
                            }
                            requestOver(isRelated,vods);
                            return;
                        }
                    }
                }
                //请求失败，请求结束
                requestOver(isRelated,new ArrayList<>());
            }

            @Override public void onFail(@NonNull Throwable e) {
                //请求失败，请求结束
                requestOver(isRelated,new ArrayList<>());
            }
        };

        HttpApi.getInstance().getService().queryVODListBySubject(url,request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxCallBack);
    }

    //请求结束，返回
    //true为相关推荐，false为猜你喜欢
    private void requestOver(boolean isRelated,List<VOD>vods){
        Log.i(TAG, "requestOver: 请求结束 "+ isRelated);
        if (isRelated){
            relatedComplete = true;
            relatedVods = vods;
        }else{
            guessComplete = true;
            guessVods = vods;
        }
        returnList();
    }


    //请求完毕，返回数据
    private void returnList(){
        //两个列表都请求完毕之后才返回
        if (guessComplete && relatedComplete){
            if (null != callback){
                callback.getRecSuccess(relatedVods,guessVods,relatedRecType,guessRecType,relatedIdentifyType,guessIdentifyType,this.relatedTracker,this.guessTracker);
            }
        }
    }

    //获取详情页推荐内容回调
    public interface GetRecCallback{
        void getRecSuccess(List<VOD> relatedVods,List<VOD> guessVods ,String relatedType,String guessType,String relatedIdentifyType,String guessTypeIdentifyType,String relatedTarcker ,String guessTarcker);
    }

    public GetRecCallback getCallback() {
        return callback;
    }

    public void setCallback(GetRecCallback callback) {
        this.callback = callback;
    }

    public String getRelatedSubjectId() {
        return relatedSubjectId;
    }

    public String getGuessSubjectId() {
        return guessSubjectId;
    }
}
