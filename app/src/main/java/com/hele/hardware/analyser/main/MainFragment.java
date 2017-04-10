package com.hele.hardware.analyser.main;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainFragment extends ListFragment implements MainContract.View {

    private MainContract.Presenter mPresenter;
    private AdapterView.OnItemClickListener mItemClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MainPresenter(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (mItemClickListener != null)
            mItemClickListener.onItemClick(l, v, position, id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AdapterView.OnItemClickListener)
            mItemClickListener = (AdapterView.OnItemClickListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mItemClickListener = null;
    }

    @Override
    public void setPresenter(@NonNull MainContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.parseList(getContext());
    }

    @Override
    public void showList(String[] array) {
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, array);
        setListAdapter(adapter);
    }
}
