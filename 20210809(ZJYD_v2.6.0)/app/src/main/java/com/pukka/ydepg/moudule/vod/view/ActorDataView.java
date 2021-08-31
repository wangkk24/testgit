package com.pukka.ydepg.moudule.vod.view;

import com.pukka.ydepg.common.http.v6bean.v6node.CastDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import java.util.List;

/**
 * Created by jiaxing on 2017/8/27.
 */

public interface ActorDataView extends LoadDataView{
    void showActorContent(List<Content> Contents, int total);

    void showFocusNum(String focusNum);

    void getCastDetail(List<CastDetail> mCastDetails);

    int getLoadNum();
}
