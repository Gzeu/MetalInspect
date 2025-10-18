package com.metalinspect.app.domain.usecases.inspection

import com.metalinspect.app.data.database.entities.Inspection
import com.metalinspect.app.data.database.entities.InspectionStatus
import com.metalinspect.app.data.repository.InspectionRepository
import com.metalinspect.app.domain.validators.InspectionValidator
import com.metalinspect.app.utils.ValidationResult
import javax.inject.Inject

interface UpdateInspectionUseCase {
    suspend fun updateInspection(inspection: Inspection): Result<Unit>
    suspend fun updateInspectionStatus(id: String, status: InspectionStatus): Result<Unit>
    suspend fun startInspection(id: String): Result<Unit>
    suspend fun completeInspection(id: String): Result<Unit>
    suspend fun cancelInspection(id: String): Result<Unit>
}

class UpdateInspectionUseCaseImpl @Inject constructor(
    private val inspectionRepository: InspectionRepository,
    private val inspectionValidator: InspectionValidator
) : UpdateInspectionUseCase {
    
    override suspend fun updateInspection(inspection: Inspection): Result<Unit> {
        return try {
            // Validate inspection data
            val validationResults = inspectionValidator.validateInspection(inspection)
            val errors = validationResults.filterIsInstance<ValidationResult.Error>()
            
            if (errors.isNotEmpty()) {
                return Result.failure(Exception(errors.first().message))
            }
            
            inspectionRepository.updateInspection(inspection)
            Result.success(Unit)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateInspectionStatus(id: String, status: InspectionStatus): Result<Unit> {
        return try {
            inspectionRepository.updateInspectionStatus(id, status)
            Result.success(Unit)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun startInspection(id: String): Result<Unit> {
        return try {
            val inspection = inspectionRepository.getInspectionById(id)
                ?: return Result.failure(Exception("Inspection not found"))
            
            // Validate inspection can be started
            if (!inspectionValidator.canStartInspection(inspection)) {
                return Result.failure(Exception("Inspection cannot be started - missing required fields"))
            }
            
            if (inspection.status != InspectionStatus.DRAFT) {
                return Result.failure(Exception("Only draft inspections can be started"))
            }
            
            inspectionRepository.updateInspectionStatus(id, InspectionStatus.IN_PROGRESS)
            Result.success(Unit)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun completeInspection(id: String): Result<Unit> {
        return try {
            val inspection = inspectionRepository.getInspectionById(id)
                ?: return Result.failure(Exception("Inspection not found"))
            
            // Validate inspection can be completed
            val validationResult = inspectionValidator.validateForCompletion(inspection)
            if (!validationResult.isValid) {
                return Result.failure(Exception(validationResult.errorMessage))
            }
            
            if (inspection.status != InspectionStatus.IN_PROGRESS) {
                return Result.failure(Exception("Only in-progress inspections can be completed"))
            }
            
            inspectionRepository.updateInspectionStatus(id, InspectionStatus.COMPLETED)
            Result.success(Unit)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun cancelInspection(id: String): Result<Unit> {
        return try {
            val inspection = inspectionRepository.getInspectionById(id)
                ?: return Result.failure(Exception("Inspection not found"))
            
            if (inspection.status == InspectionStatus.COMPLETED) {
                return Result.failure(Exception("Completed inspections cannot be cancelled"))
            }
            
            inspectionRepository.updateInspectionStatus(id, InspectionStatus.CANCELLED)
            Result.success(Unit)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}