package com.metalinspect.app.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.metalinspect.app.data.database.entities.*

@Dao
interface DefectRecordDao {
    
    @Query("SELECT * FROM defect_records WHERE inspection_id = :inspectionId ORDER BY created_at ASC")
    fun getDefectsByInspection(inspectionId: String): Flow<List<DefectRecord>>
    
    @Query("SELECT * FROM defect_records WHERE inspection_id = :inspectionId AND defect_category = :category ORDER BY created_at ASC")
    fun getDefectsByCategory(inspectionId: String, category: DefectCategory): Flow<List<DefectRecord>>
    
    @Query("SELECT * FROM defect_records WHERE inspection_id = :inspectionId AND severity = :severity ORDER BY created_at ASC")
    fun getDefectsBySeverity(inspectionId: String, severity: DefectSeverity): Flow<List<DefectRecord>>
    
    @Query("SELECT * FROM defect_records WHERE id = :id")
    suspend fun getDefectById(id: String): DefectRecord?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDefect(defect: DefectRecord)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDefects(defects: List<DefectRecord>)
    
    @Update
    suspend fun updateDefect(defect: DefectRecord)
    
    @Delete
    suspend fun deleteDefect(defect: DefectRecord)
    
    @Query("DELETE FROM defect_records WHERE inspection_id = :inspectionId")
    suspend fun deleteDefectsByInspection(inspectionId: String)
    
    @Query("SELECT COUNT(*) FROM defect_records WHERE inspection_id = :inspectionId")
    suspend fun getDefectCountByInspection(inspectionId: String): Int
    
    @Query("SELECT COUNT(*) FROM defect_records WHERE inspection_id = :inspectionId AND severity = :severity")
    suspend fun getDefectCountBySeverity(inspectionId: String, severity: DefectSeverity): Int
    
    @Query("SELECT DISTINCT defect_type FROM defect_records WHERE defect_category = :category ORDER BY defect_type ASC")
    suspend fun getDefectTypesByCategory(category: DefectCategory): List<String>
}