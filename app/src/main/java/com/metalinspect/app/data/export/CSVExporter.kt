package com.metalinspect.app.data.export

import com.metalinspect.app.data.database.entities.*
import com.metalinspect.app.domain.models.InspectionWithDetails
import com.metalinspect.app.utils.DateUtils
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CSVExporter @Inject constructor() {
    
    fun exportInspections(
        inspections: List<InspectionWithDetails>,
        outputFile: File
    ): Boolean {
        return try {
            FileWriter(outputFile).use { fileWriter ->
                CSVWriter(fileWriter).use { csvWriter ->
                    // Write headers
                    val headers = arrayOf(
                        "Inspection ID",
                        "Lot Number",
                        "Container Number",
                        "Product Type",
                        "Quantity",
                        "Weight (kg)",
                        "Unit",
                        "Port Location",
                        "Weather Conditions",
                        "Inspector Name",
                        "Inspector Company",
                        "Status",
                        "Created Date",
                        "Updated Date",
                        "Completed Date",
                        "Total Defects",
                        "Critical Defects",
                        "Major Defects",
                        "Minor Defects",
                        "Photo Count",
                        "Notes"
                    )
                    csvWriter.writeNext(headers)
                    
                    // Write data
                    inspections.forEach { inspectionWithDetails ->
                        val inspection = inspectionWithDetails.inspection
                        val inspector = inspectionWithDetails.inspector
                        val productType = inspectionWithDetails.productType
                        
                        val row = arrayOf(
                            inspection.id,
                            inspection.lotNumber,
                            inspection.containerNumber ?: "",
                            productType.name,
                            inspection.quantity.toString(),
                            inspection.weight.toString(),
                            inspection.unit,
                            inspection.portLocation,
                            inspection.weatherConditions,
                            inspector.name,
                            inspector.company,
                            inspection.status.name,
                            DateUtils.formatForExport(inspection.createdAt),
                            DateUtils.formatForExport(inspection.updatedAt),
                            inspection.completedAt?.let { DateUtils.formatForExport(it) } ?: "",
                            inspectionWithDetails.totalDefects.toString(),
                            inspectionWithDetails.criticalDefects.toString(),
                            inspectionWithDetails.majorDefects.toString(),
                            inspectionWithDetails.minorDefects.toString(),
                            inspectionWithDetails.photoCount.toString(),
                            inspection.notes ?: ""
                        )
                        csvWriter.writeNext(row)
                    }
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }
    
    fun exportDefects(
        defects: List<DefectRecord>,
        outputFile: File
    ): Boolean {
        return try {
            FileWriter(outputFile).use { fileWriter ->
                CSVWriter(fileWriter).use { csvWriter ->
                    // Write headers
                    val headers = arrayOf(
                        "Defect ID",
                        "Inspection ID",
                        "Defect Type",
                        "Category",
                        "Severity",
                        "Count",
                        "Description",
                        "Location Notes",
                        "Created Date"
                    )
                    csvWriter.writeNext(headers)
                    
                    // Write data
                    defects.forEach { defect ->
                        val row = arrayOf(
                            defect.id,
                            defect.inspectionId,
                            defect.defectType,
                            defect.defectCategory.name,
                            defect.severity.name,
                            defect.count.toString(),
                            defect.description,
                            defect.locationNotes ?: "",
                            DateUtils.formatForExport(defect.createdAt)
                        )
                        csvWriter.writeNext(row)
                    }
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }
    
    fun exportPhotos(
        photos: List<InspectionPhoto>,
        outputFile: File
    ): Boolean {
        return try {
            FileWriter(outputFile).use { fileWriter ->
                CSVWriter(fileWriter).use { csvWriter ->
                    // Write headers
                    val headers = arrayOf(
                        "Photo ID",
                        "Inspection ID",
                        "Defect Record ID",
                        "File Path",
                        "Caption",
                        "Sequence Index",
                        "File Size (bytes)",
                        "Image Width",
                        "Image Height",
                        "Created Date"
                    )
                    csvWriter.writeNext(headers)
                    
                    // Write data
                    photos.forEach { photo ->
                        val row = arrayOf(
                            photo.id,
                            photo.inspectionId,
                            photo.defectRecordId ?: "",
                            photo.filePath,
                            photo.caption ?: "",
                            photo.sequenceIndex.toString(),
                            photo.fileSize.toString(),
                            photo.imageWidth.toString(),
                            photo.imageHeight.toString(),
                            DateUtils.formatForExport(photo.createdAt)
                        )
                        csvWriter.writeNext(row)
                    }
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}