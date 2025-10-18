package com.metalinspect.app.data.database.dao

import androidx.room.*
import androidx.lifecycle.LiveData
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
    
    @Query("SELECT * FROM inspections WHERE lot_number LIKE :lotNumber ORDER BY created_at DESC")
    fun searchInspectionsByLot(lotNumber: String): Flow<List<Inspection>>
    
    @Query("""
        SELECT * FROM inspections 
        WHERE created_at BETWEEN :startDate AND :endDate 
        ORDER BY created_at DESC
    """)
    fun getInspectionsByDateRange(startDate: Long, endDate: Long): Flow<List<Inspection>>
    
    @Query("""
        SELECT * FROM inspections 
        WHERE product_type_id = :productTypeId 
        ORDER BY created_at DESC
    """)
    fun getInspectionsByProductType(productTypeId: String): Flow<List<Inspection>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspection(inspection: Inspection)
    
    @Update
    suspend fun updateInspection(inspection: Inspection)
    
    @Delete
    suspend fun deleteInspection(inspection: Inspection)
    
    @Query("DELETE FROM inspections WHERE id = :id")
    suspend fun deleteInspectionById(id: String)
    
    @Query("SELECT COUNT(*) FROM inspections")
    suspend fun getInspectionCount(): Int
    
    @Query("SELECT COUNT(*) FROM inspections WHERE status = :status")
    suspend fun getInspectionCountByStatus(status: InspectionStatus): Int
    
    @Query("""
        SELECT * FROM inspections 
        WHERE lot_number LIKE '%' || :query || '%' 
        OR container_number LIKE '%' || :query || '%'
        OR port_location LIKE '%' || :query || '%'
        ORDER BY created_at DESC
    """)
    fun searchInspections(query: String): Flow<List<Inspection>>
}
