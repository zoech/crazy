package com.imeee.crazy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import com.imeee.crazy.BaseApp;
import com.imeee.crazy.R;
import com.imeee.crazy.adapter.RankAdapter;
import com.imeee.crazy.dao.RecordDiffer;
import com.imeee.crazy.enity.Rank;
import com.imeee.crazy.enity.RankDiffer;
import com.imeee.crazy.misc.RankDiff;
import com.imeee.crazy.preference.RankPref;

/**
 * Created by Administrator on 2017/1/25.
 */

public class TotalFragment extends Fragment {
    private OnTotalFragmentListener mListener;
    private BaseApp app;

    View view = null;
    PullToRefreshListView pullToRefreshListView = null;
    ListView lvTotal = null;

    RankAdapter mRankAdapter = null;
    //List<RecordDiffer> diffList = null;

    private RankPref rankPref = null;

    public TotalFragment(){

    }

    public static TotalFragment newInstance() {
        TotalFragment fragment = new TotalFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_rank_total, container, false);

        initView();
        initData();
        initLogic();
/*
        if (app.getRankDiffer() == null
                || app.getRankDiffer().getTotalDiffer() == null
                || app.getRankDiffer().getTotalDiffer().getDiffList().isEmpty()){

            if(app.getNewRank() == null || app.getNewRank().getAllData().isEmpty()) {

                Rank rankTmp = null;
                if(rankPref.getOldRank() == null || rankPref.getOldRank().equals("")){
                    Log.i("+++Pref","--------no pref!");
                } else {
                    Log.i("+++Pref",rankPref.getOldRank());
                    rankTmp = JSON.parseObject(rankPref.getOldRank(), Rank.class);
                }

                app.setNewRank(rankTmp);
                mListener.onTotalRefresh();
            } else {
                RankDiffer rankDiffer = RankDiff.rankDiff(app.getOldRank(), app.getNewRank());
                List<RecordDiffer> diffList = rankDiffer.getTotalDiffer().getDiffList();


                app.setRankDiffer(rankDiffer);
                mRankAdapter.setRankList(diffList);
                mRankAdapter.notifyDataSetChanged();
            }

        } else {
            List<RecordDiffer> diffList = app.getRankDiffer().getTotalDiffer().getDiffList();
            mRankAdapter.setRankList(diffList);
            mRankAdapter.notifyDataSetChanged();
        }
*/


        return view;
    }

    private void initView(){
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.refreshlv_total);
        lvTotal = pullToRefreshListView.getRefreshableView();
    }

    private void initData(){
        app = (BaseApp)getActivity().getApplication();
        List<RecordDiffer> diffList = app.getRankDiffer().getTotalDiffer().getDiffList();
        if(diffList == null){
            diffList = new ArrayList<>();
        }
        mRankAdapter = new RankAdapter(getContext(), diffList);
        rankPref = app.getRankPref();
    }

    public void initLogic(){

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        pullToRefreshListView.setAdapter(mRankAdapter);

        pullToRefreshListView.setOnRefreshListener(new RefreshListener());

    }



    private class RefreshListener implements PullToRefreshBase.OnRefreshListener2<ListView> {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            // refresh the record rank, obtain the new rank list from netease and show
            mListener.onTotalRefresh();

        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            // end the refresh animate
            mListener.onTotalRefresh();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTotalFragmentListener) {
            mListener = (OnTotalFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnTotalFragmentListener {
        // TODO: Update argument type and name
        // validate all fragments' list via total fragment get new rank
        void onTotalRank();

        void onTotalRefresh();

    }

    public void notifyListUpdate(){
        if(pullToRefreshListView != null && mRankAdapter != null && app != null) {
            List<RecordDiffer> diffList = app.getRankDiffer().getTotalDiffer().getDiffList();

            if(diffList != null){
                //mRankAdapter.setRankList(new ArrayList<RecordDiffer>());
                mRankAdapter.setRankList(diffList);
                mRankAdapter.notifyDataSetChanged();
            }

            pullToRefreshListView.onRefreshComplete();
        }
    }

}
