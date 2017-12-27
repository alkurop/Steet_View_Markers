package com.alkurop.mystreetplaces.greedy;

import org.junit.Test;

public class ArrayDifference {
    int[] input = new int[] { 3, -7, 0, 10, 55, 83, 8 };

    @Test
    public void arrayDifferenceTEst() {
        System.out.println(getMinAbsoluteDifference(input));
    }

    int getMinAbsoluteDifference(int[] input) {
        int dif = Integer.MAX_VALUE;
        for (int i = 0; i < input.length; i++) {
            for (int k = i + 1; k < input.length; k++) {
                int difTemp = Math.abs(input[i] - input[k]);
                if (dif > difTemp) {
                    dif = difTemp;
                }
            }
        }
        return dif;
    }
}
