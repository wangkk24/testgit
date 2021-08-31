package com.pukka.ydepg.moudule.voice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.refresh.RefreshManager;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.iflytek.xiri.Feedback;
import com.iflytek.xiri.scene.ISceneListener;
import com.iflytek.xiri.scene.Scene;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.moudule.mytv.NewMyMovieActivity;

/**
 * @author liudong Email :liudong@easier.cn
 * @desc
 * @date 2018/1/30.
 */

public class XiriVoiceVodUtil {

    private static final String TAG = XiriVoiceVodUtil.class.getName();
    /**
     * 语音遥控action
     */
    private static final String ACTION_PLAY = "PLAY";//播放
    private static final String ACTION_RESUME = "RESUME";//继续播放
    private static final String ACTION_RESTART = "RESTART";//重新播放
    private static final String ACTION_PAUSE = "PAUSE";//暂停
    private static final String ACTION_FORWARD = "FORWARD";//快进
    private static final String ACTION_BACKWARD = "BACKWARD";//快退
    private static final String ACTION_SEEK = "SEEK";//指定位置播放
    private static final String ACTION_NEXT = "NEXT";//下一集
    private static final String ACTION_PREV = "PREV";//上一集
    private static final String ACTION_INDEX = "INDEX";//指定集号

    private static final String KEY_OFFSET = "offset";
    private static final String KEY_INDEX = "index";
    private static final String KEY_POSITION = "position";
    private static final String ACTION_HISTORY = "HISTORY";
    private static final String ACTION_LAST_EPISODE = "LAST_EPISODE";

    private VoiceVodListener mListener;

    private Context mContext;

    private Feedback mFeedback;

    private Scene focusScene;


    public XiriVoiceVodUtil(Context mContext, VoiceVodListener mVoiceVodListener, Feedback mFeedback) {
        this.mContext = mContext;
        this.mListener = mVoiceVodListener;
        this.mFeedback = mFeedback;
    }


