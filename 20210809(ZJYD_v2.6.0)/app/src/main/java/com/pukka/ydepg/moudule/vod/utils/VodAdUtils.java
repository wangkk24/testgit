package com.pukka.ydepg.moudule.vod.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.common.ad.AdConstant;
import com.pukka.ydepg.common.ad.AdManager;
import com.pukka.ydepg.common.ad.IADListener;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODListCallBack;
import com.pukka.ydepg.common.http.v6bean.v6node.Genre;
import com.pukka.ydepg.common.http.v6bean.v6node.ProduceZone;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.SubjectVODList;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertContent;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.presenter.NewDetailPresenter;

import java.util.List;

public class VodAdUtils {
    private static final String TAG = "VodAdUtils";

    //不展示广告
    public static final int AD_STATE_NOT_SHOW   = 0;
    //展示广告，但无法点击
    public static final int AD_STATE_CANT_CLICK = 1;
    //展示广告，可以点击
    public static final int AD_STATE_CAN_CLICK  = 2;

    private Context mContext;

    public interface VodAdCallBack{
        void showAd(int state,String url,AdvertContent advertContent);
    }

    private VodAdCallBack callBack;

    private DetailPresenter mDetailPresenter;

    private NewDetailPresenter mNewDetailPresenter;


    //广告的展示逻辑
    public void getVodAd(Context context, DetailPresenter mDetailPresenter, VOD vod, VodAdCallBack callBack){
        this.callBack = callBack;
        mContext = context;
        this.mDetailPresenter = mDetailPresenter;
        //查询广告平台，是否有广告
        AdManager.getInstance().queryAdvertContent(AdConstant.AdClassify.DETAIL,listener,vod);
    }

    //广告的展示逻辑
    public void getVodAd(Context context, NewDetailPresenter mDetailPresenter, VOD vod, VodAdCallBack callBack){
        if(callBack == null){
            return;
        }
        mContext = context;
        this.callBack = callBack;
        this.mNewDetailPresenter = mDetailPresenter;
        //查询SSP广告平台，是否有广告
        AdManager.getInstance().queryAdvertContent(AdConstant.AdClassify.DETAIL,listener,vod);
    }

    private final IADListener listener = new IADListener() {
        @Override
        public void onSuccess(List<AdvertContent> listAdvertContent) {
            Log.i(TAG, "广告平台返回");
            if (isNotNull(listAdvertContent)){
                String adStr = listAdvertContent.get(0).getDisplay().getBanner().getImg();
                //有广告
                if (canClick(listAdvertContent)){
                    //有SSP广告且有跳转链接
                    Log.i(TAG, "有广告且有跳转链接");
                    callBack.showAd(AD_STATE_CAN_CLICK,adStr,listAdvertContent.get(0));
                } else {
                    //有SSP广告但无跳转链接
                    Log.i(TAG, "有广告但是没有跳转链接");
                    callBack.showAd(AD_STATE_CANT_CLICK,adStr,listAdvertContent.get(0));
                }
                mDetailPresenter    = null;
                mNewDetailPresenter = null;
            }else{
                //SSP广告平台没有广告，加载运营配置广告
                loadConfiguredBannerAdvert();
            }
        }

        @Override
        public void onFail() {
            //查询SSP广告平台失败，加载运营配置广告
            loadConfiguredBannerAdvert();
        }
    };

    private void loadConfiguredBannerAdvert(){
        SuperLog.debug(TAG, "广告平台无返回");
        //没有配置，使用终端参数中配置的subjectID请求广告
        String id = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.VOD_DETAIL_AdVERT_SUBJECT_ID);
        if (!TextUtils.isEmpty(id)) {
            if (null != mDetailPresenter) {
                mDetailPresenter.getAdvertisementSubjectDetail(id, mVODListCallBack);
            }else if (null != mNewDetailPresenter){
                mNewDetailPresenter.getAdvertisementSubjectDetail(id, mVODListCallBack);
            }

        }else{
            if (null != callBack){
                Log.i(TAG, "以前的展示逻辑，不展示广告");
                callBack.showAd(AD_STATE_NOT_SHOW,"",null);
            }
        }

