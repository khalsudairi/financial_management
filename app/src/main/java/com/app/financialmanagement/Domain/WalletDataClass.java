package com.app.financialmanagement.Domain;

import java.util.ArrayList;

public class WalletDataClass {
    private String walletName,cost,startData,endDate,purpose,submitDay,userId,walletId;
    private ArrayList<UserWalletDataClass> users;

    public WalletDataClass() {
    }

    public WalletDataClass(String walletName, String cost, String startData, String endDate, String purpose, String submitDay, String userId, String walletId, ArrayList<UserWalletDataClass> users) {
        this.walletName = walletName;
        this.cost = cost;
        this.startData = startData;
        this.endDate = endDate;
        this.purpose = purpose;
        this.submitDay = submitDay;
        this.userId = userId;
        this.walletId = walletId;
        this.users = users;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getStartData() {
        return startData;
    }

    public void setStartData(String startData) {
        this.startData = startData;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getSubmitDay() {
        return submitDay;
    }

    public void setSubmitDay(String submitDay) {
        this.submitDay = submitDay;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public ArrayList<UserWalletDataClass> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserWalletDataClass> users) {
        this.users = users;
    }
}
