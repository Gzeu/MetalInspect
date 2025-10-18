package com.metalinspect.app.data.repository

import kotlinx.coroutines.flow.Flow
import com.metalinspect.app.data.database.dao.DefectRecordDao
import com.metalinspect.app.data.database.entities.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefectRepositoryImpl @Inject constructor(
    private val defectRecordDao: DefectRecordDao
) : DefectRepository {
    
    override fun getDefectsByInspection(inspectionId: String): Flow<List<DefectRecord>> {
        return defectRecordDao.getDefectsByInspection(inspectionId)
    }
    
    override fun getDefectsByCategory(inspectionId: String, category: DefectCategory): Flow<List<DefectRecord>> {
        return defectRecordDao.getDefectsByCategory(inspectionId, category)
    }
    
    override fun getDefectsBySeverity(inspectionId: String, severity: DefectSeverity): Flow<List<DefectRecord>> {
        return defectRecordDao.getDefectsBySeverity(inspectionId, severity)
    }
    
    override suspend fun getDefectById(id: String): DefectRecord? {
        return defectRecordDao.getDefectById(id)
    }
    
    override suspend fun createDefect(defect: DefectRecord): String {
        defectRecordDao.insertDefect(defect)
        return defect.id
    }
    
    override suspend fun updateDefect(defect: DefectRecord) {
        defectRecordDao.updateDefect(defect)
    }
    
    override suspend fun deleteDefect(defect: DefectRecord) {
        defectRecordDao.deleteDefect(defect)
    }
    
    override suspend fun deleteDefectsByInspection(inspectionId: String) {
        defectRecordDao.deleteDefectsByInspection(inspectionId)
    }
    
    override suspend fun getDefectCountByInspection(inspectionId: String): Int {
        return defectRecordDao.getDefectCountByInspection(inspectionId)
    }
    
    override suspend fun getDefectCountBySeverity(inspectionId: String, severity: DefectSeverity): Int {
        return defectRecordDao.getDefectCountBySeverity(inspectionId, severity)
    }
    
    override suspend fun getDefectTypesByCategory(category: DefectCategory): List<String> {
        return defectRecordDao.getDefectTypesByCategory(category)
    }
}