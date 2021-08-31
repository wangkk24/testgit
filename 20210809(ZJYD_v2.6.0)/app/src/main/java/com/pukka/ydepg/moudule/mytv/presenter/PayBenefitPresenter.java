package com.pukka.ydepg.moudule.mytv.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.common.http.v6bean.V6Constant;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.IPlayURLCallback;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.PlayURLCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Controller.PlayURLController;
import com.pukka.ydepg.common.http.v6bean.v6Controller.PlayUrlControl;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.moudule.search.presenter.BasePresenter;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

/**
 * Author: F L
 * Date: 2018/10/16
 */
public class PayBenefitPresenter extends BasePresenter {
    /*TAG 类名用于定位*/
    private static final String TAG = PayBenefitPresenter.class.getName();
    /*RxAppCompatActivity Context*/
    private RxAppCompatActivity rxAppCompatActivity;
    /*点播 鉴权控制Controller*/
    private PlayURLController mPlayVODControl;
    /*直播 鉴权控制Controller*/
    private PlayUrlControl mPlayUrlControl;
    /*是否已经通过鉴权*/
    private boolean isOrderAccessAuth = false;

    /**
     * PayBenefitPresenter 构造方法
     *
     * @param rxAppCompatActivity RxAppCompatActivity
     */
    public PayBenefitPresenter(RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = rxAppCompatActivity;
        mPlayVODControl = new PlayURLController(rxAppCompatActivity, playURLCallBack);
        mPlayUrlControl = new PlayUrlControl(iPlayURLCallback);
    }

    /**
     * 直播鉴权
     *
     * @param liveChannelSubscribe 直播鉴权信息
     * @return 是否通过鉴权
     */
    public boolean passAuthPlayChannel(String[] liveChannelSubscribe) {
        PlayChannelRequest playChannelRequest = new PlayChannelRequest();
        playChannelRequest.setChannelID(liveChannelSubscribe[0]);
        playChannelRequest.setMediaID(liveChannelSubscribe[1]);
        playChannelRequest.setBusinessType(V6Constant.ChannelURLType.BTV);
        playChannelRequest.setURLFormat("1");//HLS
        mPlayUrlControl.playChannel(playChannelRequest, compose(bindToLifecycle(rxAppCompatActivity)), rxAppCompatActivity);
        return isOrderAccessAuth;
    }

    /**
     * 点播鉴权
     *
     * @param mVODDetail VOD详情
     * @return 是否通过鉴权
     */
    public boolean passAuthPlayVOD(VODDetail mVODDetail) {
        /*点播 请求参数*/
        PlayVODRequest mPlayVodRequest = new PlayVODRequest();
        String mediaID = "";
        String VODId = "";

        String type = mVODDetail.getVODType();
        if (type.equals("0")) {//电影类型
            List<VODMediaFile> vodMediaFilesFilm = mVODDetail.getMediaFiles();
            if (vodMediaFilesFilm != null && vodMediaFilesFilm.size() != 0) {
                //鉴权
                mediaID = vodMediaFilesFilm.get(0).getID();
                VODId = mVODDetail.getID();
            }
        }else{//电视剧类型
            List<Episode> episodes = mVODDetail.getEpisodes();
            if (null != episodes && episodes.size()>0){
                Episode episode = getFirstEpisodeWithPrice(episodes);
                if (null != episode){
                    List<VODMediaFile> vodMediaFiles = episode.getVOD().getMediaFiles();
                    if (null != vodMediaFiles && vodMediaFiles.size()>0){
                        mediaID = vodMediaFiles.get(0).getID();
                        VODId = episode.getVOD().getID();
                    }
                }
            }
        }
        mPlayVodRequest.setVODID(VODId);
        mPlayVodRequest.setMediaID(mediaID);
        mPlayVodRequest.setURLFormat("1");
        mPlayVodRequest.setIsReturnProduct("1");
        mPlayVODControl.getVODPlayUrlEPG(mPlayVodRequest, compose(bindToLifecycle(rxAppCompatActivity)), null);
        return isOrderAccessAuth;
    }

    /*直播鉴权回调*/
    private IPlayURLCallback iPlayURLCallback = new IPlayURLCallback() {
        @Override
        public void getChannelPlayUrlSuccess(String channelId, String url, String bookmark,String attchUrl) {
            isOrderAccessAuth = true;
        }

        @Override
        public void getVODPlayUrlSuccess(String url, String bookmark, String productId) {
            isOrderAccessAuth = true;
        }

        @Override
        public void getPlayUrlFailed(String channelId, boolean isVOD, AuthorizeResult authorizeResult, String contentId, String url, String attchUrl) {
            isOrderAccessAuth = false;
        }

        @Override
        public void playUrlError() {
            isOrderAccessAuth = false;
        }

        @Override
        public void onChannelColumn() {

        }

        @Override
        public void onChannelIndex(int index) {

        }

        @Override
        public void getChannelPlayKey(String playKey) {

        }

        @Override
        public void getProductID(String productID) {

        }
    };

    /*点播鉴权回调*/
    private PlayURLCallBack playURLCallBack = new PlayURLCallBack() {

        @Override
        public void getVODPlayUrlSuccess(String playUlr, String mbookmark, String productId, String elapseTime) {
            Log.d(TAG, "playUlr" + playUlr);
            Log.d(TAG, "mbookmark" + mbookmark);
            Log.d(TAG, "productId" + productId);
            Log.d(TAG, "elapseTime" + elapseTime);
            isOrderAccessAuth = true;
        }

        @Override
        public void getChannelPlayUrlSuccess(String url, String attachedPlayURL, String bookmark) {
            Log.d(TAG, "url" + url);
            Log.d(TAG, "attachedPlayURL" + attachedPlayURL);
            Log.d(TAG, "bookmark" + bookmark);
            isOrderAccessAuth = true;
        }

        @Override
        public void playFail() {
            Log.d(TAG, "Error");
            isOrderAccessAuth = false;
        }

        @Override
        public void playFail(PlayVODResponse response) {
            Log.d(TAG, "playFail Error");
            isOrderAccessAuth = false;
        }

        @Override
        public void onPlaycancel() {

        }

        @Override
        public void getVODDownloadUrlSuccess(String vodID, String url, String postURL, String
                switchNum, String name) {
        }

        @Override
        public void getVODDownloadUrlFailed(String vodID, String episodeID) {

        }
    };

    //电视剧类型，取第一个价格不为零的子集，若不错在价格不为零的子集，返回Null
    private Episode getFirstEpisodeWithPrice(List<Episode> episodes){
        if (episodes != null && episodes.size() != 0) {
            for (int i = episodes.size() - 1; i >= 0 ; i --) {
                VOD vod = episodes.get(i).getVOD();
                String price = vod.getPrice();
                if (!TextUtils.isEmpty(price) && Double.parseDouble(price) != 0){
                    return episodes.get(i);
                }
            }
            return null;
        }
        return null;
    }
}
