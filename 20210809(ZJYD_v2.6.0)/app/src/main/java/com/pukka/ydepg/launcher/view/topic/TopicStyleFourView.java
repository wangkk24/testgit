package com.pukka.ydepg.launcher.view.topic;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import androidx.leanback.widget.HorizontalGridView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;
import com.pukka.ydepg.common.utils.OTTFormat;
import com.pukka.ydepg.launcher.bean.node.Topic;
import com.pukka.ydepg.launcher.ui.adapter.topic.TopicFourAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.view.topic.TopicStyleFourView.java
 * @date: 2018-03-13 22:45
 * @version: V1.0 描述当前版本功能
 */


public class TopicStyleFourView extends BaseTopicStyleView {
    private HorizontalGridView mRecyclerView;
    private int loadPageNum = 0;
    private int totalNumber;
    private TopicFourAdapter mContentAdapter;

    public TopicStyleFourView(Context context, Topic topic) {
        super(context, topic);
    }

    @Override
    void initView() {

    }

    @Override
    int getLayoutID() {
        return R.layout.topic_style_four_layout;
    }

    @Override
    void initVodListView(Context context) {
        mRecyclerView = (HorizontalGridView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        mContentAdapter = new TopicFourAdapter(new ArrayList<>(),context);
        mRecyclerView.setAdapter(mContentAdapter);
    }

    @Override
    void queryVodData() {
        loadMoviesContent(loadPageNum + "", OFFSET + "");
        loadPageNum++;
    }

    private void showMoviesContent(List<VOD> vodList) {
        //isLoadMore = false;
        boolean needclean = true;
        if (loadPageNum > 1) {
            needclean = false;
        } else {
            needclean = true;
        }
        if (needclean) {
            mRecyclerView.setSelectedPosition(0);
        }
//        noContentLayout.setVisibility(View.INVISIBLE);
        mContentAdapter.addData(vodList, needclean);
        if (needclean) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    View textView = mRecyclerView.getChildAt(0);
                    if (textView != null) {
                        textView.setFocusable(true);
                        textView.requestFocus();
                    }
                }
            }, 500);
        }
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
}
