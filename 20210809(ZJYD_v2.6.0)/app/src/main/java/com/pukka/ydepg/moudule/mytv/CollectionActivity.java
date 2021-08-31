package com.pukka.ydepg.moudule.mytv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.reflect.TypeToken;
import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Cast;
import com.pukka.ydepg.common.http.v6bean.v6node.CastRole;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.FavoCatalog;
import com.pukka.ydepg.common.http.v6bean.v6node.Genre;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryFavoriteResponse;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.ScoreControl;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.event.DeleteFavoriteEvent;
import com.pukka.ydepg.event.FavoriteChangeEvent;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.featured.bean.VODMining;
import com.pukka.ydepg.moudule.mytv.adapter.CollectionAdapter;
import com.pukka.ydepg.moudule.mytv.presenter.CollectionPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.view.CollectionView;
import com.pukka.ydepg.moudule.vod.presenter.CollectionEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pukka.ydepg.moudule.mytv.utils.VODMining.getMiguPoster;

/**
 * 收藏界面
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.moudule.mytv.CollectionActivity.java
 * @author:xj
 * @date: 2017-12-20 14:38
 */

public class CollectionActivity extends BaseActivity implements CollectionAdapter.OnDelectedListener, CollectionView {

    private static final String TAG = "CollectionActivity";
    /**
     * 收藏列表
     */
    @BindView(R.id.collection_list)
    VerticalGridView mCollectionList;

    /**
     * 请求最大数
     */
    private static final int PAGERSIZE = 30;

    /**
     * 数据列表
     */
    List<Content> mContentList = new ArrayList<>();

    /**
     * 收藏适配器
     */
    CollectionAdapter mCollectionAdapter;

    /**
     * 总数
     */
    private String mTotal = "0";
    /**
     * 总数View
     */
    @BindView(R.id.collection_title_total_count)
    TextView mTotolView;

    /**
     * 选中item position展示
     */
    @BindView(R.id.collection_title_select_number)
    TextView mSelectPositionView;

    /**
     * 详情view
     */
    @BindView(R.id.collection_detail_layout)
    View mCollectionDetailLayout;
    /**
     * image
     */
    @BindView(R.id.collection_detail_icon)
    ImageView mCollectionImage;

    /**
     * SuperScript
     */
    @BindView(R.id.vipimg)
    ImageView superScript;

    /**
     * type
     */
    @BindView(R.id.collection_info_type)
    TextView mCollectionType;
    /**
     * introduce
     */
    @BindView(R.id.collection_info_introduce)
    TextView mCollectionIntroduce;
    /**
     * director
     */
    @BindView(R.id.collection_info_director)
    TextView mCollectionDirector;
    /**
     * performer
     */
    @BindView(R.id.collection_info_performer)
    TextView mCollectionPerformer;
    /**
     * score
     */
    @BindView(R.id.collection_info_score)
    TextView mCollectionScore;


    /**
     * 阴影布局
     */
    @BindView(R.id.shadow)
    View shadow;


    /**
     * 是否加载更多
     */
    private volatile boolean mIsLoadMore;
    /**
     * 无数据
     */
    @BindView(R.id.no_data)
    View mNoData;

    @BindView(R.id.no_data_btn)
    View mNoDataBtn;
    @BindView(R.id.collection_img_4k)
    ImageView mCollection4k;
    /**
     * 是否从别的界面返回
     */
    private boolean mPause;
    /**
     * 第一次加载进来
     */
    private boolean mFirst = true;

    /**
     * 再次进入时 选择的焦点
     */
    private int mSelectPosition;

    /**
     * 是否正在加载数据
     */
    private boolean isLoading = false;
    /**
     * 长按滑动太快
     */
    private long mLastTime = 0;

    /**
     * 需要删除的item
     */
    List<Content> mDeleteContents;

    CollectionPresenter mCollectionPresenter;
    /**
     * 删除时弹窗
     */
    private PopupWindow mPopupWindow;

