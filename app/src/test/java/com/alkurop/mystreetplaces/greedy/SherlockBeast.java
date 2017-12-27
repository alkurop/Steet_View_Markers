package com.alkurop.mystreetplaces.greedy;

import org.junit.Test;

import kotlin.Pair;

public class SherlockBeast {
    int[] input = new int[] { 1, 3, 5, 11 };

    @Test
    public void beastTest() {
        for (int i : input) {
            System.out.println(getBeastNumber(i));
        }
    }

    String getBeastNumber(int input) {
        Pair<Integer, Integer> pair = null;
        int globalFives = input / 3;

        for (int fives = globalFives; fives >= 0; fives--) {
            int check = input - 3 * fives;
            if (check % 5 == 0) {
                int threes = check / 5;
                if (threes > 0 || fives > 0) {
                    pair = new Pair<>(fives * 3, threes * 5);
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();

        if (pair == null) {
            stringBuilder.append("-1");
        } else {
            for (Integer i = 0; i < pair.getFirst(); i++) {
                stringBuilder.append("5");
            }
            for (Integer i = 0; i < pair.getSecond(); i++) {
                stringBuilder.append("3");
            }
        }
        return stringBuilder.toString();
    }
}
