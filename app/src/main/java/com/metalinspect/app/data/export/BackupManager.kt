package com.metalinspect.app.data.export

import android.content.Context
import com.metalinspect.app.data.database.InspectionDatabase
import com.metalinspect.app.utils.FileUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupManager @Inject constructor(
    private val context: Context,
    private val database: InspectionDatabase,
    private val fileUtils: FileUtils
) {
    
    private val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    
    fun createBackup(): Result<File> {
        return try {
            val timestamp = dateFormat.format(Date())
            val backupFileName = "MetalInspect_backup_$timestamp.zip"
            val backupFile = File(fileUtils.getBackupDirectory(), backupFileName)
            
            // Close database connections
            database.close()
            
            ZipOutputStream(FileOutputStream(backupFile)).use { zipOut ->
                // Add database file
                val dbFile = context.getDatabasePath(InspectionDatabase.DATABASE_NAME)
                if (dbFile.exists()) {
                    addFileToZip(zipOut, dbFile, "database.db")
                }
                
                // Add photos directory
                val photosDir = File(context.getExternalFilesDir(null), "inspection_photos")
                if (photosDir.exists()) {
                    addDirectoryToZip(zipOut, photosDir, "photos/")
                }
                
                // Add signatures directory
                val signaturesDir = fileUtils.getSignatureDirectory()
                if (signaturesDir.exists()) {
                    addDirectoryToZip(zipOut, signaturesDir, "signatures/")
                }
                
                // Add backup metadata
                val metadata = createBackupMetadata()
                zipOut.putNextEntry(ZipEntry("backup_info.txt"))
                zipOut.write(metadata.toByteArray())
                zipOut.closeEntry()
            }
            
            Result.success(backupFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun restoreBackup(backupFile: File): Result<Unit> {
        return try {
            if (!backupFile.exists()) {
                return Result.failure(Exception("Backup file does not exist"))
            }
            
            // Create temporary directory for extraction
            val tempDir = File(context.cacheDir, "backup_restore")
            tempDir.mkdirs()
            
            // Extract backup
            ZipInputStream(FileInputStream(backupFile)).use { zipIn ->
                var entry = zipIn.nextEntry
                while (entry != null) {
                    val file = File(tempDir, entry.name)
                    
                    if (entry.isDirectory) {
                        file.mkdirs()
                    } else {
                        file.parentFile?.mkdirs()
                        FileOutputStream(file).use { output ->
                            zipIn.copyTo(output)
                        }
                    }
                    
                    zipIn.closeEntry()
                    entry = zipIn.nextEntry
                }
            }
            
            // Close database connections
            database.close()
            
            // Restore database
            val extractedDb = File(tempDir, "database.db")
            if (extractedDb.exists()) {
                val targetDb = context.getDatabasePath(InspectionDatabase.DATABASE_NAME)
                extractedDb.copyTo(targetDb, overwrite = true)
            }
            
            // Restore photos
            val photosDir = File(tempDir, "photos")
            if (photosDir.exists()) {
                val targetPhotosDir = File(context.getExternalFilesDir(null), "inspection_photos")
                photosDir.copyRecursively(targetPhotosDir, overwrite = true)
            }
            
            // Restore signatures
            val signaturesDir = File(tempDir, "signatures")
            if (signaturesDir.exists()) {
                val targetSignaturesDir = fileUtils.getSignatureDirectory()
                signaturesDir.copyRecursively(targetSignaturesDir, overwrite = true)
            }
            
            // Clean up
            tempDir.deleteRecursively()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun addFileToZip(zipOut: ZipOutputStream, file: File, entryName: String) {
        FileInputStream(file).use { input ->
            zipOut.putNextEntry(ZipEntry(entryName))
            input.copyTo(zipOut)
            zipOut.closeEntry()
        }
    }
    
    private fun addDirectoryToZip(zipOut: ZipOutputStream, dir: File, basePath: String) {
        dir.listFiles()?.forEach { file ->
            val entryName = basePath + file.name
            if (file.isDirectory) {
                zipOut.putNextEntry(ZipEntry("$entryName/"))
                zipOut.closeEntry()
                addDirectoryToZip(zipOut, file, "$entryName/")
            } else {
                addFileToZip(zipOut, file, entryName)
            }
        }
    }
    
    private fun createBackupMetadata(): String {
        return buildString {
            appendLine("MetalInspect Backup")
            appendLine("Created: ${Date()}")
            appendLine("Version: 1.0.0")
            appendLine("Database Version: ${InspectionDatabase.DATABASE_VERSION}")
            appendLine("Device: ${android.os.Build.MODEL}")
            appendLine("Android Version: ${android.os.Build.VERSION.RELEASE}")
        }
    }
    
    fun listBackups(): List<File> {
        val backupDir = fileUtils.getBackupDirectory()
        return backupDir.listFiles { file ->
            file.name.startsWith("MetalInspect_backup_") && file.name.endsWith(".zip")
        }?.sortedByDescending { it.lastModified() } ?: emptyList()
    }
    
    fun deleteOldBackups(maxAge: Long = 30 * 24 * 60 * 60 * 1000L) { // 30 days
        val cutoffTime = System.currentTimeMillis() - maxAge
        listBackups().forEach { backup ->
            if (backup.lastModified() < cutoffTime) {
                backup.delete()
            }
        }
    }
}