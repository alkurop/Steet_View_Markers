package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class InsertionSortTest {
    int[] getData() {
        return new int[] { 323, 22, 33456, 5, 32, 564, 763452, 345, 727, 1, 22, 333 };
    }

    @Test
    public void testInsertionSort() {
        int[] data = getData();
        sort(data);
        System.out.println(Arrays.toString(data));
    }

    /*Function to sort array using insertion sort*/
    void sort(int data[]) {
        int n = data.length;
        for (int i = 0; i < n; ++i) {
            int temp = data[i];
            while (i > 0 && data[i - 1] > data[i]) {
                data[i] = data[i - 1];
                data[i - 1] = temp;
                i--;
            }
        }
        HashSet<Integer> mIntegerHashSet = new HashSet<>();
        Iterator<Integer> iterator = mIntegerHashSet.iterator();
    }

}
