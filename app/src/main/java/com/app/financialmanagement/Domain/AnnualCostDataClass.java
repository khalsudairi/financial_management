package com.app.financialmanagement.Domain;

public class AnnualCostDataClass {
    private String name,range,money,status,userId,annualId;

    public AnnualCostDataClass() {
    }

    public AnnualCostDataClass(String name, String range, String money, String status, String userId, String annualId) {
        this.name = name;
        this.range = range;
        this.money = money;
        this.status = status;
        this.userId = userId;
        this.annualId = annualId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAnnualId() {
        return annualId;
    }

    public void setAnnualId(String annualId) {
        this.annualId = annualId;
    }
}

