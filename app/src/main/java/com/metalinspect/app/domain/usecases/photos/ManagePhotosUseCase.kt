package com.metalinspect.app.domain.usecases.photos

import kotlinx.coroutines.flow.Flow
import com.metalinspect.app.data.database.entities.InspectionPhoto
import com.metalinspect.app.data.repository.PhotoRepository
import com.metalinspect.app.domain.models.PhotoWithMetadata
import com.metalinspect.app.utils.ValidationUtils
import javax.inject.Inject

interface ManagePhotosUseCase {
    fun getPhotosByInspection(inspectionId: String): Flow<List<InspectionPhoto>>
    fun getPhotosByDefect(defectId: String): Flow<List<InspectionPhoto>>
    fun getPhotosWithMetadata(inspectionId: String): Flow<List<PhotoWithMetadata>>
    suspend fun getPhotoById(id: String): InspectionPhoto?
    suspend fun updatePhotoCaption(photoId: String, caption: String): Result<Unit>
    suspend fun deletePhoto(photoId: String): Result<Unit>
    suspend fun reorderPhotos(inspectionId: String, fromIndex: Int, toIndex: Int): Result<Unit>
    suspend fun generateThumbnail(photoId: String): Result<String?>
}

class ManagePhotosUseCaseImpl @Inject constructor(
    private val photoRepository: PhotoRepository
) : ManagePhotosUseCase {
    
    override fun getPhotosByInspection(inspectionId: String): Flow<List<InspectionPhoto>> {
        return photoRepository.getPhotosByInspection(inspectionId)
    }
    
    override fun getPhotosByDefect(defectId: String): Flow<List<InspectionPhoto>> {
        return photoRepository.getPhotosByDefect(defectId)
    }
    
    override fun getPhotosWithMetadata(inspectionId: String): Flow<List<PhotoWithMetadata>> {
        return photoRepository.getPhotosWithMetadata(inspectionId)
    }
    
    override suspend fun getPhotoById(id: String): InspectionPhoto? {
        return photoRepository.getPhotoById(id)
    }
    
    override suspend fun updatePhotoCaption(photoId: String, caption: String): Result<Unit> {
        return try {
            val captionValidation = ValidationUtils.validatePhotoCaption(caption)
            if (!captionValidation.isValid) {
                return Result.failure(Exception(captionValidation.errorMessage))
            }
            
            photoRepository.updatePhotoCaption(photoId, caption)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deletePhoto(photoId: String): Result<Unit> {
        return try {
            val photo = photoRepository.getPhotoById(photoId)
                ?: return Result.failure(Exception("Photo not found"))
            
            val success = photoRepository.deletePhoto(photo)
            if (success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete photo"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun reorderPhotos(inspectionId: String, fromIndex: Int, toIndex: Int): Result<Unit> {
        return try {
            photoRepository.reorderPhotos(inspectionId, fromIndex, toIndex)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun generateThumbnail(photoId: String): Result<String?> {
        return try {
            val photo = photoRepository.getPhotoById(photoId)
                ?: return Result.failure(Exception("Photo not found"))
            
            val thumbnailPath = photoRepository.generateThumbnail(photo)
            Result.success(thumbnailPath)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}