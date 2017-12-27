package com.alkurop.mystreetplaces.greedy;

import org.junit.Test;

import java.util.PriorityQueue;

public class Cakewalk {
    int[] input = new int[] { 1, 3, 2 };

    @Test
    public void cakeTest() {
        System.out.println("result " + findMinClori(input));
    }

    int findMinClori(int[] input) {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        for (int i : input) {
            queue.add(i);
        }
        int count = input.length - 1;
        int sum = 0;

        while (count >= 0) {
            Integer next = queue.poll();
            System.out.println("next " + next);
            System.out.println("pow " + count);
            double add = next * Math.pow(2, count);
            System.out.println("add " + add);
            sum += add;
            count--;
        }
        return sum;
    }
}
