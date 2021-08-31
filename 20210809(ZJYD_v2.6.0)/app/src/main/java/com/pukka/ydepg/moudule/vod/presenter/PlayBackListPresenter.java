package com.pukka.ydepg.moudule.vod.presenter;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODFilter;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PBSRemixRecommendResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryVODListBySubjectResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.Recommend;
import com.pukka.ydepg.common.report.ubd.scene.UBDRecommendImpression;
import com.pukka.ydepg.common.toptool.EpgTopFunctionMenu;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.livetv.presenter.base.BasePresenter;
import com.pukka.ydepg.moudule.vod.utils.RemixRecommendUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class PlayBackListPresenter extends BasePresenter {
    private static final String TAG = "PlayBackListPresenter";

    public PlayBackListPresenter() {
        getCpIdString();
    }

    //用于查询智能推荐
    TabItemPresenter presenter = new TabItemPresenter();

    private List<VOD> list;

    private PlayBackCallback callback;

    //九天曝光上报
    private String tracker = "";

    public void getRecommendList(VOD vod, PlayBackCallback callback){
        if(callback == null){
            return;
        }
        this.callback = callback;
        boolean needRemixRex = RemixRecommendUtil.getRemixRecommendSwitch();
        tracker = "";

        if (needRemixRex){
            //获取智能推荐展示
            getMixRecommend(vod.getID(), new EpgTopFunctionMenu.OnPBSRemixRecommendListener() {
                @Override
                public void getRemixRecommendData(PBSRemixRecommendResponse pbsRemixRecommendResponse) {
                    if (null != pbsRemixRecommendResponse && null != pbsRemixRecommendResponse.getRecommends() && pbsRemixRecommendResponse.getRecommends().size()>0){
                        Recommend recommend = pbsRemixRecommendResponse.getRecommends().get(0);
                        if (null != recommend.getVODs() && recommend.getVODs().size()>0){
                            list = recommend.getVODs();
                            if (!TextUtils.isEmpty(recommend.getDisplay_tracker())){
                                tracker = recommend.getDisplay_tracker();
                            }else{
                                tracker = "";
                            }
                            UBDRecommendImpression.sceneId_play       = recommend.getSceneId();
                            UBDRecommendImpression.recommendType_play = String.valueOf(recommend.getIdentifyType());
                            callback.getListSuccess(list,tracker);
                            return;
                        }
                    }

                    //没有智能推荐，获取人工推荐内容
                    getArtificialRecommend(vod);
                }

                @Override
                public void getRemixRecommendDataFail() {
                    //没有智能推荐，获取人工推荐内容
                    getArtificialRecommend(vod);
                }
            });
        }else{
            //获取人工推荐展示
            getArtificialRecommend(vod);
        }
    }

    //获取智能推荐数据
    private void getMixRecommend(String vodid, EpgTopFunctionMenu.OnPBSRemixRecommendListener onPBSRemixRecommendListener){
        presenter.queryPBSRemixRecommend(null, vodid,"",onPBSRemixRecommendListener,RemixRecommendUtil.APPPINEDID_PLAY,"12");
    }

    //获取人工推荐（最近热播和猜你爱看智能推荐关闭之后展示人工推荐）
    private void getArtificialRecommend(VOD vod){
        QueryVODListBySubjectRequest request = new QueryVODListBySubjectRequest();

        Map<String,String> map = RemixRecommendUtil.getRemixRecommendSubjectid();
        //终端参数改版
        String subjectID = "";
//        String configStr = SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.DETAIL_RECOM_CONFIG);


//        configStr = "{\"show\":{\"related\":\"1\",\"guess\":\"1\"},\"switch\":{\"related\":\"1\",\"guess\":\"1\"},\"subjectId\":{\"guess\":\"catauto2000011094\",\"related\":{\"sitcom\":\"catauto2000011091\",\"variety\":\"catauto2000011092\",\"other\":\"catauto2000011093\"}}}";

        if (null != map){
            SuperLog.debug(TAG, "VodDetailRecPresenter: cmsType " +vod.getCmsType());
            if (null != vod && vod.getCmsType().contains(VodDetailRecPresenter.RELATED_CMS_SITCOM)){
                if (!TextUtils.isEmpty(map.get(VodDetailRecPresenter.RELATED_SUBJECTID_SITCOM_KEY))){
                    subjectID = map.get(VodDetailRecPresenter.RELATED_SUBJECTID_SITCOM_KEY);
                }
            }else if (null != vod && vod.getCmsType().contains(VodDetailRecPresenter.RELATED_CMS_VARIETR)){
                if (!TextUtils.isEmpty(map.get(VodDetailRecPresenter.RELATED_SUBJECTID_VARIETR_KEY))){
                    subjectID = map.get(VodDetailRecPresenter.RELATED_SUBJECTID_VARIETR_KEY);
                }
            }else{
                if (!TextUtils.isEmpty(map.get(VodDetailRecPresenter.RELATED_SUBJECTID_OTHER_KEY))){
                    subjectID = map.get(VodDetailRecPresenter.RELATED_SUBJECTID_OTHER_KEY);
                }
            }
        }
        Log.i(TAG, "getArtificialRecommend: cmsSubjectID "+subjectID);


        if (TextUtils.isEmpty(subjectID)) {
            //没有配置，直接返回失败
            callback.getListFail();
            return;
        }

        request.setSubjectID(subjectID);
        UBDRecommendImpression.sceneId_play = subjectID;
        UBDRecommendImpression.recommendType_play = "-1";

        request.setCount("12");
        request.setOffset("0");

        if(!TextUtils.isEmpty(strCpIdList)){
            if(request.getVODFilter()!=null){
                request.getVODFilter().setCpId(strCpIdList);
            } else {
                VODFilter filter = new VODFilter();
                filter.setCpId(strCpIdList);
                request.setVODFilter(filter);
            }
        }

        String url = HttpUtil.getVspUrl(HttpConstant.QUERYVODLISTSTCPROPSBYSUBJECT) + HttpUtil.addUserVODListFilter();
        RxCallBack<QueryVODListBySubjectResponse> rxCallBack = new RxCallBack<QueryVODListBySubjectResponse>(url, OTTApplication.getContext()) {
            @Override
            public void onSuccess(QueryVODListBySubjectResponse response) {
                if (response != null && response.getResult() != null && response.getResult().getRetCode()!=null) {
                    Result result = response.getResult();
                    if(Result.RETCODE_OK.equals(result.getRetCode())){
                        //保存最近热播列表数据
                        callback.getListSuccess(response.getVODs(),tracker);
                        return;
                    }
                }
                callback.getListFail();
            }

            @Override public void onFail(@NonNull Throwable e) {
                callback.getListFail();
            }
        };

        HttpApi.getInstance().getService().queryVODListBySubject(url,request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxCallBack);
    }

    public interface PlayBackCallback{
        void getListSuccess(List<VOD> list,String tracker);
        void getListFail();
    }

    /**
     * 可展示内容的CPID,多个CPID以逗号分隔
     */
    private String strCpIdList;

    private void getCpIdString(){
        List<String> cpIdList = SessionService.getInstance().getSession().getTerminalConfigurationCpIDList();
        if( cpIdList != null ){
            for(String cpid : cpIdList){
                if(TextUtils.isEmpty(strCpIdList)){
                    strCpIdList = cpid ;
                }else{
                    strCpIdList = strCpIdList + "," + cpid;
                }
            }
        }
    }
}