package com.example.blanca_duarte_antonio_servicio.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.blanca_duarte_antonio_servicio.data.local.PreferencesManager

class ConverterViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsManager = PreferencesManager(application)

    var conversionResult by mutableStateOf("")
        private set

    fun performConversion(amountStr: String, isDollarToEuro: Boolean) {
        if (amountStr.isEmpty()) {
            conversionResult = "Introduce una cantidad"
            return
        }

        val amount = amountStr.toFloatOrNull()
        if (amount == null) {
            conversionResult = "Número inválido"
            return
        }

        val rate = prefsManager.getRate()

        val result = if (isDollarToEuro) {
            amount * rate
        } else {
            amount / rate
        }

        conversionResult = String.format("Resultado: %.2f (Tasa: %.4f)", result, rate)
    }
}