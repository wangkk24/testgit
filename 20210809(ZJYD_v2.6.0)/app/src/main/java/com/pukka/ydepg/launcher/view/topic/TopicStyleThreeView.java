package com.pukka.ydepg.launcher.view.topic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;
import com.pukka.ydepg.common.utils.OTTFormat;
import com.pukka.ydepg.launcher.bean.node.Topic;
import com.pukka.ydepg.launcher.ui.adapter.topic.TopicThreeAdapter;

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


public class TopicStyleThreeView extends BaseTopicStyleView {
    private VerticalGridView mContentList;
    private TopicThreeAdapter mContentAdapter;
    private int loadPageNum = 0;
    private HorizontalGridView mRecyclerViewTitle;
    public TopicStyleThreeView(Context context, Topic topic) {
        super(context, topic);
    }
    private int totalNumber;


    @Override
    void initView() {
        mContentList = (VerticalGridView) findViewById(R.id.movies_list);
        mContentList.setFocusable(false);
        mContentList.setVisibility(View.GONE);
    }

    @Override
    int getLayoutID() {
        return R.layout.topic_style_two_layout;
    }

    @Override
    void initVodListView(Context context) {
        mRecyclerViewTitle = (HorizontalGridView) findViewById(R.id.mRecyclerViewTitle);
        mRecyclerViewTitle.setVisibility(View.VISIBLE);
        mRecyclerViewTitle.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewTitle.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        mContentAdapter = new TopicThreeAdapter(new ArrayList<>(),context);
        mRecyclerViewTitle.setAdapter(mContentAdapter);
    }

    @Override
    void queryVodData() {
        loadMoviesContent(loadPageNum + "", OFFSET + "");
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
        //isLoadMore = false;
        /**
         * 解决焦点框移动到第一页数据最后一个时，焦点重新落到第一个view上，原因是加载了第二页数据时，first view强制获取了focus
         * loadPageNum==0代表进入此专题后加载的第一页数据，因此设置mRecyclerViewTitle.setSelectedPosition(0)、first view去获取focus
         * loadPageNum>0时，load secondpage data，走正常逻辑
         * */
        boolean needclean = true;
        if (loadPageNum > 0) {
            needclean = false;
        } else {
            needclean = true;
        }
        if (needclean) {
            mRecyclerViewTitle.setSelectedPosition(0);
        }
        mContentAdapter.addData(vodList, needclean);
        if (needclean) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    View textView = mRecyclerViewTitle.getChildAt(0);
                    if(textView!=null){
                        textView.setFocusable(true);
                        textView.requestFocus();}
                }
            }, 500);
        }
    }
}
