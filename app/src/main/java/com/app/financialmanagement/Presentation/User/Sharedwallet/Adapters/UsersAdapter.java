package com.app.financialmanagement.Presentation.User.Sharedwallet.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.financialmanagement.Domain.UserDataClass;
import com.app.financialmanagement.Domain.UserWalletDataClass;
import com.app.financialmanagement.R;
import com.app.financialmanagement.Util.CommonData;

import java.util.ArrayList;
import java.util.Iterator;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.helper> {
    private ArrayList<UserDataClass> arrayList;
    public UsersAdapter(ArrayList<UserDataClass> arrayList) {
        this.arrayList = arrayList;
    }
    public void updateData(ArrayList<UserDataClass> arrayList)
    {
        this.arrayList=arrayList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public helper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_adapter, parent, false);
        return new helper(v);
    }

    @Override
    public void onBindViewHolder(@NonNull helper holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(arrayList.get(position).getUserName());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    CommonData.selectUsers.add(new UserWalletDataClass(arrayList.get(position).getUserName(),arrayList.get(position).getUserId()));
                }else {
                    Iterator<UserWalletDataClass> iterator = CommonData.selectUsers.iterator();
                    while (iterator.hasNext()) {
                        UserWalletDataClass user = iterator.next();
                        if (user.getUserName().equals(arrayList.get(position).getUserName()) && user.getUserId().equals(arrayList.get(position).getUserName())) {
                            iterator.remove();
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class helper extends RecyclerView.ViewHolder {
        TextView name;
        CheckBox checkBox;
        public helper(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userName);
            checkBox = itemView.findViewById(R.id.checked);
          }
    }
}
