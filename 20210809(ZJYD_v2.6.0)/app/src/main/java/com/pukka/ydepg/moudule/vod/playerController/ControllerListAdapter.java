package com.pukka.ydepg.moudule.vod.playerController;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.hp.hpl.sparta.xpath.Step;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewProductOrderActivity;
import com.pukka.ydepg.moudule.vod.NewAdapter.NewChooseEpisodeAdapter;
import com.pukka.ydepg.moudule.vod.utils.BrowseEpsiodesUtils;
import com.pukka.ydepg.moudule.vod.utils.DetailCommonUtils;
import com.pukka.ydepg.moudule.vod.utils.VoddetailEpsiodesUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControllerListAdapter extends RecyclerView.Adapter<ControllerListAdapter.ViewHolder> {
    private static final String TAG = "ControllerListAdapter";

    //防止焦点快速移动错位
    private boolean canMove = true;

    //是否可以设置倍速
    private boolean canSetSpeed;

    //是否可以设置跳过片头片尾
    private boolean canSetSkip;

    private Context mContext;

    private VODDetail mVodDetail;

    float density = 1f;

    //当前展示的剧集,用于计算是否展示翻页icon
    private List<Episode> nowEpisode;
    private List<String> nowEpisodeIndex;

    private List<List<String>> mEpisodeIndexList;

    private int firstSelectedIndex = 0;

    //标识当前到第几页
    private int listPosition ;

    private String fatherPrice;

    //当前焦点所在楼层（默认在选集，也就是0层）
    private int floorFocused = 0;

    //第一次拉起，第一行获得焦点
    private boolean isFirst = true;

    //楼层个数
    private int itemCountTotal = 0;

    //楼层数组
    private List<String> floorList = new ArrayList<>();
    private static final String floorEpisode = "选集";
    private static final String floorSpeed = "倍速";
    private static final String floorSkip = "跳过片头片尾";

    BrowseEpsiodesUtils utils;

    //绑定的recycleview;
    VerticalGridView recycleview;

    //当前是否是综艺
    private boolean isNotVariety;

    private float nowSpeed;

    //倍速列表
    private List<Float> speedList = new ArrayList<>();

    //倒计时隐藏回调
    VodPlayerControllerView.HideCountDownCallback countDownCallback;

    public ControllerListAdapter(VODDetail vodDetail, boolean canSetSpeed, boolean canSetSkip, Context context , BrowseEpsiodesUtils utils , VerticalGridView recycleview,String fatherPrice, float nowSpeed) {
        this.mVodDetail = vodDetail;
        this.canSetSpeed = canSetSpeed;
        this.canSetSkip = canSetSkip;
        this.mContext = context;
        this.nowSpeed = nowSpeed;

        this.utils = utils;
        this.recycleview = recycleview;
        this.fatherPrice = fatherPrice;

        float desity = context.getResources().getDisplayMetrics().density;
        if (desity > 0){
            this.density = desity;
        }

        String vodType = mVodDetail.getVODType();
        this.isNotVariety = (vodType.equals("3") || DetailCommonUtils.isShowSerieslayout(mVodDetail.getCmsType()));
        itemCountTotal = 0;
        floorList.clear();
        if (!vodType.equals("0") && !vodType.equals("2")){
            //有子集
            this.mEpisodeIndexList = utils.getSitcomNos();
            itemCountTotal ++;
            floorList.add(floorEpisode);
        }


        if (this.canSetSpeed){
            initSpeedList();
            itemCountTotal ++;
            floorList.add(floorSpeed);
        }
        if (this.canSetSkip){
            itemCountTotal ++;
            floorList.add(floorSkip);
        }

        if (floorList.contains(floorEpisode)){
            nowEpisode = utils.getMarkEpisodes();

            for (int i = 0; i < mEpisodeIndexList.size(); i++) {
                List<String> indexList = mEpisodeIndexList.get(i);
                if (indexList.get(0).equals(nowEpisode.get(0).getSitcomNO())){
                    nowEpisodeIndex = indexList;
                    listPosition = i;
                    break;
                }
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_window_controller_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: VodPlayerControllerView onbind");

        String floor = floorList.get(position);
        switch (floor){
            case floorEpisode:{
                //选集楼层
                initEpisodes(holder);
                break;
            }
            case floorSpeed:{
                //倍速楼层
                initSpeed(holder);
                break;
            }
            case floorSkip:{
                //跳过片头片尾楼层
                initSkip(holder);
                break;
            }

            default:break;
        }

        if (isFirst && position == 0){
            holder.title.requestFocus();

            isFirst = false;
        }



    }

    //剧集的点击事件
    private View.OnClickListener episodeClickListener;

    //倍速的点击事件
    private View.OnClickListener speedClickListener;

    //跳过片头片尾的点击时间
    private View.OnClickListener skipClickListener;

    //初始化选集楼层
    private void initEpisodes(ViewHolder holder){

        String vodType = mVodDetail.getVODType();
        if (!vodType.equals("0") && !vodType.equals("2")) {
            if (vodType.equals("3") || DetailCommonUtils.isShowSerieslayout(mVodDetail.getCmsType())) {
                ControllerListEpisodeAdapter adapter = new ControllerListEpisodeAdapter(nowEpisode,nowEpisodeIndex,fatherPrice,utils,mContext);
                initOnKeyListener(holder,adapter,true,false);
                if (null != episodeClickListener){
                    adapter.setItemOnClickListener(episodeClickListener);
                }
                holder.recyclerView.setAdapter(adapter);
                int selectedIndex = 0;
                for (int i = 0; i < nowEpisodeIndex.size() ; i++) {
                    String index = nowEpisodeIndex.get(i);
                    int sitNum = Integer.valueOf(index);

                    if (sitNum == utils.getBookMarkSitNum()){
                        selectedIndex = i;
                        break;
                    }
                }

                firstSelectedIndex = selectedIndex;
                adapter.notifyDataSetChanged();
                holder.recyclerView.setSelectedPosition(firstSelectedIndex);

            }else{

                ControllerListVarietyEpisodeAdapter adapter = new ControllerListVarietyEpisodeAdapter(nowEpisode,nowEpisodeIndex,fatherPrice,utils,mContext);
                initOnKeyListener(holder,adapter,true,false);
                if (null != episodeClickListener){
                    adapter.setOnClickListener(episodeClickListener);
                }
                holder.recyclerView.setAdapter(adapter);
                int selectedIndex = 0;
                for (int i = 0; i < nowEpisodeIndex.size() ; i++) {
                    String index = nowEpisodeIndex.get(i);
                    int sitNum = Integer.valueOf(index);

                    if (sitNum == utils.getBookMarkSitNum()){
                        selectedIndex = i;
                        break;
                    }
                }

                firstSelectedIndex = selectedIndex;
                adapter.notifyDataSetChanged();
                holder.recyclerView.setSelectedPosition(firstSelectedIndex);
            }

            if (needShowPageIcon(0)){
                holder.EpisodeNextIcon.setVisibility(View.VISIBLE);
            }else{
                holder.EpisodeNextIcon.setVisibility(View.GONE);
            }
        }

    }

    //初始化倍速楼层
    private void initSpeed(ViewHolder holder){
        Log.i(TAG, "initSpeed: 初始化倍速楼层 设置倍速事件 "+(null == speedClickListener));
        holder.title.setText("倍速");
        ControllerListItemAdapter adapter = new ControllerListItemAdapter(mContext,true,false,nowSpeed);
        initOnKeyListener(holder,adapter,false,true);
        adapter.setItemOnClickListener(speedClickListener);

        for (int i = 0; i < speedList.size(); i++) {
            float speed = speedList.get(i);
            if (speed ==  nowSpeed){
                holder.recyclerView.setSelectedPosition(i);
            }
        }
        holder.recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //初始化跳过片头片尾楼层
    private void initSkip(ViewHolder holder){
        holder.title.setText("跳过片头片尾");
        ControllerListItemAdapter adapter = new ControllerListItemAdapter(mContext,false,true,1f);
        initOnKeyListener(holder,adapter,false,false);
        adapter.setItemOnClickListener(skipClickListener);
        holder.recyclerView.setAdapter(adapter);
        boolean skipOpen = SharedPreferenceUtil.getInstance().getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "",true);
        if (skipOpen){
            holder.recyclerView.setSelectedPosition(0);
        }else{
            holder.recyclerView.setSelectedPosition(1);
        }


        adapter.notifyDataSetChanged();
    }

    private void initOnKeyListener(ViewHolder holder,ControllerEpisodeAdapter adapter,boolean isEpisode ,boolean isSpeed){
        adapter.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(TAG, "onKey: "+keyCode);

                if (null != countDownCallback){
                    countDownCallback.onkey();
                }

                if (!canMove){
                    return true;
                }
                canMove = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        canMove = true;
                    }
                },100);


                if (event.getAction() == KeyEvent.ACTION_DOWN){

                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                        Log.i(TAG, "onKey: DOWN");

                        if (floorPlus(holder)){
                            floorRequestFocuse(holder);
                        }
                        return true;

                    }else if (keyCode == KeyEvent.KEYCODE_DPAD_UP){
                        Log.i(TAG, "onKey: UP");
                        if (floorReduce(holder)){
                            floorRequestFocuse(holder);
                        }
                        return true;
                    }else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
                        Log.i(TAG, "onKey: center");
                        if (isSpeed){
                            if (null != speedClickListener){
                                speedClickListener.onClick(v);
                                return true;
                            }
                        }else if (isEpisode){
                            if (null != episodeClickListener){
                                episodeClickListener.onClick(v);
                                return true;
                            }
                        }else{
                            if (null != skipClickListener){
                                skipClickListener.onClick(v);
                                return true;
                            }
                        }
                    }

                    if (isEpisode){
                        int position = (int) v.getTag(R.id.controller_item_position);
                        Log.i(TAG, "onKey:  "+position );
                        String vodType = mVodDetail.getVODType();
                        boolean isNotVariety = (vodType.equals("3") || DetailCommonUtils.isShowSerieslayout(mVodDetail.getCmsType()));
                        if ( ((isNotVariety && position == 8) || (!isNotVariety && position == 2)) && needShowPageIcon(0)){
                            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                                listPosition ++ ;
                                pageChange(holder,adapter,0,listPosition);
                                return true;
                            }

                        }else if (isNotVariety && ((utils.getTotal()%9 - 1== position ) || ((utils.getTotal()%9 == 0 && position == 8)))){
                            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && !needShowPageIcon(0)){
                               listPosition = 0;
                                pageChange(holder,adapter,0,listPosition);
                                return true;
                            }


                        }else if (!isNotVariety && ((utils.getTotal()%3 - 1== position ) || ((utils.getTotal()%3 == 0 && position == 2)))){
                            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && !needShowPageIcon(0)){
                                listPosition = 0;
                                pageChange(holder,adapter,0,listPosition);
                                return true;
                            }
                        }

                        if (position == 0){
                            //第一个item,摁左键翻页
                            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && needShowPageIcon(1)){
                                listPosition -- ;
                                pageChange(holder,adapter,1,listPosition);
                                return true;
                            }else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && !needShowPageIcon(1)){
                                listPosition = mEpisodeIndexList.size() - 1;
                                pageChange(holder,adapter,1,listPosition);
                                return true;
                            }
                        }

                    }
                }


                return false;
            }
        });

    }

    //翻页 0首个item获得焦点，1最后一个item获得焦点  listPosition标识要翻页的页面的position
    private void pageChange(ViewHolder holder,ControllerEpisodeAdapter adapter,int focused , int listPosition){
        if (listPosition >= 0 && listPosition < mEpisodeIndexList.size()){
            List<String> episodeIndex = mEpisodeIndexList.get(listPosition);
            nowEpisodeIndex = episodeIndex;
            adapter.setDataEpisodesSource(episodeIndex);
            changePageIconShowing(holder);

            holder.clearView.setFocusable(true);
            holder.clearView.requestFocus();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != holder.recyclerView.getLayoutManager()){
                        int index = 0;
                        if (focused == 1){
                            index = nowEpisodeIndex.size() - 1;
                        }
                        View view = holder.recyclerView.getLayoutManager().findViewByPosition(index);
                        if (null != view){
                            ImageView imageView = view.findViewById(R.id.controller_item_bg);
                            if (null != imageView){
                                holder.recyclerView.setSelectedPosition(index);
                                imageView.requestFocus();
                            }
                        }
                    }
                    holder.clearView.setFocusable(false);
                }
            },300);
        }
    }

    //控制判断右两侧的翻页icon是否还要显示
    private void changePageIconShowing(ViewHolder holder){
        if (needShowPageIcon(0)){
            holder.EpisodeNextIcon.setVisibility(View.VISIBLE);
        }else{
            holder.EpisodeNextIcon.setVisibility(View.GONE);
        }
    }

    //指定楼层获取焦点
    private void floorRequestFocuse(ViewHolder holder){
        Log.i(TAG, "floorRequestFocuse: null != recyclerView.getLayoutManager() "+(null != holder.recyclerView.getLayoutManager()));
        if (null != recycleview.getLayoutManager()){
            View view = recycleview.getLayoutManager().findViewByPosition(floorFocused);
            Log.i(TAG, "floorRequestFocuse: null != view "+(null != view));
            if (null != view){
                TextView textView = view.findViewById(R.id.controller_floor_title);
                Log.i(TAG, "floorRequestFocuse: null != textView "+(null != textView));
                if (null != textView){
                    textView.setFocusable(true);
                    textView.requestFocus();
                }
            }
        }
    }




    @Override
    public int getItemCount() {
        return itemCountTotal;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public HorizontalGridView recyclerView;
        public RelativeLayout recycylerviewLayout;
        //透明view,剧集换页时获取焦点，防止焦点错乱
        public TextView clearView;
        //右翻页标识Icon
        public ImageView EpisodeNextIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recycylerviewLayout = itemView.findViewById(R.id.controller_floor_list_layout);
            recyclerView = itemView.findViewById(R.id.controller_floor_list);
            recyclerView.setNumRows(1);
            recyclerView.addItemDecoration(new SpaceItemDecoration(mContext, 0));
            title = itemView.findViewById(R.id.controller_floor_title);
            clearView = itemView.findViewById(R.id.controller_clear);
            EpisodeNextIcon = itemView.findViewById(R.id.controller_icon_right);

            title.setAlpha(0.4f);
            title.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.margin_24)/density);


            title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus){
                        //展示本层列表
//                        recyclerView.setVisibility(View.VISIBLE);
                        title.setAlpha(1);
                        title.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.margin_28_5)/density);

                        //焦点移动到本层列表上
                        Log.i(TAG, "onFocus: null != recyclerView.getLayoutManager() "+(null != recyclerView.getLayoutManager()));
                        if (null != recyclerView.getLayoutManager()){
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    int selectedIndex = recyclerView.getSelectedPosition();
                                    View view = recyclerView.getLayoutManager().findViewByPosition(selectedIndex);
                                    Log.i(TAG, "onFocus: null != view "+(null != view));
                                    if (null != view){
                                        ImageView imageView = view.findViewById(R.id.controller_item_bg);
                                        Log.i(TAG, "onFocus: null != imageView "+(null != imageView));
                                        if (null != imageView){
                                            imageView.setFocusable(true);
                                            imageView.requestFocus();
                                        }
                                    }
                                }
                            },100);

                        }



                    }
                }
            });
            clearView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    return true;
                }
            });


        }
    }

    //楼层下移 回调是否能够移动
    public boolean floorPlus(ViewHolder holder){
        Log.i(TAG, "floorPlus: floorFocused "+floorFocused+ " itemCountTotal "+itemCountTotal);
        if (floorFocused + 1 >= itemCountTotal){
            return false;
        }

        if (null != recycleview.getLayoutManager()){
            View view = recycleview.getLayoutManager().findViewByPosition(floorFocused);
            Log.i(TAG, "floorPlus: null != view "+(null != view));
            if (null != view){
                //隐藏本层的列表
                RelativeLayout layout = view.findViewById(R.id.controller_floor_list_layout);
                Log.i(TAG, "floorPlus: null != horizontalGridView "+(null != layout));
                if (null != layout){
                    layout.setVisibility(View.GONE);
                }
                TextView textView = view.findViewById(R.id.controller_floor_title);
                //本层标题置灰
                if (null != textView){
                    textView.setAlpha(0.4f);
                    textView.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.margin_24)/density);
                }
            }
        }
        floorFocused ++ ;

        //移动后楼层标题高亮
        if (null != recycleview.getLayoutManager()){
            View view = recycleview.getLayoutManager().findViewByPosition(floorFocused);
            Log.i(TAG, "floorPlus: null != view "+(null != view));
            if (null != view){
                TextView textView = view.findViewById(R.id.controller_floor_title);
                //本层标题高亮
                if (null != textView){
                    textView.setAlpha(1f);
                    textView.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.margin_28_5)/density);
                }
            }
        }
        return true;
    }

    //楼层上移 回调是否能够移动
    public boolean floorReduce(ViewHolder holder){
        Log.i(TAG, "floorPlus: floorReduce "+floorFocused);
        if (floorFocused <= 0){
            return false;
        }
        //移动前的楼层标题置灰
        if (null != recycleview.getLayoutManager()){
            View view = recycleview.getLayoutManager().findViewByPosition(floorFocused);
            if (null != view){
                TextView textView = view.findViewById(R.id.controller_floor_title);
                //本层标题置灰
                if (null != textView){
                    textView.setAlpha(0.4f);
                    textView.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.margin_24)/density);
                }
            }
        }

        floorFocused --;

        if (null != recycleview.getLayoutManager()){
            View view = recycleview.getLayoutManager().findViewByPosition(floorFocused);
            if (null != view){
                TextView textView = view.findViewById(R.id.controller_floor_title);
                //本层标题高亮
                if (null != textView){
                    textView.setAlpha(1f);
                }
                //展示本层的列表
                RelativeLayout layout = view.findViewById(R.id.controller_floor_list_layout);
                Log.i(TAG, "floorPlus: null != horizontalGridView "+(null != layout));
                if (null != layout){
                    layout.setVisibility(View.VISIBLE);
                }
            }
        }


        return true;
    }

    //是否展示翻页箭头 0右侧 1左侧
    private boolean needShowPageIcon(int direction){
        Log.i(TAG, "needShowPageIcon: "+direction + " "+ listPosition + " "+ mEpisodeIndexList.size());

        if (direction == 0){
            //右侧
            if (listPosition < mEpisodeIndexList.size() -1){
                return true;
            }else{
                return false;
            }

        }else{
            //左侧
            if (listPosition > 0){
                return true;
            }else{
                return false;
            }
        }
    }

    public void setEpisodeClickListener(View.OnClickListener episodeClickListener) {
        this.episodeClickListener = episodeClickListener;
    }

    public void setSpeedClickListener(View.OnClickListener speedClickListener) {
        this.speedClickListener = speedClickListener;
    }

    public void setSkipClickListener(View.OnClickListener skipClickListener) {
        this.skipClickListener = skipClickListener;
    }

    //recycleview的间距
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private static final String TAG = "CustomItemDecoration";

        private int mSpace;

        private Context mContext;

        /**
         * @param space 传入的值，其单位视为dp
         */
        public SpaceItemDecoration(Context context, float space) {
            SuperLog.debug(TAG, "space is " + space);
            this.mContext = context;
            this.mSpace = (int) space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int itemCount = parent.getAdapter().getItemCount();
            int pos = parent.getChildAdapterPosition(view);
            SuperLog.debug(TAG, "itemCount>>" + itemCount + ";Position>>" + pos);
            if (pos == 0){
                outRect.left = mContext.getResources().getDimensionPixelSize(R.dimen.margin_6);
            }else{
                outRect.left = 0;
            }

            if (isNotVariety){
                outRect.right = mContext.getResources().getDimensionPixelSize(R.dimen.margin_5);
            }else{
                outRect.right = mContext.getResources().getDimensionPixelSize(R.dimen.margin_10);
            }

            outRect.top = mContext.getResources().getDimensionPixelSize(R.dimen.margin_5);
//            outRect.bottom = mContext.getResources().getDimensionPixelSize(R.dimen.margin_10);
            outRect.bottom = mContext.getResources().getDimensionPixelSize(R.dimen.margin_5);;

        }
    }

    //初始化倍速
    private void initSpeedList(){
        speedList.clear();
        String speeds = SessionService.getInstance().getSession().getTerminalConfigurationValue("vod_speed_list");
        if(TextUtils.isEmpty(speeds)){
            Log.e("VodEpisodesView", "TextUtils.isEmpty(speeds)");
            speedList.add(0.8f);
            speedList.add(1.0f);
            speedList.add(1.25f);
            speedList.add(1.5f);
            speedList.add(2.0f);
        }
        else{
            try {
                String[] speedarray = speeds.split(",");
                if (speedarray != null && speedarray.length == 5) {
                    Log.e("VodEpisodesView", "speedarray != null && speedarray.length == 5");
                    for(String speed:speedarray){
                        speedList.add(Float.parseFloat(speed));
                    }
                } else {
                    Log.e("VodEpisodesView", "speedarray != null && speedarray.length == 5 !!!");
                    speedList.add(0.8f);
                    speedList.add(1.0f);
                    speedList.add(1.25f);
                    speedList.add(1.5f);
                    speedList.add(2.0f);
                }
            }catch (Exception e){
                Log.e("VodEpisodesView", e.getMessage());
                speedList.add(0.8f);
                speedList.add(1.0f);
                speedList.add(1.25f);
                speedList.add(1.5f);
                speedList.add(2.0f);
            }
        }
    }

    public void setCountDownCallback(VodPlayerControllerView.HideCountDownCallback countDownCallback) {
        this.countDownCallback = countDownCallback;
    }
}
