package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools;


import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.common.http.v6bean.v6node.Profile;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.session.Session;
import com.pukka.ydepg.launcher.session.SessionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//避免重复订购的工具类
public class AvoidRepeatPaymentUtils {

    private static final String TAG = "AvoidRepeatPaymentUtils";

    private static AvoidRepeatPaymentUtils mAvoidRepeatPaymentUtils = new AvoidRepeatPaymentUtils();

    //储存已经订购的产品包id
    private List<String> alreadyPayProductIds;

    //储存已经订购产品包的订购时间 key:产品包id value:最新订购的时间
    private Map<String,String> alreadyPayProductMap;

    private String userId = "";

    private AvoidRepeatPaymentUtils() {
    }

    //获取单例
    public static AvoidRepeatPaymentUtils getInstance(){
        return mAvoidRepeatPaymentUtils;
    }

    //点击订购，记录订购时间
    public void recordPaymentTime(String productId){
        if (null == alreadyPayProductMap){
            SuperLog.info2SD(TAG, "map = null，create map");
            alreadyPayProductMap = new HashMap<String,String>();
        }
        //获取当前时间戳
        long timeStamp = System.currentTimeMillis();
        SuperLog.info2SD(TAG, "record time"+timeStamp);
        alreadyPayProductMap.put(productId,timeStamp+"");
        List<Profile> profileList = SessionService.getInstance().getSession().getProfileList();
        String id  = profileList.get(0).getID();
        if (!id.equals(userId)){
            //记录当前用户
            SuperLog.info2SD(TAG, "record user");
            userId = id;
        }
    }

    //判断是否据上次订购该产品包有30秒以上
    public boolean canPay(String productId){
        if (null == alreadyPayProductMap){
            SuperLog.info2SD(TAG, "map = null");
            return true;
        }
        List<Profile> profileList = SessionService.getInstance().getSession().getProfileList();
        if (profileList != null && profileList.size() != 0) {
            String id  = profileList.get(0).getID();
            if (!id.equals(userId)){
                //两次的用户id不一致，清空保存的产品包订购时间
                SuperLog.info2SD(TAG, "Switch User,clear Map");
                userId = id;
                if (null != alreadyPayProductMap){
                    alreadyPayProductMap = null;
                }
                return true;
            }
        }

        long timeStampNow = System.currentTimeMillis();

        String timeStampStr = alreadyPayProductMap.get(productId);
        SuperLog.info2SD(TAG, "get recorded time "+timeStampStr+"  and now time "+ timeStampNow);
        if (null == timeStampStr || TextUtils.isEmpty(timeStampStr)){
            return true;
        }else{
            long timeStampLastTime = Long.parseLong(timeStampStr);

            int time = 30;
            String timeStr = SessionService.getInstance().getSession().getTerminalConfigurationAvoidRepeatPaymentTime();
            if (null != timeStr && !TextUtils.isEmpty(timeStr) && isNumeric(timeStr)){
                time = Integer.valueOf(timeStr);
            }

            //时间相差小于30秒
            if (timeStampNow - timeStampLastTime <= time *1000){
                return false;
            }else{
                return true;
            }
        }
    }

    //判断是否全是数字
    private boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}
