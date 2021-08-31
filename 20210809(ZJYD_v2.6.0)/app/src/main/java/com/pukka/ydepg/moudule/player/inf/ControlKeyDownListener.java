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
package com.pukka.ydepg.moudule.player.inf;

import android.view.KeyEvent;

/**
 * 方便fragment中实现这两个方法;
 * activity监听然后过渡给fragment
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: ControlKeyDownListener
 * @Package com.pukka.ydepg.moudule.player.inf
 * @date 2017/12/21 14:47
 */
public interface ControlKeyDownListener {
  boolean onKeyDown(int keyCode, KeyEvent event);
  void onKeyUp(int keyCode, KeyEvent event);
}
