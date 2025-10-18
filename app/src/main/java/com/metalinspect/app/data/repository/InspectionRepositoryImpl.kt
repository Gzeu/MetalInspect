package com.metalinspect.app.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import com.metalinspect.app.data.database.dao.*
import com.metalinspect.app.data.database.entities.*
import com.metalinspect.app.domain.models.InspectionWithDetails
import com.metalinspect.app.domain.models.InspectionSummary
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InspectionRepositoryImpl @Inject constructor(
    private val inspectionDao: InspectionDao,
    private val inspectorDao: InspectorDao,
    private val productTypeDao: ProductTypeDao,
    private val defectRecordDao: DefectRecordDao,
    private val inspectionPhotoDao: InspectionPhotoDao,
    private val checklistDao: ChecklistDao
) : InspectionRepository {
    
    override fun getAllInspections(): Flow<List<Inspection>> {
        return inspectionDao.getAllInspections()
    }
    
    override fun getInspectionsByStatus(status: InspectionStatus): Flow<List<Inspection>> {
        return inspectionDao.getInspectionsByStatus(status)
    }
    
    override fun getInspectionsByInspector(inspectorId: String): Flow<List<Inspection>> {
        return inspectionDao.getInspectionsByInspector(inspectorId)
    }
    
    override suspend fun getInspectionById(id: String): Inspection? {
        return inspectionDao.getInspectionById(id)
    }
    
    override suspend fun getInspectionWithDetails(id: String): InspectionWithDetails? {
        val inspection = inspectionDao.getInspectionById(id) ?: return null
        val inspector = inspectorDao.getInspectorById(inspection.inspectorId) ?: return null
        val productType = productTypeDao.getProductTypeById(inspection.productTypeId) ?: return null
        
        val defectsFlow = defectRecordDao.getDefectsByInspection(id)
        val photosFlow = inspectionPhotoDao.getPhotosByInspection(id)
        val responsesFlow = checklistDao.getResponsesByInspection(id)
        
        // For suspend function, we need to collect the flows
        // In a real implementation, you might want to make this a Flow<InspectionWithDetails>
        return InspectionWithDetails(
            inspection = inspection,
            inspector = inspector,
            productType = productType,
            defects = emptyList(), // TODO: Collect from flow
            photos = emptyList(),  // TODO: Collect from flow
            checklistResponses = emptyList() // TODO: Collect from flow
        )
    }
    
    override fun searchInspections(query: String): Flow<List<Inspection>> {
        return inspectionDao.searchInspections(query)
    }
    
    override fun getInspectionSummaries(): Flow<List<InspectionSummary>> {
        return getAllInspections().map { inspections ->
            inspections.map { inspection ->
                InspectionSummary(
                    inspectionId = inspection.id,
                    lotNumber = inspection.lotNumber,
                    productTypeName = "", // TODO: Get product type name
                    inspectorName = "",   // TODO: Get inspector name
                    status = inspection.status,
                    createdAt = inspection.createdAt,
                    updatedAt = inspection.updatedAt,
                    completedAt = inspection.completedAt
                )
            }
        }
    }
    
    override suspend fun createInspection(inspection: Inspection): String {
        inspectionDao.insertInspection(inspection)
        return inspection.id
    }
    
    override suspend fun updateInspection(inspection: Inspection) {
        inspectionDao.updateInspection(inspection.copy(updatedAt = System.currentTimeMillis()))
    }
    
    override suspend fun deleteInspection(inspection: Inspection) {
        // Delete related data first
        defectRecordDao.deleteDefectsByInspection(inspection.id)
        inspectionPhotoDao.deletePhotosByInspection(inspection.id)
        checklistDao.deleteResponsesByInspection(inspection.id)
        
        // Then delete the inspection
        inspectionDao.deleteInspection(inspection)
    }
    
    override suspend fun updateInspectionStatus(id: String, status: InspectionStatus) {
        val inspection = inspectionDao.getInspectionById(id) ?: return
        val updatedInspection = inspection.copy(
            status = status,
            updatedAt = System.currentTimeMillis(),
            completedAt = if (status == InspectionStatus.COMPLETED) System.currentTimeMillis() else null
        )
        inspectionDao.updateInspection(updatedInspection)
    }
    
    override suspend fun getInspectionCount(): Int {
        return inspectionDao.getInspectionCount()
    }
    
    override suspend fun getInspectionCountByStatus(status: InspectionStatus): Int {
        return inspectionDao.getInspectionCountByStatus(status)
    }
}