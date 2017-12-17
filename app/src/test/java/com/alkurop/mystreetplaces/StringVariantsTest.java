package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class StringVariantsTest {
    int iterationCounter = 0;

    @Test
    public void testStringVariants() {
        int input = 1111111;
        int result = stringVariants(input);
        System.out.println("Result " + result);
        System.out.println("Iterations " + iterationCounter);
    }

    public int stringVariants(int input) {
        String stringInput = String.valueOf(input);
        HashMap<String, Integer> cache = new HashMap<>();
        return localStringVariants(stringInput, cache);
    }

    private int localStringVariants(String stringInput, HashMap<String, Integer> cache) {
        int MAX_LETTER_INDEX = 26;
        if (cache.containsKey(stringInput)) {
            return cache.get(stringInput);
        }
        iterationCounter++;
        LinkedList<String> remainingVariants = new LinkedList<>();
        int resultCount = 0;

        char[] chars = stringInput.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char[] testChars = Arrays.copyOfRange(chars, 0, chars.length - i);
            String testString = new String(testChars);
            int testInt = Integer.parseInt(testString);
            boolean startsWithZeroAndMoreThanOneDigit = testString.length() > 1
                    && testString.substring(0, 1).equalsIgnoreCase("0");

            if (testInt <= MAX_LETTER_INDEX && !startsWithZeroAndMoreThanOneDigit) {
                String remainingString = stringInput.substring(testString.length());
                if (remainingString.length() > 0) {
                    remainingVariants.add(remainingString);
                } else {
                    resultCount++;
                }

            }
        }

        for (String remainingVariant : remainingVariants) {
            resultCount += localStringVariants(remainingVariant, cache);
        }

        cache.put(stringInput, resultCount);

        return resultCount;
    }

    @Test
    public void iterationCounter() {
        int expected = countIterations(7);
        System.out.println("Expected " + expected);
    }

    int countIterations(int n) {
        if (n == 0) return 0;
        int itertions = n;
        itertions += countIterations(n - 1);
        return itertions;
    }

}
