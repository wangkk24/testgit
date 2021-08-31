package com.pukka.ydepg.moudule.vrplayer.vrplayer.logic;

import android.os.SystemClock;

import com.asha.vrlib.MDVRLibrary;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.global.AreaSpecialProperties;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.global.Constant;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.utils.LogUtil;

/**
 * 功能描述 fov巡航
 *
 * @since 2020-08-08
 */
public class FovCruiseRunnable implements Runnable {
    private static final String TAG = "FovCruiseRunnable";
    private MDVRLibrary mMDVRLibrary;

    /**
     * 是否停止标志
     */
    private volatile boolean isStop;

    /**
     * FOV调整类型
     */
    private volatile Constant.FovType fovtype;

    public FovCruiseRunnable(MDVRLibrary mdvrLibrary) {
        mMDVRLibrary = mdvrLibrary;
    }

    @Override
    public void run() {
        while (!isStop) {
            LogUtil.logI(TAG, "Enter fov cruise runnable, fovtype=" + fovtype.name());
            float yaw = mMDVRLibrary.updateCamera().getYaw();
            float pitch = mMDVRLibrary.updateCamera().getPitch();
            float deta = AreaSpecialProperties.getFovCruiseSpeed() / 60.0f;
            switch (fovtype) {
                case UP:
                    yaw -= deta;
                    yaw = yaw < -90 ? -90 : yaw;
                    break;
                case DOWN:
                    yaw += deta;
                    yaw = yaw > 90 ? 90 : yaw;
                    break;
                case LEFT:
                    pitch -= deta;
                    break;
                case RIGHT:
                    pitch += deta;
                    break;
            }
            mMDVRLibrary.updateCamera().setPitch(pitch % 360).setYaw(yaw);
            SystemClock.sleep(Constant.SLEEP_TIME);
        }
        LogUtil.logI(TAG, "Exit fov cruise runnable");
    }

    /**
     * 停止巡航
     *
     * @param flag
     */
    public void setStop(boolean flag) {
        isStop = flag;
    }

    /**
     * 设置方向
     *
     * @param fovtype
     */
    public void setFovtype(Constant.FovType fovtype) {
        this.fovtype = fovtype;
    }
}
