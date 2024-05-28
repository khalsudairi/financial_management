package com.app.financialmanagement.Presentation.User.FinacialPlane;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.app.financialmanagement.Domain.MonthlySavingCostDataClass;
import com.app.financialmanagement.Domain.PlaneDataClass;
import com.app.financialmanagement.Domain.ViewData;
import com.app.financialmanagement.R;
import com.app.financialmanagement.Util.CommonData;
import com.app.financialmanagement.Util.Dialoge.SweetDialog;
import com.app.financialmanagement.databinding.FragmentYearPlanDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.models.PieModel;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class YearPlanDetails extends Fragment {
    private FragmentYearPlanDetailsBinding mBinding;
    private int monthlyCost = 0,annualSaving=0,saving = 0, daily = 0, debts = 0, hobbies = 0, wallet = 0, previousMonthRest = 0, thisMonthRest = 0, totalIncome = 0;
    private DatabaseReference database;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentYearPlanDetailsBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        mBinding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(R.id.action_yearDetails_to_financialPlane);
            }
        });
        getData();
        return mBinding.getRoot();
    }

    private void getData() {
        database.child(CommonData.planeDataBaseClass).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        PlaneDataClass p = data.getValue(PlaneDataClass.class);
                        monthlyCost+= p.getMonthlyCost();
                        saving+=p.getSaving();
                        daily+=p.getDailyCost();
                        debts+=p.getDebts();
                        hobbies+=p.getHobbies();
                        wallet+=p.getWallet();

                    }
                    getSavingCost();
                } else {
                    funLoginField("Please add items to your financial plan");
                    Navigation.findNavController(getView()).popBackStack();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getSavingCost() {
        database.child(CommonData.annualCostDataBaseClass).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        MonthlySavingCostDataClass goal = data.getValue(MonthlySavingCostDataClass.class);
                        if (goal.getUserId().equals(userId)) {
                            int cost=calculateTheRange(goal.getRange());
                            annualSaving+=cost;
                        }
                    }
                    addDataToChart();
                }
                else{
                    addDataToChart();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addDataToChart() {
        mBinding.piechart.addPieSlice(
                new PieModel(
                        "",
                        (daily+hobbies),
                        Color.parseColor("#66BB6A")));
        mBinding.piechart.addPieSlice(
                new PieModel(
                        "",
                        debts,
                        Color.parseColor("#cdd9d3")));
        mBinding.piechart.addPieSlice(
                new PieModel(
                        "",
                        saving,
                        Color.parseColor("#f7c85c")));
        mBinding.piechart.addPieSlice(
                new PieModel(
                        "",
                        annualSaving,
                        Color.parseColor("#A2A6A4")));
        mBinding.piechart.addPieSlice(
                new PieModel(
                        "",
                        monthlyCost,
                        Color.parseColor("#242424")));
        mBinding.piechart.startAnimation();


        mBinding.monthlyCost.setText(monthlyCost+" SAR");
        mBinding.exposesCost.setText((daily+hobbies)+" SAR");
        mBinding.debtsMoney.setText(debts+" SAR");
        mBinding.annualGoals.setText(saving+" SAR");
        mBinding.savingCost.setText(annualSaving+" SAR");
    }
    private int calculateTheRange(String range){
        String cost="0";
        if (range.contains("-")) {
            String[] parts = range.split("-");

            cost=parts[1].replace(" SAR","");
        }
        else{
            cost=range;
        }

        return Integer.parseInt(cost);
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

    private void navigateTo(int id) {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(id);

    }
}