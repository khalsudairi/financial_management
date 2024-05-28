package com.app.financialmanagement.Presentation.User.FinancialStatements.DailyExpenses;

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
import com.app.financialmanagement.Domain.DailyExpensesDataClass;
import com.app.financialmanagement.R;

import java.util.ArrayList;

public class DailyExpensesAdapter extends  RecyclerView.Adapter<DailyExpensesAdapter.helper>{
    private onItemClickListener mListener;
    public interface onItemClickListener
    {
        void delete(int position);
    }
    public void setOnItemClickListener(onItemClickListener mListener)
    {
        this.mListener=mListener;
    }
    private ArrayList<DailyExpensesDataClass> arrayList;
    private int type;
    public DailyExpensesAdapter( ArrayList<DailyExpensesDataClass> arrayList, int type) {
        this.arrayList = arrayList;
        this.type=type;
    }
    public void updateData( ArrayList<DailyExpensesDataClass> arrayList,int type)
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
            holder.delete.setVisibility(View.GONE);
        }
        else{
            holder.delete.setVisibility(View.VISIBLE);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.delete(position);
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
