package com.app.financialmanagement.Domain;

public class UserWalletDataClass {
    private String userName,userId;

    public UserWalletDataClass() {
    }

    public UserWalletDataClass(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
