package com.app.financialmanagement.Presentation.User.FinancialStatements.MonthlySaving;

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
import com.app.financialmanagement.Domain.MonthlySavingCostDataClass;
import com.app.financialmanagement.R;
import com.app.financialmanagement.Util.CommonData;
import com.app.financialmanagement.Util.Dialoge.SweetDialog;
import com.app.financialmanagement.databinding.FragmentAddingMonthlySavingBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddingMonthlySaving extends Fragment {
    private FragmentAddingMonthlySavingBinding mBinding;
    private DatabaseReference database;
    private String userId;
    private SweetAlertDialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding=FragmentAddingMonthlySavingBinding.inflate(inflater,container,false);

        mBinding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_addingMonthlySaving_to_monthlySavings);

            }
        });
        range();
        database = FirebaseDatabase.getInstance().getReference(CommonData.monthlySavingDataBaseClass);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        clickAdd();
        return mBinding.getRoot();
    }

    private void clickAdd() {
        mBinding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mBinding.name.getText().toString();
                String range = mBinding.range.getSelectedItem().toString();
                String otherRange = mBinding.otherRange.getText().toString();
                if (name.isEmpty()) {
                    mBinding.name.setError("Please the Monthly Saving Name");
                } else if (range.equals("Select  Range") && otherRange.isEmpty()) {
                    Toast.makeText(getActivity(), "Please select the Range", Toast.LENGTH_SHORT).show();
                } else {
                    funLoading();
                    String id = UUID.randomUUID().toString();
                    MonthlySavingCostDataClass dataClass;
                    if (!otherRange.isEmpty()) {
                        dataClass = new MonthlySavingCostDataClass(name, otherRange, "0", "active", userId, id);
                    } else {
                        dataClass = new MonthlySavingCostDataClass(name, range, "0", "active", userId, id);
                    }
                    saveToDatabase(dataClass);
                }
            }
        });
    }

    private void saveToDatabase(MonthlySavingCostDataClass dataClass) {
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
                funLoginField("Failed to add Monthly Saving try again ");
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
        SweetAlertDialog success = SweetDialog.success(getContext(), "adding successfully");
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