
package com.pukka.ydepg.moudule.search;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.moudule.featured.view.DividerGridItemDecoration;
import com.pukka.ydepg.moudule.search.adapter.SearchMiGuResultAdapter;
import com.pukka.ydepg.moudule.search.adapter.SearchResultAdapter;
import com.pukka.ydepg.moudule.search.bean.SearchSubjectBean;
import com.pukka.ydepg.moudule.search.event.LoadFinishEvent;
import com.pukka.ydepg.moudule.search.event.SearchKeyEvent;
import com.pukka.ydepg.moudule.search.presenter.SearchPresenter;
import com.pukka.ydepg.moudule.search.view.SearchResultDataView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 搜索结果布局
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.module.search.SearchResultFragment.java
 * @author:xj
 * @date: 2017-12-19 15:54
 */
public class SearchResultFragment extends Fragment implements SearchResultDataView, SearchResultAdapter.OnItemFoucsListener {

    private static final String TAG = SearchResultFragment.class.getSimpleName();

    public  static final String SEARCH_SUBJECT_INFO = "search_subject_info";

    @BindView(R.id.search_fragment_result)
    RecyclerView mResultList;

    @BindView(R.id.no_search_result)
    View mNoDataLayout;

    private SearchResultAdapter     mSearchResultAdapter;
    private SearchMiGuResultAdapter mSearchMiGuResultAdapter;

    private int     gridNum = 5;                 //每行Item数量(咪咕页签一行4列,非咪咕页签一行5列)
    private int     mFocusPosition = -1;
    private int     mTotal;                      //搜索结果总数
    private String  index          = "1";
    private String  mSearchKey;                  //search key
    private String  mId;
    private String  mSubjectId;                  //subject id
    private boolean isActor        = false;      //是否是演员搜索页签
    private boolean isMigu         = false;      //是否是咪咕搜索页签
    private boolean mIsCreated     = false;      //是否已经创建
    private boolean mIsShowUser    = false;      //是否已经展示在用户面前
    private boolean mShouldReLoad  = true;       //是否重新加载数据
    private boolean mIsLoadMore    = false;      //是否加载更多数据
    private boolean isNeedLoadMore = false;      //标志,防止重复请求下一页数据

    private List<String> mFilterSubjectIDs;      //过滤栏目ID

    private List<Content> mContents = new ArrayList<>(); //search result

    private SearchPresenter mSearchPresenter;

