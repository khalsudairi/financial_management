package com.app.financialmanagement.Domain;

public class HobbiesDataClass {
    private String hobbyName,practiceNum,range,money,status,id,userId;

    public HobbiesDataClass() {
    }

    public HobbiesDataClass(String hobbyName, String practiceNum, String range, String money, String status, String id, String userId) {
        this.hobbyName = hobbyName;
        this.practiceNum = practiceNum;
        this.range = range;
        this.money = money;
        this.status = status;
        this.id = id;
        this.userId = userId;
    }

    public String getHobbyName() {
        return hobbyName;
    }

    public void setHobbyName(String hobbyName) {
        this.hobbyName = hobbyName;
    }

    public String getPracticeNum() {
        return practiceNum;
    }

    public void setPracticeNum(String practiceNum) {
        this.practiceNum = practiceNum;
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
