package com.app.financialmanagement.Presentation.User.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.app.financialmanagement.Util.CommonData;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MonthlyDeleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        if (currentDayOfMonth == 1) {
            Log.e("this","delete");
            FirebaseDatabase.getInstance().getReference(CommonData.monthlyDataBaseClass).removeValue();
            FirebaseDatabase.getInstance().getReference(CommonData.annualCostDataBaseClass).removeValue();
            FirebaseDatabase.getInstance().getReference(CommonData.dailyDataBaseClass).removeValue();
            FirebaseDatabase.getInstance().getReference(CommonData.hobbiesDataBaseClass).removeValue();
            FirebaseDatabase.getInstance().getReference(CommonData.monthlySavingDataBaseClass).removeValue();
        }
    }
}

