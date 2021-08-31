package com.pukka.ydepg.common.http.v6bean.v6node.multplay;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * 作者：panjw on 2021/6/11 15:53
 * <p>
 * 邮箱：panjw@easier.cn
 */
public class MarketInfo {
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

}
