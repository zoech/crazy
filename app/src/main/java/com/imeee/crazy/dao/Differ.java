package com.imeee.crazy.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zoey on 2/1/17.
 */

@Entity
public class Differ {
    @Id(autoincrement = true)
    private Long id;

    private Date updateDate;

    private Date referenceDate;

    private boolean totalIsChg;

    private long weekDiffId;

    private long totalDiffId;

    @Generated(hash = 1726965526)
    public Differ(Long id, Date updateDate, Date referenceDate, boolean totalIsChg,
            long weekDiffId, long totalDiffId) {
        this.id = id;
        this.updateDate = updateDate;
        this.referenceDate = referenceDate;
        this.totalIsChg = totalIsChg;
        this.weekDiffId = weekDiffId;
        this.totalDiffId = totalDiffId;
    }

    @Generated(hash = 1314361566)
    public Differ() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public boolean getTotalIsChg() {
        return this.totalIsChg;
    }

    public void setTotalIsChg(boolean totalIsChg) {
        this.totalIsChg = totalIsChg;
    }

    public long getWeekDiffId() {
        return this.weekDiffId;
    }

    public void setWeekDiffId(long weekDiffId) {
        this.weekDiffId = weekDiffId;
    }

    public long getTotalDiffId() {
        return this.totalDiffId;
    }

    public void setTotalDiffId(long totalDiffId) {
        this.totalDiffId = totalDiffId;
    }

    public Date getReferenceDate() {
        return this.referenceDate;
    }

    public void setReferenceDate(Date referenceDate) {
        this.referenceDate = referenceDate;
    }
}
