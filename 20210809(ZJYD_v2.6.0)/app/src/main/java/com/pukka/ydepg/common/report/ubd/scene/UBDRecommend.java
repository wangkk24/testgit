package com.pukka.ydepg.common.report.ubd.scene;

import android.text.TextUtils;

import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.Navigate;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.UBDService;
import com.pukka.ydepg.common.report.ubd.UBDTool;
import com.pukka.ydepg.common.report.ubd.extension.RecommendData;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.MainActivity;

public class UBDRecommend {
    //首页智能推荐位上报--（接口返回）
    public static void recordMainRecommend(String recommendIds,String recommendType,String sceneId,String appointedId){
        Navigate navigate = UBDSwitch.getInstance().getNavigate(UBDSwitch.getInstance().getNavPosition());
        String navId = "";
        if (null != navigate && !TextUtils.isEmpty(navigate.getId())){
            navId = "_" + navigate.getId();
        }
        //String optType = getOptType(sceneId);
        //因为2.3为混合推荐，所以可能有多个sceneId,optType暂不曝光
        //if (!TextUtils.isEmpty(optType)){
            RecommendData info = new RecommendData();
            info.setStreamNo(System.currentTimeMillis()+ CommonUtil.getMac());
            info.setOptTime(UBDTool.getInstance().getUBDFormatTime(DateCalendarUtils.PATTERN_B));
            //info.setOptType(optType);
            info.setStbUser(SessionService.getInstance().getSession().getUserId());
            info.setDeviceType(UBDConstant.DEVICE_STB);
            info.setRecommendId(recommendIds);
            info.setAppointedId(appointedId);
            info.setRecommendType(recommendType);
            info.setSceneId(sceneId);

            String extensionField = JsonParse.object2String(info, RecommendData.class);
            UBDService.recordSwitchPage(MainActivity.class.getSimpleName() + navId ,UBDConstant.Function.RECOMMEND,extensionField);
        //}
    }
}