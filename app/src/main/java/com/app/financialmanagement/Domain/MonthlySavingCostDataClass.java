package com.app.financialmanagement.Domain;

public class MonthlySavingCostDataClass {
    private String name,range,money,status,userId,id;

    public MonthlySavingCostDataClass() {
    }

    public MonthlySavingCostDataClass(String name, String range, String money, String status, String userId, String id) {
        this.name = name;
        this.range = range;
        this.money = money;
        this.status = status;
        this.userId = userId;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

