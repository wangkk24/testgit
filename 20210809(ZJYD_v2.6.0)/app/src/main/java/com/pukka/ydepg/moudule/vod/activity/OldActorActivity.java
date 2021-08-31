package com.pukka.ydepg.moudule.vod.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.CastDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.launcher.ui.fragment.FocusInterceptor;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.vod.adapter.OldSearchContentAdapter;
import com.pukka.ydepg.moudule.vod.presenter.ActorListPresenter;
import com.pukka.ydepg.moudule.vod.view.ActorDataView;
import com.pukka.ydepg.moudule.vod.view.FocusVerticalGridView;

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
public class OldActorActivity extends BaseActivity implements ActorDataView, FocusInterceptor
{


    @BindView(R.id.root_catagory_string)
    TextView mCategoryTitle;
    @BindView(R.id.page_num)
    TextView mFocusNum;
    @BindView(R.id.page_total)
    TextView mTotalNum;
    @BindView(R.id.movies_list)
    FocusVerticalGridView mActorVodList;

    private ActorListPresenter mPresenter;
    private int loadPageNum = 0;
    private String searchKey;
    private String contentType;
    private String searchScope;
    private int totalNumber;
    private OldSearchContentAdapter mAdapter;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_actor_vod_list);
        setmUnBinder(ButterKnife.bind(this));
        findViewById(R.id.content_list_no_content).setVisibility(View.INVISIBLE);
        mActorVodList.setFocusScrollStrategy(VerticalGridView.FOCUS_SCROLL_ITEM);
//        mActorVodList.setInterceptor(this);
        mActorVodList.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE)
                {
                    if (!Glide.with(OldActorActivity.this).isPaused())
                    {
                        Glide.with(OldActorActivity.this).pauseRequests();
                    }
                    return;
                }
                Glide.with(OldActorActivity.this).resumeRequests();
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView
                    .getLayoutManager().getChildCount() - 1);
                if (null != lastChildView)
                {
                    int lastChildBottom = lastChildView.getBottom();
                    int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                    int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
                    if ((lastChildBottom - recyclerBottom) < lastChildView.getHeight() &&
                        lastPosition == recyclerView.getLayoutManager().getItemCount() - 1)
                    {
                        if ((totalNumber - loadPageNum * 48 > 0) && (loadPageNum * 48 -
                            lastPosition - 1 <= 24))
                        {
                            mPresenter.loadActorList(loadPageNum * 48 + "", "48", searchKey,
                                contentType, searchScope);
                            Log.d("MoviesListFragment", "scrolled to last item!");
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
        mCategoryTitle.setSelected(true);
        mActorVodList.setNumColumns(6);
        mPresenter = new ActorListPresenter(this);
        mPresenter.setActorDataView(this);
        String name = getIntent().getStringExtra("search_name");
        if (!TextUtils.isEmpty(name))
        {
            mCategoryTitle.setText(name);
        }
        searchKey = getIntent().getStringExtra("search_key");
        contentType = getIntent().getStringExtra("content_type");
        searchScope = getIntent().getStringExtra("search_score");
        if (!TextUtils.isEmpty(searchKey) && !TextUtils.isEmpty(contentType) && !TextUtils
            .isEmpty(searchScope))
        {
            mPresenter.loadActorList((loadPageNum * 48) + "", "48", searchKey, contentType,
                searchScope);
            loadPageNum++;
        }
        mAdapter = new OldSearchContentAdapter(new ArrayList<Content>(), mPresenter, mActorVodList);
        mAdapter.setmOnKeyListener(listener);
        mActorVodList.setAdapter(mAdapter);
    }

    @Override
    public Context context()
    {
        return this;
    }

    @Override
    public void showNoContent() {
        findViewById(R.id.content_list_no_content).setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String message) { }

    @Override
    public void showActorContent(List<Content> Contents, int total) {
        mTotalNum.setText("/" + total);
        totalNumber = total;
        Log.d("MoviesListFragment", "scrolled to last item!---------->" + Contents.size());
        mAdapter.addContents(Contents);
    }

    @Override
    public void showFocusNum(String focusNum)
    {
        mFocusNum.setText(focusNum);
    }

    @Override
    public void getCastDetail(List<CastDetail> mCastDetails) { }

    @Override
    public int getLoadNum()
    {
        return loadPageNum;
    }

    @Override
    public boolean interceptFocus(KeyEvent event, View view) {
        int keycode = event.getKeyCode();
        if (keycode == KeyEvent.KEYCODE_DPAD_DOWN && mActorVodList.findFocus() != null &&
            isBorder((ViewGroup) view, mActorVodList.findFocus(), View.FOCUS_DOWN)) {
            if (totalNumber != 0 && mActorVodList.getLayoutManager().getItemCount() == totalNumber) {
                int remainder = totalNumber % 6;
                int rate = (totalNumber - remainder) / 6;
                if (rate != 0 && remainder != 0) {
                    int position = mActorVodList.getChildAdapterPosition(mActorVodList.findFocus());
                    int lastSecondPosition = rate * 6 - 1;
                    if (lastSecondPosition >= position && lastSecondPosition - 6 < position && (
                        (position + 1) % 6 > remainder || (position + 1) % 6 == 0)) {
                        mActorVodList.getLayoutManager().scrollToPosition(totalNumber - 1);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                View lastView = mActorVodList.getLayoutManager().findViewByPosition
                                        (totalNumber - 1);
                                if (null == lastView) {
                                    if (null != mActorVodList.findViewHolderForLayoutPosition(totalNumber - 1)) {
                                        lastView = mActorVodList.findViewHolderForLayoutPosition
                                                (totalNumber - 1).itemView;
                                    }
                                }
                                if (null != lastView) {
                                    lastView.setFocusable(true);
                                    lastView.requestFocus();
                                }
                            }
                        },200);

                        return true;

                    }
                }
            }
            return false;
        }

        return false;
    }

    private static final String TAG = "OldActorActivity";
    private View.OnKeyListener listener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                int position = (int) v.getTag(R.id.actor_position);
                int positionRate = position/6;
                int rate = (totalNumber-1)/6 - 1;
                Log.i(TAG, "onKey: 走进来 DOWN "+ positionRate + " "+ rate);
                if (totalNumber % 6 != 0 && totalNumber > 6 && positionRate == rate){
                    int positionRemainder = (position + 1) % 6;
                    int remainder = totalNumber % 6;
                    Log.i(TAG, "onKey: 走进来 == "+ positionRemainder + " "+ remainder);
                    if (positionRemainder > remainder || (positionRemainder == 0 && remainder != 0)){
                        Log.i(TAG, "onKey: 走进来");
                        //在最后一行
                        mActorVodList.smoothScrollToPosition(totalNumber - 1);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (null != mActorVodList.getLayoutManager()){
                                    View view = mActorVodList.getLayoutManager().findViewByPosition(totalNumber - 1);
                                    Log.i(TAG, "run: 走进来 null == view " + (null == view));
                                    if (null != view){
                                        view.requestFocus();
                                    }
                                }
                            }
                        },150);
                        return true;
                    }
                }
            }
            return false;
        }
    };

    /**
     * 判断是否是边界
     *
     * @param root
     * @param focused
     * @param direction
     * @return
     */
    private boolean isBorder(ViewGroup root, View focused, int direction) {
        return FocusFinder.getInstance().findNextFocus(root, focused, direction) == null ? true : false;
    }
}
