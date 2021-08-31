package com.pukka.ydepg.moudule.vod.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODListCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Controller.VODListController;
import com.pukka.ydepg.common.http.v6bean.v6node.CacheBean;
import com.pukka.ydepg.common.http.v6bean.v6node.Genre;
import com.pukka.ydepg.common.http.v6bean.v6node.ProduceZone;
import com.pukka.ydepg.common.http.v6bean.v6node.SortFilterBean;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.SubjectVODList;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODFilter;
import com.pukka.ydepg.common.http.v6bean.v6node.YearFilterBean;
import com.pukka.ydepg.common.http.v6bean.v6request.GetContentConfigRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QuerySubjectDetailRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODSubjectListRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.GetContentConfigResponse;
import com.pukka.ydepg.common.utils.CacheUtils;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.search.presenter.BasePresenter;
import com.pukka.ydepg.moudule.vod.cache.VodDetailCacheService;
import com.pukka.ydepg.moudule.vod.view.MoviesListDataView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author: ld
 * @date: 2017-12-19
 */
public class MoviesListPresenter extends BasePresenter implements Presenter {

    private static final String TAG = MoviesListPresenter.class.getName();

    private MoviesListDataView mMoviesListDataView;
    //查询栏目request
    private QuerySubjectDetailRequest mQuerySubjectDetailRequest = new QuerySubjectDetailRequest();
    private VODListController mVODListController;
    //筛选获取的出产国家或地区
    private List<ProduceZone> mProduceZoneList;
    //筛选获取的年份
    private List<String> mProduceYearList;
    //筛选获取的流派ID
    private List<Genre> mGenreIDList;
    //筛选获取的排序方式
    private SortFilterBean sortType;
    //被选中的栏目subject
    private Subject mSubject;
    //列表页总页数
    private String count;
    //内容列表oldFocusView
    private View oldContentFocusView;

    private RxAppCompatActivity rxAppCompatActivity;

    private QueryVODSubjectListRequest mQueryVODSubjectListRequest = new QueryVODSubjectListRequest();

    public String getCount() {
        return count;
    }

    public void setSubject(Subject subject) {
        this.mSubject = subject;
    }

    public Subject getSubject() {
        return mSubject;
    }

    public SortFilterBean getSortType() {
        return sortType;
    }

    public void setSortType(SortFilterBean sortType) {
        this.sortType = sortType;
    }

    public List<ProduceZone> getmProduceZoneList() {
        return mProduceZoneList;
    }

    public void setmProduceZoneList(List<ProduceZone> mProduceZoneList) {
        this.mProduceZoneList = mProduceZoneList;
    }

    public List<String> getmProduceYearList() {
        return mProduceYearList;
    }

    public void setmProduceYearList(List<String> mProduceYearList) {
        this.mProduceYearList = mProduceYearList;
    }

    public List<Genre> getmGenreIDList() {
        return mGenreIDList;
    }

    public void setmGenreIDList(List<Genre> mGenreIDList) {
        this.mGenreIDList = mGenreIDList;
    }

    public MoviesListPresenter(RxAppCompatActivity rxAppCompatActivity) {
        mVODListController = new VODListController(mVODListCallBack, rxAppCompatActivity);
        this.rxAppCompatActivity = rxAppCompatActivity;
    }

    public void setMoviesListDataView(MoviesListDataView moviesListDataView) {
        mMoviesListDataView = moviesListDataView;
    }

    //请求栏目列表
    public void queryCatagoryDetail(String categoryID) {
        CacheBean bean = new CacheBean();
        boolean cacheIsValid = CacheUtils.getCategoryDetail(categoryID, System.currentTimeMillis(), bean);
        if (!TextUtils.isEmpty(bean.getCategoryDetail())) {
            useSubjectQuerySubjectList(JsonParse.json2Object(bean.getCategoryDetail(), Subject.class));
            if (!cacheIsValid) {
                return;
            }
        }
        List<String> categoryList = new ArrayList<>();
        categoryList.add(categoryID);
        mQuerySubjectDetailRequest.setSubjectIds(categoryList);
        mVODListController.querySubjectDetail(mQuerySubjectDetailRequest, compose(bindToLifecycle(rxAppCompatActivity)));
    }

