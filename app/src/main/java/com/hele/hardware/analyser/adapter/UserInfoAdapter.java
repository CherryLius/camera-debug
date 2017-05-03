package com.hele.hardware.analyser.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.model.UserInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.UserHolder> {

    private List<UserInfo> mUserInfoList;

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_info, parent, false);
        return new UserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        UserInfo info = mUserInfoList.get(position);
        holder.textView.setText(info.getName());
    }

    @Override
    public int getItemCount() {
        return mUserInfoList == null ? 0 : mUserInfoList.size();
    }

    public void updateUsers(List<UserInfo> list) {
        mUserInfoList = list;
        notifyDataSetChanged();
    }

    class UserHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public UserHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}
