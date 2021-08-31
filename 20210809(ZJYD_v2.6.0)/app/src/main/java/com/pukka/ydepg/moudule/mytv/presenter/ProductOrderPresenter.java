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
package com.pukka.ydepg.moudule.mytv.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.bean.request.QueryMultiqryRequest;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.UniPayInfo;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUserOrderingSwitchRequest;
import com.pukka.ydepg.common.http.vss.request.QueryMultiUserInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUniPayInfoResponse;
import com.pukka.ydepg.common.http.vss.response.OrderProductResponse;
import com.pukka.ydepg.common.http.vss.response.QueryMultiUserInfoResponse;
import com.pukka.ydepg.common.utils.*;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.mytv.presenter.contract.ProductOrderContract;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import java.util.List;
import java.util.Locale;

/**
 * 产品订购presenter
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: ProductOrderPresenter
 * @Package com.pukka.ydepg.moudule.mytv.presenter
 * @date 2018/01/23 13:00
 */
public class ProductOrderPresenter extends ProductOrderContract.Presenter{

  private static final String TAG="ProductOrderPresenter";

  /**
   * 生成可订购的产品列表
   * @param authorizeJson
   */
  @Override public void generateProductList(String authorizeJson) {
    if(!TextUtils.isEmpty(authorizeJson)){
      //通过json工具转换成鉴权结果对象
      AuthorizeResult authorizeResult= JsonParse.json2Object(authorizeJson,AuthorizeResult.class);
      if(null!=authorizeResult){
        List<Product> productList=authorizeResult.getPricedProducts();
        if(null!=productList && productList.size()>0){
          //产品排序
          getBaseView().generateProductListSucc(HeartBeatUtil.getInstance().getProductsBySort(productList));
          return;
        }
      }
    }
    //没有产品
    getBaseView().generateProductListError();
  }

