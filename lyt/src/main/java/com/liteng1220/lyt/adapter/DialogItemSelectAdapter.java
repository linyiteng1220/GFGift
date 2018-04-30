package com.liteng1220.lyt.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liteng1220.lyt.R;
import com.liteng1220.lyt.manager.DialogManager;
import com.liteng1220.lyt.utility.BaseUtil;
import com.liteng1220.lyt.vo.ItemVo;
import com.liteng1220.lyt.widget.CustomViewOnClickListener;

import java.util.List;

public class DialogItemSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemVo> dataList;
    private Context context;
    private float iconPadding;

    private Resources res;
    private DialogManager.DialogItemSelectListener listener;

    public DialogItemSelectAdapter(Context context, List<ItemVo> dataList, DialogManager.DialogItemSelectListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
        iconPadding = context.getResources().getDimension(R.dimen.item_padding);
        res = context.getResources();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dialog_menu, parent, false);
        view.setOnClickListener(new CustomViewOnClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (listener != null) {
                    listener.onItemSelected((Integer) v.getTag());
                }
            }
        });
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            if (dataList != null) {
                TextView tvItem = ((MyHolder) holder).tvItem;
                final ItemVo itemVo = dataList.get(position);
                tvItem.setTag(itemVo.id);
                tvItem.setText(itemVo.name);

                if (itemVo.iconResId > 0) {
                    Drawable drawable = BaseUtil.wrapColor4Icon(res, itemVo.iconResId, R.color.color_primary);
                    tvItem.setCompoundDrawablePadding((int) iconPadding);
                    tvItem.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                }
            }
        }
    }

    private class MyHolder extends RecyclerView.ViewHolder {
        TextView tvItem;

        MyHolder(View itemView) {
            super(itemView);
            tvItem = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }

}
