package com.pukka.ydepg.launcher.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.v6bean.v6node.BookmarkItem;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryBookmarkResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryFavoriteResponse;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.IsTV;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.bean.node.ForceLifecycleEvent;
import com.pukka.ydepg.launcher.bean.node.SubjectVodsList;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.ui.template.HorizontalScrollSquareTemplate;
import com.pukka.ydepg.launcher.ui.template.HorizontalScrollTemplate;
import com.pukka.ydepg.launcher.ui.template.MyFunctionTemplate;
import com.pukka.ydepg.launcher.ui.template.MyListTemplate;
import com.pukka.ydepg.launcher.ui.template.PHMTemplate;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.launcher.util.Utils;
import com.pukka.ydepg.moudule.featured.bean.VODMining;
import com.pukka.ydepg.moudule.featured.bean.VodBean;
import com.pukka.ydepg.moudule.featured.view.LauncherService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的页面通用化，支持PHM配置内容
 *
 * @FileName: com.pukka.ydepg.launcher.ui.fragment.MyPHMFragment.java
 * @author: luwm
 * @data: 2018-06-22 15:56
 * @Version V1.0 <描述当前版本功能>
 */
public class MyPHMFragment extends PHMFragment {
    private MyFunctionTemplate myFunctionTemplate;
    private HorizontalScrollTemplate mHorizontalScrollTemplate;
    private HorizontalScrollSquareTemplate mHorizontalScrollSquareTemplate;
    private MyListTemplate historyTemplate;
    private MyListTemplate favoriteTemplate;
    private List<VOD> mBookMarkVods = new ArrayList<>();
    private List<VodBean> mBookMarkVodBeans = new ArrayList<>();
    private List<VodBean> mCachedBookMarkBeans = new ArrayList<>();
    private List<VOD> mFavoriteVods = new ArrayList<>();
    private List<VodBean> mFavoriteVodBeans = new ArrayList<>();
    private List<VodBean> mCachedFavoriteBeans = new ArrayList<>();
    private boolean showUserDetail = false;//是否点击了用户头像
    private boolean isFirstLoadData = true;

    public boolean isFirstLoadData() {
        return isFirstLoadData;
    }

    public boolean isShowUserDetail() {
        return showUserDetail;
    }

    public void setShowUserDetail(boolean showUserDetail) {
        this.showUserDetail = showUserDetail;
    }

    @Override
    public void loadVODData(List<SubjectVodsList> subjectVODLists) {
        super.loadVODData(subjectVODLists);
        loadMyData();
    }

    @Override
    public View getFirstFocusView() {
        if (null != myFunctionTemplate) {
            return myFunctionTemplate.getFirstView();
        } else if (null != mHorizontalScrollTemplate){
            return mHorizontalScrollTemplate.getFirstView();
        } else if (null != mHorizontalScrollSquareTemplate){
            return mHorizontalScrollSquareTemplate.getFirstView();
        }else {
            return null;
        }
    }

    public List<VOD> getmBookMarkVods() {
        return mBookMarkVods;
    }


    public List<VodBean> getmBookMarkVodBeans() {
        return mBookMarkVodBeans;
    }


    public List<VOD> getmFavoriteVods() {
        return mFavoriteVods;
    }

    public List<VodBean> getmFavoriteVodBeans() {
        return mFavoriteVodBeans;
    }

    @Override
    public void scrollToTop() {
        super.scrollToTop();
        if (getActivity() instanceof MainActivity){
            ((MainActivity)getActivity()).resetMarginForTopView(-1);
            if (null != ((MainActivity)getActivity()).getLauncherFocusHelper()){
                ((MainActivity)getActivity()).getLauncherFocusHelper().resetMarginForTopView();
            }
        }
        if (null != myFunctionTemplate) {
            myFunctionTemplate.scrollToTop();
        }else if (null != mHorizontalScrollTemplate){
            mHorizontalScrollTemplate.scrollToTop();
        }else if (null != mHorizontalScrollSquareTemplate){
            mHorizontalScrollSquareTemplate.scrollToTop();
        }
    }

    public void setMyFunctionTemplate(PHMTemplate template) {
        myFunctionTemplate = (MyFunctionTemplate) template;
    }

    public void setHorizontalScrollTemplate(PHMTemplate template) {
        mHorizontalScrollTemplate = (HorizontalScrollTemplate) template;
    }
    public void setHorizontalScrollSquareTemplate(PHMTemplate template) {
        mHorizontalScrollSquareTemplate = (HorizontalScrollSquareTemplate) template;
    }

    public void setHistoryTemplate(MyListTemplate historyTemplate) {
        this.historyTemplate = historyTemplate;
    }

