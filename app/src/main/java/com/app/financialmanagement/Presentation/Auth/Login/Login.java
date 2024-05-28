package com.app.financialmanagement.Presentation.Auth.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.financialmanagement.Presentation.Auth.Registration.Registration;
import com.app.financialmanagement.Presentation.User.UserMainActivity;
import com.app.financialmanagement.R;
import com.app.financialmanagement.Util.Dialoge.SweetDialog;
import com.app.financialmanagement.databinding.FragmentLoginBinding;
import com.bumptech.glide.Glide;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Login extends Fragment {
    private FragmentLoginBinding mBinding;
    private SweetAlertDialog loading;
    private int pStatus = 0;
    private LoginViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentLoginBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
        mBinding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new Registration()).addToBackStack(null).commit();
            }
        });
        login();
        showPassword();
        return mBinding.getRoot();
    }

    private void funLoading() {
        loading = SweetDialog.loading(getContext());
        loading.show();
    }

    private void login() {
        mBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mBinding.emailEditText.getText().toString();
                String password = mBinding.passwordEditText.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    mBinding.emailEditText.setError("Please enter your email");
                } else if (TextUtils.isEmpty(password)) {
                    mBinding.passwordEditText.setError(" Please enter your password");
                } else {
                    funLoading();
                    checkAccount(email, password);
                }

            }
        });
    }

    private void checkAccount(String email, String password) {
        viewModel.signIn(email,password).observe(getViewLifecycleOwner(),status->{
            loading.dismiss();
            if (status.equals("success"))
            {
                funLoginSuccessfully();
            }
            else {
                funLoginField("Failed to sign in to your account Please check if the password correct or not");

            }
        });
    }

    private void funLoginSuccessfully() {
        SweetAlertDialog success = SweetDialog.success(getContext(), "login successfully");
        success.show();
        try {
            Thread.sleep(2000);
            startActivity(new Intent(getActivity(), UserMainActivity.class));
            getActivity().finish();
        } catch (Exception e) {

        }

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
    private void showPassword() {
        mBinding.showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pStatus == 0) {
                    Glide.with(Login.this)
                            .load(R.drawable.baseline_visibility_24)
                            .centerCrop()
                            .into(mBinding.showPassword);
                    pStatus = 1;
                    mBinding.passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    Glide.with(Login.this)
                            .load(R.drawable.baseline_visibility_off_24)
                            .centerCrop()
                            .into(mBinding.showPassword);
                    pStatus = 0;
                    mBinding.passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }
}