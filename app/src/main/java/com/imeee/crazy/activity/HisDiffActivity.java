package com.imeee.crazy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.imeee.crazy.BaseApp;
import com.imeee.crazy.R;
import com.imeee.crazy.adapter.HistoryPagerAdapter;
import com.imeee.crazy.dao.DiffList;
import com.imeee.crazy.dao.RecordDiffer;
import com.imeee.crazy.enity.RankDiffer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zoey on 2/2/17.
 */
public class HisDiffActivity extends AppCompatActivity {
    public static final String INTENT_KEY_DIFFERID = "differid";
    /*
    public static final String INTENT_KEY_DATE = "date";
    public static final String INTENT_KEY_WEEKDIFFID = "weekid";
    public static final String INTENT_KEY_TOTALDIFFID = "totalid";
    */

    private static final String TAG = "HomeActivityViewPager()";

    RankDiffer rankDiffer = null;

    ViewPager pager = null;
    HistoryPagerAdapter historyPagerAdapter = null;
    TabLayout tabLayout = null;
    TextView tvReferenceTime = null;
    TextView tvUpdateTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* get intent data */
        Intent intent = getIntent();
        if (intent != null){
            long differId = intent.getLongExtra(INTENT_KEY_DIFFERID, -1);

            rankDiffer = ((BaseApp)getApplication()).loadDiffList(differId);
        } else {
            DiffList emptyDiffList = new DiffList();
            emptyDiffList.setDiffList(new ArrayList<RecordDiffer>());
            rankDiffer = new RankDiffer(emptyDiffList,emptyDiffList);
        }

        initView();
        initData();
        initViewPager();
    }

    private void initData() {
        initViewPager();

        Date referenceDate = null;
        Date updateDate = null;

        if(rankDiffer != null) {
            referenceDate = rankDiffer.getReferenceTime();
            updateDate = rankDiffer.getUpdateTime();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd  HH:mm");
            tvReferenceTime.setText(sdf.format(referenceDate));
            tvUpdateTime.setText(sdf.format(updateDate));
        }

    }

    private void initView() {

        pager = (ViewPager) this.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) this.findViewById(R.id.tl_tab);
        tvReferenceTime = (TextView) this.findViewById(R.id.tv_reference_date_topbar);
        tvUpdateTime = (TextView) this.findViewById(R.id.tv_update_date_topbar);
    }

    public RankDiffer getRankDiffer(){
        return this.rankDiffer;
    }

    private void initViewPager() {

        historyPagerAdapter = HistoryPagerAdapter.instantiate(this, getSupportFragmentManager());
        pager.setAdapter(historyPagerAdapter);

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

}