package com.app.financialmanagement.Presentation.User.Sharedwallet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.app.financialmanagement.Domain.UserWalletDataClass;
import com.app.financialmanagement.Domain.WalletDataClass;
import com.app.financialmanagement.Presentation.SendNotificationPack.APIService;
import com.app.financialmanagement.Presentation.SendNotificationPack.Client;
import com.app.financialmanagement.Presentation.SendNotificationPack.Data;
import com.app.financialmanagement.Presentation.SendNotificationPack.MyResponse;
import com.app.financialmanagement.Presentation.SendNotificationPack.NotificationSender;
import com.app.financialmanagement.R;
import com.app.financialmanagement.Util.CommonData;
import com.app.financialmanagement.Util.DateAndTime.CustomDatePickerDialog;
import com.app.financialmanagement.Util.Dialoge.SweetDialog;
import com.app.financialmanagement.databinding.FragmentAddingWalletBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddingWallet extends Fragment {
    private FragmentAddingWalletBinding mBinding;
    private DatabaseReference database;
    private String userId;
    private SweetAlertDialog loading;
    private APIService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAddingWalletBinding.inflate(inflater, container, false);

        mBinding.users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonData.selectUsers.clear();
                navigateToScreen(R.id.action_addWallet_to_search);
            }
        });
        mBinding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToScreen(R.id.action_addWallet_to_sharedWallet);
            }
        });
        mBinding.endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(mBinding.endDate);
            }
        });
        setMonthlyDeadline();
        database = FirebaseDatabase.getInstance().getReference(CommonData.walletDataBaseClass);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        clickAdd();
        return mBinding.getRoot();
    }

    private void clickAdd() {
        mBinding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mBinding.name.getText().toString();
                String cost = mBinding.cost.getText().toString();
                String purpose = mBinding.purpose.getText().toString();
                String endDate = mBinding.endDate.getText().toString();
                String day = mBinding.daySpinner.getSelectedItem().toString();

                if (name.isEmpty()) {
                    mBinding.name.setError("Please the Wallet Name");
                } else if (purpose.isEmpty()) {
                    mBinding.purpose.setError("Please the Purpose ");
                } else if (cost.isEmpty()) {
                    mBinding.cost.setError("Please the cost");
                } else if (endDate.isEmpty()) {
                    mBinding.endDate.setError("Please the end Date");
                } else if (CommonData.selectUsers.isEmpty()) {
                    Toast.makeText(getActivity(), "Please select the users", Toast.LENGTH_SHORT).show();
                } else {
                    funLoading();
                    String id = UUID.randomUUID().toString();
                    WalletDataClass wallet = new WalletDataClass(name, cost,getCurrentDate(), endDate, purpose, day, userId,
                            id, CommonData.selectUsers);
                    saveToDatabase(wallet);
                }
            }
        });
    }

    private void saveToDatabase(WalletDataClass dataClass) {
        database.child(dataClass.getWalletId()).setValue(dataClass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                loading.dismiss();
                funSuccessfully();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismiss();
                funLoginField("Failed to add Wallet please try again ");
            }
        });
    }

    private void navigateToScreen(int id) {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(id);
    }

    private String getCurrentDate() {
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
            int year = currentDate.getYear();
            int month = currentDate.getMonthValue();
            int dayOfMonth = currentDate.getDayOfMonth();
             return year + "/" + month + "/" + dayOfMonth;
        }
        return "";
    }

    private void setMonthlyDeadline() {
        List<String> daysList = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            daysList.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, daysList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.daySpinner.setAdapter(adapter);
    }

    private void showDatePickerDialog(TextView textView) {
        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(
                getActivity(),
                (view, year, month, dayOfMonth) -> {
                    // Handle date selection
                    String date = year + "/" + (month + 1) + "/" + dayOfMonth;
                    textView.setText(date);
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void funLoading() {
        loading = SweetDialog.loading(getContext());
        loading.show();
    }

    private void funSuccessfully() {
        SweetAlertDialog success = SweetDialog.success(getContext(), "adding successfully");
        success.show();
        sendNotification();
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

    private void sendNotification() {
        for (UserWalletDataClass users : CommonData.selectUsers) {
            getToken(users.getUserId(), "new Wallet", "Adding you in new Wallet");
        }
    }

    private void getToken(String userID, String title, String message) {
        FirebaseDatabase.getInstance().getReference("tokens").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String token = snapshot.child("token").getValue().toString();
                    sendNotifications(token, title, message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @SuppressLint("ShowToast")
            @Override
            public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null && response.body().success != 1) {
                        Toast.makeText(getActivity(), "Failed ", Toast.LENGTH_LONG);
                    } else {
                        Log.e("success", response.code() + " success " + response.body().success + " Token " + usertoken);

                    }
                } else {
                    Log.e("send Notifications", "Failed: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        StringBuilder names = new StringBuilder();
        for (UserWalletDataClass data : CommonData.selectUsers) {
            names.append(data.getUserName() + "  ");
        }
        mBinding.users.setText(names.toString());
    }
}