package com.pukka.ydepg.moudule.mytv.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODDetailCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Controller.VODListController;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.utils.IsTV;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.CpRoute;
import com.pukka.ydepg.customui.MiguQRViewPopWindow;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.player.util.OffScreenUtils;
import com.pukka.ydepg.moudule.vod.activity.ChildModeVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.pukka.ydepg.common.utils.UtilBase.getApplicationContext;

/**
 * Created by hasee on 2017/8/24.
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {

    private final Context mContext;
    private final VerticalGridView mRecyclerView;
    private volatile List<Content> mContentList;
    private OnDelectedListener mOnItemDectedListener;

    //多选模式开关
    private boolean mDeleteMoreTAG = false;
    //删除键选中
    private boolean mDeleteSelectTAG = false;

    //多选选中模式
    private boolean mDeleteChoiceTAG = false;

    private List<Content> mDeleteList = new ArrayList<>();
    private VOD mVod;
    private Content mSelectAllContent;
    private boolean mPause;

    //点击删除按钮，焦点默认落在选择框上
    private boolean isClickDelte = false;

    private Map<String, String> cpApkMap;

    public CollectionAdapter(VerticalGridView recyclerView, List<Content> contentList, Context context) {
        mContentList = contentList;
        mContext = context;
        mRecyclerView = recyclerView;
        //全选item
        mSelectAllContent = new Content();
        mVod = new VOD();
        //全选item 名称
        mVod.setName(mContext.getResources().getString(R.string.mytv_all_choose));
        mSelectAllContent.setVOD(mVod);
        cpApkMap = SessionService.getInstance().getSession().getTerminalConfigurationCPAPKINFO();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.collection_list_item, null, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        holder.delete.setTag(false);
        //holder.mDevice.setTag(position);
        Content content = mContentList.get(position);
        VOD vod = content.getVOD();
        //隐藏全选item的前置图标
        if (null != vod && (vod.getName().equals(mContext.getResources().getString(R.string.dis_all_choose)) || vod.getName().equals(mContext.getResources().getString(R.string.mytv_all_choose)))) {
            holder.mDevice.setVisibility(View.GONE);
        } else {
            if (null != vod && IsTV.isTVFar(vod)) {
                holder.mDevice.setVisibility(View.VISIBLE);
                holder.mDevice.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_tv));
            } else {
                holder.mDevice.setVisibility(View.VISIBLE);
                holder.mDevice.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_phone));
            }
        }

        if (mDeleteMoreTAG) {//多选模式 选中按钮展示
            holder.check.setVisibility(View.VISIBLE);
            holder.play.setVisibility(View.GONE);
            if (mDeleteList.contains(content)) {//删除列表包含此item 勾选
                holder.mCheckBtn.setImageResource(R.drawable.select_point);
                holder.mCheckBtn.setBackground(ContextCompat.getDrawable(mContext, R.drawable.select_focus));
            } else {
                holder.mCheckBtn.setBackground(ContextCompat.getDrawable(mContext, R.drawable.select_more));
                holder.mCheckBtn.setImageResource(R.drawable.mytv_default_bg);
            }
        } else {
            holder.check.setVisibility(View.GONE);
        }
        holder.itemLayout.setOnFocusChangeListener(mOnFocusChangeListener);
//        VOD vod = content.getVOD();

        holder.itemView.setOnClickListener(mOnClickListener);
        holder.delete.setOnClickListener(mOnClickListener);
        holder.play.setOnClickListener(mOnClickListener);

        if (isClickDelte && position == mRecyclerView.getSelectedPosition()) {
            isClickDelte = false;
            //1.10版本修改点击删除按钮，默认落焦到选择框上
            mDeleteChoiceTAG = true;
            mDeleteSelectTAG = false;
            holder.check.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg_foucs_select));
            holder.delete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_delete_item_bg));
        }/*else{
            holder.check.setBackground(ContextCompat.getDrawable(mContext, R.drawable.transparent_drawable));
            holder.delete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.transparent_drawable));
        }*/

        if (null != vod) {
            String productName = vod.getName();
            holder.name.setText(productName.trim());
            String vodType = vod.getVODType();
            if (TextUtils.equals(vodType, VOD.VODType.UN_TV_SERIES)) {//非电视剧 电影
                holder.time.setText(mContext.getResources().getString(R.string.mytv_film_type));
            } else {//电视剧 展示共几集
                String episodeCount = vod.getEpisodeCount();
                if (!TextUtils.isEmpty(episodeCount)) {
                    holder.time.setText(String.format(mContext.getResources().getString(R.string.mytv_tv_num), episodeCount));
                } else {
                    holder.time.setText(String.format(mContext.getResources().getString(R.string.mytv_tv_num), "0"));
                }

            }

        }
        holder.itemLayout.setOnKeyListener(new View.OnKeyListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_LEFT://向左
                            if (!mDeleteMoreTAG) {//非多选模式 并且没有选中删除 左点退出
                                if (!mDeleteSelectTAG && null != mOnItemDectedListener) {
                                    mOnItemDectedListener.canFinish();
                                }
                                mDeleteSelectTAG = false;
                                mDeleteChoiceTAG = false;
                                holder.delete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_delete_item_bg));
                                holder.play.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg_foucs_select));
                                holder.mNameLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg));
                            } else {//多选模式下 左点选中签选中
                                mDeleteChoiceTAG = true;
                                mDeleteSelectTAG = false;
                                holder.check.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg_foucs_select));
                                holder.delete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_delete_item_bg));
                            }
                            break;
                        case KeyEvent.KEYCODE_DPAD_RIGHT://向右
                            if (!mDeleteMoreTAG) {//非多选模式 删除按钮选中
                                mDeleteChoiceTAG = false;
                                mDeleteSelectTAG = true;
                                holder.mNameLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg));
                                holder.play.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg));
                                holder.delete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_delete_item_bg_foucs_select));
                            } else {//多选模式 删除按钮也选中
                                mDeleteChoiceTAG = false;
                                mDeleteSelectTAG = true;
                                holder.check.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg));
                                holder.delete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_delete_item_bg_foucs_select));
                            }

                            break;
                        case KeyEvent.KEYCODE_BACK://返回 多选模式下 退出 多选模式
                            if (mDeleteMoreTAG) {
                                mDeleteMoreTAG = false;
                                mDeleteChoiceTAG = false;
                                mDeleteList.clear();
                                if (mContentList.contains(mSelectAllContent)) {
                                    mContentList.remove(mSelectAllContent);
                                    if (position >= 1)//返回时删除全选item 选中焦点减1
                                        mRecyclerView.setSelectedPosition(position - 1);
                                }
                                notifyDataSetChanged();
                                return true;
                            } else {//非多选模式下直接退出
                                if (null != mOnItemDectedListener) {
                                    mOnItemDectedListener.canFinish();
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == mContentList ? 0 : mContentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        LinearLayout delete;
        TextView time;
        RelativeLayout itemLayout;
        ImageView mCheckBtn;
        LinearLayout check;
        LinearLayout play;
        RelativeLayout mNameLayout;
        ImageView mDevice;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.collection_list_item_name);
            mDevice = (ImageView) itemView.findViewById(R.id.ic_device_type);
            delete = (LinearLayout) itemView.findViewById(R.id.collection_list_item_delete);
            time = (TextView) itemView.findViewById(R.id.collection_list_item_time);
            itemLayout = (RelativeLayout) itemView.findViewById(R.id.collection_list_item_layout);
            check = (LinearLayout) itemView.findViewById(R.id.collection_list_item_check);
            play = (LinearLayout) itemView.findViewById(R.id.collection_list_item_play);
            mCheckBtn = (ImageView) itemView.findViewById(R.id.mytv_main_check_img);
            mNameLayout = (RelativeLayout) itemView.findViewById(R.id.collection_list_name_layout);
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @SuppressLint("RestrictedApi")
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            switch (v.getId()) {
                case R.id.collection_list_item_layout:
                    if (!mDeleteMoreTAG && !mDeleteSelectTAG) {
                        //跳转cp
                        if (!TextUtils.isEmpty(mContentList.get(position).getVOD().getCpId()) && CpRoute.isCp(mContentList.get(position).getVOD().getCpId())) {
                            //调用接口取voddetail
                            OffScreenUtils.getSPVodDetail(mContentList.get(position).getVOD().getID(), new VODListController((RxAppCompatActivity) mContext), (RxAppCompatActivity) mContext, new VODDetailCallBack() {
                                @Override
                                public void getVODDetailSuccess(VODDetail vodDetail, String recmActionID, List<RecmContents> recmContents) {
                                    vodDetail.getCpId();

                                    if (!TextUtils.isEmpty(vodDetail.getCpId()) && CpRoute.isCp(vodDetail.getCpId(), vodDetail.getCustomFields())) {
                                        CpRoute.goCp(vodDetail.getCustomFields());
                                    } else {
                                        //普通点击事件 跳转详情页
                                        jumpToDetailActivity(position, v);
                                    }
                                }

                                @Override
                                public void getVODDetailFailed() {
                                    SuperLog.info2SD(CollectionAdapter.class.getSimpleName(), "get cpId failed");
                                    //普通点击事件 跳转详情页
                                    jumpToDetailActivity(position, v);
                                }

                                @Override
                                public void onError() {
                                    //普通点击事件 跳转详情页
                                    jumpToDetailActivity(position, v);
                                }
                            });
//                            CpRoute.goCp(mContentList.get(position).getVOD().getCustomFields());
                        } else {
                            //普通点击事件 跳转详情页
                            jumpToDetailActivity(position, v);
                        }
                    }
                    Content content = mContentList.get(position);
                    if (!mDeleteMoreTAG && mDeleteSelectTAG) {
                        //非多选模式 删除选中 点击开启多选模式  添加全选item
                        mDeleteMoreTAG = true;
                        mVod.setName(mContext.getResources().getString(R.string.mytv_all_choose));
                        mDeleteList.add(content);
                        mContentList.add(0, mSelectAllContent);
                        mRecyclerView.setSelectedPosition(position + 1);

                        isClickDelte = true;

                        //如果只收藏了一条，点击删除，选择这一条，全选也要选择
                        if (!mDeleteList.contains(mSelectAllContent) && mContentList.size() - mDeleteList.size() == 1) {
                            mVod.setName(mContext.getResources().getString(R.string.dis_all_choose));
                            mDeleteList.add(mSelectAllContent);
                        }

                        notifyDataSetChanged();
                        return;
                    }
                    if (mDeleteMoreTAG && mDeleteSelectTAG && !mDeleteChoiceTAG) {
                        //删除操作 多选模式 删除选中 如果删除列表大于0 调用删除方法 删除时把全选item移除
                        if (null != mOnItemDectedListener && mDeleteList.size() > 0) {
                            if (mDeleteList.contains(mSelectAllContent)) {
                                mDeleteList.remove(mSelectAllContent);
                                mOnItemDectedListener.onDeleteAll();
                                return;
                            }
                            mOnItemDectedListener.onItemsDelete(mDeleteList);
                        }
                    }
                    //多选模式下 check按钮选中
                    if (mDeleteMoreTAG && mDeleteChoiceTAG && !mDeleteSelectTAG) {
                        //添加删除刷新
                        if (position == 0) {//position=0全选模式 如果删除列表和数据源数据一致 则代表此次点击为取消全选模式 删除数据 设置全选item名称为全选
                            if (mDeleteList.containsAll(mContentList)) {
                                mDeleteList.clear();
                                mVod.setName(mContext.getResources().getString(R.string.mytv_all_choose));
                            } else {//否则 则是添加全选模式 删除列表设置全选 全选item显示取消全选
                                mDeleteList.clear();
                                mDeleteList.addAll(mContentList);
                                mVod.setName(mContext.getResources().getString(R.string.dis_all_choose));
                            }
                            notifyDataSetChanged();
                        } else {
                            if (mDeleteList.contains(content)) {//单点模式 如果删除列表包含所选item 则删除
                                mDeleteList.remove(content);
                                if (mDeleteList.contains(mSelectAllContent)) {//若包含全选则取消全选 设置名称为全选
                                    mVod.setName(mContext.getResources().getString(R.string.mytv_all_choose));
                                    mDeleteList.remove(mSelectAllContent);
                                    notifyDataSetChanged();
                                } else {
                                    //刷新单个item状态
                                    notifyItemChanged(position);
                                }
                            } else {//不包含则添加到删除列表
                                mDeleteList.add(content);
                                //除了全选外其余都选了 所有全选也勾上
                                if (!mDeleteList.contains(mSelectAllContent) && mContentList.size() - mDeleteList.size() == 1) {
                                    mVod.setName(mContext.getResources().getString(R.string.dis_all_choose));
                                    mDeleteList.add(mSelectAllContent);
                                    notifyDataSetChanged();
                                } else {
                                    //刷新单个item状态
                                    notifyItemChanged(position);
                                }
                            }
                        }
                    }
                    break;
            }
        }
    };

    /**
     * 跳转详情页
     *
     * @param position position
     */
    private void jumpToDetailActivity(int position, View v) {
        Intent intent;
        if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
            //儿童版模式下跳儿童版详情页
            intent = new Intent(mContext, ChildModeVodDetailActivity.class);
        } else {
            //普通版详情页
            intent = new Intent(mContext, NewVodDetailActivity.class);
        }
        Content content = mContentList.get(position);
        if (null != content && null != content.getVOD()) {
            if (VodUtil.isMiguVod(content.getVOD())) {
//                EpgToast.showLongToast(mContext,mContext.getString(R.string.vod_watch_tips));
                MiguQRViewPopWindow miguQRViewPopWindow = new MiguQRViewPopWindow(mContext, content.getVOD().getCode(), MiguQRViewPopWindow.mFavriteResultType);
                miguQRViewPopWindow.showPopupWindow(v);
                return;
            }
            if (null != cpApkMap && cpApkMap.containsKey(content.getVOD().getCpId())) {
                PackageManager manager = mContext.getPackageManager();
                Intent mIntent = manager.getLaunchIntentForPackage(cpApkMap.get(content.getVOD().getCpId()));
                mIntent.putExtra(Constant.CONTENTCODE, content.getVOD().getCode());
                mIntent.putExtra(Constant.ACTION, "VOD");
                mContext.startActivity(mIntent);
                return;
            }
            intent.putExtra(NewVodDetailActivity.VOD_ID, content.getVOD().getID());
            intent.putExtra(NewVodDetailActivity.ORGIN_VOD, content.getVOD());
        }
        mContext.startActivity(intent);
    }

    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {//如果有焦点 展示时间
                final int position = (int) v.getTag();
                if (null == mContentList || position > mContentList.size() - 1) {
                    return;
                }
                SuperLog.debug("ReserveListAdapter", "position = " + position);
                v.findViewById(R.id.collection_list_item_time).setVisibility(View.VISIBLE);
                v.findViewById(R.id.collection_list_item_name).setSelected(true);
                //v.findViewById(R.id.ic_device_type).setSelected(true);
                //设置名称布局高度
                View view = v.findViewById(R.id.collection_list_name_layout);
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) view.getLayoutParams();
                params1.height = (int) mContext.getResources().getDimensionPixelSize(R.dimen.history_item_min_height);
                view.setLayoutParams(params1);
                //从别的界面进来获取焦点时时间view可能不展示 需要重新刷新item
                if (position == 0) {
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemChanged(position);
                        }
                    });
                }
                //从别的界面返回获取焦点时时间view可能不展示 需要重新刷新item
                if (mPause) {
                    mPause = false;
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemChanged(position);
                        }
                    });
                }
                //多选模式下 position == 0 代表全选item 不展示时间 使名称剧中
                if (position == 0 && mDeleteMoreTAG) {
                    v.findViewById(R.id.collection_list_item_time).setVisibility(View.GONE);
                }
                //Added by liuxia at 20180318 to solve problem
                //java.lang.IndexOutOfBoundsException: Invalid index 0, size is 0
                if (mContentList.size() <= position) {
                    SuperLog.error("CollectionAdapter", "collection data error.");
                    return;
                }
                Content content = mContentList.get(position);
                if (null != mOnItemDectedListener) {
                    int newPosition = position;
                    //多选模式下 position -1
                    if (mDeleteMoreTAG) {
                        newPosition--;
                    }
                    mOnItemDectedListener.onItemSelect(content, newPosition);
                }
                if (!mDeleteMoreTAG) {
                    View play = v.findViewById(R.id.collection_list_item_play);
                    if (mDeleteSelectTAG) {
                        play.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg));
                    } else {
                        play.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg_foucs_select));
                    }
                    play.setVisibility(View.VISIBLE);
                }

                View delete = v.findViewById(R.id.collection_list_item_delete);
                delete.setVisibility(View.VISIBLE);
                if (mDeleteSelectTAG) {//选中时设置背景
                    delete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_delete_item_bg_foucs_select));
                } else {
                    delete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_delete_item_bg));
                }
                //设置check 按钮大小
                View check = v.findViewById(R.id.collection_list_item_check);
                RelativeLayout.LayoutParams checkParam = (RelativeLayout.LayoutParams) check.getLayoutParams();
                checkParam.height = (int) mContext.getResources().getDimensionPixelSize(R.dimen.history_item_min_height);
                check.setLayoutParams(checkParam);
                if (mDeleteMoreTAG && mDeleteChoiceTAG) {//多选情况下 check按钮选中设置背景
                    check.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg_foucs_select));
                } else {
                    check.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg));
                }

                //选中时设置边距 下面为了扩展
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) v.getLayoutParams();
                params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.history_item_topmargin);
                params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.history_item_topmargin);
                v.setLayoutParams(params);
                v.findViewById(R.id.collection_list_name_layout).setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg));
                if (!mDeleteMoreTAG && !mDeleteSelectTAG) {//非多选模式 删除选中时设置名称布局背景
                    v.findViewById(R.id.collection_list_item_play).setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg_foucs_select));
                }
            } else {
                //没有焦点时设置item 为wrap—content
                View view = v.findViewById(R.id.collection_list_name_layout);
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) view.getLayoutParams();
                params1.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                view.setLayoutParams(params1);
                v.findViewById(R.id.collection_list_item_name).setSelected(false);
                v.findViewById(R.id.ic_device_type).setSelected(false);
                //隐藏时间布局
                v.findViewById(R.id.collection_list_item_time).setVisibility(View.GONE);
                //间距为0
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) v.getLayoutParams();
                params.topMargin = (int) mContext.getResources().getDimension(R.dimen.px_0);
                params.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.px_0);
                v.setLayoutParams(params);

                View check = v.findViewById(R.id.collection_list_item_check);
                RelativeLayout.LayoutParams checkParam = (RelativeLayout.LayoutParams) check.getLayoutParams();
                checkParam.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                check.setLayoutParams(checkParam);
                check.setBackground(ContextCompat.getDrawable(mContext, R.drawable.transparent_drawable));
                View delete = v.findViewById(R.id.collection_list_item_delete);
                delete.setVisibility(View.GONE);
                View play = v.findViewById(R.id.collection_list_item_play);
                play.setVisibility(View.GONE);
                v.findViewById(R.id.collection_list_name_layout).setBackground(ContextCompat.getDrawable(mContext, R.drawable.transparent_drawable));
            }
        }
    };

    /**
     * 插入数据
     */
    public void addData(List<Content> contentList) {
        mContentList.addAll(contentList);
        notifyItemRangeInserted(mContentList.size() - contentList.size(), contentList.size());
    }

    /**
     * 是否是从别的页面返回
     */
    public void setIsBackView(boolean pause) {
        mPause = pause;
    }

    public void addNewData(List<Content> contentList) {
        mContentList.clear();
        mContentList.addAll(contentList);
        mDeleteMoreTAG = false;
        mDeleteChoiceTAG = false;
        mDeleteSelectTAG = false;
        notifyDataSetChanged();
    }

    public interface OnDelectedListener {
        void onDeleteAll();

        void onItemsDelete(List<Content> contents);

        void onItemSelect(Content content, int position);

        void canFinish();
    }

    public void setOnDelectedListener(OnDelectedListener onItemDectedListener) {
        mOnItemDectedListener = onItemDectedListener;
    }

    /**
     * 删除
     */
    public void deleteItem() {
        //数据移除删除列表
        mContentList.removeAll(mDeleteList);
        //数据为1时，并且数据包含全选item 移除全选item 设置item为全选
        if (mContentList.size() == 1 && mContentList.contains(mSelectAllContent)) {
            mContentList.remove(mSelectAllContent);
            mVod.setName(mContext.getResources().getString(R.string.mytv_all_choose));
            mDeleteMoreTAG = false;
            mDeleteChoiceTAG = false;
            mDeleteSelectTAG = false;
        }
        mDeleteList.clear();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public boolean canLoadMore() {
        return !mDeleteMoreTAG;
    }
}
