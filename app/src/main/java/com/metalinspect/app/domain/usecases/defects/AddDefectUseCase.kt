package com.metalinspect.app.domain.usecases.defects

import com.metalinspect.app.data.database.entities.DefectRecord
import com.metalinspect.app.data.repository.DefectRepository
import com.metalinspect.app.utils.ValidationUtils
import java.util.UUID
import javax.inject.Inject

interface AddDefectUseCase {
    suspend operator fun invoke(defect: DefectRecord): Result<String>
}

class AddDefectUseCaseImpl @Inject constructor(
    private val defectRepository: DefectRepository
) : AddDefectUseCase {
    
    override suspend fun invoke(defect: DefectRecord): Result<String> {
        return try {
            // Validate defect data
            val descriptionValidation = ValidationUtils.validateDefectDescription(defect.description)
            if (!descriptionValidation.isValid) {
                return Result.failure(Exception(descriptionValidation.errorMessage))
            }
            
            if (defect.count <= 0) {
                return Result.failure(Exception("Defect count must be greater than zero"))
            }
            
            // Create defect with generated ID and timestamp
            val newDefect = defect.copy(
                id = UUID.randomUUID().toString(),
                createdAt = System.currentTimeMillis()
            )
            
            val defectId = defectRepository.createDefect(newDefect)
            Result.success(defectId)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}