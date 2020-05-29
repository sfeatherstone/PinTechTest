package com.sfeatherstone.pinformat3.pinConversion

import kotlin.random.Random

class RandomNumberGenerator {
    fun getNextInt(): Int {
        return (0..15).random()
    }
}