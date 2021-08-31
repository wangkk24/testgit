package com.pukka.ydepg.common.start;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.v6bean.v6request.QuerySubjectDetailRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySubjectDetailResponse;
import com.pukka.ydepg.common.upgrade.download.DownloaderFactory;
import com.pukka.ydepg.common.upgrade.download.IDownloadListener;
import com.pukka.ydepg.common.upgrade.download.IDownloader;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.fileutil.FileUtil;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class StartPictureManager {

    private final static String startPicName = "startPic";

    public static void loadStartPicture(Context context, ImageView imageView) {
        File file = new File(OTTApplication.getContext().getFilesDir().getAbsolutePath(), startPicName);

        if (file.exists()) {
            Glide.with(context).load(file).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
            SuperLog.info2SD(Constant.TAG, "[START->1]Begin to load [Startup] picture, picture is " + file.getName());
        } else {
            //本地文件目录没有开机图片则加载应用内置开机图片
            SuperLog.info2SD(Constant.TAG, "[START->1]Begin to load [Startup] picture, picture is the default.");
            Glide.with(context).load(R.drawable.setup).into(imageView);
        }
    }

    public void getStartPicture() {
        SuperLog.info2SD(Constant.TAG, "Begin to get start picture resource.");
        //保存开机图片的栏目ID
        String startPicSubjectID = CommonUtil.getConfigValue("start_pic_subject_id");
        SuperLog.info2SD(Constant.TAG, "Begin to get start startPicSubjectID=" + startPicSubjectID);
        if (TextUtils.isEmpty(startPicSubjectID)) {
            SuperLog.info2SD(Constant.TAG, "There is no subject ID for [Startup] picture. Load default [Startup] picture.");
            //没有配置则删除本地已经下载的开机图片
            deleteStartPic();
            return;
        }

        QuerySubjectDetailRequest request = new QuerySubjectDetailRequest();
        List<String> listSubjectID = new ArrayList<>();
        listSubjectID.add(startPicSubjectID);
        // TODO: 2021/3/18 直播订购 通过终端配置参数配置一个subjectid
        SuperLog.info2SD(Constant.TAG, "Begin to get start liveSubjectID=======" + CommonUtil.getConfigValue(com.pukka.ydepg.launcher.Constant.LiveOrder.LIVE_ORDER_SUBJECTID));
        if (!TextUtils.isEmpty(CommonUtil.getConfigValue(com.pukka.ydepg.launcher.Constant.LiveOrder.LIVE_ORDER_SUBJECTID))) {
            listSubjectID.add(CommonUtil.getConfigValue(com.pukka.ydepg.launcher.Constant.LiveOrder.LIVE_ORDER_SUBJECTID));
        }
        request.setSubjectIds(listSubjectID);
        HttpApi.getInstance().getService().QuerySubjectDetail(HttpUtil.getVspUrl(HttpConstant.QUERYSUBJECT_DETAIL), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new RxCallBack<QuerySubjectDetailResponse>(HttpConstant.QUERYSUBJECT_DETAIL, OTTApplication.getContext()) {
                    @Override
                    public void onSuccess(QuerySubjectDetailResponse response) {
                        SuperLog.info2SD(Constant.TAG, "Begin to get start response =" + response.getSubjectList().size());
                        for (int i = 0; i < response.getSubjectList().size(); i++) {

                            SuperLog.info2SD(Constant.TAG, "Begin to get start id =" + response.getSubjectList().get(i).getID());
                            if (!TextUtils.isEmpty(CommonUtil.getConfigValue(com.pukka.ydepg.launcher.Constant.LiveOrder.LIVE_ORDER_SUBJECTID)) && response.getSubjectList().get(i).getID().equals(CommonUtil.getConfigValue(com.pukka.ydepg.launcher.Constant.LiveOrder.LIVE_ORDER_SUBJECTID))) {
                                if (null != response.getSubjectList().get(i).getPicture()) {
                                    if (null != response.getSubjectList().get(i).getPicture().getBackgrounds() && response.getSubjectList().get(i).getPicture().getBackgrounds().size() > 0) {
                                        //背景
                                        LiveDataHolder.get().setmLiveOrderBG(response.getSubjectList().get(i).getPicture().getBackgrounds().get(0));
                                    }
                                    if (null != response.getSubjectList().get(i).getPicture().getOthers() && response.getSubjectList().get(i).getPicture().getOthers().size() > 0) {
                                        //确定落焦
                                        LiveDataHolder.get().setmLiveOrderSureSelected(response.getSubjectList().get(i).getPicture().getOthers().get(0));
                                    } else {
                                        //确定落焦
                                        LiveDataHolder.get().setmLiveOrderSureSelected("");
                                    }
                                    if (null != response.getSubjectList().get(i).getPicture().getDrafts() && response.getSubjectList().get(i).getPicture().getDrafts().size() > 0) {
                                        //确定普通
                                        LiveDataHolder.get().setmLiveOrderSureNormal(response.getSubjectList().get(i).getPicture().getDrafts().get(0));

                                    } else {
                                        //确定普通
                                        LiveDataHolder.get().setmLiveOrderSureNormal("");
                                    }
                                    if (null != response.getSubjectList().get(i).getPicture().getIcons() && response.getSubjectList().get(i).getPicture().getIcons().size() > 0) {
                                        //取消落焦
                                        LiveDataHolder.get().setmLiveOrderCancelSelected(response.getSubjectList().get(i).getPicture().getIcons().get(0));
                                    } else {
                                        LiveDataHolder.get().setmLiveOrderCancelSelected("");
                                    }
                                    if (null != response.getSubjectList().get(i).getPicture().getStills() && response.getSubjectList().get(i).getPicture().getStills().size() > 0) {
                                        //取消普通
                                        LiveDataHolder.get().setmLiveOrderCancelNormal(response.getSubjectList().get(i).getPicture().getStills().get(0));
                                    } else {
                                        LiveDataHolder.get().setmLiveOrderCancelNormal("");
                                    }
                                    //首次落焦
                                    if (null != response.getSubjectList().get(i).getCustomFields()) {
                                        if (null != CommonUtil.getCustomField(response.getSubjectList().get(i).getCustomFields(), com.pukka.ydepg.launcher.Constant.LiveOrder.LIVE_ORDER_DEFAULT_FOCUS)) {
                                            LiveDataHolder.get().setmFristFocus(CommonUtil.getCustomField(response.getSubjectList().get(i).getCustomFields(), com.pukka.ydepg.launcher.Constant.LiveOrder.LIVE_ORDER_DEFAULT_FOCUS));
                                        } else {
                                            LiveDataHolder.get().setmFristFocus("1");
                                        }
                                    } else {
                                        LiveDataHolder.get().setmFristFocus("1");
                                    }
                                    //文案提示
                                    if (null != response.getSubjectList().get(i).getCustomFields()) {
                                        String title = CommonUtil.getCustomField(response.getSubjectList().get(i).getCustomFields(), com.pukka.ydepg.launcher.Constant.LiveOrder.LIVE_ORDER_DESCRIPTION);
                                        if (null != title) {
                                            LiveDataHolder.get().setmLiveOrderTitle(title);
                                        } else {
                                            LiveDataHolder.get().setmLiveOrderTitle(OTTApplication.getContext().getResources().getString(R.string.live_order_title));
                                        }
                                    } else {
                                        LiveDataHolder.get().setmLiveOrderTitle(OTTApplication.getContext().getResources().getString(R.string.live_order_title));
                                    }
                                }
                            } else {
                                try {
                                    String picUrl = response.getSubjectList().get(i).getPicture().getPosters().get(0);
                                    if (TextUtils.isEmpty(picUrl)) {
                                        SuperLog.info2SD(Constant.TAG, "The poster of configured [Startup] picture is empty. Load default [Startup] picture.");
                                        deleteStartPic();
                                    } else {
                                        downloadStartPic(picUrl);
                                    }
                                } catch (Exception e) {
                                    SuperLog.error(Constant.TAG, "Get configured start pic failed, load last [Startup] picture.");
                                    SuperLog.error(Constant.TAG, e);
                                }
                            }
                        }

                    }

                    @Override
                    public void onFail(@NonNull Throwable e) {
                        SuperLog.error(Constant.TAG, "Get configured start pic failed, load last [Startup] picture.");
                        SuperLog.error(Constant.TAG, e);
                    }
                });
    }

    private void downloadStartPic(String url) {
        IDownloader downloader = DownloaderFactory.getDownloader(DownloaderFactory.SELF, new DownloadStartPicCompleteListener(), OTTApplication.getContext());
        downloader.download(url);
    }

    private static class DownloadStartPicCompleteListener implements IDownloadListener {

        @Override
        public void onComplete(String file) {
            SuperLog.info2SD(Constant.TAG, "Download start resource successfully. File : " + file);
            try {
                //开机图片路径以及下载文件路径:/data/data/包名/files
                File downloadFile = new File(file);
                File startPicFile = new File(OTTApplication.getContext().getFilesDir().getAbsolutePath(), startPicName);
                FileUtil.copyFile(downloadFile, startPicFile);
                SuperLog.info2SD(Constant.TAG, "Create start picture successfully. Picture : " + startPicFile.getAbsolutePath() + ". This picture will be loaded when STB startup next time.");
            } catch (Exception e) {
                SuperLog.error(Constant.TAG, e);
            }
        }

        @Override
        public void onFail() {
            SuperLog.error(Constant.TAG, "Download start picture failed. ");
        }

        @Override
        public void onProgress(int progress, int max) {
        }
    }

    private void deleteStartPic() {
        File startPicFile = new File(OTTApplication.getContext().getFilesDir().getAbsolutePath(), startPicName);
        if (startPicFile.exists()) {
            SuperLog.info2SD(Constant.TAG, "Delete last download [Startup] picture result = " + startPicFile.delete());
        }
    }
}