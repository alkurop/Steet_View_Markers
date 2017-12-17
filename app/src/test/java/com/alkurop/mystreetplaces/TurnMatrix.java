package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.Arrays;

public class TurnMatrix {

    int iterationCount = 0;
    int[][] data = new int[][] {
            new int[] { 28, 33, 2, 4, 5, 1 },
            new int[] { 0, 1, 0, 0, 5, 2 },
            new int[] { 5, 8, 6, 8, 5, 3 },
            new int[] { 5, 5, 1, 6, 5, 5 },
            new int[] { 66, 55, 44, 55, 66, 77 },
            new int[] { 13, 15, 15, 16, 18, 45 }
    };

    //row - outer
    //column - inner

    @Test
    public void testTurnClockwise() {
        int[][] result = turnClockwise(data);
        ternMatrixNoAdditionalMemory(data);
        for (int i = 0; i < result.length; i++) {
            System.out.println(Arrays.toString(result[i]));
        }

        System.out.println("\n");

        for (int i = 0; i < data.length; i++) {
            System.out.println(Arrays.toString(result[i]));
        }

        System.out.println(iterationCount);

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

    public void ternMatrixNoAdditionalMemory(int[][] data) {
        for (int vert = 0; vert < data.length; vert++) {
            int length = data[0].length - vert * 2;
            for (int horiz = vert; horiz < length; horiz++) {
                iterationCount += 4;

                //step1
                int[] position = new int[] { vert, horiz };
                int val1 = getValueByPosition(data, position);
                convertPosition(position);

                int val2 = getValueByPosition(data, position);
                setValueByPosition(data, position, val1);

                //step2
                convertPosition(position);
                val1 = getValueByPosition(data, position);
                setValueByPosition(data, position, val2);

                //step3
                convertPosition(position);
                val2 = getValueByPosition(data, position);
                setValueByPosition(data, position, val1);

                //step4
                convertPosition(position);
                setValueByPosition(data, position, val2);
            }
        }
    }

    private int getValueByPosition(int[][] data, int[] position) {
        return data[position[0]][position[1]];
    }

    private void setValueByPosition(int[][] data, int[] position, int value) {
        data[position[0]][position[1]] = value;
    }

    public void convertPosition(int[] position) {
        int vert = position[0];
        int horiz = position[1];

        int newVert = horiz; //+
        int newHoriz = data.length - 1 - vert;
        position[0] = newVert;
        position[1] = newHoriz;
    }
}
