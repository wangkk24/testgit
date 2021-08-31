package com.pukka.ydepg.moudule.player.util;

import android.app.Application;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.ContentRight;
import com.pukka.ydepg.common.http.v6bean.v6node.PhysicalChannel;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.DensityUtil;


public class PlayerUtils {

    private static final String SUPPORT = "1";

    private static final String CAN_USE = "1";

    /**
     * 获取时移长度
     * @param channelDetail
     * @return
     */
    public static int getPLTVLength(ChannelDetail channelDetail)
    {
        if (null == channelDetail
                || channelDetail.getPhysicalChannels() == null
                || channelDetail.getPhysicalChannels().isEmpty())
        {
            return 0;
        }

        PhysicalChannel physicalChannel = channelDetail.getPhysicalChannels().get(0);

        ContentRight contentRight = physicalChannel.getPltvCR();

        if (null == contentRight)
        {
            return 0;
        }
        if(!"1".equals(contentRight.getEnable())){
            return 0;
        }
        if(TextUtils.isEmpty(contentRight.getLength())) {
            return 0;
        } else {
            try {
                return Integer.parseInt(contentRight.getLength());
            } catch (NumberFormatException var2) {
                return 0;
            }
        }
    }

    public static int getPLTVLength(ContentRight contentRight)
    {

        if (null == contentRight)
        {
            return 0;
        }
        if(!"1".equals(contentRight.getEnable())){
            return 0;
        }
        if(TextUtils.isEmpty(contentRight.getLength())) {
            return 0;
        } else {
            try {
                return Integer.parseInt(contentRight.getLength());
            } catch (NumberFormatException var2) {
                return 0;
            }
        }
    }

    /**
     * 是否支持时移直播
     * @param channelDetail
     * @return
     */
    public static boolean supportBTV(ChannelDetail channelDetail)
    {
        if (null == channelDetail
                || channelDetail.getPhysicalChannels() == null
                || channelDetail.getPhysicalChannels().isEmpty())
        {
            return false;
        }

        PhysicalChannel physicalChannel = channelDetail.getPhysicalChannels().get(0);

        ContentRight contentRight = physicalChannel.getPltvCR();

        if (null == contentRight)
        {
            return false;
        }
        return contentRightUsable(contentRight);
    }

    /**
     * 是否支持时移直播
     * @param contentRight
     * @return
     */
    public static boolean contentRightUsable(ContentRight contentRight)
    {
        if (null == contentRight){
            return false;
        }
        boolean enable = SUPPORT.equals(contentRight.getEnable());
        boolean isContentValid = CAN_USE.equals(contentRight
                .getIsContentValid());
        String length = contentRight.getLength();

        int playLength = 0;

        if(!TextUtils.isEmpty(length)){
            try{
                playLength = Integer.parseInt(length);
            }catch (Exception e){
                playLength = 0;
            }
        }

        return enable && isContentValid && playLength > 0;
    }

    //当 View 全部可见时，返回true
    public static boolean isVisibleALL(View view){
        if (null == view){
            return false;
        }

        int screenHeight = DensityUtil.getScreenHeight(OTTApplication.getContext());

        Rect rect =new Rect();
        boolean partVisible = view.getGlobalVisibleRect(rect);

        SuperLog.debug("Visible","screenHeight=" + screenHeight +";rect.bottom="+rect.bottom+";rect.top="+rect.top+";view.getMeasuredHeight()="+view.getMeasuredHeight());
        SuperLog.debug("Visible","partVisible=" + partVisible);

        if (rect.top > 0){
            return (partVisible && (view.getMeasuredHeight() + rect.top) <= screenHeight && (rect.bottom >= view.getMeasuredHeight()));
        }
        /*boolean totalHeightVisible = (rect.bottom - rect.top) >= view.getMeasuredHeight();//减去个3，防止一些边界特殊场景，判断不准确
        boolean totalWidthVisible = (rect.right - rect.left) >= view.getMeasuredWidth();
        boolean totalViewVisible = partVisible && totalHeightVisible && totalWidthVisible;*/
        return false;
    }

}
