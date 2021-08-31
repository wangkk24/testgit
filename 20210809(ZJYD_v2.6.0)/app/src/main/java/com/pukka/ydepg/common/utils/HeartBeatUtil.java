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
package com.pukka.ydepg.common.utils;

import android.text.TextUtils;

import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.service.HeartBeatService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 心跳工具类
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: HeartBeatUtil
 * @Package com.pukka.ydepg.common.utils
 * @date 2018/01/17 13:57
 */
public class HeartBeatUtil {
    private static final String TAG = HeartBeatUtil.class.getSimpleName();

    private static volatile HeartBeatUtil mInstance;

    private HeartBeatUtil() { }

    public static HeartBeatUtil getInstance() {
        if (null == mInstance) {
            synchronized (HeartBeatUtil.class) {
                if (null == mInstance) {
                    mInstance = new HeartBeatUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 刷新下一个心跳周期数据
     *
     * @param nextCallInterval 下一次心跳周期
     */
    public void refreshNextCallInterval(String nextCallInterval) {
        SessionService.getInstance().getSession().setHeartbitInterval(nextCallInterval);
        SessionService.getInstance().commitSession();
    }

    /**
     * 获取存储的心跳周期
     */
    public long getNextCallInterval() {
        return SessionService.getInstance().getSession().getHeartbitInterval();
    }

    /**
     * 判断当前本地版本和personalData获取的version是否不一样
     *
     * @param type                type
     * @param personalDataVersion personalDataVersion
     * @return true表示版本不一致需要更新版本, false表示两个版本一致不需要更新版本
     */
    public boolean isVersionChange(String type, String personalDataVersion) {
        String localVersion = getVersion(type);
        boolean result = !localVersion.equals(personalDataVersion);
        SuperLog.info2SD(TAG, "[TYPE:" + type + "] localVersion=" + localVersion + " platformVersion="+personalDataVersion + "version change="+result);
        return result;
    }

    /**
     * 设置心跳维护某个接口的版本号
     *
     * @param type    type
     * @param version personalDataVersion
     * @see HeartBeatService.VersionType
     */
    public void setVersion(String type, String version) {
        SharedPreferenceUtil.getInstance().putString(type, version);
    }

    /**
     * 获取心跳维护某个接口的版本号
     *
     * @param type type
     */
    public String getVersion(String type) {
        return SharedPreferenceUtil.getStringData(type, "-1");
    }

    /**
     * 判断是否订购
     *
     * @param vod
     * @return true为已订购
     */
    public boolean isSubscribedByPrice(VOD vod, String fatherPrice) {
        if (null == vod) {
            return false;
        }
        String price = vod.getPrice();
        if (TextUtils.isEmpty(price)) {
            price = fatherPrice;
        }
        if (TextUtils.isEmpty(price)) {
            return true;
        } else {
            double dPrice = Double.parseDouble(price);
            if (0 == dPrice) {
                return true;
            } else {
                return "1".equals(vod.getIsSubscribed());
            }

        }

    }

    /**
     * fatherVod显示VIP问题
     *
     * @param vod
     * @return 0 显示订购
     * 1 不显示
     * 2 显示已订购
     */
    public int isShowBuyTag(VOD vod) {
        if (null == vod) {
            return 0;
        }
        String price = vod.getPrice();
        if (TextUtils.isEmpty(price)) {
            return 1;
        } else {
            double pricet = Double.parseDouble(price);
            if (0 == pricet) {
                return 1;
            } else {
                return "1".equals(vod.getIsSubscribed()) ? 2 : 0;
            }

        }

    }

    public List<Product> getProductsBySort(List<Product> productList) {
        List<String> preposeProductIDs = SessionService.getInstance().getSession().getTerminalConfigurationPreposeProductIDs();
        List<String> postPositionProductIDs = SessionService.getInstance().getSession().getTerminalConfigurationPostPositionProductIDs();
        SuperLog.debug(TAG, "getProductsBySort->productIds:" + preposeProductIDs+"|postPositionProductIDs:"+postPositionProductIDs);
        if (null != preposeProductIDs && preposeProductIDs.size() > 0 && null != productList && productList.size() > 0) {
            for (int i = 0; i < productList.size(); i++) {
                String productId = productList.get(i).getID();
                if (!TextUtils.isEmpty(productId)) {
                    for (int j = 0; j < preposeProductIDs.size(); j++) {
                        if (productId.equals(preposeProductIDs.get(j))) {
                            productList.get(i).setOrder(j);
                            break;
                        }
                    }
                }
            }
        }
        if (null != postPositionProductIDs && postPositionProductIDs.size() > 0 && null != productList && productList.size() > 0) {
            for (int i = 0; i < productList.size(); i++) {
                String productId = productList.get(i).getID();
                if (!TextUtils.isEmpty(productId)) {
                    for (int j = 0; j < postPositionProductIDs.size(); j++) {
                        if (productId.equals(postPositionProductIDs.get(j))) {
                            productList.get(i).setOrder(productList.get(i).getOrder()+j+1);
                            break;
                        }
                    }
                }
            }
        }

        if (null != productList){
            Collections.sort(productList, new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    if (o1.getOrder() < o2.getOrder()) {
                        return -1;
                    } else if (o1.getOrder() > o2.getOrder()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
        }
        return productList;
    }
}