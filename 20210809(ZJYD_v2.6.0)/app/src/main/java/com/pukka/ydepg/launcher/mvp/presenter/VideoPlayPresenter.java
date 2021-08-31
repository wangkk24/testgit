package com.pukka.ydepg.launcher.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6request.QuerySubjectDetailRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySubjectDetailResponse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.mvp.contact.VideoPlayContact;
import com.pukka.ydepg.launcher.util.RxCallBack;

import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class VideoPlayPresenter extends BasePresenter<VideoPlayContact.IVideoPlayView> implements VideoPlayContact.IVideoPlayPresenter {

    private static final String TAG = VideoPlayPresenter.class.getSimpleName();

    @Override
    public void initVideoTrailMarketingContent(QuerySubjectDetailRequest request, Context context) {
        RxCallBack<QuerySubjectDetailResponse> callBack = new RxCallBack<QuerySubjectDetailResponse>(mView,HttpConstant.QUERYSUBJECT_DETAIL, context) {
            @Override
            public void onSuccess(@NonNull QuerySubjectDetailResponse response) {
                String returnCode = response.getResult().getRetCode() + "";
                if (Result.RETCODE_OK.equals(returnCode)) {
                    Subject subject = response.getSubjectList().get(0);
                    if (subject != null) {
                        Picture picture = subject.getPicture();
                        if (picture != null) {
                            List<String> posters = picture.getPosters();
                            if (posters != null && posters.size() > 0) {
                                String trailVideoMarketingImageURL = posters.get(0);
                                if(!TextUtils.isEmpty(trailVideoMarketingImageURL)) {
                                    SuperLog.info2SD(TAG, "Query marketing content picture url succeeded. The picture url is:"+trailVideoMarketingImageURL);
                                    OTTApplication.getContext().setVideoTrialMarketingContentImageURL(trailVideoMarketingImageURL);
                                    mView.initVideoTrailMarketingContent(trailVideoMarketingImageURL);
                                }
                            } else {
                                SuperLog.error(TAG,"Query marketing content picture url failed. The property 'posters' of picture object is:"+posters);
                            }
                        } else {
                            SuperLog.error(TAG, "Query marketing content picture url failed. Picture object is null");
                        }
                    } else {
                        SuperLog.error(TAG, "Query marketing subject failed. Could not resolve the subject of subject id:" +request.getSubjectIds()+ " The result is null.");
                    }
                } else {
                    SuperLog.error(TAG, "Query marketing subject failed. error code: %s, message:"+returnCode+"|"+response.getResult().getRetMsg());
                }
            }

            @Override
            public void onFail(@NonNull Throwable e) {
                SuperLog.error(TAG, e);
            }
        };

        String url = HttpUtil.getVspUrl(HttpConstant.QUERYSUBJECT_DETAIL);
        LifecycleTransformer transformer = mView.bindToLife();
        HttpApi.getInstance().getService().QuerySubjectDetail(url, request).compose(onCompose(transformer)).subscribe(callBack);
    }

}
