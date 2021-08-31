package com.pukka.ydepg.moudule.vod.utils;

import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.HeartBeatUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.launcher.util.RxCallBack;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class PrePlayUtil {
    //用于控制预播放的工具类
    private static final String TAG = "PrePlayUtil";


    public static void getPreUrl(VODDetail vodDetail,PrePlayCallback callback){
        String vodType = vodDetail.getVODType();
        if (vodType.equals("0")) {
            List<VODMediaFile> vodMediaFiles = vodDetail.getMediaFiles();
            if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
                playVod(vodMediaFiles, vodDetail.getID(),callback);
            } else {
                if (!HeartBeatUtil.getInstance().isSubscribedByPrice(vodDetail, null == vodDetail ? "" : vodDetail.getPrice())) {
                    if (null != callback){
                        callback.playFailWithString("没有找到资源文件！");
                    }
                } else {
                    if (null != callback){
                        callback.playFailWithString("播放失败！");
                    }
                }
                return;
            }
        } else {
            List<Episode> episodes = vodDetail.getEpisodes();
            Bookmark bookmark = vodDetail.getBookmark();
            if (bookmark != null) {
                SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + bookmark.getItemID() + ",sitcomNO:" + bookmark.getSitcomNO() + ",breakpoint:" + bookmark.getRangeTime());
            }
            if (episodes != null && episodes.size() != 0) {
                Episode playEpisode = null;
                if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
                    for (Episode episode : episodes) {
                        if (bookmark.getSitcomNO().equals(episode.getSitcomNO())) {
                            playEpisode = episode;
                        }
                    }
                } else {
                    playEpisode = episodes.get(0);
                }
                if (null != playEpisode) {
                    playVod(playEpisode.getVOD().getMediaFiles(), playEpisode.getVOD().getID(),callback);
                }
            } else {
                if (null != callback){
                    callback.playFailWithString("没有可播放的子集！");
                }
                return;
            }
        }
    }

    //发送鉴权请求
    private static void playVod(List<VODMediaFile> vodMediaFiles, String id,PrePlayCallback callback){
        if (vodMediaFiles != null) {
            if (vodMediaFiles.size() != 0) {
                VODMediaFile vodMediaFile = vodMediaFiles.get(0);
                PlayVODRequest mPlayVodRequest = new PlayVODRequest();
                mPlayVodRequest.setVODID(id);
                mPlayVodRequest.setMediaID(vodMediaFile.getID());
                mPlayVodRequest.setURLFormat("1");
                mPlayVodRequest.setIsReturnProduct("1");
                playVOD(callback,mPlayVodRequest,vodMediaFile.getElapseTime());
            }
        }
    }

    //发送鉴权请求
    private static void playVOD(PrePlayCallback callback, PlayVODRequest playVODRequest,String elapseTime){
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.PLAYVOD;
        RxCallBack<PlayVODResponse> rxCallBack = new RxCallBack<PlayVODResponse>(url, OTTApplication.getContext()) {
            @Override
            public void onSuccess(PlayVODResponse response) {
                if (response != null && response.getResult() != null) {
                    Result result = response.getResult();
                    if (result != null
                            && !TextUtils.isEmpty(result.getRetCode())
                            && result.getRetCode().equals(Result.RETCODE_OK)) {
                        if (null != callback){
                            callback.playSuccessWithUrl(StringUtils.splicingPlayUrl(response.getPlayURL()), response.getBookmark()
                                    , response.getAuthorizeResult().getProductID(),elapseTime);
                            return;
                        }
                    }else{
                        if (null != callback){
                            callback.playFailWithUrl(StringUtils.splicingPlayUrl(response.getPlayURL()), response.getBookmark()
                                    , response.getAuthorizeResult().getProductID(),elapseTime);
                            return;
                        }
                    }
                }
                if (null != callback){
                    callback.playFail();
                }
            }

            @Override public void onFail(@NonNull Throwable e) {
                if (null != callback){
                    callback.playFail();
                }
            }
        };

        HttpApi.getInstance().getService().playVOD(url,playVODRequest)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxCallBack);
    }

    public interface PrePlayCallback{
        //播放失败，提示信息
        void playFailWithString(String string);

        //鉴权成功获取播放地址
        void playSuccessWithUrl(String url, String bookmark,String productId,String elapseTime);

        //鉴权失败获取试看地址
        void playFailWithUrl(String url, String bookmark,String productId,String elapseTime);

        //鉴权接口出错
        void playFail();
    }


}
