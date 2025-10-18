package com.metalinspect.app.data.repository

import kotlinx.coroutines.flow.Flow
import com.metalinspect.app.data.database.entities.InspectionPhoto
import com.metalinspect.app.domain.models.PhotoWithMetadata
import java.io.File

interface PhotoRepository {
    fun getPhotosByInspection(inspectionId: String): Flow<List<InspectionPhoto>>
    fun getPhotosByDefect(defectId: String): Flow<List<InspectionPhoto>>
    fun getPhotosWithMetadata(inspectionId: String): Flow<List<PhotoWithMetadata>>
    suspend fun getPhotoById(id: String): InspectionPhoto?
    
    suspend fun savePhoto(photo: InspectionPhoto, imageFile: File): String
    suspend fun updatePhoto(photo: InspectionPhoto)
    suspend fun deletePhoto(photo: InspectionPhoto): Boolean
    suspend fun deletePhotosByInspection(inspectionId: String)
    suspend fun deletePhotosByDefect(defectId: String)
    
    suspend fun getPhotoCountByInspection(inspectionId: String): Int
    suspend fun reorderPhotos(inspectionId: String, fromIndex: Int, toIndex: Int)
    suspend fun updatePhotoCaption(photoId: String, caption: String)
    
    suspend fun generateThumbnail(photo: InspectionPhoto): String?
    suspend fun cleanupOrphanedFiles()
}