package com.pukka.ydepg.moudule.vrplayer.vrplayer.global;

/**
 * 功能描述 封装消息what
 *
 * @author l00477311
 * @since 2020-08-05
 */
public interface MessageType {
    interface MenuControlMessage {
        int BASE = 0x0100;

        /**
         * 同步进度
         */
        int MESSAGE_SHOW_PROGRESS = BASE + 1;

        /**
         * 3秒无操作，隐藏菜单面板
         */
        int MESSAGE_HIDE_MENU = BASE + 2;

        /**
         * 开启倒计时
         */
        int MESSAGE_START_COUNTDOWN = BASE + 3;

        /**
         * 播放下个视频
         */
        int MESSAGE_PLAY_NEXT = BASE + 4;

        /**
         * 向前拖动播放进度条
         */
        int MESSAGE_DRAG_SEEKBAR_FORWOARD = BASE + 5;

        /**
         * 向后拖动播放进度条
         */
        int MESSAGE_DRAG_SEEKBAR_REWIND = BASE + 6;

        /**
         * 起播
         */
        int MESSAGE_START_PLYAER = BASE + 7;
    }

    interface MPControlMessage {
        int BASE = 0x0200;

        int MESSAGE_MPCONTROL_SEEK_TO = BASE + 1;
    }

    interface FovControlMessage {
        int BASE = 0x0300;

        int MESSAGE_FOVCONTROL_SINGLE = BASE + 1;

        int MESSAGE_FOVCONTROL_NEARCSCALE = BASE + 2;

        int MESSAGE_FOVCONTROL_RESET = BASE + 3;
    }
}
