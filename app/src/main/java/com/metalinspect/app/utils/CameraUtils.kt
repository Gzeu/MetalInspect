package com.metalinspect.app.utils

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CameraUtils @Inject constructor(
    private val context: Context
) {
    
    private val executor: Executor = ContextCompat.getMainExecutor(context)
    
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
    
    fun createCameraSelector(isBackCamera: Boolean = true): CameraSelector {
        return if (isBackCamera) {
            CameraSelector.DEFAULT_BACK_CAMERA
        } else {
            CameraSelector.DEFAULT_FRONT_CAMERA
        }
    }
    
    fun capturePhoto(
        imageCapture: ImageCapture,
        outputFile: File,
        onSuccess: (File) -> Unit,
        onError: (ImageCaptureException) -> Unit
    ) {
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()
        
        imageCapture.takePicture(
            outputFileOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    onSuccess(outputFile)
                }
                
                override fun onError(exception: ImageCaptureException) {
                    onError(exception)
                }
            }
        )
    }
    
    fun isCameraAvailable(): Boolean {
        return try {
            val cameraProvider = ProcessCameraProvider.getInstance(context).get()
            cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)
        } catch (e: Exception) {
            false
        }
    }
    
    fun hasFrontCamera(): Boolean {
        return try {
            val cameraProvider = ProcessCameraProvider.getInstance(context).get()
            cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)
        } catch (e: Exception) {
            false
        }
    }
}