package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.Arrays;

public class QuickSortTest {
    int[] getData() {
        return new int[] { 323, 22, 33456, 5, 32, 564, 763452, 345, 727, 1, 22, 333 };
    }

    @Test
    public void testQuickSort() {
        int[] data = getData();
        sort(data);
        System.out.println(Arrays.toString(data));
    }

    private void sort(int[] data) {
        quickSort(data, 0, data.length - 1);
    }

    private void quickSort(int[] data, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(data, low, high);
            quickSort(data, low, pivotIndex - 1);
            quickSort(data, pivotIndex + 1, high);
        }
    }

    private int partition(int[] data, int low, int high) {
        int pivot = data[high];
        int swap = low - 1;
        for (int i = low; i < high; i++) {
            if (data[i] <= pivot) {
                swap++;

                int temp = data[swap];
                data[swap] = data[i];
                data[i] = temp;
            }
        }
        int temp = data[swap + 1];
        data[swap + 1] = data[high];
        data[high] = temp;
        return swap;
    }
}
