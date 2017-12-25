package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.Arrays;

public class MergeSortTest {
    int[] getData() {
        return new int[] { 323, 22, 33456, 5, 32, 564, 763452, 345, 727, 1, 22, 333 };
    }

    @Test
    public void testMergeSort() {
        int[] data = getData();
        mergeSort(data);
        System.out.println(Arrays.toString(data));
    }

    private void mergeSort(int[] data) {
        if (data == null || data.length == 0) return;
        if (data.length > 1) {
            split(data, 0, data.length - 1);
        }
    }

    private void split(int[] data, int left, int right) {
        if (right > left) {
            int median = ((right + left)) / 2;
            split(data, left, median);
            split(data, median + 1, right);
            merge1(data, left, median, right);
        }
    }

    private void merge1(int[] data, int left, int median, int right) {
        int leftLength = median - left + 1;
        int rightLentgh = right - median;

        int[] leftArray = new int[leftLength];
        int[] rightArray = new int[rightLentgh];

        System.arraycopy(data, left, leftArray, 0, leftLength);
        System.arraycopy(data, median + 1, rightArray, 0, rightLentgh);

        int leftIndex = 0;
        int rightIndex = 0;

        for (int i = left; i <= right; i++) {
            if (rightIndex >= rightLentgh) {
                int leftInt = leftArray[leftIndex];
                data[i] = leftInt;
                leftIndex++;
            } else if (leftIndex >= leftLength) {
                int rightInt = rightArray[rightIndex];
                data[i] = rightInt;
                rightIndex++;
            } else {
                int leftInt = leftArray[leftIndex];
                int rightInt = rightArray[rightIndex];

                if (leftInt < rightInt) {
                    data[i] = leftInt;
                    leftIndex++;
                } else {
                    data[i] = rightInt;
                    rightIndex++;
                }
            }
        }
    }
}
