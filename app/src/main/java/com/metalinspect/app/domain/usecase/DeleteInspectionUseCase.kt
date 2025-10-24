package com.metalinspect.app.domain.usecase

import com.metalinspect.app.domain.repository.InspectionRepository
import javax.inject.Inject

/**
 * Use case for deleting an inspection
 */
class DeleteInspectionUseCase @Inject constructor(
    private val repository: InspectionRepository
) {
    suspend operator fun invoke(inspectionId: String) = repository.deleteInspection(inspectionId)
}