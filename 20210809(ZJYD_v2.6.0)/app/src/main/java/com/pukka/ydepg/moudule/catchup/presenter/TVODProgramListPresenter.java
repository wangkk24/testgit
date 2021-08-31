package com.pukka.ydepg.moudule.catchup.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.errorcode.ErrorCode;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelPlaybill;
import com.pukka.ydepg.common.http.v6bean.v6node.PlaybillLite;
import com.pukka.ydepg.common.http.v6bean.v6node.QueryChannel;
import com.pukka.ydepg.common.http.v6bean.v6node.QueryPlaybill;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryPlaybillListRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayChannelResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryPlaybillListResponse;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.DateUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.common.utils.uiutil.Strings;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.mvp.presenter.BasePresenter;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.catchup.common.TVODDataUtil;
import com.pukka.ydepg.moudule.catchup.presenter.contract.TVODProgramListContract;
import com.pukka.ydepg.moudule.player.node.Program;

import com.pukka.ydepg.service.NtpTimeService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import io.reactivex.annotations.NonNull;

/**
 * TVODProgramListPresenter
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: TVODProgramListPresenter
 * @Package com.pukka.ydepg.moudule.catchup.presenter
 * @date 2018/09/21 11:17
 */
public class TVODProgramListPresenter<T extends TVODProgramListContract.View> extends BasePresenter<T> implements TVODProgramListContract.Presenter {

    private static final String TAG="TVODProgramListPresenter";

    private RxCallBack<QueryPlaybillListResponse> mPlaybillListCallback;

    private RxCallBack<PlayChannelResponse> mPlayChannelCallback;

    public static final int DAY_COUNT=6;

    /**
     * 创建回看日期列表
     */
    @Override
    public void createTVODDateList() {
        List<String> dateList = new ArrayList<>();
        List<String> originalList = new ArrayList<>();
        List<String> voiceDateList=new ArrayList<>();
        dateList.add(Strings.getInstance().getString(R.string.epglist_date_today));
        dateList.add(Strings.getInstance().getString(R.string.epglist_date_yesterday));
        voiceDateList.add(Strings.getInstance().getString(R.string.epglist_date_today));
        voiceDateList.add(Strings.getInstance().getString(R.string.epglist_date_yesterday));
        //回看7添加节目单
        for (int i = 0; i >= -DAY_COUNT; i--) {
            if(i<=-2){
                String dateValue = DateUtil.getMothAndDayValue(i);
                if (null != dateValue){
                    dateList.add(dateValue);
                }
                String voiceDateValue=DateUtil.getChineseMothAndDayValue(i);
                if (null != voiceDateValue){
                    voiceDateList.add(voiceDateValue);
                }
            }
            if (null != DateUtil.getBeoreAfterDateValue(i)){
                originalList.add(DateUtil.getBeoreAfterDateValue(i));
            }
        }
        if (null != mView) {
            mView.onTVODDateList(originalList,dateList,voiceDateList);
        }
    }

