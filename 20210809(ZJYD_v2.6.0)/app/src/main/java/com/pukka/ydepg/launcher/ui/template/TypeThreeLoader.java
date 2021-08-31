package com.pukka.ydepg.launcher.ui.template;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.report.jiutian.JiutianService;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Desktop;
import com.pukka.ydepg.common.report.ubd.scene.UBDSwitch;
import com.pukka.ydepg.common.report.ubd.scene.UBDRecommend;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.uiutil.CpRoute;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.Utils;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;
import com.pukka.ydepg.moudule.mytv.utils.MessageDataHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载group类型为3的数据
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.ui.template.TypeThreeLoader.java
 * @date: 2018-02-01 12:51
 * @version: V1.0 描述当前版本功能
 */
public class TypeThreeLoader extends DataLoaderAdapter<VOD> {
    private static final String TAG = TypeThreeLoader.class.getSimpleName();

    @Override
    public void loadData(TextView title, TextView subtitle,View mLayer, ImageView imageView, VOD vod, ReflectRelativeLayout relativeLayout) {
        Context context = relativeLayout.getContext();
        loadPoster(vod, imageView, relativeLayout);
        loadPosterTitle(vod, title, subtitle,mLayer);
        relativeLayout.setOnClickListener(v -> {
            SuperLog.debug(TAG, "vod" + vod.getName());
            if (MessageDataHolder.get().getIsMixVideoPostImage()) {
                if (v.getParent().getParent() instanceof MixVideoTemplate) {
                    ((MixVideoTemplate) (v.getParent().getParent())).onToPlayFullScreenClick();
                }
            } else {
                if (CpRoute.isCp(vod.getCpId(),vod.getCustomFields())) {
                    CpRoute.goCp(vod.getCustomFields());
                } else {
                    if (vod.isProfileVod()){
                        //判断Vod中的custom Fields是否含有contentType,且不为空
                        if (Utils.hasH5UrlOrContentTypeForCustomField(vod.getCustomFields())){
                        //if (Utils.hasH5UrlOrContentTypeForCustomField(vod.getCustomFields())){
                            //TODO 爱看页面支持配置apk、h5、二级页面普通运营位
                            ZJVRoute.route(context, ZJVRoute.LauncherElementDataType.STATIC_ITEM,Utils.getActionUrl(vod.getCustomFields()),null, null, null,Utils.getExtraData(vod.getCustomFields()));
                        }else{
                            //不含有contentType跳转到vod详情页
                            ZJVRoute.route(context, ZJVRoute.LauncherElementDataType.VOD, null, null, vod.getID(), vod, null);
                        }
                    }else{
                        ZJVRoute.route(context, ZJVRoute.LauncherElementDataType.VOD, null, null, vod.getID(), vod, null);
                    }
                }
            }

            //if (relativeLayout.getIsRecommend()) {
            UBDSwitch.getInstance().getMainExtensionField().setRecommendType(relativeLayout.getRecommendTyp());
            UBDSwitch.getInstance().getMainExtensionField().setSceneId(relativeLayout.getSceneId());
            UBDSwitch.getInstance().getMainExtensionField().setAppointedId(relativeLayout.getAppointedId());
            //}

            //上报UBD
            UBDSwitch.getInstance().recordMainOnclick(relativeLayout.getActionUrl(), relativeLayout.getElement(), relativeLayout.getGroup(), vod,null);

            if (relativeLayout.getIsRecommend()){
                if (!TextUtils.isEmpty(relativeLayout.getClickTrackerUrl())){
                    JiutianService.reportClick(relativeLayout.getClickTrackerUrl());
                }
                PbsUaService.report(Desktop.getRecommendData(PbsUaConstant.ActionType.CLICK,relativeLayout.getElement().getRecommendId(),relativeLayout.getElement().getAppointedId(),
                        relativeLayout.getElement().getRecommendType(),relativeLayout.getElement().getSceneId(),relativeLayout.getElement().getRecommendId(),relativeLayout.getElement().getRecommendType()));
            }
        });
    }

