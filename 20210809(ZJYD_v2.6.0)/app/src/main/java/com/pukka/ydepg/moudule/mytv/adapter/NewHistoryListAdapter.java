package com.pukka.ydepg.moudule.mytv.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.BookmarkItem;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.ClickUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.event.HistoryListRequestFocusEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewHistoryListAdapter extends RecyclerView.Adapter <NewHistoryListAdapter.ViewHolder>{

    private final Context mContext;

    private volatile List<List<BookmarkItem>> dataList;

    //今天的播放记录数组是否存在
    private boolean isTodayListExist = false;

    //当前是否为编辑状态
    private boolean isEditing = false;

    //存储每个时间段播放片数的数组
    private List<String> countArr;

    public NewHistoryListAdapter(Context mContext, List<List<BookmarkItem>> dataList, List<String> countArr) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.countArr = countArr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mytv_histroy_list_item, null, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //判断是否要影藏标题 和 展示的是哪个时间段的个数
        boolean isShowTitle = true;
        int bookMarkcount = 0 ;
        if (position > 0){
            List<BookmarkItem> list1 = dataList.get(position - 1);
            List<BookmarkItem> list2 = dataList.get(position);
            List<BookmarkItem> list0 = dataList.get(0);

            BookmarkItem item1 = list1.get(0);
            VOD vod1 = item1.getVOD();
            Bookmark bookmark1 = vod1.getBookmark();

            BookmarkItem item2 = list2.get(0);
            VOD vod2 = item2.getVOD();
            Bookmark bookmark2 = vod2.getBookmark();

            BookmarkItem item0 = list0.get(0);
            VOD vod0 = item0.getVOD();
            Bookmark bookmark0 = vod0.getBookmark();

            String timeStr1 = checkTimeSlot(bookmark1.getUpdateTime());
            String timeStr2 = checkTimeSlot(bookmark2.getUpdateTime());
            String timeStr0 = checkTimeSlot(bookmark0.getUpdateTime());

            //与上一条时间段相同，不显示标题信息
            if (timeStr1.equals(timeStr2)){
                isShowTitle = false;
            }

            //与上一条时间段相同，上一条与第一条时间段不相同，为第二个时间段
            if (timeStr1.equals(timeStr2)){
                if (!timeStr1.equals(timeStr0)){
                    bookMarkcount = 1;
                }else{
                    //与上一条时间段相同，上一条与第一条时间段相同，为第一个时间段
                    bookMarkcount = 0;
                }

            }else{
                if (!timeStr1.equals(timeStr0)){
                    //与上一条时间段不相同，上一条与第一条时间段不相同，为第三个时间段
                    bookMarkcount = 2;
                }else{
                    //与上一条不相同，上一条与第一条相同，为第二个时间段
                    bookMarkcount = 1;
                }
            }
        }else{
            bookMarkcount =0;
        }

        if (!isShowTitle){
            holder.timeSlot.setVisibility(View.GONE);
            holder.titleLayout.setVisibility(View.GONE);
        }else{
            holder.timeSlot.setVisibility(View.VISIBLE);
            holder.titleLayout.setVisibility(View.VISIBLE);
        }

        if (position < dataList.size() && null != countArr){
            List<BookmarkItem> list = dataList.get(position);
            //判断最后一个元素的时间段
            if (list.size()>0 ){
                if (countArr.size() > bookMarkcount){
                    BookmarkItem item = list.get(list.size() - 1);
                    VOD vod = item.getVOD();
                    Bookmark bookmark = vod.getBookmark();

                    //如果是第一条，检查是不是今天的播放记录
                    if (position == 0){
                        isTodayListExist = checkIstoday(bookmark.getUpdateTime());
                    }

                    holder.timeSlot.setText(checkTimeSlot(bookmark.getUpdateTime()));

                    //时间戳转化为时间字符串
                    Date date = new Date(Long.parseLong(bookmark.getUpdateTime()));
                    SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
                    String timeStr = format.format(date);
//        String str = String.format(timeStr + "  "+"共观看 "+list.size()+" 部");

                    String str = String.format("共观看 "+countArr.get(bookMarkcount)+" 部");

                    int count = String.format(""+countArr.get(bookMarkcount)).length();

                    SpannableString spannableString = new SpannableString(str);
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.mytv_history_count));
                    spannableString.setSpan(colorSpan, spannableString.length() - count - 2, spannableString.length() - 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                    holder.timeAndCount.setText(spannableString);
                }
                holder.bookmarkList.setAdapter(new NewHistoryFragmentListAdapter(mContext,list,isEditing,position));
            }
        }




    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0:dataList.size();
    }

    private String checkTimeSlot(String watchTime){
        Calendar cal = Calendar.getInstance();
        long todayHoursSeconds    = cal.get(Calendar.HOUR_OF_DAY) * 60L * 60L;
        long todayMinutesSeconds  = cal.get(Calendar.MINUTE) * 60L;
        long todaySeconds         = cal.get(Calendar.SECOND);
        long todayMillis          = (todayHoursSeconds + todayMinutesSeconds + todaySeconds) * 1000;

        long curTimeMillis        = System.currentTimeMillis();
        long todayStartMillis     = curTimeMillis - todayMillis;

        long oneDayMillis         = 24L * 60 * 60 * 1000;
        long yesterdayStartMillis = todayStartMillis - oneDayMillis;
        long oneWeekStartMils     = todayStartMillis - 6*oneDayMillis;

        long watchMillis = Long.parseLong(watchTime);
        if (isTodayListExist){
            if (watchMillis >= todayStartMillis){
                //今天
                return mContext.getResources().getString(R.string.mytv_history_list_today);
            }else if (watchMillis >= oneWeekStartMils){
                //一周内
                return mContext.getResources().getString(R.string.mytv_history_list_thisweek);
            }else{
                //一月内
                return mContext.getResources().getString(R.string.mytv_history_list_thismouth);
            }
        }else{
            if (watchMillis >= yesterdayStartMillis){
                //昨天
                return mContext.getResources().getString(R.string.mytv_history_list_yesterday);
            }else if (watchMillis >= oneWeekStartMils){
                //一周内
                return mContext.getResources().getString(R.string.mytv_history_list_thisweek);
            }else{
                //一月内
                return mContext.getResources().getString(R.string.mytv_history_list_thismouth);
            }
        }
    }

    //检查是否是今天的播放记录
    private boolean checkIstoday(String updateTime){
        Calendar cal = Calendar.getInstance();
        long todayHoursSeconds    = cal.get(Calendar.HOUR_OF_DAY) * 60L * 60L;
        long todayMinutesSeconds  = cal.get(Calendar.MINUTE) * 60L;
        long todaySeconds         = cal.get(Calendar.SECOND);
        long todayMillis = (todayHoursSeconds + todayMinutesSeconds + todaySeconds) * 1000L;
        long curTimeMillis = System.currentTimeMillis();
        long todayStartMillis = curTimeMillis - todayMillis;

        long bookmarkMils = Long.parseLong(updateTime);
        if (bookmarkMils >= todayStartMillis){
            return true;
        }else{
            return false;
        }
    }

    //切换编辑状态
    public void switchEditingState(boolean isEditing){
        this.isEditing = isEditing;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //时间段 今天，昨天，一周内，一月内
        TextView timeSlot;
        //日期和个数
        TextView timeAndCount;
        //展示列表
        RecyclerView bookmarkList;
        RelativeLayout titleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            timeSlot     = itemView.findViewById(R.id.history_list_item_timeSlot);
            timeAndCount = itemView.findViewById(R.id.history_list_item_count);
            bookmarkList = itemView.findViewById(R.id.mytv_history_list_item_list);
            titleLayout = itemView.findViewById(R.id.history_list_item_top_layojut);

            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            bookmarkList.setFocusableInTouchMode(false);
            bookmarkList.setLayoutManager(manager);
            bookmarkList.addItemDecoration(new SpaceItemDecoration(mContext,mContext.getResources().getDimensionPixelSize(R.dimen.history_list_space)));
        }

        public RecyclerView getBookmarkList() {
            return bookmarkList;
        }
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration{
        private static final String TAG = "CustomItemDecoration";
        private int mSpace ;
        private Context mContext;

        /**
         * @param space 传入的值，其单位视为dp
         */
        public SpaceItemDecoration(Context context, float space) {
            SuperLog.debug(TAG, "space is " + space);
            this.mContext =context;
            this.mSpace = (int) space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int itemCount = parent.getAdapter().getItemCount();
            int pos = parent.getChildAdapterPosition(view);
            SuperLog.debug(TAG, "itemCount>>" +itemCount + ";Position>>" + pos);
            outRect.left     = 0;
            outRect.top      =  mContext.getResources().getDimensionPixelSize(R.dimen.history_list_space_top);
            outRect.bottom   = 0;
            outRect.right    =  mSpace - mContext.getResources().getDimensionPixelSize(R.dimen.history_list_space_margin);

            if (pos == 0){
                outRect.left =  mContext.getResources().getDimensionPixelSize(R.dimen.history_list_space_left);
            }else {
                outRect.left =  mSpace + mContext.getResources().getDimensionPixelSize(R.dimen.history_list_space_margin);
            }
        }
    }
}