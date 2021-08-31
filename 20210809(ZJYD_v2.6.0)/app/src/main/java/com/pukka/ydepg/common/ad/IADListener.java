package com.pukka.ydepg.common.ad;

import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertContent;

import java.util.List;

public interface IADListener {
    void onSuccess(List<AdvertContent> listAdvertContent);
    void onFail();
}
