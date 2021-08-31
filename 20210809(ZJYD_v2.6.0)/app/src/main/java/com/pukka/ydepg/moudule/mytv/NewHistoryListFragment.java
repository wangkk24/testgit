package com.pukka.ydepg.moudule.mytv;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.BookmarkItem;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.IsTV;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.event.BookmarkChangeEvent;
import com.pukka.ydepg.event.DeleteHistoryEvent;
import com.pukka.ydepg.event.HistoryListRequestFocusEvent;
import com.pukka.ydepg.event.HistoryListScrollToTopEvent;
import com.pukka.ydepg.moudule.mytv.adapter.NewHistoryListAdapter;
import com.pukka.ydepg.moudule.mytv.presenter.HistoryPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.view.HistoryDataView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * 新的观看记录界面的列表页面
 *
 * @version V2.1
 * @FileName: com.pukka.ydepg.moudule.mytv.NewHistoryListFragment.java
 * @author:weicy
 * @date: 2019-10-30 11:32
 */
public class NewHistoryListFragment extends Fragment implements HistoryDataView ,View.OnClickListener, View.OnFocusChangeListener,View.OnKeyListener {

    private static final String TAG = "NewHistoryListFragment";

    /**
     * 查询书签控制器
     */
    HistoryPresenter mPresenter;

    /**
     * 删除时弹窗
     */
    private PopupWindow mPopupWindow;

    //列表adpater
    NewHistoryListAdapter mAdapter;

    //展示列表
    VerticalGridView mRecyclerView;

    //删除按钮
    TextView mDeleteBtn;

    //无数据视图
    View mNoData;
    TextView mNoDataIntro;

    //今天的播放记录数组
    List<BookmarkItem> mBookmarkItemListToday = new ArrayList<>();

    //昨天的播放记录数组
    List<BookmarkItem> mBookmarkItemListYesterday = new ArrayList<>();

    //一周内的播放记录数组
    List<BookmarkItem> mBookmarkItemListThisWeek = new ArrayList<>();

    //更早的播放记录数组
    List<BookmarkItem> mBookmarkItemListThisMouth = new ArrayList<>();

    //展示数据的数组
    List<List<BookmarkItem>> mBookmarkList = new ArrayList<>();

    //存储每个时间段播放记录个数的数组
    List<String> countArr = new ArrayList<>();

    /*
     * 为了实现列表滚动时焦点始终在中间，
     * 将每个时间段的播放记录拆分成五个一组，
     * 分别用一行recycleview进行展示
     */

    //五个一组的展示的数组
    List<List<BookmarkItem>> mBookmarkListPre5 = new ArrayList<>();

    //无分类的观看记录List
    List<BookmarkItem> mBookmarkItemList = new ArrayList<>();

    //删除的Bookmark
    List<BookmarkItem> mDeleteList = new ArrayList<>();

    /*当前展示的内容
     1为宽带电视，其他为咪咕爱看
     */
    private String viewType  = "1";

    public final static String VIEW_TYPE_TV = "1";
    public final static String VIEW_TYPE_PHONE = "0";

    private int pagerSize = 30;

    //当前是否是编辑状态
    boolean isEditing = false;

    //记录删除前的焦点位置
    int section = 0;
    int postion = 0;
//    int lastVisiblePostion = 0;

    //用来防止刷新列表时左侧标签出现焦点特效
    public boolean showFocus = true;

    //防止重复点击
    public boolean alreadyDelete = false;

    //记录焦点移动到删除按钮之前的焦点位置
    int num = 0;

    /*************************************带参数的构造函数********************************************/

    static NewHistoryListFragment newInstance(String viewType) {
        NewHistoryListFragment f = new NewHistoryListFragment();
        Bundle b = new Bundle();
        b.putString("viewtype",viewType);
        f.setArguments(b);
        return f;
    }

    /*************************************Fragment的生命周期********************************************/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //取viewType的值
        Bundle args = getArguments();
        if (args != null) {
            String viewType = args.getString("viewtype");
            if (!TextUtils.isEmpty(viewType)){
                setViewType(viewType);
            }
        }

