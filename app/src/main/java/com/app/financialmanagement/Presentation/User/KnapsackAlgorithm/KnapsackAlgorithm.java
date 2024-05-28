package com.app.financialmanagement.Presentation.User.KnapsackAlgorithm;

import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
public class KnapsackAlgorithm {
    public static double remainingCapacity;
    public static List<KnapsackItem> createFinancialPlan(List<KnapsackItem> items,int cost) {
        List<KnapsackItem> selectedGoals = knapsack(items,cost );
        return selectedGoals;
    }


    private static List<KnapsackItem> knapsack(List<KnapsackItem> items, double capacity) {
        // Sort items by priority (higher priority first)
        List<KnapsackItem> selectedItems = new ArrayList<>();
        remainingCapacity = capacity;

        for (KnapsackItem item : items) {
            if (item.getWeight() <= remainingCapacity) {
                selectedItems.add(item);
                remainingCapacity -= item.getWeight();
            }
        }

        return selectedItems;
    }
}
