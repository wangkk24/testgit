package com.pukka.ydepg.moudule.livetv.utils;

import android.text.TextUtils;

import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.PhysicalChannel;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.utils.ChannelUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.player.node.CurrentChannelPlaybillInfo;
import com.pukka.ydepg.moudule.player.node.Schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class LiveDataHolder {

    public interface OnChannelUpdateListener {
        void onChannelUpdate();
    }

    public void setOnChannelUpdateListener(OnChannelUpdateListener onChannelUpdateListener) {
        this.onChannelUpdateListener = onChannelUpdateListener;
    }

    private OnChannelUpdateListener onChannelUpdateListener;

    /**
     * 全部频道列表
     */
    private List<Schedule> mAllSchedules;


    private List<ChannelDetail> mAllSaveSchedules;
    private Map<String, ChannelDetail> mAllSaveScheduleMap;

    /**
     * 可播放的频道缓存
     */
    private List<ChannelDetail> mSchedulesPlayable;

    private HashMap<String, List<ChannelDetail>> mHashMap;
    private List<Subject> mSubjectList;
    private List<Subject> mChildSubject;
    private String playVodBean;

    //组播开关状态是否发生变化
    private boolean isChangeMulticastSwitch = false;

    /*
     * 直播栏目缓存时间
     * */
    private String mLiveDataTime = "";

    /**
     * instance
     */
    private static volatile LiveDataHolder mInstance;

    /**
     * 频道选中的位置
     */
    private volatile int mChannelSelectPosition = 0;

    /**
     * 栏目选中的位置
     */
    private volatile int mCulomnSelectPosition = -1;

    //用户栏目Id
    private String mChannelSubjectId = "";

    /**
     * 1.是否正在显示开机导航页
     * 2.true:直播播放固定频道，不显示Mini EPG，不存储播放记录
     */
    private volatile boolean isShowingSkip = false;

    //用于临时记录直播播放固定频道的channelID、MediaId
    private volatile String mChannelId = "";
    private volatile String mMediaId = "";

    private volatile int multicastTrySeeCount = 1;

    //储存直播订购相关的
    private String mLiveOrderBG = "";
    //确认订购按钮落焦图片
    private String mLiveOrderSureSelected = "";
    //确认订购按钮正常图片
    private String mLiveOrderSureNormal = "";
    //取消订购按钮落焦图片
    private String mLiveOrderCancelSelected = "";
    //取消订购按钮普通图片
    private String mLiveOrderCancelNormal = "";

    //0:取消 1 确定  首次默认落焦
    private String mFristFocus = "1";

    //文案提示

    private String mLiveOrderTitle = "";

    public String getmLiveOrderTitle() {
        return mLiveOrderTitle;
    }

    public void setmLiveOrderTitle(String mLiveOrderTitle) {
        this.mLiveOrderTitle = mLiveOrderTitle;
    }

    public String getmLiveOrderBG() {
        return mLiveOrderBG;
    }

    public void setmLiveOrderBG(String mLiveOrderBG) {
        this.mLiveOrderBG = mLiveOrderBG;
    }

    public String getmLiveOrderSureSelected() {
        return mLiveOrderSureSelected;
    }

    public void setmLiveOrderSureSelected(String mLiveOrderSureSelected) {
        this.mLiveOrderSureSelected = mLiveOrderSureSelected;
    }

    public String getmLiveOrderSureNormal() {
        return mLiveOrderSureNormal;
    }

    public void setmLiveOrderSureNormal(String mLiveOrderSureNormal) {
        this.mLiveOrderSureNormal = mLiveOrderSureNormal;
    }

    public String getmLiveOrderCancelSelected() {
        return mLiveOrderCancelSelected;
    }

    public void setmLiveOrderCancelSelected(String mLiveOrderCancelSelected) {
        this.mLiveOrderCancelSelected = mLiveOrderCancelSelected;
    }

    public String getmLiveOrderCancelNormal() {
        return mLiveOrderCancelNormal;
    }

    public void setmLiveOrderCancelNormal(String mLiveOrderCancelNormal) {
        this.mLiveOrderCancelNormal = mLiveOrderCancelNormal;
    }

    public String getmFristFocus() {
        return mFristFocus;
    }

    public void setmFristFocus(String mFristFocus) {
        this.mFristFocus = mFristFocus;
    }


    private LiveDataHolder() {
    }

    public static LiveDataHolder get() {
        if (null == mInstance) {
            synchronized (LiveDataHolder.class) {
                if (null == mInstance) {
                    mInstance = new LiveDataHolder();
                }
            }
        }
        return mInstance;
    }

    /**
     * 从本地获取全量的缓存频道数据
     */
    public synchronized List<Schedule> getAllSchedules() {
        if (null == mAllSchedules) {
            //获取本地缓存的频道数据
            mAllSchedules = SharedPreferenceUtil.getInstance().getAllChannel();
        }
        return mAllSchedules;
    }

    /**
     * 缓存全量的频道信息数据
     */
    public synchronized void setAllSchedules(List<Schedule> allSchedules) {
        if (null != allSchedules && allSchedules.size() > 0) {
            this.mAllSchedules = allSchedules;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferenceUtil.getInstance().setAllChannel(allSchedules);
                }
            }).start();
        }
    }

    /**
     * 从本地获取全量的缓存频道数据
     */
    public List<ChannelDetail> getAllChannels() {
        if (null == mAllSaveSchedules) {
            //synchronized (this){
            SuperLog.info2SD("sdf", " ########## Start to [Get] all channel info in parseDynamicProperties ########## ");
            mAllSaveSchedules = SharedPreferenceUtil.getInstance().geSavetAllChannel();
            SuperLog.info2SD("sdf", " ########## End to [Get] all channel info in parseDynamicProperties ########## ");
            //}
        }
        return mAllSaveSchedules;
    }

    /**
     * 缓存全量的频道信息数据
     */
    public void setAllChannels(List<ChannelDetail> allSchedules) {
        if (null != allSchedules && allSchedules.size() > 0) {
            this.mAllSaveSchedules = allSchedules;
            new Thread(() ->
            {
                synchronized (this) {
                    SuperLog.info2SD("sdf", " ########## Start to [Save] all channel info in parseDynamicProperties ########## ");
                    SharedPreferenceUtil.getInstance().setSaveAllChannel(mAllSaveSchedules);
                    SuperLog.info2SD("sdf", " ########## End to [Save] all channel info in parseDynamicProperties ########## ");
                }
            }).start();
        }
    }

    public ChannelDetail getChannelDetailByChannelID(String channelID) {
        return getSaveSchedulesMap().get(channelID);
    }

    /**
     * 从本地获取全量的缓存频道数据
     */
    public Map<String, ChannelDetail> getSaveSchedulesMap() {
        if (null == mAllSaveScheduleMap) {
            //synchronized (this){
            SuperLog.info2SD("sdf", " ########## Start to [Get] all channel info in parseDynamicProperties ########## ");
            mAllSaveScheduleMap = SharedPreferenceUtil.getInstance().geSavetAllChannelMap();
            SuperLog.info2SD("sdf", " ########## End to [Get] all channel info in parseDynamicProperties ########## ");
            //}
        }
        if (mAllSaveScheduleMap == null || mAllSaveScheduleMap.size() > 0) {
            List<ChannelDetail> allSchedules = getAllChannels();
            if (allSchedules != null && allSchedules.size() > 0) {
                mAllSaveScheduleMap = new HashMap<>();
                for (ChannelDetail channelDetail : allSchedules) {
                    mAllSaveScheduleMap.put(channelDetail.getID(), channelDetail);
                }
            }
        }
        return mAllSaveScheduleMap;
    }

    /**
     * 缓存全量的频道信息数据
     */
    public void setMapID2ChannelDetail(Map<String, ChannelDetail> allSchedulesMap) {
        if (null != allSchedulesMap && allSchedulesMap.size() > 0) {
            this.mAllSaveScheduleMap = allSchedulesMap;
            new Thread(() ->
            {
                synchronized (this) {
                    SuperLog.info2SD("sdf", " ########## Start to [Save] all channel info in parseDynamicProperties ########## ");
                    SharedPreferenceUtil.getInstance().setSaveAllChannelMap(mAllSaveScheduleMap);
                    SuperLog.info2SD("sdf", " ########## End to [Save] all channel info in parseDynamicProperties ########## ");
                }
            }).start();
        }
    }


    /**
     * 从本地获取可播放频道列表
     */
    public synchronized List<ChannelDetail> getChannelPlay() {
        if (null == mSchedulesPlayable) {
            mSchedulesPlayable = SharedPreferenceUtil.getInstance().getChannelPlay();
        }
        return mSchedulesPlayable;
    }

    /**
     * 缓存可播放频道列表
     */
    public synchronized void setChannelPlay(List<ChannelDetail> allSchedules) {
        if (null != allSchedules && allSchedules.size() > 0) {
            //去除重复频道
            final List<ChannelDetail> listAllSchedules = removeDuplicateData(allSchedules);
            //所有频道按照频道号从小到大排序
            Collections.sort(listAllSchedules, new Comparator<ChannelDetail>() {
                @Override
                public int compare(ChannelDetail o1, ChannelDetail o2) {
                    if (null == o1 && null == o2) {
                        //o1 o2 都为空
                        return 0;
                    }
                    if (null == o1) {
                        //o1为空
                        return -1;
                    }
                    if (null == o2) {
                        //o2为空
                        return 1;
                    }

                    //o1 o2 都不为空
                    if (TextUtils.isEmpty(o1.getChannelNO()) && TextUtils.isEmpty(o2.getChannelNO())) {
                        //o1.No o2.No 都为空
                        return 0;
                    }
                    if (TextUtils.isEmpty(o1.getChannelNO())) {
                        //o1.No为空
                        return -1;
                    }
                    if (TextUtils.isEmpty(o2.getChannelNO())) {
                        //o2.No为空
                        return 1;
                    }
                    //o1.No o2.No 都不为空
                    Integer o1No = Integer.parseInt(o1.getChannelNO());
                    Integer o2No = Integer.parseInt(o2.getChannelNO());
                    return o1No.compareTo(o2No);
                }
            });
            //不支持4K的机顶盒频道列表移除4K频道
            remove4Kchannel(listAllSchedules);

            //全部逻辑处理完毕保存可播放频道信息到文件
            new Thread(() -> SharedPreferenceUtil.getInstance().setChannelPlay(listAllSchedules)).start();

            this.mSchedulesPlayable = listAllSchedules;

            //可播放频道列表发生变化,通知订阅者
            if (null != onChannelUpdateListener) {
                onChannelUpdateListener.onChannelUpdate();
            }
        } else {
            //全部逻辑处理完毕保存可播放频道信息到文件
            new Thread(() -> SharedPreferenceUtil.getInstance().setChannelPlay(allSchedules)).start();
            this.mSchedulesPlayable = allSchedules;
        }
    }

    //去除list中重复数据，该方法会打乱list中原有元素顺序
    private List removeDuplicateData(List list) {
        HashSet h = new HashSet(list);//元素无序(即list中元素顺序会被打乱)
        list.clear();
        list.addAll(h);
        return list;
    }

    /**
     * 从本地获取可播放频道列表toSchedule
     */
    public synchronized List<Schedule> getChannelPlayToSchedule(List<ChannelDetail> channelPlay) {
        //List<ChannelDetail> channelPlay = SharedPreferenceUtil.getInstance().getChannelPlay();
        List<Schedule> schedules = new ArrayList<>();
        if (null != channelPlay && !channelPlay.isEmpty()) {
            for (ChannelDetail channelDetail : channelPlay) {
                Schedule schedule = LiveUtils.parseSingleSchedule(channelDetail);
                schedules.add(schedule);
            }
        }
        return schedules;
    }

    /**
     * 从本地获取全量的缓存栏目和对应的频道HashMap
     */
    public synchronized HashMap<String, List<ChannelDetail>> getSaveHashMap() {
        if (null == mHashMap) {
            //获取本地缓存的频道数据
            mHashMap = SharedPreferenceUtil.getInstance().getSaveHashMap();
        }
        return mHashMap;
    }

    /**
     * 缓存全量的栏目和对应的频道HashMap
     */
    public void setSaveHashMap(HashMap<String, List<ChannelDetail>> hashMap) {
        if (null != hashMap && hashMap.size() > 0) {
            for (List<ChannelDetail> value : hashMap.values()) {
                remove4Kchannel(value);
            }
            this.mHashMap = hashMap;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this) {
                        SharedPreferenceUtil.getInstance().setSaveHashMap(mHashMap);
                    }
                }
            }).start();

        } else {
            this.mHashMap = hashMap;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this) {
                        SharedPreferenceUtil.getInstance().setSaveHashMap(mHashMap);
                    }
                }
            }).start();
        }
    }

    public void changeSaveHashMap(HashMap<String, List<ChannelDetail>> hashMap) {
        if (null != hashMap && hashMap.size() > 0) {
            for (List<ChannelDetail> value : hashMap.values()) {
                remove4Kchannel(value);
            }
            this.mHashMap = hashMap;
        }
    }

    public void remove4Kchannel(List<ChannelDetail> listChannel) {
        if (!ChannelUtil.isDeviceSupport4K()) {
            for (int i = listChannel.size() - 1; i >= 0; i--) {
                try {
                    ChannelDetail ch = listChannel.get(i);
                    List<PhysicalChannel> listPhysCh = ch.getPhysicalChannels();
                    String definition = listPhysCh.get(0).getDefinition();
                    //高清标清标识,取值包括：
                    //0: SD
                    //1: HD
                    //2: 4K
                    if (definition.equals("2")) {
                        listChannel.remove(ch);
                        SuperLog.info2SD("LiveDataHolder", "4K channel [" + ch.getName() + "] has been removed from channel list.");
                    }
                } catch (Exception e) {
                    SuperLog.error("LiveDataHolder", e);
                }
            }
        }
    }

    public synchronized List<Subject> getSaveSubjectList() {
        if (null == mSubjectList) {
            //获取本地缓存的频道数据
            mSubjectList = SharedPreferenceUtil.getInstance().getSaveSubject();
        }
        return mSubjectList;
    }

    /**
     * 缓存全量的栏目和对应的频道HashMap
     */
    public synchronized void setSaveSubjectList(List<Subject> subjectList) {
        this.mSubjectList = subjectList;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferenceUtil.getInstance().setSaveSubject(subjectList);
            }
        }).start();
    }

    public List<CurrentChannelPlaybillInfo> getCurrentPlaybillInfoList(List<ChannelDetail> list) {
        List<CurrentChannelPlaybillInfo> infoList = new ArrayList<>();
        for (ChannelDetail channelDetail : list) {
            CurrentChannelPlaybillInfo info = new CurrentChannelPlaybillInfo();
            info.setChannelId(channelDetail.getID());
            info.setChannelNo(channelDetail.getChannelNO());
            info.setChannelName(channelDetail.getName());
            if (channelDetail.getPhysicalChannels() != null
                    && channelDetail.getPhysicalChannels().size() > 0) {
                info.setChannelMediaId(channelDetail.getPhysicalChannels().get(0).getID());
            }
            infoList.add(info);
        }
        return infoList;
    }

    /**
     * 设置当前正在播放的频道位置
     *
     * @param position 频道位置
     */
    public void setChannelSelectPosition(int position) {
        mChannelSelectPosition = position;
        SharedPreferenceUtil.getInstance().setSelectChannel(position);
    }

    /**
     * 返回当前频道正在播放的位置
     */
    public int getChannelSelectPosition() {
        mChannelSelectPosition = SharedPreferenceUtil.getInstance().getSelectChannel();
        return mChannelSelectPosition;

    }

    /**
     * 设置当前正在播放的栏目位置
     *
     * @param position 栏目位置
     */
    public void setCulomnSelectPosition(int position) {
        mCulomnSelectPosition = position;
        SharedPreferenceUtil.getInstance().setSelectCulomn(position);
    }

    /**
     * 返回当前栏目正在播放的位置
     */
    public int getCulomnSelectPosition() {
        if (mCulomnSelectPosition == -1) {
            mCulomnSelectPosition = SharedPreferenceUtil.getInstance().getSelectCulomn();
        }
        return mCulomnSelectPosition;
    }

    /**
     * 保存栏目缓存的时间
     */
    public void setLiveDataTime(String liveDataTime) {
        mLiveDataTime = liveDataTime;
    }

    /**
     * 返回栏目缓存的时间
     */
    public String getLiveDataTime() {
        return mLiveDataTime;
    }

    //组播状态是否发生变化
    public void setIsChangeMulticastSwitch(boolean isChangeMulticastSwitch) {
        this.isChangeMulticastSwitch = isChangeMulticastSwitch;
    }

    public boolean isChangeMulticastSwitch() {
        return isChangeMulticastSwitch;
    }

    /**
     * 保存栏目id
     */
    public void setChannelSubjectId(String channelSubjectId) {
        mChannelSubjectId = channelSubjectId;
    }

    /**
     * 返回栏目id
     */
    public String getChannelSubjectId() {
        return mChannelSubjectId;
    }

    /**
     * 确定落焦只在登陆或者时调用一次
     */
    public void setUserIdIndexIndex() {
        SharedPreferenceUtil.getInstance().setUserId();
    }

    public String getUserIdIndexIndex() {
        //return mUserIdIndex;
        return SharedPreferenceUtil.getInstance().getUserId();
    }

    public boolean isMiniEpg() {
        return isMiniEpg;
    }

    public void setMiniEpg(boolean miniEpg) {
        isMiniEpg = miniEpg;
    }

    private boolean isMiniEpg = false;

    public void setIsShowingSkip(boolean isShowingSkip) {
        this.isShowingSkip = isShowingSkip;
    }

    public boolean getIsShowingSkip() {
        return isShowingSkip;
    }

    public void setChannelAndMediaId(String channelId, String mediaId) {
        this.mChannelId = channelId;
        this.mMediaId = mediaId;
    }

    public String getChannelAndMediaId() {
        if (TextUtils.isEmpty(mChannelId)) {
            return "";
        } else {
            return mChannelId + "," + mMediaId;
        }
    }

    public synchronized Subject getTargetSubject(String subjectID) {
        if (null == mSubjectList) {
            //获取本地缓存的频道数据
            mSubjectList = SharedPreferenceUtil.getInstance().getSaveSubject();
        }
        if (mSubjectList != null && mSubjectList.size() > 0) {
            for (Subject subject : mSubjectList) {
                if (TextUtils.equals(subjectID, subject.getID())) {
                    return subject;
                }
            }
        }
        return null;
    }

    public synchronized List<Subject> getTargetChildSubject(String subjectID) {
//        List<Subject> childs = new ArrayList<>();
//        if (null == mSubjectList) {
//            //获取本地缓存的频道数据
//            mSubjectList = SharedPreferenceUtil.getInstance().getSaveSubject();
//        }
//        if(mSubjectList != null && mSubjectList.size() > 0){
//            for(Subject subject:mSubjectList){
//                if(TextUtils.equals(subjectID, subject.getParentSubjectID())){
//                    childs.add(subject);
//                }
//            }
//        }
//        return childs;
        List<Subject> subjects = getChildSubject();
        if (subjects == null || subjects.size() == 0) {
            subjects = new ArrayList<>();
            Subject subject = getTargetSubject(subjectID);
            if (subject != null) {
                subjects.add(subject);
            }
        }
        return subjects;
    }

    public synchronized List<ChannelDetail> getTargetChannelPlay(String subjectID, boolean isQueryChild) {
        List<ChannelDetail> channelDetails = new ArrayList<>();
        if (isQueryChild) {
            Map<String, List<ChannelDetail>> channelHashMap = LiveDataHolder.get().getSaveHashMap();
            List<Subject> childSubject = getTargetChildSubject(subjectID);
            if (childSubject != null && childSubject.size() > 0) {
                for (Subject subject : childSubject) {
                    List<ChannelDetail> childChannel = channelHashMap.get(subject.getID());
                    if (childChannel != null && childChannel.size() > 0) {
                        channelDetails.addAll(childChannel);
                    }
                }
            } else {
                List<ChannelDetail> childChannel = channelHashMap.get(subjectID);
                return childChannel;
            }
        } else {
            Map<String, List<ChannelDetail>> channelHashMap = LiveDataHolder.get().getSaveHashMap();
            List<ChannelDetail> childChannel = channelHashMap.get(subjectID);
            return childChannel;
        }
        return channelDetails;
    }

    public void multicastPlayFail() {
        multicastTrySeeCount--;
    }

    public boolean canPlayMulticast() {
        return multicastTrySeeCount > 0;
    }

    public void setChildSubject(List<Subject> childSubject) {
        mChildSubject = childSubject;
    }

    public List<Subject> getChildSubject() {
        return mChildSubject;
    }

    public String getPlayVodBean() {
        return playVodBean;
    }

    public void setPlayVodBean(String playVodBean) {
        this.playVodBean = playVodBean;
    }
}