/*
 *Copyright (C) 2018 广州易杰科技, Inc.
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package com.pukka.ydepg.moudule.mytv.presenter.contract;

import android.content.Context;

import android.content.Intent;
import com.pukka.ydepg.common.http.bean.response.QueryMultiqryResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.UniPayInfo;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.vss.request.QueryMultiUserInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.vss.response.QueryMultiUserInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUniPayInfoResponse;
import com.pukka.ydepg.moudule.livetv.presenter.base.BasePresenter;
import com.pukka.ydepg.moudule.livetv.presenter.base.BaseView;
import io.reactivex.disposables.Disposable;

import java.util.List;

/**
 *
 * ProductOrderContract
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: ProductOrderContract
 * @Package com.pukka.ydepg.moudule.mytv.presenter
 * @date 2018/01/23 13:00
 */
public interface ProductOrderContract {

  interface View extends BaseView{

    /**
     * 生成产品列表数据
     * @param productList productList
     */
    void generateProductListSucc(List<Product> productList);

    /**
     * 生成产品列表数据失败
     */
    void generateProductListError();

    /**
     * 查询统一支付开通情况信息成功
     * @param response response
     */
    void queryUniPayInfoSucc(QueryUniPayInfoResponse response);

    /**
     * 查询开通数据信息失败
     */
    void queryUniPayInfoError();


    void querySubscriberSucess(String orderingSwitch);

    void querySubscriberfail();


    void queryMultiqrySuccess(QueryMultiqryResponse response);


    void queryMultiqryFail();

    /**
     * 精简的用户信息查询成功
     * @param response
     * @param intent
     */
    void queryMultiUserInfoSuccess(QueryMultiUserInfoResponse response, Intent intent);

    void queryMultiUserInfoFail(Intent intent);
  }

  abstract class Presenter extends BasePresenter<View>{
    /**
     * 通过鉴权结果的json生成可订购的产品列表
     * @param authorizeJson authorizeJson
     */
    public abstract void  generateProductList(String authorizeJson);

    /**
     * 产品列表的item选中之后,分析当前产品的有效期
     * @param product product
     * @return 返回有效期
     */
    public abstract String analyticValidity(Product product);

    /**
     * 解析海报URL
     * @param vodDetail vodDetail
     */
    public abstract String resolveVODPosterURL(VODDetail vodDetail);

    /**
     * 查询用户的统一支付开通情况
     * @param request request
     */
    public abstract void queryUniPayInfo(QueryUniInfoRequest request, Context context);

    /**
     * 解析查询出来的统一支付信息:户主信息
     * @param response response
     */
    public abstract UniPayInfo resolveMainUniPayInfo(QueryUniPayInfoResponse response);

    /**
     * 查询 精简的用户信息
     */
    public abstract void vssQueryMultiUserInfo(QueryMultiUserInfoRequest request, Context context, Intent intent);

  }
}
