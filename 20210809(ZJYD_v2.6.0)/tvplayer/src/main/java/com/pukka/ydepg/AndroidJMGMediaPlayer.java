 /*
  *Copyright (C) 2018 广州易杰科技, Inc.
  *
  *Licensed under the Apache License, Version 2.0 (the "License");
  *you may not use this file except in compliance with the License.
  *You may obtain a copy of the License at
  *
  *  http://www.apache.org/licenses/LICENSE-2.0
  *
  *Unless required by applicable law or agreed to in writing, software
  *distributed under the License is distributed on an "AS IS" BASIS,
  *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  *See the License for the specific language governing permissions and
  *limitations under the License.
  */
 package com.pukka.ydepg;

 import android.content.Context;
 import android.graphics.Bitmap;
 import android.media.AudioManager;
 import android.media.MediaPlayer;
 import android.net.Uri;
 import android.os.Build;
 import android.os.Handler;
 import android.os.HandlerThread;
 import android.os.Looper;
 import android.os.Message;
 import android.os.Process;
 import android.text.TextUtils;
 import android.util.Log;
 import android.view.Gravity;
 import android.view.SurfaceHolder;
 import android.view.SurfaceView;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.FrameLayout;

 import com.pukka.ydepg.bean.PlayerConstant;
 import com.pukka.ydepg.inf.IPlayState;
 import com.pukka.ydepg.player.R;
 import com.pukka.ydepg.util.PlayUtil;
 import com.pukka.ydepg.util.PlayerEvent;
 import com.pukka.ydepg.view.PlayView;

 import org.greenrobot.eventbus.EventBus;

 import java.io.IOException;
 import java.lang.reflect.Method;

 /**
  * 基于Android原生SDK内置的MediaPlayer播放器的实现
  * <p>
  * 播放rtsp协议的时候mediaplayer的缓存回调onBufferingUpdate不能执行，http协议可以执行
  * <p>
  * version 2.0 变化:
  * mediaPlay相关操作在子线程中操作
  * <p>
  * version 3.0变化:
  * 增加fastReleasePrepare和fastPlayVideo，针对直播快速切台,快速起播
  *
  * @author fuqiang Email： fuqiang@easier.cn
  * @version : 3.0
  * @Title: AndroidMediaPlayer
  * @Package com.pukka.ydepg
  * @date 2018/02/07 09:37
  */
 public class AndroidJMGMediaPlayer extends AbstractMediaPlayer
         implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
         MediaPlayer.OnErrorListener, MediaPlayer.OnVideoSizeChangedListener,
         MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener, SurfaceHolder.Callback {

     /**
      * LOG TAG
      */
     private static final String TAG = "AndroidMediaPlayer";
     /**
      * 播放错误
      */
     private static final int STATE_ERROR = -1;
     /**
      * 播放未开始
      */
     private static final int STATE_IDLE = 0;

     /**
      * 播放准备中
      */
     private static final int STATE_PREPARING = 1;

     /**
      * 播放准备就绪
      */
     private static final int STATE_PREPARED = 2;

     /**
      * 播放中
      */
     private static final int STATE_PLAYING = 3;

     /**
      * 暂停播放
      */
     private static final int STATE_PAUSE = 4;

     /**
      * 缓冲
      */
     private static final int STATE_BUFFING = 5;

     /**
      * 当前是不是在播放,用于播放暂停状态
      */
     private volatile boolean isPlayState = false;

     /**
      * surface是否执行了surfaceDestory
      */
     private boolean isSurfaceDestory;

     /**
      * 播放总时长
      */
     private long mDuration = 0;
     /**
      * 播放广告总时长
      */
     private long advertDuration = 0;

     /**
      * 当前时长
      */
     private long mCurrentPosition = 0;

     /**
      * 广告MediaPlayer
      */
     private volatile MediaPlayer mAdvertMediaPlayer;

     /**
      * MediaPlayer
      */
     private volatile MediaPlayer mNextMediaPlayer;

     /**
      * Context
      */
     private Context mContext;

     /**
      * 广告播放url
      */
     private String mAdvertUrl;


     /**
      * 视频地址
      */
     private String mPlayUrl;

     /**
      * 当前播放状态
      */
     private int mCurrentState = STATE_IDLE;

     /**
      * 容纳SurfaceView的容器
      */
     private FrameLayout mContainerView;

     /**
      * 播放器视图
      */
     private SurfaceView mSurfaceView;
     /**
      * 是否是小窗口播放
      */
     private boolean isWindow = false;
     /**
      * 是否是新版详情页
      */
     private boolean isNewVoddetail = false;
     /**
      * holder
      */
     private SurfaceHolder mHolder;

     /**
      * 是不是已经执行完MediaPlayer的onPrepare回调
      */
     private volatile boolean isPrepare = false;

     /**
      * 该常量只针对直播切台有用,点播默认全是true
      */
     private boolean isFastPrepareVideo = true;

     /**
      * 当前是播放失败的release,直播场景:
      * 某个频道视频播放失败,切换到下一个频道,需要走startPlay
      */
     private boolean isErrorRelease = false;

     /**
      * 书签位置
      */
     private long mBookMarkValue = 0L;

     /**
      * 是不是第一次启动该URL的播放
      */
     private boolean isFirst = true;

     /**
      * isNormalStream
      */
     private boolean isNormalStream;

     /**
      * 是不是自适应视频
      */
     private boolean isAdaptive;

     /**
      * 默认视频窗口宽度
      */
     private int mSurfaceWidth;

     /**
      * 默认视频窗口高度
      */
     private int mSurfaceHeight;

     private AudioManager mAudioManager;

     /**
      * 初始化mediaPlay
      */
     private static final int MSG_INIT = 0x110;


     /**
      * mediaplay.prepare
      */
     private static final int MSG_PREPARE = 0x111;

     /**
      * mediaPlay.release
      */
     private static final int MSG_RELEASE = 0x112;

     /**
      * mediaPlay快速起播释放准备
      */
     private static final int MSG_FAST_RELEASE_PREPARE = 0x113;

     /**
      * mediaPlay快速起播
      */
     private static final int MSG_FAST_PLAY = 0x114;

     /**
      * 子线程初始化mediaPlay
      */
     private static final int MSG_INIT_IO_THREAD = 0x115;

     /**
      * 是不是首次初始化surfaceView，建议长期维护该项目的人，把surfaceView放在xml中吧,自行处理
      */
     private boolean isFistInitSurfaceView;

     /**
      * 子线程处理耗时任务
      */
     private com.pukka.ydepg.AndroidJMGMediaPlayer.PlayHandler mPlayHandler;

     /**
      * 含looper的线程
      */
     private HandlerThread mHandlerThread;
     /**
      * 封面图片
      */
     private View mCoverView;

     /**
      * 播放类型
      */
     private int videoType = PlayUtil.VideoType.VOD;

     /**
      * 是否走了resumePlay，条件不允许，不能播放
      */
     private boolean isResumePlay;

     private int mMediaUnKnowErrorCount = 0;

     //保存视频宽高比
     private double videoRatio;

     private Handler mUIHandler = new Handler(Looper.getMainLooper());
     private boolean isFirstAdv;

     private void runOnUiThread(Runnable action) {
         mUIHandler.post(action);
     }

     private class PlayHandler extends Handler {
         PlayHandler(Looper looper) {
             super(looper);
         }

         @Override
         public void handleMessage(Message msg) {
             super.handleMessage(msg);
             if (msg.what == MSG_INIT) {
                 Log.d(TAG, "[KPI]>>>>>>>receiver>>>>MSG_INIT" + this + Thread.currentThread());
                 init();
             } else if (msg.what == MSG_PREPARE) {
                 Log.d(TAG, "[KPI]>>>>>>>receiver>>>>MSG_PREPARE" + this + Thread.currentThread());
                 prepare();
             } else if (msg.what == MSG_RELEASE) {
                 Log.d(TAG, "[KPI]>>>>>>>receiver>>>>MSG_RELEASE" + this + Thread.currentThread());
                 release();
             } else if (msg.what == MSG_FAST_RELEASE_PREPARE) {
                 Log.d(TAG, "[KPI]>>>>>>>receiver>>>>MSG_FAST_RELEASE_PREPARE" + this + Thread.currentThread());
                 isFastPrepareVideo = false;
                 isErrorRelease = false;
                 forceResetStatus();
             } else if (msg.what == MSG_FAST_PLAY) {
                 Log.d(TAG, "[KPI]>>>>>>>receiver>>>>MSG_FAST_PLAY,【MediaPlay isNull?=" + (null == mNextMediaPlayer) + "】" + this + Thread.currentThread());
                 String playUrl = (String) msg.obj;
                 if (null != mNextMediaPlayer && !isErrorRelease) {
                     isFirst = true;
                     mPlayUrl = playUrl;
                     isAutoPlay = true;
                     isSurfaceDestory = false;
                     setState(STATE_PREPARING);
                     prepare();
                 } else {
                     isFastPrepareVideo = true;
                     startPlay(playUrl);
                 }
             } else if (msg.what == MSG_INIT_IO_THREAD) {
                 initContentPlayer();
             }
         }
     }

     public AndroidJMGMediaPlayer(Context context) {
         this.mContext = context;
         mSurfaceWidth = context.getResources().getDisplayMetrics().widthPixels;
         mSurfaceHeight = context.getResources().getDisplayMetrics().heightPixels;
         mAudioManager = (AudioManager) context.getApplicationContext().
                 getSystemService(Context.AUDIO_SERVICE);
         mHandlerThread = new HandlerThread(TAG, Process.THREAD_PRIORITY_MORE_FAVORABLE);
         mHandlerThread.start();
         mPlayHandler = new com.pukka.ydepg.AndroidJMGMediaPlayer.PlayHandler(mHandlerThread.getLooper());
     }

     /**
      * 初始化和reset前贴广告和播放器
      */
//     private void initAdvertAndContentPlayer() {
//         setState(STATE_BUFFING);
//         isFistInitSurfaceView = false;
//         isPlayState = isAutoPlay;
//         isResumePlay = false;
//         Log.d(TAG, "[initAdvertAndContentPlayer]->isPlayState:" + isPlayState);
//         if (null != mAudioManager) {
//             //获取音频焦点
//             try {
//                 mAudioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
//             } catch (Exception e) {
//                 Log.e(TAG, "[initMediaPlayer] >> mAudioManager.requestAudioFocus()", e);
//             }
//         }
//         initAdvertPlayer();
//
//     }
     private void initContentPlayer() {
         Log.d(TAG, "[initContentPlayer]: " + Thread.currentThread().getName());
         boolean isNullMediaPlay = false;
         if (null == mNextMediaPlayer) {
             mNextMediaPlayer = new MediaPlayer();
             isNullMediaPlay = true;
         }
         if (!isNullMediaPlay) {//不为空,此时是快速起播进来的,需要reset状态
             try {
                 //异常处理：防止因为异常状态导致程序崩溃。
                 if (null != mNextMediaPlayer && mNextMediaPlayer.isPlaying()) {
                     mNextMediaPlayer.pause();
                 }
             } catch (Exception e) {
                 Log.e(TAG, "[initMediaPlayer] >> mMediaPlayer.isPlaying()", e);
             }
         }
         if (isNullMediaPlay && null != mNextMediaPlayer) {
             try {
                 //设置声音效果
                 mNextMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
             } catch (Exception e) {
                 //异常处理：防止因为空指针导致程序崩溃。
                 release();
                 Log.e(TAG, "setAudioStreamType(AudioManager.STREAM_MUSIC):", e);
                 return;
             }

         }
         mNextMediaPlayer.setScreenOnWhilePlaying(true);
//        //设置媒体准备完成的监听
         mNextMediaPlayer.setOnPreparedListener(this);
//        //错误监听回调
         mNextMediaPlayer.setOnErrorListener(this);
         mNextMediaPlayer.setOnInfoListener(this);

         mNextMediaPlayer.setOnVideoSizeChangedListener(this);
         mNextMediaPlayer.setScreenOnWhilePlaying(true);
         //设置播放完成监听
         mNextMediaPlayer.setOnCompletionListener(this);
         //设置媒体准备完成的监听
         mNextMediaPlayer.setOnPreparedListener(this);
         Log.d(TAG, "prepareImpl mNextMediaPlayer");

         prepareImpl(mPlayUrl, mNextMediaPlayer, true);
         try {
             if (null != mAdvertMediaPlayer) {
                 mAdvertMediaPlayer.setNextMediaPlayer(mNextMediaPlayer);
             }
         } catch (Exception e) {
             Log.e(TAG, "setNextMediaPlayer fail");
         }

         Log.d(TAG, ">>>>>>>setListener success");
     }

     @Override
     public void switchAdvertToContentPlayer() {
         isCurrentPlayAdvert = false;
         if (mAdvertMediaPlayer == null || !isPlaying()) {
             return;
         }
         Log.d(TAG, "switchAdvertToContentPlayer");
         if (null != mAdvertMediaPlayer) {
             mAdvertMediaPlayer.setDisplay(null);
         }

         if (null != mNextMediaPlayer) {
             AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
             setState(STATE_PLAYING);
             mNextMediaPlayer.setDisplay(mHolder);
             mNextMediaPlayer.setVolume(audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM), audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
             Log.d(TAG, "switchAdvertToContentPlayer mNextMediaPlayer->setDisplay");
         }

         runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 //清除封面图
                 if (null != mCoverView && mCoverView.getVisibility() == View.VISIBLE) {
                     mCoverView.setVisibility(View.GONE);
                 }
             }
         });
         setState(STATE_PLAYING);
         if (isFirst) {
             isFirst = false;
             //跳转到书签位置
             Log.d(TAG, "switchAdvertToContentPlayer mNextMediaPlayer->mBookMarkValue:" + mBookMarkValue + "|mDuration:" + mDuration);
             if (mBookMarkValue > 0 && mDuration > 0) {
                 seekTo((int) mBookMarkValue);
             }
         }
         onPreparedCallBack(PlayUtil.VideoType.VOD);


     }

     public void onPreparedCallBack(final int videoType) {
         if (null != mPlayListener) {
             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     mPlayListener.onPrepared(videoType);
                 }
             });

         }
     }


     private void initAdvertPlayer() {
         Log.d(TAG, "[initAdvertPlayer] ");
         boolean isNullmAdvertPlay = false;
         if (null == mAdvertMediaPlayer) {
             mAdvertMediaPlayer = new MediaPlayer();
             isNullmAdvertPlay = true;
         }
         if (!isNullmAdvertPlay) {//不为空,此时是快速起播进来的,需要reset状态
             try {
                 //异常处理：防止因为异常状态导致程序崩溃。
                 if (null != mAdvertMediaPlayer && mAdvertMediaPlayer.isPlaying()) {
                     mAdvertMediaPlayer.pause();
                 }
                 mAdvertMediaPlayer.reset();
             } catch (Exception e) {
                 Log.e(TAG, "[initAdvertPlayer] >> mMediaPlayer.isPlaying()", e);
             }
         }
         if (isNullmAdvertPlay && null != mAdvertMediaPlayer) {
             try {
                 //设置声音效果
                 mAdvertMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
             } catch (Exception e) {
                 //异常处理：防止因为空指针导致程序崩溃。
                 release();
                 Log.e(TAG, "setAudioStreamType(AudioManager.STREAM_MUSIC):", e);
                 return;
             }
             mAdvertMediaPlayer.setScreenOnWhilePlaying(true);
             //设置播放完成监听
             Log.d(TAG, "mAdvertMediaPlayer [setOnCompletionListener]");
             mAdvertMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                 @Override
                 public void onCompletion(MediaPlayer mediaPlayer) {
                     //广告播放结束上报
                     if (mPlayListener != null) {
                         mPlayListener.onAdVideoEnd();
                     }
                     Log.d(TAG, "mAdvertMediaPlayer [onCompletion]");
                     isFirstAdv = false;
                     switchAdvertToContentPlayer();

                 }
             });
             //设置媒体准备完成的监听
             Log.d(TAG, "mAdvertMediaPlayer [setOnPreparedListener]");
             mAdvertMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                 @Override
                 public void onPrepared(MediaPlayer mediaPlayer) {
                     Log.d(TAG, "mAdvertMediaPlayer [onPrepared]");
                     setState(STATE_BUFFING);
                     isPrepare = true;
                     advertDuration = mediaPlayer.getDuration();
                     Log.d(TAG, "mAdvertMediaPlayer [onPrepared]:advertDuration:" + advertDuration);
                     //防止当前网速慢,置于后台了了,onPrepare成功了
                     Log.d(TAG, "[onPrepared]->isPlayState:" + isPlayState + "|isSurfaceDestory:" + isSurfaceDestory + "|duration:" + mediaPlayer.getDuration());
                     if (isPlayState && !isSurfaceDestory && isFastPrepareVideo) {
                         Log.d(TAG, "mAdvertMediaPlayer [start]");
                         //isFastPrepareVideo不对点播播放起作用,点播该常量都是true
                         setState(STATE_PLAYING);
                         onPreparedCallBack(PlayUtil.VideoType.ADVERT);
                         mAdvertMediaPlayer.start();
//                        isFirstAdv=false;
                     }
                 }
             });
             isNormalStream = true;
             if (TextUtils.isEmpty(mAdvertUrl)
                     || mAdvertUrl.startsWith("rtsp://", 0)
                     || mAdvertUrl.startsWith("http://", 0)
                     || mAdvertUrl.startsWith("playlist", 0)) {
                 isNormalStream = false;
                 //其他类型的地址不设置缓冲更新监听器
                 mAdvertMediaPlayer.setOnVideoSizeChangedListener(this);
             }
             //错误监听回调
             mAdvertMediaPlayer.setOnErrorListener(this);
             mAdvertMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                 @Override
                 public boolean onInfo(MediaPlayer mediaPlayer, int what, int i1) {
                     if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                         Log.d(TAG, "[onInfo]第一帧 what:" + what);
                         //播放器渲染第一帧
                         runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 //清除封面图
                                 if (null != mCoverView && mCoverView.getVisibility() == View.VISIBLE) {
                                     mCoverView.setVisibility(View.GONE);
                                 }
                             }
                         });
                         setState(STATE_PLAYING);
                     } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                         //加载中
                         setState(STATE_BUFFING);
                     } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                         //加载完成
                         setState(STATE_PREPARED);
                     }
                     return false;
                 }
             });
             Log.d(TAG, ">>>>>>>setListener success");
         }

     }

     /**
      * 初始化和reset播放器状态
      */
     private void initMediaPlayer() {
         setState(STATE_BUFFING);
         isFistInitSurfaceView = false;
         isPlayState = isAutoPlay;
         isResumePlay = false;
         Log.d(TAG, "[initMediaPlayer]->isPlayState:" + isPlayState);
         if (null != mAudioManager) {
             //获取音频焦点
             try {
                 mAudioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
             } catch (Exception e) {
                 Log.e(TAG, "[initMediaPlayer] >> mAudioManager.requestAudioFocus()", e);
             }
         }
         boolean isNullMediaPlay = false;
         if (null == mNextMediaPlayer) {
             mNextMediaPlayer = new MediaPlayer();
             isNullMediaPlay = true;
         }
         if (!isNullMediaPlay) {//不为空,此时是快速起播进来的,需要reset状态
             try {
                 //异常处理：防止因为异常状态导致程序崩溃。
                 if (null != mNextMediaPlayer && mNextMediaPlayer.isPlaying()) {
                     mNextMediaPlayer.pause();
                 }
             } catch (Exception e) {
                 Log.e(TAG, "[initMediaPlayer] >> mMediaPlayer.isPlaying()", e);
             }
         }
         if (isNullMediaPlay && null != mNextMediaPlayer) {
             try {
                 //设置声音效果
                 mNextMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
             } catch (Exception e) {
                 //异常处理：防止因为空指针导致程序崩溃。
                 release();
                 Log.e(TAG, "setAudioStreamType(AudioManager.STREAM_MUSIC):", e);
                 return;
             }
             mNextMediaPlayer.setScreenOnWhilePlaying(true);
             //设置播放完成监听
             mNextMediaPlayer.setOnCompletionListener(this);
             //设置媒体准备完成的监听
             mNextMediaPlayer.setOnPreparedListener(this);
             isNormalStream = true;
             if (TextUtils.isEmpty(mPlayUrl)
                     || mPlayUrl.startsWith("rtsp://", 0)
                     || mPlayUrl.startsWith("http://", 0)
                     || mPlayUrl.startsWith("playlist", 0)) {
                 isNormalStream = false;
                 //其他类型的地址不设置缓冲更新监听器
                 mNextMediaPlayer.setOnBufferingUpdateListener(this);
                 mNextMediaPlayer.setOnVideoSizeChangedListener(this);
             }
             //错误监听回调
             mNextMediaPlayer.setOnErrorListener(this);
             mNextMediaPlayer.setOnInfoListener(this);
             Log.d(TAG, ">>>>>>>setListener success");
         }
     }

     /**
      * 设置数据源
      *
      * @param uri Uri.parse(path)
      */
     private boolean setDataSource(Uri uri, MediaPlayer mediaPlayer) {
         Log.d(TAG, "[setDataSource]");
         boolean flag;
         try {
             //这是一个阻塞的方法.....
             if (null != mediaPlayer) {
                 mediaPlayer.reset();
                 mediaPlayer.setDataSource(mContext, uri, null);
             }
             flag = true;
         } catch (Exception e) {
             Log.e(TAG, "[setDataSource] Exception:", e);
             flag = false;
         }
         return flag;
     }

     /**
      * 初始化播放器
      *
      * @param context context
      */
     @Override
     void initPlayer(Context context) {
         //此处改为只初始化正片的，用来说明只需要一个player
         if (!TextUtils.isEmpty(mAdvertUrl)) {
             isCurrentPlayAdvert = true;
         }
         initMediaPlayer();
//        }
//        else {
//            isCurrentPlayAdvert = true;
//            initAdvertAndContentPlayer();
//        }
         //TODO这里需要去除null!=mAdvertMediaPlayer判断
         if (null != mNextMediaPlayer) {
             if (null == mSurfaceView) {
                 //初始化SurfaceView
                 mSurfaceView = new SurfaceView(mContext);
             }
             mHolder = mSurfaceView.getHolder();
             mHolder.addCallback(com.pukka.ydepg.AndroidJMGMediaPlayer.this);
             mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
             if (mContainerView.getChildCount() == 1) {
                 isFistInitSurfaceView = true;
                 FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                         ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                 mContainerView.addView(mSurfaceView, 0, params);
             }
             mSurfaceView.setZOrderMediaOverlay(true);
         }
     }

     /**
      * 设置当前播放器状态
      *
      * @param state 状态类型
      */
     private void setState(int state) {
         this.mCurrentState = state;
         if (state == STATE_PLAYING || state == STATE_PREPARED || (isPrepare && state == STATE_PAUSE)) {
             //播放成功
             mMediaUnKnowErrorCount = 0;
             refreshPlayState(IPlayState.PLAY_STATE_HASMEDIA);
         } else if (state == STATE_IDLE) {
             refreshPlayState(IPlayState.PALY_STETE_END);
         } else if (state == STATE_BUFFING || state == STATE_PREPARING) {
             refreshPlayState(IPlayState.PLAY_STATE_BUFFERING);
         } else {
             refreshPlayState(IPlayState.PLAY_STATE_NOMEDIA);
         }
     }

     @Override
     public void startPlay(String url, long bookmark) {
         Log.d(TAG, "[startPlay]2");
         mBookMarkValue = bookmark;
         isFirst = true;
         startPlay(url);
     }

     @Override
     public void startPlay(String url, String advertUrl, long bookmark) {
         Log.d(TAG, "[startPlay]3");
//         advertUrl = "http://vfx.mtime.cn:80/Video/2019/03/14/mp4/190314223540373995.mp4";
         mBookMarkValue = bookmark;
         if (!TextUtils.isEmpty(advertUrl)) {
             isFirstAdv = true;
         }
         isFirst = true;
         mAdvertUrl = advertUrl;
//        mAdvertUrl = "igmp://233.249.3.11:10086";
         Log.d(TAG, "[startPlay]3 mAdvertUrl: " + mAdvertUrl);
         startPlay(url);
     }


     @Override
     public void startPlay(String url) {
         // TODO: 2021/6/24 待测试
         mNextMediaPlayer=null;

         Log.d(TAG, "[startPlay]1");
         mPlayUrl = url;
         isErrorRelease = false;
         isFastPrepareVideo = true;
         isAutoPlay = true;
         isSurfaceDestory = false;
         Log.d(TAG, "[KPI]>>>>>>>send>>>>MSG_INIT");
         Message msg = mPlayHandler.obtainMessage();
         msg.what = MSG_INIT;
         mPlayHandler.sendMessage(msg);
     }


     /**
      * 重新播放
      */
     @Override
     public void rePlay() {
         isFirst = false;
         isFastPrepareVideo = true;
         if (!isErrorRelease) {
             releasePlayer();
         }
         if (!TextUtils.isEmpty(mPlayUrl)) {
             startPlay(mPlayUrl);
         }
     }

     /**
      * 恢复播放
      */
     @Override
     public void resumePlay() {
         Log.d(TAG, "[resumePlay]->isSurfaceDestory:" + isSurfaceDestory);
         if (isInPlaybackState() && !isPlayState && isPrepare && !isSurfaceDestory) {
             isPlayState = true;
             Log.d(TAG, "[resumePlay]->isPlayState:" + isPlayState);
             setState(STATE_PLAYING);
//             if (isCurrentPlayAdvert) {
//                 mAdvertMediaPlayer.start();
//             } else {
             mNextMediaPlayer.start();
//             }


         }
         isResumePlay = isSurfaceDestory;
     }

     /**
      * 暂停播放
      */
     @Override
     public void pausePlay() {
         if (isInPlaybackState() && mCurrentState != STATE_PAUSE) {
             isPlayState = false;
             isResumePlay = false;
             Log.d(TAG, "[pausePlay]->isPlayState:" + isPlayState);
             Log.d(TAG, "[pause]");
             setState(STATE_PAUSE);
//             if (isCurrentPlayAdvert) {
//                 mAdvertMediaPlayer.pause();
//             } else {
             mNextMediaPlayer.pause();
//             }

         }
     }

     /**
      * 播放暂停
      */
     @Override
     public void playerOrPause() {
         if (isPrepare) {
             if (isPlayState) {
                 pausePlay();
             } else {
                 resumePlay();
             }
         }
     }

     /**
      * 重置状态需要失去音频焦点
      */
     private void forceResetStatus() {
         Log.d(TAG, "[forceResetStatus]");
         setState(STATE_IDLE);
         isPrepare = false;
         mCurrentPosition = 0;
         isPlayState = isAutoPlay;
         if (videoType != PlayUtil.VideoType.TSTV) {
             mDuration = 0;
         }
         if (null != mNextMediaPlayer) {
             boolean isPauseSuccess = false;
             try {
                 if (mNextMediaPlayer.isPlaying()) {
                     Log.i(TAG, "[forceResetStatus]->pause");
                     mNextMediaPlayer.pause();
                     isPauseSuccess = true;
                 }
             } catch (Exception e) {
                 Log.i(TAG, ">>>>>MediaPlayer.pause()", e);
             } finally {
                 if (isPauseSuccess && null != mAudioManager) {
                     //失去音频焦点
                     mAudioManager.abandonAudioFocus(null);
                 }
             }
         }
     }

     /**
      * 释放播放器
      */
     @Override
     public void releasePlayer() {
         Log.d(TAG, "[releasePlayer]" + this);
         setState(STATE_IDLE);
         isPrepare = false;
         isResumePlay = false;
         mCurrentPosition = 0;
         if (videoType != PlayUtil.VideoType.TSTV) {
             mDuration = 0;
         }
         if (null != mCoverView) {
             mCoverView.setVisibility(View.VISIBLE);
         }

//         if (videoType == PlayUtil.VideoType.TSTV || videoType == PlayUtil.VideoType.TV) {
             if (null != mNextMediaPlayer) {
                 mNextMediaPlayer.setOnErrorListener(null);
                 mNextMediaPlayer.setOnPreparedListener(null);
                 mNextMediaPlayer.setOnBufferingUpdateListener(null);
                 mNextMediaPlayer.setOnInfoListener(null);
                 mNextMediaPlayer.setOnCompletionListener(null);
//             //E5盒子视频播放失败,此时点击遥控器设置按键,置于后台,点击返回按键，重新回到直播界面,出现ANR
//             //E5盒子在点播界面连续点击多次暂停播放，出现ANR
//             //只能注释掉，try catch第二个问题还存在
////            try {
////                mMediaPlayer.setDisplay(null);
////            } catch (Exception e) {
////                Log.i(TAG, "mediaPlay.setDisplay", e);
////            }
             }

             if (null != mContainerView && null != mSurfaceView) {
                 Log.d(TAG, "removeSurfaceView and mSurfaceView=null");
                 mContainerView.removeView(mSurfaceView);
                 mSurfaceView = null;
             }

             Log.d(TAG, "[KPI]>>>>>>>send>>>>MSG_RELEASE");
             Message msg = mPlayHandler.obtainMessage();
             msg.what = MSG_RELEASE;
             mPlayHandler.sendMessage(msg);
             if (null != mHolder) {
                 mHolder.removeCallback(com.pukka.ydepg.AndroidJMGMediaPlayer.this);
                 mHolder = null;

             }
             if (null != mPlayListener) {
                 mPlayListener.onRelease();
             }
             if (null != mAudioManager) {
                 //失去音频焦点
                 mAudioManager.abandonAudioFocus(null);
             }
//         }
     }

     /**
      * 消息队列中
      * 快速起播释放,重置mediaPlay
      */
     @Override
     public void fastReleasePrepare() {
         Log.d(TAG, "[KPI]>>>>>>>send>>>>MSG_FAST_RELEASE_PREPARE");
         Message msg = mPlayHandler.obtainMessage();
         msg.what = MSG_FAST_RELEASE_PREPARE;
         mPlayHandler.sendMessage(msg);
     }

     /**
      * 消息队列中,等待MSG_FAST_RELEASE_PREPARE执行完
      * 快速起播
      *
      * @param playUrl 播放地址
      */
     @Override
     public void fastPlayVideo(String playUrl) {
         Log.d(TAG, "[KPI]>>>>>>>send>>>>MSG_FAST_PLAY");
         Message message = mPlayHandler.obtainMessage();
         message.obj = playUrl;
         message.what = MSG_FAST_PLAY;
         mPlayHandler.sendMessage(message);
     }

     private boolean isInPlaybackState() {
         return (null != mNextMediaPlayer && (mCurrentState == STATE_PLAYING
                 || mCurrentState == STATE_PAUSE
                 || mCurrentState == STATE_BUFFING
                 || mCurrentState == STATE_PREPARED));
     }

     /**
      * 是不是正在播放
      */
     @Override
     public boolean isPlaying() {
         return isPlayState && mCurrentState != STATE_ERROR && mCurrentState != STATE_IDLE;
     }

     /**
      * 是不是处在暂停状态
      *
      * @return
      */
     @Override
     public boolean isPause() {
         return mCurrentState == STATE_PAUSE && !isPlayState;
     }

     @Override
     public boolean isTSTVPause() {
         return (mCurrentState == STATE_PAUSE || mCurrentState == STATE_ERROR) && !isPlayState;
     }

     @Override
     public void setIsWindow(boolean isLittle) {
         this.isWindow = isLittle;
     }

     @Override
     public void setIsNewDetail(boolean isNewDetail) {
         this.isNewVoddetail = isNewDetail;
     }

     @Override
     public void setVideoType(int videoType) {
         this.videoType = videoType;
     }


     @Override
     public void setArtwork(Bitmap bitmap) {

     }

     /**
      * @return 当前时长
      */
     @Override
     public long getCurrentPosition() {
         Log.d(TAG, "getCurrentPosition");
         if (isInPlaybackState() && getDuration() > 0) {
             try {
//                 if (isCurrentPlayAdvert) {
//                     mCurrentPosition = mAdvertMediaPlayer.getCurrentPosition();
//                 } else {
                 mCurrentPosition = mNextMediaPlayer.getCurrentPosition();
//                 }
                 Log.d(TAG, "getCurrentPosition->mCurrentPosition:" + mCurrentPosition);
             } catch (IllegalStateException e) {
                 Log.e(TAG, ">>>>>>>getCurrentPosition()", e);
             }
         }
         //Log.e(TAG,"getCurrentPosition->mCurrentPosition:"+mCurrentPosition);
         return mCurrentPosition;
     }

     /**
      * @return 总时长
      */
     @Override
     public long getDuration() {
         if (isCurrentPlayAdvert) {
             return advertDuration;
         }
         return mDuration;
     }

     /**
      * 指定某个位置进行播放
      *
      * @param progress 进度
      */
     @Override
     public void seekTo(long progress) {
         if (isCurrentPlayAdvert) {
             if (null != mNextMediaPlayer && getDuration() > 0) {
                 Log.d(TAG, "seekto:advertDuration:" + progress);
                 mNextMediaPlayer.seekTo((int) progress);
                 mNextMediaPlayer.setVolume(0, 0);
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         //清除封面图
                         if (null != mCoverView) {
                             mCoverView.setVisibility(View.VISIBLE);
                         }
                     }
                 });
                 setState(STATE_BUFFING);
             }
         } else {
             if (null != mNextMediaPlayer && getDuration() > 0) {
                 Log.d(TAG, "seekto:" + progress);
                 mNextMediaPlayer.seekTo((int) progress);
             }
         }

     }

     @Override
     public void setIptvlength(long duration) {
         this.mTSTVLength = duration;
     }

     @Override
     public void resetMediaPlayer() {
         mNextMediaPlayer=null;
     }

     /**
      * 设置surfaceView
      *
      * @param view surfaceView
      */
     @Override
     public void setSurface(View view) {
         mContainerView = (FrameLayout) view;
         mCoverView = mContainerView.findViewById(R.id.v_cover);
     }

     @Override
     public void setZOrderOnTop(boolean isOnTop) {

     }

     /**
      * 设置视频调整模式
      *
      * @param resizeMode resizeMode
      */
     @Override
     public void setResizeMode(String resizeMode) {
         isAdaptive = resizeMode.equals(PlayView.RESIZE_MODE_AUTO);
     }

     /**
      * 刷新监听器回调的视频播放状态
      *
      * @param playState playState
      */
     private void refreshPlayState(final int playState) {
         Log.i(TAG, "[refreshPlayState]:" + playState);
         runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 if (null != mPlayListener) {
                     mPlayListener.onPlayState(playState);
                 }
                 if (null != mUpdateProgressListener) {
                     mUpdateProgressListener.onPlayState(playState);
                 }
             }
         });
     }

     /**
      * 播放结束
      *
      * @param mediaPlayer mediaPlayer
      */
     @Override
     public void onCompletion(MediaPlayer mediaPlayer) {
         Log.d(TAG, "[onCompletion]");
         if (!TextUtils.isEmpty(mAdvertUrl)) {
             if (mPlayListener != null) {
                 mPlayListener.onAdVideoEnd();
             }
             Log.d(TAG, "mAdvertMediaPlayer [onCompletion]");
             isFirstAdv = false;
//             switchAdvertToContentPlayer();
             mAdvertUrl = null;
//             mNextMediaPlayer.stop();
//             mNextMediaPlayer.reset();
             release();
//             try {
//                 mNextMediaPlayer.prepare();
//                 prepareImpl(mPlayUrl, mNextMediaPlayer, isNormalStream);
//             mPlayUrl = "http://vfx.mtime.cn:80/Video/2019/03/14/mp4/190314223540373995.mp4";
             setDataSource(Uri.parse(mPlayUrl), mNextMediaPlayer);
             try {
                 mNextMediaPlayer.prepare();
             } catch (IOException e) {
                 e.printStackTrace();
             }
//             } catch (IOException e) {
//                 e.printStackTrace();
//             }
         } else {
             setState(STATE_IDLE);
             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     if (null != mPlayListener) {
                         mPlayListener.onPlayCompleted();
                     }
                 }
             });
         }

     }

     /**
      * 播放准备就绪
      *
      * @param mediaPlayer mediaPlayer
      */
     @Override
     public void onPrepared(MediaPlayer mediaPlayer) {
         if (!TextUtils.isEmpty(mAdvertUrl)) {
             Log.d(TAG, "mAdvertMediaPlayer [onPrepared]");
             setState(STATE_BUFFING);
             isPrepare = true;
             advertDuration = mediaPlayer.getDuration();
             Log.d(TAG, "mAdvertMediaPlayer [onPrepared]:advertDuration:" + advertDuration);
             //防止当前网速慢,置于后台了了,onPrepare成功了
             Log.d(TAG, "[onPrepared]->isPlayState:" + isPlayState + "|isSurfaceDestory:" + isSurfaceDestory + "|duration:" + mediaPlayer.getDuration());
             if (isPlayState && !isSurfaceDestory && isFastPrepareVideo) {
                 Log.d(TAG, "mAdvertMediaPlayer [start]");
                 //isFastPrepareVideo不对点播播放起作用,点播该常量都是true
                 setState(STATE_PLAYING);
                 onPreparedCallBack(PlayUtil.VideoType.ADVERT);
                 mNextMediaPlayer.start();
//                        isFirstAdv=false;
             }
         } else {
             isCurrentPlayAdvert = false;
             Log.d(TAG, "[onPrepared]");
             isPrepare = true;
             if (isFastPrepareVideo) {
                 try {
                     if (videoType == PlayUtil.VideoType.TSTV) {
                         mDuration = mTSTVLength;
                     } else {
                         mDuration = mediaPlayer.getDuration();
                     }

                 } catch (Exception e) {
                     Log.e(TAG, "mediaPlayer.getDuration:", e);
                 }
             }
             if (isCurrentPlayAdvert) {
                 return;
             }
             setState(STATE_BUFFING);
             //防止当前网速慢,置于后台了了,onPrepare成功了
             Log.d(TAG, "[onPrepared]->isPlayState:" + isPlayState + "|isSurfaceDestory:" + isSurfaceDestory + "|duration:" + mDuration);
             if (isPlayState && !isSurfaceDestory && isFastPrepareVideo) {
                 //isFastPrepareVideo不对点播播放起作用,点播该常量都是true
                 if (isFirst) {
                     isFirst = false;
                     //跳转到书签位置
                     if (mBookMarkValue > 0 && mDuration > 0) {
                         seekTo((int) mBookMarkValue);
                     }
                 }
                 Log.d(TAG, "[start]");
                 mNextMediaPlayer.start();
                 onPreparedCallBack(videoType);
             }
         }
     }

     /**
      * 播放出错
      */
     @Override
     public boolean onError(MediaPlayer mediaPlayer, final int what, int extra) {
         Log.e(TAG, "[onError]what:" + what + "|extra:" + extra + "|mCurrentState:" + mCurrentState);
         isErrorRelease = true;
         boolean isUnKnowError = false;
         //E900V21c盒子，快进时候，退出播放界面出现报错提示 100，频率很高，屏蔽掉暂停状态的报错
         if (mCurrentState != STATE_IDLE && mCurrentState != STATE_PAUSE) {
             setState(STATE_ERROR);
             final String errorStr;
             switch (what) {
                 case -1004:
                     Log.d(TAG, "MEDIA_ERROR_IO");
                     errorStr = "视频读写出错";
                     break;
                 case -1007:
                     Log.d(TAG, "MEDIA_ERROR_MALFORMED");
                     errorStr = "文件格式错误";
                     break;
                 case 200:
                     Log.d(TAG, "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK");
                     errorStr = "播放错误";
                     break;
//        case 100:
//          Log.d(TAG, "MEDIA_ERROR_SERVER_DIED");
//          errorStr = "服务器出错了";
//          break;
                 case -110:
                     Log.d(TAG, "MEDIA_ERROR_TIMED_OUT");
                     errorStr = "请求超时";
                     break;
                 case -1010:
                     Log.d(TAG, "MEDIA_ERROR_UNSUPPORTED");
                     errorStr = "视频源不支持";
                     break;
                 default:
                     Log.d(TAG, "MEDIA_ERROR_UNKNOWN");
                     isUnKnowError = true;
                     mMediaUnKnowErrorCount++;
                     errorStr = "";
                     if (!TextUtils.isEmpty(mAdvertUrl)) {
                         Log.d(TAG, "播放广告视频出错");
                         mAdvertUrl = null;
                         release();
                         if (null != mPlayListener) {
                             runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     mPlayListener.onPlayError(errorStr, what, PlayerConstant.PlayerType.ANDROIDMEDIAPLAYER);
                                     mPlayListener.onTryPlayForH5();
                                     releasePlayer();
                                     if (!TextUtils.isEmpty(mPlayUrl)) {
                                         fastPlayVideo(mPlayUrl);
                                     }
                                 }
                             });
                         }
                     }
                     //errorStr = "播放失败,请重试";
                     break;
             }
             if (null != mPlayListener && !TextUtils.isEmpty(errorStr)) {
                 final boolean finalIsUnKnowError = isUnKnowError;
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         mPlayListener.onPlayError(errorStr, what, PlayerConstant.PlayerType.ANDROIDMEDIAPLAYER);
                         mPlayListener.onTryPlayForH5();
                         releasePlayer();
                         Log.d(TAG, "isUnKnowError:" + finalIsUnKnowError + "PlayUrl is Empty:" + !TextUtils.isEmpty(mPlayUrl) + ",mMediaUnKnowErrorCount:" + mMediaUnKnowErrorCount);
                         if (finalIsUnKnowError && !TextUtils.isEmpty(mPlayUrl) && mMediaUnKnowErrorCount <= 3) {
                             fastPlayVideo(mPlayUrl);
                         }
                     }
                 });
             }
         }
         return true;
     }

     private boolean isAdvertState() {
         return null != mAdvertMediaPlayer && !TextUtils.isEmpty(mAdvertUrl) && isFirstAdv;
     }


     /**
      * surfaceview视图创建
      *
      * @param surfaceHolder surfaceHolder
      */
     @Override
     public void surfaceCreated(SurfaceHolder surfaceHolder) {
         Log.d(TAG, "[surfaceCreated] isPlayState:" + isPlayState);
         if (isPrepare) {
             if (isPlayState) {
                 setState(STATE_PLAYING);
             } else {
                 setState(STATE_PAUSE);
             }
         } else {
             setState(STATE_PREPARING);
         }
         isSurfaceDestory = false;
         try {
             Log.d(TAG, "surfaceCreated:setDisplay:" + surfaceHolder);
             if (null != surfaceHolder) {
//                 if (isAdvertState()) {
//                     Log.d(TAG, "mAdvertMediaPlayer:setDisplay");
//                     mAdvertMediaPlayer.setDisplay(surfaceHolder);
//                     Log.d(TAG, "mNextMediaPlayer:setDisplay");
//                 } else {
                 mNextMediaPlayer.setDisplay(surfaceHolder);
//                 }
             }
         } catch (Exception e) {
             Log.e(TAG, "[setDisplay] Exception:", e);
         }
         if (!isPrepare) {
             Log.d(TAG, "[KPI]>>>>>>>send>>>>MSG_PREPARE");
             Message msg = mPlayHandler.obtainMessage();
             msg.what = MSG_PREPARE;
             mPlayHandler.sendMessage(msg);
         } else {
             if (isInPlaybackState() && !isPlayState && isResumePlay) {
                 Log.d(TAG, "[surfaceCreated]->isPlayState:" + isPlayState);
                 isPlayState = true;
                 isResumePlay = false;
                 setState(STATE_PLAYING);
//                 if (isAdvertState()) {
//                     Log.d(TAG, "mAdvertMediaPlayer [start]");
//
//                     mAdvertMediaPlayer.start();
//                 } else {
                 mNextMediaPlayer.start();
//                 }
             }
         }
     }

     @Override
     public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
         Log.d(TAG, "[surfaceChanged]");
     }

     /**
      * 视图销毁
      *
      * @param surfaceHolder surfaceHolder
      */
     @Override
     public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
         // TODO: 2020/8/7 待测试
