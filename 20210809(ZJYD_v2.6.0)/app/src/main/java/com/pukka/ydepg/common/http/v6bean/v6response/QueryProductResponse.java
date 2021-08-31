package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;

import java.util.List;

/**
 * Created by liudo on 2018/5/8.
 */

public class QueryProductResponse extends BaseResponse {


    @SerializedName("countTotal")
    private int countTotal;

    @SerializedName("productList")
    private List<Product> productList;

    public int getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(int countTotal) {
        this.countTotal = countTotal;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
