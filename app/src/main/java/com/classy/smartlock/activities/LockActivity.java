package com.classy.smartlock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.classy.smartlock.R;
import com.classy.smartlock.custom.MySharedPreferences;
import com.classy.smartlock.data.AppInfo;

import java.util.ArrayList;


public class LockActivity extends AppCompatActivity {

    private EditText lock_EDT_password;
    private TextView lock_TXT_text;
    private Button lock_BTN_enter;
    private Button lock_BTN_exit;

    private String password;
    private String current_locked_name;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        findViews();
        bindListeners();
        getSPInfo();

    }

    private void getSPInfo() {
        password = MySharedPreferences.getInstance().getString("password", null);
        current_locked_name = MySharedPreferences.getInstance().getString("current_locked_app", null);
    }


    private void bindListeners() {
        lock_BTN_enter.setOnClickListener(enterClickListener);
        lock_BTN_exit.setOnClickListener(exitClickListener);
    }

    private final View.OnClickListener enterClickListener = new View.OnClickListener() {
        @Override

        public void onClick(View v) {
            String input_password = lock_EDT_password.getText().toString();
            if (input_password.equals(password)) {
                Toast.makeText(LockActivity.this, "Application unlocked!", Toast.LENGTH_SHORT).show();
                ArrayList<AppInfo> locked_applications = MySharedPreferences.getInstance().getAppInfoArrayList("locked_apps", null);
                for (int i = 0; i < locked_applications.size(); i++) {
                    Log.d("AAAT", "comparing " + locked_applications.get(i).getPname() + " to the current locked app name " + current_locked_name);
                    if (locked_applications.get(i).getPname().equals(current_locked_name)) {

                        AppInfo the_locked_app = locked_applications.get(i);
                        locked_applications.remove(i);
                        MySharedPreferences.getInstance().putAppInfoArrayList("locked_apps", locked_applications);

                        ArrayList<AppInfo> unlocked_applications = MySharedPreferences.getInstance().getAppInfoArrayList("unlocked_apps", null);
                        unlocked_applications.add(the_locked_app);
                        MySharedPreferences.getInstance().putAppInfoArrayList("unlocked_apps", unlocked_applications);

                        MySharedPreferences.getInstance().putBoolean("lock_status", true);
                        Intent service_intent = new Intent(getApplicationContext(), com.classy.smartlock.services.RunningAppsService.class);
                        startForegroundService(service_intent);
                        break;
                    }
                }
                finish();
            } else {
                lock_EDT_password.setError("Wrong password!");
            }
        }
    };

    private View.OnClickListener exitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goToHomeScreen();
            finish();
        }
    };

    private void goToHomeScreen() {
        Intent homeScreen = new Intent(Intent.ACTION_MAIN);
        homeScreen.addCategory(Intent.CATEGORY_HOME);
        homeScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(homeScreen);
    }

    private void findViews() {
        lock_EDT_password = findViewById(R.id.lock_EDT_password);
        lock_BTN_enter = findViewById(R.id.lock_BTN_enter);
        lock_BTN_exit = findViewById(R.id.lock_BTN_exit);
        lock_TXT_text = findViewById(R.id.lock_TXT_text);
    }
}
