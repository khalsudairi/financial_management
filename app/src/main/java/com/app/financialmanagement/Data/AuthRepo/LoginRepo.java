package com.app.financialmanagement.Data.AuthRepo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

public class LoginRepo {
    FirebaseAuth auth;
    @Inject
    public LoginRepo(FirebaseAuth auth)
    {
        this.auth=auth;
    }
    public LiveData<String> signIn(String email, String password) {
        MutableLiveData<String> userLiveData = new MutableLiveData<>();

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    userLiveData.setValue("success");
                })
                .addOnFailureListener(e -> {
                    userLiveData.setValue("field");
                    // Handle authentication failure
                });

        return userLiveData;
    }
}
