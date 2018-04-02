package com.alkurop.mystreetplaces

import org.junit.Test

class IncrementerTest {

    fun makeIncrementer(amount: Int): () -> Int {
        var runningTotal = 0

        val incrementer: () -> Int = {
            runningTotal += amount
            runningTotal
        }

        return incrementer
    }


    @Test
    fun incrementerTest() {
        val makeIncrementer = makeIncrementer(1)
        for(i in 0..24) {
            println(makeIncrementer())
        }
    }
}
