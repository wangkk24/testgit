package com.pukka.ydepg.moudule.vod.cache;

import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.children.view.ParentSetCenterManagerView;
import com.pukka.ydepg.moudule.search.bean.SearchDataClassify;
import com.pukka.ydepg.moudule.search.bean.SearchSubjectBean;
import com.pukka.ydepg.moudule.vod.presenter.BlockingEvent;
import com.pukka.ydepg.service.NtpTimeService;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



/**
 * Created by liudo on 2019/4/11.
 */

public class VoddetailUtil {

    public static final String TAG="VoddetailUtil";

    public static  final String REST_BY_EPSIODE="rest_by_epsiode";

    public static final String  IS_OPEN_EPSIODE_PLAY="is_open_epsiode_play";


    public static  final String REST_BY_SINGLE="rest_by_single_time";

    public static final String REST_BY_TODAY="rest_by_today";


    public static final String REST_SETTING_SINGLE="rest_setting_single";

    public static final String REST_SETTING_TODAY="rest_setting_today";

    public static final String REST_SETTING_ALL="rest_setting_all_open";

    private static  VoddetailUtil instance;

    private long recordIntervalTime=1000;

    private long writeIntervalTime=30*1000L;

    /**
     * 本集一共时间
     */
    private long epsiodeAllTime;

    /**
     * 是否是本集播放完
     */
    private boolean isEpsiodeRest;


    /**
     * 是否是本集播放完临时状态
     */
    private boolean isEpsiodeRestTemp;


    private EpisodePollingListener  mEpisodePollingListener;

    /**
     * 当前的单次已用时长
     */
    private String currentSingleTime="0";
    /**
     * 过去当前的单次已用时长
     */
    private String oldSingleTime;
    /**
     * 当前一天已用时长
     */
    private String currentAllTime="0";

    /**
     * 过去当前一天已用时长
     */
    private String oldAllTime = "0";


    public interface  EpisodePollingListener{
        void handleEpisode(long episodeTime);
        void stopEpisode();
    }


    public void setEpisodePollingListener(EpisodePollingListener mEpisodePollingListener)
    {
        this.mEpisodePollingListener = mEpisodePollingListener;
    }

    public boolean isEpsiodeRestTemp()
    {
        return isEpsiodeRestTemp;
    }

    public void setEpsiodeRestTemp(boolean epsiodeRestTemp)
    {
        isEpsiodeRestTemp = epsiodeRestTemp;
    }

    /**
     * 本集已经播放时间
     */
    private long  epsiodePlayedTime;


    private long  restTime=30*60*1000l;

    private String currentYesterday;

    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");


    private Timer epsiodeTimer=new Timer(true);

    private Timer singleTimer=new Timer(true);

    private Timer todayTimer=new Timer(true);

    public boolean isEpsiodeRest()
    {
        return isEpsiodeRest;
    }

    public void setEpsiodeRest(boolean epsiodeRest)
    {
        isEpsiodeRest = epsiodeRest;
        if(!isEpsiodeRest){
            epsiodeAllTime=0;
            epsiodePlayedTime=0;
        }
    }




    public long getEpsiodePlayTime()
    {
        return epsiodeAllTime;
    }

    public void setEpsiodeAllTime(long epsiodePlayTime)
    {
        Log.e(TAG,"setEpsiodeAllTime->epsiodeAllTime:"+epsiodeAllTime);
        epsiodeAllTime = epsiodePlayTime*1000;
    }

