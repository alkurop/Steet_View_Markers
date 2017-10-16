package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class polindrome {
    @Test
    public void test() {
        String[] words = new String[] { "motom", ";aalala", "ommommo", "popwawawpop", "oooppsad" };
        String[] result = discoverPols(words);
        for (String s : result) {
            System.out.println(s);
        }
    }

    private String[] discoverPols(String[] words) {
        ArrayList<String> resultArrayList = new ArrayList<>();
        for (String word : words) {
            if (isPolindrom(word.toCharArray())) {
                resultArrayList.add(word);
            }
        }
        String[] a = new String[resultArrayList.size()];
        resultArrayList.toArray(a);
        return a;
    }

    boolean isPolindrom(char[] input) {
        if (input.length == 1) {
            return true;
        } else if (input.length % 2 != 1) {
            return false;
        } else {
            boolean polindrom = isPolindrom(Arrays.copyOfRange(input, 1, input.length - 1));
            boolean equalFirstLst = input[0] == input[input.length - 1];
            return equalFirstLst && polindrom;
        }
    }
}
