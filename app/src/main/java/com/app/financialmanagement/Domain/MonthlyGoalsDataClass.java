package com.app.financialmanagement.Domain;

public class MonthlyGoalsDataClass {
    private String goalName,goalType,range,money,status,goalId,userId;

    public MonthlyGoalsDataClass() {
    }

    public MonthlyGoalsDataClass(String goalName, String goalType, String range, String money, String status, String goalId, String userId) {
        this.goalName = goalName;
        this.goalType = goalType;
        this.range = range;
        this.money = money;
        this.status = status;
        this.goalId = goalId;
        this.userId = userId;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
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

    public String getGoalId() {
        return goalId;
    }

    public void setGoalId(String goalId) {
        this.goalId = goalId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
