package com.imeee.crazy.fragment;

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
 * Created by zoey on 2/2/17.
 */

public class HisTotalFragment extends Fragment {

    View view = null;
    ListView lvTotal = null;

    RankAdapter mRankAdapter = null;

    public HisTotalFragment(){
    }

    public static HisTotalFragment newInstance() {
        HisTotalFragment fragment = new HisTotalFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_history_total, container, false);

        initView();
        initData();
        initLogic();

        return view;
    }

    private void initView(){
        lvTotal = (ListView) view.findViewById(R.id.lv_history_total);
    }

    private void initData(){
        mRankAdapter = new RankAdapter(getContext(),
                ((HisDiffActivity)getActivity()).getRankDiffer().getTotalDiffer().getDiffList());

    }

    public void initLogic(){

        lvTotal.setAdapter(mRankAdapter);

    }

}