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

    //??????????????????
    private boolean mDeleteMoreTAG = false;
    //???????????????
    private boolean mDeleteSelectTAG = false;

    //??????????????????
    private boolean mDeleteChoiceTAG = false;

    private List<Content> mDeleteList = new ArrayList<>();
    private VOD mVod;
    private Content mSelectAllContent;
    private boolean mPause;

    //???????????????????????????????????????????????????
    private boolean isClickDelte = false;

    private Map<String, String> cpApkMap;

    public CollectionAdapter(VerticalGridView recyclerView, List<Content> contentList, Context context) {
        mContentList = contentList;
        mContext = context;
        mRecyclerView = recyclerView;
        //??????item
        mSelectAllContent = new Content();
        mVod = new VOD();
        //??????item ??????
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
        //????????????item???????????????
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

        if (mDeleteMoreTAG) {//???????????? ??????????????????
            holder.check.setVisibility(View.VISIBLE);
            holder.play.setVisibility(View.GONE);
            if (mDeleteList.contains(content)) {//?????????????????????item ??????
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
            //1.10????????????????????????????????????????????????????????????
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
            if (TextUtils.equals(vodType, VOD.VODType.UN_TV_SERIES)) {//???????????? ??????
                holder.time.setText(mContext.getResources().getString(R.string.mytv_film_type));
            } else {//????????? ???????????????
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
                        case KeyEvent.KEYCODE_DPAD_LEFT://??????
                            if (!mDeleteMoreTAG) {//??????????????? ???????????????????????? ????????????
                                if (!mDeleteSelectTAG && null != mOnItemDectedListener) {
                                    mOnItemDectedListener.canFinish();
                                }
                                mDeleteSelectTAG = false;
                                mDeleteChoiceTAG = false;
                                holder.delete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_delete_item_bg));
                                holder.play.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg_foucs_select));
                                holder.mNameLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg));
                            } else {//??????????????? ?????????????????????
                                mDeleteChoiceTAG = true;
                                mDeleteSelectTAG = false;
                                holder.check.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg_foucs_select));
                                holder.delete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_delete_item_bg));
                            }
                            break;
                        case KeyEvent.KEYCODE_DPAD_RIGHT://??????
                            if (!mDeleteMoreTAG) {//??????????????? ??????????????????
                                mDeleteChoiceTAG = false;
                                mDeleteSelectTAG = true;
                                holder.mNameLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg));
                                holder.play.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg));
                                holder.delete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_delete_item_bg_foucs_select));
                            } else {//???????????? ?????????????????????
                                mDeleteChoiceTAG = false;
                                mDeleteSelectTAG = true;
                                holder.check.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg));
                                holder.delete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_delete_item_bg_foucs_select));
                            }

                            break;
                        case KeyEvent.KEYCODE_BACK://?????? ??????????????? ?????? ????????????
                            if (mDeleteMoreTAG) {
                                mDeleteMoreTAG = false;
                                mDeleteChoiceTAG = false;
                                mDeleteList.clear();
                                if (mContentList.contains(mSelectAllContent)) {
                                    mContentList.remove(mSelectAllContent);
                                    if (position >= 1)//?????????????????????item ???????????????1
                                        mRecyclerView.setSelectedPosition(position - 1);
                                }
                                notifyDataSetChanged();
                                return true;
                            } else {//??????????????????????????????
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
                        //??????cp
                        if (!TextUtils.isEmpty(mContentList.get(position).getVOD().getCpId()) && CpRoute.isCp(mContentList.get(position).getVOD().getCpId())) {
                            //???????????????voddetail
                            OffScreenUtils.getSPVodDetail(mContentList.get(position).getVOD().getID(), new VODListController((RxAppCompatActivity) mContext), (RxAppCompatActivity) mContext, new VODDetailCallBack() {
                                @Override
                                public void getVODDetailSuccess(VODDetail vodDetail, String recmActionID, List<RecmContents> recmContents) {
                                    vodDetail.getCpId();

                                    if (!TextUtils.isEmpty(vodDetail.getCpId()) && CpRoute.isCp(vodDetail.getCpId(), vodDetail.getCustomFields())) {
                                        CpRoute.goCp(vodDetail.getCustomFields());
                                    } else {
                                        //?????????????????? ???????????????
                                        jumpToDetailActivity(position, v);
                                    }
                                }

                                @Override
                                public void getVODDetailFailed() {
                                    SuperLog.info2SD(CollectionAdapter.class.getSimpleName(), "get cpId failed");
                                    //?????????????????? ???????????????
                                    jumpToDetailActivity(position, v);
                                }

                                @Override
                                public void onError() {
                                    //?????????????????? ???????????????
                                    jumpToDetailActivity(position, v);
                                }
                            });
//                            CpRoute.goCp(mContentList.get(position).getVOD().getCustomFields());
                        } else {
                            //?????????????????? ???????????????
                            jumpToDetailActivity(position, v);
                        }
                    }
                    Content content = mContentList.get(position);
                    if (!mDeleteMoreTAG && mDeleteSelectTAG) {
                        //??????????????? ???????????? ????????????????????????  ????????????item
                        mDeleteMoreTAG = true;
                        mVod.setName(mContext.getResources().getString(R.string.mytv_all_choose));
                        mDeleteList.add(content);
                        mContentList.add(0, mSelectAllContent);
                        mRecyclerView.setSelectedPosition(position + 1);

                        isClickDelte = true;

                        //??????????????????????????????????????????????????????????????????????????????
                        if (!mDeleteList.contains(mSelectAllContent) && mContentList.size() - mDeleteList.size() == 1) {
                            mVod.setName(mContext.getResources().getString(R.string.dis_all_choose));
                            mDeleteList.add(mSelectAllContent);
                        }

                        notifyDataSetChanged();
                        return;
                    }
                    if (mDeleteMoreTAG && mDeleteSelectTAG && !mDeleteChoiceTAG) {
                        //???????????? ???????????? ???????????? ????????????????????????0 ?????????????????? ??????????????????item??????
                        if (null != mOnItemDectedListener && mDeleteList.size() > 0) {
                            if (mDeleteList.contains(mSelectAllContent)) {
                                mDeleteList.remove(mSelectAllContent);
                                mOnItemDectedListener.onDeleteAll();
                                return;
                            }
                            mOnItemDectedListener.onItemsDelete(mDeleteList);
                        }
                    }
                    //??????????????? check????????????
                    if (mDeleteMoreTAG && mDeleteChoiceTAG && !mDeleteSelectTAG) {
                        //??????????????????
                        if (position == 0) {//position=0???????????? ?????????????????????????????????????????? ?????????????????????????????????????????? ???????????? ????????????item???????????????
                            if (mDeleteList.containsAll(mContentList)) {
                                mDeleteList.clear();
                                mVod.setName(mContext.getResources().getString(R.string.mytv_all_choose));
                            } else {//?????? ???????????????????????? ???????????????????????? ??????item??????????????????
                                mDeleteList.clear();
                                mDeleteList.addAll(mContentList);
                                mVod.setName(mContext.getResources().getString(R.string.dis_all_choose));
                            }
                            notifyDataSetChanged();
                        } else {
                            if (mDeleteList.contains(content)) {//???????????? ??????????????????????????????item ?????????
                                mDeleteList.remove(content);
                                if (mDeleteList.contains(mSelectAllContent)) {//?????????????????????????????? ?????????????????????
                                    mVod.setName(mContext.getResources().getString(R.string.mytv_all_choose));
                                    mDeleteList.remove(mSelectAllContent);
                                    notifyDataSetChanged();
                                } else {
                                    //????????????item??????
                                    notifyItemChanged(position);
                                }
                            } else {//?????????????????????????????????
                                mDeleteList.add(content);
                                //?????????????????????????????? ?????????????????????
                                if (!mDeleteList.contains(mSelectAllContent) && mContentList.size() - mDeleteList.size() == 1) {
                                    mVod.setName(mContext.getResources().getString(R.string.dis_all_choose));
                                    mDeleteList.add(mSelectAllContent);
                                    notifyDataSetChanged();
                                } else {
                                    //????????????item??????
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
     * ???????????????
     *
     * @param position position
     */
    private void jumpToDetailActivity(int position, View v) {
        Intent intent;
        if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
            //???????????????????????????????????????
            intent = new Intent(mContext, ChildModeVodDetailActivity.class);
        } else {
            //??????????????????
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
            if (hasFocus) {//??????????????? ????????????
                final int position = (int) v.getTag();
                if (null == mContentList || position > mContentList.size() - 1) {
                    return;
                }
                SuperLog.debug("ReserveListAdapter", "position = " + position);
                v.findViewById(R.id.collection_list_item_time).setVisibility(View.VISIBLE);
                v.findViewById(R.id.collection_list_item_name).setSelected(true);
                //v.findViewById(R.id.ic_device_type).setSelected(true);
                //????????????????????????
                View view = v.findViewById(R.id.collection_list_name_layout);
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) view.getLayoutParams();
                params1.height = (int) mContext.getResources().getDimensionPixelSize(R.dimen.history_item_min_height);
                view.setLayoutParams(params1);
                //??????????????????????????????????????????view??????????????? ??????????????????item
                if (position == 0) {
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemChanged(position);
                        }
                    });
                }
                //??????????????????????????????????????????view??????????????? ??????????????????item
                if (mPause) {
                    mPause = false;
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemChanged(position);
                        }
                    });
                }
                //??????????????? position == 0 ????????????item ??????????????? ???????????????
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
                    //??????????????? position -1
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
                if (mDeleteSelectTAG) {//?????????????????????
                    delete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_delete_item_bg_foucs_select));
                } else {
                    delete.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_delete_item_bg));
                }
                //??????check ????????????
                View check = v.findViewById(R.id.collection_list_item_check);
                RelativeLayout.LayoutParams checkParam = (RelativeLayout.LayoutParams) check.getLayoutParams();
                checkParam.height = (int) mContext.getResources().getDimensionPixelSize(R.dimen.history_item_min_height);
                check.setLayoutParams(checkParam);
                if (mDeleteMoreTAG && mDeleteChoiceTAG) {//??????????????? check????????????????????????
                    check.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg_foucs_select));
                } else {
                    check.setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg));
                }

                //????????????????????? ??????????????????
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) v.getLayoutParams();
                params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.history_item_topmargin);
                params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.history_item_topmargin);
                v.setLayoutParams(params);
                v.findViewById(R.id.collection_list_name_layout).setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg));
                if (!mDeleteMoreTAG && !mDeleteSelectTAG) {//??????????????? ???????????????????????????????????????
                    v.findViewById(R.id.collection_list_item_play).setBackground(ContextCompat.getDrawable(mContext, R.drawable.mytv_item_bg_foucs_select));
                }
            } else {
                //?????????????????????item ???wrap???content
                View view = v.findViewById(R.id.collection_list_name_layout);
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) view.getLayoutParams();
                params1.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                view.setLayoutParams(params1);
                v.findViewById(R.id.collection_list_item_name).setSelected(false);
                v.findViewById(R.id.ic_device_type).setSelected(false);
                //??????????????????
                v.findViewById(R.id.collection_list_item_time).setVisibility(View.GONE);
                //?????????0
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
     * ????????????
     */
    public void addData(List<Content> contentList) {
        mContentList.addAll(contentList);
        notifyItemRangeInserted(mContentList.size() - contentList.size(), contentList.size());
    }

    /**
     * ??????????????????????????????
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
     * ??????
     */
    public void deleteItem() {
        //????????????????????????
        mContentList.removeAll(mDeleteList);
        //?????????1??????????????????????????????item ????????????item ??????item?????????
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
