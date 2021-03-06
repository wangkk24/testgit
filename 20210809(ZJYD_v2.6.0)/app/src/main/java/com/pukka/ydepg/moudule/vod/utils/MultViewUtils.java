package com.pukka.ydepg.moudule.vod.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.V6Constant;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayMultiMediaVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.screensaver.ScreenConstant;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ShowBuyControl;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.livetv.utils.LiveUtils;
import com.pukka.ydepg.moudule.livetv.utils.MultChannelPlayUtils;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.main.bean.AllVideoConfig;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.play.PlayConstant;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.play.TVPlayActivity;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.play.freecamera.FreeCameraPlayActivity;
import com.pukka.ydepg.moudule.player.node.CurrentChannelPlaybillInfo;
import com.pukka.ydepg.moudule.player.node.Schedule;
import com.pukka.ydepg.moudule.vod.playerController.VodPlayerControllerView;
import com.pukka.ydepg.moudule.vod.presenter.VoddetailEpsiodePresenter;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.module.VideoBean;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.view.MD360PlayerActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ?????????panjw on 2021/6/18 11:12
 * <p>
 * ?????????panjw@easier.cn
 */
public class MultViewUtils {

    private static final String TAG = MultViewUtils.class.getSimpleName();
    private static int ifdol = 0;
    private static int ifree = 1;
    private static int vr = 2;
    private static int normal = 3;

    private static BrowseEpsiodesUtils mBrowseEpsiodesUtils = new BrowseEpsiodesUtils();

    private static VodPlayerControllerView vodPlayerControllerView;

    public static boolean isIsEpsiodes() {
        return isEpsiodes;
    }

    private static boolean isEpsiodes;

    public static boolean getMultType(VODDetail mVODDetail, VoddetailEpsiodesUtils voddetailEpsiodesUtils, RxAppCompatActivity activity, boolean isVod) {
        List<String> supportDevicesList = CommonUtil.getListConfigValue(Constant.SUPPORT_EDSPATIAL_VIDEO_DEVICES);

        if (getMultType(mVODDetail, voddetailEpsiodesUtils) == normal) {
            //???????????????????????????????????????
            return true;
        }
        if(supportDevicesList.contains(CommonUtil.getDeviceType())) {
            ShowBuyControl control = new ShowBuyControl(activity);
            control.setCallBack(new ShowBuyControl.ShowBuyTagCallBack() {
                @Override
                public void showBuyTag(int showBuy) {

                }
            });

            control.setCallBack(new ShowBuyControl.ShowMultViewResponseCallBack() {
                @Override
                public void showBuyTag(PlayVODResponse response) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (getMultType(mVODDetail, voddetailEpsiodesUtils) == ifdol) {
                                //?????????
                                AllVideoConfig allVideoConfig = new AllVideoConfig();

                                if (null != mVODDetail.getCustomFields() && mVODDetail.getCustomFields().size() > 0) {
                                    if (!TextUtils.isEmpty(CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.CAMERADEGREE))) {

                                        allVideoConfig.setCameraDegree(Integer.parseInt(CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.CAMERADEGREE)));
                                    }
                                }

                                if (null != mVODDetail.getCustomFields() && mVODDetail.getCustomFields().size() > 0) {
                                    if (!TextUtils.isEmpty(CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.CAMERANUM))) {

                                        allVideoConfig.setCameraNum(Integer.parseInt(CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.CAMERANUM)));
                                    }
                                }
                                allVideoConfig.setPicture(null);
                                allVideoConfig.setMediaType(2);
                                List<String> playUrl = new ArrayList<>();
                                playUrl.add(response.getPlayURL());
//                            playUrl.add("http://121.36.11.38:80/5/16/20210506/268437780/268437780.mpd");
                                allVideoConfig.setResourcePlayURL(playUrl);
                                if (isVod) {
                                    allVideoConfig.setResourceName(mVODDetail.getName());
                                }
                                //1 ?????? 2 ??????
                                allVideoConfig.setMediaType(2);
                                Gson gson = new Gson();
                                //????????????
                                Intent intent = new Intent();
                                intent.setClass(activity, TVPlayActivity.class);
                                intent.putExtra(PlayConstant.EXTRA, gson.toJson(allVideoConfig));
                                if (isEpsiodes) {
                                    intent.putExtra("CONTENTID", mVODDetail.getID());
                                }
                                activity.startActivity(intent);

                            } else if (getMultType(mVODDetail, voddetailEpsiodesUtils) == ifree) {
                                //????????????
                                AllVideoConfig allVideoConfig = new AllVideoConfig();

                                if (null != mVODDetail.getCustomFields() && mVODDetail.getCustomFields().size() > 0) {
                                    if (!TextUtils.isEmpty(CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.CAMERADEGREE))) {

                                        allVideoConfig.setCameraDegree(Integer.parseInt(CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.CAMERADEGREE)));
                                    }
                                }

                                if (null != mVODDetail.getCustomFields() && mVODDetail.getCustomFields().size() > 0) {
                                    if (!TextUtils.isEmpty(CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.CAMERANUM))) {

                                        allVideoConfig.setCameraNum(Integer.parseInt(CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.CAMERANUM)));
                                    }
                                }

                                if (null != mVODDetail.getCustomFields() && mVODDetail.getCustomFields().size() > 0) {
                                    if (!TextUtils.isEmpty(CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.MAINCAMERAID))) {

                                        allVideoConfig.setMainCameraId(Integer.parseInt(CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.MAINCAMERAID)));
                                    }
                                }
                                allVideoConfig.setPicture(null);
                                List<String> playUrl = new ArrayList<>();
                                playUrl.add(response.getPlayURL());
