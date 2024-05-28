package com.app.financialmanagement.Presentation.User.NotificationUser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.app.financialmanagement.Domain.CalenderNotificationDataClass;
import com.app.financialmanagement.Presentation.User.Alarm.RegisterTimesWorker;
import com.app.financialmanagement.Util.CommonData;
import com.app.financialmanagement.Util.DateAndTime.CustomDatePickerDialog;
import com.app.financialmanagement.Util.Dialoge.SweetDialog;
import com.app.financialmanagement.databinding.FragmentCalendarBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CalendarFragment extends Fragment {
    private FragmentCalendarBinding mBinding;
    private DatabaseReference database;
    private String userId;
    private SweetAlertDialog loading;
    private ArrayList<CalenderNotificationDataClass> arrayList;
    private CalenderAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentCalendarBinding.inflate(inflater, container, false);

        mBinding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(0,1);
            }
        });
        database = FirebaseDatabase.getInstance().getReference(CommonData.calenderSavingDataBaseClass);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        clickAdd();
        defineItems();
        clickItem();
        return mBinding.getRoot();
    }

    private void defineItems() {
        arrayList = new ArrayList<>();
        adapter = new CalenderAdapter(arrayList);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recycler.setAdapter(adapter);
        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                if (snapshot.exists()) {
                    mBinding.noData.setVisibility(View.GONE);
                    for (DataSnapshot data : snapshot.getChildren()) {
                        CalenderNotificationDataClass  item = data.getValue(CalenderNotificationDataClass.class);
                        if  (item.getUserId().equals(userId)) {
                            arrayList.add(item);
                        }
                    }
                    if (arrayList.isEmpty()) {
                        mBinding.noData.setVisibility(View.VISIBLE);
                    }
                    else{
                        createAlarm();
                    }
                } else {
                    mBinding.noData.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clickAdd() {
        mBinding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mBinding.name.getText().toString();
                String cost = mBinding.cost.getText().toString();
                String date = mBinding.date.getText().toString();
                if (name.isEmpty()) {
                    mBinding.name.setError("Please the Mission Name");
                } else if (cost.isEmpty()) {
                    mBinding.cost.setError("Please the cost");
                } else if (date.isEmpty()) {
                    mBinding.date.setError("Please the date");
                } else {
                    funLoading();
                    String id = UUID.randomUUID().toString();
                    CalenderNotificationDataClass dataClass = new CalenderNotificationDataClass(name, cost, date, id, userId);
                    saveToDatabase(dataClass);
                }
            }
        });
    }

    private void saveToDatabase(CalenderNotificationDataClass dataClass) {
        database.child(dataClass.getId()).setValue(dataClass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                loading.dismiss();
                funSuccessfully("adding successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismiss();
                funField("Failed to add Notification please try again ");
            }
        });
    }
    private void showDatePickerDialog(int position, int i) {
        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(
                getActivity(),
                (view, year, month, dayOfMonth) -> {
                    // Handle date selection
                    String date = year + "/" + (month + 1) + "/" + dayOfMonth;
                    if (i==0){
                        CalenderNotificationDataClass dataClass=arrayList.get(position);
                        dataClass.setDate(date);
                        saveToDatabase(dataClass);
                    }
                    else{
                        mBinding.date.setText(date);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    private void clickItem()
    {
        adapter.setOnItemClickListener(new CalenderAdapter.onItemClickListener() {
            @Override
            public void delete(int position) {
                alertDialog(position);
            }

            @Override
            public void updateDate(int position) {
                showDatePickerDialog(position, 0);

            }
        });
    }
    private void alertDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Item")
                .setMessage("Are you sure you need to Delete this Item?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteItem(position);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteItem(int position) {
        database.child(arrayList.get(position).getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                funSuccessfully("Delete Item Successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                funField("Failed to delete Item");
            }
        });
    }

    private void funLoading() {
        loading = SweetDialog.loading(getContext());
        loading.show();
    }

    private void funSuccessfully(String title) {
        SweetAlertDialog success = SweetDialog.success(getContext(), title);
        success.show();
    }

    private void funField(String message) {
        SweetAlertDialog field = SweetDialog.failed(getContext(), message);
        field.show();
        field.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                field.dismiss();
            }
        });
    }

    private void createAlarm()
    {
        WorkManager.getInstance(getActivity().getApplicationContext()).cancelAllWork();
        PeriodicWorkRequest request=new PeriodicWorkRequest
                .Builder(RegisterTimesWorker.class,1, TimeUnit.DAYS)
                .addTag("Alarm")
                .build();
        WorkManager.getInstance(getActivity().getApplicationContext()).enqueueUniquePeriodicWork("Alarm", ExistingPeriodicWorkPolicy.REPLACE,request);

    }
}