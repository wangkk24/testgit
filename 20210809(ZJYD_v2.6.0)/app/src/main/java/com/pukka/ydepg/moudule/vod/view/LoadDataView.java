package com.pukka.ydepg.moudule.vod.view;

import android.content.Context;

/**
 * Created by ld on 2018/1/31.
 */

public interface LoadDataView {

    Context context();

    void showNoContent();

    void showError(String message);
}
