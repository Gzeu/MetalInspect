package com.metalinspect.app.domain.usecase

import com.metalinspect.app.domain.model.Inspection
import com.metalinspect.app.domain.repository.InspectionRepository
import javax.inject.Inject

/**
 * Use case for updating an inspection
 */
class UpdateInspectionUseCase @Inject constructor(
    private val repository: InspectionRepository
) {
    suspend operator fun invoke(inspection: Inspection) = repository.updateInspection(inspection)
}