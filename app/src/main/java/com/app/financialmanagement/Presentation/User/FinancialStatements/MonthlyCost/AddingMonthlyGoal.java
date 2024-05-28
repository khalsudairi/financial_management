package com.app.financialmanagement.Presentation.User.FinancialStatements.MonthlyCost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.app.financialmanagement.Domain.DailyExpensesDataClass;
import com.app.financialmanagement.Domain.MonthlyGoalsDataClass;
import com.app.financialmanagement.R;
import com.app.financialmanagement.Util.CommonData;
import com.app.financialmanagement.Util.Dialoge.SweetDialog;
import com.app.financialmanagement.databinding.FragmentAddingMonthlyGoalBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddingMonthlyGoal extends Fragment {
    private FragmentAddingMonthlyGoalBinding mBinding;
    private DatabaseReference database;
    private String userId;
    private SweetAlertDialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAddingMonthlyGoalBinding.inflate(inflater, container, false);
        goalType();
        range();
        status();
        mBinding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_addingMonthlyCost_to_monthlyCosts);

            }
        });
        database = FirebaseDatabase.getInstance().getReference(CommonData.monthlyDataBaseClass);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        clickAdd();
        return mBinding.getRoot();
    }

    private void status() {
        List<String> status = new ArrayList<>();
        status.add("Goal Status");
        status.add("Fixed");
        status.add("Variable");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_items, status);
        mBinding.status.setAdapter(adapter);
    }

    private void goalType() {
        List<String> goals = new ArrayList<>();
        goals.add("Goal Type");
        goals.add("Electric Bills");
        goals.add("Water Bills");
        goals.add("Rent or Mortgage");
        goals.add("Insurance Premiums");
        goals.add("Phone Bills");
        goals.add("Internet and Cable Bills");
        goals.add("Medical Bills");
        goals.add("Other");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_items, goals);
        mBinding.goals.setAdapter(adapter);
        mBinding.goals.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (goals.get(position).equals("Other")) {
                    mBinding.otherGoals.setVisibility(View.VISIBLE);
                } else {
                    mBinding.otherGoals.setVisibility(View.GONE);
                    mBinding.otherGoals.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    private void clickAdd() {
        mBinding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mBinding.goals.getSelectedItem().toString();
                String otherItems = mBinding.otherGoals.getText().toString();
                String range = mBinding.range.getSelectedItem().toString();
                String otherRange = mBinding.otherRange.getText().toString();
                String type = mBinding.status.getSelectedItem().toString();
                if (name.equals("Select  Range") && otherItems.isEmpty()) {
                    Toast.makeText(getActivity(), "Please select Monthly Goal Type", Toast.LENGTH_SHORT).show();
                } else if (range.equals("Select  Range") && otherRange.isEmpty()) {
                    Toast.makeText(getActivity(), "Please select the Range", Toast.LENGTH_SHORT).show();
                } else if (type.equals("Goal Status")) {
                    Toast.makeText(getActivity(), "Please select the Range", Toast.LENGTH_SHORT).show();
                } else {
                    funLoading();
                    String id = UUID.randomUUID().toString();
                    MonthlyGoalsDataClass dataClass;
                    if (!otherRange.isEmpty()) {
                        if (!otherItems.isEmpty()) {
                            dataClass = new MonthlyGoalsDataClass(otherItems, type, otherRange, "0", "active", id, userId);
                        } else {
                            dataClass = new MonthlyGoalsDataClass(name, type, otherRange, "0", "active", id, userId);
                        }
                    } else {
                        if (!otherItems.isEmpty()) {
                            dataClass = new MonthlyGoalsDataClass(otherItems, type, range, "0", "active", id, userId);
                        } else {
                            dataClass = new MonthlyGoalsDataClass(name, type, range, "0", "active", id, userId);
                        }
                    }
                    saveToDatabase(dataClass);
                }
            }
        });
    }

    private void saveToDatabase(MonthlyGoalsDataClass dataClass) {
        database.child(dataClass.getGoalId()).setValue(dataClass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                loading.dismiss();
                funSuccessfully();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismiss();
                funLoginField("Failed to add Monthly Goal please try again ");
            }
        });
    }

    private void funLoading() {
        loading = SweetDialog.loading(getContext());
        loading.show();
    }

    private void funSuccessfully() {
        SweetAlertDialog success = SweetDialog.success(getContext(), "adding Monthly Goal successfully");
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