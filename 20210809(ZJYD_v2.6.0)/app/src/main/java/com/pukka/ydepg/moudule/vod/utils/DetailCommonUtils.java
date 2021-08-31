package com.pukka.ydepg.moudule.vod.utils;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.Genre;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.ProduceZone;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.DeviceInfo;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ScoreControl;
import com.pukka.ydepg.common.utils.SubscriptControl;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.presenter.NewDetailPresenter;

import java.util.Collections;
import java.util.List;

import static com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.VOD_DETAIL_SUPPORT_PREPLAY;

public class DetailCommonUtils {

    private static final String TAG = "NewVodDetailActivity";

    private static String device = getDevice();

    //剧集是否需要名称展示
    public static boolean isShowSerieslayout(String cmsType) {
        boolean isSeries = true;
        List<String> terminalSubjectIds = SessionService.getInstance().getSession().getTerminalConfigurationVodNameSubjectIDS();
        if (null != terminalSubjectIds && terminalSubjectIds.size() > 0) {
            for (int i = 0; i < terminalSubjectIds.size(); i++) {
                if (terminalSubjectIds.get(i).equals(cmsType)) {
                    return false;
                }
            }
        }
        return isSeries;
    }


    //剧集是否需要倒序展示
    public static boolean isShowReverselayout(String cmsType) {
        boolean isSeries = false;
        List<String> terminalSubjectIds = SessionService.getInstance().getSession().getTerminalConfigurationVodReverseSubjectIDS();
        if (terminalSubjectIds != null && terminalSubjectIds.size() > 0) {
            for (int i = 0; i < terminalSubjectIds.size(); i++) {
                if (terminalSubjectIds.get(i).equals(cmsType)) {
                    return true;
                }
            }
        }
        return isSeries;
    }

    //获取影片出品时间和流派
    public static String getVodCategoryInfo(VOD vod){
        String info = "";
        String produceDate = vod.getProduceDate();
        ProduceZone comeFromZome = vod.getProduceZone();
        String comeFrom = "";
        if (!TextUtils.isEmpty(produceDate)) {
            if (produceDate.length() > 4) {
                info = produceDate.substring(0, 4);
            }
        }

        if (null != comeFromZome && !TextUtils.isEmpty(comeFromZome.getName())){
            comeFrom = comeFromZome.getName();
            info = info + "  |  " +comeFrom;
        }


        List<Genre> genres = vod.getGenres();
        if (genres != null && genres.size() != 0) {
            for (int i = 0; i < genres.size(); i++) {
                Genre genre = genres.get(i);
                String genreName = genre.getGenreName();
                Log.i(TAG, "getVodCategoryInfo:  "+ genreName);
                if (!TextUtils.isEmpty(genreName)) {
                    if (i == 0 && !info.endsWith("  |  ")){
                        info = info + "  |  ";
                    }
                    info = info + genreName;
                    if (i != genres.size() - 1){
                        info = info + "  |  ";
                    }
                }
            }
        }
        return info;
    }

    //获取影片的更新进度
    public static Spanned getVodUpdateInfo(VOD vod){
        String maxNum = vod.getMaxSitcomNO();
        String vodNum = vod.getVodNum();
        StringBuffer sb=new StringBuffer();
        if (!TextUtils.isEmpty(maxNum) && !TextUtils.isEmpty(vodNum)) {
            if (Integer.parseInt(vodNum) == Integer.parseInt(maxNum)) {
                sb.append("<strong><font color=\"#f3f6f6\">");
                sb.append("共");
                sb.append(maxNum);
                sb.append((DetailCommonUtils.isShowSerieslayout(vod.getCmsType()) ? "集" : "期"));
                sb.append("</font></strong>");
            } else {
                sb.append("<strong><font color=\"#f3f6f6\">");
                sb.append("更新至");
                sb.append("</font></strong>");
                sb.append("<strong><font color=\"#eac458\">");
                sb.append(maxNum);
                sb.append("</font></strong>");
                sb.append("<strong><font color=\"#f3f6f6\">");
                sb.append((DetailCommonUtils.isShowSerieslayout(vod.getCmsType()) ? "集" : "期"));

                sb.append("/共");
                sb.append(vodNum);
                sb.append((DetailCommonUtils.isShowSerieslayout(vod.getCmsType()) ? "集" : "期"));
                sb.append("</font></strong>");
            }
        }
        return Html.fromHtml(sb.toString());
    }

    //获取影片来源介绍
    public static String getVodInterduce(Context context,VOD vod ){
        String text = "";
        String cp = SessionService.getInstance().getSession().getTerminalConfigurationValue("cpId_" + vod.getCpId());
        if (!TextUtils.isEmpty(cp)) {
            if (TextUtils.isEmpty(text) && !CommonUtil.isJMGODevice()) {
                text = context.getResources().getString(R.string.details_source_data_dot);
            }
//            text += " 本视频由" + cp + "提供内容服务";
            text = text + " 本视频来自"+cp;
        }

//        List<NamedParameter> paramters = vod.getCustomFields();
//        if (null != paramters && paramters.size() != 0) {
//            StringBuffer sb = new StringBuffer(text);
//            for (int i = 0; i < paramters.size(); i++) {
//                if ("vodInfo".equals(paramters.get(i).getKey())) {
//                    String firstValue = paramters.get(i).getFistItemFromValue();
//                    if (!TextUtils.isEmpty(firstValue)) {
//                        sb.append(" " + firstValue);
//                    }
//                }
//            }
//            text = sb.toString();
//        }
        return text;
    }

