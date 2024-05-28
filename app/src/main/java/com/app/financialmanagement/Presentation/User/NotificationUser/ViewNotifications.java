package com.app.financialmanagement.Presentation.User.NotificationUser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.financialmanagement.R;
import com.app.financialmanagement.databinding.FragmentViewNotificationsBinding;

public class ViewNotifications extends Fragment {
    private FragmentViewNotificationsBinding mBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding=FragmentViewNotificationsBinding.inflate(inflater,container,false);


        mBinding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToScreen(R.id.action_notification_to_calender);
            }
        });
        return mBinding.getRoot();
    }
    private void navigateToScreen(int id) {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(id);
    }
}