    /**
     * 开启语音遥控的接收
     */
    public void startXiri() {
        SuperLog.info2SD(TAG,"XiriVoiceVodUtil startXiri");
        focusScene = new Scene(OTTApplication.getContext());
        focusScene.init(new ISceneListener() {
            @Override
            public String onQuery() {
                SuperLog.debug(TAG, "onQuery");
                return "{\"_scene\":\"com.pukka.ydepg.VOD\",\"_commands\":{\"key1\":[\"退出\",\"退出播放\",\"返回\"],\"key2\":[$P(_PLAY), \"播放\", \"继续播放\"],\"key3\":[$P(_EPISODE)],\"key4\":[\"播放记录\", \"观看记录\"],\"key5\":[\"最后一集\"]}}";
            }

            @Override
            public void onExecute(Intent intent) {
                RefreshManager.getInstance().getScreenPresenter().exit();
                mFeedback.begin(intent);
                SuperLog.debug(TAG, "onExecute" + intent);
                Bundle bundle = intent.getExtras();
                if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals("com.pukka.ydepg.VOD")) {
                    if (intent.hasExtra("_command")) {
                        String command = intent.getStringExtra("_command");
                        if ("key1".equals(command)) {
                            //首页返回会报错，屏蔽掉
                            if (OTTApplication.getContext().getCurrentActivity() instanceof MainActivity) {
                                return;
                            }
                            mListener.finish();
                            //去掉返回语音之后的提示
//                            mFeedback.feedback("返回", Feedback.EXECUTION);
                            mFeedback.feedback("", Feedback.EXECUTION);
                        } else if ("key2".equals(command)) {
                            doPlayAction(intent.getExtras());
                        } else if ("key3".equals(command)) {
                            doEpisodeAction(intent.getExtras());
                        } else if ("key4".equals(command)) {
                            doSkipHistory(intent.getExtras());
                        } else if ("key5".equals(command)) {
                            mListener.playLastEpisode();
                            mFeedback.feedback("最后一集", Feedback.EXECUTION);
                        }
                    }
                }
            }
        });
    }


    public void doSkipHistory(Bundle bundle) {
        String action = bundle.getString("_action");
        SuperLog.debug(TAG, "doSkipHistory:" + action);
        if (TextUtils.isEmpty(action)) {
            String rawtext = bundle.getString("_rawtext");
            SuperLog.debug(TAG, "doPlayRawtext:" + rawtext);
            if (rawtext.equals("观看记录") || rawtext.equals("播放记录")) {
                mListener.doSkipHistory();
                mFeedback.feedback("播放记录", Feedback.EXECUTION);
                //TODO 跳转记录
                Intent intent = new Intent(mContext, NewMyMovieActivity.class);
                intent.putExtra("id", "1");
                mContext.startActivity(intent);
            }
            return;
        }

    }

    /**
     * 处理语音遥控"PLAY"槽下对应的播放action
     *
     * @param bundle
     */
    public void doPlayAction(Bundle bundle) {
        String action = bundle.getString("_action");
        SuperLog.debug(TAG, "doPlayAction:" + action);
        if (TextUtils.isEmpty(action)) {
            String rawtext = bundle.getString("_rawtext");
            SuperLog.debug(TAG, "doPlayRawtext:" + rawtext);
            if (rawtext.equals("播放") || rawtext.equals("继续播放")) {
                //TODO 播放
                mListener.play();
                mFeedback.feedback("执行播放", Feedback.EXECUTION);
            }
            return;
        }
        if (action.equals(ACTION_PLAY) || action.equals(ACTION_RESUME)) {
            //TODO 播放
            mListener.play();
            mFeedback.feedback("执行播放", Feedback.EXECUTION);
        } else if (action.equals(ACTION_RESTART)) {
            //TODO 重新播放
            mListener.rePlay();
            mFeedback.feedback("重新播放", Feedback.EXECUTION);
        } else if (action.equals(ACTION_PAUSE)) {
            //TODO 暂停
            mListener.pause();
            mFeedback.feedback("执行暂停", Feedback.EXECUTION);
        } else if (action.equals(ACTION_FORWARD)) {
            //TODO 快进
            mListener.forWard(bundle.getInt(KEY_OFFSET));
            mFeedback.feedback("执行快进", Feedback.EXECUTION);
        } else if (action.equals(ACTION_BACKWARD)) {
            //TODO 快退
            mListener.backForward(bundle.getInt(KEY_OFFSET));
            mFeedback.feedback("执行快退", Feedback.EXECUTION);
        } else if (action.equals(ACTION_SEEK)) {
            //TODO 从指定位置播放
            mListener.seekTo(bundle.getInt(KEY_POSITION));
            mFeedback.feedback("播放指定时间", Feedback.EXECUTION);
        }
    }

    /**
     * 处理语音遥控"EPISODE"槽下对应的选集action
     *
     * @param bundle
     */
    public void doEpisodeAction(Bundle bundle) {
        String action = bundle.getString("_action");
        if (TextUtils.isEmpty(action)) {
            return;
        }
        if (action.equals(ACTION_NEXT)) {//下一集
            mListener.nextPlay();
            mFeedback.feedback("播放下一集", Feedback.EXECUTION);
        } else if (action.equals(ACTION_PREV)) {//上一级
            mListener.prevPlay();
            mFeedback.feedback("播放上一集", Feedback.EXECUTION);
        } else if (action.equals(ACTION_INDEX)) {//播放指定集号
            mListener.indexPlay(bundle.getInt(KEY_INDEX));
            mFeedback.feedback("播放指定集数", Feedback.EXECUTION);
        }
    }

    /**
     * 关闭语音遥控接收
     */
    public void stopXiri() {
        if (null != focusScene) {
            SuperLog.info2SD(TAG,"XiriVoiceVodUtil stopXiri");
            try {
                focusScene.release();
            }catch (IllegalArgumentException e)
            {
                SuperLog.info2SD(TAG,"focusScene exception");
                e.getMessage();
            }

        }
    }


}
