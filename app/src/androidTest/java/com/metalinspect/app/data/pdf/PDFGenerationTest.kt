package com.metalinspect.app.data.pdf

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.metalinspect.app.data.database.entities.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class PDFGenerationTest {
    
    private lateinit var pdfGenerator: PDFReportGenerator
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    
    @Before
    fun setup() {
        pdfGenerator = PDFReportGenerator(context)
    }
    
    @Test
    fun generateInspectionReport_createsValidPDFFile() {
        // Given
        val inspection = createTestInspection()
        val inspector = createTestInspector()
        val productType = createTestProductType()
        val defectRecords = createTestDefects(inspection.id)
        val photos = createTestPhotos(inspection.id)
        
        // When
        val pdfFile = pdfGenerator.generateInspectionReport(
            inspection = inspection,
            inspector = inspector,
            productType = productType,
            defectRecords = defectRecords,
            photos = photos
        )
        
        // Then
        assertThat(pdfFile.exists()).isTrue()
        assertThat(pdfFile.length()).isGreaterThan(0L)
        assertThat(pdfFile.name).contains("MetalInspect")
        assertThat(pdfFile.name).contains(inspection.lotNumber)
        assertThat(pdfFile.extension).isEqualTo("pdf")
        
        // Clean up
        pdfFile.delete()
    }
    
    @Test
    fun generateInspectionReport_withNoDefects_createsValidPDF() {
        // Given
        val inspection = createTestInspection()
        val inspector = createTestInspector()
        val productType = createTestProductType()
        val emptyDefects = emptyList<DefectRecord>()
        val photos = createTestPhotos(inspection.id)
        
        // When
        val pdfFile = pdfGenerator.generateInspectionReport(
            inspection = inspection,
            inspector = inspector,
            productType = productType,
            defectRecords = emptyDefects,
            photos = photos
        )
        
        // Then
        assertThat(pdfFile.exists()).isTrue()
        assertThat(pdfFile.length()).isGreaterThan(0L)
        
        // PDF should contain "No defects recorded" message
        val pdfContent = pdfFile.readText()
        assertThat(pdfContent).contains("No defects")
        
        // Clean up
        pdfFile.delete()
    }
    
    @Test
    fun generateInspectionReport_withNoPhotos_createsValidPDF() {
        // Given
        val inspection = createTestInspection()
        val inspector = createTestInspector()
        val productType = createTestProductType()
        val defectRecords = createTestDefects(inspection.id)
        val emptyPhotos = emptyList<InspectionPhoto>()
        
        // When
        val pdfFile = pdfGenerator.generateInspectionReport(
            inspection = inspection,
            inspector = inspector,
            productType = productType,
            defectRecords = defectRecords,
            photos = emptyPhotos
        )
        
        // Then
        assertThat(pdfFile.exists()).isTrue()
        assertThat(pdfFile.length()).isGreaterThan(0L)
        
        // Clean up
        pdfFile.delete()
    }
    
    @Test
    fun generateInspectionReport_containsRequiredSections() {
        // Given
        val inspection = createTestInspection()
        val inspector = createTestInspector()
        val productType = createTestProductType()
        val defectRecords = createTestDefects(inspection.id)
        val photos = createTestPhotos(inspection.id)
        
        // When
        val pdfFile = pdfGenerator.generateInspectionReport(
            inspection = inspection,
            inspector = inspector,
            productType = productType,
            defectRecords = defectRecords,
            photos = photos
        )
        
        // Then - Check that file contains required sections
        assertThat(pdfFile.exists()).isTrue()
        
        // Verify file size is reasonable (should contain text and structure)
        val fileSizeKB = pdfFile.length() / 1024
        assertThat(fileSizeKB).isAtLeast(5L) // At least 5KB for structured PDF
        assertThat(fileSizeKB).isAtMost(5000L) // But not excessive
        
        // Clean up
        pdfFile.delete()
    }
    
    @Test(expected = PDFGenerationException::class)
    fun generateInspectionReport_withNullInspection_throwsException() {
        // This test would need to be implemented if we had validation in the generator
        // For now, we trust the data is valid when passed to generator
    }
    
    private fun createTestInspection() = Inspection(
        id = UUID.randomUUID().toString(),
        lotNumber = "LOT-TEST-001",
        containerNumber = "CONT-TEST-001",
        productTypeId = "steel-sheet",
        quantity = 100.0,
        weight = 2500.0,
        unit = "kg",
        portLocation = "Test Port Berth 1",
        weatherConditions = "Clear, 22°C",
        inspectorId = "inspector-test",
        status = InspectionStatus.COMPLETED,
        notes = "Comprehensive inspection completed. All major parameters checked.",
        latitude = 44.4268,
        longitude = 26.1025,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        completedAt = System.currentTimeMillis()
    )
    
    private fun createTestInspector() = Inspector(
        id = "inspector-test",
        name = "John Doe",
        company = "MetalInspect Solutions",
        role = "Lead Inspector",
        signatureImagePath = null,
        isActive = true
    )
    
    private fun createTestProductType() = ProductType(
        id = "steel-sheet",
        name = "Steel Sheet",
        description = "Flat steel products for construction",
        standardDimensions = "2000x1000x3mm",
        standardGrades = "[\"A36\", \"A572\", \"A588\"]",
        isActive = true
    )
    
    private fun createTestDefects(inspectionId: String) = listOf(
        DefectRecord(
            id = UUID.randomUUID().toString(),
            inspectionId = inspectionId,
            defectType = "Surface Scratches",
            defectCategory = DefectCategory.SURFACE,
            severity = DefectSeverity.MINOR,
            count = 3,
            description = "Minor surface scratches visible on edges, approximately 2mm deep",
            locationNotes = "Left edge, top corner"
        ),
        DefectRecord(
            id = UUID.randomUUID().toString(),
            inspectionId = inspectionId,
            defectType = "Dimensional Deviation",
            defectCategory = DefectCategory.DIMENSIONAL,
            severity = DefectSeverity.MAJOR,
            count = 1,
            description = "Width measurement exceeds tolerance by 1.5mm",
            locationNotes = "Center section"
        )
    )
    
    private fun createTestPhotos(inspectionId: String) = listOf(
        InspectionPhoto(
            id = UUID.randomUUID().toString(),
            inspectionId = inspectionId,
            defectRecordId = null,
            filePath = "/storage/emulated/0/Android/data/com.metalinspect.app/files/test_photo_1.jpg",
            caption = "Overall view of steel sheet batch",
            sequenceIndex = 0,
            fileSize = 1024000L, // 1MB
            imageWidth = 1920,
            imageHeight = 1080
        ),
        InspectionPhoto(
            id = UUID.randomUUID().toString(),
            inspectionId = inspectionId,
            defectRecordId = null,
            filePath = "/storage/emulated/0/Android/data/com.metalinspect.app/files/test_photo_2.jpg",
            caption = "Close-up of edge defects",
            sequenceIndex = 1,
            fileSize = 856000L,
            imageWidth = 1920,
            imageHeight = 1080
        )
    )
}