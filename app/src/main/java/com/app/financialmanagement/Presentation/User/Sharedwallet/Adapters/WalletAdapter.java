package com.app.financialmanagement.Presentation.User.Sharedwallet.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.financialmanagement.Domain.CalenderNotificationDataClass;
import com.app.financialmanagement.Domain.UserWalletDataClass;
import com.app.financialmanagement.Domain.WalletDataClass;
import com.app.financialmanagement.R;
import com.app.financialmanagement.Util.CommonData;

import java.util.ArrayList;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.helper> {
    private onItemClickListener mListener;

    public interface onItemClickListener {
        void delete(int position);
    }

    public void setOnItemClickListener(onItemClickListener mListener) {
        this.mListener = mListener;
    }

    private ArrayList<WalletDataClass> arrayList;
    private Context context;
    private String userId;
    public WalletAdapter(ArrayList<WalletDataClass> arrayList,Context context,String userId) {
        this.arrayList = arrayList;
        this.context=context;
        this.userId=userId;
    }

    @NonNull
    @Override
    public helper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_adapter, parent, false);
        return new helper(v);
    }

    @Override
    public void onBindViewHolder(@NonNull helper holder, @SuppressLint("RecyclerView") int position) {
        holder.cost.setText("Cost: " + arrayList.get(position).getCost() + " SAR");
        holder.title.setText(arrayList.get(position).getWalletName());
        holder.goal.setText("Goal: " +arrayList.get(position).getPurpose());
        holder.date.setText("End Date: "+arrayList.get(position).getEndDate());
        holder.day.setText("Monthly Deadline: "+arrayList.get(position).getSubmitDay()+" every month");
        if (arrayList.get(position).getUserId().equals(userId))
        {
            holder.delete.setVisibility(View.VISIBLE);
        }
        else{
            holder.delete.setVisibility(View.GONE);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.delete(position);
            }
        });
        StringBuilder names = new StringBuilder();
        for (UserWalletDataClass data : arrayList.get(position).getUsers()) {
            names.append(data.getUserName() + "  ");
        }
        holder.users.setText(names.toString());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class helper extends RecyclerView.ViewHolder {
        TextView title, cost, date,goal,day,users;
        ImageView delete;
        public helper(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            goal = itemView.findViewById(R.id.goal);
            cost = itemView.findViewById(R.id.cost);
            date = itemView.findViewById(R.id.endDate);
            day = itemView.findViewById(R.id.day);

            delete = itemView.findViewById(R.id.delete);
            users = itemView.findViewById(R.id.users);
        }
    }
}
