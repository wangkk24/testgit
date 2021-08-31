package com.pukka.ydepg.launcher.mvp.presenter;

import android.content.Context;

import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryVODListBySubjectResponse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.mvp.contact.SubjectContact;
import com.pukka.ydepg.launcher.util.RxCallBack;

import io.reactivex.annotations.NonNull;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.mvp.presenter.SubjectPresenter.java
 * @date: 2018-03-11 17:54
 * @version: V1.0 描述当前版本功能
 */
public class SubjectPresenter extends BasePresenter<SubjectContact.ISubjectView> implements SubjectContact.ISubjectPresenter{
    @Override
    public void queryVodSubList(QueryVODListBySubjectRequest request, Context context) {
        String url = HttpUtil.getVspUrl(HttpConstant.QUERYVODLISTSTCPROPSBYSUBJECT) + HttpUtil.addUserVODListFilter();
        HttpApi.getInstance().getService().queryVODListBySubject(url,request).compose(onCompose(mView.bindToLife())).subscribe(new RxCallBack<QueryVODListBySubjectResponse>(mView,HttpConstant.QUERYVODLISTSTCPROPSBYSUBJECT, context) {
            @Override
            public void onSuccess(@NonNull QueryVODListBySubjectResponse response) {
                mView.loadData(response.getTotal(),response.getVODs());
            }

            @Override
            public void onFail(@NonNull Throwable e) {
                SuperLog.error(Constant.TAG,e);
                mView.loadData("0",null);
            }
        });
    }
}