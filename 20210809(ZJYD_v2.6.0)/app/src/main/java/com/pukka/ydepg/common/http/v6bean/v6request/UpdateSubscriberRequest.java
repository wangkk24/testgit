package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscriber;

/**
 * Created by liudo on 2018/5/15.
 */

public class UpdateSubscriberRequest {

    @SerializedName("subscriber")
    private Subscriber mSubscriber;

    public Subscriber getmSubscriber() {
        return mSubscriber;
    }

    public void setmSubscriber(Subscriber mSubscriber) {
        this.mSubscriber = mSubscriber;
    }
}
