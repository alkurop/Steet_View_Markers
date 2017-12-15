package com.alkurop.mystreetplaces;

import org.junit.Test;

public class ListDuplicateTest {
    @Test
    public void duplicateListetTest() {
        LL list = getTestData();
        LL result = removeDuplicates(list);
        System.out.println(result.head.toString());
    }

    private LL removeDuplicates(LL testData) {

        Node current = testData.head;

        while (current != null) {
            Node prev = current;
            Node next = current.next;
            while (next != null) {
                if (next.val == current.val) {
                    prev.next = next.next;
                }
                else{
                    prev = next;
                }
                next = next.next;
            }
            current = current.next;
        }

        return testData;
    }

    LL getTestData() {
        LL data = new LL();
        data.add(10);
        data.add(20);
        data.add(2);
        data.add(3);
        data.add(20);
        data.add(10);
        data.add(20);
        data.add(300);
        return data;
    }

    class Node {
        final int val;
        Node next;

        Node(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "val=" + val +
                    ", next=" + next +
                    '}';
        }
    }

    class LL {
        Node head;

        void add(int val) {
            if (head == null) {
                head = new Node(val);
            } else {
                recAdd(head, val);
            }
        }

        private void recAdd(Node node, int val) {
            if (node.next != null) {
                recAdd(node.next, val);
            } else {
                node.next = new Node(val);
            }
        }
    }
}
