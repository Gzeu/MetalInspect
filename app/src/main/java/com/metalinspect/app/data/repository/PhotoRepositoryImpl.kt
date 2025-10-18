package com.metalinspect.app.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.metalinspect.app.data.database.dao.InspectionPhotoDao
import com.metalinspect.app.data.database.dao.DefectRecordDao
import com.metalinspect.app.data.database.entities.InspectionPhoto
import com.metalinspect.app.domain.models.PhotoWithMetadata
import com.metalinspect.app.utils.FileUtils
import com.metalinspect.app.utils.ImageUtils
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepositoryImpl @Inject constructor(
    private val inspectionPhotoDao: InspectionPhotoDao,
    private val defectRecordDao: DefectRecordDao,
    private val fileUtils: FileUtils,
    private val imageUtils: ImageUtils
) : PhotoRepository {
    
    override fun getPhotosByInspection(inspectionId: String): Flow<List<InspectionPhoto>> {
        return inspectionPhotoDao.getPhotosByInspection(inspectionId)
    }
    
    override fun getPhotosByDefect(defectId: String): Flow<List<InspectionPhoto>> {
        return inspectionPhotoDao.getPhotosByDefect(defectId)
    }
    
    override fun getPhotosWithMetadata(inspectionId: String): Flow<List<PhotoWithMetadata>> {
        return getPhotosByInspection(inspectionId).map { photos ->
            photos.map { photo ->
                val relatedDefect = if (photo.defectRecordId != null) {
                    defectRecordDao.getDefectById(photo.defectRecordId)
                } else null
                
                val fileExists = File(photo.filePath).exists()
                
                PhotoWithMetadata(
                    photo = photo,
                    relatedDefect = relatedDefect,
                    fileExists = fileExists
                )
            }
        }
    }
    
    override suspend fun getPhotoById(id: String): InspectionPhoto? {
        return inspectionPhotoDao.getPhotoById(id)
    }
    
    override suspend fun savePhoto(photo: InspectionPhoto, imageFile: File): String {
        // Get the final storage location
        val photoDir = fileUtils.getInspectionPhotoDirectory(photo.inspectionId)
        val finalFile = File(photoDir, "${photo.id}.jpg")
        
        // Compress and save the image
        val success = imageUtils.compressAndSaveImage(
            inputPath = imageFile.absolutePath,
            outputPath = finalFile.absolutePath
        )
        
        if (!success) {
            throw Exception("Failed to save photo")
        }
        
        // Get image dimensions
        val (width, height) = imageUtils.getImageDimensions(finalFile.absolutePath)
        val fileSize = finalFile.length()
        
        // Update photo with final details
        val finalPhoto = photo.copy(
            filePath = finalFile.absolutePath,
            fileSize = fileSize,
            imageWidth = width,
            imageHeight = height
        )
        
        // Save to database
        inspectionPhotoDao.insertPhoto(finalPhoto)
        
        // Delete temporary file if different
        if (imageFile.absolutePath != finalFile.absolutePath) {
            imageFile.delete()
        }
        
        return finalPhoto.id
    }
    
    override suspend fun updatePhoto(photo: InspectionPhoto) {
        inspectionPhotoDao.updatePhoto(photo)
    }
    
    override suspend fun deletePhoto(photo: InspectionPhoto): Boolean {
        try {
            // Delete file first
            val file = File(photo.filePath)
            if (file.exists()) {
                file.delete()
            }
            
            // Delete from database
            inspectionPhotoDao.deletePhoto(photo)
            
            // Reorder remaining photos
            inspectionPhotoDao.reorderPhotosAfterDeletion(photo.inspectionId, photo.sequenceIndex)
            
            return true
        } catch (e: Exception) {
            return false
        }
    }
    
    override suspend fun deletePhotosByInspection(inspectionId: String) {
        val photos = inspectionPhotoDao.getPhotosByInspection(inspectionId)
        // Note: This is a Flow, in real implementation you'd collect it
        // For now, just delete from database
        inspectionPhotoDao.deletePhotosByInspection(inspectionId)
        
        // TODO: Delete physical files
        val photoDir = fileUtils.getInspectionPhotoDirectory(inspectionId)
        photoDir.deleteRecursively()
    }
    
    override suspend fun deletePhotosByDefect(defectId: String) {
        inspectionPhotoDao.deletePhotosByDefect(defectId)
    }
    
    override suspend fun getPhotoCountByInspection(inspectionId: String): Int {
        return inspectionPhotoDao.getPhotoCountByInspection(inspectionId)
    }
    
    override suspend fun reorderPhotos(inspectionId: String, fromIndex: Int, toIndex: Int) {
        // This would require more complex logic to reorder sequence indices
        // For MVP, we'll keep it simple
    }
    
    override suspend fun updatePhotoCaption(photoId: String, caption: String) {
        val photo = inspectionPhotoDao.getPhotoById(photoId) ?: return
        val updatedPhoto = photo.copy(caption = caption)
        inspectionPhotoDao.updatePhoto(updatedPhoto)
    }
    
    override suspend fun generateThumbnail(photo: InspectionPhoto): String? {
        val thumbnailDir = File(File(photo.filePath).parent, "thumbnails")
        if (!thumbnailDir.exists()) {
            thumbnailDir.mkdirs()
        }
        
        val thumbnailFile = File(thumbnailDir, "thumb_${photo.id}.jpg")
        
        return try {
            val thumbnail = imageUtils.createThumbnail(photo.filePath, 200)
            if (thumbnail != null) {
                // Save thumbnail logic here
                thumbnailFile.absolutePath
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun cleanupOrphanedFiles() {
        // Implementation to clean up files not referenced in database
        // This would be a maintenance operation
    }
}