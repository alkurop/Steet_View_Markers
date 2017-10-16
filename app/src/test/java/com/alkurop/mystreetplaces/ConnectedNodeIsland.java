package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class ConnectedNodeIsland {
    int[][] matrix = new int[][] {
            new int[] { 0, 1, 1, 0, 0 },
            new int[] { 1, 0, 1, 0, 0 },
            new int[] { 0, 0, 0, 1, 0 },
            new int[] { 0, 0, 1, 0, 0 },
            new int[] { 0, 0, 0, 0, 0 },
            new int[] { 0, 0, 0, 1, 0 },
            new int[] { 0, 0, 0, 1, 0 },
            new int[] { 0, 0, 0, 1, 0 },
    };

    @Test
    public void connectedNodesTest() {
        for (Point point : findConnectedNodes(matrix)) {
            System.out.println(point);
        }

    }

    LinkedList<Point> findConnectedNodes(int[][] matrix) {
        boolean[][] visited = new boolean[matrix.length][matrix[0].length];

        List<LinkedList<Point>> islands = new LinkedList<>();
        LinkedList<Point> currentIsland = new LinkedList<>();

        if (currentIsland.size() > 0) {
            islands.add(currentIsland);
        }

        LinkedList<Point> biggestIsland = new LinkedList<>();
        for (LinkedList<Point> island : islands) {
            if (island.size() > biggestIsland.size()) {
                biggestIsland = currentIsland;
            }
        }
        return biggestIsland;

    }



    class Point {
        final int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        boolean isConnected(Point other) {
            return Math.abs(x - other.x) <= 1 && Math.abs(y - other.y) <= 1;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "y=" + y +
                    ", x=" + x +
                    '}';
        }
    }

}
