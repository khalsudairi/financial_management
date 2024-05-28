package com.app.financialmanagement.Presentation.User.NotificationUser;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.financialmanagement.Domain.CalenderNotificationDataClass;
import com.app.financialmanagement.R;

import java.util.ArrayList;

public class CalenderAdapter extends RecyclerView.Adapter<CalenderAdapter.helper> {
    private onItemClickListener mListener;

    public interface onItemClickListener {
        void delete(int position);
        void updateDate(int position);
    }

    public void setOnItemClickListener(onItemClickListener mListener) {
        this.mListener = mListener;
    }

    private ArrayList<CalenderNotificationDataClass> arrayList;
    public CalenderAdapter(ArrayList<CalenderNotificationDataClass> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public helper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.calender_notification_adapter, parent, false);
        return new helper(v);
    }

    @Override
    public void onBindViewHolder(@NonNull helper holder, @SuppressLint("RecyclerView") int position) {
        holder.cost.setText("Cost: " + arrayList.get(position).getCost() + " SAR");
        holder.name.setText(arrayList.get(position).getMissionName());
        holder.date.setText(arrayList.get(position).getDate());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.delete(position);
            }
        });
        holder.updateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.updateDate(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class helper extends RecyclerView.ViewHolder {
        TextView name, cost, date;
        Button updateDate, delete;

        public helper(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            cost = itemView.findViewById(R.id.cost);
            updateDate = itemView.findViewById(R.id.updateDate);
            delete = itemView.findViewById(R.id.delete);


        }
    }
}
