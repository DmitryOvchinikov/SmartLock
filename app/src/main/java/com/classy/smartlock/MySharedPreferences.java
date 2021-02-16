package com.classy.smartlock;

import android.content.SharedPreferences;
import android.content.Context;

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
