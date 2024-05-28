package com.app.financialmanagement.Presentation.User.KnapsackAlgorithm;


public class KnapsackItem {

    private final double weight;
    private final int priority;
    private final String id;
    private final String name;

    public KnapsackItem(double weight, int priority, String id, String name) {
        this.weight = weight;
        this.priority = priority;
        this.id = id;
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public int getPriority() {
        return priority;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
