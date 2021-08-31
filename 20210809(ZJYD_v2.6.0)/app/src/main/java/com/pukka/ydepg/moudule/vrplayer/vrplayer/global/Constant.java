package com.pukka.ydepg.moudule.vrplayer.vrplayer.global;

import android.view.KeyEvent;

/**
 * 功能描述
 *
 * @author l00477311
 * @since 2020-08-05
 */
public interface Constant {
    /**
     * 视频列表配置文件
     */
    String FILE_NAME = "videoList.json";

    /**
     * 直播
     */
    int BTV = 1;

    /**
     * 点播
     */
    int VOD = 2;

    /**
     * 3D 上下格式
     */
    int STEREO_VERTICAL = 1;

    /**
     * 3D左右格式
     */
    int STEREO_HORIZONTAL = 2;

    /**
     * VR180的2D视频
     */
    int TWO_DIM_180 = 3;

    /**
     * 消息循环时间(ms)
     */
    int LOOP_SETP_TIME = 50;

    /**
     * 线程睡眠时间(ms)
     */
    int SLEEP_TIME = 16;

    String VR_JSON = "vrJson";

    enum FovType {
//        UP(KeyEvent.KEYCODE_DPAD_UP),
//        DOWN(KeyEvent.KEYCODE_DPAD_DOWN),
//        LEFT(KeyEvent.KEYCODE_DPAD_LEFT),
//        RIGHT(KeyEvent.KEYCODE_DPAD_RIGHT);

        //KEYCODE_DPAD_UP  -- > KEYCODE_2
        //KEYCODE_DPAD_DOWN  -- > KEYCODE_8
        //KEYCODE_DPAD_LEFT  -- > KEYCODE_4
        //KEYCODE_DPAD_RIGHT  -- > KEYCODE_6
        UP(KeyEvent.KEYCODE_2),
        DOWN(KeyEvent.KEYCODE_8),
        LEFT(KeyEvent.KEYCODE_4),
        RIGHT(KeyEvent.KEYCODE_6);

        private int value;

        FovType(int value) {
            this.value = value;
        }

        public static FovType fromKeyCode(int keyCode) {
            for (FovType fovType : FovType.values()) {
                if (fovType.value == keyCode) {
                    return fovType;
                }
            }
            return LEFT;
        }
    }

    /**
     * 从EPG模板拉起apk，退出时要发送ReturnURL
     */
    String URL = "Url";

}
