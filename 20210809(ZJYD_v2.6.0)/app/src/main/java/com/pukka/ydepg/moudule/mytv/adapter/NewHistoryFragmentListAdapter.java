package com.pukka.ydepg.moudule.mytv.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.BookmarkItem;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.ClickUtil;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.CpRoute;
import com.pukka.ydepg.customui.MiguQRViewPopWindow;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.event.DeleteHistoryEvent;
import com.pukka.ydepg.event.HistoryListRequestFocusEvent;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.vod.activity.ChildModeVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.pukka.ydepg.moudule.mytv.utils.VODMining.getPicture;

public class NewHistoryFragmentListAdapter extends RecyclerView.Adapter<NewHistoryFragmentListAdapter.ViewHolder> {
    private static final String TAG = "NewHistoryFragmentListA";

    private final Context mContext;

    private volatile List<BookmarkItem> dataList;

    private int tag;

    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_SMALL);

    //当前是是否为编辑状态
    private boolean isEditing;

    //存放需要删除的item
    private List<BookmarkItem> deleteList = new ArrayList<>();

    private Map<String, String> cpApkMap;

    private static final long MIN_CLICK_INTERVAL = 350L;

    private boolean showFocus = false;

    //防止上下快速切换
    private boolean canMove = true;

    public NewHistoryFragmentListAdapter(Context mContext, List<BookmarkItem> dataList, boolean isEditing, int tag) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.isEditing = isEditing;
        this.tag = tag;
        ClickUtil.setInterval(TAG, MIN_CLICK_INTERVAL);

        cpApkMap = SessionService.getInstance().getSession().getTerminalConfigurationCPAPKINFO();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showFocus = true;
            }
        }, 200);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mytv_history_list_list_item, null, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mOnClickListener);

        BookmarkItem item = dataList.get(position);

        //添加封面
        //书签为vod
        if (TextUtils.equals(item.getBookmarkType(), BookmarkItem.BookmarkType.VOD)) {
            VOD vod = item.getVOD();

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
                        RequestOptions options = new RequestOptions()
                                .placeholder(R.drawable.default_poster);
                        Glide.with(mContext.getApplicationContext()).load(url).apply(options).into(holder.mCover);
                    } else {
                        holder.mCover.setImageResource(R.drawable.default_poster);
                    }
                } else {
                    holder.mCover.setImageResource(R.drawable.default_poster);
                }

                String superScriptUrl = SuperScriptUtil.getInstance().getSuperScriptForCollectionHistory(vod);
                if (!TextUtils.isEmpty(superScriptUrl)){
                    Log.i(TAG, "onBindViewHolder: "+superScriptUrl);
                    if (null != mContext && mContext instanceof Activity && (!((Activity) mContext).isFinishing())){
                        GlideApp.with(mContext).load(superScriptUrl).into(holder.superScript);
                    }
                    holder.superScript.setVisibility(View.VISIBLE);
                }else{
                    holder.superScript.setVisibility(View.GONE);
                }

            } else {
                holder.mCover.setImageResource(R.drawable.default_poster);
            }
        } else {//非VOD
            holder.mCover.setImageResource(R.drawable.default_poster);
        }

        //添加VOD名称
        VOD vod = item.getVOD();
        Bookmark bookmark = vod.getBookmark();
        if (null != bookmark && !TextUtils.isEmpty(vod.getName())) {
            holder.mName.setText(vod.getName().trim());
        }

        //添加观看进度
        String rangeStr = "";
        String rangeTime = "";
        if (null != bookmark && !TextUtils.isEmpty(bookmark.getSitcomNO()) && !TextUtils.isEmpty(vod.getName())) {
            rangeStr = String.format("观看至" + bookmark.getSitcomNO() + "集");
        }
        if (null != bookmark) {
            rangeTime = bookmark.getRangeTime();
        }

        long watchTime = 0;
        String timeStr = "";
        if (!TextUtils.isEmpty(rangeTime) && null != bookmark) {
            watchTime = Long.parseLong(rangeTime);
            long min = watchTime / 60;
            if (min > 0) {
                if (!TextUtils.isEmpty(bookmark.getSitcomNO())) {
                    timeStr = String.format(rangeStr + " " + min + mContext.getResources().getString(R.string.mytv_history_list_min));
                } else {
                    timeStr = String.format("观看至" + min + mContext.getResources().getString(R.string.mytv_history_list_min));
                }
            } else {
                timeStr = String.format(mContext.getResources().getString(R.string.mytv_history_list_less_1min));
            }
            holder.mBookmark.setText(timeStr);
        } else {
            holder.mBookmark.setText(mContext.getResources().getString(R.string.mytv_history_list_less_1min));
        }

        if (isEditing) {
            holder.mMixing.setVisibility(View.VISIBLE);
            holder.mDeleIcon.setVisibility(View.VISIBLE);
        } else {
            holder.mMixing.setVisibility(View.INVISIBLE);
            holder.mDeleIcon.setVisibility(View.INVISIBLE);
        }

    }

    public static String getMiguPoster(VOD vod) {
        Picture picture = getPicture(vod);
        if (null == picture) {
            return null;
        }
        List<String> iconList = picture.getIcons();
        if (!CollectionUtil.isEmpty(iconList)) {
            return iconList.get(0);
        } else {
            List<String> draftList = picture.getDrafts();
            if (!CollectionUtil.isEmpty(draftList)) {
                return draftList.get(0);
            } else {
                List<String> titleList = picture.getTitles();
                if (!CollectionUtil.isEmpty(titleList)) {
                    return titleList.get(0);
                }
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isEditing) {
                //编辑模式下删除这条记录
                deleteList.clear();
                int position = (int) v.getTag();
                BookmarkItem item = dataList.get(position);
                deleteList.add(item);
                EventBus.getDefault().post(new DeleteHistoryEvent(deleteList, false, tag, position));
            } else {

                int position = (int) v.getTag();
                if (!TextUtils.isEmpty(dataList.get(position).getVOD().getCpId()) && CpRoute.isCp(dataList.get(position).getVOD().getCpId(),dataList.get(position).getVOD().getCustomFields())) {
                    CpRoute.goCp(dataList.get(position).getVOD().getCustomFields());
                }else {
                    //非编辑模式下跳转详情
                    jumpToDetailActivity(position, v);
                }
            }
        }
    };

    /**
     * 跳转详情页
     *
     * @param position
     */
    private void jumpToDetailActivity(int position, View v) {
        Intent intent;
        if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
            intent = new Intent(mContext, ChildModeVodDetailActivity.class);
        } else {
            intent = new Intent(mContext, NewVodDetailActivity.class);
        }
        BookmarkItem bookmarkItem = dataList.get(position);
        if (VodUtil.isMiguVod(bookmarkItem.getVOD())) {
            MiguQRViewPopWindow miguQRViewPopWindow = new MiguQRViewPopWindow(mContext, bookmarkItem.getVOD().getCode(), MiguQRViewPopWindow.mHistoryResultType);
            miguQRViewPopWindow.showPopupWindow(v);
            return;
        }
        if (null != cpApkMap && cpApkMap.containsKey(bookmarkItem.getVOD().getCpId())) {
            PackageManager manager = mContext.getPackageManager();
            Intent mIntent = manager.getLaunchIntentForPackage(cpApkMap.get(bookmarkItem.getVOD().getCpId()));
            mIntent.putExtra(Constant.CONTENTCODE, bookmarkItem.getVOD().getCode());
            mIntent.putExtra(Constant.ACTION, "VOD");
            mContext.startActivity(mIntent);
            return;
        }
        if (null != bookmarkItem.getVOD()) {
            intent.putExtra(NewVodDetailActivity.ORGIN_VOD, bookmarkItem.getVOD());
            intent.putExtra(NewVodDetailActivity.VOD_ID, bookmarkItem.getVOD().getID());
            mContext.startActivity(intent);
        }
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    public List<BookmarkItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<BookmarkItem> dataList) {
        this.dataList = dataList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mCover;

        TextView mBookmark;

        TextView mName;

        //蒙层
        View mMixing;

        //删除icon
        ImageView mDeleIcon;

        ImageView superScript;

        public ViewHolder(View itemView) {
            super(itemView);

            mCover = (ImageView) itemView.findViewById(R.id.history_list_item_image);
            mBookmark = (TextView) itemView.findViewById(R.id.history_list_item_bookmark);
            mName = (TextView) itemView.findViewById(R.id.search_result_item_name);
            mMixing = itemView.findViewById(R.id.history_list_item_cover);
            mDeleIcon = (ImageView) itemView.findViewById(R.id.history_list_item_deleteIcon);
            superScript = itemView.findViewById(R.id.vipimg);

            if (mFocusHighlight != null) {
                mFocusHighlight.onInitializeView(itemView);
            }
            itemView.setOnFocusChangeListener((v, hasFocus) -> {

                int postion = (int) v.getTag();
                if (!showFocus && postion == 0) {
                    itemView.setFocusable(false);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            itemView.setFocusable(true);
                        }
                    }, 50);
                }

                if (!showFocus) {
                    return;
                }

                canMove = false;
                Handler CanMovehandler = new Handler();
                CanMovehandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        canMove = true;
                    }
                }, 350);

                if (mFocusHighlight != null) {
                    mFocusHighlight.onItemFocused(v, hasFocus);
                }

                //编辑模式下还要更改蒙版
                if (isEditing) {
                    if (hasFocus) {
                        v.setSelected(true);
                        mMixing.setBackgroundResource(R.color.transparent_10);
                    } else {
                        v.setSelected(false);
                        mMixing.setBackgroundResource(R.color.transparent_30);
                    }
                } else {
                    if (hasFocus) {
                        v.setSelected(true);
                    } else {
                        v.setSelected(false);
                    }
                }

                int position = (int) itemView.getTag();
                if (hasFocus && position <= 4) {
                    //滚动防止信息显示不全
//                    EventBus.getDefault().post(new HistoryListScrollToTopEvent(tag));
                }

            });
            itemView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (!canMove) {
                        return true;
                    }

                    if (!showFocus) {
                        return true;
                    }

                    if (event.getAction() == KeyEvent.ACTION_DOWN) {

                        if (ClickUtil.isFastDoubleClick(TAG)) {
                            return true;
                        }

                        switch (keyCode) {
                            case KeyEvent.KEYCODE_DPAD_LEFT: {
                                int postion = (int) v.getTag();

                                if (postion == 0) {
                                    EventBus.getDefault().post(new HistoryListRequestFocusEvent(HistoryListRequestFocusEvent.REQUEST_FOCUS_ACTIVITY));
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                            case KeyEvent.KEYCODE_DPAD_RIGHT: {
                                int postion = (int) v.getTag();
                                if (postion == dataList.size() - 1) {
                                    HistoryListRequestFocusEvent event1 = new HistoryListRequestFocusEvent(HistoryListRequestFocusEvent.REQUEST_FOCUS_LIST_FRAGMENT);
                                    event1.setNum(tag);
                                    EventBus.getDefault().post(event1);
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                            case KeyEvent.KEYCODE_DPAD_UP: {
                                if (tag == 0) {
                                    EventBus.getDefault().post(new HistoryListRequestFocusEvent(HistoryListRequestFocusEvent.REQUEST_FOCUS_FRAGMENT));
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        }
                    }
                    return false;
                }
            });
        }
    }
}
