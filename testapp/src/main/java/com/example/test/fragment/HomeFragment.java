package com.example.test.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cherry.library.ui.view.recycler.SectionItemDecoration;
import com.example.test.R;
import com.example.test.adapter.SectionAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SectionAdapter mAdapter;
    private List<String> mList;
    private Toolbar mToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            activity.setSupportActionBar(mToolbar);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Context context = getContext();
        if (context instanceof Activity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            activity.setSupportActionBar(null);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mList = new ArrayList<>();

        initData();
        mAdapter = new SectionAdapter(mList);
        mRecyclerView.addItemDecoration(new SectionItemDecoration(getActivity(), new SectionItemDecoration.ISectionProvider() {
            @Override
            public long getSectionId(int position) {
                return Character.toUpperCase(mList.get(position).charAt(0));
            }

            @Override
            public String getSectionTitle(int position) {
                return mList.get(position).substring(0, 1).toUpperCase();
            }
        }));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        for (int i = 0; i < 100; i++) {
            mList.add(getRandomString());
        }
        Collections.sort(mList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.charAt(0) - o2.charAt(0);
            }
        });
    }

    private static final char[] CHARACTER = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'x'};

    public static String getRandomString() {
        Random random = new Random();
        int length = random.nextInt(6) + 4;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTER.length);
            sb.append(CHARACTER[index]);
        }
        return sb.toString();
    }
}
