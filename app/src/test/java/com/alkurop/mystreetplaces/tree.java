package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class tree {

    @Test
    public void mTest() {
        int[] array = new int[] { 222, 3, 4434, 6, 4, 0, 5, 0, 0, 0, 1, 0, 10, 28 };
        BinaryTree binaryTree = new BinaryTree();
        for (int i : array) {
            binaryTree.insert(i);
        }
        System.out.println(binaryTree.getNodeNumber(14));

    }

    class BinaryTree {
        Node root;

        class Node {
            final int val;
            Node small, big;

            Node(int val) {
                this.val = val;
            }

            @Override
            public String toString() {
                return "" + val;
            }
        }

        void insert(int value) {
            root = insertIntoParent(root, value);
        }

        private Node insertIntoParent(Node parent, int val) {
            if (parent == null) {
                return new Node(val);
            } else if (val < parent.val) {
                parent.small = insertIntoParent(parent.small, val);
            } else if (val >= parent.val) {
                parent.big = insertIntoParent(parent.big, val);
            }
            return parent;

        }

        List<Integer>    traverse() {
            return traverseParent(root);
        }

        private List<Integer> traverseParent(Node node) {
            List<Integer> nodeList = new LinkedList<>();
            if (node != null) {
                nodeList.addAll(traverseParent(node.big));
                nodeList.add(node.val);
                nodeList.addAll(traverseParent(node.small));
            }
            return nodeList;
        }

        int diagonal() {
            return getNodeDiagonal(root, 0);
        }

        int getNodeDiagonal(Node node, int parentSun) {
            if (node == null) {
                return parentSun;
            } else {
                int bigDiagonal = getNodeDiagonal(node.big, parentSun);
                int smallDiagonal = getNodeDiagonal(node.small, parentSun);
                int diagonal = bigDiagonal > smallDiagonal ? bigDiagonal : smallDiagonal;
                return diagonal + 1;
            }
        }

        List<Node> diagonalList() {
            return getNodeDiagonal(root);
        }

        List<Node> getNodeDiagonal(Node node) {
            LinkedList<Node> nodes = new LinkedList<>();

            if (node == null) {
                return nodes;
            }

            List<Node> nodeDiagonalBig = getNodeDiagonal(node.big);
            List<Node> nodeDiagonalSmall = getNodeDiagonal(node.small);

            LinkedList<Node> nodes1 = new LinkedList<>();
            nodes1.addAll(nodeDiagonalBig);
            nodes1.add(node);
            nodes1.addAll(nodeDiagonalSmall);

            List<Node> nodes2 = nodes1.size() > nodeDiagonalBig.size() ? nodes1 : nodeDiagonalBig;
            return nodes2.size() > nodeDiagonalSmall.size() ? nodes2 : nodeDiagonalSmall;

        }

        List<Node> getNodeHeight(Node node) {
            LinkedList<Node> nodes = new LinkedList<>();
            if (node != null) {
                nodes.add(node);
                List<Node> smallList = getNodeHeight(node.small);
                List<Node> bigListList = getNodeHeight(node.big);
                List<Node> diagonal = smallList.size() > bigListList.size() ? smallList : bigListList;
                nodes.addAll(diagonal);
            }
            return nodes;
        }

        Node getNodeNumber(int number) {
            return getNodeNumberRec(root, new Counter(number));
        }

        Node getNodeNumberRec(Node node, Counter counter) {
            if (node == null) {
                return null;
            }

            Node nodeNumberRec = getNodeNumberRec(node.small, counter);
            if (nodeNumberRec != null) {
                return nodeNumberRec;
            }

            counter.decrement();
            if (counter.isExpired()) return node;


            return getNodeNumberRec(node.big, counter);

        }
    }

    class Counter {
        int count;

        void decrement() {
            count--;
        }

        boolean isExpired() {
            return count == 0;
        }

        Counter(int count) {
            this.count = count;
        }
    }

}

