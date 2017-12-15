package com.alkurop.mystreetplaces;

import org.junit.Test;

public class SortSortedTest {
    int[] _1 = new int[] { 1, 2, 2, 2, 3, 4, 5, 6, 12, 44, 55, 59, 68, 77, 88 };
    int[] _2 = new int[] { 2, 22, 33, 345, 556, 667, 889 };
    int[] _3 = new int[] { 5, 8, 34, 45, 56, 67, 77, 78 };

    @Test
    public void sortSorted() {
        int[] ints = sortSorted(new int[][] { _1, _2, _3 });

        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    int[] sortSorted(int[][] arrays) {
        int length = 0;
        int[] counterArray = new int[arrays.length];
        for (int[] array : arrays) {
            length += array.length;
        }
        int resultArray[] = new int[length];
        for (int numberIndex = 0; numberIndex < length; numberIndex++) {
            Integer currentValue = null;
            Integer counrerIndex = null;
            for (int arrayIndex = 0; arrayIndex < arrays.length; arrayIndex++) {
                int[] currentArray = arrays[arrayIndex];
                int counter = counterArray[arrayIndex];
                if (currentArray.length > counter) {
                    int arrayValue = currentArray[counter];
                    if (currentValue == null || currentValue >= arrayValue) {
                        currentValue = arrayValue;
                        counrerIndex = arrayIndex;
                    }
                    resultArray[numberIndex] = currentValue.intValue();
                }
            }
            if (counrerIndex != null) {
                counterArray[counrerIndex]++;
            }
            counrerIndex = null;
            currentValue = null;

        }
        return resultArray;
    }

}
