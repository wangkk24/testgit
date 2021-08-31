package com.pukka.ydepg.common.upgrade;

public interface IUpgradeListener {
    //升级流程结束回调
    void onFinish();

    //执行非强制升级流程回调
    void onOptionalUpgrade();
}
