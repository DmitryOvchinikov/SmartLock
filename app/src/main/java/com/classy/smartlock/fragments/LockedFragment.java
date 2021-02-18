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

import com.classy.smartlock.activities.MainActivity;
import com.classy.smartlock.adapters.ApplicationsAdapter;
import com.classy.smartlock.R;
import com.classy.smartlock.custom.MySharedPreferences;

public class LockedFragment extends Fragment {

    //TODO: adapter and recyclerview stuff

    private RecyclerView locked_LST_list;

    private ApplicationsAdapter locked_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_locked, container, false);
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
        locked_LST_list.setLayoutManager(layoutManager);
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        locked_adapter = new ApplicationsAdapter(MySharedPreferences.getInstance().getAppInfoArrayList("locked_apps", null), 0);
        locked_LST_list.setAdapter(locked_adapter);
        locked_adapter.notifyDataSetChanged();
    }

    private void findViews(View view) {
        locked_LST_list = view.findViewById(R.id.locked_LST_list);
    }

}
