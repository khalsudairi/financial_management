package com.app.financialmanagement.Presentation.Auth.Registration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.app.financialmanagement.Data.AuthRepo.RegistrationRepo;
import com.app.financialmanagement.Domain.UserDataClass;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RegistrationViewModel extends ViewModel {
    RegistrationRepo repo;

    @Inject
    RegistrationViewModel(RegistrationRepo repo)
    {
        this.repo=repo;
    }
    public LiveData<String> signUp(String email, String password) {
        return repo.signUp(email, password);
    }
    public void addUserData(UserDataClass user)
    {
        repo.addUserData(user);
    }
}
