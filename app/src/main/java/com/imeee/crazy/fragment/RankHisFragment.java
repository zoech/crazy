package com.imeee.crazy.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.imeee.crazy.BaseApp;
import com.imeee.crazy.R;
import com.imeee.crazy.activity.HisDiffActivity;
import com.imeee.crazy.adapter.RankDiffHisAdapter;
import com.imeee.crazy.dao.Differ;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoey on 2/2/17.
 *
 * Rank Diff History Fragment
 */

public class RankHisFragment extends Fragment {
    private OnHisFragmentListener mListener;
    private BaseApp app;

    View view = null;
    PullToRefreshListView pullToRefreshListView = null;
    ListView lvHistory = null;

    RankDiffHisAdapter mHistoryAdapter = null;

    public RankHisFragment(){

    }

    public static RankHisFragment newInstance() {
        RankHisFragment fragment = new RankHisFragment();

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_rank_history, container, false);

        initView();
        initData();
        initLogic();

        return view;
    }

    private void initView(){
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.refreshlv_history);
        lvHistory = pullToRefreshListView.getRefreshableView();
    }

    private void initData(){
        app = (BaseApp)getActivity().getApplication();
        List<Differ> hisList = app.getRankDiffHistory();
        if (hisList == null) {
            hisList = new ArrayList<>();
        }

        mHistoryAdapter = new RankDiffHisAdapter(getContext(), hisList);

    }

    public void initLogic(){

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        pullToRefreshListView.setAdapter(mHistoryAdapter);

        pullToRefreshListView.setOnRefreshListener(new RefreshListener());

        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH-mm");

                Differ differ = mHistoryAdapter.getItem(position);
                Intent intent = new Intent(getContext(), HisDiffActivity.class);
                intent.putExtra(HisDiffActivity.INTENT_KEY_DIFFERID, differ.getId());

                startActivity(intent);
            }
        });

    }


    private class RefreshListener implements PullToRefreshBase.OnRefreshListener2<ListView> {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            // refresh the record rank, obtain the new rank list from netease and show
            mListener.onHisRefresh();

        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            // end the refresh animate
            mListener.onHisRefresh();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHisFragmentListener) {
            mListener = (OnHisFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RankDiffFragment's listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnHisFragmentListener {
        // TODO: Update argument type and name
        // validate all fragments' list via total fragment get new rank
        void onHisNotify();

        void onHisRefresh();

    }

    public void notifyListUpdate(){

        if (pullToRefreshListView != null && mHistoryAdapter != null && app != null){
            List<Differ> history = app.getRankDiffHistory();
            if (history != null) {
                //mHistoryAdapter.setHistoryList(new ArrayList<Differ>());
                mHistoryAdapter.setHistoryList(history);
                mHistoryAdapter.notifyDataSetChanged();
            }
            pullToRefreshListView.onRefreshComplete();
        }
    }
}
