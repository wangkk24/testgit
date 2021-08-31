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
package com.pukka.ydepg.common.extview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: ScrollViewExt
 * @Package com.pukka.ydepg.common.extview
 * @date 2017/09/25 10:28
 */
public class ScrollViewExt extends ScrollView {
  public ScrollViewExt(Context context) {
    super(context);
  }

  public ScrollViewExt(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ScrollViewExt(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }
}
