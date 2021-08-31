package com.pukka.ydepg.common.report.ubd.pbs.scene;

import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;

import java.util.HashMap;
import java.util.Map;

public final class Purchase {
    private Purchase(){};
    //订购中心点击产品进PBS订购页面
    public static Map<String,String> getOrderCenterData(String productId,String promotionId,String groupId,String sequence){
        Map<String,String> data = new HashMap<>();
        data.put("actionChannel",PbsUaConstant.ActionChannel.PURCHASE);//00_03
        data.put("actionType", PbsUaConstant.ActionType.CLICK);
        data.put("productId",productId);
        data.put("promotionId",promotionId);
        data.put("groupId",groupId);
        data.put("sequence",sequence);
        return data;
    }
}