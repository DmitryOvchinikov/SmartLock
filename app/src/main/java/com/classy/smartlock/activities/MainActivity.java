package com.classy.smartlock.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.classy.smartlock.R;
import com.classy.smartlock.adapters.ApplicationsAdapter;
import com.classy.smartlock.custom.MySharedPreferences;
import com.classy.smartlock.data.AppInfo;
import com.classy.smartlock.fragments.HomeFragment;
import com.classy.smartlock.fragments.LockedFragment;
import com.classy.smartlock.fragments.UnlockedFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

//TODO: HomeFragment: remind the user (via text beneath the icon most likely) that to turn off he will need to input the password / email.
//TODO: UnlockedFragment: recyclerview list of all the unlocked applications on the device ------ Maybe hold the list of all the applications in main and send it to lock / unlock via callbacks.
//TODO: LockedFragment: recyclerview list of all the locked applications on the device.
//TODO: Locked + Unlocked fragments: the option to lock/unlock an application, various sorts (alphabetical, reverse-alphabetical...), search, click on an app for information? maybe.
//TODO: Implement the password / email verification (the lock-unlock mechanism).
//TODO: Loading page?

//if((app.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0)
//Getting only user installed applications


public class MainActivity extends AppCompatActivity implements HomeFragment.OnSwitchFragmentListener {

    private RecyclerView main_LST_list;

    //NAVIGATION
    private ChipNavigationBar main_BAR_fragments;

    //DATA
    private ArrayList<AppInfo> installedApps;
    private ApplicationsAdapter applicationsAdapter;
    private boolean lock_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        getSPInfo();
        //initInstalledList();
        installedApps = getInstalledApps(false);
        initBottomBar();
    }

    private void getSPInfo() {
        lock_status = MySharedPreferences.getInstance().getBoolean("lock_status", true);
        Log.d("AAAT", "Initial grab from SP: " + lock_status);
    }

    private ArrayList<AppInfo> getInstalledApps(boolean getSysPackages) {
        int flags = PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES | PackageManager.GET_UNINSTALLED_PACKAGES;
        Log.d("AAAT", "getInstalledApps");
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
            newInfo.setIcon(p.applicationInfo.loadIcon(this.getPackageManager()));
            res.add(newInfo);
        }
        return res;
    }

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

        private void initBottomBar () {
            main_BAR_fragments.setItemSelected(R.id.fragments_home, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragments, new HomeFragment()).commit();
            main_BAR_fragments.setOnItemSelectedListener(fragmentSelectListener);
        }

        public boolean getLockStatus () {
            return lock_status;
        }

        public ArrayList<AppInfo> getInstalledApps () {
            return installedApps;
        }

        private void findViews () {
            //main_LST_list = findViewById(R.id.main_LST_list);
            main_BAR_fragments = findViewById(R.id.main_BAR_fragments);
        }

        // Saving the state of the lock on application pause.
        @Override
        protected void onPause () {
            super.onPause();
            //MySharedPreferences.getInstance().putBoolean("lock_status", lock_status);
        }

        @Override
        public void onHomeFragmentSwitch ( boolean status){
            lock_status = status;
            Log.d("AAAT", "CALLBACK IN MAIN: lock_status: " + lock_status + " status: " + status);
            MySharedPreferences.getInstance().putBoolean("lock_status", lock_status);
        }
    }