    /**
     * 获得锁屏剩余时间
     * @return
     */
    public long getRestTime()
    {
        //重置计时器
        String today=sdf.format(new Date(NtpTimeService.queryNtpTime()));
        if(TextUtils.isEmpty(currentYesterday)||!today.equals(currentYesterday)) {
            String yesterday = SharedPreferenceUtil.getInstance().getYesterdayTime();
            SuperLog.debug(TAG, "yesterday:" + yesterday + "|today:" + today + "|ntptime:" + NtpTimeService.queryNtpTime());
            if (TextUtils.isEmpty(yesterday) || !today.equals(yesterday)) {
                if (VoddetailUtil.REST_BY_TODAY.equals(SharedPreferenceUtil.getInstance().getlockScreenType())) {
                    VoddetailUtil.getInstance().stopRestTime(SharedPreferenceUtil.getInstance().getlockScreenType());
                }
                SharedPreferenceUtil.getInstance().setCurrentAllTime(0);
                SharedPreferenceUtil.getInstance().setYesterdayTime(today);
                currentYesterday=today;
            }
        }

        /**
         * 如果是今天已完结就一直锁着
         */
        if(REST_BY_TODAY.equals(SharedPreferenceUtil.getInstance().getlockScreenType())){
            return 1;
        }else{
            String  locktime= SharedPreferenceUtil.getInstance().getLockScreenPoint();
            SuperLog.debug(TAG,"锁屏剩余时间--locktime is:"+locktime);
            if(TextUtils.isEmpty(locktime)||"0".equals(locktime)){
                return 0;
            }
            return  restTime-(NtpTimeService.queryNtpTime()-Long.parseLong(locktime));
        }

    }



    private VoddetailUtil(){


    }

    public static VoddetailUtil getInstance(){
        if(null==instance){
            instance=new VoddetailUtil();
        }
        return instance;
    }


    public void  initDate(){
        currentAllTime=SharedPreferenceUtil.getInstance().getCurrentAllTime();
        currentSingleTime=SharedPreferenceUtil.getInstance().getCurrentSingleTime();
        oldAllTime=currentAllTime;
        oldSingleTime=currentSingleTime;
    }


    public boolean  isCloseChildLock(){
        HashMap<String, String> mDataMap = SharedPreferenceUtil.getInstance().getParentCenterData();
        if(null==mDataMap){
            return true;
        }
        String switchStr=mDataMap.get(ParentSetCenterManagerView.SWITCH);
        if(TextUtils.isEmpty(switchStr)){
            return true;
        }
        int lock=Integer.parseInt(switchStr);
        return lock!=1;
    }


    /**
     * 今天是否还能播放
     * @return
     */
    public  boolean isCanPlayToday(){
        if(isCloseChildLock()){

            resetSingleTime();
            resetAllTime();
            return true;
        }
        HashMap<String, String> mDataMap = SharedPreferenceUtil.getInstance().getParentCenterData();
        if(null==mDataMap){
            return true;
        }
        String alltotalStr=mDataMap.get(ParentSetCenterManagerView.ALLTIME);
        if(!TextUtils.isEmpty(currentAllTime)&&!TextUtils.isEmpty(alltotalStr)){
            if(Integer.parseInt(alltotalStr)>0)
            {
                long currentPlayed = Long.parseLong(currentAllTime);
                long all = Long.parseLong(alltotalStr) * 60 * 1000;
                SuperLog.debug(TAG, "isCanPlayToday->currentPlayed:" + currentPlayed + "|alltime:" + all);
                return currentPlayed < all;
            }else{
                resetAllTime();
            }
        }
        return true;
    }


    /**
     * 单次是否还能播放
     * @return
     */
    public  boolean isCanPlaySingle(){
        if(isCloseChildLock()){
            resetSingleTime();
            resetAllTime();
            return true;
        }

        HashMap<String, String> mDataMap = SharedPreferenceUtil.getInstance().getParentCenterData();
        if(null==mDataMap){
            return true;
        }
        String singletotalStr=mDataMap.get(ParentSetCenterManagerView.SINGLETIME);
        if(!TextUtils.isEmpty(currentSingleTime)&&!TextUtils.isEmpty(singletotalStr)){
            if(Integer.parseInt(singletotalStr)>0)
            {
                long currentPlayed = Long.parseLong(currentSingleTime);
                long all = Long.parseLong(singletotalStr) * 60 * 1000;
                SuperLog.debug(TAG, "isCanPlaySingle->currentPlayed:" + currentPlayed + "|alltime:" + all);
                return currentPlayed < all;
            }else{
                resetSingleTime();
            }
        }
        return true;
    }


    public boolean isEspiodeCanPlay(){
        SuperLog.debug(TAG,"isEspiodeCanPlay->currentPlayed:"+epsiodePlayedTime+"|alltime:"+epsiodeAllTime);
        return epsiodePlayedTime<=epsiodeAllTime;
    }

