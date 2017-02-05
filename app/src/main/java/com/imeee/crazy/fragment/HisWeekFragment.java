package com.imeee.crazy.fragment;

/**
 * Created by zoey on 2/2/17.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.imeee.crazy.R;
import com.imeee.crazy.activity.HisDiffActivity;
import com.imeee.crazy.adapter.RankAdapter;


/**
 * Created by Administrator on 2017/1/25.
 */

public class HisWeekFragment extends Fragment {

    View view = null;
    ListView lvWeek = null;

    RankAdapter mRankAdapter = null;

    public HisWeekFragment(){
    }

    public static HisWeekFragment newInstance() {
        HisWeekFragment fragment = new HisWeekFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_history_week, container, false);

        initView();
        initData();
        initLogic();

        return view;
    }

    private void initView(){
        lvWeek = (ListView) view.findViewById(R.id.lv_history_week);
    }

    private void initData(){
        mRankAdapter = new RankAdapter(getContext(),
                ((HisDiffActivity)getActivity()).getRankDiffer().getWeekDiffer().getDiffList());

    }

    public void initLogic(){

        lvWeek.setAdapter(mRankAdapter);

    }

}
