package com.imeee.crazy.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zoey on 1/29/17.
 */

public class RankPref {
    private static final String PREFERNAME = "oldRankPreferences";

    private static final String KEY_OLDRANK = "oldRankJsonStr";

    private Context context = null;

    public RankPref(Context context){

        this.context = context;

        SharedPreferences preferences = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE);

        String oldRankJson = preferences.getString(KEY_OLDRANK, null);

    }

    public void setOldRank(String rankJsonStr){
        if(rankJsonStr != null && !rankJsonStr.isEmpty()) {

            SharedPreferences.Editor editor = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE).edit();
            editor.putString(KEY_OLDRANK, rankJsonStr).apply();

        }
    }

    public String getOldRank(){
        SharedPreferences preferences = context.getSharedPreferences(PREFERNAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_OLDRANK, null);
    }
}
