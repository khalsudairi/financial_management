package com.app.financialmanagement.Presentation.User.FinacialPlane;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;

import com.app.financialmanagement.Domain.CalenderNotificationDataClass;
import com.app.financialmanagement.Domain.DailyExpensesDataClass;
import com.app.financialmanagement.Domain.HobbiesDataClass;
import com.app.financialmanagement.Domain.MonthlyGoalsDataClass;
import com.app.financialmanagement.Domain.MonthlySavingCostDataClass;
import com.app.financialmanagement.Domain.PlaneDataClass;
import com.app.financialmanagement.Domain.UserWalletDataClass;
import com.app.financialmanagement.Domain.ViewData;
import com.app.financialmanagement.Domain.WalletDataClass;
import com.app.financialmanagement.Presentation.User.KnapsackAlgorithm.KnapsackAlgorithm;
import com.app.financialmanagement.Presentation.User.KnapsackAlgorithm.KnapsackItem;
import com.app.financialmanagement.R;
import com.app.financialmanagement.Util.CommonData;
import com.app.financialmanagement.Util.Dialoge.SweetDialog;
import com.app.financialmanagement.databinding.FragmentPlaneDetailsBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.models.PieModel;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PlaneDetails extends Fragment {
    private FragmentPlaneDetailsBinding mBinding;
    private int monthlyCost=0,saving=0,daily=0,debts=0,hobbies=0,wallet=0
            ,previousMonthRest =0,thisMonthRest=0,totalIncome=0,hobbiesPercent=10,savingPercent=10;
    private DatabaseReference database;
    private String userId;
    private SweetAlertDialog loading;
    private ArrayList<ViewData> viewData;
    private boolean planCreated=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding=com.app.financialmanagement.databinding.FragmentPlaneDetailsBinding.inflate(inflater,container,false);
        database= FirebaseDatabase.getInstance().getReference();
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        viewData=new ArrayList<>();
        mBinding.title.setText(CommonData.monthName);
        mBinding.salary.setText("Salary:-"+CommonData.userData.getIncome()+" SAR");
        mBinding.free.setText("Free business:-"+CommonData.userData.getBudget()+" SAR");
        mBinding.updatePlane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        mBinding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(R.id.action_planeDetails_to_financialPlane);
            }
        });

        funLoading();
        getTheRest();
        return mBinding.getRoot();
    }
    private void navigateTo(int id)
    {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(id);

    }
    int i=0;
    private void showDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        @SuppressLint("InflateParams")
        View mView = LayoutInflater.from(getActivity()).inflate(R.layout.update_plane, null);
        final Button updatePlane;
        final RadioGroup group ;
        group=mView.findViewById(R.id.group);
        updatePlane=mView.findViewById(R.id.updatePlane);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.increaseSaving){
                    i=0;
                }
                else{
                    i=1;
                }
            }
        });
        updatePlane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePlane(i,dialog);
            }
        });
        dialog.setContentView(mView);
        dialog.show();
    }

    private void changePlane(int i, Dialog dialog) {
        if (i==0){
            hobbiesPercent-=5;
            if (hobbiesPercent>=0) {
                savingPercent +=5;
                savePlane();
                navigateTo(R.id.planeDetails);

            }
            else{
                funLoginField("Hobbies have already been canceled and you cannot change more than that");
            }

        }
        else{
            savingPercent-=5;
            if (savingPercent>=0) {
                hobbiesPercent +=5;
                savePlane();
                navigateTo(R.id.planeDetails);
            }
            else{
                funLoginField("Saving have already been canceled and you cannot change more than that");
            }
        }
        dialog.dismiss();
    }

    private void getTheRest() {
        if (CommonData.monthNumber == 1) {
            previousMonthRest = 0;
            mBinding.previous.setText(0+" SAR");
            getMonthlyCost();

        } else {
            database.child(CommonData.planeDataBaseClass).child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists() &&!planCreated){
                        for (DataSnapshot data:snapshot.getChildren())
                        {
                            PlaneDataClass p=data.getValue(PlaneDataClass.class);
                            if (p.getMonthNumber()==CommonData.monthNumber-1)
                            {
                                previousMonthRest =p.getRest();
                                mBinding.previous.setText(p.getRest()+" SAR");
                            } else if (p.getMonthNumber() == CommonData.monthNumber) {
                                hobbiesPercent=p.getHobbiesPercent();
                                savingPercent=p.getSavingPercent();
                            }
                        }
                        getMonthlyCost();
                    }
                    else if (!planCreated){
                        mBinding.previous.setText(0+" SAR");
                        getMonthlyCost();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void getMonthlyCost() {
        totalIncome=Integer.parseInt(CommonData.userData.getBudget())+Integer.parseInt(CommonData.userData.getIncome())+previousMonthRest;
        database.child(CommonData.monthlyDataBaseClass).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        MonthlyGoalsDataClass goal = data.getValue(MonthlyGoalsDataClass.class);
                        if (goal.getUserId().equals(userId)) {
                            int cost=calculateTheRange(goal.getRange());
                            monthlyCost+=cost;
                            viewData.add(new ViewData(goal.getGoalName(),cost));
                        }
                    }
                    getDailyCost();

                }
                else{
                    getDailyCost();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getDailyCost() {
        database.child(CommonData.dailyDataBaseClass).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        DailyExpensesDataClass goal = data.getValue(DailyExpensesDataClass.class);
                        if (goal.getUserId().equals(userId)) {
                            int cost=calculateTheRange(goal.getRange());
                            daily+=cost;
                            viewData.add(new ViewData(goal.getName(),cost));
                        }
                    }
                    getHobbiesCost();
                }
                else{
                    getHobbiesCost();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getHobbiesCost() {
        database.child(CommonData.hobbiesDataBaseClass).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<HobbiesDataClass> hobbiesArray=new ArrayList<>();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        HobbiesDataClass goal = data.getValue(HobbiesDataClass.class);
                        if (goal.getUserId().equals(userId)) {
                            hobbiesArray.add(goal);
                        }
                    }
                    getHobbiesKnapsack(hobbiesArray);
                }
                else{
                    getSavingCost();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getHobbiesKnapsack(ArrayList<HobbiesDataClass> hobbiesArrayList) {
        ArrayList<KnapsackItem> arrayList = new ArrayList<>();
        for (HobbiesDataClass item : hobbiesArrayList) {
            String range = item.getRange();
            if (range.contains("-")) {
                String[] parts = range.split("-");
                range = parts[1].replace("SAR", "");
                range = range.trim();
            }
            arrayList.add(new KnapsackItem(Integer.parseInt(range) * Integer.parseInt(item.getPracticeNum()) * 4, 1, item.getId(),item.getHobbyName()));
        }
        float percent= (float) (hobbiesPercent/100.0);
        int i = (int) (totalIncome * percent);
        Log.e("hobbies",percent+"");
        List<KnapsackItem> ar = KnapsackAlgorithm.createFinancialPlan(arrayList, i);
        for (KnapsackItem hj : ar) {
            int cost= (int) hj.getWeight();
            hobbies+=cost;
            viewData.add(new ViewData(hj.getName(),cost));
            Log.e("hobbies2",hobbies+"");
        }
        getSavingCost();
    }
    private void getSavingCost() {
        database.child(CommonData.monthlySavingDataBaseClass).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<MonthlySavingCostDataClass> arrayList=new ArrayList<>();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        MonthlySavingCostDataClass goal = data.getValue(MonthlySavingCostDataClass.class);
                        if (goal.getUserId().equals(userId)) {
                            arrayList.add(goal);
                        }
                    }
                    getSavingKnapsack(arrayList);

                }
                else{
                    getDebts();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getSavingKnapsack(ArrayList<MonthlySavingCostDataClass> savingArr) {
        ArrayList<KnapsackItem> arrayList = new ArrayList<>();
        for (MonthlySavingCostDataClass item : savingArr) {
            String range = item.getRange();
            if (range.contains("-")) {
                String[] parts = range.split("-");
                range = parts[1].replace("SAR", "");
                range = range.trim();
            }
            arrayList.add(new KnapsackItem(Integer.parseInt(range), 1, item.getId(),item.getName()));
        }
        float percent= (float) (savingPercent/100.0);
        int i = (int) (totalIncome * percent);
        Log.e("saving",i+"");
        List<KnapsackItem> ar = KnapsackAlgorithm.createFinancialPlan(arrayList, i);
        for (KnapsackItem hj : ar) {
            int cost= (int) hj.getWeight();
            saving+=cost;
            viewData.add(new ViewData(hj.getName(),cost));
            Log.e("saving2",saving+"");
        }
        getDebts();
    }
    private void getDebts() {
        database.child(CommonData.calenderSavingDataBaseClass).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        CalenderNotificationDataClass item = data.getValue(CalenderNotificationDataClass.class);
                        if (item.getUserId().equals(userId)) {
                            int cost=calculateTheWalletCost(item.getCost(),item.getDate(),item.getDate(),1);
                            if (cost>0) {
                                debts+=cost;
                                viewData.add(new ViewData(item.getMissionName(), cost));
                            }
                        }
                    }
                    getWallet();
                }
                else{
                    getWallet();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getWallet() {
        database.child(CommonData.walletDataBaseClass).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        WalletDataClass item = data.getValue(WalletDataClass.class);
                        if  (item.getUserId().equals(userId)) {
                            int cost=calculateTheWalletCost(item.getCost(),item.getEndDate(),item.getStartData(),item.getUsers().size()+1);
                            if (cost>0) {
                                int u=calculateTheMonthsNum(item.getStartData(),item.getEndDate());
                                Log.e("u",u+"");
                                if (u>0){
                                    cost/=u;
                                }
                                Log.e("u2",cost+"");
                                wallet+=cost;
                                viewData.add(new ViewData(item.getWalletName(), cost));
                            }
                        }
                        for (UserWalletDataClass user:item.getUsers()){
                            if (user.getUserId().equals(userId)){
                                int cost=calculateTheWalletCost(item.getCost(),item.getEndDate(),item.getStartData(),item.getUsers().size()+1);
                                Log.e("u",cost+"");
                                if (cost>0) {
                                    int u=calculateTheMonthsNum(item.getStartData(),item.getEndDate());
                                   Log.e("u",u+"");
                                    if (u>0){
                                        cost/=u;
                                    }
                                    Log.e("u2",cost+"");
                                    wallet+=cost;
                                    viewData.add(new ViewData(item.getWalletName(), cost));
                                }
                            }
                        }
                    }
                    calculateData();
                } else {
                    calculateData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void calculateData()
    {
        loading.dismiss();
        planCreated=true;
        int totalCost=monthlyCost+saving+daily+debts+hobbies+wallet;
        if (totalCost==0)
        {
            funLoginField("Please add items to your financial plan");
            Navigation.findNavController(getView()).popBackStack();
        }
        thisMonthRest=totalIncome-totalCost;
        viewDataInRecyclerView();
        setPrediction();
        addDataToChart();
    }

    private void addDataToChart() {
        mBinding.piechart.addPieSlice(
                new PieModel(
                        "Monthly Cost",
                        monthlyCost,
                        Color.parseColor("#f7c85c")));
        mBinding.piechart.addPieSlice(
                new PieModel(
                        "saving",
                        saving,
                        Color.parseColor("#66BB6A")));
        mBinding.piechart.addPieSlice(
                new PieModel(
                        "Daily Expenses",
                        daily,
                        Color.parseColor("#242424")));
        mBinding.piechart.addPieSlice(
                new PieModel(
                        "Debts",
                        debts,
                        Color.parseColor("#cdd9d3")));
        mBinding.piechart.addPieSlice(
                new PieModel(
                        "Hobbies",
                        hobbies,
                        Color.parseColor("#A2A6A4")));
        mBinding.piechart.addPieSlice(
                new PieModel(
                        "Wallet",
                        wallet,
                        Color.parseColor("#7F9E8E")));
        mBinding.piechart.startAnimation();

        mBinding.monthlyCost.setText(monthlyCost+" SAR");
        mBinding.saving.setText(saving+" SAR");
        mBinding.dailyCost.setText(daily+" SAR");
        mBinding.debtsCost.setText(debts+" SAR");
        mBinding.hobbiesCost.setText(hobbies+" SAR");
        mBinding.walletCost.setText(wallet+" SAR");
        mBinding.thisMonth.setText(thisMonthRest+" SAR");
    }
    private void savePlane()
    {
        PlaneDataClass p=new PlaneDataClass(userId,monthlyCost,saving,daily,debts,hobbies,wallet,totalIncome,thisMonthRest,CommonData.monthNumber,hobbiesPercent,savingPercent);
        database.child(CommonData.planeDataBaseClass).child(userId).child(String.valueOf(CommonData.monthNumber)).setValue(p);

    }

    private void viewDataInRecyclerView()
    {
        ViewItemAdapter adapter=new ViewItemAdapter(viewData);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerView.setAdapter(adapter);
    }
    private void setPrediction()
    {
        if (thisMonthRest<=0)
        {
            Glide.with(getActivity()).load(R.drawable.angry1).into(mBinding.image);
            mBinding.prediction.setText("You have exceeded the maximum limit of your financial plan");

        }
    }
    private int calculateTheRange(String range){
        String cost="0";
        if (range.contains("-")) {
            String[] parts = range.split("-");

            cost=parts[1].replace(" SAR","");
        }
        else{
            cost=range;
        }

        return Integer.parseInt(cost);
    }
    private int calculateTheWalletCost(String cost,String endDate,String startDate,int num){
        int c=Integer.parseInt(cost)/num;
        int end=getMonthNumber(endDate);
        int start=getMonthNumber(startDate);
        if (start<=CommonData.monthNumber &&CommonData.monthNumber<=end)
        {
            return c;
        }
        return 0;
    }
    private int calculateTheMonthsNum(String start,String end)
    {
        String []arr1=start.split("/");
        String []arr2=end.split("/");
        LocalDate startDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startDate = LocalDate.of(Integer.parseInt(arr1[0]), Integer.parseInt(arr1[1]), Integer.parseInt(arr1[2]));
            LocalDate endDate = LocalDate.of(Integer.parseInt(arr2[0]), Integer.parseInt(arr2[1]), Integer.parseInt(arr1[2]));
            Period period = Period.between(startDate, endDate);
            int months = period.getYears() * 12 + period.getMonths();

            return months;
        }
        return 0;
    }
    private int getMonthNumber(String dateString)
    {
        //   2024/4/9
        String []arr=dateString.split("/");
        int month=Integer.parseInt(arr[1]);
        return month;
    }
    private void funLoading() {
        loading = SweetDialog.loading(getContext());
        loading.show();
    }

    private void funSuccessfully() {
        SweetAlertDialog success = SweetDialog.success(getContext(), "adding successfully");
        success.show();
        Navigation.findNavController(getView()).popBackStack();
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
}