package com.pukka.ydepg.moudule.vod.utils;

import android.text.TextUtils;
import android.util.Log;

import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.moudule.vod.presenter.VoddetailEpsiodePresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrowseEpsiodesUtils {

    private static final String TAG = "BrowseEpsiodesUtils";

    //网络请求器
    private VoddetailEpsiodePresenter presenter = new VoddetailEpsiodePresenter();

    //vodid
    private String vodid = "";

    //voddetail
    private VODDetail mVoddetail;

    //总集数
    private int total = 0;

    //子集序号
    private List<String> mEpisodesCount;

    //书签序号
    private int bookMarkSitNum = 1;

    //当前正在播放的集数
    private Episode selesctedEpisode;

    //当前正在播放的集数所在的页签数据
    private List<Episode> markEpisodes;

    /*
     *子集排序方式。
     *取值包括：
     *SITCOMNO:ASC: 按集号升序排序
     *SITCOMNO:DESC: 按集号降序排序
     *说明：
     *如果sortType不传入，平台默认为SITCOMNO:ASC。
     */
    private String sortType = SITCOMNO_ASC;
    public static final String SITCOMNO_ASC = "SITCOMNO:ASC";
    public static final String SITCOMNO_DESC = "SITCOMNO:DESC";

    //是否是正序
    public boolean isPositive(){
        return sortType.equals(SITCOMNO_ASC);
    }


    //已加载的子集信息 key:序号(用每个页签的第一个序号作为key)，value:列表
    private Map<String, List<Episode>> map = new HashMap<>();

    //子集集数信息
    private Map<String, List<String>> mapForIndex = new HashMap<>();

    //每个页签子集的个数
    private int count = 35;

    //查询详情结束的回调
    VoddetailEpsiodePresenter.GetSimpleVodCallback queryVODCallback;

    public BrowseEpsiodesUtils(String vodid) {
        this.vodid = vodid;
    }

    public BrowseEpsiodesUtils(){};

    public String getVodid() {
        return vodid;
    }

    //获取vod的简要详情信息,查询完毕后若不是电影，需要查询书签所在的子集页签和最后一个页签
    public void getSimpleVod(String vodid, VoddetailEpsiodePresenter.GetSimpleVodCallback callback){
        this.vodid = vodid;
        this.queryVODCallback = callback;
        presenter.queryVOD(vodid, new VoddetailEpsiodePresenter.GetSimpleVodCallback() {
            @Override
            public void getSimpleVodSuccess(VODDetail vodDetail) {
                mVoddetail = vodDetail;

                String vodType = vodDetail.getVODType();

                //展示名称8个一组，不展示名称20个一组
                if (vodType.equals("3") || DetailCommonUtils.isShowSerieslayout(vodDetail.getCmsType())) {
                    count = 9;
                } else {
                    count = 3;
                }

                if (vodType.equals("0") || vodType.equals("2")) {
                    //没有子集，直接返回
                    if (null != queryVODCallback){
                        queryVODCallback.getSimpleVodSuccess(mVoddetail);
                    }
                } else {
                    //有子集
                    //子集是正序还是倒序
                    if (DetailCommonUtils.isShowReverselayout(mVoddetail.getCmsType())){
                        sortType = SITCOMNO_DESC;
                    }else{
                        sortType = SITCOMNO_ASC;
                    }

                    //查询简要子集信息
                    queryEpisodeBriefInfo(vodid);
                }
            }

            @Override
            public void getSimpleVodFail() {
                Log.i(TAG, "getSimpleVodFail: ");
            }
        });
    }

    //刷新书签信息
    public void refreshBookMark(Bookmark bookmark,GetEpisodeCallback callback){
        bookMarkSitNum = Integer.valueOf(bookmark.getSitcomNO());
        Log.i(TAG, "refreshBookMark:  "+bookMarkSitNum);
        getEpisode(bookmark.getSitcomNO(),callback);
    }

    public void setBookmark(List<Episode> episodes, Episode episode ){
        markEpisodes = episodes;
        selesctedEpisode = episode;
    }

    //查询连续剧简要子集信息
    public void queryEpisodeBriefInfo(String vodid){
        this.vodid = vodid;
        presenter.queryEpisodeBriefInfo(vodid,-1,0,mCallback);
    }

    //查询VOD的某一集子集
    public void getEpisode(String sitcomNO, GetEpisodeCallback callback){
        int num = -1;
        try {
            num = Integer.valueOf(sitcomNO);
        }catch (Exception e){
            SuperLog.error(TAG,e);
        }
        if (num == -1){
            return;
        }

        if (null == mEpisodesCount || mEpisodesCount.size() == 0){
            if (null != callback){
                callback.getEpisodeFail();
            }
            return;
        }

        String first = mEpisodesCount.get(0);
        String last = mEpisodesCount.get(mEpisodesCount.size() - 1);

        int firstCount = Integer.valueOf(first);
        int lastCount = Integer.valueOf(last);
        if (lastCount > firstCount){
            if (num > lastCount || num < firstCount){
                callback.getEpisodeFail();
                return;
            }
        }else if (lastCount < firstCount){
            if (num > firstCount || num < lastCount){
                callback.getEpisodeFail();
                return;
            }
        }

        int offset = getOffset(sitcomNO);

        int index = -1;
        if (isPositive()){
            index = offset + 1;
        }else{
            index = total - offset;
        }

        List<Episode> episodes = map.get(index + "");
        if (null != episodes && episodes.size() > 0){
            Log.i(TAG, "getEpisode:  map存在");
            for (int i = 0; i <episodes.size(); i++) {
                Episode episode = episodes.get(i);
                if (episode.getSitcomNO().equals(sitcomNO)){
                    Log.i(TAG, "getEpisode: 存在该子集");
                    if (null != callback){
                        callback.getEpisode(episodes, episode);
                        return;
                    }
                }
            }
        }

        Log.i(TAG, "getEpisode: 不存在该子集，请求 "+index + " "+ sitcomNO);
        //该子集未保存，请求该子集
        getEpisodeList(count, offset, new VoddetailEpsiodePresenter.GetEpisodeListCallback() {
            @Override
            public void getEpisodeListSuccess(int total, List<Episode> episodes) {
                if (null != episodes && episodes.size() > 0){
                    //缓存该子集列表
                    String sitNum = episodes.get(0).getSitcomNO();
                    map.put(sitNum,episodes);

                    for (int i = 0; i < episodes.size(); i++) {
                        Episode episode = episodes.get(i);
                        if (episode.getSitcomNO().equals(sitcomNO)){
                            Log.i(TAG, "getEpisodeListSuccess: 请求到了子集");
                            if (null != callback){
                                callback.getEpisode(episodes, episode);
                                return;
                            }
                        }
                    }

                }

                if (null != callback){
                    callback.getEpisodeFail();
                }

            }

            @Override
            public void getEpisodeListFail() {
                if (null != callback){
                    callback.getEpisodeFail();
                }
            }
        });



    }

    //查询VOD的上一集或者下一集
    public void getNextOrPreEpisode(String sitcomNO,boolean isNext,GetEpisodeCallback callback){
        Log.i(TAG, "getNextOrPreEpisode:sitcomNO "+sitcomNO + " isNext "+ isNext);
        int index = Integer.valueOf(sitcomNO);
        if (sortType.equals(SITCOMNO_ASC)){
            if (isNext){
                index ++ ;
            }else{
                index -- ;
            }

        }else if (sortType.equals(SITCOMNO_DESC)){
            if (isNext){
                index -- ;
            }else{
                index ++ ;
            }
        }

        if (index < 1){
            if (null != callback){
                callback.getEpisodeFail();
            }
        }else{
            getEpisode(index+"",callback);
        }
    }

    //查询VOD的最后一集
    public void getLastEpisode(GetEpisodeCallback callback){

        if (Integer.valueOf(mVoddetail.getVodNum()) == mEpisodesCount.size()){
            String lastStr ="";
            if (sortType.equals(SITCOMNO_ASC)){
                lastStr = mEpisodesCount.get(mEpisodesCount.size() - 1 );
            }else if (sortType.equals(SITCOMNO_DESC)){
                lastStr = mEpisodesCount.get(0);
            }
            getEpisode(lastStr,callback);
        }else{
            callback.getEpisodeFail();
        }
    }

    //查询子集列表
    public void getEpisodeList(int count, int offset, VoddetailEpsiodePresenter.GetEpisodeListCallback callback){
        presenter.queryEpisodeList(vodid, 0, count, offset, sortType, callback);
    }

    VoddetailEpsiodePresenter.GetEpisodeBriefInfoCallback mCallback = new VoddetailEpsiodePresenter.GetEpisodeBriefInfoCallback() {
        @Override
        public void getEpisodeBriefInfoSuccess(List<String> episodesCount) {

            if (null == episodesCount || episodesCount.size() == 0){
                if (null != queryVODCallback){
                    queryVODCallback.getSimpleVodSuccess(mVoddetail);
                }
                return;
            }


            mEpisodesCount = episodesCount;
            total = episodesCount.size();
            if (isPositive()){
                bookMarkSitNum = 1;
            }else{
                bookMarkSitNum = Integer.valueOf(episodesCount.get(episodesCount.size() - 1)) ;
            }

            //查询书签所在页签
            Bookmark bookmark = mVoddetail.getBookmark();

            String  zjSitcomNo = "";
            if (null != bookmark){
                List<NamedParameter> listNp = bookmark.getCustomFields();
                List<String> vodCustomNamed = CommonUtil.getCustomNamedParameterByKey(listNp, Constant.DETAIL_ZJ_BOOKMARK);
                if (null != vodCustomNamed && vodCustomNamed.size() > 0){
                    zjSitcomNo = vodCustomNamed.get(0);
                }
            }

            Log.i(TAG, "zjSitcomNo: "+zjSitcomNo);

            if (null != bookmark && !TextUtils.isEmpty(bookmark.getSitcomNO())){
                try{
                    bookMarkSitNum = Integer.valueOf(bookmark.getSitcomNO());
                }catch (Exception e){
                    DebugLog.error(TAG, e);
                }
            }else if (!TextUtils.isEmpty(zjSitcomNo)){
                try{
                    bookMarkSitNum = Integer.valueOf(bookmark.getSitcomNO());
                }catch (Exception e){
                    DebugLog.error(TAG, e);
                }
            }

            int offset = getOffset(bookMarkSitNum + "");

            getEpisodeList(count, offset, new VoddetailEpsiodePresenter.GetEpisodeListCallback() {
                @Override
                public void getEpisodeListSuccess(int total, List<Episode> episodes) {
                    Log.i(TAG, "BrowseEpsiodesUtils: "+episodes.size());
                    markEpisodes = new ArrayList<>();
                    markEpisodes.addAll(episodes);
                    selesctedEpisode = null;
                    for (int i = 0; i < episodes.size(); i++) {
                        Episode episode = episodes.get(i);
                        Log.i(TAG, "getEpisodeListSuccess:  "+episode.getSitcomNO() + " "+ bookMarkSitNum);
                        if (episode.getSitcomNO().equals(bookMarkSitNum+"")){
                            selesctedEpisode = episode;
                        }
                    }
                    if (selesctedEpisode == null){
                        selesctedEpisode = episodes.get(0);
                    }
                    Episode episode = episodes.get(0);
                    map.put(episode.getSitcomNO(),markEpisodes);
                    mVoddetail.setEpisodes(episodes);

                    //正序请求最后一页，倒序请求第一页
                    if (sortType.equals(SITCOMNO_ASC)){
                        if (total != 0 && count != 0){
                            int index = getOffset(Integer.valueOf(mEpisodesCount.get(mEpisodesCount.size() - 1))+"");

                            List<Episode> tempList = map.get(index+1+"");
                            if (tempList != null && tempList.size() > 0){
                                //最后一页就是书签所在页，结束
                                queryVODCallback.getSimpleVodSuccess(mVoddetail);
                            }else{
                                //请求最后一页
                                getEpisodeList(count, index, new VoddetailEpsiodePresenter.GetEpisodeListCallback() {
                                    @Override
                                    public void getEpisodeListSuccess(int total, List<Episode> episodes) {
                                        if (null != episodes && episodes.size() > 0){
                                            Episode tempEpisode = episodes.get(0);
                                            List<Episode> tempList1 = map.get(tempEpisode.getSitcomNO());
                                            if (null != tempList1 && tempList1.size() > 0 ){

                                            }else{
                                                map.put(tempEpisode.getSitcomNO(),episodes);
                                                List<Episode> episodeList = mVoddetail.getEpisodes();
                                                episodeList.addAll(episodes);
                                                mVoddetail.setEpisodes(episodeList);
                                            }
                                        }
                                        queryVODCallback.getSimpleVodSuccess(mVoddetail);

                                    }

                                    @Override
                                    public void getEpisodeListFail() {
                                        Log.i(TAG, "getEpisodeListFail: 1");
                                    }
                                });

                            }
                        }
                    }else if (sortType.equals(SITCOMNO_DESC)){
                        List<Episode> tempList = map.get(Integer.valueOf(mEpisodesCount.get(mEpisodesCount.size() - 1)) + "");
                        if (null != tempList && tempList.size() > 0){
                            //第一页就是书签所在页，结束
                            queryVODCallback.getSimpleVodSuccess(mVoddetail);
                        }else{
                            //请求第一页
                            getEpisodeList(count, 0, new VoddetailEpsiodePresenter.GetEpisodeListCallback() {
                                @Override
                                public void getEpisodeListSuccess(int total, List<Episode> episodes) {
                                    if (null != episodes && episodes.size() > 0){
                                        Episode tempEpisode = episodes.get(0);
                                        List<Episode> tempList1 = map.get(tempEpisode.getSitcomNO());
                                        if (null != tempList1 && tempList1.size() > 0 ){
                                            //结束

                                        }else{
                                            map.put(tempEpisode.getSitcomNO(),episodes);
                                            List<Episode> episodeList = new ArrayList<>();
                                            episodeList.addAll(mVoddetail.getEpisodes());
                                            episodeList.addAll(episodes);
                                            mVoddetail.setEpisodes(episodeList);
                                            //结束

                                        }
                                    }
                                    queryVODCallback.getSimpleVodSuccess(mVoddetail);
                                }

                                @Override
                                public void getEpisodeListFail() {
                                    Log.i(TAG, "getEpisodeListFail: 2");
                                }
                            });
                        }
                    }
                }

                @Override
                public void getEpisodeListFail() {
                    Log.i(TAG, "getEpisodeListFail: 3");
                }
            });

        }

        @Override
        public void getEpisodeBriefInfoFail() {
            Log.i(TAG, "getEpisodeBriefInfoFail: 4");
        }
    };

    private List<List<String>> SitcomNos = new ArrayList<>();

    public List<List<String>> querySitcomNos(){
        return SitcomNos;
    }

    //获取分页完的子集页签
    public List<List<String>> getSitcomNos(){

        if (null == mEpisodesCount || mEpisodesCount.size() == 0){
            return new ArrayList<>();
        }

        if (null != SitcomNos && SitcomNos.size() > 0){
            return SitcomNos;
        }

        String vodType = mVoddetail.getVODType();
        if (vodType.equals("0") || vodType.equals("2")){
            return new ArrayList<>();
        }

        mapForIndex = new HashMap<>();
        List<List<String>> SitcomNos = new ArrayList<>();
        if (mEpisodesCount.size() <= 1){
            String one = mEpisodesCount.get(0);
            List<String> tempEpisodes = new ArrayList<>();
            tempEpisodes.add(one);
            SitcomNos.add(tempEpisodes);
            this.SitcomNos = SitcomNos;
            mapForIndex.put(one,tempEpisodes);
            return SitcomNos;
        }


        int remainder = total % count;
        int totalIndex = remainder == 0 ? total/count : (total/count + 1);

        String one = mEpisodesCount.get(0);
        String two = mEpisodesCount.get(1);

        if (sortType.equals(SITCOMNO_DESC) &&  (Integer.valueOf(one) < Integer.valueOf(two))){
            Collections.reverse(mEpisodesCount);
        }

        for (int i = 0; i < totalIndex; i++) {
            SitcomNos.add(new ArrayList<String>());
        }

        int index = 0;
        for (int i = 0; i < mEpisodesCount.size(); i++) {
            index = i/count;
            List<String>list = SitcomNos.get(index);
            list.add(mEpisodesCount.get(i));
        }
        this.SitcomNos = SitcomNos;

        for (int i = 0; i < SitcomNos.size(); i++) {
            mapForIndex.put(SitcomNos.get(i).get(0),SitcomNos.get(i));
        }

        return SitcomNos;
    }

    public Map<String, List<String>> getMapForIndex() {
        return mapForIndex;
    }

    //获取某一分页的内容，可能获取到空，获取到空时调用请求接口获取
    public List<Episode> getEpisodes(int index){
        boolean isExist = false;
        if (null != map.get(index+"") && (map.get(index+"").size() > 0)){
            isExist = true;
        }
        Log.i(TAG, "getEpisodes: "+isExist);
        return map.get(index+"");
    }

    //保存
    public void saveEpisodes(List<Episode> episodes){
        if (null == episodes || episodes.size() == 0){
            return;
        }
        map.put(episodes.get(0).getSitcomNO(),episodes);
    }

    public VODDetail getmVoddetail() {
        return mVoddetail;
    }

    public Episode getSelesctedEpisode() {
        return selesctedEpisode;
    }

    public int getBookMarkSitNum() {
        return bookMarkSitNum;
    }

    public int getTotal() {
        return total;
    }

    public List<String> getmEpisodesCount() {
        return mEpisodesCount;
    }

    public List<Episode> getMarkEpisodes() {
        return markEpisodes;
    }

    //是否有下一集
    public boolean hasNext(String numStr){
        if (null == mEpisodesCount || mEpisodesCount.size() == 0){
            Log.i(TAG, "BrowseEpsiodesUtils: 为空");
            return false;
        }
        String first = mEpisodesCount.get(0);
        String last = mEpisodesCount.get(mEpisodesCount.size() - 1);

        int firstCount = Integer.valueOf(first);
        int lastCount = Integer.valueOf(last);
        int num = Integer.valueOf(numStr);
        Log.i(TAG, "BrowseEpsiodesUtils:  first "+ firstCount + " last "+lastCount +" num "+num);

        if (isPositive()){
            if (num >= lastCount){
                return false;
            }else{
                return true;
            }
        }else{
            if (num == 1){
                return false;
            }else{
                return true;
            }
        }
    }

    //查询VOD的具体一个子集
    public interface GetEpisodeCallback {
        void getEpisode(List<Episode> episodes, Episode episode);
        void getEpisodeFail();
    }

    //根据集号获得所在分页第一集的offset
    public int getOffset(String sitNum){
        int num = Integer.valueOf(sitNum);
        List<List<String>> sitNumLists = getSitcomNos();
        for (int i = 0; i < sitNumLists.size(); i++) {
            List<String> sitNums = sitNumLists.get(i);
            if ((num >= Integer.valueOf(sitNums.get(0)) && num <= Integer.valueOf(sitNums.get(sitNums.size() - 1)) )||
                    (num <= Integer.valueOf(sitNums.get(0)) && num >= Integer.valueOf(sitNums.get(sitNums.size() - 1)))){
                return i * count;
            }
        }

        return 0;

//        int num = Integer.valueOf(sitNum);
//
//        if (sortType.equals(SITCOMNO_ASC)){
//            //正序情况
//            int mutile = (num - 1)/count;
//            return mutile * count;
//        }else{
//            //逆序情况
//            int index = total - num;
//            return  index / count * count;
//        }

    }

    //从已保存的子集中，获取书签，刷新书签
    public void refresh(String sitNum){
        boolean isExist = false;
        for (List<Episode> list: map.values()) {
            for (Episode episode :list){
                if (episode.getSitcomNO().equals(sitNum)){
                    selesctedEpisode = episode;
                    markEpisodes = list;
                    bookMarkSitNum = Integer.valueOf(sitNum);
                    isExist = true;
                    break;
                }
            }
        }
        if (!isExist){
            getEpisode(sitNum, new GetEpisodeCallback() {
                @Override
                public void getEpisode(List<Episode> episodes, Episode episode) {
                    selesctedEpisode = episode;
                    markEpisodes = episodes;
                    bookMarkSitNum = Integer.valueOf(sitNum);
                }

                @Override
                public void getEpisodeFail() {

                }
            });
        }
    }

    public void refresh(String sitNum,GetEpisodeCallback callback){
        boolean isExist = false;
        for (List<Episode> list: map.values()) {
            for (Episode episode :list){
                if (episode.getSitcomNO().equals(sitNum)){
                    selesctedEpisode = episode;
                    markEpisodes = list;
                    bookMarkSitNum = Integer.valueOf(sitNum);
                    isExist = true;
                    break;
                }
            }
        }
        if (!isExist){
            getEpisode(sitNum, new GetEpisodeCallback() {
                @Override
                public void getEpisode(List<Episode> episodes, Episode episode) {
                    Log.i(TAG, "refresh getEpisode: 1");
                    selesctedEpisode = episode;
                    markEpisodes = episodes;
                    bookMarkSitNum = Integer.valueOf(sitNum);

                    if (null != callback){
                        callback.getEpisode(episodes,episode);
                    }
                }

                @Override
                public void getEpisodeFail() {
                    Log.i(TAG, "refresh getEpisode: 2");

                }
            });
        }else{
            if (null != callback){
                callback.getEpisode(new ArrayList<>(),null);
            }
        }
    }
}