    //请求栏目列表
    public void queryCatagoryDetail(String categoryID, VODListCallBack vodListCallBack) {
        CacheBean bean = new CacheBean();
        boolean cacheIsValid = CacheUtils.getCategoryDetail(categoryID, System.currentTimeMillis(), bean);
        if (!TextUtils.isEmpty(bean.getCategoryDetail())) {
            useSubjectQuerySubjectList(JsonParse.json2Object(bean.getCategoryDetail(), Subject.class));
            if (!cacheIsValid) {
                return;
            }
        }
        List<String> categoryList = new ArrayList<>();
        categoryList.add(categoryID);
        mQuerySubjectDetailRequest.setSubjectIds(categoryList);
        mVODListController.querySubjectDetail(mQuerySubjectDetailRequest, compose(bindToLifecycle(rxAppCompatActivity)), vodListCallBack);
    }

    //有子栏目的请求
    public void setCatagoryRequestParameterNew(Subject subject) {
        CacheBean bean = new CacheBean();
        boolean cacheIsValid = CacheUtils.getCategorList(subject.getID(), System.currentTimeMillis(), bean);
        if (!TextUtils.isEmpty(bean.getCategoryList())) {
            List<Subject> subjects = JsonParse.jsonToClassList(bean.getCategoryList(), Subject.class);
            if (subjects != null && subjects.size() != 0) {
                if (null != mMoviesListDataView) {
                    mMoviesListDataView.showCatagorys(subjects);
                }
                if (!cacheIsValid) {
                    return;
                }
            }
        }

        mQueryVODSubjectListRequest.setSubjectID(subject.getID());
        mQueryVODSubjectListRequest.setOffset("0");
        mQueryVODSubjectListRequest.setCount("50");
        resume();
    }

    public void resume() {
        //请求栏目列表
        mVODListController.queryVODSubjectList(mQueryVODSubjectListRequest, compose(bindToLifecycle(rxAppCompatActivity)));
    }

    //vod列表没有过滤条件请求
    public void loadMoviesContent(String offset) {
        SuperLog.debug(TAG, "loadMoviesContent offset:" + offset);
        if (mSubject != null) {
            loadMoviesContent(mSubject, offset, "24");
        }
    }

    public void resetLoadNum() {
        if (null != mMoviesListDataView) {
            mMoviesListDataView.resetLoadNum();
        }
    }

    //vod列表没有过滤条件请求
    public void loadMoviesContent(Subject subject, String offset, String count) {
        if (null != mMoviesListDataView) {
            this.mSubject = subject;
            if (offset.equals("0")) {
                mMoviesListDataView.showLoading();
            }
            QueryVODListBySubjectRequest request = new QueryVODListBySubjectRequest();
            request.setSubjectID(subject.getID());
            request.setCount(count);
            request.setOffset(offset);
            SuperLog.error(TAG, "queryVODSubjectList-> request->" + subject.getName());
            mVODListController.queryVODListBySubject(request, compose(bindToLifecycle(rxAppCompatActivity)), subject.getID(),null);
        }
    }

    //vod列表没有过滤条件请求
    public void loadMoviesContent(String subjectID, String offset, String count, VODListCallBack vodListCallBack) {
        QueryVODListBySubjectRequest request = new QueryVODListBySubjectRequest();
        request.setSubjectID(subjectID);
        request.setCount(count);
        request.setOffset(offset);
        mVODListController.queryVODListBySubject(request, compose(bindToLifecycle(rxAppCompatActivity)), subjectID, vodListCallBack);
    }

    //vod列表有过滤条件请求
    public void loadVODListBySubject(String offset, String count, VODFilter vodFilter, String sortType) {
        if (mSubject != null) {
            loadVODListBySubject(mSubject, offset, count, vodFilter, sortType);
        }
    }

    //vod列表有过滤条件请求
    public void loadVODListBySubject(Subject subject, String offset, String count, VODFilter vodFilter, String sortType) {
        this.mSubject = subject;
        QueryVODListBySubjectRequest request = new QueryVODListBySubjectRequest();
        request.setSubjectID(subject.getID());
        request.setCount(count);
        request.setOffset(offset);
        request.setVODFilter(vodFilter);
        request.setSortType(sortType);
        SuperLog.debug(TAG, "queryVODSubjectList->  offset>=24 request->" + subject.getName());
        mVODListController.queryVODListBySubject(request, compose(bindToLifecycle(rxAppCompatActivity)), subject.getID(),null);
    }

