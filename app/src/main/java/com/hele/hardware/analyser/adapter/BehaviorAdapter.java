package com.hele.hardware.analyser.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cherry.library.ui.view.DotLineView;
import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.model.DotItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/20.
 */

public class BehaviorAdapter extends RecyclerView.Adapter<BehaviorAdapter.Holder> {

    private Context mContext;
    private List<DotItem> mItemList;

    public BehaviorAdapter(Context context) {
        mContext = context;
    }

    public BehaviorAdapter(List<DotItem> items) {
        mItemList = items;
    }

    public void update(List<DotItem> items) {
        mItemList = items;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step_tips, null);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        DotItem item = mItemList.get(position);

        holder.dotView.setNormalColor(ContextCompat.getColor(mContext, R.color.colorOrange_200));
        holder.dotView.setSelectedColor(ContextCompat.getColor(mContext, R.color.colorOrange_800));

        holder.dotView.setSelected(item.isSelected());
        holder.textView.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.dot_view)
        DotLineView dotView;
        @BindView(R.id.tv_title)
        AppCompatTextView textView;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
