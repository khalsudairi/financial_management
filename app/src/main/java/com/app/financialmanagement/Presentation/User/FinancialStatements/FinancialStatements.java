package com.app.financialmanagement.Presentation.User.FinancialStatements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.app.financialmanagement.R;
import com.app.financialmanagement.databinding.FragmentFinancialStatementsBinding;

public class FinancialStatements extends Fragment {
    private FragmentFinancialStatementsBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentFinancialStatementsBinding.inflate(inflater, container, false);
        mBinding.dailyExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToScreen(R.id.action_financialStatement_to_dailyExpenses);
            }
        });
        mBinding.hobbies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToScreen(R.id.action_financialStatement_to_hobbies);
            }
        });
        mBinding.monthlyCosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToScreen(R.id.action_financialStatement_to_monthlyCosts);
            }
        });
        mBinding.annualCosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToScreen(R.id.action_financialStatement_to_annualCosts);
            }
        });
        mBinding.monthlySavings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToScreen(R.id.action_financialStatement_to_monthlySavings);
            }
        });
        return mBinding.getRoot();
    }
    private void navigateToScreen(int id) {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(id);
    }
}