package com.alkurop.mystreetplaces;

import android.support.annotation.NonNull;

import org.junit.Test;

import java.util.HashMap;
import java.util.TreeSet;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class strings {

    @Test
    public void mTest() {
        String stting = "CABABAABACCBBABddddd";
        int k = 1;
        System.out.println(soultion(stting, k));
    }

    public String soultion(String input, int a) {
        StringBuilder stringBuilder = new StringBuilder();
        HashMap<Character, CharWrapper> map = new HashMap<>();
        for (int position = 0; position < input.length(); position++) {
            char c = input.charAt(position);
            if (!map.containsKey(c)) {
                stringBuilder.append(c);
                CharWrapper value = new CharWrapper(c);
                value.position = position;
                map.put(c, value);
            } else {
                CharWrapper charWrapper = map.get(c);
                charWrapper.count++;
            }
        }

        TreeSet<CharWrapper> sortedSet = new TreeSet<>(map.values());
        while (sortedSet.size() > 0) {
            CharWrapper value = sortedSet.first();
            sortedSet.pollFirst();
            if (value.count != 0) {
                int i = stringBuilder.lastIndexOf(String.valueOf(value.mChar)) + a;
                if (i < stringBuilder.length()) {
                    stringBuilder.append(value.mChar);

                    value.position = stringBuilder.length() - 1;
                    value.count--;

                    sortedSet.add(value);
                } else {
                    return "cannot" + value.mChar + " " + stringBuilder.toString();
                }
            }
        }
        return stringBuilder.toString();
    }

    class CharWrapper implements Comparable<CharWrapper> {
        final char mChar;
        int count;
        int position;

        CharWrapper(char aChar) {
            mChar = aChar;
        }

        @Override
        public int compareTo(@NonNull CharWrapper o) {
             return this.position + o.position;
        }
    }

}