//        isSurfaceDestory = true;
         //E5盒子视频播放失败,此时点击遥控器设置按键,置于后台,点击返回按键，重新回到直播界面,出现ANR
         //E5盒子在点播界面连续点击多次暂停播放，出现ANR
         //只能注释掉，try catch第二个问题还存在
//        try {
//            if (null != mMediaPlayer) {
//                mMediaPlayer.setDisplay(null);
//            }
//        }catch (Exception e){
//            Log.e(TAG,">>>>>surfaceDestroyed:",e);
//        }
     }

     @Override
     public void onVideoSizeChanged(final MediaPlayer mp, int width, int height) {
         getDuration();
         Log.d(TAG, "[onVideoSizeChanged] isFirstAdv >> width:" + width + ",height:" + height + "---getDuration()=" + getDuration());
         Log.i(TAG, "[onVideoSizeChanged] isAdaptive " + isAdaptive + " isFirstAdv " + isFirstAdv + " null != mSurfaceView " + (null != mSurfaceView));
         if (isAdaptive && null != mSurfaceView && !isFirstAdv) {
             synchronized (MediaPlayer.class) {
                 //视频内容自适应
                 final int videoWidth = mp.getVideoWidth();
                 final int videoHeight = mp.getVideoHeight();
                 final double screenRatio = mSurfaceWidth * 1.0 / mSurfaceHeight;
                 final double videoRatio = videoWidth * 1.0 / videoHeight;
                 this.videoRatio = videoRatio;
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         if (null == mSurfaceView) {
                             return;
                         }
                         FrameLayout.LayoutParams layoutParams =
                                 (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
                         //全屏播放设置自适应宽高
                         if (!isWindow) {
                             if (screenRatio > videoRatio) {
                                 layoutParams.height = mSurfaceHeight;
                                 layoutParams.width = (int) (layoutParams.height * videoRatio);
                             } else {
                                 layoutParams.width = mSurfaceWidth;
                                 layoutParams.height = (int) (layoutParams.width / videoRatio);
                             }
                         } else if (isNewVoddetail) {
                             if (screenRatio <= videoRatio) {
                                 //新版详情页的处理
                                 if (windosState == 1) {
                                     //大屏状态
                                     layoutParams.width = mContext.getResources().getDisplayMetrics().widthPixels;
                                 } else if (windosState == 0) {
                                     //小屏状态
                                     if (mSurfaceWidth == 1280) {
                                         layoutParams.width = 561;
                                     } else if (mSurfaceWidth == 1920) {
                                         layoutParams.width = 841;
                                     }
                                 }
                                 layoutParams.height = (int) (layoutParams.width / videoRatio);
                             } else {
                                 //新版详情页的处理
                                 if (windosState == 1) {
                                     //大屏状态
                                     layoutParams.height = mContext.getResources().getDisplayMetrics().heightPixels;
                                 } else if (windosState == 0) {
                                     //小屏状态
                                     if (mSurfaceWidth == 1280) {
                                         layoutParams.height = 318;
                                     } else if (mSurfaceWidth == 1920) {
                                         layoutParams.height = 477;
                                     }
                                 }
                                 layoutParams.width = (int) (layoutParams.height * videoRatio);
                             }
                         }
                         layoutParams.gravity = Gravity.CENTER;
                         //将计算出的视频尺寸设置到surfaceView 让视频自动填充。
                         mSurfaceView.setLayoutParams(layoutParams);
                     }
                 });
             }

         }
     }

     @Override
     public boolean onInfo(MediaPlayer mp, int what, int extra) {
         Log.d(TAG, "[onInfo] what:" + what);
//         if (isCurrentPlayAdvert) {
//             return false;
//         }
         if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
             Log.d(TAG, "[onInfo]第一帧 what:" + what);
             //播放器渲染第一帧
             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     //清除封面图
                     if (null != mCoverView && mCoverView.getVisibility() == View.VISIBLE) {
                         mCoverView.setVisibility(View.GONE);
                     }
                 }
             });
             setState(STATE_PLAYING);
         } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
             //加载中
             setState(STATE_BUFFING);
         } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
             //加载完成
             setState(STATE_PREPARED);
         }
         return false;
     }

     @Override
     public void onBufferingUpdate(MediaPlayer mp, int percent) {
     }

     /**
      * 销毁释放
      */
     @Override
     public void onDestoryPlay() {
         Log.d(TAG, "onDestoryPlay()");
         if (null != mContainerView && null != mSurfaceView) {
             Log.d(TAG, "removeSurfaceView and mSurfaceView=null");
             mContainerView.removeView(mSurfaceView);
             mSurfaceView = null;
         }
         if (null != mPlayListener) {
             mPlayListener.onRelease();
         }
         Log.d(TAG, "[KPI]>>>>>>>send>>>>MSG_RELEASE");
         release();
         //activity销毁时，不会走release
//        Message msg=mPlayHandler.obtainMessage();
//        msg.what=MSG_RELEASE;
//        mPlayHandler.sendMessage(msg);
         if (null != mPlayHandler) {
             mPlayHandler.removeCallbacksAndMessages(null);
         }
         if (null != mHandlerThread) {
             mHandlerThread.quit();
         }
         mNextMediaPlayer = null;
     }

     /**
      * 初始化mediaPlay
      */
     private void init() {
         runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 initPlayer(mContext);

                 if (!isFistInitSurfaceView) {
                     surfaceCreated(mHolder);
                 }
             }
         });
     }


     /**
      * 异步setDataSource & prepare
      */
     private void prepare() {
         if (!isFastPrepareVideo && null != mNextMediaPlayer) {
             //当前是直播快速切台
             try {
                 mNextMediaPlayer.reset();
                 Log.d(TAG, "prepare->MediaPlayer.reset()");
             } catch (Throwable e) {
                 Log.e(TAG, ">>>>prepare()==>>【MediaPlayer.reset()】", e);
             }
         }
         if (!TextUtils.isEmpty(mAdvertUrl)) {
             Log.d(TAG, "prepareImpl mAdvertMediaPlayer");
             prepareImpl(mAdvertUrl, mNextMediaPlayer, isNormalStream);
//            Log.d(TAG, "[KPI]>>>>>>>send>>>>MSG_INIT_IO_THREAD");

//            Message msg = mPlayHandler.obtainMessage();
//            msg.what = MSG_INIT_IO_THREAD;
//            mPlayHandler.sendMessage(msg);

         } else {
             Log.d(TAG, "prepareImpl mNextMediaPlayer");
             prepareImpl(mPlayUrl, mNextMediaPlayer, isNormalStream);
         }
     }

     private void prepareImpl(String playurl, final MediaPlayer mediaPlayer, boolean isNormalStream) {
         Log.d(TAG, "prepareImpl");
         if (!TextUtils.isEmpty(playurl) && !setDataSource(Uri.parse(playurl), mediaPlayer)) {
             Log.d(TAG, "setDataSource error");
             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     onError(mediaPlayer, 0, 0);
                 }
             });
             return;
         }
         if (!isNormalStream) {
             try {
                 Log.d(TAG, "[prepareAsync]");
                 isFastPrepareVideo = true;
                 if (null != mediaPlayer) {
                     mediaPlayer.prepareAsync();
                 }
             } catch (Exception e) {
                 Log.e(TAG, "[prepareAsync] Exception:", e);
             }
         } else {
             try {
                 Log.d(TAG, "[prepare]");
                 isFastPrepareVideo = true;
                 if (null != mediaPlayer) {
                     mediaPlayer.prepare();
                 }
             } catch (Exception e) {
                 Log.e(TAG, "[prepare] Exception:", e);
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         onError(mediaPlayer, 0, 0);
                     }
                 });
             }
         }
     }


     /**
      * 异步释放mediaPlay
      */
     private void release() {
         Log.d(TAG, "[release] start");

         releaseImpl(mNextMediaPlayer);
//         if (videoType == PlayUtil.VideoType.TSTV || videoType == PlayUtil.VideoType.TV) {
//             mNextMediaPlayer = null;
//         }
//         releaseImpl(mAdvertMediaPlayer);
         isCurrentPlayAdvert = false;
//         mAdvertMediaPlayer = null;
         EventBus.getDefault().post(new PlayerEvent());
         Log.d(TAG, "[release] end");
     }

     private void releaseImpl(MediaPlayer mediaPlayer) {
         if (null != mediaPlayer) {
             try {
                 mediaPlayer.stop();
                 Log.d(TAG, "mediaPlay.stop()");
             } catch (Exception e) {
                 Log.i(TAG, "mediaPlay.stop", e);
             }
             try {
                 mediaPlayer.reset();
                 Log.d(TAG, "mediaPlay.reset()");
             } catch (Exception e) {
                 Log.i(TAG, "mediaPlay.reset", e);
             }
//             try {
//                 mediaPlayer.release();
//                 Log.d(TAG, "mediaPlay.release()");
//             } catch (Exception e) {
//                 Log.i(TAG, "mediaPlay.release", e);
//             }
         }
     }

     @Override
     public void reInitPlayHandler() {
         if (!mHandlerThread.isAlive()) {
             mHandlerThread = new HandlerThread(TAG, Process.THREAD_PRIORITY_MORE_FAVORABLE);
             mHandlerThread.start();
             mPlayHandler = new com.pukka.ydepg.AndroidJMGMediaPlayer.PlayHandler(mHandlerThread.getLooper());
         }
     }

     @Override
     public void setSpeed(float speed) {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             mNextMediaPlayer.setPlaybackParams(mNextMediaPlayer.getPlaybackParams().setSpeed(speed));
         } else {
             //TODO
             try {
                 Method method = MediaPlayer.class.getMethod("setSpeed", float.class);
                 method.invoke(mNextMediaPlayer, speed);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
     }

     @Override
     public boolean canSetSpeed(float speed) {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             return true;
         } else {
             //TODO
             try {
                 Method method = MediaPlayer.class.getMethod("setSpeed", float.class);
                 method.invoke(mNextMediaPlayer, speed);
                 Log.e("gwptest", "setspeed return ture");
                 return true;
             } catch (Exception e) {
                 e.printStackTrace();
                 return false;
             }
         }
     }

     @Override
     public void setNullPlayer() {
mNextMediaPlayer=null;
     }

     /*
      *由于新版详情页需要大小屏之间进行切换，自适应surface大小会导致拉伸问题
      */
     @Override
     public void setSurfaceViewSize(float width, float height) {
         Log.i(TAG, "onVideoSizeChanged: " + isWindow + mSurfaceWidth + " " + mSurfaceHeight);
         final double screenRatio = mSurfaceWidth * 1.0 / mSurfaceHeight;
         if (isAdaptive && null != mSurfaceView && !isFirstAdv) {
             if (videoRatio == 0) {
                 return;
             }
             FrameLayout.LayoutParams layoutParams =
                     (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();

             if (screenRatio <= videoRatio) {
                 //新版详情页的处理
                 if (windosState == 1) {
                     //大屏状态
                     layoutParams.width = mContext.getResources().getDisplayMetrics().widthPixels;
                 } else if (windosState == 0) {
                     //小屏状态
                     if (mSurfaceWidth == 1280) {
                         layoutParams.width = 561;
                     } else if (mSurfaceWidth == 1920) {
                         layoutParams.width = 841;
                     }
                 }
                 layoutParams.height = (int) (layoutParams.width / videoRatio);
             } else {
                 //新版详情页的处理
                 if (windosState == 1) {
                     //大屏状态
                     layoutParams.height = mContext.getResources().getDisplayMetrics().heightPixels;
                 } else if (windosState == 0) {
                     //小屏状态
                     if (mSurfaceWidth == 1280) {
                         layoutParams.height = 318;
                     } else if (mSurfaceWidth == 1920) {
                         layoutParams.height = 477;
                     }
                 }
                 layoutParams.width = (int) (layoutParams.height * videoRatio);
             }

//            if (isNewVoddetail){
//                //新版详情页的处理
//                layoutParams.width = (int) width;
//                layoutParams.height = (int) (layoutParams.width / videoRatio);
//            }
             //全屏播放设置自适应宽高
             layoutParams.gravity = Gravity.CENTER;
             //将计算出的视频尺寸设置到surfaceView 让视频自动填充。
             mSurfaceView.setLayoutParams(layoutParams);
         } else if (isAdaptive && null != mSurfaceView && isFirstAdv) {
             if (videoRatio == 0) {
                 return;
             }
             FrameLayout.LayoutParams layoutParams =
                     (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
             layoutParams.width = (int) width;
             layoutParams.height = (int) height;
             layoutParams.gravity = Gravity.CENTER;
             mSurfaceView.setLayoutParams(layoutParams);
         }


     }
 }