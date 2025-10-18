package com.metalinspect.app.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionUtils @Inject constructor(
    private val context: Context
) {
    
    companion object {
        val CAMERA_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
        
        val STORAGE_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
        
        val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        
        const val REQUEST_CODE_CAMERA = 100
        const val REQUEST_CODE_STORAGE = 101
        const val REQUEST_CODE_LOCATION = 102
    }
    
    fun hasCameraPermission(): Boolean {
        return CAMERA_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    fun hasStoragePermission(): Boolean {
        return STORAGE_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    fun hasLocationPermission(): Boolean {
        return LOCATION_PERMISSIONS.any { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    fun getRequiredPermissions(): List<String> {
        val requiredPermissions = mutableListOf<String>()
        
        if (!hasCameraPermission()) {
            requiredPermissions.addAll(CAMERA_PERMISSIONS)
        }
        
        if (!hasStoragePermission()) {
            requiredPermissions.addAll(STORAGE_PERMISSIONS)
        }
        
        return requiredPermissions
    }
    
    fun getCameraPermissionRationale(): String {
        return "Camera access is needed to capture inspection photos"
    }
    
    fun getStoragePermissionRationale(): String {
        return "Storage access is needed to save photos and reports"
    }
    
    fun getLocationPermissionRationale(): String {
        return "Location access is needed to add GPS coordinates to inspections (optional)"
    }
}