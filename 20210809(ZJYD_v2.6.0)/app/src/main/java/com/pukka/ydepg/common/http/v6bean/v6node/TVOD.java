/*
 *Copyright (C) 2018 广州易杰科技, Inc.
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
package com.pukka.ydepg.common.http.v6bean.v6node;

import android.text.TextUtils;
import java.util.List;

/**
 * 回看VOD
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: TVOD
 * @Package com.pukka.ydepg.common.http.v6bean.v6node
 * @date 2018/01/22 13:44
 */
public class TVOD {
  private String totalCount;
  private List<VOD> vodList;

  public String getTotalCount() {
    if(TextUtils.isEmpty(totalCount)){
      return "0";
    }
    return totalCount;
  }

  public void setTotalCount(String totalCount) {
    this.totalCount = totalCount;
  }

  public List<VOD> getVodList() {
    return vodList;
  }

  public void setVodList(List<VOD> vodList) {
    this.vodList = vodList;
  }

  public TVOD(String totalCount, List<VOD> vodList) {
    this.totalCount = totalCount;
    this.vodList = vodList;
  }
}
