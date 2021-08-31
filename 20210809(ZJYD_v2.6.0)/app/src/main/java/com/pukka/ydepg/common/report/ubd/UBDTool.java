package com.pukka.ydepg.common.report.ubd;

import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity;
import com.pukka.ydepg.moudule.vod.activity.ChildModeVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.VodDetailActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UBDTool {

    /********* 私有区 *********/
    //不在BaseActivity的onResume中上报UBD的Activity集合,这些Activity由于各种原因需要自己上报
    private List<String> listSpecialActivity = new ArrayList<>();

    private UBDTool() {
        //listSpecialActivity.add(VodMainActivity.class.getSimpleName());
        listSpecialActivity.add(OnDemandVideoActivity.class.getSimpleName());

        //详情页面要上报VOD.NAME,该参数在onResume之后才能获取,因此无法再BaseActivity中上报
        listSpecialActivity.add(VodDetailActivity.class.getSimpleName());

        //详情页面要上报VOD.NAME,该参数在onResume之后才能获取,因此无法再BaseActivity中上报
        listSpecialActivity.add(ChildModeVodDetailActivity.class.getSimpleName());

        //详情页面要上报VOD.NAME,该参数在onResume之后才能获取,因此无法再BaseActivity中上报
        listSpecialActivity.add(NewVodDetailActivity.class.getSimpleName());
    }

    private boolean isReportUBD(){
        String userGroup = SessionService.getInstance().getSession().getUserGroup();
        List<String> ubdUserGroup = CommonUtil.getListConfigValue("ubd_user_group");
        SuperLog.info2SD(UBDConstant.TAG, "Current userGroup = " + userGroup);
        SuperLog.info2SD(UBDConstant.TAG, "UBD     userGroup = " + ubdUserGroup);
        if( ubdUserGroup != null && (ubdUserGroup.contains(userGroup) || ubdUserGroup.contains("-1"))){
            return true;
        } else {
            SuperLog.info2SD(UBDConstant.TAG, "No need to report UBD action for this user according userGroup.");
            return false;
        }
    }





    /********* 公有区 *********/
    public void init(){
        //终端配置参数总开关[终端配置参数key=UBD_switch value=1为开状态]
        String ubdSwitch = SessionService.getInstance().getSession().getTerminalConfigurationValue(UBDConstant.UBD_SWITCH_KEY);
        if ( UBDConstant.UBD_OPEN.equals(ubdSwitch) ) {
            //分组开关[终端配置参数key=ubd_user_group 用户所属组与配置组相同为开状态]
            if(isReportUBD()){
                UBDService.init();
            }
        } else {
            SuperLog.info2SD(UBDConstant.TAG,"UBD function is close by TerminalConfigurationValue[" + UBDConstant.UBD_SWITCH_KEY + "].");
        }
    }

    //获取通用数据上报动作时间
    public String getUBDFormatTime(String pattern){
        return DateCalendarUtils.getTime(new Date(),pattern);
    }

    //特殊activity列表中的activity不在baseActivity中上报，由其自己处理
    public boolean isReport(String activity){
        if(listSpecialActivity.contains(activity)){
            return false;
        } else {
            return true;
        }
    }





    /********* 静态区 *********/
    private static UBDTool instance;

    public static UBDTool getInstance(){
        if( instance == null ){
            instance = new UBDTool();
        }
        return instance;
    }
}