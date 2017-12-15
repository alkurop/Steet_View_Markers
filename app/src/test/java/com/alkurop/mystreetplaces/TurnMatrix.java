package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.Arrays;

public class TurnMatrix {
    int[][] data = new int[][] {
            new int[] { 28, 33, 2, 4, 5 },
            new int[] { 0, 1, 0, 0, 5 },
            new int[] { 5, 8, 6, 8, 5 },
            new int[] { 5, 5, 1, 6, 5 }
    };

    //row - outer
    //column - inner

    @Test
    public void testTurnClockwise() {
        int[][] result = turnClockwise(data);
        for (int i = 0; i < result.length; i++) {
            System.out.println(Arrays.toString(result[i]));
        }

    }

    private int[][] turnClockwise(int[][] data) {

        int[][] result = new int[data[0].length][data.length];

        for (int vert = 0; vert < data.length; vert++) {
            for (int horiz = 0; horiz < data[vert].length; horiz++) {
                int newVert = horiz; //+
                int newHoriz = data.length - 1 - vert;
                result[newVert][newHoriz] = data[vert][horiz];
            }
        }
        return result;
    }
}
