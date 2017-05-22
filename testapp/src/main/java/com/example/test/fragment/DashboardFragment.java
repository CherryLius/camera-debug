package com.example.test.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.test.R;

/**
 * Created by Administrator on 2017/5/22.
 */

public class DashboardFragment extends Fragment {

    private SearchView mSearchView;
    private Toolbar mToolbar;
    private AppCompatActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mToolbar = (Toolbar) contentView.findViewById(R.id.toolbar);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("Test", "Dash onAttach");
        if (context instanceof AppCompatActivity) {
            mActivity = (AppCompatActivity) context;
            mActivity.setSupportActionBar(mToolbar);
            Log.e("Test", "Dash actionBar=" + mActivity.getSupportActionBar());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("Test", "Dash onDetach");
        if (mActivity != null) {
            mActivity.setSupportActionBar(null);
            mActivity = null;
        }
    }
}
