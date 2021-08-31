package com.pukka.ydepg.moudule.vod.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.CastDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.vod.adapter.SearchContentAdapter;
import com.pukka.ydepg.moudule.vod.presenter.ActorListPresenter;
import com.pukka.ydepg.moudule.vod.view.ActorDataView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 演员详情
 *
 * @author: ld
 * @date: 2017-12-19
 */
public class ActorActivity extends BaseActivity implements ActorDataView
{

    private String TAG=getClass().getName();

    @BindView(R.id.actor_name)
    TextView actorNametv;

    @BindView(R.id.actor_type)
    TextView actorTypetv;

    @BindView(R.id.bgimg)
    ImageView bgImg;

    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.movies_list)
    HorizontalGridView mActorVodList;

    private ActorListPresenter mPresenter;
    private int loadPageNum = 0;
    private String searchKey;
    private String contentType;
    private String searchScope;
    private int totalNumber;
    private SearchContentAdapter mAdapter;

    private int mCount=48;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_vod_list);
        setmUnBinder(ButterKnife.bind(this));
        mActorVodList.setFocusScrollStrategy(VerticalGridView.FOCUS_SCROLL_ITEM);
        mActorVodList.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE)
                {
                    if (!Glide.with(ActorActivity.this).isPaused())
                    {
                        Glide.with(ActorActivity.this).pauseRequests();
                    }
                    return;
                }
                SuperLog.debug(TAG," addOnScrollListener lastChildView");
                Glide.with(ActorActivity.this).resumeRequests();
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView
                    .getLayoutManager().getChildCount() - 1);
                if (null != lastChildView)
                {
                    SuperLog.debug(TAG," addOnScrollListener lastChildView");
                    int lastChildBottom = lastChildView.getBottom();
                    int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                    int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
                    if ((lastChildBottom - recyclerBottom) < lastChildView.getHeight() &&
                        lastPosition == recyclerView.getLayoutManager().getItemCount() - 1)
                    {
                        if ((totalNumber - loadPageNum * mCount > 0) && (loadPageNum * mCount -
                            lastPosition - 1 <= 24))
                        {
                            SuperLog.debug(TAG," addOnScrollListener lastChildView");
                            mPresenter.loadActorList(loadPageNum * mCount + "", mCount+"", searchKey,
                                contentType, searchScope);
                            loadPageNum++;
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mPresenter = new ActorListPresenter(this);
        mPresenter.setActorDataView(this);
        String name = getIntent().getStringExtra("search_name");
        if (!TextUtils.isEmpty(name))
        {
            actorNametv.setText(name);
        }
        searchKey = getIntent().getStringExtra("search_key");
        contentType = getIntent().getStringExtra("content_type");
        searchScope = getIntent().getStringExtra("search_score");
        if (!TextUtils.isEmpty(searchKey) && !TextUtils.isEmpty(contentType) && !TextUtils
            .isEmpty(searchScope))
        {
            mPresenter.loadActorList((loadPageNum * mCount) + "", mCount+"", searchKey, contentType,
                searchScope);
            loadPageNum++;
        }
        String castId=getIntent().getStringExtra("cast_id");
        List<String> castIds=new ArrayList<>();
        castIds.add(castId);
        mPresenter.getCastDetails(castIds);
        mAdapter = new SearchContentAdapter(new ArrayList<Content>(), mPresenter, mActorVodList);
        mActorVodList.setAdapter(mAdapter);
    }

    @Override
    public Context context()
    {
        return this;
    }

    @Override
    public void showNoContent() { }

    @Override
    public void showError(String message) { }

    @Override
    public void showActorContent(List<Content> Contents, int total) {
        totalNumber = total;
        Log.d("MoviesListFragment", "scrolled to last item!---------->" + Contents.size());
        mAdapter.addContents(Contents);
    }

    @Override
    public void showFocusNum(String focusNum) { }

    @Override
    public void getCastDetail(List<CastDetail> mCastDetails) {
        SuperLog.debug(TAG,"getCastDetail");
        if(null!=mCastDetails&&mCastDetails.size()>0) {
            CastDetail castDetail = mCastDetails.get(0);
            actorTypetv.setText(castDetail.getTitle());
            description.setText(castDetail.getIntroduce());
            Picture picture = castDetail.getPicture();
            if (picture != null) {
                String bgUrl = null;
                List<String> posters = picture.getPosters();
                List<String> bgUrls = picture.getBackgrounds();
                if (null != bgUrls && bgUrls.size() != 0) {
                    bgUrl = bgUrls.get(0);
                }
                if (TextUtils.isEmpty(bgUrl)) {
                    if (posters != null && posters.size() != 0)
                    {
                        bgUrl = posters.get(0);
                    }
                }
                if (!TextUtils.isEmpty(bgUrl)) {
                    if (!this.isFinishing()) {
                        RequestOptions options  = new RequestOptions()
                                .error(R.drawable.default_detail_bg);

                        Glide.with(this).load(bgUrl).apply(options).into(bgImg);
                    }
                }
            }
        }
    }

    @Override
    public int getLoadNum()
    {
        return loadPageNum;
    }
}