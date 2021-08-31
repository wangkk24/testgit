package com.pukka.ydepg.launcher.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.launcher.bean.node.NamedParameter;
import com.pukka.ydepg.launcher.bean.node.Topic;
import com.pukka.ydepg.launcher.mvp.contact.SubjectContact;
import com.pukka.ydepg.launcher.mvp.presenter.SubjectPresenter;
import com.pukka.ydepg.launcher.view.topic.BaseTopicStyleView;
import com.pukka.ydepg.launcher.view.topic.TopicViewManager;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.featured.view.TopicService;
import com.pukka.ydepg.moudule.vod.activity.VodMainActivity;

import java.util.List;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.ui.activity.SubjectActivity.java
 * @date: 2018-03-11 17:19
 * @version: V1.0 描述当前版本功能
 */
public class TopicActivity extends BaseActivity<SubjectPresenter> implements SubjectContact.ISubjectView {
    private static Topic mTopic;
    private String categoryId;
    private BaseTopicStyleView mBaseTopicStyleView;
    public static final String TOPIC_OBJECT = "topicObject";
    public static final String TOPIC_H5 = "h5";
    public static final String TOPIC_NATIVE = "native";
    public static final String TOPIC_ACTIVITY = "activity";
    public static final String TOPIC_TITLE = "topicTitle";

    private static String mTopicUbd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopic = (Topic) getIntent().getSerializableExtra(TOPIC_OBJECT);
        categoryId = getIntent().getStringExtra(VodMainActivity.CATEGORY_ID);
        mBaseTopicStyleView =
                TopicViewManager.getTopicView(null == mTopic ? "" : mTopic.getTopicStyleId(), mTopic, this);
        setContentView(mBaseTopicStyleView);
        setTopicUbd();
    }

    //返回专题上报 拼接专题id和专题name
    public static String getTopicId(){
        return mTopicUbd;
    }

    private void setTopicUbd(){
        if (null != mTopic){
            String topicName = null;
            List<NamedParameter> params = mTopic.getParams();
            if (!CollectionUtil.isEmpty(params)){
                for (NamedParameter namedParameter : params){
                    if (namedParameter.getKey().equalsIgnoreCase("topicTitle")){
                        if (!CollectionUtil.isEmpty(namedParameter.getValueList())){
                            topicName = namedParameter.getValueList().get(0);
                        }
                    }
                }
            }
            if (!TextUtils.isEmpty(topicName)){
                mTopicUbd =  mTopic.getId() + "_" + topicName;
            }else{
                mTopicUbd =  mTopic.getId();
            }
        }
    }

    public void refreshTopic(){
        Topic topic = TopicService.getInstance().getTopicMap().get(categoryId);
        if (topic != null && TopicViewManager.isStyleIdValid(topic.getTopicStyleId())) {
            mTopic = topic;
        }
        mBaseTopicStyleView = TopicViewManager.getTopicView(null == mTopic ? "" : mTopic.getTopicStyleId(), mTopic, this);
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(mBaseTopicStyleView);
            }
        });
    }

    @Override
    protected void initPresenter() {
        presenter = new SubjectPresenter();
    }

    public void queryData(QueryVODListBySubjectRequest request) {
        presenter.queryVodSubList(request, this);
    }

    @Override
    public void loadData(String total, List<VOD> VODs) {
        mBaseTopicStyleView.loadVodData(total, VODs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTopicUbd = null;
    }
}