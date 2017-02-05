package com.imeee.crazy.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by zoey on 2/1/17.
 */

@Entity
public class DiffList {

    @Id(autoincrement = true)
    private Long id;

    private boolean isChg;

    @ToMany(referencedJoinProperty = "diffListId")
    @OrderBy("rank ASC")
    private List<RecordDiffer> diffList;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1523674974)
    private transient DiffListDao myDao;

    @Generated(hash = 1085107812)
    public DiffList(Long id, boolean isChg) {
        this.id = id;
        this.isChg = isChg;
    }

    @Generated(hash = 1709271252)
    public DiffList() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsChg() {
        return this.isChg;
    }

    public void setIsChg(boolean isChg) {
        this.isChg = isChg;
    }

    public void setDiffList(List<RecordDiffer> diffList){
        this.diffList = diffList;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1631354478)
    public List<RecordDiffer> getDiffList() {
        if (diffList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RecordDifferDao targetDao = daoSession.getRecordDifferDao();
            List<RecordDiffer> diffListNew = targetDao._queryDiffList_DiffList(id);
            synchronized (this) {
                if (diffList == null) {
                    diffList = diffListNew;
                }
            }
        }
        return diffList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1991995524)
    public synchronized void resetDiffList() {
        diffList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 633053688)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDiffListDao() : null;
    }
}
