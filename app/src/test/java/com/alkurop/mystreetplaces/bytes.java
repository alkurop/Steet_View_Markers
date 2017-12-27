package com.alkurop.mystreetplaces;

import org.junit.Test;

public class bytes {
    @Test
    public void xorTest() {
        int k = -100;
        int l = 100;
        int mask = 0x80000000;
        System.out.println(Integer.toBinaryString(k));
        System.out.println(Integer.toBinaryString(l));
        System.out.println(Integer.toBinaryString(mask));
        System.out.println(Integer.toBinaryString(~k +1));
        System.out.println(Integer.toBinaryString(l));
        //System.out.println(Integer.compare(k ^ mask, l ^ mask));
    }
}
