/*
 *Copyright (C) 2017 广州易杰科技, Inc.
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package com.pukka.ydepg.moudule.player.util;

import android.util.ArrayMap;
import android.view.KeyEvent;

/**
 * 遥控器键盘按键对应的code
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: RemoteKeyEvent
 * @Package com.pukka.ydepg.moudule.player.util
 * @date 2018/01/11 11:07
 */
public class RemoteKeyEvent {

  /**
   * 直播
   */
  public static final int BTV = 1181;

  /**
   * 点播
   */
  public static final int VOD = 1182;

  /**
   * 回看
   */
  public static final int TVOD = 1184;

  /**
   * VOD快进
   */
  public static final int VOD_FAST_FORWARD = KeyEvent.KEYCODE_MEDIA_FAST_FORWARD;

  /**
   * VOD快退
   */
  public static final int VOD_FAST_REWIND = KeyEvent.KEYCODE_MEDIA_REWIND;

  /**
   * 媒体播放暂停
   */
  public static final int MEDIA_PAUSE_PLAY = KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;

  /**
   * 频道+
   */
  public static final int CHANNEL_UP = KeyEvent.KEYCODE_CHANNEL_UP;

  /**
   * 频道-
   */
  public static final int CHANNEL_DOWN = KeyEvent.KEYCODE_CHANNEL_DOWN;

  /**
   * 在直播界面切换出Infoview菜单
   */
  public static final int CHANNEL_MENU = KeyEvent.KEYCODE_MENU;

  /**
   * 显示直播频道页
   */
  public static final int SHOW_EPGVIEW = KeyEvent.KEYCODE_DPAD_CENTER;

  private static RemoteKeyEvent mKeyEvent;

  private ArrayMap<Integer, Integer> mBaseKeyCodeValue = new ArrayMap<>();

  private ArrayMap<Integer, Integer> mVODKeyCodeValue = new ArrayMap<>();

  private ArrayMap<Integer, Integer> mBTVKeyCodeValue = new ArrayMap<>();

  public int getKeyCodeValue(int code) {
    if (mBaseKeyCodeValue.containsKey(code)) {
      return mBaseKeyCodeValue.get(code);
    }
    return code;
  }

  public int getVODKeyCodeValue(int code) {
    if (mVODKeyCodeValue.containsKey(code)) {
      return mVODKeyCodeValue.get(code);
    }
    return code;
  }

  public int getBTVKeyCodeValue(int code) {
    if (mBTVKeyCodeValue.containsKey(code)) {
      return mBTVKeyCodeValue.get(code);
    }
    return code;
  }

  public static synchronized RemoteKeyEvent getInstance() {
    if (null == mKeyEvent) {
      mKeyEvent = new RemoteKeyEvent();
    }
    return mKeyEvent;
  }

  private RemoteKeyEvent() {
    //BTV 直播
    mBaseKeyCodeValue.put(BTV, BTV);
    mBaseKeyCodeValue.put(KeyEvent.KEYCODE_PROG_RED, BTV);

    //TVOD 回看
    mBaseKeyCodeValue.put(TVOD, TVOD);
    mBaseKeyCodeValue.put(KeyEvent.KEYCODE_PROG_GREEN, TVOD);

    //点播
    mBaseKeyCodeValue.put(VOD, VOD);
    mBaseKeyCodeValue.put(KeyEvent.KEYCODE_PROG_YELLOW, VOD);

    //0-9按键
    mBaseKeyCodeValue.put(KeyEvent.KEYCODE_0, 0);
    mBaseKeyCodeValue.put(KeyEvent.KEYCODE_1, 1);
    mBaseKeyCodeValue.put(KeyEvent.KEYCODE_2, 2);
    mBaseKeyCodeValue.put(KeyEvent.KEYCODE_3, 3);
    mBaseKeyCodeValue.put(KeyEvent.KEYCODE_4, 4);
    mBaseKeyCodeValue.put(KeyEvent.KEYCODE_5, 5);
    mBaseKeyCodeValue.put(KeyEvent.KEYCODE_6, 6);
    mBaseKeyCodeValue.put(KeyEvent.KEYCODE_7, 7);
    mBaseKeyCodeValue.put(KeyEvent.KEYCODE_8, 8);
    mBaseKeyCodeValue.put(KeyEvent.KEYCODE_9, 9);

    //VOD快进,快退
    mVODKeyCodeValue.put(KeyEvent.KEYCODE_DPAD_RIGHT, VOD_FAST_FORWARD);
    mVODKeyCodeValue.put(KeyEvent.KEYCODE_PAGE_DOWN, VOD_FAST_FORWARD);
    mVODKeyCodeValue.put(KeyEvent.KEYCODE_DPAD_LEFT, VOD_FAST_REWIND);
    mVODKeyCodeValue.put(KeyEvent.KEYCODE_PAGE_UP, VOD_FAST_REWIND);

    //VOD媒体播放暂停code
    mVODKeyCodeValue.put(KeyEvent.KEYCODE_DPAD_CENTER, MEDIA_PAUSE_PLAY);
    mVODKeyCodeValue.put(KeyEvent.KEYCODE_ENTER, MEDIA_PAUSE_PLAY);
    mVODKeyCodeValue.put(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, MEDIA_PAUSE_PLAY);

    //频道+
    mBTVKeyCodeValue.put(KeyEvent.KEYCODE_CHANNEL_UP, CHANNEL_UP);
    mBTVKeyCodeValue.put(KeyEvent.KEYCODE_DPAD_UP, CHANNEL_UP);

    //频道-
    mBTVKeyCodeValue.put(KeyEvent.KEYCODE_CHANNEL_DOWN, CHANNEL_DOWN);
    mBTVKeyCodeValue.put(KeyEvent.KEYCODE_DPAD_DOWN, CHANNEL_DOWN);

    //channel menu
//    mBTVKeyCodeValue.put(KeyEvent.KEYCODE_DPAD_LEFT, CHANNEL_MENU);
    mBTVKeyCodeValue.put(KeyEvent.KEYCODE_MENU, CHANNEL_MENU);

    //直播频道页
//    mBTVKeyCodeValue.put(KeyEvent.KEYCODE_DPAD_RIGHT, SHOW_EPGVIEW);
    //新增两个keycode对应呼唤出来频道页
    mBTVKeyCodeValue.put(KeyEvent.KEYCODE_DPAD_CENTER, SHOW_EPGVIEW);
    mBTVKeyCodeValue.put(KeyEvent.KEYCODE_ENTER, SHOW_EPGVIEW);
//    mBTVKeyCodeValue.put(KeyEvent.KEYCODE_MENU, SHOW_EPGVIEW);
}
}
