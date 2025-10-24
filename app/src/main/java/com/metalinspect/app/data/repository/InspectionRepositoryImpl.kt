package com.metalinspect.app.data.repository

import com.metalinspect.app.data.database.dao.DefectDao
import com.metalinspect.app.data.database.dao.InspectionDao
import com.metalinspect.app.data.database.entities.DefectEntity
import com.metalinspect.app.data.database.entities.InspectionEntity
import com.metalinspect.app.data.database.relations.InspectionWithDefects
import com.metalinspect.app.domain.model.Defect
import com.metalinspect.app.domain.model.Inspection
import com.metalinspect.app.domain.repository.InspectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of InspectionRepository
 * Handles data operations and domain model mapping
 */
@Singleton
class InspectionRepositoryImpl @Inject constructor(
    private val inspectionDao: InspectionDao,
    private val defectDao: DefectDao
) : InspectionRepository {
    
    // Create Operations
    override suspend fun createInspection(inspection: Inspection): Result<String> {
        return try {
            val entity = inspection.toEntity()
            inspectionDao.insertInspection(entity)
            Result.success(inspection.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun createDefect(defect: Defect): Result<String> {
        return try {
            val entity = defect.toEntity()
            defectDao.insertDefect(entity)
            Result.success(defect.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun createDefects(defects: List<Defect>): Result<List<String>> {
        return try {
            val entities = defects.map { it.toEntity() }
            defectDao.insertDefects(entities)
            Result.success(defects.map { it.id })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Read Operations
    override suspend fun getInspectionById(id: String): Result<Inspection?> {
        return try {
            val entity = inspectionDao.getInspectionById(id)
            Result.success(entity?.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getInspectionByIdFlow(id: String): Flow<Inspection?> {
        return inspectionDao.getInspectionByIdFlow(id)
            .map { it?.toDomainModel() }
    }
    
    override fun getAllInspections(): Flow<List<Inspection>> {
        return inspectionDao.getAllInspectionsFlow()
            .map { entities -> entities.map { it.toDomainModel() } }
    }
    
    override fun getInspectionsByStatus(isCompleted: Boolean): Flow<List<Inspection>> {
        return inspectionDao.getInspectionsByCompletionStatus(isCompleted)
            .map { entities -> entities.map { it.toDomainModel() } }
    }
    
    override fun getInspectionsByType(type: String): Flow<List<Inspection>> {
        return inspectionDao.getInspectionsByType(type)
            .map { entities -> entities.map { it.toDomainModel() } }
    }
    
    override fun getInspectionsByVessel(vesselName: String): Flow<List<Inspection>> {
        return inspectionDao.getInspectionsByVessel("%$vesselName%")
            .map { entities -> entities.map { it.toDomainModel() } }
    }
    
    override fun getInspectionsByDateRange(startDate: Date, endDate: Date): Flow<List<Inspection>> {
        return inspectionDao.getInspectionsByDateRange(startDate, endDate)
            .map { entities -> entities.map { it.toDomainModel() } }
    }
    
    override fun getInspectionsByInspector(inspectorName: String): Flow<List<Inspection>> {
        return inspectionDao.getInspectionsByInspector(inspectorName)
            .map { entities -> entities.map { it.toDomainModel() } }
    }
    
    override fun searchInspections(query: String): Flow<List<Inspection>> {
        return inspectionDao.searchInspections(query)
            .map { entities -> entities.map { it.toDomainModel() } }
    }
    
    override suspend fun getInspectionWithDefects(id: String): Result<Pair<Inspection, List<Defect>>?> {
        return try {
            val inspectionWithDefects = inspectionDao.getInspectionWithDefects(id)
            val result = inspectionWithDefects?.let { relationData ->
                val inspection = relationData.inspection.toDomainModel()
                val defects = relationData.defects.map { it.toDomainModel() }
                Pair(inspection, defects)
            }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getAllInspectionsWithDefects(): Flow<List<Pair<Inspection, List<Defect>>>> {
        return inspectionDao.getAllInspectionsWithDefects()
            .map { inspectionsWithDefects ->
                inspectionsWithDefects.map { relationData ->
                    val inspection = relationData.inspection.toDomainModel()
                    val defects = relationData.defects.map { it.toDomainModel() }
                    Pair(inspection, defects)
                }
            }
    }
    
    // Defect Operations
    override suspend fun getDefectById(id: String): Result<Defect?> {
        return try {
            val entity = defectDao.getDefectById(id)
            Result.success(entity?.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getDefectsByInspectionId(inspectionId: String): Result<List<Defect>> {
        return try {
            val entities = defectDao.getDefectsByInspectionId(inspectionId)
            Result.success(entities.map { it.toDomainModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getDefectsByInspectionIdFlow(inspectionId: String): Flow<List<Defect>> {
        return defectDao.getDefectsByInspectionIdFlow(inspectionId)
            .map { entities -> entities.map { it.toDomainModel() } }
    }
    
    override fun getAllDefects(): Flow<List<Defect>> {
        return defectDao.getAllDefectsFlow()
            .map { entities -> entities.map { it.toDomainModel() } }
    }
    
    override fun getDefectsByType(type: String): Flow<List<Defect>> {
        return defectDao.getDefectsByType(type)
            .map { entities -> entities.map { it.toDomainModel() } }
    }
    
    override fun getDefectsBySeverity(severity: String): Flow<List<Defect>> {
        return defectDao.getDefectsBySeverity(severity)
            .map { entities -> entities.map { it.toDomainModel() } }
    }
    
    override fun getCriticalDefects(): Flow<List<Defect>> {
        return defectDao.getCriticalDefects()
            .map { entities -> entities.map { it.toDomainModel() } }
    }
    
    // Update Operations
    override suspend fun updateInspection(inspection: Inspection): Result<Unit> {
        return try {
            val entity = inspection.toEntity()
            inspectionDao.updateInspection(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateDefect(defect: Defect): Result<Unit> {
        return try {
            val entity = defect.toEntity()
            defectDao.updateDefect(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateInspectionStatus(id: String, isCompleted: Boolean): Result<Unit> {
        return try {
            inspectionDao.updateCompletionStatus(listOf(id), isCompleted)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateSyncStatus(ids: List<String>, isSynced: Boolean): Result<Unit> {
        return try {
            inspectionDao.updateSyncStatus(ids, isSynced)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Delete Operations
    override suspend fun deleteInspection(id: String): Result<Unit> {
        return try {
            inspectionDao.deleteInspectionById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteDefect(id: String): Result<Unit> {
        return try {
            defectDao.deleteDefectById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteDefectsByInspectionId(inspectionId: String): Result<Unit> {
        return try {
            defectDao.deleteDefectsByInspectionId(inspectionId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Statistics Operations
    override suspend fun getInspectionStatistics(): Result<Map<String, Any>> {
        return try {
            val totalCount = inspectionDao.getTotalInspectionsCount()
            val completedCount = inspectionDao.getCompletedInspectionsCount()
            val pendingSyncCount = inspectionDao.getPendingSyncCount()
            val totalCargoQuantity = inspectionDao.getTotalCargoQuantity() ?: 0.0
            
            val statistics = mapOf(
                "totalInspections" to totalCount,
                "completedInspections" to completedCount,
                "pendingInspections" to (totalCount - completedCount),
                "pendingSyncCount" to pendingSyncCount,
                "totalCargoQuantity" to totalCargoQuantity,
                "completionRate" to if (totalCount > 0) (completedCount.toDouble() / totalCount * 100) else 0.0
            )
            
            Result.success(statistics)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getDefectStatistics(): Result<Map<String, Any>> {
        return try {
            val totalDefects = defectDao.getTotalDefectsCount()
            val criticalDefects = defectDao.getCriticalDefectsCount()
            val typeStats = defectDao.getDefectTypeStatistics()
            val severityStats = defectDao.getDefectSeverityStatistics()
            
            val statistics = mapOf(
                "totalDefects" to totalDefects,
                "criticalDefects" to criticalDefects,
                "defectsByType" to typeStats.associate { it.defect_type to it.count },
                "defectsBySeverity" to severityStats.associate { it.severity to it.count },
                "criticalRate" to if (totalDefects > 0) (criticalDefects.toDouble() / totalDefects * 100) else 0.0
            )
            
            Result.success(statistics)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Sync Operations
    override suspend fun getPendingSyncInspections(): Result<List<Inspection>> {
        return try {
            val entities = inspectionDao.getInspectionsBySyncStatus(false)
            Result.success(entities.map { it.toDomainModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// Extension functions for mapping between domain models and entities
private fun Inspection.toEntity(): InspectionEntity {
    return InspectionEntity(
        id = id,
        vesselName = vesselName,
        cargoDescription = cargoDescription,
        inspectionDate = inspectionDate,
        inspectorName = inspectorName,
        location = location,
        weatherConditions = weatherConditions,
        cargoQuantity = cargoQuantity,
        cargoUnit = cargoUnit,
        inspectionType = inspectionType,
        qualityGrade = qualityGrade,
        moistureContent = moistureContent,
        contaminationLevel = contaminationLevel,
        overallCondition = overallCondition,
        notes = notes,
        photoPaths = photoPaths,
        isCompleted = isCompleted,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

private fun InspectionEntity.toDomainModel(): Inspection {
    return Inspection(
        id = id,
        vesselName = vesselName,
        cargoDescription = cargoDescription,
        inspectionDate = inspectionDate,
        inspectorName = inspectorName,
        location = location,
        weatherConditions = weatherConditions,
        cargoQuantity = cargoQuantity,
        cargoUnit = cargoUnit,
        inspectionType = inspectionType,
        qualityGrade = qualityGrade,
        moistureContent = moistureContent,
        contaminationLevel = contaminationLevel,
        overallCondition = overallCondition,
        notes = notes,
        photoPaths = photoPaths,
        isCompleted = isCompleted,
        isSynced = isSynced,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

private fun Defect.toEntity(): DefectEntity {
    return DefectEntity(
        id = id,
        inspectionId = inspectionId,
        defectType = defectType,
        severity = severity,
        description = description,
        locationDescription = locationDescription,
        estimatedAffectedQuantity = estimatedAffectedQuantity,
        estimatedAffectedPercentage = estimatedAffectedPercentage,
        photoPaths = photoPaths,
        recommendedAction = recommendedAction,
        isCritical = isCritical,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

private fun DefectEntity.toDomainModel(): Defect {
    return Defect(
        id = id,
        inspectionId = inspectionId,
        defectType = defectType,
        severity = severity,
        description = description,
        locationDescription = locationDescription,
        estimatedAffectedQuantity = estimatedAffectedQuantity,
        estimatedAffectedPercentage = estimatedAffectedPercentage,
        photoPaths = photoPaths,
        recommendedAction = recommendedAction,
        isCritical = isCritical,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}