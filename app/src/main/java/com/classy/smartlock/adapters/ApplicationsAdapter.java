package com.classy.smartlock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.classy.smartlock.R;
import com.classy.smartlock.data.AppInfo;
import com.classy.smartlock.fragments.LockedFragment;

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
            Glide.with(holder.itemView.getContext()).load(appInfoArrayList.get(position).getIcon()).into(holder.row_IMG_appIcon);
            if (fragment_num == 0) {
                holder.row_layout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.rectangle_drawable_red));
            } else {
                holder.row_layout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.rectangle_drawable_green));
            }
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

            //row_layout.setBackground();
        }
    }
}



