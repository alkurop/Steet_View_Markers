package com.alkurop.mystreetplaces;

import org.junit.Test;

public class KthElem {
    @Test
    public void test() {
        int[] input = new int[] { 1, 3, 5, 8, 4, 12, 88, 99, 2 };
        System.out.println(findKthSmallestElem(input, 9));
    }

    int findKthSmallestElem(int[] input, int k) {
        Tree tree = new Tree();
        for (int item : input) {
            tree.addItem(item);
        }
        Node kthItem = tree.findKthItem(k);
        if (kthItem != null) {
            return kthItem.value;
        } else {
            throw new IllegalArgumentException("no such element");
        }
    }

    class Tree {
        Node root;

        void addItem(int value) {
            root = addItemRec(root, value);
        }

        Node addItemRec(Node root, int value) {
            if (root == null) {
                return new Node(value);
            }
            if (value < root.value) {
                root.left = addItemRec(root.left, value);
            } else if (value > root.value) {
                root.right = addItemRec(root.right, value);
            }
            return root;
        }

        Node findKthItem(int k) {
            return findKthItemRec(new Counter(k), root);
        }

        Node findKthItemRec(Counter counter, Node node) {
            if (node == null) return null;

            Node left = findKthItemRec(counter, node.left);
            if (left != null) return left;

            counter.decrement();
            if (counter.isExpited()) return node;

            return findKthItemRec(counter, node.right);

        }

        class Counter {
            int count;

            Counter(int count) {
                this.count = count;
            }

            void decrement() {
                count--;
            }

            boolean isExpited() {
                return count <= 0;
            }
        }
    }

    class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }
}
