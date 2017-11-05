package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class Kosaraju {
    int coint = 0;
    int[][] matrix = new int[][] {
            new int[] { 1, 1, 1, 1, 1 },
            new int[] { 1, 1, 1, 1, 1 },
            new int[] { 1, 1, 1, 1, 1 },
            new int[] { 1, 1, 1, 1, 1 },
            new int[] { 1, 1, 1, 1, 1 },
            new int[] { 1, 1, 1, 1, 1 },
            new int[] { 1, 1, 1, 1, 1 },
            new int[] { 1, 1, 1, 1, 1 },
    };

    @Test
    public void connectedNodesTest() {
        for (Point point : findConnectedNodes(matrix)) {
            System.out.println(point);
        }
        System.out.println(coint);

    }

    LinkedList<Point> findConnectedNodes(int[][] matrix) {
        boolean[][] visited = new boolean[matrix.length][matrix[0].length];

        List<LinkedList<Point>> islands = new LinkedList<>();

        for (int i = 0; i < matrix.length; i++) {
            int[] row = matrix[i];
            for (int i1 = 0; i1 < row.length; i1++) {
                LinkedList<Point> points = traverseMatrix(matrix, visited, i1, i);
                if (points.size() > 0) {
                    islands.add(points);
                }
            }
        }

        LinkedList<Point> biggestIsland = new LinkedList<>();
        for (LinkedList<Point> island : islands) {
            if (island.size() > biggestIsland.size()) {
                biggestIsland = island;
            }
        }
        return biggestIsland;

    }

    LinkedList<Point> traverseMatrix(int[][] matrix, boolean[][] visited, int x, int y) {
        coint++;
        LinkedList<Point> linkedList = new LinkedList<>();
        Point point = new Point(x, y, matrix[y][x]);
        if (!point.isVisited(visited)) {
            point.visited(visited);
            if (point.isOn()) {
                linkedList.add(point);
                if (matrix.length - 1 > y && matrix[y].length - 1 > x) {
                    linkedList.addAll(traverseMatrix(matrix, visited, x + 1, y));
                }
                if (x > 0) {
                    linkedList.addAll(traverseMatrix(matrix, visited, x - 1, y));
                }
                if (y < matrix.length - 1) {
                    linkedList.addAll(traverseMatrix(matrix, visited, x, y + 1));
                }
                if (y > 0) {
                    linkedList.addAll(traverseMatrix(matrix, visited, x, y - 1));
                }
                if (x > 0 && y < matrix.length - 1) {
                    linkedList.addAll(traverseMatrix(matrix, visited, x - 1, y + 1));
                }
                if (y > 0 && x < matrix[y].length - 1) {
                    linkedList.addAll(traverseMatrix(matrix, visited, x + 1, y - 1));
                }
                if (x < matrix[y].length - 1 && y < matrix.length - 1) {
                    linkedList.addAll(traverseMatrix(matrix, visited, x + 1, y + 1));
                }
                if (x > 0 && y > 0) {
                    linkedList.addAll(traverseMatrix(matrix, visited, x - 1, y - 1));
                }

            }
        }
        return linkedList;
    }

    class Point {
        final int x, y, value;

        Point(int x, int y, int value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        boolean isConnected(Point other) {
            return Math.abs(x - other.x) <= 1 && Math.abs(y - other.y) <= 1;
        }

        boolean isVisited(boolean[][] visited) {
            return visited[y][x];
        }

        void visited(boolean[][] visited) {
            visited[y][x] = true;
        }

        boolean isOn() {
            return value > 0;
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

