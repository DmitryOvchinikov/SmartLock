package com.classy.smartlock.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.classy.smartlock.MainActivity;
import com.classy.smartlock.MySharedPreferences;
import com.classy.smartlock.R;

import net.colindodd.toggleimagebutton.ToggleImageButton;

public class HomeFragment extends Fragment {

    //TODO: text switches from on to off, dialogue when its the first use

    //Callback
    OnSwitchFragmentListener callback;

    private ToggleImageButton home_BTN_activate;
    private TextView home_TXT_state;
    private TextView home_TXT_instructions;

    private boolean lock_status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        setLock();
        home_BTN_activate.setOnClickListener(onClickListener);
    }

    private void setLock() {
        Log.d("AAAT", "setLock");
        getStatus();
        Log.d("AAAT", "Status: " + lock_status);
        if (lock_status) {
            home_BTN_activate.setChecked(true);
            home_TXT_state.setText(R.string.home_fragment_locked);
        } else {
            home_BTN_activate.setChecked(false);
            home_TXT_state.setText(R.string.home_fragment_unlocked);
        }
    }

    private void getStatus() {
        Log.d("AAAT", "getStatus");
        //MainActivity mainActivity = (MainActivity) getActivity();
        //lock_status = mainActivity.getLockStatus();
        lock_status = MySharedPreferences.getInstance().getBoolean("lock_status", false);
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("AAAT", "onClick");
            lock_status = !lock_status;
            if (lock_status) {
                home_TXT_state.setText(R.string.home_fragment_locked);
                home_TXT_instructions.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle_drawable_red));
            } else {
                home_TXT_state.setText(R.string.home_fragment_unlocked);
                home_TXT_instructions.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle_drawable_green));
            }
            Log.d("AAAT", "New Status Value: " + lock_status);
            MySharedPreferences.getInstance().putBoolean("lock_status", lock_status);
        }
    };

    private void findViews(View view) {
        home_BTN_activate = view.findViewById(R.id.home_BTN_activate);
        home_TXT_state = view.findViewById(R.id.home_TXT_state);
        home_TXT_instructions = view.findViewById(R.id.home_TXT_instructions);
    }

    public void setOnSwitchFragmentListener(HomeFragment.OnSwitchFragmentListener callback) {
        this.callback = callback;
        Log.d("AAAT", "CALLBACK IN HOME:  " + callback);
    }

    public interface OnSwitchFragmentListener {
        public void onHomeFragmentSwitch(boolean status);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (OnSwitchFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnSwitchFragmentListener");
        }
    }
}
