package com.pukka.ydepg.moudule.vod.presenter;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryEpisodeBriedInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryEpisodeListReuqest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryEpisodeBriefInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryEpisodeListResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryVODResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.livetv.presenter.base.BasePresenter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

//用于请求详情页子集信息的presenter
public class VoddetailEpsiodePresenter extends BasePresenter {

    //请求简要的VOD详情（不包含子集信息）
    public void queryVOD(String vodid,GetSimpleVodCallback callback){
        QueryVODRequest request = new QueryVODRequest();
        request.setVODID(vodid);
        request.setIDType(0);
        request.setIsFilter(0);
        request.setIsReturnAllMediaSubscription(1);

        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERY_VOD;

        RxCallBack<QueryVODResponse> rxCallBack = new RxCallBack<QueryVODResponse>(url, OTTApplication.getContext()) {
            @Override
            public void onSuccess(QueryVODResponse response) {
                if (response != null && response.getResult() != null && response.getResult().getRetCode()!=null) {
                    Result result = response.getResult();
                    if(Result.RETCODE_OK.equals(result.getRetCode()) && null != response.getVODDetail()){
                        if (null != callback){
                            callback.getSimpleVodSuccess(response.getVODDetail());
                            return;
                        }
                    }
                }
                if (null != callback){
                    callback.getSimpleVodFail();
                }
            }

            @Override public void onFail(@NonNull Throwable e) {
                if (null != callback){
                    callback.getSimpleVodFail();
                }
            }
        };

        HttpApi.getInstance().getService().queryVOD(url,request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxCallBack);

    }

    //查询连续剧简要子集信息
    public void queryEpisodeBriefInfo(String vodid, int count, int offset, GetEpisodeBriefInfoCallback callback){
        QueryEpisodeBriedInfoRequest request = new QueryEpisodeBriedInfoRequest();
        request.setVODID(vodid);
        request.setIDType(0);
        request.setCount(count);
        request.setOffset(offset);

        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUREY_EPISODE_BRIEF_INFO;

        RxCallBack<QueryEpisodeBriefInfoResponse> rxCallBack = new RxCallBack<QueryEpisodeBriefInfoResponse>(url, OTTApplication.getContext()) {
            @Override
            public void onSuccess(QueryEpisodeBriefInfoResponse response) {
                if (response != null && response.getResult() != null && response.getResult().getRetCode()!=null) {
                    Result result = response.getResult();
                    if(Result.RETCODE_OK.equals(result.getRetCode()) && null != response.getSitcomNOs()){
                        if (null != callback){
                            callback.getEpisodeBriefInfoSuccess(response.getSitcomNOs());
                            return;
                        }
                    }
                }
                if (null != callback){
                    callback.getEpisodeBriefInfoFail();
                }
            }

            @Override public void onFail(@NonNull Throwable e) {
                if (null != callback){
                    callback.getEpisodeBriefInfoFail();
                }
            }
        };

        HttpApi.getInstance().getService().queryEpisodeBriefInfo(url,request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxCallBack);
    }

    //查询VOD子集列表
    public void queryEpisodeList(String vodid, int isSubscriberedOnSeries, int count, int offset, String sortType, GetEpisodeListCallback callback){
        QueryEpisodeListReuqest reuqest = new QueryEpisodeListReuqest();
        reuqest.setVODID(vodid);
        reuqest.setIsSubscriberedOnSeries(isSubscriberedOnSeries);
        reuqest.setIDType(0);
        reuqest.setCount(count);
        reuqest.setOffset(offset);
        reuqest.setIsIncludeClipVOD(0);
        reuqest.setIsFilter(0);
        reuqest.setSortType(sortType);

        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERY_EPISODE_LIST;

        RxCallBack<QueryEpisodeListResponse> rxCallBack = new RxCallBack<QueryEpisodeListResponse>(url, OTTApplication.getContext()) {
            @Override
            public void onSuccess(QueryEpisodeListResponse response) {
                if (response != null && response.getResult() != null && response.getResult().getRetCode()!=null) {
                    Result result = response.getResult();
                    if(Result.RETCODE_OK.equals(result.getRetCode()) && null != response.getEpisodes()){
                        if (null != callback){
                            callback.getEpisodeListSuccess(response.getTotal(), response.getEpisodes());
                            return;
                        }
                    }
                }
                if (null != callback){
                    callback.getEpisodeListFail();
                }
            }

            @Override public void onFail(@NonNull Throwable e) {
                if (null != callback){
                    callback.getEpisodeListFail();
                }
            }
        };

        HttpApi.getInstance().getService().queryEpisodeList(url,reuqest)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxCallBack);

    }

    public interface GetSimpleVodCallback {
        void getSimpleVodSuccess(VODDetail vodDetail);
        void getSimpleVodFail();
    }

    public interface GetEpisodeBriefInfoCallback {
        void getEpisodeBriefInfoSuccess(List<String> episodesCount);
        void getEpisodeBriefInfoFail();
    }

    public interface GetEpisodeListCallback {
        void getEpisodeListSuccess(int total, List<Episode> episodes);
        void getEpisodeListFail();
    }




}
