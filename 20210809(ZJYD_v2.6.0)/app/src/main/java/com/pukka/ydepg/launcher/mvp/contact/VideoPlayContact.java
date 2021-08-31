package com.pukka.ydepg.launcher.mvp.contact;

import android.content.Context;

import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6request.QuerySubjectDetailRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;

import java.util.List;

/**
 * 视频播放界面Contact
 */
public interface VideoPlayContact {
    interface IVideoPlayView extends IBaseContact.IBaseView {
        void initVideoTrailMarketingContent(String trailVideoMarketingImageURL);
    }

    interface IVideoPlayPresenter extends IBaseContact.IBasePresenter {

        void initVideoTrailMarketingContent(QuerySubjectDetailRequest request, Context context);
    }
}
