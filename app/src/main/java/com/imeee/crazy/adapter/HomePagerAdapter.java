package com.imeee.crazy.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.imeee.crazy.fragment.TotalFragment;
import com.imeee.crazy.fragment.WeekFragment;
import com.imeee.crazy.fragment.RankHisFragment;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/1/25.
 */

public class HomePagerAdapter extends FragmentPagerAdapter{

    public static final int INDEX_WEEK = 0;
    public static final int INDEX_TOTAL = 1;
    public static final int INDEX_HISTORY = 2;

    public static final String TITLE_WEEK = "week";
    public static final String TITLE_TOTAL = "total";
    public static final String TITLE_HISTORY = "history";

    private Context context;
    private ArrayList<Fragment> fragments = null;
    private ArrayList<String> titles = null;

    private HomePagerAdapter(Context context,
                             FragmentManager fm){
        super(fm);

        this.context = context;
        titles = new ArrayList<>();
        fragments = new ArrayList<>();
        /*
        titles.add(context.getString(R.string.tab_recipe));
        titles.add(context.getString(R.string.tab_order));
        titles.add(context.getString(R.string.tab_me));
        */

        // add two fragment
        fragments.add(WeekFragment.newInstance());
        fragments.add(TotalFragment.newInstance());
        fragments.add(RankHisFragment.newInstance());

        // set two title
        titles.add(TITLE_WEEK);
        titles.add(TITLE_TOTAL);
        titles.add(TITLE_HISTORY);
    }

    public static HomePagerAdapter instantiate(Context context,
                                               FragmentManager fm){

        return new HomePagerAdapter(context, fm);
    }


    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

}
