package com.imeee.crazy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.imeee.crazy.R;
import com.imeee.crazy.dao.RecordDiffer;

/**
 * Created by Administrator on 2017/1/25.
 */

public class RankAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;

    private List<RecordDiffer> diffList = null;

    public RankAdapter(Context context, List<RecordDiffer> diffList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.diffList = diffList;
    }

    public void setRankList(List<RecordDiffer> diffList){
        this.diffList = diffList;
    }

    @Override
    public int getCount() {
        return this.diffList.size();
    }

    @Override
    public RecordDiffer getItem(int position){
        return this.diffList.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        final RecordDiffer record = diffList.get(position);
        int rankDiff = record.getRankChg();
        int scoreDiff = record.getScoreChg();

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_record,null);

            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_song_name);
            holder.tvScore = (TextView) convertView.findViewById(R.id.tv_score);
            holder.tvRank = (TextView) convertView.findViewById(R.id.tv_rank);
            holder.tvRankDiff = (TextView) convertView.findViewById(R.id.tv_diff_rank);
            holder.tvScoreDiff = (TextView) convertView.findViewById(R.id.tv_diff_score);
            holder.tvRankDiffMark = (TextView) convertView.findViewById(R.id.tv_diffMark_rank);;
            holder.tvScoreDiffMark = (TextView) convertView.findViewById(R.id.tv_diffMark_score);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvRank.setText(String.valueOf(position + 1));
        holder.tvName.setText(record.getName());
        holder.tvScore.setText(String.valueOf(record.getScore()));
        holder.tvRankDiff.setText(String.valueOf(Math.abs(rankDiff)));
        holder.tvScoreDiff.setText(String.valueOf(Math.abs(scoreDiff)));

        if (rankDiff > 0){
            holder.tvRankDiffMark.setText("+");
            holder.tvRankDiffMark.setTextColor(context.getResources().getColor(R.color.itemBgIcr));
            holder.tvRankDiff.setTextColor(context.getResources().getColor(R.color.itemBgIcr));
            //holder.llContainer.setBackgroundColor(context.getResources().getColor(R.color.itemBgIcr));
        } else if (rankDiff < 0){
            holder.tvRankDiffMark.setText("-");
            holder.tvRankDiffMark.setTextColor(context.getResources().getColor(R.color.itemBgDec));
            holder.tvRankDiff.setTextColor(context.getResources().getColor(R.color.itemBgDec));
            //holder.llContainer.setBackgroundColor(context.getResources().getColor(R.color.itemBgDec));
        } else {
            holder.tvRankDiffMark.setText("=");
            holder.tvRankDiffMark.setTextColor(context.getResources().getColor(R.color.black));
            holder.tvRankDiff.setTextColor(context.getResources().getColor(R.color.black));
            //holder.llContainer.setBackgroundColor(context.getResources().getColor(R.color.itemBgNotChg));
        }

        if (scoreDiff > 0){
            holder.tvScoreDiffMark.setText("+");
            holder.tvScoreDiffMark.setTextColor(context.getResources().getColor(R.color.itemBgIcr));
            holder.tvScoreDiff.setTextColor(context.getResources().getColor(R.color.itemBgIcr));
        } else if (scoreDiff < 0){
            holder.tvScoreDiffMark.setText("-");
            holder.tvScoreDiffMark.setTextColor(context.getResources().getColor(R.color.itemBgDec));
            holder.tvScoreDiff.setTextColor(context.getResources().getColor(R.color.itemBgDec));
        } else {
            holder.tvScoreDiffMark.setText("=");
            holder.tvScoreDiffMark.setTextColor(context.getResources().getColor(R.color.black));
            holder.tvScoreDiff.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }

    private class ViewHolder {
        public TextView tvName;
        public TextView tvScore;
        public TextView tvRank;
        public TextView tvRankDiff;
        public TextView tvScoreDiff;
        public TextView tvRankDiffMark;
        public TextView tvScoreDiffMark;
    }

}
