package com.imeee.crazy.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imeee.crazy.BaseApp;
import com.imeee.crazy.adapter.HomePagerAdapter;
import com.imeee.crazy.R;
import com.imeee.crazy.api.ApiInterface;
import com.imeee.crazy.api.SecInfo;
import com.imeee.crazy.enity.Rank;
import com.imeee.crazy.enity.RankDiffer;
import com.imeee.crazy.fragment.RankHisFragment;
import com.imeee.crazy.fragment.TotalFragment;
import com.imeee.crazy.fragment.WeekFragment;
import com.imeee.crazy.misc.RankDiff;
import com.imeee.crazy.misc.ResHandle;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements WeekFragment.OnWeekFragmentListener,
                                                            TotalFragment.OnTotalFragmentListener,
                                                            RankHisFragment.OnHisFragmentListener{

    private static final String TAG = "HomeActivityViewPager()";

    private static HomeActivity instance = null;
    private BaseApp app = null;

    ViewPager pager = null;
    HomePagerAdapter homePagerAdapter = null;
    TabLayout tabLayout = null;
    TextView tvReferenceTime = null;
    TextView tvUpdateTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        instance = this;

        initView();
        initData();
        initViewPager();
        initListener();
    }

    private void initData() {
        app = (BaseApp) getApplication();
        initViewPager();

        notifyAllListUpdate();
    }

    private void initView() {

        pager = (ViewPager) this.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) this.findViewById(R.id.tl_tab);
        tvReferenceTime = (TextView) this.findViewById(R.id.tv_reference_date_topbar);
        tvUpdateTime = (TextView) this.findViewById(R.id.tv_update_date_topbar);
    }

    private void initListener(){

    }

    private void initViewPager() {

        homePagerAdapter = HomePagerAdapter.instantiate(this, getSupportFragmentManager());
        pager.setAdapter(homePagerAdapter);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
            @Override
            public void onPageScrollStateChanged(int arg0) {
                //Log.d(TAG, "--------changed:" + arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                Log.d(TAG, "-------scrolled arg0:" + arg0);
                Log.d(TAG, "-------scrolled arg1:" + arg1);
                Log.d(TAG, "-------scrolled arg2:" + arg2);
            }

            @Override
            public void onPageSelected(int pos) {
                Log.d(TAG, "------selected:" + pos);
            }
        });

        tabLayout.setupWithViewPager(pager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tabDownLine));
        tabLayout.setTabTextColors(getResources().getColor(R.color.whiteUnselectd),
                getResources().getColor(R.color.whiteSelectd));
    }

    @Override
    public void onWeekRank(){
        notifyAllListUpdate();
    }

    @Override
    public void onWeekRefresh(){
        getRank();
    }

    @Override
    public void onTotalRefresh(){
        getRank();
    }

    @Override
    public void onTotalRank(){
        notifyAllListUpdate();
    }

    @Override
    public void onHisRefresh(){
        //getRank();

        clearDbDilog();
    }

    @Override
    public void onHisNotify(){
        notifyAllListUpdate();
    }

    public void notifyAllListUpdate(){
        WeekFragment weekFragment = (WeekFragment) homePagerAdapter.getItem(0);
        TotalFragment totalFragment = (TotalFragment) homePagerAdapter.getItem(1);
        RankHisFragment hisFragment = (RankHisFragment) homePagerAdapter.getItem(2);

        if(weekFragment != null) {
            weekFragment.notifyListUpdate();
        }
        if(totalFragment != null) {
            totalFragment.notifyListUpdate();
        }
        if(hisFragment != null) {
            hisFragment.notifyListUpdate();
        }

        notifyUpdateTime();
    }

    public static HomeActivity getInstance(){
        return instance;
    }

    public void notifyUpdateTime(){
        Date referenceDate = null;
        Date updateDate = null;

        if(app == null){
            referenceDate = updateDate = new Date();
        } else {
            RankDiffer rankDiffer = app.getRankDiffer();
            if (rankDiffer == null) {
                referenceDate = updateDate = new Date();
            } else {
                referenceDate = rankDiffer.getReferenceTime();
                updateDate = rankDiffer.getUpdateTime();
            }
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        tvReferenceTime.setText(sdf.format(referenceDate));
        tvUpdateTime.setText(sdf.format(updateDate));
    }


    private void getRank(){

        ApiInterface i = app.getHttpClient().getApiInterface();

        Call<String> getRecipe = i.getRank("", SecInfo.PARAMS,SecInfo.ENCSECKEY);

        getRecipe.enqueue(new Callback<String>(){
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.body() == null || response.body().equals("")){
                    // no response body
                    alertDilog("network info", "null or empty response body");
                } else {

                    String jsonStr = response.body();
                    Log.i("+++", jsonStr);

                    JSONObject jobj = JSON.parseObject(jsonStr);

                    if(ResHandle.getResponseCode(jobj) == 200) {
                        // response ok

                        Rank newRank = ResHandle.parseRank(jobj);

                        if(app.getNewRank() != null) {
                            app.setOldRank(app.getNewRank());
                        }

                        RankDiffer rankDiffer = null;
                        if(app.getOldRank() == null){
                            rankDiffer = RankDiff.rankDiff(null,newRank);
                        } else {
                            rankDiffer = RankDiff.rankDiff(app.getOldRank(),newRank);
                        }

                        app.setNewRank(newRank);
                        app.setRankDiffer(rankDiffer);
                        // save the new rank to preference cache
                        app.getRankPref().setOldRank(JSON.toJSONString(newRank));

                        if(rankDiffer.getIsChg()){
                            app.saveDiffToDb(rankDiffer);
                            /*
                            RankHisFragment hisFragment = (RankHisFragment) homePagerAdapter.getItem(2);
                            if(hisFragment != null) {
                                hisFragment.notifyListUpdate();
                            }
                            */

                        }

                    } else { // code in response json is not 200
                        alertDilog("network info", "the code of return json is not 200");
                    }
                }

                notifyAllListUpdate();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("+++", t.toString());
                t.printStackTrace();

                alertDilog("network info", "network failed, check your network");

                notifyAllListUpdate();
            }
        });

    }


    private void alertDilog(String title, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        /*
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        */
        builder.show();
    }

    private void clearDbDilog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("database info");
        builder.setMessage("Are you sure to clear all database data?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                app.clearDatabase();
                app.getRankDiffHistory().clear();
                RankHisFragment hisFragment = (RankHisFragment) homePagerAdapter.getItem(2);
                if(hisFragment != null) {
                    hisFragment.notifyListUpdate();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                RankHisFragment hisFragment = (RankHisFragment) homePagerAdapter.getItem(2);
                if(hisFragment != null) {
                    hisFragment.notifyListUpdate();
                }
            }
        });
        builder.show();
    }
}
