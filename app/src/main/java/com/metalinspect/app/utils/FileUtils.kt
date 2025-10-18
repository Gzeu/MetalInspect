package com.metalinspect.app.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileUtils @Inject constructor(
    private val context: Context
) {
    
    fun getInspectionPhotoDirectory(inspectionId: String): File {
        val photoDir = File(context.getExternalFilesDir(Constants.PHOTO_DIRECTORY), inspectionId)
        if (!photoDir.exists()) {
            photoDir.mkdirs()
        }
        return photoDir
    }
    
    fun getReportsDirectory(): File {
        val reportsDir = File(context.getExternalFilesDir(Constants.PDF_DIRECTORY))
        if (!reportsDir.exists()) {
            reportsDir.mkdirs()
        }
        return reportsDir
    }
    
    fun getBackupDirectory(): File {
        val backupDir = File(context.getExternalFilesDir(Constants.BACKUP_DIRECTORY))
        if (!backupDir.exists()) {
            backupDir.mkdirs()
        }
        return backupDir
    }
    
    fun getSignatureDirectory(): File {
        val signatureDir = File(context.getExternalFilesDir(Constants.SIGNATURE_DIRECTORY))
        if (!signatureDir.exists()) {
            signatureDir.mkdirs()
        }
        return signatureDir
    }
    
    fun createPhotoFile(inspectionId: String): File {
        val photoDir = getInspectionPhotoDirectory(inspectionId)
        val timestamp = System.currentTimeMillis()
        val fileName = "IMG_${inspectionId}_$timestamp.jpg"
        return File(photoDir, fileName)
    }
    
    fun getUriForFile(file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            Constants.FILE_PROVIDER_AUTHORITY,
            file
        )
    }
    
    fun deleteFile(filePath: String): Boolean {
        return try {
            val file = File(filePath)
            file.delete()
        } catch (e: Exception) {
            false
        }
    }
    
    fun getFileSize(filePath: String): Long {
        return try {
            File(filePath).length()
        } catch (e: Exception) {
            0L
        }
    }
    
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
    
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
            setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }
    
    fun cleanupOldFiles(directory: File, maxAgeMs: Long) {
        if (!directory.exists()) return
        
        val currentTime = System.currentTimeMillis()
        directory.listFiles()?.forEach { file ->
            if (file.lastModified() + maxAgeMs < currentTime) {
                file.delete()
            }
        }
    }
    
    fun getDirectorySize(directory: File): Long {
        if (!directory.exists()) return 0L
        
        var size = 0L
        directory.walkTopDown().forEach { file ->
            if (file.isFile) {
                size += file.length()
            }
        }
        return size
    }
}
