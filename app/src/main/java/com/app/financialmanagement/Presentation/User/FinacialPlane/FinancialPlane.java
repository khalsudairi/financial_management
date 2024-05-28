package com.app.financialmanagement.Presentation.User.FinacialPlane;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.app.financialmanagement.Domain.DailyExpensesDataClass;
import com.app.financialmanagement.Domain.HobbiesDataClass;
import com.app.financialmanagement.Domain.MonthlyGoalsDataClass;
import com.app.financialmanagement.R;
import com.app.financialmanagement.Util.CommonData;
import com.app.financialmanagement.Util.Dialoge.SweetDialog;
import com.app.financialmanagement.databinding.FragmentFinancialPlaneBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FinancialPlane extends Fragment {
    private FragmentFinancialPlaneBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentFinancialPlaneBinding.inflate(inflater, container, false);
        mBinding.month1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTheMonth() == 1) {
                    CommonData.monthNumber=1;
                    CommonData.monthName = "January";
                    navigateTo(R.id.action_financialPlane_to_planeDetails);
                } else {
                    funLoginFailed("You cannot view the financial plan for this month");
                }
            }
        });
        mBinding.month2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTheMonth() == 2) {
                    CommonData.monthNumber=2;
                    CommonData.monthName = "February";
                    navigateTo(R.id.action_financialPlane_to_planeDetails);
                } else {
                    funLoginFailed("You cannot view the financial plan for this month");
                }
            }
        });
        mBinding.month3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTheMonth() == 3) {
                    CommonData.monthNumber=3;
                    CommonData.monthName = "March";
                    navigateTo(R.id.action_financialPlane_to_planeDetails);
                } else {
                    funLoginFailed("You cannot view the financial plan for this month");
                }
            }
        });
        mBinding.month4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTheMonth() == 4) {
                    CommonData.monthNumber=4;
                    CommonData.monthName = "April";
                    navigateTo(R.id.action_financialPlane_to_planeDetails);
                } else {
                    funLoginFailed("You cannot view the financial plan for this month");
                }
            }
        });
        mBinding.month5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTheMonth() == 5) {
                    CommonData.monthNumber=5;
                    CommonData.monthName = "May";
                    navigateTo(R.id.action_financialPlane_to_planeDetails);
                } else {
                    funLoginFailed("You cannot view the financial plan for this month");
                }
            }
        });
        mBinding.month6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTheMonth() == 6) {
                    CommonData.monthName = "June";
                    CommonData.monthNumber=6;
                    navigateTo(R.id.action_financialPlane_to_planeDetails);
                } else {
                    funLoginFailed("You cannot view the financial plan for this month");
                }
            }
        });
        mBinding.month7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTheMonth() == 7) {
                    CommonData.monthNumber=7;
                    CommonData.monthName = "July";
                    navigateTo(R.id.action_financialPlane_to_planeDetails);
                } else {
                    funLoginFailed("You cannot view the financial plan for this month");
                }
            }
        });
        mBinding.month8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTheMonth() == 8) {
                    CommonData.monthNumber=8;
                    CommonData.monthName = "August";
                    navigateTo(R.id.action_financialPlane_to_planeDetails);
                } else {
                    funLoginFailed("You cannot view the financial plan for this month");
                }
            }
        });
        mBinding.month9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTheMonth() == 9) {
                    CommonData.monthNumber=9;
                    CommonData.monthName = "September";
                    navigateTo(R.id.action_financialPlane_to_planeDetails);
                } else {
                    funLoginFailed("You cannot view the financial plan for this month");
                }
            }
        });
        mBinding.month10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTheMonth() == 10) {
                    CommonData.monthName = "October";
                    CommonData.monthNumber=10;
                    navigateTo(R.id.action_financialPlane_to_planeDetails);
                } else {
                    funLoginFailed("You cannot view the financial plan for this month");
                }
            }
        });
        mBinding.month11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTheMonth() == 11) {
                    CommonData.monthName = "November";
                    CommonData.monthNumber=11;
                    navigateTo(R.id.action_financialPlane_to_planeDetails);
                } else {
                    funLoginFailed("You cannot view the financial plan for this month");
                }
            }
        });
        mBinding.month12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTheMonth() == 12) {
                    CommonData.monthName = "December";
                    CommonData.monthNumber=12;
                    navigateTo(R.id.action_financialPlane_to_planeDetails);
                } else {
                    funLoginFailed("You cannot view the financial plan for this month");
                }
            }
        });
        mBinding.year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(R.id.action_financialPlane_to_yearDetails);
            }
        });
        return mBinding.getRoot();
    }

    private void navigateTo(int id) {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(id);

    }

    private int getTheMonth() {
        LocalDate currentDate = null;
        int month = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
            month = currentDate.getMonthValue();
        }
        return month;
    }

    private void funLoginFailed(String message) {
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