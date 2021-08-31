package com.pukka.ydepg.launcher.mvp.contact;

import android.content.Context;

import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;

import java.util.List;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.mvp.contact.SubjectContact.java
 * @date: 2018-03-11 17:20
 * @version: V1.0 描述当前版本功能
 */


public interface SubjectContact extends IBaseContact {
    interface ISubjectView extends IBaseContact.IBaseView{
         void loadData(String total,List<VOD> VODs);
    }
    interface ISubjectPresenter extends IBaseContact.IBasePresenter{
        void queryVodSubList(QueryVODListBySubjectRequest request, Context context);
    }
}
