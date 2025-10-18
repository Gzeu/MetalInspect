package com.metalinspect.app.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.metalinspect.app.data.database.entities.InspectionPhoto

@Dao
interface InspectionPhotoDao {
    
    @Query("SELECT * FROM inspection_photos WHERE inspection_id = :inspectionId ORDER BY sequence_index ASC")
    fun getPhotosByInspection(inspectionId: String): Flow<List<InspectionPhoto>>
    
    @Query("SELECT * FROM inspection_photos WHERE defect_record_id = :defectId ORDER BY sequence_index ASC")
    fun getPhotosByDefect(defectId: String): Flow<List<InspectionPhoto>>
    
    @Query("SELECT * FROM inspection_photos WHERE id = :id")
    suspend fun getPhotoById(id: String): InspectionPhoto?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: InspectionPhoto)
    
    @Update
    suspend fun updatePhoto(photo: InspectionPhoto)
    
    @Delete
    suspend fun deletePhoto(photo: InspectionPhoto)
    
    @Query("DELETE FROM inspection_photos WHERE id = :id")
    suspend fun deletePhotoById(id: String)
    
    @Query("DELETE FROM inspection_photos WHERE inspection_id = :inspectionId")
    suspend fun deletePhotosByInspection(inspectionId: String)
    
    @Query("DELETE FROM inspection_photos WHERE defect_record_id = :defectId")
    suspend fun deletePhotosByDefect(defectId: String)
    
    @Query("SELECT COUNT(*) FROM inspection_photos WHERE inspection_id = :inspectionId")
    suspend fun getPhotoCountByInspection(inspectionId: String): Int
    
    @Query("SELECT MAX(sequence_index) FROM inspection_photos WHERE inspection_id = :inspectionId")
    suspend fun getMaxSequenceIndex(inspectionId: String): Int?
    
    @Query("""
        UPDATE inspection_photos 
        SET sequence_index = sequence_index - 1 
        WHERE inspection_id = :inspectionId AND sequence_index > :deletedIndex
    """)
    suspend fun reorderPhotosAfterDeletion(inspectionId: String, deletedIndex: Int)
}
