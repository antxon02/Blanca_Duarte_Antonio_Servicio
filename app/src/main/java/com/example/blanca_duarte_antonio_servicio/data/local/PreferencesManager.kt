package com.example.blanca_duarte_antonio_servicio.data.local

import android.content.Context
import com.example.blanca_duarte_antonio_servicio.util.Constants

class PreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    fun saveRate(rate: Float) {
        prefs.edit().putFloat(Constants.KEY_EXCHANGE_RATE, rate).apply()
    }

    fun getRate(): Float {
        return prefs.getFloat(Constants.KEY_EXCHANGE_RATE, Constants.DEFAULT_RATE)
    }
}