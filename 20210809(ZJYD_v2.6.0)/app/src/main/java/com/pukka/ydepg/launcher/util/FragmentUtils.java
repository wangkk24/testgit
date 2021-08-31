package com.pukka.ydepg.launcher.util;

import android.text.TextUtils;

import com.pukka.ydepg.common.http.v6bean.v6node.ExtraData;
import com.pukka.ydepg.launcher.bean.GroupElement;

import java.util.ArrayList;
import java.util.List;

public class FragmentUtils {

    //当前页面是否含通栏运营位仅对指定用户展示
    public static List<GroupElement> getDesignateUserGroupElement(List<GroupElement> groupElements){
        if (null == groupElements || groupElements.size() == 0){
            return null;
        }
        List<GroupElement> groupElementList = new ArrayList<>();
        for (int i = 0; i < groupElements.size(); i++){
            ExtraData extraData = groupElements.get(i).getGroup().getExtraData();
            //group的扩展参数getDesignate_user=1，且getAppointedId不为空
            if (null != extraData && !TextUtils.isEmpty(extraData.getUse_cimp_data())
                    && extraData.getUse_cimp_data().equalsIgnoreCase("1")
                    && !TextUtils.isEmpty(extraData.getAppointedId())){
                groupElementList.add(groupElements.get(i));
            }
        }
        if (groupElementList.size() > 0){
            return groupElementList;
        }
        return null;
    }

}
