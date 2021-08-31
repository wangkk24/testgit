package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.common.http.bean.node.XmppMessage;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySTBOrderInfoResponse;
import com.pukka.ydepg.common.report.ubd.extension.PurchaseData;
import com.pukka.ydepg.common.report.ubd.scene.UBDPurchase;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.event.FinishPlayUrlEvent;
import com.pukka.ydepg.event.PlayUrlEvent;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.ui.fragment.WebActivity;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.bean.ParmForH5Order;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.bean.ProductForH5Order;
import com.pukka.ydepg.moudule.player.node.CurrentChannelPlaybillInfo;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.presenter.NewDetailPresenter;
import com.pukka.ydepg.moudule.vod.utils.OrderConfigUtils;
import com.pukka.ydepg.moudule.vod.view.DetailDataView;
import com.pukka.ydepg.moudule.vod.view.NewDetailDataView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

//用于跳转至H5订购页面的工具类
public class JumpToH5OrderUtils {
    private static final String TAG = "JumpToH5OrderUtils";

    //赠送遥控器url
    private static final String giftUrl = "http://aikanvod.miguvideo.com:8858/pvideo/p/AC_RemoteCollection.jsp";

    //H5订购页面url
//    private static final String H5Url = "http://aikanvod.miguvideo.com:8858/pvideo/p/channelOrder.jsp?param=";
    private static final String H5Url = "http://aikanvod.miguvideo.com:8858/pvideo/p/channelOrder.jsp?";

    //H5支付页面url
    private static final String H5PayUrl = "http://aikanvod.miguvideo.com:8858/pvideo/p/channelPay.jsp?param=";
    private static final String TestUrl = "http://aikanvod.miguvideo.com:8858/pvideo/p/cs_fuc.jsp?param=";

    //H5订购过渡页
//    private static final String H5Transition = "http://aikanvod.miguvideo.com:8858/pvideo/p/channelMiddlePage.jsp?param=";
    private static final String H5Transition = "http://aikanvod.miguvideo.com:8858/pvideo/p/channelMiddlePage.jsp?";
    /**
     * 产品信息
     */
    private Product mProductInfo;

    /**
     * VOD详情
     */
    private VODDetail mVODDetail;

    /**
     * 频道ID和媒体资源ID
     */
    private String[] mCHANNELMEDIA_ID = null;

    /**
     * 是不是VOD订购
     */
    private boolean isVODSubscribe = false;

    /**
     * 是不是TVOD订购
     */
    private boolean isTVODSubscribe = false;

    /**
     * 是不是试看订购
     */
    private boolean isTrySeeSubscribe = false;

    /**
     * 是从订购中心
     */
    private boolean isOrderCenter = false;

    private boolean isOffScreen = false;
    private XmppMessage mXmppMessage;

    private boolean isDoingFinish = false;

    private static JumpToH5OrderUtils jumpToH5OrderUtils = new JumpToH5OrderUtils();

    //是否正在订购页面
    private boolean isDoingOrder = false;

    private JumpToH5OrderUtils(){

    }

    //获取单例
    public static JumpToH5OrderUtils getInstance(){
        return jumpToH5OrderUtils;
    }

    //VOD跳转
    public void jumpToH5OrderFromVOD(List<Product> products, Context context, boolean isVODSubscribe , boolean isTVODSubscribe, String[] mCHANNELMEDIA_ID, VODDetail mVODDetail){
        clearInfo();
        isDoingOrder = true;
        this.isVODSubscribe = isVODSubscribe;
        this.isTVODSubscribe = isTVODSubscribe;
        this.mCHANNELMEDIA_ID = mCHANNELMEDIA_ID;
        this.mVODDetail = mVODDetail;
        jumpToH5Order(products,context);
    }

