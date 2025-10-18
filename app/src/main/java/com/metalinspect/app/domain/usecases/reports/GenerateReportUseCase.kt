package com.metalinspect.app.domain.usecases.reports

import com.metalinspect.app.data.pdf.PDFReportGenerator
import com.metalinspect.app.data.repository.InspectionRepository
import com.metalinspect.app.data.repository.DefectRepository
import com.metalinspect.app.data.repository.PhotoRepository
import com.metalinspect.app.data.repository.InspectorRepository
import com.metalinspect.app.data.repository.ProductRepository
import kotlinx.coroutines.flow.first
import java.io.File
import javax.inject.Inject

interface GenerateReportUseCase {
    suspend operator fun invoke(inspectionId: String): Result<File>
}

class GenerateReportUseCaseImpl @Inject constructor(
    private val inspectionRepository: InspectionRepository,
    private val defectRepository: DefectRepository,
    private val photoRepository: PhotoRepository,
    private val inspectorRepository: InspectorRepository,
    private val productRepository: ProductRepository,
    private val pdfReportGenerator: PDFReportGenerator
) : GenerateReportUseCase {
    
    override suspend fun invoke(inspectionId: String): Result<File> {
        return try {
            // Get inspection data
            val inspection = inspectionRepository.getInspectionById(inspectionId)
                ?: return Result.failure(Exception("Inspection not found"))
            
            val inspector = inspectorRepository.getInspectorById(inspection.inspectorId)
                ?: return Result.failure(Exception("Inspector not found"))
            
            val productType = productRepository.getProductTypeById(inspection.productTypeId)
                ?: return Result.failure(Exception("Product type not found"))
            
            // Get related data
            val defects = defectRepository.getDefectsByInspection(inspectionId).first()
            val photos = photoRepository.getPhotosByInspection(inspectionId).first()
            
            // Generate PDF report
            val reportFile = pdfReportGenerator.generateInspectionReport(
                inspection = inspection,
                inspector = inspector,
                productType = productType,
                defectRecords = defects,
                photos = photos
            )
            
            Result.success(reportFile)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}