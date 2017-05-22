package com.example.test.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.test.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment implements ItemListDialogFragment.Listener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getChildFragmentManager().beginTransaction().add(R.id.fragment_container, ItemListDialogFragment.newInstance(3)).commit();
    }

    @Override
    public void onItemClicked(int position) {

    }
}