    //试看跳转
    public void jumpToH5OrderFromTrySee(List<Product> products, Context context, boolean isVODSubscribe , boolean isTVODSubscribe, String[] mCHANNELMEDIA_ID, VODDetail mVODDetail){
        clearInfo();
        isDoingOrder = true;
        this.isTrySeeSubscribe = true;
        this.isVODSubscribe = isVODSubscribe;
        this.isTVODSubscribe = isTVODSubscribe;
        this.mCHANNELMEDIA_ID = mCHANNELMEDIA_ID;
        this.mVODDetail = mVODDetail;
        jumpToH5Order(products,context);
    }

    //订购中心跳转
    public void jumpToH5OrderFromOrderCenter(List<Product> products, Context context){
        clearInfo();
        isDoingOrder = true;
        this.isOrderCenter = true;
        jumpToH5Order(products,context);
    }

    //甩屏跳转
    public void jumpToH5ORderFromOffScreen(List<Product> products, Context context ,boolean isOffScreen,  XmppMessage xmppmessage,VODDetail mVODDetail){
        clearInfo();
        isDoingOrder = true;
        this.isOffScreen = isOffScreen;
        this.isTrySeeSubscribe = true;
        this.mXmppMessage = xmppmessage;
        this.mVODDetail = mVODDetail;
        jumpToH5Order(products,context);
    }

