package com.sfeatherstone.pinformat3.pinConversion

import kotlin.experimental.or

internal val onlyNumbersRegex by lazy { Regex("""^[0-9]*$""") }
internal fun String.isOnlyDigits() = onlyNumbersRegex.matches(this)

class Iso3Pin(private val randomNumberGenerator: RandomNumberGenerator) {

    sealed class Result {
        enum class FailReason {
            PinHasNonNumbers,
            PinTooShort,
            PinTooLong,
            PanToShort,
            PanTooLong,
            PanCheckFailed,
            PanHasNonNumbers
        }
        data class Fail(val reason: FailReason) : Result()
        data class Success(val pinBlock: ByteArray) : Result() {
            override fun equals(other: Any?): Boolean {
                return other is Success && pinBlock.contentHashCode() == other.pinBlock.contentHashCode()
            }
        }
    }

    private fun setHiNibbleValue(value: Byte): Byte = (0xF0 and (value.toInt() shl 4)).toByte()
    private fun setLowNibbleValue(value: Byte): Byte = (0x0F and value.toInt()).toByte()

    private fun validateParams(pin: String, pan: String): Result? {
        if (pan.length<14) {
            return Result.Fail(Result.FailReason.PanToShort)
        }
        if (!pan.isOnlyDigits()) {
            return Result.Fail(Result.FailReason.PanHasNonNumbers)
        }
        if (pin.length < 4) {
            return Result.Fail(Result.FailReason.PinTooShort)
        }
        if (pin.length > 12) {
            return Result.Fail(Result.FailReason.PinTooLong)
        }
        if (!pin.isOnlyDigits()) {
            return Result.Fail(Result.FailReason.PinHasNonNumbers)
        }
        return null
    }

    private fun preparePart1(number: String): IntArray =
        IntArray(16) { n ->
            when {
                n == 0 -> 0
                n == 1 -> number.length
                n - 2 < number.length -> (number[n-2] - ZERO)
                else -> randomNumberGenerator.getNextInt()
            }
        }

    private fun preparePart2(number: String): IntArray =
        IntArray(16) { n ->
            val rightMost = number.substring(number.length-12)
            when {
                n < 4 -> 0
                else -> (rightMost[n - 4]- ZERO).toInt()
            }
        }

    private fun convertToPackedByteArray(result: IntArray): ByteArray =
        ByteArray(8) { n ->
            setHiNibbleValue(result[n*2].toByte()).or(setLowNibbleValue(result[(n*2)+1].toByte()))
        }

    fun generatePinBlock(pin: String, pan: String): Result {
        validateParams(pin, pan)?.let { return it }

        val partOne = preparePart1(pin)
        val partTwo = preparePart2(pan)
        val result = IntArray(16 ) { n ->
            partOne[n].xor(partTwo[n])
        }
        return Result.Success(convertToPackedByteArray(result))
    }

    companion object {
        val ZERO: Char = '0'
    }

}
