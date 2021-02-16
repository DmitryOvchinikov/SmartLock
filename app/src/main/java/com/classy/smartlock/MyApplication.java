package com.classy.smartlock;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Creating a single instance of the SP
        MySharedPreferences.initHelper(this);
    }
}