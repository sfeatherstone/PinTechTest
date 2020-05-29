package com.sfeatherstone.pinformat3.pinConversion

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class Iso3PinTest {

    var index = 0
    val numbers = arrayOf(0x5,0xa,0x5,0xa, 0x5,0xa,0x5,0xa, 0x5,0xa,0x5,0xa, 0x5,0xa,0x5,0xa)
    val randomNumberGenerator: RandomNumberGenerator = mock()

    @Before
    fun setup() {
        index = 0
    }

    private fun getNextRandom():Int {
        return numbers[index++]
    }

    @Test
    fun `When entering PAN with non digits fail`() {
        val iso3Pin = Iso3Pin(randomNumberGenerator)
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PanHasNonNumbers), iso3Pin.generatePinBlock("1234", "1234567890123456 "))
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PanHasNonNumbers), iso3Pin.generatePinBlock("1234", " 1234567890123456"))
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PanHasNonNumbers), iso3Pin.generatePinBlock("1234", "1x34567890123456"))
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PanHasNonNumbers), iso3Pin.generatePinBlock("1234", "1234568901234Ω6"))
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PanHasNonNumbers), iso3Pin.generatePinBlock("1234", "1234568901234.6"))
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PanHasNonNumbers), iso3Pin.generatePinBlock("1234", "1234.68901234.6"))
    }

    @Test
    fun `When entering PIN with non digits fail`() {
        val iso3Pin = Iso3Pin(randomNumberGenerator)
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PinHasNonNumbers), iso3Pin.generatePinBlock("1234 ", "1234567890123456"))
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PinHasNonNumbers), iso3Pin.generatePinBlock(" 1234 ", "1234567890123456"))
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PinHasNonNumbers), iso3Pin.generatePinBlock(" 1234", "1234567890123456"))
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PinHasNonNumbers), iso3Pin.generatePinBlock("12Ω4", "1234567890123456"))
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PinHasNonNumbers), iso3Pin.generatePinBlock("123.4", "1234567890123456"))
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PinHasNonNumbers), iso3Pin.generatePinBlock("2345678980e1", "1234567890123456"))
    }

    @Test
    fun `When entering PIN to short fail`() {
        val iso3Pin = Iso3Pin(randomNumberGenerator)
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PinTooShort), iso3Pin.generatePinBlock("123", "1234567890123456"))
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PinTooShort), iso3Pin.generatePinBlock("12", "1234567890123456"))
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PinTooShort), iso3Pin.generatePinBlock("1", "1234567890123456"))
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PinTooShort), iso3Pin.generatePinBlock("", "1234567890123456"))
    }

    @Test
    fun `When entering PIN to long fail`() {
        val iso3Pin = Iso3Pin(randomNumberGenerator)
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PinTooLong), iso3Pin.generatePinBlock("1234567890123", "1234567890123456"))
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PinTooLong), iso3Pin.generatePinBlock("12345678901234", "1234567890123456"))
        assertEquals(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PinTooLong), iso3Pin.generatePinBlock("123456789012345", "1234567890123456"))
    }

    @Test
    fun `When entering PIN check for pass`() {
        val iso3Pin = Iso3Pin(randomNumberGenerator)
        whenever(randomNumberGenerator.getNextInt()).thenReturn(0)
        assertEquals(Iso3Pin.Result.Success(byteArrayOf(0x04,0x0,0x45,0x67,0x89u.toByte(),0x01,0x23,0x45)), iso3Pin.generatePinBlock("0000", "0123456789012345"))
        index = 0
        assertNotEquals("check for false tests", Iso3Pin.Result.Success(byteArrayOf(0x04,0x01,0x45,0x67,0x89u.toByte(),0x01,0x23,0x45)), iso3Pin.generatePinBlock("0000", "0123456789012345"))
        index = 0
        assertEquals(Iso3Pin.Result.Success(byteArrayOf(0x04,0x12,0x71,0x67,0x89u.toByte(),0x01,0x23,0x45)), iso3Pin.generatePinBlock("1234", "0123456789012345"))
        index = 0
        assertEquals(Iso3Pin.Result.Success(byteArrayOf(0x05,0x12,0x71,0x37,0x89u.toByte(),0x01,0x23,0x45)), iso3Pin.generatePinBlock("12345", "0123456789012345"))
        index = 0
        assertEquals(Iso3Pin.Result.Success(byteArrayOf(0x06,0x12,0x71,0x31,0x89u.toByte(),0x01,0x23,0x45)), iso3Pin.generatePinBlock("123456", "0123456789012345"))
     }

    @Test
    fun `When entering PIN check for pass with changing random`() {
        val iso3Pin = Iso3Pin(randomNumberGenerator)
        whenever(randomNumberGenerator.getNextInt()).thenAnswer{ getNextRandom() }
        assertEquals(Iso3Pin.Result.Success(byteArrayOf(0x04,0x0,0x45,0x3d,0xd3u.toByte(),0x5b,0x79,0x1f)), iso3Pin.generatePinBlock("0000", "0123456789012345"))
        index = 0
        assertNotEquals("check for false tests", Iso3Pin.Result.Success(byteArrayOf(0x04,0x01,0x45,0x67,0x89u.toByte(),0x01,0x23,0x45)), iso3Pin.generatePinBlock("0000", "0123456789012345"))
        index = 0
        assertEquals(Iso3Pin.Result.Success(byteArrayOf(0x04,0x12,0x71,0x3d,0xd3u.toByte(),0x5b,0x79,0x1f)), iso3Pin.generatePinBlock("1234", "0123456789012345"))
    }

}