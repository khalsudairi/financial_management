package com.app.financialmanagement.DI;


import static java.lang.reflect.Array.get;

import com.app.financialmanagement.Data.AuthRepo.LoginRepo;
import com.app.financialmanagement.Data.AuthRepo.RegistrationRepo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    LoginRepo getLoginRepo(FirebaseAuth auth)
    {
        return new LoginRepo(auth);
    }

    @Provides
    @Singleton
    RegistrationRepo getRegistrationRepo(FirebaseAuth auth,DatabaseReference database)
    {
        return new RegistrationRepo(auth,database);
    }

    @Singleton
    @Provides
    FirebaseAuth getFirebaseAuth()
    {
        return FirebaseAuth.getInstance();
    }

    @Singleton
    @Provides
    DatabaseReference getDatabase()
    {
        return FirebaseDatabase.getInstance().getReference();
    }
}