    /**
     * 回看播放鉴权
     */
    @Override
    public void playChannel(Context context, PlayChannelRequest request) {
        if(null!=mPlayChannelCallback){
            mPlayChannelCallback.dispose();
            mPlayChannelCallback=null;
        }
        mPlayChannelCallback = new RxCallBack<PlayChannelResponse>(HttpConstant.PLAYCHANNEL, context) {
            @Override
            public void onSuccess(PlayChannelResponse response) {
                if (null != response && null != response.getResult()) {
                    Result result = response.getResult();
                    String returnCode = result.getRetCode();
                    if (!TextUtils.isEmpty(returnCode)) {
                        //鉴权结果
                        AuthorizeResult authorizeResult = response.getAuthorizeResult();
                        if (returnCode.equals(Result.RETCODE_OK)) {
                            String productId = null;
                            if (null != authorizeResult) {
                                productId = authorizeResult.getProductID();
                            }
                            String url = response.getPlayURL();
                            //鉴权成功获取到的产品ID
                            if (!TextUtils.isEmpty(productId) || !TextUtils.isEmpty(url)) {
                                //播放地址
                                SuperLog.debug(TAG, "[playChannel] get playUrl success.");
                                mView.onPlayChannelUrlSuccess(request.getChannelID(), StringUtils.splicingPlayUrl(url), response.getBookmark());
                            }
                        }else {
                            SuperLog.error(TAG,
                                    "[playChannel] error code = "
                                            + returnCode + " message = " + result.getRetMsg());
                            if (null != authorizeResult) {
                                //订购的产品列表为空
                                if (returnCode.equals("146021000")
                                        || (null == authorizeResult.getPricedProducts()
                                        || authorizeResult.getPricedProducts().size() == 0)) {
                                    EpgToast.showLongToast(OTTApplication.getContext(), "无可订购产品！");
                                    return;
                                }
                                //跳转到订购页时,需要携带的参数
                                mView.onPlayChannelUrlFailed(response.getAuthorizeResult());
                            } else {
                                mView.onPlayChannelUrlError();
                                handleErrorIncludeTimeOut(returnCode,HttpConstant.PLAYCHANNEL,context);
                            }
                        }
                    }
                }
            }
            @Override
            public void onFail(@NonNull Throwable e) {
                if(null!=mView){
                    mView.onPlayChannelUrlError();
                }
            }
        };

        HttpApi.getInstance().getService().playChannel(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.PLAYCHANNEL, request)
                .compose(onCompose(mView.bindToLife()))
                .subscribe(mPlayChannelCallback);
    }

