package com.app.financialmanagement.Presentation.User.FinancialStatements.MonthlyCost;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.financialmanagement.Domain.MonthlyGoalsDataClass;
import com.app.financialmanagement.R;
import com.app.financialmanagement.Util.CommonData;
import com.app.financialmanagement.Util.Dialoge.SweetDialog;
import com.app.financialmanagement.databinding.FragmentMonthlyCostsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MonthlyCosts extends Fragment {
    private FragmentMonthlyCostsBinding mBinding;
    private DatabaseReference database;
    private String userId;
    private SweetAlertDialog loading;
    private ArrayList<MonthlyGoalsDataClass> reachedList;
    private MonthlyGoalsAdapter adapter;
    private int cost = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentMonthlyCostsBinding.inflate(inflater, container, false);


        mBinding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToScreen(R.id.action_monthlyCosts_to_financialStatement);
            }
        });
        mBinding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToScreen(R.id.action_monthlyCosts_to_addingMonthlyCost);
            }
        });
        funLoading();
        defineItems();
        clickDeleteItem();
        return mBinding.getRoot();
    }

    private void defineItems() {
        database = FirebaseDatabase.getInstance().getReference(CommonData.monthlyDataBaseClass);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        reachedList = new ArrayList<>();
        adapter = new MonthlyGoalsAdapter(reachedList, 0);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerView.setAdapter(adapter);

        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reachedList.clear();
                loading.dismiss();
                cost=0;
                if (snapshot.exists()) {
                    mBinding.noData.setVisibility(View.GONE);
                    for (DataSnapshot data : snapshot.getChildren()) {
                        MonthlyGoalsDataClass goal = data.getValue(MonthlyGoalsDataClass.class);
                        if (goal.getUserId().equals(userId)) {
                            reachedList.add(goal);
                            cost += calculateTheRange(goal.getRange());

                        }
                    }
                    if (reachedList.isEmpty()) {
                        mBinding.noData.setVisibility(View.VISIBLE);
                        mBinding.cardViewGraph.setVisibility(View.GONE);
                    }
                    else {
                        setDataInGraph();
                    }
                } else {
                    mBinding.noData.setVisibility(View.VISIBLE);
                    mBinding.cardViewGraph.setVisibility(View.GONE);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setDataInGraph() {
        predicate();
        ArrayList<PieModel> pieModels = new ArrayList<>();
        if (reachedList.size() > 2) {
            Log.e("this", "three");
            mBinding.save1Text.setText(reachedList.get(0).getGoalName());
            int cost1 = calculateTheRange(reachedList.get(0).getRange());
            pieModels.add(new PieModel(reachedList.get(0).getGoalName(), cost1, Color.parseColor("#f7c85c")));

            mBinding.save2Text.setText(reachedList.get(1).getGoalName());
            mBinding.save2.setVisibility(View.VISIBLE);
            int cost2 = calculateTheRange(reachedList.get(1).getRange());
            pieModels.add(new PieModel(reachedList.get(1).getGoalName(), cost2, Color.parseColor("#66BB6A")));


            int cost3 = 0;
            mBinding.others.setVisibility(View.VISIBLE);
            for (int i=2;i<reachedList.size();i++) {
                cost3 += calculateTheRange(reachedList.get(i).getRange());
            }
            pieModels.add(new PieModel("others", cost3, Color.parseColor("#A2A6A4")));
        } else if (reachedList.size() == 2) {
            mBinding.save1Text.setText(reachedList.get(0).getGoalName());
            int cost1 = calculateTheRange(reachedList.get(0).getRange());
            pieModels.add(new PieModel(reachedList.get(0).getGoalName(), cost1, Color.parseColor("#f7c85c")));

            mBinding.save2Text.setText(reachedList.get(1).getGoalName());
            mBinding.save2.setVisibility(View.VISIBLE);
            int cost2 = calculateTheRange(reachedList.get(1).getRange());
            pieModels.add(new PieModel(reachedList.get(1).getGoalName(), cost2, Color.parseColor("#66BB6A")));
        } else if (reachedList.size() == 1) {
            Log.e("this", "one");
            mBinding.save1Text.setText(reachedList.get(0).getGoalName());
            int cost1 = calculateTheRange(reachedList.get(0).getRange());
            pieModels.add(new PieModel(reachedList.get(0).getGoalName(), cost1, Color.parseColor("#f7c85c")));
        }
        for (PieModel p : pieModels) {
            mBinding.piechart.addPieSlice(
                    p);
        }
        mBinding.piechart.startAnimation();
    }


    private void predicate() {
        int persent = (int) ((Integer.parseInt(CommonData.userData.getBudget()) + Integer.parseInt(CommonData.userData.getIncome())) * .20);
        if (cost >= persent) {
            mBinding.color.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            mBinding.text.setText("You have exceeded the maximum addition limit");
        }
    }
    private int calculateTheRange(String range) {
        String cost = "0";
        if (range.contains("-")) {
            String[] parts = range.split("-");

            cost = parts[1].replace(" SAR", "");
        } else {
            cost = range;
        }

        return Integer.parseInt(cost);
    }
    private void clickDeleteItem() {
        adapter.setOnItemClickListener(new MonthlyGoalsAdapter.onItemClickListener() {
            @Override
            public void delete(int position) {
                alertDialog(position);
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
        database.child(reachedList.get(position).getGoalId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                funSuccessfully();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                funField("Failed to delete Item");
            }
        });
    }

    private void funSuccessfully() {
        SweetAlertDialog success = SweetDialog.success(getContext(), "Delete successfully");
        success.show();
    }

    private void funField(String message) {
        SweetAlertDialog field = SweetDialog.failed(getContext(), message);
        field.show();
    }

    private void funLoading() {
        loading = SweetDialog.loading(getContext());
        loading.show();
    }

    private void navigateToScreen(int id) {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(id);
    }

}