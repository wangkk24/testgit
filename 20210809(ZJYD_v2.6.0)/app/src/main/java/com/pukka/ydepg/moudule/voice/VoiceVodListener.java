package com.pukka.ydepg.moudule.voice;

/**
 * @author liudong Email :liudong@easier.cn
 * @desc
 * @date 2018/1/30.
 */
public interface VoiceVodListener {

    void play();

    void pause();

    void forWard(long time);

    void backForward(long time);

    void finish();

    void nextPlay();

    void prevPlay();

    void indexPlay(int index);

    void seekTo(int position);

    void rePlay();

    void doSkipHistory();

    void playLastEpisode();
}
