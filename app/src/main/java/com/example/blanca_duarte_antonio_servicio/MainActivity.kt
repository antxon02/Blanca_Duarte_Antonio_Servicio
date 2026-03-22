package com.example.blanca_duarte_antonio_servicio

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.blanca_duarte_antonio_servicio.data.worker.ExchangeRateWorker
import com.example.blanca_duarte_antonio_servicio.ui.screen.ConverterScreen
import com.example.blanca_duarte_antonio_servicio.ui.theme.Blanca_Duarte_Antonio_ServicioTheme
import com.example.blanca_duarte_antonio_servicio.ui.viewmodel.ConverterViewModel
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private val viewModel: ConverterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {

            }.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setupWorkManager()

        setContent {
            Blanca_Duarte_Antonio_ServicioTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ConverterScreen(viewModel)
                }
            }
        }
    }

    private fun setupWorkManager() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<ExchangeRateWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "UpdateRateWork",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}

