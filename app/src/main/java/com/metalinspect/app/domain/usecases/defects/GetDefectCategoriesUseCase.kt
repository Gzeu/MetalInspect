package com.metalinspect.app.domain.usecases.defects

import kotlinx.coroutines.flow.Flow
import com.metalinspect.app.data.database.entities.*
import com.metalinspect.app.data.repository.DefectRepository
import javax.inject.Inject

interface GetDefectCategoriesUseCase {
    fun getDefectsByInspection(inspectionId: String): Flow<List<DefectRecord>>
    fun getDefectsByCategory(inspectionId: String, category: DefectCategory): Flow<List<DefectRecord>>
    fun getDefectsBySeverity(inspectionId: String, severity: DefectSeverity): Flow<List<DefectRecord>>
    suspend fun getDefectById(id: String): DefectRecord?
    suspend fun getDefectCountByInspection(inspectionId: String): Int
    suspend fun getDefectTypesByCategory(category: DefectCategory): List<String>
    suspend fun updateDefect(defect: DefectRecord): Result<Unit>
    suspend fun deleteDefect(defectId: String): Result<Unit>
}

class GetDefectCategoriesUseCaseImpl @Inject constructor(
    private val defectRepository: DefectRepository
) : GetDefectCategoriesUseCase {
    
    override fun getDefectsByInspection(inspectionId: String): Flow<List<DefectRecord>> {
        return defectRepository.getDefectsByInspection(inspectionId)
    }
    
    override fun getDefectsByCategory(inspectionId: String, category: DefectCategory): Flow<List<DefectRecord>> {
        return defectRepository.getDefectsByCategory(inspectionId, category)
    }
    
    override fun getDefectsBySeverity(inspectionId: String, severity: DefectSeverity): Flow<List<DefectRecord>> {
        return defectRepository.getDefectsBySeverity(inspectionId, severity)
    }
    
    override suspend fun getDefectById(id: String): DefectRecord? {
        return defectRepository.getDefectById(id)
    }
    
    override suspend fun getDefectCountByInspection(inspectionId: String): Int {
        return defectRepository.getDefectCountByInspection(inspectionId)
    }
    
    override suspend fun getDefectTypesByCategory(category: DefectCategory): List<String> {
        return defectRepository.getDefectTypesByCategory(category)
    }
    
    override suspend fun updateDefect(defect: DefectRecord): Result<Unit> {
        return try {
            defectRepository.updateDefect(defect)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteDefect(defectId: String): Result<Unit> {
        return try {
            val defect = defectRepository.getDefectById(defectId)
                ?: return Result.failure(Exception("Defect not found"))
            
            defectRepository.deleteDefect(defect)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}