//                            playUrl.add("http://121.36.11.38:80/5/16/20210506/268437780/268437780.mpd");
                                allVideoConfig.setResourcePlayURL(playUrl);
                                allVideoConfig.setResourceName(mVODDetail.getName());
                                //1 ?????? 2 ??????
                                allVideoConfig.setMediaType(2);
                                allVideoConfig.setContentID(mVODDetail.getID());
                                allVideoConfig.setReturnURL("");
                                Gson gson = new Gson();
                                //????????????
                                Intent intent = new Intent();
                                intent.setClass(activity, FreeCameraPlayActivity.class);
                                intent.putExtra(PlayConstant.EXTRA, gson.toJson(allVideoConfig));
                                intent.putExtra("CONTENTID", mVODDetail.getID());
                                if (isEpsiodes) {
                                    intent.putExtra("VOD", voddetailEpsiodesUtils.getSelesctedEpisode().getVOD());
                                } else {
                                    intent.putExtra("VOD", mVODDetail);
                                }

                                activity.startActivity(intent);
                            } else if (getMultType(mVODDetail, voddetailEpsiodesUtils) == vr) {
                                //vr
                                VideoBean videoBean = new VideoBean();
                                videoBean.setContentID(mVODDetail.getID());
                                videoBean.setReturnURL("");
                                videoBean.setMediaType(2);

                                videoBean.setResourceName(mVODDetail.getName());
                                List<String> playUrl = new ArrayList<>();
                                playUrl.add(response.getPlayURL());
//                            playUrl.add("http://121.36.11.38:80/wh7f454c46tw2062814276_-586542620/5/16/20200921/268436084/index.m3u8");
                                videoBean.setResourcePlayURL(playUrl);
                                //???????????????
                                //1.????????????dimension?????????2D??????3D
                                // dimension == 2D -- 2D
                                // dimension == 3D -- 3D
                                //2.?????????2D????????????isVRContent, viewDegree?????????2D_180??????2D_360:
                                // isVRContent == 1 && viewDegree == 2 -- 2D_180
                                // isVRContent == 1 && viewDegree == 1 -- 2D_360
                                //3.?????????3D???????????????isVRContent, viewDegree?????????180 ?????? 360
                                // isVRContent == 1 && viewDegree == 2 -- 2D_180 -> formatOf3D == 1 --> PlayerType3D_LR_180
                                // isVRContent == 1 && viewDegree == 2 -- 2D_180 -> formatOf3D == 2 --> PlayerType3D_UD_180
                                // ----------------------------------------------------------------------------------------
                                // isVRContent == 1 && viewDegree == 1 -- 2D_180 -> formatOf3D == 1 --> PlayerType3D_LR_360
                                // isVRContent == 1 && viewDegree == 1 -- 2D_180 -> formatOf3D == 2 --> PlayerType3D_UD_360


                                /**
                                 * ????????????
                                 * 3: VR180 ??? 2D ??????;
                                 * 4: VR360 ??? 2D ??????;
                                 * 5: VR180 ??? 3D ??????;
                                 * 6: VR360 ??? 3D ??????;
                                 */
                                if (!TextUtils.isEmpty(mVODDetail.getMediaFiles().get(0).getDimension())) {
                                    if (mVODDetail.getMediaFiles().get(0).getDimension().equals("2")) {
                                        if (!TextUtils.isEmpty(mVODDetail.getMediaFiles().get(0).getViewDegree())) {
                                            if (mVODDetail.getMediaFiles().get(0).getViewDegree().equals("2")) {
                                                //2d180
                                                videoBean.setResourceType(3);
                                            } else if (mVODDetail.getMediaFiles().get(0).getViewDegree().equals("1")) {
                                                //2d360
                                                videoBean.setResourceType(4);
                                            }
                                        }

                                    } else if (mVODDetail.getMediaFiles().get(0).getDimension().equals("3")) {
                                        if (!TextUtils.isEmpty(mVODDetail.getMediaFiles().get(0).getViewDegree())) {
                                            if (mVODDetail.getMediaFiles().get(0).getViewDegree().equals("2")) {
                                                //3d180
                                                videoBean.setResourceType(3);
                                            } else if (mVODDetail.getMediaFiles().get(0).getViewDegree().equals("1")) {
                                                //3d360
                                                videoBean.setResourceType(4);
                                            }
                                            if (null != mVODDetail.getMediaFiles().get(0).getFormatOf3D()) {
                                                //??????
                                                if (mVODDetail.getMediaFiles().get(0).getFormatOf3D().equals("1")) {
                                                    videoBean.setType3D(2);
                                                } else if (mVODDetail.getMediaFiles().get(0).getFormatOf3D().equals("2")) {
                                                    videoBean.setType3D(1);
                                                }

                                            }

                                        }

                                    }
                                }

                                MD360PlayerActivity.setExtra(mVODDetail.getID(), isEpsiodes ? voddetailEpsiodesUtils.getSelesctedEpisode().getVOD() : mVODDetail);
                                MD360PlayerActivity.startVideo(activity, videoBean);
                            }
                        }
                    });


                }

                @Override
                public void showBuyTags(PlayMultiMediaVODResponse response) {
                    if (getMultType(mVODDetail, voddetailEpsiodesUtils) == ifdol) {
                        //?????????
                        AllVideoConfig allVideoConfig = new AllVideoConfig();

                        CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.IFIDOL);
                        if (null != mVODDetail.getCustomFields() && mVODDetail.getCustomFields().size() > 0) {
                            if (!TextUtils.isEmpty(CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.CAMERADEGREE))) {

                                allVideoConfig.setCameraDegree(Integer.parseInt(CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.CAMERADEGREE)));
                            }
                        }

                        if (null != mVODDetail.getCustomFields() && mVODDetail.getCustomFields().size() > 0) {
                            if (!TextUtils.isEmpty(CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.CAMERANUM))) {

                                allVideoConfig.setCameraNum(Integer.parseInt(CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.CAMERANUM)));
                            }
                        }
                        allVideoConfig.setPicture(null);
                        allVideoConfig.setMediaType(2);
                        List<String> playUrl = new ArrayList<>();
                        for (int i = 0; i < response.getMultiMediaAuthorizeResults().size(); i++) {
                            playUrl.add(response.getMultiMediaAuthorizeResults().get(i).getPlayURL());
                        }
