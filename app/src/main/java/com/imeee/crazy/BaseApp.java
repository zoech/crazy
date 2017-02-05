package com.imeee.crazy;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.imeee.crazy.api.HttpClient;
import com.imeee.crazy.dao.DaoMaster;
import com.imeee.crazy.dao.DaoSession;
import com.imeee.crazy.dao.DiffList;
import com.imeee.crazy.dao.DiffListDao;
import com.imeee.crazy.dao.Differ;
import com.imeee.crazy.dao.DifferDao;
import com.imeee.crazy.dao.RecordDiffer;
import com.imeee.crazy.dao.RecordDifferDao;
import com.imeee.crazy.enity.Rank;
import com.imeee.crazy.enity.RankDiffer;
import com.imeee.crazy.misc.RankDiff;
import com.imeee.crazy.preference.RankPref;
import com.imeee.crazy.service.MonitorService;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zoey on 1/29/17.
 */

public class BaseApp extends Application {

    private Rank newRank = null;
    private Rank oldRank = null;
    private RankDiffer rankDiffer = null;
    private List<Differ> historyDiffLis = null;


    private HttpClient httpClient = null;

    private RankPref rankPref = null;

    private DaoSession daoSession = null;


    // MonitorService background's running interval
    private long monitorInterval;
    private long monitorIntervalNight;


    // use in notifyCation, as notify id;
    private int notifyId = 0;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        httpClient = new HttpClient();
        rankPref = new RankPref(getBaseContext());
        daoSession = null;

        newRank = null;
        oldRank = null;
        rankDiffer = RankDiff.emptyRankDiffer();

        // 10 minutes
        monitorInterval = 10 * 60 * 1000;

        //1 hour
        monitorIntervalNight = 60 * 60 * 1000;

