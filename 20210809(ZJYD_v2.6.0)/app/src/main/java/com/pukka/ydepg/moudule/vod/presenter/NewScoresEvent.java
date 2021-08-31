package com.pukka.ydepg.moudule.vod.presenter;

import java.util.List;

/**
 * Created by liudo on 2018/3/1.
 */

public class NewScoresEvent {

    private List<Float> newScores;

    public NewScoresEvent(List<Float> newScores){
        this.newScores=newScores;
    }

    public List<Float> getNewScores() {
        return newScores;
    }


}
