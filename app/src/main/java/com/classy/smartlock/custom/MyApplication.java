package com.classy.smartlock.custom;

import android.app.Application;

import com.classy.smartlock.custom.MySharedPreferences;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Creating a single instance of the SP
        MySharedPreferences.initHelper(this);
    }
}