package com.imeee.crazy.enity;

import com.imeee.crazy.dao.DiffList;

import java.util.Date;

/**
 * Created by zoey on 2/2/17.
 */

public class RankDiffer {
    private DiffList weekDiffer;
    private DiffList totalDiffer;
    private boolean isChg;
    private Date updateTime;
    private Date referenceTime;

    public RankDiffer(){
        weekDiffer = null;
        totalDiffer = null;
        isChg = false;

        this.updateTime = referenceTime = null;
        //this.updateTime = new Date();
    }

    public RankDiffer(DiffList weekDiffer,
                      DiffList totalDiffer){
        this.weekDiffer = weekDiffer;
        this.totalDiffer = totalDiffer;

        this.isChg = this.weekDiffer.getIsChg() || this.totalDiffer.getIsChg();

        this.updateTime = referenceTime = null;
        //this.updateTime = new Date();
    }

    public void setWeekDiffer(DiffList weekDiffer){
        this.weekDiffer = weekDiffer;
    }

    public void setTotalDiffer(DiffList totalDiffer){
        this.totalDiffer = totalDiffer;
    }

    public DiffList getWeekDiffer(){
        return this.weekDiffer;
    }

    public DiffList getTotalDiffer(){
        return this.totalDiffer;
    }

    public boolean getIsChg(){
        return this.isChg;
    }

    public void setIsChg(boolean b){
        this.isChg = b;
    }

    public Date getUpdateTime(){
        return this.updateTime;
    }

    public void setUpdateTime(Date date){
        this.updateTime = date;
    }

    public Date getReferenceTime(){
        return this.referenceTime;
    }

    public void setReferenceTime(Date date){
        this.referenceTime = date;
    }
}
