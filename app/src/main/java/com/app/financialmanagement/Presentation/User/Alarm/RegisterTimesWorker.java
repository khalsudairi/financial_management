package com.app.financialmanagement.Presentation.User.Alarm;


import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.app.financialmanagement.Domain.CalenderNotificationDataClass;
import com.app.financialmanagement.Util.CommonData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RegisterTimesWorker extends Worker {

    public RegisterTimesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Result doWork() {
        try {
            Calendar calendar = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                calendar = Calendar.getInstance();
            }
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE) ;
            int  seconds= calendar.get(Calendar.SECOND) ;
            ArrayList<CalenderNotificationDataClass> times=new ArrayList<>();
            Log.e("hour",hour+"");
            DatabaseReference data= FirebaseDatabase.getInstance().getReference(CommonData.calenderSavingDataBaseClass);
            String userId= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
            data.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        times.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            CalenderNotificationDataClass  item = data.getValue(CalenderNotificationDataClass.class);
                            if  (item.getUserId().equals(userId)) {
                                times.add(item);
                            }
                        }
                        alarm(times,hour,minute,seconds);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            return Result.failure();
        }catch (Exception e)
        {
            return Result.retry();
        }
    }

    private void alarm(ArrayList<CalenderNotificationDataClass> times, int hour, int minute, int seconds) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            times.forEach(s ->{
                String time="00"+":"+"00";
                String selectTime= "00"+":"+"00"+":"+"00";
                String alarmTime = "" + s.getDate() + " " +s.getMissionName()+time;//unique for every alarm
                String currentTime=hour+":"+minute+":"+seconds;
                long delay = CommonData.calculateDelay(currentTime, selectTime);
                Log.e("delay",delay+"");
                Log.e("current time",currentTime+"");
                Log.e("alarm time",time+"");

                if (delay > 0) {
                    Data input = new Data.Builder()
                            .putString("title", "Financial Management")
                            .putString("content", s.getMissionName())
                            .build();

                    OneTimeWorkRequest registerPrayerRequest = new OneTimeWorkRequest
                            .Builder(AlarmNotification.class)
                            .addTag(alarmTime)
                            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                            .setInputData(input)
                            .build();

                    WorkManager.getInstance(getApplicationContext())
                            .enqueueUniqueWork(alarmTime, ExistingWorkPolicy.REPLACE, registerPrayerRequest);
                }
            });
        }

    }
}

