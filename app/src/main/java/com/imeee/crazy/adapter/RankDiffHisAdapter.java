package com.imeee.crazy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imeee.crazy.R;
import com.imeee.crazy.dao.Differ;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by zoey on 2/2/17.
 */

public class RankDiffHisAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    private List<Differ> historyList = null;

    public RankDiffHisAdapter(Context context, List<Differ> historyList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.historyList = historyList;
    }

    public void setHistoryList(List<Differ> historyList){
        this.historyList = historyList;
    }

    @Override
    public int getCount() {
        return this.historyList.size();
    }

    @Override
    public Differ getItem(int position){
        return this.historyList.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        final Differ history = historyList.get(position);

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_rank_history,null);

            holder = new ViewHolder();
            holder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tvIsTotalChg = (TextView) convertView.findViewById(R.id.tv_isTotalChg);
            holder.tvUpdateTime = (TextView) convertView.findViewById(R.id.tv_update_date);
            holder.tvReferceTime = (TextView) convertView.findViewById(R.id.tv_reference_date);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd  HH:mm");

        holder.tvNum.setText(String.valueOf(position + 1));
        holder.tvUpdateTime.setText(sdf.format(history.getUpdateDate()));
        holder.tvReferceTime.setText(sdf.format(history.getReferenceDate()));

        if(history.getTotalIsChg()){
            holder.tvIsTotalChg.setText("(^_^) total updated!!");
        } else {
            holder.tvIsTotalChg.setText("(._.) total not updated~");
        }

        return convertView;
    }

    private class ViewHolder {
        public TextView tvNum;
        public TextView tvIsTotalChg;
        public TextView tvUpdateTime;
        public TextView tvReferceTime;
    }
}
