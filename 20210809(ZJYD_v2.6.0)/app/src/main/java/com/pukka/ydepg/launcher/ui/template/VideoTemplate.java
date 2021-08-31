package com.pukka.ydepg.launcher.ui.template;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.PlayVodBean;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6response.GetVODDetailResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.customui.tv.PlayViewWindow;
import com.pukka.ydepg.inf.IPlayListener;
import com.pukka.ydepg.inf.IPlayState;
import com.pukka.ydepg.launcher.bean.GroupElement;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoTemplate extends PHMTemplate implements View.OnFocusChangeListener, View.OnClickListener {
    private static final String TAG = "VideoTemplate";
    private OnFocusChangeListener onFocusChangeListener;
    private Context context;
    private int playState = 0;
    private PlayViewWindow videoView;//左侧播放窗口
    private RelativeLayout videoContainer;
    private ImageView ivPoster;
    private List<VOD> datas = new ArrayList<>();
    private Map<Integer, String> urls = new HashMap<>();
    private List<RelativeLayout> views = new ArrayList<>();
    private int queryPosition = 0;//请求第几个VOD地址
    private int playPostion = 0;//当前正在播放第几个
    private int viewPosition;//当前模板在页面中的位置
    private boolean isUrlsReady = false;
    private static final int QUERY_URL_SUCCESS = 1110;
    private static final int QUERY_URLS_FINISHED = 1111;
    private long playTime = 0;
    private boolean isFirstAttached = true;
    private static final int QUERY_FAILED = 1112;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case QUERY_URL_SUCCESS:
                case QUERY_FAILED:
                    queryPosition++;
                    queryUrl();
                    break;
                case QUERY_URLS_FINISHED:
                    isUrlsReady = true;
                    if (fragment.getUserVisibleHint()) {
                        startPlayVideo();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public VideoTemplate(Context context) {
        super(context);
    }

    public VideoTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoTemplate(Context context, int layoutId, PHMFragment fragment, OnFocusChangeListener onFocusChangeListener) {
        super(context);
        this.context = context;
        this.layoutId = layoutId;
        this.fragment = fragment;
        this.onFocusChangeListener = onFocusChangeListener;
        setFocusable(false);
        initView(context, layoutId);
    }

    @Override
    public View getFirstView() {
        return videoContainer;
    }

    protected void initView(Context context, int layoutId) {
        super.initView(context, layoutId);
        ivPoster = (ImageView) findViewById(R.id.iv_pan_l1_r4_left_poster);
        videoView = (PlayViewWindow) findViewById(R.id.pan_l1_r4_video);
        videoView.setFocusable(false);
        views.add((RelativeLayout) findViewById(R.id.rl_pan_l1_r4_item0));
        views.add((RelativeLayout) findViewById(R.id.rl_pan_l1_r4_item1));
        views.add((RelativeLayout) findViewById(R.id.rl_pan_l1_r4_item2));
        views.add((RelativeLayout) findViewById(R.id.rl_pan_l1_r4_item3));
        videoContainer = (RelativeLayout) findViewById(R.id.pan_l1_r4_left);
        videoContainer.setOnClickListener(this);
        videoView.setOnPlayCallback(new IPlayListener() {
            @Override
            public void onPlayState(int playbackState) {
                playState = playbackState;
                if (canPlayOrPause()) {
                    ivPoster.setVisibility(INVISIBLE);
                }
            }

            @Override
            public void onPrepared(int  Videotype) { }

            @Override
            public void onRelease() {
                playState = 0;
            }

            @Override
            public void onPlayError(String msg, int errorCode, int playerType) {
                playNextVideo();
            }

            @Override
            public void onPlayCompleted() {
                playNextVideo();
            }

            @Override
            public void onDetached(long time) {
                fragment.removeVideoTemplate(viewPosition);
                fragment.addPlayPosition(viewPosition, playPostion);
                fragment.addPlayTime(viewPosition, time);
                fragment.addUrls(viewPosition, urls);
                videoView.releasePlayer();
            }

            @Override
            public void onAttached() {
                fragment.addFunctionTemplate(viewPosition, VideoTemplate.this);
                urls = fragment.getUrlsByPosition(viewPosition);
                playTime = fragment.getPlayTimeByPosition(viewPosition);
                playPostion = fragment.getPlayPosition(viewPosition);
                if (fragment.getUserVisibleHint()) {
                    startPlayVideo();
                }
                isFirstAttached = false;
            }

            @Override
            public void onTryPlayForH5() { }

            @Override
            public void onAdVideoEnd() {

            }
        });

    }

    public void onFragmentPause() {
        playTime = videoView.getCurrentPosition();
        fragment.addPlayPosition(viewPosition, playPostion);
        videoView.releasePlayer();
    }

    public void onFragmentResume() {
        if (fragment.getUserVisibleHint() && !CollectionUtil.isEmpty(datas)) {
            playPostion = fragment.getPlayPosition(viewPosition);
            setPlayIcon(playPostion);
            startPlayVideo();
        }

    }

    private void playNextVideo() {
        playPostion++;
        playPostion = playPostion % datas.size();//防止越界，重新开始
        playTime = 0;
        setPlayIcon(playPostion);
        // 播放小三角设置可见
        if (!isAllUrlsEmpty()) {
            setPoster(datas.get(playPostion));
            if (urls.containsKey(playPostion)) {
                startPlay(urls.get(playPostion));

            } else {
                startPlayVod(datas.get(playPostion));
            }
        }

    }

    private boolean isAllUrlsEmpty() {
        for (int key : urls.keySet()) {
            if (!TextUtils.isEmpty(urls.get(key))) {
                return false;
            }
        }
        return true;
    }

    public boolean isViewVisiable() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) fragment.getRecyclerViewTV().getLayoutManager();
        int position = linearLayoutManager.getPosition(VideoTemplate.this);
        int firstVisiablePosition = linearLayoutManager.findFirstVisibleItemPosition();
        int lastVisiablePosition = linearLayoutManager.findLastVisibleItemPosition();
        return position <= lastVisiablePosition && position >= firstVisiablePosition;
    }

    @Override
    public void setElementData(GroupElement groupElement, int i) { }

    public void bindSubjectVOD(List<VOD> vodList, int position) {
        if (CollectionUtil.isEmpty(vodList)) {
            return;
        }
        // 第一次初始化如果在顶部不会触发onAttach需要手动添加
        fragment.addFunctionTemplate(position, VideoTemplate.this);
        queryPosition = 0;
        viewPosition = position;
        urls = fragment.getUrlsByPosition(viewPosition);
        playTime = fragment.getPlayTimeByPosition(viewPosition);
        playPostion = fragment.getPlayPosition(viewPosition);
        //最多展示四个内容
        if (vodList.size() > 4) {
            datas = vodList.subList(0, 4);
        } else {
            datas = vodList;
        }
        setPoster(datas.get(playPostion));
        if (urls.size() <= 0) {
            queryUrl();
        }
        for (int i = 0; i < datas.size(); i++) {
            //前两个子VIEW不可落焦,不展示内容
            RelativeLayout view = views.get(i);
            view.setOnFocusChangeListener(this);
            view.setTag(i);
            view.setOnClickListener(this);
            TextView textView = (TextView) view.getChildAt(1);
            if (TextUtils.isEmpty(vodList.get(i).getName())) {
                textView.setText("");
            } else {
                textView.setText(vodList.get(i).getName());
            }
        }
    }

    private void queryUrl() {
        if (queryPosition < datas.size()) {
            getVODDetail(datas.get(queryPosition), false, queryPosition);
        } else {
            mHandler.sendEmptyMessage(QUERY_URLS_FINISHED);
        }
    }

    /**
     * 加载海报图片
     *
     * @param vod
     */
    private void setPoster(VOD vod) {
        if (VISIBLE != ivPoster.getVisibility()) {
            ivPoster.setVisibility(VISIBLE);
        }
        Picture picture = vod.getPicture();
        String imgUrl = "";
        if (null != picture) {
            List<String> adList = picture.getAds();
            if (adList != null && adList.size() > 0) {
                imgUrl = adList.get(0).toString();
            }
            //如果上面都没取到，则去posters去取
            if (TextUtils.isEmpty(imgUrl)) {
                //DebugLog.debug(TAG, "海报");
                List<String> posterList = picture.getPosters();
                if (posterList != null && posterList.size() > 0) {
                    imgUrl = posterList.get(0).toString();
                }
            }
        }
        RequestOptions options  = new RequestOptions()
                .placeholder(R.drawable.default_poster_bg).error(R.drawable.default_poster_bg);

        Glide.with(context).load(imgUrl).apply(options).into(ivPoster);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        onFocusChangeListener.onFocusChange(v, hasFocus);
        if (hasFocus) {
            ((RelativeLayout) v).getChildAt(1).setSelected(true);
        } else {
            ((RelativeLayout) v).getChildAt(1).setSelected(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (R.id.pan_l1_r4_left == v.getId()) {
            //轮播位无数据时，避免点击播放窗口Crash
            if (playPostion >= datas.size()){
                return;
            }
            VOD currentVod = datas.get(playPostion);
//            if (null != currentVod) {
//                ZJVRoute.route(context, ZJVRoute.LauncherElementDataType.VOD, null, null, currentVod.getID(), currentVod, null);
//            }
            if (urls.isEmpty() || TextUtils.isEmpty(urls.get(playPostion))) {
                return;
            }
            Intent intent = new Intent(context, OnDemandVideoActivity.class);
            PlayVodBean bean = new PlayVodBean();
            bean.setVodId(currentVod.getID());
            bean.setVodName(currentVod.getName());
            bean.setPlayUrl(urls.get(playPostion));
//            String vodBeanStr = JsonParse.object2String(bean);
//            intent.putExtra(OnDemandVideoActivity.PLAY_VOD_BEAN, vodBeanStr);
            LiveDataHolder.get().setPlayVodBean(JsonParse.object2String(bean));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            playTime = 0;
            int position = (int) v.getTag();
            playPostion = position;
            setPoster(datas.get(playPostion));
            if (urls.containsKey(position)) {
                setPlayIcon(playPostion);
                startPlay(urls.get(position));
            } else {
                startPlayVod(datas.get(position));
            }
            // 播放小三角设置可见
            setPlayIcon(position);
        }
    }

    private void hideAllIcon() {
        for (int i = 0; i < views.size(); i++) {
            (views.get(i)).getChildAt(0).setVisibility(INVISIBLE);
        }
    }

    /**
     * @param vod
     * @param isSingleRequest 是否是单独请求
     */
    private void getVODDetail(VOD vod, boolean isSingleRequest, int position) {
        fragment.getPresenter().getVODDetail(vod.getID(), new RxCallBack<GetVODDetailResponse>(HttpConstant.GETVODDETAIL, getContext()) {
            @Override
            public void onSuccess(GetVODDetailResponse getVODDetailResponse) {
                getVODUrl(getVODDetailResponse.getVODDetail(), isSingleRequest, position);
            }

            @Override
            public void onFail(Throwable e) {
                SuperLog.debug(TAG, "getVODDetail failed :" + e.toString());
                urls.put(position, "");
                mHandler.sendEmptyMessage(QUERY_FAILED);
            }
        });


    }


    private void getVODUrl(VODDetail detail, boolean isSingleRequest, int position) {
        if (null == detail)return;
        List<VODMediaFile> vodMediaFiles = detail.getMediaFiles();
        if (vodMediaFiles == null || vodMediaFiles.size() == 0) {
            urls.put(position, "");
            mHandler.sendEmptyMessage(QUERY_FAILED);
        }
        fragment.getPresenter().getPlayUrl(detail, new RxCallBack<PlayVODResponse>(HttpConstant.PLAYVOD, getContext()) {
            @Override
            public void onSuccess(PlayVODResponse playVODResponse) {
                String playUrl = StringUtils.splicingPlayUrl(playVODResponse.getPlayURL());
                if (!isSingleRequest) {
                    urls.put(position, playUrl);
                    mHandler.sendEmptyMessage(QUERY_URL_SUCCESS);
                } else {
                    urls.put(position, playUrl);
                    startPlay(playUrl);
                    setPlayIcon(position);
                }
            }

            @Override
            public void onFail(Throwable e) {
                urls.put(position, "");
                mHandler.sendEmptyMessage(QUERY_FAILED);
            }
        });
    }

    private void startPlay(String url) {
        videoView.releasePlayer();
        if((null!=fragment&&!fragment.isNotCanPlayNow()))
        {
            if (!TextUtils.isEmpty(url))
            {
                videoView.startPlay(url);
            }
            else
            {
                playNextVideo();
            }
        }
    }

    private boolean canPlayOrPause() {
        return playState == IPlayState.PLAY_STATE_HASMEDIA || playState == IPlayState.PLAY_STATE_BUFFERING;
    }

    private void setPlayIcon(int position) {
        hideAllIcon();
        views.get(position).getChildAt(0).setVisibility(VISIBLE);
    }

    /**
     * 点击还未请求到播放地址的vod
     *
     * @param vod
     */
    private void startPlayVod(VOD vod) {
        getVODDetail(vod, true, playPostion);
    }

    @Override
    public View getLastView() {
        return views.get(3);
    }

    public void startPlayVideo() {
        setPlayIcon(playPostion);
        //控件不可见时不播放
        if (datas.size() <= 0 || isAllUrlsEmpty() || !isViewVisiable()) {
            return;
        }
        if (urls.containsKey(playPostion)) {
            startPlay(urls.get(playPostion));
        } else {
            getVODDetail(datas.get(playPostion), true, playPostion);
        }
    }


}
