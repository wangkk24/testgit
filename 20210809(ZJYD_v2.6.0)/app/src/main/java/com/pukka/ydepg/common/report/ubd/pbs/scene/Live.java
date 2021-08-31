package com.pukka.ydepg.common.report.ubd.pbs.scene;

import java.util.HashMap;
import java.util.Map;

public class Live {
    //直播收费提示页
    public static Map<String,String> getPurchaseData(String actionType, String productId, String promotionId, String groupId){
        Map<String,String> data = new HashMap<>();
        data.put("actionChannel","00_04");
        data.put("actionType", actionType);
        data.put("productId",productId);
        data.put("promotionId",promotionId);
        data.put("groupId",groupId);
        return data;
    }
}
