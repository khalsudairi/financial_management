package com.app.financialmanagement.Presentation.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.financialmanagement.Domain.UserDataClass;
import com.app.financialmanagement.MainActivity;
import com.app.financialmanagement.Util.CommonData;
import com.app.financialmanagement.Util.Dialoge.SweetDialog;
import com.app.financialmanagement.Util.Validation.ValidationPhoneNumber;
import com.app.financialmanagement.databinding.FragmentUserProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UserProfile extends Fragment {
    private FragmentUserProfileBinding mBinding;
    private DatabaseReference database;
    private String userId;
    private SweetAlertDialog loading;
    private UserDataClass user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentUserProfileBinding.inflate(inflater, container, false);
        mBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });
        mBinding.updateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEnteredData();
            }
        });
        funLoading();
         getDataFromFirebase();
        return mBinding.getRoot();
    }

    private void getDataFromFirebase() {
        database = FirebaseDatabase.getInstance().getReference(CommonData.userDataBaseClass);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        database.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loading.dismiss();
                String userName = snapshot.child("userName").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String phoneNumber = snapshot.child("phoneNumber").getValue().toString();
                String income = snapshot.child("income").getValue().toString();
                String budget = snapshot.child("budget").getValue().toString();
                String familyNumber = snapshot.child("familyNumber").getValue().toString();
                String userId = snapshot.child("userId").getValue().toString();
                user = new UserDataClass(userName, email, phoneNumber, income, budget, familyNumber, userId);
                addDatatoDesign();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addDatatoDesign() {
        mBinding.userName.setText(user.getUserName());
        mBinding.budget.setText(user.getBudget());
        mBinding.familyNumbre.setText(user.getFamilyNumber());
        mBinding.income.setText(user.getIncome());
        mBinding.phone.setText(user.getPhoneNumber());
    }

    public void checkEnteredData() {
        String name = mBinding.userName.getText().toString();
        String familyNumber = mBinding.familyNumbre.getText().toString();
        String phoneNum = mBinding.phone.getText().toString();
        String income = mBinding.income.getText().toString();
        String budget = mBinding.budget.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mBinding.userName.setError("Please enter your name");
        }
        if (TextUtils.isEmpty(familyNumber)) {
            mBinding.familyNumbre.setError("Please enter your Family Number");
        } else if (!ValidationPhoneNumber.validatePhoneNumber(phoneNum)) {
            mBinding.phone.setError("please enter your phone number start with(05) and contain 10 number  ");
        } else if (TextUtils.isEmpty(budget)) {
            mBinding.budget.setError("please enter your Budget ");
        }  else if (TextUtils.isEmpty(income)) {
            mBinding.income.setError("please enter your income ");
        }  else {
            funLoading();
            user.setUserName(name);
            user.setBudget(budget);
            user.setFamilyNumber(familyNumber);
            user.setPhoneNumber(phoneNum);
            user.setIncome(income);
            addUserData();
        }
    }

    public void addUserData() {
        database.child(user.getUserId()).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                CommonData.userData=user;
                funSuccessfully();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                funField("Failed to Update Profile Data Please try Again");
            }
        });
    }

    private void funSuccessfully() {
        SweetAlertDialog dialog = SweetDialog.success(getActivity(), "Update Profile Data Successfully");
        dialog.show();
    }

    private void funField(String title) {
        SweetAlertDialog dialog = SweetDialog.failed(getActivity(), title);
        dialog.show();
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismiss();
            }
        });
    }

    private void funLoading() {
        loading = SweetDialog.loading(getContext());
        loading.show();
    }

    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Log out")
                .setMessage("Are you sure you need to log out?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}