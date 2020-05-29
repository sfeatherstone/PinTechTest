package com.sfeatherstone.pinformat3.di

import com.sfeatherstone.pinformat3.PinViewModel
import com.sfeatherstone.pinformat3.pinConversion.Iso3Pin
import com.sfeatherstone.pinformat3.pinConversion.RandomNumberGenerator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { PinViewModel(get()) }

    factory { RandomNumberGenerator() }
    factory { Iso3Pin(get()) }
}