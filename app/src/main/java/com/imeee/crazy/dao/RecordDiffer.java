package com.imeee.crazy.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zoey on 2/1/17.
 */

@Entity
public class RecordDiffer {

    @Id(autoincrement = true)
    private Long id;

    private long diffListId;

    private String name;

    private int rank;

    private int score;

    private int rankChg;

    private int scoreChg;

    @Generated(hash = 225855250)
    public RecordDiffer(Long id, long diffListId, String name, int rank, int score,
            int rankChg, int scoreChg) {
        this.id = id;
        this.diffListId = diffListId;
        this.name = name;
        this.rank = rank;
        this.score = score;
        this.rankChg = rankChg;
        this.scoreChg = scoreChg;
    }

    @Generated(hash = 758231639)
    public RecordDiffer() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDiffListId() {
        return this.diffListId;
    }

    public void setDiffListId(long diffListId) {
        this.diffListId = diffListId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRankChg() {
        return this.rankChg;
    }

    public void setRankChg(int rankChg) {
        this.rankChg = rankChg;
    }

    public int getScoreChg() {
        return this.scoreChg;
    }

    public void setScoreChg(int scoreChg) {
        this.scoreChg = scoreChg;
    }

    // getters and setters for id and user ...

}
