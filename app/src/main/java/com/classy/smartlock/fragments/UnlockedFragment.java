package com.classy.smartlock.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.classy.smartlock.R;
import com.classy.smartlock.adapters.ApplicationsAdapter;
import com.classy.smartlock.custom.MySharedPreferences;

public class UnlockedFragment extends Fragment {

    private RecyclerView unlocked_LST_list;
    private ApplicationsAdapter unlocked_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_unlocked, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        initList();

    }


    private void initList() {
        Log.d("AAAT", "initInstalledList");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        unlocked_LST_list.setLayoutManager(layoutManager);
        unlocked_adapter = new ApplicationsAdapter((MySharedPreferences.getInstance().getAppInfoArrayList("unlocked_apps", null)), 1);
        unlocked_LST_list.setAdapter(unlocked_adapter);
        unlocked_adapter.notifyDataSetChanged();
    }

    private void findViews (View view) {
            unlocked_LST_list = view.findViewById(R.id.unlocked_LST_list);
        }
    }
