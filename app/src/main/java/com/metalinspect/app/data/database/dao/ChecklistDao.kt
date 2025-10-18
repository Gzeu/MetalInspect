package com.metalinspect.app.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.metalinspect.app.data.database.entities.*

@Dao
interface ChecklistDao {
    
    @Query("SELECT * FROM checklist_items WHERE is_active = 1 ORDER BY sequence_order ASC")
    fun getActiveChecklistItems(): Flow<List<ChecklistItem>>
    
    @Query("SELECT * FROM checklist_items WHERE category = :category AND is_active = 1 ORDER BY sequence_order ASC")
    fun getChecklistItemsByCategory(category: ChecklistCategory): Flow<List<ChecklistItem>>
    
    @Query("SELECT * FROM checklist_items WHERE id = :id")
    suspend fun getChecklistItemById(id: String): ChecklistItem?
    
    @Query("SELECT * FROM checklist_responses WHERE inspection_id = :inspectionId")
    fun getResponsesByInspection(inspectionId: String): Flow<List<ChecklistResponse>>
    
    @Query("SELECT * FROM checklist_responses WHERE inspection_id = :inspectionId AND checklist_item_id = :itemId")
    suspend fun getResponse(inspectionId: String, itemId: String): ChecklistResponse?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklistItem(item: ChecklistItem)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklistItems(items: List<ChecklistItem>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponse(response: ChecklistResponse)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponses(responses: List<ChecklistResponse>)
    
    @Update
    suspend fun updateChecklistItem(item: ChecklistItem)
    
    @Update
    suspend fun updateResponse(response: ChecklistResponse)
    
    @Delete
    suspend fun deleteChecklistItem(item: ChecklistItem)
    
    @Delete
    suspend fun deleteResponse(response: ChecklistResponse)
    
    @Query("DELETE FROM checklist_responses WHERE inspection_id = :inspectionId")
    suspend fun deleteResponsesByInspection(inspectionId: String)
    
    @Query("SELECT COUNT(*) FROM checklist_responses WHERE inspection_id = :inspectionId")
    suspend fun getResponseCountByInspection(inspectionId: String): Int
    
    @Query("SELECT COUNT(*) FROM checklist_items WHERE category = :category AND is_active = 1")
    suspend fun getActiveItemCountByCategory(category: ChecklistCategory): Int
}