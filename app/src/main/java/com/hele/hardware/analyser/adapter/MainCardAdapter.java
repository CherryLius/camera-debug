package com.hele.hardware.analyser.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.model.CardItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/19.
 */

public class MainCardAdapter extends RecyclerView.Adapter<MainCardAdapter.CardHolder> {

    private Context mContext;
    private List<CardItem> mCardItemList;

    public MainCardAdapter(Context context) {
        mContext = context;
    }

    public void updateMenu(List<CardItem> list) {
        mCardItemList = list;
        notifyDataSetChanged();
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_card, null);
        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        CardItem item = mCardItemList.get(position);
        if (!TextUtils.isEmpty(item.getTitle()))
            holder.titleView.setText(item.getTitle());
        if (!TextUtils.isEmpty(item.getDescription()))
            holder.descriptionView.setText(item.getDescription());
        if (item.getIconId() != 0)
            holder.imageView.setImageResource(item.getIconId());
    }

    @Override
    public int getItemCount() {
        return mCardItemList != null ? mCardItemList.size() : 0;
    }

    class CardHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        AppCompatTextView titleView;
        @BindView(R.id.tv_description)
        AppCompatTextView descriptionView;
        @BindView(R.id.iv_icon)
        AppCompatImageView imageView;

        public CardHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
