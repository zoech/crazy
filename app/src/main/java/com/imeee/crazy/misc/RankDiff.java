package com.imeee.crazy.misc;

import com.imeee.crazy.dao.DiffList;
import com.imeee.crazy.dao.RecordDiffer;
import com.imeee.crazy.enity.Rank;
import com.imeee.crazy.enity.RankDiffer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zoey on 1/29/17.
 */

public class RankDiff {

    public static RankDiffer rankDiff(Rank oldRank,
                                      Rank newRank){

        if(newRank == null) {
            return emptyRankDiffer();
        }


        DiffList weekDiffer = null;
        DiffList totalDiffer = null;

        if(oldRank == null || oldRank.getWeekData().isEmpty()){
            weekDiffer = listDiff(null, newRank.getWeekData());
        } else {
            weekDiffer = listDiff(oldRank.getWeekData(),
                    newRank.getWeekData());
        }

        if(oldRank == null || oldRank.getAllData().isEmpty()){
            totalDiffer = listDiff(null, newRank.getAllData());
        } else {
            totalDiffer = listDiff(oldRank.getAllData(),
                    newRank.getAllData());
        }

        RankDiffer rankDiffer = new RankDiffer(weekDiffer, totalDiffer);
        rankDiffer.setUpdateTime(newRank.getDate());
        rankDiffer.setReferenceTime(oldRank.getDate());
        return rankDiffer;
    }


    public static DiffList listDiff(List<Rank.Record> oldRank,
                                    List<Rank.Record> newRank){
        //if(newRank == null || newRank.isEmpty()){
        if(newRank == null){
            //return null;
            newRank = new ArrayList();
        }
        DiffList diffList = new DiffList(null, false);
        List<RecordDiffer> differList = new ArrayList<>();

        boolean isChg = false;
        for (int rankNew = 0; rankNew < newRank.size(); ++rankNew){
            Rank.Record newRecord = newRank.get(rankNew);
            int rankOld = -1;
            if(oldRank != null) {
                for (int i = 0; i < oldRank.size(); ++i){

                    if(newRecord.getId() == oldRank.get(i).getId()){
                        rankOld = i;
                        break;
                    }
                } // end of old rank list's loop
            } // if (oldRank != null)


            String name = newRecord.getName();
            int score = newRecord.getScore();

            if(rankOld == -1){
                isChg = true;
                differList.add(new RecordDiffer(null, 0, name, rankNew, score, 2222, score));
            } else {
                Rank.Record oldRecord = oldRank.get(rankOld);

                int scoreChg = newRecord.getScore() - oldRecord.getScore();
                int rankChg = rankOld - rankNew;

                if(rankChg != 0 || scoreChg != 0){
                    isChg = true;
                }

                differList.add(new RecordDiffer(null, 0, name, rankNew, score, rankChg, scoreChg));
            }

        } // end of new rank list's loop

        diffList.setIsChg(isChg);
        diffList.setDiffList(differList);

        return diffList;
    }

    public static RankDiffer emptyRankDiffer(){
        DiffList emptyDiffList = new DiffList(null, false);
        emptyDiffList.setDiffList(new ArrayList<RecordDiffer>());

        RankDiffer rankDiffer = new RankDiffer(emptyDiffList, emptyDiffList);
        Date date = new Date();
        rankDiffer.setReferenceTime(date);
        rankDiffer.setUpdateTime(date);
        return rankDiffer;
    }
}
