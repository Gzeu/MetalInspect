package com.metalinspect.app.domain.usecases.inspection

import com.metalinspect.app.data.database.entities.InspectionStatus
import com.metalinspect.app.data.repository.InspectionRepository
import javax.inject.Inject

interface DeleteInspectionUseCase {
    suspend operator fun invoke(inspectionId: String): Result<Unit>
}

class DeleteInspectionUseCaseImpl @Inject constructor(
    private val inspectionRepository: InspectionRepository
) : DeleteInspectionUseCase {
    
    override suspend fun invoke(inspectionId: String): Result<Unit> {
        return try {
            val inspection = inspectionRepository.getInspectionById(inspectionId)
                ?: return Result.failure(Exception("Inspection not found"))
            
            // Business rule: Don't allow deletion of completed inspections
            if (inspection.status == InspectionStatus.COMPLETED) {
                return Result.failure(Exception("Cannot delete completed inspections"))
            }
            
            inspectionRepository.deleteInspection(inspection)
            Result.success(Unit)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}