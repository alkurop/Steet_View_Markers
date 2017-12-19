package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.Stack;

public class SortStackTest {
    Stack<Integer> getdata() {
        Stack<Integer> stack = new Stack<>();
        stack.push(10);
        stack.push(20);
        stack.push(3310);
        stack.push(140);
        stack.push(1);
        stack.push(12);
        stack.push(43);
        stack.push(510);
        stack.push(55);
        stack.push(1066);
        stack.push(1230);
        stack.push(110);
        return stack;
    }

    @Test
    public void sortStackTest() {
        Stack<Integer> data = getdata();
        sort(data);
        for (Integer integer : data) {
            System.out.println(integer);
        }
    }

    private void sort(Stack<Integer> data) {
        int count = 0;
        Stack<Integer> tempStack = new Stack<>();
        int size = data.size();

        boolean isSorted;
        do {
            isSorted = true;
            for (int i = 0; i < size; i++) {
                count++;
                Integer firstItem = data.pop();
                if (tempStack.isEmpty()) {
                    tempStack.push(firstItem);
                } else {
                    Integer secondItem = tempStack.pop();
                    if(secondItem < firstItem){
                        tempStack.push(firstItem);
                        tempStack.push(secondItem);
                        isSorted = false;
                    }else{
                        tempStack.push(secondItem);
                        tempStack.push(firstItem);
                    }
                }
            }

            for (int i = 0; i < size; i++) {
                data.push(tempStack.pop());
            }
        } while (!isSorted);
        System.out.println(count);
    }
}
