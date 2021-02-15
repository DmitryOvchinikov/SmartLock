package com.classy.smartlock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ApplicationsViewHolder> {

    private ArrayList<AppInfo> appInfoArrayList;
    private Context context;

    public ApplicationsAdapter(ArrayList<AppInfo> appInfoArrayList, Context context) {
        this.appInfoArrayList = appInfoArrayList;
        this.context = context;
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
            Glide.with(context).load(appInfoArrayList.get(position).getIcon()).into(holder.row_IMG_appIcon);
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

        public ApplicationsViewHolder(@NonNull View itemView) {
            super(itemView);
            row_TXT_appName = itemView.findViewById(R.id.row_TXT_appName);
            row_IMG_appIcon = itemView.findViewById(R.id.row_IMG_appIcon);
            row_IMG_lock = itemView.findViewById(R.id.row_IMG_lock);
        }
    }
}



