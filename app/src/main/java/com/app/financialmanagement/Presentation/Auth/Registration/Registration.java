package com.app.financialmanagement.Presentation.Auth.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.financialmanagement.Domain.UserDataClass;
import com.app.financialmanagement.Presentation.Auth.Login.Login;
import com.app.financialmanagement.Presentation.User.UserMainActivity;
import com.app.financialmanagement.R;
import com.app.financialmanagement.Util.Dialoge.SweetDialog;
import com.app.financialmanagement.Util.Validation.EmailValidator;
import com.app.financialmanagement.Util.Validation.PasswordValidation;
import com.app.financialmanagement.Util.Validation.ValidationPhoneNumber;
import com.app.financialmanagement.databinding.FragmentRegistrationBinding;
import com.bumptech.glide.Glide;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Registration extends Fragment {
    private FragmentRegistrationBinding mBinding;
    private SweetAlertDialog loading;
    private RegistrationViewModel viewModel;
    private int pStatus = 0;
    private int pStatus2 = 0;
    private UserDataClass user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentRegistrationBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(RegistrationViewModel.class);


        mBinding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new Login()).addToBackStack(null).commit();
            }
        });
        mBinding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new Login()).addToBackStack(null).commit();
            }
        });
        mBinding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEnteredData();
            }
        });
        showPassword();
        showPassword2();
        return mBinding.getRoot();

    }

    public void checkEnteredData() {
        String name = mBinding.userName.getText().toString();
        String familyNumber = mBinding.familyNumbre.getText().toString();
        String email = mBinding.emailEditText.getText().toString();
        String phoneNum = mBinding.phone.getText().toString();
        String password = mBinding.passwordEditText.getText().toString();
        String conPassword = mBinding.conPasswordEditText.getText().toString();
        String income = mBinding.income.getText().toString();
        String budget = mBinding.budget.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mBinding.userName.setError("Please enter your name");
        }if (TextUtils.isEmpty(familyNumber)) {
            mBinding.familyNumbre.setError("Please enter your Family Number");
        } else if (!EmailValidator.isValidEmail(email)) {
            mBinding.emailEditText.setError("please enter your email Correctly 'user@gmail.com'");
        } else if (!PasswordValidation.validatePassword(password)) {
            mBinding.passwordEditText.setError("Please enter Strong Password contain small,capital,spacial character and digits");
        } else if (!conPassword.equals(password)) {
            mBinding.conPasswordEditText.setError("password don't match");
        } else if (!ValidationPhoneNumber.validatePhoneNumber(phoneNum)) {
            mBinding.phone.setError("please enter your phone number start with(05) and contain 10 number  ");
        } else if (TextUtils.isEmpty(budget)) {
            mBinding.budget.setError("please enter your Budget ");
        } else if (TextUtils.isEmpty(income)) {
            mBinding.income.setError("please enter your income ");
        } else {
            funLoading();
            user=new UserDataClass(name,email,phoneNum,income,budget,familyNumber,"");
            createAccount(email, password);
        }
    }


    private void funLoading() {
        loading = SweetDialog.loading(getActivity());
        loading.show();
    }

    private void createAccount(String email, String password) {
        viewModel.signUp(email, password).observe(getViewLifecycleOwner(), Id -> {
            loading.dismiss();
            if (Id.equals("field")) {
                funField("failed to create account Please change your email");
            } else {
                funSuccessfully();
                user.setUserId(Id);
                viewModel.addUserData(user);
            }
        });
    }

    private void funSuccessfully() {
        SweetAlertDialog dialog = SweetDialog.success(getActivity(), "Create Account Successfully");
        dialog.show();
        try {
            Thread.sleep(1000);
            startActivity(new Intent(getActivity(), UserMainActivity.class));
            getActivity().finish();
        } catch (Exception e) {

        }

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

    private void showPassword() {
        mBinding.showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pStatus == 0) {
                    Glide.with(Registration.this)
                            .load(R.drawable.baseline_visibility_off_24)
                            .centerCrop()
                            .into(mBinding.showPassword);
                    pStatus = 1;
                    mBinding.passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    Glide.with(Registration.this)
                            .load(R.drawable.baseline_visibility_24)
                            .centerCrop()
                            .into(mBinding.showPassword);
                    pStatus = 0;
                    mBinding.passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    private void showPassword2() {
        mBinding.showPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pStatus2 == 0) {
                    Glide.with(Registration.this)
                            .load(R.drawable.baseline_visibility_off_24)
                            .centerCrop()
                            .into(mBinding.showPassword2);
                    pStatus2 = 1;
                    mBinding.conPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    Glide.with(Registration.this)
                            .load(R.drawable.baseline_visibility_24)
                            .centerCrop()
                            .into(mBinding.showPassword2);
                    pStatus2 = 0;
                    mBinding.conPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }
}