    public void setFavoriteTemplate(MyListTemplate favoriteTemplate) {
        this.favoriteTemplate = favoriteTemplate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        SuperLog.debug(TAG,"MyPHMFragment->onResume|NotCanPlayNow:"+getNotCanPlayNow() + "UserVisibleHint="+getUserVisibleHint());
        if(!getNotCanPlayNow() && getUserVisibleHint()) {
            loadMyData();//我的页面每次切换都需要重新请求收藏和历史
        }
    }

    public void loadMyData() {
        SuperLog.debug(TAG,"Begin to update Personal data in MyPHMFragment");
        queryFavorite();
        queryBookMark();
    }

    @Subscribe
    public void forceLifecycle(ForceLifecycleEvent forceLifecycleEvent){
        if(ForceLifecycleEvent.PAUSE==forceLifecycleEvent.getState()){
            onPause();
            setNotCanPlayNow(true);
        }else{
            setNotCanPlayNow(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //TODO modified by liuxia at 2020118 优化代码,调用放此处不确定是否有影响
        dismissMiguQRViewPopWindow();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void queryFavorite() {
        presenter.queryFavorite(new RxCallBack<QueryFavoriteResponse>(HttpConstant.QUERYFAVORITE, getActivity()) {
            @Override
            public void onSuccess(QueryFavoriteResponse queryFavoriteResponse) {
                isFirstLoadData = false;
                String retCode = queryFavoriteResponse.getResult().getRetCode();
                if (TextUtils.equals(retCode, Result.RETCODE_OK)) {
                    mFavoriteVods = getFavVODList(queryFavoriteResponse.getFavorites());
                    mFavoriteVodBeans = convert2SearchVODBean(mFavoriteVods);
                    if (null != mPHMAdapter && isChange(mCachedFavoriteBeans, mFavoriteVodBeans) && -1 != mPHMAdapter.getTemplateFavoriteIndex()) {
                        setMineFocus();
                        mPHMAdapter.notifyItemChanged(mPHMAdapter.getTemplateFavoriteIndex());
                        scrollToTop();
                    }
                    if (null != mPHMAdapter && CollectionUtil.isEmpty(mFavoriteVodBeans) && -1 != mPHMAdapter.getTemplateFavoriteIndex()) {
                        mPHMAdapter.notifyItemChanged(mPHMAdapter.getTemplateFavoriteIndex());
                    }
                    mCachedFavoriteBeans = mFavoriteVodBeans;
                }
            }

            @Override
            public void onFail(Throwable e) {
                isFirstLoadData = false;
                if (null != mPHMAdapter && mPHMAdapter.getTemplateFavoriteIndex() != -1){
                    mPHMAdapter.notifyItemChanged(mPHMAdapter.getTemplateFavoriteIndex());
                }
            }
        });
    }

    /**
     *
     */
    private void queryBookMark() {
        presenter.queryBookMark(new RxCallBack<QueryBookmarkResponse>(HttpConstant.QUERYBOOKMARK, getActivity()) {
            @Override
            public void onSuccess(QueryBookmarkResponse queryBookmarkResponse) {
                isFirstLoadData = false;
                String retCode = queryBookmarkResponse.getResult().getRetCode();
                if (TextUtils.equals(retCode, Result.RETCODE_OK)) {
                    mBookMarkVods = getBookMarkVODList(queryBookmarkResponse.getBookmarks());
                    mBookMarkVodBeans = convert2SearchVODBean(mBookMarkVods);
                    if (null != mPHMAdapter && isChange(mCachedBookMarkBeans, mBookMarkVodBeans) && -1 != mPHMAdapter.getTemplateHistrotyIndex()) {
                        setMineFocus();
                        mPHMAdapter.notifyItemChanged(mPHMAdapter.getTemplateHistrotyIndex());
                        scrollToTop();
                    }
                    if (null != mPHMAdapter && CollectionUtil.isEmpty(mBookMarkVodBeans) && -1 != mPHMAdapter.getTemplateHistrotyIndex()) {
                        mPHMAdapter.notifyItemChanged(mPHMAdapter.getTemplateHistrotyIndex());
                    }
                    mCachedBookMarkBeans = mBookMarkVodBeans;
                }
            }

            @Override
            public void onFail(Throwable e) {
                isFirstLoadData = false;
                if (null != mPHMAdapter){
                    mPHMAdapter.notifyItemChanged(mPHMAdapter.getTemplateHistrotyIndex());
                }
            }
        });
    }

    /**
     * 设置当前我的导航栏获取焦点
     */
    private void setMineFocus() {
        if (getActivity() instanceof MainActivity && !SharedPreferenceUtil.getInstance().getIsChildrenEpg()
                && !OTTApplication.getContext().getSearchFocus()) {
            MainActivity activity = (MainActivity) getActivity();
            if (null != activity.getLauncherFocusHelper() && activity.getCurrentFragment() instanceof MyPHMFragment) {
                activity.getLauncherFocusHelper().clearFocusEffect();
                activity.getLauncherFocusHelper().navItemGetFocus(LauncherService.getInstance().getMineIndex());
            }
        }
    }

    public void dismissMiguQRViewPopWindow() {
        if (null != favoriteTemplate) {
            favoriteTemplate.dismissMiguQRViewPopWindow();
        }
        if (null != historyTemplate) {
            historyTemplate.dismissMiguQRViewPopWindow();
        }
    }

    /**
     * 从BookmarkItem取出VOD
     *
     * @param bookmarks
     * @return
     */
    public List<VOD> getBookMarkVODList(List<BookmarkItem> bookmarks) {
        if (CollectionUtil.isEmpty(bookmarks)) {
            return new ArrayList<>();
        }
        List<BookmarkItem> items=new ArrayList<>();
        //儿童模式只展示儿童模式书签
        if(SharedPreferenceUtil.getInstance().getIsChildrenEpg())
        {
            for (int i = 0; i < bookmarks.size(); i++)
            {
                if (IsTV.isChildMode(bookmarks.get(i).getVOD()) && IsTV.isNotVRBM(bookmarks.get(i).getVOD()))
                {
                    if (items.size() < 5){
                        items.add(bookmarks.get(i));
                    }else{
                        break;
                    }
                }
            }
        }else{
            for (int i = 0; i < bookmarks.size(); i++)
            {
                if (IsTV.isNotVRBM(bookmarks.get(i).getVOD()))
                {
                    if (items.size() < 5){
                        items.add(bookmarks.get(i));
                    }else{
                        break;
                    }
                }
            }
        }
        List<VOD> bookmarkVODList = new ArrayList<>();
        for (BookmarkItem bookmarkItem : items) {
            bookmarkVODList.add(bookmarkItem.getVOD());
        }
        return bookmarkVODList;
    }


    /**
     * 从Content取出VOD
     *
     * @param favorites
     * @return
     */
    public List<VOD> getFavVODList(List<Content> favorites) {
        if (CollectionUtil.isEmpty(favorites)) {
            return new ArrayList<>();
        }
        List<VOD> favOfVODList = new ArrayList<>();
        for (Content favItem : favorites) {
            if (IsTV.isNotVRFar(favItem.getVOD())){
                if (favOfVODList.size() < 5){
                    favOfVODList.add(favItem.getVOD());
                }else{
                    break;
                }
            }
        }
        return favOfVODList;
    }

    /**
     * 判断数据是否有变化，有变化再刷新页面
     *
     * @return
     */
    private boolean isChange(List<VodBean> oldList, List<VodBean> newList) {
        if (CollectionUtil.isEmpty(oldList) && !CollectionUtil.isEmpty(newList)) {
            return true;
        }
        if (oldList.size() != newList.size()) {
            return true;
        }
        for (int i = 0; i < oldList.size(); i++) {
            if (!oldList.get(i).getId().equalsIgnoreCase(newList.get(i).getId())) {
                return true;
            }
            if (oldList.get(i).getRate() != null && !oldList.get(i).getRate().equalsIgnoreCase(newList
                    .get(i)
                    .getRate())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 把VOD转换成vodbean
     *
     * @param vodList
     * @return
     */
    public static List<VodBean> convert2SearchVODBean(List<VOD> vodList) {
        List<VodBean> vodBeanList = new ArrayList<>();
        if (!CollectionUtil.isEmpty(vodList)) {
            for (VOD vod : vodList) {
                if (null == vod)continue;
                VodBean vodBean = new VodBean();
                vodBean.setId(vod.getID());
                vodBean.setName(vod.getName());
                vodBean.setRate(Utils.formatRate(vod.getAverageScore()));
                vodBean.setHD(VODMediaFile.Definition.HD.equals(VODMining.getDefinition(vod)));
                vodBean.setDefinition(VODMining.getDefinition(vod));
                vodBean.setCpId(vod.getCpId());
                vodBean.setVODType(vod.getVODType());
                vodBean.setCustomFields(vod.getCustomFields());
                vodBean.setBookmark(vod.getBookmark());
                if (VodUtil.isMiguVod(vod)) {
                    vodBean.setPoster(VODMining.getMiguPoster(vod));
                } else {
                    vodBean.setPoster(VODMining.getPoster(vod));
                }
                vodBean.setHCSSlaveAddressList(VODMining.getHCSSlaveAddressList(vod));
                vodBean.setSubjectIds(vod.getSubjectIDs());
                vodBeanList.add(vodBean);
            }
        }
        return vodBeanList;
    }
}