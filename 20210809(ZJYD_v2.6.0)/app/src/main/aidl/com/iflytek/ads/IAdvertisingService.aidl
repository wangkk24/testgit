// IAdvertisingService.aidl
package com.iflytek.ads;

import com.iflytek.ads.IVideoControl;

// Declare any non-default types here with import statements

interface IAdvertisingService {

    void setExtParams(String ext);

    void showAd(String vid, int playTime);

    void playStateChange(String videoInfo, int openType, int playTime);

    void setVideoControl(in IVideoControl videoControl);

}
