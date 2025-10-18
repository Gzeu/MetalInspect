package com.metalinspect.app.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CameraUtils @Inject constructor(
    private val context: Context
) {
    
    private var cameraExecutor: ExecutorService? = null
    
    fun getCameraExecutor(): ExecutorService {
        if (cameraExecutor == null) {
            cameraExecutor = Executors.newSingleThreadExecutor()
        }
        return cameraExecutor!!
    }
    
    fun shutdownExecutor() {
        cameraExecutor?.shutdown()
        cameraExecutor = null
    }
    
    fun hasCameraHardware(): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }
    
    fun hasBackCamera(): Boolean {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        return cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)
    }
    
    fun hasFrontCamera(): Boolean {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        return cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)
    }
    
    fun createImageCapture(): ImageCapture {
        return ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetRotation(android.view.Surface.ROTATION_0)
            .build()
    }
    
    fun createPreview(): Preview {
        return Preview.Builder()
            .setTargetRotation(android.view.Surface.ROTATION_0)
            .build()
    }
    
    fun getCameraSelector(useFrontCamera: Boolean = false): CameraSelector {
        return if (useFrontCamera && hasFrontCamera()) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
    }
}
