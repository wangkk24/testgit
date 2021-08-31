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
package com.pukka.ydepg.moudule.livetv.utils;

import android.text.TextUtils;

import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 直播缓存数据工具类,有分页的也只缓存第一页
 *
 * 缓存TVGUIDE页
 * {
 *   栏目列表,
 *   回看VOD列表,
 *   回看剧集列表,
 *   当天及未来7天的节目单列表数据
 * }
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: LiveTVCacheUtil
 * @Package com.pukka.ydepg.moudule.livetv.utils
 * @date 2018/01/16 09:37
 */
public class LiveTVCacheUtil {
  /**
   * TAG object
   */
  private static final String TAG="LiveTVCacheUtil";

  /**
   * 直播数据缓存,分隔匹配的字符串
   */
  private static final String COMMON_REGEX="@zjyd";

  /**
   * 缓存栏目的key
   */
  public  static final String CACHE_COLUMN_KEY="cache_columnlist";

  /**
   * 记录上一次播放的频道信息[频道ID,媒体资源ID] 用于再次进入LiveTV后跳转到上次播放的频道继续播放
   */
  public static final String RECORD_CHANNELINFO="record_channelinfo";

  /**
   * 分隔字符
   */
  private Pattern mPattern=Pattern.compile(COMMON_REGEX);

  private static LiveTVCacheUtil mCacheUtil;

  private LiveTVCacheUtil(){}

  public static synchronized LiveTVCacheUtil getInstance(){
    if(null==mCacheUtil){
      mCacheUtil=new LiveTVCacheUtil();
    }
    return mCacheUtil;
  }


  /**
   * (加密)缓存栏目列表数据
   * @param subjectList
   */
  public void cacheColumnList(List<Subject> subjectList)
  {
    if (null != subjectList)
    {
      SuperLog.info2SD(TAG, "[cacheColumnList] size :" + subjectList.size());
    }
    else
    {
      SuperLog.error(TAG, "[cacheColumnList] is null");
    }
    SharedPreferenceUtil.getInstance().putString(CACHE_COLUMN_KEY, System.currentTimeMillis() + COMMON_REGEX + JsonParse.classListToJson(subjectList, Subject.class));
  }

  /**
   * 记录最后一次直播鉴权信息
   * @param channelID
   */
  public void recordPlayChannelInfo(String channelID,String mediaID){
    LiveDataHolder.get().setIsChangeMulticastSwitch(false);
    if (TextUtils.isEmpty(channelID) && TextUtils.isEmpty(channelID)){
      SharedPreferenceUtil.getInstance().putString(RECORD_CHANNELINFO,"");
    }else{
      SharedPreferenceUtil.getInstance().putString(RECORD_CHANNELINFO,channelID+COMMON_REGEX+mediaID);
    }
  }

  /**
   * 返回最后一次直播鉴权信息
   * @return
   */
  public String[] getRecordPlayChannelInfo(){
    String json=SharedPreferenceUtil.getStringData(RECORD_CHANNELINFO,"");
    if(!TextUtils.isEmpty(json)){
      return mPattern.split(json);
    }
    return null;
  }
}