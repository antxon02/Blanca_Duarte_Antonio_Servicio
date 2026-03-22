package com.example.blanca_duarte_antonio_servicio.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.blanca_duarte_antonio_servicio.data.local.PreferencesManager
import com.example.blanca_duarte_antonio_servicio.util.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class ExchangeRateWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val client = OkHttpClient()
        val request = Request.Builder().url(Constants.URL_CAMBIO).build()

        return try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) throw IOException("Error server: ${response.code}")

            val bodyString = response.body?.string()?.trim()

            if (!bodyString.isNullOrEmpty()) {
                val rate = bodyString.toFloat()

                val prefsManager = PreferencesManager(applicationContext)
                prefsManager.saveRate(rate)

                showNotification("Tasa actualizada: $rate")
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun showNotification(message: String) {
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "currency_channel_id"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Actualización Moneda",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_popup_sync)
            .setContentTitle("Conversor Moneda")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        try {
            manager.notify(1, notification)
        } catch (e: SecurityException) { }
    }
}