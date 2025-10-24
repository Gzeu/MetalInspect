package com.metalinspect.app.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.metalinspect.app.data.database.entities.DefectEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * Data Access Object for defect operations
 * Provides CRUD operations and queries for defects
 */
@Dao
interface DefectDao {
    
    // Basic CRUD Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDefect(defect: DefectEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDefects(defects: List<DefectEntity>): List<Long>
    
    @Update
    suspend fun updateDefect(defect: DefectEntity): Int
    
    @Delete
    suspend fun deleteDefect(defect: DefectEntity): Int
    
    @Query("DELETE FROM defects WHERE id = :defectId")
    suspend fun deleteDefectById(defectId: String): Int
    
    @Query("DELETE FROM defects WHERE inspection_id = :inspectionId")
    suspend fun deleteDefectsByInspectionId(inspectionId: String): Int
    
    // Query Operations
    @Query("SELECT * FROM defects WHERE id = :defectId")
    suspend fun getDefectById(defectId: String): DefectEntity?
    
    @Query("SELECT * FROM defects WHERE inspection_id = :inspectionId ORDER BY created_at DESC")
    suspend fun getDefectsByInspectionId(inspectionId: String): List<DefectEntity>
    
    @Query("SELECT * FROM defects WHERE inspection_id = :inspectionId ORDER BY created_at DESC")
    fun getDefectsByInspectionIdFlow(inspectionId: String): Flow<List<DefectEntity>>
    
    @Query("SELECT * FROM defects ORDER BY created_at DESC")
    fun getAllDefectsFlow(): Flow<List<DefectEntity>>
    
    @Query("SELECT * FROM defects ORDER BY created_at DESC")
    suspend fun getAllDefects(): List<DefectEntity>
    
    // Filtered Queries
    @Query("SELECT * FROM defects WHERE defect_type = :defectType ORDER BY created_at DESC")
    fun getDefectsByType(defectType: String): Flow<List<DefectEntity>>
    
    @Query("SELECT * FROM defects WHERE severity = :severity ORDER BY created_at DESC")
    fun getDefectsBySeverity(severity: String): Flow<List<DefectEntity>>
    
    @Query("SELECT * FROM defects WHERE is_critical = :isCritical ORDER BY created_at DESC")
    fun getCriticalDefects(isCritical: Boolean = true): Flow<List<DefectEntity>>
    
    @Query("""
        SELECT * FROM defects 
        WHERE created_at BETWEEN :startDate AND :endDate 
        ORDER BY created_at DESC
    """)
    fun getDefectsByDateRange(startDate: Date, endDate: Date): Flow<List<DefectEntity>>
    
    // Search Query
    @Query("""
        SELECT * FROM defects 
        WHERE description LIKE '%' || :searchQuery || '%' 
        OR location_description LIKE '%' || :searchQuery || '%'
        OR recommended_action LIKE '%' || :searchQuery || '%'
        ORDER BY created_at DESC
    """)
    fun searchDefects(searchQuery: String): Flow<List<DefectEntity>>
    
    // Statistical Queries
    @Query("SELECT COUNT(*) FROM defects")
    suspend fun getTotalDefectsCount(): Int
    
    @Query("SELECT COUNT(*) FROM defects WHERE inspection_id = :inspectionId")
    suspend fun getDefectCountByInspection(inspectionId: String): Int
    
    @Query("SELECT COUNT(*) FROM defects WHERE defect_type = :defectType")
    suspend fun getDefectCountByType(defectType: String): Int
    
    @Query("SELECT COUNT(*) FROM defects WHERE severity = :severity")
    suspend fun getDefectCountBySeverity(severity: String): Int
    
    @Query("SELECT COUNT(*) FROM defects WHERE is_critical = 1")
    suspend fun getCriticalDefectsCount(): Int
    
    @Query("""
        SELECT AVG(estimated_affected_percentage) FROM defects 
        WHERE estimated_affected_percentage IS NOT NULL
    """)
    suspend fun getAverageAffectedPercentage(): Double?
    
    @Query("""
        SELECT SUM(estimated_affected_quantity) FROM defects 
        WHERE estimated_affected_quantity IS NOT NULL
    """)
    suspend fun getTotalAffectedQuantity(): Double?
    
    // Complex Queries
    @Query("""
        SELECT defect_type, COUNT(*) as count FROM defects 
        GROUP BY defect_type 
        ORDER BY count DESC
    """)
    suspend fun getDefectTypeStatistics(): List<DefectTypeCount>
    
    @Query("""
        SELECT severity, COUNT(*) as count FROM defects 
        GROUP BY severity 
        ORDER BY 
            CASE severity 
                WHEN 'critical' THEN 1 
                WHEN 'high' THEN 2 
                WHEN 'medium' THEN 3 
                WHEN 'low' THEN 4 
                ELSE 5 
            END
    """)
    suspend fun getDefectSeverityStatistics(): List<DefectSeverityCount>
    
    // Batch Operations
    @Query("UPDATE defects SET severity = :newSeverity WHERE id IN (:defectIds)")
    suspend fun updateDefectSeverity(defectIds: List<String>, newSeverity: String): Int
    
    @Query("UPDATE defects SET is_critical = :isCritical WHERE id IN (:defectIds)")
    suspend fun updateCriticalStatus(defectIds: List<String>, isCritical: Boolean): Int
    
    @Query("DELETE FROM defects WHERE id IN (:defectIds)")
    suspend fun deleteDefectsByIds(defectIds: List<String>): Int
}

/**
 * Data class for defect type statistics
 */
data class DefectTypeCount(
    val defect_type: String,
    val count: Int
)

/**
 * Data class for defect severity statistics
 */
data class DefectSeverityCount(
    val severity: String,
    val count: Int
)