//                    playUrl.add(response.getMultiMediaAuthorizeResults());
//                            playUrl.add("http://121.36.11.38:80/5/16/20210506/268437780/268437780.mpd");
                        allVideoConfig.setResourcePlayURL(playUrl);
                        if (isVod) {
                            allVideoConfig.setResourceName(mVODDetail.getName());
                        }
                        //1 ?????? 2 ??????
                        allVideoConfig.setMediaType(2);
                        Gson gson = new Gson();
                        //????????????
                        Intent intent = new Intent();
                        intent.setClass(activity, TVPlayActivity.class);
                        intent.putExtra(PlayConstant.EXTRA, gson.toJson(allVideoConfig));
                        if (isEpsiodes) {
                            intent.putExtra("CONTENTID", mVODDetail.getID());
                        }
                        activity.startActivity(intent);

                    }
                }
            });

            PlayVODRequest playVODRequest = new PlayVODRequest();
            if (isEpsiodes) {
                if (voddetailEpsiodesUtils != null && voddetailEpsiodesUtils.getSelesctedEpisode() != null) {
                    playVODRequest.setVODID(voddetailEpsiodesUtils.getSelesctedEpisode().getVOD().getID());
                    playVODRequest.setMediaID(voddetailEpsiodesUtils.getSelesctedEpisode().getVOD().getMediaFiles().get(0).getID());
                } else {
                    SuperLog.info2SD(TAG, "voddetailEpsiodesUtils or voddetailEpsiodesUtils.getSelesctedEpisode() is null");
                }

            } else {
                playVODRequest.setVODID(mVODDetail.getID());
                playVODRequest.setMediaID(mVODDetail.getMediaFiles().get(0).getID());
            }
            playVODRequest.setURLFormat("1");
            playVODRequest.setIsReturnProduct("1");
            control.playVod(playVODRequest);
        }else
        {
            EpgToast.showToast(OTTApplication.getContext(),"?????????????????????????????????????????????");
        }
        return false;
    }

    private static int getMultType(VODDetail mVODDetail, VoddetailEpsiodesUtils voddetailEpsiodesUtils) {

        /* * 1??????VOD.customFields????????????Key=ifIdol??? ?????????????????????????????????????????????????????????0/1???????????????0
         * 1?????????????????????????????????0?????????????????????????????????????????????????????????????????????????????????
         * 2?????????????????????????????????VOD.Picture.others[]??????
         * 3??????VODMediaFile.customFields????????????Key=cameraInfo,??????????????????????????????????????????????????????????????????????????????????????????,??????????????????????????????
         * ???????????????1????????????????????????2?????????????????????
         * ?????????????????????????????????VOD.Picture.others[index]??????????????????
         * ???????????????1 0??????????????????????????????????????????????????????????????????Picture.others[0]?????????????????????
         * 4???????????????VODMediaFile.customFields????????????Key=cameraPosition??????????????????????????????????????????????????????0~12?????????0????????????????????????1~12???????????????0????????????????????????*/
//        String ifidol = CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.IFIDOL);//?????????
        String ifidol = "";//?????????

                /*1??????VODDetail.customFields????????????Key=ifFreeD??? ???????????????????????????????????????????????????0/1???????????????0r
                0???????????????????????????,1??????????????????????????????????????????????????????????????????
                2??????VODDetail.customFields????????????Key=cameraNum,??????????????????????????????????????????1??????????????????36????????????value???36????????????1~36???
                3??????VODDetail.customFields????????????Key=mainCameraId??????????????????ID
                4??????VODDetail.customFields????????????Key=cameraDegree?????????????????????????????????360?????????????????????*/
//        String iffreed = CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.IFFREED);//????????????
        String iffreed = "";//????????????
        String isVr = "";

        if (mVODDetail.getVODType().equals("0") || mVODDetail.getVODType().equals("2")) {
            isEpsiodes = false;
            //??????
            ifidol = CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.IFIDOL);
            iffreed = CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.IFFREED);
            if (null != mVODDetail.getMediaFiles() && mVODDetail.getMediaFiles().size() > 0 && null != mVODDetail.getMediaFiles().get(0).getIsVRContent()) {
                isVr = mVODDetail.getMediaFiles().get(0).getIsVRContent();
            }
        } else {
            isEpsiodes = true;
            //?????????
            if (null != voddetailEpsiodesUtils && voddetailEpsiodesUtils.getSelesctedEpisode() != null) {
                //??????
                ifidol = CommonUtil.getCustomField(voddetailEpsiodesUtils.getSelesctedEpisode().getVOD().getCustomFields(), ScreenConstant.IFIDOL);
                iffreed = CommonUtil.getCustomField(voddetailEpsiodesUtils.getSelesctedEpisode().getVOD().getCustomFields(), ScreenConstant.IFFREED);
                if (null != voddetailEpsiodesUtils.getSelesctedEpisode().getVOD().getMediaFiles() && voddetailEpsiodesUtils.getSelesctedEpisode().getVOD().getMediaFiles().size() > 0 && null != voddetailEpsiodesUtils.getSelesctedEpisode().getVOD().getMediaFiles().get(0).getIsVRContent()) {
                    isVr = voddetailEpsiodesUtils.getSelesctedEpisode().getVOD().getMediaFiles().get(0).getIsVRContent();
                }
            }
        }
        //?????????????????????????????????
        //????????????
        if (null != ifidol && ifidol.equals("1")) {
            return ifdol;

        } else if (null != iffreed && iffreed.equals("1")) {
            //?????????
            return ifree;
        } else if (!TextUtils.isEmpty(isVr) && isVr.equals("1")) {
            //vr
            return vr;
        } else {
            //????????????
            return normal;
        }
    }

    public static boolean getChannelMultType(ChannelDetail channelDetail, Context context) {
        if (getChannelMultType(channelDetail) == normal) {
            return true;
        }
        List<String> supportDevicesList = CommonUtil.getListConfigValue(Constant.SUPPORT_EDSPATIAL_VIDEO_DEVICES);

        if(supportDevicesList.contains(CommonUtil.getDeviceType())) {
            MultChannelPlayUtils multChannelPlayUtils = new MultChannelPlayUtils();
            multChannelPlayUtils.setmPlayurlCallback(new MultChannelPlayUtils.PlayurlCallback() {
                @Override
                public void getPlayUrlSuccess(String url) {
                    SuperLog.info2SD(TAG, "url is " + url);
                    if (getChannelMultType(channelDetail) == ifdol) {
                        //?????????
                        AllVideoConfig allVideoConfig = new AllVideoConfig();

                        if (null != channelDetail.getCustomFields() && channelDetail.getCustomFields().size() > 0) {
                            if (!TextUtils.isEmpty(CommonUtil.getCustomField(channelDetail.getCustomFields(), ScreenConstant.CAMERADEGREE))) {

                                allVideoConfig.setCameraDegree(Integer.parseInt(CommonUtil.getCustomField(channelDetail.getCustomFields(), ScreenConstant.CAMERADEGREE)));
                            }
                        }

                        if (null != channelDetail.getCustomFields() && channelDetail.getCustomFields().size() > 0) {
                            if (!TextUtils.isEmpty(CommonUtil.getCustomField(channelDetail.getCustomFields(), ScreenConstant.CAMERANUM))) {

                                allVideoConfig.setCameraNum(Integer.parseInt(CommonUtil.getCustomField(channelDetail.getCustomFields(), ScreenConstant.CAMERANUM)));
                            }
                        }
                        allVideoConfig.setPicture(null);
                        allVideoConfig.setMediaType(2);
                        List<String> playUrl = new ArrayList<>();
                        playUrl.add("");
//                            playUrl.add("http://121.36.11.38:80/5/16/20210506/268437780/268437780.mpd");
                        allVideoConfig.setResourcePlayURL(playUrl);
                        allVideoConfig.setResourceName(channelDetail.getName());
                        //1 ?????? 2 ??????
                        allVideoConfig.setMediaType(1);
                        Gson gson = new Gson();
                        //????????????
                        Intent intent = new Intent();
                        intent.setClass(context, TVPlayActivity.class);
                        intent.putExtra(PlayConstant.EXTRA, gson.toJson(allVideoConfig));
                        if (isEpsiodes) {
                            intent.putExtra("CONTENTID", channelDetail.getID());
                        }
                        context.startActivity(intent);

                    } else if (getChannelMultType(channelDetail) == ifree) {
                        //????????????
                        AllVideoConfig allVideoConfig = new AllVideoConfig();

                        if (null != channelDetail.getCustomFields() && channelDetail.getCustomFields().size() > 0) {
                            if (!TextUtils.isEmpty(CommonUtil.getCustomField(channelDetail.getCustomFields(), ScreenConstant.CAMERADEGREE))) {

                                allVideoConfig.setCameraDegree(Integer.parseInt(CommonUtil.getCustomField(channelDetail.getCustomFields(), ScreenConstant.CAMERADEGREE)));
                            }
                        }

                        if (null != channelDetail.getCustomFields() && channelDetail.getCustomFields().size() > 0) {
                            if (!TextUtils.isEmpty(CommonUtil.getCustomField(channelDetail.getCustomFields(), ScreenConstant.CAMERANUM))) {

                                allVideoConfig.setCameraNum(Integer.parseInt(CommonUtil.getCustomField(channelDetail.getCustomFields(), ScreenConstant.CAMERANUM)));
                            }
                        }

                        if (null != channelDetail.getCustomFields() && channelDetail.getCustomFields().size() > 0) {
                            if (!TextUtils.isEmpty(CommonUtil.getCustomField(channelDetail.getCustomFields(), ScreenConstant.MAINCAMERAID))) {

                                allVideoConfig.setMainCameraId(Integer.parseInt(CommonUtil.getCustomField(channelDetail.getCustomFields(), ScreenConstant.MAINCAMERAID)));
                            }
                        }
                        allVideoConfig.setPicture(null);
                        List<String> playUrl = new ArrayList<>();
                        playUrl.add(url);
//                            playUrl.add("http://121.36.11.38:80/5/16/20210506/268437780/268437780.mpd");
                        allVideoConfig.setResourcePlayURL(playUrl);
                        allVideoConfig.setResourceName(channelDetail.getName());
                        //1 ?????? 2 ??????
                        allVideoConfig.setMediaType(2);
                        allVideoConfig.setContentID(channelDetail.getID());
                        allVideoConfig.setReturnURL("");
                        Gson gson = new Gson();
                        //????????????
                        Intent intent = new Intent();
                        intent.setClass(context, FreeCameraPlayActivity.class);
                        intent.putExtra(PlayConstant.EXTRA, gson.toJson(allVideoConfig));
                        if (isEpsiodes) {
                            intent.putExtra("CONTENTID", channelDetail.getID());
                        }
                        context.startActivity(intent);
                    } else if (getChannelMultType(channelDetail) == vr) {
                        //vr
                        VideoBean videoBean = new VideoBean();
                        videoBean.setResourceName(channelDetail.getName());
                        List<String> playUrl = new ArrayList<>();
                        playUrl.add(url);
//                            playUrl.add("http://121.36.11.38:80/wh7f454c46tw2062814276_-586542620/5/16/20200921/268436084/index.m3u8");
                        videoBean.setResourcePlayURL(playUrl);
                        //???????????????
                        //1.????????????dimension?????????2D??????3D
                        // dimension == 2D -- 2D
                        // dimension == 3D -- 3D
                        //2.?????????2D????????????isVRContent, viewDegree?????????2D_180??????2D_360:
                        // isVRContent == 1 && viewDegree == 2 -- 2D_180
                        // isVRContent == 1 && viewDegree == 1 -- 2D_360
                        //3.?????????3D???????????????isVRContent, viewDegree?????????180 ?????? 360
                        // isVRContent == 1 && viewDegree == 2 -- 2D_180 -> formatOf3D == 1 --> PlayerType3D_LR_180
                        // isVRContent == 1 && viewDegree == 2 -- 2D_180 -> formatOf3D == 2 --> PlayerType3D_UD_180
                        // ----------------------------------------------------------------------------------------
                        // isVRContent == 1 && viewDegree == 1 -- 2D_180 -> formatOf3D == 1 --> PlayerType3D_LR_360
                        // isVRContent == 1 && viewDegree == 1 -- 2D_180 -> formatOf3D == 2 --> PlayerType3D_UD_360


                        /**
                         * ????????????
                         * 3: VR180 ??? 2D ??????;
                         * 4: VR360 ??? 2D ??????;
                         * 5: VR180 ??? 3D ??????;
                         * 6: VR360 ??? 3D ??????;
                         */
                        if (!TextUtils.isEmpty(channelDetail.getPhysicalChannels().get(0).getDimension())) {
                            if (channelDetail.getPhysicalChannels().get(0).getDimension().equals("2")) {
                                if (!TextUtils.isEmpty(channelDetail.getPhysicalChannels().get(0).getViewDegree())) {
                                    if (channelDetail.getPhysicalChannels().get(0).getViewDegree().equals("2")) {
                                        //2d180
                                        videoBean.setResourceType(3);
                                    } else if (channelDetail.getPhysicalChannels().get(0).getViewDegree().equals("1")) {
                                        //2d360
                                        videoBean.setResourceType(4);
                                    }
                                }

                            } else if (channelDetail.getPhysicalChannels().get(0).getDimension().equals("3")) {
                                if (!TextUtils.isEmpty(channelDetail.getPhysicalChannels().get(0).getViewDegree())) {
                                    if (channelDetail.getPhysicalChannels().get(0).getViewDegree().equals("2")) {
                                        //3d180
                                        videoBean.setResourceType(3);
                                    } else if (channelDetail.getPhysicalChannels().get(0).getViewDegree().equals("1")) {
                                        //3d360
                                        videoBean.setResourceType(4);
                                    }
                                    if (null != channelDetail.getPhysicalChannels().get(0).getFormatOf3D()) {
                                        //??????
                                        if (channelDetail.getPhysicalChannels().get(0).getFormatOf3D().equals("1")) {
                                            videoBean.setType3D(2);
                                        } else if (channelDetail.getPhysicalChannels().get(0).getFormatOf3D().equals("2")) {
                                            videoBean.setType3D(1);
                                        }

                                    }

                                }

                            }
                        }

                        MD360PlayerActivity.startVideo(context, videoBean);
                    }

                }

                @Override
                public void getPlayUrlError() {

                }
            });

            Schedule schedule = setChannelToSchedule(channelDetail);
            CurrentChannelPlaybillInfo playbillInfo = new CurrentChannelPlaybillInfo();
            playbillInfo.setChannelId(schedule.getId());
            playbillInfo.setChannelMediaId(schedule.getMediaID());
            playbillInfo.setChannelNo(schedule.getChannelNo());

            //??????request
            PlayChannelRequest playChannelRequest = new PlayChannelRequest();
            playChannelRequest.setChannelID(playbillInfo.getChannelId());
            //?????????ID
            String playbillID = playbillInfo.getPlaybillId();
            if (!TextUtils.isEmpty(playbillID) && !TextUtils.isEmpty(playbillInfo.getChannelProgramName())) {
                //?????????ID
                playChannelRequest.setPlaybillID(playbillID);
            }
            playChannelRequest.setMediaID(playbillInfo.getChannelMediaId());
            playChannelRequest.setBusinessType(V6Constant.ChannelURLType.BTV);
            playChannelRequest.setURLFormat("1");//HLS
            RxAppCompatActivity rxAppCompatActivity = (RxAppCompatActivity) context;
            multChannelPlayUtils.playChannel(playChannelRequest, rxAppCompatActivity, context);
        }else
        {
            EpgToast.showToast(OTTApplication.getContext(),"?????????????????????????????????????????????");
        }

        return false;
    }

    private static int getChannelMultType(ChannelDetail channelDetail) {
        /* * 1??????VOD.customFields????????????Key=ifIdol??? ?????????????????????????????????????????????????????????0/1???????????????0
         * 1?????????????????????????????????0?????????????????????????????????????????????????????????????????????????????????
         * 2?????????????????????????????????VOD.Picture.others[]??????
         * 3??????VODMediaFile.customFields????????????Key=cameraInfo,??????????????????????????????????????????????????????????????????????????????????????????,??????????????????????????????
         * ???????????????1????????????????????????2?????????????????????
         * ?????????????????????????????????VOD.Picture.others[index]??????????????????
         * ???????????????1 0??????????????????????????????????????????????????????????????????Picture.others[0]?????????????????????
         * 4???????????????VODMediaFile.customFields????????????Key=cameraPosition??????????????????????????????????????????????????????0~12?????????0????????????????????????1~12???????????????0????????????????????????*/
//        String ifidol = CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.IFIDOL);//?????????

                /*1??????VODDetail.customFields????????????Key=ifFreeD??? ???????????????????????????????????????????????????0/1???????????????0r
                0???????????????????????????,1??????????????????????????????????????????????????????????????????
                2??????VODDetail.customFields????????????Key=cameraNum,??????????????????????????????????????????1??????????????????36????????????value???36????????????1~36???
                3??????VODDetail.customFields????????????Key=mainCameraId??????????????????ID
                4??????VODDetail.customFields????????????Key=cameraDegree?????????????????????????????????360?????????????????????*/
//        String iffreed = CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.IFFREED);//????????????
        String isVr = "";

        //??????
        String ifidol = CommonUtil.getCustomField(channelDetail.getCustomFields(), ScreenConstant.IFIDOL);
        String iffreed = CommonUtil.getCustomField(channelDetail.getCustomFields(), ScreenConstant.IFFREED);
        if (null != channelDetail.getPhysicalChannels() && channelDetail.getPhysicalChannels().size() > 0 && null != channelDetail.getPhysicalChannels().get(0).getIsVRContent()) {
            isVr = channelDetail.getPhysicalChannels().get(0).getIsVRContent();
        }
        //?????????????????????????????????
        //????????????
        if (null != ifidol && ifidol.equals("1")) {
            return ifdol;

        } else if (null != iffreed && iffreed.equals("1")) {
            //?????????
            return ifree;
        } else if (!TextUtils.isEmpty(isVr) && isVr.equals("1")) {
            //vr
            return vr;
        } else {
            //????????????
            return normal;
        }
    }

