package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.ArrayList;

public class SumTest {
    @Test
    public void test() {
        int[] input = new int[] { -1, 2, -1, 3, 4, -5, -8, 5, 6, -2, 1, 0, 0, 4, 2, 3 };
        for (Integer[] ints : findSUmZeroIndexes(input)) {
            System.out.println("");
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
        }

    }

    Integer[][] findSUmZeroIndexes(int[] input) {
        ArrayList<Integer[]> resultList = new ArrayList<>();

        int iteractionCounter = 0;

        for (int i = 0; i < input.length - 2; i++) {
            for (int i1 = i + 1; i1 < input.length - 1; i1++) {
                for (int i2 = i1 + 1; i2 < input.length; i2++) {
                    iteractionCounter++;
                    if (input[i1] + input[i2] + input[i] == 0) {
                        resultList.add(new Integer[] { i, i1, i2 });
                    }
                }
            }
        }
        Integer[][] result = new Integer[resultList.size()][];
        resultList.toArray(result);
        System.out.println("iteration " + iteractionCounter);
        return result;
    }

}
