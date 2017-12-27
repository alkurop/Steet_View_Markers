package com.alkurop.mystreetplaces.greedy;

import android.support.annotation.NonNull;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;

public class PriyankaToys {
    int[] input = new int[] { 1, 2, 3, 17, 102, 4, 6, 8, 10, 11, 13, 15, 17, 33, 9 };

    @Test
    public void test() {
        System.out.println(getCost(input));
    }

    int getCost(int[] input) {
        Arrays.sort(input);

        PriorityQueue<ToysContainer> indexCollections = new PriorityQueue<>();
        for (int i = 0; i < input.length; i++) {
            ToysContainer container = new ToysContainer();
            container.add(i);
            int weight = input[i];
            for (int k = i + 1; k < input.length; k++) {
                int nextWeight = input[k];
                if (nextWeight <= weight + 4) {
                    container.add(k);
                } else {
                    break;
                }
            }
            if (container.toys.size() > 1) {
                indexCollections.add(container);
            }
        }

        ArrayList<ArrayList<Integer[]>> smallestCollections = findSmallestCollections(indexCollections);

        int minCount = input.length;
        for (ArrayList<Integer[]> smallestCollection : smallestCollections) {
            int totalSize = input.length;
            for (Integer[] integers : smallestCollection) {
                totalSize -= integers.length - 1;
            }
            if (totalSize < minCount) {
                minCount = totalSize;
            }
        }
        return minCount;
    }

    ArrayList<ArrayList<Integer[]>> findSmallestCollections(PriorityQueue<ToysContainer> queue) {
        ArrayList<ArrayList<Integer[]>> collections = new ArrayList<>();
        int outerCounter = queue.size();
        while (outerCounter > 0) {
            outerCounter--;

            ArrayList<Integer[]> integers = new ArrayList<>();
            collections.add(integers);

            ToysContainer poll = queue.poll();
            Iterator<ToysContainer> iterator = queue.iterator();

            Integer[] array = new Integer[poll.toys.size()];
            poll.toys.toArray(array);
            integers.add(array);

            HashSet<Integer> indexes = new HashSet<>();
            indexes.addAll(poll.toys);

            while (iterator.hasNext()) {
                ToysContainer next = iterator.next();
                boolean shouldAdd = true;
                for (Integer toy : next.toys) {
                    if (indexes.contains(toy)) {
                        shouldAdd = false;
                        break;
                    }
                }
                if (shouldAdd) {
                    indexes.addAll(next.toys);
                    Integer[] array2 = new Integer[next.toys.size()];
                    next.toys.toArray(array2);
                    integers.add(array2);
                }
            }
        }
        return collections;
    }

    class ToysContainer implements Comparable<ToysContainer> {
        final ArrayList<Integer> toys;

        ToysContainer() {
            toys = new ArrayList<>();
        }

        @Override
        public int compareTo(@NonNull ToysContainer o) {
            return Integer.compare(o.toys.size(), this.toys.size());
        }

        void add(int toyIndex) {
            toys.add(toyIndex);
        }
    }
}
