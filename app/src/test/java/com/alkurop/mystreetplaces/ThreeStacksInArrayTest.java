package com.alkurop.mystreetplaces;

import org.jetbrains.annotations.Nullable;
import org.junit.Test;

public class ThreeStacksInArrayTest {
    @Test
    public void testStack() {
        ThreeStacks stacks = new ThreeStacks();

        for (int i = 50; i < 100; i++) {
            stacks.pushToSecond(i);
        }
        for (int i = 0; i < 50; i++) {
            stacks.pushToFirst(i);
        }
        for (int i = 100; i < 150; i++) {
            stacks.pushToThird(i);
        }
        for (int i = 100; i < 150; i++) {
            System.out.println("Third " + stacks.popThird());
        }
        for (int i = 0; i < 50; i++) {
            System.out.println("First " + stacks.popFirst());
        }
        for (int i = 50; i < 100; i++) {
            System.out.println("Second " + stacks.popSecond());
        }
    }

    interface ThreeStacksInArray {
        void pushToFirst(int value);

        void pushToSecond(int value);

        void pushToThird(int value);

        @Nullable
        Integer popFirst();

        @Nullable
        Integer popSecond();

        @Nullable
        Integer popThird();
    }

    class ThreeStacks implements ThreeStacksInArray {
        private static final int STACK_COUNT = 3;
        private static final int DATA_OFFSET = STACK_COUNT * 2;
        private static final int INDEX_OFFSET = DATA_OFFSET - 1;
        private static final int DEFAULT_LENGTH = 15;
        private static final float GROW_K = 1.5f;
        private Integer[] container;

        public ThreeStacks() {
            int initialLength = DATA_OFFSET + DEFAULT_LENGTH * STACK_COUNT;
            container = new Integer[initialLength];
            container[0] = 0;
            container[1] = 0;
            container[2] = 0;
            container[3] = DEFAULT_LENGTH;
            container[4] = DEFAULT_LENGTH;
            container[5] = DEFAULT_LENGTH;
        }

        @Override
        public void pushToFirst(int value) {
            pushToStack(value, 0);
        }

        @Override
        public void pushToSecond(int value) {
            pushToStack(value, 1);
        }

        @Override
        public void pushToThird(int value) {
            pushToStack(value, 2);
        }

        @Nullable
        @Override
        public Integer popFirst() {
            return popFromStack(0);
        }

        @Nullable
        @Override
        public Integer popSecond() {
            return popFromStack(1);
        }

        @Nullable
        @Override
        public Integer popThird() {
            return popFromStack(2);
        }

        private void pushToStack(int value, int stackNumber) {

            if (shouldGrow(stackNumber)) {
                growStack(stackNumber);
            }

            int offset = INDEX_OFFSET;
            for (int i = 0; i < stackNumber; i++) {
                offset += getMax(i);
            }
            offset += getCur(stackNumber) + 1;
            container[offset] = value;

            increment(stackNumber);
        }

        @Nullable
        private Integer popFromStack(int stackNumber) {
            int cur = getCur(stackNumber);
            if (cur == 0) {
                return null;
            }

            int offset = INDEX_OFFSET + cur;
            for (int i = 0; i < stackNumber; i++) {
                offset += getMax(i);
            }

            Integer value = container[offset];
            if (value != null) {
                decrement(stackNumber);
                container[offset] = null;
            }
            if (shouldShrink(stackNumber)) {
                shrink(stackNumber);
            }
            return value;
        }

        private boolean shouldGrow(int stackNumber) {
            int maxStackLength = getMax(stackNumber);
            int newNumber = getCur(stackNumber) + 1;
            return newNumber >= maxStackLength;
        }

        private void growStack(int stackNumber) {
            System.out.println("Grow Stack " + stackNumber);

            int currentMaxLength = getMax(stackNumber);
            int newMaxStackLength = (int) (currentMaxLength * GROW_K);
            int delta = newMaxStackLength - currentMaxLength;

            Integer[] newArray = new Integer[container.length + delta];

            int offset = INDEX_OFFSET;
            int newOffset = offset;

            //copy indexes
            System.arraycopy(container, 0, newArray, 0, DATA_OFFSET);

            for (int i = 0; i < STACK_COUNT; i++) {
                int maxLength = getMax(i);
                int dataLength = getCur(i);

                System.arraycopy(container, offset, newArray, newOffset, dataLength + 1);

                offset += maxLength;
                newOffset += maxLength;
                if (i == stackNumber) {
                    newArray[stackNumber + STACK_COUNT] += delta;
                    newOffset += delta;
                }
            }

            container = newArray;
        }

        private boolean shouldShrink(int stack) {
            int maxLength = getMax(stack);
            int cur = getCur(stack);
            int delta = maxLength - cur;
            int newMaxLength = (int) (cur * GROW_K);
            int required = newMaxLength < DEFAULT_LENGTH ? DEFAULT_LENGTH : newMaxLength;
            return delta / 2 > required;
        }

        private void shrink(int stack) {
            System.out.println("Shrink Stack " + stack);

            int cur = getCur(stack);
            int temp = (int) (cur * GROW_K);
            int newStackMaxLength = temp < DEFAULT_LENGTH ? DEFAULT_LENGTH : temp;

            int newArrayLength = DATA_OFFSET;
            for (int i = 0; i < STACK_COUNT; i++) {
                newArrayLength += (i == stack) ? newStackMaxLength : getMax(i);
            }
            Integer[] newArray = new Integer[newArrayLength];

            //copy indexes
            System.arraycopy(container, 0, newArray, 0, DATA_OFFSET);

            int offset = INDEX_OFFSET;
            int newOffset = INDEX_OFFSET;

            for (int i = 0; i < STACK_COUNT; i++) {
                int currentMax = getMax(i);
                int length = (i == stack) ? newStackMaxLength : currentMax;
                System.arraycopy(container, offset, newArray, newOffset, length);
                offset += currentMax;
                newOffset += length;
            }
            newArray[stack + STACK_COUNT] = newStackMaxLength;
            container = newArray;
        }

        private int getCur(int stack) {
            return container[stack];
        }

        private int getMax(int stack) {
            return container[stack + STACK_COUNT];
        }

        private void increment(int stack) {
            container[stack]++;
        }

        private void decrement(int stack) {
            container[stack]--;
        }
    }
}
