package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.bean.node.Marketing;
import com.pukka.ydepg.common.http.bean.node.OfferInfo;
import com.pukka.ydepg.common.http.v6bean.v6node.CustomGroup;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.service.NtpTimeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketingUtils {

    //product扩展参数，营销活动信息
    private final static String zjProductOfSales = "zjProductOfSales";

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    private static final String TAG = "MarketingUtils";

    //将用户的分组与产品包支持营销活动的分组比较，取出第一个匹配的营销活动，如果没有，返回null
    static public Marketing getmarketingProduct(List<CustomGroup> customGroups, Product product, List<OfferInfo> offerInfos){
        //取出产品包扩展参数中的营销产品字段
        List<NamedParameter> listNp = product.getCustomFields();
        List<String> marketInfo = CommonUtil.getCustomNamedParameterByKey(listNp,zjProductOfSales);
        Map<String, Marketing> marketingMap = new HashMap<>();
        if (null != marketInfo && marketInfo.size()>0){
            String str = marketInfo.get(0);
            if (!TextUtils.isEmpty(str)){
                //转换成对象
                marketingMap = JsonParse.json2Object(str,new TypeToken<HashMap<String, Marketing>>() {}.getType());
            }
        }
        if (null == marketingMap){
            return null;
        }
        //遍历用户组
        for (int j = 0; j < customGroups.size(); j++) {
            CustomGroup group = customGroups.get(j);
            //用户是否属于这个用户组 1属于 0不属于
            if (group.getLabelValue().equals("1")){
                //取产品包的扩展参数里面的营销活动，看能否匹配上
                //产品包中包含用户组的营销活动
                if (null != marketingMap.get(group.getGroupId())){
                    Marketing marketing = marketingMap.get(group.getGroupId());
                    if (null != marketing){
                        try {
                            //对比生效时间
                            long endTime = sdf.parse(marketing.getEndTime()).getTime();
                            long startTime = sdf.parse(marketing.getStartTime()).getTime();
                            long currentTime = NtpTimeService.queryNtpTime();
                            //用户组匹配且在生效时间内的营销活动
                            if (endTime > currentTime && startTime <= currentTime){
                                //判断用户是否已经订购这个订购tag
                                if (!isMarketingOrdered(marketing,offerInfos)){
                                    return marketing;
                                }
                            }
                        }catch (ParseException e){
                            SuperLog.error(TAG, e);
                        }
                    }
                }
            }
        }
        return null;
    }

    //判断Marking是否已经订购过
    static private Boolean isMarketingOrdered(Marketing marketing, List<OfferInfo> offerInfos){
        if (null != offerInfos && offerInfos.size() > 0) {
            for (int i = 0; i < offerInfos.size(); i++) {
                //如果这个Marking的OfferID和tagID在offerInfos中。返回true
                if ((offerInfos.get(i).getOfferID().equals(marketing.getId()) || offerInfos.get(i).getOfferID().equals(marketing.getTag()))) {
                    return true;
                }
            }
        }
        return false;
    }
}