  @Override public String analyticValidity(Product product) {
    if(null == product){
      return "";
    }
    Resources resources=OTTApplication.getContext().getResources();
    String productType = product.getProductType();
    //产品类型: 0>包周期;1>按次
    String indateValue="";
    switch (productType){
      case "1":
        //周期产品
        String display_day = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.PPVORDER_SHOW_TIME);
        if(TextUtils.isEmpty(display_day)){
          display_day="72";
        }
        String rentPeriod = product.getRentPeriod();//获取有效期
        if(!TextUtils.isEmpty(rentPeriod)){
          int disp;
          try {
             disp = Integer.parseInt(display_day);
          }catch (Exception e){
              disp=72;
          }
          int rentp=Integer.parseInt(rentPeriod);
          if(rentp>=disp&&rentp>=24){
            int retain=rentp%24;
            int rate=rentp/24;
            indateValue= String.format(resources.getString(R.string.order_list_nday),String.valueOf(rate))+(retain==0?"":String.format(resources.getString(R.string.order_list_nhour),String.valueOf(retain)));
          }else{
            //xx小时
            indateValue = String.format(resources.getString(R.string.order_list_nhour),rentPeriod);
          }
        }

        break;
      case "0":
        String chargeMode = product.getChargeMode();//包周期类型
        if(!TextUtils.isEmpty(chargeMode)){
          switch (chargeMode){
            case "0"://包月
              //一个月
              indateValue = resources.getString(R.string.order_list_amonth);
              break;
            case "10"://包多天
              //xxx天
              indateValue = String.format(resources.getString(R.string.order_list_nday),
                      product.getPeriodLength());
              break;
            case "13"://包多月
              //xxx月
              indateValue = String.format(resources.getString(R.string.order_list_nmonth),
                      product.getPeriodLength());
              break;
            case "18"://包天
              //一天
              indateValue = resources.getString(R.string.order_list_onday);
              break;
            case "19"://包周
              //一周
              indateValue = resources.getString(R.string.order_list_aweek);
              break;
            case "20"://包多周
              //xxx周
              indateValue = String.format(resources.getString(R.string.order_list_nweek),
                      product.getPeriodLength());
              break;
            case "21"://包年
              indateValue = "";
              break;
            default://在产品类型下未获取到周期产品计量方式
              //一个月
              indateValue = resources.getString(R.string.order_list_amonth);
              break;
          }
        }else{
          //一个月
          indateValue = resources.getString(R.string.order_list_amonth);
          //chargeMode不为空
        }
        break;
      default://未匹配到相应的产品类型
        indateValue = "";
        break;
    }
    return indateValue;
  }

  /**
   * 解析海报URL
   * @param vodDetail vodDetail
   */
  @Override public String resolveVODPosterURL(VODDetail vodDetail) {
    String posterUrl="";
    Picture picture=vodDetail.getPicture();
    if (null!=picture) {
      List<String> posters = picture.getPosters();
      if(null!=posters && posters.size()>0){
        String poster = posters.get(0);
        if (!TextUtils.isEmpty(poster)) {
          posterUrl = poster;
        }
      } else {
        List<String> iconList = picture.getIcons();
        if (null!=iconList && iconList.size() > 0) {
          String poster = iconList.get(0);
          if (!TextUtils.isEmpty(poster)) {
            posterUrl = poster;
          }
        }
      }
    }
    return posterUrl;
  }

  /**
   * 查询统一支付开通情况
   * @param request request
   */
  @Override public void queryUniPayInfo(QueryUniInfoRequest request, Context context) {
    HttpApi.getInstance().getService().queryUniPayInfo(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
        + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.QUERY_UNIPAY_INFO, request)
        .compose(compose(getBaseView().bindToLife()))
        .subscribe(new RxCallBack<QueryUniPayInfoResponse>(HttpConstant.QUERY_UNIPAY_INFO, context) {
          @Override
          public void onSuccess(@NonNull QueryUniPayInfoResponse response) {
            if(null!=response && null!=response.getResult()){
              String returnCode=response.getResult().getRetCode();
              String resultMsg=response.getResult().getRetMsg();
              if(!TextUtils.isEmpty(returnCode) && returnCode.equals(Result.RETCODE_OK)){
                if(isViewAttached()){
                  getBaseView().queryUniPayInfoSucc(response);
                }
              }else{
                SuperLog.error(TAG,"returnCode:"+returnCode+",resultMsg:"+resultMsg);
//                EpgToast.showToast(OTTApplication.getContext(),
//                    OTTApplication.getContext().getString(R.string.query_unipay_failed));
                if(isViewAttached()){
                  getBaseView().queryUniPayInfoError();
                }
              }
            }
          }
          @Override public void onFail(@NonNull Throwable e) {
            if(isViewAttached()){
              getBaseView().queryUniPayInfoError();
            }
          }
        });
  }


  public void querySubscribe(QueryUniInfoRequest request) {
    String interfaceName = HttpConstant.QUERY_SUBSCRIBE_INFO;
    String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + interfaceName;
    HttpApi.getInstance().getService().querySubscriberInfo(url, request).compose(compose(getBaseView().bindToLife())).subscribe(querySubscriberResponse -> {
      String retCode = querySubscriberResponse.getResult().getRetCode();
      if (TextUtils.equals(retCode, Result.RETCODE_OK)) {
        List<NamedParameter> customFields = querySubscriberResponse.getSubscriber().getCustomFields();
        String orderingSwitch="0";
        if (!CollectionUtil.isEmpty(customFields)) {
          for (NamedParameter namedParameter : customFields) {
            if(com.pukka.ydepg.common.constant.Constant.ORDERING_SWITCH.equals(namedParameter.getKey())&&!CollectionUtil.isEmpty(namedParameter.getValues())){
              orderingSwitch=namedParameter.getValues().get(0);
            }
          }

        }
        if(isViewAttached()){
          getBaseView().querySubscriberSucess(orderingSwitch);
        }
      }else{
        if(isViewAttached()){
          getBaseView().querySubscriberfail();
        }
      }
    }, throwable -> {
      if(isViewAttached()){
        getBaseView().querySubscriberfail();
      }
    });
  }

  //VRS当前童锁状态查询功能
  @SuppressLint("CheckResult")
  public void queryUserOrderingSwitch() {
    QueryUserOrderingSwitchRequest request = new QueryUserOrderingSwitchRequest();
    UserInfo userInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
    request.setUserId(userInfo.getUserId());
    String interfaceName = HttpConstant.QUERY_USER_ORDER_SIWTCH;
    String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
    if (null == getBaseView()) {
      return;
    }
    HttpApi.getInstance().getService().queryUserOrderingSwitch(url, request).compose(compose(getBaseView().bindToLife())).subscribe(querySubscriberResponse -> {
      String retCode = querySubscriberResponse.getResult().getReturnCode();
      SuperLog.debug(TAG,"queryUserOrderingSwitch retCode = "+ retCode);
      if (TextUtils.equals(retCode, Result.RETCODE_OK_TWO)) {
        //0关闭 默认值；1开启
        String orderingSwitch="0";
        if (!TextUtils.isEmpty(querySubscriberResponse.getOrderingSwitch())){
          orderingSwitch = querySubscriberResponse.getOrderingSwitch();
        }
        if (getBaseView() != null) {
          getBaseView().querySubscriberSucess(orderingSwitch);
        }
      }else{
        if (getBaseView() != null) {
          getBaseView().querySubscriberfail();
        }
      }
    }, throwable -> {
      if (getBaseView() != null) {
        getBaseView().querySubscriberfail();
      }
    });
  }



  public void  queryMultiqry(QueryMultiqryRequest request){
    if (null == getBaseView() || null == OTTApplication.getContext()){
      return;
    }
      String interfaceName = HttpConstant.QUERY_MULTIQRY;
      String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
      HttpApi.getInstance().getService().queryMultiqry(url, request).compose(compose(getBaseView().bindToLife())).subscribe(queryMultiqryResponse -> {
          if (null!=queryMultiqryResponse&&(TextUtils.equals(queryMultiqryResponse.getCode(), Result.RETCODE_OK)||TextUtils.equals(queryMultiqryResponse.getCode(), Result.RETCODE_OK_TWO))) {
              if(isViewAttached()){
                  getBaseView().queryMultiqrySuccess(queryMultiqryResponse);
              }
          }else{
              if(isViewAttached()){
                  getBaseView().queryMultiqryFail();
              }
          }
      }, throwable -> {
          if(isViewAttached()){
              getBaseView().queryMultiqryFail();
          }
      });





  }




  /**
   * 解析户主信息:
   * 先判断PAY_TYPE,如果值是0，表示未开通统一支付；如果非0，在判断PAY_STATE，
   * 如果户主的PAY_STATE=1或者PAY_STATE=2，表示统一支付正常；获取户主号码可通过ROLE_ID=1，对应的FBILL_ID;
   * @param response response
   */
  @Override public UniPayInfo resolveMainUniPayInfo(QueryUniPayInfoResponse response) {
    UniPayInfo mainUniPayInfo = null;
    if (!"0".equals(response.getPayType())) {
      List<UniPayInfo> uniPayInfoList = response.getUniPayList();
      if (uniPayInfoList != null && !uniPayInfoList.isEmpty()) {
        for (UniPayInfo uniPayInfo : uniPayInfoList) {
          if ("1".equals(uniPayInfo.getRoleID())) {
            if ("1".equals(uniPayInfo.getPayState()) || "2".equals(uniPayInfo.getPayState())) {
              mainUniPayInfo = uniPayInfo;
              break;
            }
          }
        }
      }
    }
    return mainUniPayInfo;
  }

  @Override
  public void vssQueryMultiUserInfo(QueryMultiUserInfoRequest request, Context context, Intent intent) {
    String interfaceName = HttpConstant.QUERY_MULTI_USER_INFO;
    String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
    HttpApi.getInstance().getService().queryMultiUserInfo(url, request).compose(compose(getBaseView().bindToLife()))
            .subscribe(new RxCallBack<QueryMultiUserInfoResponse>(HttpConstant.QUERY_MULTI_USER_INFO, context) {
       @Override
       public void onSuccess(QueryMultiUserInfoResponse queryMultiUserInfoResponse) {
         if (!isViewAttached()) {
           SuperLog.error(TAG, "The View is not attached");
           return;
         }
         if (null == queryMultiUserInfoResponse) {
           SuperLog.error(TAG, "Check user status failed. The response is null");
           getBaseView().queryMultiUserInfoFail(intent);
           return;
         }
         if ((TextUtils.equals(queryMultiUserInfoResponse.getCode(), Result.RETCODE_OK)
                 || TextUtils.equals(queryMultiUserInfoResponse.getCode(), Result.RETCODE_OK_TWO))
                 && (TextUtils.equals(queryMultiUserInfoResponse.getUserState(), Constant.USER_STATE_IN_USE))) {
           getBaseView().queryMultiUserInfoSuccess(queryMultiUserInfoResponse, intent);
         } else {
           SuperLog.error(TAG, String.format(Locale.CHINA
                   , "QueryMultiUserInfo failed. %s"
                   , JsonParse.object2String(queryMultiUserInfoResponse)));
           getBaseView().queryMultiUserInfoFail(intent);
         }
       }

       @Override
       public void onFail(Throwable e) {
         getBaseView().queryMultiUserInfoFail(intent);
       }
     });
  }
}
