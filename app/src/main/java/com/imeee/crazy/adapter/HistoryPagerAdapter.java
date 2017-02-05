package com.imeee.crazy.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.imeee.crazy.fragment.HisTotalFragment;
import com.imeee.crazy.fragment.HisWeekFragment;
import com.imeee.crazy.fragment.TotalFragment;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/1/25.
 */

public class HistoryPagerAdapter extends FragmentPagerAdapter {

    public static final int INDEX_WEEK = 0;
    public static final int INDEX_TOTAL = 1;

    public static final String TITLE_WEEK = "week";
    public static final String TITLE_TOTAL = "total";

    private Context context;
    private ArrayList<Fragment> fragments = null;
    private ArrayList<String> titles = null;

    private HistoryPagerAdapter(Context context,
                             FragmentManager fm){
        super(fm);

        this.context = context;
        titles = new ArrayList<>();
        fragments = new ArrayList<>();

        // add two fragment
        fragments.add(HisWeekFragment.newInstance());
        fragments.add(HisTotalFragment.newInstance());

        // set two title
        titles.add(TITLE_WEEK);
        titles.add(TITLE_TOTAL);
    }

    public static HistoryPagerAdapter instantiate(Context context,
                                               FragmentManager fm){

        return new HistoryPagerAdapter(context, fm);
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
