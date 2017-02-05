package com.imeee.crazy.misc;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imeee.crazy.enity.Rank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoey on 1/29/17.
 */

public class ResHandle {

    private static final String KEY_WEEKDATA = "weekData";
    private static final String KEY_ALLDATA = "allData";
    public static int getResponseCode(JSONObject job){
        return job.getIntValue("code");
    }

    public static Rank parseRank(JSONObject job){
        JSONArray allDataArray = getDataArray(job, KEY_ALLDATA);
        JSONArray weekDataArray = getDataArray(job, KEY_WEEKDATA);

        List<Rank.Record> allData = new ArrayList<>();
        List<Rank.Record> weekData = new ArrayList<>();

        for (int rank = 0; rank < allDataArray.size(); ++rank){
            int score = 0;
            int id = 0;
            String name = "undefined";

            JSONObject recordObj = allDataArray.getJSONObject(rank);
            JSONObject songObj = recordObj.getJSONObject("song");

            score = recordObj.getIntValue("score");
            id = songObj.getIntValue("id");
            name = songObj.getString("name");

            allData.add(new Rank.Record(name, id, score));
        }

        for (int rank = 0; rank < weekDataArray.size(); ++rank){
            int score = 0;
            int id = 0;
            String name = "undefined";

            JSONObject recordObj = weekDataArray.getJSONObject(rank);
            JSONObject songObj = recordObj.getJSONObject("song");

            score = recordObj.getIntValue("score");
            id = songObj.getIntValue("id");
            name = songObj.getString("name");

            weekData.add(new Rank.Record(name, id, score));
        }

        return new Rank(allData,weekData);

    }




    private static JSONArray getDataArray(JSONObject job, String dataKey){
        return job.getJSONArray(dataKey);
    }
}
