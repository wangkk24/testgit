package com.pukka.ydepg.moudule.voice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.moudule.catchup.activity.CatchUpActivity;
import com.pukka.ydepg.moudule.catchup.activity.TVODProgramListActivity;
import com.pukka.ydepg.moudule.catchup.common.TVODDataUtil;
import com.pukka.ydepg.moudule.catchup.presenter.TVODProgramListPresenter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by liudo on 2018/10/17.
 */

public class CatchUpVoiceReceiver extends BroadcastReceiver
{
    private static final String TAG = CatchUpVoiceReceiver.class.getName();

    private static final String LIVETV_CMD = "com.iflytek.xiri.action.TVBACK";

    /**
     * 频道名称
     */
    private static final String CHANNEL_NAME = "channelname";
    /**
     * 节目名称
     */
    private static final String NAME = "name";
    /**
     * 开始日期yyyy-MM-dd
     */
    private static final String START_DATE = "startdate";
    /**
     * 结束日期yyyy-MM-dd
     */
    private static final String END_DATE = "enddate";
    /**
     * 开始时间HH:mm:ss
     */
    private static final String START_TIME = "starttime";
    /**
     * 结束时间HH:mm:ss
     */
    private static final String END_TIME = "end_time";
    /**
     * 扩展信息（用于透传原始节目单信息，可选)
     */
    private static final String EXTENDINFO = "extendinfo";
    /**
     * 频道code
     */
    private static final String CHANNEL_ID = "channelid";
    /**
     * 频道号
     */
    private static final String NO = "no";

    private static final String INTENT_ACTION = "action";

    private static final String INTENT_CHANNEL_ID = "channelid";



    /**
     * 选择播放哪个节目单，哪个频道
     */
    private static final String CMD_SELECT = "select";
    /**
     * 打开回看
     */
    private static final String CMD_OPEN = "open";

    private String mChannelName;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        SuperLog.info(TAG, "onReceive action:" + action);
        if (!TextUtils.isEmpty(action))
        {
            if (action.equals(LIVETV_CMD))
            {
                String actionValue = intent.getStringExtra(INTENT_ACTION);
                SuperLog.debug(TAG, "actionValue:" + actionValue);
                if (!TextUtils.isEmpty(actionValue))
                {
                    switch (actionValue)
                    {
                        case CMD_OPEN:
                            SuperLog.debug(TAG, "open->打开回看");
                            switchCatchUpTV(context);
                            break;
                        case CMD_SELECT:
                            String channelName = intent.getStringExtra(CHANNEL_NAME);
                            String name = intent.getStringExtra(NAME);
                            String startdate = intent.getStringExtra(START_DATE);
                            String enddate = intent.getStringExtra(END_DATE);
                            String starttime = intent.getStringExtra(START_TIME);
                            String endtime = intent.getStringExtra(END_TIME);
                            String extendinfo = intent.getStringExtra(EXTENDINFO);
                            String channelCode = intent.getStringExtra(CHANNEL_ID);
//                            channelCode = "42329890";
                            String no = intent.getStringExtra(NO);
                            SuperLog.debug(TAG, "select->channelName:" + channelName + "|name:" +
                                name + "|startdate:" + startdate + "|enddate:" + enddate +
                                "|starttime:" + starttime + "|endtime:" + endtime +
                                "|channelCode:" + channelCode + "|no:" + no + "|extendinfo:" +
                                extendinfo);
                            if (authority(channelCode))
                            {
                                HashMap<String, String> map = new HashMap<>();
                                if (!TextUtils.isEmpty(channelCode))
                                {
                                    map.put(CatchUpActivity.CHANNEL_ID, channelCode);
                                    if(TextUtils.isEmpty(channelName)){
                                       channelName=mChannelName;
                                    }
                                    if (!TextUtils.isEmpty(channelName))
                                    {
                                        map.put(CatchUpActivity.CHANNEL_NAME, channelName);
                                    }
                                    if (!TextUtils.isEmpty(startdate))
                                    {
                                        if (DateOutRange(startdate))
                                        {
                                            EpgToast.showToast(OTTApplication.getContext(),
                                                "只展示过去6天回看");
                                            return;
                                        }
                                        else
                                        {
                                            map.put(CatchUpActivity.CHANNEL_DATE, startdate);
                                        }

                                    }

                                    switchTVODProgram(context, map);
                                }

                            }else{
                                EpgToast.showLongToast(OTTApplication.getContext(),"当前用户无权限观看此频道");
                            }

                            break;
                    }
                }
            }
        }
        else
        {
            SuperLog.error(TAG, "VoiceReceiver Intent == null");
        }
    }

    private boolean DateOutRange(String startdate)
    {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try
        {
            long channelStartTime=dft.parse(startdate).getTime();
            //用Calender毫秒值比较不准确，这里添加12小时
            channelStartTime=channelStartTime+12*60*60*1000;
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.setTime(new Date());
            mCalendar.set(Calendar.HOUR_OF_DAY,23);
            mCalendar.set(Calendar.MINUTE,59);
            mCalendar.set(Calendar.SECOND,59);
            long rangestartTime=mCalendar.getTimeInMillis();
            mCalendar.set(Calendar.DATE, mCalendar.get(Calendar.DATE) -TVODProgramListPresenter.DAY_COUNT);
            mCalendar.set(Calendar.HOUR_OF_DAY,0);
            mCalendar.set(Calendar.MINUTE,0);
            mCalendar.set(Calendar.SECOND,0);
            long rangeEndTime=mCalendar.getTimeInMillis();
            if(rangeEndTime<=channelStartTime&&channelStartTime<=rangestartTime){
                return false;
            }
        }
        catch (ParseException e)
        {
            SuperLog.error(TAG,e);
        }

        return true;
    }

    public  boolean authority(String channelId)
    {
        if(TextUtils.isEmpty(channelId)){
            return false;
        }
        HashMap<String, List<ChannelDetail>> channelMap= TVODDataUtil.getInstance().getFirstSubjectName2ListChannel();
        if(null==channelMap||channelMap.isEmpty()){
            return false;
        }
        for(String key:channelMap.keySet()){
            List<ChannelDetail> channelDetailList=channelMap.get(key);
            if(null!=channelDetailList&&!channelDetailList.isEmpty()){
                for(int i=0;i<channelDetailList.size();i++){
                    if(channelId.equals(channelDetailList.get(i).getID())){
                        mChannelName=channelDetailList.get(i).getName();
                        return true;
                    }

                }
            }

        }
        return false;
    }


    public void switchCatchUpTV(Context context)
    {
        Intent it = new Intent(context, CatchUpActivity.class);
        //        it.putExtra(LiveTVActivity.VIDEO_TYPE, LiveTVActivity.VIDEO_TYPE_LIVETV);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }

    public void switchTVODProgram(Context context, Map<String, String> map)
    {
        Intent intent = new Intent(context, TVODProgramListActivity.class);
        if (null != map && map.size() > 0)
        {
            for (String key : map.keySet())
            {
                intent.putExtra(key, map.get(key));
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
