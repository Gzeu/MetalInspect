package com.metalinspect.app.domain.usecases.photos

import com.metalinspect.app.data.database.entities.InspectionPhoto
import com.metalinspect.app.data.repository.PhotoRepository
import com.metalinspect.app.utils.ValidationUtils
import java.io.File
import java.util.UUID
import javax.inject.Inject

interface CapturePhotoUseCase {
    suspend operator fun invoke(
        inspectionId: String,
        imageFile: File,
        caption: String? = null,
        defectRecordId: String? = null
    ): Result<String>
}

class CapturePhotoUseCaseImpl @Inject constructor(
    private val photoRepository: PhotoRepository
) : CapturePhotoUseCase {
    
    override suspend fun invoke(
        inspectionId: String,
        imageFile: File,
        caption: String?,
        defectRecordId: String?
    ): Result<String> {
        return try {
            // Validate inputs
            if (inspectionId.isBlank()) {
                return Result.failure(Exception("Inspection ID is required"))
            }
            
            if (!imageFile.exists()) {
                return Result.failure(Exception("Image file does not exist"))
            }
            
            val captionValidation = ValidationUtils.validatePhotoCaption(caption)
            if (!captionValidation.isValid) {
                return Result.failure(Exception(captionValidation.errorMessage))
            }
            
            // Get next sequence index
            val photoCount = photoRepository.getPhotoCountByInspection(inspectionId)
            
            // Create photo record
            val photo = InspectionPhoto(
                id = UUID.randomUUID().toString(),
                inspectionId = inspectionId,
                defectRecordId = defectRecordId,
                filePath = "", // Will be set by repository
                caption = caption,
                sequenceIndex = photoCount,
                fileSize = imageFile.length(),
                imageWidth = 0, // Will be set by repository
                imageHeight = 0, // Will be set by repository
                createdAt = System.currentTimeMillis()
            )
            
            val photoId = photoRepository.savePhoto(photo, imageFile)
            Result.success(photoId)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}