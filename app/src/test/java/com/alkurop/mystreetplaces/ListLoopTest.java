package com.alkurop.mystreetplaces;

import org.junit.Test;

public class ListLoopTest {
    class ListItem {
        ListItem next;
        final int value;

        ListItem(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "ListItem{" +
                    ", value=" + value +
                    '}';
        }

        boolean hasNext() {
            return next != null;
        }
    }

    ListItem getDataHead() {
        ListItem head = new ListItem(0);
        ListItem current = new ListItem(1);
        head.next = current;
        for (int i = 0; i < 49; i++) {
            ListItem item = new ListItem(i);
            current.next = item;
            current = item;
        }
        ListItem current2 = new ListItem(50);
        current.next = current2;
        for (int i = 100; i < 200; i++) {
            ListItem item = new ListItem(i);
            current2.next = item;
            current2 = item;
        }
        current2.next = current;
        return head;
    }

    @Test
    public void loopTest() {
        ListItem listItem = getDataHead();
        ListItem loopItem = getLoopItem(listItem);
        System.out.println(loopItem);
    }

    private ListItem getLoopItem(ListItem listItem) {

        if (listItem == null || !listItem.hasNext()) return null;

        ListItem slow = listItem;
        ListItem fast = listItem;

        while (slow != null && fast != null && fast.hasNext()) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                break;
            }
        }

        if (slow != null) {
            fast = listItem;
            while (fast != slow){
                slow = slow.next;
                fast = fast.next;
            }

        }

        return slow;
    }
}
