package com.app.financialmanagement.Domain;

public class ViewData {
    private String itemName;
    private int cost;

    public ViewData(String itemName, int cost) {
        this.itemName = itemName;
        this.cost = cost;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
