package com.pukka.ydepg.moudule.livetv.rate;

import android.text.TextUtils;

import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

public class RateUtil {

    private final static String TAG = RateUtil.class.getSimpleName();

    //0(流畅:1.5M)
    public final static String RATE_SD  = "0";

    //1(高清:2.5M)
    public final static String RATE_HD  = "1";

    //2(超清:4M/6M)
    public final static String RATE_UHD = "2";

    //3(全高清：8M)
    public final static String RATE_FHD = "3";

    public static void setRateInfo(ChannelDetail detail){
        String rate = CommonUtil.getCustomField(detail.getCustomFields(),"definitionMap");
        //rate = "0:53715630|1:53776311|3:42329858";
        if(!TextUtils.isEmpty(rate)){
            try{
                SuperLog.info2SD(TAG,"Channel[" + detail.getID() + "|"+ detail.getName() + "] rate is : " + rate);
                while(!rate.isEmpty()) {
                    int breakPos = rate.indexOf("|");	           //竖线位置
                    if(breakPos<=0) {
                        break;
                    }
                    String singleRate = rate.substring(0,breakPos);//单个码率数据  0:53715630
                    String[] rateData = singleRate.split(":");
                    detail.getMapRate2ID().put(rateData[0], rateData[1]);
                    rate = rate.substring(breakPos+1);
                }

                String[] rateData = rate.split(":");
                detail.getMapRate2ID().put(rateData[0], rateData[1]);
            }catch (Exception e){
                SuperLog.error(TAG,e);
            }
        }
    }
}
