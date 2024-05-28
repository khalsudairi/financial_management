package com.app.financialmanagement.Domain;

public class CalenderNotificationDataClass {
    private String missionName,cost,date,id,userId;

    public CalenderNotificationDataClass() {
    }

    public CalenderNotificationDataClass(String missionName, String cost, String date, String id, String userId) {
        this.missionName = missionName;
        this.cost = cost;
        this.date = date;
        this.id = id;
        this.userId = userId;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
