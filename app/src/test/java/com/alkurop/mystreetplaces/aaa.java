package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.Arrays;

public class aaa {

    @Test
    public void test33() {
        int[] input = new int[] {
                512, 125, 928, 381, 890, 90, 512, 789, 469, 473, 908, 990, 195, 763, 102, 643, 458, 366, 684, 857, 126, 534, 974, 875, 459, 892, 686, 373, 127, 297, 576, 991, 774, 856, 372, 664, 946,
                237, 806, 767, 62, 714, 758, 258, 477, 860, 253, 287, 579, 289, 496

        };

        System.out.println(find_min_actions(input));
    }

    static int find_min_actions(int[] input) {
        Arrays.sort(input);

        int[] minMax = new int[] { 0, input.length - 1 };
        int calcCount = 0;
        boolean shouldIncrease = true;

        while (minMax[0] != minMax[1]) {
            boolean switchMin = false;
            int dif = input[minMax[1]] - input[minMax[0]];
            int calcDif = 0;
            if (dif >= 5) {
                int ost = dif % 5;
                int times = (dif - ost) / 5;
                calcDif = 5 * times;
                calcCount += times;
                switchMin = ost == 0;
            } else if (dif >= 2) {

                int ost = dif % 2;
                int times = (dif - ost) / 2;
                calcDif = 2 * times;
                calcCount += times;
                switchMin = ost == 0;

            } else if (dif >= 1) {
                calcDif = 1;
                calcCount++;
                switchMin = true;
            }

            minMax = decrease(input, calcDif, minMax[1], minMax[0], switchMin);
            shouldIncrease = !shouldIncrease;
        }
        return calcCount;
    }

    static int[] minMax(int[] input) {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        int maxIndex = 0;
        int minIndex = 0;
        for (int i = 0; i < input.length; i++) {
            if (input[i] > max) {
                max = input[i];
                maxIndex = i;
            }
            if (input[i] < min) {
                min = input[i];
                minIndex = i;
            }
        }
        for (int i = 0; i < input.length; i++) {
            input[i] -= min;
        }
        return new int[] { minIndex, maxIndex };
    }

    static int[] decrease(int[] input, int dif, int maxInput, int minInput, boolean sw) {
        int maxIndex = maxInput - 1 < 0 ? input.length - 1 : maxInput - 1;
        if (Integer.MIN_VALUE - dif <= input[maxIndex]) {
            for (int i = 0; i < input.length; i++) {
                input[i] += Integer.MAX_VALUE /2;
            }
        }
        input[maxInput] -= dif;
        int i = sw ? maxInput : minInput;
        if (i == maxIndex) {
            return minMax(input);
        }
        return new int[] { i, maxIndex };
    }

}
