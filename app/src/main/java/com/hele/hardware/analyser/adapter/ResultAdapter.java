package com.hele.hardware.analyser.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hele.hardware.analyser.App;
import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.model.ResultInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultHolder> {

    private List<ResultInfo> mResults;

    public ResultAdapter() {

    }

    @Override
    public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result_info, parent, false);
        return new ResultHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ResultHolder holder, int position) {
        ResultInfo info = mResults.get(position);
        Glide.with(App.getContext()).load(info.getPicturePath()).into(holder.imageView);
        holder.textView.setText(info.getValue());
    }

    @Override
    public int getItemCount() {
        return mResults == null ? 0 : mResults.size();
    }

    public void updateList(List<ResultInfo> list) {
        mResults = list;
        notifyDataSetChanged();
    }

    class ResultHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        AppCompatImageView imageView;
        @BindView(R.id.tv_description)
        AppCompatTextView textView;

        public ResultHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