    //获取更新时间
    public static String getUpdateTime(Context context,VOD vod ){
        List<NamedParameter> paramters = vod.getCustomFields();
        if (null != paramters && paramters.size() != 0) {
            for (int i = 0; i < paramters.size(); i++) {
                if ("vodInfo".equals(paramters.get(i).getKey())) {
                    return paramters.get(i).getFistItemFromValue();
                }
            }
        }
        return "";
    }

    //获取4k提示语
    public static String get4KWarnningtip(Context context){
        String m4KWarnningtip = SessionService.getInstance().getSession().getTerminalConfigurationVod4kWarnningTip();
        if (!TextUtils.isEmpty(m4KWarnningtip)){
            return m4KWarnningtip;
        }else{
            if (canPlayPre()){
                return context.getResources().getString(R.string.details_4k_warnning);
            }else{
                return context.getResources().getString(R.string.details_source_data_dot) + context.getResources().getString(R.string.details_4k_warnning);
            }

        }
    }

    //vod是否需要展示评分
    public static boolean needShowScore(VOD vod){
        if (null != vod) {
            //vod对象时是第一次加载，不展示评分，防止评分先展示后影藏
            if (vod instanceof VODDetail) {
                VODDetail detail = (VODDetail) vod;
                if (ScoreControl.newNeedShowScoreWithCMSType(detail)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    //获取剧集列表，如果需要倒序，反转列表
    public static List<Episode> getEpisodes(List<Episode> episodes, VODDetail detail){
        if (episodes.size() > 1 && Integer.parseInt(episodes.get(0).getSitcomNO()) < Integer.parseInt(episodes.get(1).getSitcomNO()) && DetailCommonUtils.isShowReverselayout(detail.getCmsType())) {
            Collections.reverse(episodes);
        }
        return episodes;
    }

    //获取书签页的剧集
    public static int getSelectEpisode(List<Episode> episodes,VODDetail detail){
        Bookmark bookmark = detail.getBookmark();
        if (bookmark != null) {
            for (int i = 0; i < episodes.size(); i++) {
                Episode episode = episodes.get(i);
                if (bookmark.getSubContentID().equals(episode.getVOD().getID())) {
                    return i;
                }
            }
        }
        return 0;
    }


    //调整布局
    public static ViewGroup.LayoutParams getLayoutParam(int total,ViewGroup.LayoutParams params,Context context){
        if (total> 10) {
            params.height = context.getResources().getDimensionPixelOffset(R.dimen.margin_152);
        } else {
            params.height = context.getResources().getDimensionPixelOffset(R.dimen.margin_80);
        }
        return params;
    }

    //调整布局
    public static ViewGroup.LayoutParams getLayoutParamForVariety(int total,ViewGroup.LayoutParams params,Context context){

        if (total <= 5) {
            params.height = context.getResources().getDimensionPixelOffset(R.dimen.margin_106);
        } else if (8 >= total && total > 5) {
            params.height = context.getResources().getDimensionPixelOffset(R.dimen.margin_152);
        }
        return params;
    }

    //鉴权vod
    public static void authenticateVOd(VODDetail detail, DetailPresenter mDetailPresenter, String lastPlayUrl, String lastPlayID,Context context) {
        detail.setIsSubscribed("1");
        mDetailPresenter.setVODDetail(detail);
        String vodType = detail.getVODType();
        if (vodType.equals("0")) {
            List<VODMediaFile> vodMediaFiles = detail.getMediaFiles();
            if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
                mDetailPresenter.setLastPlayUrl(lastPlayUrl);
                mDetailPresenter.setLastPlayID(lastPlayID);
                mDetailPresenter.playVOD(detail);
            } else {
                if (!detail.getIsSubscribed().equals("1")) {
                    EpgToast.showToast(context, "没有找到资源文件！");
                } else {
                    EpgToast.showToast(context, "播放失败！");
                }
            }
        } else {
            Episode playEpisode = null;
            if (null != mDetailPresenter.getEpisode()) {
                playEpisode = mDetailPresenter.getEpisode();
            } else {
                List<Episode> episodes = detail.getEpisodes();
                Bookmark bookmark = detail.getBookmark();
                if (bookmark != null) {
                    SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + bookmark.getItemID() + ",sitcomNO:" + bookmark.getSitcomNO() + ",breakpoint:" + bookmark.getRangeTime());
                }
                if (episodes != null && episodes.size() != 0) {
                    if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
                        for (Episode episode : episodes) {
                            if (bookmark.getSitcomNO().equals(episode.getSitcomNO())) {
                                playEpisode = episode;
                            }
                        }
                    } else {
                        playEpisode = episodes.get(0);
                    }
                }
            }
            if (null != playEpisode) {
                mDetailPresenter.setLastPlayUrl(lastPlayUrl);
                mDetailPresenter.setLastPlayID(lastPlayID);
                mDetailPresenter.playVOD(playEpisode);
            }
        }
    }

    //鉴权vod
    public static void authenticateVOd(VODDetail detail, NewDetailPresenter mDetailPresenter, String lastPlayUrl, String lastPlayID, Context context) {
        detail.setIsSubscribed("1");
        mDetailPresenter.setVODDetail(detail);
        String vodType = detail.getVODType();
        if (vodType.equals("0")) {
            List<VODMediaFile> vodMediaFiles = detail.getMediaFiles();
            if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
                mDetailPresenter.setLastPlayUrl(lastPlayUrl);
                mDetailPresenter.setLastPlayID(lastPlayID);
                mDetailPresenter.playVOD(detail);
            } else {
                if (!detail.getIsSubscribed().equals("1")) {
                    EpgToast.showToast(context, "没有找到资源文件！");
                } else {
                    EpgToast.showToast(context, "播放失败！");
                }
            }
        } else {
            Episode playEpisode = null;
            if (null != mDetailPresenter.getEpisode()) {
                playEpisode = mDetailPresenter.getEpisode();
            } else {
                List<Episode> episodes = detail.getEpisodes();
                Bookmark bookmark = detail.getBookmark();
                if (bookmark != null) {
                    SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + bookmark.getItemID() + ",sitcomNO:" + bookmark.getSitcomNO() + ",breakpoint:" + bookmark.getRangeTime());
                }
                if (episodes != null && episodes.size() != 0) {
                    if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
                        for (Episode episode : episodes) {
                            if (bookmark.getSitcomNO().equals(episode.getSitcomNO())) {
                                playEpisode = episode;
                            }
                        }
                    } else {
                        playEpisode = episodes.get(0);
                    }
                }
            }
            if (null != playEpisode) {
                mDetailPresenter.setLastPlayUrl(lastPlayUrl);
                mDetailPresenter.setLastPlayID(lastPlayID);
                mDetailPresenter.playVOD(playEpisode);
            }
        }
    }

    //判断当前页面支不支持预播放
    public static boolean  canPlay(boolean is4kResource){
//        if (true){
//            return false;
//        }
        List<String> deviceSupportPrePlay = CommonUtil.getListConfigValue(VOD_DETAIL_SUPPORT_PREPLAY);

        //deviceSupportPrePlay.remove(CommonUtil.getDeviceType());

        if(CollectionUtil.isEmpty(deviceSupportPrePlay)){
            //没有配置，不可播放
            return false;
        }

        if(deviceSupportPrePlay.contains(device)){
            //当前设备支持预播放设备,取决于是否是4k片源
            return VodUtil.canPlay(is4kResource);
        } else {
            //当前设备为不支持预播放设备，不可预播放
            return false;
        }
    }

    public static boolean canPlayPre(){
//        if (true){
//            return false;
//        }
        List<String> deviceSupportPrePlay = CommonUtil.getListConfigValue(VOD_DETAIL_SUPPORT_PREPLAY);

        //deviceSupportPrePlay.remove(CommonUtil.getDeviceType());

        if(CollectionUtil.isEmpty(deviceSupportPrePlay)){
            //没有配置，不可播放
            return false;
        }
        if(deviceSupportPrePlay.contains(device)){
            //当前设备支持预播放设备
            return true;
        } else {
            //当前设备为不支持预播放设备，不可预播放
            return false;
        }
    }



    private static String getDevice(){
        String systemModel = DeviceInfo.getSystemInfo(Constant.DEVICE_RAW);
        if (TextUtils.isEmpty(systemModel) || "unknown".equals(systemModel)) {
            systemModel = Build.MODEL;
        }
        return systemModel;
    }

    //判断当前影片是否是4k片源
    public static boolean is4k(VODDetail vodDetail){
        if (SubscriptControl.iszj4KVOD(vodDetail)){
            return true;
        }

        List<VODMediaFile> vodMediaFileList = null;
        String vodType = vodDetail.getVODType();
        //电视剧角标展示业务
        if ("1".equals(vodType) || "3".equals(vodType)) {
            List<Episode> episodes = vodDetail.getEpisodes();
            if (null != episodes && episodes.size() > 0 && null != episodes.get(0).getVOD()) {
                vodMediaFileList = episodes.get(0).getVOD().getMediaFiles();
            }
        } else {
            //电影角标示业务
            vodMediaFileList = vodDetail.getMediaFiles();
        }

        if (vodMediaFileList != null && vodMediaFileList.size() != 0) {
            String definition = vodMediaFileList.get(0).getDefinition();
            if ("2".equals(definition)) {
                return true;
            }
        }

        return false;
    }
}