    @Override
    public void queryPlaybillList(Context context, String dateValue, List channelIds) {
        if (null != mPlaybillListCallback) {
            mPlaybillListCallback.dispose();
            mPlaybillListCallback = null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String nowDate = sdf.format(new Date(NtpTimeService.queryNtpTime()));
        List<Program> localPrograms=TVODDataUtil.getInstance().getTVODProgramList(dateValue+channelIds.get(0).toString());
        if(!CollectionUtil.isEmpty(localPrograms)){
            mView.onQueryPlayBillListSuccess(dateValue,localPrograms.size(),resizeProgramList(localPrograms));
            return;
        }
        mPlaybillListCallback = new RxCallBack<QueryPlaybillListResponse>(context) {
            @Override
            public void onSuccess(QueryPlaybillListResponse response) {
                Result result = response.getResult();
                String retCode = result.getRetCode();
                if(retCode.equals(Result.RETCODE_OK)){
                    List<Program> parseProgramList=parseProgram(nowDate.equals(dateValue),response);
                    if(!CollectionUtil.isEmpty(parseProgramList)){
                        TVODDataUtil.getInstance().setTVODProgramList(dateValue+channelIds.get(0).toString(),parseProgramList);
                        mView.onQueryPlayBillListSuccess(dateValue,parseProgramList.size(),resizeProgramList(parseProgramList));
                    }else{
                        mView.onPlayBillListEmpty(dateValue);
                    }
                }else{
                    EpgToast.showToast(OTTApplication.getContext(),ErrorCode.findError(HttpConstant.QUERYPLAYBILLLIST,retCode).getMessage());
                    mView.onQueryPlaybillListFailed();
                }
            }

            @Override
            public void onFail(@NonNull Throwable e) {
                SuperLog.error(TAG,e);
                if(null!=mView){
                    mView.onQueryPlaybillListFailed();
                }
            }
        };
        long startTime;
        String endTime;
        if(nowDate.equals(dateValue)){//今天
            startTime= DateCalendarUtils.getTime(dateValue);
            endTime=String.valueOf(NtpTimeService.queryNtpTime());
        }else{
            startTime = DateCalendarUtils.getTime(dateValue);
            String selectDay= DateCalendarUtils.formatDate(startTime,"yyyyMMdd");
            endTime=String.valueOf(DateCalendarUtils.getEndTimeOfDay(selectDay));
        }
        SuperLog.debug("QueryPlaybillListTAG", "[startTime:" + startTime + ",endTime:" + endTime + "]");
        //查询节目单列表
        QueryPlaybillListRequest request = new QueryPlaybillListRequest();
        QueryChannel queryChannel = new QueryChannel();
        queryChannel.setChannelIDs(channelIds);
        queryChannel.setContentType("CHANNEL");
        queryChannel.setIsReturnAllMedia("1");

        QueryPlaybill queryPlaybill = new QueryPlaybill();
        //查询回看节目单接口的type传2，只展示录制成功的节目单，修改后合入2.1版本
        //解决问题 单号 13192254  现网部分回看报无可订购产品
        queryPlaybill.setType("2");
        queryPlaybill.setStartTime(String.valueOf(startTime));
        queryPlaybill.setCount("100");
        queryPlaybill.setOffset("0");
        queryPlaybill.setIsFillProgram("1");
        queryPlaybill.setEndTime(endTime);
        //按节目单开始时间升序排序
        queryPlaybill.setSortType("STARTTIME:ASC");

        request.setNeedChannel("0");
        request.setQueryChannel(queryChannel);
        request.setQueryPlaybill(queryPlaybill);

        HttpApi.getInstance().getService().queryPlaybillList(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERYPLAYBILLLIST, request)
                .compose(onCompose(mView.bindToLife()))
                .subscribe(mPlaybillListCallback);
    }

    /**
     * 生成节目单列表
     */
    private List<Program> parseProgram(boolean isToday,QueryPlaybillListResponse queryPlaybillListResponse) {
        List<ChannelPlaybill> channelPlaybillList=queryPlaybillListResponse.getChannelPlaybills();
        if(CollectionUtil.isEmpty(channelPlaybillList)){
            return null;
        }
        List<PlaybillLite> playbills = channelPlaybillList.get(0).getPlaybillLites();
        List<Program> programs = new ArrayList<>();
        int index=0;
        for (PlaybillLite bill : playbills) {
            if(isToday && index==playbills.size()-1){
                //今天回看数据把最后一条数据过滤掉
                break;
            }
            index++;
            if(bill.getIsFillProgram().equals("1")){
                //填充节目单需要过滤掉
                continue;
            }
            Program pro = new Program();
            //频道ID
            pro.setChannelID(bill.getChannelID());
            //节目单名称
            pro.setName(TextUtils.isEmpty(bill.getName())?Strings.getInstance().getString(R.string.channel_playbill_name_empty):bill.getName());
            //节目单ID
            pro.setId(bill.getID());
            //节目单开始时间
            pro.setStartTime(bill.getStartTime());
            //节目单结束时间
            pro.setEndTime(bill.getEndTime());
            programs.add(pro);
        }
        return programs;
    }

    /**
     * 调整数据
     */
    private List<ArrayMap<Integer,List<Program>>> resizeProgramList(List<Program> programs){
        List<ArrayMap<Integer,List<Program>>> arrayMaps=new ArrayList<>();
        int programSize=programs.size();
        //总页数
        int totalPageNum = (programSize  +  26  - 1) / 26;
        //分页后的全部数据
        List<List<Program>> totalProgramList=new ArrayList<>();
        for (int i=1;i<=totalPageNum;i++){
            try {
                if(totalPageNum==1){
                    totalProgramList.add(0,programs.subList(0,programSize));
                }else if(i==totalPageNum){
                    totalProgramList.add((i-1),programs.subList((i-1)*26,programSize));
                }else{
                    totalProgramList.add((i-1),programs.subList((i-1)*26,26*i));
                }
            }catch (Exception e){
                SuperLog.error(TAG,e);
            }
        }
        //对分页数据进行重组,每页数据分成2列,1列13条数据，每页26条数据
        for (int j=0;j<totalProgramList.size();j++){
            List<Program> programList=totalProgramList.get(j);
            List<Program> leftPageProgram=new ArrayList<>();
            List<Program> rightPageProgram=new ArrayList<>();
            for (int i=0;i<programList.size();i++){
                Program program=programList.get(i);
                if(i<13){
                    leftPageProgram.add(program);
                }else if(i<13*2){
                    rightPageProgram.add(program);
                }
            }
            ArrayMap<Integer,List<Program>> mp=new ArrayMap<>();
            mp.put(0,leftPageProgram);
            mp.put(1,rightPageProgram);
            arrayMaps.add(j,mp);
        }
        return arrayMaps;
    }
}
