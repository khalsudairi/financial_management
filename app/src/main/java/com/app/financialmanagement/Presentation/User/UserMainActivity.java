package com.app.financialmanagement.Presentation.User;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.Navigation;

import com.app.financialmanagement.Domain.PlaneDataClass;
import com.app.financialmanagement.Domain.UserDataClass;
import com.app.financialmanagement.Presentation.SendNotificationPack.APIService;
import com.app.financialmanagement.Presentation.SendNotificationPack.Client;
import com.app.financialmanagement.Presentation.User.Alarm.MonthlyDeleteReceiver;
import com.app.financialmanagement.R;
import com.app.financialmanagement.Util.CommonData;
import com.app.financialmanagement.Util.Helper.HelperLocalLanguage;
import com.app.financialmanagement.databinding.ActivityUserMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.time.LocalDate;
import java.util.Calendar;

public class UserMainActivity extends AppCompatActivity {
    private ActivityUserMainBinding mainBinding;
    private APIService apiService;
    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HelperLocalLanguage.setLocale(this, "en");
        mainBinding = ActivityUserMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        mainBinding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.financialStatements) {
                    navigateToScreen(R.id.financialStatement);
                } else if (item.getItemId() == R.id.plane) {
                    navigateToScreen(R.id.financialPlane);
                } else if (item.getItemId() == R.id.wallet) {
                    navigateToScreen(R.id.sharedWallet);
                } else if (item.getItemId() == R.id.calender) {
                    navigateToScreen(R.id.calender);
                } else if (item.getItemId() == R.id.profile) {
                    navigateToScreen(R.id.profile);
                }
                return true;
            }
        });
        getDataFromFirebase();
        updateToken();
        scheduleMonthlyDeleteAlarm(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);

            } else {
                // repeat the permission or open app details
            }
        }
    }

    private void updateToken() {
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get new FCM registration token
                    userToken = task.getResult();
                    System.out.println(userToken);
                    FirebaseDatabase.getInstance().getReference("tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("token").setValue(userToken);
                });
    }

    private void scheduleMonthlyDeleteAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MonthlyDeleteReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Set the alarm to start at the beginning of each month
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // Set the alarm to trigger at the beginning of each month
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void navigateToScreen(int id) {
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(id);
    }

    private void getDataFromFirebase() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(CommonData.userDataBaseClass);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        database.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.child("userName").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String phoneNumber = snapshot.child("phoneNumber").getValue().toString();
                String income = snapshot.child("income").getValue().toString();
                String free = snapshot.child("budget").getValue().toString();
                String familyNumber = snapshot.child("familyNumber").getValue().toString();
                String userId = snapshot.child("userId").getValue().toString();
                UserDataClass user = new UserDataClass(userName, email, phoneNumber, income, free, familyNumber, userId);
                CommonData.userData = user;
                getTheRest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getTheRest() {
        DatabaseReference database=FirebaseDatabase.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        int month=getTheMonth();
        database.child(CommonData.planeDataBaseClass).child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot data:snapshot.getChildren())
                        {
                            PlaneDataClass p=data.getValue(PlaneDataClass.class);
                            if (p.getMonthNumber()==month-1)
                            {
                                CommonData.previousMonthRest =p.getRest();
                            } else if (p.getMonthNumber() == month) {
                                CommonData.hobbiesPercent=p.getHobbiesPercent();
                                CommonData.savingPercent=p.getSavingPercent();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    private int getTheMonth() {
        LocalDate currentDate = null;
        int month = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
            month = currentDate.getMonthValue();//2024/4/19
        }
        return month;
    }
}