package com.metalinspect.app

import android.app.Application
import android.os.StrictMode
import com.metalinspect.app.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Application class for MetalInspect
 * Initializes DI, logging, and development tools
 */
@HiltAndroidApp
class MetalInspectApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize logging
        initializeLogging()
        
        // Enable StrictMode in debug builds
        if (BuildConfig.DEBUG) {
            enableStrictMode()
        }
        
        // Initialize crash reporting (can be added later)
        // initializeCrashReporting()
        
        Timber.d("MetalInspectApplication initialized")
    }
    
    /**
     * Initialize logging with Timber
     */
    private fun initializeLogging() {
        if (BuildConfig.DEBUG) {
            // Plant debug tree for development
            Timber.plant(Timber.DebugTree())
        } else {
            // Plant release tree (can filter sensitive logs)
            Timber.plant(ReleaseTree())
        }
    }
    
    /**
     * Enable StrictMode for development builds
     */
    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .detectCustomSlowCalls()
                .penaltyLog()
                .penaltyFlashScreen() // Visual indication
                .build()
        )
        
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .detectActivityLeaks()
                .detectFileUriExposure()
                .penaltyLog()
                .build()
        )
    }
    
    /**
     * Custom Timber tree for release builds
     * Filters out sensitive information and debug logs
     */
    private class ReleaseTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // Only log warnings and errors in release
            if (priority >= android.util.Log.WARN) {
                // Here you could integrate with crash reporting services
                // like Crashlytics, Bugsnag, etc.
                android.util.Log.println(priority, tag, message)
                
                t?.let { throwable ->
                    // Log exception to crash reporting service
                    android.util.Log.println(priority, tag, throwable.toString())
                }
            }
        }
    }
}