package com.app.financialmanagement.Presentation.User.Sharedwallet;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.financialmanagement.Domain.UserDataClass;
import com.app.financialmanagement.Presentation.User.Sharedwallet.Adapters.UsersAdapter;
import com.app.financialmanagement.R;
import com.app.financialmanagement.Util.CommonData;
import com.app.financialmanagement.Util.Dialoge.SweetDialog;
import com.app.financialmanagement.databinding.FragmentSearchForUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SearchForUser extends Fragment {
    private FragmentSearchForUserBinding mBinding;
    private DatabaseReference database;
    private String userId;
    private SweetAlertDialog loading;
    private ArrayList<UserDataClass> arrayList,searchList=new ArrayList<>();
    private UsersAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentSearchForUserBinding.inflate(inflater, container, false);

        mBinding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToScreen(R.id.action_search_to_addWallet);
            }
        });
        mBinding.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).popBackStack();
            }
        });
        funLoading();
        database = FirebaseDatabase.getInstance().getReference(CommonData.userDataBaseClass);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        defineItems();
        search();
        return mBinding.getRoot();
    }

    private void defineItems() {
        arrayList = new ArrayList<>();
        adapter = new UsersAdapter(arrayList);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recycler.setAdapter(adapter);
        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loading.dismiss();
                arrayList.clear();
                if (snapshot.exists()) {
                    mBinding.noData.setVisibility(View.GONE);
                    for (DataSnapshot data : snapshot.getChildren()) {
                        UserDataClass item = data.getValue(UserDataClass.class);
                        if (!item.getUserId().equals(userId)) {
                            arrayList.add(item);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    mBinding.noData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void funLoading() {
        loading = SweetDialog.loading(getContext());
        loading.show();
    }
    private void search()
    {
        mBinding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchForItem(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void searchForItem(CharSequence s) {
        searchList.clear();
        for(UserDataClass user:arrayList)
        {
            if (user.getUserName().contains(s))
            {
                searchList.add(user);
            }
        }
        adapter.updateData(searchList);
    }

    private void navigateToScreen(int id) {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(id);
    }
}