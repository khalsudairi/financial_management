package com.app.financialmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.app.financialmanagement.Presentation.Auth.Login.Login;
import com.app.financialmanagement.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,new Login()).commit();

        mainBinding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.login)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,new Login()).addToBackStack(null).commit();
                }
                else if (item.getItemId()==R.id.aboutUs)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,new AboutUs()).addToBackStack(null).commit();
                }
                return true;
            }
        });
    }
}