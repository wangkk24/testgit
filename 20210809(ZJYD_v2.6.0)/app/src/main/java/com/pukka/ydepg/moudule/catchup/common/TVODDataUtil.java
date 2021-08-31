package com.pukka.ydepg.moudule.catchup.common;

import android.text.TextUtils;
import android.util.ArrayMap;

import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.PhysicalChannel;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.utils.ChannelUtil;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.player.node.Program;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TVODDataUtil implements LiveDataHolder.OnChannelUpdateListener {

    private static final String TAG = TVODDataUtil.class.getName();

    private static volatile TVODDataUtil instance;

    private boolean isChannelUpdate = false;

    private boolean isFristIntoCatchupTv=true;

    public boolean isFristIntoCatchupTv() {
        return isFristIntoCatchupTv;
    }

    public void setFristIntoCatchupTv(boolean fristIntoCatchupTv) {
        isFristIntoCatchupTv = fristIntoCatchupTv;
    }

    public List<Subject> getListSubject() {
        return listSubject;
    }

    public void setListSubject(List<Subject> listSubject) {
        this.listSubject = listSubject;
    }

    private List<Subject> listSubject;
    //用户实际可看回看栏目名

    public Map<String, ChannelDetail> getMapID2Channel() {
        return mapID2Channel;
    }

    private Map<String,ChannelDetail> mapID2Channel;


    //数据，HashMap,key=栏目id,value=该栏目下的频道集合
    private HashMap<String, List<ChannelDetail>> mSubjectName2ListChannel = new HashMap<>();

    //开机缓存所有回看频道
    private HashMap<String, List<ChannelDetail>> mSubjectID2ListChannel = new HashMap<>();

    public HashMap<String, List<ChannelDetail>> getFirstSubjectName2ListChannel()
    {
        return mSubjectID2ListChannel;
    }

    /**
     * 频道下对应的回看节目单,key=日期+频道ID，value=频道日期对应下的节目单列表数据
     */
    private ArrayMap<String,List<Program>> mTVODProgramMaps=new ArrayMap<>();

    private TVODDataUtil(){}

    public static TVODDataUtil getInstance(){
        if (null == instance) {
            synchronized (TVODDataUtil.class) {
                if (null == instance) {
                    instance = new TVODDataUtil();
                    LiveDataHolder.get().setOnChannelUpdateListener(instance);
                }
            }
        }
        return instance;
    }

    public HashMap<String, List<ChannelDetail>> getMapSubjectName2listChannel() {
        return mSubjectName2ListChannel;
    }

    public void setMapSubjectName2listChannel(String subjectName,List<ChannelDetail> channelDetails){
        mSubjectName2ListChannel.put(subjectName,channelDetails);
    }

    public List<String> getListSubjectName() {
        List<String> listSubjectName = new ArrayList<>();
        if (null != listSubject && listSubject.size() > 0){
            for(Subject subject : listSubject){
                listSubjectName.add(subject.getName());
            }
        }
        return listSubjectName;
    }

    public void createChannelMapData(){
        if(mapID2Channel == null || mapID2Channel.isEmpty()){
            //获取平台所有频道列表
            List<ChannelDetail> listAllChannel = LiveDataHolder.get().getAllChannels();
            mapID2Channel = new HashMap<>();
            for(ChannelDetail channel:listAllChannel){
                mapID2Channel.put(channel.getID(),channel);
            }
        }
    }

    private boolean isChannel4K(ChannelDetail channel) {
        try {
            List<PhysicalChannel> listPhysCh = channel.getPhysicalChannels();
            String definition = listPhysCh.get(0).getDefinition();
            //高清标清标识,取值包括：
            //0: SD
            //1: HD
            //2: 4K
            if (definition.equals("2")) {
                SuperLog.info2SD("LiveDataHolder", "4K channel [" + channel.getName() + "] has been removed from TVOD channel list.");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            SuperLog.error("LiveDataHolder", e);
            return false;
        }
    }

    // 生成用户实际可用的栏目数据(平台返回的回看栏目ID列表和PHM上配置的用户可以观看的回看栏目ID列表的交集)
    public List<Subject> getUserRealTVODSubjectID(List<Subject> listSubjectFromVsp){
        if(listSubjectFromVsp == null){
            SuperLog.error(TAG,"TVOD subject list from platform is empty.");
            return null;
        }
        List<String> listConfigSubjectID = getUserConfigTVODSubjectID();
        List<Subject> listRealSubject = new ArrayList<>();
        if(listConfigSubjectID != null){
            for(Subject subject : listSubjectFromVsp){
                if(listConfigSubjectID.contains(subject.getID())){
                    listRealSubject.add(subject);
                }
            }
        }
        return listRealSubject;
    }

    // 获取配置的订户可用直播/回看栏目ID(launcher.json中配置的)
    private List<String> getUserConfigTVODSubjectID(){
        try{
            String channelSubjectsID = "";
            // 读取launcher.json中配置的订户可用栏目ID，多个是用英文逗号隔开的
            if (null != LauncherService.getInstance().getLauncher().getExtraData()) {
                channelSubjectsID = LauncherService.getInstance().getLauncher().getExtraData().get(LiveTVActivity.KEY_CHANNEL_SUBJECTS);
            }
            SuperLog.debug(TAG, "PHM config user subject ID is ：" + channelSubjectsID);

            if (!TextUtils.isEmpty(channelSubjectsID)) {
                LiveDataHolder.get().setChannelSubjectId(channelSubjectsID);
                String[] channelSubjectsIdStr = channelSubjectsID.split(",");
                return new ArrayList<>(Arrays.asList(channelSubjectsIdStr));
            } else {
                return null;
            }
        } catch (Exception e){
            SuperLog.error(TAG,e);
            return null;
        }
    }

    @Override
    public void onChannelUpdate() {
        //频道数据发生更新，因此TVOD栏目和频道数据也需要更新
        SuperLog.info2SD(TAG," <<<<<<<<<< Channel detail information update, need update TVOD cache data >>>>>>>>>> ");
        isChannelUpdate = true;
    }

    public void clearTVODProgramList(){
        mTVODProgramMaps.clear();
    }

    public void setTVODProgramList(String key,List<Program> programList){
        mTVODProgramMaps.put(key,programList);
    }

    public List<Program> getTVODProgramList(String key){
        if(mTVODProgramMaps.containsKey(key)){
            return mTVODProgramMaps.get(key);
        }
        return null;
    }

    //判断机顶盒是否支持4K 频道是否是4K频道 过滤
    public List<ChannelDetail> filter4KChannel(List<ChannelDetail> channelDetailList){
        List<ChannelDetail> channelDetailList1 = new ArrayList<>();
        //遍历频道列表，判断每个频道归属
        for(ChannelDetail channelDetail : channelDetailList){
            ChannelDetail channelDetail1 = getMapID2Channel().get(channelDetail.getID());
            if (null == channelDetail1){
                continue;
            }
            if(!ChannelUtil.isDeviceSupport4K() && isChannel4K(channelDetail1)){
                //机顶盒不支持(允许)播放4K频道，且频道属于4K频道则该频道不添加到TVOD可观看频道栏目分类中
                continue;
            }
            channelDetailList1.add(channelDetail1);
        }
        return channelDetailList1;
    }


    public void createCacheData(){
        mSubjectID2ListChannel = new HashMap<>();
        //获取平台所有频道列表
        List<ChannelDetail> listAllChannel = LiveDataHolder.get().getAllChannels();
        if ( null != listSubject &&listSubject.size()>0 && !CollectionUtil.isEmpty(listAllChannel)) {
            // ========== 平台返回数据正常 ==========
            //用户实际可看回看栏目ID->该栏目下频道
            HashMap<String,List<ChannelDetail>> mapSubjectID2listChannel = new HashMap<>();
            //初始化本地缓存数据
            for(Subject subject:listSubject){
                mapSubjectID2listChannel.put(subject.getID(),new ArrayList<>());
            }
            //遍历频道列表，判断每个频道归属
            for(ChannelDetail channelDetail : listAllChannel){
                if(!ChannelUtil.isDeviceSupport4K() && isChannel4K(channelDetail)){
                    //机顶盒不支持(允许)播放4K频道，且频道属于4K频道则该频道不添加到TVOD可观看频道栏目分类中
                    continue;
                }
                if(null!=channelDetail.getSubjectIDs()){
                    //对于每一个频道所归属的每一个栏目，只有该栏目属于用户可观看栏目列表才添加此频道到对应栏目中去
                    for(String channelSubjectID : channelDetail.getSubjectIDs()){
                        List<ChannelDetail> listChannelDetail = mapSubjectID2listChannel.get(channelSubjectID);
                        if(null != listChannelDetail ){
                            //引用类型，修改后相当于MAP中数据也修改
                            listChannelDetail.add(channelDetail);
                            //由于一个频道可能归属多个栏目，因此内层FOR可能命中多次，为防止一个频道多次添加到"全部"频道栏目中，通过此标志位判断
                        }
                    }

                }
            }

            //过滤没有频道的栏目
            for(Subject subject:listSubject){
                List<ChannelDetail> temp = mapSubjectID2listChannel.get(subject.getID());
                if(CollectionUtil.isEmpty(temp)){
                    mapSubjectID2listChannel.remove(subject.getID());
                }
            }
            mSubjectID2ListChannel = mapSubjectID2listChannel;
        }
    }
}