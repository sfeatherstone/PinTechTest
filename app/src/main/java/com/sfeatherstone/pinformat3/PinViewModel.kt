package com.sfeatherstone.pinformat3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sfeatherstone.pinformat3.pinConversion.Iso3Pin

class PinViewModel(private val iso3Pin: Iso3Pin): ViewModel() {

    private val iso3PinStateMutable = MutableLiveData<Iso3Pin.Result>(Iso3Pin.Result.Fail(Iso3Pin.Result.FailReason.PinTooShort))
    val iso3PinState: LiveData<Iso3Pin.Result> = iso3PinStateMutable

    fun updatePin(pin: String, pan: String) {
        iso3PinStateMutable.value = iso3Pin.generatePinBlock(pin, pan)
    }
}