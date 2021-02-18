package com.classy.smartlock.custom;

import android.content.SharedPreferences;
import android.content.Context;
import android.util.Log;

import com.classy.smartlock.data.AppInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Set;

public class MySharedPreferences {

        private static MySharedPreferences instance;
        private SharedPreferences preferences;

        public static MySharedPreferences getInstance() {
            return instance;
        }

        private MySharedPreferences(Context context) {
            preferences = context.getApplicationContext().getSharedPreferences("LOCK", Context.MODE_PRIVATE);
        }

        public static MySharedPreferences initHelper(Context context) {
            if (instance == null) {
                instance = new MySharedPreferences(context);
            }
            return instance;
        }

        public void putString(String key, String value) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.apply();
        }

        public void putAppInfoArrayList(String key, ArrayList<AppInfo> appInfo) {
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(appInfo);
            Log.d("AAAT","Putting json: key - " + key + " json - " + json);
            editor.putString(key, json);
            editor.apply();
        }

        public ArrayList<AppInfo> getAppInfoArrayList(String key, String def) {
            Gson gson = new Gson();
            String json = preferences.getString(key, def);
            Log.d("AAAT","Getting json: key - " + key + " json - " + json);
            ArrayList<AppInfo> appInfos = gson.fromJson(json, new TypeToken<ArrayList<AppInfo>>(){}.getType());
            return appInfos;
        }

        public String getString(String key, String def) {
            return preferences.getString(key, def);
        }

        public void putBoolean(String key, boolean value) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }

        public boolean getBoolean(String key, boolean def) {
            return preferences.getBoolean(key, def);
        }


    }
