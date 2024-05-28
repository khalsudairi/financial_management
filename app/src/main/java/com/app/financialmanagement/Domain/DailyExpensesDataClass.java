package com.app.financialmanagement.Domain;

public class DailyExpensesDataClass {
    private String name,range,money,status,id,userId;

    public DailyExpensesDataClass() {
    }

    public DailyExpensesDataClass(String name, String range, String money, String status, String id, String userId) {
        this.name = name;
        this.range = range;
        this.money = money;
        this.status = status;
        this.id = id;
        this.userId = userId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
