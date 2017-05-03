package com.example.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cherry.library.ui.view.recycler.SectionItemDecoration;
import com.example.test.adapter.SectionAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/5/3.
 */

public class SectionActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SectionAdapter mAdapter;
    private List<String> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mList = new ArrayList<>();

        initData();
        mAdapter = new SectionAdapter(mList);
        mRecyclerView.addItemDecoration(new SectionItemDecoration(this, new SectionItemDecoration.ISectionProvider() {
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
