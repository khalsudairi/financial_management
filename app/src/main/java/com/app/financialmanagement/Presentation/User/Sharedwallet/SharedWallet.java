package com.app.financialmanagement.Presentation.User.Sharedwallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.financialmanagement.Domain.CalenderNotificationDataClass;
import com.app.financialmanagement.Domain.UserWalletDataClass;
import com.app.financialmanagement.Domain.WalletDataClass;
import com.app.financialmanagement.Presentation.User.FinancialStatements.MonthlySaving.MonthlySavingAdapter;
import com.app.financialmanagement.Presentation.User.NotificationUser.CalenderAdapter;
import com.app.financialmanagement.Presentation.User.Sharedwallet.Adapters.WalletAdapter;
import com.app.financialmanagement.R;
import com.app.financialmanagement.Util.CommonData;
import com.app.financialmanagement.Util.Dialoge.SweetDialog;
import com.app.financialmanagement.databinding.FragmentSharedWalletBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SharedWallet extends Fragment {
    private FragmentSharedWalletBinding mBinding;
    private DatabaseReference database;
    private String userId;
    private SweetAlertDialog loading;
    private ArrayList<WalletDataClass> arrayList;
    private WalletAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding=FragmentSharedWalletBinding.inflate(inflater,container,false);
        mBinding.addWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToScreen(R.id.action_sharedWallet_to_addWallet);
            }
        });
        funLoading();
        database = FirebaseDatabase.getInstance().getReference(CommonData.walletDataBaseClass);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        defineItems();
        clickDeleteItem();
        return mBinding.getRoot();
    }

    private void defineItems() {
        arrayList = new ArrayList<>();
        adapter = new WalletAdapter(arrayList,getContext(),userId);
        mBinding.users.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.users.setAdapter(adapter);
        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                loading.dismiss();
                if (snapshot.exists()) {
                    mBinding.noData.setVisibility(View.GONE);
                    for (DataSnapshot data : snapshot.getChildren()) {
                        WalletDataClass  item = data.getValue(WalletDataClass.class);
                        if  (item.getUserId().equals(userId)) {
                            arrayList.add(item);
                        }
                        for (UserWalletDataClass user:item.getUsers()){
                            if (user.getUserId().equals(userId)){
                                arrayList.add(item);
                            }
                        }
                    }
                    if (arrayList.isEmpty()) {
                        mBinding.noData.setVisibility(View.VISIBLE);
                    }
                } else {
                    mBinding.noData.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void clickDeleteItem() {
        adapter.setOnItemClickListener(new WalletAdapter.onItemClickListener() {
            @Override
            public void delete(int position) {
                alertDialog(position);
            }
        });
    }

    private void alertDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Item")
                .setMessage("Are you sure you need to Delete this Item?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteItem(position);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteItem(int position) {
        database.child(arrayList.get(position).getWalletId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                funSuccessfully();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                funField("Failed to delete Item");
            }
        });
    }

    private void funSuccessfully() {
        SweetAlertDialog success = SweetDialog.success(getContext(), "Delete successfully");
        success.show();
    }

    private void funField(String message) {
        SweetAlertDialog field = SweetDialog.failed(getContext(), message);
        field.show();
    }
    private void funLoading() {
        loading = SweetDialog.loading(getContext());
        loading.show();
    }

    private void navigateToScreen(int id) {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(id);
    }
}