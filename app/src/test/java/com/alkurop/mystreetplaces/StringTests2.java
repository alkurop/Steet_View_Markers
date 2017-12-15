package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class StringTests2 {
    private static String MATCH = "hackerrank";


    static boolean solution(String input){
        int nextIndex = 0;
        char [] matchArray = MATCH.toCharArray();
        for(int matchIndex = 0; matchIndex < MATCH.length(); matchIndex ++){
            char matchChar = matchArray[matchIndex];
            String substring = input.substring(nextIndex);
            int index = substring.indexOf(matchChar);
            if(index != -1){
                nextIndex = index;
            }else{
                return false;
            }
        }
        return true;
    }

    static void printResult(boolean result){
        System.out.println(result ? "YES" : "NO");
    }

    @Test public void test(){
        prints(solutions("We promptly judged antique ivory buckles for the next prize"));
    }


    static boolean solutions(String input){
        Set<Character> charSet = new HashSet<>();
        for(char c = 'a'; c <= 'z'; c ++){
            charSet.add(c);
        }
        char [] inputArray = input.toLowerCase().toCharArray();

        for (char c :inputArray){
            if(charSet.contains(c)){
                charSet.remove(c);
            }
        }

        return charSet.size() == 0;
    }

    static void prints(boolean result){
        System.out.println(result ? "pangram" : "not pangram");
    }

}