//    public static void showEpiodes(String mVODID, Context context, Handler handler) {
//        vodPlayerControllerView = null;
//        mBrowseEpsiodesUtils.getSimpleVod(mVODID, new VoddetailEpsiodePresenter.GetSimpleVodCallback() {
//            @Override
//            public void getSimpleVodSuccess(VODDetail vodDetail) {
//                vodPlayerControllerView = new VodPlayerControllerView(context, false, false, vodDetail, mBrowseEpsiodesUtils, handler, vodDetail.getPrice(), 1f);
//            }
//
//            @Override
//            public void getSimpleVodFail() {
//
//            }
//        });
//    }
//
//    public static void showView(View ParentView) {
//        if (vodPlayerControllerView != null) {
//            vodPlayerControllerView.showList(ParentView);
//        }
//    }

    /*channelDetail???schedule*/
    private static Schedule setChannelToSchedule(ChannelDetail channelDetail) {
        Schedule schedule = new Schedule();
        schedule.setId(channelDetail.getID());
        schedule.setChannelNo(channelDetail.getChannelNO());
        //??????ID
        String mediaId = "";
        List<Integer> maxList = new ArrayList<>();
        if (channelDetail.getPhysicalChannels() != null
                && channelDetail.getPhysicalChannels().size() > 0) {
            if (channelDetail.getPhysicalChannels().size() == 1) {
                mediaId = channelDetail.getPhysicalChannels().get(0).getID();
            } else {
                for (int i = 0; i < channelDetail.getPhysicalChannels().size(); i++) {
                    maxList.add(Integer.parseInt(channelDetail.getPhysicalChannels().get(i).getDefinition()));
                }
                //??????????????????,?????????????????????
                mediaId = channelDetail.getPhysicalChannels()
                        .get(maxList.indexOf(Collections.max(maxList)))
                        .getID();
            }
            schedule.setMediaID(mediaId);

        }
        return schedule;
    }

    public static void refresh(String sitNum)
    {
        mBrowseEpsiodesUtils.refresh(sitNum);
    }
}
