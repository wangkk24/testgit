package com.pukka.ydepg.moudule.vrplayer.vrplayer.logic;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.KeyEvent;

import com.asha.vrlib.MDVRLibrary;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.global.AreaSpecialProperties;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.global.Constant;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.global.MessageType;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.global.NonUIHandlerThread;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.utils.LogUtil;

/**
 * 功能描述 FOV视角控制器
 *
 * @since 2020-08-08
 */
public class FovController implements ILogic {
    private static final String TAG = "FovController";

    private Context mContext;

    private MDVRLibrary mMDVRLibrary;

    private Handler fovCtrHandler = null;

    private FovCruiseRunnable fovCruiseRunnable;

    @Override
    public void init(Context context, Object object) {
        mContext = context;
        if (object instanceof MDVRLibrary) {
            mMDVRLibrary = (MDVRLibrary) object;
        }
        fovCtrHandler = new Handler(NonUIHandlerThread.getNonUILooper(), new FovCtrCallBack());
        fovCruiseRunnable = new FovCruiseRunnable(mMDVRLibrary);
    }

    @Override
    public void sendMessage(Message message) {
        fovCtrHandler.sendMessage(message);
    }

    @Override
    public void sendEmptyMessage(int what) {
        fovCtrHandler.sendEmptyMessage(what);
    }

    @Override
    public void sendMessageDelayed(Message message, long delay) {
        fovCtrHandler.sendMessageDelayed(message, delay);
    }

    @Override
    public void post(Runnable runnable) {
        fovCtrHandler.post(runnable);
    }

    @Override
    public void removeMessage(int what) {
        fovCtrHandler.removeMessages(what);
    }

    @Override
    public void removeCallbacks(Runnable runnable) {
        fovCtrHandler.removeCallbacks(runnable);
    }

    @Override
    public void removeAllMessage() {
        fovCtrHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void destroy() {
        this.mContext = null;
        removeAllMessage();
        fovCtrHandler = null;
        mMDVRLibrary = null;
        fovCruiseRunnable.setStop(true);
        fovCruiseRunnable = null;
    }

    /**
     * 开始fov巡航
     *
     * @param keyCode
     */
    public void startFovCruise(int keyCode) {
        LogUtil.logI(TAG, "start Fov Cruise, pitch =" + mMDVRLibrary.updateCamera().getPitch()
                + ", yaw=" + mMDVRLibrary.updateCamera().getYaw());
        fovCruiseRunnable.setStop(false);
        Constant.FovType fovtype = Constant.FovType.fromKeyCode(keyCode);
        fovCruiseRunnable.setFovtype(fovtype);
        post(fovCruiseRunnable);
    }

    /**
     * 结束fov巡航
     */
    public void stopFovCruise() {
        fovCruiseRunnable.setStop(true);
        SystemClock.sleep(Constant.SLEEP_TIME); // 保证一定结速
        removeCallbacks(fovCruiseRunnable);
        LogUtil.logI(TAG, "stop Fov Cruise, pitch =" + mMDVRLibrary.updateCamera().getPitch()
                + ", yaw=" + mMDVRLibrary.updateCamera().getYaw());
    }

    /**
     * 单次fov转动
     *
     * @param keyCode
     */
    public void singleFovRotate(int keyCode) {
        removeMessage(MessageType.FovControlMessage.MESSAGE_FOVCONTROL_SINGLE);
        Message msg = new Message();
        msg.what = MessageType.FovControlMessage.MESSAGE_FOVCONTROL_SINGLE;
        msg.arg1 = keyCode;
        msg.arg2 = 10;
        sendMessage(msg);
    }

    /**
     * FOV视角是否在默认值
     *
     * @return
     */
    public boolean isFovInDefault(int orignPitch) {
        float curPitch = mMDVRLibrary.updateCamera().getPitch() % 360;
        float curYaw = mMDVRLibrary.updateCamera().getYaw();
        float curNearScale = mMDVRLibrary.updateCamera().getNearScale();
        if (Math.abs(curPitch - orignPitch) < AreaSpecialProperties.getFovCruiseSpeed() / 60f
                && Math.abs(curYaw) < AreaSpecialProperties.getFovCruiseSpeed() / 60f
                && Math.abs(curNearScale) < AreaSpecialProperties.getSingleFovDelegate() / 200f) {
            return true;
        }
        return false;
    }

    // fov视角按帧转动
    private void fovRotatePerFrame(int keyCode) {
        float yaw = mMDVRLibrary.updateCamera().getYaw();
        float pitch = mMDVRLibrary.updateCamera().getPitch();
        float deta = AreaSpecialProperties.getSingleFovDelegate() / 10f;
//        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
//            yaw -= deta;
//            yaw = yaw < -90 ? -90 : yaw;
//        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
//            yaw += deta;
//            yaw = yaw > 90 ? 90 : yaw;
//        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
//            pitch -= deta;
//        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
//            pitch += deta;
//        }

        //KEYCODE_DPAD_UP  -- > KEYCODE_2
        //KEYCODE_DPAD_DOWN  -- > KEYCODE_8
        //KEYCODE_DPAD_LEFT  -- > KEYCODE_4
        //KEYCODE_DPAD_RIGHT  -- > KEYCODE_6
        if (keyCode == KeyEvent.KEYCODE_2) {
            yaw -= deta;
            yaw = yaw < -90 ? -90 : yaw;
        } else if (keyCode == KeyEvent.KEYCODE_8) {
            yaw += deta;
            yaw = yaw > 90 ? 90 : yaw;
        } else if (keyCode == KeyEvent.KEYCODE_4) {
            pitch -= deta;
        } else if (keyCode == KeyEvent.KEYCODE_6) {
            pitch += deta;
        }
        mMDVRLibrary.updateCamera().setYaw(yaw).setPitch(pitch % 360);
    }

    // 处理拉近拉远
    private void dealNearScale(int keyCode) {
        float nearScale = mMDVRLibrary.updateCamera().getNearScale();
        float deta = AreaSpecialProperties.getSingleFovDelegate() / 200f;

        //MARK:TODO -- 功能修改： 将上页、下页  拉远拉近功能改为  上键下键
//        if (keyCode == KeyEvent.KEYCODE_PAGE_UP) { // 拉远
//            nearScale = nearScale - deta;
//            nearScale = nearScale < -0.5f ? -0.5f : nearScale;
//        } else if (keyCode == KeyEvent.KEYCODE_PAGE_DOWN) { // 拉近
//            nearScale = nearScale + deta;
//            nearScale = nearScale > 0.5f ? 0.5f : nearScale;
//        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) { // 拉远
            nearScale = nearScale - deta;
            nearScale = nearScale < -0.5f ? -0.5f : nearScale;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) { // 拉近
            nearScale = nearScale + deta;
            nearScale = nearScale > 0.5f ? 0.5f : nearScale;
        }
        mMDVRLibrary.updateCamera().setNearScale(nearScale);
    }

