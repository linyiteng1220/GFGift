package com.liteng1220.gfgift.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.liteng1220.gfgift.R;

import java.util.List;

public class SearchListAdapter extends BaseAdapter {

    public SearchListAdapter(Context context, List<Data> dataList) {
        super(context, dataList);
    }

    public SearchListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_tv;
    }

    @Override
    protected BaseHolder newViewHolder(View view) {
        return new MyHolder(view);
    }

    @Override
    protected void setData2ViewHolder(BaseAdapter.Data data, RecyclerView.ViewHolder holder) {
        if (data instanceof SearchResultData) {
            SearchResultData searchResultData = (SearchResultData) data;
            ((MyHolder) holder).tvName.setText(searchResultData.name);
            ((MyHolder) holder).tvTotalNum.setText(searchResultData.totalNum);
            ((MyHolder) holder).tvStarring.setText(searchResultData.starring);
        }
    }

    private class MyHolder extends BaseAdapter.BaseHolder {
        TextView tvName;
        TextView tvTotalNum;
        TextView tvStarring;

        MyHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTotalNum = (TextView) itemView.findViewById(R.id.tv_total_num);
            tvStarring = (TextView) itemView.findViewById(R.id.tv_starring);
        }
    }

    public static class SearchResultData extends BaseAdapter.Data {
        public String uuid;
        public String name;
        public String totalNum;
        public String starring;
    }
}
