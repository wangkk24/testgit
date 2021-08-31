package com.pukka.ydepg.launcher.ui.template;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.VerticalGridView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.adapter.CommonAdapter;
import com.pukka.ydepg.common.adapter.base.ViewHolder;
import com.pukka.ydepg.common.extview.ShimmerImageView;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.CornersTransform;
import com.pukka.ydepg.common.utils.GlideUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;
import com.pukka.ydepg.moudule.search.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 竖向滑动
 */
@SuppressLint("ViewConstructor")
public class VerticalScrollTestTemplate extends PHMTemplate {
    private static final String TAG = "VerticalScrollTestTemplate";
    private static final String HOT_RANK = "hot_rank";//key
    private static final String HOT_RANK_POSTER = "hot_rank_poster";//标题海报
    private static final String HOT_RANK_SERIES_POSTER = "hot_rank_series_poster";//电视剧标题海报
    private static final String HOT_RANK_SERIES = "hot_rank_series";//电视剧子集海报
    private static final String HOT_RANK_MOVIE_POSTER = "hot_rank_movie_poster";//电影标题海报
    private static final String HOT_RANK_MOVIE = "hot_rank_movie";//电影子集海报
    private static final String HOT_RANK_VARIETYSHOW_POSTER = "hot_rank_varietyshow_poster";//综艺标题海报
    private static final String HOT_RANK_VARIETYSHOW = "hot_rank_varietyshow";//综艺子集海报

    private VerticalGridView recyclerViewTV;
    private VerticalGridView recyclerViewTV1;
    private VerticalGridView recyclerViewTV2;

    private String contentUrl;//加载网络图片拼接地址

    private Group mGroup;

    public VerticalScrollTestTemplate(Context context, int layoutId, PHMFragment fragment, OnFocusChangeListener focusChangeListener) {
        super(context, layoutId, fragment, focusChangeListener);
    }

    @SuppressLint({"NewApi", "RestrictedApi"})
    @Override
    protected void initView(Context context, int layoutId) {
        super.initView(context, layoutId);

    }

    //海报
    private ImageView imHotRankPoster,imHotRankSubPoster01,imHotRankSubPoster02,imHotRankSubPoster03;

    //VarietyShow
    private List<Element> mSeriesList = new ArrayList<>();//电视剧
    private List<Element> mMovieList = new ArrayList<>();//电影
    private List<Element> mVarietyShowList = new ArrayList<>();//综艺