        //结束手动回收presenter
        mDetailPresenter = null;
        mNewDetailPresenter = null;
    }



    private VODListCallBack mVODListCallBack=new VODListCallBack() {

        @Override
        public void querySubjectDetailSuccess(int total, List<Subject> subjects) {
            if(isConfigAd(subjects)){
                String adUrl = "";
                int versionCode = ConfigUtil.getConfig(mContext).getVersionCode();
                if (versionCode > 446){
                    adUrl = subjects.get(0).getPicture().getTitles().get(0);
                }else{
                    adUrl = subjects.get(0).getPicture().getIcons().get(0);
                }

                if (null != callBack){
                    //展示运营配置广告
                    Log.i(TAG, "以前的展示逻辑，展示广告，但是无法点击");
                    callBack.showAd(AD_STATE_CANT_CLICK,adUrl,null);
                }
            }else{
                if (null != callBack){
                    //不展示广告
                    Log.i(TAG, "以前的展示逻辑，不展示广告");
                    callBack.showAd(AD_STATE_NOT_SHOW,"",null);
                }
            }

            //手动回收presenter
            mDetailPresenter = null;
            mNewDetailPresenter = null;
        }

        @Override
        public void queryVODListBySubjectSuccess(int total, List<VOD> vodList, String subjectId) {}

        @Override
        public void queryVODListBySubjectFailed()
        {

        }

        @Override
        public void queryVODSubjectListSuccess(int total, List<Subject> subjects)
        {

        }

        @Override
        public void queryVODSubjectListFailed()
        {

        }

        @Override
        public void querySubjectVODBySubjectIDSuccess(int total, List<SubjectVODList>
                subjectVODLists)
        {

        }

        @Override
        public void querySubjectVODBySubjectIDFailed()
        {

        }

        @Override
        public void getContentConfigSuccess(List<ProduceZone> produceZoneList, List<Genre> genreList)
        {

        }

        @Override
        public void getContentConfigFailed()
        {

        }

        @Override
        public void queryPSBRecommendSuccess(int total, List<VOD> vodDetails) {

        }

        @Override
        public void queryPSBRecommendFail() {

        }

        @Override
        public void onError()
        {

        }
    };



    //广告是否有bannerUrl
    private boolean isNotNull(List<AdvertContent> listAdvertContent){
        return null != listAdvertContent && listAdvertContent.size() > 0
                && null != listAdvertContent.get(0)
                && null != listAdvertContent.get(0).getDisplay()
                && null != listAdvertContent.get(0).getDisplay().getBanner()
                && null != listAdvertContent.get(0).getDisplay().getBanner().getImg();
    }

    //广告是否有跳转链接
    private boolean canClick(List<AdvertContent> listAdvertContent){
        return null != listAdvertContent && listAdvertContent.size() > 0
                && null != listAdvertContent.get(0)
                && null != listAdvertContent.get(0).getDisplay()
                && null != listAdvertContent.get(0).getDisplay().getBanner()
                && null != listAdvertContent.get(0).getDisplay().getBanner().getLink()
                && null != listAdvertContent.get(0).getDisplay().getBanner().getLink().getUrl();
    }

    //picture中是否配置有广告
    private boolean isConfigAd(List<Subject> subjects){
        return (null!=subjects
                && !subjects.isEmpty()
                && null != subjects.get(0)
                && null != subjects.get(0).getPicture()
                && null != subjects.get(0).getPicture().getIcons()
                && subjects.get(0).getPicture().getIcons().size() > 0
                && null != subjects.get(0).getPicture().getIcons().get(0))
                ||(null!=subjects
                && !subjects.isEmpty()
                && null != subjects.get(0)
                && null != subjects.get(0).getPicture()
                && null != subjects.get(0).getPicture().getTitles()
                && subjects.get(0).getPicture().getTitles().size() > 0
                && null != subjects.get(0).getPicture().getTitles().get(0));
    }




}
