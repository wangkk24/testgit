package com.pukka.ydepg.launcher.view.topic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;
import com.pukka.ydepg.common.utils.OTTFormat;
import com.pukka.ydepg.launcher.bean.node.Topic;
import com.pukka.ydepg.launcher.ui.adapter.topic.TopicTwoAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.view.topic.TopicStyleTwoView.java
 * @date: 2018-03-13 22:29
 * @version: V1.0 描述当前版本功能
 */


public class TopicStyleTwoView extends BaseTopicStyleView {
    private VerticalGridView mContentList;
    private TopicTwoAdapter mContentAdapter;
    private int loadPageNum = 0;
    private int totalNumber;
    public TopicStyleTwoView(Context context, Topic topic) {
        super(context, topic);
    }

    @Override
    void initView() {

    }

    @Override
    int getLayoutID() {
        return R.layout.topic_style_two_layout;
    }

    @SuppressLint("RestrictedApi")
    @Override
    void initVodListView(Context context) {
        mContentList = (VerticalGridView) findViewById(R.id.movies_list);
        mContentList.setFocusable(false);
        mContentList.setFocusScrollStrategy(VerticalGridView.FOCUS_SCROLL_ITEM);
        mContentList.setNumColumns(3);
        mContentList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(activity).resumeRequests();
                    return;
                }

                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
                //if ((lastChildBottom - recyclerBottom) < lastChildView.getHeight() && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                if (totalNumber > 12 && (totalNumber - loadPageNum * 24 > 0) && (loadPageNum * 24 - lastPosition - 1 <= 12)) {
                    loadMoviesContent(loadPageNum * OFFSET + "", OFFSET + "");
                    loadPageNum++;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });

        mContentAdapter = new TopicTwoAdapter(new ArrayList<VOD>(), mContentList,context);
        mContentAdapter.setColumnNum(3);
        mContentList.setAdapter(mContentAdapter);
    }

    @Override
    void queryVodData() {
        loadMoviesContent(loadPageNum + "", OFFSET+ "");
        loadPageNum++;
    }

    private void loadMoviesContent(String offset, String count) {
        QueryVODListBySubjectRequest request = new QueryVODListBySubjectRequest();
        request.setSubjectID(topic.getRelationSubjectId());
        request.setCount(count);
        request.setOffset(offset);
        activity.queryData(request);
    }

    @Override
    public void loadVodData(String total, List<VOD> vodList) {
        totalNumber= OTTFormat.convertInt(total);
        if (TextUtils.isEmpty(total) || Integer.parseInt(total) < 1) {
            return;
        } else if (vodList.size() != 0) {
            showMoviesContent(vodList);
        }
    }

    @SuppressLint("RestrictedApi")
    private void showMoviesContent(List<VOD> vodList) {
        boolean needclean = true;
        needclean = loadPageNum <= 1;
        if (needclean) {
            mContentList.setSelectedPosition(0);
        }
        mContentAdapter.addData(vodList, needclean);
    }
}
