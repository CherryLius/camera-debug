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
import com.hele.hardware.analyser.base.BaseRecyclerAdapter;
import com.hele.hardware.analyser.model.ResultInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ResultAdapter extends BaseRecyclerAdapter<ResultAdapter.ResultHolder> {

    private List<ResultInfo> mResults;
    private SimpleDateFormat mFormatter;

    public ResultAdapter() {
        mFormatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    }

    @Override
    public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result_info, parent, false);
        return new ResultHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ResultHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ResultInfo info = mResults.get(position);
        Glide.with(App.getContext()).load(info.getPicturePath()).into(holder.imageView);
        holder.valueView.setText(info.getValue());
        holder.nameView.setText(info.getName());
        holder.dateView.setText(mFormatter.format(new Date(info.getDateTime())));
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
        AppCompatTextView valueView;
        @BindView(R.id.tv_name)
        AppCompatTextView nameView;
        @BindView(R.id.tv_date)
        AppCompatTextView dateView;


        public ResultHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
