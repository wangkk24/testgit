package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.launcher.bean.node.NamedParameter;
import java.util.List;

public class QueryUniInfoRequest {

  @SerializedName("extensionFields")
  private List<NamedParameter> extensionFieldList;

  public List<NamedParameter> getExtensionFieldList() {
    return extensionFieldList;
  }

  public void setExtensionFieldList(List<NamedParameter> extensionFieldList) {
    this.extensionFieldList = extensionFieldList;
  }
}
