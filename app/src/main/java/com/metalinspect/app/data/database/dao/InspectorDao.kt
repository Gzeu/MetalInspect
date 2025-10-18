package com.metalinspect.app.data.database.dao

import androidx.room.*
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import com.metalinspect.app.data.database.entities.Inspector

@Dao
interface InspectorDao {
    
    @Query("SELECT * FROM inspectors ORDER BY name ASC")
    fun getAllInspectors(): Flow<List<Inspector>>
    
    @Query("SELECT * FROM inspectors WHERE is_active = 1 ORDER BY name ASC")
    fun getActiveInspectors(): Flow<List<Inspector>>
    
    @Query("SELECT * FROM inspectors WHERE id = :id")
    suspend fun getInspectorById(id: String): Inspector?
    
    @Query("SELECT * FROM inspectors WHERE is_active = 1 LIMIT 1")
    suspend fun getActiveInspector(): Inspector?
    
    @Query("SELECT * FROM inspectors WHERE company = :company ORDER BY name ASC")
    fun getInspectorsByCompany(company: String): Flow<List<Inspector>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspector(inspector: Inspector)
    
    @Update
    suspend fun updateInspector(inspector: Inspector)
    
    @Delete
    suspend fun deleteInspector(inspector: Inspector)
    
    @Query("UPDATE inspectors SET is_active = 0")
    suspend fun deactivateAllInspectors()
    
    @Query("UPDATE inspectors SET is_active = 1 WHERE id = :id")
    suspend fun setActiveInspector(id: String)
    
    @Query("SELECT COUNT(*) FROM inspectors")
    suspend fun getInspectorCount(): Int
    
    @Query("DELETE FROM inspectors WHERE id = :id")
    suspend fun deleteInspectorById(id: String)
}
