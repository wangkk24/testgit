/*
 *Copyright (C) 2017 广州易杰科技, Inc.
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
package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: LockItem
 * @Package com.pukka.ydepg.common.http.v6bean.v6node
 * @date 2017/10/31 16:31
 */
public class LockItem
{
    private static final String TAG = LockItem.class.getSimpleName();
    @SerializedName("lockType")
    String lockType;

    public interface LockType
    {
        String CHANNEL = "CHANNEL";
        String SUBJECT = "SUBJECT";
        String VOD = "VOD";
        String PROGRAM = "PROGRAM";
        String VAS = "VAS";
        String SERIES = "SERIES";
        String GENRE = "GENRE";
    }

    @SerializedName("VOD")
    VOD vod;

    @SerializedName("channel")
    Channel channel;

    @SerializedName("subject")
    Subject subject;

    @SerializedName("playbill")
    Playbill playbill;

    @SerializedName("VAS")
    VAS vas;

    @SerializedName("genre")
    Genre genre;

    @SerializedName("lockSeries")
    LockSeries lockSeries;

    public String getLockType()
    {
        return lockType;
    }

    public void setLockType(String lockType)
    {
        this.lockType = lockType;
    }

    public VOD getVOD()
    {
        return vod;
    }

    public void setVOD(VOD vod)
    {
        this.vod = vod;
    }

    public Channel getChannel()
    {
        return channel;
    }

    public void setChannel(Channel channel)
    {
        this.channel = channel;
    }

    public Subject getSubject()
    {
        return subject;
    }

    public void setSubject(Subject subject)
    {
        this.subject = subject;
    }

    public Playbill getPlaybill()
    {
        return playbill;
    }

    public void setPlaybill(Playbill playbill)
    {
        this.playbill = playbill;
    }

    public VAS getVAS()
    {
        return vas;
    }

    public void setVAS(VAS vas)
    {
        this.vas = vas;
    }

    public Genre getGenre()
    {
        return genre;
    }

    public void setGenre(Genre genre)
    {
        this.genre = genre;
    }

    public LockSeries getLockSeries()
    {
        return lockSeries;
    }

    public void setLockSeries(LockSeries lockSeries)
    {
        this.lockSeries = lockSeries;
    }

    @Override
    public String toString()
    {
        return "LockItem{" +
                "lockType='" + lockType + '\'' +
                ", vod=" + vod +
                ", channel=" + channel +
                ", subject=" + subject +
                ", playbill=" + playbill +
                ", vas=" + vas +
                ", genre=" + genre +
                ", lockSeries=" + lockSeries +
                '}';
    }
}