    /**
     * 开始休息
     */
    public void startRestTimeDecrement(String type){
        SharedPreferenceUtil.getInstance().setLockScreenPoint(String.valueOf(NtpTimeService.queryNtpTime()));
        SharedPreferenceUtil.getInstance().setLockScreenType(type);
    }

    /**
     * 重置本集休息
     */
    public void resetEpsiodeTime() {
        epsiodePlayedTime = 0;
        epsiodeAllTime = 0;
        isEpsiodeRest = false;
    }

    /**
     * 重置单次时间
     */
    public void resetSingleTime(){
        if(TextUtils.isEmpty(currentSingleTime)||!currentSingleTime.equals("0")) {
            currentSingleTime = "0";
            oldSingleTime="0";
            SharedPreferenceUtil.getInstance().setCurrentSingleTime(0);
        }

    }

    /**
     * 重置一天时间
     */
    public void resetAllTime(){
        if(TextUtils.isEmpty(currentAllTime)||!currentAllTime.equals("0")) {
            currentAllTime="0";
            oldAllTime="0";
            SharedPreferenceUtil.getInstance().setCurrentAllTime(0);
        }
    }

    /**
     * 暂停休息
     */
    public void stopRestTime(String type){
        SuperLog.debug(TAG,"stopRestTime->type:"+type);
        SharedPreferenceUtil.getInstance().setLockScreenPoint("0");
        if(REST_BY_TODAY.equals(type)){
            resetAllTime();
            resetSingleTime();
        }else if(REST_BY_SINGLE.equals(type)){
            resetSingleTime();
            epsiodePlayedTime=0;
        }else if(REST_BY_EPSIODE.equals(type)){
            epsiodePlayedTime=0;
            epsiodeAllTime=0;
        }
        SharedPreferenceUtil.getInstance().setLockScreenType("");
    }

    /**
     * 本集播放开始计时
     */
    public void startEpsiodeRecordTime(){
        stopEpsiodeRecordTime();
        if(null==epsiodeTimer){
            epsiodeTimer=new Timer(true);
        }
        epsiodeTimer.schedule(new TimerTask() {
            @Override
            public void run()
            {
                epsiodePlayedTime=epsiodePlayedTime+recordIntervalTime;
                if(!isEspiodeCanPlay()){
                    EventBus.getDefault().post(new BlockingEvent(REST_BY_EPSIODE));
                }else{
                    if(null!=mEpisodePollingListener){
                        mEpisodePollingListener.handleEpisode(epsiodeAllTime-epsiodePlayedTime);
                    }
                }

            }
        },0,recordIntervalTime);
    }

    /**
     * 本集播放停止计时
     */
    public void stopEpsiodeRecordTime(){
        if(null!=epsiodeTimer){
            epsiodeTimer.cancel();
            epsiodeTimer=null;
            if(null!=mEpisodePollingListener){
                mEpisodePollingListener.stopEpisode();
            }
        }
    }

    /**
     * 开始单次播放计时
     */
    public void startSingleRecordTime(){
        SuperLog.debug(TAG,"startSingleRecordTime");
        if(isCloseChildLock()){
            return;
        }
        stopSingleRecordTime();
        if(null==singleTimer){
            singleTimer=new Timer(true);
        }
        singleTimer.schedule(new TimerTask() {
            @Override
            public void run()
            {
                if(!TextUtils.isEmpty(currentSingleTime)){
                    currentSingleTime=String.valueOf(Long.parseLong(currentSingleTime)+recordIntervalTime);
                    long interval= Long.parseLong(currentSingleTime)-Long.parseLong(oldSingleTime);
                    if(interval>writeIntervalTime) {
                        SharedPreferenceUtil.getInstance().setCurrentSingleTime(Long.parseLong(currentSingleTime));
                        oldSingleTime=currentSingleTime;
                    }
                    SuperLog.debug(TAG,"*****startSingleRecordTime******");
                    if(!isCanPlaySingle()){
                        SharedPreferenceUtil.getInstance().setCurrentSingleTime(Long.parseLong(currentSingleTime));
                        EventBus.getDefault().post(new BlockingEvent(REST_BY_SINGLE));
                    }
                }
            }
        },0,recordIntervalTime);
    }

    /**
     * 结束单次播放计时
     */
    public void stopSingleRecordTime(){
        if(null!=singleTimer){
            singleTimer.cancel();
            singleTimer=null;
        }
    }


