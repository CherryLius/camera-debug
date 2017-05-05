package com.hele.hardware.analyser.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hele.hardware.analyser.listener.OnItemClickListener;

/**
 * Created by Administrator on 2017/5/5.
 */

public abstract class BaseRecyclerAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final T holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(holder.itemView, position, getItemId(position));
            }
        });
    }
}
