package com.alkurop.mystreetplaces;

import org.junit.Test;

public class test33 {
    @Test public void test(){
        print(solution("91011"));
    }
    static int solution(String input){
        int firstResult = 0;

        int numberLength = 1;
        int nextResult = 0;
        int nextNumberIndex = 0;

        while(nextNumberIndex + numberLength < input.length()){
            String nextString = input.substring(nextNumberIndex, nextNumberIndex + numberLength);
            String lastChar = nextString.substring(nextString.length() - 1);

            int nextInt = Integer.parseInt(nextString);
            int lastPartInt = Integer.parseInt(lastChar);

            if(lastPartInt == 9){
                numberLength ++;
            }

            if(nextInt - nextResult == 1){
                if(firstResult == 0 ){
                    firstResult = nextResult;
                }
                nextResult = nextInt;
                nextNumberIndex += numberLength;

            } else{
                numberLength ++;
            }
        }
        return firstResult;
    }

    static void print(int result){
        if(result == 0){
            System.out.println("NO");
        }else{
            System.out.println("YES " + result);
        }
    }

}
