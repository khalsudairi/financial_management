package com.app.financialmanagement.Domain;

public class UserDataClass {
    private String userName,email,phoneNumber,income,budget,familyNumber,family,userId;

    public UserDataClass() {
    }

    public UserDataClass(String userName, String email, String phoneNumber, String income, String budget, String familyNumber, String userId) {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.income = income;
        this.budget = budget;
        this.familyNumber = familyNumber;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getFamilyNumber() {
        return familyNumber;
    }

    public void setFamilyNumber(String familyNumber) {
        this.familyNumber = familyNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