    //加载筛选内容
    public void loadFilterContent() {
        setmProduceYearList(null);
        setmProduceZoneList(null);
        setSortType(null);
        setmGenreIDList(null);
        //判断终端配置参数里是否有值
        if (getFilterFromConfiguration()) {
            return;
        }
        GetContentConfigResponse getContentConfigResponse = VodDetailCacheService.getInstance().getGetContentConfigResponse();
        if (getContentConfigResponse != null) {
            setContentConfig(getContentConfigResponse);
        } else {
            GetContentConfigRequest getContentConfigRequest = new GetContentConfigRequest();
            mVODListController.getContentConfig(getContentConfigRequest, compose(bindToLifecycle(rxAppCompatActivity)));
        }

    }

    public boolean getFilterFromConfiguration() {
        boolean isHasValue = false;
        String genreStr = SessionService.getInstance().getSession().getTerminalConfigurationValue("Genre");
        String produceZoneStr = SessionService.getInstance().getSession().getTerminalConfigurationValue("ProduceZone");
        if (null != genreStr && null != produceZoneStr) {
            String[] genreStrs = genreStr.split(",");
            String[] produceZoneStrs = produceZoneStr.split(",");

            List<ProduceZone> produceZones = new ArrayList<>();
            ProduceZone produceZoneBean = new ProduceZone();
            produceZoneBean.setName("全部");
            produceZones.add(produceZoneBean);

            List<Genre> genres = new ArrayList<>();
            Genre genreBean = new Genre();
            genreBean.setGenreName("全部");
            genres.add(genreBean);

            for (int i = 0; i < genreStrs.length; i++) {
                String[] values = genreStrs[i].split("\\|");
                if (values.length > 1) {
                    Genre genre = new Genre();
                    genre.setGenreID(values[0]);
                    genre.setGenreName(values[1]);
                    genres.add(genre);
                }

            }
            for (String string : produceZoneStrs) {
                String[] values = string.split("\\|");
                if (values.length > 1) {
                    ProduceZone produce = new ProduceZone();
                    produce.setID(values[0]);
                    produce.setName(values[1]);
                    produceZones.add(produce);
                }
            }
            if (genres.size() > 1 && produceZones.size() > 1) {
                isHasValue = true;
                showFilter(genres, produceZones);
            }
        }

        return isHasValue;
    }

    //获得筛选内容的处理
    public void showFilter(List<Genre> genres, List<ProduceZone> produceZones) {
        List<SortFilterBean> sortFilterBeanList = new ArrayList<>();
        SortFilterBean sort1 = new SortFilterBean();
        sort1.setName("好评");
        sort1.setSortType("AVGSCORE:DESC");
        sortFilterBeanList.add(sort1);
        SortFilterBean sort3 = new SortFilterBean();
        sort3.setName("热门");
        sort3.setSortType("PLAYTIMES:DESC");
        sortFilterBeanList.add(sort3);
        SortFilterBean sort2 = new SortFilterBean();
        sort2.setName("最近");
        sort2.setSortType("STARTTIME:DESC");
        sortFilterBeanList.add(sort2);
        List<YearFilterBean> yearList = new ArrayList<>();

        YearFilterBean yearFilterBeanAll = new YearFilterBean();
        yearFilterBeanAll.setName("全部");
        yearFilterBeanAll.setFilterType("all");
        yearList.add(yearFilterBeanAll);
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        //全部 2021-2015 更早 一共展示9个可选年份
        for (int i = 0; i < 8; i++) {
            YearFilterBean yearFilterBean = new YearFilterBean();
            if(i == 7){
                yearFilterBean.setName("更早");
            }else{
                yearFilterBean.setName("" + (thisYear - i));
            }
            yearFilterBean.setFilterType("" + (thisYear - i));
            yearList.add(yearFilterBean);
        }
        if (null != mMoviesListDataView) {
            mMoviesListDataView.showFilterContent(sortFilterBeanList, genres, produceZones, yearList);
        }
    }

