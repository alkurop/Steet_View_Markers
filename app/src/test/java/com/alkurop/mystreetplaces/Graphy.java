package com.alkurop.mystreetplaces;

import org.junit.Test;

public class Graphy {
    public long factorial(int number) {
        long result = 1;

        for (int factor = 2; factor <= number; factor++) {
            result *= factor;
        }

        return result;
    }

    long combinations(int n, int k){
        return factorial(n) / (factorial(k) * factorial(n-k));
    }

    @Test public void t(){
        long combinations = combinations(4, 2);
        System.out.println(combinations);
    }
}
