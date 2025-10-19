package com.metalinspect.app.presentation.camera

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.google.common.truth.Truth.assertThat
import com.metalinspect.app.utils.CameraUtils
import com.metalinspect.app.utils.FileUtils
import com.metalinspect.app.utils.ImageUtils
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class CameraIntegrationTest {
    
    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    
    private lateinit var cameraUtils: CameraUtils
    private lateinit var fileUtils: FileUtils
    private lateinit var imageUtils: ImageUtils
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    
    @Before
    fun setup() {
        cameraUtils = CameraUtils(context)
        fileUtils = FileUtils(context)
        imageUtils = ImageUtils(context)
    }
    
    @Test
    fun cameraUtils_detectsCameraAvailability() {
        // When
        val isCameraAvailable = cameraUtils.isCameraAvailable()
        val hasFrontCamera = cameraUtils.hasFrontCamera()
        
        // Then
        // Most devices should have at least a back camera
        // Note: This might fail on emulators without camera support
        if (context.packageManager.hasSystemFeature("android.hardware.camera")) {
            assertThat(isCameraAvailable).isTrue()
        }
        
        // Front camera is optional
        if (context.packageManager.hasSystemFeature("android.hardware.camera.front")) {
            assertThat(hasFrontCamera).isTrue()
        }
    }
    
    @Test
    fun fileUtils_createsRequiredDirectories() {
        // Given
        val inspectionId = UUID.randomUUID().toString()
        
        // When
        val photoDir = fileUtils.getInspectionPhotoDirectory(inspectionId)
        val reportsDir = fileUtils.getReportsDirectory()
        val backupDir = fileUtils.getBackupDirectory()
        val tempDir = fileUtils.getTempDirectory()
        
        // Then
        assertThat(photoDir.exists()).isTrue()
        assertThat(photoDir.isDirectory).isTrue()
        assertThat(reportsDir.exists()).isTrue()
        assertThat(reportsDir.isDirectory).isTrue()
        assertThat(backupDir.exists()).isTrue()
        assertThat(backupDir.isDirectory).isTrue()
        assertThat(tempDir.exists()).isTrue()
        assertThat(tempDir.isDirectory).isTrue()
    }
    
    @Test
    fun fileUtils_createsTempImageFile() {
        // Given
        val inspectionId = "test-inspection"
        
        // When
        val tempFile = fileUtils.createTempImageFile(inspectionId)
        
        // Then
        assertThat(tempFile.parentFile?.exists()).isTrue()
        assertThat(tempFile.name).contains(inspectionId)
        assertThat(tempFile.name).endsWith(".jpg")
        
        // Clean up
        tempFile.delete()
    }
    
    @Test
    fun fileUtils_providesStorageInfo() {
        // When
        val storageInfo = fileUtils.getStorageInfo()
        
        // Then
        assertThat(storageInfo.totalSpaceBytes).isGreaterThan(0L)
        assertThat(storageInfo.freeSpaceBytes).isAtLeast(0L)
        assertThat(storageInfo.usedSpaceBytes).isAtLeast(0L)
        assertThat(storageInfo.totalSpaceMB).isGreaterThan(0L)
        assertThat(storageInfo.freeSpacePercentage).isBetween(0f, 100f)
    }
    
    @Test
    fun imageUtils_handlesSampleImageOperations() {
        // Given - Create a sample test image
        val testImageFile = createSampleImageFile()
        val outputPath = "${context.cacheDir}/compressed_test_image.jpg"
        
        try {
            // When
            val compressionSuccess = imageUtils.compressAndSaveImage(
                inputPath = testImageFile.absolutePath,
                outputPath = outputPath,
                quality = 85,
                maxWidth = 1920,
                maxHeight = 1080
            )
            
            // Then
            assertThat(compressionSuccess).isTrue()
            
            val outputFile = File(outputPath)
            assertThat(outputFile.exists()).isTrue()
            assertThat(outputFile.length()).isGreaterThan(0L)
            
            // Verify dimensions
            val dimensions = imageUtils.getImageDimensions(outputPath)
            assertThat(dimensions.first).isAtMost(1920)
            assertThat(dimensions.second).isAtMost(1080)
            
            // Test thumbnail creation
            val thumbnail = imageUtils.createThumbnail(outputPath, 200)
            assertThat(thumbnail).isNotNull()
            assertThat(thumbnail!!.width).isAtMost(200)
            assertThat(thumbnail.height).isAtMost(200)
            
            thumbnail.recycle()
            outputFile.delete()
            
        } finally {
            testImageFile.delete()
        }
    }
    
    @Test
    fun fileUtils_cleanupTempFiles() {
        // Given - Create some temp files
        val tempDir = fileUtils.getTempDirectory()
        val oldFile = File(tempDir, "old_temp_file.jpg").apply {
            createNewFile()
            // Set last modified to 25 hours ago
            setLastModified(System.currentTimeMillis() - (25 * 60 * 60 * 1000L))
        }
        val newFile = File(tempDir, "new_temp_file.jpg").apply {
            createNewFile()
        }
        
        // When
        fileUtils.cleanupTempFiles()
        
        // Then
        assertThat(oldFile.exists()).isFalse() // Should be cleaned up
        assertThat(newFile.exists()).isTrue()  // Should remain
        
        // Clean up
        newFile.delete()
    }
    
    private fun createSampleImageFile(): File {
        // Create a minimal valid JPEG file for testing
        val testFile = File(context.cacheDir, "test_image_${System.currentTimeMillis()}.jpg")
        
        // Minimal JPEG header + data (placeholder for testing)
        val minimalJpegData = byteArrayOf(
            0xFF.toByte(), 0xD8.toByte(), // JPEG SOI marker
            0xFF.toByte(), 0xE0.toByte(), // APP0 marker
            0x00.toByte(), 0x10.toByte(), // Length
            0x4A.toByte(), 0x46.toByte(), 0x49.toByte(), 0x46.toByte(), 0x00.toByte(), // "JFIF\0"
            0x01.toByte(), 0x01.toByte(), // Version
            0x01.toByte(), 0x00.toByte(), 0x01.toByte(), 0x00.toByte(), 0x01.toByte(), 0x00.toByte(), 0x00.toByte(),
            0xFF.toByte(), 0xD9.toByte()  // JPEG EOI marker
        )
        
        testFile.writeBytes(minimalJpegData)
        return testFile
    }
}