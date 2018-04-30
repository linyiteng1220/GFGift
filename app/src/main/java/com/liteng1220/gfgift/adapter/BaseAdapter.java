package com.liteng1220.gfgift.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liteng1220.lyt.widget.CustomViewOnClickListener;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_FOOTER = 2;

    private View viewHeader;
    private View viewFooter;
    private List<Data> dataList;
    private Context context;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    public BaseAdapter(Context context) {
        this(context, new ArrayList<Data>());
    }

    public BaseAdapter(Context context, List<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public void setHeader(View viewHeader) {
        this.viewHeader = viewHeader;
        notifyItemInserted(0);
    }

    public void addFooter(View viewFooter) {
        removeFooter();

        this.viewFooter = viewFooter;
        viewFooter.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeFooter() {
        if (viewFooter != null) {
            viewFooter = null;
            notifyItemRemoved(getItemCount() - 1);
        }
    }

    public void addData(List<Data> dataList) {
        if (dataList != null) {
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    public void updateData(List<Data> dataList) {
        if (dataList != null) {
            this.dataList.clear();
            addData(dataList);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (viewHeader != null && position == 0) {
            return TYPE_HEADER;
        }

        if (viewFooter != null && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }

        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (dataList == null) {
            if (viewHeader != null && viewFooter != null) {
                return 2;
            } else if (viewHeader != null || viewFooter != null) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (viewHeader != null && viewFooter != null) {
                return dataList.size() + 2;
            } else if (viewHeader != null || viewFooter != null) {
                return dataList.size() + 1;
            } else {
                return dataList.size();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewHeader != null && viewType == TYPE_HEADER) {
            return newViewHolder(viewHeader);
        }

        if (viewType == TYPE_FOOTER) {
            return newViewHolder(viewFooter);
        }

        View layout = LayoutInflater.from(context).inflate(getItemLayoutResId(), parent, false);
        return newViewHolder(layout);
    }

    protected abstract int getItemLayoutResId();
    protected abstract BaseHolder newViewHolder(View view);
    protected abstract void setData2ViewHolder(Data data, RecyclerView.ViewHolder holder);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) != TYPE_NORMAL) {
            return;
        }

        final int realPosition = getRealPosition(holder);
        if (holder instanceof BaseHolder) {
            if (dataList != null) {
                final Data data = dataList.get(realPosition);
                setData2ViewHolder(data, holder);

                if (itemClickListener != null) {
                    holder.itemView.setOnClickListener(new CustomViewOnClickListener() {
                        @Override
                        public void onSingleClick(View v) {
                            itemClickListener.onItemClick(realPosition, data);
                        }
                    });
                }

                if (itemLongClickListener != null) {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            return itemLongClickListener.onItemLongClick(realPosition, data);
                        }
                    });
                }
            }
        }
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return viewHeader == null ? position : position - 1;
    }

    public void clearData() {
        if (this.dataList != null) {
            dataList.clear();
            notifyDataSetChanged();
        }
    }

    protected class BaseHolder extends RecyclerView.ViewHolder {
        public BaseHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Data data);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position, Data data);
    }

    public static class Data {
    }
}
