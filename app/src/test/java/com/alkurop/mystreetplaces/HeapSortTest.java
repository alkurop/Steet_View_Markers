package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.Arrays;

public class HeapSortTest {
    int[] getData() {
        return new int[] { 323, 22, 33456, 5, 32, 564, 763452, 345, 543526, 727, 1, 22, 333 };
    }

    @Test
    public void heapSortTest() {
        int[] data = getData();
        sort(data);
        System.out.println(Arrays.toString(data));
    }

    void sort(int[] data) {
        int n = data.length;
        for (int i = n / 2; i >= 0; i--) {
            heapify(data, n, i);
        }
        for (int i = n - 1; i >= 0; i--) {
            // Move current root to end
            int temp = data[0];
            data[0] = data[i];
            data[i] = temp;

            // call max heapify on the reduced heap
            heapify(data, i, 0);
        }
    }

    void heapify(int[] data, int n, int i) {
        int largest = i;
        int left = 2 * i;
        int right = 2 * i + 1;

        if (left < n && data[largest] < data[left]) {
            largest = left;
        }

        if (right < n && data[largest] < data[right]) {
            largest = right;
        }
        if (largest != i) {
            int temp = data[i];
            data[i] = data[largest];
            data[largest] = temp;
        }
    }
}