    /**
     * 是否有收藏操作触发
     */
    private boolean isCollectionChange;

    //记录加载到的offset
    private int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        mCollectionPresenter = new CollectionPresenter(this);
        mCollectionPresenter.setDataView(this);
        setmUnBinder(ButterKnife.bind(this));
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsLoadMore = false;
        if (mFirst) {
            setFirstData();
            mFirst = false;
        } else if (isCollectionChange) {
            requestData();
        }
    }

    private void setFirstData() {
        Intent intent = getIntent();
        String collection = intent.getStringExtra("collection");
        if (!TextUtils.isEmpty(collection)) {
            QueryFavoriteResponse response = JsonParse.json2Object(collection, new TypeToken<QueryFavoriteResponse>() {
            }.getType());
            if (null != response)
                queryFavoriteSuccess(Integer.parseInt(response.getTotal()), response.getFavorites());
        } else {
            requestData();
        }
    }

    /**
     * 请求收藏列表
     */
    private void requestData() {
        if (!mIsLoadMore) {
            offset = 0;
            mSelectPosition = 0;
            mNoData.setVisibility(View.GONE);
            mContentList.clear();
            mCollectionAdapter.notifyDataSetChanged();
            mCollectionDetailLayout.setVisibility(View.GONE);
            mSelectPositionView.setText("");
            mTotolView.setText("");
        }
        isCollectionChange = false;
        if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
            if (TextUtils.isEmpty(OTTApplication.getContext().getFavoCatalogID())) {
                mCollectionPresenter.queryFavoCatalog(0, PAGERSIZE);
            } else {
                mCollectionPresenter.queryFavorite(0, PAGERSIZE, OTTApplication.getContext().getFavoCatalogID());
            }
        }else{
            mCollectionPresenter.queryFavorite(offset, PAGERSIZE,"");
        }
    }

    private void initView() {
        mCollectionAdapter = new CollectionAdapter(mCollectionList, mContentList, this);
        mCollectionList.setAdapter(mCollectionAdapter);
        mCollectionAdapter.setOnDelectedListener(this);
        mCollectionList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    return;
                }

                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                int lastChildBottom = lastChildView.getBottom();
                int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
                if (((lastChildBottom - recyclerBottom) < lastChildView.getHeight() && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1)) {
                    Log.d("OrderedListActivity", "scrolled to last item!");
                    if (offset < Integer.parseInt(mTotal) && null != mCollectionAdapter && mCollectionAdapter.canLoadMore()) {
                        if (isLoading) {
                            return;
                        }
                        mIsLoadMore = true;
                        requestData();
                        isLoading = true;
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mNoDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchVodMainList();
            }
        });
        mCollectionDetailLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDeleteAll() {
        mDeleteContents = mContentList;
        showDeleteDialog(findViewById(R.id.main_collection_layout), true);
    }

    @Override
    public void onItemsDelete(List<Content> contents) {
        mDeleteContents = contents;
        showDeleteDialog(findViewById(R.id.main_collection_layout), false);
    }

    @Override
    public void onItemSelect(Content content, int position) {
        Log.i(TAG, "updateDetail: onItemSelect "+ position);
        //更新右边详情界面
        mSelectPositionView.setText(position + 1 + "");
        if (position < 0) {
            mSelectPositionView.setVisibility(View.GONE);
            mTotolView.setVisibility(View.GONE);
            mCollectionDetailLayout.setVisibility(View.VISIBLE);
            mCollectionScore.setVisibility(View.GONE);
            mCollectionType.setVisibility(View.VISIBLE);
            mCollectionIntroduce.setVisibility(View.GONE);
            mCollectionDirector.setVisibility(View.GONE);
            mCollectionPerformer.setVisibility(View.GONE);
            mCollectionImage.setVisibility(View.GONE);
            mCollection4k.setVisibility(View.GONE);
            shadow.setVisibility(View.GONE);
            if (null != content) {
                VOD vod = content.getVOD();
                if (null != vod && !TextUtils.isEmpty(vod.getName())) {
                    if (TextUtils.equals(vod.getName(), getResources().getString(R.string.dis_all_choose))) {
                        mCollectionType.setText(getResources().getString(R.string.clear_all));
                    } else {
                        mCollectionType.setText("");
                    }
                }
            }
        } else {
            Log.i(TAG, "updateDetail: onItemSelect update");
            mSelectPositionView.setVisibility(View.VISIBLE);
            mTotolView.setVisibility(View.VISIBLE);
            mCollectionDetailLayout.setVisibility(View.VISIBLE);
            updateDetail(content);
        }
    }


    /**
     * 更新右边详情界面
     *
     * @param content
     */
    private void updateDetail(Content content) {
        Log.i(TAG, "updateDetail: ");
        if (null != content) {
            shadow.setVisibility(View.VISIBLE);
            mCollectionType.setVisibility(View.VISIBLE);
            mCollectionIntroduce.setVisibility(View.VISIBLE);
            mCollectionDirector.setVisibility(View.VISIBLE);
            mCollectionPerformer.setVisibility(View.GONE);
            mCollectionImage.setVisibility(View.VISIBLE);
            mCollection4k.setVisibility(View.VISIBLE);
            mCollectionImage.setImageResource(R.drawable.default_poster);
            if (TextUtils.equals(content.getContentType(), Content.ContentType.AUDIO_VOD) || TextUtils.equals(content.getContentType(), Content.ContentType.VIDEO_VOD)) {
                VOD vod = content.getVOD();
                if (null != vod) {
                    Picture picture = vod.getPicture();
                    String cpId = vod.getCpId();
                    if (null != picture) {
                        String url = "";
                        if (VodUtil.isMiguVod(vod)) {// 判断是否是别的终端添加
                            url = getMiguPoster(vod);
                        } else {
                            List<String> posterList = picture.getPosters();
                            if (null != posterList && posterList.size() > 0) {
                                url = posterList.get(0);
                            }
                        }
                        if (!TextUtils.isEmpty(url)) {
                            RequestOptions options  = new RequestOptions()
                                    .placeholder(getResources().getDrawable(R.drawable.default_poster))
                                    .error(R.drawable.default_poster);
                            Glide.with(this).load(url).apply(options).into(mCollectionImage);
                            //ImageLoadManager.loadImageFromUrl(mCollectionImage, url, R.drawable.default_poster);
                            if (VODMining.getDefinition(vod).equals("2")) {
                                mCollection4k.setBackgroundResource(R.drawable.details_right_4k_icon);
                            } else {
                                mCollection4k.setBackgroundResource(0);
                            }
                        }
                    }
                    if (TextUtils.isEmpty(vod.getAverageScore())) {
                        mCollectionScore.setVisibility(View.GONE);
                    } else {
                        String score = vod.getAverageScore();
                        if (ScoreControl.newNeedShowScore(vod)) {
                            //默认评分7.0
                            if (!TextUtils.isEmpty(score) && score.equals("0.0")) {
                                score = "7.0";
                            }
                            mCollectionScore.setText(score);
                            mCollectionScore.setVisibility(View.VISIBLE);
                        } else {
                            mCollectionScore.setVisibility(View.GONE);
                        }
                    }
                    List<Genre> genreList = vod.getGenres();
                    String actors = "";
                    String directors = "";
                    StringBuffer genres = new StringBuffer();
                    if (null != genreList) {
                        for (Genre genre : genreList) {
                            if (TextUtils.isEmpty(genres)) {
                                genres.append(genre.getGenreName());
                            } else {
                                genres.append("," + genre.getGenreName());
                            }
                        }
                    }
                    List<CastRole> castRoleList = vod.getCastRoles();
                    StringBuffer actor = new StringBuffer();
                    StringBuffer director = new StringBuffer();
                    if (null != castRoleList) {
                        for (CastRole castRole : castRoleList) {
                            if (TextUtils.equals(castRole.getRoleType(), CastRole.RoleType.ACTOR)) {
                                List<Cast> castList = castRole.getCasts();
                                for (Cast cast : castList) {
                                    if (TextUtils.isEmpty(actor)) {
                                        actor.append(cast.getCastName());
                                    } else {
                                        actor.append("," + cast.getCastName());
                                    }
                                }

                            }

                            if (TextUtils.equals(castRole.getRoleType(), CastRole.RoleType.DIRECTOR)) {
                                List<Cast> castList = castRole.getCasts();
                                for (Cast cast : castList) {
                                    if (TextUtils.isEmpty(director)) {
                                        director.append(cast.getCastName());
                                    } else {
                                        director.append("," + cast.getCastName());
                                    }
                                }

                            }
                        }
                    }
                    if (TextUtils.isEmpty(actor)) {
                        actors = getResources().getString(R.string.mytv_order_unknown);
                        mCollectionDirector.setVisibility(View.GONE);
                    } else {
                        actors = actor.toString();
                    }
                    if (TextUtils.isEmpty(director)) {
                        directors = getResources().getString(R.string.mytv_order_unknown);
                        mCollectionIntroduce.setVisibility(View.GONE);
                    } else {
                        directors = director.toString();
                    }
                    if (TextUtils.isEmpty(genres)) {
                        genres.append(getResources().getString(R.string.mytv_order_unknown));
                        mCollectionType.setVisibility(View.GONE);
                    }
                    setStyleText(mCollectionType, getString(R.string.mytv_order_type) + " " + genres.toString());
                    setStyleText(mCollectionIntroduce, getString(R.string.mytv_order_director) + " " + directors);
                    setStyleText(mCollectionDirector, getString(R.string.mytv_order_actor) + " " + actors);

                    String superScriptUrl = SuperScriptUtil.getInstance().getSuperScriptForCollectionHistory(vod);
                    if (!TextUtils.isEmpty(superScriptUrl)){
                        GlideApp.with(this).load(superScriptUrl).into(superScript);
                        superScript.setVisibility(View.VISIBLE);
                    }else{
                        superScript.setVisibility(View.GONE);
                    }
                }
            } else {//非VOD不做处理
            }
        }
    }

    /**
     * TextView字体样式
     *
     * @param tv
     * @param text
     */
    public void setStyleText(TextView tv, String text) {
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.c23_color)), 0, 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(style);
    }

    /* 收藏相关 */
    @SuppressLint("RestrictedApi")
    @Override
    public void queryFavoriteSuccess(int total, List<Content> reminderList) {
        mNoData.setVisibility(View.GONE);
        if (mFirst){
            offset = 30;
        }else{
            offset = offset + reminderList.size();
        }
        mCollectionList.requestFocus();
        mTotal = total + "";
        if (!TextUtils.isEmpty(mTotal)) {
            if (Integer.parseInt(mTotal) > 0) {
                mTotolView.setText(String.format(getResources().getString(R.string.ordered_list_total_count), mTotal));
            } else {
                mTotolView.setText("");
            }
        }
        if (null != reminderList && !reminderList.isEmpty()) {
            if (mIsLoadMore) {
                mCollectionAdapter.addData(reminderList);
            } else {
                mCollectionAdapter.setIsBackView(mPause);
                mCollectionList.setSelectedPosition(mSelectPosition);
                mCollectionAdapter.addNewData(reminderList);
                mPause = false;
            }
        } else {
            if (!mIsLoadMore) {
                mSelectPositionView.setText("");
                mContentList.clear();
                mCollectionAdapter.notifyDataSetChanged();
                mCollectionDetailLayout.setVisibility(View.GONE);
                mNoData.setVisibility(View.VISIBLE);
                mNoDataBtn.requestFocus();
            }
        }

        isLoading = false;

    }

    @Override
    public void queryFavoriteFail() {
        if (!mIsLoadMore) {
            mContentList.clear();
            mCollectionAdapter.notifyDataSetChanged();
            mNoData.setVisibility(View.VISIBLE);
            mNoDataBtn.requestFocus();
            mCollectionDetailLayout.setVisibility(View.GONE);
            mSelectPositionView.setText("");
            mTotolView.setText("");
        }
        isLoading = false;
    }

    /* 删除收藏相关 */
    @Override
    public void deleteFavoriteSuccess() {
        sendUpdataMessage();
        if (!TextUtils.isEmpty(mTotal) && mDeleteContents != null) {
            mTotal = Integer.parseInt(mTotal) - mDeleteContents.size() + "";
            if (Integer.parseInt(mTotal) > 0) {
                mTotolView.setText(String.format(getResources().getString(R.string.ordered_list_total_count), mTotal));
            } else {
                mTotolView.setText("");
            }
        }
        mCollectionAdapter.deleteItem();
        if (mContentList.size() == 0) {
            mSelectPositionView.setText("");
            mCollectionDetailLayout.setVisibility(View.GONE);
            mNoData.setVisibility(View.VISIBLE);
            mNoDataBtn.requestFocus();
        }
    }

    @Override
    public void deleteFavoriteFail() {

    }

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
                mCollectionPresenter.queryFavorite(0, PAGERSIZE, catalogID);
            }else{
                //调查询收藏夹接口成功，但收藏夹文件名字不匹配
                mNoData.setVisibility(View.VISIBLE);
                mNoDataBtn.requestFocus();
            }

        }else{
            //调查询收藏夹接口成功，但无收藏夹，显示无数据提示
            mNoData.setVisibility(View.VISIBLE);
            mNoDataBtn.requestFocus();
        }
    }

    //请求文件夹失败，显示无数据
    @Override
    public void queryFavoCatalogFail() {
        mNoData.setVisibility(View.VISIBLE);
        mNoDataBtn.requestFocus();
    }


    private void showDeleteDialog(View parent, final boolean isDeleteAll) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_delete_collection_layout, null);
        View ok = view.findViewById(R.id.mytv_dialog_sure);
        View cancel = view.findViewById(R.id.mytv_dialog_cancel);
        if (null == mPopupWindow) {
            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        }
        ok.requestFocus();
