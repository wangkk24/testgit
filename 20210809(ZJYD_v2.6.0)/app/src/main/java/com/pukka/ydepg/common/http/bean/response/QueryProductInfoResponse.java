package com.pukka.ydepg.common.http.bean.response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscription;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QueryProductInfoResponse {


    @SerializedName("result")
    private Result result;
    @SerializedName("total")
    private String total;
    @SerializedName("products")
    private List<Subscription> products;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Subscription> getProducts() {
        return products;
    }

    public void setProducts(List<Subscription> products) {
        this.products = products;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
