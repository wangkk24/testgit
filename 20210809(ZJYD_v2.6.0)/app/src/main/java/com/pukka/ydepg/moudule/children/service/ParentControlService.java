package com.pukka.ydepg.moudule.children.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.huawei.stb.child.ParentControlCache;
import com.pukka.ydepg.common.http.bean.node.ParentControlData;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.moudule.vod.cache.VoddetailUtil;

import java.util.ArrayList;
import java.util.List;


public class ParentControlService extends Service {

    public static final String TAG = "ParentControlService";

    private List<String> types=new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        types.add(VoddetailUtil.REST_BY_EPSIODE);
        types.add(VoddetailUtil.REST_BY_TODAY);
        types.add(VoddetailUtil.REST_BY_SINGLE);
        return mBinder;
    }




  private final  ParentControlCache.Stub mBinder=new ParentControlCache.Stub() {

      //获得锁屏剩余时间
      @Override
      public long getLockScreenRestTime() throws RemoteException {
          try{
              return VoddetailUtil.getInstance().getRestTime();
          }catch (Exception e){
              throw new RemoteException("getLockScreenRestTime is error");
          }

      }
      //获得锁屏类型
      @Override
      public String getLockScreenType() throws RemoteException {
          try{
              return SharedPreferenceUtil.getInstance().getlockScreenType();
          }catch (Exception e){
              throw new RemoteException("getLockScreenType is error");
          }
      }
      //获得父母控制数据
      @Override
      public String getParentCenterData() throws RemoteException {
          try{
              return SharedPreferenceUtil.getInstance().getParentCenterDataStr();
          }catch (Exception e){
              throw new RemoteException("getParentCenterData is error");
          }

      }
      //获得当前已用单个时长
      @Override
      public String getUsedSingleTime() throws RemoteException {
          try{
              return SharedPreferenceUtil.getInstance().getCurrentSingleTime();
          }catch (Exception e){
              throw new RemoteException("getUsedSingleTime is error");
          }

      }
      //获得当前已用一天总时长
      @Override
      public String getUsedDayAllTime() throws RemoteException {
          try{
              return SharedPreferenceUtil.getInstance().getCurrentAllTime();
          }catch (Exception e){
              throw new RemoteException("getUsedDayAllTime is error");
          }

      }
      //获得锁屏时间戳
      @Override
      public String getLockScreenTimeStamp() throws RemoteException {
          try {
              return SharedPreferenceUtil.getInstance().getLockScreenPoint();
          }catch (Exception e){
              throw new RemoteException("getLockScreenTimeStamp is error");
          }
      }
      //获得观看本集剩余时长
      @Override
      public long getEpsiodePlayAllTime() throws RemoteException {
          try {
              return VoddetailUtil.getInstance().getEpsiodePlayTime();
          }catch (Exception e){
              throw new RemoteException("getEpsiodePlayAllTime is error");
          }
      }
      //获得观看本集已用时长
      @Override
      public long epsiodePlayedTime() throws RemoteException {
          try{
              return VoddetailUtil.getInstance().getEpsiodePlayedTime();
          }catch (Exception e){
              throw new RemoteException("epsiodePlayedTime is error");
          }

      }
      //上报累计时长
      @Override
      public void setChildUsedTime(String LockScreenType,long usedMillisecond) throws RemoteException {
          SuperLog.debug(TAG,"上报累计时长setChildUsedTime"+"LockScreenType="+LockScreenType+";usedMillisecond="+usedMillisecond);
          if(!types.contains(LockScreenType)){
              throw  new RemoteException("setChildUsedTime the type is error");
          }
          if(VoddetailUtil.REST_BY_EPSIODE.equals(LockScreenType)){
              VoddetailUtil.getInstance().setEpsiodePlayedTime(usedMillisecond);
          }else if(VoddetailUtil.REST_BY_TODAY.equals(LockScreenType)){
                SharedPreferenceUtil.getInstance().setCurrentAllTime(usedMillisecond);
          }else if(VoddetailUtil.REST_BY_SINGLE.equals(LockScreenType)){
                SharedPreferenceUtil.getInstance().setCurrentSingleTime(usedMillisecond);
          }
      }
      //设置锁屏
      @Override
      public void setLockScreen(String LockScreenType) throws RemoteException {
          if(!types.contains(LockScreenType)){
            throw  new RemoteException(" setLockScreen the type is error");
          }
          SuperLog.debug(TAG,"setLockScreen--LockScreenType is "+LockScreenType);
          VoddetailUtil.getInstance().startRestTimeDecrement(LockScreenType);
      }
      //设置解锁
      @Override
      public void setUnLockScreen(String LockScreenType) throws RemoteException {
          if(!types.contains(LockScreenType)){
              throw  new RemoteException("setUnLockScreen the type is error");
          }
          SuperLog.debug(TAG,"setUnLockScreen--LockScreenType is "+LockScreenType);
         VoddetailUtil.getInstance().stopRestTime(LockScreenType);
      }

      //设置观看本集休息
      @Override
      public void setEpsiodeRest(boolean epsiodeRest) throws RemoteException {
          try{
              SuperLog.debug(TAG,"setEpsiodeRest--epsiodeRest is "+epsiodeRest);
              VoddetailUtil.getInstance().setEpsiodeRest(epsiodeRest);
          }catch (Exception e){
              throw  new RemoteException("setEpsiodeRest is error");
          }

      }

      //  //是否为本集休息
      @Override
      public boolean isEpsiodeRest() throws RemoteException {
          try{
              return VoddetailUtil.getInstance().isEpsiodeRest();
          }catch (Exception e){
              throw new RemoteException("isEpsiodeRest is error");
          }
      }

      //设置解锁
      @Override
      public void setParentCenterData(String jsonStr) throws RemoteException {
          SuperLog.debug(TAG,"setParentCenterData--jsonStr is: "+jsonStr);
         ParentControlData   data= JsonParse.json2Object(jsonStr, ParentControlData.class);
         if(null==data){
             throw new RemoteException("setParentCenterData the data json is wrong");
         }
         if(!isInteger(data.getOnOrOff())){
             throw new RemoteException("setParentCenterData the switch  is not integer");
         }

         if(!isInteger(data.getAlltime())){
             throw new RemoteException("setParentCenterData the alltime  is not integer");
         }

          if(!isInteger(data.getSingletime())){
              throw new RemoteException("setParentCenterData the alltime  is not integer");
          }
//          if(!TextUtils.isEmpty(data.getBirthday())){
//              if(data.getBirthday().trim().length()!=10||!isInteger(data.getBirthday().substring(0, 4))||!isInteger(data.getBirthday().substring(5, 7))||!isInteger(data.getBirthday().substring(8, 10))){
//                  throw new RemoteException("setParentCenterData the Data  is wrong");
//              }
//          }
          SharedPreferenceUtil.getInstance().putString(SharedPreferenceUtil.Key.SAVE_PARENT_CENTER_DATA + "", jsonStr);;
      }

      //设置观看本集休息
      @Override
      public void setEpsiodePlayAllTime(long epsiodePlayTime) throws RemoteException {
          try{
              SuperLog.debug(TAG,"setEpsiodePlayAllTime--epsiodePlayTime is: "+epsiodePlayTime);
              VoddetailUtil.getInstance().setEpsiodeAllTime(epsiodePlayTime);
          }catch (Exception e){
              throw  new RemoteException("setEpsiodePlayAllTime is error");
          }
      }
  };

    public boolean  isInteger(String str){
        if(TextUtils.isEmpty(str)){
           return true;
        }
        try {
            int m=Integer.parseInt(str);
            return true;
        }catch (Exception e){
            return false;
        }
    }


}
