package com.pukka.ydepg.common.screensaver.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.ad.AdConstant;
import com.pukka.ydepg.common.ad.AdManager;
import com.pukka.ydepg.common.ad.AdUtil;
import com.pukka.ydepg.common.ad.IADListener;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertContent;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryVODListBySubjectResponse;
import com.pukka.ydepg.common.screensaver.ScreenConstant;
import com.pukka.ydepg.common.screensaver.ScreenContract;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.util.RxCallBack;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class ScreenModel implements ScreenContract.IModel {

    private ScreenContract.IPresenter screenPresenter;

    //包含SSP的本地广告数据集
    private List<ScreenAdvertContent> listAdvertContent;

    //纯运营广告的本地广告数据集
    private List<ScreenAdvertContent> listAdvertContentNoSsp;

    public ScreenModel(ScreenContract.IPresenter screenPresenter) {
        this.screenPresenter        = screenPresenter;
        this.listAdvertContent      = new ArrayList<>();
        this.listAdvertContentNoSsp = new ArrayList<>();
    }

    //对Presenter暴露的能力,获取本地屏保广告数据
    @Override
    public List<ScreenAdvertContent> getAdvertContent() {
        if(screenPresenter.isPlaySsp() && !CollectionUtil.isEmpty(listAdvertContent)){
            return listAdvertContent;
        } else {
            return listAdvertContentNoSsp;
        }
    }

    //对Presenter暴露的能力,查询服务端屏保广告数据
    @Override
    public void queryScreenAdvert() {

        String advertSubjectID = CommonUtil.getConfigValue(ScreenConstant.TERMINAL_PARAM_ADVERT_SUBJECT);
        if(BuildConfig.DEBUG){
            advertSubjectID = "catauto2000174088";
        }

        if( TextUtils.isEmpty(advertSubjectID) ){
            SuperLog.info2SD(ScreenConstant.TAG,"Subject of Screensaver is not configured. No [VSP] advert data");
            querySspScreenAdvert(null);
        } else {
            SuperLog.info2SD(ScreenConstant.TAG,"Subject of Screensaver is "+ advertSubjectID + ". Begin to get [VSP] advert");
            queryAdvertVod(advertSubjectID);
        }

    }

    //查询广告栏目下的广告VOD对象,调用接口QueryVODListStcPropsBySubject
    private void queryAdvertVod(String advertSubjectID) {
        QueryVODListBySubjectRequest request = new QueryVODListBySubjectRequest();
        request.setSubjectID(advertSubjectID);
        request.setCount("50");
        request.setOffset("0");
        //当subjectID为叶子栏目且sortType未携带，sortType默认取CNTARRANGE:ASC=>按栏目下内容编排顺序排序，仅对subjectID表示的栏目是叶子栏目时才有效
        //request.setSortType("CNTARRANGE:ASC");

        RxCallBack<QueryVODListBySubjectResponse> rxCallBack = new ScreenVodListCallback(HttpConstant.QUERYVODLISTSTCPROPSBYSUBJECT, OTTApplication.getContext());

        String url = HttpUtil.getVspUrl(HttpConstant.QUERYVODLISTSTCPROPSBYSUBJECT) + HttpUtil.addUserVODListFilter();
        HttpApi.getInstance().getService().queryVODListBySubject(url, request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(rxCallBack);
    }

    private class ScreenVodListCallback extends RxCallBack<QueryVODListBySubjectResponse> {

        ScreenVodListCallback(String interfaceName, Context context) {
            super(interfaceName, context);
        }

        @Override
        public void onSuccess(QueryVODListBySubjectResponse response) {
            SuperLog.info2SD(ScreenConstant.TAG, "Get VSP screen advert successfully,number="+response.getTotal());
            if(CollectionUtil.isEmpty(response.getVODs())){
                querySspScreenAdvert(null);
            } else {
                querySspScreenAdvert(response.getVODs());
            }
        }

        @Override
        public void onFail(@NonNull Throwable e) {
            SuperLog.error(ScreenConstant.TAG, e);
            SuperLog.error(ScreenConstant.TAG, "Get VSP screen advert vod list exception.");
            querySspScreenAdvert(null);
        }
    };

    //查询SSP平台的广告
    private void querySspScreenAdvert(List<VOD> listVOD){
        SuperLog.info2SD(ScreenConstant.TAG,"Begin to get SSP screen advert.");
        AdManager.getInstance().queryAdvertContent(AdConstant.AdClassify.SCREEN, new IADListener() {
            @Override
            public void onSuccess(List<AdvertContent> listSspContent) {
                SuperLog.info2SD(ScreenConstant.TAG,"Get SSP screen advert successfully, size="+(CollectionUtil.isEmpty(listSspContent)?0:listSspContent.size()));
                buildLocalAdvertData(listVOD,listSspContent);
                if(listSspContent.isEmpty()){
                    //true不用设，每次开启屏保都初始化为true
                    screenPresenter.setIsSsp(false);
                }
                screenPresenter.onQueryAdvertFinished(!CollectionUtil.isEmpty(listAdvertContent),!CollectionUtil.isEmpty(listAdvertContentNoSsp));
            }

            @Override
            public void onFail() {
                SuperLog.error(ScreenConstant.TAG,"Get SSP screen advert failed. Can not show ssp screensaver advert.");
                //查SSP广告失败则只播放运营配置广告
                buildLocalAdvertData(listVOD,null);
                screenPresenter.setIsSsp(false);
                screenPresenter.onQueryAdvertFinished(false,!CollectionUtil.isEmpty(listAdvertContentNoSsp));
            }
        },null);
    }

    //暂时废弃
    private String getPlacementCodeFromVod(VOD vod){
        String placementCodes = null;
        try{
            placementCodes = CommonUtil.getCustomField(vod.getCustomFields(),ScreenConstant.CUSTOM_FIELD_PLACEMENT_CODE);
        } catch (Exception e){
            Log.d(ScreenConstant.TAG, "createSspAdvertPlacementCode:",e );
        }
        return placementCodes;
    }

    //将华为平台广告和SSP平台广告统一整合成本地屏保广告数据,注意数据有顺序性
    //listVOD            从VSP平台查询到的屏保广告栏目下的屏保广告VOD对象列表
    //listAdvertContent  从SSP广告平台查询到的屏保广告
    //Vod对象中customField中Extra字段(key=zjapkextra)value格式样例 {"cpId":"2","albumId":"1051629","chnId":"1000001"}
    private void buildLocalAdvertData(List<VOD> listVOD, List<AdvertContent> listSspAdvertContent){
//        //每次请求后清除上一次历史数据
//        this.listAdvertContent.clear();
//        this.listAdvertContentNoSsp.clear();

        //包含SSP的本地广告数据集
        List<ScreenAdvertContent> listAdvertContent      = new ArrayList<>();

        //SSP广告顺序
        List<String> listPlacementCode = Arrays.asList(
                AdConstant.AdvertInfo.SCREEN_BANNER_CODE_1,
                AdConstant.AdvertInfo.SCREEN_BANNER_CODE_2,
                AdConstant.AdvertInfo.SCREEN_BANNER_CODE_3,
                AdConstant.AdvertInfo.SCREEN_BANNER_CODE_4,
                AdConstant.AdvertInfo.SCREEN_BANNER_CODE_5);

        //构建SSP广告对象的本地广告,用于初始播放SSP广告屏保内容
        if(!CollectionUtil.isEmpty(listSspAdvertContent)){
            //优先按照SSP的数据展示屏保，顺序信息也按照SSP配置的来，SSP广告空位置由运营配置广告填补,数量固定为5(最多为5)
            int i=0;
            for(String placementCode:listPlacementCode){
                AdvertContent sspContent = getSspAdvertContent(placementCode,listSspAdvertContent);//获取placementCode对应的SSP广告
                ScreenAdvertContent content = new ScreenAdvertContent();
                if(sspContent == null){
                    //指定位置SSP广告为空，用运营配置广告填充
                    //listVOD肯定不为空,之前有验证
                    if(listVOD == null || listVOD.size()<i+1){
                        SuperLog.error(ScreenConstant.TAG,"VSP AD list is empty,can not set config AD to : "+placementCode);
                        continue;
                    }
                    VOD vod = listVOD.get(i);
                    i++;
                    content = generateConfigAd(vod);
                    if(content == null){
                        SuperLog.error(ScreenConstant.TAG,"VSP AD banner is empty,can not set config AD to : "+placementCode);
                        continue;
                    }
                    content.setType(ScreenAdvertContent.TYPE_VSP);
                } else {
                    //指定位置SSP广告不为空,用SSP广告填充
                    content.setType(ScreenAdvertContent.TYPE_SSP);
                    String banner = AdUtil.getSSPAdvertValue(sspContent,"banner");
                    if(TextUtils.isEmpty(banner)){
                        SuperLog.error(ScreenConstant.TAG,"SSP AD banner is empty,can not set SSP AD to : "+placementCode);
                        continue;     //没有广告海报
                    }
                    content.setBannerUrl(banner);
                    content.setClickUrl(AdUtil.getSSPAdvertValue(sspContent,"clickUrl"));
                    content.setSspContent(sspContent);//保存完整SSP广告数据,用于上报话单
                }
                listAdvertContent.add(content);
            }
        }

        //纯运营广告的本地广告数据集
        List<ScreenAdvertContent> listAdvertContentNoSsp = new ArrayList<>();
        //构建不包含SSP广告的本地广告,用于SSP广告播放时间结束后播放的屏保内容
        if(!CollectionUtil.isEmpty(listVOD)){
            for(VOD vod : listVOD){
                ScreenAdvertContent content = generateConfigAd(vod);
                listAdvertContentNoSsp.add(content);
                if(listAdvertContentNoSsp.size()==5){
                    break;
                }
            }
        }

        this.listAdvertContentNoSsp = listAdvertContentNoSsp;
        this.listAdvertContent      = listAdvertContent;
        SuperLog.info2SD(ScreenConstant.TAG,"SSP advert size = "+this.listAdvertContent.size() + "\tVSP advert size = "+this.listAdvertContentNoSsp.size() );
        //日志信息，方便定位问题，无逻辑意义
//        int index = 1;
//        for(ScreenAdvertContent content : this.listAdvertContent){
//            SuperLog.info2SD("Check liuxia",index++ + " FinalScreenAdvert=" + content.getBannerUrl());
//        }
    }

    //根据运营配置的VOD生成一条运营配置的本地屏保广告数据
    private ScreenAdvertContent generateConfigAd(VOD vod){
        ScreenAdvertContent content = new ScreenAdvertContent();
        content.setType(ScreenAdvertContent.TYPE_VSP);
        String banner = vod.getPicture().getPosters().get(0);
        if(TextUtils.isEmpty(banner)){
            return null;
        }
        content.setBannerUrl(banner);

        //话单数据
        content.setContentID(vod.getID());
        content.setContentName(vod.getName());
        //跳转数据
        content.setClickUrl(CommonUtil.getCustomField(vod.getCustomFields(),ScreenConstant.CUSTOM_FIELD_CLICK_URL));
        content.setPkg(CommonUtil.getCustomField(vod.getCustomFields(),ScreenConstant.CUSTOM_APK_PACKAGE));
        content.setCls(CommonUtil.getCustomField(vod.getCustomFields(),ScreenConstant.CUSTOM_APK_CLASS));
        content.setExtra(CommonUtil.getCustomField(vod.getCustomFields(),ScreenConstant.CUSTOM_APK_EXTRA));
        return content;
    }

    //获取单张屏保图片展示时长,单位ms
    public long getBannerInterval(){
        String sInterval = CommonUtil.getConfigValue(ScreenConstant.TERMINAL_PARAM_ADVERT_TIME);
        long   interval;
        try{
            interval = Long.parseLong(sInterval)*1000L;
        } catch (Exception e){
            interval = ScreenConstant.DEFAULT_ADVERT_INTERVAL;
        }
        return interval;
    }

    //返回SSP屏保广告播放时长,单位ms,时间到后改为只播放运营配置屏保
    public long getAdvertDuration(){
        String sInterval = CommonUtil.getConfigValue(ScreenConstant.TERMINAL_PARAM_SSP_ADVERT_DURATION);
        long   interval;
        try{
            interval = Long.parseLong(sInterval)*1000L;
        } catch (Exception e){
            interval = ScreenConstant.DEFAULT_SSP_ADVERT_DURATION*1000L;
        }
        return interval;
    }

    private String getVodAdType(VOD vod){
        String type = CommonUtil.getCustomField(vod.getCustomFields(),ScreenConstant.CUSTOM_FIELD_ADVERT_TYPE);
        if(ScreenAdvertContent.TYPE_SSP.equals(type) || ScreenAdvertContent.TYPE_VSP.equals(type)){
            return type;
        } else {
            //没有配置或者配置错误认为是运营配置类型的广告
            return ScreenAdvertContent.TYPE_VSP;
        }
    }

    //获取指定placementCode的SSP广告,无则返回空
    private AdvertContent getSspAdvertContent(String placementCode, List<AdvertContent> sspListAdvertContent){
        if(CollectionUtil.isEmpty(sspListAdvertContent)){
            SuperLog.error(ScreenConstant.TAG,"sspListAdvertContent is empty");
            return null;
        }
        for(AdvertContent content : sspListAdvertContent){
            if(content.getPlacementCode().equals(placementCode)){
                return content;
            }
        }
        SuperLog.error(ScreenConstant.TAG,"Can not find sspAdvertContent by the placementCode : "+placementCode);
        return null;
    }
}