    /**
     * 加载海报图片
     *
     * @param vod
     * @param imageView
     */
    private void loadPoster(VOD vod, ImageView imageView, ReflectRelativeLayout relativeLayout) {
        imageView.setVisibility(View.VISIBLE);
        String imgUrl = null;
        try {
            //判断横版竖版，横版和竖版数据源不同。
            Picture picture = vod.getPicture();
            SuperLog.debug(TAG, vod.getName() + relativeLayout.getPosterType());
            if (picture != null) {
                if (relativeLayout.getPosterIsAlienType() == true && null != picture.getExtensionFields()
                        && picture.getExtensionFields().size() > 0) {//异形海报
                    List<NamedParameter> secondaryWides = picture.getExtensionFields();
                    imgUrl = secondaryWides.get(0).getValues().get(0);
                } else if (relativeLayout.getPosterType() == relativeLayout.BANNER) {  //4.437banner,banner先去others取取不到再去ads取
                    List<String> others = picture.getOthers();
                    if (others != null && others.size() > 0) {
                        imgUrl = others.get(0);
                    } else {
                        List<String> adList = picture.getAds();
                        if (adList != null && adList.size() > 0) {
                            imgUrl = adList.get(0);
                        }
                    }
                } else if (relativeLayout.getPosterType() == relativeLayout.HORIZONTAL) {  //横版1.53
                    List<String> channelBlackWhites = picture.getChannelBlackWhites();
                    if (channelBlackWhites != null && channelBlackWhites.size() > 0) {
                        imgUrl = channelBlackWhites.get(0);
                    }else{
                        List<String> adList = picture.getAds();
                        if (adList != null && adList.size() > 0) {
                            imgUrl = adList.get(0);
                        }
                    }
                } else if (relativeLayout.getPosterType() == relativeLayout.VERTICAL) {//0.73
                    //竖版
                    //竖版先从Titles去取
                    List<String> titleList = picture.getTitles();
                    if (titleList != null && titleList.size() > 0) {
                        imgUrl = titleList.get(0);
                    }
                } else if (relativeLayout.getPosterType() == relativeLayout.HORIZONTAL_WIDE) {//5.68
                    List<String> mainWidesList = picture.getMainWides();
                    if (mainWidesList != null && mainWidesList.size() > 0) {
                        imgUrl = mainWidesList.get(0);
                    }
                }else if (relativeLayout.getPosterType() == relativeLayout.HORIZONTAL_TWO) {//2.33
                    List<String> channelNamePics = picture.getChannelNamePics();
                    if (channelNamePics != null && channelNamePics.size() > 0) {
                        imgUrl = channelNamePics.get(0);
                    }else{
                        List<String> adList = picture.getAds();
                        if (adList != null && adList.size() > 0) {
                            imgUrl = adList.get(0);
                        }
                    }
                }/* else {
                    //宽版横版
                    List<String> deflates = picture.getDeflates();
                    if (deflates != null && deflates.size() > 0) {
                        imgUrl = deflates.get(0);
                    }
                    //缩略图取不到去广告图
                    if (TextUtils.isEmpty(imgUrl)) {
                        List<String> ads = picture.getAds();
                        if (null != ads && ads.size() > 0) {
                            imgUrl = ads.get(0);
                        }
                    }
                }*/

                //如果上面都没取到，则去posters去取
                if (TextUtils.isEmpty(imgUrl)) {
                    //DebugLog.debug(TAG, "海报");
                    List<String> posterList = picture.getPosters();
                    if (posterList != null && posterList.size() > 0) {
                        imgUrl = posterList.get(0);
                    }
                }
                //如果上面都没取到，展示本地图片
                if (!TextUtils.isEmpty(imgUrl)) {
                    SuperLog.debug(TAG, "imageUrl:" + imgUrl);
                    relativeLayout.loadPosterUrl(imgUrl, imageView);
                } else {
                    relativeLayout.loadDefaultPic(imageView);
                }
            } else {
                relativeLayout.loadDefaultPic(imageView);
            }
        } catch (Exception e) {
            relativeLayout.loadDefaultPic(imageView);
            SuperLog.error(TAG, e);
        }
    }

    /**
     * 加载海报文字内容
     *
     * @param vod
     * @param textView1
     * @param textView2
     */

    private void loadPosterTitle(VOD vod, TextView textView1, TextView textView2, View mLayer) {
        int vodType = Integer.valueOf(vod.getVODType());
        if (vodType == 0) {
            if (textView1 != null) {
                textView1.setText(vod.getName());
            }
            if (textView2 != null) {
                textView2.setVisibility(View.GONE);
            }
        } else if (vodType != 0) {
            if (null != textView1) {
                textView1.setText(vod.getName());
            }
            if (null == textView2) {
                return;
            }
            textView2.setVisibility(View.VISIBLE);

            String tx;
            if (null != vod.getSubjectIDs()) {
                String vodType1 = getVodType(vod.getSubjectIDs());
                if ("series".equals(vodType1)) {
                    tx = "集";
                } else {
                    tx = "期";
                }
            } else {
                tx = "集";
            }
            if (vod.getVodNum() != null && vod.getMaxSitcomNO() != null) {
                //总集数
                int vodNum = Integer.valueOf(vod.getVodNum());
                //最新子集的集号
                int maxSitcomNO = Integer.valueOf(vod.getMaxSitcomNO());
                if (vodNum == maxSitcomNO) {
                    textView2.setText("全" + vod.getVodNum() + tx);
                } else {
                    textView2.setText("更新至第" + vod.getMaxSitcomNO() + tx);
                }
            } else if (vod.getVodNum() != null && vod.getMaxSitcomNO() == null) {
                //总集数
                textView2.setText("全" + vod.getVodNum() + tx);
            } else if (vod.getVodNum() == null && vod.getMaxSitcomNO() != null) {
                textView2.setText("更新至第" + vod.getMaxSitcomNO() + tx);
            } else {
                //隐藏集数
                textView2.setVisibility(View.GONE);
            }
        }

        //首页加载动态数据，没有标题时隐藏资源位底部阴影
        if ((null == textView1 || TextUtils.isEmpty(vod.getName())) && (null == textView2 || textView2.getVisibility() != View.VISIBLE) && null != mLayer){
            mLayer.setVisibility(View.GONE);
        }
    }

    private String getVodType(List<String> subjectIds) {
        List<String> terminalSubjectIds = SessionService.getInstance().getSession().getTerminalConfigurationSubjectIDS();
        if (null != subjectIds && subjectIds.size() != 0) {
            for (int i = 0; i < terminalSubjectIds.size(); i++) {
                for (int j = 0; j < subjectIds.size(); j++) {
                    if (terminalSubjectIds.get(i).equals(subjectIds.get(j))) {
                        return "series";
                    }
                }
            }
        }
        return "variety";
    }
}
