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

    //???????????????????????????????????????
    private boolean relatedSwitch = true;
    //????????????????????????????????????id
    private String relatedSubjectId = "";

    //???????????????????????????????????????
    private boolean guessSwitch = true;
    //????????????????????????????????????id
    private String guessSubjectId = "";

    private static final String SHOW = "show";
    private static final String SWITCH = "switch";
    public static final String SUBJECTID = "subjectId";
    public static final String RELATED = "related";
    public static final String GUESS = "guess";

    public static final String RELATEDID = "relatedReco25";
    public static final String GUESSID = "guessReco25";

    //???????????? 0???????????? 1???????????? -1????????????
    public String relatedRecType = recType_no_exist;
    public String guessRecType = recType_no_exist;

    private static final String recType_mix = "0";
    private static final String recType_hand = "1";
    private static final String recType_no_exist = "-1";

    //??????????????????
    private List<VOD> relatedVods = new ArrayList<>();
    //??????????????????
    private List<VOD> guessVods = new ArrayList<>();

    //????????????IdentifyType
    private String relatedIdentifyType = "";
    //????????????IdentifyType
    private String guessIdentifyType = "";

    //??????????????????????????????
    private boolean relatedComplete = false;
    //??????????????????????????????
    private boolean guessComplete = false;

    //????????????????????????
    private boolean relatedShow = true;
    //????????????????????????
    private boolean guessShow = true;

    private GetRecCallback callback;

    /**********???????????????????????????????????????**********/
    //?????????????????????key
    public final static String RELATED_SUBJECTID_SITCOM_KEY = "sitcom";
    //????????????????????????key
    public final static String RELATED_SUBJECTID_VARIETR_KEY = "variety";
    //?????????????????????key
    public final static String RELATED_SUBJECTID_OTHER_KEY = "other";

    //Vod???CmsType?????????
    public final static String RELATED_CMS_SITCOM = "?????????";
    //Vod???CmsType??????
    public final static String RELATED_CMS_VARIETR = "??????";

    //???????????????tarckerUrl???????????????????????????????????????
    private String guessTracker = "";

    //???????????????tarckerUrl???????????????????????????????????????
    private String relatedTracker = "";

    //????????????????????????
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
                //??????????????????
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
                //??????????????????
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
                //?????????????????????????????????
                if (null != map && null != map.get(SUBJECTID)){
                    Map subjectIdMap = (Map) map.get(SUBJECTID);
                    if (null != subjectIdMap && null != subjectIdMap.get(GUESS)){
                        //???????????????????????????id?????????????????????cmsType??????
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
                        //???????????????????????????id?????????
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
        //??????????????????
        if (relatedShow){
            if (relatedSwitch){
                //??????????????????????????????
                getMixRecommend(true);
            }else if (!TextUtils.isEmpty(relatedSubjectId)){
                //??????????????????????????????
                loadMoviesContent(true,relatedSubjectId);
            }else{
                //????????????????????????????????????????????????
                requestOver(true,new ArrayList<>());
            }
        }else{
            //??????????????????
            requestOver(true,new ArrayList<>());
        }

        //??????????????????
        if (guessShow){
            if (guessSwitch){
                //??????????????????????????????
                getMixRecommend(false);
            }else if (!TextUtils.isEmpty(guessSubjectId)){
                //??????????????????????????????
                loadMoviesContent(false,guessSubjectId);
            }else{
                //????????????????????????????????????????????????
                requestOver(false,new ArrayList<>());
            }
        }else{
            //??????????????????
            requestOver(false,new ArrayList<>());
        }

    }

    //true??????????????????false???????????????
    public void getMixRecommend(boolean isRelated){
        Log.i(TAG, "getMixRecommend: ?????????????????? "+ isRelated);
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
                        //????????????12?????????
                        if (recommend.getVODs().size() >= 12){
                            Log.i(TAG, "getMixRecommend: ?????????????????? ??????");
                            List<VOD> vods = recommend.getVODs().subList(0,12);
                            //?????????????????????
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

                //????????????????????????????????????12????????????????????????
                Log.i(TAG, "getMixRecommend: ?????????????????? ???????????????");
                if (TextUtils.isEmpty(finalSubjectId)){
                    //???????????????????????????????????????
                    requestOver(isRelated,new ArrayList<>());
                }else{
                    //??????????????????
                    loadMoviesContent(isRelated, finalSubjectId);
                }

            }

            @Override
            public void getRemixRecommendDataFail() {
                //?????????????????????????????????
                if (TextUtils.isEmpty(finalSubjectId)){
                    //???????????????????????????????????????
                    requestOver(isRelated,new ArrayList<>());
                }else{
                    //??????????????????
                    loadMoviesContent(isRelated, finalSubjectId);
                }
            }
        }, appointedId, "12");
    }

    private void loadMoviesContent(boolean isRelated,String subjectId) {
        Log.i(TAG, "loadMoviesContent: ?????????????????? "+subjectId);

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
                        //????????????
                        //??????12???
                        if (null != response.getVODs() && response.getVODs().size() > 0){
                            List<VOD>vods;
                            if (response.getVODs().size() < 12){
                                Log.i(TAG, "loadMoviesContent: ?????????????????? ??????12???");
                                //??????12????????????????????????
                                vods = new ArrayList<>();
                            }else{
                                Log.i(TAG, "loadMoviesContent: ?????????????????? ??????");
                                vods = response.getVODs().subList(0,12);
                            }
                            //????????????
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
                //???????????????????????????
                requestOver(isRelated,new ArrayList<>());
            }

            @Override public void onFail(@NonNull Throwable e) {
                //???????????????????????????
                requestOver(isRelated,new ArrayList<>());
            }
        };

        HttpApi.getInstance().getService().queryVODListBySubject(url,request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxCallBack);
    }

    //?????????????????????
    //true??????????????????false???????????????
    private void requestOver(boolean isRelated,List<VOD>vods){
        Log.i(TAG, "requestOver: ???????????? "+ isRelated);
        if (isRelated){
            relatedComplete = true;
            relatedVods = vods;
        }else{
            guessComplete = true;
            guessVods = vods;
        }
        returnList();
    }


    //???????????????????????????
    private void returnList(){
        //??????????????????????????????????????????
        if (guessComplete && relatedComplete){
            if (null != callback){
                callback.getRecSuccess(relatedVods,guessVods,relatedRecType,guessRecType,relatedIdentifyType,guessIdentifyType,this.relatedTracker,this.guessTracker);
            }
        }
    }

    //?????????????????????????????????
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
