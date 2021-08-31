package com.pukka.ydepg.common.utils;

import android.os.Build;
import android.text.TextUtils;

import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.featured.bean.VodBean;

import java.util.ArrayList;
import java.util.List;

public class VodUtil {

    private static final String TAG=VodUtil.class.getName();

    private static String device = getDevice();

    public static boolean canPlay(boolean is4kResource){
//        if (true){
//            return false;
//        }
        if ( !is4kResource ){
            //非4K片源，可直接播放
            return true;
        }

        return isDeviceSupport4K();
    }

    //当前机顶盒是否支持4K片源
    public static boolean isDeviceSupport4K(){
        List<String> deviceUnsupport4K = SessionService.getInstance().getSession().getTerminalConfigurationUnsupport4KDevice();
        if( deviceUnsupport4K == null || deviceUnsupport4K.size() == 0 ){
            //没有配置，可播放
            return true;
        }

        if(deviceUnsupport4K.contains(device)){
            //当前设备为不支持4K设备，不可播放
            return false;
        } else {
            //当前设备为支持4K设备，可播放
            return true;
        }
    }

    private static String getDevice(){
        String systemModel = DeviceInfo.getSystemInfo(Constant.DEVICE_RAW);
        if (TextUtils.isEmpty(systemModel) || "unknown".equals(systemModel)) {
            systemModel = Build.MODEL;
        }
        return systemModel;
    }

    public static boolean isMiguVod(VOD vod){
        boolean isMiguVod = false;
        String miguCpID= SessionService.getInstance().getSession().getMiguCpID();
        if(vod != null && !TextUtils.isEmpty(vod.getCpId()) && !TextUtils.isEmpty(miguCpID)&&miguCpID.contains(vod.getCpId())){
            isMiguVod = true;
        }
        return isMiguVod;
    }

    public static boolean isMiguVod(VODDetail vod){
        boolean isMiguVod = false;
        String miguCpID= SessionService.getInstance().getSession().getMiguCpID();
        if(vod != null && !TextUtils.isEmpty(vod.getCpId()) && !TextUtils.isEmpty(miguCpID)&&miguCpID.contains(vod.getCpId())){
            isMiguVod = true;
        }
        return isMiguVod;
    }

    public static boolean isMiguVod(VodBean vod){
        boolean isMiguVod = false;
        String miguCpID= SessionService.getInstance().getSession().getMiguCpID();
        if(vod != null && !TextUtils.isEmpty(vod.getCpId()) && !TextUtils.isEmpty(miguCpID)&&miguCpID.contains(vod.getCpId())){
            isMiguVod = true;
        }
        return isMiguVod;
    }

    public static boolean isMiguVod(String cpID){
        boolean isMiguVod = false;
        String miguCpID= SessionService.getInstance().getSession().getMiguCpID();
        if(!TextUtils.isEmpty(cpID) && miguCpID.contains(cpID)){
            isMiguVod = true;
        }
        return isMiguVod;
    }


    public static String getSimpleVoddetail(VODDetail mVODDetail,int previewDuration){
        SuperLog.debug(TAG,"voddetail code:"+mVODDetail.getCode());
        VODDetail vodDetail = new VODDetail();
        //vodDetail.setPicture(mVODDetail.getPicture()); 解决该对象(搜索多啦A梦)过大导致的启动播放Activity crash问题
        vodDetail.setName(mVODDetail.getName());
        vodDetail.setIntroduce(mVODDetail.getIntroduce());
        vodDetail.setID(mVODDetail.getID());
        vodDetail.setVODType(mVODDetail.getVODType());
        vodDetail.setMediaFiles(mVODDetail.getMediaFiles());
        //电视剧比较多时点击播放卡顿,只取自己需要的
        List<Episode> episodes=new ArrayList<>();
        Episode episode=getFirstEpisodeWithPrice(mVODDetail.getEpisodes());
        if(null!=episode){
            episodes.add(episode);
        }
        vodDetail.setEpisodes(episodes);
        vodDetail.setPrice(mVODDetail.getPrice());
        vodDetail.setCustomFields(mVODDetail.getCustomFields());
        vodDetail.setPreviewDuration(previewDuration);
        vodDetail.setCode(mVODDetail.getCode());
        vodDetail.setSubjectIDs(mVODDetail.getSubjectIDs());
        //贴片广告需要
        vodDetail.setCmsType(mVODDetail.getCmsType());
        vodDetail.setGenres(mVODDetail.getGenres());
        return  JsonParse.object2String(vodDetail);
    }

    //电视剧类型，取第一个价格不为零的子集，若不存在价格不为零的子集，返回Null
    private static  Episode getFirstEpisodeWithPrice(List<Episode> episodes){
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
