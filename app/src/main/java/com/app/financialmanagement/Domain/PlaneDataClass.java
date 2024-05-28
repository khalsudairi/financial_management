package com.app.financialmanagement.Domain;

public class PlaneDataClass {
    private String userId;
    private int monthlyCost,saving,dailyCost,debts,hobbies,wallet,totalIncome,
            rest,monthNumber,hobbiesPercent,savingPercent;
    public PlaneDataClass() {
    }

    public PlaneDataClass(String userId, int monthlyCost, int saving, int dailyCost, int debts, int hobbies, int wallet, int totalIncome, int rest, int monthNumber, int hobbiesPercent, int savingPercent) {
        this.userId = userId;
        this.monthlyCost = monthlyCost;
        this.saving = saving;
        this.dailyCost = dailyCost;
        this.debts = debts;
        this.hobbies = hobbies;
        this.wallet = wallet;
        this.totalIncome = totalIncome;
        this.rest = rest;
        this.monthNumber = monthNumber;
        this.hobbiesPercent = hobbiesPercent;
        this.savingPercent = savingPercent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getMonthlyCost() {
        return monthlyCost;
    }

    public void setMonthlyCost(int monthlyCost) {
        this.monthlyCost = monthlyCost;
    }

    public int getSaving() {
        return saving;
    }

    public void setSaving(int saving) {
        this.saving = saving;
    }

    public int getDailyCost() {
        return dailyCost;
    }

    public void setDailyCost(int dailyCost) {
        this.dailyCost = dailyCost;
    }

    public int getDebts() {
        return debts;
    }

    public void setDebts(int debts) {
        this.debts = debts;
    }

    public int getHobbies() {
        return hobbies;
    }

    public void setHobbies(int hobbies) {
        this.hobbies = hobbies;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    public int getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(int totalIncome) {
        this.totalIncome = totalIncome;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(int monthNumber) {
        this.monthNumber = monthNumber;
    }

    public int getHobbiesPercent() {
        return hobbiesPercent;
    }

    public void setHobbiesPercent(int hobbiesPercent) {
        this.hobbiesPercent = hobbiesPercent;
    }

    public int getSavingPercent() {
        return savingPercent;
    }

    public void setSavingPercent(int savingPercent) {
        this.savingPercent = savingPercent;
    }
}
