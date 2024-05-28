 package com.app.financialmanagement.Presentation.User.FinancialStatements.MonthlySaving;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.financialmanagement.Domain.MonthlyGoalsDataClass;
import com.app.financialmanagement.Domain.MonthlySavingCostDataClass;
import com.app.financialmanagement.R;

import java.util.ArrayList;

public class MonthlySavingAdapter extends  RecyclerView.Adapter<MonthlySavingAdapter.helper>{
    private onItemClickListener mListener;
    public interface onItemClickListener
    {
        void delete(int position);
        void returnItem(int position);
    }
    public void setOnItemClickListener(onItemClickListener mListener)
    {
        this.mListener=mListener;
    }
    private ArrayList<MonthlySavingCostDataClass> arrayList;
    private int type;
    public MonthlySavingAdapter(ArrayList<MonthlySavingCostDataClass> arrayList, int type) {
        this.arrayList = arrayList;
        this.type=type;
    }
    public void updateData(ArrayList<MonthlySavingCostDataClass> arrayList,int type)
    {
        this.arrayList =arrayList;
        this.type=type;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public helper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_active_annual_cost,parent,false);
        return new helper(v);
    }

    @Override
    public void onBindViewHolder(@NonNull helper holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.savedMoney.setText(arrayList.get(position).getMoney());
        String range= arrayList.get(position).getRange();
        if (range.contains("-")) {
            String[] parts = range.split("-");
            range=parts[1];
            holder.itemCost.setText(parts[1]);
            holder.savedMoney.setText(parts[1]);

        } else {
            holder.itemCost.setText(range);
            holder.savedMoney.setText(range);
        }
        holder.progressBar.setMax(Integer.parseInt(range.replace(" SAR", "")));
        holder.progressBar.setProgress(Integer.parseInt(range.replace(" SAR", "")));

        if (type==1){
            holder.delete.setImageResource(R.drawable.baseline_replay_24);
        }
        else{
            holder.delete.setImageResource(R.drawable.baseline_delete_24);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type==1) {
                    mListener.returnItem(position);
                }
                else{
                    mListener.delete(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class helper extends RecyclerView.ViewHolder{
        TextView name,savedMoney,itemCost;
        ProgressBar progressBar;
        ImageView delete;
        public helper(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            savedMoney=itemView.findViewById(R.id.savedMoney);
            itemCost=itemView.findViewById(R.id.itemCost);
            progressBar=itemView.findViewById(R.id.progressBar);
            delete=itemView.findViewById(R.id.delete);


        }
    }
}
