package com.app.financialmanagement.Util;

import com.app.financialmanagement.Domain.UserDataClass;
import com.app.financialmanagement.Domain.UserWalletDataClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommonData {
    public static String monthName;
    public static int monthNumber;
    public static int previousMonthRest=0;
    public static int hobbiesPercent=10;
    public static int savingPercent=10;

    public static String planeDataBaseClass ="FinancialPlane";
    public static String userDataBaseClass ="Users";
    public static String annualCostDataBaseClass="AnnualCosts";
    public static String dailyDataBaseClass ="DailyExpenses";
    public static String hobbiesDataBaseClass ="Hobbies";
    public static String monthlyDataBaseClass ="MonthlyGoals";
    public static String monthlySavingDataBaseClass ="MonthlySaving";
    public static String calenderSavingDataBaseClass ="CalenderNotifications";

    public static String walletDataBaseClass ="wallets";

    public static UserDataClass userData;

    public static ArrayList<UserWalletDataClass> selectUsers=new ArrayList<>();

    public static long calculateDelay(String currentTime, String time) {
        try {
            //8:00:00
            //12:00:00
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date date1 = format.parse(currentTime);
            Date date2 = format.parse(time);

            return (date2.getTime() - date1.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }

    }
}