    @SuppressLint("RestrictedApi")
    public void setDatas(List<Element> elementList, VerticalScrollTestTemplate verticalScrollTestTemplate, Group group) {

        this.mGroup = group;
        mSeriesList = new ArrayList<>();//电视剧
        mMovieList = new ArrayList<>();//电影
        mVarietyShowList = new ArrayList<>();//综艺

        //刷新数据重复问题
        recyclerViewTV = findViewById(R.id.rv_hot_rank01);
        //recyclerViewTV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerViewTV1 = findViewById(R.id.rv_hot_rank02);
        //recyclerViewTV1.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerViewTV2 = findViewById(R.id.rv_hot_rank03);
        //recyclerViewTV2.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        contentUrl = "http://" + AuthenticateManager.getInstance().getUserInfo().getIP()
                + ":" + AuthenticateManager.getInstance().getUserInfo().getPort()
                + SharedPreferenceUtil.getInstance().getLauncherLink();
        //onFragmentPause();

        recyclerViewTV.setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ALIGNED);
        recyclerViewTV1.setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ALIGNED);
        recyclerViewTV2.setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ALIGNED);


        imHotRankPoster = verticalScrollTestTemplate.findViewById(R.id.im_hot_rank_poster);
        imHotRankSubPoster01 = verticalScrollTestTemplate.findViewById(R.id.im_hot_rank_subposter01);
        imHotRankSubPoster02 = verticalScrollTestTemplate.findViewById(R.id.im_hot_rank_subposter02);
        imHotRankSubPoster03 = verticalScrollTestTemplate.findViewById(R.id.im_hot_rank_subposter03);

        if (null != elementList && elementList.size() > 0){
            for (Element element : elementList){
                if (null != element.getExtraData() && !TextUtils.isEmpty(element.getExtraData().get(HOT_RANK))){
                    if (element.getExtraData().get(HOT_RANK).equalsIgnoreCase(HOT_RANK_POSTER)){
                        //标题海报
                        loadImPoster(imHotRankPoster,element.getElementDataList().get(0).getContentURL());
                    }else if (element.getExtraData().get(HOT_RANK).equalsIgnoreCase(HOT_RANK_SERIES_POSTER)){
                        //电视剧标题海报
                        loadImPoster(imHotRankSubPoster01,element.getElementDataList().get(0).getContentURL());
                    }else if (element.getExtraData().get(HOT_RANK).equalsIgnoreCase(HOT_RANK_SERIES)){
                        //电视剧子集海报
                        mSeriesList.add(element);
                    }else if (element.getExtraData().get(HOT_RANK).equalsIgnoreCase(HOT_RANK_MOVIE_POSTER)){
                        //电影海报
                        loadImPoster(imHotRankSubPoster02,element.getElementDataList().get(0).getContentURL());
                    }else if (element.getExtraData().get(HOT_RANK).equalsIgnoreCase(HOT_RANK_MOVIE)){
                        //电影子集海报
                        mMovieList.add(element);
                    }else if (element.getExtraData().get(HOT_RANK).equalsIgnoreCase(HOT_RANK_VARIETYSHOW_POSTER)){
                        //综艺海报
                        loadImPoster(imHotRankSubPoster03,element.getElementDataList().get(0).getContentURL());
                    }else if (element.getExtraData().get(HOT_RANK).equalsIgnoreCase(HOT_RANK_VARIETYSHOW)){
                        //综艺子集海报
                        mVarietyShowList.add(element);
                    }
                }
            }
        }

        initAdapter1();
        initAdapter2();
        initAdapter3();
    }

    private void initAdapter1() {
        CommonAdapter adapter = new CommonAdapter<Element>(context, R.layout.item_series_vertical_scroll_square, mSeriesList) {
            @Override
            protected void convert(ViewHolder holder, Element element, int position) {
                //防止复用导致资源位内容错乱
                holder.setIsRecyclable(false);
                ReflectRelativeLayout relativeLayout = (ReflectRelativeLayout) holder.itemView;
                ShimmerImageView shimmerImageView = holder.itemView.findViewById(R.id.shimmer_image_vertical_scroll);
                shimmerImageView.setImageResource(R.drawable.hot_movie01);
                relativeLayout.setGroup(mGroup);
                relativeLayout.setRadius(0.0f);
                relativeLayout.setOnFocusChangeListener(onFocusChangeListener);
                relativeLayout.setDefaultData(true);
                relativeLayout.setElementData(element);
            }
        };
        recyclerViewTV.setAdapter(adapter);
    }

    private void initAdapter2() {
        CommonAdapter adapter = new CommonAdapter<Element>(context, R.layout.item_vertical_scroll_square, mMovieList) {
            @Override
            protected void convert(ViewHolder holder, Element element, int position) {
                //防止复用导致资源位内容错乱
                holder.setIsRecyclable(false);
                ReflectRelativeLayout relativeLayout = (ReflectRelativeLayout) holder.itemView;
                ShimmerImageView shimmerImageView = holder.itemView.findViewById(R.id.shimmer_image_vertical_scroll);
                shimmerImageView.setImageResource(R.drawable.hot_movie01);
                relativeLayout.setGroup(mGroup);
                relativeLayout.setRadius(0.0f);
                relativeLayout.setOnFocusChangeListener(onFocusChangeListener);
                relativeLayout.setDefaultData(true);
                relativeLayout.setElementData(element);
            }
        };
        recyclerViewTV1.setAdapter(adapter);
    }

    private void initAdapter3() {
        CommonAdapter adapter = new CommonAdapter<Element>(context, R.layout.item_varietyshow_vertical_scroll_square, mVarietyShowList) {
            @Override
            protected void convert(ViewHolder holder, Element element, int position) {
                //防止复用导致资源位内容错乱
                holder.setIsRecyclable(false);
                ReflectRelativeLayout relativeLayout = (ReflectRelativeLayout) holder.itemView;
                ShimmerImageView shimmerImageView = holder.itemView.findViewById(R.id.shimmer_image_vertical_scroll);
                shimmerImageView.setImageResource(R.drawable.hot_movie01);
                relativeLayout.setGroup(mGroup);
                relativeLayout.setRadius(0.0f);
                relativeLayout.setOnFocusChangeListener(onFocusChangeListener);
                relativeLayout.setDefaultData(true);
                relativeLayout.setElementData(element);

            }
        };
        recyclerViewTV2.setAdapter(adapter);
    }

    private void loadImPoster(ImageView imageView,String imUrl){
        RequestOptions options  = new RequestOptions()
                .placeholder(getResources().getDrawable(R.drawable.hot_movie))
                .transform(new CornersTransform(ScreenUtil.getDimensionF(getContext(), R.dimen.my_moreItem_radius)));
        Glide.with(getContext()).load(contentUrl + GlideUtil.getUrl(imUrl)).apply(options).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                SuperLog.error(TAG,e);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(imageView);
    }

    @Override
    public View getFirstView() {
        return  recyclerViewTV.getLayoutManager().findViewByPosition(0);
    }

    public void scrollToTop() {
        recyclerViewTV.getLayoutManager().scrollToPosition(0);
        recyclerViewTV1.getLayoutManager().scrollToPosition(0);
        recyclerViewTV2.getLayoutManager().scrollToPosition(0);
    }
}