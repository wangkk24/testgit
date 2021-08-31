package com.pukka.ydepg.moudule.vod.utils;

import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.http.v6bean.v6node.AlacarteChoosedContent;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.OwnChoose.OwnChooseEvent;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.OwnChoose.OwnChoosePresenter;


import java.util.ArrayList;
import java.util.List;

//配置订购提示的工具类
public class OrderConfigUtils {
    private static final String TAG = "OrderConfigUtils";

    private static OrderConfigUtils mArderConfigUtils = new OrderConfigUtils();

    //是否需要弹出弹框
    private boolean needShowPopWindow = false;

    //是否是自选集场景
    boolean isAlacarte = false;


    //请求成功（不成功弹出默认样式）
    boolean isAlacarteSuccess = false;

    //如果有自选包，自选包的id（不包含506_等）
    String productId = "";

    //如果有自选包，自选包的id（包含506_等）
    String code = "";

    OwnChooseEvent event;

    OwnChoosePresenter presenter;

    VodDetailPBSConfigUtils vodDetailPBSConfigUtils = new VodDetailPBSConfigUtils("");

    String vodCode;
    ConfigCallBack callBack;

    public static OrderConfigUtils getInstance(){
        return mArderConfigUtils;
    }

    public OrderConfigUtils() {
    }

    public void config(String vodCode,ConfigCallBack callBack){
        isAlacarteSuccess = false;
        if (null == event){
            setEvent(new OwnChooseEvent(false));
        }
        this.needShowPopWindow = event.needShowAlacarteChoosePopWindow;
        this.vodCode = vodCode;
        this.callBack = callBack;
        productId = "";
        code = "";
        presenter = new OwnChoosePresenter();

        if (null != event.getPricedProducts()){
            List<Product> products = event.getPricedProducts();
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);

                if (OwnChooseEvent.CODE_BRONZE_ENOUGH.equals(product.getID()) ||
                        OwnChooseEvent.CODE_BRONZE_NOT_ENOUGH.equals(product.getID()) ||
                        OwnChooseEvent.CODE_SILVER_ENOUGH.equals(product.getID()) ||
                        OwnChooseEvent.CODE_SILVER_NOT_ENOUGH.equals(product.getID())){
                    if (product.getID().contains("506_")){
                        isAlacarte = true;
                        //会员充足
                        String productId = product.getID();
                        this.productId  = productId.substring(productId.indexOf("506_") + "506_".length());
                        presenter.setProductId(this.productId);
                        this.code = productId;
                        break;
                    }else if (product.getID().contains("507_")){
                        isAlacarte = true;
                        //会员不足
                        String productId = product.getID();
                        this.productId  = productId.substring(productId.indexOf("507_") + "507_".length());
                        presenter.setProductId(this.productId);
                        this.code = productId;
                        break;
                    }
                }
            }

            if (isAlacarte){
                //包含自选集包，先查询自选集包情况
                getAlacarteConfig();
            }else{
                presenter = null;
                //不包含自选集包，直接查询PBS接口
                getOrderInfo();
            }
        }else{
            //没有鉴权结果，返回失败
            isAlacarteSuccess = false;
            callBack.configDone();
        }


    }

    //获取自选集包详情
    private void getAlacarteConfig(){
        presenter.getAlacarteChooseContent(vodCode, OTTApplication.getContext(), new OwnChoosePresenter.GetAlacarteChooseContentCallBack() {
            @Override
            public void getAlacarteChooseContentSuccsee(List<AlacarteChoosedContent> alacarteChoosedContents) {
                if (null != alacarteChoosedContents && alacarteChoosedContents.size() > 0){
                    Log.i(TAG, "getAlacarteChooseContentSuccsee: ");
                    AlacarteChoosedContent content = alacarteChoosedContents.get(0);
                    presenter.setProductId(content.getProductID());
                    presenter.setSubscriptionId(content.getSubscriptionID());
                    presenter.setTotal(content.getResidualChooseNum() + "");
                }else{
                    Log.i(TAG, "getAlacarteChooseContentFail: ");
                    //请求失败，presenter置为空
                    presenter = null;
                }

                getOrderInfo();

            }

            @Override
            public void getAlacarteChooseContentFail(String descirbe) {
                Log.i(TAG, "getAlacarteChooseContentFail: ");
                //请求失败，presenter置为空
                presenter = null;
                getOrderInfo();
            }
        });
    }

    //获取pbs的OrderInfo
    private void getOrderInfo(){
        String productIdStr = "";
        List<Product> products = event.getPricedProducts();
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            if (i != 0){
                productIdStr = productIdStr + "," + product.getID();
            }else{
                productIdStr = productIdStr + product.getID();
            }
        }
        if (!TextUtils.isEmpty(productIdStr)){
            vodDetailPBSConfigUtils.configOrderInfo(productIdStr, new VodDetailPBSConfigUtils.GetOrderInfoCallBack() {
                @Override
                public void getOrderInfoDone() {
                    if (null != vodDetailPBSConfigUtils.getmPlayVodquerySTBOrderInfoResponse()){
                        Log.i(TAG, "getOrderInfoSuccess: ");
                        isAlacarteSuccess = true;
                    }else{
                        Log.i(TAG, "getOrderInfoFail: ");
                        isAlacarteSuccess = false;
                    }
                    callBack.configDone();
                    callBack = null;
                }
            });
        }else{
            Log.i(TAG, "getOrderInfoFail: ");
            isAlacarteSuccess = false;
            callBack.configDone();
            callBack = null;
        }


    }

    public void setEvent(OwnChooseEvent event) {
        this.event = event;
        this.needShowPopWindow = event.needShowAlacarteChoosePopWindow;
        if (!needShowPopWindow){
            clear();
        }
    }

    public OwnChooseEvent getEvent() {
        return event;
    }

    public boolean needShowPopWindow(){
        return needShowPopWindow;
    }

    public boolean isAlacarte() {
        return isAlacarte;
    }

    public void setAlacarte(boolean alacarte) {
        isAlacarte = alacarte;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public OwnChoosePresenter getPresenter() {
        Log.i(TAG, "getPresenter: "+(null == presenter));
        return presenter;
    }

    public VodDetailPBSConfigUtils getVodDetailPBSConfigUtils() {
        return vodDetailPBSConfigUtils;
    }

    //清空，为下一次播放做准备
    public void clear(){
        //是否需要弹出弹框
        needShowPopWindow = false;

        //是否是自选集场景
        isAlacarte = false;

        //请求成功（不成功弹出默认样式）
        isAlacarteSuccess = false;

        //如果有自选包，自选包的id（不包含506_等）
        productId = "";

        //如果有自选包，自选包的id（包含506_等）
        code = "";

        vodDetailPBSConfigUtils = new VodDetailPBSConfigUtils("");

        if (null != event){
            event.setPricedProducts(new ArrayList<>());
        }

    }

    public boolean isAlacarteSuccess() {
        return isAlacarteSuccess;
    }

    public interface ConfigCallBack{
        void configDone();
    }

}
