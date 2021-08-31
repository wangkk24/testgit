package com.pukka.ydepg.moudule.search.utils;

import android.text.TextUtils;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.Strings;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.search.bean.SearchActorBean;
import com.pukka.ydepg.moudule.search.bean.SearchDataClassify;
import com.pukka.ydepg.moudule.search.bean.SearchSubjectBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.module.search.utils.SearchUtils.java
 * @author:xj
 * @date: 2017-12-19 16:23
 */
public class SearchUtils {

    private static final String TAG = SearchUtils.class.getSimpleName();

    private static final String DEFAULT_ID = "all";

    private static final String DEFAULT_SUBJECT_ID = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.SEARCH_SUBJECT_All);

    public static List<SearchSubjectBean> initSubjectData(){
        SearchDataClassify<SearchSubjectBean> subjectClassify = SessionService.getInstance().getSession().getTerminalConfigurationSearchSubjects();
        List<SearchSubjectBean> listSubject = null;
        if(subjectClassify != null){
            if(SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
                //儿童版使用儿童版栏目信息
                listSubject = subjectClassify.getChild();
            } else {
                //普通版使用普通版配置
                listSubject = subjectClassify.getCommon();
            }
        }

        //数据异常则使用默认全部栏目
        if(listSubject == null || listSubject.size() == 0){
            SearchSubjectBean searchSubjectBean = new SearchSubjectBean();
            searchSubjectBean.setSubjectName(Strings.getInstance().getString(R.string.search_result_type_all));
            searchSubjectBean.setSubjectID(DEFAULT_SUBJECT_ID);
            searchSubjectBean.setId(DEFAULT_ID);
            searchSubjectBean.setSubjectType(0);
            listSubject = new ArrayList<>();
            listSubject.add(searchSubjectBean);
        }
        return listSubject;
    }

    public static List<String> initEveryoneSearchData(){
        List<String> listHotKey = new ArrayList<>();
        try {
            SearchDataClassify<String> hotKeyClassify = SessionService.getInstance().getSession().getTerminalConfigurationHotKeys();
            if(null!=hotKeyClassify) {
                if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
                    //儿童版使用儿童版热词
                    listHotKey = hotKeyClassify.getChild();
                } else {
                    //普通版使用普通版热词
                    listHotKey = hotKeyClassify.getCommon();
                }
            }
            if(listHotKey == null || listHotKey.isEmpty() ){
                listHotKey = new ArrayList<>();
            }
        }catch (Exception e){
            SuperLog.error(TAG,e);
        }
        return listHotKey;
    }

    public static List<SearchActorBean> initActorData(){
        List<SearchActorBean> listHotActor = new ArrayList<>();
        try {
            SearchDataClassify<SearchActorBean> hotActorClassify = SessionService.getInstance().getSession().getTerminalConfigurationHotActors();
            if(SharedPreferenceUtil.getInstance().getIsChildrenEpg() && null != hotActorClassify){
                //儿童版使用儿童版影人
                listHotActor = hotActorClassify.getChild();
            } else if (null != hotActorClassify){
                //普通版使用普通版影人
                listHotActor = hotActorClassify.getCommon();
            }
            if( listHotActor == null || listHotActor.isEmpty() ){
                listHotActor = new ArrayList<>();
            }
        } catch (Exception e){
            SuperLog.error(TAG,e);
        }
        return listHotActor;
    }

    public static String initHotSubject(){
        String hotSubject = null;
        try {
            SearchDataClassify<String> hotSubjectClassify = SessionService.getInstance().getSession().getTerminalConfigurationHotCategory();
            if(SharedPreferenceUtil.getInstance().getIsChildrenEpg() && null != hotSubjectClassify && null != hotSubjectClassify.getChild() && hotSubjectClassify.getChild().size() > 0){
                //儿童版使用儿童版热词
                hotSubject = hotSubjectClassify.getChild().get(0);
            } else if (null != hotSubjectClassify && null != hotSubjectClassify.getCommon() && hotSubjectClassify.getCommon().size() > 0){
                //普通版使用普通版热词
                hotSubject = hotSubjectClassify.getCommon().get(0);
            }
        } catch (Exception e){
            //解析失败使用[全部]栏目作为热搜栏目
            SuperLog.error(TAG,e);
        }
        if(TextUtils.isEmpty(hotSubject)){
            hotSubject = DEFAULT_SUBJECT_ID;
        }
        return hotSubject;
    }

    public enum ActorType{
        TOPIC,
        WEB,
        SEARCH,
        UNKNOWN
    }

    //如果SearchActorBean类型中所有信息都存在,逻辑顺序为 TOPIC>WEB>SEARCH
    public static ActorType getActorType(SearchActorBean actor){
        if( actor.getCategoryID() != null ){
            return ActorType.TOPIC;
        } else if (actor.getUrl() != null){
            return ActorType.WEB;
        } else if (!TextUtils.isEmpty(actor.getName())){
            return ActorType.SEARCH;
        } else {
            return ActorType.UNKNOWN;
        }
    }
}
