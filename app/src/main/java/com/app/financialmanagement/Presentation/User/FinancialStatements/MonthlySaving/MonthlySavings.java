package com.app.financialmanagement.Presentation.User.FinancialStatements.MonthlySaving;

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

import com.app.financialmanagement.Domain.MonthlySavingCostDataClass;
import com.app.financialmanagement.R;
import com.app.financialmanagement.Util.CommonData;
import com.app.financialmanagement.Util.Dialoge.SweetDialog;
import com.app.financialmanagement.databinding.FragmentMonthlySavingsBinding;
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

public class MonthlySavings extends Fragment {
    private FragmentMonthlySavingsBinding mBinding;
    private DatabaseReference database;
    private String userId;
    private SweetAlertDialog loading;
    private ArrayList<MonthlySavingCostDataClass> reachedList;
    private MonthlySavingAdapter adapter;
    private int cost = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentMonthlySavingsBinding.inflate(inflater, container, false);


        mBinding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToScreen(R.id.action_monthlySavings_to_financialStatement);
            }
        });
        mBinding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToScreen(R.id.action_monthlySavings_to_addingMonthlySaving);
            }
        });
        funLoading();
        defineItems();
        clickDeleteItem();
        return mBinding.getRoot();
    }

    private void defineItems() {
        database = FirebaseDatabase.getInstance().getReference(CommonData.monthlySavingDataBaseClass);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        reachedList = new ArrayList<>();
        adapter = new MonthlySavingAdapter(reachedList, 0);
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
                        MonthlySavingCostDataClass goal = data.getValue(MonthlySavingCostDataClass.class);
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
            mBinding.save1Text.setText(reachedList.get(0).getName());
            int cost1 = calculateTheRange(reachedList.get(0).getRange());
            pieModels.add(new PieModel(reachedList.get(0).getName(), cost1, Color.parseColor("#f7c85c")));

            mBinding.save2Text.setText(reachedList.get(1).getName());
            mBinding.save2.setVisibility(View.VISIBLE);
            int cost2 = calculateTheRange(reachedList.get(1).getRange());
            pieModels.add(new PieModel(reachedList.get(1).getName(), cost2, Color.parseColor("#66BB6A")));


            int cost3 = 0;
            mBinding.others.setVisibility(View.VISIBLE);
            for (int i=2;i<reachedList.size();i++) {
                cost3 += calculateTheRange(reachedList.get(i).getRange());
            }
            pieModels.add(new PieModel("others", cost3, Color.parseColor("#A2A6A4")));
        } else if (reachedList.size() == 2) {
            mBinding.save1Text.setText(reachedList.get(0).getName());
            int cost1 = calculateTheRange(reachedList.get(0).getRange());
            pieModels.add(new PieModel(reachedList.get(0).getName(), cost1, Color.parseColor("#f7c85c")));

            mBinding.save2Text.setText(reachedList.get(1).getName());
            mBinding.save2.setVisibility(View.VISIBLE);
            int cost2 = calculateTheRange(reachedList.get(1).getRange());
            pieModels.add(new PieModel(reachedList.get(1).getName(), cost2, Color.parseColor("#66BB6A")));
        } else if (reachedList.size() == 1) {
            Log.e("this", "one");
            mBinding.save1Text.setText(reachedList.get(0).getName());
            int cost1 = calculateTheRange(reachedList.get(0).getRange());
            pieModels.add(new PieModel(reachedList.get(0).getName(), cost1, Color.parseColor("#f7c85c")));
        }
        for (PieModel p : pieModels) {
            mBinding.piechart.addPieSlice(
                    p);
        }
        mBinding.piechart.startAnimation();
    }


    private void predicate() {
        float percent= (float) (CommonData.savingPercent/100.0);
        int persent = (int) ((Integer.parseInt(CommonData.userData.getBudget()) + Integer.parseInt(CommonData.userData.getIncome())+CommonData.previousMonthRest) * percent);
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
        adapter.setOnItemClickListener(new MonthlySavingAdapter.onItemClickListener() {
            @Override
            public void delete(int position) {
                alertDialog("Delete Item","Are you sure you need to Delete this Item?",position,1);
            }

            @Override
            public void returnItem(int position) {
                alertDialog("Return Item","Are you sure you need to return this Item?",position,2);

            }
        });
    }

    private void alertDialog(String title, String message, int position, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (i==1) {
                    deleteItem(position);
                }else{
                    returnItem(position);
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteItem(int position) {
        database.child(reachedList.get(position).getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                funSuccessfully("Delete successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                funField("Failed to delete Item");
            }
        });
    }
    private void returnItem(int position)
    {
        MonthlySavingCostDataClass monthly=reachedList.get(position);
        monthly.setMoney(0+"");
        monthly.setStatus("active");
        database.child(monthly.getId()).setValue(monthly);
        funSuccessfully("Return Item successfully");
    }
    private void funSuccessfully(String title) {
        SweetAlertDialog success = SweetDialog.success(getContext(), title);
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