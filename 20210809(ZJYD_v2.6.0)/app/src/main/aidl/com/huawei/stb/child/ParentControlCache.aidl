// ParentControlCache.aidl
package com.huawei.stb.child;

// Declare any non-default types here with import statements

interface ParentControlCache {
    //获得锁屏剩余时间
    long getLockScreenRestTime();
    //获得锁屏类型
    String getLockScreenType();
    //获得父母控制数据
    String getParentCenterData();
    //获得当前已用单个时长
    String getUsedSingleTime();
    //获得当前已用一天总时长
    String getUsedDayAllTime();
    //获得锁屏时间戳
    String getLockScreenTimeStamp();
     //获得观看本集剩余时长
    long getEpsiodePlayAllTime();
     //获得观看本集已用时长
    long epsiodePlayedTime();
     //上报累计时长
    void setChildUsedTime(String LockScreenType,long usedMillisecond);
    //设置锁屏
    void setLockScreen(String LockScreenType);
    //设置解锁
    void setUnLockScreen(String LockScreenType);
    //设置观看本集休息
    void setEpsiodeRest(boolean epsiodeRest);
    //是否为本集休息
    boolean isEpsiodeRest();
    //设置父母控制数据
    void setParentCenterData(String jsonStr);
    //设置观看本集剩余时长 单位为秒
    void setEpsiodePlayAllTime(long epsiodePlayTime);

}
