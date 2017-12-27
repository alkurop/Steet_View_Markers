package com.alkurop.mystreetplaces.greedy;

import org.junit.Test;

import java.util.Arrays;

public class MarkToys {
    int[] input = new int[] { 1, 12, 5, 111, 200, 10 };

    @Test
    public void markToys() {
        System.out.println(getTouysNumber(input, 50));
    }

    int getTouysNumber(int[] input, int moneyAmount) {
        Arrays.sort(input);
        int index = 0;
        int amount = 0;
        while (index < input.length && input[index] <= moneyAmount) {
            int price = input[index];
            amount++;
            moneyAmount -= price;
            index++;
        }
        return amount;
    }
}
