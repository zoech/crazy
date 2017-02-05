package com.imeee.crazy.enity;

/**
 * Created by zoey on 1/29/17.
 */

public class RecordDiffer {
    private String name;
    private int score;
    private int rankChg;
    private int scoreChg;

    public RecordDiffer(){
        name = "undefined";
        score = 0;
        rankChg = 0;
        scoreChg = 0;
    }

    public RecordDiffer(String name,
                        int score,
                        int rankChg,
                        int scoreChg){
        this.name = name;
        this.score = score;
        this.rankChg = rankChg;
        this.scoreChg = scoreChg;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setScore(int score){
        this.score = score;
    }

    public void setRankChg(int rankChg){
        this.rankChg = rankChg;
    }

    public void setScoreChg(int scoreChg){
        this.scoreChg = scoreChg;
    }

    public String getName(){
        return this.name;
    }

    public int getScore(){
        return this.score;
    }

    public int getRankChg(){
        return this.rankChg;
    }

    public int getScoreChg(){
        return this.scoreChg;
    }
}
