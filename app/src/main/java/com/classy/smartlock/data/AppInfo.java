package com.classy.smartlock.data;

import java.io.Serializable;

public class AppInfo implements Serializable, Comparable<AppInfo> {

    private String appname = "";
    private String pname = "";
    private String versionName = "";
    private int versionCode = 0;

    public AppInfo() {
    }

    public AppInfo(String appname, String pname, String versionName, int versionCode) {
        this.appname = appname;
        this.pname = pname;
        this.versionName = versionName;
        this.versionCode = versionCode;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    // Compare by package names
    @Override
    public int compareTo(AppInfo appInfo) {
        return pname.compareTo(appInfo.getPname());
    }
}
