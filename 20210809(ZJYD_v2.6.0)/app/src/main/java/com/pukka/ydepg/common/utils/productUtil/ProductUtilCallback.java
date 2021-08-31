package com.pukka.ydepg.common.utils.productUtil;

import java.util.List;
import java.util.Map;

public interface ProductUtilCallback {
    void onCheckPackageRelationshipSuccess(Map<String, List<String>> params);
    void onQueryProductInfoFailed();
    void onQueryProductEmpty();
}
