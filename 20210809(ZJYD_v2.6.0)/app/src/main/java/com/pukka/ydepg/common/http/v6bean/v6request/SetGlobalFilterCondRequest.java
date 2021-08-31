package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.GlobalFilter;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hasee on 2017/5/19.
 */

public class SetGlobalFilterCondRequest {
    @SerializedName("globalFilter")
    private GlobalFilter globalFilter;
    /**
     *扩展信息。
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public GlobalFilter getGlobalFilter() {
        return globalFilter;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setGlobalFilter(GlobalFilter globalFilter) {
        this.globalFilter = globalFilter;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
