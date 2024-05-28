package com.app.financialmanagement.Presentation.User.FinacialPlane;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.financialmanagement.Domain.AnnualCostDataClass;
import com.app.financialmanagement.Domain.ViewData;
import com.app.financialmanagement.R;

import java.util.ArrayList;

public class ViewItemAdapter extends  RecyclerView.Adapter<ViewItemAdapter.helper>{
    private ArrayList<ViewData> arrayList;

    public ViewItemAdapter(ArrayList<ViewData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public helper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view_date,parent,false);
        return new helper(v);
    }

    @Override
    public void onBindViewHolder(@NonNull helper holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(arrayList.get(position).getItemName());
        holder.itemCost.setText(arrayList.get(position).getCost()+" SAR");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class helper extends RecyclerView.ViewHolder{
        TextView name,itemCost;
        public helper(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.item);
            itemCost=itemView.findViewById(R.id.cost);

        }
    }
}
