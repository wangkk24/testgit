package com.pukka.ydepg.common.report.ubd.scene;

import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.UBDService;
import com.pukka.ydepg.common.report.ubd.extension.SwitchData;
import com.pukka.ydepg.common.utils.JsonParse;

public class UBDOther {

    //收藏上报
    public static void recordCreateFavorite(String contentId,String recommendType){
        SwitchData data = new SwitchData();
        data.setContentID(contentId);
        data.setRecommendType(recommendType);
        data.setActionID(UBDConstant.ACTION.FAVORITE);
        UBDService.recordSwitchPage(
                UBDConstant.Function.COLLECT,
                UBDConstant.Function.COLLECT,
                JsonParse.object2String(data));
    }
}
