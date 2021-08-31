package com.pukka.ydepg.launcher.view.topic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.leanback.widget.BrowseFrameLayout;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.OTTFormat;
import com.pukka.ydepg.launcher.bean.node.Topic;
import com.pukka.ydepg.launcher.ui.adapter.topic.TopicOneAdapter;
import com.pukka.ydepg.moudule.featured.view.LimitScrollViewPager;
import com.pukka.ydepg.moudule.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by y00275296 on 2018/3/4.
 */
public class TopicStyleOneView extends BaseTopicStyleView {

    private static final String TAG = TopicStyleOneView.class.getSimpleName();
    private LinearLayout noContentLayout;
    private TopicOneAdapter contentAdapter;
    private int loadPageNum = 0;
    private int totalNumber;

    public TopicStyleOneView(Context context, Topic topic) {
        super(context, topic);
    }

    public TopicStyleOneView(Context context) {
        super(context);
    }

    public TopicStyleOneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopicStyleOneView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    void initView() {
        noContentLayout = findViewById(R.id.movies_no_content);
    }

    @Override
    void queryVodData() {
        loadMoviesContent(String.valueOf(COUNT));
    }

    @Override
    int getLayoutID() {
        return R.layout.topic_style_one_layout;
    }

    @SuppressLint("RestrictedApi")
    @Override
    void initVodListView(Context context) {
        VerticalGridView contentListView = findViewById(R.id.movies_list);
        contentListView.setFocusable(true);
        contentListView.setFocusScrollStrategy(VerticalGridView.FOCUS_SCROLL_ITEM);
        contentListView.setNumColumns(6);
        contentListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(activity).resumeRequests();
                     return;
                }
                int tempCount = recyclerView.getLayoutManager().getChildCount() - 1;
           
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);

                SuperLog.debug(TAG,"tempCount="+tempCount+"\tlastPosition="+lastPosition);
                //if ((lastChildBottom - recyclerBottom) < lastChildView.getHeight() && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                if ( (totalNumber - loadPageNum * COUNT > 0) && (loadPageNum * COUNT - lastPosition - 1 <= 12)) {
                    //Added by liuxia at 20201110 解决问题:loadPageNum变量第一次赋值不生效,值仍为零,所以需要额外判断一下
                    if(loadPageNum == 0){
                        loadPageNum = 1;
                    }
                    SuperLog.debug(TAG,"Need to get New data,offset="+loadPageNum * COUNT+"\tcount="+COUNT);
                    loadMoviesContent( String.valueOf(COUNT));
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {}
        });

        contentAdapter = new TopicOneAdapter(new ArrayList<VOD>(), contentListView);
        contentAdapter.setColumnNum(6);
        contentListView.setAdapter(contentAdapter);
    }

    @Override
    public void loadVodData(String total,List<VOD> vodList) {
        totalNumber = OTTFormat.convertInt(total);
        if ( totalNumber < 1 ) {
            showNoContent();
        } else {
            showMoviesContent(vodList);
        }
    }

    private void loadMoviesContent(String count) {
        QueryVODListBySubjectRequest request = new QueryVODListBySubjectRequest();
        request.setSubjectID(topic.getRelationSubjectId());
        request.setCount(count);
        request.setOffset(String.valueOf( loadPageNum * COUNT));
        loadPageNum = loadPageNum + 1;
        activity.queryData(request);
    }

    private void showMoviesContent(List<VOD> vodList) {
        noContentLayout.setVisibility(View.INVISIBLE);
        contentAdapter.addData(vodList);
    }

    private void showNoContent() {
        noContentLayout.setVisibility(View.VISIBLE);
    }
}