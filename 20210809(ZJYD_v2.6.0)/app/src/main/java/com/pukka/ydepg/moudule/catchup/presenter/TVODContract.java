package com.pukka.ydepg.moudule.catchup.presenter;

import android.content.Context;

import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryChannelSubjectListResponse;
import com.pukka.ydepg.launcher.mvp.contact.IBaseContact;

import java.util.List;

import io.reactivex.ObservableTransformer;

public interface TVODContract {

    interface View extends IBaseContact.IBaseView{
        void onQueryChannelSubjectListSuccess();
        void onQueryChannelSubjectListFailed();
        void onQueryChannelStcPropsBySubjectSuccess(List<ChannelDetail> channelDetails,int position);
        void onQueryChannelStcPropsBySubjectFailed();
    }

    interface Presenter<T extends TVODContract.View> extends IBaseContact.IBasePresenter<T> {
        void queryChannelSubjectList();
        void queryChannelStcPropsBySubject(String subjectID,int position);
    }
}
