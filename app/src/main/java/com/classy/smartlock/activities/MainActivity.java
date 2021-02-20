package com.classy.smartlock.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AppOpsManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.classy.smartlock.R;
import com.classy.smartlock.adapters.ApplicationsAdapter;
import com.classy.smartlock.custom.MySharedPreferences;
import com.classy.smartlock.data.AppInfo;
import com.classy.smartlock.fragments.HomeFragment;
import com.classy.smartlock.fragments.LockedFragment;
import com.classy.smartlock.fragments.UnlockedFragment;
import com.classy.smartlock.services.RunningAppsService;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

//TODO: stop moving locked/unlocked apps from the list while the app is locked


public class MainActivity extends AppCompatActivity {

    //NAVIGATION
    private ChipNavigationBar main_BAR_fragments;

    //DATA
    private ArrayList<AppInfo> installedApps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        installedApps = getApps(false);
        getSPInfo();
        if (!checkUsagePermission()) {
            askUsagePermission();
        }
        initBottomBar();

        Intent service_intent = new Intent(this, com.classy.smartlock.services.RunningAppsService.class);
        startForegroundService(service_intent);
    }

    private void getSPInfo() {
        if (MySharedPreferences.getInstance().getAppInfoArrayList("locked_apps", null) == null) {
            Log.d("AAAT", "Putting installedapps in the SP!");
            MySharedPreferences.getInstance().putAppInfoArrayList("unlocked_apps", installedApps);
        }
    }

    private ArrayList<AppInfo> getApps(boolean getSysPackages) {
        int flags = PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES;
        Log.d("AAAT", "getApps");
        ArrayList<AppInfo> res = new ArrayList<>();
        List<PackageInfo> packs = this.getPackageManager().getInstalledPackages(flags);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue;
            }
            if (isSystemPackage(p)) {
                continue;
            }
            AppInfo newInfo = new AppInfo();
            newInfo.setAppname(p.applicationInfo.loadLabel(this.getPackageManager()).toString());
            newInfo.setPname(p.packageName);
            newInfo.setVersionName(p.versionName);
            newInfo.setVersionCode(p.versionCode);
            res.add(newInfo);
        }
        return res;
    }

    // Checking if the application is a system app
    private boolean isSystemPackage(PackageInfo packageInfo) {
        return ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    // A listener for the ChipNavigationBar, switching to different fragments onClick
    private ChipNavigationBar.OnItemSelectedListener fragmentSelectListener = new ChipNavigationBar.OnItemSelectedListener() {
        @Override
        public void onItemSelected(int i) {
            Fragment fragment = null;
            switch (i) {
                case R.id.fragments_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.fragments_unlocked:
                    fragment = new UnlockedFragment();
                    break;
                case R.id.fragments_locked:
                    fragment = new LockedFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragments, fragment).commit();
        }
    };

    // Asking for the UsageAccess permission
    private void askUsagePermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Usage Access Required!")
                .setMessage("This application requires Usage Access to properly function, please click \"Settings\" and turn the access on.")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent usageAccessIntent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivityForResult(usageAccessIntent, 1);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setCancelable(false);
        builder.show();
    }

    // Initializing the bottom fragment navigation bar
    private void initBottomBar() {
        main_BAR_fragments.setItemSelected(R.id.fragments_home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragments, new HomeFragment()).commit();
        main_BAR_fragments.setOnItemSelectedListener(fragmentSelectListener);
    }

    private void findViews() {
        main_BAR_fragments = findViewById(R.id.main_BAR_fragments);
    }

    // Checking if the application has the Usage permission enabled
    private boolean checkUsagePermission() {
        try {
            PackageManager packageManager = this.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager)this.getSystemService(APP_OPS_SERVICE);
            int mode = -1;
            if (appOpsManager != null) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            }
            return mode==AppOpsManager.MODE_ALLOWED;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}