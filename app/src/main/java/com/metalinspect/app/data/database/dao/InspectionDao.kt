package com.metalinspect.app.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.metalinspect.app.data.database.entities.InspectionEntity
import com.metalinspect.app.data.database.relations.InspectionWithDefects
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * Data Access Object for inspection operations
 * Provides CRUD operations and complex queries for inspections
 */
@Dao
interface InspectionDao {
    
    // Basic CRUD Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspection(inspection: InspectionEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspections(inspections: List<InspectionEntity>): List<Long>
    
    @Update
    suspend fun updateInspection(inspection: InspectionEntity): Int
    
    @Delete
    suspend fun deleteInspection(inspection: InspectionEntity): Int
    
    @Query("DELETE FROM inspections WHERE id = :inspectionId")
    suspend fun deleteInspectionById(inspectionId: String): Int
    
    // Query Operations
    @Query("SELECT * FROM inspections WHERE id = :inspectionId")
    suspend fun getInspectionById(inspectionId: String): InspectionEntity?
    
    @Query("SELECT * FROM inspections WHERE id = :inspectionId")
    fun getInspectionByIdFlow(inspectionId: String): Flow<InspectionEntity?>
    
    @Query("SELECT * FROM inspections ORDER BY inspection_date DESC")
    fun getAllInspectionsFlow(): Flow<List<InspectionEntity>>
    
    @Query("SELECT * FROM inspections ORDER BY inspection_date DESC")
    suspend fun getAllInspections(): List<InspectionEntity>
    
    // Filtered Queries
    @Query("SELECT * FROM inspections WHERE is_completed = :isCompleted ORDER BY inspection_date DESC")
    fun getInspectionsByCompletionStatus(isCompleted: Boolean): Flow<List<InspectionEntity>>
    
    @Query("SELECT * FROM inspections WHERE is_synced = :isSynced ORDER BY inspection_date DESC")
    suspend fun getInspectionsBySyncStatus(isSynced: Boolean): List<InspectionEntity>
    
    @Query("SELECT * FROM inspections WHERE inspection_type = :type ORDER BY inspection_date DESC")
    fun getInspectionsByType(type: String): Flow<List<InspectionEntity>>
    
    @Query("SELECT * FROM inspections WHERE vessel_name LIKE :vesselName ORDER BY inspection_date DESC")
    fun getInspectionsByVessel(vesselName: String): Flow<List<InspectionEntity>>
    
    @Query("""
        SELECT * FROM inspections 
        WHERE inspection_date BETWEEN :startDate AND :endDate 
        ORDER BY inspection_date DESC
    """)
    fun getInspectionsByDateRange(startDate: Date, endDate: Date): Flow<List<InspectionEntity>>
    
    @Query("""
        SELECT * FROM inspections 
        WHERE inspector_name = :inspectorName 
        ORDER BY inspection_date DESC
    """)
    fun getInspectionsByInspector(inspectorName: String): Flow<List<InspectionEntity>>
    
    // Search Query
    @Query("""
        SELECT * FROM inspections 
        WHERE vessel_name LIKE '%' || :searchQuery || '%' 
        OR cargo_description LIKE '%' || :searchQuery || '%' 
        OR inspector_name LIKE '%' || :searchQuery || '%'
        OR location LIKE '%' || :searchQuery || '%'
        ORDER BY inspection_date DESC
    """)
    fun searchInspections(searchQuery: String): Flow<List<InspectionEntity>>
    
    // Statistical Queries
    @Query("SELECT COUNT(*) FROM inspections")
    suspend fun getTotalInspectionsCount(): Int
    
    @Query("SELECT COUNT(*) FROM inspections WHERE is_completed = 1")
    suspend fun getCompletedInspectionsCount(): Int
    
    @Query("SELECT COUNT(*) FROM inspections WHERE is_synced = 0")
    suspend fun getPendingSyncCount(): Int
    
    @Query("SELECT SUM(cargo_quantity) FROM inspections WHERE is_completed = 1")
    suspend fun getTotalCargoQuantity(): Double?
    
    @Query("""
        SELECT COUNT(*) FROM inspections 
        WHERE inspection_date >= :startDate AND inspection_date <= :endDate
    """)
    suspend fun getInspectionCountByDateRange(startDate: Date, endDate: Date): Int
    
    // Complex Relations
    @Transaction
    @Query("SELECT * FROM inspections WHERE id = :inspectionId")
    suspend fun getInspectionWithDefects(inspectionId: String): InspectionWithDefects?
    
    @Transaction
    @Query("SELECT * FROM inspections ORDER BY inspection_date DESC")
    fun getAllInspectionsWithDefects(): Flow<List<InspectionWithDefects>>
    
    @Transaction
    @Query("SELECT * FROM inspections WHERE is_completed = :isCompleted ORDER BY inspection_date DESC")
    fun getInspectionsWithDefectsByStatus(isCompleted: Boolean): Flow<List<InspectionWithDefects>>
    
    // Batch Operations
    @Query("UPDATE inspections SET is_completed = :isCompleted WHERE id IN (:inspectionIds)")
    suspend fun updateCompletionStatus(inspectionIds: List<String>, isCompleted: Boolean): Int
    
    @Query("UPDATE inspections SET is_synced = :isSynced WHERE id IN (:inspectionIds)")
    suspend fun updateSyncStatus(inspectionIds: List<String>, isSynced: Boolean): Int
    
    @Query("DELETE FROM inspections WHERE id IN (:inspectionIds)")
    suspend fun deleteInspectionsByIds(inspectionIds: List<String>): Int
    
    // Data Cleanup
    @Query("DELETE FROM inspections WHERE created_at < :cutoffDate AND is_completed = 1")
    suspend fun deleteOldCompletedInspections(cutoffDate: Date): Int
}