//        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                deleteReserves(content);
//            }
//        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                deleteReserves(mDeleteContents, isDeleteAll);

            }
        });
        mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 删除请求
     *
     * @param contents
     */
    private void deleteReserves(List<Content> contents, boolean isDeleteAll) {
        mCollectionPresenter.deleteFavorites(contents, isDeleteAll);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (mContentList.size() == 0) {
                    finish();
                }
                break;
            default:
                break;
        }
        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
            case KeyEvent.ACTION_UP:
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if (System.currentTimeMillis() - mLastTime > 500) {
                        Log.d(CollectionActivity.class.getSimpleName(), "ACTION_UP");
                        mLastTime = System.currentTimeMillis();
                    } else {
                        return true;
                    }
                }
                break;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void canFinish() {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPause = true;

    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onStop() {
        super.onStop();
        if (null != mCollectionAdapter) {
            mSelectPosition = mCollectionList.getSelectedPosition();
        }
    }

    /**
     * update fragment
     */
    private void sendUpdataMessage() {

//        CollectionListFragment.updataData();
        List<String> idList = new ArrayList<>();
        if (null != mDeleteContents && mDeleteContents.size() > 0){
            for (int i = 0; i < mDeleteContents.size(); i++) {
                Content content = mDeleteContents.get(i);
                if (null != content) {
                    VOD vod = content.getVOD();
                    if (null != vod) {
                        idList.add(vod.getID() + "");
                    }
                }
            }
        }
        EventBus.getDefault().post(new DeleteFavoriteEvent(idList));
    }

    @Subscribe
    public void onEvent(FavoriteChangeEvent event) {
        mIsLoadMore = false;
        requestData();
    }

    /**
     * 收藏发生变化
     *
     * @param event event
     */
    @Subscribe
    public void onEvent(CollectionEvent event) {
        isCollectionChange = true;
    }

}