        // initial daoSession
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"crazy-db");
        Database db = helper.getWritableDb();
        this.daoSession = new DaoMaster(db).newSession();


        // initial oldRank;
        loadLastRank();
        getRankDiffHistory();


        Intent intent = new Intent(this, MonitorService.class);
        startService(intent);
    }



    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
    }

    public HttpClient getHttpClient() { return this.httpClient; }

    public Rank getNewRank(){ return this.newRank; }

    public Rank getOldRank(){ return this.oldRank; }

    public void setNewRank(Rank rank){ this.newRank = rank; }

    public void setOldRank(Rank rank){ this.oldRank = rank; }

    public RankPref getRankPref(){ return this.rankPref; }

    public RankDiffer getRankDiffer(){ return this.rankDiffer; }

    public void setRankDiffer(RankDiffer rankDiffer) { this.rankDiffer = rankDiffer; }

    public long getMonitorInterval(){ return this.monitorInterval; }

    public long getMonitorIntervalNight() { return this.monitorIntervalNight; }

    public DaoSession getDaoSession(){
        if(this.daoSession == null){
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"crazy-db");
            Database db = helper.getWritableDb();
            this.daoSession = new DaoMaster(db).newSession();
        }

        return this.daoSession;
    }

    public Differ saveDiffToDb(RankDiffer rankDiffer){
        // save the diff to database;
        Log.i("+++++db", "save diff to data base");


        // save DiffList to db
        daoSession.getDiffListDao().insert(rankDiffer.getWeekDiffer());
        daoSession.getDiffListDao().insert(rankDiffer.getTotalDiffer());

        Log.i("++++db DiffList id", String.valueOf(rankDiffer.getWeekDiffer().getId()));
        Log.i("++++db DiffList id", String.valueOf(rankDiffer.getTotalDiffer().getId()));



        /****************************************************************************************/
        // create new Differ record and save to db
        //Differ differ = new Differ(null, new Date(), rankDiffer.getTotalDiffer().getIsChg(),
        //        rankDiffer.getWeekDiffer().getId(), rankDiffer.getTotalDiffer().getId());
        Differ differ = new Differ(null,    rankDiffer.getUpdateTime(),
                                            rankDiffer.getReferenceTime(),
                                            rankDiffer.getTotalDiffer().getIsChg(),
                                            rankDiffer.getWeekDiffer().getId(),
                                            rankDiffer.getTotalDiffer().getId());
        daoSession.getDifferDao().insert(differ);







        /************************************************************************************/

        // save RecordDiffer to db
        //DiffList weekDiffer = rankDiffer.getWeekDiffer();
        //DiffList totalDiffer = rankDiffer.getTotalDiffer();

        RecordDifferDao recordDifferDao = daoSession.getRecordDifferDao();
        List<RecordDiffer> weekDiffer = rankDiffer.getWeekDiffer().getDiffList();
        List<RecordDiffer> totalDiffer = rankDiffer.getTotalDiffer().getDiffList();
        for(int i = 0; i < weekDiffer.size(); ++i){
            RecordDiffer recordDiffer = weekDiffer.get(i);
            recordDiffer.setDiffListId(rankDiffer.getWeekDiffer().getId());
            recordDifferDao.insert(recordDiffer);
        }

        for(int i = 0; i < totalDiffer.size(); ++i){
            RecordDiffer recordDiffer = totalDiffer.get(i);
            recordDiffer.setDiffListId(rankDiffer.getTotalDiffer().getId());
            recordDifferDao.insert(recordDiffer);
        }


        Log.i("++++db RecordDiffer id", String.valueOf(rankDiffer.getWeekDiffer().getDiffList().get(4).getId()));



        /**************************************************************************************/
        // update historyDiffList, note if change to rx, only notify history fragment until differ is insert into db
        getRankDiffHistory().add(0,differ);

        return differ;
    }

    public List<Differ> getRankDiffHistory(){
        // return a list of rank diff history
        Log.i("+++++db", "load history from data base");

        if(this.historyDiffLis == null){
            // load history from db
            Query<Differ> differsQuery = daoSession.getDifferDao().queryBuilder().
                                            orderDesc(DifferDao.Properties.UpdateDate).build();
            List<Differ> history = differsQuery.list();
            if (history == null){
                history = new ArrayList<>();
            }

            this.historyDiffLis = history;
        }

        return this.historyDiffLis;
    }

    public RankDiffer loadDiffList(long differId){
        // load from db
        DiffListDao diffListDao = daoSession.getDiffListDao();
        DifferDao differDao = daoSession.getDifferDao();
        RecordDifferDao recordDifferDao= daoSession.getRecordDifferDao();

        RankDiffer rankDiffer = new RankDiffer(new DiffList(),new DiffList());


        Differ differ = differDao.load(differId);
        if(differ == null){
            DiffList emptyDiffList = new DiffList();
            emptyDiffList.setDiffList(new ArrayList<RecordDiffer>());
            rankDiffer.setWeekDiffer(emptyDiffList);
            rankDiffer.setTotalDiffer(emptyDiffList);
        } else {
            rankDiffer.setUpdateTime(differ.getUpdateDate());
            rankDiffer.setReferenceTime(differ.getReferenceDate());
            rankDiffer.setIsChg(true);

            DiffList weekDiffList = diffListDao.load(differ.getWeekDiffId());
            DiffList totalDiffList = diffListDao.load(differ.getTotalDiffId());

            if(weekDiffList != null){
                rankDiffer.setWeekDiffer(weekDiffList);
/*
                List<RecordDiffer> recordDiffers = recordDifferDao.queryBuilder()
                        .where(RecordDifferDao.Properties.DiffListId.eq(weekDiffList.getId()))
                        .orderAsc(RecordDifferDao.Properties.Rank)
                        .list();

                rankDiffer.getWeekDiffer().setDiffList(recordDiffers);
*/
            } else {
                DiffList emptyDiffList = new DiffList();
                emptyDiffList.setDiffList(new ArrayList<RecordDiffer>());
                rankDiffer.setWeekDiffer(emptyDiffList);
            }
            if(totalDiffList != null){
                rankDiffer.setTotalDiffer(totalDiffList);
/*
                List<RecordDiffer> recordDiffers = recordDifferDao.queryBuilder()
                        .where(RecordDifferDao.Properties.DiffListId.eq(totalDiffList.getId()))
                        .orderAsc(RecordDifferDao.Properties.Rank)
                        .list();

                rankDiffer.getTotalDiffer().setDiffList(recordDiffers);
*/
            } else {
                DiffList emptyDiffList = new DiffList();
                emptyDiffList.setDiffList(new ArrayList<RecordDiffer>());
                rankDiffer.setTotalDiffer(emptyDiffList);
            }
        }


        return rankDiffer;
    }

    public void clearDatabase(){
        DiffListDao diffListDao = daoSession.getDiffListDao();
        DifferDao differDao = daoSession.getDifferDao();
        RecordDifferDao recordDifferDao= daoSession.getRecordDifferDao();

        recordDifferDao.deleteAll();
        diffListDao.deleteAll();
        differDao.deleteAll();
    }

    private void loadLastRank(){
        String oldRankJson = this.rankPref.getOldRank();
        this.oldRank = null;
        if (oldRankJson != null && !oldRankJson.equals("")) {
            Log.i("+++Pref",oldRankJson);
            this.oldRank = JSON.parseObject(oldRankJson, Rank.class);
        } else {
            this.oldRank = getEmptyRank();
        }
    }

    public Rank getEmptyRank(){
        List<Rank.Record> emptyRecordList = new ArrayList<>();
        return new Rank(emptyRecordList, emptyRecordList);
    }

    public int getNotifyId(){
        return notifyId++;
    }

}
