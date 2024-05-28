package com.app.financialmanagement.Data.AuthRepo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.financialmanagement.Domain.UserDataClass;
import com.app.financialmanagement.Util.CommonData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

public class RegistrationRepo {
    FirebaseAuth auth;
    DatabaseReference database;
    @Inject
    public RegistrationRepo(FirebaseAuth auth,DatabaseReference database)
    {
        this.auth=auth;
        this.database=database;
    }
    public LiveData<String> signUp(String email, String password) {
        MutableLiveData<String> userLiveData = new MutableLiveData<>();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    userLiveData.setValue(authResult.getUser().getUid().toString());
                })
                .addOnFailureListener(e -> {
                    userLiveData.setValue("field");
                    // Handle authentication failure
                });

        return userLiveData;
    }
    public void addUserData(UserDataClass user)
    {
        database.child(CommonData.userDataBaseClass).child(user.getUserId()).setValue(user);
    }
}
