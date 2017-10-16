package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest3 {

    @Test
    public void mTest() {
        int[] inOrder = new int[] { 4, 2, 5, 1, 6, 7, 3, 8 };
        int[] postorder = new int[] { 4, 5, 2, 6, 7, 8, 3, 1 };
        BinaryTree binaryTree = new BinaryTree();
        binaryTree.reconstructInOrderPostorder(inOrder, postorder);
        binaryTree.traverseBFS(new Visitor() {
            @Override
            public void visit(BinaryTree.Node node) {
                System.out.print(node.val);
            }
        });
        System.out.println("");

        binaryTree.traverseBsfOpt(new Visitor() {
            @Override
            public void visit(BinaryTree.Node node) {
                System.out.print(node.val);
            }
        });

    }

    class BinaryTree {
        Node root;

        class Node {
            final int val;
            Node left, right;

            Node(int val) {
                this.val = val;
            }

            @Override
            public String toString() {
                return "" + val;
            }
        }

        void reconstructInOrderPostorder(int[] inOrder, int[] postOrder) {
            root = reconstrInPost(inOrder, postOrder);
        }

        Node reconstrInPost(int[] inOrder, int[] postOrder) {
            if (postOrder.length == 0 || inOrder.length == 0) {
                return null;
            }
            int rootValue = postOrder[postOrder.length - 1];

            int inOrderIndex = 0;
            for (int i = 0; i < inOrder.length; i++) {
                if (inOrder[i] == rootValue) {
                    inOrderIndex = i;
                    break;
                }
            }

            int[] inOrderLeft = Arrays.copyOfRange(inOrder, 0, inOrderIndex);
            int[] postorderLeft = Arrays.copyOfRange(postOrder, 0, inOrderLeft.length);

            int[] inOrderRight = Arrays.copyOfRange(inOrder, inOrderIndex + 1, inOrder.length);
            int[] postorderRight = Arrays.copyOfRange(postOrder, postOrder.length - inOrderRight.length - 1, postOrder.length - 1);

            Node rootNode = new Node(rootValue);

            Node left = reconstrInPost(inOrderLeft, postorderLeft);
            Node right = reconstrInPost(inOrderRight, postorderRight);
            rootNode.left = left;
            rootNode.right = right;
            return rootNode;
        }

        void insert(int value) {
            root = insertIntoParent(root, value);
        }

        private Node insertIntoParent(Node parent, int val) {
            if (parent == null) {
                return new Node(val);
            } else if (val < parent.val) {
                parent.left = insertIntoParent(parent.left, val);
            } else if (val > parent.val) {
                parent.right = insertIntoParent(parent.right, val);
            }
            return parent;

        }

        void traverseBFS(Visitor visitor) {
            traverseBFSParent(root, visitor);
        }

        private void traverseBFSParent(Node node, Visitor visitor) {
            int height = findHeight(node);
            for (int i = 0; i < height; i++) {
                traverseLayer(node, i, visitor);
            }
        }

        void traverseLayer(Node node, int layer, Visitor visitor) {
            if (node == null) {
                return;
            }

            if (layer != 0) {
                traverseLayer(node.left, layer - 1, visitor);
                traverseLayer(node.right, layer - 1, visitor);
            } else {
                visitor.visit(node);
            }
        }

        void traverseBsfOpt(Visitor visitor){
            traverseBsfOpt(root, visitor);
        }

        void traverseBsfOpt(Node node, Visitor visitor) {
            LinkedList<Node> nodeQue = new LinkedList<>();
            nodeQue.add(node);
            while (!nodeQue.isEmpty()) {
                Node poll = nodeQue.poll();
                visitor.visit(poll);
                if (poll.left != null) {
                    nodeQue.add(poll.left);
                }
                if (poll.right != null) {
                    nodeQue.add(poll.right);
                }
            }
        }

        int findHeight(Node node) {
            if (node == null) {
                return 0;
            }
            int leftHeight = findHeight(node.left);
            int rightHeight = findHeight(node.right);
            return Math.max(leftHeight, rightHeight) + 1;
        }

        List<Integer> traverseInorder() {
            return traverseParent(root);
        }

        private List<Integer> traverseParent(Node node) {
            List<Integer> nodeList = new LinkedList<>();
            if (node != null) {
                nodeList.addAll(traverseParent(node.left));
                nodeList.add(node.val);
                nodeList.addAll(traverseParent(node.right));
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
                int bigDiagonal = getNodeDiagonal(node.right, parentSun);
                int smallDiagonal = getNodeDiagonal(node.left, parentSun);
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

            List<Node> nodeHeightBig = getNodeHeight(node.right);
            List<Node> nodeHeightSmall = getNodeHeight(node.left);

            List<Node> nodeDiagonalBig = getNodeDiagonal(node.right);
            List<Node> nodeDiagonalSmall = getNodeDiagonal(node.left);

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
                List<Node> smallList = getNodeHeight(node.left);
                List<Node> bigListList = getNodeHeight(node.right);
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

            Node nodeNumberRec = getNodeNumberRec(node.left, counter);
            if (nodeNumberRec != null) {
                return nodeNumberRec;
            }

            counter.decrement();
            if (counter.isExpired()) return node;

            return getNodeNumberRec(node.right, counter);

        }

    }

    interface Visitor {
        void visit(BinaryTree.Node node);
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

