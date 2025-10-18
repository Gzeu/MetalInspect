package com.metalinspect.app.domain.usecases.inspection

import com.metalinspect.app.data.database.entities.Inspection
import com.metalinspect.app.data.database.entities.InspectionStatus
import com.metalinspect.app.data.repository.InspectionRepository
import com.metalinspect.app.domain.validators.InspectionValidator
import com.metalinspect.app.utils.ValidationResult
import java.util.UUID
import javax.inject.Inject

interface CreateInspectionUseCase {
    suspend operator fun invoke(inspection: Inspection): Result<String>
}

class CreateInspectionUseCaseImpl @Inject constructor(
    private val inspectionRepository: InspectionRepository,
    private val inspectionValidator: InspectionValidator
) : CreateInspectionUseCase {
    
    override suspend fun invoke(inspection: Inspection): Result<String> {
        return try {
            // Validate inspection data
            val validationResults = inspectionValidator.validateInspection(inspection)
            val errors = validationResults.filterIsInstance<ValidationResult.Error>()
            
            if (errors.isNotEmpty()) {
                return Result.failure(Exception(errors.first().message))
            }
            
            // Create inspection with generated ID and timestamps
            val newInspection = inspection.copy(
                id = UUID.randomUUID().toString(),
                status = InspectionStatus.DRAFT,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            val inspectionId = inspectionRepository.createInspection(newInspection)
            Result.success(inspectionId)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}