    public static SearchResultFragment newInstance(SearchSubjectBean searchSubjectBean) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SEARCH_SUBJECT_INFO,searchSubjectBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.search_fragment_result_layout, container, false);
        //用于SearchActivity中)mResultViewPager.findViewWithTag找到当前显示的view
        view.setTag(mId);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        mSearchPresenter = new SearchPresenter((RxAppCompatActivity) getActivity());
        mSearchPresenter.setmSearchResultDataView(this);
        mIsCreated = true;
        //initData();
        initView();
        if (mIsShowUser) {
            sendSearchRequest();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        //栏目过多时，Fragment数量多,FragmentStatePagerAdapter只会缓存部分,其他Fragment会销毁(Fragment对象还在),将无法收到EventBus发来的重新发起搜索的消息
        //此时需要在销毁时将一些必要变量重置,保证Fragment重建时能正确处理业务流程
        mContents.clear();
        mIsCreated    = false;
        mShouldReLoad = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsShowUser = isVisibleToUser;
        if (isVisibleToUser){
            SuperLog.debug(TAG,"setUserVisibleHint:total="+mTotal);
            updateTotalView(mTotal);
            mFocusPosition = -1;
            updateCustomPage(index);
        }
        if (mIsCreated && mIsShowUser && mShouldReLoad){
            sendSearchRequest();
        }

        if( isVisibleToUser && 0 == mTotal && !mShouldReLoad){
            mNoDataLayout.setVisibility(View.VISIBLE);
            mResultList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFocus(boolean hasFocus,int position) {
        if (hasFocus){
            int customPage = position / gridNum + 1;
            mFocusPosition = position % gridNum;
            int customLastPage = (mContents.size()-1)/ gridNum +1;
            //防止超出总页数
            int totalPage=(mTotal-1)/gridNum + 1;
            updateCustomPage(customPage+"");
            //防止重复加载数据
            if (!isNeedLoadMore
                    && customPage == customLastPage
                    && customPage >= 3
                    && customPage <  totalPage){
                isNeedLoadMore =true;
                mIsLoadMore = true;
                sendSearchRequest();
            }
        }
    }

    @Subscribe
    public void onEvent(SearchKeyEvent searchKeyEvent){
        mSearchKey    = searchKeyEvent.getmSearchKey();
        mShouldReLoad = true;
        mIsLoadMore   = false;
        if (mIsShowUser) {
            sendSearchRequest();
        }
    }

    private void initData() {
        Bundle bundle = getArguments();
        SearchSubjectBean searchSubjectBean = (SearchSubjectBean) bundle.getSerializable(SEARCH_SUBJECT_INFO);
        mId        = searchSubjectBean.getId();
        mSubjectId = searchSubjectBean.getSubjectID();
        mFilterSubjectIDs = searchSubjectBean.getFilterSubjectIDs();
        isActor    = searchSubjectBean.getSubjectType() == SearchSubjectBean.SubjectType.ACTOR;
        isMigu     = searchSubjectBean.getSubjectType() == SearchSubjectBean.SubjectType.MIGU;
        gridNum    = isMigu ? 4 : 5;
    }

    private void initView() {
        mResultList.setLayoutManager(new GridLayoutManager(getContext(), gridNum));
        if (isMigu) {
            mSearchMiGuResultAdapter = new SearchMiGuResultAdapter(getContext(),mContents);
            mResultList.setAdapter(mSearchMiGuResultAdapter);
            mSearchMiGuResultAdapter.setOnItemFoucsListener(this);
        } else {
            mSearchResultAdapter = new SearchResultAdapter(getContext(),mContents);
            mResultList.setAdapter(mSearchResultAdapter);
            mSearchResultAdapter.setOnItemFoucsListener(this);
        }

        mResultList.addItemDecoration(new DividerGridItemDecoration(getContext()));
        mResultList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if (getActivity() instanceof SearchActivity){
                        SearchActivity searchActivity = (SearchActivity) getActivity();
                        searchActivity.setFocusView();
                    }
                }
            }
        });
    }

    private void sendSearchRequest() {
        mShouldReLoad = false;
        if( getActivity() != null ){
            mSearchKey = ((SearchActivity)getActivity()).getmSearchKey();
        }

        int offset;
        if (mIsLoadMore) {
            offset =  mContents.size();
        }else {
            offset = 0;
        }
        mSearchPresenter.searchContent(mSubjectId, mSearchKey,offset, mFilterSubjectIDs,isActor,isMigu);
    }

    @Override
    public void searchContentSuccess(int total, List<Content> contents,String key) {
        mTotal = total;
        updateTotalView(mTotal);
        if (!mIsLoadMore){
            mContents.clear();
            if (null != contents && !contents.isEmpty()){
                updateCustomPage("1");
                mContents.addAll(contents);
                mNoDataLayout.setVisibility(View.GONE);
                mResultList.setVisibility(View.VISIBLE);
                //如果触发了新的搜索动作，则搜索结果回到顶部，否则会停留在上一次停留的位置
                mResultList.scrollToPosition(0);
            } else {
                updateCustomPage("0");
                mNoDataLayout.setVisibility(View.VISIBLE);
                mResultList.setVisibility(View.GONE);
            }
            if (isMigu) {
                mSearchMiGuResultAdapter.notifyDataSetChanged();
            } else {
                mSearchResultAdapter.notifyDataSetChanged();
            }
        } else {
            if (null != contents && !contents.isEmpty()){
                mContents.addAll(contents);
                if(isMigu){
                    mSearchMiGuResultAdapter.notifyItemRangeChanged(mContents.size() - contents.size(),contents.size());
                } else {
                    mSearchResultAdapter.notifyItemRangeChanged(mContents.size() - contents.size(),contents.size());
                }
            }
        }
        isNeedLoadMore =false;
        EventBus.getDefault().post(new LoadFinishEvent(key));
    }

    @Override
    public void searchContentFail(String key) {
        EventBus.getDefault().post(new LoadFinishEvent(key));
        if (!mIsLoadMore){
            mTotal = 0;
            mContents.clear();
            if(isMigu){
                mSearchMiGuResultAdapter.notifyDataSetChanged();
            } else {
                mSearchResultAdapter.notifyDataSetChanged();
            }
            mNoDataLayout.setVisibility(View.VISIBLE);
            mResultList.setVisibility(View.GONE);
            SuperLog.debug(TAG,"searchContentFail:total="+mTotal);
            updateTotalView(mTotal);
        }
        isNeedLoadMore =false;
    }

    private void updateTotalView(int total){
        SuperLog.debug(TAG,"updateTotalView:total="+total);
        if (getActivity() instanceof SearchActivity){
            SearchActivity searchActivity = (SearchActivity) getActivity();
            searchActivity.setTotal(total,mShouldReLoad);
        }
    }

    private void updateCustomPage(String customPage){
        index = customPage;
        if (getActivity() instanceof SearchActivity){
            SearchActivity searchActivity = (SearchActivity) getActivity();
            searchActivity.setCustomPage(customPage);
        }
    }

    public int getmFocusPosition() {
        return mFocusPosition;
    }
}