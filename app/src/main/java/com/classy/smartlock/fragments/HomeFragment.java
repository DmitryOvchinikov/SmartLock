package com.classy.smartlock.fragments;

import android.app.usage.UsageEvents;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.classy.smartlock.custom.MySharedPreferences;
import com.classy.smartlock.R;

import net.colindodd.toggleimagebutton.ToggleImageButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    //TODO: dialogue when its the first use

    private ToggleImageButton home_BTN_activate;
    private TextView home_TXT_state;
    private TextView home_TXT_instructions;
    private TextView home_TXT_help;

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
            home_TXT_instructions.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle_drawable_red));
            home_TXT_help.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle_drawable_red));
        } else {
            home_BTN_activate.setChecked(false);
            home_TXT_state.setText(R.string.home_fragment_unlocked);
            home_TXT_instructions.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle_drawable_green));
            home_TXT_help.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle_drawable_green));
        }
    }

    private void getStatus() {
        Log.d("AAAT", "getStatus");
        lock_status = MySharedPreferences.getInstance().getBoolean("lock_status", false);
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("AAAT", "onClick");
            if (MySharedPreferences.getInstance().getString("email", null) == null && MySharedPreferences.getInstance().getString("password", null) == null) {
                home_BTN_activate.setChecked(false);
                createPassword();
            } else {
                lock_status = !lock_status;
                if (lock_status) {
                    home_TXT_state.setText(R.string.home_fragment_locked);
                    home_TXT_instructions.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle_drawable_red));
                    home_TXT_help.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle_drawable_red));
                } else {
                    home_TXT_state.setText(R.string.home_fragment_unlocked);
                    home_TXT_instructions.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle_drawable_green));
                    home_TXT_help.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle_drawable_green));
                }
                Log.d("AAAT", "New Status Value: " + lock_status);
                MySharedPreferences.getInstance().putBoolean("lock_status", lock_status);
            }
        }
    };

    private void createPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Choose")
                .setMessage("You have to choose a password to lock the applications.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder password_builder = new AlertDialog.Builder(getContext());
                        LayoutInflater li = LayoutInflater.from(getContext());
                        View password_dialog_view = li.inflate(R.layout.password_dialog, null);

                        password_builder.setView(password_dialog_view);
                        EditText userInput = password_dialog_view.findViewById(R.id.password_dialog_edit);

                        password_builder.setCancelable(false)
                                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MySharedPreferences.getInstance().putString("password", userInput.getText().toString());
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        password_builder.create().show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);
        builder.show();
    }

    private void findViews(View view) {
        home_BTN_activate = view.findViewById(R.id.home_BTN_activate);
        home_TXT_state = view.findViewById(R.id.home_TXT_state);
        home_TXT_instructions = view.findViewById(R.id.home_TXT_instructions);
        home_TXT_help = view.findViewById(R.id.home_TXT_help);
    }
}
