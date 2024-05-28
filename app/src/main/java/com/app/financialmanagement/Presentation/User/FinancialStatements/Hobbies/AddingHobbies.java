package com.app.financialmanagement.Presentation.User.FinancialStatements.Hobbies;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.app.financialmanagement.Domain.AnnualCostDataClass;
import com.app.financialmanagement.Domain.HobbiesDataClass;
import com.app.financialmanagement.R;
import com.app.financialmanagement.Util.CommonData;
import com.app.financialmanagement.Util.Dialoge.SweetDialog;
import com.app.financialmanagement.databinding.FragmentAddingHobbiesBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddingHobbies extends Fragment {
    private FragmentAddingHobbiesBinding mBinding;
    private DatabaseReference database;
    private String userId;
    private SweetAlertDialog loading;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding=FragmentAddingHobbiesBinding.inflate(inflater,container,false);
        mBinding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_addingHobbies_to_hobbies);

            }
        });
        range();
        database = FirebaseDatabase.getInstance().getReference(CommonData.hobbiesDataBaseClass);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        clickAdd();
        return mBinding.getRoot();
    }

    private void clickAdd() {
        mBinding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mBinding.hobbyName.getText().toString();
                String practice = mBinding.number.getText().toString();
                String range = mBinding.range.getSelectedItem().toString();
                String otherRange = mBinding.otherRange.getText().toString();
                if (name.isEmpty()) {
                    mBinding.hobbyName.setError("Please enter the Hobby Name");
                } if (practice.isEmpty()) {
                    mBinding.number.setError("Please enter How many times does he practice the hobby a week?");
                } else if (range.equals("Select  Range") && otherRange.isEmpty()) {
                    Toast.makeText(getActivity(), "Please select the Range", Toast.LENGTH_SHORT).show();
                } else {
                    funLoading();
                    String id = UUID.randomUUID().toString();
                    HobbiesDataClass dataClass;
                    if (!otherRange.isEmpty()) {
                        dataClass = new HobbiesDataClass(name,practice, otherRange, "0", "active", id,userId);
                    } else {
                        dataClass = new HobbiesDataClass(name,practice, range, "0", "active", id,userId);
                    }
                    saveToDatabase(dataClass);
                }
            }
        });
    }

    private void saveToDatabase(HobbiesDataClass dataClass) {
        database.child(dataClass.getId()).setValue(dataClass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                loading.dismiss();
                funSuccessfully();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismiss();
                funLoginField("Failed to add Annual cost please try again ");
            }
        });
    }

    private void range() {
        List<String> ranges = new ArrayList<>();
        ranges.add("Select  Range");
        ranges.add("50-100 SAR");
        ranges.add("100-500 SAR");
        ranges.add("500-1000 SAR");
        ranges.add("1000-1500 SAR");
        ranges.add("1500-2000 SAR");
        ranges.add("2000-3000 SAR");
        ranges.add("3000-4000 SAR");
        ranges.add("Other");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_items, ranges);
        mBinding.range.setAdapter(adapter);
        mBinding.range.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (ranges.get(position).equals("Other")) {
                    mBinding.otherRange.setVisibility(View.VISIBLE);
                } else {
                    mBinding.otherRange.setVisibility(View.GONE);
                    mBinding.otherRange.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void funLoading() {
        loading = SweetDialog.loading(getContext());
        loading.show();
    }

    private void funSuccessfully() {
        SweetAlertDialog success = SweetDialog.success(getContext(), "adding Annual cost  successfully");
        success.show();
        Navigation.findNavController(getView()).popBackStack();
    }

    private void funLoginField(String message) {
        SweetAlertDialog field = SweetDialog.failed(getContext(), message);
        field.show();
        field.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                field.dismiss();
            }
        });
    }
}