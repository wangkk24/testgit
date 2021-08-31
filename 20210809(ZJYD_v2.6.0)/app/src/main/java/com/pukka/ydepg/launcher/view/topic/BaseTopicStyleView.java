package com.pukka.ydepg.launcher.view.topic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.widget.BrowseFrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.GlideUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.bean.node.Topic;
import com.pukka.ydepg.launcher.ui.activity.TopicActivity;
import com.pukka.ydepg.launcher.util.TopicParamsParse;
import com.pukka.ydepg.moudule.featured.view.LauncherService;

import java.util.List;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.view.topic.BaseTopicStyleView.java
 * @date: 2018-03-13 22:04
 * @version: V1.0 描述当前版本功能
 */
public abstract class BaseTopicStyleView extends BrowseFrameLayout {
    protected static final int OFFSET = 24;
    protected final int COUNT = 24;
    protected Topic topic;
    protected TopicActivity activity;
    protected TextView topicTitleView;
    protected TextView topicDescView;
    private static final String TAG= "BaseTopicStyleView";
    public static final String TOPIC_TITLE = "topicTitle";
    public static final String TOPIC_INTRO = "topicIntro";
    public static final String TOPIC_BG = "topicBG";
    public BaseTopicStyleView(Context context) {
        super(context);
    }

    public BaseTopicStyleView(Context context, Topic topic) {
        this(context);
        setFocusable(false);
        setBackgroundResource(R.drawable.default_detail_bg);
        this.topic = topic;
        activity = (TopicActivity) context;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(getLayoutID(), this, true);
        initCommonView();
        initView();
        initVodListView(context);
        queryVodData();
    }

    protected void initCommonView(){
        topicTitleView = (TextView) findViewById(R.id.tv_topic_title);
        topicDescView = (TextView) findViewById(R.id.tv_topic_desc);
        String background = TopicParamsParse.getTopicParam(topic.getParams(),TOPIC_BG);
        String topicTitle = TopicParamsParse.getTopicParam(topic.getParams(),TOPIC_TITLE);
        String topicDesc = TopicParamsParse.getTopicParam(topic.getParams(),TOPIC_INTRO);
        //是否是使用PHS独立部署
        if (!TextUtils.isEmpty(LauncherService.getInstance().getQueryPhmLauncher()) && LauncherService.getInstance().getQueryPhmLauncher().equalsIgnoreCase("1")
                && !TextUtils.isEmpty(GlideUtil.getPHSImageUrl())){
            background = GlideUtil.getPHSImageUrl() + background;
        }
        SuperLog.debug(TAG, "background=" + background + "topicTitle=" + topicTitle + "topicDesc=" + topicDesc);
        if (!TextUtils.isEmpty(background)) {
            SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {

                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    setBackground(resource);
                    SuperLog.debug(TAG, "set background");
                }
            };

            RequestOptions options  = new RequestOptions()
                    .skipMemoryCache(true);

            Glide.with(activity).load(background).apply(options).into(simpleTarget);
        }
        if (!TextUtils.isEmpty(topicTitle) &&topicTitleView!= null) {
            topicTitleView.setText(topicTitle);
        }
        if (!TextUtils.isEmpty(topicDesc) &&topicDescView!= null) {
            topicDescView.setText(topicDesc);
        }
    }

    abstract void initView();

    abstract int getLayoutID();

    abstract void initVodListView(Context context);

    abstract void queryVodData();

    abstract public void loadVodData(String total,List<VOD> vodList);

    public BaseTopicStyleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseTopicStyleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}