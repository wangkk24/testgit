package com.pukka.ydepg.event;

import java.util.List;

public class PlayerSpeedChangeEvent {
    private int speed;
    private List<Float> speedList;
    public PlayerSpeedChangeEvent(int speed, List<Float> speedList){
        this.speed = speed;
        this.speedList = speedList;
    }

    public int getSpeed() {
        return speed;
    }

    public List<Float> getSpeedList() {
        return speedList;
    }
}
