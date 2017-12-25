package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

public class GraphsDsf {
    static class Graph {
        final int [][]mat;

        Graph(int number){
            mat = new int [number][number];
        }

        public void addIntersection(int x, int y){
            mat[x][y] = 1;
            mat[y][x] = 1;
        }

        public long factorial(int number) {
            long result = 1;

            for (int factor = 2; factor <= number; factor++) {
                result *= factor;
            }

            return result;
        }

        long combinations(int n, int k){
            return factorial(n) / (factorial(k) * factorial(n-k));
        }

        long count(){
            LinkedList<HashSet<Integer>> intersections = getIntersections(mat);

            int count = mat.length;
            long totalCount = combinations(count, 2);
            long interCount = 0;
            for(HashSet<Integer> inter : intersections){
                System.out.println(inter.size());
                interCount += combinations(inter.size(), 2);
            }
            return totalCount ;
        }

        LinkedList<HashSet<Integer>> getIntersections(int[][] mat) {
            LinkedList<HashSet<Integer>> islands = new LinkedList<>();
            int nodeCount = mat.length;
            if (nodeCount == 0) {
                return islands;
            }
            boolean[] isVisited = new boolean[nodeCount];
            Stack<Integer> stack = new Stack<>();

            for (int k = 0; k < nodeCount; k++) {
                if (!isVisited[k]) {
                    stack.push(k);
                    if (islands.isEmpty() || !islands.getLast().isEmpty()) {
                        islands.add(new HashSet<Integer>());
                    }
                    dsfRec(mat, nodeCount, stack, isVisited, islands);
                }
            }
            if (islands.getLast().isEmpty()) {
                islands.removeLast();
            }
            return islands;
        }

        void dsfRec(int[][] mat,
                    int nodeCount,
                    Stack<Integer> stack,
                    boolean[] isVisited,
                    LinkedList<HashSet<Integer>> islands) {

            while (!stack.isEmpty()) {
                Integer islandNumber = stack.pop();
                isVisited[islandNumber] = true;
                for (int i = 0; i < nodeCount; i++) {
                    boolean intersection = mat[islandNumber][i] == 1;
                    if (intersection) {
                        islands.getLast().add(i);
                        if (!isVisited[i]) {
                            stack.push(i);
                            dsfRec(mat, nodeCount, stack, isVisited, islands);
                        }
                    }
                }
            }
        }
    }

    @Test public void testInt(){
        Graph graph = new Graph(5);
        graph.addIntersection(0,1);
        graph.addIntersection(2,3);
        graph.addIntersection(0,4);
        graph.count();
    }



}