    private class FovCtrCallBack implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MessageType.FovControlMessage.MESSAGE_FOVCONTROL_SINGLE:
                    LogUtil.logI(TAG, "Receive msg MESSAGE_FOVCONTROL_SINGLE, keyCode=" + msg.arg1);
                    fovRotatePerFrame(msg.arg1);
                    if (msg.arg2 > 1) {
                        Message message = new Message();
                        message.what = MessageType.FovControlMessage.MESSAGE_FOVCONTROL_SINGLE;
                        message.arg1 = msg.arg1;
                        message.arg2 = msg.arg2 - 1;
                        sendMessageDelayed(message, Constant.SLEEP_TIME);
                    } else {
                        LogUtil.logI(TAG, "stop Fov single control, pitch="
                                + mMDVRLibrary.updateCamera().getPitch() + ", yaw=" + mMDVRLibrary.updateCamera().getYaw());
                    }
                    break;
                case MessageType.FovControlMessage.MESSAGE_FOVCONTROL_NEARCSCALE:
                    LogUtil.logI(TAG, "Receive msg MESSAGE_FOVCONTROL_NEARCSCALE, keyCode=" + msg.arg1);
                    dealNearScale(msg.arg1);
                    LogUtil.logI(TAG, "stop camara lens control =" + (1 - mMDVRLibrary.updateCamera().getNearScale()));
                    break;
                case MessageType.FovControlMessage.MESSAGE_FOVCONTROL_RESET:
                    mMDVRLibrary.updateCamera().setPitch(msg.arg1).setYaw(0).setNearScale(0);
                    LogUtil.logI(TAG, "fov reset, pitch=" + mMDVRLibrary.updateCamera().getPitch()
                            + ", yaw=" + mMDVRLibrary.updateCamera().getYaw() + ", nearScale="
                            + mMDVRLibrary.updateCamera().getNearScale());
                    break;
            }
            return false;
        }
    }
}
