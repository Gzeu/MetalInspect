package com.metalinspect.app.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.metalinspect.app.data.database.entities.Inspection
import com.metalinspect.app.data.database.entities.InspectionStatus

@Dao
interface InspectionDao {
    
    @Query("SELECT * FROM inspections ORDER BY created_at DESC")
    fun getAllInspections(): Flow<List<Inspection>>
    
    @Query("SELECT * FROM inspections WHERE status = :status ORDER BY created_at DESC")
    fun getInspectionsByStatus(status: InspectionStatus): Flow<List<Inspection>>
    
    @Query("SELECT * FROM inspections WHERE inspector_id = :inspectorId ORDER BY created_at DESC")
    fun getInspectionsByInspector(inspectorId: String): Flow<List<Inspection>>
    
    @Query("SELECT * FROM inspections WHERE id = :id")
    suspend fun getInspectionById(id: String): Inspection?
    
    @Query("""
        SELECT * FROM inspections 
        WHERE lot_number LIKE '%' || :query || '%' 
        OR container_number LIKE '%' || :query || '%'
        OR port_location LIKE '%' || :query || '%'
        OR notes LIKE '%' || :query || '%'
        ORDER BY created_at DESC
    """)
    fun searchInspections(query: String): Flow<List<Inspection>>
    
    @Query("SELECT * FROM inspections WHERE created_at BETWEEN :startDate AND :endDate ORDER BY created_at DESC")
    fun getInspectionsByDateRange(startDate: Long, endDate: Long): Flow<List<Inspection>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspection(inspection: Inspection)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspections(inspections: List<Inspection>)
    
    @Update
    suspend fun updateInspection(inspection: Inspection)
    
    @Delete
    suspend fun deleteInspection(inspection: Inspection)
    
    @Query("UPDATE inspections SET status = :status, updated_at = :updatedAt WHERE id = :id")
    suspend fun updateInspectionStatus(id: String, status: InspectionStatus, updatedAt: Long)
    
    @Query("SELECT COUNT(*) FROM inspections")
    suspend fun getInspectionCount(): Int
    
    @Query("SELECT COUNT(*) FROM inspections WHERE status = :status")
    suspend fun getInspectionCountByStatus(status: InspectionStatus): Int
    
    @Query("SELECT COUNT(*) FROM inspections WHERE created_at >= :startDate")
    suspend fun getInspectionCountSince(startDate: Long): Int
    
    @Query("DELETE FROM inspections WHERE status = :status")
    suspend fun deleteInspectionsByStatus(status: InspectionStatus)
}