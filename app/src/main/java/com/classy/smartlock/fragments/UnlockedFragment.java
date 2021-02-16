package com.classy.smartlock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.classy.smartlock.R;

public class UnlockedFragment extends Fragment {

    //TODO: adapter and recyclerview stuff

    private RecyclerView unlocked_LST_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_unlocked, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
    }

    private void findViews(View view) {
        unlocked_LST_list = view.findViewById(R.id.unlocked_LST_list);
    }
}
