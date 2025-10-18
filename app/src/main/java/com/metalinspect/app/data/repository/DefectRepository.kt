package com.metalinspect.app.data.repository

import kotlinx.coroutines.flow.Flow
import com.metalinspect.app.data.database.entities.*

interface DefectRepository {
    fun getDefectsByInspection(inspectionId: String): Flow<List<DefectRecord>>
    fun getDefectsByCategory(inspectionId: String, category: DefectCategory): Flow<List<DefectRecord>>
    fun getDefectsBySeverity(inspectionId: String, severity: DefectSeverity): Flow<List<DefectRecord>>
    suspend fun getDefectById(id: String): DefectRecord?
    
    suspend fun createDefect(defect: DefectRecord): String
    suspend fun updateDefect(defect: DefectRecord)
    suspend fun deleteDefect(defect: DefectRecord)
    suspend fun deleteDefectsByInspection(inspectionId: String)
    
    suspend fun getDefectCountByInspection(inspectionId: String): Int
    suspend fun getDefectCountBySeverity(inspectionId: String, severity: DefectSeverity): Int
    suspend fun getDefectTypesByCategory(category: DefectCategory): List<String>
}