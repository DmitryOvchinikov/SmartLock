package com.classy.smartlock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

//TODO: HomeFragment: Big turn on / turn off icon (on first turn on choose password / email), remind the user (via text beneath the icon most likely) that to turn off he needs to input the password / email.
//TODO: UnlockedFragment: recyclerview list of all the unlocked applications on the device ------ Maybe hold the list of all the applications in main and send it to lock / unlock via callbacks.
//TODO: LockedFragment: recyclerview list of all the locked applications on the device.
//TODO: Locked + Unlocked fragments: the option to lock/unlock an application, various sorts (alphabetical, reverse-alphabetical...), search, click on an app for information? maybe.
//TODO: Design the recyclerview rows.
//TODO: Implement the password / email verification (the lock-unlock mechanism).
//TODO: Implement SharedPreferences or something similar to remember the locks.


public class MainActivity extends AppCompatActivity {

    private RecyclerView main_LST_list;
    private ArrayList<AppInfo> installedApps;
    private ApplicationsAdapter applicationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initInstalledList();
    }

    private ArrayList<AppInfo> getPackages() {
        ArrayList<AppInfo> apps = getInstalledApps(false); /* false = no system packages */
        final int max = apps.size();
        for (int i = 0; i < max; i++) {
            //Log.d("AAAT", apps.get(i).toString());
            //apps.get(i);
        }
        return apps;
    }

    private ArrayList<AppInfo> getInstalledApps(boolean getSysPackages) {
        ArrayList<AppInfo> res = new ArrayList<AppInfo>();
        List<PackageInfo> packs = this.getPackageManager().getInstalledPackages(0);
        Log.d("AAAT", packs.toString());
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue;
            }
            AppInfo newInfo = new AppInfo();
            Log.d("AAAT", "APP NAME: " + p.applicationInfo.loadLabel(this.getPackageManager()).toString());
            newInfo.setAppname(p.applicationInfo.loadLabel(this.getPackageManager()).toString());
            newInfo.setPname(p.packageName);
            newInfo.setVersionName(p.versionName);
            newInfo.setVersionCode(p.versionCode);
            newInfo.setIcon(p.applicationInfo.loadIcon(this.getPackageManager()));
            res.add(newInfo);
        }
        return res;
    }

    private void initInstalledList() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        main_LST_list.setLayoutManager(layoutManager);
        applicationsAdapter = new ApplicationsAdapter(getPackages(), this);
        main_LST_list.setAdapter(applicationsAdapter);
        applicationsAdapter.notifyDataSetChanged();
    }

    private void findViews() {
        main_LST_list = findViewById(R.id.main_LST_list);
    }
}