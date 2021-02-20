package com.classy.smartlock.adapters;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.classy.smartlock.R;
import com.classy.smartlock.custom.MySharedPreferences;
import com.classy.smartlock.data.AppInfo;

import java.util.ArrayList;

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ApplicationsViewHolder> {

    private ArrayList<AppInfo> appInfoArrayList;
    private int fragment_num;

    public ApplicationsAdapter(ArrayList<AppInfo> appInfoArrayList, int fragment) {
        this.appInfoArrayList = appInfoArrayList;
        this.fragment_num = fragment;
    }

    @NonNull
    @Override
    public ApplicationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.applications_row, parent, false);
        return new ApplicationsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationsViewHolder holder, int position) {

            holder.row_TXT_appName.setText(appInfoArrayList.get(position).getAppname());

        try {
            Glide.with(holder.itemView.getContext()).load(holder.itemView.getContext().getPackageManager().getApplicationIcon(appInfoArrayList.get(position).getPname())).into(holder.row_IMG_appIcon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (fragment_num == 0) {
                holder.row_layout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.rectangle_drawable_red));
            } else {
                holder.row_layout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.rectangle_drawable_green));
            }

        holder.row_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MySharedPreferences.getInstance().getBoolean("lock_status", false)) {
                    //Cant lock/unlock if the application is locked.
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    builder.setTitle("Locking/Unlocking " + holder.row_TXT_appName.getText())
                            .setMessage("Are you sure?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    if (fragment_num == 0) {
                                        ArrayList<AppInfo> unlocked_apps = MySharedPreferences.getInstance().getAppInfoArrayList("unlocked_apps", null);
                                        if (!unlocked_apps.contains(appInfoArrayList.get(position))) {
                                            unlocked_apps.add(appInfoArrayList.get(position));
                                        }
                                        MySharedPreferences.getInstance().putAppInfoArrayList("unlocked_apps", unlocked_apps);
                                        appInfoArrayList.remove(position);
                                        MySharedPreferences.getInstance().putAppInfoArrayList("locked_apps", appInfoArrayList);
                                    } else {
                                        ArrayList<AppInfo> locked_apps = MySharedPreferences.getInstance().getAppInfoArrayList("locked_apps", null);
                                        if (locked_apps == null) {
                                            locked_apps = new ArrayList<AppInfo>();
                                        }
                                        if (!locked_apps.contains(appInfoArrayList.get(position))) {
                                            locked_apps.add(appInfoArrayList.get(position));
                                        }
                                        MySharedPreferences.getInstance().putAppInfoArrayList("locked_apps", locked_apps);
                                        appInfoArrayList.remove(position);
                                        MySharedPreferences.getInstance().putAppInfoArrayList("unlocked_apps", appInfoArrayList);
                                    }
                                    ApplicationsAdapter.this.notifyDataSetChanged();
                                    dialoginterface.dismiss();
                                }
                            })
                            .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                            .setCancelable(true);
                    builder.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (appInfoArrayList == null) {
            return 0;
        }
        return appInfoArrayList.size();
    }

    public static class ApplicationsViewHolder extends RecyclerView.ViewHolder {
        private TextView row_TXT_appName;
        private ImageView row_IMG_appIcon;
        private ImageView row_IMG_lock;
        private LinearLayout row_layout;

        public ApplicationsViewHolder(@NonNull View itemView) {
            super(itemView);
            row_TXT_appName = itemView.findViewById(R.id.row_TXT_appName);
            row_IMG_appIcon = itemView.findViewById(R.id.row_IMG_appIcon);
            row_IMG_lock = itemView.findViewById(R.id.row_IMG_lock);
            row_layout = itemView.findViewById(R.id.row_layout);

        }
    }
}