    //跳转至支付页面
    public void jumpToH5Pay(List<Product> products, Context context, String[] mCHANNELMEDIA_ID, boolean isOffScreen, XmppMessage mXmppMessage){
        isDoingOrder = true;
        this.isVODSubscribe = false;
        this.isTVODSubscribe = false;
        this.isTrySeeSubscribe = true;
        this.mCHANNELMEDIA_ID = mCHANNELMEDIA_ID;
        this.isOffScreen = isOffScreen;
        this.mXmppMessage = mXmppMessage;

        if (null == products || products.size() == 0){
            return;
        }

        ParmForH5Order parm = new ParmForH5Order();
        UserInfo mUserInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        if (null != mUserInfo)
        {
            parm.setUserID(mUserInfo.getUserId());
        }

        //直接跳转订购的只有直播一种情况
        parm.setFrontpage("4");

        parm.setBackWay("playparam.orderFinish()");

        Product product = products.get(0);
        parm.setProductId(product.getID());

        if (product.getProductType().equals("1")){
            parm.setProductType("2");
        }else if (product.getChargeMode().equals("0")){
            parm.setProductType("0");
        }else if (product.getChargeMode().equals("10") && product.getPeriodLength().equals("90")){
            parm.setProductType("3");
        }else if (product.getChargeMode().equals("10") && product.getPeriodLength().equals("365")){
            parm.setProductType("1");
        }

        if (null != mVODDetail){
            parm.setContentId(mVODDetail.getID());
        }

        String parmStr = JsonParse.object2String(parm);
        String encodeStr = URLEncoder.encode(parmStr);
        String url = H5PayUrl + encodeStr;


        Intent intent = new Intent();
        intent.setClass(context, WebActivity.class);
        intent.putExtra("url", url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void jumpToH5PayProducts(List<Product> products, Context context, String[] mCHANNELMEDIA_ID, boolean isOffScreen, XmppMessage mXmppMessage){
        isDoingOrder = true;
        this.isVODSubscribe = false;
        this.isTVODSubscribe = false;
        this.isTrySeeSubscribe = true;
        this.mCHANNELMEDIA_ID = mCHANNELMEDIA_ID;
        this.isOffScreen = isOffScreen;
        this.mXmppMessage = mXmppMessage;

        if (null == products || products.size() == 0){
            return;
        }

        ParmForH5Order parm = new ParmForH5Order();
        UserInfo mUserInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        if (null != mUserInfo)
        {
            parm.setUserID(mUserInfo.getUserId());
        }

        //直接跳转订购的只有直播一种情况
        parm.setFrontpage("4");

        parm.setBackWay("playparam.orderFinish()");

        List<ProductForH5Order> list = new ArrayList<>();

        if (null != OrderConfigUtils.getInstance()
                && null != OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils()
                && null != OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils().getmPlayVodquerySTBOrderInfoResponse()
                && !CollectionUtil.isEmpty(OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils().getmPlayVodquerySTBOrderInfoResponse().getProductList())){
            Log.i(TAG, "jumpToH5PayProducts: 取OrderConfigUtils");
            List<String> ids = OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils().getmPlayVodquerySTBOrderInfoResponse().getProductList();
            for (int i = 0; i < ids.size() ; i++) {
                ProductForH5Order product1 = new ProductForH5Order();
                product1.setProductId(ids.get(i));
                if (null != mVODDetail){
                    product1.setContentId(mVODDetail.getID());
                }
                list.add(product1);
            }
        }else{
            Log.i(TAG, "jumpToH5PayProducts: playvod");
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                ProductForH5Order product1 = new ProductForH5Order();
                product1.setProductId(product.getID());

                if (null != mVODDetail){
                    product1.setContentId(mVODDetail.getID());
                }

                list.add(product1);
            }
        }

        parm.setProduct(list);

        String parmStr = JsonParse.object2String(parm);
        String encodeStr = URLEncoder.encode(parmStr);
        String url = "";
        if (isOrderCenter && list.size() == 1){
            url = H5Transition;
        }else{
            url = H5Url;
        }

        if (null != OrderConfigUtils.getInstance()
                && null != OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils()
                && null != OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils().getmPlayVodquerySTBOrderInfoResponse()){
            QuerySTBOrderInfoResponse response = OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils().getmPlayVodquerySTBOrderInfoResponse();
            if (!TextUtils.isEmpty(response.getSceneType())){
                url = url + "sceneType="+response.getSceneType()+"&";
            }

            if (!TextUtils.isEmpty(response.getSubProduct())){
                url = url + "subProduct="+response.getSubProduct()+"&";
            }

//            if (null != response.getProductList() && response.getProductList().size() > 0){
//                String listStr = "";
//                for (int i = 0; i < response.getProductList().size(); i++) {
//                    if (i == 0){
//                        listStr = listStr + response.getProductList().get(i);
//                    }else{
//                        listStr = listStr + "," + response.getProductList().get(i);
//                    }
//                }
//                url = url + "productList="+listStr+"&";
//            }
        }

        url = url + "param=" +encodeStr;


        Intent intent = new Intent();
        intent.setClass(context, WebActivity.class);
        SuperLog.debug(TAG,"jumpToH5,url : "+url);
        intent.putExtra("url", url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //跳转到H5订购页面
    private void jumpToH5Order(List<Product> products, Context context){
        if (null == products || products.size() == 0){
            return;
        }
        isDoingOrder = true;
        isDoingFinish = false;

        ParmForH5Order parm = new ParmForH5Order();
        UserInfo mUserInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        if (null != mUserInfo)
        {
            parm.setUserID(mUserInfo.getUserId());
        }
        if (isOrderCenter){
            parm.setFrontpage("3");
        }else if (isTrySeeSubscribe){
            parm.setFrontpage("2");
        }else{
            parm.setFrontpage("1");
        }

        parm.setBackWay("playparam.orderFinish()");

        List<ProductForH5Order> list = new ArrayList<>();
        if (null != OrderConfigUtils.getInstance()
                && null != OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils()
                && null != OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils().getmPlayVodquerySTBOrderInfoResponse()
                && !CollectionUtil.isEmpty(OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils().getmPlayVodquerySTBOrderInfoResponse().getProductList())){
            Log.i(TAG, "jumpToH5PayProducts: 取OrderConfigUtils");
            List<String> ids = OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils().getmPlayVodquerySTBOrderInfoResponse().getProductList();
            for (int i = 0; i < ids.size() ; i++) {
                ProductForH5Order product1 = new ProductForH5Order();
                product1.setProductId(ids.get(i));
                if (null != mVODDetail){
                    product1.setContentId(mVODDetail.getID());
                }
                list.add(product1);
            }
        }else{
            Log.i(TAG, "jumpToH5PayProducts: playvod");
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                ProductForH5Order product1 = new ProductForH5Order();
                product1.setProductId(product.getID());

                if (null != mVODDetail){
                    product1.setContentId(mVODDetail.getID());
                }

                list.add(product1);
            }
        }

        parm.setProduct(list);

        String parmStr = JsonParse.object2String(parm);
        String encodeStr = URLEncoder.encode(parmStr);
        String url = "";
        if (isOrderCenter && list.size() == 1){
            url = H5Transition;
        }else{
            url = H5Url;
        }

        if (null != OrderConfigUtils.getInstance()
                && null != OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils()
                && null != OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils().getmPlayVodquerySTBOrderInfoResponse()){
            QuerySTBOrderInfoResponse response = OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils().getmPlayVodquerySTBOrderInfoResponse();
            if (!TextUtils.isEmpty(response.getSceneType())){
                url = url + "sceneType="+response.getSceneType()+"&";
            }

            if (!TextUtils.isEmpty(response.getSubProduct())){
                url = url + "subProduct="+response.getSubProduct()+"&";
            }

//            if (null != response.getProductList() && response.getProductList().size() > 0){
//                String listStr = "";
//                for (int i = 0; i < response.getProductList().size(); i++) {
//                    if (i == 0){
//                        listStr = listStr + response.getProductList().get(i);
//                    }else{
//                        listStr = listStr + "," + response.getProductList().get(i);
//                    }
//                }
//                url = url + "productList="+listStr+"&";
//            }
        }

        url = url + "param=" + encodeStr;

        Intent intent = new Intent();
        intent.setClass(context, WebActivity.class);
        SuperLog.debug(TAG,"jumpToH5,url : "+url);
        intent.putExtra("url", url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //跳转到H5活动页
    public void jumpToH5Event(Context context, boolean isTrySeeSubscribe, boolean isOrderCenter,boolean isVODSubscribe , boolean isTVODSubscribe, String[] mCHANNELMEDIA_ID, VODDetail mVODDetail, Product mProductInfo){
        clearInfo();
        this.isTrySeeSubscribe = isTrySeeSubscribe;
        this.isVODSubscribe = isVODSubscribe;
        this.isTVODSubscribe = isTVODSubscribe;
        this.mCHANNELMEDIA_ID = mCHANNELMEDIA_ID;
        this.mVODDetail = mVODDetail;
        this.mProductInfo = mProductInfo;
        this.isOrderCenter = isOrderCenter;

        Intent intent = new Intent();
        intent.setClass(context, WebActivity.class);
        intent.putExtra("url", giftUrl);
        context.startActivity(intent);
    }

    private void playOffscreen(NewDetailDataView mDetailDataView)
    {
        NewDetailPresenter mDetailPresenter = new NewDetailPresenter((RxAppCompatActivity) mDetailDataView);
        mDetailPresenter.setDetailDataView(mDetailDataView);
        mDetailPresenter.setVODDetail(mVODDetail);
        if (null != mXmppMessage && null != mVODDetail)
        {
            String type = mXmppMessage.getMediaType();
            String vodId = null;
            String childVodId = null;
            if (!TextUtils.isEmpty(type) && ("2".equals(type)))
            { // VOD

                String mediaCode = mXmppMessage.getMediaCode();
                String[] str = mediaCode.split(",");
                if (str != null)
                {
                    vodId = str[0];
                    if (str.length > 1)
                    {
                        childVodId = mediaCode.split(",")[1];
                    }
                }

            }
            if (childVodId != null && childVodId.indexOf("index") > -1 && vodId != null)
            {
                String index = childVodId.substring(5);
                List<VODMediaFile> clipfiles = mVODDetail.getClipfiles();
                VODMediaFile vodMediaFile = clipfiles.get(Integer.parseInt(index));
                mDetailPresenter.playClipfile(vodMediaFile, vodId, vodMediaFile.getElapseTime());
            }
            else
            {
                if (mVODDetail.getVODType().equals("0"))
                {
                    List<VODMediaFile> vodMediaFiles = mVODDetail.getMediaFiles();
                    if (vodMediaFiles != null && vodMediaFiles.size() != 0)
                    {
                        VODMediaFile vodMediaFile = vodMediaFiles.get(0);
                        mDetailPresenter.playClipfile(vodMediaFile, vodId, vodMediaFile
                                .getElapseTime());
                    }
                }
                else
                {//剧集
                    List<Episode> episodes = mVODDetail.getEpisodes();
                    for (Episode episode : episodes)
                    {
                        VOD vod = episode.getVOD();
                        if (vod.getID().equals(childVodId))
                        {
                            List<VODMediaFile> vodMediaFiles = vod.getMediaFiles();
                            if (vodMediaFiles != null && vodMediaFiles.size() != 0)
                            {
                                VODMediaFile vodMediaFile = vodMediaFiles.get(0);
                                mDetailPresenter.playClipfile(vodMediaFile, vod.getID(),
                                        vodMediaFile.getElapseTime());
                            }
                        }
                    }
                }
            }
        }
    }


    //H5订购回调
    public void H5orderFinish(NewDetailDataView mDetailDataView) {
        isDoingFinish = true;
        isDoingOrder = false;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Product mProduct = new Product();
                mProduct.setID(mProductInfo == null ? "" : mProductInfo.getID());
                //订购完成后，上报
                UBDPurchase.record(mProduct, mVODDetail == null ? "" : mVODDetail.getID(), PurchaseData.SUCCESS);

                SuperLog.info2SD(TAG, "send PlayUrlEvent >>> CHANNELMEDIA_ID:" + (null !=
                        mCHANNELMEDIA_ID) + "|isVODSubscribe:" + isVODSubscribe + "|isTVODSubscribe:"
                        + isTVODSubscribe + "|isTrySeeSubscribe:" + isTrySeeSubscribe);
                if (null != mCHANNELMEDIA_ID) {
                    CurrentChannelPlaybillInfo playbillInfo = new CurrentChannelPlaybillInfo();
                    playbillInfo.setChannelId(mCHANNELMEDIA_ID[0]);
                    playbillInfo.setChannelMediaId(mCHANNELMEDIA_ID[1]);
                    //直播鉴权
                    EventBus.getDefault().post(new PlayUrlEvent(playbillInfo));
                } else if (isVODSubscribe) {
                    //VOD鉴权
                    if (isOffScreen) {
                        playOffscreen(mDetailDataView);
                    } else {
                        EventBus.getDefault().post(new PlayUrlEvent(true, false,mProductInfo==null?"":mProductInfo.getID()));
                    }
                } else if (isTVODSubscribe) {
                    //回看鉴权
                    EventBus.getDefault().post(new PlayUrlEvent(false, true, mProductInfo == null ? "" : mProductInfo.getID()));
                } else if (isTrySeeSubscribe) {
                    EventBus.getDefault().post(new PlayUrlEvent(false, false, true, mProductInfo == null ? "" : mProductInfo.getID()));
                } else if (isOrderCenter) {
                    EventBus.getDefault().post(new PlayUrlEvent(false, false, false, true, mProductInfo == null ? "" : mProductInfo.getID()));
                } else {
                    EventBus.getDefault().post(new PlayUrlEvent(false, false, true, mProductInfo == null ? "" : mProductInfo.getID()));
                }

                clearInfo();
            }
        }, 500);

//        Handler handler1 = new Handler();
//        handler1.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                isDoingFinish = false;
//            }
//        },8000);
    }

    //清除保存的订购信息
    private void clearInfo(){
        mProductInfo = null;

        mVODDetail = null;

        mCHANNELMEDIA_ID = null;

        isVODSubscribe = false;

        isTVODSubscribe = false;

        isTrySeeSubscribe = false;

        isOrderCenter = false;
    }

    public boolean isDoingFinish() {
        return isDoingFinish;
    }

    public void setDoingFinish(boolean doingFinish) {
        isDoingFinish = doingFinish;
    }

    public boolean isDoingOrder() {
        return isDoingOrder;
    }

    public void setDoingOrder(boolean doingOrder) {
        isDoingOrder = doingOrder;
    }
}
