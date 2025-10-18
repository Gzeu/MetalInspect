package com.metalinspect.app.utils

import android.content.Context
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileUtils @Inject constructor(
    private val context: Context
) {
    
    fun getInspectionPhotoDirectory(inspectionId: String): File {
        val photosDir = File(context.getExternalFilesDir(Constants.PHOTO_DIRECTORY), inspectionId)
        if (!photosDir.exists()) {
            photosDir.mkdirs()
        }
        return photosDir
    }
    
    fun getReportsDirectory(): File {
        val reportsDir = File(context.getExternalFilesDir(null), Constants.PDF_DIRECTORY)
        if (!reportsDir.exists()) {
            reportsDir.mkdirs()
        }
        return reportsDir
    }
    
    fun getBackupDirectory(): File {
        val backupDir = File(context.getExternalFilesDir(null), Constants.BACKUP_DIRECTORY)
        if (!backupDir.exists()) {
            backupDir.mkdirs()
        }
        return backupDir
    }
    
    fun getSignatureDirectory(): File {
        val signaturesDir = File(context.getExternalFilesDir(null), Constants.SIGNATURE_DIRECTORY)
        if (!signaturesDir.exists()) {
            signaturesDir.mkdirs()
        }
        return signaturesDir
    }
    
    fun getTempDirectory(): File {
        val tempDir = File(context.cacheDir, Constants.TEMP_DIRECTORY)
        if (!tempDir.exists()) {
            tempDir.mkdirs()
        }
        return tempDir
    }
    
    fun createTempImageFile(inspectionId: String): File {
        val tempDir = getTempDirectory()
        val timestamp = System.currentTimeMillis()
        return File(tempDir, "temp_${inspectionId}_${timestamp}${Constants.JPG_EXTENSION}")
    }
    
    fun getStorageInfo(): StorageInfo {
        val externalFilesDir = context.getExternalFilesDir(null)
        val totalSpace = externalFilesDir?.totalSpace ?: 0L
        val freeSpace = externalFilesDir?.freeSpace ?: 0L
        val usedSpace = totalSpace - freeSpace
        
        return StorageInfo(
            totalSpaceBytes = totalSpace,
            freeSpaceBytes = freeSpace,
            usedSpaceBytes = usedSpace
        )
    }
    
    fun cleanupTempFiles() {
        val tempDir = getTempDirectory()
        val cutoffTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000L) // 24 hours
        
        tempDir.listFiles()?.forEach { file ->
            if (file.lastModified() < cutoffTime) {
                file.delete()
            }
        }
    }
    
    fun getFileSize(filePath: String): Long {
        return try {
            File(filePath).length()
        } catch (e: Exception) {
            0L
        }
    }
    
    fun deleteFile(filePath: String): Boolean {
        return try {
            File(filePath).delete()
        } catch (e: Exception) {
            false
        }
    }
    
    fun fileExists(filePath: String): Boolean {
        return try {
            File(filePath).exists()
        } catch (e: Exception) {
            false
        }
    }
}

data class StorageInfo(
    val totalSpaceBytes: Long,
    val freeSpaceBytes: Long,
    val usedSpaceBytes: Long
) {
    val totalSpaceMB: Long get() = totalSpaceBytes / (1024 * 1024)
    val freeSpaceMB: Long get() = freeSpaceBytes / (1024 * 1024)
    val usedSpaceMB: Long get() = usedSpaceBytes / (1024 * 1024)
    
    val freeSpacePercentage: Float get() = if (totalSpaceBytes > 0) {
        (freeSpaceBytes.toFloat() / totalSpaceBytes.toFloat()) * 100f
    } else 0f
}

class PDFGenerationException(message: String, cause: Throwable? = null) : Exception(message, cause)