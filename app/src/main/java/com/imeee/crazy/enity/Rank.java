package com.imeee.crazy.enity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zoey on 1/29/17.
 */

public class Rank {
    private List<Record> allData;
    private List<Record> weekData;
    private Date date;

    public Rank(){
        allData = new ArrayList<>();
        weekData = new ArrayList<>();
        this.date = new Date();
    }

    public Rank(List<Record> allData,
                List<Record> weekData){
        this.allData = allData;
        this.weekData = weekData;
        this.date = new Date();
    }

    public void setAllData(List<Record> allData){
        this.allData = allData;
    }

    public void setWeekData(List<Record> weekData){
        this.weekData = weekData;
    }

    public List<Record> getAllData(){
        return this.allData;
    }

    public List<Record> getWeekData(){
        return this.weekData;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public Date getDate(){
        return this.date;
    }


    public static class Record {
        private String name;
        private int id;
        private int score;

        public Record(){
            this.name = "undefined";
            this.id = 0;
            this.score = 0;
        }

        public Record(String name,
                      int id,
                      int score){
            this.name = name;
            this.id = id;
            this.score = score;
        }

        public void setName(String name){
            this.name = name;
        }

        public void setId(int id){
            this.id = id;
        }

        public void setScore(int score){
            this.score = score;
        }

        public String getName(){
            return this.name;
        }

        public int getId(){
            return this.id;
        }

        public int getScore(){
            return this.score;
        }
    }
}
