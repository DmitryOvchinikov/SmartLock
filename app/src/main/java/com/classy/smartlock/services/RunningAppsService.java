package com.classy.smartlock.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.classy.smartlock.activities.LockActivity;
import com.classy.smartlock.custom.MySharedPreferences;
import com.classy.smartlock.data.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class RunningAppsService extends Service {

    private Runnable runnable;
    private Handler app_checking;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("AAAT", "Service created!");

        String CHANNEL_ID = "my_channel_01";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "SmartLock Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("").setContentText("").build();
        startForeground(1, notification);

        app_checking = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!MySharedPreferences.getInstance().getBoolean("lock_status", false)) {
                    Log.d("AAAT", "Doing nothing!");
                } else {
                    Log.d("AAAT", "Checking recent apps!");
                    List<UsageEvents.Event> apps = getRecentApps();
                    for (int i = 0; i < apps.size(); i++) {
                        if (!apps.get(i).getPackageName().equals("com.classy.smartlock")) {
                            checkLocked(apps.get(i).getPackageName());
                        }
                    }
                    Log.d("AAAT", "Finished checking!");
                }
                app_checking.postDelayed(runnable, 3000);
            }
        };
        app_checking.postDelayed(runnable, 1000);
    }

    private void checkLocked(String packageName) {
        Log.d("AAAT", "checkLocked");
        ArrayList<AppInfo> appInfos = MySharedPreferences.getInstance().getAppInfoArrayList("locked_apps", null);
        if (appInfos != null) {
            for (int i = 0; i < appInfos.size(); i++) {
                Log.d("AAAT", "locked app name: " + appInfos.get(i).getPname() + " comparing against recent app opened: " + packageName);
                if (appInfos.get(i).getPname().equals(packageName)) {
                    Log.d("AAAT", "Comparison is correct! Locking app!");
                    MySharedPreferences.getInstance().putBoolean("lock_status", false);
                    lockApp(packageName);
                    break;
                }
            }
        }
    }

    private void lockApp(String packageName) {
        Intent lockIntent = new Intent(this, LockActivity.class);
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MySharedPreferences.getInstance().putString("current_locked_app", packageName);
        //lockIntent.putExtra("locked_app", packageName.toString());
        this.startActivity(lockIntent);
    }

    public List<UsageEvents.Event> getRecentApps() {
        UsageEvents.Event currentEvent;
        List<UsageEvents.Event> allEvents = new ArrayList<>();
        long time = System.currentTimeMillis();

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);

        UsageEvents usageEvents = mUsageStatsManager.queryEvents(time - (1000 * 4), time + (4 * 1000));
        UsageEvents.Event event = new UsageEvents.Event();
        Log.d("AAAT", "event: " + event.getPackageName());
        while (usageEvents.hasNextEvent()) {
            currentEvent = new UsageEvents.Event();
            Log.d("AAAT", "nextEvent: " + currentEvent.getPackageName());
            usageEvents.getNextEvent(currentEvent);
            if (currentEvent.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED) {
                if (!allEvents.contains(currentEvent)) {
                    allEvents.add(currentEvent);
                }
            }
        }
        for (int i = 0; i < allEvents.size(); i++) {
            Log.d("AAAT", "running apps: " + allEvents.get(i).getPackageName());
        }

        return allEvents;
    }

    @Override
    public void onDestroy() {
        Log.d("AAAT", "onDestroy");
        app_checking.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
