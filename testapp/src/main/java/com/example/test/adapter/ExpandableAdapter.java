package com.example.test.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.model.ExpandableBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private List<ExpandableBean> mList;
    private Context mContext;

    public ExpandableAdapter(Context context, List<ExpandableBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<ExpandableBean> beanList = mList.get(groupPosition).list;
        return beanList == null ? 0 : beanList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<ExpandableBean> beanList = mList.get(groupPosition).list;
        return beanList == null ? null : beanList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_simple_text_item, null);
            holder = new ParentHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        } else {
            holder = (ParentHolder) convertView.getTag();
        }
        ExpandableBean bean = mList.get(groupPosition);
        holder.tv.setText(bean.name);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_simple_text_item, null);
            holder = new ChildHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.tv.setTextColor(Color.RED);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        List<ExpandableBean> list = mList.get(groupPosition).list;
        if (list != null) {
            ExpandableBean bean = list.get(childPosition);
            holder.tv.setText(bean.name);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void updateSingleView(View view) {
        if (view == null) return;
        Object tag = view.getTag();
        if (tag == null) return;
        if (tag instanceof ParentHolder) {
            ParentHolder holder = (ParentHolder) tag;
            holder.tv.setText("changed!");
        } else if (tag instanceof ChildHolder) {
            ChildHolder holder = (ChildHolder) tag;
            holder.tv.setText("changed child!");
        }
    }

    private class ParentHolder {
        TextView tv;
    }

    private class ChildHolder {
        TextView tv;
    }
}
