package com.example.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.example.test.adapter.ExpandableAdapter;
import com.example.test.model.ExpandableBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */

public class ExpandableActivity extends AppCompatActivity implements View.OnClickListener {

    ExpandableListView expandableListView;
    EditText editText1;
    EditText editText2;
    Button button;
    List<ExpandableBean> mList = new ArrayList<>();
    ExpandableAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_expandable_listview);
        editText1 = (EditText) findViewById(R.id.et1);
        editText2 = (EditText) findViewById(R.id.et2);
        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(this);
        expandableListView = (ExpandableListView) findViewById(R.id.expand_list_view);


        init(mList, false);
        mAdapter = new ExpandableAdapter(this, mList);

        expandableListView.setAdapter(mAdapter);
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            expandableListView.expandGroup(i);
        }
    }

    void init(List<ExpandableBean> beanList, boolean child) {
        String title = child ? "child " : "title ";
        ExpandableBean bean = new ExpandableBean();
        bean.name = title + 1;
        if (!child) {
            bean.list = new ArrayList<>();
            init(bean.list, true);
        }
        beanList.add(bean);

        bean = new ExpandableBean();
        bean.name = title + 2;
        if (!child) {
            bean.list = new ArrayList<>();
            init(bean.list, true);
        }
        beanList.add(bean);

        bean = new ExpandableBean();
        bean.name = title + 3;
        if (!child) {
            bean.list = new ArrayList<>();
            init(bean.list, true);
        }
        beanList.add(bean);
    }

    @Override
    public void onClick(View v) {
        int groupPosition = Integer.valueOf(editText1.getEditableText().toString());
        int childPosition = Integer.valueOf(editText2.getEditableText().toString());

        int first = expandableListView.getFirstVisiblePosition();
        int last = expandableListView.getLastVisiblePosition();
        Log.i("Test", "first " + first + ",last " + last);

        View firstView = expandableListView.getChildAt(first - first);
        View lastView = expandableListView.getChildAt(last - first);

        Log.i("Test", "first tag=" + (firstView == null ? null : firstView.getTag())
                + ", lastTag=" + (lastView == null ? null : lastView.getTag()));

        View childView = getChildView(groupPosition, childPosition);
        Log.e("Test", "childView tag=" + (childView == null ? null : childView.getTag()));
        mAdapter.updateSingleView(childView);

    }

    private View getChildView(int groupPosition, int childPosition) {
        int ret = -1;
        int index = -1;
        ok:
        for (int i = 0; i < mList.size(); i++) {
            index++;
            List<ExpandableBean> beanList = mList.get(i).list;
            for (int j = 0; j < beanList.size(); j++) {
                index++;
                if (i == groupPosition && j == childPosition) {
                    ret = index;
                    break ok;
                }
            }
        }

        int first = expandableListView.getFirstVisiblePosition();
        Log.e("Test", "index=" + index + ",ret=" + ret);
        return expandableListView.getChildAt(ret - first);
    }
}
