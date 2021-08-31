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
 * 作者：panjw on 2021/6/18 11:12
 * <p>
 * 邮箱：panjw@easier.cn
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
            //普通片源，用本地播放器播放
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
                                //多机位
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
                                //1 直播 2 点播
                                allVideoConfig.setMediaType(2);
                                Gson gson = new Gson();
                                //自由视角
                                Intent intent = new Intent();
                                intent.setClass(activity, TVPlayActivity.class);
                                intent.putExtra(PlayConstant.EXTRA, gson.toJson(allVideoConfig));
                                if (isEpsiodes) {
                                    intent.putExtra("CONTENTID", mVODDetail.getID());
                                }
                                activity.startActivity(intent);

                            } else if (getMultType(mVODDetail, voddetailEpsiodesUtils) == ifree) {
                                //自由视角
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
                                //1 直播 2 点播
                                allVideoConfig.setMediaType(2);
                                allVideoConfig.setContentID(mVODDetail.getID());
                                allVideoConfig.setReturnURL("");
                                Gson gson = new Gson();
                                //自由视角
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
                                //判断逻辑：
                                //1.根据字段dimension判断是2D还是3D
                                // dimension == 2D -- 2D
                                // dimension == 3D -- 3D
                                //2.如果是2D，在根据isVRContent, viewDegree判断是2D_180还是2D_360:
                                // isVRContent == 1 && viewDegree == 2 -- 2D_180
                                // isVRContent == 1 && viewDegree == 1 -- 2D_360
                                //3.如果是3D，首先根据isVRContent, viewDegree判断是180 还是 360
                                // isVRContent == 1 && viewDegree == 2 -- 2D_180 -> formatOf3D == 1 --> PlayerType3D_LR_180
                                // isVRContent == 1 && viewDegree == 2 -- 2D_180 -> formatOf3D == 2 --> PlayerType3D_UD_180
                                // ----------------------------------------------------------------------------------------
                                // isVRContent == 1 && viewDegree == 1 -- 2D_180 -> formatOf3D == 1 --> PlayerType3D_LR_360
                                // isVRContent == 1 && viewDegree == 1 -- 2D_180 -> formatOf3D == 2 --> PlayerType3D_UD_360


                                /**
                                 * 视频类型
                                 * 3: VR180 的 2D 视频;
                                 * 4: VR360 的 2D 视频;
                                 * 5: VR180 的 3D 视频;
                                 * 6: VR360 的 3D 视频;
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
                                                //左右
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
                        //多机位
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
                        //1 直播 2 点播
                        allVideoConfig.setMediaType(2);
                        Gson gson = new Gson();
                        //自由视角
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
            EpgToast.showToast(OTTApplication.getContext(),"您使用的设备暂不支持播放该内容");
        }
        return false;
    }

    private static int getMultType(VODDetail mVODDetail, VoddetailEpsiodesUtils voddetailEpsiodesUtils) {

        /* * 1、在VOD.customFields里面增加Key=ifIdol， 用于标识视频是否为多视角视频，可选值为0/1，默认值为0
         * 1表示视频为多视角视频，0表示视频为普通视频。如果该项没有配置，默认为普通视频。
         * 2、机位对应的海报配置在VOD.Picture.others[]中。
         * 3、在VODMediaFile.customFields里面增加Key=cameraInfo,用于标识机位的类型为特性机位或普通多机位。配置值为“机位类型,机位海报对应索引”。
         * 机位类型：1表示普通多机位，2表示特征机位。
         * 机位海报对应索引：对应VOD.Picture.others[index]中对应的图片
         * 比如配置“1 0”，表示当前媒资为普通多机位，其对应的海报是Picture.others[0]中的图片地址。
         * 4、【扩展】VODMediaFile.customFields里面增加Key=cameraPosition，对应的是普通多机位的方位，可选值为0~12，其中0表示为默认机位，1~12表示相对于0位置的时钟位置。*/
//        String ifidol = CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.IFIDOL);//多视角
        String ifidol = "";//多视角

                /*1、在VODDetail.customFields里面增加Key=ifFreeD， 用于标识是否支持自由视角，可选值为0/1，默认值为0r
                0表示视频为普通视频,1自由视角。如果该项没有配置，默认为普通视频。
                2、在VODDetail.customFields里面增加Key=cameraNum,用于标识机位数量，机位编号从1开始，比如有36路视角，value是36，编号从1~36。
                3、在VODDetail.customFields里面增加Key=mainCameraId，表示主机位ID
                4、在VODDetail.customFields里面增加Key=cameraDegree，表示机位环绕度数，如360即为完整圆周。*/
//        String iffreed = CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.IFFREED);//自由视角
        String iffreed = "";//自由视角
        String isVr = "";

        if (mVODDetail.getVODType().equals("0") || mVODDetail.getVODType().equals("2")) {
            isEpsiodes = false;
            //电影
            ifidol = CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.IFIDOL);
            iffreed = CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.IFFREED);
            if (null != mVODDetail.getMediaFiles() && mVODDetail.getMediaFiles().size() > 0 && null != mVODDetail.getMediaFiles().get(0).getIsVRContent()) {
                isVr = mVODDetail.getMediaFiles().get(0).getIsVRContent();
            }
        } else {
            isEpsiodes = true;
            //电视剧
            if (null != voddetailEpsiodesUtils && voddetailEpsiodesUtils.getSelesctedEpisode() != null) {
                //电影
                ifidol = CommonUtil.getCustomField(voddetailEpsiodesUtils.getSelesctedEpisode().getVOD().getCustomFields(), ScreenConstant.IFIDOL);
                iffreed = CommonUtil.getCustomField(voddetailEpsiodesUtils.getSelesctedEpisode().getVOD().getCustomFields(), ScreenConstant.IFFREED);
                if (null != voddetailEpsiodesUtils.getSelesctedEpisode().getVOD().getMediaFiles() && voddetailEpsiodesUtils.getSelesctedEpisode().getVOD().getMediaFiles().size() > 0 && null != voddetailEpsiodesUtils.getSelesctedEpisode().getVOD().getMediaFiles().get(0).getIsVRContent()) {
                    isVr = voddetailEpsiodesUtils.getSelesctedEpisode().getVOD().getMediaFiles().get(0).getIsVRContent();
                }
            }
        }
        //在此判断需要播放的类型
        //自由视角
        if (null != ifidol && ifidol.equals("1")) {
            return ifdol;

        } else if (null != iffreed && iffreed.equals("1")) {
            //多机位
            return ifree;
        } else if (!TextUtils.isEmpty(isVr) && isVr.equals("1")) {
            //vr
            return vr;
        } else {
            //普通播放
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
                        //多机位
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
                        //1 直播 2 点播
                        allVideoConfig.setMediaType(1);
                        Gson gson = new Gson();
                        //自由视角
                        Intent intent = new Intent();
                        intent.setClass(context, TVPlayActivity.class);
                        intent.putExtra(PlayConstant.EXTRA, gson.toJson(allVideoConfig));
                        if (isEpsiodes) {
                            intent.putExtra("CONTENTID", channelDetail.getID());
                        }
                        context.startActivity(intent);

                    } else if (getChannelMultType(channelDetail) == ifree) {
                        //自由视角
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
                        //1 直播 2 点播
                        allVideoConfig.setMediaType(2);
                        allVideoConfig.setContentID(channelDetail.getID());
                        allVideoConfig.setReturnURL("");
                        Gson gson = new Gson();
                        //自由视角
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
                        //判断逻辑：
                        //1.根据字段dimension判断是2D还是3D
                        // dimension == 2D -- 2D
                        // dimension == 3D -- 3D
                        //2.如果是2D，在根据isVRContent, viewDegree判断是2D_180还是2D_360:
                        // isVRContent == 1 && viewDegree == 2 -- 2D_180
                        // isVRContent == 1 && viewDegree == 1 -- 2D_360
                        //3.如果是3D，首先根据isVRContent, viewDegree判断是180 还是 360
                        // isVRContent == 1 && viewDegree == 2 -- 2D_180 -> formatOf3D == 1 --> PlayerType3D_LR_180
                        // isVRContent == 1 && viewDegree == 2 -- 2D_180 -> formatOf3D == 2 --> PlayerType3D_UD_180
                        // ----------------------------------------------------------------------------------------
                        // isVRContent == 1 && viewDegree == 1 -- 2D_180 -> formatOf3D == 1 --> PlayerType3D_LR_360
                        // isVRContent == 1 && viewDegree == 1 -- 2D_180 -> formatOf3D == 2 --> PlayerType3D_UD_360


                        /**
                         * 视频类型
                         * 3: VR180 的 2D 视频;
                         * 4: VR360 的 2D 视频;
                         * 5: VR180 的 3D 视频;
                         * 6: VR360 的 3D 视频;
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
                                        //左右
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

            //鉴权request
            PlayChannelRequest playChannelRequest = new PlayChannelRequest();
            playChannelRequest.setChannelID(playbillInfo.getChannelId());
            //节目单ID
            String playbillID = playbillInfo.getPlaybillId();
            if (!TextUtils.isEmpty(playbillID) && !TextUtils.isEmpty(playbillInfo.getChannelProgramName())) {
                //节目单ID
                playChannelRequest.setPlaybillID(playbillID);
            }
            playChannelRequest.setMediaID(playbillInfo.getChannelMediaId());
            playChannelRequest.setBusinessType(V6Constant.ChannelURLType.BTV);
            playChannelRequest.setURLFormat("1");//HLS
            RxAppCompatActivity rxAppCompatActivity = (RxAppCompatActivity) context;
            multChannelPlayUtils.playChannel(playChannelRequest, rxAppCompatActivity, context);
        }else
        {
            EpgToast.showToast(OTTApplication.getContext(),"您使用的设备暂不支持播放该内容");
        }

        return false;
    }

    private static int getChannelMultType(ChannelDetail channelDetail) {
        /* * 1、在VOD.customFields里面增加Key=ifIdol， 用于标识视频是否为多视角视频，可选值为0/1，默认值为0
         * 1表示视频为多视角视频，0表示视频为普通视频。如果该项没有配置，默认为普通视频。
         * 2、机位对应的海报配置在VOD.Picture.others[]中。
         * 3、在VODMediaFile.customFields里面增加Key=cameraInfo,用于标识机位的类型为特性机位或普通多机位。配置值为“机位类型,机位海报对应索引”。
         * 机位类型：1表示普通多机位，2表示特征机位。
         * 机位海报对应索引：对应VOD.Picture.others[index]中对应的图片
         * 比如配置“1 0”，表示当前媒资为普通多机位，其对应的海报是Picture.others[0]中的图片地址。
         * 4、【扩展】VODMediaFile.customFields里面增加Key=cameraPosition，对应的是普通多机位的方位，可选值为0~12，其中0表示为默认机位，1~12表示相对于0位置的时钟位置。*/
//        String ifidol = CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.IFIDOL);//多视角

                /*1、在VODDetail.customFields里面增加Key=ifFreeD， 用于标识是否支持自由视角，可选值为0/1，默认值为0r
                0表示视频为普通视频,1自由视角。如果该项没有配置，默认为普通视频。
                2、在VODDetail.customFields里面增加Key=cameraNum,用于标识机位数量，机位编号从1开始，比如有36路视角，value是36，编号从1~36。
                3、在VODDetail.customFields里面增加Key=mainCameraId，表示主机位ID
                4、在VODDetail.customFields里面增加Key=cameraDegree，表示机位环绕度数，如360即为完整圆周。*/
//        String iffreed = CommonUtil.getCustomField(mVODDetail.getCustomFields(), ScreenConstant.IFFREED);//自由视角
        String isVr = "";

        //电影
        String ifidol = CommonUtil.getCustomField(channelDetail.getCustomFields(), ScreenConstant.IFIDOL);
        String iffreed = CommonUtil.getCustomField(channelDetail.getCustomFields(), ScreenConstant.IFFREED);
        if (null != channelDetail.getPhysicalChannels() && channelDetail.getPhysicalChannels().size() > 0 && null != channelDetail.getPhysicalChannels().get(0).getIsVRContent()) {
            isVr = channelDetail.getPhysicalChannels().get(0).getIsVRContent();
        }
        //在此判断需要播放的类型
        //自由视角
        if (null != ifidol && ifidol.equals("1")) {
            return ifdol;

        } else if (null != iffreed && iffreed.equals("1")) {
            //多机位
            return ifree;
        } else if (!TextUtils.isEmpty(isVr) && isVr.equals("1")) {
            //vr
            return vr;
        } else {
            //普通播放
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

    /*channelDetail转schedule*/
    private static Schedule setChannelToSchedule(ChannelDetail channelDetail) {
        Schedule schedule = new Schedule();
        schedule.setId(channelDetail.getID());
        schedule.setChannelNo(channelDetail.getChannelNO());
        //媒体ID
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
                //高清标清标识,取高清度最大的
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