    private void setContentConfig(GetContentConfigResponse response) {
        List<ProduceZone> produceZones = new ArrayList<>();
        ProduceZone produceZoneBean = new ProduceZone();
        produceZoneBean.setName("全部");
        produceZones.add(produceZoneBean);
        produceZones.addAll(response.getProduceZones());
        List<Genre> genres = new ArrayList<>();
        Genre genreBean = new Genre();
        genreBean.setGenreName("全部");
        genres.add(genreBean);
        genres.addAll(response.getGenres());
        showFilter(genres, produceZones);
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    //判断是否有子栏目，
    private void useSubjectQuerySubjectList(Subject subject) {
        if (null == mMoviesListDataView||subject == null) return;
        mMoviesListDataView.showCategoryName(subject);
        String hasChildren = subject.getHasChildren();
        if (!TextUtils.isEmpty(hasChildren)) {
            //1:有子栏目的情况
            if (hasChildren.equals("1")) {
                setCatagoryRequestParameterNew(subject);
            } else {
                SuperLog.debug(TAG, "useSubjectQuerySubjectList->loadMoviesContent ");
                mMoviesListDataView.hideLine();
                mMoviesListDataView.hideCategory();
                loadMoviesContent(subject, "0", "24");
                resetLoadNum();
            }
        }
    }


    private VODListCallBack mVODListCallBack = new VODListCallBack() {

        //vod列表获取成功
        @Override
        public void queryVODListBySubjectSuccess(final int count, List<VOD> vodList, String subjectId) {
            SuperLog.error(TAG, " queryVODListBySubject Success vod count:" + count + "|vodList:" + vodList.size());
            if (null == mMoviesListDataView) return;
            mMoviesListDataView.hideLoading();
            if (mSubject != null && mSubject.getID().equals(subjectId)) {
                mMoviesListDataView.showTogalPage(count);
                if (vodList.size() == 0 && mMoviesListDataView.getLoadNum() == 1) {
                    SuperLog.debug(TAG, " queryVODListBySubjectSuccess vodList.size() == 0 && mMoviesListDataView.getLoadNum() == 1");
                    mMoviesListDataView.showNoContent();
                } else if (vodList.size() != 0) {
                    SuperLog.debug(TAG, "vodList.size() !=0");
                    mMoviesListDataView.showMoviesContent(vodList,subjectId);
                }
            }
        }

        @Override
        public void queryVODListBySubjectFailed() {
            if (null != mMoviesListDataView) {
                mMoviesListDataView.setLoadNum();
                mMoviesListDataView.hideLoading();
            }
        }

        @Override
        public void queryVODSubjectListSuccess(int total, List<Subject> subjects) {
            if (subjects != null && subjects.size() != 0) {
                String categoryId = subjects.get(0).getParentSubjectID();
                CacheUtils.saveCategoryList(categoryId, System.currentTimeMillis(), JsonParse.listToJsonString(subjects));
                if (null != mMoviesListDataView) {
                    mMoviesListDataView.showCatagorys(subjects);
                }
            }
            if (null != mMoviesListDataView) {
                mMoviesListDataView.hideLoading();
            }

        }

        @Override
        public void queryVODSubjectListFailed() {
            if (null != mMoviesListDataView) {
                mMoviesListDataView.hideLoading();
            }

        }

        //栏目列表获取成功
        @Override
        public void querySubjectVODBySubjectIDSuccess(int total, List<SubjectVODList> subjectVODLists) {
            List<Subject> subjects = new ArrayList<>();
            if (subjectVODLists != null && subjectVODLists.size() != 0) {
                for (SubjectVODList subjectVODList : subjectVODLists) {
                    subjects.add(subjectVODList.getSubject());
                }
            }
            if (subjects != null && subjects.size() != 0) {
                String categoryId = subjects.get(0).getParentSubjectID();
                CacheUtils.saveCategoryList(categoryId, System.currentTimeMillis(), JsonParse.listToJsonString(subjects));
            }
            if(null!=mMoviesListDataView)
            mMoviesListDataView.showCatagorys(subjects);
        }

        @Override
        public void querySubjectVODBySubjectIDFailed() {
            if (null != mMoviesListDataView) {
                mMoviesListDataView.showError(null);
            }
        }

        //筛选内容获取成功
        @Override
        public void getContentConfigSuccess(List<ProduceZone> produceZoneList, List<Genre> genreList) {
            List<ProduceZone> produceZones = new ArrayList<>();
            ProduceZone produceZoneBean = new ProduceZone();
            produceZoneBean.setName("全部");
            produceZones.add(produceZoneBean);
            produceZones.addAll(produceZoneList);
            for (ProduceZone produceZone : produceZones) {
                Log.d("TAG", produceZone.getName());
            }
            List<Genre> genres = new ArrayList<>();
            Genre genreBean = new Genre();
            genreBean.setGenreName("全部");
            genres.add(genreBean);
            genres.addAll(genreList);
            for (Genre genre : genres) {
                Log.d("TAG", genre.getGenreName());
            }
            List<SortFilterBean> sortFilterBeanList = new ArrayList<>();
            SortFilterBean sort2 = new SortFilterBean();
            sort2.setName("最近");
            sort2.setSortType("STARTTIME:DESC");
            sortFilterBeanList.add(sort2);
            SortFilterBean sort1 = new SortFilterBean();
            sort1.setName("好评");
            sort1.setSortType("AVGSCORE:DESC");
            sortFilterBeanList.add(sort1);
            SortFilterBean sort3 = new SortFilterBean();
            sort3.setName("热门");
            sort3.setSortType("PLAYTIMES:DESC");
            sortFilterBeanList.add(sort3);

            List<YearFilterBean> yearList = new ArrayList<>();
            YearFilterBean yearFilterBeanAll = new YearFilterBean();
            yearFilterBeanAll.setName("全部");
            yearFilterBeanAll.setFilterType("all");
            yearList.add(yearFilterBeanAll);
            int thisYear = Calendar.getInstance().get(Calendar.YEAR);
            //全部 2021-2015 更早 一共展示9个可选年份
            for (int i = 0; i < 8; i++) {
                YearFilterBean yearFilterBean = new YearFilterBean();
                if(i == 7){
                    yearFilterBean.setName("更早");
                }else{
                    yearFilterBean.setName("" + (thisYear - i));
                }
                yearFilterBean.setFilterType("" + (thisYear - i));
                yearList.add(yearFilterBean);
            }
            if (null != mMoviesListDataView) {
                mMoviesListDataView.showFilterContent(sortFilterBeanList, genres, produceZones, yearList);
            }
        }

        @Override
        public void getContentConfigFailed() { }

        @Override
        public void querySubjectDetailSuccess(int total, List<Subject> subjects) {
            if (subjects != null && subjects.size() != 0) {
                Subject subject = subjects.get(0);
                if (subject != null) {
                    CacheUtils.saveCategoryDetail(subject.getID(), System.currentTimeMillis(), JsonParse.object2String(subject));
                    useSubjectQuerySubjectList(subject);
                }
            }
        }

        @Override
        public void queryPSBRecommendSuccess(int total, List<VOD> vodDetails) { }

        @Override
        public void queryPSBRecommendFail() { }

        @Override
        public void onError() {
            if (null != mMoviesListDataView) {
                mMoviesListDataView.hideLoading();
                mMoviesListDataView.showError(null);
            }
        }
    };

    //隐藏筛选条件
    public void hideFilterCondition() {
        if (null != mMoviesListDataView) {
            mMoviesListDataView.hideFilter();
        }
    }

    public void setSearchFocus() {
        if (null != mMoviesListDataView) {
            mMoviesListDataView.setSearchFocus();
        }
    }

    public void resetContentStatus() {
        if (null != mMoviesListDataView) {
            mMoviesListDataView.resetContentStatus();
        }
    }

    public void resetFilterField() {
        if (null != mMoviesListDataView) {
            mMoviesListDataView.resetFilterField();
        }
    }

    public View getOldContentFocusView() {
        return oldContentFocusView;
    }


    public void setOldContentFocusView(View oldContentFocusView) {
        this.oldContentFocusView = oldContentFocusView;
    }
}
