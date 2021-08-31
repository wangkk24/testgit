package com.pukka.ydepg.moudule.mytv;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.FavoCatalog;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryFavoriteResponse;
import com.pukka.ydepg.common.utils.IsTV;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.event.DeleteFavoriteEvent;
import com.pukka.ydepg.event.FavoriteChangeEvent;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.moudule.mytv.adapter.CollectionListAdapter;
import com.pukka.ydepg.moudule.mytv.presenter.CollectionPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.view.CollectionView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 收藏界面
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.moudule.mytv.CollectionListFragment.java
 * @author:xj
 * @date: 2018-01-18 15:28
 */

public class CollectionListFragment extends Fragment implements CollectionView {
    RecyclerView mRecyclerView;
    List<Content> mContentList = new ArrayList<>();
    private CollectionPresenter mPresenter;
    private static final int pagerSize = 30;
    private CollectionListAdapter mCollectionListAdapter;
    View mNoData;
    private QueryFavoriteResponse mResponse;
    private static AtomicInteger mTotal=new AtomicInteger(0);

    //判断影片类型的扩展参数
    public final static String PLAY_FROM_TERMINAL = "playFromTerminal";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mytv_history_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mPresenter = new CollectionPresenter((RxAppCompatActivity) getActivity());
        mPresenter.setDataView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initData() {
        //少儿动漫界面，传少儿动漫收藏夹ID
        if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
            if (TextUtils.isEmpty(OTTApplication.getContext().getFavoCatalogID())) {
                mPresenter.queryFavoCatalog(0, pagerSize);
            } else {
                mPresenter.queryFavorite(0, pagerSize, OTTApplication.getContext().getFavoCatalogID());
            }
        } else {
            mPresenter.queryFavorite(0, pagerSize, "");
        }
    }

    private void initView(View view) {
        mNoData = view.findViewById(R.id.no_data);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mytv_history_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mCollectionListAdapter = new CollectionListAdapter(mContentList, getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mResponse = null;
        mContentList.clear();
        mCollectionListAdapter.notifyDataSetChanged();
        mNoData.setVisibility(View.GONE);
        initData();
    }

    public QueryFavoriteResponse getFragmentData() {
        //防止平台配置几百条posters url导致内存溢出
        List<Content> tempContentList = new ArrayList<>();
        if (null != mContentList && mContentList.size() > 0) {
            tempContentList.addAll(mContentList);
            for (int i = 0; i < mContentList.size(); i++) {
                if (null != mContentList.get(i).getVOD() && null != mContentList.get(i).getVOD().getPicture() && null != mContentList.get(i).getVOD().getPicture().getPosters() && mContentList.get(i).getVOD().getPicture().getPosters().size() > 0) {
                    List<String> posters = new ArrayList<>();
                    posters.add(mContentList.get(i).getVOD().getPicture().getPosters().get(0));
                    tempContentList.get(i).getVOD().getPicture().setPosters(posters);
                }
            }
        }
        if (null == mResponse) {
            if (null != mContentList && !mContentList.isEmpty() && mTotal.get() > 0) {
                mResponse = new QueryFavoriteResponse();
                mResponse.setTotal(String.valueOf(mTotal));
                mResponse.setFavorites(mContentList);
            }
        } else {
            mResponse.setTotal(String.valueOf(mTotal));
            mResponse.setFavorites(mContentList);
        }
        return mResponse;
    }

    @Override
    public void queryFavoriteSuccess(int total, List<Content> favorites) {
        mTotal.set(total);
        mContentList.clear();
        mNoData.setVisibility(View.GONE);
        if (null != favorites && !favorites.isEmpty()) {
            //过滤掉VR记录后
            for (int i = 0; i < favorites.size(); i++) {
                Content favorite = favorites.get(i);
                VOD vod = favorite.getVOD();
                if (IsTV.isNotVRFar(vod)){
                    mContentList.add(favorite);
                }
            }
//            mContentList.addAll(favorites);
        } else {
            mNoData.setVisibility(View.VISIBLE);
        }
        mCollectionListAdapter.notifyDataSetChanged();
    }

    @Override
    public void queryFavoriteFail() {
        mResponse = null;
        mContentList.clear();
        mCollectionListAdapter.notifyDataSetChanged();
        mNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void deleteFavoriteSuccess() {

    }

    @Override
    public void deleteFavoriteFail() {

    }

    //请求文件夹成功
    @Override
    public void queryFavoCatalogSuccess(int total, List<FavoCatalog> favoCatalogList) {
        if (total > 0 && null != favoCatalogList && favoCatalogList.size() > 0) {
            String catalogID = null;
            for (int i = 0; i < favoCatalogList.size(); i++) {
                if (Constant.CHILD_FAVROTIE_FOLDER.equals(favoCatalogList.get(i).getCatalogName())) {
                    catalogID = favoCatalogList.get(i).getCatalogID();
                    break;
                }
            }
            if (!TextUtils.isEmpty(catalogID)) {
                OTTApplication.getContext().setFavoCatalogID(catalogID);
                mPresenter.queryFavorite(0, pagerSize, catalogID);
            }else{
                //调查询收藏夹接口成功，但收藏夹文件名字不匹配
                mNoData.setVisibility(View.VISIBLE);
            }

        }else{
            //调查询收藏夹接口成功，但无收藏夹，显示无数据提示
            mNoData.setVisibility(View.VISIBLE);
        }
    }

    //请求文件夹失败
    @Override
    public void queryFavoCatalogFail() {
        mNoData.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onEvent(DeleteFavoriteEvent event) {
        if (null != event) {
            Iterator<Content> iterator = mContentList.iterator();
            List<String> ids = event.getVodCids();
            while (iterator.hasNext()) {
                Content content = iterator.next();
                if (null != ids) {
                    if (ids.contains(content.getVOD().getID())) {
                        iterator.remove();
                        mTotal.decrementAndGet();;
                    }
                }
            }
        }
        if (mContentList.size() == 0) {
            mNoData.setVisibility(View.VISIBLE);
        } else {
            mNoData.setVisibility(View.GONE);
        }
        mCollectionListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(FavoriteChangeEvent event) {
        initData();
    }
}