    /**
     * 开始今天播放计时
     */
    public void startAllRecordTime(){
        SuperLog.debug(TAG,"startAllRecordTime");
        if(isCloseChildLock()){
            return;
        }
        stopAllRecordTime();
        if(null==todayTimer){
            todayTimer=new Timer(true);
        }
        todayTimer.schedule(new TimerTask() {
            @Override
            public void run()
            {
                if(!TextUtils.isEmpty(currentAllTime)){
                    currentAllTime=String.valueOf(Long.parseLong(currentAllTime)+recordIntervalTime);
                    long interval=Long.parseLong(currentAllTime)-Long.parseLong(oldAllTime);
                    if(interval>writeIntervalTime) {
                        SharedPreferenceUtil.getInstance().setCurrentAllTime(Long.parseLong(currentAllTime));
                        oldAllTime=currentAllTime;
                    }
                    SuperLog.debug(TAG,"*****startAllRecordTime******");
                    if(!isCanPlayToday()){
                        SharedPreferenceUtil.getInstance().setCurrentAllTime(Long.parseLong(currentAllTime));
                        EventBus.getDefault().post(new BlockingEvent(REST_BY_TODAY));
                    }
                }
            }
        },0,recordIntervalTime);
    }

    /**
     * 结束单次播放计时
     */
    public void stopAllRecordTime(){
        SuperLog.debug(TAG,"stopAllRecordTime");
        if(null!=todayTimer){
            todayTimer.cancel();
            todayTimer=null;
        }
    }


    public boolean  isChildVod(VODDetail vodDetail){
        boolean isChildMode=false;
        SearchDataClassify<SearchSubjectBean> subjectClassify = SessionService.getInstance().getSession().getTerminalConfigurationSearchSubjects();
        List<SearchSubjectBean> subjectBeans= subjectClassify.getChild();
        if(null!=subjectBeans&&subjectBeans.size()>0)
        {
            List<String> filterSubjects=null;
            for(int i=0;i<subjectBeans.size();i++){
                filterSubjects = subjectBeans.get(i).getFilterSubjectIDs();
                if(null!=filterSubjects&&filterSubjects.size()>0){
                    break;
                }
            }
            if (null != filterSubjects && filterSubjects.size() > 0 && null != vodDetail && null != vodDetail.getSubjectIDs() && vodDetail.getSubjectIDs().size() > 0)
            {
                for (int i = 0; i < filterSubjects.size(); i++)
                {
                    if (vodDetail.getSubjectIDs().contains(filterSubjects.get(i)))
                    {
                        isChildMode = true;
                        break;
                    }
                }

            }
        }
        return  isChildMode;
    }

    public void storeDate(){
        SharedPreferenceUtil.getInstance().setCurrentSingleTime(TextUtils.isEmpty(currentSingleTime)?0L:Long.parseLong(currentSingleTime));
        SharedPreferenceUtil.getInstance().setCurrentAllTime(TextUtils.isEmpty(currentAllTime)?0L:Long.parseLong(currentAllTime));
    }


    public void setEpsiodePlayedTime(long epsiodePlayedTime) {
        this.epsiodePlayedTime = epsiodePlayedTime;
    }

    public long getEpsiodePlayedTime() {
        return epsiodePlayedTime;
    }

    public String getVodBanner(Picture picture){
        List<String> posters = picture.getPosters();
        if (!CollectionUtil.isEmpty(posters)) {
            String poster = posters.get(0);
            if (!TextUtils.isEmpty(poster)) {
                return poster;
            } else {
                List<String> iconList = picture.getIcons();
                if (!CollectionUtil.isEmpty(iconList)) {
                    String icon = iconList.get(0);
                    if (!TextUtils.isEmpty(poster)) {
                        return icon;
                    }
                }
            }
        }
        return null;
    }

    public String getBackground(Picture picture,String defaultBg){
        String       bgUrl  = defaultBg;
        List<String> bgUrls = picture.getBackgrounds();
        if (!CollectionUtil.isEmpty(bgUrls)) {
            if(!TextUtils.isEmpty(bgUrls.get(0))){
                bgUrl = bgUrls.get(0);
            }
        }
        return bgUrl;
    }
}