        //注册EventBus
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mytv_history_list_recyclerview,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mPresenter = new HistoryPresenter((RxAppCompatActivity) getActivity());
        mPresenter.setDataView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDeleteList.clear();
        isEditing = false;
        //增加延时，防止详情页回来之后书签未能刷新
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        },250);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter = null;
    }

    /*************************************使用到的自定义方法********************************************/

    private void initView(View view){
        mNoData = view.findViewById(R.id.no_data);
        mNoDataIntro = (TextView) view.findViewById(R.id.no_data_intro);

        //设置无数据视图的信息
        mNoDataIntro.setText(getResources().getString(R.string.has_no_watch_history_new));

        //初始化RecycleView
        mRecyclerView = (VerticalGridView) view.findViewById(R.id.recyclerview);


//        mRecyclerView.setInterceptor(new FocusInterceptor() {
//            @Override
//            public boolean interceptFocus(KeyEvent event, View view) {
//                return false;
//            }
//        });
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setFocusable(false);
        mRecyclerView.setNumColumns(1);
        mRecyclerView.setAdapter(mAdapter = new NewHistoryListAdapter(getContext(),mBookmarkListPre5,countArr));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (null != getActivity() && !getActivity().isFinishing()){
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        Glide.with(getActivity().getApplicationContext()).resumeRequests();
                    }else {
                        Glide.with(getActivity().getApplicationContext()).pauseRequests();
                    }
                }
            }
        });

        mDeleteBtn = (TextView) view.findViewById(R.id.history_list_dele);
        mDeleteBtn.setVisibility(View.INVISIBLE);
        mDeleteBtn.setOnClickListener(this);
        mDeleteBtn.setOnFocusChangeListener(this);
        mDeleteBtn.setOnKeyListener(this);
    }

    private void initData(){
        if (null == mPresenter){
            return;
        }
        pagerSize= 50;
        mPresenter.queryBookmark(pagerSize,0);
    }

    //将获取到的bookmarkList对时间进行分类展示
    private void separateBookmarkList(){

        //根据viewtype过滤掉tv或者手机端信息

        List<BookmarkItem> tempList = new ArrayList<>();
        tempList.addAll(mBookmarkItemList);
        Log.i(TAG, "separateBookmarkList:   "+mBookmarkItemList.size());
        mBookmarkItemList.clear();

        for (int i = 0; i < tempList.size(); i++) {
            BookmarkItem item = tempList.get(i);
            VOD vod = item.getVOD();

            Bookmark bookmark = vod.getBookmark();
            long bookmarkMils = Long.parseLong(bookmark.getUpdateTime());

            if (viewType.equals(VIEW_TYPE_TV)){
                if (IsTV.isTV(vod)){
                    mBookmarkItemList.add(item);
                }
            }else{
                if (IsTV.isMigu(vod)){
                    mBookmarkItemList.add(item);
                }
            }

        }

        if (mBookmarkItemList.size() == 0){
            mNoData.setVisibility(View.VISIBLE);
            mDeleteBtn.setVisibility(View.INVISIBLE);
        }

        clearBookmarkList();


        Calendar cal = Calendar.getInstance();
        long todayHoursSeconds    = cal.get(Calendar.HOUR_OF_DAY) * 60L * 60L;
        long todayMinutesSeconds  = cal.get(Calendar.MINUTE) * 60L;
        long todaySeconds         = cal.get(Calendar.SECOND);

        long todayMillis = (todayHoursSeconds + todayMinutesSeconds + todaySeconds) * 1000;
        long oneDayMillis = 24L * 60 * 60 * 1000;
        long curTimeMillis = System.currentTimeMillis();
        long todayStartMillis = curTimeMillis - todayMillis;
        long yesterdayStartMilis = todayStartMillis - oneDayMillis;
        long oneWeekStartMils = todayStartMillis - 6*oneDayMillis;

        for (int i = 0; i < mBookmarkItemList.size(); i++) {
            BookmarkItem item = mBookmarkItemList.get(i);
            //获取时间戳
            VOD vod = item.getVOD();
            Bookmark bookmark = vod.getBookmark();
            if (null != bookmark && !TextUtils.isEmpty(bookmark.getUpdateTime())){
                long bookmarkMils = Long.parseLong(bookmark.getUpdateTime());

                if (bookmarkMils >= todayStartMillis){
                    //加入今天播放记录数组
                    mBookmarkItemListToday.add(item);
                }else if (bookmarkMils >= oneWeekStartMils && bookmarkMils < todayStartMillis){
                    //加入一周内播放记录数组
                    mBookmarkItemListThisWeek.add(item);
                }else {
                    //加入更早的播放记录数组
                    mBookmarkItemListThisMouth.add(item);
                }
            }
        }

        //20191212需求更改，取消昨天分组，如果需要重新加入昨天分组，打开以下代码
        if (mBookmarkItemListToday.size() == 0){
            //如果今天的播放记录为空，则加入昨天的播放记录
            for (int i = 0; i < mBookmarkItemList.size(); i++) {
                BookmarkItem item = mBookmarkItemList.get(i);
                //获取时间戳
                VOD vod = item.getVOD();
                Bookmark bookmark = vod.getBookmark();
                if (null != bookmark && !TextUtils.isEmpty(bookmark.getUpdateTime())){
                    long bookmarkMils = Long.parseLong(bookmark.getUpdateTime());
                    if (bookmarkMils >= yesterdayStartMilis && bookmarkMils < todayStartMillis){
                        //加入昨天的播放记录数组
                        mBookmarkItemListYesterday.add(item);
                        //将昨天的播放记录从更早的播放记录内移除
                        mBookmarkItemListThisWeek.remove(item);
                    }
                }
            }
        }
        mBookmarkList.clear();

        if (mBookmarkItemListToday.size()>0){
            mBookmarkList.add(mBookmarkItemListToday);
        }
        if (mBookmarkItemListYesterday.size()>0){
            mBookmarkList.add(mBookmarkItemListYesterday);
        }
        if (mBookmarkItemListThisWeek.size()>0){
            mBookmarkList.add(mBookmarkItemListThisWeek);
        }
        if (mBookmarkItemListThisMouth.size()>0){
            mBookmarkList.add(mBookmarkItemListThisMouth);
        }

        //将展示数组拆分成5个一组
        separateBookmarkListTo5();
    }

    private void separateBookmarkListTo5(){
        //将每个时间段的个数存储
        countArr.clear();
        for (int i = 0; i < mBookmarkList.size(); i++) {
            countArr.add(String.format(mBookmarkList.get(i).size() + ""));
        }
        //清空5个一组的数组
        mBookmarkListPre5.clear();
        //遍历按照时间拆分的数组
        for (int i = 0; i < mBookmarkList.size() ; i++) {
            List<BookmarkItem> list = mBookmarkList.get(i);
            int index = 0;
            //能被5整除就创建size/5个数组，不能就创建size/5+1个数组
            if (list.size()%5 == 0){
                index = list.size()/5;
            }else{
                index = list.size()/5+1;
            }
            List<List<BookmarkItem>> tempArr = new ArrayList<List<BookmarkItem>>();
            for (int j = 0; j < index; j++) {
                tempArr.add(new ArrayList<BookmarkItem>());
            }

            //5个一组放进新数组中
            for (int a = 0; a < list.size(); a++) {
                BookmarkItem item = list.get(a);
                int integer = a/5;
                List<BookmarkItem> bookmarkItemList = tempArr.get(integer);
                bookmarkItemList.add(item);
            }
            mBookmarkListPre5.addAll(tempArr);
        }
    }

    //清空分类后的BookmarkList
    private void clearBookmarkList(){

        mBookmarkItemListToday.clear();
        mBookmarkItemListYesterday.clear();
        mBookmarkItemListThisWeek.clear();
        mBookmarkItemListThisMouth.clear();

    }

    //退出编辑状态
    public void quieEdit(){
        isEditing = false;
        mDeleteBtn.setText(getResources().getString(R.string.mytv_history_delete));
        refreshUI(false);
    }

    //切换编辑状态
    public void refreshUI(boolean isEditing){
        mAdapter.switchEditingState(isEditing);
    }

    //删除后刷新列表，焦点落到最近的单位上
    public void nearItemRequestFocus(){
        //数据为空
        if (mBookmarkList.size() == 0){
            quieEdit();
            mNoData.setVisibility(View.VISIBLE);
            mDeleteBtn.setVisibility(View.INVISIBLE);
        }else{
            //取最近的item
            if (section >= mBookmarkListPre5.size()){
                section = mBookmarkListPre5.size() - 1;
            }

            List<BookmarkItem> list = mBookmarkListPre5.get(section);

            if (postion >= list.size()){
                postion = list.size() - 1;
            }
            Log.i(TAG, "nearItemRequestFocus:  "+ section+" "+ postion);

//            if (lastVisiblePostion >= list.size()){
//                lastVisiblePostion = list.size() - 1;
//            }

            View view = getmRecyclerView().getLayoutManager().findViewByPosition(section);

            if (null == view){
                showFocus = true;
                return ;
            }

            if (null != view.findViewById(R.id.mytv_history_list_item_list)){
                RecyclerView bookmarkList = view.findViewById(R.id.mytv_history_list_item_list);
//                bookmarkList.scrollToPosition(lastVisiblePostion);

                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        LinearLayoutManager layoutManager = (LinearLayoutManager) bookmarkList.getLayoutManager();
                        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();

                        if (postion - firstItemPosition < 0){
                            showFocus = true;
                            return;
                        }

                        View view = bookmarkList.getLayoutManager().findViewByPosition(postion);
                        if (null != view){
                            view.requestFocus();
                        }
                        showFocus = true;
                    }
                },100);
            }
        }
    }

    //删除弹框
    private void showDeleteDialog(View parent, final boolean deleteAll) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_delete_collection_layout_new, null);
        View ok = view.findViewById(R.id.mytv_dialog_sure);
        View cancel = view.findViewById(R.id.mytv_dialog_cancel);
        if (null == mPopupWindow) {
            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        }
        ok.requestFocus();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDeleteList.clear();
                mDeleteList.addAll(mBookmarkItemList);
                mPresenter.deleteBookmark(mDeleteList,false);

                mPopupWindow.dismiss();

            }
        });
         mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }


    /*************************************EventBus方法********************************************/


    @Subscribe
    public void onEvent(BookmarkChangeEvent event){
        initData();
    }

    //列表滚动至顶部，防止信息显示不全
    @Subscribe
    public void onEvent(HistoryListScrollToTopEvent event){
        int postion = event.getPostion();
        mRecyclerView.smoothScrollToPosition(postion);
    }

    //焦点聚焦到删除按钮上
    @Subscribe
    public void onEvent(HistoryListRequestFocusEvent event){
        if (event.getViewNeedFocus().equals(HistoryListRequestFocusEvent.REQUEST_FOCUS_LIST_FRAGMENT)){
            if (mDeleteBtn.getVisibility() == View.VISIBLE){
                mDeleteBtn.requestFocus();
                num = event.getNum();
            }
        }
    }

    //删除
    @Subscribe
    public void onEvent(DeleteHistoryEvent event){

        if (!alreadyDelete){
            alreadyDelete = true;
            List<BookmarkItem>bookmarks = event.getBookmarkItems();
            boolean deleteAll = event.isDeleteAll();
            this.section = event.getSection();
            this.postion = event.getPostion();

//            //记录滚动位置
//            LinearLayoutManager layoutManager = (LinearLayoutManager) getmRecyclerView().getLayoutManager();
//            int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
//            if (section - firstItemPosition >= 0){
//                View view = getmRecyclerView().getChildAt(section-firstItemPosition);
//                if (null != view){
//                    if (null != getmRecyclerView().getChildViewHolder(view)){
//                        NewHistoryListAdapter.ViewHolder holder = (NewHistoryListAdapter.ViewHolder) getmRecyclerView().getChildViewHolder(view);
//                        LinearLayoutManager manager = (LinearLayoutManager) holder.getBookmarkList().getLayoutManager();
//                        lastVisiblePostion = manager.findLastVisibleItemPosition();
//                    }
//                }
//            }



            if (null != bookmarks && bookmarks.size()>0){
                mDeleteList.clear();
                mDeleteList.addAll(bookmarks);
                mPresenter.deleteBookmark(bookmarks,false);
//                deleteBookmarkSuccess();
            }
        }



    }

    /*************************************HistoryDataView回调********************************************/
    @Override
    public void queryBookmarkSuccess(int total, List<BookmarkItem> bookmarks) {
        if (null == getActivity() || getActivity().isFinishing()){
            return;
        }

        //是否是第一次加载
        boolean isFirst = false;
        int mark = mBookmarkItemList.size();
        if (mark == 0){
            isFirst = true;
        }

        //是否需要第一项落焦
        boolean isNeedFirstFocus = false;

        if (!isFirst){

            //根据viewtype过滤掉tv或者手机端信息

            List<BookmarkItem> tempList = new ArrayList<>();

            for (int i = 0; i < bookmarks.size(); i++) {
                BookmarkItem item = bookmarks.get(i);
                VOD vod = item.getVOD();

                if (viewType.equals(VIEW_TYPE_TV)){
                    if (IsTV.isTV(vod)){
                        tempList.add(item);
                    }
                }else{
                    if (IsTV.isMigu(vod)){
                        tempList.add(item);
                    }
                }
            }

            //记录下第一项的书签时间
            BookmarkItem item = mBookmarkItemList.get(0);
            VOD vod = item.getVOD();
            Bookmark bookmark = vod.getBookmark();
            String updateTime = bookmark.getUpdateTime();
            //与请求后的第一项对比，判断需不需要刷新列表
            BookmarkItem item1 = tempList.get(0);
            VOD vod1 = item1.getVOD();
            Bookmark bookmark1 = vod1.getBookmark();
            String updateTime1 = bookmark1.getUpdateTime();

            //第一项时间不同，需要刷新列表，焦点落在第一项
            if (!updateTime.equals(updateTime1)){
                isNeedFirstFocus = true;
            }

            if (updateTime.equals(updateTime1) && mBookmarkItemList.size() == tempList.size()){
                //书签数量相同且时间相同，不需要更新列表
                if (mBookmarkItemList.size()>0){
                    mNoData.setVisibility(View.GONE);
                    mDeleteBtn.setVisibility(View.VISIBLE);
                }
                return;
            }
        }

        //是否是顶部页签切换引起的数据更新
        boolean comeFromSwitch = false;

        if (null != getActivity()){
            View history =  getActivity().findViewById(R.id.history_list_tv_bg);
            View collection = getActivity().findViewById(R.id.history_list_phone_bg);
            if (history.isSelected() || collection.isSelected()){
                comeFromSwitch = true;
            }
        }

        if (isNeedFirstFocus && !comeFromSwitch){
            showFocus = false;
        }

        mBookmarkItemList.clear();
        mBookmarkList.clear();
        clearBookmarkList();
        mNoData.setVisibility(View.GONE);
        mDeleteBtn.setVisibility(View.VISIBLE);
        if (null != bookmarks && !bookmarks.isEmpty()) {
            mBookmarkItemList.addAll(bookmarks);
            //将获取的bookmarkList进行时间分类
            separateBookmarkList();
        } else {
            mNoData.setVisibility(View.VISIBLE);
            mDeleteBtn.setVisibility(View.INVISIBLE);
        }
        mAdapter.notifyDataSetChanged();

        //落焦到第一个元素
        if (isNeedFirstFocus && !comeFromSwitch){

            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //先滚到最上方，防止第一个item取不到的情况
                    getmRecyclerView().scrollToPosition(0);

                    Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (null == getActivity() || getActivity().isFinishing()){
                                return;
                            }

                            View view = getmRecyclerView().getChildAt(0);
                            if (null == view){
                                return ;
                            }
                            if (null != getmRecyclerView().getChildViewHolder(view)){
                                NewHistoryListAdapter.ViewHolder holder = (NewHistoryListAdapter.ViewHolder) getmRecyclerView().getChildViewHolder(view);

                                View itemView = holder.getBookmarkList().getChildAt(0);

                                if (null != itemView){
                                    itemView.requestFocus();
                                }
                            }
                            showFocus = true;
                        }
                    },300);
                }
            },100);
        }
    }

    @Override
    public void queryBookmarkFail() {
        if (null == getActivity() || getActivity().isFinishing()){
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBookmarkItemList.clear();
                mBookmarkList.clear();
                clearBookmarkList();
                mAdapter.notifyDataSetChanged();
                mNoData.setVisibility(View.VISIBLE);
                mDeleteBtn.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override

    public void deleteBookmarkSuccess() {

        //删除播放记录，返回首页时刷新爱看界面数据
        OTTApplication.getContext().setIsRefreshPbsEpg(true);

        for (int i = 0; i < mBookmarkList.size(); i++) {
            List<BookmarkItem> list = mBookmarkList.get(i);
            for (int j = 0; j < mDeleteList.size(); j++) {
                BookmarkItem item = mDeleteList.get(j);
                if (list.contains(item)){
                    list.remove(item);
                    //无分类数组也删除
                    mBookmarkItemList.remove(item);
                }
            }
        }

        Iterator<List<BookmarkItem>> iterator = mBookmarkList.iterator();
        while (iterator.hasNext()) {
            List<BookmarkItem> value = iterator.next();
            if (value.size() == 0) {
                iterator.remove();
            }
        }

        alreadyDelete = false;
        showFocus = false;

        //重新对数组进行5个一组的分割
        separateBookmarkListTo5();
        mRecyclerView.setFocusable(false);

        mAdapter.notifyDataSetChanged();

        if (mBookmarkList.size() == 0){
            //没有播放记录，展示无数据页面
            mDeleteBtn.setVisibility(View.INVISIBLE);
            mNoData.setVisibility(View.VISIBLE);
            showFocus = true;
            quieEdit();
            //落焦到顶部标签
            EventBus.getDefault().post(new HistoryListRequestFocusEvent(HistoryListRequestFocusEvent.REQUEST_FOCUS_FRAGMENT));
            return;
        }

        mDeleteBtn.setFocusable(false);

        Handler handler = new Handler();
        //落焦到最近的单位上
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setFocusable(true);
                //落焦到最近的单位上
                nearItemRequestFocus();
                mDeleteBtn.setFocusable(true);
            }
        },300);
    }

    @Override
    public void deleteBookmarkFail() {

        alreadyDelete = false;

    }

    /*************************************按键回调********************************************/
    @Override
    public void onClick(View v) {
        if (!isEditing){
            //非编辑状态进入编辑状态
            isEditing = true;
            mDeleteBtn.setText(getResources().getString(R.string.mytv_history_deleteAll));
            refreshUI(isEditing);
        }else{
            //编辑状态点击清空播放记录

            showDeleteDialog(getView(), true);

//            mDeleteList.clear();
//            mDeleteList.addAll(mBookmarkItemList);
//            mPresenter.deleteBookmark(mDeleteList,true);
//            deleteBookmarkSuccess();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            mDeleteBtn.setSelected(true);
        }else{
            mDeleteBtn.setSelected(false);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT://向左
                {
                    View view = getLastItem(num);
                    if (null != view){
                        view.requestFocus();
                        return true;
                    }
                }
                case KeyEvent.KEYCODE_DPAD_UP://向上
                {
                    return true;
                }
                case KeyEvent.KEYCODE_DPAD_DOWN://向下
                {
                    return true;
                }
                case KeyEvent.KEYCODE_DPAD_RIGHT://向右
                {
                    return true;
                }
                default:
                    break;
            }
        return false;
    }

    /*************************************set&get函数********************************************/

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public VerticalGridView getmRecyclerView() {
        return mRecyclerView;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void scrollToTop(){
        if(mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    public boolean hasData(){
        return mBookmarkListPre5.size() > 0;
    }

    //获取列表第一项
    public View getFirstItem(){
        if (hasData()){
            View view = getmRecyclerView().getLayoutManager().findViewByPosition(0);
            if (null != view){
                if (null != view.findViewById(R.id.mytv_history_list_item_list)){
                    RecyclerView bookmarkList = view.findViewById(R.id.mytv_history_list_item_list);
                    return bookmarkList.getLayoutManager().findViewByPosition(0);
                }
            }
        }
        return null;
    }
    //获取列表最后一项
    public View getLastItem(int num){
        if (hasData()){
            View view = getmRecyclerView().getLayoutManager().findViewByPosition(num);
            if (null != view){
                if (null != view.findViewById(R.id.mytv_history_list_item_list)){
                    RecyclerView bookmarkList = view.findViewById(R.id.mytv_history_list_item_list);
                    if (num < mBookmarkListPre5.size()){
                        List<BookmarkItem> list = mBookmarkListPre5.get(num);
                        return bookmarkList.getLayoutManager().findViewByPosition(list.size()-1);
                    }

                }
            }
        }
        return null;
    }
}
