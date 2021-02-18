package com.classy.smartlock.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

public class RunningAppsService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        String CHANNEL_ID = "my_channel_01";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "SmartLock Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("").setContentText("").build();
        startForeground(1, notification);

        CountDownTimer check = new CountDownTimer(5000, 5000)
        {

            @Override
            public void onTick(long millisUntilFinished)
            {
            }

            @Override
            public void onFinish()
            {
                List<UsageEvents.Event> apps = getRecentApps();
                for (int i = 0; i < apps.size(); i++) {
                    if (!apps.get(i).getPackageName().equals("com.classy.smartlock")) {
                        checkLocked(apps.get(i).getPackageName());
                    }
                }

//            if(!activityOnTop.equals("com.classy.smartlock"))
//            {
//                Intent lockIntent = new Intent(this, LockScreenActivity.class);
//                lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                this.startActivity(lockIntent);*/
//            }
            }
        }.start();
    }

    private void checkLocked(String packageName) {
        //TODO: get the list of the locked apps from MainActivity via getExtra, check against it and run the lock mechanism
    }

    public List<UsageEvents.Event> getRecentApps() {
        UsageEvents.Event currentEvent;
        List<UsageEvents.Event> allEvents = new ArrayList<>();
        long time = System.currentTimeMillis();

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);

        UsageEvents usageEvents = mUsageStatsManager.queryEvents(time - 1000 * 4, time + (4 * 1000));
        UsageEvents.Event event = new UsageEvents.Event();
        while (usageEvents.hasNextEvent()) {
            currentEvent = new UsageEvents.Event();
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
}
