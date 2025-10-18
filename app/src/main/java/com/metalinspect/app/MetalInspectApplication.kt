package com.metalinspect.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.metalinspect.app.utils.Constants
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MetalInspectApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        
        // Initialize logging
        if (BuildConfig.DEBUG_LOGGING) {
            Timber.plant(Timber.DebugTree())
        }
        
        // Log app startup
        Timber.i("MetalInspect application started - Version: ${BuildConfig.VERSION_NAME}")
        
        // Enable StrictMode in debug builds
        if (BuildConfig.DEBUG) {
            enableStrictMode()
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(if (BuildConfig.DEBUG) android.util.Log.DEBUG else android.util.Log.ERROR)
            .build()
    }

    private fun enableStrictMode() {
        try {
            android.os.StrictMode.setThreadPolicy(
                android.os.StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
            
            android.os.StrictMode.setVmPolicy(
                android.os.StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
            
            Timber.d("StrictMode enabled for debugging")
        } catch (e: Exception) {
            Timber.e(e, "Failed to enable StrictMode")
        }
    }
}
